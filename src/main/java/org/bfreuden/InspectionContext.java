package org.bfreuden;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Lists;
import com.squareup.javapoet.ClassName;
import com.sun.javadoc.*;
import com.sun.tools.doclets.internal.toolkit.util.ClassDocCatalog;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;

import java.util.*;

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
    public final Set<String> modelApiClasses = new HashSet<>();
    public final Set<String> resultApiClasses = new HashSet<>();
    public final Set<String> nonApiParameterAndReturnClasses = new HashSet<>();
    public final Map<String, ClassDoc> classDocs = new HashMap<String, ClassDoc>();
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
        if (classDoc.name().endsWith("Exception"))
            return false;
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
            if (qualifiedTypeName.endsWith("Options"))
                optionsApiClasses.add(qualifiedTypeName);
            if (qualifiedTypeName.endsWith("Model"))
                modelApiClasses.add(qualifiedTypeName);
            if (qualifiedTypeName.endsWith("Result"))
                resultApiClasses.add(qualifiedTypeName);

            // process fields
            FieldDoc[] fields = classDoc.fields(true);
            for (FieldDoc fieldDoc : fields) {
                Type type = fieldDoc.type();
                inspect(qualifiedTypeName, type, "field");
            }

            // process interfaces
            ClassDoc[] interfaces = classDoc.interfaces();
            for (ClassDoc interf : interfaces) {
                inspect(qualifiedTypeName, interf, "implements");
            }

            // process super class
            ClassDoc superclass = classDoc.superclass();
            if (superclass != null) {
                inspect(qualifiedTypeName, superclass, "extends");
            }

            // process methods
            MethodDoc[] methods = classDoc.methods(true);
            for (MethodDoc methodDoc : methods) {
                // return type
                Type type = methodDoc.returnType();
                ClassDoc first = inspect(qualifiedTypeName, type, "return:"+methodDoc.name());
                if (first != null) {
                    String typeQualifiedName = first.qualifiedTypeName();
                    nonApiParameterAndReturnClasses.add(typeQualifiedName);
                    if (typeQualifiedName.startsWith("org.bson"))
                        bsonBasedClasses.add(qualifiedTypeName);
                    if (typeQualifiedName.contains("Publisher") && !qualifiedTypeName.contains("Publisher")) {
                        reactiveApiClasses.add(qualifiedTypeName);
                        publishersApiClasses.add(typeQualifiedName);
                    }
                }
                // parameters
                Parameter[] parameters = methodDoc.parameters();
                for (Parameter parameter: parameters) {
                    Type type1 = parameter.type();
                    ClassDoc first2 = inspect(qualifiedTypeName, type1, "param:"+methodDoc.name());
                    if (first2 != null) {
                        String typeQualifiedName = first2.qualifiedTypeName();
                        nonApiParameterAndReturnClasses.add(typeQualifiedName);
                        if (typeQualifiedName.startsWith("org.bson"))
                            bsonBasedClasses.add(qualifiedTypeName);
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
                    ClassDoc first2 = inspect(qualifiedTypeName, type1, "ctor-param");
                    if (first2 != null) {
                        String typeQualifiedName = first2.qualifiedTypeName();
                        nonApiParameterAndReturnClasses.add(typeQualifiedName);
                        if (typeQualifiedName.startsWith("org.bson"))
                            bsonBasedClasses.add(qualifiedTypeName);
                    }
                }
            }
        }
        return true;
    }

    private ClassDoc inspect(String qualifiedTypeName, Type type, String linkName) {
        Set<ClassDoc> classDocs = referencedTypes(type);
        boolean first = true;
        for (ClassDoc doc: classDocs) {
            String typeQualifiedName = doc.qualifiedTypeName();
            if (inspect(doc))
                maybeAddEdge(qualifiedTypeName, typeQualifiedName, first ? linkName : "dep:"+ linkName);
            first = false;
        }
        return classDocs.isEmpty() ? null : classDocs.iterator().next(); 
    }

    private Set<ClassDoc> referencedTypes(Type type) {
        Set<ClassDoc> result = new LinkedHashSet<>();
        referencedTypes(result, type);
        return result;
    }
    private void referencedTypes(Set<ClassDoc> result, Type type) {
        if (type.isPrimitive())
            return;
        if (type instanceof ParameterizedType) {
            ParameterizedType ptype = (ParameterizedType)type;
            result.add(ptype.asClassDoc());
            for (Type arg : ptype.typeArguments())
                referencedTypes(result, arg);
        } else if (type instanceof ClassDoc) {
            result.add((ClassDoc) type);
        } else if (type.getClass().getName().contains("ArrayType")) {
            result.add(type.asClassDoc());
        } else if (type instanceof TypeVariable) {
        } else if (type instanceof WildcardType) {
            WildcardType wtype = (WildcardType) type;
            Type[] types = wtype.extendsBounds();
            if (types != null)
                for (Type t: types)
                    referencedTypes(result, t);
            Type[] types2 = wtype.superBounds();
            if (types2 != null)
                for (Type t: types2)
                    referencedTypes(result, t);
        } else {
            throw new IllegalStateException(type.getClass().getName());

        }
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

    public void finalizeInspection(ClassDoc[] classes) {
        addExtendsImplements(classes);
        for (String vertx : graph.vertexSet())
            nonApiParameterAndReturnClasses.remove(vertx);

    }

    private void addExtendsImplements(ClassDoc[] classes) {
        int nbVertices;
        do {
            nbVertices = graph.vertexSet().size();
            for (ClassDoc classDoc: classes) {
                // if not inspected already
                if (!graph.vertexSet().contains(classDoc.qualifiedTypeName())) {
                    ClassDoc[] interfaces = classDoc.interfaces();
                    if (interfaces != null) {
                        for (ClassDoc interf: interfaces) {
                            // but if a super interface has been inspected
                            if (graph.vertexSet().contains(interf.qualifiedTypeName())) {
                                // inspect
                                inspect(classDoc.qualifiedTypeName(), classDoc, "implements");
                            }
                        }
                    }
                    ClassDoc superclass = classDoc.superclass();
                    if (superclass != null) {
                        // but if a super class has been inspected
                        if (graph.vertexSet().contains(superclass.qualifiedTypeName())) {
                            // inspect
                            inspect(classDoc.qualifiedTypeName(), classDoc, "extends");
                        }
                    }
                }

            }

        } while (graph.vertexSet().size() != nbVertices);
    }


}
