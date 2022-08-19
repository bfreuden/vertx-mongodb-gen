package io.vertx.mongo.client;

import io.vertx.codegen.annotations.Nullable;
import io.vertx.core.*;
import io.vertx.core.json.JsonObject;
import io.vertx.core.streams.WriteStream;

public class SimpleTest {

    static MongoClient mongoClient = null;
    public static void main(String[] args) {
        try {
            Vertx vertx = Vertx.vertx();
            mongoClient = MongoClient.create(vertx, new ClientConfig().useObjectIds(true));
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
            // get some items
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
            // count documents of a collection
            mongoClient
                    .getDatabase("cinebio")
                    .getCollection("documents")
                    .countDocuments()
                    .map(res -> "count: " + res)
                    .onFailure(Throwable::printStackTrace)
                    .onSuccess(System.out::println);
            // get first document of a collection
            mongoClient
                    .getDatabase("cinebio")
                    .getCollection("documents")
                    .find()
                    .first()
                    .map(res -> "find: " + res)
                    .onFailure(Throwable::printStackTrace)
                    .onSuccess(System.out::println);

            // test transactions
            MongoCollection<JsonObject> collection = mongoClient.getDatabase("cinebio")
                    .getCollection("documents2");

            mongoClient.startSession()
                    .map(session -> new SessionContext<>(mongoClient, session))
                    .map(SessionContext::startTransaction)
                    .compose(context -> context.withResult(collection.insertOne(context.session(), new JsonObject().put("foo", "bar"))))
                    .compose(ResultSessionContext::commitTransaction)
                    .onSuccess(res -> System.out.println("transaction committed"))
                    .onFailure(Throwable::printStackTrace);

            // hang to wait for results
            synchronized (vertx) {
                vertx.wait();
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    public static class SessionContext<S extends SessionContext<S>> {
        private final MongoClient client;
        private final ClientSession session;
        private boolean aborted = false;

        public SessionContext(MongoClient mongoClient, ClientSession session) {
            this.client = mongoClient;
            this.session = session;
        }

        public MongoClient client() {
            return client;
        }

        public ClientSession session() {
            return session;
        }

        public S startTransaction() {
            session.startTransaction();
            return (S)this;
        }

        public Future<S> commitTransaction() {
            Promise<S> promise = Promise.promise();
            session.commitTransaction().onComplete(ar -> {
                if (ar.failed())
                    promise.fail(ar.cause());
                else
                    promise.complete((S)this);

            });
            return promise.future();
        }

        public Future<S> abortTransaction() {
            Promise<S> promise = Promise.promise();
            session.abortTransaction().onComplete(ar -> {
                if (ar.failed())
                    promise.fail(ar.cause());
                else
                    promise.complete((S)this);

            });
            return promise.future();
        }

        public <T> Future<ResultSessionContext<T>> withResult(Future<T> result) {
            Promise<ResultSessionContext<T>> promise = Promise.promise();
            result.onComplete(ar -> {
                if (ar.failed())
                    promise.fail(ar.cause());
                else
                    promise.complete(new ResultSessionContext<T>(client, session, ar.result()));
            });
            return promise.future();
        }

    }
    public static class ResultSessionContext<T> extends SessionContext<ResultSessionContext<T>> {

        private T result;
        public ResultSessionContext(MongoClient mongoClient, ClientSession session, T result) {
            super(mongoClient, session);
            this.result = result;
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
