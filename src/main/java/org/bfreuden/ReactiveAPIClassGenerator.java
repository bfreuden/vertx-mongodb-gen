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

import javax.lang.model.element.Modifier;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

public class ReactiveAPIClassGenerator {

    private final InspectionContext context;
    private final ClassDoc classDoc;
    private String targetClassName;
    private TypeName maybeParameterizedTypeName;

    public ReactiveAPIClassGenerator(InspectionContext context, ClassDoc classDoc) {
        this.context = context;
        this.classDoc = classDoc;
    }

    public void generate(File genSourceDir) throws IOException {
        JavaFile javaFile = getJavaFile();
        javaFile.writeTo(genSourceDir);
    }

    private JavaFile getJavaFile() {
        if (!classDoc.isInterface() && !classDoc.isClass())
            throw new IllegalArgumentException("not implemented");
        String packageName = getTargetPackage();
        TypeSpec.Builder type = classDoc.isClass() ? TypeSpec.classBuilder(getClassName()) :  TypeSpec.interfaceBuilder(getClassName());
        this.targetClassName = mapPackageName(classDoc.containingPackage().name()) + "." + getClassName();
        ParameterizedType parameterizedType = classDoc.asParameterizedType();
        if (parameterizedType != null) {
            // fixme hardcoded TDocument
            this.maybeParameterizedTypeName = ParameterizedTypeName.get(ClassName.bestGuess(this.targetClassName), TypeVariableName.get("TDocument"));
        } else {
            this.maybeParameterizedTypeName = ClassName.bestGuess(this.targetClassName);
        }

        String doc = classDoc.getRawCommentText();
        String javadoc = Arrays.stream(doc.split("[\\n\\r]+"))
                .filter(s -> !s.trim().isEmpty() && !s.contains("TDocument"))
                .collect(Collectors.joining("\n"));
        type.addJavadoc(javadoc);
        type.addModifiers(Modifier.PUBLIC);
        TypeVariable[] typeVariables = classDoc.typeParameters();
        for (TypeVariable typeVariable : typeVariables)
            type.addTypeVariable(TypeVariableName.get(typeVariable.toString()));
        for (ClassDoc inter : classDoc.interfaces()) {
            String superClassName = inter.qualifiedTypeName();
            if (isSupportedSuperClass(superClassName)) {
                try {
                    type.addSuperinterface(TypeName.get(Class.forName(superClassName)));
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
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
                        type.superclass(TypeName.get(Class.forName(superClassName)));
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
                continue;
            }
            List<MethodSpec> methodSpecs = convertMethod(methodDoc);
            if (methodSpecs != null) {
                for (MethodSpec methodSpec : methodSpecs)
                    type.addMethod(methodSpec);
            }
        }
        JavaFile javaFile = JavaFile.builder(packageName, type.build())
                .build();
        return javaFile;
    }

    private List<MethodSpec> convertMethod(MethodDoc methodDoc) {
        // TResult parametrized methods are ignored, we just want JsonObject
        TypeVariable[] methodTypeVariables = methodDoc.typeParameters();
        Optional<TypeVariable> resultType = Arrays.stream(methodTypeVariables).filter(v -> v.qualifiedTypeName().equals("TResult")).findFirst();
        boolean resultParameter = resultType.isPresent();
        if (resultParameter) {
            System.out.println("WARNING: return type of " + methodDoc + " has been ignored because it has a TResult type parameter");
            return null;
        }
        String rawCommentText = methodDoc.getRawCommentText();
        String rawCommentText2 = null;

        String targetMethodName = methodDoc.name();
        TypeName returnType = null;
        if (classDoc.qualifiedTypeName().equals(GridFSBucket.class.getName()) &&
                methodDoc.name().equals("downloadToPublisher")
        ) {
            if (rawCommentText.contains(" id"))
                targetMethodName = "downloadByObjectId";
            else
                targetMethodName = "downloadByFilename";

        } else if (classDoc.qualifiedTypeName().equals(MongoDatabase.class.getName()) &&
                methodDoc.name().equals("getCollection")
        ) {
            if (Arrays.stream(methodDoc.typeParameters()).findFirst().isPresent()) {

                returnType = ParameterizedTypeName.get(ClassName.bestGuess(mapPackageName(MongoCollection.class.getName())), TypeVariableName.get("TDocument"));
            } else {
                returnType = ParameterizedTypeName.get(ClassName.bestGuess(mapPackageName(MongoCollection.class.getName())), ClassName.bestGuess(JsonObject.class.getName()));
            }
        } else if (classDoc.qualifiedTypeName().equals(MongoCollection.class.getName())) {
            ParameterizedType parameterizedType = methodDoc.returnType().asParameterizedType();
            if (parameterizedType != null && parameterizedType.toString().equals(MongoCollection.class.getName() + "<TDocument>")) {
                returnType = ParameterizedTypeName.get(ClassName.bestGuess(mapPackageName(MongoCollection.class.getName())), TypeVariableName.get("TDocument"));
            }
        }
        MethodSpec.Builder methodBuilder1 = MethodSpec.methodBuilder(targetMethodName);
        MethodSpec.Builder methodBuilder2 = MethodSpec.methodBuilder(targetMethodName);
        if (classDoc.qualifiedTypeName().equals(MongoDatabase.class.getName()) &&
            methodDoc.name().equals("getCollection") &&
                Arrays.stream(methodDoc.typeParameters()).findFirst().isPresent()
        ) {
            methodBuilder1.addTypeVariable(TypeVariableName.get("TDocument"));
            methodBuilder2.addTypeVariable(TypeVariableName.get("TDocument"));
        } else  if (classDoc.qualifiedTypeName().equals(MongoCollection.class.getName()) &&
            methodDoc.name().equals("withDocumentClass") &&
                Arrays.stream(methodDoc.typeParameters()).findFirst().isPresent()
        ) {
            methodBuilder1.addTypeVariable(TypeVariableName.get("NewTDocument"));
            methodBuilder2.addTypeVariable(TypeVariableName.get("NewTDocument"));
        }
        boolean singlePublisher = false;
        if (classDoc.isInterface()) {
            methodBuilder1.addModifiers(Modifier.ABSTRACT);
            methodBuilder1.addModifiers(Modifier.PUBLIC);
            methodBuilder2.addModifiers(Modifier.ABSTRACT);
            methodBuilder2.addModifiers(Modifier.PUBLIC);
        }
        if (returnType != null) {
            methodBuilder1.returns(returnType);
            methodBuilder2.returns(returnType);
        } else {
            methodBuilder2.returns(this.maybeParameterizedTypeName);
            ActualType actualReturnType = getActualType(methodDoc, null, methodDoc.returnType(), TypeLocation.RETURN);
            if (actualReturnType == null)
                return null;
            if (actualReturnType.isPublisher) {
                if (rawCommentText != null) {
                    StringJoiner newRawCommentText = new StringJoiner("\n");
                    StringJoiner asyncNewRawCommentText = new StringJoiner("\n");
                    String replacement = methodDoc.name().equals("watch") ? "read stream" : (actualReturnType.singlePublisher ? "future" : "result");
                    for (String docLine : Arrays.stream(rawCommentText.split("\\n+")).collect(Collectors.toList())) {
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
                            String newReturnLine = docLine.substring(0, index) + " a reference to <code>this</code>";
                            asyncNewRawCommentText.add(newReturnLine);
                        } else {
                            newRawCommentText.add(docLine);
                            asyncNewRawCommentText.add(docLine);
                        }
                    }
                    rawCommentText = newRawCommentText.toString();
                    if (actualReturnType.singlePublisher)
                        rawCommentText2 = asyncNewRawCommentText.toString();
                }
                if (actualReturnType.singlePublisher) {
                    singlePublisher = true;
                    if (actualReturnType.parameterClassName != null) {
                        if (actualReturnType.parameterClassName.equals("TDocument")) {
                            returnType = ParameterizedTypeName.get(ClassName.get(Future.class), TypeVariableName.get("TDocument"));
                            methodBuilder1.returns(returnType);
                        } else {
                            TypeName paramTypeName;
                            if (context.reactiveApiClasses.contains(actualReturnType.parameterClassName)) {
                                paramTypeName = ClassName.bestGuess(mapPackageName(actualReturnType.parameterClassName));
                            } else if (Types.isKnown(actualReturnType.parameterClassName)) {
                                String mapped = Types.getMapped(actualReturnType.parameterClassName);
                                if (mapped.equals("byte[]"))
                                    paramTypeName = TypeName.get(byte[].class);
                                else
                                    paramTypeName = ClassName.bestGuess(mapped);
                            } else
                                paramTypeName = ClassName.bestGuess(actualReturnType.parameterClassName);
                            returnType = ParameterizedTypeName.get(ClassName.get(Future.class), paramTypeName);
                            methodBuilder1.returns(returnType);
                        }
                    } else {
                        methodBuilder1.returns(ParameterizedTypeName.get(ClassName.get(Future.class), ClassName.get(Void.class)));
                        returnType = ClassName.get(Void.class);
                    }
                } else {
                    if (actualReturnType.parameterClassName != null) {
                        ClassName mongoResultClass = methodDoc.name().equals("watch") ? ClassName.get(ReadStream.class) : ClassName.get("io.vertx.mongo.client", "MongoResult");
                        if (actualReturnType.parameterClassName.equals("TDocument"))
                            methodBuilder1.returns(ParameterizedTypeName.get(mongoResultClass, TypeVariableName.get("TDocument")));
                        else {
                            if (Types.isKnown(actualReturnType.parameterClassName))
                                methodBuilder1.returns(ParameterizedTypeName.get(mongoResultClass, ActualType.fromFullyQualifiedName(Types.getMapped(actualReturnType.parameterClassName)).typeName));
                            else
                                methodBuilder1.returns(ParameterizedTypeName.get(mongoResultClass, ActualType.fromFullyQualifiedName(actualReturnType.parameterClassName).typeName));
                        }

                    } else if (actualReturnType.publisherClassName.equals("com.mongodb.reactivestreams.client.gridfs.GridFSFindPublisher"))
                        methodBuilder1.returns(ParameterizedTypeName.get(ClassName.get("io.vertx.mongo.client", "MongoResult"), ClassName.get("com.mongodb.client.gridfs.model", "GridFSFile")));
                    else
                        throw new IllegalStateException("not implemented or not supported");
                }
            } else {
                actualReturnType.setAsReturnTypeOf(methodBuilder1);
            }
        }
        if (rawCommentText != null)
            methodBuilder1.addJavadoc(rawCommentText.replace('$', ' '));
        if (singlePublisher && rawCommentText2 != null)
            methodBuilder2.addJavadoc(rawCommentText2.replace('$', ' '));

        for (Parameter param : methodDoc.parameters()) {
            ActualType actualParamType = getActualType(methodDoc, param.name(), param.type(), TypeLocation.PARAMETER);
            if (actualParamType == null)
                return null;
            // FIXME
            if (actualParamType.isPublisher)
                return null;
            ParameterSpec.Builder builder = actualParamType.paramSpecBuilder(param.name());
            methodBuilder1.addParameter(builder.build());
            if (singlePublisher)
                methodBuilder2.addParameter(builder.build());
        }
        ArrayList<MethodSpec> result = new ArrayList<>();
        result.add(methodBuilder1.build());
        if (singlePublisher) {
            methodBuilder2.addParameter(ParameterSpec.builder(ParameterizedTypeName.get(ClassName.get(Handler.class), ParameterizedTypeName.get(ClassName.get(AsyncResult.class), returnType)), "handler").build());
            result.add(methodBuilder2.build());
        }
        return result;
    }

    static class ActualType {
        TypeName typeName;
        java.lang.reflect.Type type;
        boolean isPublisher;
        boolean singlePublisher;
        String publisherClassName;
        String parameterClassName;

        private ActualType() {}

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
            actualType.type = new byte[0].getClass();
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
                    actualType.singlePublisher = returnSpec.contains("single element") || returnSpec.contains("a publisher for the") || returnSpec.contains("a Publisher containing the");
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

    enum TypeLocation {
        PARAMETER,
        RETURN
    }


    private ActualType getActualType(MethodDoc methodDoc, String name, Type type, TypeLocation location) {
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

    private static Set<String> SUPPORTED_SUPER_CLASSES = new HashSet<>();
    static {
        SUPPORTED_SUPER_CLASSES.add("java.io.Closeable");
        SUPPORTED_SUPER_CLASSES.add("java.lang.AutoCloseable");
    }
    private boolean isSupportedSuperClass(String superClassName) {
        if (SUPPORTED_SUPER_CLASSES.contains(superClassName))
            return true;
        return context.classDocs.containsKey(superClassName) && !context.others.contains(superClassName);
    }

    private String getClassName() {
        return classDoc.typeName();
    }

    private String getTargetPackage() {
        String packageName = classDoc.containingPackage().name();
        return mapPackageName(packageName);
    }

    private String mapPackageName(String packageNameOrClassName) {
        if (packageNameOrClassName.startsWith("com.mongodb.reactivestreams")) {
            packageNameOrClassName = packageNameOrClassName.replace("com.mongodb.reactivestreams", "io.vertx.mongo");
            return packageNameOrClassName;
        } else if (packageNameOrClassName.startsWith("com.mongodb")) {
            packageNameOrClassName = packageNameOrClassName.replace("com.mongodb.foobar", "io.vertx.mongo");
            return packageNameOrClassName;
        } else {
            throw new IllegalArgumentException();
        }
    }

}
