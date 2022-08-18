package io.vertx.mongo.client.impl;

import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.streams.ReadStream;
import io.vertx.mongo.client.MongoResult;
import io.vertx.mongo.impl.ListResultSubscriber;
import io.vertx.mongo.impl.MongoClientContext;
import io.vertx.mongo.impl.PublisherAdapter;
import io.vertx.mongo.impl.SingleResultSubscriber;
import org.reactivestreams.Publisher;

import java.util.List;
import java.util.Objects;

public class MongoResultImpl<TDocument> implements MongoResult<TDocument> {

    private final Publisher<TDocument> publisher;
    private final MongoClientContext clientContext;
    private final int batchSize;
    public MongoResultImpl(MongoClientContext clientContext, Publisher<TDocument> publisher) {
        Objects.requireNonNull(clientContext, "clientContext is null");
        Objects.requireNonNull(publisher, "publisher is null");
        this.clientContext = clientContext;
        this.publisher = publisher;
        this.batchSize = -1;
    }

    public MongoResultImpl(MongoClientContext clientContext, Publisher<TDocument> publisher, int batchSize) {
        Objects.requireNonNull(clientContext, "clientContext is null");
        Objects.requireNonNull(publisher, "publisher is null");
        if (batchSize < 0)
            throw new IllegalArgumentException("batchSize must be non-negative");
        this.clientContext = clientContext;
        this.publisher = publisher;
        this.batchSize = batchSize;
    }

    @Override
    public Future<TDocument> first() {
        Promise<TDocument> promise = Promise.promise();
        SingleResultSubscriber<TDocument> __subscriber = new SingleResultSubscriber<>(promise);
        publisher.subscribe(__subscriber);
        return promise.future();
    }

    @Override
    public Future<List<TDocument>> all() {
        Promise<List<TDocument>> promise = Promise.promise();
        ListResultSubscriber<TDocument> __subscriber = new ListResultSubscriber<>(promise);
        publisher.subscribe(__subscriber);
        return promise.future();
    }

    @Override
    public Future<List<TDocument>> some(int maxItems) {
        if (maxItems < 0)
            throw new IllegalArgumentException("maxItems must be non-negative");
        Promise<List<TDocument>> promise = Promise.promise();
        ListResultSubscriber<TDocument> __subscriber = new ListResultSubscriber<>(promise, maxItems);
        publisher.subscribe(__subscriber);
        return promise.future();
    }

    @Override
    public ReadStream<TDocument> stream() {
        return new PublisherAdapter<>(clientContext.getContext(), publisher, batchSize != -1 ? batchSize : 1);
    }

    @Override
    public ReadStream<TDocument> stream(int batchSize) {
        if (this.batchSize != -1)
            throw new IllegalStateException("batchSize has already been set using options");
        return new PublisherAdapter<>(clientContext.getContext(), publisher, batchSize);
    }


}
