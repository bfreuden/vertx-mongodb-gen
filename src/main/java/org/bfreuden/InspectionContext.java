package org.bfreuden;

import com.google.common.collect.HashMultimap;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeSpec;
import com.sun.javadoc.*;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class InspectionContext {

    public static class PublisherDesc {
        ClassName resultClassName;
        String firstMethodName;
        String toCollectionMethodName;
        String batchSizePropertyName;
    }
    public Map<String, PublisherDesc> publisherDescriptions = new HashMap<>();
    public Map<String, String> publisherOptionsClasses = new HashMap<>();
    ConversionUtilsGenerator conversionUtilsGenerator = new ConversionUtilsGenerator();
    public final Set<String> apiPackages = new HashSet<>();
    public final Set<String> dependenciesPackages = new HashSet<>();
    public final Set<String> stopClasses = new HashSet<>();
    public final Set<String> stopEdges = new HashSet<>();
    public HashSet<String> otherApiClasses = new HashSet<>();
    HashMultimap<String, String> edgeLabels = HashMultimap.create();
    public final Set<String> reactiveApiClasses = new HashSet<>();
    public final Set<String> enumApiClasses = new HashSet<>();
    public final Set<String> publishersApiClasses = new HashSet<>();
    public final Set<String> nonApiParameterAndReturnClasses = new HashSet<>();
    public final Map<String, ClassDoc> classDocs = new HashMap<String, ClassDoc>();
    public final Map<String, TypeSpec.Builder> typeBuilders = new HashMap<String, TypeSpec.Builder>();
    public final Set<String> optionsApiClasses = new HashSet<>();
    public final Set<String> reactiveClasses = new HashSet<>();
    public final Set<String> builderClasses = new HashSet<>();
    public final Set<String> bsonBasedClasses = new HashSet<>();
    Graph<String, DefaultEdge> graph = new DefaultDirectedGraph<>(DefaultEdge.class);

    public InspectionContext() {
    }

    private String getPackageName(ClassDoc classDoc) {
        return classDoc.containingPackage().name();
    }

    private String getPackageName(String className) {
        int dotIndex = className.lastIndexOf('.');
        if (dotIndex != -1) {
            return className.substring(0, dotIndex);
        } else {
            return "";
        }
    }

    private boolean isApiAuthorizedPackage(ClassDoc classDoc) {
        String packageName = getPackageName(classDoc);
        String qualifiedTypeName = classDoc.qualifiedTypeName();
        if (qualifiedTypeName.startsWith("org.reactivestreams."))
            reactiveClasses.add(qualifiedTypeName);
        if (dependenciesPackages.stream().anyMatch(packageName::startsWith))
            return false;
        if (!apiPackages.contains(packageName))
            throw new IllegalArgumentException(qualifiedTypeName + " does not belong to authorized packages");
        return true;
    }

    private boolean isStopClass(ClassDoc classDoc) {
        return true;
    }

    public boolean inspect(ClassDoc classDoc) {
        if (!isApiAuthorizedPackage(classDoc))
            return false;
        if (stopClasses.contains(classDoc.qualifiedTypeName()))
            return false;
        String qualifiedTypeName = classDoc.qualifiedTypeName();

        if (!graph.containsVertex(qualifiedTypeName)) {
            if (classDoc.superclassType() != null && classDoc.superclassType().qualifiedTypeName().equals(Enum.class.getName()))
                enumApiClasses.add(qualifiedTypeName);
            if (qualifiedTypeName.endsWith(".Builder"))
                builderClasses.add(qualifiedTypeName);
            System.out.println("Processing " + qualifiedTypeName);
            graph.addVertex(qualifiedTypeName);
            classDocs.put(qualifiedTypeName, classDoc);
            if (classDoc.isClass()) {
                typeBuilders.put(qualifiedTypeName, TypeSpec.classBuilder(classDoc.typeName()));
            } else if (classDoc.isInterface()) {
                typeBuilders.put(qualifiedTypeName, TypeSpec.interfaceBuilder(classDoc.typeName()));
            } else if (classDoc.isEnum()) {
                typeBuilders.put(qualifiedTypeName, TypeSpec.enumBuilder(classDoc.typeName()));
            } else {
                throw new IllegalArgumentException();
            }
            if (qualifiedTypeName.endsWith("Options"))
                optionsApiClasses.add(qualifiedTypeName);

            // process fields
            FieldDoc[] fields = classDoc.fields(true);
            for (FieldDoc fieldDoc : fields) {
                Type type = fieldDoc.type();
                if (!type.isPrimitive()) {
                    ClassDoc classDoc1 = type.asClassDoc();
                    String typeQualifiedName = classDoc1.qualifiedTypeName();
                    if (inspect(classDoc1))
                        maybeAddEdge(qualifiedTypeName, typeQualifiedName, "field");
                }
            }

            // process interfaces
            ClassDoc[] interfaces = classDoc.interfaces();
            for (ClassDoc interf : interfaces) {
                String interfTypeName = interf.qualifiedTypeName();
                if (inspect(interf))
                    maybeAddEdge(qualifiedTypeName, interfTypeName, "implements");

            }

            // process super class
            ClassDoc superclass = classDoc.superclass();
            if (superclass != null) {
                String superclassTypeName = superclass.qualifiedTypeName();
                if (inspect(superclass))
                    maybeAddEdge(qualifiedTypeName, superclassTypeName, "extends");
            }

            // process methods
            MethodDoc[] methods = classDoc.methods(true);
            for (MethodDoc methodDoc : methods) {
                boolean reactiveApiMethod = false;
                // return type
                Type type = methodDoc.returnType();
                if (!type.isPrimitive()) {
                    ClassDoc classDoc1 = type.asClassDoc();
                    String typeQualifiedName = classDoc1.qualifiedTypeName();
                    nonApiParameterAndReturnClasses.add(typeQualifiedName);
                    if (typeQualifiedName.startsWith("org.bson"))
                        bsonBasedClasses.add(qualifiedTypeName);
                    if (typeQualifiedName.contains("Publisher") && !qualifiedTypeName.contains("Publisher")) {
                        reactiveApiMethod = true;
                        reactiveApiClasses.add(qualifiedTypeName);
                        publishersApiClasses.add(typeQualifiedName);
                    }
                    if (inspect(classDoc1))
                        maybeAddEdge(qualifiedTypeName, typeQualifiedName, "return:"+methodDoc.name());
                }
                // parameters
                Parameter[] parameters = methodDoc.parameters();
                for (Parameter parameter: parameters) {
                    Type type1 = parameter.type();
                    if (!type1.isPrimitive()) {
                        ClassDoc classDoc1 = type1.asClassDoc();
                        String typeQualifiedName = classDoc1.qualifiedTypeName();
                        nonApiParameterAndReturnClasses.add(typeQualifiedName);
                        if (typeQualifiedName.startsWith("org.bson"))
                            bsonBasedClasses.add(qualifiedTypeName);
                        if (inspect(classDoc1))
                            maybeAddEdge(qualifiedTypeName, typeQualifiedName, "param:"+methodDoc.name());
                    }
                }
            }

            // process constructors
            ConstructorDoc[] constructors = classDoc.constructors(true);
            for (ConstructorDoc constructorDoc : constructors) {
                // parameters
                Parameter[] parameters = constructorDoc.parameters();
                for (Parameter parameter: parameters) {
                    Type type1 = parameter.type();
                    if (!type1.isPrimitive()) {
                        ClassDoc classDoc1 = type1.asClassDoc();
                        String typeQualifiedName = classDoc1.qualifiedTypeName();
                        nonApiParameterAndReturnClasses.add(typeQualifiedName);
                        if (typeQualifiedName.startsWith("org.bson"))
                            bsonBasedClasses.add(qualifiedTypeName);
                        if (inspect(classDoc1))
                            maybeAddEdge(qualifiedTypeName, typeQualifiedName, "ctor-param");

                    }
                }
            }
        }
        return true;
    }

    private void maybeAddEdge(String sourceVertex, String targetVertex, String edgeLabel) {
        if (stopEdges.contains(sourceVertex + "#" + edgeLabel))
            return;
        if (!sourceVertex.equals(targetVertex)) {
            String edge = sourceVertex + "->" + targetVertex;
            if (!graph.containsEdge(sourceVertex, targetVertex)) {
                graph.addEdge(sourceVertex, targetVertex);
            }
            edgeLabels.put(edge, edgeLabel);
        }
    }

    public void finalizeInspection() {
        for (String vertx : graph.vertexSet())
            nonApiParameterAndReturnClasses.remove(vertx);
    }

}
