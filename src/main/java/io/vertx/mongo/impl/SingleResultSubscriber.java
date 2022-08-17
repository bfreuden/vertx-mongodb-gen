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
    private final Promise<T> promise;

    public SingleResultSubscriber(Promise<T> promise) {
        Objects.requireNonNull(promise, "promise is null");
        this.promise = promise;
    }

    @Override
    public void onSubscribe(Subscription s) {
        s.request(Long.MAX_VALUE);
    }

    @Override
    public void onNext(T t) {
        if (received == null) {
            received = t;
        }
    }

    @Override
    public void onError(Throwable t) {
        promise.fail(t);
    }

    @Override
    public void onComplete() {
        promise.complete(received);
    }
}
