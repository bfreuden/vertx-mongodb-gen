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

import com.mongodb.ReadConcern;
import com.mongodb.ReadPreference;
import com.mongodb.WriteConcern;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.mongo.MongoCollectionResult;
import io.vertx.mongo.MongoResult;
import io.vertx.mongo.client.model.CreateCollectionOptions;
import io.vertx.mongo.client.model.CreateViewOptions;
import io.vertx.mongo.client.model.changestream.ChangeStreamDocument;
import io.vertx.mongo.impl.MongoClientContext;
import java.lang.Class;
import java.lang.String;
import java.lang.Void;
import org.bson.codecs.configuration.CodecRegistry;

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
   *  Get the codec registry for the MongoDatabase.
   *
   *  @return the {@link org.bson.codecs.configuration.CodecRegistry}
   */
  CodecRegistry getCodecRegistry();

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
   *  Create a new MongoDatabase instance with a different codec registry.
   *
   *  @param codecRegistry the new {@link org.bson.codecs.configuration.CodecRegistry} for the collection
   *  @return a new MongoDatabase instance with the different codec registry
   */
  MongoDatabase withCodecRegistry(CodecRegistry codecRegistry);

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
   *  @return a future containing the command result
   */
  Future<JsonObject> runCommand(JsonObject command);

  /**
   *  Executes command in the context of the current database.
   *  @param command the command to be run
   *  @param resultHandler an async result containing the command result
   */
  void runCommand(JsonObject command, Handler<AsyncResult<JsonObject>> resultHandler);

  /**
   *  Executes command in the context of the current database.
   *  @param command        the command to be run
   *  @param readPreference the {@link com.mongodb.ReadPreference} to be used when executing the command
   *  @return a future containing the command result
   */
  Future<JsonObject> runCommand(JsonObject command, ReadPreference readPreference);

  /**
   *  Executes command in the context of the current database.
   *  @param command        the command to be run
   *  @param readPreference the {@link com.mongodb.ReadPreference} to be used when executing the command
   *  @param resultHandler an async result containing the command result
   */
  void runCommand(JsonObject command, ReadPreference readPreference,
      Handler<AsyncResult<JsonObject>> resultHandler);

  /**
   *  Executes command in the context of the current database.
   *  @param clientSession the client session with which to associate this operation
   *  @param command the command to be run
   *  @return a future containing the command result
   *  @mongodb.server.release 3.6
   *  @since 1.7
   */
  Future<JsonObject> runCommand(ClientSession clientSession, JsonObject command);

  /**
   *  Executes command in the context of the current database.
   *  @param clientSession the client session with which to associate this operation
   *  @param command the command to be run
   *  @param resultHandler an async result containing the command result
   *  @mongodb.server.release 3.6
   *  @since 1.7
   */
  void runCommand(ClientSession clientSession, JsonObject command,
      Handler<AsyncResult<JsonObject>> resultHandler);

  /**
   *  Executes command in the context of the current database.
   *  @param clientSession the client session with which to associate this operation
   *  @param command        the command to be run
   *  @param readPreference the {@link com.mongodb.ReadPreference} to be used when executing the command
   *  @return a future containing the command result
   *  @mongodb.server.release 3.6
   *  @since 1.7
   */
  Future<JsonObject> runCommand(ClientSession clientSession, JsonObject command,
      ReadPreference readPreference);

  /**
   *  Executes command in the context of the current database.
   *  @param clientSession the client session with which to associate this operation
   *  @param command        the command to be run
   *  @param readPreference the {@link com.mongodb.ReadPreference} to be used when executing the command
   *  @param resultHandler an async result containing the command result
   *  @mongodb.server.release 3.6
   *  @since 1.7
   */
  void runCommand(ClientSession clientSession, JsonObject command, ReadPreference readPreference,
      Handler<AsyncResult<JsonObject>> resultHandler);

  /**
   *  Drops this database.
   *  @return a future identifying when the database has been dropped
   *  @mongodb.driver.manual reference/commands/dropDatabase/#dbcmd.dropDatabase Drop database
   */
  Future<Void> drop();

  /**
   *  Drops this database.
   *  @param resultHandler an async result identifying when the database has been dropped
   *  @mongodb.driver.manual reference/commands/dropDatabase/#dbcmd.dropDatabase Drop database
   */
  void drop(Handler<AsyncResult<Void>> resultHandler);

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
   *  @param resultHandler an async result identifying when the database has been dropped
   *  @mongodb.driver.manual reference/commands/dropDatabase/#dbcmd.dropDatabase Drop database
   *  @mongodb.server.release 3.6
   *  @since 1.7
   */
  void drop(ClientSession clientSession, Handler<AsyncResult<Void>> resultHandler);

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
   *  @param options options
   *  @return the fluent list collections interface
   *  @mongodb.driver.manual reference/command/listCollections listCollections
   */
  MongoResult<JsonObject> listCollections(ListCollectionsOptions options);

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
   *  Finds all the collections in this database.
   *  @param clientSession the client session with which to associate this operation
   *  @param options options
   *  @return the fluent list collections interface
   *  @mongodb.driver.manual reference/command/listCollections listCollections
   *  @mongodb.server.release 3.6
   *  @since 1.7
   */
  MongoResult<JsonObject> listCollections(ClientSession clientSession,
      ListCollectionsOptions options);

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
   *  @param resultHandler an async result identifying when the collection has been created
   *  @mongodb.driver.manual reference/commands/create Create Command
   */
  void createCollection(String collectionName, Handler<AsyncResult<Void>> resultHandler);

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
   *  @param resultHandler an async result identifying when the collection has been created
   *  @mongodb.driver.manual reference/commands/create Create Command
   */
  void createCollection(String collectionName, CreateCollectionOptions options,
      Handler<AsyncResult<Void>> resultHandler);

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
   *  @param resultHandler an async result identifying when the collection has been created
   *  @mongodb.driver.manual reference/commands/create Create Command
   *  @mongodb.server.release 3.6
   *  @since 1.7
   */
  void createCollection(ClientSession clientSession, String collectionName,
      Handler<AsyncResult<Void>> resultHandler);

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
   *  @param resultHandler an async result identifying when the collection has been created
   *  @mongodb.driver.manual reference/commands/create Create Command
   *  @mongodb.server.release 3.6
   *  @since 1.7
   */
  void createCollection(ClientSession clientSession, String collectionName,
      CreateCollectionOptions options, Handler<AsyncResult<Void>> resultHandler);

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
  Future<Void> createView(String viewName, String viewOn, JsonArray pipeline);

  /**
   *  Creates a view with the given name, backing collection/view name, and aggregation pipeline that defines the view.
   *  @param viewName the name of the view to create
   *  @param viewOn   the backing collection/view for the view
   *  @param pipeline the pipeline that defines the view
   *  @param resultHandler an async result identifying when the collection view has been created
   *  @since 1.3
   *  @mongodb.server.release 3.4
   *  @mongodb.driver.manual reference/command/create Create Command
   */
  void createView(String viewName, String viewOn, JsonArray pipeline,
      Handler<AsyncResult<Void>> resultHandler);

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
  Future<Void> createView(String viewName, String viewOn, JsonArray pipeline,
      CreateViewOptions createViewOptions);

  /**
   *  Creates a view with the given name, backing collection/view name, aggregation pipeline, and options that defines the view.
   *  @param viewName the name of the view to create
   *  @param viewOn   the backing collection/view for the view
   *  @param pipeline the pipeline that defines the view
   *  @param createViewOptions various options for creating the view
   *  @param resultHandler an async result identifying when the collection view has been created
   *  @since 1.3
   *  @mongodb.server.release 3.4
   *  @mongodb.driver.manual reference/command/create Create Command
   */
  void createView(String viewName, String viewOn, JsonArray pipeline,
      CreateViewOptions createViewOptions, Handler<AsyncResult<Void>> resultHandler);

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
      JsonArray pipeline);

  /**
   *  Creates a view with the given name, backing collection/view name, and aggregation pipeline that defines the view.
   *  @param clientSession the client session with which to associate this operation
   *  @param viewName the name of the view to create
   *  @param viewOn   the backing collection/view for the view
   *  @param pipeline the pipeline that defines the view
   *  @param resultHandler an async result identifying when the collection view has been created
   *  @mongodb.driver.manual reference/command/create Create Command
   *  @mongodb.server.release 3.6
   *  @since 1.7
   */
  void createView(ClientSession clientSession, String viewName, String viewOn, JsonArray pipeline,
      Handler<AsyncResult<Void>> resultHandler);

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
      JsonArray pipeline, CreateViewOptions createViewOptions);

  /**
   *  Creates a view with the given name, backing collection/view name, aggregation pipeline, and options that defines the view.
   *  @param clientSession the client session with which to associate this operation
   *  @param viewName the name of the view to create
   *  @param viewOn   the backing collection/view for the view
   *  @param pipeline the pipeline that defines the view
   *  @param createViewOptions various options for creating the view
   *  @param resultHandler an async result identifying when the collection view has been created
   *  @mongodb.driver.manual reference/command/create Create Command
   *  @mongodb.server.release 3.6
   *  @since 1.7
   */
  void createView(ClientSession clientSession, String viewName, String viewOn, JsonArray pipeline,
      CreateViewOptions createViewOptions, Handler<AsyncResult<Void>> resultHandler);

  /**
   *  Creates a change stream for this database.
   *  @return the change stream read stream
   *  @mongodb.driver.dochub core/changestreams Change Streams
   *  @since 1.9
   *  @mongodb.server.release 4.0
   */
  MongoResult<ChangeStreamDocument<JsonObject>> watch();

  /**
   *  Creates a change stream for this database.
   *  @param options options
   *  @return the change stream read stream
   *  @mongodb.driver.dochub core/changestreams Change Streams
   *  @since 1.9
   *  @mongodb.server.release 4.0
   */
  MongoResult<ChangeStreamDocument<JsonObject>> watch(ChangeStreamOptions options);

  /**
   *  Creates a change stream for this database.
   *  @param pipeline the aggregation pipeline to apply to the change stream.
   *  @return the change stream read stream
   *  @mongodb.driver.dochub core/changestreams Change Streams
   *  @since 1.9
   *  @mongodb.server.release 4.0
   */
  MongoResult<ChangeStreamDocument<JsonObject>> watch(JsonArray pipeline);

  /**
   *  Creates a change stream for this database.
   *  @param pipeline the aggregation pipeline to apply to the change stream.
   *  @param options options
   *  @return the change stream read stream
   *  @mongodb.driver.dochub core/changestreams Change Streams
   *  @since 1.9
   *  @mongodb.server.release 4.0
   */
  MongoResult<ChangeStreamDocument<JsonObject>> watch(JsonArray pipeline,
      ChangeStreamOptions options);

  /**
   *  Creates a change stream for this database.
   *  @param clientSession the client session with which to associate this operation
   *  @return the change stream read stream
   *  @since 1.9
   *  @mongodb.server.release 4.0
   *  @mongodb.driver.dochub core/changestreams Change Streams
   */
  MongoResult<ChangeStreamDocument<JsonObject>> watch(ClientSession clientSession);

  /**
   *  Creates a change stream for this database.
   *  @param clientSession the client session with which to associate this operation
   *  @param options options
   *  @return the change stream read stream
   *  @since 1.9
   *  @mongodb.server.release 4.0
   *  @mongodb.driver.dochub core/changestreams Change Streams
   */
  MongoResult<ChangeStreamDocument<JsonObject>> watch(ClientSession clientSession,
      ChangeStreamOptions options);

  /**
   *  Creates a change stream for this database.
   *  @param clientSession the client session with which to associate this operation
   *  @param pipeline the aggregation pipeline to apply to the change stream.
   *  @return the change stream read stream
   *  @since 1.9
   *  @mongodb.server.release 4.0
   *  @mongodb.driver.dochub core/changestreams Change Streams
   */
  MongoResult<ChangeStreamDocument<JsonObject>> watch(ClientSession clientSession,
      JsonArray pipeline);

  /**
   *  Creates a change stream for this database.
   *  @param clientSession the client session with which to associate this operation
   *  @param pipeline the aggregation pipeline to apply to the change stream.
   *  @param options options
   *  @return the change stream read stream
   *  @since 1.9
   *  @mongodb.server.release 4.0
   *  @mongodb.driver.dochub core/changestreams Change Streams
   */
  MongoResult<ChangeStreamDocument<JsonObject>> watch(ClientSession clientSession,
      JsonArray pipeline, ChangeStreamOptions options);

  /**
   *  Runs an aggregation framework pipeline on the database for pipeline stages
   *  that do not require an underlying collection, such as {@code $currentOp} and {@code $listLocalSessions}.
   *  @param pipeline the aggregation pipeline
   *  @return an result containing the result of the aggregation operation
   *  @since 1.11
   *  @mongodb.driver.manual reference/command/aggregate/#dbcmd.aggregate Aggregate Command
   *  @mongodb.server.release 3.6
   */
  MongoCollectionResult<JsonObject> aggregate(JsonArray pipeline);

  /**
   *  Runs an aggregation framework pipeline on the database for pipeline stages
   *  that do not require an underlying collection, such as {@code $currentOp} and {@code $listLocalSessions}.
   *  @param pipeline the aggregation pipeline
   *  @param options options
   *  @return an result containing the result of the aggregation operation
   *  @since 1.11
   *  @mongodb.driver.manual reference/command/aggregate/#dbcmd.aggregate Aggregate Command
   *  @mongodb.server.release 3.6
   */
  MongoCollectionResult<JsonObject> aggregate(JsonArray pipeline, AggregateOptions options);

  /**
   *  Runs an aggregation framework pipeline on the database for pipeline stages
   *  that do not require an underlying collection, such as {@code $currentOp} and {@code $listLocalSessions}.
   *  @param clientSession the client session with which to associate this operation
   *  @param pipeline the aggregation pipeline
   *  @return an result containing the result of the aggregation operation
   *  @since 1.11
   *  @mongodb.driver.manual reference/command/aggregate/#dbcmd.aggregate Aggregate Command
   *  @mongodb.server.release 3.6
   */
  MongoCollectionResult<JsonObject> aggregate(ClientSession clientSession, JsonArray pipeline);

  /**
   *  Runs an aggregation framework pipeline on the database for pipeline stages
   *  that do not require an underlying collection, such as {@code $currentOp} and {@code $listLocalSessions}.
   *  @param clientSession the client session with which to associate this operation
   *  @param pipeline the aggregation pipeline
   *  @param options options
   *  @return an result containing the result of the aggregation operation
   *  @since 1.11
   *  @mongodb.driver.manual reference/command/aggregate/#dbcmd.aggregate Aggregate Command
   *  @mongodb.server.release 3.6
   */
  MongoCollectionResult<JsonObject> aggregate(ClientSession clientSession, JsonArray pipeline,
      AggregateOptions options);

  /**
   * @return mongo object
   * @hidden
   */
  com.mongodb.reactivestreams.client.MongoDatabase toDriverClass();
}
