package io.vertx.mongo.impl;

import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.mongo.MongoCollectionResult;
import org.reactivestreams.Publisher;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

public class MongoCollectionResultImpl<TDocument> extends MongoResultImpl<TDocument> implements MongoCollectionResult<TDocument> {

    private final Supplier<Publisher<Void>> toCollectionPublisher;

    public MongoCollectionResultImpl(Supplier<Publisher<Void>> toCollectionPublisher, MongoClientContext clientContext, Publisher<TDocument> publisher) {
        super(clientContext, publisher);
        Objects.requireNonNull(toCollectionPublisher, "toCollectionPublisher is null");
        this.toCollectionPublisher = toCollectionPublisher;
    }

    public MongoCollectionResultImpl(Supplier<Publisher<Void>> toCollectionPublisher, MongoClientContext clientContext, Publisher<TDocument> publisher, int batchSize) {
        super(clientContext, publisher, batchSize);
        Objects.requireNonNull(toCollectionPublisher, "toCollectionPublisher is null");
        this.toCollectionPublisher = toCollectionPublisher;
    }

    public MongoCollectionResultImpl(Supplier<Publisher<Void>> toCollectionPublisher, MongoClientContext clientContext, Publisher<TDocument> publisher, Supplier<Publisher<TDocument>> firstPublisher) {
        super(clientContext, publisher, firstPublisher);
        Objects.requireNonNull(toCollectionPublisher, "toCollectionPublisher is null");
        this.toCollectionPublisher = toCollectionPublisher;
    }

    public MongoCollectionResultImpl(Supplier<Publisher<Void>> toCollectionPublisher, MongoClientContext clientContext, Publisher<TDocument> publisher, Supplier<Publisher<TDocument>> firstPublisher, int batchSize) {
        super(clientContext, publisher, firstPublisher, batchSize);
        Objects.requireNonNull(toCollectionPublisher, "toCollectionPublisher is null");
        this.toCollectionPublisher = toCollectionPublisher;
    }


    public MongoCollectionResultImpl(Supplier<Publisher<Void>> toCollectionPublisher, MongoClientContext clientContext, Publisher<TDocument> publisher, Function<TDocument, TDocument> mapper) {
        super(clientContext, publisher, mapper);
        Objects.requireNonNull(toCollectionPublisher, "toCollectionPublisher is null");
        this.toCollectionPublisher = toCollectionPublisher;
    }

    public MongoCollectionResultImpl(Supplier<Publisher<Void>> toCollectionPublisher, MongoClientContext clientContext, Publisher<TDocument> publisher, Function<TDocument, TDocument> mapper, int batchSize) {
        super(clientContext, publisher, mapper, batchSize);
        Objects.requireNonNull(toCollectionPublisher, "toCollectionPublisher is null");
        this.toCollectionPublisher = toCollectionPublisher;
    }

    public MongoCollectionResultImpl(Supplier<Publisher<Void>> toCollectionPublisher, MongoClientContext clientContext, Publisher<TDocument> publisher, Function<TDocument, TDocument> mapper, Supplier<Publisher<TDocument>> firstPublisher) {
        super(clientContext, publisher, mapper, firstPublisher);
        Objects.requireNonNull(toCollectionPublisher, "toCollectionPublisher is null");
        this.toCollectionPublisher = toCollectionPublisher;
    }

    public MongoCollectionResultImpl(Supplier<Publisher<Void>> toCollectionPublisher, MongoClientContext clientContext, Publisher<TDocument> publisher, Function<TDocument, TDocument> mapper, Supplier<Publisher<TDocument>> firstPublisher, int batchSize) {
        super(clientContext, publisher, mapper, firstPublisher, batchSize);
        Objects.requireNonNull(toCollectionPublisher, "toCollectionPublisher is null");
        this.toCollectionPublisher = toCollectionPublisher;
    }

    @Override
    public Future<Void> toCollection() {
        Promise<Void> promise = Promise.promise();
        SingleResultSubscriber<Void> __subscriber = new SingleResultSubscriber<>(clientContext, promise);
        toCollectionPublisher.get().subscribe(__subscriber);
        return promise.future();
    }
}
