package io.vertx.mongo.client.impl;

import static io.vertx.mongo.impl.Utils.setHandler;
import static java.util.Objects.requireNonNull;

import com.mongodb.reactivestreams.client.MongoClient;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.core.streams.ReadStream;
import io.vertx.mongo.ClientSessionOptions;
import io.vertx.mongo.client.ChangeStreamOptions;
import io.vertx.mongo.client.ClientSession;
import io.vertx.mongo.client.ListDatabasesOptions;
import io.vertx.mongo.client.MongoDatabase;
import io.vertx.mongo.client.MongoResult;
import io.vertx.mongo.connection.ClusterDescription;
import io.vertx.mongo.impl.ConversionUtilsImpl;
import java.io.Closeable;
import java.lang.Override;
import java.lang.String;
import java.util.List;
import org.bson.conversions.Bson;

public class MongoClientImpl extends MongoClientBase implements Closeable {
  protected MongoClient wrapped;

  @Override
  public MongoDatabase getDatabase(String name) {
    requireNonNull(name, "name cannot be null");
    wrapped.getDatabase(name);
    return null;
  }

  @Override
  public void close() {
    wrapped.close();
  }

  @Override
  public MongoResult<String> listDatabaseNames() {
    wrapped.listDatabaseNames();
    return null;
  }

  @Override
  public MongoResult<String> listDatabaseNames(ClientSession clientSession) {
    requireNonNull(clientSession, "clientSession cannot be null");
    com.mongodb.reactivestreams.client.ClientSession __clientSession = clientSession.toDriverClass();
    wrapped.listDatabaseNames(__clientSession);
    return null;
  }

  @Override
  public MongoResult<JsonObject> listDatabases() {
    wrapped.listDatabases();
    return null;
  }

  @Override
  public MongoResult<JsonObject> listDatabases(ListDatabasesOptions options) {
    return null;
  }

  @Override
  public MongoResult<JsonObject> listDatabases(ClientSession clientSession) {
    requireNonNull(clientSession, "clientSession cannot be null");
    com.mongodb.reactivestreams.client.ClientSession __clientSession = clientSession.toDriverClass();
    wrapped.listDatabases(__clientSession);
    return null;
  }

  @Override
  public MongoResult<JsonObject> listDatabases(ClientSession clientSession,
      ListDatabasesOptions options) {
    return null;
  }

  @Override
  public ReadStream<JsonObject> watch() {
    wrapped.watch();
    return null;
  }

  @Override
  public ReadStream<JsonObject> watch(ChangeStreamOptions options) {
    return null;
  }

  @Override
  public ReadStream<JsonObject> watch(List<JsonObject> pipeline) {
    requireNonNull(pipeline, "pipeline cannot be null");
    List<? extends Bson> __pipeline = ConversionUtilsImpl.INSTANCE.toBsonList(pipeline);
    wrapped.watch(__pipeline);
    return null;
  }

  @Override
  public ReadStream<JsonObject> watch(List<JsonObject> pipeline, ChangeStreamOptions options) {
    return null;
  }

  @Override
  public ReadStream<JsonObject> watch(ClientSession clientSession) {
    requireNonNull(clientSession, "clientSession cannot be null");
    com.mongodb.reactivestreams.client.ClientSession __clientSession = clientSession.toDriverClass();
    wrapped.watch(__clientSession);
    return null;
  }

  @Override
  public ReadStream<JsonObject> watch(ClientSession clientSession, ChangeStreamOptions options) {
    return null;
  }

  @Override
  public ReadStream<JsonObject> watch(ClientSession clientSession, List<JsonObject> pipeline) {
    requireNonNull(clientSession, "clientSession cannot be null");
    requireNonNull(pipeline, "pipeline cannot be null");
    com.mongodb.reactivestreams.client.ClientSession __clientSession = clientSession.toDriverClass();
    List<? extends Bson> __pipeline = ConversionUtilsImpl.INSTANCE.toBsonList(pipeline);
    wrapped.watch(__clientSession, __pipeline);
    return null;
  }

  @Override
  public ReadStream<JsonObject> watch(ClientSession clientSession, List<JsonObject> pipeline,
      ChangeStreamOptions options) {
    return null;
  }

  @Override
  public Future<ClientSession> startSession() {
    wrapped.startSession();
    return null;
  }

  @Override
  public io.vertx.mongo.client.MongoClient startSession(
      Handler<AsyncResult<ClientSession>> resultHandler) {
    Future<ClientSession> future = this.startSession();
    setHandler(future, resultHandler);
    return this;
  }

  @Override
  public Future<ClientSession> startSession(ClientSessionOptions options) {
    requireNonNull(options, "options cannot be null");
    com.mongodb.ClientSessionOptions __options = options.toDriverClass();
    wrapped.startSession(__options);
    return null;
  }

  @Override
  public io.vertx.mongo.client.MongoClient startSession(ClientSessionOptions options,
      Handler<AsyncResult<ClientSession>> resultHandler) {
    Future<ClientSession> future = this.startSession(options);
    setHandler(future, resultHandler);
    return this;
  }

  @Override
  public ClusterDescription getClusterDescription() {
    wrapped.getClusterDescription();
    return null;
  }

  public MongoClient toDriverClass() {
    return wrapped;
  }
}
