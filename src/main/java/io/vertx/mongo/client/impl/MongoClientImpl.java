package io.vertx.mongo.client.impl;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.core.streams.ReadStream;
import io.vertx.mongo.ClientSessionOptions;
import io.vertx.mongo.client.ChangeStreamOptions;
import io.vertx.mongo.client.ClientSession;
import io.vertx.mongo.client.ListDatabasesOptions;
import io.vertx.mongo.client.MongoClient;
import io.vertx.mongo.client.MongoDatabase;
import io.vertx.mongo.client.MongoResult;
import io.vertx.mongo.connection.ClusterDescription;
import io.vertx.mongo.impl.Utils;
import java.io.Closeable;
import java.lang.Override;
import java.lang.String;
import java.util.List;

public class MongoClientImpl extends MongoClientBase implements Closeable {
  @Override
  public MongoDatabase getDatabase(String name) {
    return null;
  }

  @Override
  public void close() {
  }

  @Override
  public MongoResult<String> listDatabaseNames() {
    return null;
  }

  @Override
  public MongoResult<String> listDatabaseNames(ClientSession clientSession) {
    return null;
  }

  @Override
  public MongoResult<JsonObject> listDatabases() {
    return null;
  }

  @Override
  public MongoResult<JsonObject> listDatabases(ListDatabasesOptions options) {
    return null;
  }

  @Override
  public MongoResult<JsonObject> listDatabases(ClientSession clientSession) {
    return null;
  }

  @Override
  public MongoResult<JsonObject> listDatabases(ClientSession clientSession,
      ListDatabasesOptions options) {
    return null;
  }

  @Override
  public ReadStream<JsonObject> watch() {
    return null;
  }

  @Override
  public ReadStream<JsonObject> watch(ChangeStreamOptions options) {
    return null;
  }

  @Override
  public ReadStream<JsonObject> watch(List<JsonObject> pipeline) {
    return null;
  }

  @Override
  public ReadStream<JsonObject> watch(List<JsonObject> pipeline, ChangeStreamOptions options) {
    return null;
  }

  @Override
  public ReadStream<JsonObject> watch(ClientSession clientSession) {
    return null;
  }

  @Override
  public ReadStream<JsonObject> watch(ClientSession clientSession, ChangeStreamOptions options) {
    return null;
  }

  @Override
  public ReadStream<JsonObject> watch(ClientSession clientSession, List<JsonObject> pipeline) {
    return null;
  }

  @Override
  public ReadStream<JsonObject> watch(ClientSession clientSession, List<JsonObject> pipeline,
      ChangeStreamOptions options) {
    return null;
  }

  @Override
  public Future<ClientSession> startSession() {
    return null;
  }

  @Override
  public MongoClient startSession(Handler<AsyncResult<ClientSession>> resultHandler) {
    Future<ClientSession> future = this.startSession();
    Utils.setHandler(future, resultHandler);
    return this;
  }

  @Override
  public Future<ClientSession> startSession(ClientSessionOptions options) {
    com.mongodb.ClientSessionOptions __options = options.toDriverClass();
    return null;
  }

  @Override
  public MongoClient startSession(ClientSessionOptions options,
      Handler<AsyncResult<ClientSession>> resultHandler) {
    Future<ClientSession> future = this.startSession(options);
    Utils.setHandler(future, resultHandler);
    return this;
  }

  @Override
  public ClusterDescription getClusterDescription() {
    return null;
  }
}
