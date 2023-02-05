package io.vertx.mongo;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.streams.ReadStream;

import java.util.List;

import static io.vertx.mongo.impl.Utils.setHandler;

public interface MongoResult<TDocument> {

    /**
     * Handler called with the first item of the result
     * @param resultHandler handler
     */
    default void first(Handler<AsyncResult<TDocument>> resultHandler)  {
        Future<TDocument> future = this.first();
        setHandler(future, resultHandler);
    }

    /**
     * Returns a future of the first item of the result
     * @return a future of the first item of the result
     */
    Future<TDocument> first();


    /**
     * Handler called with a list containing all items of the result
     * @param resultHandler handler called with a list containing all items of the result
     */
    default void all(Handler<AsyncResult<List<TDocument>>> resultHandler) {
        Future<List<TDocument>> future = this.all();
        setHandler(future, resultHandler);
    }

    /**
     * Returns a future of the list containing all items of the result
     * @return a future of the list containing all items of the result
     */
    Future<List<TDocument>> all();

    /**
     * Handler called with a list containing some items of the result
     * @param maxItems maximum number of items
     * @param resultHandler handler called with a list containing some items of the result
     */
    default void some(int maxItems, Handler<AsyncResult<List<TDocument>>> resultHandler) {
        Future<List<TDocument>> future = this.some(maxItems);
        setHandler(future, resultHandler);
    }

    /**
     * Returns a future of the list containing some items of the result
     * @param maxItems maximum number of items
     * @return a future of the list containing some items of the result
     */
    Future<List<TDocument>> some(int maxItems);

    /**
     * Returns the result as a read stream of items
     * @return read stream of items
     */
    ReadStream<TDocument> stream();

    /**
     * Returns the result as a read stream of items
     * @param batchSize batch size
     * @return read stream of items
     */
    ReadStream<TDocument> stream(int batchSize);

}
