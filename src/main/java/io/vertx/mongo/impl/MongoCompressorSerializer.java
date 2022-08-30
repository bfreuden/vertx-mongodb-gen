package io.vertx.mongo.impl;

import com.mongodb.MongoCompressor;
import io.vertx.core.json.JsonObject;
import io.vertx.mongo.client.impl.OptionSerializer;

public class MongoCompressorSerializer extends OptionSerializer<MongoCompressor> {

    public MongoCompressorSerializer(MongoCompressor value) {
        super(value);
    }

    public MongoCompressorSerializer(JsonObject jsonValue) {
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
