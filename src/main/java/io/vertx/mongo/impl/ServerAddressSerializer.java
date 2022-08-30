package io.vertx.mongo.impl;

import com.mongodb.ServerAddress;
import io.vertx.core.json.JsonObject;
import io.vertx.mongo.client.impl.OptionSerializer;

public class ServerAddressSerializer extends OptionSerializer<ServerAddress> {

    public ServerAddressSerializer(ServerAddress value) {
        super(value);
    }

    public ServerAddressSerializer(JsonObject jsonValue) {
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
