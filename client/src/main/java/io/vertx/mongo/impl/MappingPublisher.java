package io.vertx.mongo.impl;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.function.Function;

public class MappingPublisher<I, O> implements Publisher<O> {

    private class MappingSubscriber implements Subscriber<I> {

        private final Subscriber<? super O> mapped;

        public MappingSubscriber(Subscriber<? super O> mapped) {
            this.mapped = mapped;
        }

        @Override
        public void onSubscribe(Subscription subscription) {
            mapped.onSubscribe(subscription);
        }

        @Override
        public void onNext(I i) {
            mapped.onNext(mapper.apply(i));
        }

        @Override
        public void onError(Throwable throwable) {
            mapped.onError(throwable);
        }

        @Override
        public void onComplete() {
            mapped.onComplete();
        }
    }
    private Publisher<I> mapped;
    private final Function<I, O> mapper;

    public MappingPublisher(Publisher<I> mapped, Function<I, O> mapper) {
        this.mapped = mapped;
        this.mapper = mapper;

    }

    @Override
    public void subscribe(Subscriber<? super O> subscriber) {
        mapped.subscribe(new MappingSubscriber(subscriber));
    }
}
