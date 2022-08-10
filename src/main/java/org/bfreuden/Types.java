package org.bfreuden;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Types {

    private static Map<String, String> MAPPING = new HashMap<>();
    private static Set<String> IGNORED = new HashSet<>();

    static {
        MAPPING.put("org.bson.Document", JsonObject.class.getName());
//        mapping.put("javax.net.ssl.SSLContext", "");
        MAPPING.put("java.lang.Integer", "");
        MAPPING.put("java.net.InetAddress", "");
        MAPPING.put("java.lang.Long", "");
        MAPPING.put("java.util.Map", "");
        MAPPING.put("java.util.Set", "");
//        mapping.put("org.bson.UuidRepresentation", ""); // https://mongodb.github.io/mongo-java-driver/3.5/javadoc/org/bson/UuidRepresentation.html
        MAPPING.put("java.lang.Throwable", "");
        MAPPING.put("java.lang.Double", "");
        IGNORED.add("org.bson.codecs.configuration.CodecRegistry");
        MAPPING.put("org.bson.BsonDocument", JsonObject.class.getName());
        MAPPING.put("org.bson.conversions.Bson", JsonObject.class.getName());
        MAPPING.put("java.lang.String", "");
        MAPPING.put("org.bson.ByteBuf", "");
//        mapping.put("org.reactivestreams.Publisher", "");
        MAPPING.put("org.bson.types.ObjectId", JsonObject.class.getName()); // really?
        MAPPING.put("java.util.Date", "");
        IGNORED.add("com.mongodb.internal.async.client.AsyncClientSession");
        MAPPING.put("org.bson.BsonValue", "java.lang.Object");
        MAPPING.put("org.bson.BsonArray", JsonArray.class.getName());
        MAPPING.put("java.net.InetSocketAddress", "");
        MAPPING.put("java.util.List", "");
        MAPPING.put("org.bson.BsonTimestamp", "long");
        MAPPING.put("java.lang.Boolean", "");
        MAPPING.put("java.util.Iterator", "");
        MAPPING.put("java.util.concurrent.TimeUnit", "");
        MAPPING.put("java.lang.Object", "");
        MAPPING.put("java.lang.Class", "java.lang.Class");
        MAPPING.put("org.bson.BsonBinary", "byte[]");
        MAPPING.put("? extends org.bson.conversions.Bson", JsonObject.class.getName());
        MAPPING.put("? extends com.mongodb.client.model.WriteModel<? extends TDocument>", "io.vertx.mongo.BulkOperation");
    }

    public static boolean isKnown(String qualifiedTypeName) {
        return MAPPING.containsKey(qualifiedTypeName);
    }

    public static String getMapped(String qualifiedTypeName) {
        String mapped = MAPPING.get(qualifiedTypeName);
        return mapped.isEmpty() ? qualifiedTypeName : mapped;
    }

    public static boolean isIgnored(String qualifiedTypeName) {
        return IGNORED.contains(qualifiedTypeName);
    }

}
