package io.vertx.mongo.client.gridfs.impl;

import com.mongodb.client.gridfs.model.GridFSFile;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.mongo.ReadConcern;
import io.vertx.mongo.ReadPreference;
import io.vertx.mongo.WriteConcern;
import io.vertx.mongo.client.ClientSession;
import io.vertx.mongo.client.MongoResult;
import io.vertx.mongo.client.gridfs.GridFSBucket;
import io.vertx.mongo.client.gridfs.GridFSFindOptions;
import io.vertx.mongo.client.gridfs.model.GridFSDownloadOptions;
import io.vertx.mongo.impl.Utils;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.Void;

public class GridFSBucketImpl extends GridFSBucketBase {
  @Override
  public String getBucketName() {
    return null;
  }

  @Override
  public int getChunkSizeBytes() {
    return 0;
  }

  @Override
  public WriteConcern getWriteConcern() {
    return null;
  }

  @Override
  public ReadPreference getReadPreference() {
    return null;
  }

  @Override
  public ReadConcern getReadConcern() {
    return null;
  }

  @Override
  public GridFSBucket withChunkSizeBytes(int chunkSizeBytes) {
    return null;
  }

  @Override
  public GridFSBucket withReadPreference(ReadPreference readPreference) {
    return null;
  }

  @Override
  public GridFSBucket withWriteConcern(WriteConcern writeConcern) {
    return null;
  }

  @Override
  public GridFSBucket withReadConcern(ReadConcern readConcern) {
    return null;
  }

  @Override
  public Future<Void> downloadByObjectId(JsonObject id) {
    return null;
  }

  @Override
  public GridFSBucket downloadByObjectId(JsonObject id, Handler<AsyncResult<Void>> resultHandler) {
    Future<Void> future = this.downloadByObjectId(id);
    Utils.setHandler(future, resultHandler);
    return this;
  }

  @Override
  public Future<Void> downloadByFilename(String filename) {
    return null;
  }

  @Override
  public GridFSBucket downloadByFilename(String filename,
      Handler<AsyncResult<Void>> resultHandler) {
    Future<Void> future = this.downloadByFilename(filename);
    Utils.setHandler(future, resultHandler);
    return this;
  }

  @Override
  public Future<Void> downloadByFilename(String filename, GridFSDownloadOptions options) {
    return null;
  }

  @Override
  public GridFSBucket downloadByFilename(String filename, GridFSDownloadOptions options,
      Handler<AsyncResult<Void>> resultHandler) {
    Future<Void> future = this.downloadByFilename(filename, options);
    Utils.setHandler(future, resultHandler);
    return this;
  }

  @Override
  public Future<Void> downloadByObjectId(ClientSession clientSession, JsonObject id) {
    return null;
  }

  @Override
  public GridFSBucket downloadByObjectId(ClientSession clientSession, JsonObject id,
      Handler<AsyncResult<Void>> resultHandler) {
    Future<Void> future = this.downloadByObjectId(clientSession, id);
    Utils.setHandler(future, resultHandler);
    return this;
  }

  @Override
  public Future<Void> downloadByFilename(ClientSession clientSession, String filename) {
    return null;
  }

  @Override
  public GridFSBucket downloadByFilename(ClientSession clientSession, String filename,
      Handler<AsyncResult<Void>> resultHandler) {
    Future<Void> future = this.downloadByFilename(clientSession, filename);
    Utils.setHandler(future, resultHandler);
    return this;
  }

  @Override
  public Future<Void> downloadByFilename(ClientSession clientSession, String filename,
      GridFSDownloadOptions options) {
    return null;
  }

  @Override
  public GridFSBucket downloadByFilename(ClientSession clientSession, String filename,
      GridFSDownloadOptions options, Handler<AsyncResult<Void>> resultHandler) {
    Future<Void> future = this.downloadByFilename(clientSession, filename, options);
    Utils.setHandler(future, resultHandler);
    return this;
  }

  @Override
  public MongoResult<GridFSFile> find() {
    return null;
  }

  @Override
  public MongoResult<GridFSFile> find(GridFSFindOptions options) {
    return null;
  }

  @Override
  public MongoResult<GridFSFile> find(JsonObject filter) {
    return null;
  }

  @Override
  public MongoResult<GridFSFile> find(JsonObject filter, GridFSFindOptions options) {
    return null;
  }

  @Override
  public MongoResult<GridFSFile> find(ClientSession clientSession) {
    return null;
  }

  @Override
  public MongoResult<GridFSFile> find(ClientSession clientSession, GridFSFindOptions options) {
    return null;
  }

  @Override
  public MongoResult<GridFSFile> find(ClientSession clientSession, JsonObject filter) {
    return null;
  }

  @Override
  public MongoResult<GridFSFile> find(ClientSession clientSession, JsonObject filter,
      GridFSFindOptions options) {
    return null;
  }

  @Override
  public Future<Void> delete(JsonObject id) {
    return null;
  }

  @Override
  public GridFSBucket delete(JsonObject id, Handler<AsyncResult<Void>> resultHandler) {
    Future<Void> future = this.delete(id);
    Utils.setHandler(future, resultHandler);
    return this;
  }

  @Override
  public Future<Void> delete(Object id) {
    return null;
  }

  @Override
  public GridFSBucket delete(Object id, Handler<AsyncResult<Void>> resultHandler) {
    Future<Void> future = this.delete(id);
    Utils.setHandler(future, resultHandler);
    return this;
  }

  @Override
  public Future<Void> delete(ClientSession clientSession, JsonObject id) {
    return null;
  }

  @Override
  public GridFSBucket delete(ClientSession clientSession, JsonObject id,
      Handler<AsyncResult<Void>> resultHandler) {
    Future<Void> future = this.delete(clientSession, id);
    Utils.setHandler(future, resultHandler);
    return this;
  }

  @Override
  public Future<Void> delete(ClientSession clientSession, Object id) {
    return null;
  }

  @Override
  public GridFSBucket delete(ClientSession clientSession, Object id,
      Handler<AsyncResult<Void>> resultHandler) {
    Future<Void> future = this.delete(clientSession, id);
    Utils.setHandler(future, resultHandler);
    return this;
  }

  @Override
  public Future<Void> rename(JsonObject id, String newFilename) {
    return null;
  }

  @Override
  public GridFSBucket rename(JsonObject id, String newFilename,
      Handler<AsyncResult<Void>> resultHandler) {
    Future<Void> future = this.rename(id, newFilename);
    Utils.setHandler(future, resultHandler);
    return this;
  }

  @Override
  public Future<Void> rename(Object id, String newFilename) {
    return null;
  }

  @Override
  public GridFSBucket rename(Object id, String newFilename,
      Handler<AsyncResult<Void>> resultHandler) {
    Future<Void> future = this.rename(id, newFilename);
    Utils.setHandler(future, resultHandler);
    return this;
  }

  @Override
  public Future<Void> rename(ClientSession clientSession, JsonObject id, String newFilename) {
    return null;
  }

  @Override
  public GridFSBucket rename(ClientSession clientSession, JsonObject id, String newFilename,
      Handler<AsyncResult<Void>> resultHandler) {
    Future<Void> future = this.rename(clientSession, id, newFilename);
    Utils.setHandler(future, resultHandler);
    return this;
  }

  @Override
  public Future<Void> rename(ClientSession clientSession, Object id, String newFilename) {
    return null;
  }

  @Override
  public GridFSBucket rename(ClientSession clientSession, Object id, String newFilename,
      Handler<AsyncResult<Void>> resultHandler) {
    Future<Void> future = this.rename(clientSession, id, newFilename);
    Utils.setHandler(future, resultHandler);
    return this;
  }

  @Override
  public Future<Void> drop() {
    return null;
  }

  @Override
  public GridFSBucket drop(Handler<AsyncResult<Void>> resultHandler) {
    Future<Void> future = this.drop();
    Utils.setHandler(future, resultHandler);
    return this;
  }

  @Override
  public Future<Void> drop(ClientSession clientSession) {
    return null;
  }

  @Override
  public GridFSBucket drop(ClientSession clientSession, Handler<AsyncResult<Void>> resultHandler) {
    Future<Void> future = this.drop(clientSession);
    Utils.setHandler(future, resultHandler);
    return this;
  }
}
