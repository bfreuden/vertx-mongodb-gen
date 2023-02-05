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

import io.vertx.core.Context;
import io.vertx.core.Handler;
import io.vertx.core.streams.ReadStream;
import io.vertx.core.streams.impl.InboundBuffer;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscription;

import java.util.Objects;
import java.util.function.Function;

public class PublisherAdapter2<I, O> implements ReadStream<O> {

    private enum State {
        IDLE, STARTED, EXHAUSTED, STOPPED
    }

    private final Context context;
    private final Publisher<I> publisher;
    private final Function<I, O> mapper;
    private final InboundBuffer<I> internalQueue;
    private final int batchSize;

    private State state;
    private int requestedNotReceived, receivedNotDelivered;
    private Handler<O> handler;
    private Handler<Throwable> exceptionHandler;
    private Handler<Void> endHandler;
    private Subscription subscription;

    public PublisherAdapter2(Context context, Publisher<I> publisher, Function<I, O> mapper, int batchSize) {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(publisher, "publisher is null");
        this.context = context;
        this.publisher = publisher;
        this.mapper = mapper;
        this.batchSize = batchSize > 0 ? batchSize : 256;
        internalQueue = new InboundBuffer<I>(context);
        state = State.IDLE;
    }

    @Override
    public synchronized ReadStream<O> exceptionHandler(Handler<Throwable> handler) {
        if (state != State.STOPPED) {
            exceptionHandler = handler;
        }
        return this;
    }

    @Override
    public ReadStream<O> handler(Handler<O> handler) {
        synchronized (this) {
            if (state == State.STOPPED) {
                return this;
            }
        }
        if (handler == null) {
            stop();
            context.runOnContext(v -> handleEnd());
        } else {
            synchronized (this) {
                this.handler = handler;
            }
            internalQueue.handler(this::handleOut);
            boolean subscribe = false;
            synchronized (this) {
                if (state == State.IDLE) {
                    state = State.STARTED;
                    subscribe = true;
                }
            }
            if (subscribe) {
                publisher.subscribe(new Subscriber());
            }
        }
        return this;
    }

    @Override
    public ReadStream<O> pause() {
        synchronized (this) {
            if (state == State.STOPPED) {
                return this;
            }
        }
        internalQueue.pause();
        return this;
    }

    @Override
    public ReadStream<O> resume() {
        synchronized (this) {
            if (state == State.STOPPED) {
                return this;
            }
        }
        internalQueue.resume();
        return this;
    }

    @Override
    public synchronized ReadStream<O> fetch(long amount) {
        synchronized (this) {
            if (state == State.STOPPED) {
                return this;
            }
        }
        internalQueue.fetch(amount);
        return this;
    }

    @Override
    public synchronized ReadStream<O> endHandler(Handler<Void> endHandler) {
        if (state != State.STOPPED) {
            this.endHandler = endHandler;
        }
        return this;
    }

    private void handleIn(I item) {
        synchronized (this) {
            if (state == State.STOPPED) {
                return;
            }
            receivedNotDelivered++;
            requestedNotReceived--;
        }
        internalQueue.write(item);
    }

    private void handleOut(I item) {
        synchronized (this) {
            if (state == State.STOPPED) {
                return;
            }
            receivedNotDelivered--;
        }
        try {
            handler.handle(mapper.apply(item));
        } catch (Throwable error) {
            handleException(error);
            return;
        }
        State s;
        boolean requestMore;
        synchronized (this) {
            if (receivedNotDelivered != 0) {
                return;
            }
            s = state;
            requestMore = requestedNotReceived == 0;
        }
        if (s == State.EXHAUSTED) {
            stop();
            handleEnd();
        } else if (requestMore) {
            requestMore();
        }
    }

    private void handleOnComplete() {
        boolean stop;
        synchronized (this) {
            if (state == State.STOPPED) {
                return;
            }
            state = State.EXHAUSTED;
            stop = receivedNotDelivered == 0;
        }
        if (stop) {
            stop();
            handleEnd();
        }
    }

    private void handleException(Throwable cause) {
        Handler<Throwable> h;
        synchronized (this) {
            h = state != State.STOPPED ? exceptionHandler : null;
        }
        if (h != null) {
            stop();
            h.handle(cause);
        }
    }

    private void requestMore() {
        Subscription s;
        synchronized (this) {
            if (state == State.STOPPED) {
                return;
            }
            s = this.subscription;
            requestedNotReceived += batchSize;
        }
        s.request(batchSize);
    }

    private void handleEnd() {
        Handler<Void> h;
        synchronized (this) {
            h = endHandler;
        }
        if (h != null) {
            h.handle(null);
        }
    }

    private void stop() {
        Subscription s;
        synchronized (this) {
            state = State.STOPPED;
            s = this.subscription;
        }
        internalQueue.handler(null).drainHandler(null);
        if (s != null) {
            s.cancel();
        }
    }

    private class Subscriber implements org.reactivestreams.Subscriber<I> {

        @Override
        public void onSubscribe(Subscription subscription) {
            context.runOnContext(v -> {
                synchronized (PublisherAdapter2.this) {
                    PublisherAdapter2.this.subscription = subscription;
                }
                requestMore();
            });
        }

        @Override
        public void onNext(I i) {
            context.runOnContext(v -> handleIn(i));
        }

        @Override
        public void onError(Throwable t) {
            context.runOnContext(v -> handleException(t));
        }

        @Override
        public void onComplete() {
            context.runOnContext(v -> handleOnComplete());
        }
    }
}