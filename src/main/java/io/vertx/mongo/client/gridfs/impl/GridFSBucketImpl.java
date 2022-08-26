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
import static java.util.Objects.requireNonNull;

import com.mongodb.ReadConcern;
import com.mongodb.ReadPreference;
import com.mongodb.WriteConcern;
import com.mongodb.reactivestreams.client.gridfs.GridFSBucket;
import com.mongodb.reactivestreams.client.gridfs.GridFSDownloadPublisher;
import com.mongodb.reactivestreams.client.gridfs.GridFSFindPublisher;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;
import io.vertx.mongo.MongoResult;
import io.vertx.mongo.client.ClientSession;
import io.vertx.mongo.client.gridfs.GridFSFindOptions;
import io.vertx.mongo.client.gridfs.model.GridFSDownloadOptions;
import io.vertx.mongo.client.gridfs.model.GridFSFile;
import io.vertx.mongo.impl.ConversionUtilsImpl;
import io.vertx.mongo.impl.MappingPublisher;
import io.vertx.mongo.impl.MongoClientContext;
import io.vertx.mongo.impl.MongoResultImpl;
import io.vertx.mongo.impl.SingleResultSubscriber;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.Void;
import java.nio.ByteBuffer;
import org.bson.BsonValue;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.reactivestreams.Publisher;

public class GridFSBucketImpl extends GridFSBucketBase {
  protected final MongoClientContext clientContext;

  protected final GridFSBucket wrapped;

  public GridFSBucketImpl(MongoClientContext clientContext, GridFSBucket wrapped) {
    this.clientContext = clientContext;
    this.wrapped = wrapped;
  }

  @Override
  public String getBucketName() {
    return wrapped.getBucketName();
  }

  @Override
  public int getChunkSizeBytes() {
    return wrapped.getChunkSizeBytes();
  }

  @Override
  public WriteConcern getWriteConcern() {
    return wrapped.getWriteConcern();
  }

  @Override
  public ReadPreference getReadPreference() {
    return wrapped.getReadPreference();
  }

  @Override
  public ReadConcern getReadConcern() {
    return wrapped.getReadConcern();
  }

  @Override
  public io.vertx.mongo.client.gridfs.GridFSBucket withChunkSizeBytes(int chunkSizeBytes) {
    GridFSBucket __result = wrapped.withChunkSizeBytes(chunkSizeBytes);
    return new GridFSBucketImpl(clientContext, __result);
  }

  @Override
  public io.vertx.mongo.client.gridfs.GridFSBucket withReadPreference(
      ReadPreference readPreference) {
    requireNonNull(readPreference, "readPreference is null");
    GridFSBucket __result = wrapped.withReadPreference(readPreference);
    return new GridFSBucketImpl(clientContext, __result);
  }

  @Override
  public io.vertx.mongo.client.gridfs.GridFSBucket withWriteConcern(WriteConcern writeConcern) {
    requireNonNull(writeConcern, "writeConcern is null");
    GridFSBucket __result = wrapped.withWriteConcern(writeConcern);
    return new GridFSBucketImpl(clientContext, __result);
  }

  @Override
  public io.vertx.mongo.client.gridfs.GridFSBucket withReadConcern(ReadConcern readConcern) {
    requireNonNull(readConcern, "readConcern is null");
    GridFSBucket __result = wrapped.withReadConcern(readConcern);
    return new GridFSBucketImpl(clientContext, __result);
  }

  @Override
  public Future<ByteBuffer> downloadByObjectId(String id) {
    requireNonNull(id, "id is null");
    ObjectId __id = ConversionUtilsImpl.INSTANCE.toObjectId(id);
    GridFSDownloadPublisher __publisher = wrapped.downloadToPublisher(__id);
    Promise<ByteBuffer> __promise = clientContext.getVertx().promise();
    __publisher.subscribe(new SingleResultSubscriber<>(clientContext, __promise));
    return __promise.future();
  }

  @Override
  public io.vertx.mongo.client.gridfs.GridFSBucket downloadByObjectId(String id,
      Handler<AsyncResult<ByteBuffer>> resultHandler) {
    Future<ByteBuffer> __future = this.downloadByObjectId(id);
    setHandler(__future, resultHandler);
    return this;
  }

  @Override
  public Future<ByteBuffer> downloadByFilename(String filename) {
    requireNonNull(filename, "filename is null");
    GridFSDownloadPublisher __publisher = wrapped.downloadToPublisher(filename);
    Promise<ByteBuffer> __promise = clientContext.getVertx().promise();
    __publisher.subscribe(new SingleResultSubscriber<>(clientContext, __promise));
    return __promise.future();
  }

  @Override
  public io.vertx.mongo.client.gridfs.GridFSBucket downloadByFilename(String filename,
      Handler<AsyncResult<ByteBuffer>> resultHandler) {
    Future<ByteBuffer> __future = this.downloadByFilename(filename);
    setHandler(__future, resultHandler);
    return this;
  }

  @Override
  public Future<ByteBuffer> downloadByFilename(String filename, GridFSDownloadOptions options) {
    requireNonNull(filename, "filename is null");
    requireNonNull(options, "options is null");
    com.mongodb.client.gridfs.model.GridFSDownloadOptions __options = options.toDriverClass();
    GridFSDownloadPublisher __publisher = wrapped.downloadToPublisher(filename, __options);
    Promise<ByteBuffer> __promise = clientContext.getVertx().promise();
    __publisher.subscribe(new SingleResultSubscriber<>(clientContext, __promise));
    return __promise.future();
  }

  @Override
  public io.vertx.mongo.client.gridfs.GridFSBucket downloadByFilename(String filename,
      GridFSDownloadOptions options, Handler<AsyncResult<ByteBuffer>> resultHandler) {
    Future<ByteBuffer> __future = this.downloadByFilename(filename, options);
    setHandler(__future, resultHandler);
    return this;
  }

  @Override
  public Future<ByteBuffer> downloadByObjectId(ClientSession clientSession, String id) {
    requireNonNull(clientSession, "clientSession is null");
    requireNonNull(id, "id is null");
    com.mongodb.reactivestreams.client.ClientSession __clientSession = clientSession.toDriverClass();
    ObjectId __id = ConversionUtilsImpl.INSTANCE.toObjectId(id);
    GridFSDownloadPublisher __publisher = wrapped.downloadToPublisher(__clientSession, __id);
    Promise<ByteBuffer> __promise = clientContext.getVertx().promise();
    __publisher.subscribe(new SingleResultSubscriber<>(clientContext, __promise));
    return __promise.future();
  }

  @Override
  public io.vertx.mongo.client.gridfs.GridFSBucket downloadByObjectId(ClientSession clientSession,
      String id, Handler<AsyncResult<ByteBuffer>> resultHandler) {
    Future<ByteBuffer> __future = this.downloadByObjectId(clientSession, id);
    setHandler(__future, resultHandler);
    return this;
  }

  @Override
  public Future<ByteBuffer> downloadByFilename(ClientSession clientSession, String filename) {
    requireNonNull(clientSession, "clientSession is null");
    requireNonNull(filename, "filename is null");
    com.mongodb.reactivestreams.client.ClientSession __clientSession = clientSession.toDriverClass();
    GridFSDownloadPublisher __publisher = wrapped.downloadToPublisher(__clientSession, filename);
    Promise<ByteBuffer> __promise = clientContext.getVertx().promise();
    __publisher.subscribe(new SingleResultSubscriber<>(clientContext, __promise));
    return __promise.future();
  }

  @Override
  public io.vertx.mongo.client.gridfs.GridFSBucket downloadByFilename(ClientSession clientSession,
      String filename, Handler<AsyncResult<ByteBuffer>> resultHandler) {
    Future<ByteBuffer> __future = this.downloadByFilename(clientSession, filename);
    setHandler(__future, resultHandler);
    return this;
  }

  @Override
  public Future<ByteBuffer> downloadByFilename(ClientSession clientSession, String filename,
      GridFSDownloadOptions options) {
    requireNonNull(clientSession, "clientSession is null");
    requireNonNull(filename, "filename is null");
    requireNonNull(options, "options is null");
    com.mongodb.reactivestreams.client.ClientSession __clientSession = clientSession.toDriverClass();
    com.mongodb.client.gridfs.model.GridFSDownloadOptions __options = options.toDriverClass();
    GridFSDownloadPublisher __publisher = wrapped.downloadToPublisher(__clientSession, filename, __options);
    Promise<ByteBuffer> __promise = clientContext.getVertx().promise();
    __publisher.subscribe(new SingleResultSubscriber<>(clientContext, __promise));
    return __promise.future();
  }

  @Override
  public io.vertx.mongo.client.gridfs.GridFSBucket downloadByFilename(ClientSession clientSession,
      String filename, GridFSDownloadOptions options,
      Handler<AsyncResult<ByteBuffer>> resultHandler) {
    Future<ByteBuffer> __future = this.downloadByFilename(clientSession, filename, options);
    setHandler(__future, resultHandler);
    return this;
  }

  @Override
  public MongoResult<GridFSFile> find() {
    GridFSFindPublisher __publisher = wrapped.find();
    MappingPublisher<com.mongodb.client.gridfs.model.GridFSFile, GridFSFile> __mappingPublisher = new MappingPublisher<>(__publisher, GridFSFile::fromDriverClass);
    return new MongoResultImpl<>(clientContext, __mappingPublisher, __mappingPublisher::first);
  }

  @Override
  public MongoResult<GridFSFile> find(GridFSFindOptions options) {
    GridFSFindPublisher __publisher = wrapped.find();
    MappingPublisher<com.mongodb.client.gridfs.model.GridFSFile, GridFSFile> __mappingPublisher = new MappingPublisher<>(__publisher, GridFSFile::fromDriverClass);
    options.initializePublisher(__publisher);
    Integer __batchSize = options.getBatchSize();
    if (__batchSize != null) {
      return new MongoResultImpl<>(clientContext, __mappingPublisher, __mappingPublisher::first, __batchSize);
    } else {
      return new MongoResultImpl<>(clientContext, __mappingPublisher, __mappingPublisher::first);
    }
  }

  @Override
  public MongoResult<GridFSFile> find(JsonObject filter) {
    requireNonNull(filter, "filter is null");
    Bson __filter = ConversionUtilsImpl.INSTANCE.toBson(filter);
    GridFSFindPublisher __publisher = wrapped.find(__filter);
    MappingPublisher<com.mongodb.client.gridfs.model.GridFSFile, GridFSFile> __mappingPublisher = new MappingPublisher<>(__publisher, GridFSFile::fromDriverClass);
    return new MongoResultImpl<>(clientContext, __mappingPublisher, __mappingPublisher::first);
  }

  @Override
  public MongoResult<GridFSFile> find(JsonObject filter, GridFSFindOptions options) {
    requireNonNull(filter, "filter is null");
    Bson __filter = ConversionUtilsImpl.INSTANCE.toBson(filter);
    GridFSFindPublisher __publisher = wrapped.find(__filter);
    MappingPublisher<com.mongodb.client.gridfs.model.GridFSFile, GridFSFile> __mappingPublisher = new MappingPublisher<>(__publisher, GridFSFile::fromDriverClass);
    options.initializePublisher(__publisher);
    Integer __batchSize = options.getBatchSize();
    if (__batchSize != null) {
      return new MongoResultImpl<>(clientContext, __mappingPublisher, __mappingPublisher::first, __batchSize);
    } else {
      return new MongoResultImpl<>(clientContext, __mappingPublisher, __mappingPublisher::first);
    }
  }

  @Override
  public MongoResult<GridFSFile> find(ClientSession clientSession) {
    requireNonNull(clientSession, "clientSession is null");
    com.mongodb.reactivestreams.client.ClientSession __clientSession = clientSession.toDriverClass();
    GridFSFindPublisher __publisher = wrapped.find(__clientSession);
    MappingPublisher<com.mongodb.client.gridfs.model.GridFSFile, GridFSFile> __mappingPublisher = new MappingPublisher<>(__publisher, GridFSFile::fromDriverClass);
    return new MongoResultImpl<>(clientContext, __mappingPublisher, __mappingPublisher::first);
  }

  @Override
  public MongoResult<GridFSFile> find(ClientSession clientSession, GridFSFindOptions options) {
    requireNonNull(clientSession, "clientSession is null");
    com.mongodb.reactivestreams.client.ClientSession __clientSession = clientSession.toDriverClass();
    GridFSFindPublisher __publisher = wrapped.find(__clientSession);
    MappingPublisher<com.mongodb.client.gridfs.model.GridFSFile, GridFSFile> __mappingPublisher = new MappingPublisher<>(__publisher, GridFSFile::fromDriverClass);
    options.initializePublisher(__publisher);
    Integer __batchSize = options.getBatchSize();
    if (__batchSize != null) {
      return new MongoResultImpl<>(clientContext, __mappingPublisher, __mappingPublisher::first, __batchSize);
    } else {
      return new MongoResultImpl<>(clientContext, __mappingPublisher, __mappingPublisher::first);
    }
  }

  @Override
  public MongoResult<GridFSFile> find(ClientSession clientSession, JsonObject filter) {
    requireNonNull(clientSession, "clientSession is null");
    requireNonNull(filter, "filter is null");
    com.mongodb.reactivestreams.client.ClientSession __clientSession = clientSession.toDriverClass();
    Bson __filter = ConversionUtilsImpl.INSTANCE.toBson(filter);
    GridFSFindPublisher __publisher = wrapped.find(__clientSession, __filter);
    MappingPublisher<com.mongodb.client.gridfs.model.GridFSFile, GridFSFile> __mappingPublisher = new MappingPublisher<>(__publisher, GridFSFile::fromDriverClass);
    return new MongoResultImpl<>(clientContext, __mappingPublisher, __mappingPublisher::first);
  }

  @Override
  public MongoResult<GridFSFile> find(ClientSession clientSession, JsonObject filter,
      GridFSFindOptions options) {
    requireNonNull(clientSession, "clientSession is null");
    requireNonNull(filter, "filter is null");
    com.mongodb.reactivestreams.client.ClientSession __clientSession = clientSession.toDriverClass();
    Bson __filter = ConversionUtilsImpl.INSTANCE.toBson(filter);
    GridFSFindPublisher __publisher = wrapped.find(__clientSession, __filter);
    MappingPublisher<com.mongodb.client.gridfs.model.GridFSFile, GridFSFile> __mappingPublisher = new MappingPublisher<>(__publisher, GridFSFile::fromDriverClass);
    options.initializePublisher(__publisher);
    Integer __batchSize = options.getBatchSize();
    if (__batchSize != null) {
      return new MongoResultImpl<>(clientContext, __mappingPublisher, __mappingPublisher::first, __batchSize);
    } else {
      return new MongoResultImpl<>(clientContext, __mappingPublisher, __mappingPublisher::first);
    }
  }

  @Override
  public Future<Void> delete(String id) {
    requireNonNull(id, "id is null");
    ObjectId __id = ConversionUtilsImpl.INSTANCE.toObjectId(id);
    Publisher<Void> __publisher = wrapped.delete(__id);
    Promise<Void> __promise = clientContext.getVertx().promise();
    __publisher.subscribe(new SingleResultSubscriber<>(clientContext, __promise));
    return __promise.future();
  }

  @Override
  public io.vertx.mongo.client.gridfs.GridFSBucket delete(String id,
      Handler<AsyncResult<Void>> resultHandler) {
    Future<Void> __future = this.delete(id);
    setHandler(__future, resultHandler);
    return this;
  }

  @Override
  public Future<Void> delete(Object id) {
    requireNonNull(id, "id is null");
    BsonValue __id = ConversionUtilsImpl.INSTANCE.toBsonValue(id);
    Publisher<Void> __publisher = wrapped.delete(__id);
    Promise<Void> __promise = clientContext.getVertx().promise();
    __publisher.subscribe(new SingleResultSubscriber<>(clientContext, __promise));
    return __promise.future();
  }

  @Override
  public io.vertx.mongo.client.gridfs.GridFSBucket delete(Object id,
      Handler<AsyncResult<Void>> resultHandler) {
    Future<Void> __future = this.delete(id);
    setHandler(__future, resultHandler);
    return this;
  }

  @Override
  public Future<Void> delete(ClientSession clientSession, String id) {
    requireNonNull(clientSession, "clientSession is null");
    requireNonNull(id, "id is null");
    com.mongodb.reactivestreams.client.ClientSession __clientSession = clientSession.toDriverClass();
    ObjectId __id = ConversionUtilsImpl.INSTANCE.toObjectId(id);
    Publisher<Void> __publisher = wrapped.delete(__clientSession, __id);
    Promise<Void> __promise = clientContext.getVertx().promise();
    __publisher.subscribe(new SingleResultSubscriber<>(clientContext, __promise));
    return __promise.future();
  }

  @Override
  public io.vertx.mongo.client.gridfs.GridFSBucket delete(ClientSession clientSession, String id,
      Handler<AsyncResult<Void>> resultHandler) {
    Future<Void> __future = this.delete(clientSession, id);
    setHandler(__future, resultHandler);
    return this;
  }

  @Override
  public Future<Void> delete(ClientSession clientSession, Object id) {
    requireNonNull(clientSession, "clientSession is null");
    requireNonNull(id, "id is null");
    com.mongodb.reactivestreams.client.ClientSession __clientSession = clientSession.toDriverClass();
    BsonValue __id = ConversionUtilsImpl.INSTANCE.toBsonValue(id);
    Publisher<Void> __publisher = wrapped.delete(__clientSession, __id);
    Promise<Void> __promise = clientContext.getVertx().promise();
    __publisher.subscribe(new SingleResultSubscriber<>(clientContext, __promise));
    return __promise.future();
  }

  @Override
  public io.vertx.mongo.client.gridfs.GridFSBucket delete(ClientSession clientSession, Object id,
      Handler<AsyncResult<Void>> resultHandler) {
    Future<Void> __future = this.delete(clientSession, id);
    setHandler(__future, resultHandler);
    return this;
  }

  @Override
  public Future<Void> rename(String id, String newFilename) {
    requireNonNull(id, "id is null");
    requireNonNull(newFilename, "newFilename is null");
    ObjectId __id = ConversionUtilsImpl.INSTANCE.toObjectId(id);
    Publisher<Void> __publisher = wrapped.rename(__id, newFilename);
    Promise<Void> __promise = clientContext.getVertx().promise();
    __publisher.subscribe(new SingleResultSubscriber<>(clientContext, __promise));
    return __promise.future();
  }

  @Override
  public io.vertx.mongo.client.gridfs.GridFSBucket rename(String id, String newFilename,
      Handler<AsyncResult<Void>> resultHandler) {
    Future<Void> __future = this.rename(id, newFilename);
    setHandler(__future, resultHandler);
    return this;
  }

  @Override
  public Future<Void> rename(Object id, String newFilename) {
    requireNonNull(id, "id is null");
    requireNonNull(newFilename, "newFilename is null");
    BsonValue __id = ConversionUtilsImpl.INSTANCE.toBsonValue(id);
    Publisher<Void> __publisher = wrapped.rename(__id, newFilename);
    Promise<Void> __promise = clientContext.getVertx().promise();
    __publisher.subscribe(new SingleResultSubscriber<>(clientContext, __promise));
    return __promise.future();
  }

  @Override
  public io.vertx.mongo.client.gridfs.GridFSBucket rename(Object id, String newFilename,
      Handler<AsyncResult<Void>> resultHandler) {
    Future<Void> __future = this.rename(id, newFilename);
    setHandler(__future, resultHandler);
    return this;
  }

  @Override
  public Future<Void> rename(ClientSession clientSession, String id, String newFilename) {
    requireNonNull(clientSession, "clientSession is null");
    requireNonNull(id, "id is null");
    requireNonNull(newFilename, "newFilename is null");
    com.mongodb.reactivestreams.client.ClientSession __clientSession = clientSession.toDriverClass();
    ObjectId __id = ConversionUtilsImpl.INSTANCE.toObjectId(id);
    Publisher<Void> __publisher = wrapped.rename(__clientSession, __id, newFilename);
    Promise<Void> __promise = clientContext.getVertx().promise();
    __publisher.subscribe(new SingleResultSubscriber<>(clientContext, __promise));
    return __promise.future();
  }

  @Override
  public io.vertx.mongo.client.gridfs.GridFSBucket rename(ClientSession clientSession, String id,
      String newFilename, Handler<AsyncResult<Void>> resultHandler) {
    Future<Void> __future = this.rename(clientSession, id, newFilename);
    setHandler(__future, resultHandler);
    return this;
  }

  @Override
  public Future<Void> rename(ClientSession clientSession, Object id, String newFilename) {
    requireNonNull(clientSession, "clientSession is null");
    requireNonNull(id, "id is null");
    requireNonNull(newFilename, "newFilename is null");
    com.mongodb.reactivestreams.client.ClientSession __clientSession = clientSession.toDriverClass();
    BsonValue __id = ConversionUtilsImpl.INSTANCE.toBsonValue(id);
    Publisher<Void> __publisher = wrapped.rename(__clientSession, __id, newFilename);
    Promise<Void> __promise = clientContext.getVertx().promise();
    __publisher.subscribe(new SingleResultSubscriber<>(clientContext, __promise));
    return __promise.future();
  }

  @Override
  public io.vertx.mongo.client.gridfs.GridFSBucket rename(ClientSession clientSession, Object id,
      String newFilename, Handler<AsyncResult<Void>> resultHandler) {
    Future<Void> __future = this.rename(clientSession, id, newFilename);
    setHandler(__future, resultHandler);
    return this;
  }

  @Override
  public Future<Void> drop() {
    Publisher<Void> __publisher = wrapped.drop();
    Promise<Void> __promise = clientContext.getVertx().promise();
    __publisher.subscribe(new SingleResultSubscriber<>(clientContext, __promise));
    return __promise.future();
  }

  @Override
  public io.vertx.mongo.client.gridfs.GridFSBucket drop(Handler<AsyncResult<Void>> resultHandler) {
    Future<Void> __future = this.drop();
    setHandler(__future, resultHandler);
    return this;
  }

  @Override
  public Future<Void> drop(ClientSession clientSession) {
    requireNonNull(clientSession, "clientSession is null");
    com.mongodb.reactivestreams.client.ClientSession __clientSession = clientSession.toDriverClass();
    Publisher<Void> __publisher = wrapped.drop(__clientSession);
    Promise<Void> __promise = clientContext.getVertx().promise();
    __publisher.subscribe(new SingleResultSubscriber<>(clientContext, __promise));
    return __promise.future();
  }

  @Override
  public io.vertx.mongo.client.gridfs.GridFSBucket drop(ClientSession clientSession,
      Handler<AsyncResult<Void>> resultHandler) {
    Future<Void> __future = this.drop(clientSession);
    setHandler(__future, resultHandler);
    return this;
  }

  public GridFSBucket toDriverClass() {
    return wrapped;
  }
}
