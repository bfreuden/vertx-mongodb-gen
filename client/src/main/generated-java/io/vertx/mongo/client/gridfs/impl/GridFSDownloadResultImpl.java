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

import static io.vertx.mongo.impl.Utils.setHandler;

import com.mongodb.reactivestreams.client.gridfs.GridFSDownloadPublisher;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Promise;
import io.vertx.core.buffer.Buffer;
import io.vertx.mongo.client.FindResult;
import io.vertx.mongo.client.gridfs.GridFSDownloadResult;
import io.vertx.mongo.client.gridfs.model.GridFSFile;
import io.vertx.mongo.impl.MappingPublisher;
import io.vertx.mongo.impl.MongoClientContext;
import io.vertx.mongo.impl.MongoResultImpl;
import io.vertx.mongo.impl.SingleResultSubscriber;
import java.lang.Override;
import java.nio.ByteBuffer;
import java.util.function.Function;
import org.reactivestreams.Publisher;

public class GridFSDownloadResultImpl extends GridFSDownloadResultBase {
  protected final GridFSDownloadPublisher wrapped;

  public GridFSDownloadResultImpl(MongoClientContext clientContext,
      GridFSDownloadPublisher wrapped) {
    super(clientContext, new MappingPublisher<>(wrapped, clientContext.getMapper()::toBuffer));
    this.wrapped = wrapped;
  }

  public GridFSDownloadResultImpl(MongoClientContext clientContext, GridFSDownloadPublisher wrapped,
      Function<Buffer, Buffer> mapper) {
    super(clientContext, new MappingPublisher<>(wrapped, clientContext.getMapper()::toBuffer));
    this.wrapped = wrapped;
  }

  @Override
  public Future<GridFSFile> getGridFSFile() {
    Publisher<com.mongodb.client.gridfs.model.GridFSFile> __publisher = wrapped.getGridFSFile();
    Promise<com.mongodb.client.gridfs.model.GridFSFile> __promise = clientContext.getVertx().promise();
    __publisher.subscribe(new SingleResultSubscriber<>(clientContext, __promise));
    return __promise.future().map(_item -> GridFSFile.fromDriverClass(clientContext, _item));
  }

  @Override
  public void getGridFSFile(Handler<AsyncResult<GridFSFile>> resultHandler) {
    Future<GridFSFile> __future = this.getGridFSFile();
    setHandler(__future, resultHandler);
  }

  public MongoClientContext getClientContext() {
    return clientContext;
  }
}
