package io.vertx.mongo.impl;

import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.streams.ReadStream;
import io.vertx.mongo.MongoResult;
import org.reactivestreams.Publisher;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;

public class MongoResultImpl2<I, O> implements MongoResult<O> {

    private final Publisher<I> publisher;
    protected final MongoClientContext clientContext;
    private final Function<I, O> mapper;
    private final int batchSize;
    public MongoResultImpl2(MongoClientContext clientContext, Publisher<I> publisher, Function<I, O> mapper) {
        this(clientContext, publisher, mapper, -1);
    }

    public MongoResultImpl2(MongoClientContext clientContext, Publisher<I> publisher, Function<I, O> mapper, int batchSize) {
        Objects.requireNonNull(clientContext, "clientContext is null");
        Objects.requireNonNull(publisher, "publisher is null");
        if (batchSize < 0)
            throw new IllegalArgumentException("batchSize must be non-negative");
        this.clientContext = clientContext;
        this.publisher = publisher;
        this.mapper = mapper;
        this.batchSize = batchSize;
    }

    protected Publisher<I> firstPublisher() {
        return null;
    }

    @Override
    public Future<O> first() {
        Promise<O> promise = Promise.promise();
        Publisher<I> firstPublisher = firstPublisher();
        SingleResultSubscriber2<I, O> __subscriber = new SingleResultSubscriber2<>(clientContext, promise, mapper);
        if (firstPublisher == null)
            publisher.subscribe(__subscriber);
        else
            firstPublisher.subscribe(__subscriber);
        return promise.future();
    }

    @Override
    public Future<List<O>> all() {
        Promise<List<O>> promise = Promise.promise();
        ListResultSubscriber2<I, O> __subscriber = new ListResultSubscriber2<>(clientContext, promise, mapper);
        publisher.subscribe(__subscriber);
        return promise.future();
    }

    @Override
    public Future<List<O>> some(int maxItems) {
        if (maxItems < 0)
            throw new IllegalArgumentException("maxItems must be non-negative");
        Promise<List<O>> promise = Promise.promise();
        ListResultSubscriber2<I, O> __subscriber = new ListResultSubscriber2<>(clientContext, promise, mapper, maxItems);
        publisher.subscribe(__subscriber);
        return promise.future();
    }

    @Override
    public ReadStream<O> stream() {
        return new PublisherAdapter2<>(clientContext.getContext(), publisher, mapper, batchSize != -1 ? batchSize : 1);
    }

    @Override
    public ReadStream<O> stream(int batchSize) {
        if (this.batchSize != -1)
            throw new IllegalStateException("batchSize has already been set using options");
        return new PublisherAdapter2<>(clientContext.getContext(), publisher, mapper, batchSize);
    }

}
