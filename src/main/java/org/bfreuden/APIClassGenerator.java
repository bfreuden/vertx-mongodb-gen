package org.bfreuden;

import com.mongodb.reactivestreams.client.gridfs.GridFSBucket;
import com.squareup.javapoet.*;
import com.sun.javadoc.*;
import io.vertx.core.json.JsonObject;

import javax.lang.model.element.Modifier;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

public abstract class APIClassGenerator {


    protected static Set<String> SUPPORTED_SUPER_CLASSES = new HashSet<>();
    static {
        SUPPORTED_SUPER_CLASSES.add("java.io.Closeable");
        SUPPORTED_SUPER_CLASSES.add("java.lang.AutoCloseable");
    }
    protected final InspectionContext context;
    protected final ClassDoc classDoc;
    protected String targetClassName;
    protected TypeName maybeParameterizedTypeName;

    public APIClassGenerator(InspectionContext context, ClassDoc classDoc) {
        this.context = context;
        this.classDoc = classDoc;
    }

    public void generate(File genSourceDir) throws IOException {
        JavaFile javaFile = getJavaFile();
        javaFile.writeTo(genSourceDir);
    }

    protected ActualType getActualType(MethodDoc methodDoc, String name, Type type, TypeLocation location) {
        if (!type.isPrimitive()) {
            String qualifiedTypeName = type.qualifiedTypeName();
            if (qualifiedTypeName.equals("TDocument")) {
                return ActualType.fromFullyQualifiedName(JsonObject.class.getName());
            } else if (Types.isIgnored(qualifiedTypeName)) {
                System.out.println("ignored method: " + methodDoc);
                return null;
            } else if (Types.isKnown(qualifiedTypeName)) {
                ParameterizedType parameterizedType = type.asParameterizedType();
                String mappedType = Types.getMapped(qualifiedTypeName);
                if (parameterizedType != null) {
                    String qualified = parameterizedType.toString();
                    String parameterClassName = qualified.substring(qualified.indexOf('<') + 1, qualified.length() - 1);
                    if (parameterClassName.equals("TDocument")) {
                        return ActualType.fromTypeName(ParameterizedTypeName.get(ClassName.bestGuess(mappedType), TypeVariableName.get("TDocument")));
                    } else if (parameterClassName.equals("? extends TDocument")) {
                        return ActualType.fromTypeName(ParameterizedTypeName.get(ClassName.bestGuess(mappedType), WildcardTypeName.supertypeOf(TypeVariableName.get("TDocument"))));
                    } else {
                        if (Types.isKnown(parameterClassName)) {
                            String mappedParameterClassName = Types.getMapped("? extends org.bson.conversions.Bson");
                            return ActualType.fromTypeName(ParameterizedTypeName.get(ClassName.bestGuess(mappedType), ClassName.bestGuess(mappedParameterClassName)));
                        } else {
                            return ActualType.fromTypeName(ParameterizedTypeName.get(ClassName.bestGuess(mappedType), ClassName.bestGuess(parameterClassName)));
                        }
                    }
                } else {
                    mappedType = mappedType.isEmpty() ? qualifiedTypeName : mappedType;
                    if (mappedType.contains("["))
                        return ActualType.fromArray(mappedType);
                    else
                        return ActualType.fromFullyQualifiedName(mappedType);
                }
            } else if (context.enumApiClasses.contains(qualifiedTypeName)) {
                return ActualType.fromFullyQualifiedName(qualifiedTypeName);
            } else if (context.reactiveApiClasses.contains(qualifiedTypeName)) {
                return ActualType.fromFullyQualifiedName(mapPackageName(qualifiedTypeName));
            } else if (context.builderClasses.contains(qualifiedTypeName)) {
                return ActualType.fromFullyQualifiedName(mapPackageName(qualifiedTypeName));
            } else if (context.optionsApiClasses.contains(qualifiedTypeName)) {
                return ActualType.fromFullyQualifiedName(mapPackageName(qualifiedTypeName));
            } else if (qualifiedTypeName.equals("org.reactivestreams.Publisher")) {
                return ActualType.fromPublisher(methodDoc, type);
            } else if (context.publishersApiClasses.contains(qualifiedTypeName)) {
                return ActualType.fromPublisher(methodDoc, type);
            } else if (context.others.contains(qualifiedTypeName)) {
                return ActualType.fromFullyQualifiedName(mapPackageName(qualifiedTypeName));
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
        if (SUPPORTED_SUPER_CLASSES.contains(superClassName))
            return true;
        return context.classDocs.containsKey(superClassName) && !context.others.contains(superClassName);
    }

    protected String getClassName() {
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

    protected abstract JavaFile getJavaFile();
    enum TypeLocation {
        PARAMETER,
        RETURN
    }

    static class ActualType {
        TypeName typeName;
        java.lang.reflect.Type type;
        boolean isPublisher;
        boolean singlePublisher;
        String publisherClassName;
        String parameterClassName;

        private ActualType() {
        }

        static ActualType fromType(java.lang.reflect.Type type) {
            ActualType actualType = new ActualType();
            actualType.type = type;
            return actualType;
        }

        static ActualType fromTypeName(TypeName typeName) {
            ActualType actualType = new ActualType();
            actualType.typeName = typeName;
            return actualType;
        }

        static ActualType fromFullyQualifiedName(String fullyQualifiedName) {
            ActualType actualType = new ActualType();
            String packageName = fullyQualifiedName.substring(0, fullyQualifiedName.lastIndexOf('.'));
            String simpleName = fullyQualifiedName.substring(fullyQualifiedName.lastIndexOf('.') + 1);
            actualType.typeName = ClassName.get(packageName, simpleName);
            return actualType;
        }

        static ActualType fromPackageAndSimpleName(String packageName, String simpleName) {
            ActualType actualType = new ActualType();
            actualType.typeName = ClassName.get(packageName, simpleName);
            return actualType;
        }

        public static ActualType fromArray(String mappedType) {
            ActualType actualType = new ActualType();
            if (!mappedType.equals("byte[]"))
                throw new IllegalArgumentException();
            actualType.type = byte[].class;
            return actualType;
        }

        public static ActualType fromPublisher(MethodDoc methodDoc, Type type) {
            ActualType actualType = new ActualType();
            actualType.isPublisher = true;
            actualType.publisherClassName = type.qualifiedTypeName();
            ParameterizedType parameterizedType = type.asParameterizedType();
            if (parameterizedType != null) {
                String qualified = parameterizedType.toString();
                actualType.parameterClassName = qualified.substring(qualified.indexOf('<') + 1, qualified.length() - 1);
                if (actualType.parameterClassName.equals("com.mongodb.reactivestreams.client.Success") ||
                        actualType.parameterClassName.equals(Void.class.getName())
                ) {
                    actualType.singlePublisher = true;
                    actualType.parameterClassName = Void.class.getName();
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
                methodBuilder.returns(typeName);
        }

        public ParameterSpec.Builder paramSpecBuilder(String name) {
            if (type != null)
                return ParameterSpec.builder(type, name);
            else
                return ParameterSpec.builder(typeName, name);
        }
    }
}
