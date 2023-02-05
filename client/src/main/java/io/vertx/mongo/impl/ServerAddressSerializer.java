package io.vertx.mongo.impl;

import com.mongodb.ServerAddress;
import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;
import io.vertx.mongo.client.impl.OptionSerializer;

import java.util.List;

@DataObject
public class ServerAddressSerializer extends OptionSerializer<ServerAddress> {

    public ServerAddressSerializer(ServerAddress value) {
        super(value);
    }

    public ServerAddressSerializer(JsonObject jsonValue) {
        super(jsonValue);
    }


    @Override
    protected void fromJson(JsonObject jsonValue) {
        String host = jsonValue.getString("host");
        Integer port = jsonValue.getInteger("port");
        this.value = port == null ? new ServerAddress(host) : new ServerAddress(host, port);
    }

    @Override
    public JsonObject toJson() {
        JsonObject result = new JsonObject();
        result.put("host", value.getHost());
        if (value.getPort() != 0)
            result.put("port", value.getPort());
        return result;
    }
}
