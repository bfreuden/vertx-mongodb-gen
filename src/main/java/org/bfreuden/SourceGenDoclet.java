package org.bfreuden;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.mongodb.*;
import com.mongodb.connection.StreamFactoryFactory;
import com.mongodb.selector.ServerSelector;
import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.LanguageVersion;
import com.sun.javadoc.RootDoc;
import org.apache.commons.io.FileUtils;
import org.bson.codecs.configuration.CodecRegistry;
import org.jgrapht.Graph;
import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.event.ConnectedComponentTraversalEvent;
import org.jgrapht.event.EdgeTraversalEvent;
import org.jgrapht.event.TraversalListenerAdapter;
import org.jgrapht.event.VertexTraversalEvent;
import org.jgrapht.graph.AsSubgraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.nio.Attribute;
import org.jgrapht.nio.DefaultAttribute;
import org.jgrapht.nio.gexf.GEXFExporter;
import org.jgrapht.traverse.BreadthFirstIterator;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

public class SourceGenDoclet {


    public static LanguageVersion languageVersion() {
        return LanguageVersion.JAVA_1_5;
    }
    public static boolean start(RootDoc root) throws IOException {
        ClassDoc[] classes = root.classes();

        InspectionContext inspectionContext = new InspectionContext();

        inspectionContext.inspect(findClassDoc(classes, "com.mongodb.reactivestreams.client.MongoClient"));
        inspectionContext.inspect(findClassDoc(classes, "com.mongodb.reactivestreams.client.MongoClients"));
        inspectionContext.inspect(findClassDoc(classes, "com.mongodb.reactivestreams.client.gridfs.GridFSBuckets"));
        inspectionContext.inspect(findClassDoc(classes, "com.mongodb.reactivestreams.client.vault.ClientEncryption"));
        inspectionContext.finalizeInspection(classes);

        Graph<String, DefaultEdge> graph = inspectionContext.graph;
        exportToGephi(inspectionContext, sugGraphFrom( graph, "com.mongodb.CreateIndexCommitQuorum"), new File("CreateIndexCommitQuorum.gexf"));
        Graph<String, DefaultEdge> initStuffGraph = sugGraphFrom( graph, "com.mongodb.MongoClientSettings", "com.mongodb.ConnectionString", "com.mongodb.MongoDriverInformation");
        exportToGephi(inspectionContext, initStuffGraph, new File("InitStuff.gexf"));

        initStuffGraph.removeVertex("com.mongodb.ReadPreference");
        initStuffGraph.removeVertex("com.mongodb.WriteConcern");
        initStuffGraph.removeVertex("com.mongodb.ReadConcern");
        initStuffGraph.removeVertex("com.mongodb.connection.ClusterDescription");

        exportToGephi(inspectionContext, graph, new File("graph.gexf"));
        AsSubgraph<String, DefaultEdge> graphNoInit = new AsSubgraph<>(graph, Sets.difference(graph.vertexSet(), initStuffGraph.vertexSet()), Sets.difference(graph.edgeSet(), initStuffGraph.edgeSet()));
        exportToGephi(inspectionContext, graphNoInit, new File("GraphNoInit.gexf"));
//        ConnectivityInspector<String, DefaultEdge> connectivityInspector = new ConnectivityInspector<>(graphNoInit);
//        DefaultDirectedGraph<String, DefaultEdge> graphNoInitConnected = new DefaultDirectedGraph<>(DefaultEdge.class);
//        for (String reactiveApiClass : inspectionContext.reactiveApiClasses) {
//            for (String vertx: connectivityInspector.connectedSetOf(reactiveApiClass)) {
//                graphNoInitConnected.addVertex(vertx);
//            }
//        }
//        for (DefaultEdge edge : graphNoInit.edgeSet()) {
//            String edgeSource = graphNoInit.getEdgeSource(edge);
//            if (!graphNoInitConnected.containsVertex(edgeSource))
//                continue;
//            String edgeTarget = graphNoInit.getEdgeTarget(edge);
//            if (!graphNoInitConnected.containsVertex(edgeTarget))
//                continue;
//            graphNoInitConnected.addEdge(edgeSource, edgeTarget);
//        }
        System.out.println("Graph no init");
        for (String clazz : graphNoInit.vertexSet()) {
            if (clazz.startsWith("com.mongodb.reactivestreams"))
                System.out.println("https://mongodb.github.io/mongo-java-driver/4.1/apidocs/mongodb-driver-reactivestreams/" +clazz.replace('.', '/') + ".html");
            else
                System.out.println("https://mongodb.github.io/mongo-java-driver/4.1/apidocs/mongodb-driver-core/" +clazz.replace('.', '/') + ".html");

        }
        File genSourceDir = new File("src/main/java");

        generateMainSources(genSourceDir, inspectionContext, graphNoInit);

        InspectionContext clientSettingsContext = new InspectionContext();
        clientSettingsContext.conversionUtilsGenerator = inspectionContext.conversionUtilsGenerator;
        clientSettingsContext.inspect(findClassDoc(classes, MongoClientSettings.class.getName()));
        clientSettingsContext.finalizeInspection(classes);
        generateClientBuilderSources(genSourceDir, clientSettingsContext, clientSettingsContext.graph);

        return true;
    }

    private static void generateClientBuilderSources(File genSourceDir, InspectionContext inspectionContext, Graph<String, DefaultEdge> graph) throws IOException {
        inspectionContext.otherApiClasses.remove(Block.class.getName());
        inspectionContext.otherApiClasses.remove(ConnectionString.class.getName());
        inspectionContext.otherApiClasses.remove(ServerAddress.class.getName());// TODO accept it
        inspectionContext.otherApiClasses.remove(ServerSelector.class.getName());
        inspectionContext.otherApiClasses.remove(StreamFactoryFactory.class.getName());
//        inspectionContext.otherApiClasses.remove(MongoCompressor.class.getName()); // TODO accept it
//        inspectionContext.otherApiClasses.remove(MongoCredential.class.getName()); // TODO accept it
        inspectionContext.otherApiClasses.add(CodecRegistry.class.getName());

        inspectionContext.excludedApiClasses.add(Block.class.getName());
        inspectionContext.excludedApiClasses.add(ConnectionString.class.getName());
        inspectionContext.excludedApiClasses.add(ServerAddress.class.getName()); // TODO accept it
        inspectionContext.excludedApiClasses.add(ServerSelector.class.getName());
        inspectionContext.excludedApiClasses.add(CodecRegistry.class.getName());
        inspectionContext.excludedApiClasses.add(StreamFactoryFactory.class.getName());
//        inspectionContext.excludedApiClasses.add(MongoCompressor.class.getName());  // TODO accept it
//        inspectionContext.excludedApiClasses.add(MongoCredential.class.getName()); // TODO accept it

        inspectionContext.optionsApiClasses.remove(ReadPreferenceHedgeOptions.class.getName());
        inspectionContext.builderClasses.remove(ReadPreferenceHedgeOptions.Builder.class.getName());

        String sourceSet = "/client-builder/";

        identifyClassTypes(inspectionContext, graph, sourceSet);
        for (String options : inspectionContext.optionsApiClasses) {
            new OptionsAPIClassGenerator(inspectionContext, inspectionContext.classDocs.get(options), false, true)
                    .generate(genSourceDir);
        }

        new BuilderPostInitializerClassGenerator(inspectionContext).generate(genSourceDir);
    }

    private static void generateMainSources(File genSourceDir, InspectionContext inspectionContext, Graph<String, DefaultEdge> graph) throws IOException {

        String sourceSet = "/";
        // FIXME hardcoded?
//        inspectionContext.optionsApiClasses.remove("com.mongodb.TransactionOptions");
//        inspectionContext.builderClasses.remove("com.mongodb.TransactionOptions.Builder");

        inspectionContext.optionsApiClasses.add("com.mongodb.client.model.Collation");
        inspectionContext.builderClasses.add("com.mongodb.client.model.Collation.Builder");
        inspectionContext.otherApiClasses.remove("com.mongodb.client.model.Collation.Builder");
        inspectionContext.otherApiClasses.remove("com.mongodb.client.model.Collation");

//        inspectionContext.otherApiClasses.add("com.mongodb.TransactionOptions");
        inspectionContext.resultApiClasses.add("com.mongodb.bulk.BulkWriteInsert");
        inspectionContext.resultApiClasses.add("com.mongodb.bulk.BulkWriteUpsert");
        inspectionContext.resultApiClasses.add("com.mongodb.client.model.changestream.UpdateDescription");
        inspectionContext.resultApiClasses.add("com.mongodb.client.model.changestream.ChangeStreamDocument");
        inspectionContext.resultApiClasses.add("com.mongodb.client.gridfs.model.GridFSFile");

        identifyClassTypes(inspectionContext, graph, sourceSet);

        for (String result : inspectionContext.resultApiClasses) {
            new BeanAPIClassGenerator(inspectionContext, inspectionContext.classDocs.get(result), true)
                    .generate(genSourceDir);
        }
        for (String model : inspectionContext.modelApiClasses) {
            new BeanAPIClassGenerator(inspectionContext, inspectionContext.classDocs.get(model), false)
                    .generate(genSourceDir);
        }
        for (String options : inspectionContext.optionsApiClasses) {
            new OptionsAPIClassGenerator(inspectionContext, inspectionContext.classDocs.get(options), false, false)
                    .generate(genSourceDir);
        }
        for (String reactive : inspectionContext.publishersApiClasses) {
            new PublisherOptionsAPIClassGenerator(inspectionContext, inspectionContext.classDocs.get(reactive))
                    .generate(genSourceDir);
        }
        for (String reactive : inspectionContext.publishersApiClasses) {
            new PublisherResultAPIClassGenerator(inspectionContext, inspectionContext.classDocs.get(reactive))
                    .generate(genSourceDir);
        }
//        int i = 0;
        for (String reactive : inspectionContext.reactiveApiClasses) {
//            i++;
//            if (i == 2)
//                continue;
            new ReactiveAPIClassGenerator(inspectionContext, inspectionContext.classDocs.get(reactive))
                    .generate(genSourceDir);
//            if (i == 7)
//                break;
        }
        inspectionContext.conversionUtilsGenerator.generateSource(genSourceDir);
    }

    private static void identifyClassTypes(InspectionContext inspectionContext, Graph<String, DefaultEdge> graph, String sourceSet) throws IOException {
        // remove isolated nodes
        ArrayList<String> vertices = new ArrayList<>(graph.vertexSet());
        for (String vertex : vertices)
            if (graph.inDegreeOf(vertex) == 0 && graph.outDegreeOf(vertex) == 0)
                graph.removeVertex(vertex);

        // remove isolated class + builder class couples
        ConnectivityInspector<String, DefaultEdge> stringDefaultEdgeConnectivityInspector = new ConnectivityInspector<>(graph);
        List<Set<String>> sets = stringDefaultEdgeConnectivityInspector.connectedSets();
        for (Set<String> nodes : sets) {
            if (nodes.size() == 2) {
                Iterator<String> iterator = nodes.iterator();
                String node1 = iterator.next();
                String node2 = iterator.next();
                if (node1.equals(node2+".Builder") || node2.equals(node1 + ".Builder")) {
                    graph.removeVertex(node1);
                    graph.removeVertex(node2);
                }
            }
        }
        for (Set<String> classes: Lists.newArrayList(
                inspectionContext.reactiveApiClasses, inspectionContext.enumApiClasses, inspectionContext.publishersApiClasses,
                inspectionContext.nonApiParameterAndReturnClasses,
                inspectionContext.modelApiClasses,
                inspectionContext.resultApiClasses,
                inspectionContext.optionsApiClasses, inspectionContext.reactiveClasses,
                inspectionContext.builderClasses, inspectionContext.bsonBasedClasses) )
            classes.removeIf(it -> !graph.vertexSet().contains(it));

        DijkstraShortestPath<String, DefaultEdge> shortestPath = new DijkstraShortestPath<>(graph);
        inspectionContext.isolatedApiClasses = new HashSet<>();
        inspectionContext.linkedApiClasses = new HashSet<>();
        for (String vertex : graph.vertexSet()) {
            boolean isolated = true;
            for (String reactiveApiClass : inspectionContext.reactiveApiClasses) {
                if (shortestPath.getPath(vertex, reactiveApiClass) != null) {
                    isolated = false;
                    break;
                }
            }
            if (isolated)
                inspectionContext.isolatedApiClasses.add(vertex);
            else if (!inspectionContext.reactiveApiClasses.contains(vertex))
                inspectionContext.linkedApiClasses.add(vertex);

        }

        inspectionContext.otherApiClasses = new HashSet<>();
        for (String isolatedApiClass : inspectionContext.isolatedApiClasses) {
            if (!inspectionContext.reactiveApiClasses.contains(isolatedApiClass) &&
            !inspectionContext.publishersApiClasses.contains(isolatedApiClass) &&
            !inspectionContext.enumApiClasses.contains(isolatedApiClass) &&
            !inspectionContext.builderClasses.contains(isolatedApiClass) &&
            !inspectionContext.modelApiClasses.contains(isolatedApiClass) &&
            !inspectionContext.resultApiClasses.contains(isolatedApiClass) &&
            !inspectionContext.optionsApiClasses.contains(isolatedApiClass)
            )
                inspectionContext.otherApiClasses.add(isolatedApiClass);
        }
        ArrayList<String> objects = new ArrayList<>(inspectionContext.otherApiClasses);
        for (String other: objects) {
            ClassDoc classDoc = inspectionContext.classDocs.get(other);
            if (classDoc.getRawCommentText().trim().startsWith("The default options")) {
                inspectionContext.otherApiClasses.remove(other);
                inspectionContext.optionsApiClasses.add(other);
            }
        }

        System.out.println("other api classes: ?");
        System.out.println("other api classes (" + inspectionContext.otherApiClasses.size() + "): " + inspectionContext.otherApiClasses);
        FileUtils.writeLines(new File("src/main/resources" + sourceSet + "other.txt"), inspectionContext.otherApiClasses.stream().sorted().collect(Collectors.toList()));


        System.out.println("reactive classes: rxjava classes used in mongodb api");
        System.out.println("reactive classes: " + inspectionContext.reactiveClasses);
        System.out.println("reactive api classes: api classes using rxjava, that must be rewritten using callbacks and futures");
        System.out.println("reactive api classes (" + inspectionContext.reactiveApiClasses.size() + "): " + inspectionContext.reactiveApiClasses);
        FileUtils.writeLines(new File("src/main/resources" + sourceSet + "reactive.txt"), inspectionContext.reactiveApiClasses.stream().sorted().collect(Collectors.toList()));
        System.out.println("isolated api classes: classes that have no \"link\" back to a reactive api class (could be used as-is in the vertx api)");
        System.out.println("isolated api classes (" + inspectionContext.isolatedApiClasses.size() + "): " + inspectionContext.isolatedApiClasses);
        FileUtils.writeLines(new File("src/main/resources" + sourceSet + "isolated.txt"), inspectionContext.isolatedApiClasses.stream().sorted().collect(Collectors.toList()));
        System.out.println("linked api classes: classes that have a \"link\" back to a reactive api class (could not be used as-is in the vertx api)");
        System.out.println("linked api classes (" + inspectionContext.linkedApiClasses.size() + "): " + inspectionContext.linkedApiClasses);
        FileUtils.writeLines(new File("src/main/resources" + sourceSet + "linked.txt"), inspectionContext.linkedApiClasses.stream().sorted().collect(Collectors.toList()));
        System.out.println("publishers api classes: classes extending rxjava publisher, that should lead to the creating of an options class");
        System.out.println("publishers api classes (" + inspectionContext.publishersApiClasses.size() + "): " + inspectionContext.publishersApiClasses);
        FileUtils.writeLines(new File("src/main/resources" + sourceSet + "publishers.txt"), inspectionContext.publishersApiClasses.stream().sorted().collect(Collectors.toList()));
        System.out.println("options api classes: classes seen as a parameter of a function returning a publisher");
        System.out.println("options api classes (" + inspectionContext.optionsApiClasses.size() + "): " + inspectionContext.optionsApiClasses);
        FileUtils.writeLines(new File("src/main/resources" + sourceSet + "options.txt"), inspectionContext.optionsApiClasses.stream().sorted().collect(Collectors.toList()));
        System.out.println("enum api classes: enums");
        System.out.println("enum api classes (" + inspectionContext.enumApiClasses.size() + "): " + inspectionContext.enumApiClasses);
        FileUtils.writeLines(new File("src/main/resources" + sourceSet + "enum.txt"), inspectionContext.enumApiClasses.stream().sorted().collect(Collectors.toList()));
        System.out.println("builder api classes: builders of read-only beans");
        System.out.println("builder api classes (" + inspectionContext.builderClasses.size() + "): " + inspectionContext.builderClasses);
        FileUtils.writeLines(new File("src/main/resources" + sourceSet + "builder.txt"), inspectionContext.builderClasses.stream().sorted().collect(Collectors.toList()));
        System.out.println("result api classes: beans with hidden constructors and no setters");
        System.out.println("result api classes (" + inspectionContext.resultApiClasses.size() + "): " + inspectionContext.resultApiClasses);
        FileUtils.writeLines(new File("src/main/resources" + sourceSet + "result.txt"), inspectionContext.resultApiClasses.stream().sorted().collect(Collectors.toList()));
        System.out.println("model api classes: beans with constructors but no setters");
        System.out.println("model api classes (" + inspectionContext.modelApiClasses.size() + "): " + inspectionContext.modelApiClasses);
        FileUtils.writeLines(new File("src/main/resources" + sourceSet + "model.txt"), inspectionContext.modelApiClasses.stream().sorted().collect(Collectors.toList()));
        System.out.println("bson-based api classes: classes containing methods or constructors have bson parameters or return type");
        System.out.println("bson-based api classes (" + inspectionContext.bsonBasedClasses.size() + "): " + inspectionContext.bsonBasedClasses);
        FileUtils.writeLines(new File("src/main/resources" + sourceSet + "bson.txt"), inspectionContext.bsonBasedClasses.stream().sorted().collect(Collectors.toList()));
        System.out.println("non-api parameter and return classes: classes of method parameters and return values not belonging to the api");
        System.out.println("non-api parameter and return classes (" + inspectionContext.nonApiParameterAndReturnClasses.size() + "): " + inspectionContext.nonApiParameterAndReturnClasses);
        FileUtils.writeLines(new File("src/main/resources" + sourceSet + "non-api.txt"), inspectionContext.nonApiParameterAndReturnClasses.stream().sorted().collect(Collectors.toList()));
    }

    private static Graph<String, DefaultEdge> sugGraphFrom(Graph<String, DefaultEdge> graph, String... startVertices) {
        //FIXME in the end there might be missing edges between the connected components of start vertices
        Set<String> vertices = new HashSet<>();
        Set<DefaultEdge> edges = new HashSet<>();
        for (String vertex: startVertices) {
            BreadthFirstIterator<String, DefaultEdge> iterator = new BreadthFirstIterator<>(graph, vertex);
            AtomicBoolean ccFinished = new AtomicBoolean(false);
            iterator.addTraversalListener(new TraversalListenerAdapter<String, DefaultEdge>() {
                public void connectedComponentFinished(ConnectedComponentTraversalEvent e) {
                    ccFinished.set(true);
                }

                public void connectedComponentStarted(ConnectedComponentTraversalEvent e) {
                    System.out.println();
                }
                @Override
                public void edgeTraversed(EdgeTraversalEvent<DefaultEdge> edgeTraversalEvent) {
                    if (!ccFinished.get())
                        edges.add(edgeTraversalEvent.getEdge());
                }

                @Override
                public void vertexTraversed(VertexTraversalEvent<String> vertexTraversalEvent) {
                    if (!ccFinished.get())
                        vertices.add(vertexTraversalEvent.getVertex());

                }
            });
            while (iterator.hasNext())
                iterator.next();
        }
        AsSubgraph<String, DefaultEdge> result = new AsSubgraph<>(graph, vertices, edges);
        return result;

    }
    private static void exportToGephi(InspectionContext inspectionContext, Graph<String, DefaultEdge> graph, File file) {
        GEXFExporter<String, DefaultEdge> exporter = new GEXFExporter<>(s-> s.substring("com.mongodb.".length()), s-> s.toString());
        exporter.setParameter(GEXFExporter.Parameter.EXPORT_EDGE_LABELS, true);
        exporter.setVertexAttributeProvider(vertex -> {
            Map<String, Attribute> strings = new HashMap<>();
            boolean isReactive = inspectionContext.reactiveApiClasses.contains(vertex);
            vertex = vertex.substring("com.mongodb.".length());
            strings.put("label", DefaultAttribute.createAttribute(isReactive ? vertex + " (async)" : vertex));
            return strings;
        });
        exporter.setEdgeAttributeProvider( edge -> {
            Map<String, Attribute> strings = new HashMap<>();
            String edgeSource = graph.getEdgeSource(edge);
            String edgeTarget = graph.getEdgeTarget(edge);
            Set<String> labels = inspectionContext.edgeLabels.get(edgeSource + "->" + edgeTarget);
            String firstLabel = labels.iterator().next();
            if (labels.size() == 1)
                strings.put("label", DefaultAttribute.createAttribute(firstLabel));
            else
                strings.put("label", DefaultAttribute.createAttribute(firstLabel +" (+ " + (labels.size() -1) + ")"));
            return strings;
        });
        exporter.exportGraph(graph, file);
    }

    private static ClassDoc findClassDoc(ClassDoc[] classes, String startClass) {
        return Arrays.stream(classes).filter(c -> c.qualifiedTypeName().equals(startClass)).findFirst().orElseThrow(() -> new IllegalArgumentException("can't find start class"));
    }


    public static void main(String[] args) {
        File sourcePath = new File("merged-sources");

        com.sun.tools.javadoc.Main.execute(SourceGenDoclet.class.getClassLoader(),
                "-doclet",
                SourceGenDoclet.class.getName(),
                "-sourcepath",
                sourcePath.getPath(),
                "-public",
                "-subpackages",
                "com.mongodb",
                "-exclude",
                "**/com/mongodb/**/internal/**"
        );
    }

}
