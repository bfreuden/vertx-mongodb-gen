package io.vertx.mongo;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;

import static io.vertx.mongo.impl.Utils.setHandler;

public interface MongoCollectionResult<TDocument> extends MongoResult<TDocument> {

    /**
     * Handler called when the collection operation completes
     * @param resultHandler handler
     */
    default void toCollection(Handler<AsyncResult<Void>> resultHandler) {
        Future<Void> future = this.toCollection();
        setHandler(future, resultHandler);
    };

    /**
     * Returns a future of the collection operation completeness
     * @return a future of the collection operation completeness
     */
    Future<Void> toCollection();

}
