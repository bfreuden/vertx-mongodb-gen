package io.vertx.mongo.client;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;

public interface MongoAggregateResult<TDocument> extends MongoResult<TDocument> {

    /**
     * Handler called when the aggregation completes
     * @param handler handler
     */
    void toCollection(Handler<AsyncResult<Void>> handler);

    /**
     * Returns a future of the aggregation completeness
     * @return a future of the aggregation completeness
     */
    Future<Void> toCollection();

}
