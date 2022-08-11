package io.vertx.mongo.client;

import com.mongodb.connection.ClusterDescription;
import com.mongodb.connection.ClusterSettings;
import com.mongodb.event.ClusterListener;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.core.streams.ReadStream;
import io.vertx.mongo.ClientSessionOptions;
import java.io.Closeable;
import java.lang.String;
import java.util.List;

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
   *  Gets the database with the given name.
   *
   *  @param name the name of the database
   *  @return the database
   */
  MongoDatabase getDatabase(String name);

  /**
   *  Close the client, which will close all underlying cached resources, including, for example,
   *  sockets and background monitoring threads.
   */
  void close();

  /**
   *  Get a list of the database names
   *  @mongodb.driver.manual reference/commands/listDatabases List Databases
   *  @return an iterable containing all the names of all the databases
   */
  MongoResult<String> listDatabaseNames();

  /**
   *  Get a list of the database names
   *  @param clientSession the client session with which to associate this operation
   *  @mongodb.driver.manual reference/commands/listDatabases List Databases
   *  @return an iterable containing all the names of all the databases
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
   *  @param clientSession the client session with which to associate this operation
   *  @return the fluent list databases interface
   *  @mongodb.server.release 3.6
   *  @since 1.7
   */
  MongoResult<JsonObject> listDatabases(ClientSession clientSession);

  /**
   *  Creates a change stream for this client.
   *  @return the change stream iterable
   *  @mongodb.driver.dochub core/changestreams Change Streams
   *  @since 1.9
   *  @mongodb.server.release 4.0
   */
  ReadStream<JsonObject> watch();

  /**
   *  Creates a change stream for this client.
   *  @param pipeline the aggregation pipeline to apply to the change stream.
   *  @return the change stream iterable
   *  @mongodb.driver.dochub core/changestreams Change Streams
   *  @since 1.9
   *  @mongodb.server.release 4.0
   */
  ReadStream<JsonObject> watch(List<JsonObject> pipeline);

  /**
   *  Creates a change stream for this client.
   *  @param clientSession the client session with which to associate this operation
   *  @return the change stream iterable
   *  @since 1.9
   *  @mongodb.server.release 4.0
   *  @mongodb.driver.dochub core/changestreams Change Streams
   */
  ReadStream<JsonObject> watch(ClientSession clientSession);

  /**
   *  Creates a change stream for this client.
   *  @param clientSession the client session with which to associate this operation
   *  @param pipeline the aggregation pipeline to apply to the change stream.
   *  @return the change stream iterable
   *  @since 1.9
   *  @mongodb.server.release 4.0
   *  @mongodb.driver.dochub core/changestreams Change Streams
   */
  ReadStream<JsonObject> watch(ClientSession clientSession, List<JsonObject> pipeline);

  /**
   *  Creates a client session.
   *  @return a future for the client session.
   *  @mongodb.server.release 3.6
   *  @since 1.9
   */
  Future<ClientSession> startSession();

  /**
   *  Creates a client session.
   *  @param handler an async result for the client session.
   *  @return <code>this</code>
   *  @mongodb.server.release 3.6
   *  @since 1.9
   */
  MongoClient startSession(Handler<AsyncResult<ClientSession>> handler);

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
   *  @param handler an async result for the client session.
   *  @return <code>this</code>
   *  @mongodb.server.release 3.6
   *  @since 1.7
   */
  MongoClient startSession(ClientSessionOptions options,
      Handler<AsyncResult<ClientSession>> handler);

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
}
