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

//import com.mongodb.client.model.changestream.ChangeStreamDocument;
import com.mongodb.reactivestreams.client.ChangeStreamPublisher;
import io.vertx.mongo.client.ChangeStreamResult;
//import io.vertx.mongo.client.model.changestream.ChangeStreamDocument;
import io.vertx.mongo.impl.MongoClientContext;

import java.util.function.Function;

import io.vertx.mongo.impl.MongoResultImpl2;
import org.reactivestreams.Publisher;

public class ChangeStreamResultImpl<TMongo, TVertx> extends MongoResultImpl2<com.mongodb.client.model.changestream.ChangeStreamDocument<TMongo>, io.vertx.mongo.client.model.changestream.ChangeStreamDocument<TVertx>>
implements ChangeStreamResult<TVertx> {
  protected final ChangeStreamPublisher<TMongo> wrapped;

  public ChangeStreamResultImpl(MongoClientContext clientContext,
      ChangeStreamPublisher<TMongo> wrapped,
      Function<com.mongodb.client.model.changestream.ChangeStreamDocument<TMongo>, io.vertx.mongo.client.model.changestream.ChangeStreamDocument<TVertx>> mapper) {
    super(clientContext, wrapped, mapper);
    this.wrapped = wrapped;
  }

  public ChangeStreamResultImpl(MongoClientContext clientContext,
      ChangeStreamPublisher<TMongo> wrapped,
      Function<com.mongodb.client.model.changestream.ChangeStreamDocument<TMongo>, io.vertx.mongo.client.model.changestream.ChangeStreamDocument<TVertx>> mapper,
      int batchSize) {
    super(clientContext, wrapped, mapper, batchSize);
    this.wrapped = wrapped;
  }

//  @Override
//  public <ChangeStreamDocument<TResult>> MongoResult<ChangeStreamDocument<TResult>> withDocumentClass(Class<ChangeStreamDocument<TResult>> clazz) {
//    requireNonNull(clazz, "clazz is null");
//    Publisher<ChangeStreamDocument<TResult>> __publisher = wrapped.withDocumentClass(clazz);
//    return new MongoResultImpl<>(clientContext, __publisher, outputMapper);
//  }

  public MongoClientContext getClientContext() {
    return clientContext;
  }

  protected Publisher<com.mongodb.client.model.changestream.ChangeStreamDocument<TMongo>> firstPublisher() {
    return wrapped.first();
  }
}
