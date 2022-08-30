package io.vertx.mongo.impl;

import com.mongodb.WriteConcern;
import io.vertx.core.json.JsonObject;
import io.vertx.mongo.client.impl.OptionSerializer;

public class WriteConcernSerializer extends OptionSerializer<WriteConcern> {
    public WriteConcernSerializer(WriteConcern value) {
        super(value);
    }

    public WriteConcernSerializer(JsonObject jsonValue) {
        super(jsonValue);
    }

    @Override
    protected void fromJson() {
        //TODO
        throw new IllegalStateException("TODO");
    }

    @Override
    public JsonObject toJson() {
        //TODO
        throw new IllegalStateException("TODO");
    }
}
