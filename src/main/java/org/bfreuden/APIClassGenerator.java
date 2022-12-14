package org.bfreuden;

import com.google.common.collect.Lists;
import com.mongodb.connection.ClusterSettings;
import com.mongodb.event.ClusterListener;
import com.mongodb.reactivestreams.client.ClientSession;
import com.squareup.javapoet.*;
import com.sun.javadoc.*;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.apache.commons.io.FileUtils;
import org.bfreuden.mappers.*;
import org.bson.BsonValue;
import org.reactivestreams.Publisher;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

public abstract class APIClassGenerator {


    protected final InspectionContext context;
    protected final ClassDoc classDoc;
    protected TypeName fluentReturnType;
    protected TypeName superClass;
    protected final List<MongoMethod> methods =  new ArrayList<>();
    protected final List<MongoMethod> constructors = new ArrayList<>();
    protected LinkedHashMap<String, OptionsAPIClassGenerator.Option> optionsByName = new LinkedHashMap<>();
    protected Set<String> staticImports = new HashSet<>();
    protected String generatePackageInfoForPackage = null;
    protected boolean isDataObject;
    protected boolean publisherOption;

    public APIClassGenerator(InspectionContext context, ClassDoc classDoc) {
        this.context = context;
        this.classDoc = classDoc;
    }

    protected abstract List<JavaFile.Builder> getJavaFiles();

    public void generate(File genSourceDir) throws IOException {
        analyzeClass();
        List<JavaFile.Builder> javaFiles = getJavaFiles();
        for (JavaFile.Builder javaFile : javaFiles) {
            javaFile.addFileComment(Copyright.COPYRIGHT);
            javaFile.build().writeTo(genSourceDir);
        }
        if (generatePackageInfoForPackage != null) {
            String targetPackage = generatePackageInfoForPackage.replace('.', '/');
            File packageInfo = new File(genSourceDir, targetPackage + "/package-info.java");
            if (!packageInfo.exists()) {
                packageInfo.getParentFile().mkdirs();
                FileUtils.writeLines(packageInfo, Lists.newArrayList(
                        "@ModuleGen(name = \"" + generatePackageInfoForPackage.replace('.', '-') + "\", groupPackage = \"" + generatePackageInfoForPackage + "\")",
                        "package " + generatePackageInfoForPackage + ";",
                        "",
                        "import io.vertx.codegen.annotations.ModuleGen;"
                ));
            }
        }
    }

    protected abstract void analyzeClass();

    private ActualType getActualType2(ExecutableMemberDoc methodDoc, String name, Type type, TypeLocation location, List<ActualType> argumentOfParameterizedType) {
        if (type.isPrimitive()) {
            try {
                Field field = TypeName.class.getField(type.toString().toUpperCase());
                return ActualType.fromTypeName((TypeName) field.get(null));
            } catch (NoSuchFieldException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        } else if (type instanceof ParameterizedType) {
            ParameterizedType ctype = (ParameterizedType) type;
            Type[] typeArguments = ctype.typeArguments();
            if (typeArguments == null || typeArguments.length == 0)
                throw new IllegalStateException("parameterized type without argument");
            List<TypeName> mongoTypeArguments = new ArrayList<>();
            List<TypeName> vertxTypeArguments = new ArrayList<>();
            List<ActualType> argumentTypes = new ArrayList<>();
            for (Type typeArgument : typeArguments) {
                ActualType argumentType = getActualType2(methodDoc, name, typeArgument, location, null);
                argumentTypes.add(argumentType);
                mongoTypeArguments.add(argumentType.mongoType);
                vertxTypeArguments.add(argumentType.vertxType);
            }
            ActualType containingType = getActualType2(methodDoc, name, ctype.asClassDoc(), location, argumentTypes);
            if (containingType.mapper instanceof ToReactiveImplMapperGenerator ||
                    containingType.mapper instanceof ToDriverClassMapperGenerator ||
                    containingType.mapper instanceof FromDriverClassMapperGenerator
            ) {
                containingType.mapper.setGeneric(true);
            } else if (containingType.mapper instanceof CollectionMapperGenerator) {
                if (argumentTypes.get(0).serializerType != null) {
                    containingType.serializerType = argumentTypes.get(0).serializerType;
                    containingType.serializedType = argumentTypes.get(0).serializedType;
                    containingType.serializedContainerClassName = (ClassName) containingType.mongoType;
                    containingType.serializedOptionType = ParameterizedTypeName.get(containingType.serializedContainerClassName, containingType.serializerType);
                }
                MapperGenerator itemMapper = argumentTypes.get(0).mapper;
                if (itemMapper == null)
                    containingType.mapper = null;
                else
                    ((CollectionMapperGenerator) containingType.mapper).setItemMapper(itemMapper);
            } else if (containingType.mapper instanceof MapMapperGenerator) {
                MapperGenerator keyMapper = argumentTypes.get(0).mapper;
                MapperGenerator valueMapper = argumentTypes.get(1).mapper;
                if (keyMapper == null && valueMapper == null) {
                    containingType.mapper = null;
                } else {
                    ((MapMapperGenerator)containingType.mapper).setKeyMapper(keyMapper);
                    ((MapMapperGenerator)containingType.mapper).setValueMapper(valueMapper);
                }
            }
            containingType.mongoType = ParameterizedTypeName.get((ClassName)containingType.mongoType, mongoTypeArguments.toArray(new TypeName[0]));
            if (!containingType.isPublisher) {
                containingType.vertxType = ParameterizedTypeName.get((ClassName)containingType.vertxType, vertxTypeArguments.toArray(new TypeName[0]));
            } else {
                // delayed initialization of the publisher
                if (mongoTypeArguments.size() != 1 || vertxTypeArguments.size() != 1)
                    throw new IllegalStateException("not the expected number of arguments for a publisher");
                initializePublisherFields(methodDoc, mongoTypeArguments.get(0), argumentTypes.get(0), containingType);
            }
            return containingType;
        } else if (type instanceof ClassDoc) {
            ClassDoc ctype = (ClassDoc)type;
            String qualifiedTypeName = ctype.qualifiedTypeName();

            if (Types.isIgnored(qualifiedTypeName)) {
                System.out.println("WARNING: ignored method because return type is ignored: " + methodDoc);
                throw new RuntimeException("@ignored type@");
            }
            if (context.excludedApiClasses.contains(qualifiedTypeName))
                throw new IllegalStateException("@unsupported class@ " + qualifiedTypeName);
            if (Types.isAcceptedAsIsButNeedsSerializer(qualifiedTypeName)) {
                ActualType result = ActualType.fromTypeName(ClassName.bestGuess(qualifiedTypeName));
                result.serializedType = result.mongoType;
                result.serializerType = ClassName.bestGuess(mapToSerializer(qualifiedTypeName));
                result.serializedOptionType = result.serializerType;
                return result;
            }
            if (Types.isAcceptedAsIs(qualifiedTypeName)) {
                ActualType result = ActualType.fromTypeName(ClassName.bestGuess(qualifiedTypeName));
                if (qualifiedTypeName.equals(List.class.getName()) || qualifiedTypeName.equals(Set.class.getName()))
                    result.mapper = new CollectionMapperGenerator();
                else if (qualifiedTypeName.equals(Map.class.getName()))
                    result.mapper = new MapMapperGenerator();
                return result;
            }

            TypeName mapped2 = Types.getMapped2(qualifiedTypeName);
            if (mapped2 != null) {
                ActualType result = ActualType.fromMappedTypeName(ClassName.bestGuess(qualifiedTypeName), mapped2);
                String conversionMethod;
                if (location == TypeLocation.PARAMETER)
                    conversionMethod = context.conversionUtilsGenerator.addConversion(result.vertxType, result.mongoType);
                else
                    conversionMethod = context.conversionUtilsGenerator.addConversion(result.mongoType, result.vertxType);
                result.mapper = new ConversionUtilsMapperGenerator(conversionMethod);
                return result;
            }

            if (qualifiedTypeName.equals(Publisher.class.getName())) {
                ActualType actualType = ActualType.fromTypeName(ClassName.get(Publisher.class));
                actualType.isPublisher = true;
                actualType.publisherClassName = ClassName.get(Publisher.class);
                return actualType;
            }
            if (context.classDocs.containsKey(qualifiedTypeName)) {
                if (context.enumApiClasses.contains(qualifiedTypeName)) {
                    return ActualType.fromTypeName(ClassName.bestGuess(qualifiedTypeName));
                } else {
                    ActualType actualType = ActualType.fromMappedTypeName(ClassName.bestGuess(qualifiedTypeName), ClassName.bestGuess(mapPackageName(qualifiedTypeName)));
                    boolean isReactiveApiClass = context.reactiveApiClasses.contains(qualifiedTypeName);
                    if (context.resultApiClasses.contains(qualifiedTypeName) || isReactiveApiClass)
                        actualType.toMongoEnabledType = true;
                    // look for a publisher
                    Type[] interfaces = ctype.interfaceTypes();
                    if (interfaces != null) {
                        for (Type interf : interfaces) {
                            if (interf.qualifiedTypeName().equals(Publisher.class.getName())) {
                                actualType.isPublisher = true;
                                actualType.publisherClassName = ClassName.bestGuess(qualifiedTypeName);
                                ParameterizedType parameterizedInterfaceType = interf.asParameterizedType();
                                if (parameterizedInterfaceType == null)
                                    throw new IllegalStateException("publisher interface not parametrized");
                                Type[] publishedTypes = parameterizedInterfaceType.typeArguments();
                                if (publishedTypes == null || publishedTypes.length != 1)
                                    throw new IllegalStateException("publisher interface must have a single parameter");
                                // HACK for ListDatabasePublisher (?)
                                if ((publishedTypes[0] instanceof TypeVariable))
                                    break;
                                // HACK for ChangeStreamPublisher
                                if (publishedTypes[0] instanceof ParameterizedType) {
                                    if (((ParameterizedType)publishedTypes[0]).typeArguments()[0] instanceof TypeVariable && argumentOfParameterizedType.size() == 1) {
                                        ActualType publishedType = getActualType2(methodDoc, name, publishedTypes[0].asClassDoc(), location, null);
                                        publishedType.vertxType = ParameterizedTypeName.get((ClassName)publishedType.vertxType, argumentOfParameterizedType.get(0).vertxType);
                                        publishedType.mongoType = ParameterizedTypeName.get((ClassName)publishedType.mongoType, argumentOfParameterizedType.get(0).mongoType);
                                        actualType.publishedType = publishedType;
                                        break;
                                    }
                                }
                                actualType.publishedType = getActualType2(methodDoc, name, publishedTypes[0], location, null);
                                initializePublisherFields(methodDoc, actualType.publishedType.mongoType, null, actualType);
                                return actualType;

                            }
                        }
                    }
                    if (location == TypeLocation.RETURN && context.reactiveApiClasses.contains(qualifiedTypeName))
                        actualType.mapper = new ToReactiveImplMapperGenerator(ClassName.bestGuess(mapPackageName(qualifiedTypeName, true)));
                    else if (location == TypeLocation.RETURN) {
                        actualType.mapper = new FromDriverClassMapperGenerator(actualType.vertxType);
                    } else {
                        boolean needsInputMapper = argumentOfParameterizedType != null && !argumentOfParameterizedType.isEmpty();
                        actualType.mapper = new ToDriverClassMapperGenerator(needsInputMapper);
                    }
                    return actualType;
                }
            } else {
                throw new IllegalStateException("@unsupported class@: " + qualifiedTypeName);
            }
        } else if (type.getClass().getName().contains("ArrayType")) {
            ActualType actualType2 = getActualType2(methodDoc, name, type.asClassDoc(), location, null);
            actualType2.mongoType = ArrayTypeName.of(actualType2.mongoType);
            actualType2.vertxType = ArrayTypeName.of(actualType2.vertxType);
            return actualType2;
        } else if (type instanceof TypeVariable) {
            TypeVariable ctype = (TypeVariable)type;
            return ActualType.fromTypeName(TypeVariableName.get(ctype.typeName()));
        } else if (type instanceof WildcardType) {
            WildcardType wtype = (WildcardType) type;
            Type[] extendsTypes = wtype.extendsBounds();
            Type[] superTypes = wtype.superBounds();
            if (extendsTypes != null && extendsTypes.length > 0 && superTypes != null && superTypes.length > 0)
                throw new IllegalStateException("wildcard with many extends and super type: " + wtype);
            if (extendsTypes != null) {
                if (extendsTypes.length > 1)
                    throw new IllegalStateException("wildcard with many extends type: " + wtype);
                for (Type extendsType: extendsTypes) {
                    ActualType containingType = getActualType2(methodDoc, name, extendsType, location, null);
                    containingType.mongoType = WildcardTypeName.subtypeOf(containingType.mongoType);
                    if (!containingType.vertxType.toString().equals(JsonObject.class.getName()) &&
                        !containingType.vertxType.toString().equals(JsonArray.class.getName())
                    )
                        containingType.vertxType = WildcardTypeName.subtypeOf(containingType.vertxType);
                    return containingType;
                }
            }
            if (superTypes != null) {
                if (superTypes.length > 1)
                    throw new IllegalStateException("wildcard with many super type: " + wtype);
                for (Type superType: superTypes) {
                    ActualType containingType = getActualType2(methodDoc, name, superType, location, null);
                    containingType.mongoType = WildcardTypeName.supertypeOf(containingType.mongoType);
                    if (!containingType.vertxType.toString().equals(JsonObject.class.getName()) &&
                        !containingType.vertxType.toString().equals(JsonArray.class.getName())
                    )
                        containingType.vertxType = WildcardTypeName.supertypeOf(containingType.vertxType);
                    return containingType;
                }
            }
            throw new IllegalStateException("wildcard with no extends or super types: " + wtype);
        } else {
            throw new IllegalStateException("unsupported javadoc type: " + type.getClass().getName());
        }
    }

    private void initializePublisherFields(ExecutableMemberDoc methodDoc, TypeName mongoPublishedType, ActualType publishedType, ActualType containingType) {
        boolean isGridFSDownloadPublisher = methodDoc instanceof MethodDoc && ((MethodDoc)methodDoc).returnType().typeName().equals("GridFSDownloadPublisher");
        // check single publisher
        if (mongoPublishedType.toString().equals("com.mongodb.reactivestreams.client.Success") || mongoPublishedType.toString().equals(Void.class.getName())) {
            containingType.singlePublisher = true;
            containingType.publishedType = ActualType.fromMappedTypeName(mongoPublishedType, ClassName.get(Void.class));
        }
        if (methodDoc.name().equals("runCommand"))
            containingType.singlePublisher = true;
        if (!containingType.singlePublisher) {
            String rawCommentText = methodDoc.getRawCommentText();
            Optional<String> first = Arrays.stream(rawCommentText.split("\\n+")).filter(it -> it.contains("@return")).findFirst();
            if (first.isPresent()) {
                String returnSpec = first.get();
                containingType.singlePublisher = returnSpec.contains("empty publisher") ||  returnSpec.contains("single element") || returnSpec.contains("a publisher for the") || returnSpec.contains("a Publisher containing the");
            } else {
                throw new IllegalStateException("no @return: can't detect single publisher");
            }
        }
        // check published type
        if (containingType.publishedType == null)
            containingType.publishedType = publishedType;
        // initialize output vertxType
        if (isGridFSDownloadPublisher) {
            containingType.singlePublisher = false;
            containingType.publishedType.mapper = null;
        }
        if (!containingType.singlePublisher) {
            String publisherClassName = containingType.publisherClassName.toString();
            if (publisherClassName.equals(Publisher.class.getName())) {
                containingType.vertxType = ParameterizedTypeName.get(ClassName.bestGuess("io.vertx.mongo.MongoResult"), containingType.publishedType.vertxType);
            } else {
                InspectionContext.PublisherDesc publisherDesc = context.publisherDescriptions.get(publisherClassName);
                if (publisherDesc == null)
                    throw new IllegalStateException("no such publisher in context: " + publisherClassName);
                if (publisherDesc.toCollectionMethodName != null)
                    containingType.vertxType = ParameterizedTypeName.get(ClassName.bestGuess("io.vertx.mongo.MongoCollectionResult"), containingType.publishedType.vertxType);
                else if (isGridFSDownloadPublisher)
                    containingType.vertxType = ClassName.bestGuess("io.vertx.mongo.client.gridfs.GridFSDownloadResult");
                else
                    containingType.vertxType = ParameterizedTypeName.get(ClassName.bestGuess("io.vertx.mongo.MongoResult"), containingType.publishedType.vertxType);
            }
        } else {
            containingType.vertxType = ParameterizedTypeName.get(ClassName.get(Future.class), containingType.publishedType.vertxType);
        }
    }


    protected ActualType getActualType(ExecutableMemberDoc methodDoc, String name, Type type, TypeLocation location) {
        try {
            ActualType result = getActualType2(methodDoc, name, type, location, null);
            // List of JsonObjects are converted to JsonArray when passed as a parameter
            if (location == TypeLocation.PARAMETER && result.vertxType.toString().equals(ParameterizedTypeName.get(List.class, JsonObject.class).toString())) {
                result.vertxType = ClassName.get(JsonArray.class);
                String conversionMethod = context.conversionUtilsGenerator.addConversion(result.vertxType, result.mongoType);
                result.mapper = new ConversionUtilsMapperGenerator(conversionMethod);
            }
            return result;
        } catch (RuntimeException ex) {
            if ("@ignored type@".equals(ex.getMessage()))
                return null;
            else
                throw new RuntimeException(ex.getMessage(), ex);
        }
    }

    protected boolean isSupportedSuperClass(String superClassName) {
        if (Types.isSupportedSuperClass(superClassName))
            return true;
        return false;
//        return context.classDocs.containsKey(superClassName) && !context.otherApiClasses.contains(superClassName);
    }

    protected String getTargetQualifiedClassName() {
        return mapPackageName(classDoc.containingPackage().name()) + "." + getTargetClassName();
    }

    protected String getTargetClassName() {
        return classDoc.typeName();
    }

    protected String getTargetPackage() {
        String packageName = classDoc.containingPackage().name();
        return mapPackageName(packageName);
    }

    protected String mapPackageName(String packageNameOrClassName, boolean... addImpl) {
        if (packageNameOrClassName.startsWith("com.mongodb.reactivestreams")) {
            packageNameOrClassName = packageNameOrClassName.replace("com.mongodb.reactivestreams", "io.vertx.mongo");
        } else if (packageNameOrClassName.startsWith("com.mongodb")) {
            packageNameOrClassName = packageNameOrClassName.replace("com.mongodb", "io.vertx.mongo");
        } else {
            throw new IllegalArgumentException();
        }
        if (addImpl.length > 0 && addImpl[0]) {
            int index = packageNameOrClassName.lastIndexOf('.');
            String packageName = packageNameOrClassName.substring(0, index + 1);
            String last = packageNameOrClassName.substring(index + 1);
            if (Character.isUpperCase(last.charAt(0)))
                packageNameOrClassName = packageName + "impl." + last + "Impl";
            else
                packageNameOrClassName = packageName + "impl";
        }
        return packageNameOrClassName;
    }
    protected String mapToImpl(String packageNameOrClassName) {
        return mapToImplSuffix(packageNameOrClassName, "Impl");
    }

    protected String mapToSerializer(String packageNameOrClassName) {
        String suffix = "Serializer";
        return mapToImplSuffix(mapPackageName(packageNameOrClassName), suffix);
    }

    private static String mapToImplSuffix(String packageNameOrClassName, String suffix) {
        int index = packageNameOrClassName.lastIndexOf('.');
        String packageName = packageNameOrClassName.substring(0, index + 1);
        String last = packageNameOrClassName.substring(index + 1);
        if (Character.isUpperCase(last.charAt(0)))
            packageNameOrClassName = packageName + "impl." + last + suffix;
        else
            packageNameOrClassName = packageName + "impl";
        return packageNameOrClassName;
    }

    protected void addStaticImports(JavaFile.Builder builder) {
        for (String staticImport : staticImports) {
            int index = staticImport.lastIndexOf('.');
            String className = staticImport.substring(0, index);
            String methodName = staticImport.substring(index + 1);
            builder.addStaticImport(ClassName.bestGuess(className), methodName);
        }
    }

    private String sanitizeJavadocLine(String line) {
        // TODO hack
        line = line
                .replaceAll("\\{@link ([A-Za-z]+).Builder}", "{@link $1}")
                .replace("{@link ObjectId}", String.format("{@link %s}", org.bson.types.ObjectId.class.getName()))
                .replace("{@link BsonValue}", String.format("{@link %s}", BsonValue.class.getName()))
                .replace("{@link ClusterSettings}", String.format("{@link %s}", mapPackageName(ClusterSettings.class.getName())))
                .replace("@see ClientSession", String.format("@see %s", mapPackageName(ClientSession.class.getName())))
                .replace("{@link ClusterListener}", String.format("{@link %s}", ClusterListener.class.getName()))
                .replace("@see ClusterSettings.", String.format("@see %s.", ClusterSettings.class.getName()))
                .replace("#getRetryWrites", "#isRetryWrites")
                .replace("#getRetryReads", "#isRetryReads")
                .replace("$", "$$");
        return line;
    }

    protected String sanitizeJavadoc(String javadoc) {
        return this.sanitizeJavadocLine(javadoc);
    }

    enum TypeLocation {
        PARAMETER,
        RETURN
    }

    static class ActualType {
        public MapperGenerator mapper;
        public boolean isNullable;
        public boolean isBinaryReadStream;
        public boolean isBinaryWriteStream;
        public TypeName serializerType;
        public ClassName serializedContainerClassName;
        public TypeName serializedOptionType;
        public TypeName serializedType;
        boolean toMongoEnabledType = false;
        public ActualType publishedType;
        TypeName mongoType;
        TypeName vertxType;
        boolean isPublisher;
        boolean singlePublisher;
        ClassName publisherClassName;
        boolean isReactive;

        private ActualType() {
        }


        static ActualType fromTypeName(TypeName type) {
            ActualType actualType = new ActualType();
            actualType.mongoType = type;
            actualType.vertxType = type;
            return actualType;
        }

        static ActualType fromMappedTypeName(TypeName mongoType, TypeName vertxType) {
            ActualType actualType = new ActualType();
            actualType.mongoType = mongoType;
            actualType.vertxType = vertxType;
            return actualType;
        }

    }

    protected static class MongoMethodParameter {
        public String conversionMethod;
        String name;
        ActualType type;

        TypeName getFullVertxType() {
            return type.vertxType;
        }

        TypeName getFullMongoType() {
            return type.mongoType;
        }
    }

    protected class MongoMethod {
        String signature;
        ArrayList<TypeVariableName> typeVariables = new ArrayList<>();
        String mongoName;
        String vertxName;
        String mongoJavadoc;
        String vertxResultOrFutureJavadoc;
        String vertxAsyncJavadoc;
        String vertxWithOptionsJavadoc;
        String vertxNoStreamJavadoc;
        String vertxNoStreamAsyncJavadoc;
        ActualType returnType;
        List<MongoMethodParameter> params = new ArrayList<>();

        void computeJavadocs() {
            if (mongoJavadoc != null) {
                mongoJavadoc = sanitizeJavadoc(mongoJavadoc);
                mongoJavadoc = mongoJavadoc.replace("the Publisher providing the file data", "the stream providing the file data");
                this.vertxResultOrFutureJavadoc = this.mongoJavadoc;
                if (returnType != null && returnType.isPublisher) {
                    StringJoiner newRawCommentText = new StringJoiner("\n");
                    StringJoiner asyncNewRawCommentText = new StringJoiner("\n");
                    StringJoiner withOptionsNewRawCommentText = new StringJoiner("\n");
                    String replacement = mongoName.equals("watch") ? "read stream" : (returnType.singlePublisher ? "future" : "result");
                    for (String docLine : Arrays.stream(mongoJavadoc.split("\\n+")).collect(Collectors.toList())) {
                        if (docLine.contains("@return")) {
                            String optionsParamName = returnType.publisherClassName.toString().endsWith("GridFSDownloadPublisher") ? "controlOptions" : "options";
                            withOptionsNewRawCommentText.add(docLine.replaceAll("@return.*", "@param " + optionsParamName + " options"));
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
//                            int index = docLine.indexOf("@return") + "@return".length();
//                            String newReturnLine = docLine.substring(0, index) + " <code>this</code>";
//                            asyncNewRawCommentText.add(newReturnLine);
                        } else {
                            newRawCommentText.add(docLine);
                            asyncNewRawCommentText.add(docLine);
                            withOptionsNewRawCommentText.add(docLine);

                        }
                    }
                    this.vertxResultOrFutureJavadoc = newRawCommentText.toString();
                    this.vertxAsyncJavadoc = asyncNewRawCommentText.toString();
                    this.vertxWithOptionsJavadoc = withOptionsNewRawCommentText.toString();
                    this.vertxNoStreamJavadoc = vertxResultOrFutureJavadoc
                            .replaceAll("@param source[^\n]+", "")
                            .replace("@param filename the filename", "@param filename the filename providing the file data");
                    this.vertxNoStreamAsyncJavadoc = vertxAsyncJavadoc
                            .replaceAll("@param source[^\n]+", "")
                            .replace("@param filename the filename", "@param filename the filename providing the file data");
                }
            }
        }
    }

}
