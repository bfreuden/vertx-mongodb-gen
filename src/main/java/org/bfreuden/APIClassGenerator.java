package org.bfreuden;

import com.squareup.javapoet.*;
import com.sun.javadoc.*;
import io.vertx.core.json.JsonObject;

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

    protected abstract List<JavaFile> getJavaFiles();

    public void generate(File genSourceDir) throws IOException {
        analyzeClass();
        List<JavaFile> javaFiles = getJavaFiles();
        for (JavaFile javaFile : javaFiles)
            javaFile.writeTo(genSourceDir);
    }

    protected abstract void analyzeClass();

    protected ActualType getActualType(MethodDoc methodDoc, String name, Type type, TypeLocation location) {
        if (!type.isPrimitive()) {
            String qualifiedTypeName = type.qualifiedTypeName();
            if (qualifiedTypeName.equals("TDocument")) {
                return ActualType.fromMappedTypeName(TypeVariableName.get("TDocument"), ClassName.get(JsonObject.class));
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
                        return ActualType.fromTypeName(ParameterizedTypeName.get((ClassName) mappedType, WildcardTypeName.supertypeOf(TypeVariableName.get("TDocument"))));
                    } else {
                        if (Types.isKnown(parameterClassName)) {
                            TypeName mappedParameterClassName = Types.getMapped("? extends org.bson.conversions.Bson");
                            return ActualType.fromTypeName(ParameterizedTypeName.get((ClassName) mappedType, mappedParameterClassName));
                        } else {
                            return ActualType.fromTypeName(ParameterizedTypeName.get((ClassName) mappedType, ClassName.bestGuess(parameterClassName)));
                        }
                    }
                } else {
                    return ActualType.fromTypeName(mappedType);
                }
            } else if (context.enumApiClasses.contains(qualifiedTypeName)) {
                return ActualType.fromTypeName(ClassName.bestGuess(qualifiedTypeName));
            } else if (context.reactiveApiClasses.contains(qualifiedTypeName)) {
                return ActualType.fromMappedTypeName(ClassName.bestGuess(qualifiedTypeName), ClassName.bestGuess(mapPackageName(qualifiedTypeName)));
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

    private boolean isSupportedReturnType(String qualifiedTypeName) {
        return false;
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

    protected String mapPackageName(String packageNameOrClassName) {
        if (packageNameOrClassName.startsWith("com.mongodb.reactivestreams")) {
            packageNameOrClassName = packageNameOrClassName.replace("com.mongodb.reactivestreams", "io.vertx.mongo");
            return packageNameOrClassName;
        } else if (packageNameOrClassName.startsWith("com.mongodb")) {
            packageNameOrClassName = packageNameOrClassName.replace("com.mongodb", "io.vertx.mongo");
            return packageNameOrClassName;
        } else {
            throw new IllegalArgumentException();
        }
    }

    enum TypeLocation {
        PARAMETER,
        RETURN
    }

    static class ActualType {
        TypeName mongoType;
        TypeName vertxType;
        java.lang.reflect.Type type;
        boolean isPublisher;
        boolean singlePublisher;
        String publisherClassName;
        String publisherParameterClassName;

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
            actualType.publisherClassName = type.qualifiedTypeName();
            ParameterizedType parameterizedType = type.asParameterizedType();
            if (parameterizedType != null) {
                String qualified = parameterizedType.toString();
                actualType.publisherParameterClassName = qualified.substring(qualified.indexOf('<') + 1, qualified.length() - 1);
                if (actualType.publisherParameterClassName.equals("com.mongodb.reactivestreams.client.Success") ||
                        actualType.publisherParameterClassName.equals(Void.class.getName())
                ) {
                    actualType.singlePublisher = true;
                    actualType.publisherParameterClassName = Void.class.getName();
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

        public void setAsReturnTypeOf(MethodSpec.Builder methodBuilder) {
            if (type != null)
                methodBuilder.returns(type);
            else
                methodBuilder.returns(vertxType);
        }

        public ParameterSpec.Builder paramSpecBuilder(String name) {
            if (type != null)
                return ParameterSpec.builder(type, name);
            else
                return ParameterSpec.builder(vertxType, name);
        }
    }
}
