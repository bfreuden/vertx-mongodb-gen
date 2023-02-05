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
    protected void fromJson(JsonObject jsonValue) {
        if (jsonValue.size() != 1)
            throw new IllegalStateException("not implemented");
        String mode = jsonValue.getString("mode");
        if (mode == null)
            throw new IllegalStateException("not implemented");
        this.value = ReadPreference.valueOf(mode);
    }

    @Override
    public JsonObject toJson() {
        return new JsonObject(value.toDocument().toJson());
    }
}
