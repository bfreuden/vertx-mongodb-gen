package io.vertx.mongo.impl;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;

import java.util.Objects;

public class Utils {

    public static <T> void setHandler(Future<T> future, Handler<AsyncResult<T>> handler) {
        Objects.requireNonNull(future, "future must not be null");
        if (handler != null) {
            future.onComplete(handler);
        }
    }
}
