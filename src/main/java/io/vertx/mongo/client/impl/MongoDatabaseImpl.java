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

import static java.util.Objects.requireNonNull;

import com.mongodb.reactivestreams.client.MongoDatabase;
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
import io.vertx.mongo.impl.MongoClientContext;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.Void;
import java.util.List;
import org.bson.Document;
import org.bson.conversions.Bson;

public class MongoDatabaseImpl extends MongoDatabaseBase {
  protected MongoClientContext clientContext;

  protected MongoDatabase wrapped;

  public MongoDatabaseImpl(MongoClientContext clientContext, MongoDatabase wrapped) {
    this.clientContext = clientContext;
    this.wrapped = wrapped;
  }

  @Override
  public String getName() {
    return wrapped.getName();
  }

  @Override
  public ReadPreference getReadPreference() {
    return ReadPreference.fromDriverClass(wrapped.getReadPreference());
  }

  @Override
  public WriteConcern getWriteConcern() {
    return WriteConcern.fromDriverClass(wrapped.getWriteConcern());
  }

  @Override
  public ReadConcern getReadConcern() {
    return ReadConcern.fromDriverClass(wrapped.getReadConcern());
  }

  @Override
  public io.vertx.mongo.client.MongoDatabase withReadPreference(ReadPreference readPreference) {
    requireNonNull(readPreference, "readPreference cannot be null");
    com.mongodb.ReadPreference __readPreference = readPreference.toDriverClass();
    MongoDatabase __wrapped = wrapped.withReadPreference(__readPreference);
    return new MongoDatabaseImpl(this.clientContext, __wrapped);
  }

  @Override
  public io.vertx.mongo.client.MongoDatabase withWriteConcern(WriteConcern writeConcern) {
    requireNonNull(writeConcern, "writeConcern cannot be null");
    com.mongodb.WriteConcern __writeConcern = writeConcern.toDriverClass();
    MongoDatabase __wrapped = wrapped.withWriteConcern(__writeConcern);
    return new MongoDatabaseImpl(this.clientContext, __wrapped);
  }

  @Override
  public io.vertx.mongo.client.MongoDatabase withReadConcern(ReadConcern readConcern) {
    requireNonNull(readConcern, "readConcern cannot be null");
    com.mongodb.ReadConcern __readConcern = readConcern.toDriverClass();
    MongoDatabase __wrapped = wrapped.withReadConcern(__readConcern);
    return new MongoDatabaseImpl(this.clientContext, __wrapped);
  }

  @Override
  public MongoCollection<Document> getCollection(String collectionName) {
    requireNonNull(collectionName, "collectionName cannot be null");
    com.mongodb.reactivestreams.client.MongoCollection<Document> __wrapped = wrapped.getCollection(collectionName);
    return new MongoCollectionImpl<Document>(this.clientContext, __wrapped);
  }

  @Override
  public <TDocument> MongoCollection<TDocument> getCollection(String collectionName,
      Class<TDocument> clazz) {
    requireNonNull(collectionName, "collectionName cannot be null");
    requireNonNull(clazz, "clazz cannot be null");
    com.mongodb.reactivestreams.client.MongoCollection<TDocument> __wrapped = wrapped.getCollection(collectionName, clazz);
    return new MongoCollectionImpl<TDocument>(this.clientContext, __wrapped);
  }

  @Override
  public MongoResult<JsonObject> runCommand(JsonObject command) {
    requireNonNull(command, "command cannot be null");
    Bson __command = ConversionUtilsImpl.INSTANCE.toBson(command);
    //  TODO add implementation
    return null;
  }

  @Override
  public MongoResult<JsonObject> runCommand(JsonObject command, ReadPreference readPreference) {
    requireNonNull(command, "command cannot be null");
    requireNonNull(readPreference, "readPreference cannot be null");
    Bson __command = ConversionUtilsImpl.INSTANCE.toBson(command);
    com.mongodb.ReadPreference __readPreference = readPreference.toDriverClass();
    //  TODO add implementation
    return null;
  }

  @Override
  public MongoResult<JsonObject> runCommand(ClientSession clientSession, JsonObject command) {
    requireNonNull(clientSession, "clientSession cannot be null");
    requireNonNull(command, "command cannot be null");
    com.mongodb.reactivestreams.client.ClientSession __clientSession = clientSession.toDriverClass();
    Bson __command = ConversionUtilsImpl.INSTANCE.toBson(command);
    //  TODO add implementation
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
    //  TODO add implementation
    return null;
  }

  @Override
  public MongoResult<Void> drop() {
    //  TODO add implementation
    return null;
  }

  @Override
  public MongoResult<Void> drop(ClientSession clientSession) {
    requireNonNull(clientSession, "clientSession cannot be null");
    com.mongodb.reactivestreams.client.ClientSession __clientSession = clientSession.toDriverClass();
    //  TODO add implementation
    return null;
  }

  @Override
  public MongoResult<String> listCollectionNames() {
    //  TODO add implementation
    return null;
  }

  @Override
  public MongoResult<String> listCollectionNames(ClientSession clientSession) {
    requireNonNull(clientSession, "clientSession cannot be null");
    com.mongodb.reactivestreams.client.ClientSession __clientSession = clientSession.toDriverClass();
    //  TODO add implementation
    return null;
  }

  @Override
  public MongoResult<JsonObject> listCollections() {
    //  TODO add implementation
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
    //  TODO add implementation
    return null;
  }

  @Override
  public MongoResult<JsonObject> listCollections(ClientSession clientSession,
      ListCollectionsOptions options) {
    return null;
  }

  @Override
  public MongoResult<Void> createCollection(String collectionName) {
    requireNonNull(collectionName, "collectionName cannot be null");
    //  TODO add implementation
    return null;
  }

  @Override
  public MongoResult<Void> createCollection(String collectionName,
      CreateCollectionOptions options) {
    requireNonNull(collectionName, "collectionName cannot be null");
    requireNonNull(options, "options cannot be null");
    com.mongodb.client.model.CreateCollectionOptions __options = options.toDriverClass();
    //  TODO add implementation
    return null;
  }

  @Override
  public MongoResult<Void> createCollection(ClientSession clientSession, String collectionName) {
    requireNonNull(clientSession, "clientSession cannot be null");
    requireNonNull(collectionName, "collectionName cannot be null");
    com.mongodb.reactivestreams.client.ClientSession __clientSession = clientSession.toDriverClass();
    //  TODO add implementation
    return null;
  }

  @Override
  public MongoResult<Void> createCollection(ClientSession clientSession, String collectionName,
      CreateCollectionOptions options) {
    requireNonNull(clientSession, "clientSession cannot be null");
    requireNonNull(collectionName, "collectionName cannot be null");
    requireNonNull(options, "options cannot be null");
    com.mongodb.reactivestreams.client.ClientSession __clientSession = clientSession.toDriverClass();
    com.mongodb.client.model.CreateCollectionOptions __options = options.toDriverClass();
    //  TODO add implementation
    return null;
  }

  @Override
  public MongoResult<Void> createView(String viewName, String viewOn, List<JsonObject> pipeline) {
    requireNonNull(viewName, "viewName cannot be null");
    requireNonNull(viewOn, "viewOn cannot be null");
    requireNonNull(pipeline, "pipeline cannot be null");
    List<? extends Bson> __pipeline = ConversionUtilsImpl.INSTANCE.toBsonList(pipeline);
    //  TODO add implementation
    return null;
  }

  @Override
  public MongoResult<Void> createView(String viewName, String viewOn, List<JsonObject> pipeline,
      CreateViewOptions createViewOptions) {
    requireNonNull(viewName, "viewName cannot be null");
    requireNonNull(viewOn, "viewOn cannot be null");
    requireNonNull(pipeline, "pipeline cannot be null");
    requireNonNull(createViewOptions, "createViewOptions cannot be null");
    List<? extends Bson> __pipeline = ConversionUtilsImpl.INSTANCE.toBsonList(pipeline);
    com.mongodb.client.model.CreateViewOptions __createViewOptions = createViewOptions.toDriverClass();
    //  TODO add implementation
    return null;
  }

  @Override
  public MongoResult<Void> createView(ClientSession clientSession, String viewName, String viewOn,
      List<JsonObject> pipeline) {
    requireNonNull(clientSession, "clientSession cannot be null");
    requireNonNull(viewName, "viewName cannot be null");
    requireNonNull(viewOn, "viewOn cannot be null");
    requireNonNull(pipeline, "pipeline cannot be null");
    com.mongodb.reactivestreams.client.ClientSession __clientSession = clientSession.toDriverClass();
    List<? extends Bson> __pipeline = ConversionUtilsImpl.INSTANCE.toBsonList(pipeline);
    //  TODO add implementation
    return null;
  }

  @Override
  public MongoResult<Void> createView(ClientSession clientSession, String viewName, String viewOn,
      List<JsonObject> pipeline, CreateViewOptions createViewOptions) {
    requireNonNull(clientSession, "clientSession cannot be null");
    requireNonNull(viewName, "viewName cannot be null");
    requireNonNull(viewOn, "viewOn cannot be null");
    requireNonNull(pipeline, "pipeline cannot be null");
    requireNonNull(createViewOptions, "createViewOptions cannot be null");
    com.mongodb.reactivestreams.client.ClientSession __clientSession = clientSession.toDriverClass();
    List<? extends Bson> __pipeline = ConversionUtilsImpl.INSTANCE.toBsonList(pipeline);
    com.mongodb.client.model.CreateViewOptions __createViewOptions = createViewOptions.toDriverClass();
    //  TODO add implementation
    return null;
  }

  @Override
  public ReadStream<JsonObject> watch() {
    //  TODO add implementation
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
    //  TODO add implementation
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
    //  TODO add implementation
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
    //  TODO add implementation
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
    //  TODO add implementation
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
    //  TODO add implementation
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
