package io.vertx.mongo.client;

import com.mongodb.reactivestreams.client.MongoClients;
import io.vertx.core.Context;
import io.vertx.core.Vertx;
import io.vertx.mongo.client.impl.MongoClientImpl;
import io.vertx.mongo.impl.MongoClientContext;

public class SimpleTest {

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        Context context = vertx.getOrCreateContext();
        MongoClientImpl mongoClient = new MongoClientImpl(new MongoClientContext(vertx, context), MongoClients.create());
        mongoClient
                .listDatabaseNames()
                .first()
                .onFailure(Throwable::printStackTrace)
                .onSuccess(System.out::println);

    }
}
