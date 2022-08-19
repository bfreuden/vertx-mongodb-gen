/*
 * Copyright 2019 The Vert.x Community.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.vertx.mongo.impl;

import io.vertx.core.Promise;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.Objects;

public class SingleResultSubscriber<T> implements Subscriber<T> {

    private T received;
    protected final MongoClientContext clientContext;
    private final Promise<T> promise;
    private Subscription subscription;
    private boolean completed;

    public SingleResultSubscriber(MongoClientContext clientContext, Promise<T> promise) {
        Objects.requireNonNull(clientContext, "clientContext is null");
        Objects.requireNonNull(promise, "promise is null");
        this.clientContext = clientContext;
        this.promise = promise;
    }

    @Override
    public void onSubscribe(Subscription s) {
        this.subscription = s;
        s.request(1);
    }

    @Override
    public void onNext(T t) {
        if (received == null) {
            received = t;
            completed = true;
            subscription.cancel();
            clientContext.getContext().runOnContext(ar -> {
                promise.complete(received);
            });
        }
    }

    @Override
    public void onError(Throwable t) {
        clientContext.getContext().runOnContext(ar -> {
            promise.fail(t);
        });
    }

    @Override
    public void onComplete() {
        if (!completed) {
            clientContext.getContext().runOnContext(ar -> {
                promise.complete(received);
            });
        }
    }
}
