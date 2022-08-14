package io.vertx.mongo.client;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;

public interface MongoResult<TDocument> extends MongoBaseResult<TDocument> {

    /**
     * Handler called with the first item of the result
     * @param handler handler
     */
    void first(Handler<AsyncResult<TDocument>> handler);

    /**
     * Returns a future of the first item of the result
     * @return a future of the first item of the result
     */
    Future<TDocument> first();

}
