package io.vertx.mongo.client.gridfs.impl;

import static io.vertx.mongo.impl.Utils.setHandler;
import static java.util.Objects.requireNonNull;

import com.mongodb.client.gridfs.model.GridFSFile;
import com.mongodb.reactivestreams.client.gridfs.GridFSBucket;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.mongo.ReadConcern;
import io.vertx.mongo.ReadPreference;
import io.vertx.mongo.WriteConcern;
import io.vertx.mongo.client.ClientSession;
import io.vertx.mongo.client.MongoResult;
import io.vertx.mongo.client.gridfs.GridFSFindOptions;
import io.vertx.mongo.client.gridfs.model.GridFSDownloadOptions;
import io.vertx.mongo.impl.ConversionUtilsImpl;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.Void;
import org.bson.BsonValue;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

public class GridFSBucketImpl extends GridFSBucketBase {
  protected GridFSBucket wrapped;

  @Override
  public String getBucketName() {
    wrapped.getBucketName();
    return null;
  }

  @Override
  public int getChunkSizeBytes() {
    wrapped.getChunkSizeBytes();
    return 0;
  }

  @Override
  public WriteConcern getWriteConcern() {
    wrapped.getWriteConcern();
    return null;
  }

  @Override
  public ReadPreference getReadPreference() {
    wrapped.getReadPreference();
    return null;
  }

  @Override
  public ReadConcern getReadConcern() {
    wrapped.getReadConcern();
    return null;
  }

  @Override
  public io.vertx.mongo.client.gridfs.GridFSBucket withChunkSizeBytes(int chunkSizeBytes) {
    wrapped.withChunkSizeBytes(chunkSizeBytes);
    return null;
  }

  @Override
  public io.vertx.mongo.client.gridfs.GridFSBucket withReadPreference(
      ReadPreference readPreference) {
    requireNonNull(readPreference, "readPreference cannot be null");
    com.mongodb.ReadPreference __readPreference = readPreference.toDriverClass();
    wrapped.withReadPreference(__readPreference);
    return null;
  }

  @Override
  public io.vertx.mongo.client.gridfs.GridFSBucket withWriteConcern(WriteConcern writeConcern) {
    requireNonNull(writeConcern, "writeConcern cannot be null");
    com.mongodb.WriteConcern __writeConcern = writeConcern.toDriverClass();
    wrapped.withWriteConcern(__writeConcern);
    return null;
  }

  @Override
  public io.vertx.mongo.client.gridfs.GridFSBucket withReadConcern(ReadConcern readConcern) {
    requireNonNull(readConcern, "readConcern cannot be null");
    com.mongodb.ReadConcern __readConcern = readConcern.toDriverClass();
    wrapped.withReadConcern(__readConcern);
    return null;
  }

  @Override
  public Future<Void> downloadByObjectId(JsonObject id) {
    requireNonNull(id, "id cannot be null");
    ObjectId __id = ConversionUtilsImpl.INSTANCE.toObjectId(id);
    wrapped.downloadToPublisher(__id);
    return null;
  }

  @Override
  public io.vertx.mongo.client.gridfs.GridFSBucket downloadByObjectId(JsonObject id,
      Handler<AsyncResult<Void>> resultHandler) {
    Future<Void> future = this.downloadByObjectId(id);
    setHandler(future, resultHandler);
    return this;
  }

  @Override
  public Future<Void> downloadByFilename(String filename) {
    requireNonNull(filename, "filename cannot be null");
    wrapped.downloadToPublisher(filename);
    return null;
  }

  @Override
  public io.vertx.mongo.client.gridfs.GridFSBucket downloadByFilename(String filename,
      Handler<AsyncResult<Void>> resultHandler) {
    Future<Void> future = this.downloadByFilename(filename);
    setHandler(future, resultHandler);
    return this;
  }

  @Override
  public Future<Void> downloadByFilename(String filename, GridFSDownloadOptions options) {
    requireNonNull(filename, "filename cannot be null");
    requireNonNull(options, "options cannot be null");
    com.mongodb.client.gridfs.model.GridFSDownloadOptions __options = options.toDriverClass();
    wrapped.downloadToPublisher(filename, __options);
    return null;
  }

  @Override
  public io.vertx.mongo.client.gridfs.GridFSBucket downloadByFilename(String filename,
      GridFSDownloadOptions options, Handler<AsyncResult<Void>> resultHandler) {
    Future<Void> future = this.downloadByFilename(filename, options);
    setHandler(future, resultHandler);
    return this;
  }

  @Override
  public Future<Void> downloadByObjectId(ClientSession clientSession, JsonObject id) {
    requireNonNull(clientSession, "clientSession cannot be null");
    requireNonNull(id, "id cannot be null");
    com.mongodb.reactivestreams.client.ClientSession __clientSession = clientSession.toDriverClass();
    ObjectId __id = ConversionUtilsImpl.INSTANCE.toObjectId(id);
    wrapped.downloadToPublisher(__clientSession, __id);
    return null;
  }

  @Override
  public io.vertx.mongo.client.gridfs.GridFSBucket downloadByObjectId(ClientSession clientSession,
      JsonObject id, Handler<AsyncResult<Void>> resultHandler) {
    Future<Void> future = this.downloadByObjectId(clientSession, id);
    setHandler(future, resultHandler);
    return this;
  }

  @Override
  public Future<Void> downloadByFilename(ClientSession clientSession, String filename) {
    requireNonNull(clientSession, "clientSession cannot be null");
    requireNonNull(filename, "filename cannot be null");
    com.mongodb.reactivestreams.client.ClientSession __clientSession = clientSession.toDriverClass();
    wrapped.downloadToPublisher(__clientSession, filename);
    return null;
  }

  @Override
  public io.vertx.mongo.client.gridfs.GridFSBucket downloadByFilename(ClientSession clientSession,
      String filename, Handler<AsyncResult<Void>> resultHandler) {
    Future<Void> future = this.downloadByFilename(clientSession, filename);
    setHandler(future, resultHandler);
    return this;
  }

  @Override
  public Future<Void> downloadByFilename(ClientSession clientSession, String filename,
      GridFSDownloadOptions options) {
    requireNonNull(clientSession, "clientSession cannot be null");
    requireNonNull(filename, "filename cannot be null");
    requireNonNull(options, "options cannot be null");
    com.mongodb.reactivestreams.client.ClientSession __clientSession = clientSession.toDriverClass();
    com.mongodb.client.gridfs.model.GridFSDownloadOptions __options = options.toDriverClass();
    wrapped.downloadToPublisher(__clientSession, filename, __options);
    return null;
  }

  @Override
  public io.vertx.mongo.client.gridfs.GridFSBucket downloadByFilename(ClientSession clientSession,
      String filename, GridFSDownloadOptions options, Handler<AsyncResult<Void>> resultHandler) {
    Future<Void> future = this.downloadByFilename(clientSession, filename, options);
    setHandler(future, resultHandler);
    return this;
  }

  @Override
  public MongoResult<GridFSFile> find() {
    wrapped.find();
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
    wrapped.find(__filter);
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
    wrapped.find(__clientSession);
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
    wrapped.find(__clientSession, __filter);
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
    wrapped.delete(__id);
    return null;
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
    wrapped.delete(__id);
    return null;
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
    wrapped.delete(__clientSession, __id);
    return null;
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
    wrapped.delete(__clientSession, __id);
    return null;
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
    wrapped.rename(__id, newFilename);
    return null;
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
    wrapped.rename(__id, newFilename);
    return null;
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
    wrapped.rename(__clientSession, __id, newFilename);
    return null;
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
    wrapped.rename(__clientSession, __id, newFilename);
    return null;
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
    wrapped.drop();
    return null;
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
    wrapped.drop(__clientSession);
    return null;
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
