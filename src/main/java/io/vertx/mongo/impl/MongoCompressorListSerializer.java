package io.vertx.mongo.impl;

import com.mongodb.MongoCompressor;
import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;
import io.vertx.mongo.client.impl.OptionListSerializer;

import java.util.List;

@DataObject
public class MongoCompressorListSerializer extends OptionListSerializer<MongoCompressor, MongoCompressorSerializer> {

    public MongoCompressorListSerializer(List<MongoCompressor> value) {
        super(value, MongoCompressorSerializer.class, MongoCompressor.class);
    }

    public MongoCompressorListSerializer(JsonObject jsonValue) {
        super(jsonValue, MongoCompressorSerializer.class, MongoCompressor.class);
    }

    @Override
    public JsonObject toJson() {
        return toJsonInternal();
    }
}
