package io.vertx.mongo.impl;

import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.streams.ReadStream;
import io.vertx.mongo.MongoResult;
import org.reactivestreams.Publisher;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;

public class MongoResultImpl<T> implements MongoResult<T> {

    private final Publisher<T> publisher;
    protected final MongoClientContext clientContext;
    private final Function<T, T> mapper;
    private final int batchSize;
    public MongoResultImpl(MongoClientContext clientContext, Publisher<T> publisher) {
        this(clientContext, publisher, null, -1);
    }

    public MongoResultImpl(MongoClientContext clientContext, Publisher<T> publisher, int batchSize) {
        this(clientContext, publisher, null, batchSize);
    }

    public MongoResultImpl(MongoClientContext clientContext, Publisher<T> publisher, Function<T, T> mapper) {
        this(clientContext, publisher, mapper, -1);
    }

    public MongoResultImpl(MongoClientContext clientContext, Publisher<T> publisher, Function<T, T> mapper, int batchSize) {
        Objects.requireNonNull(clientContext, "clientContext is null");
        Objects.requireNonNull(publisher, "publisher is null");
        this.clientContext = clientContext;
        this.publisher = publisher;
        this.mapper = mapper;
        this.batchSize = batchSize;
    }

    protected Publisher<T> firstPublisher() {
        return null;
    }

    @Override
    public Future<T> first() {
        Promise<T> promise = Promise.promise();
        Publisher<T> firstPublisher = firstPublisher();
        SingleResultSubscriber<T> __subscriber = new SingleResultSubscriber<>(clientContext, promise, mapper);
        if (firstPublisher == null)
            publisher.subscribe(__subscriber);
        else
            firstPublisher.subscribe(__subscriber);
        return promise.future();
    }

    @Override
    public Future<List<T>> all() {
        Promise<List<T>> promise = Promise.promise();
        ListResultSubscriber<T> __subscriber = new ListResultSubscriber<>(clientContext, promise, mapper);
        publisher.subscribe(__subscriber);
        return promise.future();
    }

    @Override
    public Future<List<T>> some(int maxItems) {
        if (maxItems < 0)
            throw new IllegalArgumentException("maxItems must be non-negative");
        Promise<List<T>> promise = Promise.promise();
        ListResultSubscriber<T> __subscriber = new ListResultSubscriber<>(clientContext, promise, mapper, maxItems);
        publisher.subscribe(__subscriber);
        return promise.future();
    }

    @Override
    public ReadStream<T> stream() {
        return new PublisherAdapter<>(clientContext.getContext(), publisher, mapper, batchSize != -1 ? batchSize : 1);
    }

    @Override
    public ReadStream<T> stream(int batchSize) {
        if (this.batchSize != -1)
            throw new IllegalStateException("batchSize has already been set using options");
        return new PublisherAdapter<>(clientContext.getContext(), publisher, mapper, batchSize);
    }

}
