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

import static java.util.Objects.requireNonNull;

import com.mongodb.ExplainVerbosity;
import com.mongodb.reactivestreams.client.FindPublisher;
import io.vertx.core.json.JsonObject;
import io.vertx.mongo.MongoResult;
import io.vertx.mongo.client.FindResult;
import io.vertx.mongo.impl.MongoClientContext;
import io.vertx.mongo.impl.MongoResultImpl;
import java.lang.Class;
import java.lang.Override;
import java.util.function.Function;
import org.reactivestreams.Publisher;

public class FindResultImpl<TResult> extends MongoResultImpl<TResult> implements FindResult<TResult> {
  protected final FindPublisher<TResult> wrapped;

  public FindResultImpl(MongoClientContext clientContext, FindPublisher<TResult> wrapped) {
    super(clientContext, wrapped);
    this.wrapped = wrapped;
  }

  public FindResultImpl(MongoClientContext clientContext, FindPublisher<TResult> wrapped,
      Function<TResult, TResult> mapper) {
    super(clientContext, wrapped, mapper);
    this.wrapped = wrapped;
  }

  public FindResultImpl(MongoClientContext clientContext, FindPublisher<TResult> wrapped,
      int batchSize) {
    super(clientContext, wrapped, batchSize);
    this.wrapped = wrapped;
  }

  public FindResultImpl(MongoClientContext clientContext, FindPublisher<TResult> wrapped,
                        Function<TResult, TResult> mapper, int batchSize) {
    super(clientContext, wrapped, mapper, batchSize);
    this.wrapped = wrapped;
  }

  @Override
  public MongoResult<JsonObject> explain() {
    Publisher<JsonObject> __publisher = wrapped.explain(JsonObject.class);
    return new MongoResultImpl<>(clientContext, __publisher, clientContext.getConfig().getOutputMapper());
  }

  @Override
  public MongoResult<JsonObject> explain(ExplainVerbosity verbosity) {
    requireNonNull(verbosity, "verbosity is null");
    Publisher<JsonObject> __publisher = wrapped.explain(JsonObject.class, verbosity);
    return new MongoResultImpl<>(clientContext, __publisher, clientContext.getConfig().getOutputMapper());
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
