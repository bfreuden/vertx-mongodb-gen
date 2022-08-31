package org.bfreuden;

import com.mongodb.reactivestreams.client.gridfs.GridFSBucket;
import com.squareup.javapoet.*;
import com.sun.javadoc.*;
import io.vertx.codegen.annotations.DataObject;
import io.vertx.codegen.annotations.Nullable;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonObject;
import io.vertx.core.streams.ReadStream;
import io.vertx.core.streams.WriteStream;
import io.vertx.mongo.client.impl.OptionSerializer;
import org.bfreuden.mappers.ConversionUtilsMapperGenerator;
import org.reactivestreams.Publisher;

import javax.lang.model.element.Modifier;
import java.nio.ByteBuffer;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

public abstract class GenericAPIClassGenerator extends APIClassGenerator {

    protected String classJavadoc;
    protected final ArrayList<TypeVariableName> typeVariables = new ArrayList<>();
    protected boolean currentMethodHasPublisherOptions;
    protected boolean currentMethodIsNoStream;
    protected boolean hasBuilder;
    protected String configurableName;
    protected boolean isResultOnlyOptions;
    protected boolean resultBean;
    protected ClassName currentlyGeneratedTypeName;
    protected String currentlyGeneratedPackageName;

    public GenericAPIClassGenerator(InspectionContext context, ClassDoc classDoc) {
        super(context, classDoc);
    }

    @Override
    protected List<JavaFile.Builder> getJavaFiles() {
        return null;
    }

    protected MongoMethod analyzeMethod(MethodDoc methodDoc) {
        Type methodReturnType = methodDoc.returnType();
        return analyzeMethodOrConstructor(methodDoc, methodReturnType);
    }

    protected MongoMethod analyzeConstructor(ConstructorDoc methodDoc) {
        return analyzeMethodOrConstructor(methodDoc, null);
    }

    private MongoMethod analyzeMethodOrConstructor(ExecutableMemberDoc methodDoc, Type methodReturnType) {
        TypeVariable[] methodTypeVariables = methodDoc.typeParameters();
        Optional<TypeVariable> resultType = Arrays.stream(methodTypeVariables).filter(v -> v.qualifiedTypeName().equals("TResult")).findFirst();
        boolean resultParameter = resultType.isPresent();
        if (resultParameter) {
            System.out.println("INFO: method " + methodDoc + " has been ignored because it has a TResult type parameter");
            return null;
        }

        if (classDoc.qualifiedTypeName().equals(GridFSBucket.class.getName()) &&
                methodDoc.getRawCommentText().contains("custom id")
        ) {
            // ignore for the moment because it conflicts
            System.out.println("WARNING: method " + methodDoc + " is conflicting with others so it has been ignored");
            return null;
        }

        MongoMethod mongoMethod = new MongoMethod();
        mongoMethod.signature = methodDoc.signature();
        mongoMethod.mongoName = methodDoc.name();
        mongoMethod.vertxName = methodDoc.name();
        mongoMethod.mongoJavadoc = methodDoc.getRawCommentText();
        TypeVariable[] typeVariables = methodDoc.typeParameters();
        if (typeVariables != null && typeVariables.length > 0) {
            for (TypeVariable variable : typeVariables) {
                mongoMethod.typeVariables.add(TypeVariableName.get(variable.typeName()));
            }
        }

        mongoMethod.returnType = methodReturnType == null ? null : getActualType(methodDoc, null, methodReturnType, TypeLocation.RETURN);
        if (methodReturnType != null && mongoMethod.returnType == null) {
            System.out.println("WARNING: return type of " + methodDoc + " is unknown so method has been ignored");
            return null;
        }

        if (classDoc.qualifiedTypeName().equals(GridFSBucket.class.getName()) &&
                methodDoc.name().equals("downloadToPublisher")
        ) {
            if (mongoMethod.mongoJavadoc.contains(" id"))
                mongoMethod.vertxName = "downloadByObjectId";
            else
                mongoMethod.vertxName = "downloadByFilename";

        }
        if (classDoc.qualifiedTypeName().equals(GridFSBucket.class.getName()) &&
                methodDoc.name().equals("uploadFromPublisher")
        ) {
            mongoMethod.vertxName = "uploadStream";
        }

        for (Parameter param : methodDoc.parameters()) {
            MongoMethodParameter methodParameter = new MongoMethodParameter();
            methodParameter.name = param.name();
            methodParameter.type = getActualType(methodDoc, param.name(), param.type(), TypeLocation.PARAMETER);
            if (methodParameter.type == null) {
                System.out.println("WARNING: param type of " + methodDoc + " is unknown so method has been ignored");
                return null;
            }
            if (methodParameter.type.isPublisher) {
                if (mongoMethod.mongoName.contains("upload")) {
                    if (methodParameter.type.mongoType.toString().equals(ParameterizedTypeName.get(ClassName.get(Publisher.class), ClassName.get(ByteBuffer.class)).toString())) {
                        methodParameter.type.vertxType = ParameterizedTypeName.get(ClassName.get(ReadStream.class), ClassName.get(Buffer.class));
                        methodParameter.type.publishedType.vertxType = ClassName.get(Buffer.class);
                        String conversionMethod = context.conversionUtilsGenerator.addConversion(ClassName.get(ByteBuffer.class), ClassName.get(Buffer.class));
                        methodParameter.type.publishedType.mapper = new ConversionUtilsMapperGenerator(conversionMethod);
                        methodParameter.type.isBinaryReadStream = true;
                    } else {
                        System.out.println("WARNING: one param of " + methodDoc + " is a publisher so method has been ignored because it does not publish " + ByteBuffer.class);
                    }
                } else if (mongoMethod.mongoName.contains("download")) {
                    if (methodParameter.type.mongoType.toString().equals(ParameterizedTypeName.get(ClassName.get(Publisher.class), ClassName.get(ByteBuffer.class)).toString())) {
                        methodParameter.type.vertxType = ParameterizedTypeName.get(ClassName.get(WriteStream.class), ClassName.get(Buffer.class));
                        methodParameter.type.publishedType.vertxType = ClassName.get(Buffer.class);
                        String conversionMethod = context.conversionUtilsGenerator.addConversion(ClassName.get(Buffer.class), ClassName.get(ByteBuffer.class));
                        methodParameter.type.publishedType.mapper = new ConversionUtilsMapperGenerator(conversionMethod);
                        methodParameter.type.isBinaryWriteStream = true;
                    } else {
                        System.out.println("WARNING: one param of " + methodDoc + " is a publisher so method has been ignored because it does not publish " + ByteBuffer.class);
                    }
                } else {
                    System.out.println("WARNING: one param of " + methodDoc + " is a publisher so method has been ignored because we can't decide if it is an upload or a download");
                    return null;
                }
            }
            mongoMethod.params.add(methodParameter);
        }
        mongoMethod.computeJavadocs();
        return mongoMethod;
    }

    protected MethodSpec.Builder fromDriverClassBuilder() {
        MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder("fromDriverClass")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC);

        methodBuilder.addJavadoc("@param from from\n@return mongo object\n@hidden");
        TypeName returnType;
        for (TypeVariableName typeVariableName: typeVariables)
            methodBuilder.addTypeVariable(typeVariableName);
        if (typeVariables.isEmpty()) {
            returnType = ClassName.bestGuess(mapPackageName(classDoc.qualifiedTypeName()));
            methodBuilder.addParameter(ParameterSpec.builder(ClassName.bestGuess(classDoc.qualifiedTypeName()), "from").build());
        } else {
            returnType = ParameterizedTypeName.get(ClassName.bestGuess(mapPackageName(classDoc.qualifiedTypeName())), typeVariables.toArray(new TypeName[0]));
            methodBuilder.addParameter(ParameterSpec.builder(ParameterizedTypeName.get(ClassName.bestGuess(classDoc.qualifiedTypeName()), typeVariables.toArray(new TypeName[0])), "from").build());
        }
        methodBuilder.returns(returnType);
        staticImports.add("java.util.Objects.requireNonNull");
        methodBuilder.addStatement("requireNonNull(from, $S)", "from is null");
        methodBuilder.addStatement("$T result = new $T()",returnType, returnType);
        for (Option option: optionsByName.values()) {
            methodBuilder.beginControlFlow("try");
            if (option.withTimeUnit) {
                methodBuilder.addStatement("result." + option.name + " = from." + option.mongoGetterName + "($T.MILLISECONDS)",  ClassName.get(TimeUnit.class));
            } else if (option.type.mapper != null) {
                methodBuilder.addStatement(option.type.mapper.asStatementFromExpression("result." + option.name + " = %s", "from." + option.mongoGetterName + "()"));
            } else {
                methodBuilder.addStatement("result." + option.name + " = from." + option.mongoGetterName + "()");
            }
            methodBuilder.nextControlFlow("catch (Exception ex)");
            methodBuilder.addStatement("result." + option.name +"Exception = ex");
            methodBuilder.endControlFlow();
        }
        methodBuilder.addStatement("return result");
        return methodBuilder;
    }

    private static class MethodWriteConfig {
        final String vertxName;
        final List<MongoMethodParameter> params;
        final String vertxResultOrFutureJavadoc;
        final String vertxAsyncJavadoc;
        final String vertxWithOptionsJavadoc;
        final BiConsumer<MongoMethod, MethodSpec.Builder> methodCustomizer;
        final boolean isNoStream;
        public MethodWriteConfig(
                String vertxName,
                List<MongoMethodParameter> params,
                String vertxResultOrFutureJavadoc,
                String vertxAsyncJavadoc,
                String vertxWithOptionsJavadoc,
                BiConsumer<MongoMethod, MethodSpec.Builder> methodCustomizer,
                boolean isNoStream
        ) {
            this.vertxName = vertxName;
            this.params = params;
            this.vertxResultOrFutureJavadoc = vertxResultOrFutureJavadoc;
            this.vertxAsyncJavadoc = vertxAsyncJavadoc;
            this.vertxWithOptionsJavadoc = vertxWithOptionsJavadoc;
            this.methodCustomizer = methodCustomizer;
            this.isNoStream = isNoStream;
        }
    }

    public void inflateType(
        TypeSpec.Builder type,
        boolean isImpl,
        BiConsumer<MongoMethod, MethodSpec.Builder> ctorCustomizer,
        BiConsumer<MongoMethod, MethodSpec.Builder> methodCustomizer,
        BiConsumer<MongoMethod, MethodSpec.Builder> handlerMethodCustomizer,
        BiConsumer<MongoMethod, MethodSpec.Builder> noStreamMethodCustomizer
    ) {
        if (!isImpl)
            type.addJavadoc(classJavadoc);
        for (TypeVariableName typeVariable : typeVariables)
            type.addTypeVariable(typeVariable);

        for (MongoMethod method : constructors) {
            this.currentMethodHasPublisherOptions = false;
            MethodSpec.Builder methodBuilder = MethodSpec.constructorBuilder()
                    .addModifiers(Modifier.PUBLIC);
            for (TypeVariableName variable : method.typeVariables)
                methodBuilder.addTypeVariable(variable);
            for (MongoMethodParameter param : method.params) {
                methodBuilder.addParameter(ParameterSpec.builder(param.type.vertxType, param.name).build());
            }
            if (method.vertxResultOrFutureJavadoc != null && !isImpl)
                methodBuilder.addJavadoc(method.vertxResultOrFutureJavadoc);
            if (isImpl)
                methodBuilder.addAnnotation(Override.class);
            if (ctorCustomizer != null)
                ctorCustomizer.accept(method, methodBuilder);
            type.addMethod(methodBuilder.build());
        }
        for (MongoMethod method : methods) {
            this.currentMethodHasPublisherOptions = false;
            this.currentMethodIsNoStream = false;
            boolean methodReturnsGridFSPublisher = method.returnType.publisherClassName != null && method.returnType.publisherClassName.toString().endsWith("GridFSDownloadPublisher");
            boolean gridfsUploadMethod = method.mongoName.equals("uploadFromPublisher");
            List<MethodWriteConfig> configs = new ArrayList<>();
            configs.add(new MethodWriteConfig(
                    method.vertxName,
                    method.params,
                    method.vertxResultOrFutureJavadoc,
                    method.vertxAsyncJavadoc,
                    method.vertxWithOptionsJavadoc,
                    methodCustomizer,
                    false)
            );
            if (gridfsUploadMethod)
                configs.add(new MethodWriteConfig(
                        method.vertxName.replace("Stream", "File"),
                        method.params.stream().filter(it -> !it.type.vertxType.toString().contains("ReadStream")).collect(Collectors.toList()),
                        method.vertxNoStreamJavadoc,
                        method.vertxNoStreamAsyncJavadoc,
                        method.vertxWithOptionsJavadoc,
                        noStreamMethodCustomizer,
                        true
                ));
            for (MethodWriteConfig methodWriteConfig : configs) {
                {
                    this.currentMethodIsNoStream = methodWriteConfig.isNoStream;
                    MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder(methodWriteConfig.vertxName)
                            .addModifiers(Modifier.PUBLIC)
                            .returns(method.returnType.vertxType);
                    if (!isImpl)
                        methodBuilder.addModifiers(Modifier.ABSTRACT);
                    for (TypeVariableName variable : method.typeVariables)
                        methodBuilder.addTypeVariable(variable);
                    for (MongoMethodParameter param : methodWriteConfig.params) {
                        ParameterSpec.Builder builder = ParameterSpec.builder(param.type.vertxType, param.name);
                        if (param.type.isNullable)
                            builder.addAnnotation(Nullable.class);
                        methodBuilder.addParameter(builder.build());
                    }
                    if (methodWriteConfig.vertxResultOrFutureJavadoc != null && !isImpl)
                        methodBuilder.addJavadoc(methodWriteConfig.vertxResultOrFutureJavadoc);
                    if (isImpl)
                        methodBuilder.addAnnotation(Override.class);
                    if (methodWriteConfig.methodCustomizer != null)
                        methodWriteConfig.methodCustomizer.accept(method, methodBuilder);
                    type.addMethod(methodBuilder.build());
                }

                if (method.returnType.singlePublisher) {
                    MethodSpec.Builder handlerMethodBuilder = MethodSpec.methodBuilder(methodWriteConfig.vertxName)
                            .addModifiers(Modifier.PUBLIC)
                            .returns(TypeName.VOID);
                    if (!isImpl)
                        handlerMethodBuilder.addModifiers(Modifier.ABSTRACT);
                    for (TypeVariableName variable : method.typeVariables)
                        handlerMethodBuilder.addTypeVariable(variable);
                    for (MongoMethodParameter param : methodWriteConfig.params) {
                        handlerMethodBuilder.addParameter(ParameterSpec.builder(param.type.vertxType, param.name).build());
                    }
                    handlerMethodBuilder.addParameter(ParameterSpec.builder(ParameterizedTypeName.get(ClassName.get(Handler.class), ParameterizedTypeName.get(ClassName.get(AsyncResult.class), method.returnType.publishedType.vertxType)), "resultHandler").build());
                    if (methodWriteConfig.vertxAsyncJavadoc != null && !isImpl)
                        handlerMethodBuilder.addJavadoc(methodWriteConfig.vertxAsyncJavadoc);
                    if (isImpl)
                        handlerMethodBuilder.addAnnotation(Override.class);
                    if (handlerMethodCustomizer != null)
                        handlerMethodCustomizer.accept(method, handlerMethodBuilder);
                    type.addMethod(handlerMethodBuilder.build());
                } else if (method.returnType.isPublisher && !method.returnType.publisherClassName.toString().equals(Publisher.class.getName())) {
                    this.currentMethodHasPublisherOptions = true;
                    MethodSpec.Builder methodWithOptionsBuilder = MethodSpec.methodBuilder(methodWriteConfig.vertxName)
                            .addModifiers(Modifier.PUBLIC)
                            .returns(method.returnType.vertxType);
                    if (!isImpl)
                        methodWithOptionsBuilder.addModifiers(Modifier.ABSTRACT);
                    for (TypeVariableName variable : method.typeVariables)
                        methodWithOptionsBuilder.addTypeVariable(variable);
                    for (MongoMethodParameter param : methodWriteConfig.params) {
                        methodWithOptionsBuilder.addParameter(ParameterSpec.builder(param.type.vertxType, param.name).build());
                    }
                    String optionsClass = context.publisherOptionsClasses.get(method.returnType.publisherClassName.toString());
                    //TODO hack!
                    String optionsParamName = methodReturnsGridFSPublisher ? "controlOptions" : "options";
                    methodWithOptionsBuilder.addParameter(ParameterSpec.builder(ClassName.bestGuess(optionsClass), optionsParamName).build());
                    if (methodWriteConfig.vertxWithOptionsJavadoc != null && !isImpl)
                        methodWithOptionsBuilder.addJavadoc(methodWriteConfig.vertxWithOptionsJavadoc);
                    if (isImpl)
                        methodWithOptionsBuilder.addAnnotation(Override.class);
                    if (methodWriteConfig.methodCustomizer != null)
                        methodWriteConfig.methodCustomizer.accept(method, methodWithOptionsBuilder);
                    type.addMethod(methodWithOptionsBuilder.build());

                }
            }
        }
    }

    protected MethodSpec.Builder toDriverClassOrInitDriverBuilderClassMethod(boolean isDelegatingClass) {
        MethodSpec.Builder toMongo;
        String methodName;
        String params = "";
        if (hasBuilder) {
            params = "builder";
            toMongo = MethodSpec.methodBuilder(methodName="initializeDriverBuilderClass")
                    .addJavadoc("@param builder MongoDB driver builder\n@hidden")
                    .addParameter(ClassName.bestGuess(classDoc.qualifiedTypeName() + ".Builder"), "builder")
                    .addModifiers(Modifier.PUBLIC);
        } else {
            toMongo = MethodSpec.methodBuilder(methodName="toDriverClass")
                    .addJavadoc("@return MongoDB driver object\n@hidden")
                    .addModifiers(Modifier.PUBLIC)
                    .returns(ClassName.bestGuess(classDoc.qualifiedTypeName()));
        }
        if (isDelegatingClass) {
            toMongo.addStatement((params.isEmpty() ? "return " : "") + ("this.serializer." + methodName + "(" + params + ")"));
        } else {
            List<Option> requiredOptions = optionsByName.values().stream().filter(it -> it.mandatory).collect(Collectors.toList());
            StringJoiner ctorParams = new StringJoiner(", ");
            for (Option option : requiredOptions) {
                toMongo
                        .beginControlFlow("if (this." + option.name + " == null)")
                        .addStatement("throw new IllegalArgumentException($S)", option.name + " is mandatory")
                        .endControlFlow();
                if (option.withTimeUnit || option.type.toMongoEnabledType)
                    throw new IllegalStateException("not implemented");
                ctorParams.add("this." +option.name);
            }
            if (hasBuilder) {
                configurableName = "builder";
            } else {
                configurableName = "result";
                toMongo.addStatement("$T result = new $T(" + ctorParams + ")", ClassName.bestGuess(classDoc.qualifiedTypeName()), ClassName.bestGuess(classDoc.qualifiedTypeName()));
            }
        }
        return toMongo;
    }

    protected void addOptionToMongoBuilder(MethodSpec.Builder toMongoBuilder, Option option) {
        if (option.mandatory)
            return;
        toMongoBuilder.beginControlFlow("if (this." + option.name + " != null)");
        if (option.withTimeUnit) {
            toMongoBuilder.addStatement(configurableName + "." + option.mongoSetterName + "(this." + option.name + ", $T.MILLISECONDS)", TimeUnit.class);
        } else if (option.isBlock) {
            toMongoBuilder.addStatement(configurableName + "." + option.mongoSetterName + "(_builder -> " + option.name + ".initializeDriverBuilderClass(_builder))");
        } else if (option.type.toMongoEnabledType) {
            toMongoBuilder.addStatement(configurableName + "." + option.mongoSetterName + "(this." + option.name + ".toDriverClass())");
        } else if (option.type.mapper != null) {
            toMongoBuilder.addStatement(option.type.mapper.asStatementFromExpression(configurableName + "." + option.mongoSetterName + "(%s)", "this." + option.name));
        } else {
            toMongoBuilder.addStatement(configurableName + "." + option.mongoSetterName + "(this." + option.name + ")");
        }
        toMongoBuilder.endControlFlow();
    }

    protected MethodSpec finalizeToDriverClassMethod(MethodSpec.Builder toMongoBuilder) {
        if (!hasBuilder)
            toMongoBuilder.addStatement("return result");
        return toMongoBuilder.build();
    }

    protected void inflateOptionType(TypeSpec.Builder type, boolean hasSerializer, boolean isSerializer, String delegateClassName) {
        boolean isDelegatingClass = hasSerializer && !isSerializer;
        if (isDataObject) {
            generatePackageInfoForPackage = currentlyGeneratedPackageName;
            AnnotationSpec.Builder annotationBuilder = AnnotationSpec.builder(DataObject.class);
            if (!isDelegatingClass)
                annotationBuilder.addMember("generateConverter", CodeBlock.of("true"));
            type.addAnnotation(annotationBuilder.build());
            type.addMethod(MethodSpec.constructorBuilder()
                    .addModifiers(Modifier.PUBLIC).build());
            MethodSpec.Builder jsonCtorBuilder = MethodSpec.constructorBuilder()
                    .addModifiers(Modifier.PUBLIC)
                    .addParameter(ClassName.get(JsonObject.class), "json");
            if (isDelegatingClass)
                jsonCtorBuilder.addStatement("serializer = new $T(json)", ClassName.bestGuess(delegateClassName));
            else
                jsonCtorBuilder.addStatement("$T.fromJson(json, this)", ClassName.bestGuess(currentlyGeneratedTypeName + "Converter"));
            type.addMethod(jsonCtorBuilder.build());
            MethodSpec.Builder toJsonMethodBuilder = MethodSpec.methodBuilder("toJson")
                    .addModifiers(Modifier.PUBLIC)
                    .returns(ClassName.get(JsonObject.class));
            if (isDelegatingClass) {
                toJsonMethodBuilder.addStatement("return serializer.toJson()");
            } else {
                toJsonMethodBuilder
                        .addStatement("$T result = new $T()", ClassName.get(JsonObject.class), ClassName.get(JsonObject.class))
                        .addStatement("$T.toJson(this, result)", ClassName.bestGuess(currentlyGeneratedTypeName + "Converter"))
                        .addStatement("return result");
            }
            type.addMethod(toJsonMethodBuilder.build());
        }
        MethodSpec.Builder toDriverClassOrInitDriverBuilderClassMethod = toDriverClassOrInitDriverBuilderClassMethod(isDelegatingClass);
        if (hasBuilder) {
            MethodSpec.Builder toMongo2 = MethodSpec.methodBuilder("toDriverClass")
                    .addModifiers(Modifier.PUBLIC)
                    .returns(ClassName.bestGuess(classDoc.qualifiedTypeName()));
            if (!isSerializer)
                toMongo2.addJavadoc("@return MongoDB driver object\n@hidden");
            if (isDelegatingClass) {
                toMongo2.addStatement("return this.serializer.toDriverClass()");
            } else {
                StringJoiner ctorParams = new StringJoiner(", ");
                List<Option> requiredOptions = optionsByName.values().stream().filter(it -> it.mandatory).collect(Collectors.toList());
                for (Option option : requiredOptions) {
                    toMongo2
                            .beginControlFlow("if (this." + option.name + " == null)")
                            .addStatement("throw new IllegalArgumentException($S)", option.name + " is mandatory")
                            .endControlFlow();
                    if (option.withTimeUnit || option.type.toMongoEnabledType)
                        throw new IllegalStateException("not implemented");
                    ctorParams.add("this." +option.name);
                }
                toMongo2.addStatement("$T builder = $T.builder(" + ctorParams + ")", ClassName.bestGuess(classDoc.qualifiedTypeName()+".Builder"), ClassName.bestGuess(classDoc.qualifiedTypeName()));
                toMongo2.addStatement("initializeDriverBuilderClass(builder)");
                toMongo2.addStatement("return builder.build()");
            }
            type.addMethod(toMongo2.build());
        }
        if (isDelegatingClass) {
            ClassName type1 = ClassName.bestGuess(delegateClassName);
            FieldSpec.Builder fieldBuilder = FieldSpec.builder(type1, "serializer")
                    .initializer("new $T()", type1)
                    .addModifiers(Modifier.PRIVATE);
            type.addField(fieldBuilder.build());
        }
        for (Option option: optionsByName.values()) {
            if (!isDelegatingClass) {
                if (!isResultOnlyOptions)
                    addOptionToMongoBuilder(toDriverClassOrInitDriverBuilderClassMethod, option);
                // write field
                FieldSpec.Builder fieldBuilder = FieldSpec.builder(option.type.vertxType, option.name).addModifiers(Modifier.PRIVATE);
                if (option.mongoJavadoc != null && !isSerializer) {
                    // FIXME javadoc hack
                    option.mongoJavadoc = option.mongoJavadoc.replace("hint(Bson)", "setHint(JsonObject)");
                    Optional<String> firstParam = Arrays.stream(option.mongoJavadoc.split("\n+")).filter(it -> it.contains("@param")).findFirst();
                    if (firstParam.isPresent()) {
                        String paramLine = firstParam.get();
                        paramLine = paramLine.substring(paramLine.indexOf("@param") + 6).trim();
                        paramLine = paramLine.substring(paramLine.indexOf(' ') + 1);
                        fieldBuilder.addJavadoc(CodeBlock.of(paramLine));
                    }
                }
                type.addField(fieldBuilder.build());
            }
            boolean internalName = option.type.serializerType != null && isSerializer;
            if (option.mongoSetterName != null) {
                // TODO sometimes the setter name is not really good (block)
                {
                    MethodSpec.Builder setterBuilder = optionSetterBuilder(option, !isSerializer, internalName)
                            .addParameter(option.type.vertxType, option.setterParamName);
                    if (isDelegatingClass) {
                        setterBuilder.addStatement("this.serializer." + optionSetterName(option, option.type.serializerType != null) + "(" + option.setterParamName + ")");
                    } else {
                        setterBuilder.addStatement("this." + option.name + " = " + option.setterParamName);
                    }
                    setterBuilder.addStatement("return this");
                    type.addMethod(setterBuilder.build());
                }
                if (internalName) {
                    MethodSpec.Builder setterBuilder = optionSetterBuilder(option, false, false)
                            .addParameter(option.type.serializedOptionType, option.setterParamName);
                    if (option.type.serializedContainerClassName == null)
                        setterBuilder.addStatement(String.format("this.%s = %s == null ? null : %s.getValue()", option.name, option.setterParamName, option.setterParamName));
                    else
                        setterBuilder.addStatement(String.format("this.%s = $T.fromSerializerList(%s)", option.name, option.setterParamName), ClassName.get(OptionSerializer.class));
                    setterBuilder.addStatement("return this");
                    type.addMethod(setterBuilder.build());
                }
            }
            {
                MethodSpec.Builder getterBuilder = optionGetterBuilder(option, !isSerializer, internalName)
                        .returns(option.type.vertxType);
                if (isDelegatingClass) {
                    getterBuilder.addStatement("return this.serializer." + optionGetterName(option, option.type.serializerType != null) + "()");
                } else {
                    getterBuilder.addStatement("return " + option.name);
                }
                type.addMethod(getterBuilder.build());
            }
            if (internalName) {
                MethodSpec.Builder getterBuilder = optionGetterBuilder(option, false, false)
                        .returns(option.type.serializedOptionType);
                if (option.type.serializedContainerClassName == null)
                    getterBuilder.addStatement(String.format("return this.%s == null ? null : new $T(this.%s)", option.name, option.name), option.type.serializerType);
                else
                    getterBuilder.addStatement(String.format("return $T.toSerializerList(this.%s, $T.class, $T.class)", option.name),
                            ClassName.get(OptionSerializer.class),
                            option.type.serializerType,
                            option.type.serializedType
                    );
                type.addMethod(getterBuilder.build());
            }

        }
        if (isDelegatingClass)
            type.addMethod(toDriverClassOrInitDriverBuilderClassMethod.build());
        else if (!isResultOnlyOptions)
            type.addMethod(finalizeToDriverClassMethod(toDriverClassOrInitDriverBuilderClassMethod));
    }

    private String optionGetterName(Option option, boolean internalName) {
        boolean isBoolean = option.type.vertxType.toString().toLowerCase().contains("boolean");
        String prefix = internalName ? "__" : "";
        String getterBaseName = Character.toUpperCase(option.name.charAt(0)) + option.name.substring(1);
        return prefix + (isBoolean ? "is" : "get") + getterBaseName;
    }

    private MethodSpec.Builder optionGetterBuilder(Option option, boolean addJavadoc, boolean isInternalOfSerializer) {
        String getterName = optionGetterName(option, isInternalOfSerializer);
        MethodSpec.Builder getterBuilder = MethodSpec.methodBuilder(getterName)
                .addModifiers(Modifier.PUBLIC);
        if (resultBean) {
            getterBuilder.beginControlFlow("if (" + option.name + "Exception != null) ");
            getterBuilder.addStatement("throw new RuntimeException(" + option.name + "Exception)");
            getterBuilder.endControlFlow();
        }
        if (option.deprecated)
            getterBuilder.addAnnotation(Deprecated.class);
        if (option.mongoGetterJavadoc != null && addJavadoc) {
            if (option.withTimeUnit) {
                String[] split = option.mongoGetterJavadoc.split("\n");
                StringJoiner joiner = new StringJoiner("\n");
                for (String line : split) {
                    if (line.contains("@param"))
                        continue;
                    if (option.withTimeUnit && line.contains("@return") && line.contains("in the given time unit"))
                        line = line.replace("in the given time unit", "(in milliseconds)");
                    joiner.add(line);
                }
                    getterBuilder.addJavadoc(sanitizeJavadoc(joiner.toString()));
            } else {
                    getterBuilder.addJavadoc(sanitizeJavadoc(option.mongoGetterJavadoc));
            }
        }
        return getterBuilder;
    }

    protected String optionSetterName(Option option, boolean internalName) {
        String prefix = internalName ? "__" : "";
        return prefix + "set" + option.name.substring(0, 1).toUpperCase() + option.name.substring(1);
    }


    private MethodSpec.Builder optionSetterBuilder(Option option, boolean addJavadoc, boolean internalName) {
        MethodSpec.Builder setterBuilder = MethodSpec.methodBuilder(optionSetterName(option, internalName))
                .addModifiers(Modifier.PUBLIC)
//                .returns(currentlyGeneratedTypeName != null ? currentlyGeneratedTypeName : ClassName.bestGuess(getTargetQualifiedClassName()));
                .returns(currentlyGeneratedTypeName);
        if (option.mongoJavadoc != null && addJavadoc) {
            if (option.withTimeUnit) {
                String[] split = option.mongoJavadoc.split("\n");
                boolean paramFound = false;
                StringJoiner joiner = new StringJoiner("\n");
                for (String line : split) {
                    if (!paramFound) {
                        paramFound = line.contains("@param");
                        if (paramFound)
                            line += " (in milliseconds)";
                        joiner.add(line);
                    } else if (!line.contains("@param")) {
                        joiner.add(line);
                    }
                }
                setterBuilder.addJavadoc(sanitizeJavadoc(joiner.toString()));
            } else {
                setterBuilder.addJavadoc(sanitizeJavadoc(option.mongoJavadoc));
            }
        }
        if (option.deprecated)
            setterBuilder.addAnnotation(Deprecated.class);
        return setterBuilder;
    }

    protected static class Option {
        public boolean deprecated;
        public String mongoGetterName;
        public ActualType type;
        String name;
        String setterParamName;
        String mongoSetterName;
        String mongoJavadoc;
        String mongoGetterJavadoc;
        boolean mandatory = false;
        boolean withTimeUnit = false;
        boolean isBlock = false;
    }
}
