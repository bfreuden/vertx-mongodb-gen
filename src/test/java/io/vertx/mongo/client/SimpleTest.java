package io.vertx.mongo.client;

import com.mongodb.reactivestreams.client.MongoClients;
import io.vertx.codegen.annotations.Nullable;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.impl.ContextInternal;
import io.vertx.core.streams.WriteStream;
import io.vertx.mongo.client.impl.MongoClientImpl;
import io.vertx.mongo.impl.MongoClientContext;

public class SimpleTest {

    public static void main(String[] args) {
        try {
            Vertx vertx = Vertx.vertx();
            ContextInternal context = (ContextInternal) vertx.getOrCreateContext();
            MongoClientImpl mongoClient = new MongoClientImpl(new MongoClientContext(vertx, context), MongoClients.create());
            // get first item
            mongoClient
                    .listDatabaseNames()
                    .first()
                    .map(res -> "first: " + res)
                    .onFailure(Throwable::printStackTrace)
                    .onSuccess(System.out::println);
            // get all items
            mongoClient
                    .listDatabaseNames()
                    .all()
                    .map(res -> "all: " + res)
                    .onFailure(Throwable::printStackTrace)
                    .onSuccess(System.out::println);
            mongoClient
                    .listDatabaseNames()
                    .some(5)
                    .map(res -> "some: " + res)
                    .onFailure(Throwable::printStackTrace)
                    .onSuccess(System.out::println);
            // get stream of items
            mongoClient
                    .listDatabaseNames()
                    .stream().pipeTo(new PrintlnWriteStream<>());
            // count documents of a collections
            mongoClient
                    .getDatabase("cinebio")
                    .getCollection("documents")
                    .countDocuments()
                    .map(res -> "count: " + res)
                    .onFailure(Throwable::printStackTrace)
                    .onSuccess(System.out::println);
            // hang to wait for results
            synchronized (vertx) {
                vertx.wait();
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }


}

class PrintlnWriteStream<T> implements WriteStream<T> {
    @Override
    public WriteStream<T> exceptionHandler(@Nullable Handler<Throwable> handler) {
        return null;
    }

    @Override
    public Future<Void> write(T s) {
        System.out.println(s);
        return Future.succeededFuture();
    }

    @Override
    public void write(T s, Handler<AsyncResult<Void>> handler) {
        System.out.println(s);
        handler.handle(Future.succeededFuture());
    }

    @Override
    public void end(Handler<AsyncResult<Void>> handler) {
        handler.handle(Future.succeededFuture());
    }

    @Override
    public WriteStream<T> setWriteQueueMaxSize(int i) {
        return null;
    }

    @Override
    public boolean writeQueueFull() {
        return false;
    }

    @Override
    public WriteStream<T> drainHandler(@Nullable Handler<Void> handler) {
        return null;
    }
}
