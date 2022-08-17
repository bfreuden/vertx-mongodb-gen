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
import io.vertx.core.Vertx;
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

  protected Vertx vertx;

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
  public io.vertx.mongo.client.MongoDatabase withReadPreference(ReadPreference readPreference) {
    requireNonNull(readPreference, "readPreference cannot be null");
    com.mongodb.ReadPreference __readPreference = readPreference.toDriverClass();
    return null;
  }

  @Override
  public io.vertx.mongo.client.MongoDatabase withWriteConcern(WriteConcern writeConcern) {
    requireNonNull(writeConcern, "writeConcern cannot be null");
    com.mongodb.WriteConcern __writeConcern = writeConcern.toDriverClass();
    return null;
  }

  @Override
  public io.vertx.mongo.client.MongoDatabase withReadConcern(ReadConcern readConcern) {
    requireNonNull(readConcern, "readConcern cannot be null");
    com.mongodb.ReadConcern __readConcern = readConcern.toDriverClass();
    return null;
  }

  @Override
  public MongoCollection<JsonObject> getCollection(String collectionName) {
    requireNonNull(collectionName, "collectionName cannot be null");
    return null;
  }

  @Override
  public <TDocument> MongoCollection<TDocument> getCollection(String collectionName,
      Class<TDocument> clazz) {
    requireNonNull(collectionName, "collectionName cannot be null");
    requireNonNull(clazz, "clazz cannot be null");
    return null;
  }

  @Override
  public MongoResult<JsonObject> runCommand(JsonObject command) {
    requireNonNull(command, "command cannot be null");
    Bson __command = ConversionUtilsImpl.INSTANCE.toBson(command);
    return null;
  }

  @Override
  public MongoResult<JsonObject> runCommand(JsonObject command, ReadPreference readPreference) {
    requireNonNull(command, "command cannot be null");
    requireNonNull(readPreference, "readPreference cannot be null");
    Bson __command = ConversionUtilsImpl.INSTANCE.toBson(command);
    com.mongodb.ReadPreference __readPreference = readPreference.toDriverClass();
    return null;
  }

  @Override
  public MongoResult<JsonObject> runCommand(ClientSession clientSession, JsonObject command) {
    requireNonNull(clientSession, "clientSession cannot be null");
    requireNonNull(command, "command cannot be null");
    com.mongodb.reactivestreams.client.ClientSession __clientSession = clientSession.toDriverClass();
    Bson __command = ConversionUtilsImpl.INSTANCE.toBson(command);
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
    return null;
  }

  @Override
  public MongoResult<Void> drop() {
    return null;
  }

  @Override
  public MongoResult<Void> drop(ClientSession clientSession) {
    requireNonNull(clientSession, "clientSession cannot be null");
    com.mongodb.reactivestreams.client.ClientSession __clientSession = clientSession.toDriverClass();
    return null;
  }

  @Override
  public MongoResult<String> listCollectionNames() {
    return null;
  }

  @Override
  public MongoResult<String> listCollectionNames(ClientSession clientSession) {
    requireNonNull(clientSession, "clientSession cannot be null");
    com.mongodb.reactivestreams.client.ClientSession __clientSession = clientSession.toDriverClass();
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
    requireNonNull(clientSession, "clientSession cannot be null");
    com.mongodb.reactivestreams.client.ClientSession __clientSession = clientSession.toDriverClass();
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
    return null;
  }

  @Override
  public MongoResult<Void> createCollection(String collectionName,
      CreateCollectionOptions options) {
    requireNonNull(collectionName, "collectionName cannot be null");
    requireNonNull(options, "options cannot be null");
    com.mongodb.client.model.CreateCollectionOptions __options = options.toDriverClass();
    return null;
  }

  @Override
  public MongoResult<Void> createCollection(ClientSession clientSession, String collectionName) {
    requireNonNull(clientSession, "clientSession cannot be null");
    requireNonNull(collectionName, "collectionName cannot be null");
    com.mongodb.reactivestreams.client.ClientSession __clientSession = clientSession.toDriverClass();
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
    return null;
  }

  @Override
  public MongoResult<Void> createView(String viewName, String viewOn, List<JsonObject> pipeline) {
    requireNonNull(viewName, "viewName cannot be null");
    requireNonNull(viewOn, "viewOn cannot be null");
    requireNonNull(pipeline, "pipeline cannot be null");
    List<? extends Bson> __pipeline = ConversionUtilsImpl.INSTANCE.toBsonList(pipeline);
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
    requireNonNull(pipeline, "pipeline cannot be null");
    List<? extends Bson> __pipeline = ConversionUtilsImpl.INSTANCE.toBsonList(pipeline);
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
