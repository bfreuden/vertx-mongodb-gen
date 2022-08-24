package org.bfreuden;

import com.mongodb.reactivestreams.client.gridfs.GridFSBucket;
import com.squareup.javapoet.*;
import com.sun.javadoc.*;
import io.vertx.codegen.annotations.DataObject;
import org.reactivestreams.Publisher;

import javax.lang.model.element.Modifier;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

public abstract class GenericAPIClassGenerator extends APIClassGenerator {

    protected String classJavadoc;
    protected final ArrayList<TypeVariableName> typeVariables = new ArrayList<>();
    protected boolean currentMethodHasPublisherOptions;
    protected boolean hasBuilder;
    protected String configurableName;
    protected boolean isResultOnlyOptions;

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
            System.out.println("INFO: return type of " + methodDoc + " has been ignored because it has a TResult type parameter");
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

        ActualType actualReturnType = methodReturnType == null ? null : getActualType(methodDoc, null, methodReturnType, TypeLocation.RETURN);
        if (methodReturnType != null && actualReturnType == null) {
            System.out.println("WARNING: return type of " + methodDoc + " is unknown so method has been ignored");
            return null;
        }
        if (actualReturnType != null) {
            mongoMethod.computeActualReturnTypes(context, actualReturnType);
            if (mongoMethod.returnType.mongoType != null && mongoMethod.returnType.mongoType.toString().equals(classDoc.qualifiedTypeName()))
                mongoMethod.returnType.vertxType = this.fluentReturnType;
        }
        if (classDoc.qualifiedTypeName().equals(GridFSBucket.class.getName()) &&
                methodDoc.name().equals("downloadToPublisher")
        ) {
            if (mongoMethod.mongoJavadoc.contains(" id"))
                mongoMethod.vertxName = "downloadByObjectId";
            else
                mongoMethod.vertxName = "downloadByFilename";

        }
        if (methodReturnType != null && mongoMethod.returnType.isPublisher &&
                mongoMethod.returnType.mongoType != null &&
                !context.reactiveApiClasses.contains(mongoMethod.returnType.mongoType.toString()) &&
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

    public void inflateType(
        TypeSpec.Builder type,
        boolean isImpl,
        BiConsumer<MongoMethod, MethodSpec.Builder> ctorCustomizer,
        BiConsumer<MongoMethod, MethodSpec.Builder> methodCustomizer,
        BiConsumer<MongoMethod, MethodSpec.Builder> handlerMethodCustomizer
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
                methodBuilder.addParameter(ParameterSpec.builder(param.vertxType, param.name).build());
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

    protected MethodSpec.Builder toMongoBuilder() {
        MethodSpec.Builder toMongo = MethodSpec.methodBuilder("toDriverClass")
                .addJavadoc("@return MongoDB driver object\n@hidden")
                .addModifiers(Modifier.PUBLIC)
                .returns(ClassName.bestGuess(classDoc.qualifiedTypeName()));
        List<Option> requiredOptions = optionsByName.values().stream().filter(it -> it.mandatory).collect(Collectors.toList());
        StringJoiner ctorParams = new StringJoiner(", ");
        for (Option option : requiredOptions) {
            toMongo
                    .beginControlFlow("if (this." + option.name + " == null)")
                    .addStatement("throw new IllegalArgumentException($S)", option.name + " is mandatory")
                    .endControlFlow();
            if (option.withTimeUnit || option.optionType)
                throw new IllegalStateException("not implemented");
            ctorParams.add("this." +option.name);
        }
        if (hasBuilder) {
            configurableName = "builder";
            toMongo.addStatement("$T builder = $T.builder(" + ctorParams + ")", ClassName.bestGuess(classDoc.qualifiedTypeName()+".Builder"), ClassName.bestGuess(classDoc.qualifiedTypeName()));
        } else {
            configurableName = "result";
            toMongo.addStatement("$T result = new $T(" + ctorParams + ")", ClassName.bestGuess(classDoc.qualifiedTypeName()), ClassName.bestGuess(classDoc.qualifiedTypeName()));
        }
        return toMongo;
    }

    protected void addOptionToMongoBuilder(MethodSpec.Builder toMongoBuilder, Option option) {
        if (option.mandatory)
            return;
        toMongoBuilder.beginControlFlow("if (this." + option.name + " != null)");
        if (option.withTimeUnit) {
            toMongoBuilder.addStatement(configurableName + "." + option.mongoSetterName + "(this." + option.name + ", $T.MILLISECONDS)", TimeUnit.class);
        } else if (option.optionType) {
            toMongoBuilder.addStatement(configurableName + "." + option.mongoSetterName + "(this." + option.name + ".toDriverClass())");
        } else if (option.conversionMethod != null) {
            toMongoBuilder.addStatement(configurableName + "." + option.mongoSetterName + "($T.INSTANCE." + option.conversionMethod + "(this." + option.name + "))", ClassName.bestGuess("io.vertx.mongo.impl.ConversionUtilsImpl"));
        } else {
            toMongoBuilder.addStatement(configurableName + "." + option.mongoSetterName + "(this." + option.name + ")");
        }
        toMongoBuilder.endControlFlow();

    }

    protected MethodSpec toMongo(MethodSpec.Builder toMongoBuilder) {
        if (hasBuilder)
            toMongoBuilder.addStatement("return builder.build()");
        else
            toMongoBuilder.addStatement("return result");
        return toMongoBuilder.build();
    }

    protected void inflateOptionType(TypeSpec.Builder type) {
        type.addAnnotation(AnnotationSpec.builder(DataObject.class).addMember("generateConverter", CodeBlock.of("true")).build());
        MethodSpec.Builder toMongoMethod = toMongoBuilder();
        for (Option option: optionsByName.values()) {
            if (option.name.equals("commitQuorum")) {
                System.out.println("WARNING: ignoring commitQuorum parameter");
                continue;
            }
            if (!isResultOnlyOptions)
                addOptionToMongoBuilder(toMongoMethod, option);
            FieldSpec.Builder fieldBuilder = FieldSpec.builder(option.vertxType, option.name).addModifiers(Modifier.PRIVATE);
            if (option.mongoJavadoc != null) {
                option.mongoJavadoc = option.mongoJavadoc.replace("hint(Bson)", "hint(JsonObject)");
                Optional<String> firstParam = Arrays.stream(option.mongoJavadoc.split("\n+")).filter(it -> it.contains("@param")).findFirst();
                if (firstParam.isPresent()) {
                    String paramLine = firstParam.get();
                    paramLine = paramLine.substring(paramLine.indexOf("@param") + 6).trim();
                    paramLine = paramLine.substring(paramLine.indexOf(' ') + 1);
                    fieldBuilder.addJavadoc(CodeBlock.of(paramLine));
                }
            }
            type.addField(fieldBuilder.build());
            if (option.mongoSetterName != null) {
                MethodSpec.Builder setterBuilder = MethodSpec.methodBuilder(option.name)
                        .addModifiers(Modifier.PUBLIC)
                        .addParameter(option.vertxType, option.setterParamName)
                        .returns(ClassName.bestGuess(getTargetPackage() + "." + getTargetClassName()))
                        .addStatement("return this");
                if (option.mongoJavadoc != null) {
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
                        setterBuilder.addJavadoc(joiner.toString().replace("$", "$$"));
                    } else {
                        setterBuilder.addJavadoc(option.mongoJavadoc.replace("$", "$$"));
                    }
                }
                if (option.deprecated)
                    setterBuilder.addAnnotation(Deprecated.class);
                type.addMethod(setterBuilder.build());
            }
            boolean isBoolean = option.vertxType.toString().toLowerCase().contains("boolean");
            MethodSpec.Builder getterBuilder = MethodSpec.methodBuilder((isBoolean ? "is" : "get") + Character.toUpperCase(option.name.charAt(0)) + option.name.substring(1))
                    .addModifiers(Modifier.PUBLIC)
                    .returns(option.vertxType)
                    .addStatement("return " + option.name);
            if (option.deprecated)
                getterBuilder.addAnnotation(Deprecated.class);
            if (option.mongoGetterJavadoc != null) {
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
                    getterBuilder.addJavadoc(joiner.toString().replace("$", "$$"));
                } else {
                    getterBuilder.addJavadoc(option.mongoGetterJavadoc.replace("$", "$$"));
                }
            }
            type.addMethod(getterBuilder.build());
        }
        if (!isResultOnlyOptions)
            type.addMethod(toMongo(toMongoMethod));
    }

    protected static class Option {
        public boolean deprecated;
        public String conversionMethod;
        public String mongoGetterName;
        String name;
        String setterParamName;
        TypeName mongoType;
        TypeName vertxType;
        String mongoSetterName;
        String mongoJavadoc;
        String mongoGetterJavadoc;
        boolean optionType = false;
        boolean mandatory = false;
        boolean withTimeUnit = false;
    }
}