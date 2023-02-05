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
import static java.util.Objects.requireNonNull;

import com.mongodb.ExplainVerbosity;
import com.mongodb.reactivestreams.client.AggregatePublisher;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;
import io.vertx.mongo.MongoResult;
import io.vertx.mongo.client.AggregateResult;
import io.vertx.mongo.impl.MappingPublisher;
import io.vertx.mongo.impl.MongoClientContext;
import io.vertx.mongo.impl.MongoResultImpl;
import io.vertx.mongo.impl.SingleResultSubscriber;
import java.lang.Class;
import java.lang.Override;
import java.lang.Void;
import java.util.function.Function;

import org.bson.BsonDocument;
import org.bson.Document;
import org.reactivestreams.Publisher;

public class AggregateResultImpl<TResult> extends MongoResultImpl<TResult> implements AggregateResult<TResult> {
  protected final AggregatePublisher<TResult> wrapped;

  public AggregateResultImpl(MongoClientContext clientContext,
      AggregatePublisher<TResult> wrapped) {
    super(clientContext, wrapped);
    this.wrapped = wrapped;
  }

  public AggregateResultImpl(MongoClientContext clientContext,
      AggregatePublisher<TResult> wrapped, Function<TResult, TResult> mapper) {
    super(clientContext, wrapped, mapper);
    this.wrapped = wrapped;
  }

  public AggregateResultImpl(MongoClientContext clientContext,
      AggregatePublisher<TResult> wrapped, int batchSize) {
    super(clientContext, wrapped, batchSize);
    this.wrapped = wrapped;
  }

  public AggregateResultImpl(MongoClientContext clientContext,
      AggregatePublisher<TResult> wrapped, Function<TResult, TResult> mapper, int batchSize) {
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

  @Override
  public MongoResult<JsonObject> explain() {
    Publisher<JsonObject> __publisher = wrapped.explain(JsonObject.class);
    return new MongoResultImpl<>(clientContext, __publisher, clientContext.getConfig().getOutputMapper());
  }

  @Override
  public MongoResult<JsonObject> explain(ExplainVerbosity verbosity) {
    requireNonNull(verbosity, "verbosity is null");
    Publisher<Document> __publisher = wrapped.explain(verbosity);
    MappingPublisher<Document, JsonObject> _mappingPublisher = new MappingPublisher<>(__publisher, doc -> clientContext.getMapper().toJsonObject(doc));
    return new MongoResultImpl<>(clientContext, _mappingPublisher, clientContext.getConfig().getOutputMapper());
  }

  @Override
  public <E> MongoResult<E> explain(Class<E> explainResultClass) {
    requireNonNull(explainResultClass, "explainResultClass is null");
    Publisher<E> __publisher = wrapped.explain(explainResultClass);
    return new MongoResultImpl<>(clientContext, __publisher);
  }

  @Override
  public <E> MongoResult<E> explain(Class<E> explainResultClass, ExplainVerbosity verbosity) {
    requireNonNull(explainResultClass, "explainResultClass is null");
    requireNonNull(verbosity, "verbosity is null");
    Publisher<E> __publisher = wrapped.explain(explainResultClass, verbosity);
    return new MongoResultImpl<>(clientContext, __publisher);
  }

  public MongoClientContext getClientContext() {
    return clientContext;
  }

  protected Publisher<TResult> firstPublisher() {
    return wrapped.first();
  }
}
