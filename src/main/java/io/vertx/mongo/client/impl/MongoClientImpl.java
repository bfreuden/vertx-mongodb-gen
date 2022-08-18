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

import com.mongodb.reactivestreams.client.ChangeStreamPublisher;
import com.mongodb.reactivestreams.client.ListDatabasesPublisher;
import com.mongodb.reactivestreams.client.MongoClient;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;
import io.vertx.core.streams.ReadStream;
import io.vertx.mongo.ClientSessionOptions;
import io.vertx.mongo.MongoResult;
import io.vertx.mongo.client.ChangeStreamOptions;
import io.vertx.mongo.client.ClientSession;
import io.vertx.mongo.client.ListDatabasesOptions;
import io.vertx.mongo.client.MongoDatabase;
import io.vertx.mongo.connection.ClusterDescription;
import io.vertx.mongo.impl.ConversionUtilsImpl;
import io.vertx.mongo.impl.MongoClientContext;
import io.vertx.mongo.impl.MongoResultImpl;
import io.vertx.mongo.impl.SingleResultSubscriber;
import java.io.Closeable;
import java.lang.Override;
import java.lang.String;
import java.util.List;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.reactivestreams.Publisher;

public class MongoClientImpl extends MongoClientBase implements Closeable {
  protected final MongoClientContext clientContext;

  protected final MongoClient wrapped;

  public MongoClientImpl(MongoClientContext clientContext, MongoClient wrapped) {
    this.clientContext = clientContext;
    this.wrapped = wrapped;
  }

  @Override
  public MongoDatabase getDatabase(String name) {
    requireNonNull(name, "name is null");
    com.mongodb.reactivestreams.client.MongoDatabase __wrapped = wrapped.getDatabase(name);
    return new MongoDatabaseImpl(this.clientContext, __wrapped);
  }

  @Override
  public void close() {
    wrapped.close();
  }

  @Override
  public MongoResult<String> listDatabaseNames() {
    Publisher<String> __publisher = wrapped.listDatabaseNames();
    return new MongoResultImpl<>(clientContext, __publisher);
  }

  @Override
  public MongoResult<String> listDatabaseNames(ClientSession clientSession) {
    requireNonNull(clientSession, "clientSession is null");
    com.mongodb.reactivestreams.client.ClientSession __clientSession = clientSession.toDriverClass();
    Publisher<String> __publisher = wrapped.listDatabaseNames(__clientSession);
    return new MongoResultImpl<>(clientContext, __publisher);
  }

  @Override
  public MongoResult<JsonObject> listDatabases() {
    //  TODO use mongo mapper result!
    ListDatabasesPublisher<Document> __publisher = wrapped.listDatabases();
    return null;
  }

  @Override
  public MongoResult<JsonObject> listDatabases(ListDatabasesOptions options) {
    //  TODO use mongo mapper result!
    ListDatabasesPublisher<Document> __publisher = wrapped.listDatabases();
    options.initializePublisher(__publisher);
    return null;
  }

  @Override
  public MongoResult<JsonObject> listDatabases(ClientSession clientSession) {
    requireNonNull(clientSession, "clientSession is null");
    com.mongodb.reactivestreams.client.ClientSession __clientSession = clientSession.toDriverClass();
    //  TODO use mongo mapper result!
    ListDatabasesPublisher<Document> __publisher = wrapped.listDatabases(__clientSession);
    return null;
  }

  @Override
  public MongoResult<JsonObject> listDatabases(ClientSession clientSession,
      ListDatabasesOptions options) {
    requireNonNull(clientSession, "clientSession is null");
    com.mongodb.reactivestreams.client.ClientSession __clientSession = clientSession.toDriverClass();
    //  TODO use mongo mapper result!
    ListDatabasesPublisher<Document> __publisher = wrapped.listDatabases(__clientSession);
    options.initializePublisher(__publisher);
    return null;
  }

  @Override
  public ReadStream<JsonObject> watch() {
    //  TODO use mongo mapper result!
    ChangeStreamPublisher<Document> __publisher = wrapped.watch();
    return null;
  }

  @Override
  public ReadStream<JsonObject> watch(ChangeStreamOptions options) {
    //  TODO use mongo mapper result!
    ChangeStreamPublisher<Document> __publisher = wrapped.watch();
    options.initializePublisher(__publisher);
    return null;
  }

  @Override
  public ReadStream<JsonObject> watch(List<JsonObject> pipeline) {
    requireNonNull(pipeline, "pipeline is null");
    List<? extends Bson> __pipeline = ConversionUtilsImpl.INSTANCE.toBsonList(pipeline);
    //  TODO use mongo mapper result!
    ChangeStreamPublisher<Document> __publisher = wrapped.watch(__pipeline);
    return null;
  }

  @Override
  public ReadStream<JsonObject> watch(List<JsonObject> pipeline, ChangeStreamOptions options) {
    requireNonNull(pipeline, "pipeline is null");
    List<? extends Bson> __pipeline = ConversionUtilsImpl.INSTANCE.toBsonList(pipeline);
    //  TODO use mongo mapper result!
    ChangeStreamPublisher<Document> __publisher = wrapped.watch(__pipeline);
    options.initializePublisher(__publisher);
    return null;
  }

  @Override
  public ReadStream<JsonObject> watch(ClientSession clientSession) {
    requireNonNull(clientSession, "clientSession is null");
    com.mongodb.reactivestreams.client.ClientSession __clientSession = clientSession.toDriverClass();
    //  TODO use mongo mapper result!
    ChangeStreamPublisher<Document> __publisher = wrapped.watch(__clientSession);
    return null;
  }

  @Override
  public ReadStream<JsonObject> watch(ClientSession clientSession, ChangeStreamOptions options) {
    requireNonNull(clientSession, "clientSession is null");
    com.mongodb.reactivestreams.client.ClientSession __clientSession = clientSession.toDriverClass();
    //  TODO use mongo mapper result!
    ChangeStreamPublisher<Document> __publisher = wrapped.watch(__clientSession);
    options.initializePublisher(__publisher);
    return null;
  }

  @Override
  public ReadStream<JsonObject> watch(ClientSession clientSession, List<JsonObject> pipeline) {
    requireNonNull(clientSession, "clientSession is null");
    requireNonNull(pipeline, "pipeline is null");
    com.mongodb.reactivestreams.client.ClientSession __clientSession = clientSession.toDriverClass();
    List<? extends Bson> __pipeline = ConversionUtilsImpl.INSTANCE.toBsonList(pipeline);
    //  TODO use mongo mapper result!
    ChangeStreamPublisher<Document> __publisher = wrapped.watch(__clientSession, __pipeline);
    return null;
  }

  @Override
  public ReadStream<JsonObject> watch(ClientSession clientSession, List<JsonObject> pipeline,
      ChangeStreamOptions options) {
    requireNonNull(clientSession, "clientSession is null");
    requireNonNull(pipeline, "pipeline is null");
    com.mongodb.reactivestreams.client.ClientSession __clientSession = clientSession.toDriverClass();
    List<? extends Bson> __pipeline = ConversionUtilsImpl.INSTANCE.toBsonList(pipeline);
    //  TODO use mongo mapper result!
    ChangeStreamPublisher<Document> __publisher = wrapped.watch(__clientSession, __pipeline);
    options.initializePublisher(__publisher);
    return null;
  }

  @Override
  public Future<ClientSession> startSession() {
    Publisher<com.mongodb.reactivestreams.client.ClientSession> __publisher = wrapped.startSession();
    Promise<com.mongodb.reactivestreams.client.ClientSession> __promise = Promise.promise();
    __publisher.subscribe(new SingleResultSubscriber<>(__promise));
    return __promise.future().map(ConversionUtilsImpl.INSTANCE::toClientSession);
  }

  @Override
  public io.vertx.mongo.client.MongoClient startSession(
      Handler<AsyncResult<ClientSession>> resultHandler) {
    Future<ClientSession> __future = this.startSession();
    setHandler(__future, resultHandler);
    return this;
  }

  @Override
  public Future<ClientSession> startSession(ClientSessionOptions options) {
    requireNonNull(options, "options is null");
    com.mongodb.ClientSessionOptions __options = options.toDriverClass();
    Publisher<com.mongodb.reactivestreams.client.ClientSession> __publisher = wrapped.startSession(__options);
    Promise<com.mongodb.reactivestreams.client.ClientSession> __promise = Promise.promise();
    __publisher.subscribe(new SingleResultSubscriber<>(__promise));
    return __promise.future().map(ConversionUtilsImpl.INSTANCE::toClientSession);
  }

  @Override
  public io.vertx.mongo.client.MongoClient startSession(ClientSessionOptions options,
      Handler<AsyncResult<ClientSession>> resultHandler) {
    Future<ClientSession> __future = this.startSession(options);
    setHandler(__future, resultHandler);
    return this;
  }

  @Override
  public ClusterDescription getClusterDescription() {
    return ClusterDescription.fromDriverClass(wrapped.getClusterDescription());
  }

  public MongoClient toDriverClass() {
    return wrapped;
  }
}
