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

import com.mongodb.client.gridfs.model.GridFSFile;
import com.mongodb.reactivestreams.client.gridfs.GridFSBucket;
import com.mongodb.reactivestreams.client.gridfs.GridFSDownloadPublisher;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;
import io.vertx.mongo.ReadConcern;
import io.vertx.mongo.ReadPreference;
import io.vertx.mongo.WriteConcern;
import io.vertx.mongo.client.ClientSession;
import io.vertx.mongo.client.MongoResult;
import io.vertx.mongo.client.gridfs.GridFSFindOptions;
import io.vertx.mongo.client.gridfs.model.GridFSDownloadOptions;
import io.vertx.mongo.impl.ConversionUtilsImpl;
import io.vertx.mongo.impl.MongoClientContext;
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
  protected MongoClientContext clientContext;

  protected GridFSBucket wrapped;

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
    return WriteConcern.fromDriverClass(wrapped.getWriteConcern());
  }

  @Override
  public ReadPreference getReadPreference() {
    return ReadPreference.fromDriverClass(wrapped.getReadPreference());
  }

  @Override
  public ReadConcern getReadConcern() {
    return ReadConcern.fromDriverClass(wrapped.getReadConcern());
  }

  @Override
  public io.vertx.mongo.client.gridfs.GridFSBucket withChunkSizeBytes(int chunkSizeBytes) {
    GridFSBucket __wrapped = wrapped.withChunkSizeBytes(chunkSizeBytes);
    return new GridFSBucketImpl(this.clientContext, __wrapped);
  }

  @Override
  public io.vertx.mongo.client.gridfs.GridFSBucket withReadPreference(
      ReadPreference readPreference) {
    requireNonNull(readPreference, "readPreference cannot be null");
    com.mongodb.ReadPreference __readPreference = readPreference.toDriverClass();
    GridFSBucket __wrapped = wrapped.withReadPreference(__readPreference);
    return new GridFSBucketImpl(this.clientContext, __wrapped);
  }

  @Override
  public io.vertx.mongo.client.gridfs.GridFSBucket withWriteConcern(WriteConcern writeConcern) {
    requireNonNull(writeConcern, "writeConcern cannot be null");
    com.mongodb.WriteConcern __writeConcern = writeConcern.toDriverClass();
    GridFSBucket __wrapped = wrapped.withWriteConcern(__writeConcern);
    return new GridFSBucketImpl(this.clientContext, __wrapped);
  }

  @Override
  public io.vertx.mongo.client.gridfs.GridFSBucket withReadConcern(ReadConcern readConcern) {
    requireNonNull(readConcern, "readConcern cannot be null");
    com.mongodb.ReadConcern __readConcern = readConcern.toDriverClass();
    GridFSBucket __wrapped = wrapped.withReadConcern(__readConcern);
    return new GridFSBucketImpl(this.clientContext, __wrapped);
  }

  @Override
  public Future<ByteBuffer> downloadByObjectId(JsonObject id) {
    requireNonNull(id, "id cannot be null");
    ObjectId __id = ConversionUtilsImpl.INSTANCE.toObjectId(id);
    GridFSDownloadPublisher __publisher = wrapped.downloadToPublisher(__id);
    Promise<ByteBuffer> promise = Promise.promise();
    __publisher.subscribe(new SingleResultSubscriber<>(promise));
    return promise.future();
  }

  @Override
  public io.vertx.mongo.client.gridfs.GridFSBucket downloadByObjectId(JsonObject id,
      Handler<AsyncResult<ByteBuffer>> resultHandler) {
    Future<ByteBuffer> future = this.downloadByObjectId(id);
    setHandler(future, resultHandler);
    return this;
  }

  @Override
  public Future<ByteBuffer> downloadByFilename(String filename) {
    requireNonNull(filename, "filename cannot be null");
    GridFSDownloadPublisher __publisher = wrapped.downloadToPublisher(filename);
    Promise<ByteBuffer> promise = Promise.promise();
    __publisher.subscribe(new SingleResultSubscriber<>(promise));
    return promise.future();
  }

  @Override
  public io.vertx.mongo.client.gridfs.GridFSBucket downloadByFilename(String filename,
      Handler<AsyncResult<ByteBuffer>> resultHandler) {
    Future<ByteBuffer> future = this.downloadByFilename(filename);
    setHandler(future, resultHandler);
    return this;
  }

  @Override
  public Future<ByteBuffer> downloadByFilename(String filename, GridFSDownloadOptions options) {
    requireNonNull(filename, "filename cannot be null");
    requireNonNull(options, "options cannot be null");
    com.mongodb.client.gridfs.model.GridFSDownloadOptions __options = options.toDriverClass();
    GridFSDownloadPublisher __publisher = wrapped.downloadToPublisher(filename, __options);
    Promise<ByteBuffer> promise = Promise.promise();
    __publisher.subscribe(new SingleResultSubscriber<>(promise));
    return promise.future();
  }

  @Override
  public io.vertx.mongo.client.gridfs.GridFSBucket downloadByFilename(String filename,
      GridFSDownloadOptions options, Handler<AsyncResult<ByteBuffer>> resultHandler) {
    Future<ByteBuffer> future = this.downloadByFilename(filename, options);
    setHandler(future, resultHandler);
    return this;
  }

  @Override
  public Future<ByteBuffer> downloadByObjectId(ClientSession clientSession, JsonObject id) {
    requireNonNull(clientSession, "clientSession cannot be null");
    requireNonNull(id, "id cannot be null");
    com.mongodb.reactivestreams.client.ClientSession __clientSession = clientSession.toDriverClass();
    ObjectId __id = ConversionUtilsImpl.INSTANCE.toObjectId(id);
    GridFSDownloadPublisher __publisher = wrapped.downloadToPublisher(__clientSession, __id);
    Promise<ByteBuffer> promise = Promise.promise();
    __publisher.subscribe(new SingleResultSubscriber<>(promise));
    return promise.future();
  }

  @Override
  public io.vertx.mongo.client.gridfs.GridFSBucket downloadByObjectId(ClientSession clientSession,
      JsonObject id, Handler<AsyncResult<ByteBuffer>> resultHandler) {
    Future<ByteBuffer> future = this.downloadByObjectId(clientSession, id);
    setHandler(future, resultHandler);
    return this;
  }

  @Override
  public Future<ByteBuffer> downloadByFilename(ClientSession clientSession, String filename) {
    requireNonNull(clientSession, "clientSession cannot be null");
    requireNonNull(filename, "filename cannot be null");
    com.mongodb.reactivestreams.client.ClientSession __clientSession = clientSession.toDriverClass();
    GridFSDownloadPublisher __publisher = wrapped.downloadToPublisher(__clientSession, filename);
    Promise<ByteBuffer> promise = Promise.promise();
    __publisher.subscribe(new SingleResultSubscriber<>(promise));
    return promise.future();
  }

  @Override
  public io.vertx.mongo.client.gridfs.GridFSBucket downloadByFilename(ClientSession clientSession,
      String filename, Handler<AsyncResult<ByteBuffer>> resultHandler) {
    Future<ByteBuffer> future = this.downloadByFilename(clientSession, filename);
    setHandler(future, resultHandler);
    return this;
  }

  @Override
  public Future<ByteBuffer> downloadByFilename(ClientSession clientSession, String filename,
      GridFSDownloadOptions options) {
    requireNonNull(clientSession, "clientSession cannot be null");
    requireNonNull(filename, "filename cannot be null");
    requireNonNull(options, "options cannot be null");
    com.mongodb.reactivestreams.client.ClientSession __clientSession = clientSession.toDriverClass();
    com.mongodb.client.gridfs.model.GridFSDownloadOptions __options = options.toDriverClass();
    GridFSDownloadPublisher __publisher = wrapped.downloadToPublisher(__clientSession, filename, __options);
    Promise<ByteBuffer> promise = Promise.promise();
    __publisher.subscribe(new SingleResultSubscriber<>(promise));
    return promise.future();
  }

  @Override
  public io.vertx.mongo.client.gridfs.GridFSBucket downloadByFilename(ClientSession clientSession,
      String filename, GridFSDownloadOptions options,
      Handler<AsyncResult<ByteBuffer>> resultHandler) {
    Future<ByteBuffer> future = this.downloadByFilename(clientSession, filename, options);
    setHandler(future, resultHandler);
    return this;
  }

  @Override
  public MongoResult<GridFSFile> find() {
    //  TODO add implementation
    return null;
  }

  @Override
  public MongoResult<GridFSFile> find(GridFSFindOptions options) {
    return null;
  }

  @Override
  public MongoResult<GridFSFile> find(JsonObject filter) {
    requireNonNull(filter, "filter cannot be null");
    Bson __filter = ConversionUtilsImpl.INSTANCE.toBson(filter);
    //  TODO add implementation
    return null;
  }

  @Override
  public MongoResult<GridFSFile> find(JsonObject filter, GridFSFindOptions options) {
    return null;
  }

  @Override
  public MongoResult<GridFSFile> find(ClientSession clientSession) {
    requireNonNull(clientSession, "clientSession cannot be null");
    com.mongodb.reactivestreams.client.ClientSession __clientSession = clientSession.toDriverClass();
    //  TODO add implementation
    return null;
  }

  @Override
  public MongoResult<GridFSFile> find(ClientSession clientSession, GridFSFindOptions options) {
    return null;
  }

  @Override
  public MongoResult<GridFSFile> find(ClientSession clientSession, JsonObject filter) {
    requireNonNull(clientSession, "clientSession cannot be null");
    requireNonNull(filter, "filter cannot be null");
    com.mongodb.reactivestreams.client.ClientSession __clientSession = clientSession.toDriverClass();
    Bson __filter = ConversionUtilsImpl.INSTANCE.toBson(filter);
    //  TODO add implementation
    return null;
  }

  @Override
  public MongoResult<GridFSFile> find(ClientSession clientSession, JsonObject filter,
      GridFSFindOptions options) {
    return null;
  }

  @Override
  public Future<Void> delete(JsonObject id) {
    requireNonNull(id, "id cannot be null");
    ObjectId __id = ConversionUtilsImpl.INSTANCE.toObjectId(id);
    Publisher<Void> __publisher = wrapped.delete(__id);
    Promise<Void> promise = Promise.promise();
    __publisher.subscribe(new SingleResultSubscriber<>(promise));
    return promise.future();
  }

  @Override
  public io.vertx.mongo.client.gridfs.GridFSBucket delete(JsonObject id,
      Handler<AsyncResult<Void>> resultHandler) {
    Future<Void> future = this.delete(id);
    setHandler(future, resultHandler);
    return this;
  }

  @Override
  public Future<Void> delete(Object id) {
    requireNonNull(id, "id cannot be null");
    BsonValue __id = ConversionUtilsImpl.INSTANCE.toBsonValue(id);
    Publisher<Void> __publisher = wrapped.delete(__id);
    Promise<Void> promise = Promise.promise();
    __publisher.subscribe(new SingleResultSubscriber<>(promise));
    return promise.future();
  }

  @Override
  public io.vertx.mongo.client.gridfs.GridFSBucket delete(Object id,
      Handler<AsyncResult<Void>> resultHandler) {
    Future<Void> future = this.delete(id);
    setHandler(future, resultHandler);
    return this;
  }

  @Override
  public Future<Void> delete(ClientSession clientSession, JsonObject id) {
    requireNonNull(clientSession, "clientSession cannot be null");
    requireNonNull(id, "id cannot be null");
    com.mongodb.reactivestreams.client.ClientSession __clientSession = clientSession.toDriverClass();
    ObjectId __id = ConversionUtilsImpl.INSTANCE.toObjectId(id);
    Publisher<Void> __publisher = wrapped.delete(__clientSession, __id);
    Promise<Void> promise = Promise.promise();
    __publisher.subscribe(new SingleResultSubscriber<>(promise));
    return promise.future();
  }

  @Override
  public io.vertx.mongo.client.gridfs.GridFSBucket delete(ClientSession clientSession,
      JsonObject id, Handler<AsyncResult<Void>> resultHandler) {
    Future<Void> future = this.delete(clientSession, id);
    setHandler(future, resultHandler);
    return this;
  }

  @Override
  public Future<Void> delete(ClientSession clientSession, Object id) {
    requireNonNull(clientSession, "clientSession cannot be null");
    requireNonNull(id, "id cannot be null");
    com.mongodb.reactivestreams.client.ClientSession __clientSession = clientSession.toDriverClass();
    BsonValue __id = ConversionUtilsImpl.INSTANCE.toBsonValue(id);
    Publisher<Void> __publisher = wrapped.delete(__clientSession, __id);
    Promise<Void> promise = Promise.promise();
    __publisher.subscribe(new SingleResultSubscriber<>(promise));
    return promise.future();
  }

  @Override
  public io.vertx.mongo.client.gridfs.GridFSBucket delete(ClientSession clientSession, Object id,
      Handler<AsyncResult<Void>> resultHandler) {
    Future<Void> future = this.delete(clientSession, id);
    setHandler(future, resultHandler);
    return this;
  }

  @Override
  public Future<Void> rename(JsonObject id, String newFilename) {
    requireNonNull(id, "id cannot be null");
    requireNonNull(newFilename, "newFilename cannot be null");
    ObjectId __id = ConversionUtilsImpl.INSTANCE.toObjectId(id);
    Publisher<Void> __publisher = wrapped.rename(__id, newFilename);
    Promise<Void> promise = Promise.promise();
    __publisher.subscribe(new SingleResultSubscriber<>(promise));
    return promise.future();
  }

  @Override
  public io.vertx.mongo.client.gridfs.GridFSBucket rename(JsonObject id, String newFilename,
      Handler<AsyncResult<Void>> resultHandler) {
    Future<Void> future = this.rename(id, newFilename);
    setHandler(future, resultHandler);
    return this;
  }

  @Override
  public Future<Void> rename(Object id, String newFilename) {
    requireNonNull(id, "id cannot be null");
    requireNonNull(newFilename, "newFilename cannot be null");
    BsonValue __id = ConversionUtilsImpl.INSTANCE.toBsonValue(id);
    Publisher<Void> __publisher = wrapped.rename(__id, newFilename);
    Promise<Void> promise = Promise.promise();
    __publisher.subscribe(new SingleResultSubscriber<>(promise));
    return promise.future();
  }

  @Override
  public io.vertx.mongo.client.gridfs.GridFSBucket rename(Object id, String newFilename,
      Handler<AsyncResult<Void>> resultHandler) {
    Future<Void> future = this.rename(id, newFilename);
    setHandler(future, resultHandler);
    return this;
  }

  @Override
  public Future<Void> rename(ClientSession clientSession, JsonObject id, String newFilename) {
    requireNonNull(clientSession, "clientSession cannot be null");
    requireNonNull(id, "id cannot be null");
    requireNonNull(newFilename, "newFilename cannot be null");
    com.mongodb.reactivestreams.client.ClientSession __clientSession = clientSession.toDriverClass();
    ObjectId __id = ConversionUtilsImpl.INSTANCE.toObjectId(id);
    Publisher<Void> __publisher = wrapped.rename(__clientSession, __id, newFilename);
    Promise<Void> promise = Promise.promise();
    __publisher.subscribe(new SingleResultSubscriber<>(promise));
    return promise.future();
  }

  @Override
  public io.vertx.mongo.client.gridfs.GridFSBucket rename(ClientSession clientSession,
      JsonObject id, String newFilename, Handler<AsyncResult<Void>> resultHandler) {
    Future<Void> future = this.rename(clientSession, id, newFilename);
    setHandler(future, resultHandler);
    return this;
  }

  @Override
  public Future<Void> rename(ClientSession clientSession, Object id, String newFilename) {
    requireNonNull(clientSession, "clientSession cannot be null");
    requireNonNull(id, "id cannot be null");
    requireNonNull(newFilename, "newFilename cannot be null");
    com.mongodb.reactivestreams.client.ClientSession __clientSession = clientSession.toDriverClass();
    BsonValue __id = ConversionUtilsImpl.INSTANCE.toBsonValue(id);
    Publisher<Void> __publisher = wrapped.rename(__clientSession, __id, newFilename);
    Promise<Void> promise = Promise.promise();
    __publisher.subscribe(new SingleResultSubscriber<>(promise));
    return promise.future();
  }

  @Override
  public io.vertx.mongo.client.gridfs.GridFSBucket rename(ClientSession clientSession, Object id,
      String newFilename, Handler<AsyncResult<Void>> resultHandler) {
    Future<Void> future = this.rename(clientSession, id, newFilename);
    setHandler(future, resultHandler);
    return this;
  }

  @Override
  public Future<Void> drop() {
    Publisher<Void> __publisher = wrapped.drop();
    Promise<Void> promise = Promise.promise();
    __publisher.subscribe(new SingleResultSubscriber<>(promise));
    return promise.future();
  }

  @Override
  public io.vertx.mongo.client.gridfs.GridFSBucket drop(Handler<AsyncResult<Void>> resultHandler) {
    Future<Void> future = this.drop();
    setHandler(future, resultHandler);
    return this;
  }

  @Override
  public Future<Void> drop(ClientSession clientSession) {
    requireNonNull(clientSession, "clientSession cannot be null");
    com.mongodb.reactivestreams.client.ClientSession __clientSession = clientSession.toDriverClass();
    Publisher<Void> __publisher = wrapped.drop(__clientSession);
    Promise<Void> promise = Promise.promise();
    __publisher.subscribe(new SingleResultSubscriber<>(promise));
    return promise.future();
  }

  @Override
  public io.vertx.mongo.client.gridfs.GridFSBucket drop(ClientSession clientSession,
      Handler<AsyncResult<Void>> resultHandler) {
    Future<Void> future = this.drop(clientSession);
    setHandler(future, resultHandler);
    return this;
  }

  public GridFSBucket toDriverClass() {
    return wrapped;
  }
}
