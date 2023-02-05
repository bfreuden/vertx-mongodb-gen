package io.vertx.mongo.client.impl;

import io.vertx.mongo.client.MongoCollection;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public abstract class MongoCollectionBase<TDocument> implements MongoCollection<TDocument> {

    protected TDocument mapDoc(TDocument doc, Function<TDocument, TDocument> mapper) {
        if (doc == null || mapper == null)
            return doc;
        return mapper.apply(doc);
    }

    protected List<? extends TDocument> mapDocList(List<? extends TDocument> list, Function<TDocument, TDocument> mapper) {
        if (list == null || mapper == null)
            return list;
        List<TDocument> result = null;
        int i = 0;
        for (TDocument item : list) {
            TDocument mapped = mapper.apply(item);
            if(result != null) {
                result.add(mapped);
            } else if (mapped != item) {
                result = new ArrayList<>(list.size());
                result.addAll(list.subList(0, i));
                result.add(mapped);
            }
            i++;
        }
        return result == null ? list : result;
    }

}
