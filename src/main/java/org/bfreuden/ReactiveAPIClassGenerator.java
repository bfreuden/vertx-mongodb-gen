package org.bfreuden;

import com.google.common.collect.Lists;
import com.mongodb.reactivestreams.client.MongoCollection;
import com.mongodb.reactivestreams.client.MongoDatabase;
import com.mongodb.reactivestreams.client.gridfs.GridFSBucket;
import com.squareup.javapoet.*;
import com.sun.javadoc.*;
import io.vertx.core.*;
import io.vertx.core.json.JsonObject;
import io.vertx.core.streams.ReadStream;
import io.vertx.mongo.client.MongoResult;
import org.reactivestreams.Publisher;

import javax.lang.model.element.Modifier;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

public class ReactiveAPIClassGenerator extends APIClassGenerator {


    private Set<String> staticImports = new HashSet<>();
    private String classJavadoc;
    private ArrayList<TypeVariableName> typeVariables;
    private ArrayList<TypeName> superInterfaces;
    private TypeName superClass;
    private List<MongoMethod> methods =  new ArrayList<>();
    private TypeName wrappedType;
    private boolean currentMethodHasPublisherOptions;


    protected class MongoMethod {
        public String resultConversionMethod;
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
                returnType.mongoType = actualReturnType.mongoType;
                returnType.vertxType = actualReturnType.vertxType;
                returnType.noArgPublisher = actualReturnType.noArgPublisher;
                if (actualReturnType.singlePublisher) {
                    returnType.isSinglePublisher = true;
                    if (actualReturnType.mongoType != null) {
                        if (actualReturnType.mongoType.toString().equals("TDocument")) {
                            returnType.vertxResultClassName = ClassName.get(Future.class);
                        } else {
                            TypeName paramTypeName;
                            if (context.reactiveApiClasses.contains(actualReturnType.mongoType.toString())) {
                                paramTypeName = ClassName.bestGuess(mapPackageName(actualReturnType.mongoType.toString()));
                            } else if (Types.isKnown(actualReturnType.mongoType.toString())) {
                                paramTypeName = Types.getMapped(actualReturnType.mongoType.toString());
                            } else
                                paramTypeName = ClassName.bestGuess(actualReturnType.mongoType.toString());
                            returnType.vertxType = paramTypeName;
                            returnType.vertxResultClassName = ClassName.get(Future.class);
                        }
                    } else {
                        returnType.vertxType = ClassName.get(Void.class);
                        returnType.vertxResultClassName = ClassName.get(Future.class);
                    }
                    handlerParam = ParameterizedTypeName.get(ClassName.get(Handler.class), ParameterizedTypeName.get(ClassName.get(AsyncResult.class), returnType.vertxType));
                } else {
                    if (actualReturnType.mongoType != null) {
                        returnType.vertxResultClassName = mongoName.equals("watch") ? ClassName.get(ReadStream.class) : ClassName.get(MongoResult.class);
                        if (actualReturnType.mongoType.toString().equals("TDocument")) {
                            returnType.vertxType = TypeVariableName.get("TDocument");
                        } else if (Types.isKnown(actualReturnType.mongoType.toString())) {
                            returnType.vertxType = Types.getMapped(actualReturnType.mongoType.toString());
                        } else {
                            returnType.vertxType = actualReturnType.vertxType;
                        }

                    } else if (actualReturnType.publisherClassName.toString().equals("com.mongodb.reactivestreams.client.gridfs.GridFSFindPublisher")) {
                        returnType.vertxResultClassName = ClassName.get(MongoResult.class);
                        returnType.vertxType = ClassName.bestGuess("com.mongodb.client.gridfs.model.GridFSFile");
                    } else
                        throw new IllegalStateException("not implemented or not supported");
                }
            } else {
                returnType.mongoType = actualReturnType.mongoType;
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
        boolean reactiveType = false;
        String name;
        ClassName publisherClassName;
        boolean isPublisher;
        boolean isSinglePublisher;
        boolean noArgPublisher;
        TypeName mongoType;
        TypeName vertxType;
        ClassName vertxResultClassName;

        TypeName getFullVertxType() {
            return isPublisher ? ParameterizedTypeName.get(vertxResultClassName, vertxType) : vertxType;
        }

        TypeName getFullMongoType() {
            return isPublisher ? (noArgPublisher ? publisherClassName : ParameterizedTypeName.get(publisherClassName, mongoType)) : mongoType;
        }
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
            this.wrappedType = ParameterizedTypeName.get(ClassName.bestGuess(classDoc.qualifiedTypeName()), TypeVariableName.get("TDocument"));
        } else {
            this.fluentReturnType = ClassName.bestGuess(getTargetQualifiedClassName());
            this.wrappedType = ClassName.bestGuess(classDoc.qualifiedTypeName());
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


    protected List<JavaFile.Builder> getJavaFiles() {
        return Lists.newArrayList(getInterfaceFile(), getImplFile());
    }

    private JavaFile.Builder getInterfaceFile() {
        TypeSpec.Builder typeBuilder = TypeSpec.interfaceBuilder(getTargetClassName());
        typeBuilder.addModifiers(Modifier.PUBLIC);
        boolean isImpl = false;
        staticImports.clear();
        for (TypeName inter : superInterfaces)
            typeBuilder.addSuperinterface(inter);

        inflateType(typeBuilder, isImpl, null, null);

        typeBuilder.addMethod(
                MethodSpec.methodBuilder("toDriverClass")
                        .addJavadoc("@return mongo object\n@hidden")
                        .addModifiers(Modifier.ABSTRACT, Modifier.PUBLIC)
                        .returns(wrappedType)
                        .build()
        );
        JavaFile.Builder builder = JavaFile.builder(getTargetPackage(), typeBuilder.build());
        addStaticImports(builder);
        return builder;
    }

    private void addStaticImports(JavaFile.Builder builder) {
        for (String staticImport : staticImports) {
            int index = staticImport.lastIndexOf('.');
            String className = staticImport.substring(0, index);
            String methodName = staticImport.substring(index + 1);
            builder.addStaticImport(ClassName.bestGuess(className), methodName);
        }
    }

    private JavaFile.Builder getImplFile() {
        TypeSpec.Builder typeBuilder = TypeSpec.classBuilder(getTargetClassName() + "Impl");
        typeBuilder.addModifiers(Modifier.PUBLIC);
        boolean isImpl = true;
        staticImports.clear();
        ClassName superclass = ClassName.bestGuess(getTargetPackage() + ".impl." + getTargetClassName() + "Base");
        TypeName extendsClassType = typeVariables.isEmpty() ? superclass : ParameterizedTypeName.get(superclass, typeVariables.toArray(new TypeVariableName[0]));

        typeBuilder.superclass(extendsClassType);

        for (TypeName inter : superInterfaces)
            typeBuilder.addSuperinterface(inter);

        inflateType(typeBuilder, isImpl, this::implementMethod, this::implementHandlerMethod);

        typeBuilder.addField(FieldSpec.builder(ClassName.bestGuess("io.vertx.mongo.impl.MongoClientContext"), "clientContext").addModifiers(Modifier.PROTECTED, Modifier.FINAL).build());
        typeBuilder.addField(FieldSpec.builder(wrappedType, "wrapped").addModifiers(Modifier.PROTECTED, Modifier.FINAL).build());

        typeBuilder.addMethod(MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(ParameterSpec.builder(ClassName.bestGuess("io.vertx.mongo.impl.MongoClientContext"), "clientContext").build())
                .addParameter(ParameterSpec.builder(wrappedType, "wrapped").build())
                .addStatement("this.clientContext = clientContext")
                .addStatement("this.wrapped = wrapped")
                .build()
        );

        typeBuilder.addMethod(
                MethodSpec.methodBuilder("toDriverClass")
                        .addModifiers(Modifier.PUBLIC)
                        .returns(wrappedType)
                        .addStatement("return wrapped")
                        .build()
        );

        JavaFile.Builder builder = JavaFile.builder(getTargetPackage() + ".impl", typeBuilder.build());
        addStaticImports(builder);
        return builder;
    }

    private void implementMethod(MongoMethod method, MethodSpec.Builder methodBuilder) {
        for (MongoMethodParameter param : method.params) {
            if (!param.vertxType.isPrimitive()) {
                staticImports.add("java.util.Objects.requireNonNull");
                methodBuilder.addStatement("requireNonNull(" + param.name + ", $S)", param.name + " is null");
            }
        }
        StringJoiner paramNames = new StringJoiner(", ");
        for (MongoMethodParameter param : method.params) {
            if (param.optionType) {
                String paramName = "__" + param.name;
                paramNames.add(paramName);
                methodBuilder.addStatement("$T " + paramName + " = " + param.name + ".toDriverClass()", param.mongoType);
            } else if (param.conversionMethod != null) {
                String paramName = "__" + param.name;
                paramNames.add(paramName);
                methodBuilder.addStatement("$T " + paramName +  " = $T.INSTANCE." + param.conversionMethod + "(" + param.name + ")",
                        param.mongoType,
                        ClassName.bestGuess("io.vertx.mongo.impl.ConversionUtilsImpl"));
            } else if (param.reactiveType) {
                String paramName = "__" + param.name;
                paramNames.add(paramName);
                methodBuilder.addStatement("$T " + paramName +  " = " + param.name + ".toDriverClass()", param.mongoType);
            } else {
                paramNames.add(param.name);
            }
        }
        TypeName fullMongoType = method.returnType.getFullMongoType();
        TypeName returnedVertxReactiveClass = null;
        if (context.reactiveApiClasses.contains(fullMongoType.toString()))
            returnedVertxReactiveClass = ClassName.bestGuess(mapPackageName(fullMongoType.toString(), true));
        if (returnedVertxReactiveClass == null && fullMongoType instanceof ParameterizedTypeName) {
            ParameterizedTypeName paramFullMongoType = (ParameterizedTypeName) fullMongoType;
            if (paramFullMongoType.typeArguments.size() > 1)
                throw new IllegalStateException("not implemented");
            ParameterizedTypeName paramFullVertxType = (ParameterizedTypeName) method.returnType.getFullVertxType();
            if (paramFullVertxType.typeArguments.size() != 1)
                throw new IllegalStateException("unexpected");
            if (context.reactiveApiClasses.contains(paramFullMongoType.rawType.toString()))
                returnedVertxReactiveClass = ParameterizedTypeName.get(
                        ClassName.bestGuess(mapPackageName(paramFullMongoType.rawType.toString(), true)),
                        paramFullVertxType.typeArguments.get(0)
                );
        }

        if (method.returnType.isPublisher) {
            if (method.mongoName.contains("ulk")) { // FIXME
                //methodBuilder.addStatement("wrapped." + method.mongoName +  "(" + paramNames + ")");
                String returnType = method.returnType.vertxType.toString();
                methodBuilder.addComment(" TODO add implementation");
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
            } else if (method.returnType.isSinglePublisher) {
                methodBuilder.addStatement("$T __publisher = wrapped." + method.mongoName +  "(" + paramNames + ")", fullMongoType);
                methodBuilder.addStatement("$T __promise = $T.promise()", ParameterizedTypeName.get(ClassName.get(Promise.class), method.returnType.mongoType), ClassName.get(Promise.class));
                methodBuilder.addStatement("__publisher.subscribe(new $T<>(__promise))", ClassName.bestGuess("io.vertx.mongo.impl.SingleResultSubscriber"));
                if (returnedVertxReactiveClass != null) {
                    methodBuilder.addStatement("return __promise.future().map(__wrapped -> new $T(this.clientContext, __wrapped))",
                            ClassName.bestGuess(getTargetQualifiedClassName() +".Impl")
                    );
                } else if (method.resultConversionMethod == null) {
                    methodBuilder.addStatement("return __promise.future()");
                } else {
                    methodBuilder.addStatement("return __promise.future().map($T.INSTANCE::" + method.resultConversionMethod + ")", ClassName.bestGuess("io.vertx.mongo.impl.ConversionUtilsImpl"));
                }
            } else if (method.returnType.vertxType.equals(method.returnType.mongoType)) {
                methodBuilder.addStatement("$T __publisher = wrapped." + method.mongoName +  "(" + paramNames + ")", fullMongoType);
                if (currentMethodHasPublisherOptions)
                    methodBuilder.addStatement("options.initializePublisher(__publisher)");
                methodBuilder.addStatement("return new $T<>(clientContext, __publisher)", ClassName.bestGuess("io.vertx.mongo.client.impl.MongoResultImpl"));
            } else {
                methodBuilder.addComment(" TODO use mongo mapper result!");
                methodBuilder.addStatement("$T __publisher = wrapped." + method.mongoName +  "(" + paramNames + ")", fullMongoType);
                if (currentMethodHasPublisherOptions)
                    methodBuilder.addStatement("options.initializePublisher(__publisher)");
                String returnType = method.returnType.vertxType.toString();
                methodBuilder.addStatement("return null");
            }
        } else {
            if (returnedVertxReactiveClass != null) {
                if (method.mongoName.equals("getCollection") && method.params.size() == 1) {
                    // special case
                    methodBuilder.addStatement("$T __wrapped = wrapped.getCollection(collectionName, $T.class)",
                            ParameterizedTypeName.get(ClassName.get(MongoCollection.class), ClassName.get(JsonObject.class)),
                            ClassName.get(JsonObject.class)
                    );
                } else {
                    methodBuilder.addStatement("$T __wrapped = wrapped." + method.mongoName + "(" + paramNames + ")", fullMongoType);
                }
                methodBuilder.addStatement("return new $T(this.clientContext, __wrapped)",
                        returnedVertxReactiveClass
                );
            } else {
                String returnType = method.returnType.vertxType.toString();
                String returnKeyword = "return ";
                if (returnType.equals("void") || returnType.equals("java.lang.Void"))
                    returnKeyword = "";
                if (method.resultConversionMethod == null) {
                    if (context.otherApiClasses.contains(method.returnType.mongoType.toString()) ||
                            context.optionsApiClasses.contains(method.returnType.mongoType.toString()))
                        methodBuilder.addStatement(returnKeyword + "$T.fromDriverClass(wrapped." + method.mongoName +  "(" + paramNames + "))", method.returnType.vertxType);
                    else
                        methodBuilder.addStatement(returnKeyword + "wrapped." + method.mongoName +  "(" + paramNames + ")", fullMongoType);
                } else {
                    methodBuilder.addStatement("$T __result = wrapped." + method.mongoName +  "(" + paramNames + ")", fullMongoType);
                    methodBuilder.addStatement(returnKeyword + "T.INSTANCE." + method.resultConversionMethod + "(__result)", ClassName.bestGuess("io.vertx.mongo.impl.ConversionUtilsImpl"));
                }

            }
        }
    }
    private void implementHandlerMethod(MongoMethod method, MethodSpec.Builder methodBuilder) {
        StringJoiner paramNames = new StringJoiner(", ");
        for (MongoMethodParameter param : method.params)
            paramNames.add(param.name);
        staticImports.add("io.vertx.mongo.impl.Utils.setHandler");
        methodBuilder
                .addStatement("$T __future = this." + method.vertxName + "(" + paramNames + ")", method.returnType.getFullVertxType())
                .addStatement("setHandler(__future, resultHandler)")
                .addStatement("return this");

    }

    public void inflateType(
        TypeSpec.Builder type,
        boolean isImpl,
        BiConsumer<MongoMethod, MethodSpec.Builder> methodCustomizer,
        BiConsumer<MongoMethod, MethodSpec.Builder> handlerMethodCustomizer
    ) {
        if (!isImpl)
            type.addJavadoc(classJavadoc);
        for (TypeVariableName typeVariable : typeVariables)
            type.addTypeVariable(typeVariable);

        for (MongoMethod method : methods) {
            this.currentMethodHasPublisherOptions = false;
            {
                MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder(method.vertxName)
                        .addModifiers(Modifier.PUBLIC)
                        .returns(method.returnType.getFullVertxType());
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
            } else if (method.returnType.isPublisher && !method.returnType.publisherClassName.toString().equals(Publisher.class.getName())) {
                this.currentMethodHasPublisherOptions = true;
                MethodSpec.Builder methodWithOptionsBuilder = MethodSpec.methodBuilder(method.vertxName)
                        .addModifiers(Modifier.PUBLIC)
                        .returns(method.returnType.getFullVertxType());
                if (!isImpl)
                    methodWithOptionsBuilder.addModifiers(Modifier.ABSTRACT);
                for (TypeVariableName variable : method.typeVariables)
                    methodWithOptionsBuilder.addTypeVariable(variable);
                for (MongoMethodParameter param : method.params) {
                    methodWithOptionsBuilder.addParameter(ParameterSpec.builder(param.vertxType, param.name).build());
                }
                String optionsClass = context.publisherOptionsClasses.get(method.returnType.publisherClassName.toString());
                methodWithOptionsBuilder.addParameter(ParameterSpec.builder(ClassName.bestGuess(optionsClass), "options").build());
                if (method.vertxWithOptionsJavadoc != null &&!isImpl)
                    methodWithOptionsBuilder.addJavadoc(method.vertxWithOptionsJavadoc);
                if (isImpl)
                    methodWithOptionsBuilder.addAnnotation(Override.class);
                if (methodCustomizer != null)
                    methodCustomizer.accept(method, methodWithOptionsBuilder);
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

        ActualType returnType = null;
//        if (classDoc.qualifiedTypeName().equals(MongoDatabase.class.getName()) &&
//                methodDoc.name().equals("getCollection")
//        ) {
//            TypeName paramTypeName;
//            if (Arrays.stream(methodDoc.typeParameters()).findFirst().isPresent()) {
//                returnType = ActualType.fromMappedTypeName(ClassName.bestGuess("io.vertx.mongo.client.MongoCollection"), TypeVariableName.get("TDocument"));
//            } else {
//                returnType = ActualType.fromMappedTypeName(ClassName.bestGuess("io.vertx.mongo.client.MongoCollection"), ClassName.get(JsonObject.class));
//            }
//        } else if (classDoc.qualifiedTypeName().equals(MongoCollection.class.getName())) {
//            ParameterizedType parameterizedType = methodDoc.returnType().asParameterizedType();
//            if (parameterizedType != null && parameterizedType.toString().equals(MongoCollection.class.getName() + "<TDocument>")) {
//                returnType = ParameterizedTypeName.get(ClassName.bestGuess(mapPackageName(MongoCollection.class.getName())), TypeVariableName.get("TDocument"));
//            }
//        }
//        if (returnType == null) {
            ActualType actualReturnType = getActualType(methodDoc, null, methodDoc.returnType(), TypeLocation.RETURN);
            if (actualReturnType == null) {
                System.out.println("WARNING: return type of " + methodDoc + " is unknown so method has been ignored");
                return null;
            }
            mongoMethod.computeActualReturnTypes(context, actualReturnType);
            if (mongoMethod.returnType.mongoType != null && mongoMethod.returnType.mongoType.toString().equals(classDoc.qualifiedTypeName()))
                mongoMethod.returnType.vertxType = this.fluentReturnType;
//        } else {
//            mongoMethod.returnType = new MongoMethodParameter();
//            mongoMethod.returnType.vertxType = returnType;
//        }

        if (classDoc.qualifiedTypeName().equals(GridFSBucket.class.getName()) &&
                methodDoc.name().equals("downloadToPublisher")
        ) {
            if (mongoMethod.mongoJavadoc.contains(" id"))
                mongoMethod.vertxName = "downloadByObjectId";
            else
                mongoMethod.vertxName = "downloadByFilename";

        }
        if (mongoMethod.returnType.isPublisher &&
                mongoMethod.returnType.mongoType != null &&
                mongoMethod.returnType.mongoType.toString().contains(".") && // not a TDocument
                !mongoMethod.returnType.mongoType.equals(mongoMethod.returnType.vertxType)
        ) {
            mongoMethod.resultConversionMethod = context.conversionUtilsGenerator.addConversion(mongoMethod.returnType.mongoType, mongoMethod.returnType.vertxType);
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
            ClassName mongoRawType = null;
            if (methodParameter.mongoType instanceof ParameterizedTypeName) {
                ParameterizedTypeName pmongoType = (ParameterizedTypeName)methodParameter.mongoType;
                mongoRawType = pmongoType.rawType;
            } else if (methodParameter.mongoType instanceof ClassName) {
                mongoRawType = (ClassName) methodParameter.mongoType;
            }
            if (context.optionsApiClasses.contains(methodParameter.mongoType.toString()) || context.otherApiClasses.contains(methodParameter.mongoType.toString())) {
                methodParameter.optionType = true;
            } else if (mongoRawType != null && context.reactiveApiClasses.contains(mongoRawType.toString())) {
                methodParameter.reactiveType = true;
            }

            if (!(methodParameter.vertxType instanceof TypeVariableName) &&
                    !methodParameter.vertxType.toString().equals(methodParameter.mongoType.toString()) &&
                    !context.otherApiClasses.contains(methodParameter.mongoType.toString()) &&
                    !context.optionsApiClasses.contains(methodParameter.mongoType.toString()) &&
                    !context.reactiveApiClasses.contains(methodParameter.mongoType.toString())
            ) {
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
