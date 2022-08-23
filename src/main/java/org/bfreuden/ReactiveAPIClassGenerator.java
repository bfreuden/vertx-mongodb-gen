package org.bfreuden;

import com.google.common.collect.Lists;
import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoCollection;
import com.mongodb.reactivestreams.client.gridfs.GridFSBucket;
import com.squareup.javapoet.*;
import com.sun.javadoc.*;
import io.vertx.core.*;
import io.vertx.core.json.JsonObject;
import org.bson.Document;

import javax.lang.model.element.Modifier;
import java.util.*;
import java.util.stream.Collectors;

public class ReactiveAPIClassGenerator extends GenericAPIClassGenerator {

    private ArrayList<TypeName> superInterfaces;
    private TypeName wrappedType;
    private boolean isMongoClient;

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

        if (typeVariables != null)
            for (TypeVariable typeVariable : typeVariables)
                this.typeVariables.add(TypeVariableName.get(typeVariable.toString()));

        this.superInterfaces = new ArrayList<TypeName>();
        this.isMongoClient = classDoc.qualifiedTypeName().equals(MongoClient.class.getName());
        for (ClassDoc inter : classDoc.interfaces()) {
            String superClassName = inter.qualifiedTypeName();
            if (isSupportedSuperClass(superClassName)) {
                superInterfaces.add(ClassName.bestGuess(superClassName));
            } else {
                System.out.println("WARNING: interface of " + classDoc.qualifiedTypeName() + " has been ignored: " + superClassName);
            }
        }
        if (isMongoClient)
            superInterfaces.add(ClassName.get(Closeable.class));

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
            if (isMongoClient && methodDoc.name().equals("close"))
                continue;
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

        if (getTargetClassName().equals("MongoClient")) {
            typeBuilder.addMethod(
                    MethodSpec.methodBuilder("create")
                            .returns(ClassName.bestGuess(getTargetQualifiedClassName()))
                            .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                            .addParameter(ParameterSpec.builder(Vertx.class, "vertx").build())
                            .addJavadoc("Create a Mongo client which maintains its own data source and connects to a default server.\n" +
                                    "\n" +
                                    "@param vertx  the Vert.x instance\n" +
                                    "@return the client\n")
                            .addStatement("return new $T(vertx, new $T(), $T.randomUUID().toString())", ClassName.bestGuess(mapToImpl(getTargetQualifiedClassName())), ClassName.bestGuess("io.vertx.mongo.client.ClientConfig"), ClassName.get(UUID.class))
                            .build());
            typeBuilder.addMethod(
                    MethodSpec.methodBuilder("create")
                            .returns(ClassName.bestGuess(getTargetQualifiedClassName()))
                            .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                            .addParameter(ParameterSpec.builder(Vertx.class, "vertx").build())
                            .addParameter(ParameterSpec.builder(ClassName.bestGuess("io.vertx.mongo.client.ClientConfig"), "config").build())
                            .addJavadoc("Create a Mongo client which maintains its own data source.\n" +
                                    "\n" +
                                    "@param vertx  the Vert.x instance\n" +
                                    "@param config the configuration\n" +
                                    "@return the client\n")
                            .addStatement("return new $T(vertx, config, $T.randomUUID().toString())", ClassName.bestGuess(mapToImpl(getTargetQualifiedClassName())), ClassName.get(UUID.class))
                            .build());
            typeBuilder.addMethod(
                    MethodSpec.methodBuilder("createShared")
                            .returns(ClassName.bestGuess(getTargetQualifiedClassName()))
                            .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                            .addParameter(ParameterSpec.builder(Vertx.class, "vertx").build())
                            .addParameter(ParameterSpec.builder(ClassName.bestGuess("io.vertx.mongo.client.ClientConfig"), "config").build())
                            .addParameter(ParameterSpec.builder(ClassName.get(String.class), "dataSourceName").build())
                            .addJavadoc("Create a Mongo client which shares its data source with any other Mongo clients created with the same\n" +
                                    "data source name\n" +
                                    "\n" +
                                    "@param vertx          the Vert.x instance\n" +
                                    "@param config         the configuration\n" +
                                    "@param dataSourceName the data source name\n" +
                                    "@return the client\n")
                            .addStatement("return new $T(vertx, config, dataSourceName)", ClassName.bestGuess(mapToImpl(getTargetQualifiedClassName())))
                            .build());

            typeBuilder.addMethod(
                    MethodSpec.methodBuilder("close")
                            .returns(ParameterizedTypeName.get(ClassName.get(Future.class), ClassName.get(Void.class)))
                            .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                            .addJavadoc("Close the client and release its resources")
                            .build());

            typeBuilder.addMethod(
                    MethodSpec.methodBuilder("close")
                            .returns(TypeName.VOID)
                            .addParameter(ParameterSpec.builder(ParameterizedTypeName.get(ClassName.get(Handler.class), ParameterizedTypeName.get(ClassName.get(AsyncResult.class), ClassName.get(Void.class))), "handler").build())
                            .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                            .addJavadoc("Close the client and release its resources\n@param handler handler")
                            .build());
        }

        inflateType(typeBuilder, isImpl, null, null, null);

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

        inflateType(typeBuilder, isImpl, null, this::implementMethod, this::implementHandlerMethod);

        if (isMongoClient) {
            typeBuilder.addMethod(MethodSpec.constructorBuilder()
                    .addModifiers(Modifier.PUBLIC)
                    .addParameter(ParameterSpec.builder(Vertx.class, "vertx").build())
                    .addParameter(ParameterSpec.builder(ClassName.bestGuess("io.vertx.mongo.client.ClientConfig"), "config").build())
                    .addParameter(ParameterSpec.builder(ClassName.get(String.class), "dataSourceName").build())
                    .addJavadoc("Create a Mongo client which shares its data source with any other Mongo clients created with the same\n" +
                            "data source name\n" +
                            "\n" +
                            "@param vertx          the Vert.x instance\n" +
                            "@param config         the configuration\n" +
                            "@param dataSourceName the data source name\n" +
                            "@return the client\n")
                    .addStatement("super(vertx, config, dataSourceName)")
                    .build()
            );
        } else {
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
        }

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
            if (context.reactiveApiClasses.contains(paramFullMongoType.rawType.toString())) {
                    returnedVertxReactiveClass = ParameterizedTypeName.get(
                            ClassName.bestGuess(mapPackageName(paramFullMongoType.rawType.toString(), true)),
                            paramFullVertxType.typeArguments.get(0)
                    );
            }
        }
        if (method.returnType.isSinglePublisher && fullMongoType instanceof ParameterizedTypeName) {
            ParameterizedTypeName paramFullMongoType = (ParameterizedTypeName) fullMongoType;
            if (paramFullMongoType.typeArguments.size() > 1)
                throw new IllegalStateException("not implemented");
            if (context.reactiveApiClasses.contains(paramFullMongoType.typeArguments.get(0).toString()))
                returnedVertxReactiveClass =
                        ClassName.bestGuess(mapPackageName(paramFullMongoType.typeArguments.get(0).toString(), true));
        }

        if (method.returnType.isPublisher) {
            if (method.mongoName.contains("ulk") || method.mongoName.equals("watch")) {
                // FIXME
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
                methodBuilder.addStatement("__publisher.subscribe(new $T<>(clientContext, __promise))", ClassName.bestGuess("io.vertx.mongo.impl.SingleResultSubscriber"));
                if (returnedVertxReactiveClass != null) {
                    methodBuilder.addStatement("return __promise.future().map(__wrapped -> new $T(this.clientContext, __wrapped))",
                            returnedVertxReactiveClass
                    );
                } else if (method.resultConversionMethod == null) {
                    methodBuilder.addStatement("return __promise.future()");
                } else {
                    methodBuilder.addStatement("return __promise.future().map($T.INSTANCE::" + method.resultConversionMethod + ")", ClassName.bestGuess("io.vertx.mongo.impl.ConversionUtilsImpl"));
                }
            } else if (method.returnType.vertxType.equals(method.returnType.mongoType)) {
                methodBuilder.addStatement("$T __publisher = wrapped." + method.mongoName +  "(" + paramNames + ")", fullMongoType);
                writePublisherMethod(method, methodBuilder);
//                if (currentMethodHasPublisherOptions)
//                    methodBuilder.addStatement("options.initializePublisher(__publisher)");
//                methodBuilder.addStatement("return new $T<>(clientContext, __publisher)", ClassName.bestGuess("io.vertx.mongo.impl.MongoResultImpl"));
            } else {
                if (!method.returnType.vertxType.toString().equals(JsonObject.class.getName())||
                        !method.returnType.mongoType.toString().equals(Document.class.getName()))
                    throw new IllegalStateException("not implemented: need a mapper?");
                paramNames.add("$T.class");
                methodBuilder.addStatement("$T __publisher = wrapped." + method.mongoName +  "(" + paramNames + ")",
                        ParameterizedTypeName.get(method.returnType.publisherClassName, ClassName.get(JsonObject.class)),
                        ClassName.get(JsonObject.class)
                );
                writePublisherMethod(method, methodBuilder);
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

    private void writePublisherMethod(MongoMethod method, MethodSpec.Builder methodBuilder) {
        if (currentMethodHasPublisherOptions)
            methodBuilder.addStatement("options.initializePublisher(__publisher)");
        InspectionContext.PublisherDesc publisherDesc = context.publisherDescriptions.get(method.returnType.publisherClassName.toString());
        ClassName resultClassName = ClassName.bestGuess("io.vertx.mongo.MongoResult");
        StringJoiner resultParamNames = new StringJoiner(", ");
        if (publisherDesc != null)
            resultClassName = publisherDesc.resultClassName;
        resultClassName = ClassName.bestGuess(mapToImpl(resultClassName.toString()));
        if (publisherDesc != null && publisherDesc.toCollectionMethodName != null)
            resultParamNames.add("__publisher::" + publisherDesc.toCollectionMethodName);
        resultParamNames.add("clientContext");
        resultParamNames.add("__publisher");
        if (publisherDesc != null && publisherDesc.firstMethodName != null)
            resultParamNames.add("__publisher::" + publisherDesc.firstMethodName);
        if (publisherDesc != null && publisherDesc.batchSizePropertyName != null && currentMethodHasPublisherOptions) {
            methodBuilder.addStatement("Integer __batchSize = options.getBatchSize()");
            methodBuilder.beginControlFlow("if (__batchSize != null)");
            methodBuilder.addStatement("return new $T<>("  + resultParamNames + ", __batchSize)", resultClassName);
            methodBuilder.nextControlFlow("else");
            methodBuilder.addStatement("return new $T<>("  + resultParamNames + ")", resultClassName);
            methodBuilder.endControlFlow();
        } else {
            methodBuilder.addStatement("return new $T<>("  + resultParamNames + ")", resultClassName);
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


}
