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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ListResultSubscriber<T> implements Subscriber<T> {

    private final List<T> received = new ArrayList<>();
    private final MongoClientContext clientContext;
    private final Promise<List<T>> promise;
    private final int maxItems;
    private Subscription subscription;
    private boolean completed;

    public ListResultSubscriber(MongoClientContext clientContext, Promise<List<T>> promise) {
        Objects.requireNonNull(clientContext, "clientContext is null");
        Objects.requireNonNull(promise, "promise is null");
        this.clientContext = clientContext;
        this.promise = promise;
        this.maxItems = -1;
    }

    public ListResultSubscriber(MongoClientContext clientContext, Promise<List<T>> promise, int maxItems) {
        Objects.requireNonNull(clientContext, "clientContext is null");
        Objects.requireNonNull(promise, "promise is null");
        if (maxItems < 0)
            throw new IllegalArgumentException("maxItems must be non-negative");
        this.clientContext = clientContext;
        this.promise = promise;
        this.maxItems = maxItems;
    }

    @Override
    public void onSubscribe(Subscription s) {
        s.request(maxItems != -1 ? maxItems : Long.MAX_VALUE);
        this.subscription = s;
    }

    @Override
    public void onNext(T t) {
        received.add(t);
        if (maxItems != -1 && received.size() >= maxItems) {
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
