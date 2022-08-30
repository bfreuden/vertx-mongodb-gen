package io.vertx.mongo.impl;

import com.mongodb.CreateIndexCommitQuorum;
import io.vertx.core.json.JsonObject;
import io.vertx.mongo.client.impl.OptionSerializer;

public class CreateIndexCommitQuorumSerializer extends OptionSerializer<CreateIndexCommitQuorum> {
    public CreateIndexCommitQuorumSerializer(CreateIndexCommitQuorum value) {
        super(value);
    }

    public CreateIndexCommitQuorumSerializer(JsonObject jsonValue) {
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
