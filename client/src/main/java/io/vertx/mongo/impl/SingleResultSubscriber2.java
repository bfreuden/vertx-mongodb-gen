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
import java.util.function.Function;

public class SingleResultSubscriber2<I, O> implements Subscriber<I> {

    private I received;
    protected final MongoClientContext clientContext;
    private final Promise<O> promise;
    private final Function<I, O> mapper;
    private Subscription subscription;
    private boolean completed;

    //TODO add a isSingleSubscription boolean param to avoid cancelling the subscription for nothing
    //once the first item has been received
    public SingleResultSubscriber2(MongoClientContext clientContext, Promise<O> promise, Function<I, O> mapper) {
        Objects.requireNonNull(clientContext, "clientContext is null");
        Objects.requireNonNull(promise, "promise is null");
        // commented-out on purpose
        // Objects.requireNonNull(mapper, "mapper is null");
        this.clientContext = clientContext;
        this.promise = promise;
        this.mapper = mapper;
    }

    @Override
    public void onSubscribe(Subscription s) {
        this.subscription = s;
        s.request(1);
    }

    @Override
    public void onNext(I i) {
        if (received == null) {
            received = i;
            completed = true;
            subscription.cancel();
            //TODO is it correct to complete the promise on context?
            // why isn't it necessary in the current vertx mongo-client?
            clientContext.getContext().runOnContext(ar -> {
                try {
                    promise.complete(mapper.apply(received));
                } catch (Throwable error) {
                    promise.fail(error);
                    subscription.cancel();
                }
            });
        }
    }

    @Override
    public void onError(Throwable t) {
        //TODO is it correct to fail the promise on context?
        // why isn't it necessary in the current vertx mongo-client?
        clientContext.getContext().runOnContext(ar -> {
            promise.fail(t);
        });
    }

    @Override
    public void onComplete() {
        if (!completed) {
            //TODO is it correct to complete the promise on context?
            // why isn't it necessary in the current vertx mongo-client?
            clientContext.getContext().runOnContext(ar -> {
                promise.complete(null);
            });
        }
    }
}
