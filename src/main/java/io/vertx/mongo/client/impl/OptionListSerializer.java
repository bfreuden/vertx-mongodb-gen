package io.vertx.mongo.client.impl;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public abstract class OptionListSerializer<T, S extends OptionSerializer<T>> extends OptionSerializer<List<T>> {

    private final Class<S> serializerClass;
    private final Class<T> objectClass;

    public OptionListSerializer(List<T> value, Class<S> serializerClass, Class<T> objectClass) {
        super(value);
        this.serializerClass = serializerClass;
        this.objectClass = objectClass;
    }

    public OptionListSerializer(JsonObject jsonValue, Class<S> serializerClass, Class<T> objectClass) {
        super((JsonObject) null);
        this.serializerClass = serializerClass;
        this.objectClass = objectClass;
        fromJson(jsonValue);
    }

    protected void fromJson(JsonObject jsonValue) {
        JsonArray list = jsonValue.getJsonArray("list");
        this.value = new ArrayList<>(list.size());
        for (int i=0 ; i<list.size() ; i++) {
            JsonObject jsonObject = list.getJsonObject(i);
            try {
                Constructor<S> ctor = serializerClass.getDeclaredConstructor(JsonObject.class);
                S serializer = ctor.newInstance(jsonObject);
                serializer.fromJson(jsonObject);
                this.value.add(serializer.getValue());
            } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }
    };

    public List<T> getValue() {
        return value;
    }

    public void setValue(List<T> value) {
        this.value = value;
    }

    protected JsonObject toJsonInternal() {
        JsonObject result = new JsonObject();
        JsonArray list = new JsonArray();
        result.put("list", list);
        for (T val : value) {
            try {
                Constructor<S> ctor = serializerClass.getDeclaredConstructor(objectClass);
                S serializer = ctor.newInstance(val);
                list.add(serializer.toJson());
            } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }
        return result;
    }

}
