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
import com.mongodb.reactivestreams.client.gridfs.GridFSUploadPublisher;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Promise;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonObject;
import io.vertx.core.streams.ReadStream;
import io.vertx.mongo.ObjectId;
import io.vertx.mongo.client.ClientSession;
import io.vertx.mongo.client.gridfs.GridFSDownloadControlOptions;
import io.vertx.mongo.client.gridfs.GridFSDownloadResult;
import io.vertx.mongo.client.gridfs.GridFSFindOptions;
import io.vertx.mongo.client.gridfs.GridFSFindResult;
import io.vertx.mongo.client.gridfs.model.GridFSDownloadOptions;
import io.vertx.mongo.client.gridfs.model.GridFSFile;
import io.vertx.mongo.client.gridfs.model.GridFSUploadOptions;
import io.vertx.mongo.impl.MappingPublisher;
import io.vertx.mongo.impl.MongoClientContext;
import io.vertx.mongo.impl.SingleResultSubscriber;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.Void;
import org.bson.BsonValue;
import org.bson.conversions.Bson;
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
  public Future<ObjectId> uploadStream(String filename, ReadStream<Buffer> source) {
    requireNonNull(filename, "filename is null");
    requireNonNull(source, "source is null");
    GridFSReadStreamPublisher __source = new GridFSReadStreamPublisher(source);
    GridFSUploadPublisher<org.bson.types.ObjectId> __publisher = wrapped.uploadFromPublisher(filename, __source);
    Promise<org.bson.types.ObjectId> __promise = clientContext.getVertx().promise();
    __publisher.subscribe(new SingleResultSubscriber<>(clientContext, __promise));
    return __promise.future().map(clientContext.getMapper()::toObjectId);
  }

  @Override
  public void uploadStream(String filename, ReadStream<Buffer> source,
      Handler<AsyncResult<ObjectId>> resultHandler) {
    Future<ObjectId> __future = this.uploadStream(filename, source);
    setHandler(__future, resultHandler);
  }

  @Override
  public Future<ObjectId> uploadFile(String filename) {
    requireNonNull(filename, "fileName cannot be null");
    return openFile(clientContext.getVertx(), filename).compose(_stream -> uploadStream(filename, _stream));
  }

  @Override
  public void uploadFile(String filename, Handler<AsyncResult<ObjectId>> resultHandler) {
    Future<ObjectId> __future = this.uploadFile(filename);
    setHandler(__future, resultHandler);
  }

  @Override
  public Future<ObjectId> uploadStream(String filename, ReadStream<Buffer> source,
      GridFSUploadOptions options) {
    requireNonNull(filename, "filename is null");
    requireNonNull(source, "source is null");
    requireNonNull(options, "options is null");
    GridFSReadStreamPublisher __source = new GridFSReadStreamPublisher(source);
    com.mongodb.client.gridfs.model.GridFSUploadOptions __options = options.toDriverClass(clientContext);
    GridFSUploadPublisher<org.bson.types.ObjectId> __publisher = wrapped.uploadFromPublisher(filename, __source, __options);
    Promise<org.bson.types.ObjectId> __promise = clientContext.getVertx().promise();
    __publisher.subscribe(new SingleResultSubscriber<>(clientContext, __promise));
    return __promise.future().map(clientContext.getMapper()::toObjectId);
  }

  @Override
  public void uploadStream(String filename, ReadStream<Buffer> source, GridFSUploadOptions options,
      Handler<AsyncResult<ObjectId>> resultHandler) {
    Future<ObjectId> __future = this.uploadStream(filename, source, options);
    setHandler(__future, resultHandler);
  }

  @Override
  public Future<ObjectId> uploadFile(String filename, GridFSUploadOptions options) {
    requireNonNull(filename, "fileName cannot be null");
    return openFile(clientContext.getVertx(), filename).compose(_stream -> uploadStream(filename, _stream, options));
  }

  @Override
  public void uploadFile(String filename, GridFSUploadOptions options,
      Handler<AsyncResult<ObjectId>> resultHandler) {
    Future<ObjectId> __future = this.uploadFile(filename, options);
    setHandler(__future, resultHandler);
  }

  @Override
  public Future<ObjectId> uploadStream(ClientSession clientSession, String filename,
      ReadStream<Buffer> source) {
    requireNonNull(clientSession, "clientSession is null");
    requireNonNull(filename, "filename is null");
    requireNonNull(source, "source is null");
    com.mongodb.reactivestreams.client.ClientSession __clientSession = clientSession.toDriverClass(clientContext);
    GridFSReadStreamPublisher __source = new GridFSReadStreamPublisher(source);
    GridFSUploadPublisher<org.bson.types.ObjectId> __publisher = wrapped.uploadFromPublisher(__clientSession, filename, __source);
    Promise<org.bson.types.ObjectId> __promise = clientContext.getVertx().promise();
    __publisher.subscribe(new SingleResultSubscriber<>(clientContext, __promise));
    return __promise.future().map(clientContext.getMapper()::toObjectId);
  }

  @Override
  public void uploadStream(ClientSession clientSession, String filename, ReadStream<Buffer> source,
      Handler<AsyncResult<ObjectId>> resultHandler) {
    Future<ObjectId> __future = this.uploadStream(clientSession, filename, source);
    setHandler(__future, resultHandler);
  }

  @Override
  public Future<ObjectId> uploadFile(ClientSession clientSession, String filename) {
    requireNonNull(filename, "fileName cannot be null");
    return openFile(clientContext.getVertx(), filename).compose(_stream -> uploadStream(clientSession, filename, _stream));
  }

  @Override
  public void uploadFile(ClientSession clientSession, String filename,
      Handler<AsyncResult<ObjectId>> resultHandler) {
    Future<ObjectId> __future = this.uploadFile(clientSession, filename);
    setHandler(__future, resultHandler);
  }

  @Override
  public Future<ObjectId> uploadStream(ClientSession clientSession, String filename,
      ReadStream<Buffer> source, GridFSUploadOptions options) {
    requireNonNull(clientSession, "clientSession is null");
    requireNonNull(filename, "filename is null");
    requireNonNull(source, "source is null");
    requireNonNull(options, "options is null");
    com.mongodb.reactivestreams.client.ClientSession __clientSession = clientSession.toDriverClass(clientContext);
    GridFSReadStreamPublisher __source = new GridFSReadStreamPublisher(source);
    com.mongodb.client.gridfs.model.GridFSUploadOptions __options = options.toDriverClass(clientContext);
    GridFSUploadPublisher<org.bson.types.ObjectId> __publisher = wrapped.uploadFromPublisher(__clientSession, filename, __source, __options);
    Promise<org.bson.types.ObjectId> __promise = clientContext.getVertx().promise();
    __publisher.subscribe(new SingleResultSubscriber<>(clientContext, __promise));
    return __promise.future().map(clientContext.getMapper()::toObjectId);
  }

  @Override
  public void uploadStream(ClientSession clientSession, String filename, ReadStream<Buffer> source,
      GridFSUploadOptions options, Handler<AsyncResult<ObjectId>> resultHandler) {
    Future<ObjectId> __future = this.uploadStream(clientSession, filename, source, options);
    setHandler(__future, resultHandler);
  }

  @Override
  public Future<ObjectId> uploadFile(ClientSession clientSession, String filename,
      GridFSUploadOptions options) {
    requireNonNull(filename, "fileName cannot be null");
    return openFile(clientContext.getVertx(), filename).compose(_stream -> uploadStream(clientSession, filename, _stream, options));
  }

  @Override
  public void uploadFile(ClientSession clientSession, String filename, GridFSUploadOptions options,
      Handler<AsyncResult<ObjectId>> resultHandler) {
    Future<ObjectId> __future = this.uploadFile(clientSession, filename, options);
    setHandler(__future, resultHandler);
  }

  @Override
  public GridFSDownloadResult downloadByObjectId(ObjectId id) {
    requireNonNull(id, "id is null");
    org.bson.types.ObjectId __id = clientContext.getMapper().toObjectId(id);
    GridFSDownloadPublisher __publisher = wrapped.downloadToPublisher(__id);
    return new GridFSDownloadResultImpl(clientContext, __publisher);
  }

  @Override
  public GridFSDownloadResult downloadByObjectId(ObjectId id,
      GridFSDownloadControlOptions controlOptions) {
    requireNonNull(id, "id is null");
    org.bson.types.ObjectId __id = clientContext.getMapper().toObjectId(id);
    GridFSDownloadPublisher __publisher = wrapped.downloadToPublisher(__id);
    controlOptions.initializePublisher(clientContext, __publisher);
    return new GridFSDownloadResultImpl(clientContext, __publisher);
  }

  @Override
  public GridFSDownloadResult downloadByFilename(String filename) {
    requireNonNull(filename, "filename is null");
    GridFSDownloadPublisher __publisher = wrapped.downloadToPublisher(filename);
    return new GridFSDownloadResultImpl(clientContext, __publisher);
  }

  @Override
  public GridFSDownloadResult downloadByFilename(String filename,
      GridFSDownloadControlOptions controlOptions) {
    requireNonNull(filename, "filename is null");
    GridFSDownloadPublisher __publisher = wrapped.downloadToPublisher(filename);
    controlOptions.initializePublisher(clientContext, __publisher);
    return new GridFSDownloadResultImpl(clientContext, __publisher);
  }

  @Override
  public GridFSDownloadResult downloadByFilename(String filename,
      GridFSDownloadOptions options) {
    requireNonNull(filename, "filename is null");
    requireNonNull(options, "options is null");
    com.mongodb.client.gridfs.model.GridFSDownloadOptions __options = options.toDriverClass(clientContext);
    GridFSDownloadPublisher __publisher = wrapped.downloadToPublisher(filename, __options);
    return new GridFSDownloadResultImpl(clientContext, __publisher);
  }

  @Override
  public GridFSDownloadResult downloadByFilename(String filename,
      GridFSDownloadOptions options, GridFSDownloadControlOptions controlOptions) {
    requireNonNull(filename, "filename is null");
    requireNonNull(options, "options is null");
    com.mongodb.client.gridfs.model.GridFSDownloadOptions __options = options.toDriverClass(clientContext);
    GridFSDownloadPublisher __publisher = wrapped.downloadToPublisher(filename, __options);
    controlOptions.initializePublisher(clientContext, __publisher);
    return new GridFSDownloadResultImpl(clientContext, __publisher);
  }

  @Override
  public GridFSDownloadResult downloadByObjectId(ClientSession clientSession, ObjectId id) {
    requireNonNull(clientSession, "clientSession is null");
    requireNonNull(id, "id is null");
    com.mongodb.reactivestreams.client.ClientSession __clientSession = clientSession.toDriverClass(clientContext);
    org.bson.types.ObjectId __id = clientContext.getMapper().toObjectId(id);
    GridFSDownloadPublisher __publisher = wrapped.downloadToPublisher(__clientSession, __id);
    return new GridFSDownloadResultImpl(clientContext, __publisher);
  }

  @Override
  public GridFSDownloadResult downloadByObjectId(ClientSession clientSession, ObjectId id,
      GridFSDownloadControlOptions controlOptions) {
    requireNonNull(clientSession, "clientSession is null");
    requireNonNull(id, "id is null");
    com.mongodb.reactivestreams.client.ClientSession __clientSession = clientSession.toDriverClass(clientContext);
    org.bson.types.ObjectId __id = clientContext.getMapper().toObjectId(id);
    GridFSDownloadPublisher __publisher = wrapped.downloadToPublisher(__clientSession, __id);
    controlOptions.initializePublisher(clientContext, __publisher);
    return new GridFSDownloadResultImpl(clientContext, __publisher);
  }

  @Override
  public GridFSDownloadResult downloadByFilename(ClientSession clientSession,
      String filename) {
    requireNonNull(clientSession, "clientSession is null");
    requireNonNull(filename, "filename is null");
    com.mongodb.reactivestreams.client.ClientSession __clientSession = clientSession.toDriverClass(clientContext);
    GridFSDownloadPublisher __publisher = wrapped.downloadToPublisher(__clientSession, filename);
    return new GridFSDownloadResultImpl(clientContext, __publisher);
  }

  @Override
  public GridFSDownloadResult downloadByFilename(ClientSession clientSession,
      String filename, GridFSDownloadControlOptions controlOptions) {
    requireNonNull(clientSession, "clientSession is null");
    requireNonNull(filename, "filename is null");
    com.mongodb.reactivestreams.client.ClientSession __clientSession = clientSession.toDriverClass(clientContext);
    GridFSDownloadPublisher __publisher = wrapped.downloadToPublisher(__clientSession, filename);
    controlOptions.initializePublisher(clientContext, __publisher);
    return new GridFSDownloadResultImpl(clientContext, __publisher);
  }

  @Override
  public GridFSDownloadResult downloadByFilename(ClientSession clientSession,
      String filename, GridFSDownloadOptions options) {
    requireNonNull(clientSession, "clientSession is null");
    requireNonNull(filename, "filename is null");
    requireNonNull(options, "options is null");
    com.mongodb.reactivestreams.client.ClientSession __clientSession = clientSession.toDriverClass(clientContext);
    com.mongodb.client.gridfs.model.GridFSDownloadOptions __options = options.toDriverClass(clientContext);
    GridFSDownloadPublisher __publisher = wrapped.downloadToPublisher(__clientSession, filename, __options);
    return new GridFSDownloadResultImpl(clientContext, __publisher);
  }

  @Override
  public GridFSDownloadResult downloadByFilename(ClientSession clientSession,
      String filename, GridFSDownloadOptions options, GridFSDownloadControlOptions controlOptions) {
    requireNonNull(clientSession, "clientSession is null");
    requireNonNull(filename, "filename is null");
    requireNonNull(options, "options is null");
    com.mongodb.reactivestreams.client.ClientSession __clientSession = clientSession.toDriverClass(clientContext);
    com.mongodb.client.gridfs.model.GridFSDownloadOptions __options = options.toDriverClass(clientContext);
    GridFSDownloadPublisher __publisher = wrapped.downloadToPublisher(__clientSession, filename, __options);
    controlOptions.initializePublisher(clientContext, __publisher);
    return new GridFSDownloadResultImpl(clientContext, __publisher);
  }

//  @Override
//  public GridFSFindResult<GridFSFile> find() {
//    GridFSFindPublisher __publisher = wrapped.find();
//    MappingPublisher<com.mongodb.client.gridfs.model.GridFSFile, GridFSFile> __mappingPublisher = new MappingPublisher<>(__publisher, _item -> GridFSFile.fromDriverClass(clientContext, _item));
//    return new GridFSFindResultImpl<>(clientContext, __mappingPublisher);
//  }
//
//  @Override
//  public GridFSFindResult<GridFSFile> find(GridFSFindOptions options) {
//    GridFSFindPublisher __publisher = wrapped.find();
//    MappingPublisher<com.mongodb.client.gridfs.model.GridFSFile, GridFSFile> __mappingPublisher = new MappingPublisher<>(__publisher, _item -> GridFSFile.fromDriverClass(clientContext, _item));
//    options.initializePublisher(clientContext, __publisher);
//    Integer __batchSize = options.getBatchSize();
//    if (__batchSize != null) {
//      return new GridFSFindResultImpl<>(clientContext, __mappingPublisher, __batchSize);
//    } else {
//      return new GridFSFindResultImpl<>(clientContext, __mappingPublisher);
//    }
//  }
//
//  @Override
//  public GridFSFindResult<GridFSFile> find(JsonObject filter) {
//    requireNonNull(filter, "filter is null");
//    Bson __filter = clientContext.getMapper().toBson(filter);
//    GridFSFindPublisher __publisher = wrapped.find(__filter);
//    MappingPublisher<com.mongodb.client.gridfs.model.GridFSFile, GridFSFile> __mappingPublisher = new MappingPublisher<>(__publisher, _item -> GridFSFile.fromDriverClass(clientContext, _item));
//    return new GridFSFindResultImpl<>(clientContext, __mappingPublisher);
//  }
//
//  @Override
//  public GridFSFindResult<GridFSFile> find(JsonObject filter, GridFSFindOptions options) {
//    requireNonNull(filter, "filter is null");
//    Bson __filter = clientContext.getMapper().toBson(filter);
//    GridFSFindPublisher __publisher = wrapped.find(__filter);
//    MappingPublisher<com.mongodb.client.gridfs.model.GridFSFile, GridFSFile> __mappingPublisher = new MappingPublisher<>(__publisher, _item -> GridFSFile.fromDriverClass(clientContext, _item));
//    options.initializePublisher(clientContext, __publisher);
//    Integer __batchSize = options.getBatchSize();
//    if (__batchSize != null) {
//      return new GridFSFindResultImpl<>(clientContext, __mappingPublisher, __batchSize);
//    } else {
//      return new GridFSFindResultImpl<>(clientContext, __mappingPublisher);
//    }
//  }
//
//  @Override
//  public GridFSFindResult<GridFSFile> find(ClientSession clientSession) {
//    requireNonNull(clientSession, "clientSession is null");
//    com.mongodb.reactivestreams.client.ClientSession __clientSession = clientSession.toDriverClass(clientContext);
//    GridFSFindPublisher __publisher = wrapped.find(__clientSession);
//    MappingPublisher<com.mongodb.client.gridfs.model.GridFSFile, GridFSFile> __mappingPublisher = new MappingPublisher<>(__publisher, _item -> GridFSFile.fromDriverClass(clientContext, _item));
//    return new GridFSFindResultImpl<>(clientContext, __mappingPublisher);
//  }
//
//  @Override
//  public GridFSFindResult<GridFSFile> find(ClientSession clientSession, GridFSFindOptions options) {
//    requireNonNull(clientSession, "clientSession is null");
//    com.mongodb.reactivestreams.client.ClientSession __clientSession = clientSession.toDriverClass(clientContext);
//    GridFSFindPublisher __publisher = wrapped.find(__clientSession);
//    MappingPublisher<com.mongodb.client.gridfs.model.GridFSFile, GridFSFile> __mappingPublisher = new MappingPublisher<>(__publisher, _item -> GridFSFile.fromDriverClass(clientContext, _item));
//    options.initializePublisher(clientContext, __publisher);
//    Integer __batchSize = options.getBatchSize();
//    if (__batchSize != null) {
//      return new GridFSFindResultImpl<>(clientContext, __mappingPublisher, __batchSize);
//    } else {
//      return new GridFSFindResultImpl<>(clientContext, __mappingPublisher);
//    }
//  }
//
//  @Override
//  public GridFSFindResult<GridFSFile> find(ClientSession clientSession, JsonObject filter) {
//    requireNonNull(clientSession, "clientSession is null");
//    requireNonNull(filter, "filter is null");
//    com.mongodb.reactivestreams.client.ClientSession __clientSession = clientSession.toDriverClass(clientContext);
//    Bson __filter = clientContext.getMapper().toBson(filter);
//    GridFSFindPublisher __publisher = wrapped.find(__clientSession, __filter);
//    MappingPublisher<com.mongodb.client.gridfs.model.GridFSFile, GridFSFile> __mappingPublisher = new MappingPublisher<>(__publisher, _item -> GridFSFile.fromDriverClass(clientContext, _item));
//    return new GridFSFindResultImpl<>(clientContext, __mappingPublisher);
//  }
//
//  @Override
//  public GridFSFindResult<GridFSFile> find(ClientSession clientSession, JsonObject filter,
//      GridFSFindOptions options) {
//    requireNonNull(clientSession, "clientSession is null");
//    requireNonNull(filter, "filter is null");
//    com.mongodb.reactivestreams.client.ClientSession __clientSession = clientSession.toDriverClass(clientContext);
//    Bson __filter = clientContext.getMapper().toBson(filter);
//    GridFSFindPublisher __publisher = wrapped.find(__clientSession, __filter);
//    MappingPublisher<com.mongodb.client.gridfs.model.GridFSFile, GridFSFile> __mappingPublisher = new MappingPublisher<>(__publisher, _item -> GridFSFile.fromDriverClass(clientContext, _item));
//    options.initializePublisher(clientContext, __publisher);
//    Integer __batchSize = options.getBatchSize();
//    if (__batchSize != null) {
//      return new GridFSFindResultImpl<>(clientContext, __mappingPublisher, __batchSize);
//    } else {
//      return new GridFSFindResultImpl<>(clientContext, __mappingPublisher);
//    }
//  }

  @Override
  public Future<Void> delete(ObjectId id) {
    requireNonNull(id, "id is null");
    org.bson.types.ObjectId __id = clientContext.getMapper().toObjectId(id);
    Publisher<Void> __publisher = wrapped.delete(__id);
    Promise<Void> __promise = clientContext.getVertx().promise();
    __publisher.subscribe(new SingleResultSubscriber<>(clientContext, __promise));
    return __promise.future();
  }

  @Override
  public void delete(ObjectId id, Handler<AsyncResult<Void>> resultHandler) {
    Future<Void> __future = this.delete(id);
    setHandler(__future, resultHandler);
  }

  @Override
  public Future<Void> delete(Object id) {
    requireNonNull(id, "id is null");
    BsonValue __id = clientContext.getMapper().toBsonValue(id);
    Publisher<Void> __publisher = wrapped.delete(__id);
    Promise<Void> __promise = clientContext.getVertx().promise();
    __publisher.subscribe(new SingleResultSubscriber<>(clientContext, __promise));
    return __promise.future();
  }

  @Override
  public void delete(Object id, Handler<AsyncResult<Void>> resultHandler) {
    Future<Void> __future = this.delete(id);
    setHandler(__future, resultHandler);
  }

  @Override
  public Future<Void> delete(ClientSession clientSession, ObjectId id) {
    requireNonNull(clientSession, "clientSession is null");
    requireNonNull(id, "id is null");
    com.mongodb.reactivestreams.client.ClientSession __clientSession = clientSession.toDriverClass(clientContext);
    org.bson.types.ObjectId __id = clientContext.getMapper().toObjectId(id);
    Publisher<Void> __publisher = wrapped.delete(__clientSession, __id);
    Promise<Void> __promise = clientContext.getVertx().promise();
    __publisher.subscribe(new SingleResultSubscriber<>(clientContext, __promise));
    return __promise.future();
  }

  @Override
  public void delete(ClientSession clientSession, ObjectId id,
      Handler<AsyncResult<Void>> resultHandler) {
    Future<Void> __future = this.delete(clientSession, id);
    setHandler(__future, resultHandler);
  }

  @Override
  public Future<Void> delete(ClientSession clientSession, Object id) {
    requireNonNull(clientSession, "clientSession is null");
    requireNonNull(id, "id is null");
    com.mongodb.reactivestreams.client.ClientSession __clientSession = clientSession.toDriverClass(clientContext);
    BsonValue __id = clientContext.getMapper().toBsonValue(id);
    Publisher<Void> __publisher = wrapped.delete(__clientSession, __id);
    Promise<Void> __promise = clientContext.getVertx().promise();
    __publisher.subscribe(new SingleResultSubscriber<>(clientContext, __promise));
    return __promise.future();
  }

  @Override
  public void delete(ClientSession clientSession, Object id,
      Handler<AsyncResult<Void>> resultHandler) {
    Future<Void> __future = this.delete(clientSession, id);
    setHandler(__future, resultHandler);
  }

  @Override
  public Future<Void> rename(ObjectId id, String newFilename) {
    requireNonNull(id, "id is null");
    requireNonNull(newFilename, "newFilename is null");
    org.bson.types.ObjectId __id = clientContext.getMapper().toObjectId(id);
    Publisher<Void> __publisher = wrapped.rename(__id, newFilename);
    Promise<Void> __promise = clientContext.getVertx().promise();
    __publisher.subscribe(new SingleResultSubscriber<>(clientContext, __promise));
    return __promise.future();
  }

  @Override
  public void rename(ObjectId id, String newFilename, Handler<AsyncResult<Void>> resultHandler) {
    Future<Void> __future = this.rename(id, newFilename);
    setHandler(__future, resultHandler);
  }

  @Override
  public Future<Void> rename(Object id, String newFilename) {
    requireNonNull(id, "id is null");
    requireNonNull(newFilename, "newFilename is null");
    BsonValue __id = clientContext.getMapper().toBsonValue(id);
    Publisher<Void> __publisher = wrapped.rename(__id, newFilename);
    Promise<Void> __promise = clientContext.getVertx().promise();
    __publisher.subscribe(new SingleResultSubscriber<>(clientContext, __promise));
    return __promise.future();
  }

  @Override
  public void rename(Object id, String newFilename, Handler<AsyncResult<Void>> resultHandler) {
    Future<Void> __future = this.rename(id, newFilename);
    setHandler(__future, resultHandler);
  }

  @Override
  public Future<Void> rename(ClientSession clientSession, ObjectId id, String newFilename) {
    requireNonNull(clientSession, "clientSession is null");
    requireNonNull(id, "id is null");
    requireNonNull(newFilename, "newFilename is null");
    com.mongodb.reactivestreams.client.ClientSession __clientSession = clientSession.toDriverClass(clientContext);
    org.bson.types.ObjectId __id = clientContext.getMapper().toObjectId(id);
    Publisher<Void> __publisher = wrapped.rename(__clientSession, __id, newFilename);
    Promise<Void> __promise = clientContext.getVertx().promise();
    __publisher.subscribe(new SingleResultSubscriber<>(clientContext, __promise));
    return __promise.future();
  }

  @Override
  public void rename(ClientSession clientSession, ObjectId id, String newFilename,
      Handler<AsyncResult<Void>> resultHandler) {
    Future<Void> __future = this.rename(clientSession, id, newFilename);
    setHandler(__future, resultHandler);
  }

  @Override
  public Future<Void> rename(ClientSession clientSession, Object id, String newFilename) {
    requireNonNull(clientSession, "clientSession is null");
    requireNonNull(id, "id is null");
    requireNonNull(newFilename, "newFilename is null");
    com.mongodb.reactivestreams.client.ClientSession __clientSession = clientSession.toDriverClass(clientContext);
    BsonValue __id = clientContext.getMapper().toBsonValue(id);
    Publisher<Void> __publisher = wrapped.rename(__clientSession, __id, newFilename);
    Promise<Void> __promise = clientContext.getVertx().promise();
    __publisher.subscribe(new SingleResultSubscriber<>(clientContext, __promise));
    return __promise.future();
  }

  @Override
  public void rename(ClientSession clientSession, Object id, String newFilename,
      Handler<AsyncResult<Void>> resultHandler) {
    Future<Void> __future = this.rename(clientSession, id, newFilename);
    setHandler(__future, resultHandler);
  }

  @Override
  public Future<Void> drop() {
    Publisher<Void> __publisher = wrapped.drop();
    Promise<Void> __promise = clientContext.getVertx().promise();
    __publisher.subscribe(new SingleResultSubscriber<>(clientContext, __promise));
    return __promise.future();
  }

  @Override
  public void drop(Handler<AsyncResult<Void>> resultHandler) {
    Future<Void> __future = this.drop();
    setHandler(__future, resultHandler);
  }

  @Override
  public Future<Void> drop(ClientSession clientSession) {
    requireNonNull(clientSession, "clientSession is null");
    com.mongodb.reactivestreams.client.ClientSession __clientSession = clientSession.toDriverClass(clientContext);
    Publisher<Void> __publisher = wrapped.drop(__clientSession);
    Promise<Void> __promise = clientContext.getVertx().promise();
    __publisher.subscribe(new SingleResultSubscriber<>(clientContext, __promise));
    return __promise.future();
  }

  @Override
  public void drop(ClientSession clientSession, Handler<AsyncResult<Void>> resultHandler) {
    Future<Void> __future = this.drop(clientSession);
    setHandler(__future, resultHandler);
  }

  public MongoClientContext getClientContext() {
    return clientContext;
  }

  public GridFSBucket toDriverClass(MongoClientContext clientContext) {
    return wrapped;
  }
}
