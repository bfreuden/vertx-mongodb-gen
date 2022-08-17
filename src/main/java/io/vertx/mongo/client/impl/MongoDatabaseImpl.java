package io.vertx.mongo.client.impl;

import static io.vertx.mongo.impl.Utils.setHandler;
import static java.util.Objects.requireNonNull;

import com.mongodb.reactivestreams.client.MongoDatabase;
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
import io.vertx.mongo.client.MongoResult;
import io.vertx.mongo.client.model.CreateCollectionOptions;
import io.vertx.mongo.client.model.CreateViewOptions;
import io.vertx.mongo.impl.ConversionUtilsImpl;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.Void;
import java.util.List;
import org.bson.conversions.Bson;

public class MongoDatabaseImpl extends MongoDatabaseBase {
  protected MongoDatabase wrapped;

  @Override
  public String getName() {
    wrapped.getName();
    return null;
  }

  @Override
  public ReadPreference getReadPreference() {
    wrapped.getReadPreference();
    return null;
  }

  @Override
  public WriteConcern getWriteConcern() {
    wrapped.getWriteConcern();
    return null;
  }

  @Override
  public ReadConcern getReadConcern() {
    wrapped.getReadConcern();
    return null;
  }

  @Override
  public io.vertx.mongo.client.MongoDatabase withReadPreference(ReadPreference readPreference) {
    requireNonNull(readPreference, "readPreference cannot be null");
    com.mongodb.ReadPreference __readPreference = readPreference.toDriverClass();
    wrapped.withReadPreference(__readPreference);
    return null;
  }

  @Override
  public io.vertx.mongo.client.MongoDatabase withWriteConcern(WriteConcern writeConcern) {
    requireNonNull(writeConcern, "writeConcern cannot be null");
    com.mongodb.WriteConcern __writeConcern = writeConcern.toDriverClass();
    wrapped.withWriteConcern(__writeConcern);
    return null;
  }

  @Override
  public io.vertx.mongo.client.MongoDatabase withReadConcern(ReadConcern readConcern) {
    requireNonNull(readConcern, "readConcern cannot be null");
    com.mongodb.ReadConcern __readConcern = readConcern.toDriverClass();
    wrapped.withReadConcern(__readConcern);
    return null;
  }

  @Override
  public MongoCollection<JsonObject> getCollection(String collectionName) {
    requireNonNull(collectionName, "collectionName cannot be null");
    wrapped.getCollection(collectionName);
    return null;
  }

  @Override
  public <TDocument> MongoCollection<TDocument> getCollection(String collectionName,
      Class<TDocument> clazz) {
    requireNonNull(collectionName, "collectionName cannot be null");
    requireNonNull(clazz, "clazz cannot be null");
    wrapped.getCollection(collectionName, clazz);
    return null;
  }

  @Override
  public MongoResult<JsonObject> runCommand(JsonObject command) {
    requireNonNull(command, "command cannot be null");
    Bson __command = ConversionUtilsImpl.INSTANCE.toBson(command);
    wrapped.runCommand(__command);
    return null;
  }

  @Override
  public MongoResult<JsonObject> runCommand(JsonObject command, ReadPreference readPreference) {
    requireNonNull(command, "command cannot be null");
    requireNonNull(readPreference, "readPreference cannot be null");
    Bson __command = ConversionUtilsImpl.INSTANCE.toBson(command);
    com.mongodb.ReadPreference __readPreference = readPreference.toDriverClass();
    wrapped.runCommand(__command, __readPreference);
    return null;
  }

  @Override
  public MongoResult<JsonObject> runCommand(ClientSession clientSession, JsonObject command) {
    requireNonNull(clientSession, "clientSession cannot be null");
    requireNonNull(command, "command cannot be null");
    com.mongodb.reactivestreams.client.ClientSession __clientSession = clientSession.toDriverClass();
    Bson __command = ConversionUtilsImpl.INSTANCE.toBson(command);
    wrapped.runCommand(__clientSession, __command);
    return null;
  }

  @Override
  public MongoResult<JsonObject> runCommand(ClientSession clientSession, JsonObject command,
      ReadPreference readPreference) {
    requireNonNull(clientSession, "clientSession cannot be null");
    requireNonNull(command, "command cannot be null");
    requireNonNull(readPreference, "readPreference cannot be null");
    com.mongodb.reactivestreams.client.ClientSession __clientSession = clientSession.toDriverClass();
    Bson __command = ConversionUtilsImpl.INSTANCE.toBson(command);
    com.mongodb.ReadPreference __readPreference = readPreference.toDriverClass();
    wrapped.runCommand(__clientSession, __command, __readPreference);
    return null;
  }

  @Override
  public Future<Void> drop() {
    wrapped.drop();
    return null;
  }

  @Override
  public io.vertx.mongo.client.MongoDatabase drop(Handler<AsyncResult<Void>> resultHandler) {
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
  public io.vertx.mongo.client.MongoDatabase drop(ClientSession clientSession,
      Handler<AsyncResult<Void>> resultHandler) {
    Future<Void> future = this.drop(clientSession);
    setHandler(future, resultHandler);
    return this;
  }

  @Override
  public MongoResult<String> listCollectionNames() {
    wrapped.listCollectionNames();
    return null;
  }

  @Override
  public MongoResult<String> listCollectionNames(ClientSession clientSession) {
    requireNonNull(clientSession, "clientSession cannot be null");
    com.mongodb.reactivestreams.client.ClientSession __clientSession = clientSession.toDriverClass();
    wrapped.listCollectionNames(__clientSession);
    return null;
  }

  @Override
  public MongoResult<JsonObject> listCollections() {
    wrapped.listCollections();
    return null;
  }

  @Override
  public MongoResult<JsonObject> listCollections(ListCollectionsOptions options) {
    return null;
  }

  @Override
  public MongoResult<JsonObject> listCollections(ClientSession clientSession) {
    requireNonNull(clientSession, "clientSession cannot be null");
    com.mongodb.reactivestreams.client.ClientSession __clientSession = clientSession.toDriverClass();
    wrapped.listCollections(__clientSession);
    return null;
  }

  @Override
  public MongoResult<JsonObject> listCollections(ClientSession clientSession,
      ListCollectionsOptions options) {
    return null;
  }

  @Override
  public Future<Void> createCollection(String collectionName) {
    requireNonNull(collectionName, "collectionName cannot be null");
    wrapped.createCollection(collectionName);
    return null;
  }

  @Override
  public io.vertx.mongo.client.MongoDatabase createCollection(String collectionName,
      Handler<AsyncResult<Void>> resultHandler) {
    Future<Void> future = this.createCollection(collectionName);
    setHandler(future, resultHandler);
    return this;
  }

  @Override
  public Future<Void> createCollection(String collectionName, CreateCollectionOptions options) {
    requireNonNull(collectionName, "collectionName cannot be null");
    requireNonNull(options, "options cannot be null");
    com.mongodb.client.model.CreateCollectionOptions __options = options.toDriverClass();
    wrapped.createCollection(collectionName, __options);
    return null;
  }

  @Override
  public io.vertx.mongo.client.MongoDatabase createCollection(String collectionName,
      CreateCollectionOptions options, Handler<AsyncResult<Void>> resultHandler) {
    Future<Void> future = this.createCollection(collectionName, options);
    setHandler(future, resultHandler);
    return this;
  }

  @Override
  public Future<Void> createCollection(ClientSession clientSession, String collectionName) {
    requireNonNull(clientSession, "clientSession cannot be null");
    requireNonNull(collectionName, "collectionName cannot be null");
    com.mongodb.reactivestreams.client.ClientSession __clientSession = clientSession.toDriverClass();
    wrapped.createCollection(__clientSession, collectionName);
    return null;
  }

  @Override
  public io.vertx.mongo.client.MongoDatabase createCollection(ClientSession clientSession,
      String collectionName, Handler<AsyncResult<Void>> resultHandler) {
    Future<Void> future = this.createCollection(clientSession, collectionName);
    setHandler(future, resultHandler);
    return this;
  }

  @Override
  public Future<Void> createCollection(ClientSession clientSession, String collectionName,
      CreateCollectionOptions options) {
    requireNonNull(clientSession, "clientSession cannot be null");
    requireNonNull(collectionName, "collectionName cannot be null");
    requireNonNull(options, "options cannot be null");
    com.mongodb.reactivestreams.client.ClientSession __clientSession = clientSession.toDriverClass();
    com.mongodb.client.model.CreateCollectionOptions __options = options.toDriverClass();
    wrapped.createCollection(__clientSession, collectionName, __options);
    return null;
  }

  @Override
  public io.vertx.mongo.client.MongoDatabase createCollection(ClientSession clientSession,
      String collectionName, CreateCollectionOptions options,
      Handler<AsyncResult<Void>> resultHandler) {
    Future<Void> future = this.createCollection(clientSession, collectionName, options);
    setHandler(future, resultHandler);
    return this;
  }

  @Override
  public Future<Void> createView(String viewName, String viewOn, List<JsonObject> pipeline) {
    requireNonNull(viewName, "viewName cannot be null");
    requireNonNull(viewOn, "viewOn cannot be null");
    requireNonNull(pipeline, "pipeline cannot be null");
    List<? extends Bson> __pipeline = ConversionUtilsImpl.INSTANCE.toBsonList(pipeline);
    wrapped.createView(viewName, viewOn, __pipeline);
    return null;
  }

  @Override
  public io.vertx.mongo.client.MongoDatabase createView(String viewName, String viewOn,
      List<JsonObject> pipeline, Handler<AsyncResult<Void>> resultHandler) {
    Future<Void> future = this.createView(viewName, viewOn, pipeline);
    setHandler(future, resultHandler);
    return this;
  }

  @Override
  public Future<Void> createView(String viewName, String viewOn, List<JsonObject> pipeline,
      CreateViewOptions createViewOptions) {
    requireNonNull(viewName, "viewName cannot be null");
    requireNonNull(viewOn, "viewOn cannot be null");
    requireNonNull(pipeline, "pipeline cannot be null");
    requireNonNull(createViewOptions, "createViewOptions cannot be null");
    List<? extends Bson> __pipeline = ConversionUtilsImpl.INSTANCE.toBsonList(pipeline);
    com.mongodb.client.model.CreateViewOptions __createViewOptions = createViewOptions.toDriverClass();
    wrapped.createView(viewName, viewOn, __pipeline, __createViewOptions);
    return null;
  }

  @Override
  public io.vertx.mongo.client.MongoDatabase createView(String viewName, String viewOn,
      List<JsonObject> pipeline, CreateViewOptions createViewOptions,
      Handler<AsyncResult<Void>> resultHandler) {
    Future<Void> future = this.createView(viewName, viewOn, pipeline, createViewOptions);
    setHandler(future, resultHandler);
    return this;
  }

  @Override
  public Future<Void> createView(ClientSession clientSession, String viewName, String viewOn,
      List<JsonObject> pipeline) {
    requireNonNull(clientSession, "clientSession cannot be null");
    requireNonNull(viewName, "viewName cannot be null");
    requireNonNull(viewOn, "viewOn cannot be null");
    requireNonNull(pipeline, "pipeline cannot be null");
    com.mongodb.reactivestreams.client.ClientSession __clientSession = clientSession.toDriverClass();
    List<? extends Bson> __pipeline = ConversionUtilsImpl.INSTANCE.toBsonList(pipeline);
    wrapped.createView(__clientSession, viewName, viewOn, __pipeline);
    return null;
  }

  @Override
  public io.vertx.mongo.client.MongoDatabase createView(ClientSession clientSession,
      String viewName, String viewOn, List<JsonObject> pipeline,
      Handler<AsyncResult<Void>> resultHandler) {
    Future<Void> future = this.createView(clientSession, viewName, viewOn, pipeline);
    setHandler(future, resultHandler);
    return this;
  }

  @Override
  public Future<Void> createView(ClientSession clientSession, String viewName, String viewOn,
      List<JsonObject> pipeline, CreateViewOptions createViewOptions) {
    requireNonNull(clientSession, "clientSession cannot be null");
    requireNonNull(viewName, "viewName cannot be null");
    requireNonNull(viewOn, "viewOn cannot be null");
    requireNonNull(pipeline, "pipeline cannot be null");
    requireNonNull(createViewOptions, "createViewOptions cannot be null");
    com.mongodb.reactivestreams.client.ClientSession __clientSession = clientSession.toDriverClass();
    List<? extends Bson> __pipeline = ConversionUtilsImpl.INSTANCE.toBsonList(pipeline);
    com.mongodb.client.model.CreateViewOptions __createViewOptions = createViewOptions.toDriverClass();
    wrapped.createView(__clientSession, viewName, viewOn, __pipeline, __createViewOptions);
    return null;
  }

  @Override
  public io.vertx.mongo.client.MongoDatabase createView(ClientSession clientSession,
      String viewName, String viewOn, List<JsonObject> pipeline,
      CreateViewOptions createViewOptions, Handler<AsyncResult<Void>> resultHandler) {
    Future<Void> future = this.createView(clientSession, viewName, viewOn, pipeline, createViewOptions);
    setHandler(future, resultHandler);
    return this;
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
  public MongoResult<JsonObject> aggregate(List<JsonObject> pipeline) {
    requireNonNull(pipeline, "pipeline cannot be null");
    List<? extends Bson> __pipeline = ConversionUtilsImpl.INSTANCE.toBsonList(pipeline);
    wrapped.aggregate(__pipeline);
    return null;
  }

  @Override
  public MongoResult<JsonObject> aggregate(List<JsonObject> pipeline, AggregateOptions options) {
    return null;
  }

  @Override
  public MongoResult<JsonObject> aggregate(ClientSession clientSession, List<JsonObject> pipeline) {
    requireNonNull(clientSession, "clientSession cannot be null");
    requireNonNull(pipeline, "pipeline cannot be null");
    com.mongodb.reactivestreams.client.ClientSession __clientSession = clientSession.toDriverClass();
    List<? extends Bson> __pipeline = ConversionUtilsImpl.INSTANCE.toBsonList(pipeline);
    wrapped.aggregate(__clientSession, __pipeline);
    return null;
  }

  @Override
  public MongoResult<JsonObject> aggregate(ClientSession clientSession, List<JsonObject> pipeline,
      AggregateOptions options) {
    return null;
  }

  public MongoDatabase toDriverClass() {
    return wrapped;
  }
}
