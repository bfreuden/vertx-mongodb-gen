package io.vertx.mongo.client.impl;

import io.vertx.core.json.JsonObject;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public abstract class OptionSerializer<T> {

    protected T value;

    public OptionSerializer(T value) {
        this.value = value;
    }

    public OptionSerializer(JsonObject jsonValue) {
        if (jsonValue != null)
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

    public static <V, S extends OptionSerializer<V>> List<S> toSerializerList(List<V> list, Class<S> serializerClass, Class<V> objectClass) {
        if (list == null)
            return null;
        ArrayList<S> result = new ArrayList<>(list.size());
        for (V item : list) {
            try {
                Constructor<S> ctor = serializerClass.getConstructor(objectClass);
                S serializer = ctor.newInstance(item);
                result.add(serializer);
            } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }
        return result;
    }

    public static  <V, S extends OptionSerializer<V>> List<V> fromSerializerList(List<S> list) {
        if (list == null)
            return null;
        ArrayList<V> result = new ArrayList<>(list.size());
        for (S serializer : list) {
            result.add(serializer.value);
        }
        return result;
    }


}
