package io.vertx.mongo.client.impl;

import io.vertx.core.json.JsonObject;

public abstract class OptionSerializer<T> {

    protected T value;

    public OptionSerializer(T value) {
        this.value = value;
    }

    public OptionSerializer(JsonObject jsonValue) {
        this.fromJson();
    }

    protected abstract void fromJson();

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public abstract JsonObject toJson();
    
}
