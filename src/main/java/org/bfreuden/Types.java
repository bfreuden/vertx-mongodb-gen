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
    }

    private static Map<String, TypeName> MAPPING = new HashMap<>();
    private static Map<String, TypeName> MAPPING2 = new HashMap<>();
    private static Set<String> IGNORED = new HashSet<>();
    private static Set<String> ACCEPTED = new HashSet<>();

    static {
        IGNORED.add("org.bson.codecs.configuration.CodecRegistry");
        IGNORED.add("com.mongodb.internal.async.client.AsyncClientSession");


        MAPPING.put("org.bson.Document", ClassName.get(JsonObject.class));
//        mapping.put("javax.net.ssl.SSLContext", "");
        MAPPING.put("java.lang.Integer", ClassName.get(Integer.class));
        MAPPING.put("java.net.InetAddress", ClassName.get(InetAddress.class));
        MAPPING.put("java.lang.Long", ClassName.get(Long.class));
//        MAPPING.put("java.util.Map", ClassName.get(Map.class)); // analyze generics?
//        MAPPING.put("java.util.Set", ClassName.get(Set.class)); // analyze generics?
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
//        MAPPING.put("java.util.List", ClassName.get(List.class)); // analyze generics?
        MAPPING.put("org.bson.BsonTimestamp", ClassName.get(Long.class));
        MAPPING.put("java.lang.Boolean", ClassName.get(Boolean.class));
//        MAPPING.put("java.util.Iterator", ClassName.get(Iterator.class)); // analyze generics?
        MAPPING.put("java.util.concurrent.TimeUnit", ClassName.get(TimeUnit.class));
        MAPPING.put("java.lang.Object", ClassName.get(Object.class));
        MAPPING.put("java.lang.Class", ClassName.get(Class.class));
        MAPPING.put("org.bson.BsonBinary", TypeName.get(byte[].class));
        MAPPING.put("? extends org.bson.conversions.Bson", ClassName.get(JsonObject.class));
        MAPPING.put("? extends com.mongodb.client.model.WriteModel<? extends TDocument>", ClassName.bestGuess("io.vertx.mongo.BulkOperation"));


        MAPPING2.put("org.bson.Document", ClassName.get(JsonObject.class));
//        mapping.put("javax.net.ssl.SSLContext", "");
        ACCEPTED.add("java.lang.Integer");
        ACCEPTED.add("java.net.InetAddress");
        ACCEPTED.add("java.lang.Long");
        ACCEPTED.add("java.lang.Void");
        ACCEPTED.add("java.util.Map");
        ACCEPTED.add("java.util.List");
        ACCEPTED.add("java.util.Set");
//        MAPPING.put("java.util.Map", ClassName.get(Map.class)); // analyze generics?
//        MAPPING.put("java.util.Set", ClassName.get(Set.class)); // analyze generics?
//        mapping.put("org.bson.UuidRepresentation", ""); // https://mongodb.github.io/mongo-java-driver/3.5/javadoc/org/bson/UuidRepresentation.html
        ACCEPTED.add("java.lang.Throwable");
        ACCEPTED.add("java.lang.Double");
        ACCEPTED.add("java.nio.ByteBuffer");
        MAPPING2.put("org.bson.BsonDocument", ClassName.get(JsonObject.class));
        MAPPING2.put("org.bson.conversions.Bson", ClassName.get(JsonObject.class));
        ACCEPTED.add("java.lang.String");
        MAPPING2.put("org.bson.ByteBuf", TypeName.get(byte[].class));
        MAPPING2.put("org.bson.types.ObjectId", ClassName.get(String.class)); // really?
        ACCEPTED.add("java.util.Date");
        MAPPING2.put("org.bson.BsonValue", ClassName.get(Object.class)); // really?
        MAPPING2.put("org.bson.BsonArray", ClassName.get(JsonArray.class));
        ACCEPTED.add("java.net.InetSocketAddress");
//        MAPPING.put("java.util.List", ClassName.get(List.class)); // analyze generics?
        MAPPING2.put("org.bson.BsonTimestamp", ClassName.get(Long.class));
        MAPPING2.put("org.bson.BsonInt64", ClassName.get(Long.class));
        ACCEPTED.add("java.lang.Boolean");
//        MAPPING.put("java.util.Iterator", ClassName.get(Iterator.class)); // analyze generics?
        ACCEPTED.add("java.util.concurrent.TimeUnit");
        ACCEPTED.add("java.lang.Object");
        ACCEPTED.add("java.lang.Class");
        MAPPING2.put("org.bson.BsonBinary", TypeName.get(byte[].class));
    }

    public static boolean isKnown(String qualifiedTypeName) {
        return MAPPING.containsKey(qualifiedTypeName);
    }

    public static boolean isAcceptedAsIs(String qualifiedTypeName) {
        return ACCEPTED.contains(qualifiedTypeName);
    }

    public static TypeName getMapped(String qualifiedTypeName) {
        return MAPPING.get(qualifiedTypeName);
    }

    public static TypeName getMapped2(String qualifiedTypeName) {
        return MAPPING2.get(qualifiedTypeName);
    }

    public static boolean isIgnored(String qualifiedTypeName) {
        return IGNORED.contains(qualifiedTypeName);
    }

    public static boolean isSupportedSuperClass(String qualifiedTypeName) {
        return SUPPORTED_SUPER_CLASSES.contains(qualifiedTypeName);
    }

}
