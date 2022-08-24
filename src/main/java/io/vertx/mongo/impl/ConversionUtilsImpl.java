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
    public List<Bson> toBsonList(List<JsonObject> from) {
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
    public List<BulkWriteInsert> toBulkWriteInsertList(List<com.mongodb.bulk.BulkWriteInsert> from) {
        return null;
    }

    @Override
    public List<BulkWriteUpsert> toBulkWriteUpsertList(List<com.mongodb.bulk.BulkWriteUpsert> from) {
        return null;
    }

    @Override
    public byte[] toByteArray(BsonBinary from) {
        return new byte[0];
    }

    @Override
    public DeleteOptions toDeleteOptions(io.vertx.mongo.client.model.DeleteOptions from) {
        return null;
    }

    @Override
    public Document toDocument(JsonObject from) {
        return null;
    }

    @Override
    public List<IndexModel> toIndexModelList(List<io.vertx.mongo.client.model.IndexModel> from) {
        return null;
    }

    @Override
    public IndexOptions toIndexOptions(io.vertx.mongo.client.model.IndexOptions from) {
        return null;
    }

    @Override
    public Map<Integer, Object> toIntegerObjectMap(Map<Integer, BsonValue> from) {
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
    public JsonObject toJsonObject(ObjectId from) {
        return null;
    }

    @Override
    public Object toObject(BsonValue from) {
        return null;
    }

    @Override
    public ObjectId toObjectId(JsonObject from) {
        return null;
    }

    @Override
    public ReplaceOptions toReplaceOptions(io.vertx.mongo.client.model.ReplaceOptions from) {
        return null;
    }

    @Override
    public UpdateOptions toUpdateOptions(io.vertx.mongo.client.model.UpdateOptions from) {
        return null;
    }

    @Override
    public <T> List<WriteModel<T>> toWriteModelList(List<io.vertx.mongo.client.model.WriteModel<T>> from) {
        return null;
    }

    @Override
    public Long toLong(BsonTimestamp clusterTime) {
        return null;
    }

    @Override
    public Long toLong(BsonInt64 clusterTime) {
        return null;
    }
}
