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

    private static Map<String, TypeName> MAPPING2 = new HashMap<>();
    private static Set<String> IGNORED = new HashSet<>();
    private static Set<String> ACCEPTED = new HashSet<>();

    static {
        IGNORED.add("org.bson.codecs.configuration.CodecRegistry");
        IGNORED.add("com.mongodb.internal.async.client.AsyncClientSession");


        MAPPING2.put("org.bson.Document", ClassName.get(JsonObject.class));
        ACCEPTED.add("java.lang.Integer");
        ACCEPTED.add("java.net.InetAddress");
        ACCEPTED.add("java.lang.Long");
        ACCEPTED.add("java.lang.Void");
        ACCEPTED.add("java.util.Map");
        ACCEPTED.add("java.util.List");
        ACCEPTED.add("java.util.Set");
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
        MAPPING2.put("org.bson.BsonTimestamp", ClassName.get(Long.class));
        MAPPING2.put("org.bson.BsonInt64", ClassName.get(Long.class));
        ACCEPTED.add("java.lang.Boolean");
        ACCEPTED.add("java.util.concurrent.TimeUnit");
        ACCEPTED.add("java.lang.Object");
        ACCEPTED.add("java.lang.Class");
        MAPPING2.put("org.bson.BsonBinary", TypeName.get(byte[].class));
    }

    public static boolean isAcceptedAsIs(String qualifiedTypeName) {
        return ACCEPTED.contains(qualifiedTypeName);
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
