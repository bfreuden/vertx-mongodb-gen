package io.vertx.mongo.impl;

import com.mongodb.CreateIndexCommitQuorum;
import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;
import io.vertx.mongo.client.impl.OptionSerializer;

@DataObject
public class CreateIndexCommitQuorumSerializer extends OptionSerializer<CreateIndexCommitQuorum> {
    public CreateIndexCommitQuorumSerializer(CreateIndexCommitQuorum value) {
        super(value);
    }

    public CreateIndexCommitQuorumSerializer(JsonObject jsonValue) {
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
