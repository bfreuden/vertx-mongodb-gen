package io.vertx.mongo.client.impl;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.core.streams.ReadStream;
import io.vertx.mongo.ReadConcern;
import io.vertx.mongo.ReadPreference;
import io.vertx.mongo.WriteConcern;
import io.vertx.mongo.client.AggregateOptions;
import io.vertx.mongo.client.ChangeStreamOptions;
import io.vertx.mongo.client.ClientSession;
import io.vertx.mongo.client.ListCollectionsOptions;
import io.vertx.mongo.client.MongoCollection;
import io.vertx.mongo.client.MongoDatabase;
import io.vertx.mongo.client.MongoResult;
import io.vertx.mongo.client.model.CreateCollectionOptions;
import io.vertx.mongo.client.model.CreateViewOptions;
import io.vertx.mongo.impl.Utils;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.Void;
import java.util.List;

public class MongoDatabaseImpl extends MongoDatabaseBase {
  @Override
  public String getName() {
    return null;
  }

  @Override
  public ReadPreference getReadPreference() {
    return null;
  }

  @Override
  public WriteConcern getWriteConcern() {
    return null;
  }

  @Override
  public ReadConcern getReadConcern() {
    return null;
  }

  @Override
  public MongoDatabase withReadPreference(ReadPreference readPreference) {
    com.mongodb.ReadPreference __readPreference = readPreference.toDriverClass();
    return null;
  }

  @Override
  public MongoDatabase withWriteConcern(WriteConcern writeConcern) {
    com.mongodb.WriteConcern __writeConcern = writeConcern.toDriverClass();
    return null;
  }

  @Override
  public MongoDatabase withReadConcern(ReadConcern readConcern) {
    com.mongodb.ReadConcern __readConcern = readConcern.toDriverClass();
    return null;
  }

  @Override
  public MongoCollection<JsonObject> getCollection(String collectionName) {
    return null;
  }

  @Override
  public <TDocument> MongoCollection<TDocument> getCollection(String collectionName,
      Class<TDocument> clazz) {
    return null;
  }

  @Override
  public MongoResult<JsonObject> runCommand(JsonObject command) {
    return null;
  }

  @Override
  public MongoResult<JsonObject> runCommand(JsonObject command, ReadPreference readPreference) {
    com.mongodb.ReadPreference __readPreference = readPreference.toDriverClass();
    return null;
  }

  @Override
  public MongoResult<JsonObject> runCommand(ClientSession clientSession, JsonObject command) {
    return null;
  }

  @Override
  public MongoResult<JsonObject> runCommand(ClientSession clientSession, JsonObject command,
      ReadPreference readPreference) {
    com.mongodb.ReadPreference __readPreference = readPreference.toDriverClass();
    return null;
  }

  @Override
  public Future<Void> drop() {
    return null;
  }

  @Override
  public MongoDatabase drop(Handler<AsyncResult<Void>> resultHandler) {
    Future<Void> future = this.drop();
    Utils.setHandler(future, resultHandler);
    return this;
  }

  @Override
  public Future<Void> drop(ClientSession clientSession) {
    return null;
  }

  @Override
  public MongoDatabase drop(ClientSession clientSession, Handler<AsyncResult<Void>> resultHandler) {
    Future<Void> future = this.drop(clientSession);
    Utils.setHandler(future, resultHandler);
    return this;
  }

  @Override
  public MongoResult<String> listCollectionNames() {
    return null;
  }

  @Override
  public MongoResult<String> listCollectionNames(ClientSession clientSession) {
    return null;
  }

  @Override
  public MongoResult<JsonObject> listCollections() {
    return null;
  }

  @Override
  public MongoResult<JsonObject> listCollections(ListCollectionsOptions options) {
    return null;
  }

  @Override
  public MongoResult<JsonObject> listCollections(ClientSession clientSession) {
    return null;
  }

  @Override
  public MongoResult<JsonObject> listCollections(ClientSession clientSession,
      ListCollectionsOptions options) {
    return null;
  }

  @Override
  public Future<Void> createCollection(String collectionName) {
    return null;
  }

  @Override
  public MongoDatabase createCollection(String collectionName,
      Handler<AsyncResult<Void>> resultHandler) {
    Future<Void> future = this.createCollection(collectionName);
    Utils.setHandler(future, resultHandler);
    return this;
  }

  @Override
  public Future<Void> createCollection(String collectionName, CreateCollectionOptions options) {
    com.mongodb.client.model.CreateCollectionOptions __options = options.toDriverClass();
    return null;
  }

  @Override
  public MongoDatabase createCollection(String collectionName, CreateCollectionOptions options,
      Handler<AsyncResult<Void>> resultHandler) {
    Future<Void> future = this.createCollection(collectionName, options);
    Utils.setHandler(future, resultHandler);
    return this;
  }

  @Override
  public Future<Void> createCollection(ClientSession clientSession, String collectionName) {
    return null;
  }

  @Override
  public MongoDatabase createCollection(ClientSession clientSession, String collectionName,
      Handler<AsyncResult<Void>> resultHandler) {
    Future<Void> future = this.createCollection(clientSession, collectionName);
    Utils.setHandler(future, resultHandler);
    return this;
  }

  @Override
  public Future<Void> createCollection(ClientSession clientSession, String collectionName,
      CreateCollectionOptions options) {
    com.mongodb.client.model.CreateCollectionOptions __options = options.toDriverClass();
    return null;
  }

  @Override
  public MongoDatabase createCollection(ClientSession clientSession, String collectionName,
      CreateCollectionOptions options, Handler<AsyncResult<Void>> resultHandler) {
    Future<Void> future = this.createCollection(clientSession, collectionName, options);
    Utils.setHandler(future, resultHandler);
    return this;
  }

  @Override
  public Future<Void> createView(String viewName, String viewOn, List<JsonObject> pipeline) {
    return null;
  }

  @Override
  public MongoDatabase createView(String viewName, String viewOn, List<JsonObject> pipeline,
      Handler<AsyncResult<Void>> resultHandler) {
    Future<Void> future = this.createView(viewName, viewOn, pipeline);
    Utils.setHandler(future, resultHandler);
    return this;
  }

  @Override
  public Future<Void> createView(String viewName, String viewOn, List<JsonObject> pipeline,
      CreateViewOptions createViewOptions) {
    com.mongodb.client.model.CreateViewOptions __createViewOptions = createViewOptions.toDriverClass();
    return null;
  }

  @Override
  public MongoDatabase createView(String viewName, String viewOn, List<JsonObject> pipeline,
      CreateViewOptions createViewOptions, Handler<AsyncResult<Void>> resultHandler) {
    Future<Void> future = this.createView(viewName, viewOn, pipeline, createViewOptions);
    Utils.setHandler(future, resultHandler);
    return this;
  }

  @Override
  public Future<Void> createView(ClientSession clientSession, String viewName, String viewOn,
      List<JsonObject> pipeline) {
    return null;
  }

  @Override
  public MongoDatabase createView(ClientSession clientSession, String viewName, String viewOn,
      List<JsonObject> pipeline, Handler<AsyncResult<Void>> resultHandler) {
    Future<Void> future = this.createView(clientSession, viewName, viewOn, pipeline);
    Utils.setHandler(future, resultHandler);
    return this;
  }

  @Override
  public Future<Void> createView(ClientSession clientSession, String viewName, String viewOn,
      List<JsonObject> pipeline, CreateViewOptions createViewOptions) {
    com.mongodb.client.model.CreateViewOptions __createViewOptions = createViewOptions.toDriverClass();
    return null;
  }

  @Override
  public MongoDatabase createView(ClientSession clientSession, String viewName, String viewOn,
      List<JsonObject> pipeline, CreateViewOptions createViewOptions,
      Handler<AsyncResult<Void>> resultHandler) {
    Future<Void> future = this.createView(clientSession, viewName, viewOn, pipeline, createViewOptions);
    Utils.setHandler(future, resultHandler);
    return this;
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
  public MongoResult<JsonObject> aggregate(List<JsonObject> pipeline) {
    return null;
  }

  @Override
  public MongoResult<JsonObject> aggregate(List<JsonObject> pipeline, AggregateOptions options) {
    return null;
  }

  @Override
  public MongoResult<JsonObject> aggregate(ClientSession clientSession, List<JsonObject> pipeline) {
    return null;
  }

  @Override
  public MongoResult<JsonObject> aggregate(ClientSession clientSession, List<JsonObject> pipeline,
      AggregateOptions options) {
    return null;
  }
}
