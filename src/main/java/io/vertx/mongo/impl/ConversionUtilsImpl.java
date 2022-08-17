package io.vertx.mongo.impl;

import io.vertx.core.json.JsonObject;
import org.bson.BsonBinary;
import org.bson.BsonDocument;
import org.bson.BsonTimestamp;
import org.bson.Document;
import org.bson.conversions.Bson;

public class ConversionUtilsImpl implements ConversionUtils {


    public static final ConversionUtilsImpl INSTANCE = new ConversionUtilsImpl();

    @Override
    public Bson toBson(JsonObject from) {
        return null;
    }

    @Override
    public BsonBinary toBsonBinary(byte[] from) {
        return null;
    }

    @Override
    public BsonDocument toBsonDocument(JsonObject from) {
        return null;
    }

    @Override
    public BsonTimestamp toBsonTimestamp(Long from) {
        return null;
    }

    @Override
    public Document toDocument(JsonObject from) {
        return null;
    }
}
