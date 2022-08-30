package io.vertx.mongo.impl;

import com.mongodb.MongoCredential;
import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;
import io.vertx.mongo.client.impl.OptionSerializer;

@DataObject
public class MongoCredentialSerializer extends OptionSerializer<MongoCredential> {
    public MongoCredentialSerializer(MongoCredential value) {
        super(value);
    }

    public MongoCredentialSerializer(JsonObject jsonValue) {
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
