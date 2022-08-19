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

import com.mongodb.reactivestreams.client.ListDatabasesPublisher;
import io.vertx.core.AsyncResult;
import io.vertx.core.Closeable;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.mongo.ClientSessionOptions;
import io.vertx.mongo.MongoResult;
import io.vertx.mongo.client.ChangeStreamOptions;
import io.vertx.mongo.client.ClientConfig;
import io.vertx.mongo.client.ClientSession;
import io.vertx.mongo.client.ListDatabasesOptions;
import io.vertx.mongo.client.MongoClient;
import io.vertx.mongo.client.MongoDatabase;
import io.vertx.mongo.connection.ClusterDescription;
import io.vertx.mongo.impl.ConversionUtilsImpl;
import io.vertx.mongo.impl.MongoResultImpl;
import io.vertx.mongo.impl.SingleResultSubscriber;
import java.lang.Override;
import java.lang.String;
import java.util.List;
import org.bson.conversions.Bson;
import org.reactivestreams.Publisher;

public class MongoClientImpl extends MongoClientBase implements Closeable {
  /**
   * Create a Mongo client which shares its data source with any other Mongo clients created with the same
   * data source name
   *
   * @param vertx          the Vert.x instance
   * @param config         the configuration
   * @param dataSourceName the data source name
   * @return the client
   */
  public MongoClientImpl(Vertx vertx, ClientConfig config, String dataSourceName) {
    super(vertx, config, dataSourceName);
  }

  @Override
  public MongoDatabase getDatabase(String name) {
    requireNonNull(name, "name is null");
    com.mongodb.reactivestreams.client.MongoDatabase __wrapped = wrapped.getDatabase(name);
    return new MongoDatabaseImpl(this.clientContext, __wrapped);
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
    ListDatabasesPublisher<JsonObject> __publisher = wrapped.listDatabases(JsonObject.class);
    return new MongoResultImpl<>(clientContext, __publisher, __publisher::first);
  }

  @Override
  public MongoResult<JsonObject> listDatabases(ListDatabasesOptions options) {
    ListDatabasesPublisher<JsonObject> __publisher = wrapped.listDatabases(JsonObject.class);
    options.initializePublisher(__publisher);
    Integer __batchSize = options.getBatchSize();
    if (__batchSize != null) {
      return new MongoResultImpl<>(clientContext, __publisher, __publisher::first, __batchSize);
    } else {
      return new MongoResultImpl<>(clientContext, __publisher, __publisher::first);
    }
  }

  @Override
  public MongoResult<JsonObject> listDatabases(ClientSession clientSession) {
    requireNonNull(clientSession, "clientSession is null");
    com.mongodb.reactivestreams.client.ClientSession __clientSession = clientSession.toDriverClass();
    ListDatabasesPublisher<JsonObject> __publisher = wrapped.listDatabases(__clientSession, JsonObject.class);
    return new MongoResultImpl<>(clientContext, __publisher, __publisher::first);
  }

  @Override
  public MongoResult<JsonObject> listDatabases(ClientSession clientSession,
      ListDatabasesOptions options) {
    requireNonNull(clientSession, "clientSession is null");
    com.mongodb.reactivestreams.client.ClientSession __clientSession = clientSession.toDriverClass();
    ListDatabasesPublisher<JsonObject> __publisher = wrapped.listDatabases(__clientSession, JsonObject.class);
    options.initializePublisher(__publisher);
    Integer __batchSize = options.getBatchSize();
    if (__batchSize != null) {
      return new MongoResultImpl<>(clientContext, __publisher, __publisher::first, __batchSize);
    } else {
      return new MongoResultImpl<>(clientContext, __publisher, __publisher::first);
    }
  }

  @Override
  public MongoResult<JsonObject> watch() {
    //  TODO add implementation
    return null;
  }

  @Override
  public MongoResult<JsonObject> watch(ChangeStreamOptions options) {
    //  TODO add implementation
    return null;
  }

  @Override
  public MongoResult<JsonObject> watch(List<JsonObject> pipeline) {
    requireNonNull(pipeline, "pipeline is null");
    List<? extends Bson> __pipeline = ConversionUtilsImpl.INSTANCE.toBsonList(pipeline);
    //  TODO add implementation
    return null;
  }

  @Override
  public MongoResult<JsonObject> watch(List<JsonObject> pipeline, ChangeStreamOptions options) {
    requireNonNull(pipeline, "pipeline is null");
    List<? extends Bson> __pipeline = ConversionUtilsImpl.INSTANCE.toBsonList(pipeline);
    //  TODO add implementation
    return null;
  }

  @Override
  public MongoResult<JsonObject> watch(ClientSession clientSession) {
    requireNonNull(clientSession, "clientSession is null");
    com.mongodb.reactivestreams.client.ClientSession __clientSession = clientSession.toDriverClass();
    //  TODO add implementation
    return null;
  }

  @Override
  public MongoResult<JsonObject> watch(ClientSession clientSession, ChangeStreamOptions options) {
    requireNonNull(clientSession, "clientSession is null");
    com.mongodb.reactivestreams.client.ClientSession __clientSession = clientSession.toDriverClass();
    //  TODO add implementation
    return null;
  }

  @Override
  public MongoResult<JsonObject> watch(ClientSession clientSession, List<JsonObject> pipeline) {
    requireNonNull(clientSession, "clientSession is null");
    requireNonNull(pipeline, "pipeline is null");
    com.mongodb.reactivestreams.client.ClientSession __clientSession = clientSession.toDriverClass();
    List<? extends Bson> __pipeline = ConversionUtilsImpl.INSTANCE.toBsonList(pipeline);
    //  TODO add implementation
    return null;
  }

  @Override
  public MongoResult<JsonObject> watch(ClientSession clientSession, List<JsonObject> pipeline,
      ChangeStreamOptions options) {
    requireNonNull(clientSession, "clientSession is null");
    requireNonNull(pipeline, "pipeline is null");
    com.mongodb.reactivestreams.client.ClientSession __clientSession = clientSession.toDriverClass();
    List<? extends Bson> __pipeline = ConversionUtilsImpl.INSTANCE.toBsonList(pipeline);
    //  TODO add implementation
    return null;
  }

  @Override
  public Future<ClientSession> startSession() {
    Publisher<com.mongodb.reactivestreams.client.ClientSession> __publisher = wrapped.startSession();
    Promise<com.mongodb.reactivestreams.client.ClientSession> __promise = Promise.promise();
    __publisher.subscribe(new SingleResultSubscriber<>(clientContext, __promise));
    return __promise.future().map(__wrapped -> new ClientSessionImpl(this.clientContext, __wrapped));
  }

  @Override
  public MongoClient startSession(Handler<AsyncResult<ClientSession>> resultHandler) {
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
    __publisher.subscribe(new SingleResultSubscriber<>(clientContext, __promise));
    return __promise.future().map(__wrapped -> new ClientSessionImpl(this.clientContext, __wrapped));
  }

  @Override
  public MongoClient startSession(ClientSessionOptions options,
      Handler<AsyncResult<ClientSession>> resultHandler) {
    Future<ClientSession> __future = this.startSession(options);
    setHandler(__future, resultHandler);
    return this;
  }

  @Override
  public ClusterDescription getClusterDescription() {
    return ClusterDescription.fromDriverClass(wrapped.getClusterDescription());
  }

  public com.mongodb.reactivestreams.client.MongoClient toDriverClass() {
    return wrapped;
  }
}
