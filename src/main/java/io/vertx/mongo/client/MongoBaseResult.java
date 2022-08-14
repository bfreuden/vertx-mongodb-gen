package io.vertx.mongo.client;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.streams.ReadStream;

import java.util.List;

public interface MongoBaseResult<TDocument> {

    /**
     * Handler called with a list containing all items of the result
     * @param handler handler called with a list containing all items of the result
     */
    void all(Handler<AsyncResult<List<TDocument>>> handler);

    /**
     * Returns a future of the list containing all items of the result
     * @return a future of the list containing all items of the result
     */
    Future<List<TDocument>> all();

    /**
     * Returns the result as a read stream of items
     * @return read stream of items
     */
    ReadStream<TDocument> stream();

}
