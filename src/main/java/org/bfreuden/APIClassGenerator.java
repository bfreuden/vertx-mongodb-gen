package org.bfreuden;

import com.squareup.javapoet.*;
import com.sun.javadoc.*;
import org.reactivestreams.Publisher;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;

public abstract class APIClassGenerator {


    protected final InspectionContext context;
    protected final ClassDoc classDoc;
    protected TypeName fluentReturnType;

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

    protected ActualType getActualType(MethodDoc methodDoc, String name, Type type, TypeLocation location) {
        if (!type.isPrimitive()) {
            String qualifiedTypeName = type.qualifiedTypeName();
            if (qualifiedTypeName.equals("TDocument")) {
                return ActualType.fromTypeName(TypeVariableName.get("TDocument"));
            } else if (Types.isIgnored(qualifiedTypeName)) {
                System.out.println("WARNING: ignored method because return type is ignored: " + methodDoc);
                return null;
            } else if (Types.isKnown(qualifiedTypeName)) {
                ParameterizedType parameterizedType = type.asParameterizedType();
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
                ParameterizedType parameterizedType = type.asParameterizedType();
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
    }

    protected boolean isSupportedSuperClass(String superClassName) {
        if (Types.isSupportedSuperClass(superClassName))
            return true;
        return context.classDocs.containsKey(superClassName) && !context.otherApiClasses.contains(superClassName);
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

    enum TypeLocation {
        PARAMETER,
        RETURN
    }

    static class ActualType {
        TypeName mongoType;
        TypeName vertxType;
        boolean isPublisher;
        boolean singlePublisher;
        boolean noArgPublisher;
        ClassName publisherClassName;

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

        public static ActualType fromPublisher(MethodDoc methodDoc, Type type) {
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
}
