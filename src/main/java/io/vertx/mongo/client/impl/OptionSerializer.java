package io.vertx.mongo.client.impl;

import io.vertx.core.json.JsonObject;

public abstract class OptionSerializer<T> {

    protected T value;

    public OptionSerializer(T value) {
        this.value = value;
    }

    public OptionSerializer(JsonObject jsonValue) {
        this.fromJson(jsonValue);
    }

    protected abstract void fromJson(JsonObject jsonValue);

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public abstract JsonObject toJson();
    
}