package org.bfreuden;

import com.mongodb.reactivestreams.client.gridfs.GridFSBucket;
import com.squareup.javapoet.*;
import com.sun.javadoc.*;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.bfreuden.mappers.*;
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
            if (containingType.vertxImplType != null)
                containingType.vertxImplType = ParameterizedTypeName.get((ClassName)containingType.vertxImplType, vertxTypeArguments.toArray(new TypeName[0]));
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
                    actualType.isApiType = true;
                    boolean isReactiveApiClass = context.reactiveApiClasses.contains(qualifiedTypeName);
                    if (context.resultApiClasses.contains(qualifiedTypeName) || isReactiveApiClass)
                        actualType.toMongoEnabledType = true;
                    if (isReactiveApiClass)
                        actualType.vertxImplType = ClassName.bestGuess(mapPackageName(qualifiedTypeName, true));
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
                                        actualType.publishedByInterface = true;
                                        break;
                                    }
                                }
                                actualType.publishedType = getActualType2(methodDoc, name, publishedTypes[0], location, null);
                                actualType.publishedByInterface = true;
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
                        actualType.mapper = new ToDriverClassMapperGenerator();
                    }
                    return actualType;
                }
            } else {
                throw new IllegalStateException("unsupported class: " + qualifiedTypeName);
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
//            result.isNullable = result.vertxType.toString().endsWith("Options");

//            if (result.isApiType || "watch".equals(methodDoc.name())) // FIXME watch
//                return result;
//            if (result.isPublisher) {
//                if (!result.publishedType.vertxType.toString().equals(result.publishedType.mongoType.toString()) && !result.toMongoEnabledType)
//                    result.conversionMethod = context.conversionUtilsGenerator.addConversion(result.publishedType.mongoType, result.publishedType.vertxType);
//            } else {
//                if (!result.vertxType.toString().equals(result.mongoType.toString()) && !result.toMongoEnabledType)
//                    if (location == TypeLocation.RETURN)
//                        result.conversionMethod = context.conversionUtilsGenerator.addConversion(result.mongoType, result.vertxType);
//                    else
//                        result.conversionMethod = context.conversionUtilsGenerator.addConversion(result.vertxType, result.mongoType);
//            }
            return result;
        } catch (RuntimeException ex) {
            if ("@ignored type@".equals(ex.getMessage()))
                return null;
            else
                throw new RuntimeException(ex);
        }
    }
    protected ActualType getActualType1(ExecutableMemberDoc methodDoc, String name, Type type, TypeLocation location) {
        try {
            if (!type.isPrimitive()) {
                String qualifiedTypeName = type.qualifiedTypeName();
                ParameterizedType parameterizedType = type.asParameterizedType();
                if (qualifiedTypeName.equals("TDocument")) {
                    return ActualType.fromTypeName(TypeVariableName.get("TDocument"));
                } else if (qualifiedTypeName.equals("T")) {
                    return ActualType.fromTypeName(TypeVariableName.get("T"));
                } else if (Types.isIgnored(qualifiedTypeName)) {
                    System.out.println("WARNING: ignored method because return type is ignored: " + methodDoc);
                    return null;
                } else if (qualifiedTypeName.startsWith("java.util") && Collection.class.isAssignableFrom(Class.forName(qualifiedTypeName))
                        || qualifiedTypeName.equals(Iterator.class.getName())
                        || qualifiedTypeName.equals(Map.class.getName())
                ) {
                    ArrayList<ActualType> actualTypes = new ArrayList<>();
                    boolean mapped = false;
                    for (Type typeArg : parameterizedType.typeArguments()) {
                        if (typeArg instanceof WildcardType)
                            typeArg = ((WildcardType)typeArg).asClassDoc();
                        ActualType actualType = getActualType(methodDoc, name, typeArg, location);
                        actualTypes.add(actualType);
                        if (!actualType.mongoType.toString().equals(actualType.vertxType.toString()))
                            mapped = true;
                    }
                    ParameterizedTypeName mongoType = ParameterizedTypeName.get(ClassName.bestGuess(qualifiedTypeName), actualTypes.stream().map(it -> it.mongoType).collect(Collectors.toList()).toArray(new TypeName[0]));
                    if (mapped) {
                        ParameterizedTypeName vertxType = ParameterizedTypeName.get(ClassName.bestGuess(qualifiedTypeName), actualTypes.stream().map(it -> it.vertxType).collect(Collectors.toList()).toArray(new TypeName[0]));
                        return ActualType.fromMappedTypeName(mongoType, vertxType);
                    } else {
                        return ActualType.fromTypeName(mongoType);
                    }

                } else if (Types.isKnown(qualifiedTypeName)) {
                    TypeName mappedType = Types.getMapped(qualifiedTypeName);
                    if (parameterizedType != null) {
                        String qualified = parameterizedType.toString();
                        String parameterClassName = qualified.substring(qualified.indexOf('<') + 1, qualified.length() - 1);
                        if (parameterClassName.equals("TDocument")) {
                            return ActualType.fromTypeName(ParameterizedTypeName.get((ClassName) mappedType, TypeVariableName.get("TDocument")));
                        } else if (parameterClassName.equals("? extends TDocument")) {
                            return ActualType.fromTypeName(ParameterizedTypeName.get((ClassName) mappedType, WildcardTypeName.subtypeOf(TypeVariableName.get("TDocument"))));
                        } else {
                            if (Types.isKnown(parameterClassName)) {
                                TypeName mappedParameterClassName = Types.getMapped("? extends org.bson.conversions.Bson");
                                return ActualType.fromMappedTypeName(
                                        ParameterizedTypeName.get((ClassName) mappedType, WildcardTypeName.subtypeOf(ClassName.bestGuess("org.bson.conversions.Bson"))),
                                        ParameterizedTypeName.get((ClassName) mappedType, mappedParameterClassName)
                                );
                            } else {
                                return ActualType.fromTypeName(ParameterizedTypeName.get((ClassName) mappedType, ClassName.bestGuess(parameterClassName)));
                            }
                        }
                    } else {
                        if (mappedType.toString().equals(qualifiedTypeName))
                            return ActualType.fromTypeName(mappedType);
                        else
                            return ActualType.fromMappedTypeName(ClassName.bestGuess(qualifiedTypeName), mappedType);
                    }
                } else if (context.enumApiClasses.contains(qualifiedTypeName)) {
                    return ActualType.fromTypeName(ClassName.bestGuess(qualifiedTypeName));
                } else if (context.reactiveApiClasses.contains(qualifiedTypeName)) {
                    if (parameterizedType != null) {
                        if (parameterizedType.typeArguments().length > 1)
                            throw new IllegalStateException("unsupported number of parameters " + parameterizedType);
                        Type type1 = parameterizedType.typeArguments()[0];
                        TypeName mongoElemType = TypeVariableName.get(type1.typeName());
                        TypeName vertxElemType = mongoElemType;
                        String elemQualifiedTypeName = type1.qualifiedTypeName();
                        if (elemQualifiedTypeName.contains(".")) {
                            if (Types.isKnown(elemQualifiedTypeName))
                                vertxElemType = Types.getMapped(elemQualifiedTypeName);
                            else
                                vertxElemType = ClassName.bestGuess(elemQualifiedTypeName);
                        }
                        return ActualType.fromMappedTypeName(
                                ParameterizedTypeName.get(ClassName.bestGuess(qualifiedTypeName), mongoElemType),
                                ParameterizedTypeName.get(ClassName.bestGuess(mapPackageName(qualifiedTypeName)), vertxElemType)
                        );

                    } else {
                        return ActualType.fromMappedTypeName(ClassName.bestGuess(qualifiedTypeName), ClassName.bestGuess(mapPackageName(qualifiedTypeName)));
                    }
                } else if (context.builderClasses.contains(qualifiedTypeName)) {
                    return ActualType.fromMappedTypeName(ClassName.bestGuess(qualifiedTypeName), ClassName.bestGuess(mapPackageName(qualifiedTypeName)));
                } else if (context.optionsApiClasses.contains(qualifiedTypeName)) {
                    return ActualType.fromMappedTypeName(ClassName.bestGuess(qualifiedTypeName), ClassName.bestGuess(mapPackageName(qualifiedTypeName)));
                } else if (qualifiedTypeName.equals("org.reactivestreams.Publisher")) {
                    return ActualType.fromPublisher(methodDoc, type);
                } else if (context.publishersApiClasses.contains(qualifiedTypeName)) {
                    return ActualType.fromPublisher(methodDoc, type);
                } else if (context.otherApiClasses.contains(qualifiedTypeName)) {
                    return ActualType.fromMappedTypeName(ClassName.bestGuess(qualifiedTypeName), ClassName.bestGuess(mapPackageName(qualifiedTypeName)));
                } else if (context.resultApiClasses.contains(qualifiedTypeName)) {
                    return ActualType.fromMappedTypeName(ClassName.bestGuess(qualifiedTypeName), ClassName.bestGuess(mapPackageName(qualifiedTypeName)));
                } else if (context.modelApiClasses.contains(qualifiedTypeName)) {
                    return ActualType.fromMappedTypeName(ClassName.bestGuess(qualifiedTypeName), ClassName.bestGuess(mapPackageName(qualifiedTypeName)));
                }
                throw new IllegalStateException(qualifiedTypeName);
            } else {
                try {
                    Field field = TypeName.class.getField(type.toString().toUpperCase());
                    return ActualType.fromTypeName((TypeName) field.get(null));
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }

        } catch (ClassNotFoundException ex) {
            throw new IllegalStateException(ex);
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
        int index = packageNameOrClassName.lastIndexOf('.');
        String packageName = packageNameOrClassName.substring(0, index + 1);
        String last = packageNameOrClassName.substring(index + 1);
        if (Character.isUpperCase(last.charAt(0)))
            packageNameOrClassName = packageName + "impl." + last + "Impl";
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

    enum TypeLocation {
        PARAMETER,
        RETURN
    }

    static class ActualType {
        public MapperGenerator mapper;
        public boolean isNullable;
        boolean toMongoEnabledType = false; // remove
        public ActualType publishedType;
        public boolean publishedByInterface; // remove
        TypeName mongoType;
        TypeName vertxType;
        boolean isApiType; // remove
        TypeName vertxImplType; // remove
        boolean isPublisher;
        boolean singlePublisher;
        boolean noArgPublisher; // remove
        ClassName publisherClassName;
        boolean isReactive;
        String conversionMethod; // remove
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

        public static ActualType fromPublisher(ExecutableMemberDoc methodDoc, Type type) {
            ActualType actualType = new ActualType();
            actualType.isPublisher = true;
            actualType.publisherClassName = ClassName.bestGuess(type.qualifiedTypeName());
            ParameterizedType parameterizedType = type.asParameterizedType();
            if (parameterizedType != null) {
                if (parameterizedType.typeArguments().length > 1)
                    throw new IllegalStateException("not implemented");
                String qualified = parameterizedType.typeArguments()[0].toString();
                if (qualified.contains(".")) {
                    actualType.mongoType = ClassName.bestGuess(qualified);
                } else {
                    actualType.mongoType = TypeVariableName.get(qualified);
                }
                actualType.vertxType = actualType.mongoType;
                if (actualType.mongoType.toString().equals("com.mongodb.reactivestreams.client.Success") ||
                        actualType.mongoType.toString().equals(Void.class.getName())
                ) {
                    actualType.singlePublisher = true;
                    actualType.vertxType = ClassName.get(Void.class);
                }
            } else {
                Type[] types = type.asClassDoc().interfaceTypes();
                if (types != null) {
                    for (Type atype : types) {
                        if (atype.qualifiedTypeName().equals(Publisher.class.getName())) {
                            ParameterizedType parameterizedType1 = atype.asParameterizedType();
                            Type[] types1 = parameterizedType1.typeArguments();
                            actualType.mongoType = ClassName.bestGuess(types1[0].qualifiedTypeName());
                        }
                    }
                }
                if (actualType.mongoType == null)
                    throw new IllegalStateException("Unable to detect published type of " + type.qualifiedTypeName());
                actualType.vertxType = actualType.mongoType;
                actualType.noArgPublisher = true;
                if (Types.isKnown(actualType.mongoType.toString())) {
                    actualType.vertxType = Types.getMapped(actualType.mongoType.toString());
                }
            }
            if (!actualType.singlePublisher) {
                String rawCommentText = methodDoc.getRawCommentText();
                Optional<String> first = Arrays.stream(rawCommentText.split("\\n+")).filter(it -> it.contains("@return")).findFirst();
                if (first.isPresent()) {
                    String returnSpec = first.get();
                    actualType.singlePublisher = returnSpec.contains("empty publisher") ||  returnSpec.contains("single element") || returnSpec.contains("a publisher for the") || returnSpec.contains("a Publisher containing the");
                } else {
                    throw new IllegalStateException("no @return");
                }
            }
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
        ActualType returnType;
        List<MongoMethodParameter> params = new ArrayList<>();

        void computeActualReturnTypes(InspectionContext context, ActualType actualReturnType) {
//            returnType = new MongoMethodParameter();
            returnType = actualReturnType;

//            if (actualReturnType.isPublisher) {
//                returnType.type = actualReturnType;
//                if (actualReturnType.singlePublisher) {
//                    if (actualReturnType.mongoType != null) {
//                        if (actualReturnType.mongoType.toString().equals("TDocument")) {
//                            returnType.vertxResultClassName = ClassName.get(Future.class);
//                        } else {
//                            TypeName paramTypeName;
//                            if (context.reactiveApiClasses.contains(actualReturnType.mongoType.toString())) {
//                                paramTypeName = ClassName.bestGuess(mapPackageName(actualReturnType.mongoType.toString()));
//                            } else if (Types.isKnown(actualReturnType.mongoType.toString())) {
//                                paramTypeName = Types.getMapped(actualReturnType.mongoType.toString());
//                            } else
//                                paramTypeName = ClassName.bestGuess(actualReturnType.mongoType.toString());
//                            returnType.vertxType = paramTypeName;
//                            returnType.vertxResultClassName = ClassName.get(Future.class);
//                        }
//                    } else {
//                        returnType.vertxType = ClassName.get(Void.class);
//                        returnType.vertxResultClassName = ClassName.get(Future.class);
//                    }
//                    handlerParam = ParameterizedTypeName.get(ClassName.get(Handler.class), ParameterizedTypeName.get(ClassName.get(AsyncResult.class), returnType.vertxType));
//                } else {
//                    if (actualReturnType.mongoType != null) {
////                        returnType.vertxResultClassName = mongoName.equals("watch") ? ClassName.get(ReadStream.class) : ClassName.get(MongoResult.class);
//                        InspectionContext.PublisherDesc publisherDesc = context.publisherDescriptions.get(actualReturnType.publisherClassName.toString());
//                        if (publisherDesc != null) {
//                            returnType.vertxResultClassName = publisherDesc.resultClassName;
//                        } else {
//                            returnType.vertxResultClassName = ClassName.get(MongoResult.class);
//                        }
//                        if (actualReturnType.mongoType.toString().equals("TDocument")) {
//                            returnType.vertxType = TypeVariableName.get("TDocument");
//                        } else if (Types.isKnown(actualReturnType.mongoType.toString())) {
//                            returnType.vertxType = Types.getMapped(actualReturnType.mongoType.toString());
//                        } else {
//                            returnType.vertxType = actualReturnType.vertxType;
//                        }
//
//                    } else if (actualReturnType.publisherClassName.toString().equals("com.mongodb.reactivestreams.client.gridfs.GridFSFindPublisher")) {
//                        returnType.vertxResultClassName = ClassName.get(MongoResult.class);
//                        returnType.vertxType = ClassName.bestGuess("com.mongodb.client.gridfs.model.GridFSFile");
//                    } else
//                        throw new IllegalStateException("not implemented or not supported");
//                }
//            } else {
//                returnType.mongoType = actualReturnType.mongoType;
//                returnType.vertxType = actualReturnType.vertxType;
//            }

        }
        void computeJavadocs() {
            if (mongoJavadoc != null) {
                mongoJavadoc = mongoJavadoc.replace("$", "$$");
                this.vertxResultOrFutureJavadoc = this.mongoJavadoc;
                if (returnType != null && returnType.isPublisher) {
                    StringJoiner newRawCommentText = new StringJoiner("\n");
                    StringJoiner asyncNewRawCommentText = new StringJoiner("\n");
                    StringJoiner withOptionsNewRawCommentText = new StringJoiner("\n");
                    String replacement = mongoName.equals("watch") ? "read stream" : (returnType.singlePublisher ? "future" : "result");
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
//        if (actualReturnType != null) {
//            mongoMethod.computeActualReturnTypes(context, actualReturnType);
//            if (mongoMethod.returnType.type.mongoType != null && mongoMethod.returnType.type.mongoType.toString().equals(classDoc.qualifiedTypeName()))
//                mongoMethod.returnType.type.vertxType = this.fluentReturnType;
//        }
        if (classDoc.qualifiedTypeName().equals(GridFSBucket.class.getName()) &&
                methodDoc.name().equals("downloadToPublisher")
        ) {
            if (mongoMethod.mongoJavadoc.contains(" id"))
                mongoMethod.vertxName = "downloadByObjectId";
            else
                mongoMethod.vertxName = "downloadByFilename";
        }
//        if (mongoMethod.returnType.type.isPublisher &&
//                mongoMethod.returnType.type.mongoType != null &&
//                !context.reactiveApiClasses.contains(mongoMethod.returnType.type.mongoType.toString()) &&
//                mongoMethod.returnType.type.mongoType.toString().contains(".") && // not a TDocument
//                !mongoMethod.returnType.type.mongoType.equals(mongoMethod.returnType.type.vertxType)
//        ) {
//            mongoMethod.resultConversionMethod = context.conversionUtilsGenerator.addConversion(mongoMethod.returnType.type.mongoType, mongoMethod.returnType.type.vertxType);
//        }
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
            methodParameter.type = actualParamType;
            ClassName mongoRawType = null;
//            if (methodParameter.mongoType instanceof ParameterizedTypeName) {
//                ParameterizedTypeName pmongoType = (ParameterizedTypeName)methodParameter.mongoType;
//                mongoRawType = pmongoType.rawType;
//            } else if (methodParameter.mongoType instanceof ClassName) {
//                mongoRawType = (ClassName) methodParameter.mongoType;
//            }
//            if (context.optionsApiClasses.contains(methodParameter.mongoType.toString()) || context.otherApiClasses.contains(methodParameter.mongoType.toString())) {
//                methodParameter.toMongoEnabledType = true;
//            }

//            if (!(methodParameter.vertxType instanceof TypeVariableName) &&
//                    !methodParameter.vertxType.toString().equals(methodParameter.mongoType.toString()) &&
//                    !context.otherApiClasses.contains(methodParameter.mongoType.toString()) &&
//                    !context.optionsApiClasses.contains(methodParameter.mongoType.toString()) &&
//                    !context.reactiveApiClasses.contains(methodParameter.mongoType.toString())
//            ) {
//                methodParameter.conversionMethod = context.conversionUtilsGenerator.addConversion(methodParameter.vertxType, methodParameter.mongoType);
//            }
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
