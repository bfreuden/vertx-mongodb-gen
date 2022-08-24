package io.vertx.mongo.impl;

import com.mongodb.client.model.*;
import io.vertx.core.json.JsonObject;
import io.vertx.mongo.bulk.BulkWriteInsert;
import io.vertx.mongo.bulk.BulkWriteUpsert;
import org.bson.*;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.util.List;
import java.util.Map;

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
    public BsonInt64 toBsonInt64(Long from) {
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
    public byte[] toByteArray(BsonBinary from) {
        return new byte[0];
    }

    @Override
    public Document toDocument(JsonObject from) {
        return null;
    }

    @Override
    public JsonObject toJsonObject(Bson from) {
        return null;
    }

    @Override
    public JsonObject toJsonObject(Document from) {
        return null;
    }

    @Override
    public JsonObject toJsonObject(BsonDocument from) {
        return null;
    }

    @Override
    public Long toLong(BsonTimestamp from) {
        return null;
    }

    @Override
    public Long toLong(BsonInt64 from) {
        return null;
    }

    @Override
    public Object toObject(BsonValue from) {
        return null;
    }

    @Override
    public ObjectId toObjectId(String from) {
        return null;
    }

    @Override
    public String toString(ObjectId from) {
        return null;
    }
}
