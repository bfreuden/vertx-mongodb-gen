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

import com.mongodb.reactivestreams.client.MongoClient;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
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
import io.vertx.mongo.impl.SingleResultSubscriber;
import java.io.Closeable;
import java.lang.Override;
import java.lang.String;
import java.util.List;
import org.bson.conversions.Bson;
import org.reactivestreams.Publisher;

public class MongoClientImpl extends MongoClientBase implements Closeable {
  protected MongoClient wrapped;

  protected Vertx vertx;

  @Override
  public MongoDatabase getDatabase(String name) {
    requireNonNull(name, "name cannot be null");
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
    requireNonNull(clientSession, "clientSession cannot be null");
    com.mongodb.reactivestreams.client.ClientSession __clientSession = clientSession.toDriverClass();
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
    requireNonNull(clientSession, "clientSession cannot be null");
    com.mongodb.reactivestreams.client.ClientSession __clientSession = clientSession.toDriverClass();
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
  public Future<ClientSession> startSession() {
    Publisher<com.mongodb.reactivestreams.client.ClientSession> __publisher = wrapped.startSession();
    Promise<ClientSession> promise = Promise.promise();
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
    Publisher<com.mongodb.reactivestreams.client.ClientSession> __publisher = wrapped.startSession(__options);
    Promise<ClientSession> promise = Promise.promise();
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
    return null;
  }

  public MongoClient toDriverClass() {
    return wrapped;
  }
}
