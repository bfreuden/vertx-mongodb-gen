package io.vertx.mongo.impl;

import com.mongodb.ServerAddress;
import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;
import io.vertx.mongo.client.impl.OptionListSerializer;

import java.util.List;

@DataObject
public class ServerAddressListSerializer extends OptionListSerializer<ServerAddress, ServerAddressSerializer> {

    public ServerAddressListSerializer(List<ServerAddress> value) {
        super(value, ServerAddressSerializer.class, ServerAddress.class);
    }

    public ServerAddressListSerializer(JsonObject jsonValue) {
        super(jsonValue, ServerAddressSerializer.class, ServerAddress.class);
    }

    public JsonObject toJson() {
        return super.toJsonInternal();
    }
}
