//
//  Copyright 2022 The Vert.x Community.
//
//  Licensed under the Apache License, Version 2.0 (the "License");
//  you may not use this file except in compliance with the License.
//  You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
//  Unless required by applicable law or agreed to in writing, software
//  distributed under the License is distributed on an "AS IS" BASIS,
//  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//  See the License for the specific language governing permissions and
//  limitations under the License.
//
package io.vertx.mongo.client.impl;

import static io.vertx.mongo.impl.Utils.setHandler;

import com.mongodb.reactivestreams.client.MapReducePublisher;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Promise;
import io.vertx.mongo.client.MapReduceResult;
import io.vertx.mongo.impl.MongoClientContext;
import io.vertx.mongo.impl.MongoResultImpl;
import io.vertx.mongo.impl.SingleResultSubscriber;
import java.lang.Override;
import java.lang.Void;
import java.util.function.Function;
import org.reactivestreams.Publisher;

public class MapReduceResultImpl<TResult> extends MongoResultImpl<TResult> implements MapReduceResult<TResult> {
  protected final MapReducePublisher<TResult> wrapped;

  public MapReduceResultImpl(MongoClientContext clientContext,
      MapReducePublisher<TResult> wrapped) {
    super(clientContext, wrapped);
    this.wrapped = wrapped;
  }

  public MapReduceResultImpl(MongoClientContext clientContext,
      MapReducePublisher<TResult> wrapped, Function<TResult, TResult> mapper) {
    super(clientContext, wrapped, mapper);
    this.wrapped = wrapped;
  }

  public MapReduceResultImpl(MongoClientContext clientContext,
      MapReducePublisher<TResult> wrapped, int batchSize) {
    super(clientContext, wrapped, batchSize);
    this.wrapped = wrapped;
  }

  public MapReduceResultImpl(MongoClientContext clientContext,
      MapReducePublisher<TResult> wrapped, Function<TResult, TResult> mapper, int batchSize) {
    super(clientContext, wrapped, mapper, batchSize);
    this.wrapped = wrapped;
  }

  @Override
  public Future<Void> toCollection() {
    Publisher<Void> __publisher = wrapped.toCollection();
    Promise<Void> __promise = clientContext.getVertx().promise();
    __publisher.subscribe(new SingleResultSubscriber<>(clientContext, __promise));
    return __promise.future();
  }

  @Override
  public void toCollection(Handler<AsyncResult<Void>> resultHandler) {
    Future<Void> __future = this.toCollection();
    setHandler(__future, resultHandler);
  }

  public MongoClientContext getClientContext() {
    return clientContext;
  }

  protected Publisher<TResult> firstPublisher() {
    return wrapped.first();
  }
}
