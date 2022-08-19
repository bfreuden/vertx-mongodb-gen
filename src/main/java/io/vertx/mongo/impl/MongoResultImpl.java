package io.vertx.mongo.impl;

import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.streams.ReadStream;
import io.vertx.mongo.MongoResult;
import org.reactivestreams.Publisher;

import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

public class MongoResultImpl<TDocument> implements MongoResult<TDocument> {

    private final Publisher<TDocument> publisher;
    protected final MongoClientContext clientContext;
    private final Supplier<Publisher<TDocument>> firstPublisher;
    private final int batchSize;
    public MongoResultImpl(MongoClientContext clientContext, Publisher<TDocument> publisher) {
        Objects.requireNonNull(clientContext, "clientContext is null");
        Objects.requireNonNull(publisher, "publisher is null");
        this.clientContext = clientContext;
        this.publisher = publisher;
        this.firstPublisher = null;
        this.batchSize = -1;
    }

    public MongoResultImpl(MongoClientContext clientContext, Publisher<TDocument> publisher, int batchSize) {
        Objects.requireNonNull(clientContext, "clientContext is null");
        Objects.requireNonNull(publisher, "publisher is null");
        if (batchSize < 0)
            throw new IllegalArgumentException("batchSize must be non-negative");
        this.clientContext = clientContext;
        this.publisher = publisher;
        this.firstPublisher = null;
        this.batchSize = batchSize;
    }

    public MongoResultImpl(MongoClientContext clientContext, Publisher<TDocument> publisher, Supplier<Publisher<TDocument>> firstPublisher) {
        Objects.requireNonNull(clientContext, "clientContext is null");
        Objects.requireNonNull(publisher, "publisher is null");
        Objects.requireNonNull(firstPublisher, "firstPublisher is null");
        this.clientContext = clientContext;
        this.publisher = publisher;
        this.firstPublisher = firstPublisher;
        this.batchSize = -1;
    }

    public MongoResultImpl(MongoClientContext clientContext, Publisher<TDocument> publisher, Supplier<Publisher<TDocument>> firstPublisher, int batchSize) {
        Objects.requireNonNull(clientContext, "clientContext is null");
        Objects.requireNonNull(publisher, "publisher is null");
        Objects.requireNonNull(firstPublisher, "firstPublisher is null");
        if (batchSize < 0)
            throw new IllegalArgumentException("batchSize must be non-negative");
        this.clientContext = clientContext;
        this.publisher = publisher;
        this.firstPublisher = firstPublisher;
        this.batchSize = batchSize;
    }

    @Override
    public Future<TDocument> first() {
        Promise<TDocument> promise = Promise.promise();
        SingleResultSubscriber<TDocument> __subscriber = new SingleResultSubscriber<>(clientContext, promise);
        if (firstPublisher == null)
            publisher.subscribe(__subscriber);
        else
            firstPublisher.get().subscribe(__subscriber);
        return promise.future();
    }

    @Override
    public Future<List<TDocument>> all() {
        Promise<List<TDocument>> promise = Promise.promise();
        ListResultSubscriber<TDocument> __subscriber = new ListResultSubscriber<>(clientContext, promise);
        publisher.subscribe(__subscriber);
        return promise.future();
    }

    @Override
    public Future<List<TDocument>> some(int maxItems) {
        if (maxItems < 0)
            throw new IllegalArgumentException("maxItems must be non-negative");
        Promise<List<TDocument>> promise = Promise.promise();
        ListResultSubscriber<TDocument> __subscriber = new ListResultSubscriber<>(clientContext, promise, maxItems);
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
