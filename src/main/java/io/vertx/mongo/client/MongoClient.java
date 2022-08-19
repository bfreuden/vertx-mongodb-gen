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
package io.vertx.mongo.client;

import io.vertx.core.AsyncResult;
import io.vertx.core.Closeable;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.mongo.ClientSessionOptions;
import io.vertx.mongo.MongoResult;
import io.vertx.mongo.client.impl.MongoClientImpl;
import io.vertx.mongo.connection.ClusterDescription;
import java.lang.String;
import java.lang.Void;
import java.util.List;
import java.util.UUID;

/**
 *  A client-side representation of a MongoDB cluster.  Instances can represent either a standalone MongoDB instance, a replica set,
 *  or a sharded cluster.  Instance of this class are responsible for maintaining an up-to-date state of the cluster,
 *  and possibly cache resources related to this, including background threads for monitoring, and connection pools.
 *  <p>
 *  Instance of this class server as factories for {@code MongoDatabase} instances.
 *  </p>
 *  @since 1.0
 */
public interface MongoClient extends Closeable {
  /**
   * Create a Mongo client which maintains its own data source and connects to a default server.
   *
   * @param vertx  the Vert.x instance
   * @return the client
   */
  static MongoClient create(Vertx vertx) {
    return new MongoClientImpl(vertx, new ClientConfig(), UUID.randomUUID().toString());
  }

  /**
   * Create a Mongo client which maintains its own data source.
   *
   * @param vertx  the Vert.x instance
   * @param config the configuration
   * @return the client
   */
  static MongoClient create(Vertx vertx, ClientConfig config) {
    return new MongoClientImpl(vertx, config, UUID.randomUUID().toString());
  }

  /**
   * Create a Mongo client which shares its data source with any other Mongo clients created with the same
   * data source name
   *
   * @param vertx          the Vert.x instance
   * @param config         the configuration
   * @param dataSourceName the data source name
   * @return the client
   */
  static MongoClient createShared(Vertx vertx, ClientConfig config, String dataSourceName) {
    return new MongoClientImpl(vertx, config, dataSourceName);
  }

  /**
   * Like {@link #close(Handler)} but returns a {@code Future} of the asynchronous result
   */
  Future<Void> close();

  /**
   * Like {@link #close(Handler)} but returns a {@code Future} of the asynchronous result
   */
  void close(Handler<AsyncResult<Void>> handler);

  /**
   *  Gets the database with the given name.
   *
   *  @param name the name of the database
   *  @return the database
   */
  MongoDatabase getDatabase(String name);

  /**
   *  Get a list of the database names
   *  @mongodb.driver.manual reference/commands/listDatabases List Databases
   *  @return an result containing all the names of all the databases
   */
  MongoResult<String> listDatabaseNames();

  /**
   *  Get a list of the database names
   *  @param clientSession the client session with which to associate this operation
   *  @mongodb.driver.manual reference/commands/listDatabases List Databases
   *  @return an result containing all the names of all the databases
   *  @mongodb.server.release 3.6
   *  @since 1.7
   */
  MongoResult<String> listDatabaseNames(ClientSession clientSession);

  /**
   *  Gets the list of databases
   *  @return the fluent list databases interface
   */
  MongoResult<JsonObject> listDatabases();

  /**
   *  Gets the list of databases
   *  @param options options
   *  @return the fluent list databases interface
   */
  MongoResult<JsonObject> listDatabases(ListDatabasesOptions options);

  /**
   *  Gets the list of databases
   *  @param clientSession the client session with which to associate this operation
   *  @return the fluent list databases interface
   *  @mongodb.server.release 3.6
   *  @since 1.7
   */
  MongoResult<JsonObject> listDatabases(ClientSession clientSession);

  /**
   *  Gets the list of databases
   *  @param clientSession the client session with which to associate this operation
   *  @param options options
   *  @return the fluent list databases interface
   *  @mongodb.server.release 3.6
   *  @since 1.7
   */
  MongoResult<JsonObject> listDatabases(ClientSession clientSession, ListDatabasesOptions options);

  /**
   *  Creates a change stream for this client.
   *  @return the change stream read stream
   *  @mongodb.driver.dochub core/changestreams Change Streams
   *  @since 1.9
   *  @mongodb.server.release 4.0
   */
  MongoResult<JsonObject> watch();

  /**
   *  Creates a change stream for this client.
   *  @param options options
   *  @return the change stream read stream
   *  @mongodb.driver.dochub core/changestreams Change Streams
   *  @since 1.9
   *  @mongodb.server.release 4.0
   */
  MongoResult<JsonObject> watch(ChangeStreamOptions options);

  /**
   *  Creates a change stream for this client.
   *  @param pipeline the aggregation pipeline to apply to the change stream.
   *  @return the change stream read stream
   *  @mongodb.driver.dochub core/changestreams Change Streams
   *  @since 1.9
   *  @mongodb.server.release 4.0
   */
  MongoResult<JsonObject> watch(List<JsonObject> pipeline);

  /**
   *  Creates a change stream for this client.
   *  @param pipeline the aggregation pipeline to apply to the change stream.
   *  @param options options
   *  @return the change stream read stream
   *  @mongodb.driver.dochub core/changestreams Change Streams
   *  @since 1.9
   *  @mongodb.server.release 4.0
   */
  MongoResult<JsonObject> watch(List<JsonObject> pipeline, ChangeStreamOptions options);

  /**
   *  Creates a change stream for this client.
   *  @param clientSession the client session with which to associate this operation
   *  @return the change stream read stream
   *  @since 1.9
   *  @mongodb.server.release 4.0
   *  @mongodb.driver.dochub core/changestreams Change Streams
   */
  MongoResult<JsonObject> watch(ClientSession clientSession);

  /**
   *  Creates a change stream for this client.
   *  @param clientSession the client session with which to associate this operation
   *  @param options options
   *  @return the change stream read stream
   *  @since 1.9
   *  @mongodb.server.release 4.0
   *  @mongodb.driver.dochub core/changestreams Change Streams
   */
  MongoResult<JsonObject> watch(ClientSession clientSession, ChangeStreamOptions options);

  /**
   *  Creates a change stream for this client.
   *  @param clientSession the client session with which to associate this operation
   *  @param pipeline the aggregation pipeline to apply to the change stream.
   *  @return the change stream read stream
   *  @since 1.9
   *  @mongodb.server.release 4.0
   *  @mongodb.driver.dochub core/changestreams Change Streams
   */
  MongoResult<JsonObject> watch(ClientSession clientSession, List<JsonObject> pipeline);

  /**
   *  Creates a change stream for this client.
   *  @param clientSession the client session with which to associate this operation
   *  @param pipeline the aggregation pipeline to apply to the change stream.
   *  @param options options
   *  @return the change stream read stream
   *  @since 1.9
   *  @mongodb.server.release 4.0
   *  @mongodb.driver.dochub core/changestreams Change Streams
   */
  MongoResult<JsonObject> watch(ClientSession clientSession, List<JsonObject> pipeline,
      ChangeStreamOptions options);

  /**
   *  Creates a client session.
   *  @return a future for the client session.
   *  @mongodb.server.release 3.6
   *  @since 1.9
   */
  Future<ClientSession> startSession();

  /**
   *  Creates a client session.
   *  @param resultHandler an async result for the client session.
   *  @return <code>this</code>
   *  @mongodb.server.release 3.6
   *  @since 1.9
   */
  MongoClient startSession(Handler<AsyncResult<ClientSession>> resultHandler);

  /**
   *  Creates a client session.
   *  @param options the options for the client session
   *  @return a future for the client session.
   *  @mongodb.server.release 3.6
   *  @since 1.7
   */
  Future<ClientSession> startSession(ClientSessionOptions options);

  /**
   *  Creates a client session.
   *  @param options the options for the client session
   *  @param resultHandler an async result for the client session.
   *  @return <code>this</code>
   *  @mongodb.server.release 3.6
   *  @since 1.7
   */
  MongoClient startSession(ClientSessionOptions options,
      Handler<AsyncResult<ClientSession>> resultHandler);

  /**
   *  Gets the current cluster description.
   *
   *  <p>
   *  This method will not block, meaning that it may return a {@link ClusterDescription} whose {@code clusterType} is unknown
   *  and whose {@link com.mongodb.connection.ServerDescription}s are all in the connecting state.  If the application requires
   *  notifications after the driver has connected to a member of the cluster, it should register a {@link ClusterListener} via
   *  the {@link ClusterSettings} in {@link com.mongodb.MongoClientSettings}.
   *  </p>
   *
   *  @return the current cluster description
   *  @see ClusterSettings.Builder#addClusterListener(ClusterListener)
   *  @see com.mongodb.MongoClientSettings.Builder#applyToClusterSettings(com.mongodb.Block)
   *  @since 4.1
   */
  ClusterDescription getClusterDescription();

  /**
   * @return mongo object
   * @hidden
   */
  com.mongodb.reactivestreams.client.MongoClient toDriverClass();
}
