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

import com.mongodb.reactivestreams.client.DistinctPublisher;
import io.vertx.mongo.client.DistinctResult;
import io.vertx.mongo.impl.MongoClientContext;
import io.vertx.mongo.impl.MongoResultImpl;
import java.util.function.Function;
import org.reactivestreams.Publisher;

public class DistinctResultImpl<TResult> extends MongoResultImpl<TResult> implements DistinctResult<TResult> {
  protected final DistinctPublisher<TResult> wrapped;

  public DistinctResultImpl(MongoClientContext clientContext,
      DistinctPublisher<TResult> wrapped) {
    super(clientContext, wrapped);
    this.wrapped = wrapped;
  }

  public DistinctResultImpl(MongoClientContext clientContext, DistinctPublisher<TResult> wrapped,
      Function<TResult, TResult> mapper) {
    super(clientContext, wrapped, mapper);
    this.wrapped = wrapped;
  }

  public DistinctResultImpl(MongoClientContext clientContext, DistinctPublisher<TResult> wrapped,
      int batchSize) {
    super(clientContext, wrapped, batchSize);
    this.wrapped = wrapped;
  }

  public DistinctResultImpl(MongoClientContext clientContext, DistinctPublisher<TResult> wrapped,
      Function<TResult, TResult> mapper, int batchSize) {
    super(clientContext, wrapped, mapper, batchSize);
    this.wrapped = wrapped;
  }

  public MongoClientContext getClientContext() {
    return clientContext;
  }

  protected Publisher<TResult> firstPublisher() {
    return wrapped.first();
  }
}
