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
package io.vertx.mongo.client.impl;

import static io.vertx.mongo.impl.Utils.setHandler;
import static java.util.Objects.requireNonNull;

import com.mongodb.reactivestreams.client.AggregatePublisher;
import com.mongodb.reactivestreams.client.ChangeStreamPublisher;
import com.mongodb.reactivestreams.client.ListCollectionsPublisher;
import com.mongodb.reactivestreams.client.MongoDatabase;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.mongo.MongoCollectionResult;
import io.vertx.mongo.MongoResult;
import io.vertx.mongo.ReadConcern;
import io.vertx.mongo.ReadPreference;
import io.vertx.mongo.WriteConcern;
import io.vertx.mongo.client.AggregateOptions;
import io.vertx.mongo.client.ChangeStreamOptions;
import io.vertx.mongo.client.ClientSession;
import io.vertx.mongo.client.ListCollectionsOptions;
import io.vertx.mongo.client.MongoCollection;
import io.vertx.mongo.client.model.CreateCollectionOptions;
import io.vertx.mongo.client.model.CreateViewOptions;
import io.vertx.mongo.client.model.changestream.ChangeStreamDocument;
import io.vertx.mongo.impl.ConversionUtilsImpl;
import io.vertx.mongo.impl.MappingPublisher;
import io.vertx.mongo.impl.MongoClientContext;
import io.vertx.mongo.impl.MongoCollectionResultImpl;
import io.vertx.mongo.impl.MongoResultImpl;
import io.vertx.mongo.impl.SingleResultSubscriber;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.Void;
import java.util.List;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.conversions.Bson;
import org.reactivestreams.Publisher;

public class MongoDatabaseImpl extends MongoDatabaseBase {
  protected final MongoClientContext clientContext;

  protected final MongoDatabase wrapped;

  public MongoDatabaseImpl(MongoClientContext clientContext, MongoDatabase wrapped) {
    this.clientContext = clientContext;
    this.wrapped = wrapped;
  }

  @Override
  public String getName() {
    return wrapped.getName();
  }

  @Override
  public CodecRegistry getCodecRegistry() {
    return wrapped.getCodecRegistry();
  }

  @Override
  public ReadPreference getReadPreference() {
    com.mongodb.ReadPreference __result = wrapped.getReadPreference();
    return ReadPreference.fromDriverClass(__result);
  }

  @Override
  public WriteConcern getWriteConcern() {
    com.mongodb.WriteConcern __result = wrapped.getWriteConcern();
    return WriteConcern.fromDriverClass(__result);
  }

  @Override
  public ReadConcern getReadConcern() {
    com.mongodb.ReadConcern __result = wrapped.getReadConcern();
    return ReadConcern.fromDriverClass(__result);
  }

  @Override
  public io.vertx.mongo.client.MongoDatabase withCodecRegistry(CodecRegistry codecRegistry) {
    requireNonNull(codecRegistry, "codecRegistry is null");
    MongoDatabase __result = wrapped.withCodecRegistry(codecRegistry);
    return new MongoDatabaseImpl(clientContext, __result);
  }

  @Override
  public io.vertx.mongo.client.MongoDatabase withReadPreference(ReadPreference readPreference) {
    requireNonNull(readPreference, "readPreference is null");
    com.mongodb.ReadPreference __readPreference = readPreference.toDriverClass();
    MongoDatabase __result = wrapped.withReadPreference(__readPreference);
    return new MongoDatabaseImpl(clientContext, __result);
  }

  @Override
  public io.vertx.mongo.client.MongoDatabase withWriteConcern(WriteConcern writeConcern) {
    requireNonNull(writeConcern, "writeConcern is null");
    com.mongodb.WriteConcern __writeConcern = writeConcern.toDriverClass();
    MongoDatabase __result = wrapped.withWriteConcern(__writeConcern);
    return new MongoDatabaseImpl(clientContext, __result);
  }

  @Override
  public io.vertx.mongo.client.MongoDatabase withReadConcern(ReadConcern readConcern) {
    requireNonNull(readConcern, "readConcern is null");
    com.mongodb.ReadConcern __readConcern = readConcern.toDriverClass();
    MongoDatabase __result = wrapped.withReadConcern(__readConcern);
    return new MongoDatabaseImpl(clientContext, __result);
  }

  @Override
  public MongoCollection<JsonObject> getCollection(String collectionName) {
    requireNonNull(collectionName, "collectionName is null");
    com.mongodb.reactivestreams.client.MongoCollection<JsonObject> __wrapped = wrapped.getCollection(collectionName, JsonObject.class);
    return new MongoCollectionImpl<>(this.clientContext, __wrapped);
  }

  @Override
  public <TDocument> MongoCollection<TDocument> getCollection(String collectionName,
      Class<TDocument> clazz) {
    requireNonNull(collectionName, "collectionName is null");
    requireNonNull(clazz, "clazz is null");
    com.mongodb.reactivestreams.client.MongoCollection<TDocument> __result = wrapped.getCollection(collectionName, clazz);
    return new MongoCollectionImpl<>(clientContext, __result);
  }

  @Override
  public Future<JsonObject> runCommand(JsonObject command) {
    requireNonNull(command, "command is null");
    Bson __command = ConversionUtilsImpl.INSTANCE.toBson(command);
    Publisher<JsonObject> __publisher = wrapped.runCommand(__command, JsonObject.class);
    Promise<JsonObject> __promise = clientContext.getVertx().promise();
    __publisher.subscribe(new SingleResultSubscriber<>(clientContext, __promise));
    return __promise.future();
  }

  @Override
  public io.vertx.mongo.client.MongoDatabase runCommand(JsonObject command,
      Handler<AsyncResult<JsonObject>> resultHandler) {
    Future<JsonObject> __future = this.runCommand(command);
    setHandler(__future, resultHandler);
    return this;
  }

  @Override
  public Future<JsonObject> runCommand(JsonObject command, ReadPreference readPreference) {
    requireNonNull(command, "command is null");
    requireNonNull(readPreference, "readPreference is null");
    Bson __command = ConversionUtilsImpl.INSTANCE.toBson(command);
    com.mongodb.ReadPreference __readPreference = readPreference.toDriverClass();
    Publisher<JsonObject> __publisher = wrapped.runCommand(__command, __readPreference, JsonObject.class);
    Promise<JsonObject> __promise = clientContext.getVertx().promise();
    __publisher.subscribe(new SingleResultSubscriber<>(clientContext, __promise));
    return __promise.future();
  }

  @Override
  public io.vertx.mongo.client.MongoDatabase runCommand(JsonObject command,
      ReadPreference readPreference, Handler<AsyncResult<JsonObject>> resultHandler) {
    Future<JsonObject> __future = this.runCommand(command, readPreference);
    setHandler(__future, resultHandler);
    return this;
  }

  @Override
  public Future<JsonObject> runCommand(ClientSession clientSession, JsonObject command) {
    requireNonNull(clientSession, "clientSession is null");
    requireNonNull(command, "command is null");
    com.mongodb.reactivestreams.client.ClientSession __clientSession = clientSession.toDriverClass();
    Bson __command = ConversionUtilsImpl.INSTANCE.toBson(command);
    Publisher<JsonObject> __publisher = wrapped.runCommand(__clientSession, __command, JsonObject.class);
    Promise<JsonObject> __promise = clientContext.getVertx().promise();
    __publisher.subscribe(new SingleResultSubscriber<>(clientContext, __promise));
    return __promise.future();
  }

  @Override
  public io.vertx.mongo.client.MongoDatabase runCommand(ClientSession clientSession,
      JsonObject command, Handler<AsyncResult<JsonObject>> resultHandler) {
    Future<JsonObject> __future = this.runCommand(clientSession, command);
    setHandler(__future, resultHandler);
    return this;
  }

  @Override
  public Future<JsonObject> runCommand(ClientSession clientSession, JsonObject command,
      ReadPreference readPreference) {
    requireNonNull(clientSession, "clientSession is null");
    requireNonNull(command, "command is null");
    requireNonNull(readPreference, "readPreference is null");
    com.mongodb.reactivestreams.client.ClientSession __clientSession = clientSession.toDriverClass();
    Bson __command = ConversionUtilsImpl.INSTANCE.toBson(command);
    com.mongodb.ReadPreference __readPreference = readPreference.toDriverClass();
    Publisher<JsonObject> __publisher = wrapped.runCommand(__clientSession, __command, __readPreference, JsonObject.class);
    Promise<JsonObject> __promise = clientContext.getVertx().promise();
    __publisher.subscribe(new SingleResultSubscriber<>(clientContext, __promise));
    return __promise.future();
  }

  @Override
  public io.vertx.mongo.client.MongoDatabase runCommand(ClientSession clientSession,
      JsonObject command, ReadPreference readPreference,
      Handler<AsyncResult<JsonObject>> resultHandler) {
    Future<JsonObject> __future = this.runCommand(clientSession, command, readPreference);
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
  public io.vertx.mongo.client.MongoDatabase drop(Handler<AsyncResult<Void>> resultHandler) {
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
  public io.vertx.mongo.client.MongoDatabase drop(ClientSession clientSession,
      Handler<AsyncResult<Void>> resultHandler) {
    Future<Void> __future = this.drop(clientSession);
    setHandler(__future, resultHandler);
    return this;
  }

  @Override
  public MongoResult<String> listCollectionNames() {
    Publisher<String> __publisher = wrapped.listCollectionNames();
    return new MongoResultImpl<>(clientContext, __publisher);
  }

  @Override
  public MongoResult<String> listCollectionNames(ClientSession clientSession) {
    requireNonNull(clientSession, "clientSession is null");
    com.mongodb.reactivestreams.client.ClientSession __clientSession = clientSession.toDriverClass();
    Publisher<String> __publisher = wrapped.listCollectionNames(__clientSession);
    return new MongoResultImpl<>(clientContext, __publisher);
  }

  @Override
  public MongoResult<JsonObject> listCollections() {
    ListCollectionsPublisher<JsonObject> __publisher = wrapped.listCollections(JsonObject.class);
    return new MongoResultImpl<>(clientContext, __publisher, __publisher::first);
  }

  @Override
  public MongoResult<JsonObject> listCollections(ListCollectionsOptions options) {
    ListCollectionsPublisher<JsonObject> __publisher = wrapped.listCollections(JsonObject.class);
    options.initializePublisher(__publisher);
    Integer __batchSize = options.getBatchSize();
    if (__batchSize != null) {
      return new MongoResultImpl<>(clientContext, __publisher, __publisher::first, __batchSize);
    } else {
      return new MongoResultImpl<>(clientContext, __publisher, __publisher::first);
    }
  }

  @Override
  public MongoResult<JsonObject> listCollections(ClientSession clientSession) {
    requireNonNull(clientSession, "clientSession is null");
    com.mongodb.reactivestreams.client.ClientSession __clientSession = clientSession.toDriverClass();
    ListCollectionsPublisher<JsonObject> __publisher = wrapped.listCollections(__clientSession, JsonObject.class);
    return new MongoResultImpl<>(clientContext, __publisher, __publisher::first);
  }

  @Override
  public MongoResult<JsonObject> listCollections(ClientSession clientSession,
      ListCollectionsOptions options) {
    requireNonNull(clientSession, "clientSession is null");
    com.mongodb.reactivestreams.client.ClientSession __clientSession = clientSession.toDriverClass();
    ListCollectionsPublisher<JsonObject> __publisher = wrapped.listCollections(__clientSession, JsonObject.class);
    options.initializePublisher(__publisher);
    Integer __batchSize = options.getBatchSize();
    if (__batchSize != null) {
      return new MongoResultImpl<>(clientContext, __publisher, __publisher::first, __batchSize);
    } else {
      return new MongoResultImpl<>(clientContext, __publisher, __publisher::first);
    }
  }

  @Override
  public Future<Void> createCollection(String collectionName) {
    requireNonNull(collectionName, "collectionName is null");
    Publisher<Void> __publisher = wrapped.createCollection(collectionName);
    Promise<Void> __promise = clientContext.getVertx().promise();
    __publisher.subscribe(new SingleResultSubscriber<>(clientContext, __promise));
    return __promise.future();
  }

  @Override
  public io.vertx.mongo.client.MongoDatabase createCollection(String collectionName,
      Handler<AsyncResult<Void>> resultHandler) {
    Future<Void> __future = this.createCollection(collectionName);
    setHandler(__future, resultHandler);
    return this;
  }

  @Override
  public Future<Void> createCollection(String collectionName, CreateCollectionOptions options) {
    requireNonNull(collectionName, "collectionName is null");
    requireNonNull(options, "options is null");
    com.mongodb.client.model.CreateCollectionOptions __options = options.toDriverClass();
    Publisher<Void> __publisher = wrapped.createCollection(collectionName, __options);
    Promise<Void> __promise = clientContext.getVertx().promise();
    __publisher.subscribe(new SingleResultSubscriber<>(clientContext, __promise));
    return __promise.future();
  }

  @Override
  public io.vertx.mongo.client.MongoDatabase createCollection(String collectionName,
      CreateCollectionOptions options, Handler<AsyncResult<Void>> resultHandler) {
    Future<Void> __future = this.createCollection(collectionName, options);
    setHandler(__future, resultHandler);
    return this;
  }

  @Override
  public Future<Void> createCollection(ClientSession clientSession, String collectionName) {
    requireNonNull(clientSession, "clientSession is null");
    requireNonNull(collectionName, "collectionName is null");
    com.mongodb.reactivestreams.client.ClientSession __clientSession = clientSession.toDriverClass();
    Publisher<Void> __publisher = wrapped.createCollection(__clientSession, collectionName);
    Promise<Void> __promise = clientContext.getVertx().promise();
    __publisher.subscribe(new SingleResultSubscriber<>(clientContext, __promise));
    return __promise.future();
  }

  @Override
  public io.vertx.mongo.client.MongoDatabase createCollection(ClientSession clientSession,
      String collectionName, Handler<AsyncResult<Void>> resultHandler) {
    Future<Void> __future = this.createCollection(clientSession, collectionName);
    setHandler(__future, resultHandler);
    return this;
  }

  @Override
  public Future<Void> createCollection(ClientSession clientSession, String collectionName,
      CreateCollectionOptions options) {
    requireNonNull(clientSession, "clientSession is null");
    requireNonNull(collectionName, "collectionName is null");
    requireNonNull(options, "options is null");
    com.mongodb.reactivestreams.client.ClientSession __clientSession = clientSession.toDriverClass();
    com.mongodb.client.model.CreateCollectionOptions __options = options.toDriverClass();
    Publisher<Void> __publisher = wrapped.createCollection(__clientSession, collectionName, __options);
    Promise<Void> __promise = clientContext.getVertx().promise();
    __publisher.subscribe(new SingleResultSubscriber<>(clientContext, __promise));
    return __promise.future();
  }

  @Override
  public io.vertx.mongo.client.MongoDatabase createCollection(ClientSession clientSession,
      String collectionName, CreateCollectionOptions options,
      Handler<AsyncResult<Void>> resultHandler) {
    Future<Void> __future = this.createCollection(clientSession, collectionName, options);
    setHandler(__future, resultHandler);
    return this;
  }

  @Override
  public Future<Void> createView(String viewName, String viewOn, JsonArray pipeline) {
    requireNonNull(viewName, "viewName is null");
    requireNonNull(viewOn, "viewOn is null");
    requireNonNull(pipeline, "pipeline is null");
    List<? extends Bson> __pipeline = ConversionUtilsImpl.INSTANCE.toBsonList(pipeline);
    Publisher<Void> __publisher = wrapped.createView(viewName, viewOn, __pipeline);
    Promise<Void> __promise = clientContext.getVertx().promise();
    __publisher.subscribe(new SingleResultSubscriber<>(clientContext, __promise));
    return __promise.future();
  }

  @Override
  public io.vertx.mongo.client.MongoDatabase createView(String viewName, String viewOn,
      JsonArray pipeline, Handler<AsyncResult<Void>> resultHandler) {
    Future<Void> __future = this.createView(viewName, viewOn, pipeline);
    setHandler(__future, resultHandler);
    return this;
  }

  @Override
  public Future<Void> createView(String viewName, String viewOn, JsonArray pipeline,
      CreateViewOptions createViewOptions) {
    requireNonNull(viewName, "viewName is null");
    requireNonNull(viewOn, "viewOn is null");
    requireNonNull(pipeline, "pipeline is null");
    requireNonNull(createViewOptions, "createViewOptions is null");
    List<? extends Bson> __pipeline = ConversionUtilsImpl.INSTANCE.toBsonList(pipeline);
    com.mongodb.client.model.CreateViewOptions __createViewOptions = createViewOptions.toDriverClass();
    Publisher<Void> __publisher = wrapped.createView(viewName, viewOn, __pipeline, __createViewOptions);
    Promise<Void> __promise = clientContext.getVertx().promise();
    __publisher.subscribe(new SingleResultSubscriber<>(clientContext, __promise));
    return __promise.future();
  }

  @Override
  public io.vertx.mongo.client.MongoDatabase createView(String viewName, String viewOn,
      JsonArray pipeline, CreateViewOptions createViewOptions,
      Handler<AsyncResult<Void>> resultHandler) {
    Future<Void> __future = this.createView(viewName, viewOn, pipeline, createViewOptions);
    setHandler(__future, resultHandler);
    return this;
  }

  @Override
  public Future<Void> createView(ClientSession clientSession, String viewName, String viewOn,
      JsonArray pipeline) {
    requireNonNull(clientSession, "clientSession is null");
    requireNonNull(viewName, "viewName is null");
    requireNonNull(viewOn, "viewOn is null");
    requireNonNull(pipeline, "pipeline is null");
    com.mongodb.reactivestreams.client.ClientSession __clientSession = clientSession.toDriverClass();
    List<? extends Bson> __pipeline = ConversionUtilsImpl.INSTANCE.toBsonList(pipeline);
    Publisher<Void> __publisher = wrapped.createView(__clientSession, viewName, viewOn, __pipeline);
    Promise<Void> __promise = clientContext.getVertx().promise();
    __publisher.subscribe(new SingleResultSubscriber<>(clientContext, __promise));
    return __promise.future();
  }

  @Override
  public io.vertx.mongo.client.MongoDatabase createView(ClientSession clientSession,
      String viewName, String viewOn, JsonArray pipeline,
      Handler<AsyncResult<Void>> resultHandler) {
    Future<Void> __future = this.createView(clientSession, viewName, viewOn, pipeline);
    setHandler(__future, resultHandler);
    return this;
  }

  @Override
  public Future<Void> createView(ClientSession clientSession, String viewName, String viewOn,
      JsonArray pipeline, CreateViewOptions createViewOptions) {
    requireNonNull(clientSession, "clientSession is null");
    requireNonNull(viewName, "viewName is null");
    requireNonNull(viewOn, "viewOn is null");
    requireNonNull(pipeline, "pipeline is null");
    requireNonNull(createViewOptions, "createViewOptions is null");
    com.mongodb.reactivestreams.client.ClientSession __clientSession = clientSession.toDriverClass();
    List<? extends Bson> __pipeline = ConversionUtilsImpl.INSTANCE.toBsonList(pipeline);
    com.mongodb.client.model.CreateViewOptions __createViewOptions = createViewOptions.toDriverClass();
    Publisher<Void> __publisher = wrapped.createView(__clientSession, viewName, viewOn, __pipeline, __createViewOptions);
    Promise<Void> __promise = clientContext.getVertx().promise();
    __publisher.subscribe(new SingleResultSubscriber<>(clientContext, __promise));
    return __promise.future();
  }

  @Override
  public io.vertx.mongo.client.MongoDatabase createView(ClientSession clientSession,
      String viewName, String viewOn, JsonArray pipeline, CreateViewOptions createViewOptions,
      Handler<AsyncResult<Void>> resultHandler) {
    Future<Void> __future = this.createView(clientSession, viewName, viewOn, pipeline, createViewOptions);
    setHandler(__future, resultHandler);
    return this;
  }

  @Override
  public MongoResult<ChangeStreamDocument<JsonObject>> watch() {
    ChangeStreamPublisher<JsonObject> __publisher = wrapped.watch(JsonObject.class);
    MappingPublisher<com.mongodb.client.model.changestream.ChangeStreamDocument<JsonObject>, ChangeStreamDocument<JsonObject>> __mappingPublisher = new MappingPublisher<>(__publisher, ChangeStreamDocument::fromDriverClass);
    return new MongoResultImpl<>(clientContext, __mappingPublisher, __mappingPublisher::first);
  }

  @Override
  public MongoResult<ChangeStreamDocument<JsonObject>> watch(ChangeStreamOptions options) {
    ChangeStreamPublisher<JsonObject> __publisher = wrapped.watch(JsonObject.class);
    MappingPublisher<com.mongodb.client.model.changestream.ChangeStreamDocument<JsonObject>, ChangeStreamDocument<JsonObject>> __mappingPublisher = new MappingPublisher<>(__publisher, ChangeStreamDocument::fromDriverClass);
    options.initializePublisher(__publisher);
    Integer __batchSize = options.getBatchSize();
    if (__batchSize != null) {
      return new MongoResultImpl<>(clientContext, __mappingPublisher, __mappingPublisher::first, __batchSize);
    } else {
      return new MongoResultImpl<>(clientContext, __mappingPublisher, __mappingPublisher::first);
    }
  }

  @Override
  public MongoResult<ChangeStreamDocument<JsonObject>> watch(JsonArray pipeline) {
    requireNonNull(pipeline, "pipeline is null");
    List<? extends Bson> __pipeline = ConversionUtilsImpl.INSTANCE.toBsonList(pipeline);
    ChangeStreamPublisher<JsonObject> __publisher = wrapped.watch(__pipeline, JsonObject.class);
    MappingPublisher<com.mongodb.client.model.changestream.ChangeStreamDocument<JsonObject>, ChangeStreamDocument<JsonObject>> __mappingPublisher = new MappingPublisher<>(__publisher, ChangeStreamDocument::fromDriverClass);
    return new MongoResultImpl<>(clientContext, __mappingPublisher, __mappingPublisher::first);
  }

  @Override
  public MongoResult<ChangeStreamDocument<JsonObject>> watch(JsonArray pipeline,
      ChangeStreamOptions options) {
    requireNonNull(pipeline, "pipeline is null");
    List<? extends Bson> __pipeline = ConversionUtilsImpl.INSTANCE.toBsonList(pipeline);
    ChangeStreamPublisher<JsonObject> __publisher = wrapped.watch(__pipeline, JsonObject.class);
    MappingPublisher<com.mongodb.client.model.changestream.ChangeStreamDocument<JsonObject>, ChangeStreamDocument<JsonObject>> __mappingPublisher = new MappingPublisher<>(__publisher, ChangeStreamDocument::fromDriverClass);
    options.initializePublisher(__publisher);
    Integer __batchSize = options.getBatchSize();
    if (__batchSize != null) {
      return new MongoResultImpl<>(clientContext, __mappingPublisher, __mappingPublisher::first, __batchSize);
    } else {
      return new MongoResultImpl<>(clientContext, __mappingPublisher, __mappingPublisher::first);
    }
  }

  @Override
  public MongoResult<ChangeStreamDocument<JsonObject>> watch(ClientSession clientSession) {
    requireNonNull(clientSession, "clientSession is null");
    com.mongodb.reactivestreams.client.ClientSession __clientSession = clientSession.toDriverClass();
    ChangeStreamPublisher<JsonObject> __publisher = wrapped.watch(__clientSession, JsonObject.class);
    MappingPublisher<com.mongodb.client.model.changestream.ChangeStreamDocument<JsonObject>, ChangeStreamDocument<JsonObject>> __mappingPublisher = new MappingPublisher<>(__publisher, ChangeStreamDocument::fromDriverClass);
    return new MongoResultImpl<>(clientContext, __mappingPublisher, __mappingPublisher::first);
  }

  @Override
  public MongoResult<ChangeStreamDocument<JsonObject>> watch(ClientSession clientSession,
      ChangeStreamOptions options) {
    requireNonNull(clientSession, "clientSession is null");
    com.mongodb.reactivestreams.client.ClientSession __clientSession = clientSession.toDriverClass();
    ChangeStreamPublisher<JsonObject> __publisher = wrapped.watch(__clientSession, JsonObject.class);
    MappingPublisher<com.mongodb.client.model.changestream.ChangeStreamDocument<JsonObject>, ChangeStreamDocument<JsonObject>> __mappingPublisher = new MappingPublisher<>(__publisher, ChangeStreamDocument::fromDriverClass);
    options.initializePublisher(__publisher);
    Integer __batchSize = options.getBatchSize();
    if (__batchSize != null) {
      return new MongoResultImpl<>(clientContext, __mappingPublisher, __mappingPublisher::first, __batchSize);
    } else {
      return new MongoResultImpl<>(clientContext, __mappingPublisher, __mappingPublisher::first);
    }
  }

  @Override
  public MongoResult<ChangeStreamDocument<JsonObject>> watch(ClientSession clientSession,
      JsonArray pipeline) {
    requireNonNull(clientSession, "clientSession is null");
    requireNonNull(pipeline, "pipeline is null");
    com.mongodb.reactivestreams.client.ClientSession __clientSession = clientSession.toDriverClass();
    List<? extends Bson> __pipeline = ConversionUtilsImpl.INSTANCE.toBsonList(pipeline);
    ChangeStreamPublisher<JsonObject> __publisher = wrapped.watch(__clientSession, __pipeline, JsonObject.class);
    MappingPublisher<com.mongodb.client.model.changestream.ChangeStreamDocument<JsonObject>, ChangeStreamDocument<JsonObject>> __mappingPublisher = new MappingPublisher<>(__publisher, ChangeStreamDocument::fromDriverClass);
    return new MongoResultImpl<>(clientContext, __mappingPublisher, __mappingPublisher::first);
  }

  @Override
  public MongoResult<ChangeStreamDocument<JsonObject>> watch(ClientSession clientSession,
      JsonArray pipeline, ChangeStreamOptions options) {
    requireNonNull(clientSession, "clientSession is null");
    requireNonNull(pipeline, "pipeline is null");
    com.mongodb.reactivestreams.client.ClientSession __clientSession = clientSession.toDriverClass();
    List<? extends Bson> __pipeline = ConversionUtilsImpl.INSTANCE.toBsonList(pipeline);
    ChangeStreamPublisher<JsonObject> __publisher = wrapped.watch(__clientSession, __pipeline, JsonObject.class);
    MappingPublisher<com.mongodb.client.model.changestream.ChangeStreamDocument<JsonObject>, ChangeStreamDocument<JsonObject>> __mappingPublisher = new MappingPublisher<>(__publisher, ChangeStreamDocument::fromDriverClass);
    options.initializePublisher(__publisher);
    Integer __batchSize = options.getBatchSize();
    if (__batchSize != null) {
      return new MongoResultImpl<>(clientContext, __mappingPublisher, __mappingPublisher::first, __batchSize);
    } else {
      return new MongoResultImpl<>(clientContext, __mappingPublisher, __mappingPublisher::first);
    }
  }

  @Override
  public MongoCollectionResult<JsonObject> aggregate(JsonArray pipeline) {
    requireNonNull(pipeline, "pipeline is null");
    List<? extends Bson> __pipeline = ConversionUtilsImpl.INSTANCE.toBsonList(pipeline);
    AggregatePublisher<JsonObject> __publisher = wrapped.aggregate(__pipeline, JsonObject.class);
    return new MongoCollectionResultImpl<>(__publisher::toCollection, clientContext, __publisher, __publisher::first);
  }

  @Override
  public MongoCollectionResult<JsonObject> aggregate(JsonArray pipeline, AggregateOptions options) {
    requireNonNull(pipeline, "pipeline is null");
    List<? extends Bson> __pipeline = ConversionUtilsImpl.INSTANCE.toBsonList(pipeline);
    AggregatePublisher<JsonObject> __publisher = wrapped.aggregate(__pipeline, JsonObject.class);
    options.initializePublisher(__publisher);
    Integer __batchSize = options.getBatchSize();
    if (__batchSize != null) {
      return new MongoCollectionResultImpl<>(__publisher::toCollection, clientContext, __publisher, __publisher::first, __batchSize);
    } else {
      return new MongoCollectionResultImpl<>(__publisher::toCollection, clientContext, __publisher, __publisher::first);
    }
  }

  @Override
  public MongoCollectionResult<JsonObject> aggregate(ClientSession clientSession,
      JsonArray pipeline) {
    requireNonNull(clientSession, "clientSession is null");
    requireNonNull(pipeline, "pipeline is null");
    com.mongodb.reactivestreams.client.ClientSession __clientSession = clientSession.toDriverClass();
    List<? extends Bson> __pipeline = ConversionUtilsImpl.INSTANCE.toBsonList(pipeline);
    AggregatePublisher<JsonObject> __publisher = wrapped.aggregate(__clientSession, __pipeline, JsonObject.class);
    return new MongoCollectionResultImpl<>(__publisher::toCollection, clientContext, __publisher, __publisher::first);
  }

  @Override
  public MongoCollectionResult<JsonObject> aggregate(ClientSession clientSession,
      JsonArray pipeline, AggregateOptions options) {
    requireNonNull(clientSession, "clientSession is null");
    requireNonNull(pipeline, "pipeline is null");
    com.mongodb.reactivestreams.client.ClientSession __clientSession = clientSession.toDriverClass();
    List<? extends Bson> __pipeline = ConversionUtilsImpl.INSTANCE.toBsonList(pipeline);
    AggregatePublisher<JsonObject> __publisher = wrapped.aggregate(__clientSession, __pipeline, JsonObject.class);
    options.initializePublisher(__publisher);
    Integer __batchSize = options.getBatchSize();
    if (__batchSize != null) {
      return new MongoCollectionResultImpl<>(__publisher::toCollection, clientContext, __publisher, __publisher::first, __batchSize);
    } else {
      return new MongoCollectionResultImpl<>(__publisher::toCollection, clientContext, __publisher, __publisher::first);
    }
  }

  public MongoDatabase toDriverClass() {
    return wrapped;
  }
}
