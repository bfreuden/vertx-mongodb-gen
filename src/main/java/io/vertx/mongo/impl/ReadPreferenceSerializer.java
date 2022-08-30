package io.vertx.mongo.impl;

import com.mongodb.ReadPreference;
import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;
import io.vertx.mongo.client.impl.OptionSerializer;

@DataObject
public class ReadPreferenceSerializer extends OptionSerializer<ReadPreference> {

    public ReadPreferenceSerializer(ReadPreference value) {
        super(value);
    }

    public ReadPreferenceSerializer(JsonObject jsonValue) {
        super(jsonValue);
    }

    @Override
    protected void fromJson() {
        throw new IllegalStateException("not implemented");
    }

    @Override
    public JsonObject toJson() {
        return new JsonObject(value.toDocument().toJson());
    }
}
