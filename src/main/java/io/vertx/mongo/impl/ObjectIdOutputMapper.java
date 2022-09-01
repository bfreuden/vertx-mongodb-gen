package io.vertx.mongo.impl;

import io.vertx.core.json.JsonObject;
import io.vertx.mongo.impl.codec.json.JsonObjectCodec;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;

public class ObjectIdOutputMapper implements Function<JsonObject, JsonObject> {

    @Override
    public JsonObject apply(JsonObject json) {
//        return json;
        if (json == null)
            return null;
        Object _id = json.getValue("_id");
        if (_id instanceof Map || _id instanceof JsonObject) {
            Map<String, Object> map = _id instanceof Map ? (Map<String, Object>) _id : ((JsonObject)_id).getMap();
            String oid = (String) map.get(JsonObjectCodec.OID_FIELD);
            if (oid == null)
                return json;
            LinkedHashMap<String, Object> replacement = new LinkedHashMap<>();
            for (Map.Entry<String, Object> entry: json.getMap().entrySet()) {
                String key = entry.getKey();
                if (key.equals("_id"))
                    replacement.put("_id", oid);
                else
                    replacement.put(entry.getKey(), entry.getValue());
            }
            return new JsonObject(replacement);
        } else {
            return json;
        }
    }

}
