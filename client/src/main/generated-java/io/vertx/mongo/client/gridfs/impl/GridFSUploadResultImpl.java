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
package io.vertx.mongo.client.gridfs.impl;

import com.mongodb.reactivestreams.client.gridfs.GridFSUploadPublisher;
import io.vertx.mongo.ObjectId;
import io.vertx.mongo.client.gridfs.GridFSUploadResult;
import io.vertx.mongo.impl.MongoClientContext;
import io.vertx.mongo.impl.MongoResultImpl;
import java.lang.Object;
import java.lang.Override;
import java.util.function.Function;
import org.bson.BsonValue;

public class GridFSUploadResultImpl<T> extends MongoResultImpl<T> implements GridFSUploadResult<T> {
  protected final GridFSUploadPublisher<T> wrapped;

  public GridFSUploadResultImpl(MongoClientContext clientContext,
      GridFSUploadPublisher<T> wrapped) {
    super(clientContext, wrapped);
    this.wrapped = wrapped;
  }

  public GridFSUploadResultImpl(MongoClientContext clientContext,
      GridFSUploadPublisher<T> wrapped, Function<T, T> mapper) {
    super(clientContext, wrapped, mapper);
    this.wrapped = wrapped;
  }

  @Override
  public ObjectId getObjectId() {
    org.bson.types.ObjectId __result = wrapped.getObjectId();
    return clientContext.getMapper().toObjectId(__result);
  }

  @Override
  public Object getId() {
    BsonValue __result = wrapped.getId();
    return clientContext.getMapper().toObject(__result);
  }

  public MongoClientContext getClientContext() {
    return clientContext;
  }
}
