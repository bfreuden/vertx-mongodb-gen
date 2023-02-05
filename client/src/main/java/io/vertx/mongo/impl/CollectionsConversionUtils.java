package io.vertx.mongo.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class CollectionsConversionUtils {

    public static <I, O> List<O> mapItems(List<I> input, Function<I, O> mapper) {
        return input.stream().map(mapper).collect(Collectors.toList());
    }

    public static <I, O> Set<O> mapItems(Set<I> input, Function<I, O> mapper) {
        return input.stream().map(mapper).collect(Collectors.toSet());
    }

    public static <IK, IV, OK, OV> Map<OK, OV> mapEntries(Map<IK, IV> input, Function<IK, OK> keyMapper, Function<IV, OV> valueMapper) {
        HashMap<OK, OV> result = new HashMap<>();
        for (Map.Entry<IK, IV> entry : input.entrySet()) {
            result.put(keyMapper.apply(entry.getKey()), valueMapper.apply(entry.getValue()));
        }
        return result;
    }

    public static <K, IV, OV> Map<K, OV> mapValues(Map<K, IV> input, Function<IV, OV> valueMapper) {
        HashMap<K, OV> result = new HashMap<>();
        for (Map.Entry<K, IV> entry : input.entrySet()) {
            result.put(entry.getKey(), valueMapper.apply(entry.getValue()));
        }
        return result;
    }

    public static <IK, OK, V> Map<OK, V> mapKeys(Map<IK, V> input, Function<IK, OK> keyMapper) {
        HashMap<OK, V> result = new HashMap<>();
        for (Map.Entry<IK, V> entry : input.entrySet()) {
            result.put(keyMapper.apply(entry.getKey()), entry.getValue());
        }
        return result;
    }

}
