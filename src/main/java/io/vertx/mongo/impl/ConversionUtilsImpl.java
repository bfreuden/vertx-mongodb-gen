package io.vertx.mongo.impl;

import com.mongodb.client.model.*;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.mongo.bulk.BulkWriteInsert;
import io.vertx.mongo.bulk.BulkWriteUpsert;
import org.bson.*;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ConversionUtilsImpl implements ConversionUtils {


    public static final ConversionUtilsImpl INSTANCE = new ConversionUtilsImpl();

    @Override
    public Bson toBson(JsonObject from) {
        return new JsonObjectBsonAdapter(from);
    }

    @Override
    public BsonBinary toBsonBinary(byte[] from) {
        return new BsonBinary(BsonBinarySubType.BINARY, from);
    }

    @Override
    public BsonDocument toBsonDocument(JsonObject from) {
        throw new IllegalStateException("not implemented");
    }

    @Override
    public BsonInt64 toBsonInt64(Long from) {
        return new BsonInt64(from);
    }

    @Override
    public List<Bson> toBsonList(JsonArray from) {
        ArrayList<Bson> result = new ArrayList<>();
        for (int i=0 ; i<from.size() ; i++) {
            JsonObject jsonObject = from.getJsonObject(i);
            result.add(toBson(jsonObject));
        }
        return result;
    }

    @Override
    public BsonTimestamp toBsonTimestamp(Long from) {
        return new BsonTimestamp(from);
    }

    @Override
    public BsonValue toBsonValue(Object from) {
        throw new IllegalStateException("not implemented");
    }

    @Override
    public byte[] toByteArray(BsonBinary from) {
        return from.getData();
    }

    @Override
    public Document toDocument(JsonObject from) {
        throw new IllegalStateException("not implemented");
    }

    @Override
    public JsonObject toJsonObject(Bson from) {
        throw new IllegalStateException("not implemented");
    }

    @Override
    public JsonObject toJsonObject(Document from) {
        throw new IllegalStateException("not implemented");
    }

    @Override
    public JsonObject toJsonObject(BsonDocument from) {
        throw new IllegalStateException("not implemented");
    }

    @Override
    public Long toLong(BsonTimestamp from) {
        return from.getValue();
    }

    @Override
    public Long toLong(BsonInt64 from) {
        return from.getValue();
    }

    @Override
    public Object toObject(BsonValue from) {
        if (from == null)
            return null;
        if (from instanceof BsonObjectId) {
            return ((BsonObjectId)from).getValue().toHexString();
        } else if (from instanceof BsonString) {
            return ((BsonString)from).getValue();
        } else if (from instanceof BsonInt64) {
            return ((BsonInt64)from).getValue();
        } else if (from instanceof BsonInt32) {
            return ((BsonInt32)from).getValue();
        }
        throw new IllegalStateException("not implemented: " + from.getClass());
    }

    @Override
    public ObjectId toObjectId(String from) {
        return new ObjectId(from);
    }

    @Override
    public String toString(ObjectId from) {
        return from.toHexString();
    }
}
