package org.bfreuden;

import com.google.common.collect.Lists;
import com.mongodb.client.model.changestream.ChangeStreamDocument;
import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoCollection;
import com.mongodb.reactivestreams.client.gridfs.GridFSBucket;
import com.mongodb.reactivestreams.client.gridfs.GridFSBuckets;
import com.squareup.javapoet.*;
import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.MethodDoc;
import com.sun.javadoc.Type;
import com.sun.javadoc.TypeVariable;
import io.vertx.core.*;
import io.vertx.core.json.JsonObject;
import io.vertx.mongo.client.gridfs.impl.GridFSReadStreamPublisher;
import io.vertx.mongo.impl.MappingPublisher;
import org.bfreuden.mappers.MapperGenerator;
import org.bson.Document;

import javax.lang.model.element.Modifier;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ReactiveAPIClassGenerator extends GenericAPIClassGenerator {

    private ArrayList<TypeName> superInterfaces;
    private TypeName wrappedType;
    private boolean isMongoClient;
    private boolean isMongoCollection;

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
            this.classJavadoc = sanitizeJavadoc(this.classJavadoc);
            this.classJavadoc = Arrays.stream(this.classJavadoc.split("[\\n\\r]+"))
                    .filter(s -> !s.trim().isEmpty() && !s.contains("TDocument"))
                    .collect(Collectors.joining("\n"));
        }

        if (typeVariables != null)
            for (TypeVariable typeVariable : typeVariables)
                this.typeVariables.add(TypeVariableName.get(typeVariable.toString()));

        this.superInterfaces = new ArrayList<TypeName>();
        this.isMongoClient = classDoc.qualifiedTypeName().equals(MongoClient.class.getName());
        this.isMongoCollection = classDoc.qualifiedTypeName().equals(MongoCollection.class.getName());
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
                    MethodSpec.methodBuilder("createShared")
                            .returns(ClassName.bestGuess(getTargetQualifiedClassName()))
                            .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                            .addParameter(ParameterSpec.builder(Vertx.class, "vertx").build())
                            .addParameter(ParameterSpec.builder(ClassName.bestGuess("io.vertx.mongo.client.ClientConfig"), "config").build())
                            .addJavadoc("Create a Mongo client which shares its data source with any other Mongo clients created with the same\n" +
                                    "default data source\n" +
                                    "\n" +
                                    "@param vertx          the Vert.x instance\n" +
                                    "@param config         the configuration\n" +
                                    "@return the client\n")
                            .addStatement("return new $T(vertx, config, $S)", ClassName.bestGuess(mapToImpl(getTargetQualifiedClassName())), "__MONGO-DEFAULT-DS")
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
        if (classDoc.name().equals("GridFSBucket")) {
            typeBuilder.addMethod(MethodSpec.methodBuilder("create")
                    .addParameter(ClassName.bestGuess("io.vertx.mongo.client.MongoDatabase"), "database")
                    .addModifiers(Modifier.STATIC, Modifier.PUBLIC)
                    .returns(fluentReturnType)
                    .addStatement("return new $T((($T)database).getClientContext(), $T.create(database.toDriverClass((($T)database).getClientContext())))",

                            ClassName.bestGuess("io.vertx.mongo.client.gridfs.impl.GridFSBucketImpl"),
                            ClassName.bestGuess("io.vertx.mongo.client.impl.MongoDatabaseImpl"),
                            ClassName.get(GridFSBuckets.class),
                            ClassName.bestGuess("io.vertx.mongo.client.impl.MongoDatabaseImpl")
                    )
                    .build());
            typeBuilder.addMethod(MethodSpec.methodBuilder("create")
                    .addParameter(ClassName.bestGuess("io.vertx.mongo.client.MongoDatabase"), "database")
                    .addParameter(ClassName.get(String.class), "bucketName")
                    .addModifiers(Modifier.STATIC, Modifier.PUBLIC)
                    .returns(fluentReturnType)
                    .addStatement("return new $T((($T)database).getClientContext(), $T.create(database.toDriverClass((($T)database).getClientContext()), bucketName))",
                            ClassName.bestGuess("io.vertx.mongo.client.gridfs.impl.GridFSBucketImpl"),
                            ClassName.bestGuess("io.vertx.mongo.client.impl.MongoDatabaseImpl"),
                            ClassName.get(GridFSBuckets.class),
                            ClassName.bestGuess("io.vertx.mongo.client.impl.MongoDatabaseImpl")
                    )
                    .build());
        }

        inflateType(typeBuilder, isImpl, null, null, null, null);

        typeBuilder.addMethod(
                MethodSpec.methodBuilder("toDriverClass")
                        .addParameter(ClassName.bestGuess("io.vertx.mongo.impl.MongoClientContext"), "clientContext")
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

        inflateType(typeBuilder, isImpl, null, this::implementMethod, this::implementHandlerMethod, this::implementNoStreamMethod);

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
            if (isMongoCollection) {
                ParameterizedTypeName mapperType = ParameterizedTypeName.get(ClassName.get(Function.class), TypeVariableName.get("TDocument"), TypeVariableName.get("TDocument"));
                typeBuilder.addField(FieldSpec.builder(mapperType, "inputMapper").addModifiers(Modifier.PROTECTED, Modifier.FINAL).build());
                typeBuilder.addField(FieldSpec.builder(mapperType, "outputMapper").addModifiers(Modifier.PROTECTED, Modifier.FINAL).build());
                typeBuilder.addMethod(MethodSpec.constructorBuilder()
                        .addModifiers(Modifier.PUBLIC)
                        .addParameter(ParameterSpec.builder(ClassName.bestGuess("io.vertx.mongo.impl.MongoClientContext"), "clientContext").build())
                        .addParameter(ParameterSpec.builder(wrappedType, "wrapped").build())
                        .addStatement("this(clientContext, wrapped, null, null)")
                        .build());
                typeBuilder.addMethod(MethodSpec.constructorBuilder()
                        .addModifiers(Modifier.PUBLIC)
                        .addParameter(ParameterSpec.builder(ClassName.bestGuess("io.vertx.mongo.impl.MongoClientContext"), "clientContext").build())
                        .addParameter(ParameterSpec.builder(wrappedType, "wrapped").build())
                        .addParameter(ParameterSpec.builder(mapperType, "inputMapper").build())
                        .addParameter(ParameterSpec.builder(mapperType, "outputMapper").build())
                        .addStatement("this.clientContext = clientContext")
                        .addStatement("this.wrapped = wrapped")
                        .addStatement("this.inputMapper = inputMapper")
                        .addStatement("this.outputMapper = outputMapper")
                        .build());
            } else {
                typeBuilder.addMethod(MethodSpec.constructorBuilder()
                        .addModifiers(Modifier.PUBLIC)
                        .addParameter(ParameterSpec.builder(ClassName.bestGuess("io.vertx.mongo.impl.MongoClientContext"), "clientContext").build())
                        .addParameter(ParameterSpec.builder(wrappedType, "wrapped").build())
                        .addStatement("this.clientContext = clientContext")
                        .addStatement("this.wrapped = wrapped")
                        .build());
            }
            typeBuilder.addMethod(MethodSpec.methodBuilder("getClientContext")
                    .addModifiers(Modifier.PUBLIC)
                    .returns(ClassName.bestGuess("io.vertx.mongo.impl.MongoClientContext"))
                    .addStatement("return clientContext")
                    .build());
        }
        typeBuilder.addMethod(
                MethodSpec.methodBuilder("toDriverClass")
                        .addParameter(ClassName.bestGuess("io.vertx.mongo.impl.MongoClientContext"), "clientContext")
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
            if (!param.type.vertxType.isPrimitive() && !param.type.isNullable) {
                staticImports.add("java.util.Objects.requireNonNull");
                methodBuilder.addStatement("requireNonNull(" + param.name + ", $S)", param.name + " is null");
            }
        }
        StringJoiner paramNames = new StringJoiner(", ");
        for (MongoMethodParameter param : method.params) {
            MapperGenerator mapper = param.type.mapper;
            if (param.type.isPublisher && param.type.isBinaryReadStream) {
                String paramName = "__" + param.name;
                paramNames.add(paramName);
                methodBuilder.addStatement("$T " + paramName + " = new $T(" + param.name + ")", ClassName.get(GridFSReadStreamPublisher.class), ClassName.get(GridFSReadStreamPublisher.class));
            } else if (mapper != null) {
                String originalParamName = param.name;
                //TODO hack
                if (method.vertxName.equals("bulkWrite") && param.name.equals("requests"))
                    originalParamName = String.format("((List<? extends WriteModel<TDocument>>)%s)", param.name);
                String paramName = "__" + param.name;
                paramNames.add(paramName);
                if (param.type.isNullable)
                    methodBuilder.addStatement(mapper.asStatementFromExpression("$T " + paramName + " = " + param.name +" == null ? null : %s", originalParamName, param.type.mongoType, null));
                else
                    methodBuilder.addStatement(mapper.asStatementFromExpression("$T " + paramName + " = %s", originalParamName, param.type.mongoType, null));
            } else {
                paramNames.add(param.name);
            }
            String vertxTypeString = param.type.vertxType.toString();
            //TODO hack
            if (vertxTypeString.contains("TDocument") && !vertxTypeString.contains("NewTDocument") && !param.name.equals("clazz")) {
                if (vertxTypeString.equals("TDocument"))
                    methodBuilder.addStatement(String.format("%s = mapDoc(%s, inputMapper)", param.name, param.name));
                else if (!vertxTypeString.contains("WriteModel"))
                    methodBuilder.addStatement(String.format("%s = mapDocList(%s, inputMapper)", param.name, param.name));
            } else if (vertxTypeString.contains("JsonArray") && param.type.mapper == null) {
                // should not happen
                methodBuilder.addComment("FIXME handle JsonArray mapping");
            } else if (vertxTypeString.contains("JsonObject") && param.type.mapper == null) {
                // should not happen
                methodBuilder.addComment("FIXME handle JsonObject mapping");
            }
        }
        if (method.returnType.isPublisher) {
            if (method.returnType.singlePublisher) {
                // special mapping case that can be handled by the mongo driver (Document to JsonObject) because we have the codec
                if (method.returnType.publishedType.vertxType.toString().equals(JsonObject.class.getName()) &&
                        method.returnType.publishedType.mongoType.toString().equals(Document.class.getName())
                ) {
                    ParameterizedTypeName publisherType = ParameterizedTypeName.get(method.returnType.publisherClassName, method.returnType.publishedType.vertxType);
                    paramNames.add("$T.class");
                    methodBuilder.addStatement("$T __publisher = wrapped." + method.mongoName +  "(" + paramNames + ")", publisherType, method.returnType.publishedType.vertxType);
                    methodBuilder.addStatement("$T __promise = clientContext.getVertx().promise()", ParameterizedTypeName.get(ClassName.get(Promise.class), method.returnType.publishedType.vertxType));
                    method.returnType.publishedType.mapper = null; // already mapped using mongo driver facility
                } else {
                    methodBuilder.addStatement("$T __publisher = wrapped." + method.mongoName +  "(" + paramNames + ")", method.returnType.mongoType);
                    methodBuilder.addStatement("$T __promise = clientContext.getVertx().promise()", ParameterizedTypeName.get(ClassName.get(Promise.class), method.returnType.publishedType.mongoType));
                }

                methodBuilder.addStatement("__publisher.subscribe(new $T<>(clientContext, __promise))", ClassName.bestGuess("io.vertx.mongo.impl.SingleResultSubscriber"));
                String publishedTypeString = method.returnType.publishedType.vertxType.toString();
                String mapper = null;
                String mapperCall = null;
                if (publishedTypeString.contains("TDocument")) {
                    if (publishedTypeString.equals("TDocument")) {
                        mapper = "outputMapper";
                        mapperCall = ".map(outputMapper)";
                    } else {
                        methodBuilder.addComment("FIXME single map something based on document");
                    }
                } else if (publishedTypeString.equals(JsonObject.class.getName())) {
                    mapper = "clientContext.getConfig().getOutputMapper()";
                    mapperCall = ".map(clientContext.getConfig().getOutputMapper())";
                }
                if (mapperCall != null) {
                    methodBuilder.beginControlFlow(String.format("if (%s == null)", mapper));
                }
                if (method.returnType.publishedType.mapper == null) {
                    methodBuilder.addStatement("return __promise.future()");
                } else {
                    methodBuilder.addStatement(method.returnType.publishedType.mapper.asStatementFromLambdaOrMethodRef("return __promise.future().map(%s)"));
                }
                if (mapperCall != null) {
                    methodBuilder.nextControlFlow("else");
                    if (method.returnType.publishedType.mapper == null) {
                        methodBuilder.addStatement("return __promise.future()" + mapperCall);
                    } else {
                        methodBuilder.addStatement(method.returnType.publishedType.mapper.asStatementFromLambdaOrMethodRef("return __promise.future().map(%s)" + mapperCall));
                    }
                    methodBuilder.endControlFlow();
                }
            } else if (method.returnType.vertxType.equals(method.returnType.mongoType)) {
                methodBuilder.addStatement("$T __publisher = wrapped." + method.mongoName +  "(" + paramNames + ")", method.returnType.mongoType);
                writePublisherMethod(method, methodBuilder);
            } else {
                String publisherVarName = "__publisher";
                if (method.returnType.publishedType.vertxType.toString().equals(JsonObject.class.getName()) &&
                        method.returnType.publishedType.mongoType.toString().equals(Document.class.getName())
                ) {
                    TypeName publisherType = ParameterizedTypeName.get(method.returnType.publisherClassName, method.returnType.publishedType.vertxType);
                    paramNames.add("$T.class");
                    methodBuilder.addStatement("$T __publisher = wrapped." + method.mongoName +  "(" + paramNames + ")", publisherType, method.returnType.publishedType.vertxType);
                } else if (method.returnType.publishedType.mapper != null) {
                    publisherVarName = "__mappingPublisher";
                    if (method.returnType.publishedType.vertxType.toString().contains("JsonObject") && method.returnType.publishedType.mongoType.toString().contains("Document")) {
                        TypeName publisherType = ParameterizedTypeName.get(method.returnType.publisherClassName, ClassName.get(JsonObject.class));
                        paramNames.add("$T.class");
                        methodBuilder.addStatement("$T __publisher = wrapped." + method.mongoName +  "(" + paramNames + ")", publisherType, ClassName.get(JsonObject.class));
                        // TODO HACK for change streams?
                        TypeName mapperFirstParam;
                        if (method.returnType.publishedType.mongoType.toString().contains("ChangeStream")) {
                            mapperFirstParam = ParameterizedTypeName.get(ClassName.get(ChangeStreamDocument.class), ClassName.get(JsonObject.class));
                        } else {
                            mapperFirstParam = method.returnType.publishedType.mongoType;
                        }
                        methodBuilder.addStatement(method.returnType.publishedType.mapper.asStatementFromLambdaOrMethodRef("$T __mappingPublisher = new $T<>(__publisher, %s)",
                                ParameterizedTypeName.get(ClassName.get(MappingPublisher.class), mapperFirstParam, method.returnType.publishedType.vertxType),
                                ClassName.get(MappingPublisher.class), null
                        ));
                    } else {
                        TypeName publisherType = method.returnType.mongoType;
                        methodBuilder.addStatement("$T __publisher = wrapped." + method.mongoName +  "(" + paramNames + ")", publisherType);
                        methodBuilder.addStatement(method.returnType.publishedType.mapper.asStatementFromLambdaOrMethodRef("$T __mappingPublisher = new $T<>(__publisher, %s)",
                                ParameterizedTypeName.get(ClassName.get(MappingPublisher.class), method.returnType.publishedType.mongoType, method.returnType.publishedType.vertxType),
                                ClassName.get(MappingPublisher.class), null
                        ));
                    }
                } else {
                    methodBuilder.addStatement("$T __publisher = wrapped." + method.mongoName +  "(" + paramNames + ")", method.returnType.mongoType);
                }
                writePublisherMethod(method, methodBuilder, publisherVarName);
            }
        } else {
            if (method.mongoName.equals("getCollection") && method.params.size() == 1) {
                // special case
                methodBuilder.addStatement("$T __wrapped = wrapped.getCollection(collectionName, $T.class)",
                        ParameterizedTypeName.get(ClassName.get(MongoCollection.class), ClassName.get(JsonObject.class)),
                        ClassName.get(JsonObject.class)
                );
                methodBuilder.addStatement("return new $T<>(this.clientContext, __wrapped, this.clientContext.getConfig().getInputMapper(), this.clientContext.getConfig().getOutputMapper())", ClassName.bestGuess("io.vertx.mongo.client.impl.MongoCollectionImpl"));
            } else {
                String returnType = method.returnType.vertxType.toString();
                String returnKeyword = "return ";
                if (returnType.equals("void") || returnType.equals("java.lang.Void"))
                    returnKeyword = "";
                if (method.returnType.mapper == null) {
                        methodBuilder.addStatement(returnKeyword + "wrapped." + method.mongoName +  "(" + paramNames + ")");
                } else {
                    methodBuilder.addStatement("$T __result = wrapped." + method.mongoName +  "(" + paramNames + ")", method.returnType.mongoType);
                    methodBuilder.addStatement(method.returnType.mapper.asStatementFromExpression(returnKeyword + "%s", "__result"));
                }

            }
        }
    }

    private void writePublisherMethod(MongoMethod method, MethodSpec.Builder methodBuilder, String... publisherVarNames) {
        String publisherVarName = "__publisher";
        if (publisherVarNames.length > 0)
            publisherVarName = publisherVarNames[0];
        if (currentMethodHasPublisherOptions) {
            String optionsParamName = method.returnType.publisherClassName.toString().endsWith("GridFSDownloadPublisher") ? "controlOptions" : "options";
            methodBuilder.addStatement(String.format("%s.initializePublisher(clientContext, %s)", optionsParamName, "__publisher"));
        }
        InspectionContext.PublisherDesc publisherDesc = context.publisherDescriptions.get(method.returnType.publisherClassName.toString());
        ClassName resultClassName = ClassName.bestGuess("io.vertx.mongo.MongoResult");
        StringJoiner resultParamNames = new StringJoiner(", ");
        if (publisherDesc != null)
            resultClassName = publisherDesc.resultClassName;
        resultClassName = ClassName.bestGuess(mapToImpl(resultClassName.toString()));
        if (publisherDesc != null && publisherDesc.toCollectionMethodName != null)
            resultParamNames.add(String.format("%s::%s", publisherVarName, publisherDesc.toCollectionMethodName));
        resultParamNames.add("clientContext");
        resultParamNames.add(publisherVarName);
        String publishedTypeString = method.returnType.publishedType.vertxType.toString();
        if (publishedTypeString.contains("TDocument")) {
            if (publishedTypeString.equals("TDocument"))
                resultParamNames.add("outputMapper");
            else
                resultParamNames.add("outputMapper");
        } else if (publishedTypeString.equals(JsonObject.class.getName())) {
            resultParamNames.add("clientContext.getConfig().getOutputMapper()");
        }
        if (publisherDesc != null && publisherDesc.firstMethodName != null)
            resultParamNames.add(String.format("%s::%s", publisherVarName, publisherDesc.firstMethodName));
        String publisherParamBracket = method.returnType.vertxType instanceof ParameterizedTypeName ? "<>" : "";
        if (publisherDesc != null && publisherDesc.batchSizePropertyName != null && currentMethodHasPublisherOptions) {
            methodBuilder.addStatement("Integer __batchSize = options.getBatchSize()");
            methodBuilder.beginControlFlow("if (__batchSize != null)");
            methodBuilder.addStatement(String.format("return new $T%s("  + resultParamNames + ", __batchSize)", publisherParamBracket), resultClassName);
            methodBuilder.nextControlFlow("else");
            methodBuilder.addStatement(String.format("return new $T%s("  + resultParamNames + ")", publisherParamBracket), resultClassName);
            methodBuilder.endControlFlow();
        } else {
            methodBuilder.addStatement(String.format("return new $T%s("  + resultParamNames + ")", publisherParamBracket), resultClassName);
        }
    }

    private void implementHandlerMethod(MongoMethod method, MethodSpec.Builder methodBuilder) {
        StringJoiner paramNames = new StringJoiner(", ");
        for (MongoMethodParameter param : method.params)
            if (!currentMethodIsNoStream || !param.type.vertxType.toString().contains("Stream"))
                paramNames.add(param.name);
        staticImports.add("io.vertx.mongo.impl.Utils.setHandler");
        String methodName = method.vertxName;
        if (currentMethodIsNoStream)
            methodName = methodName.replace("Stream", "File");

        methodBuilder
                .addStatement("$T __future = this." + methodName + "(" + paramNames + ")", method.returnType.vertxType)
                .addStatement("setHandler(__future, resultHandler)")
//                .addStatement("return this")
        ;

    }

    private void implementNoStreamMethod(MongoMethod method, MethodSpec.Builder methodBuilder) {
        if (method.params.stream().noneMatch(p -> p.name.equals("filename")))
            throw new IllegalStateException("no filename param");
        StringJoiner params = new StringJoiner(", ");
        for (MongoMethodParameter param: method.params)
            if (param.type.vertxType.toString().contains("Stream"))
                params.add("_stream");
            else
                params.add(param.name);

        if (currentMethodHasPublisherOptions)
            params.add("controlOptions");
        methodBuilder
                .addStatement("requireNonNull(filename, $S)", "fileName cannot be null")
                .addStatement(String.format("return openFile(clientContext.getVertx(), filename).compose(_stream -> uploadStream(%s))", params));

    }


}
