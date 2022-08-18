package io.vertx.mongo.client;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.mongo.MongoResult;

public interface MongoCollectionResult<TDocument> extends MongoResult<TDocument> {

    /**
     * Handler called when the collection operation completes
     * @param handler handler
     */
    void toCollection(Handler<AsyncResult<Void>> handler);

    /**
     * Returns a future of the collection operation completeness
     * @return a future of the collection operation completeness
     */
    Future<Void> toCollection();

}
