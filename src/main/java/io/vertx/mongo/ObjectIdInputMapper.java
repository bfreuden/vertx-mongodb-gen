package io.vertx.mongo;

import io.vertx.core.json.JsonObject;
import io.vertx.mongo.impl.codec.json.JsonObjectCodec;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;

public class ObjectIdInputMapper implements Function<JsonObject, JsonObject> {

    @Override
    public JsonObject apply(JsonObject json) {
        if (json == null)
            return null;
        Object _id = json.getValue("_id");
        if (_id instanceof String) {
            Map<String, Object> map = json.getMap();
            LinkedHashMap<String, Object> replacement = new LinkedHashMap<>();
            for (Map.Entry<String, Object> entry: map.entrySet()) {
                String key = entry.getKey();
                if (key.equals("_id"))
                    replacement.put("_id", new JsonObject().put(JsonObjectCodec.OID_FIELD, _id));
                else
                    replacement.put(entry.getKey(), entry.getValue());
            }
            return new JsonObject(replacement);
        } else {
            return json;
        }
    }

}
