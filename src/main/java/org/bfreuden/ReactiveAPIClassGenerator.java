package org.bfreuden;

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

import javax.lang.model.element.Modifier;
import java.util.*;
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
        String vertxFutureJavadoc;
        MongoMethodParameter returnType;
        List<MongoMethodParameter> params = new ArrayList<>();
        ParameterizedTypeName handlerParam;

        void computeActualReturnTypes(InspectionContext context, ActualType actualReturnType) {
            returnType = new MongoMethodParameter();

            if (actualReturnType.isPublisher) {
                returnType.isPublisher = true;
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
                mongoJavadoc = mongoJavadoc.replace("$", "&#x24;");
                this.vertxResultOrFutureJavadoc = this.mongoJavadoc;
                if (returnType.isPublisher) {
                    StringJoiner newRawCommentText = new StringJoiner("\n");
                    StringJoiner asyncNewRawCommentText = new StringJoiner("\n");
                    String replacement = mongoName.equals("watch") ? "read stream" : (returnType.isSinglePublisher ? "future" : "result");
                    for (String docLine : Arrays.stream(mongoJavadoc.split("\\n+")).collect(Collectors.toList())) {
                        if (docLine.contains("@return")) {
                            String newDocLine = docLine
                                    .replace("observable", replacement)
                                    .replace("Observable", replacement)
                                    .replace("publisher", replacement)
                                    .replace("Publisher", replacement);
                            newRawCommentText.add(newDocLine);
                            String newDocLine2 = docLine
                                    .replace("@return", "@param handler")
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
                        }
                    }
                    this.vertxResultOrFutureJavadoc = newRawCommentText.toString();
                    this.vertxAsyncJavadoc = asyncNewRawCommentText.toString();
                }
            }
        }
    }

    protected static class MongoMethodParameter {
        String name;
        String publisherQualifiedName;
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
            this.classJavadoc = classJavadoc.replace("$", "&#x24;");
            this.classJavadoc = Arrays.stream(this.classJavadoc.split("[\\n\\r]+"))
                    .filter(s -> !s.trim().isEmpty() && !s.contains("TDocument"))
                    .collect(Collectors.joining("\n"));
        }

        this.typeVariables = new ArrayList<>();
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
        TypeSpec.Builder type = classDoc.isClass() ? TypeSpec.classBuilder(getTargetClassName()) :  TypeSpec.interfaceBuilder(getTargetClassName());
        type.addModifiers(Modifier.PUBLIC);
        type.addJavadoc(classJavadoc);

        for (TypeVariableName typeVariable : typeVariables)
            type.addTypeVariable(typeVariable);

        for (TypeName inter : superInterfaces)
            type.addSuperinterface(inter);

        if (superClass != null)
            type.superclass(superClass);

        for (MongoMethod method : methods) {
            {
                MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder(method.vertxName)
                        .addModifiers(Modifier.PUBLIC)
                        .addModifiers(Modifier.ABSTRACT)
                        .returns(method.returnType.vertxType);
//                if (method.returnType.vertxType.equals(fluentReturnType)) {
                    for (TypeVariableName variable : method.typeVariables)
                        methodBuilder.addTypeVariable(variable);
//                }
                for (MongoMethodParameter param : method.params) {
                    methodBuilder.addParameter(ParameterSpec.builder(param.vertxType, param.name).build());
                }
                if (method.vertxResultOrFutureJavadoc != null)
                    methodBuilder.addJavadoc(CodeBlock.of(method.vertxResultOrFutureJavadoc));
                type.addMethod(methodBuilder.build());

            }

            if (method.returnType.isSinglePublisher) {
                MethodSpec.Builder handlerMethodBuilder = MethodSpec.methodBuilder(method.vertxName)
                        .addModifiers(Modifier.PUBLIC)
                        .addModifiers(Modifier.ABSTRACT)
                        .returns(fluentReturnType);
//                if (method.returnType.vertxType.equals(fluentReturnType)) {
                    for (TypeVariableName variable : method.typeVariables)
                        handlerMethodBuilder.addTypeVariable(variable);
//                }
                for (MongoMethodParameter param : method.params) {
                    handlerMethodBuilder.addParameter(ParameterSpec.builder(param.vertxType, param.name).build());
                }
                handlerMethodBuilder.addParameter(ParameterSpec.builder(method.handlerParam, "handler").build());
                if (method.vertxAsyncJavadoc != null)
                    handlerMethodBuilder.addJavadoc(CodeBlock.of(method.vertxAsyncJavadoc));
                type.addMethod(handlerMethodBuilder.build());
            }

        }
        return Collections.singletonList(JavaFile.builder(getTargetPackage(), type.build()).build());
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

//        if (classDoc.qualifiedTypeName().equals(MongoDatabase.class.getName()) &&
//            methodDoc.name().equals("getCollection") &&
//                Arrays.stream(methodDoc.typeParameters()).findFirst().isPresent()
//        ) {
//            mongoMethod.typeVariables.add(TypeVariableName.get("TDocument"));
//        } else  if (classDoc.qualifiedTypeName().equals(MongoCollection.class.getName()) &&
//            methodDoc.name().equals("withDocumentClass") &&
//                Arrays.stream(methodDoc.typeParameters()).findFirst().isPresent()
//        ) {
//            mongoMethod.typeVariables.add(TypeVariableName.get("NewTDocument"));
//        }


        for (Parameter param : methodDoc.parameters()) {
            MongoMethodParameter methodParameter = new MongoMethodParameter();
            methodParameter.name = param.name();
            ActualType actualParamType = getActualType(methodDoc, param.name(), param.type(), TypeLocation.PARAMETER);
            if (actualParamType == null) {
                System.out.println("WARNING: param type of " + methodDoc + " is unknown so method has been ignored");
                return null;
            }
//            ParameterizedType parameterizedType = param.type().asParameterizedType();
//            if (param.type().qualifiedTypeName().equals(Class.class.getName()) && parameterizedType != null) {
//                for (Type type : parameterizedType.typeArguments()) {
//                    mongoMethod.typeVariables.add(TypeVariableName.get(type.typeName()));
//                }
//            }
            methodParameter.mongoType = actualParamType.vertxType;
            methodParameter.vertxType = actualParamType.vertxType;
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
