package org.bfreuden;

import com.google.common.collect.Lists;
import com.mongodb.reactivestreams.client.MongoCollection;
import com.mongodb.reactivestreams.client.MongoDatabase;
import com.mongodb.reactivestreams.client.gridfs.GridFSBucket;
import com.squareup.javapoet.*;
import com.sun.javadoc.*;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.core.streams.ReadStream;
import io.vertx.mongo.client.MongoResult;
import org.reactivestreams.Publisher;

import javax.lang.model.element.Modifier;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

public class ReactiveAPIClassGenerator extends APIClassGenerator {


    private String classJavadoc;
    private ArrayList<TypeVariableName> typeVariables;
    private ArrayList<TypeName> superInterfaces;
    private TypeName superClass;
    private List<MongoMethod> methods =  new ArrayList<>();


    protected class MongoMethod {
        ArrayList<TypeVariableName> typeVariables = new ArrayList<>();
        String mongoName;
        String vertxName;
        String mongoJavadoc;
        String vertxResultOrFutureJavadoc;
        String vertxAsyncJavadoc;
        String vertxWithOptionsJavadoc;
        MongoMethodParameter returnType;
        List<MongoMethodParameter> params = new ArrayList<>();
        ParameterizedTypeName handlerParam;

        void computeActualReturnTypes(InspectionContext context, ActualType actualReturnType) {
            returnType = new MongoMethodParameter();

            if (actualReturnType.isPublisher) {
                returnType.isPublisher = true;
                returnType.publisherClassName = actualReturnType.publisherClassName;
                if (actualReturnType.singlePublisher) {
                    returnType.isSinglePublisher = true;
                    if (actualReturnType.publisherParameterClassName != null) {
                        if (actualReturnType.publisherParameterClassName.equals("TDocument")) {
                            returnType.paramType = TypeVariableName.get("TDocument");
                            returnType.vertxType = ParameterizedTypeName.get(ClassName.get(Future.class), returnType.paramType);
                        } else {
                            TypeName paramTypeName;
                            if (context.reactiveApiClasses.contains(actualReturnType.publisherParameterClassName)) {
                                paramTypeName = ClassName.bestGuess(mapPackageName(actualReturnType.publisherParameterClassName));
                            } else if (Types.isKnown(actualReturnType.publisherParameterClassName)) {
                                paramTypeName = Types.getMapped(actualReturnType.publisherParameterClassName);
                            } else
                                paramTypeName = ClassName.bestGuess(actualReturnType.publisherParameterClassName);
                            returnType.paramType = paramTypeName;
                            returnType.vertxType = ParameterizedTypeName.get(ClassName.get(Future.class), paramTypeName);
                        }
                    } else {
                        returnType.paramType = ClassName.get(Void.class);
                        returnType.vertxType = ParameterizedTypeName.get(ClassName.get(Future.class), returnType.paramType);
                    }
                    handlerParam = ParameterizedTypeName.get(ClassName.get(Handler.class), ParameterizedTypeName.get(ClassName.get(AsyncResult.class), returnType.paramType));
                } else {
                    if (actualReturnType.publisherParameterClassName != null) {
                        ClassName mongoResultClass = mongoName.equals("watch") ? ClassName.get(ReadStream.class) : ClassName.get(MongoResult.class);
                        if (actualReturnType.publisherParameterClassName.equals("TDocument")) {
                            returnType.paramType = TypeVariableName.get("TDocument");
                            returnType.vertxType = ParameterizedTypeName.get(mongoResultClass, returnType.paramType);
                        } else if (Types.isKnown(actualReturnType.publisherParameterClassName)) {
                                returnType.paramType = Types.getMapped(actualReturnType.publisherParameterClassName);
                                returnType.vertxType = ParameterizedTypeName.get(mongoResultClass, returnType.paramType);
                        } else {
                            returnType.paramType = actualReturnType.vertxType;
                            returnType.vertxType = ParameterizedTypeName.get(mongoResultClass, returnType.paramType);
                        }

                    } else if (actualReturnType.publisherClassName.equals("com.mongodb.reactivestreams.client.gridfs.GridFSFindPublisher")) {
                        returnType.paramType = ClassName.bestGuess("com.mongodb.client.gridfs.model.GridFSFile");
                        returnType.vertxType = ParameterizedTypeName.get(ClassName.get(MongoResult.class), returnType.paramType);
                    } else
                        throw new IllegalStateException("not implemented or not supported");
                }
            } else {
                returnType.paramType = null;
                returnType.vertxType = actualReturnType.vertxType;
            }

        }
        void computeJavadocs() {
            if (mongoJavadoc != null) {
                mongoJavadoc = mongoJavadoc.replace("$", "$$");
                this.vertxResultOrFutureJavadoc = this.mongoJavadoc;
                if (returnType.isPublisher) {
                    StringJoiner newRawCommentText = new StringJoiner("\n");
                    StringJoiner asyncNewRawCommentText = new StringJoiner("\n");
                    StringJoiner withOptionsNewRawCommentText = new StringJoiner("\n");
                    String replacement = mongoName.equals("watch") ? "read stream" : (returnType.isSinglePublisher ? "future" : "result");
                    for (String docLine : Arrays.stream(mongoJavadoc.split("\\n+")).collect(Collectors.toList())) {
                        if (docLine.contains("@return")) {
                            withOptionsNewRawCommentText.add(docLine.replaceAll("@return.*", "@param options options"));
                            String newDocLine = docLine
                                    .replace("Iterable", replacement)
                                    .replace("iterable", replacement)
                                    .replace("observable", replacement)
                                    .replace("Observable", replacement)
                                    .replace("publisher", replacement)
                                    .replace("Publisher", replacement);
                            newRawCommentText.add(newDocLine);
                            withOptionsNewRawCommentText.add(newDocLine);
                            String newDocLine2 = docLine
                                    .replace("@return", "@param resultHandler")
                                    .replace("Iterable", "async result")
                                    .replace("iterable", "async result")
                                    .replace("observable", "async result")
                                    .replace("Observable", "async result")
                                    .replace("publisher", "async result")
                                    .replace("Publisher", "async result")
                                    .replace(" a async result", " an async result");
                            asyncNewRawCommentText.add(newDocLine2);
                            int index = docLine.indexOf("@return") + "@return".length();
                            String newReturnLine = docLine.substring(0, index) + " <code>this</code>";
                            asyncNewRawCommentText.add(newReturnLine);
                        } else {
                            newRawCommentText.add(docLine);
                            asyncNewRawCommentText.add(docLine);
                            withOptionsNewRawCommentText.add(docLine);
                        }
                    }
                    this.vertxResultOrFutureJavadoc = newRawCommentText.toString();
                    this.vertxAsyncJavadoc = asyncNewRawCommentText.toString();
                    this.vertxWithOptionsJavadoc = withOptionsNewRawCommentText.toString();
                }
            }
        }
    }

    protected static class MongoMethodParameter {
        boolean optionType = false;
        public String conversionMethod;
        String name;
        String publisherClassName;
        boolean isPublisher;
        boolean isSinglePublisher;
        TypeName mongoType;
        TypeName vertxType;
        TypeName paramType;
    }


    public ReactiveAPIClassGenerator(InspectionContext context, ClassDoc classDoc) {
        super(context, classDoc);
    }

    @Override
    protected void analyzeClass() {
        if (!classDoc.isInterface() && !classDoc.isClass())
            throw new IllegalArgumentException("not implemented");
        TypeVariable[] typeVariables = classDoc.typeParameters();
        if (typeVariables != null  && typeVariables.length > 0) {
            // fixme hardcoded TDocument
            this.fluentReturnType = ParameterizedTypeName.get(ClassName.bestGuess(getTargetQualifiedClassName()), TypeVariableName.get("TDocument"));
        } else {
            this.fluentReturnType = ClassName.bestGuess(getTargetQualifiedClassName());
        }

        this.classJavadoc = classDoc.getRawCommentText();
        if (this.classJavadoc != null) {
            this.classJavadoc = classJavadoc.replace("$", "$$");
            this.classJavadoc = Arrays.stream(this.classJavadoc.split("[\\n\\r]+"))
                    .filter(s -> !s.trim().isEmpty() && !s.contains("TDocument"))
                    .collect(Collectors.joining("\n"));
        }

        this.typeVariables = new ArrayList<>();
        if (typeVariables != null)
            for (TypeVariable typeVariable : typeVariables)
                this.typeVariables.add(TypeVariableName.get(typeVariable.toString()));

        this.superInterfaces = new ArrayList<TypeName>();
        for (ClassDoc inter : classDoc.interfaces()) {
            String superClassName = inter.qualifiedTypeName();
            if (isSupportedSuperClass(superClassName)) {
                superInterfaces.add(ClassName.bestGuess(superClassName));
            } else {
                System.out.println("WARNING: interface of " + classDoc.qualifiedTypeName() + " has been ignored: " + superClassName);
            }
        }
        if (classDoc.isClass()) {
            Type superClass = classDoc.superclassType();
            if (superClass != null) {
                String superClassName = superClass.qualifiedTypeName();
                if (isSupportedSuperClass(superClassName)) {
                    try {
                        this.superClass = TypeName.get(Class.forName(superClassName));
                    } catch (ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    System.out.println("WARNING: superclass of " + classDoc.qualifiedTypeName() + " has been ignored: " + superClassName);
                }
            }
        }

        for (MethodDoc methodDoc : classDoc.methods()) {
            if (classDoc.qualifiedTypeName().equals(GridFSBucket.class.getName()) &&
                    methodDoc.name().equals("downloadToPublisher") &&
                    methodDoc.getRawCommentText().contains("custom id")
            ) {
                // ignore for the moment because it conflicts
                System.out.println("WARNING: method ignored for the moment " + methodDoc);
                continue;
            }
            MongoMethod method = analyzeMethod(methodDoc);
            if (method != null)
                methods.add(method);
        }

    }


    protected List<JavaFile> getJavaFiles() {
        return Lists.newArrayList(getInterfaceFile(), getImplFile());
    }

    private JavaFile getInterfaceFile() {
        TypeSpec.Builder type = TypeSpec.interfaceBuilder(getTargetClassName());
        type.addModifiers(Modifier.PUBLIC);
        boolean isImpl = false;

        for (TypeName inter : superInterfaces)
            type.addSuperinterface(inter);

        inflateType(type, isImpl, null, null, null);

        return JavaFile.builder(getTargetPackage(), type.build()).build();
    }
    private JavaFile getImplFile() {
        TypeSpec.Builder type = TypeSpec.classBuilder(getTargetClassName() + "Impl");
        type.addModifiers(Modifier.PUBLIC);
        boolean isImpl = true;

        ClassName superclass = ClassName.bestGuess(getTargetPackage() + ".impl." + getTargetClassName() + "Base");
        TypeName extendsClassType = typeVariables.isEmpty() ? superclass : ParameterizedTypeName.get(superclass, typeVariables.toArray(new TypeVariableName[0]));

        type.superclass(extendsClassType);

        for (TypeName inter : superInterfaces)
            type.addSuperinterface(inter);

        inflateType(type, isImpl, this::implementMethod, this::implementHandlerMethod, this::implementWithOptionsMethod);

        return JavaFile.builder(getTargetPackage()+ ".impl", type.build()).build();
    }

    private void implementMethod(MongoMethod method, MethodSpec.Builder methodBuilder) {
//        StringJoiner paramNames = new StringJoiner(", ");
//        for (MongoMethodParameter param : method.params) {
//            if (param.optionType) {
//                String paramName = "__" + param.name;
//                paramNames.add(paramName);
//                methodBuilder.addStatement("$T " + paramName + " = this." + param.name + ".toDriverClass())", param.mongoType);
//            } else if (param.conversionMethod != null) {
//                String paramName = "__" + param.name;
//                paramNames.add(paramName);
//                methodBuilder.addStatement("$T " + param.name +  " = this.($T.INSTANCE." + param.conversionMethod + "(" + param.name + "))", ClassName.bestGuess("io.vertx.mongo.impl.ConversionUtilsImpl"));
//            } else {
//                paramNames.add(param.name);
//            }
//        }
//        if (method.returnType.isSinglePublisher) {
//            StringJoiner paramNames = new StringJoiner(", ");
//            for (MongoMethodParameter param : method.params)
//                paramNames.add(param.name);
//
//            methodBuilder
//                    .addStatement("$T future = this." + method.mongoName + "(" + paramNames + ")", method.returnType.vertxType)
//                    .addStatement("$T.setHandler(future, resultHandler)", ClassName.bestGuess("io.vertx.mongo.impl.Utils"))
//                    .addStatement("return this");
//
//        } else {
            String returnType = method.returnType.vertxType.toString();
            if (!returnType.equals("void") && !returnType.equals("java.lang.Void")) {
                if (returnType.equals("int"))
                    methodBuilder.addStatement("return 0");
                else if (returnType.equals("boolean"))
                    methodBuilder.addStatement("return false");
                else if (returnType.equals("long"))
                    methodBuilder.addStatement("return 0L");
                else
                    methodBuilder.addStatement("return null");
            }
//        }
    }
    private void implementHandlerMethod(MongoMethod method, MethodSpec.Builder methodBuilder) {
        StringJoiner paramNames = new StringJoiner(", ");
        for (MongoMethodParameter param : method.params)
            paramNames.add(param.name);

        methodBuilder
                .addStatement("$T future = this." + method.vertxName + "(" + paramNames + ")", method.returnType.vertxType)
                .addStatement("$T.setHandler(future, resultHandler)", ClassName.bestGuess("io.vertx.mongo.impl.Utils"))
                .addStatement("return this");

    }

    private void implementWithOptionsMethod(MongoMethod method, MethodSpec.Builder methodBuilder) {
        String returnType = method.returnType.paramType.toString();
        if (!returnType.equals("void") && !returnType.equals("java.lang.Void"))
            methodBuilder.addStatement("return null");
    }

    public void inflateType(
        TypeSpec.Builder type,
        boolean isImpl,
        BiConsumer<MongoMethod, MethodSpec.Builder> methodCustomizer,
        BiConsumer<MongoMethod, MethodSpec.Builder> handlerMethodCustomizer,
        BiConsumer<MongoMethod, MethodSpec.Builder> withOptionsMethodCustomizer
    ) {
        if (!isImpl)
            type.addJavadoc(classJavadoc);
        for (TypeVariableName typeVariable : typeVariables)
            type.addTypeVariable(typeVariable);

        for (MongoMethod method : methods) {
            {
                MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder(method.vertxName)
                        .addModifiers(Modifier.PUBLIC)
                        .returns(method.returnType.vertxType);
                if (!isImpl)
                    methodBuilder.addModifiers(Modifier.ABSTRACT);
                for (TypeVariableName variable : method.typeVariables)
                    methodBuilder.addTypeVariable(variable);
                for (MongoMethodParameter param : method.params) {
                    methodBuilder.addParameter(ParameterSpec.builder(param.vertxType, param.name).build());
                }
                if (method.vertxResultOrFutureJavadoc != null && !isImpl)
                    methodBuilder.addJavadoc(method.vertxResultOrFutureJavadoc);
                if (isImpl)
                    methodBuilder.addAnnotation(Override.class);
                if (methodCustomizer != null)
                    methodCustomizer.accept(method, methodBuilder);
                type.addMethod(methodBuilder.build());
            }

            if (method.returnType.isSinglePublisher) {
                MethodSpec.Builder handlerMethodBuilder = MethodSpec.methodBuilder(method.vertxName)
                        .addModifiers(Modifier.PUBLIC)
                        .returns(fluentReturnType);
                if (!isImpl)
                    handlerMethodBuilder.addModifiers(Modifier.ABSTRACT);
                for (TypeVariableName variable : method.typeVariables)
                    handlerMethodBuilder.addTypeVariable(variable);
                for (MongoMethodParameter param : method.params) {
                    handlerMethodBuilder.addParameter(ParameterSpec.builder(param.vertxType, param.name).build());
                }
                handlerMethodBuilder.addParameter(ParameterSpec.builder(method.handlerParam, "resultHandler").build());
                if (method.vertxAsyncJavadoc != null && !isImpl)
                    handlerMethodBuilder.addJavadoc(method.vertxAsyncJavadoc);
                if (isImpl)
                    handlerMethodBuilder.addAnnotation(Override.class);
                if (handlerMethodCustomizer != null)
                    handlerMethodCustomizer.accept(method, handlerMethodBuilder);
                type.addMethod(handlerMethodBuilder.build());
            } else if (method.returnType.isPublisher && !method.returnType.publisherClassName.equals(Publisher.class.getName())) {
                MethodSpec.Builder methodWithOptionsBuilder = MethodSpec.methodBuilder(method.vertxName)
                        .addModifiers(Modifier.PUBLIC)
                        .returns(method.returnType.vertxType);
                if (!isImpl)
                    methodWithOptionsBuilder.addModifiers(Modifier.ABSTRACT);
                for (TypeVariableName variable : method.typeVariables)
                    methodWithOptionsBuilder.addTypeVariable(variable);
                for (MongoMethodParameter param : method.params) {
                    methodWithOptionsBuilder.addParameter(ParameterSpec.builder(param.vertxType, param.name).build());
                }
                String optionsClass = context.publisherOptionsClasses.get(method.returnType.publisherClassName);
                methodWithOptionsBuilder.addParameter(ParameterSpec.builder(ClassName.bestGuess(optionsClass), "options").build());
                if (method.vertxWithOptionsJavadoc != null &&!isImpl)
                    methodWithOptionsBuilder.addJavadoc(method.vertxWithOptionsJavadoc);
                if (isImpl)
                    methodWithOptionsBuilder.addAnnotation(Override.class);
                if (withOptionsMethodCustomizer != null)
                    withOptionsMethodCustomizer.accept(method, methodWithOptionsBuilder);
                type.addMethod(methodWithOptionsBuilder.build());

            }

        }
    }

    protected MongoMethod analyzeMethod(MethodDoc methodDoc) {
        TypeVariable[] methodTypeVariables = methodDoc.typeParameters();
        Optional<TypeVariable> resultType = Arrays.stream(methodTypeVariables).filter(v -> v.qualifiedTypeName().equals("TResult")).findFirst();
        boolean resultParameter = resultType.isPresent();
        if (resultParameter) {
            System.out.println("WARNING: return type of " + methodDoc + " has been ignored because it has a TResult type parameter");
            return null;
        }


        if (classDoc.qualifiedTypeName().equals(GridFSBucket.class.getName()) &&
                methodDoc.name().equals("downloadToPublisher") &&
                methodDoc.getRawCommentText().contains("custom id")
        ) {
            // ignore for the moment because it conflicts
            System.out.println("WARNING: method " + methodDoc + " is conflicting with others so it has been ignored");
            return null;
        }

        MongoMethod mongoMethod = new MongoMethod();
        mongoMethod.mongoName = methodDoc.name();
        mongoMethod.vertxName = methodDoc.name();
        mongoMethod.mongoJavadoc = methodDoc.getRawCommentText();
        TypeVariable[] typeVariables = methodDoc.typeParameters();
        if (typeVariables != null && typeVariables.length > 0) {
            for (TypeVariable variable : typeVariables) {
                mongoMethod.typeVariables.add(TypeVariableName.get(variable.typeName()));
            }
        }

        ParameterizedTypeName returnType = null;
        if (classDoc.qualifiedTypeName().equals(MongoDatabase.class.getName()) &&
                methodDoc.name().equals("getCollection")
        ) {
            TypeName paramTypeName;
            if (Arrays.stream(methodDoc.typeParameters()).findFirst().isPresent()) {
                paramTypeName = TypeVariableName.get("TDocument");
            } else {
                paramTypeName = ClassName.get(JsonObject.class);
            }
            returnType = ParameterizedTypeName.get(ClassName.bestGuess(mapPackageName(MongoCollection.class.getName())), paramTypeName);
        } else if (classDoc.qualifiedTypeName().equals(MongoCollection.class.getName())) {
            ParameterizedType parameterizedType = methodDoc.returnType().asParameterizedType();
            if (parameterizedType != null && parameterizedType.toString().equals(MongoCollection.class.getName() + "<TDocument>")) {
                returnType = ParameterizedTypeName.get(ClassName.bestGuess(mapPackageName(MongoCollection.class.getName())), TypeVariableName.get("TDocument"));
            }
        }
        if (returnType == null) {
            ActualType actualReturnType = getActualType(methodDoc, null, methodDoc.returnType(), TypeLocation.RETURN);
            if (actualReturnType == null) {
                System.out.println("WARNING: return type of " + methodDoc + " is unknown so method has been ignored");
                return null;
            }
            mongoMethod.computeActualReturnTypes(context, actualReturnType);
        } else {
            mongoMethod.returnType = new MongoMethodParameter();
            mongoMethod.returnType.vertxType = returnType;
        }

        if (classDoc.qualifiedTypeName().equals(GridFSBucket.class.getName()) &&
                methodDoc.name().equals("downloadToPublisher")
        ) {
            if (mongoMethod.mongoJavadoc.contains(" id"))
                mongoMethod.vertxName = "downloadByObjectId";
            else
                mongoMethod.vertxName = "downloadByFilename";

        }

        for (Parameter param : methodDoc.parameters()) {
            MongoMethodParameter methodParameter = new MongoMethodParameter();
            methodParameter.name = param.name();
            ActualType actualParamType = getActualType(methodDoc, param.name(), param.type(), TypeLocation.PARAMETER);
            if (actualParamType == null) {
                System.out.println("WARNING: param type of " + methodDoc + " is unknown so method has been ignored");
                return null;
            }
            if (actualParamType.isPublisher) {
                System.out.println("WARNING: one param of " + methodDoc + " is a publisher so method has been ignored");
                return null;
            }
            methodParameter.mongoType = actualParamType.mongoType;
            methodParameter.vertxType = actualParamType.vertxType;
            if (context.optionsApiClasses.contains(methodParameter.mongoType.toString()) || context.otherApiClasses.contains(methodParameter.mongoType.toString())) {
                methodParameter.optionType = true;
            }

            if (!(methodParameter.vertxType instanceof TypeVariableName) && !methodParameter.vertxType.toString().equals(methodParameter.mongoType.toString())) {
                methodParameter.conversionMethod = context.conversionUtilsGenerator.addConversion(methodParameter.vertxType, methodParameter.mongoType);
            }
            mongoMethod.params.add(methodParameter);
            // FIXME: for the moment publisher parameters are ignored
            if (actualParamType.isPublisher) {
                System.out.println("WARNING: param type of " + methodDoc + " is publisher so method has been ignored");
                return null;
            }
        }
        mongoMethod.computeJavadocs();
        return mongoMethod;
    }


}
