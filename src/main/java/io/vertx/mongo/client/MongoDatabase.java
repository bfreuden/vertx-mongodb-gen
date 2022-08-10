package io.vertx.mongo.client;

import com.mongodb.ReadConcern;
import com.mongodb.ReadPreference;
import com.mongodb.WriteConcern;
import com.mongodb.client.model.CreateCollectionOptions;
import com.mongodb.client.model.CreateViewOptions;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.core.streams.ReadStream;
import java.lang.Class;
import java.lang.String;
import java.lang.Void;
import java.util.List;

/**
 *  The MongoDatabase interface.
 *  <p>Note: Additions to this interface will not be considered to break binary compatibility.</p>
 *  @since 1.0
 */
public interface MongoDatabase {
  /**
   *  Gets the name of the database.
   *
   *  @return the database name
   */
  String getName();

  /**
   *  Get the read preference for the MongoDatabase.
   *
   *  @return the {@link com.mongodb.ReadPreference}
   */
  ReadPreference getReadPreference();

  /**
   *  Get the write concern for the MongoDatabase.
   *
   *  @return the {@link com.mongodb.WriteConcern}
   */
  WriteConcern getWriteConcern();

  /**
   *  Get the read concern for the MongoCollection.
   *
   *  @return the {@link com.mongodb.ReadConcern}
   *  @since 1.2
   *  @mongodb.server.release 3.2
   */
  ReadConcern getReadConcern();

  /**
   *  Create a new MongoDatabase instance with a different read preference.
   *
   *  @param readPreference the new {@link com.mongodb.ReadPreference} for the collection
   *  @return a new MongoDatabase instance with the different readPreference
   */
  MongoDatabase withReadPreference(ReadPreference readPreference);

  /**
   *  Create a new MongoDatabase instance with a different write concern.
   *
   *  @param writeConcern the new {@link com.mongodb.WriteConcern} for the collection
   *  @return a new MongoDatabase instance with the different writeConcern
   */
  MongoDatabase withWriteConcern(WriteConcern writeConcern);

  /**
   *  Create a new MongoDatabase instance with a different read concern.
   *
   *  @param readConcern the new {@link ReadConcern} for the collection
   *  @return a new MongoDatabase instance with the different ReadConcern
   *  @since 1.2
   *  @mongodb.server.release 3.2
   */
  MongoDatabase withReadConcern(ReadConcern readConcern);

  /**
   *  Gets a collection.
   *
   *  @param collectionName the name of the collection to return
   *  @return the collection
   */
  MongoCollection<JsonObject> getCollection(String collectionName);

  /**
   *  Gets a collection, with a specific default document class.
   *
   *  @param collectionName the name of the collection to return
   *  @param clazz          the default class to cast any documents returned from the database into.
   *  @param <TDocument>    the type of the class to use instead of {@code Document}.
   *  @return the collection
   */
  <TDocument> MongoCollection<TDocument> getCollection(String collectionName,
      Class<TDocument> clazz);

  /**
   *  Executes command in the context of the current database.
   *  @param command the command to be run
   *  @return a result containing the command result
   */
  MongoResult<JsonObject> runCommand(JsonObject command);

  /**
   *  Executes command in the context of the current database.
   *  @param command        the command to be run
   *  @param readPreference the {@link com.mongodb.ReadPreference} to be used when executing the command
   *  @return a result containing the command result
   */
  MongoResult<JsonObject> runCommand(JsonObject command, ReadPreference readPreference);

  /**
   *  Executes command in the context of the current database.
   *  @param clientSession the client session with which to associate this operation
   *  @param command the command to be run
   *  @return a result containing the command result
   *  @mongodb.server.release 3.6
   *  @since 1.7
   */
  MongoResult<JsonObject> runCommand(ClientSession clientSession, JsonObject command);

  /**
   *  Executes command in the context of the current database.
   *  @param clientSession the client session with which to associate this operation
   *  @param command        the command to be run
   *  @param readPreference the {@link com.mongodb.ReadPreference} to be used when executing the command
   *  @return a result containing the command result
   *  @mongodb.server.release 3.6
   *  @since 1.7
   */
  MongoResult<JsonObject> runCommand(ClientSession clientSession, JsonObject command,
      ReadPreference readPreference);

  /**
   *  Drops this database.
   *  @return a future identifying when the database has been dropped
   *  @mongodb.driver.manual reference/commands/dropDatabase/#dbcmd.dropDatabase Drop database
   */
  Future<Void> drop();

  /**
   *  Drops this database.
   *  @param handler an async result identifying when the database has been dropped
   *  @return a reference to <code>this</code>
   *  @mongodb.driver.manual reference/commands/dropDatabase/#dbcmd.dropDatabase Drop database
   */
  MongoDatabase drop(Handler<AsyncResult<Future<Void>>> handler);

  /**
   *  Drops this database.
   *  @param clientSession the client session with which to associate this operation
   *  @return a future identifying when the database has been dropped
   *  @mongodb.driver.manual reference/commands/dropDatabase/#dbcmd.dropDatabase Drop database
   *  @mongodb.server.release 3.6
   *  @since 1.7
   */
  Future<Void> drop(ClientSession clientSession);

  /**
   *  Drops this database.
   *  @param clientSession the client session with which to associate this operation
   *  @param handler an async result identifying when the database has been dropped
   *  @return a reference to <code>this</code>
   *  @mongodb.driver.manual reference/commands/dropDatabase/#dbcmd.dropDatabase Drop database
   *  @mongodb.server.release 3.6
   *  @since 1.7
   */
  MongoDatabase drop(ClientSession clientSession, Handler<AsyncResult<Future<Void>>> handler);

  /**
   *  Gets the names of all the collections in this database.
   *  @return a result with all the names of all the collections in this database
   */
  MongoResult<String> listCollectionNames();

  /**
   *  Gets the names of all the collections in this database.
   *  @param clientSession the client session with which to associate this operation
   *  @return a result with all the names of all the collections in this database
   *  @mongodb.server.release 3.6
   *  @since 1.7
   */
  MongoResult<String> listCollectionNames(ClientSession clientSession);

  /**
   *  Finds all the collections in this database.
   *  @return the fluent list collections interface
   *  @mongodb.driver.manual reference/command/listCollections listCollections
   */
  MongoResult<JsonObject> listCollections();

  /**
   *  Finds all the collections in this database.
   *  @param clientSession the client session with which to associate this operation
   *  @return the fluent list collections interface
   *  @mongodb.driver.manual reference/command/listCollections listCollections
   *  @mongodb.server.release 3.6
   *  @since 1.7
   */
  MongoResult<JsonObject> listCollections(ClientSession clientSession);

  /**
   *  Create a new collection with the given name.
   *  @param collectionName the name for the new collection to create
   *  @return a future identifying when the collection has been created
   *  @mongodb.driver.manual reference/commands/create Create Command
   */
  Future<Void> createCollection(String collectionName);

  /**
   *  Create a new collection with the given name.
   *  @param collectionName the name for the new collection to create
   *  @param handler an async result identifying when the collection has been created
   *  @return a reference to <code>this</code>
   *  @mongodb.driver.manual reference/commands/create Create Command
   */
  MongoDatabase createCollection(String collectionName, Handler<AsyncResult<Future<Void>>> handler);

  /**
   *  Create a new collection with the selected options
   *  @param collectionName the name for the new collection to create
   *  @param options        various options for creating the collection
   *  @return a future identifying when the collection has been created
   *  @mongodb.driver.manual reference/commands/create Create Command
   */
  Future<Void> createCollection(String collectionName, CreateCollectionOptions options);

  /**
   *  Create a new collection with the selected options
   *  @param collectionName the name for the new collection to create
   *  @param options        various options for creating the collection
   *  @param handler an async result identifying when the collection has been created
   *  @return a reference to <code>this</code>
   *  @mongodb.driver.manual reference/commands/create Create Command
   */
  MongoDatabase createCollection(String collectionName, CreateCollectionOptions options,
      Handler<AsyncResult<Future<Void>>> handler);

  /**
   *  Create a new collection with the given name.
   *  @param clientSession the client session with which to associate this operation
   *  @param collectionName the name for the new collection to create
   *  @return a future identifying when the collection has been created
   *  @mongodb.driver.manual reference/commands/create Create Command
   *  @mongodb.server.release 3.6
   *  @since 1.7
   */
  Future<Void> createCollection(ClientSession clientSession, String collectionName);

  /**
   *  Create a new collection with the given name.
   *  @param clientSession the client session with which to associate this operation
   *  @param collectionName the name for the new collection to create
   *  @param handler an async result identifying when the collection has been created
   *  @return a reference to <code>this</code>
   *  @mongodb.driver.manual reference/commands/create Create Command
   *  @mongodb.server.release 3.6
   *  @since 1.7
   */
  MongoDatabase createCollection(ClientSession clientSession, String collectionName,
      Handler<AsyncResult<Future<Void>>> handler);

  /**
   *  Create a new collection with the selected options
   *  @param clientSession the client session with which to associate this operation
   *  @param collectionName the name for the new collection to create
   *  @param options        various options for creating the collection
   *  @return a future identifying when the collection has been created
   *  @mongodb.driver.manual reference/commands/create Create Command
   *  @mongodb.server.release 3.6
   *  @since 1.7
   */
  Future<Void> createCollection(ClientSession clientSession, String collectionName,
      CreateCollectionOptions options);

  /**
   *  Create a new collection with the selected options
   *  @param clientSession the client session with which to associate this operation
   *  @param collectionName the name for the new collection to create
   *  @param options        various options for creating the collection
   *  @param handler an async result identifying when the collection has been created
   *  @return a reference to <code>this</code>
   *  @mongodb.driver.manual reference/commands/create Create Command
   *  @mongodb.server.release 3.6
   *  @since 1.7
   */
  MongoDatabase createCollection(ClientSession clientSession, String collectionName,
      CreateCollectionOptions options, Handler<AsyncResult<Future<Void>>> handler);

  /**
   *  Creates a view with the given name, backing collection/view name, and aggregation pipeline that defines the view.
   *  @param viewName the name of the view to create
   *  @param viewOn   the backing collection/view for the view
   *  @param pipeline the pipeline that defines the view
   *  @return an future identifying when the collection view has been created
   *  @since 1.3
   *  @mongodb.server.release 3.4
   *  @mongodb.driver.manual reference/command/create Create Command
   */
  Future<Void> createView(String viewName, String viewOn, List<JsonObject> pipeline);

  /**
   *  Creates a view with the given name, backing collection/view name, and aggregation pipeline that defines the view.
   *  @param viewName the name of the view to create
   *  @param viewOn   the backing collection/view for the view
   *  @param pipeline the pipeline that defines the view
   *  @param handler an async result identifying when the collection view has been created
   *  @return a reference to <code>this</code>
   *  @since 1.3
   *  @mongodb.server.release 3.4
   *  @mongodb.driver.manual reference/command/create Create Command
   */
  MongoDatabase createView(String viewName, String viewOn, List<JsonObject> pipeline,
      Handler<AsyncResult<Future<Void>>> handler);

  /**
   *  Creates a view with the given name, backing collection/view name, aggregation pipeline, and options that defines the view.
   *  @param viewName the name of the view to create
   *  @param viewOn   the backing collection/view for the view
   *  @param pipeline the pipeline that defines the view
   *  @param createViewOptions various options for creating the view
   *  @return an future identifying when the collection view has been created
   *  @since 1.3
   *  @mongodb.server.release 3.4
   *  @mongodb.driver.manual reference/command/create Create Command
   */
  Future<Void> createView(String viewName, String viewOn, List<JsonObject> pipeline,
      CreateViewOptions createViewOptions);

  /**
   *  Creates a view with the given name, backing collection/view name, aggregation pipeline, and options that defines the view.
   *  @param viewName the name of the view to create
   *  @param viewOn   the backing collection/view for the view
   *  @param pipeline the pipeline that defines the view
   *  @param createViewOptions various options for creating the view
   *  @param handler an async result identifying when the collection view has been created
   *  @return a reference to <code>this</code>
   *  @since 1.3
   *  @mongodb.server.release 3.4
   *  @mongodb.driver.manual reference/command/create Create Command
   */
  MongoDatabase createView(String viewName, String viewOn, List<JsonObject> pipeline,
      CreateViewOptions createViewOptions, Handler<AsyncResult<Future<Void>>> handler);

  /**
   *  Creates a view with the given name, backing collection/view name, and aggregation pipeline that defines the view.
   *  @param clientSession the client session with which to associate this operation
   *  @param viewName the name of the view to create
   *  @param viewOn   the backing collection/view for the view
   *  @param pipeline the pipeline that defines the view
   *  @return an future identifying when the collection view has been created
   *  @mongodb.driver.manual reference/command/create Create Command
   *  @mongodb.server.release 3.6
   *  @since 1.7
   */
  Future<Void> createView(ClientSession clientSession, String viewName, String viewOn,
      List<JsonObject> pipeline);

  /**
   *  Creates a view with the given name, backing collection/view name, and aggregation pipeline that defines the view.
   *  @param clientSession the client session with which to associate this operation
   *  @param viewName the name of the view to create
   *  @param viewOn   the backing collection/view for the view
   *  @param pipeline the pipeline that defines the view
   *  @param handler an async result identifying when the collection view has been created
   *  @return a reference to <code>this</code>
   *  @mongodb.driver.manual reference/command/create Create Command
   *  @mongodb.server.release 3.6
   *  @since 1.7
   */
  MongoDatabase createView(ClientSession clientSession, String viewName, String viewOn,
      List<JsonObject> pipeline, Handler<AsyncResult<Future<Void>>> handler);

  /**
   *  Creates a view with the given name, backing collection/view name, aggregation pipeline, and options that defines the view.
   *  @param clientSession the client session with which to associate this operation
   *  @param viewName the name of the view to create
   *  @param viewOn   the backing collection/view for the view
   *  @param pipeline the pipeline that defines the view
   *  @param createViewOptions various options for creating the view
   *  @return an future identifying when the collection view has been created
   *  @mongodb.driver.manual reference/command/create Create Command
   *  @mongodb.server.release 3.6
   *  @since 1.7
   */
  Future<Void> createView(ClientSession clientSession, String viewName, String viewOn,
      List<JsonObject> pipeline, CreateViewOptions createViewOptions);

  /**
   *  Creates a view with the given name, backing collection/view name, aggregation pipeline, and options that defines the view.
   *  @param clientSession the client session with which to associate this operation
   *  @param viewName the name of the view to create
   *  @param viewOn   the backing collection/view for the view
   *  @param pipeline the pipeline that defines the view
   *  @param createViewOptions various options for creating the view
   *  @param handler an async result identifying when the collection view has been created
   *  @return a reference to <code>this</code>
   *  @mongodb.driver.manual reference/command/create Create Command
   *  @mongodb.server.release 3.6
   *  @since 1.7
   */
  MongoDatabase createView(ClientSession clientSession, String viewName, String viewOn,
      List<JsonObject> pipeline, CreateViewOptions createViewOptions,
      Handler<AsyncResult<Future<Void>>> handler);

  /**
   *  Creates a change stream for this database.
   *  @return the change stream iterable
   *  @mongodb.driver.dochub core/changestreams Change Streams
   *  @since 1.9
   *  @mongodb.server.release 4.0
   */
  ReadStream<JsonObject> watch();

  /**
   *  Creates a change stream for this database.
   *  @param pipeline the aggregation pipeline to apply to the change stream.
   *  @return the change stream iterable
   *  @mongodb.driver.dochub core/changestreams Change Streams
   *  @since 1.9
   *  @mongodb.server.release 4.0
   */
  ReadStream<JsonObject> watch(List<JsonObject> pipeline);

  /**
   *  Creates a change stream for this database.
   *  @param clientSession the client session with which to associate this operation
   *  @return the change stream iterable
   *  @since 1.9
   *  @mongodb.server.release 4.0
   *  @mongodb.driver.dochub core/changestreams Change Streams
   */
  ReadStream<JsonObject> watch(ClientSession clientSession);

  /**
   *  Creates a change stream for this database.
   *  @param clientSession the client session with which to associate this operation
   *  @param pipeline the aggregation pipeline to apply to the change stream.
   *  @return the change stream iterable
   *  @since 1.9
   *  @mongodb.server.release 4.0
   *  @mongodb.driver.dochub core/changestreams Change Streams
   */
  ReadStream<JsonObject> watch(ClientSession clientSession, List<JsonObject> pipeline);

  /**
   *  Runs an aggregation framework pipeline on the database for pipeline stages
   *  that do not require an underlying collection, such as {@code  currentOp} and {@code  listLocalSessions}.
   *  @param pipeline the aggregation pipeline
   *  @return an iterable containing the result of the aggregation operation
   *  @since 1.11
   *  @mongodb.driver.manual reference/command/aggregate/#dbcmd.aggregate Aggregate Command
   *  @mongodb.server.release 3.6
   */
  MongoResult<JsonObject> aggregate(List<JsonObject> pipeline);

  /**
   *  Runs an aggregation framework pipeline on the database for pipeline stages
   *  that do not require an underlying collection, such as {@code  currentOp} and {@code  listLocalSessions}.
   *  @param clientSession the client session with which to associate this operation
   *  @param pipeline the aggregation pipeline
   *  @return an iterable containing the result of the aggregation operation
   *  @since 1.11
   *  @mongodb.driver.manual reference/command/aggregate/#dbcmd.aggregate Aggregate Command
   *  @mongodb.server.release 3.6
   */
  MongoResult<JsonObject> aggregate(ClientSession clientSession, List<JsonObject> pipeline);
}
