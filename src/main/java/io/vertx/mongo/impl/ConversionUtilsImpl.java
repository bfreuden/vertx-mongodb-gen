package io.vertx.mongo.impl;

import io.vertx.core.json.JsonObject;
import org.bson.*;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.util.List;

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
    public List<? extends Bson> toBsonList(List<JsonObject> from) {
        return null;
    }

    @Override
    public BsonTimestamp toBsonTimestamp(Long from) {
        return null;
    }

    @Override
    public BsonValue toBsonValue(Object from) {
        return null;
    }

    @Override
    public Document toDocument(JsonObject from) {
        return null;
    }

    @Override
    public ObjectId toObjectId(JsonObject from) {
        return null;
    }
}
