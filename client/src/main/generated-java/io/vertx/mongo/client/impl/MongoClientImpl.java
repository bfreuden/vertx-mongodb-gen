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

import com.mongodb.connection.ClusterDescription;
import com.mongodb.reactivestreams.client.ChangeStreamPublisher;
import com.mongodb.reactivestreams.client.ListDatabasesPublisher;
import com.mongodb.reactivestreams.client.MongoClient;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.mongo.ClientSessionOptions;
import io.vertx.mongo.MongoResult;
import io.vertx.mongo.client.ChangeStreamOptions;
import io.vertx.mongo.client.ChangeStreamResult;
import io.vertx.mongo.client.ClientConfig;
import io.vertx.mongo.client.ClientSession;
import io.vertx.mongo.client.ListDatabasesOptions;
import io.vertx.mongo.client.ListDatabasesResult;
import io.vertx.mongo.client.MongoDatabase;
import io.vertx.mongo.client.model.changestream.ChangeStreamDocument;
import io.vertx.mongo.impl.MappingPublisher;
import io.vertx.mongo.impl.MongoClientContext;
import io.vertx.mongo.impl.MongoResultImpl;
import io.vertx.mongo.impl.SingleResultSubscriber;
import java.lang.Override;
import java.lang.String;
import java.util.List;
import org.bson.conversions.Bson;
import org.reactivestreams.Publisher;

public class MongoClientImpl extends MongoClientBase {
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
    com.mongodb.reactivestreams.client.MongoDatabase __result = wrapped.getDatabase(name);
    return new MongoDatabaseImpl(clientContext, __result);
  }

  @Override
  public MongoResult<String> listDatabaseNames() {
    Publisher<String> __publisher = wrapped.listDatabaseNames();
    return new MongoResultImpl<>(clientContext, __publisher);
  }

  @Override
  public MongoResult<String> listDatabaseNames(ClientSession clientSession) {
    requireNonNull(clientSession, "clientSession is null");
    com.mongodb.reactivestreams.client.ClientSession __clientSession = clientSession.toDriverClass(clientContext);
    Publisher<String> __publisher = wrapped.listDatabaseNames(__clientSession);
    return new MongoResultImpl<>(clientContext, __publisher);
  }

  @Override
  public ListDatabasesResult<JsonObject> listDatabases() {
    ListDatabasesPublisher<JsonObject> __publisher = wrapped.listDatabases(JsonObject.class);
    return new ListDatabasesResultImpl<>(clientContext, __publisher, clientContext.getConfig().getOutputMapper());
  }

  @Override
  public ListDatabasesResult<JsonObject> listDatabases(ListDatabasesOptions options) {
    ListDatabasesPublisher<JsonObject> __publisher = wrapped.listDatabases(JsonObject.class);
    options.initializePublisher(clientContext, __publisher);
    Integer __batchSize = options.getBatchSize();
    if (__batchSize != null) {
      return new ListDatabasesResultImpl<>(clientContext, __publisher, clientContext.getConfig().getOutputMapper(), __batchSize);
    } else {
      return new ListDatabasesResultImpl<>(clientContext, __publisher, clientContext.getConfig().getOutputMapper());
    }
  }

  @Override
  public ListDatabasesResult<JsonObject> listDatabases(ClientSession clientSession) {
    requireNonNull(clientSession, "clientSession is null");
    com.mongodb.reactivestreams.client.ClientSession __clientSession = clientSession.toDriverClass(clientContext);
    ListDatabasesPublisher<JsonObject> __publisher = wrapped.listDatabases(__clientSession, JsonObject.class);
    return new ListDatabasesResultImpl<>(clientContext, __publisher, clientContext.getConfig().getOutputMapper());
  }

  @Override
  public ListDatabasesResult<JsonObject> listDatabases(ClientSession clientSession,
      ListDatabasesOptions options) {
    requireNonNull(clientSession, "clientSession is null");
    com.mongodb.reactivestreams.client.ClientSession __clientSession = clientSession.toDriverClass(clientContext);
    ListDatabasesPublisher<JsonObject> __publisher = wrapped.listDatabases(__clientSession, JsonObject.class);
    options.initializePublisher(clientContext, __publisher);
    Integer __batchSize = options.getBatchSize();
    if (__batchSize != null) {
      return new ListDatabasesResultImpl<>(clientContext, __publisher, clientContext.getConfig().getOutputMapper(), __batchSize);
    } else {
      return new ListDatabasesResultImpl<>(clientContext, __publisher, clientContext.getConfig().getOutputMapper());
    }
  }

  @Override
  public ChangeStreamResult<JsonObject> watch() {
    ChangeStreamPublisher<JsonObject> __publisher = wrapped.watch(JsonObject.class);
    return new ChangeStreamResultImpl<>(clientContext, __publisher, _item -> ChangeStreamDocument.fromDriverClass(clientContext, _item));
  }

  @Override
  public ChangeStreamResult<JsonObject> watch(ChangeStreamOptions options) {
    ChangeStreamPublisher<JsonObject> __publisher = wrapped.watch(JsonObject.class);
    options.initializePublisher(clientContext, __publisher);
    Integer __batchSize = options.getBatchSize();
    if (__batchSize != null) {
      return new ChangeStreamResultImpl<>(clientContext, __publisher, _item -> ChangeStreamDocument.fromDriverClass(clientContext, _item), __batchSize);
    } else {
      return new ChangeStreamResultImpl<>(clientContext, __publisher, _item -> ChangeStreamDocument.fromDriverClass(clientContext, _item));
    }
  }

  @Override
  public ChangeStreamResult<JsonObject> watch(JsonArray pipeline) {
    requireNonNull(pipeline, "pipeline is null");
    List<? extends Bson> __pipeline = clientContext.getMapper().toBsonList(pipeline);
    ChangeStreamPublisher<JsonObject> __publisher = wrapped.watch(__pipeline, JsonObject.class);
    return new ChangeStreamResultImpl<>(clientContext, __publisher, _item -> ChangeStreamDocument.fromDriverClass(clientContext, _item));
  }

  @Override
  public ChangeStreamResult<JsonObject> watch(JsonArray pipeline,
      ChangeStreamOptions options) {
    requireNonNull(pipeline, "pipeline is null");
    List<? extends Bson> __pipeline = clientContext.getMapper().toBsonList(pipeline);
    ChangeStreamPublisher<JsonObject> __publisher = wrapped.watch(__pipeline, JsonObject.class);
    options.initializePublisher(clientContext, __publisher);
    Integer __batchSize = options.getBatchSize();
    if (__batchSize != null) {
      return new ChangeStreamResultImpl<>(clientContext, __publisher, _item -> ChangeStreamDocument.fromDriverClass(clientContext, _item), __batchSize);
    } else {
      return new ChangeStreamResultImpl<>(clientContext, __publisher, _item -> ChangeStreamDocument.fromDriverClass(clientContext, _item));
    }
  }

  @Override
  public ChangeStreamResult<JsonObject> watch(ClientSession clientSession) {
    requireNonNull(clientSession, "clientSession is null");
    com.mongodb.reactivestreams.client.ClientSession __clientSession = clientSession.toDriverClass(clientContext);
    ChangeStreamPublisher<JsonObject> __publisher = wrapped.watch(__clientSession, JsonObject.class);
    MappingPublisher<com.mongodb.client.model.changestream.ChangeStreamDocument<JsonObject>, ChangeStreamDocument<JsonObject>> __mappingPublisher = new MappingPublisher<>(__publisher, _item -> ChangeStreamDocument.fromDriverClass(clientContext, _item));
    return new ChangeStreamResultImpl<>(clientContext, __publisher, _item -> ChangeStreamDocument.fromDriverClass(clientContext, _item));
  }

  @Override
  public ChangeStreamResult<JsonObject> watch(ClientSession clientSession,
      ChangeStreamOptions options) {
    requireNonNull(clientSession, "clientSession is null");
    com.mongodb.reactivestreams.client.ClientSession __clientSession = clientSession.toDriverClass(clientContext);
    ChangeStreamPublisher<JsonObject> __publisher = wrapped.watch(__clientSession, JsonObject.class);
    options.initializePublisher(clientContext, __publisher);
    Integer __batchSize = options.getBatchSize();
    if (__batchSize != null) {
      return new ChangeStreamResultImpl<>(clientContext, __publisher, _item -> ChangeStreamDocument.fromDriverClass(clientContext, _item), __batchSize);
    } else {
      return new ChangeStreamResultImpl<>(clientContext, __publisher, _item -> ChangeStreamDocument.fromDriverClass(clientContext, _item));
    }
  }

  @Override
  public ChangeStreamResult<JsonObject> watch(ClientSession clientSession,
      JsonArray pipeline) {
    requireNonNull(clientSession, "clientSession is null");
    requireNonNull(pipeline, "pipeline is null");
    com.mongodb.reactivestreams.client.ClientSession __clientSession = clientSession.toDriverClass(clientContext);
    List<? extends Bson> __pipeline = clientContext.getMapper().toBsonList(pipeline);
    ChangeStreamPublisher<JsonObject> __publisher = wrapped.watch(__clientSession, __pipeline, JsonObject.class);
    return new ChangeStreamResultImpl<>(clientContext, __publisher, _item -> ChangeStreamDocument.fromDriverClass(clientContext, _item));
  }

  @Override
  public ChangeStreamResult<JsonObject> watch(ClientSession clientSession,
      JsonArray pipeline, ChangeStreamOptions options) {
    requireNonNull(clientSession, "clientSession is null");
    requireNonNull(pipeline, "pipeline is null");
    com.mongodb.reactivestreams.client.ClientSession __clientSession = clientSession.toDriverClass(clientContext);
    List<? extends Bson> __pipeline = clientContext.getMapper().toBsonList(pipeline);
    ChangeStreamPublisher<JsonObject> __publisher = wrapped.watch(__clientSession, __pipeline, JsonObject.class);
    options.initializePublisher(clientContext, __publisher);
    Integer __batchSize = options.getBatchSize();
    if (__batchSize != null) {
      return new ChangeStreamResultImpl<>(clientContext, __publisher, _item -> ChangeStreamDocument.fromDriverClass(clientContext, _item), __batchSize);
    } else {
      return new ChangeStreamResultImpl<>(clientContext, __publisher, _item -> ChangeStreamDocument.fromDriverClass(clientContext, _item));
    }
  }

  @Override
  public Future<ClientSession> startSession() {
    Publisher<com.mongodb.reactivestreams.client.ClientSession> __publisher = wrapped.startSession();
    Promise<com.mongodb.reactivestreams.client.ClientSession> __promise = clientContext.getVertx().promise();
    __publisher.subscribe(new SingleResultSubscriber<>(clientContext, __promise));
    return __promise.future().map(_reactive -> new ClientSessionImpl(clientContext, _reactive));
  }

  @Override
  public void startSession(Handler<AsyncResult<ClientSession>> resultHandler) {
    Future<ClientSession> __future = this.startSession();
    setHandler(__future, resultHandler);
  }

  @Override
  public Future<ClientSession> startSession(ClientSessionOptions options) {
    requireNonNull(options, "options is null");
    com.mongodb.ClientSessionOptions __options = options.toDriverClass(clientContext);
    Publisher<com.mongodb.reactivestreams.client.ClientSession> __publisher = wrapped.startSession(__options);
    Promise<com.mongodb.reactivestreams.client.ClientSession> __promise = clientContext.getVertx().promise();
    __publisher.subscribe(new SingleResultSubscriber<>(clientContext, __promise));
    return __promise.future().map(_reactive -> new ClientSessionImpl(clientContext, _reactive));
  }

  @Override
  public void startSession(ClientSessionOptions options,
      Handler<AsyncResult<ClientSession>> resultHandler) {
    Future<ClientSession> __future = this.startSession(options);
    setHandler(__future, resultHandler);
  }

  @Override
  public ClusterDescription getClusterDescription() {
    return wrapped.getClusterDescription();
  }

  public MongoClient toDriverClass(MongoClientContext clientContext) {
    return wrapped;
  }
}
