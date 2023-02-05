package io.vertx.mongo.client.impl;

import io.vertx.core.json.JsonObject;
import io.vertx.mongo.impl.ConversionUtilsImpl;
import io.vertx.mongo.impl.codec.json.JsonObjectCodec;
import org.bson.BsonValue;

import java.util.function.Function;

public class JsonObjectIdProvider implements Function<JsonObject, BsonValue> {

    private final boolean useObjectIds;

    public JsonObjectIdProvider(boolean useObjectIds) {
        this.useObjectIds = useObjectIds;
    }

    @Override
    public BsonValue apply(JsonObject document) {
        JsonObject id;
        if (useObjectIds) {
            id = document.getJsonObject("_id");
        } else {
            Object sid = document.getValue("_id");
            if (!(sid instanceof String))
                throw new IllegalArgumentException("document is expected to have a string _id");
            id = new JsonObject().put(JsonObjectCodec.OID_FIELD, sid);
        }
        return ConversionUtilsImpl.toBsonValueInternal(id);
    }
}
