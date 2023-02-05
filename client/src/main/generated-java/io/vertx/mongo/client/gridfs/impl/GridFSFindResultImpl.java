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

import com.mongodb.reactivestreams.client.gridfs.GridFSFindPublisher;
import io.vertx.mongo.client.gridfs.GridFSFindResult;
import io.vertx.mongo.client.gridfs.model.GridFSFile;
import io.vertx.mongo.impl.MappingPublisher;
import io.vertx.mongo.impl.MongoClientContext;
import io.vertx.mongo.impl.MongoResultImpl;
import java.util.function.Function;
import org.reactivestreams.Publisher;

public class GridFSFindResultImpl extends MongoResultImpl<GridFSFile> implements GridFSFindResult {
  protected final GridFSFindPublisher wrapped;

  public GridFSFindResultImpl(MongoClientContext clientContext, GridFSFindPublisher wrapped) {
    super(clientContext, new MappingPublisher<>(wrapped, file -> GridFSFile.fromDriverClass(clientContext, file)));
    this.wrapped = wrapped;
  }

  public GridFSFindResultImpl(MongoClientContext clientContext, GridFSFindPublisher wrapped,
      Function<GridFSFile, GridFSFile> mapper) {
    super(clientContext, new MappingPublisher<>(wrapped, file -> GridFSFile.fromDriverClass(clientContext, file)), mapper);
    this.wrapped = wrapped;
  }

  public GridFSFindResultImpl(MongoClientContext clientContext, GridFSFindPublisher wrapped,
      int batchSize) {
    super(clientContext, new MappingPublisher<>(wrapped, file -> GridFSFile.fromDriverClass(clientContext, file)), batchSize);
    this.wrapped = wrapped;
  }

  public GridFSFindResultImpl(MongoClientContext clientContext, GridFSFindPublisher wrapped,
      Function<GridFSFile, GridFSFile> mapper, int batchSize) {
    super(clientContext, new MappingPublisher<>(wrapped, file -> GridFSFile.fromDriverClass(clientContext, file)), mapper, batchSize);
    this.wrapped = wrapped;
  }

  public MongoClientContext getClientContext() {
    return clientContext;
  }

  protected Publisher<GridFSFile> firstPublisher() {
    return new MappingPublisher<>(wrapped.first(), file -> GridFSFile.fromDriverClass(clientContext, file));
  }
}
