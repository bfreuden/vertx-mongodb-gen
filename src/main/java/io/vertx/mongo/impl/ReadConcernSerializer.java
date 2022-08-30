package io.vertx.mongo.impl;

import com.mongodb.ReadConcern;
import io.vertx.core.json.JsonObject;
import io.vertx.mongo.client.impl.OptionSerializer;

public class ReadConcernSerializer extends OptionSerializer<ReadConcern> {

    public ReadConcernSerializer(ReadConcern value) {
        super(value);
    }

    public ReadConcernSerializer(JsonObject jsonValue) {
        super(jsonValue);
    }

    @Override
    protected void fromJson(JsonObject jsonValue) {
        //TODO
        throw new IllegalStateException("TODO");
    }

    @Override
    public JsonObject toJson() {
        //TODO
        throw new IllegalStateException("TODO");
    }
}
