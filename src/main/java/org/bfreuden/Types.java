package org.bfreuden;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeName;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class Types {

    private static Set<String> SUPPORTED_SUPER_CLASSES = new HashSet<>();
    static {
        SUPPORTED_SUPER_CLASSES.add("java.io.Closeable");
        SUPPORTED_SUPER_CLASSES.add("java.lang.AutoCloseable");
    }

    private static Map<String, TypeName> MAPPING = new HashMap<>();
    private static Set<String> IGNORED = new HashSet<>();

    static {
        IGNORED.add("org.bson.codecs.configuration.CodecRegistry");
        IGNORED.add("com.mongodb.internal.async.client.AsyncClientSession");


        MAPPING.put("org.bson.Document", ClassName.get(JsonObject.class));
//        mapping.put("javax.net.ssl.SSLContext", "");
        MAPPING.put("java.lang.Integer", ClassName.get(Integer.class));
        MAPPING.put("java.net.InetAddress", ClassName.get(InetAddress.class));
        MAPPING.put("java.lang.Long", ClassName.get(Long.class));
        MAPPING.put("java.util.Map", ClassName.get(Map.class)); // analyze generics?
        MAPPING.put("java.util.Set", ClassName.get(Set.class)); // analyze generics?
//        mapping.put("org.bson.UuidRepresentation", ""); // https://mongodb.github.io/mongo-java-driver/3.5/javadoc/org/bson/UuidRepresentation.html
        MAPPING.put("java.lang.Throwable", ClassName.get(Throwable.class));
        MAPPING.put("java.lang.Double", ClassName.get(Double.class));
        MAPPING.put("org.bson.BsonDocument", ClassName.get(JsonObject.class));
        MAPPING.put("org.bson.conversions.Bson", ClassName.get(JsonObject.class));
        MAPPING.put("java.lang.String", ClassName.get(String.class));
        MAPPING.put("org.bson.ByteBuf", TypeName.get(byte[].class));
        MAPPING.put("org.bson.types.ObjectId", ClassName.get(JsonObject.class)); // really?
        MAPPING.put("java.util.Date", ClassName.get(java.util.Date.class));
        MAPPING.put("org.bson.BsonValue", ClassName.get(Object.class)); // really?
        MAPPING.put("org.bson.BsonArray", ClassName.get(JsonArray.class));
        MAPPING.put("java.net.InetSocketAddress", ClassName.get(InetSocketAddress.class));
        MAPPING.put("java.util.List", ClassName.get(List.class)); // analyze generics?
        MAPPING.put("org.bson.BsonTimestamp", ClassName.get(Long.class));
        MAPPING.put("java.lang.Boolean", ClassName.get(Boolean.class));
        MAPPING.put("java.util.Iterator", ClassName.get(Iterator.class)); // analyze generics?
        MAPPING.put("java.util.concurrent.TimeUnit", ClassName.get(TimeUnit.class));
        MAPPING.put("java.lang.Object", ClassName.get(Object.class));
        MAPPING.put("java.lang.Class", ClassName.get(Class.class));
        MAPPING.put("org.bson.BsonBinary", TypeName.get(byte[].class));
        MAPPING.put("? extends org.bson.conversions.Bson", ClassName.get(JsonObject.class));
        MAPPING.put("? extends com.mongodb.client.model.WriteModel<? extends TDocument>", ClassName.bestGuess("io.vertx.mongo.BulkOperation"));
    }

    public static boolean isKnown(String qualifiedTypeName) {
        return MAPPING.containsKey(qualifiedTypeName);
    }

    public static TypeName getMapped(String qualifiedTypeName) {
        return MAPPING.get(qualifiedTypeName);
    }

    public static boolean isIgnored(String qualifiedTypeName) {
        return IGNORED.contains(qualifiedTypeName);
    }

    public static boolean isSupportedSuperClass(String qualifiedTypeName) {
        return SUPPORTED_SUPER_CLASSES.contains(qualifiedTypeName);
    }

}
