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

import com.mongodb.bulk.BulkWriteResult;
import com.mongodb.client.model.IndexModel;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.InsertManyResult;
import com.mongodb.client.result.InsertOneResult;
import com.mongodb.client.result.UpdateResult;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.core.streams.ReadStream;
import io.vertx.mongo.MongoNamespace;
import io.vertx.mongo.ReadConcern;
import io.vertx.mongo.ReadPreference;
import io.vertx.mongo.WriteConcern;
import io.vertx.mongo.client.model.BulkWriteOptions;
import io.vertx.mongo.client.model.CountOptions;
import io.vertx.mongo.client.model.CreateIndexOptions;
import io.vertx.mongo.client.model.DeleteOptions;
import io.vertx.mongo.client.model.DropIndexOptions;
import io.vertx.mongo.client.model.EstimatedDocumentCountOptions;
import io.vertx.mongo.client.model.FindOneAndDeleteOptions;
import io.vertx.mongo.client.model.FindOneAndReplaceOptions;
import io.vertx.mongo.client.model.FindOneAndUpdateOptions;
import io.vertx.mongo.client.model.IndexOptions;
import io.vertx.mongo.client.model.InsertManyOptions;
import io.vertx.mongo.client.model.InsertOneOptions;
import io.vertx.mongo.client.model.RenameCollectionOptions;
import io.vertx.mongo.client.model.ReplaceOptions;
import io.vertx.mongo.client.model.UpdateOptions;
import java.lang.Class;
import java.lang.Long;
import java.lang.String;
import java.lang.Void;
import java.util.List;

/**
 *  The MongoCollection interface.
 *  <p>Note: Additions to this interface will not be considered to break binary compatibility.</p>
 *  @since 1.0
 */
public interface MongoCollection<TDocument> {
  /**
   *  Gets the namespace of this collection.
   *
   *  @return the namespace
   */
  MongoNamespace getNamespace();

  /**
   *  Get the class of documents stored in this collection.
   *
   *  @return the class
   */
  Class<TDocument> getDocumentClass();

  /**
   *  Get the read preference for the MongoCollection.
   *
   *  @return the {@link com.mongodb.ReadPreference}
   */
  ReadPreference getReadPreference();

  /**
   *  Get the write concern for the MongoCollection.
   *
   *  @return the {@link com.mongodb.WriteConcern}
   */
  WriteConcern getWriteConcern();

  /**
   *  Get the read concern for the MongoCollection.
   *
   *  @return the {@link com.mongodb.ReadConcern}
   *  @mongodb.server.release 3.2
   *  @since 1.2
   */
  ReadConcern getReadConcern();

  /**
   *  Create a new MongoCollection instance with a different default class to cast any documents returned from the database into..
   *
   *  @param clazz          the default class to cast any documents returned from the database into.
   *  @param <NewTDocument> The type that the new collection will encode documents from and decode documents to
   *  @return a new MongoCollection instance with the different default class
   */
  <NewTDocument> MongoCollection withDocumentClass(Class<NewTDocument> clazz);

  /**
   *  Create a new MongoCollection instance with a different read preference.
   *
   *  @param readPreference the new {@link com.mongodb.ReadPreference} for the collection
   *  @return a new MongoCollection instance with the different readPreference
   */
  MongoCollection<TDocument> withReadPreference(ReadPreference readPreference);

  /**
   *  Create a new MongoCollection instance with a different write concern.
   *
   *  @param writeConcern the new {@link com.mongodb.WriteConcern} for the collection
   *  @return a new MongoCollection instance with the different writeConcern
   */
  MongoCollection<TDocument> withWriteConcern(WriteConcern writeConcern);

  /**
   *  Create a new MongoCollection instance with a different read concern.
   *
   *  @param readConcern the new {@link ReadConcern} for the collection
   *  @return a new MongoCollection instance with the different ReadConcern
   *  @mongodb.server.release 3.2
   *  @since 1.2
   */
  MongoCollection<TDocument> withReadConcern(ReadConcern readConcern);

  /**
   *  Gets an estimate of the count of documents in a collection using collection metadata.
   *  @return a future with a single element indicating the estimated number of documents
   *  @since 1.9
   */
  Future<Long> estimatedDocumentCount();

  /**
   *  Gets an estimate of the count of documents in a collection using collection metadata.
   *  @param resultHandler an async result with a single element indicating the estimated number of documents
   *  @return <code>this</code>
   *  @since 1.9
   */
  MongoCollection<TDocument> estimatedDocumentCount(Handler<AsyncResult<Long>> resultHandler);

  /**
   *  Gets an estimate of the count of documents in a collection using collection metadata.
   *  @param options the options describing the count
   *  @return a future with a single element indicating the estimated number of documents
   *  @since 1.9
   */
  Future<Long> estimatedDocumentCount(EstimatedDocumentCountOptions options);

  /**
   *  Gets an estimate of the count of documents in a collection using collection metadata.
   *  @param options the options describing the count
   *  @param resultHandler an async result with a single element indicating the estimated number of documents
   *  @return <code>this</code>
   *  @since 1.9
   */
  MongoCollection<TDocument> estimatedDocumentCount(EstimatedDocumentCountOptions options,
      Handler<AsyncResult<Long>> resultHandler);

  /**
   *  Counts the number of documents in the collection.
   *  <p>
   *  Note: For a fast count of the total documents in a collection see {@link #estimatedDocumentCount()}.<br>
   *  Note: When migrating from {@code count()} to {@code countDocuments()} the following query operators must be replaced:
   *  </p>
   *  <pre>
   *   +-------------+--------------------------------+
   *   | Operator    | Replacement                    |
   *   +=============+================================+
   *   | $where      |  $expr                         |
   *   +-------------+--------------------------------+
   *   | $near       |  $geoWithin with $center       |
   *   +-------------+--------------------------------+
   *   | $nearSphere |  $geoWithin with $centerSphere |
   *   +-------------+--------------------------------+
   *  </pre>
   *  @return a future with a single element indicating the number of documents
   *  @since 1.9
   */
  Future<Long> countDocuments();

  /**
   *  Counts the number of documents in the collection.
   *  <p>
   *  Note: For a fast count of the total documents in a collection see {@link #estimatedDocumentCount()}.<br>
   *  Note: When migrating from {@code count()} to {@code countDocuments()} the following query operators must be replaced:
   *  </p>
   *  <pre>
   *   +-------------+--------------------------------+
   *   | Operator    | Replacement                    |
   *   +=============+================================+
   *   | $where      |  $expr                         |
   *   +-------------+--------------------------------+
   *   | $near       |  $geoWithin with $center       |
   *   +-------------+--------------------------------+
   *   | $nearSphere |  $geoWithin with $centerSphere |
   *   +-------------+--------------------------------+
   *  </pre>
   *  @param resultHandler an async result with a single element indicating the number of documents
   *  @return <code>this</code>
   *  @since 1.9
   */
  MongoCollection<TDocument> countDocuments(Handler<AsyncResult<Long>> resultHandler);

  /**
   *  Counts the number of documents in the collection according to the given options.
   *  <p>
   *  Note: For a fast count of the total documents in a collection see {@link #estimatedDocumentCount()}.<br>
   *  Note: When migrating from {@code count()} to {@code countDocuments()} the following query operators must be replaced:
   *  </p>
   *  <pre>
   *   +-------------+--------------------------------+
   *   | Operator    | Replacement                    |
   *   +=============+================================+
   *   | $where      |  $expr                         |
   *   +-------------+--------------------------------+
   *   | $near       |  $geoWithin with $center       |
   *   +-------------+--------------------------------+
   *   | $nearSphere |  $geoWithin with $centerSphere |
   *   +-------------+--------------------------------+
   *  </pre>
   *  @param filter the query filter
   *  @return a future with a single element indicating the number of documents
   *  @since 1.9
   */
  Future<Long> countDocuments(JsonObject filter);

  /**
   *  Counts the number of documents in the collection according to the given options.
   *  <p>
   *  Note: For a fast count of the total documents in a collection see {@link #estimatedDocumentCount()}.<br>
   *  Note: When migrating from {@code count()} to {@code countDocuments()} the following query operators must be replaced:
   *  </p>
   *  <pre>
   *   +-------------+--------------------------------+
   *   | Operator    | Replacement                    |
   *   +=============+================================+
   *   | $where      |  $expr                         |
   *   +-------------+--------------------------------+
   *   | $near       |  $geoWithin with $center       |
   *   +-------------+--------------------------------+
   *   | $nearSphere |  $geoWithin with $centerSphere |
   *   +-------------+--------------------------------+
   *  </pre>
   *  @param filter the query filter
   *  @param resultHandler an async result with a single element indicating the number of documents
   *  @return <code>this</code>
   *  @since 1.9
   */
  MongoCollection<TDocument> countDocuments(JsonObject filter,
      Handler<AsyncResult<Long>> resultHandler);

  /**
   *  Counts the number of documents in the collection according to the given options.
   *  <p>
   *  Note: For a fast count of the total documents in a collection see {@link #estimatedDocumentCount()}.<br>
   *  Note: When migrating from {@code count()} to {@code countDocuments()} the following query operators must be replaced:
   *  </p>
   *  <pre>
   *   +-------------+--------------------------------+
   *   | Operator    | Replacement                    |
   *   +=============+================================+
   *   | $where      |  $expr                         |
   *   +-------------+--------------------------------+
   *   | $near       |  $geoWithin with $center       |
   *   +-------------+--------------------------------+
   *   | $nearSphere |  $geoWithin with $centerSphere |
   *   +-------------+--------------------------------+
   *  </pre>
   *  @param filter  the query filter
   *  @param options the options describing the count
   *  @return a future with a single element indicating the number of documents
   *  @since 1.9
   */
  Future<Long> countDocuments(JsonObject filter, CountOptions options);

  /**
   *  Counts the number of documents in the collection according to the given options.
   *  <p>
   *  Note: For a fast count of the total documents in a collection see {@link #estimatedDocumentCount()}.<br>
   *  Note: When migrating from {@code count()} to {@code countDocuments()} the following query operators must be replaced:
   *  </p>
   *  <pre>
   *   +-------------+--------------------------------+
   *   | Operator    | Replacement                    |
   *   +=============+================================+
   *   | $where      |  $expr                         |
   *   +-------------+--------------------------------+
   *   | $near       |  $geoWithin with $center       |
   *   +-------------+--------------------------------+
   *   | $nearSphere |  $geoWithin with $centerSphere |
   *   +-------------+--------------------------------+
   *  </pre>
   *  @param filter  the query filter
   *  @param options the options describing the count
   *  @param resultHandler an async result with a single element indicating the number of documents
   *  @return <code>this</code>
   *  @since 1.9
   */
  MongoCollection<TDocument> countDocuments(JsonObject filter, CountOptions options,
      Handler<AsyncResult<Long>> resultHandler);

  /**
   *  Counts the number of documents in the collection.
   *  <p>
   *  Note: For a fast count of the total documents in a collection see {@link #estimatedDocumentCount()}.<br>
   *  Note: When migrating from {@code count()} to {@code countDocuments()} the following query operators must be replaced:
   *  </p>
   *  <pre>
   *   +-------------+--------------------------------+
   *   | Operator    | Replacement                    |
   *   +=============+================================+
   *   | $where      |  $expr                         |
   *   +-------------+--------------------------------+
   *   | $near       |  $geoWithin with $center       |
   *   +-------------+--------------------------------+
   *   | $nearSphere |  $geoWithin with $centerSphere |
   *   +-------------+--------------------------------+
   *  </pre>
   *  @param clientSession the client session with which to associate this operation
   *  @return a future with a single element indicating the number of documents
   *  @mongodb.server.release 3.6
   *  @since 1.9
   */
  Future<Long> countDocuments(ClientSession clientSession);

  /**
   *  Counts the number of documents in the collection.
   *  <p>
   *  Note: For a fast count of the total documents in a collection see {@link #estimatedDocumentCount()}.<br>
   *  Note: When migrating from {@code count()} to {@code countDocuments()} the following query operators must be replaced:
   *  </p>
   *  <pre>
   *   +-------------+--------------------------------+
   *   | Operator    | Replacement                    |
   *   +=============+================================+
   *   | $where      |  $expr                         |
   *   +-------------+--------------------------------+
   *   | $near       |  $geoWithin with $center       |
   *   +-------------+--------------------------------+
   *   | $nearSphere |  $geoWithin with $centerSphere |
   *   +-------------+--------------------------------+
   *  </pre>
   *  @param clientSession the client session with which to associate this operation
   *  @param resultHandler an async result with a single element indicating the number of documents
   *  @return <code>this</code>
   *  @mongodb.server.release 3.6
   *  @since 1.9
   */
  MongoCollection<TDocument> countDocuments(ClientSession clientSession,
      Handler<AsyncResult<Long>> resultHandler);

  /**
   *  Counts the number of documents in the collection according to the given options.
   *  <p>
   *  Note: For a fast count of the total documents in a collection see {@link #estimatedDocumentCount()}.
   *  </p>
   *  @param clientSession the client session with which to associate this operation
   *  @param filter the query filter
   *  @return a future with a single element indicating the number of documents
   *  @mongodb.server.release 3.6
   *  @since 1.9
   */
  Future<Long> countDocuments(ClientSession clientSession, JsonObject filter);

  /**
   *  Counts the number of documents in the collection according to the given options.
   *  <p>
   *  Note: For a fast count of the total documents in a collection see {@link #estimatedDocumentCount()}.
   *  </p>
   *  @param clientSession the client session with which to associate this operation
   *  @param filter the query filter
   *  @param resultHandler an async result with a single element indicating the number of documents
   *  @return <code>this</code>
   *  @mongodb.server.release 3.6
   *  @since 1.9
   */
  MongoCollection<TDocument> countDocuments(ClientSession clientSession, JsonObject filter,
      Handler<AsyncResult<Long>> resultHandler);

  /**
   *  Counts the number of documents in the collection according to the given options.
   *  <p>
   *  Note: For a fast count of the total documents in a collection see {@link #estimatedDocumentCount()}.<br>
   *  Note: When migrating from {@code count()} to {@code countDocuments()} the following query operators must be replaced:
   *  </p>
   *  <pre>
   *   +-------------+--------------------------------+
   *   | Operator    | Replacement                    |
   *   +=============+================================+
   *   | $where      |  $expr                         |
   *   +-------------+--------------------------------+
   *   | $near       |  $geoWithin with $center       |
   *   +-------------+--------------------------------+
   *   | $nearSphere |  $geoWithin with $centerSphere |
   *   +-------------+--------------------------------+
   *  </pre>
   *  @param clientSession the client session with which to associate this operation
   *  @param filter  the query filter
   *  @param options the options describing the count
   *  @return a future with a single element indicating the number of documents
   *  @mongodb.server.release 3.6
   *  @since 1.9
   */
  Future<Long> countDocuments(ClientSession clientSession, JsonObject filter, CountOptions options);

  /**
   *  Counts the number of documents in the collection according to the given options.
   *  <p>
   *  Note: For a fast count of the total documents in a collection see {@link #estimatedDocumentCount()}.<br>
   *  Note: When migrating from {@code count()} to {@code countDocuments()} the following query operators must be replaced:
   *  </p>
   *  <pre>
   *   +-------------+--------------------------------+
   *   | Operator    | Replacement                    |
   *   +=============+================================+
   *   | $where      |  $expr                         |
   *   +-------------+--------------------------------+
   *   | $near       |  $geoWithin with $center       |
   *   +-------------+--------------------------------+
   *   | $nearSphere |  $geoWithin with $centerSphere |
   *   +-------------+--------------------------------+
   *  </pre>
   *  @param clientSession the client session with which to associate this operation
   *  @param filter  the query filter
   *  @param options the options describing the count
   *  @param resultHandler an async result with a single element indicating the number of documents
   *  @return <code>this</code>
   *  @mongodb.server.release 3.6
   *  @since 1.9
   */
  MongoCollection<TDocument> countDocuments(ClientSession clientSession, JsonObject filter,
      CountOptions options, Handler<AsyncResult<Long>> resultHandler);

  /**
   *  Finds all documents in the collection.
   *  @return the fluent find interface
   *  @mongodb.driver.manual tutorial/query-documents/ Find
   */
  MongoResult<TDocument> find();

  /**
   *  Finds all documents in the collection.
   *  @param options options
   *  @return the fluent find interface
   *  @mongodb.driver.manual tutorial/query-documents/ Find
   */
  MongoResult<TDocument> find(FindOptions options);

  /**
   *  Finds all documents in the collection.
   *  @param filter the query filter
   *  @return the fluent find interface
   *  @mongodb.driver.manual tutorial/query-documents/ Find
   */
  MongoResult<TDocument> find(JsonObject filter);

  /**
   *  Finds all documents in the collection.
   *  @param filter the query filter
   *  @param options options
   *  @return the fluent find interface
   *  @mongodb.driver.manual tutorial/query-documents/ Find
   */
  MongoResult<TDocument> find(JsonObject filter, FindOptions options);

  /**
   *  Finds all documents in the collection.
   *  @param clientSession the client session with which to associate this operation
   *  @return the fluent find interface
   *  @mongodb.driver.manual tutorial/query-documents/ Find
   *  @mongodb.server.release 3.6
   *  @since 1.7
   */
  MongoResult<TDocument> find(ClientSession clientSession);

  /**
   *  Finds all documents in the collection.
   *  @param clientSession the client session with which to associate this operation
   *  @param options options
   *  @return the fluent find interface
   *  @mongodb.driver.manual tutorial/query-documents/ Find
   *  @mongodb.server.release 3.6
   *  @since 1.7
   */
  MongoResult<TDocument> find(ClientSession clientSession, FindOptions options);

  /**
   *  Finds all documents in the collection.
   *  @param clientSession the client session with which to associate this operation
   *  @param filter the query filter
   *  @return the fluent find interface
   *  @mongodb.driver.manual tutorial/query-documents/ Find
   *  @mongodb.server.release 3.6
   *  @since 1.7
   */
  MongoResult<TDocument> find(ClientSession clientSession, JsonObject filter);

  /**
   *  Finds all documents in the collection.
   *  @param clientSession the client session with which to associate this operation
   *  @param filter the query filter
   *  @param options options
   *  @return the fluent find interface
   *  @mongodb.driver.manual tutorial/query-documents/ Find
   *  @mongodb.server.release 3.6
   *  @since 1.7
   */
  MongoResult<TDocument> find(ClientSession clientSession, JsonObject filter, FindOptions options);

  /**
   *  Aggregates documents according to the specified aggregation pipeline.
   *  @param pipeline the aggregate pipeline
   *  @return a result containing the result of the aggregation operation
   *  @mongodb.driver.manual aggregation/ Aggregation
   */
  MongoResult<TDocument> aggregate(List<JsonObject> pipeline);

  /**
   *  Aggregates documents according to the specified aggregation pipeline.
   *  @param pipeline the aggregate pipeline
   *  @param options options
   *  @return a result containing the result of the aggregation operation
   *  @mongodb.driver.manual aggregation/ Aggregation
   */
  MongoResult<TDocument> aggregate(List<JsonObject> pipeline, AggregateOptions options);

  /**
   *  Aggregates documents according to the specified aggregation pipeline.
   *  @param clientSession the client session with which to associate this operation
   *  @param pipeline the aggregate pipeline
   *  @return a result containing the result of the aggregation operation
   *  @mongodb.driver.manual aggregation/ Aggregation
   *  @mongodb.server.release 3.6
   *  @since 1.7
   */
  MongoResult<TDocument> aggregate(ClientSession clientSession, List<JsonObject> pipeline);

  /**
   *  Aggregates documents according to the specified aggregation pipeline.
   *  @param clientSession the client session with which to associate this operation
   *  @param pipeline the aggregate pipeline
   *  @param options options
   *  @return a result containing the result of the aggregation operation
   *  @mongodb.driver.manual aggregation/ Aggregation
   *  @mongodb.server.release 3.6
   *  @since 1.7
   */
  MongoResult<TDocument> aggregate(ClientSession clientSession, List<JsonObject> pipeline,
      AggregateOptions options);

  /**
   *  Creates a change stream for this collection.
   *  @return the change stream read stream
   *  @mongodb.driver.manual reference/operator/aggregation/changeStream $changeStream
   *  @mongodb.server.release 3.6
   *  @since 1.7
   */
  ReadStream<JsonObject> watch();

  /**
   *  Creates a change stream for this collection.
   *  @param options options
   *  @return the change stream read stream
   *  @mongodb.driver.manual reference/operator/aggregation/changeStream $changeStream
   *  @mongodb.server.release 3.6
   *  @since 1.7
   */
  ReadStream<JsonObject> watch(ChangeStreamOptions options);

  /**
   *  Creates a change stream for this collection.
   *  @param pipeline the aggregation pipeline to apply to the change stream
   *  @return the change stream read stream
   *  @mongodb.driver.manual reference/operator/aggregation/changeStream $changeStream
   *  @since 1.6
   */
  ReadStream<JsonObject> watch(List<JsonObject> pipeline);

  /**
   *  Creates a change stream for this collection.
   *  @param pipeline the aggregation pipeline to apply to the change stream
   *  @param options options
   *  @return the change stream read stream
   *  @mongodb.driver.manual reference/operator/aggregation/changeStream $changeStream
   *  @since 1.6
   */
  ReadStream<JsonObject> watch(List<JsonObject> pipeline, ChangeStreamOptions options);

  /**
   *  Creates a change stream for this collection.
   *  @param clientSession the client session with which to associate this operation
   *  @return the change stream read stream
   *  @mongodb.driver.manual reference/operator/aggregation/changeStream $changeStream
   *  @mongodb.server.release 3.6
   *  @since 1.7
   */
  ReadStream<JsonObject> watch(ClientSession clientSession);

  /**
   *  Creates a change stream for this collection.
   *  @param clientSession the client session with which to associate this operation
   *  @param options options
   *  @return the change stream read stream
   *  @mongodb.driver.manual reference/operator/aggregation/changeStream $changeStream
   *  @mongodb.server.release 3.6
   *  @since 1.7
   */
  ReadStream<JsonObject> watch(ClientSession clientSession, ChangeStreamOptions options);

  /**
   *  Creates a change stream for this collection.
   *  @param clientSession the client session with which to associate this operation
   *  @param pipeline the aggregation pipeline to apply to the change stream
   *  @return the change stream read stream
   *  @mongodb.driver.manual reference/operator/aggregation/changeStream $changeStream
   *  @mongodb.server.release 3.6
   *  @since 1.7
   */
  ReadStream<JsonObject> watch(ClientSession clientSession, List<JsonObject> pipeline);

  /**
   *  Creates a change stream for this collection.
   *  @param clientSession the client session with which to associate this operation
   *  @param pipeline the aggregation pipeline to apply to the change stream
   *  @param options options
   *  @return the change stream read stream
   *  @mongodb.driver.manual reference/operator/aggregation/changeStream $changeStream
   *  @mongodb.server.release 3.6
   *  @since 1.7
   */
  ReadStream<JsonObject> watch(ClientSession clientSession, List<JsonObject> pipeline,
      ChangeStreamOptions options);

  /**
   *  Aggregates documents according to the specified map-reduce function.
   *  @param mapFunction    A JavaScript function that associates or "maps" a value with a key and emits the key and value pair.
   *  @param reduceFunction A JavaScript function that "reduces" to a single object all the values associated with a particular key.
   *  @return an result containing the result of the map-reduce operation
   *  @mongodb.driver.manual reference/command/mapReduce/ map-reduce
   */
  MongoResult<TDocument> mapReduce(String mapFunction, String reduceFunction);

  /**
   *  Aggregates documents according to the specified map-reduce function.
   *  @param mapFunction    A JavaScript function that associates or "maps" a value with a key and emits the key and value pair.
   *  @param reduceFunction A JavaScript function that "reduces" to a single object all the values associated with a particular key.
   *  @param options options
   *  @return an result containing the result of the map-reduce operation
   *  @mongodb.driver.manual reference/command/mapReduce/ map-reduce
   */
  MongoResult<TDocument> mapReduce(String mapFunction, String reduceFunction,
      MapReduceOptions options);

  /**
   *  Aggregates documents according to the specified map-reduce function.
   *  @param clientSession the client session with which to associate this operation
   *  @param mapFunction    A JavaScript function that associates or "maps" a value with a key and emits the key and value pair.
   *  @param reduceFunction A JavaScript function that "reduces" to a single object all the values associated with a particular key.
   *  @return an result containing the result of the map-reduce operation
   *  @mongodb.driver.manual reference/command/mapReduce/ map-reduce
   *  @mongodb.server.release 3.6
   *  @since 1.7
   */
  MongoResult<TDocument> mapReduce(ClientSession clientSession, String mapFunction,
      String reduceFunction);

  /**
   *  Aggregates documents according to the specified map-reduce function.
   *  @param clientSession the client session with which to associate this operation
   *  @param mapFunction    A JavaScript function that associates or "maps" a value with a key and emits the key and value pair.
   *  @param reduceFunction A JavaScript function that "reduces" to a single object all the values associated with a particular key.
   *  @param options options
   *  @return an result containing the result of the map-reduce operation
   *  @mongodb.driver.manual reference/command/mapReduce/ map-reduce
   *  @mongodb.server.release 3.6
   *  @since 1.7
   */
  MongoResult<TDocument> mapReduce(ClientSession clientSession, String mapFunction,
      String reduceFunction, MapReduceOptions options);

  /**
   *  Executes a mix of inserts, updates, replaces, and deletes.
   *  @param requests the writes to execute
   *  @return a future with a single element the BulkWriteResult
   */
  Future<BulkWriteResult> bulkWrite(List<JsonObject> requests);

  /**
   *  Executes a mix of inserts, updates, replaces, and deletes.
   *  @param requests the writes to execute
   *  @param resultHandler an async result with a single element the BulkWriteResult
   *  @return <code>this</code>
   */
  MongoCollection<TDocument> bulkWrite(List<JsonObject> requests,
      Handler<AsyncResult<BulkWriteResult>> resultHandler);

  /**
   *  Executes a mix of inserts, updates, replaces, and deletes.
   *  @param requests the writes to execute
   *  @param options  the options to apply to the bulk write operation
   *  @return a future with a single element the BulkWriteResult
   */
  Future<BulkWriteResult> bulkWrite(List<JsonObject> requests, BulkWriteOptions options);

  /**
   *  Executes a mix of inserts, updates, replaces, and deletes.
   *  @param requests the writes to execute
   *  @param options  the options to apply to the bulk write operation
   *  @param resultHandler an async result with a single element the BulkWriteResult
   *  @return <code>this</code>
   */
  MongoCollection<TDocument> bulkWrite(List<JsonObject> requests, BulkWriteOptions options,
      Handler<AsyncResult<BulkWriteResult>> resultHandler);

  /**
   *  Executes a mix of inserts, updates, replaces, and deletes.
   *  @param clientSession the client session with which to associate this operation
   *  @param requests the writes to execute
   *  @return a future with a single element the BulkWriteResult
   *  @mongodb.server.release 3.6
   *  @since 1.7
   */
  Future<BulkWriteResult> bulkWrite(ClientSession clientSession, List<JsonObject> requests);

  /**
   *  Executes a mix of inserts, updates, replaces, and deletes.
   *  @param clientSession the client session with which to associate this operation
   *  @param requests the writes to execute
   *  @param resultHandler an async result with a single element the BulkWriteResult
   *  @return <code>this</code>
   *  @mongodb.server.release 3.6
   *  @since 1.7
   */
  MongoCollection<TDocument> bulkWrite(ClientSession clientSession, List<JsonObject> requests,
      Handler<AsyncResult<BulkWriteResult>> resultHandler);

  /**
   *  Executes a mix of inserts, updates, replaces, and deletes.
   *  @param clientSession the client session with which to associate this operation
   *  @param requests the writes to execute
   *  @param options  the options to apply to the bulk write operation
   *  @return a future with a single element the BulkWriteResult
   *  @mongodb.server.release 3.6
   *  @since 1.7
   */
  Future<BulkWriteResult> bulkWrite(ClientSession clientSession, List<JsonObject> requests,
      BulkWriteOptions options);

  /**
   *  Executes a mix of inserts, updates, replaces, and deletes.
   *  @param clientSession the client session with which to associate this operation
   *  @param requests the writes to execute
   *  @param options  the options to apply to the bulk write operation
   *  @param resultHandler an async result with a single element the BulkWriteResult
   *  @return <code>this</code>
   *  @mongodb.server.release 3.6
   *  @since 1.7
   */
  MongoCollection<TDocument> bulkWrite(ClientSession clientSession, List<JsonObject> requests,
      BulkWriteOptions options, Handler<AsyncResult<BulkWriteResult>> resultHandler);

  /**
   *  Inserts the provided document. If the document is missing an identifier, the driver should generate one.
   *  @param document the document to insert
   *  @return a future with a single element with the InsertOneResult or with either a
   *  com.mongodb.DuplicateKeyException or com.mongodb.MongoException
   */
  Future<InsertOneResult> insertOne(TDocument document);

  /**
   *  Inserts the provided document. If the document is missing an identifier, the driver should generate one.
   *  @param document the document to insert
   *  @param resultHandler an async result with a single element with the InsertOneResult or with either a
   *  @return <code>this</code>
   *  com.mongodb.DuplicateKeyException or com.mongodb.MongoException
   */
  MongoCollection<TDocument> insertOne(TDocument document,
      Handler<AsyncResult<InsertOneResult>> resultHandler);

  /**
   *  Inserts the provided document. If the document is missing an identifier, the driver should generate one.
   *  @param document the document to insert
   *  @param options  the options to apply to the operation
   *  @return a future with a single element with the InsertOneResult or with either a
   *  com.mongodb.DuplicateKeyException or com.mongodb.MongoException
   *  @since 1.2
   */
  Future<InsertOneResult> insertOne(TDocument document, InsertOneOptions options);

  /**
   *  Inserts the provided document. If the document is missing an identifier, the driver should generate one.
   *  @param document the document to insert
   *  @param options  the options to apply to the operation
   *  @param resultHandler an async result with a single element with the InsertOneResult or with either a
   *  @return <code>this</code>
   *  com.mongodb.DuplicateKeyException or com.mongodb.MongoException
   *  @since 1.2
   */
  MongoCollection<TDocument> insertOne(TDocument document, InsertOneOptions options,
      Handler<AsyncResult<InsertOneResult>> resultHandler);

  /**
   *  Inserts the provided document. If the document is missing an identifier, the driver should generate one.
   *  @param clientSession the client session with which to associate this operation
   *  @param document the document to insert
   *  @return a future with a single element with the InsertOneResult or with either a
   *  com.mongodb.DuplicateKeyException or com.mongodb.MongoException
   *  @mongodb.server.release 3.6
   *  @since 1.7
   */
  Future<InsertOneResult> insertOne(ClientSession clientSession, TDocument document);

  /**
   *  Inserts the provided document. If the document is missing an identifier, the driver should generate one.
   *  @param clientSession the client session with which to associate this operation
   *  @param document the document to insert
   *  @param resultHandler an async result with a single element with the InsertOneResult or with either a
   *  @return <code>this</code>
   *  com.mongodb.DuplicateKeyException or com.mongodb.MongoException
   *  @mongodb.server.release 3.6
   *  @since 1.7
   */
  MongoCollection<TDocument> insertOne(ClientSession clientSession, TDocument document,
      Handler<AsyncResult<InsertOneResult>> resultHandler);

  /**
   *  Inserts the provided document. If the document is missing an identifier, the driver should generate one.
   *  @param clientSession the client session with which to associate this operation
   *  @param document the document to insert
   *  @param options  the options to apply to the operation
   *  @return a future with a single element with the InsertOneResult or with either a
   *  com.mongodb.DuplicateKeyException or com.mongodb.MongoException
   *  @mongodb.server.release 3.6
   *  @since 1.7
   */
  Future<InsertOneResult> insertOne(ClientSession clientSession, TDocument document,
      InsertOneOptions options);

  /**
   *  Inserts the provided document. If the document is missing an identifier, the driver should generate one.
   *  @param clientSession the client session with which to associate this operation
   *  @param document the document to insert
   *  @param options  the options to apply to the operation
   *  @param resultHandler an async result with a single element with the InsertOneResult or with either a
   *  @return <code>this</code>
   *  com.mongodb.DuplicateKeyException or com.mongodb.MongoException
   *  @mongodb.server.release 3.6
   *  @since 1.7
   */
  MongoCollection<TDocument> insertOne(ClientSession clientSession, TDocument document,
      InsertOneOptions options, Handler<AsyncResult<InsertOneResult>> resultHandler);

  /**
   *  Inserts a batch of documents.
   *  @param documents the documents to insert
   *  @return a future with a single element with the InsertManyResult or with either a
   *  com.mongodb.DuplicateKeyException or com.mongodb.MongoException
   */
  Future<InsertManyResult> insertMany(List<? extends TDocument> documents);

  /**
   *  Inserts a batch of documents.
   *  @param documents the documents to insert
   *  @param resultHandler an async result with a single element with the InsertManyResult or with either a
   *  @return <code>this</code>
   *  com.mongodb.DuplicateKeyException or com.mongodb.MongoException
   */
  MongoCollection<TDocument> insertMany(List<? extends TDocument> documents,
      Handler<AsyncResult<InsertManyResult>> resultHandler);

  /**
   *  Inserts a batch of documents.
   *  @param documents the documents to insert
   *  @param options   the options to apply to the operation
   *  @return a future with a single element with the InsertManyResult or with either a
   *  com.mongodb.DuplicateKeyException or com.mongodb.MongoException
   */
  Future<InsertManyResult> insertMany(List<? extends TDocument> documents,
      InsertManyOptions options);

  /**
   *  Inserts a batch of documents.
   *  @param documents the documents to insert
   *  @param options   the options to apply to the operation
   *  @param resultHandler an async result with a single element with the InsertManyResult or with either a
   *  @return <code>this</code>
   *  com.mongodb.DuplicateKeyException or com.mongodb.MongoException
   */
  MongoCollection<TDocument> insertMany(List<? extends TDocument> documents,
      InsertManyOptions options, Handler<AsyncResult<InsertManyResult>> resultHandler);

  /**
   *  Inserts a batch of documents.
   *  @param clientSession the client session with which to associate this operation
   *  @param documents the documents to insert
   *  @return a future with a single element with the InsertManyResult or with either a
   *  com.mongodb.DuplicateKeyException or com.mongodb.MongoException
   *  @mongodb.server.release 3.6
   *  @since 1.7
   */
  Future<InsertManyResult> insertMany(ClientSession clientSession,
      List<? extends TDocument> documents);

  /**
   *  Inserts a batch of documents.
   *  @param clientSession the client session with which to associate this operation
   *  @param documents the documents to insert
   *  @param resultHandler an async result with a single element with the InsertManyResult or with either a
   *  @return <code>this</code>
   *  com.mongodb.DuplicateKeyException or com.mongodb.MongoException
   *  @mongodb.server.release 3.6
   *  @since 1.7
   */
  MongoCollection<TDocument> insertMany(ClientSession clientSession,
      List<? extends TDocument> documents, Handler<AsyncResult<InsertManyResult>> resultHandler);

  /**
   *  Inserts a batch of documents.
   *  @param clientSession the client session with which to associate this operation
   *  @param documents the documents to insert
   *  @param options   the options to apply to the operation
   *  @return a future with a single element with the InsertManyResult or with either a
   *  com.mongodb.DuplicateKeyException or com.mongodb.MongoException
   *  @mongodb.server.release 3.6
   *  @since 1.7
   */
  Future<InsertManyResult> insertMany(ClientSession clientSession,
      List<? extends TDocument> documents, InsertManyOptions options);

  /**
   *  Inserts a batch of documents.
   *  @param clientSession the client session with which to associate this operation
   *  @param documents the documents to insert
   *  @param options   the options to apply to the operation
   *  @param resultHandler an async result with a single element with the InsertManyResult or with either a
   *  @return <code>this</code>
   *  com.mongodb.DuplicateKeyException or com.mongodb.MongoException
   *  @mongodb.server.release 3.6
   *  @since 1.7
   */
  MongoCollection<TDocument> insertMany(ClientSession clientSession,
      List<? extends TDocument> documents, InsertManyOptions options,
      Handler<AsyncResult<InsertManyResult>> resultHandler);

  /**
   *  Removes at most one document from the collection that matches the given filter.  If no documents match, the collection is not
   *  modified.
   *  @param filter the query filter to apply the the delete operation
   *  @return a future with a single element the DeleteResult or with an com.mongodb.MongoException
   */
  Future<DeleteResult> deleteOne(JsonObject filter);

  /**
   *  Removes at most one document from the collection that matches the given filter.  If no documents match, the collection is not
   *  modified.
   *  @param filter the query filter to apply the the delete operation
   *  @param resultHandler an async result with a single element the DeleteResult or with an com.mongodb.MongoException
   *  @return <code>this</code>
   */
  MongoCollection<TDocument> deleteOne(JsonObject filter,
      Handler<AsyncResult<DeleteResult>> resultHandler);

  /**
   *  Removes at most one document from the collection that matches the given filter.  If no documents match, the collection is not
   *  modified.
   *  @param filter the query filter to apply the the delete operation
   *  @param options the options to apply to the delete operation
   *  @return a future with a single element the DeleteResult or with an com.mongodb.MongoException
   *  @since 1.5
   */
  Future<DeleteResult> deleteOne(JsonObject filter, DeleteOptions options);

  /**
   *  Removes at most one document from the collection that matches the given filter.  If no documents match, the collection is not
   *  modified.
   *  @param filter the query filter to apply the the delete operation
   *  @param options the options to apply to the delete operation
   *  @param resultHandler an async result with a single element the DeleteResult or with an com.mongodb.MongoException
   *  @return <code>this</code>
   *  @since 1.5
   */
  MongoCollection<TDocument> deleteOne(JsonObject filter, DeleteOptions options,
      Handler<AsyncResult<DeleteResult>> resultHandler);

  /**
   *  Removes at most one document from the collection that matches the given filter.  If no documents match, the collection is not
   *  modified.
   *  @param clientSession the client session with which to associate this operation
   *  @param filter the query filter to apply the the delete operation
   *  @return a future with a single element the DeleteResult or with an com.mongodb.MongoException
   *  @mongodb.server.release 3.6
   *  @since 1.7
   */
  Future<DeleteResult> deleteOne(ClientSession clientSession, JsonObject filter);

  /**
   *  Removes at most one document from the collection that matches the given filter.  If no documents match, the collection is not
   *  modified.
   *  @param clientSession the client session with which to associate this operation
   *  @param filter the query filter to apply the the delete operation
   *  @param resultHandler an async result with a single element the DeleteResult or with an com.mongodb.MongoException
   *  @return <code>this</code>
   *  @mongodb.server.release 3.6
   *  @since 1.7
   */
  MongoCollection<TDocument> deleteOne(ClientSession clientSession, JsonObject filter,
      Handler<AsyncResult<DeleteResult>> resultHandler);

  /**
   *  Removes at most one document from the collection that matches the given filter.  If no documents match, the collection is not
   *  modified.
   *  @param clientSession the client session with which to associate this operation
   *  @param filter the query filter to apply the the delete operation
   *  @param options the options to apply to the delete operation
   *  @return a future with a single element the DeleteResult or with an com.mongodb.MongoException
   *  @mongodb.server.release 3.6
   *  @since 1.7
   */
  Future<DeleteResult> deleteOne(ClientSession clientSession, JsonObject filter,
      DeleteOptions options);

  /**
   *  Removes at most one document from the collection that matches the given filter.  If no documents match, the collection is not
   *  modified.
   *  @param clientSession the client session with which to associate this operation
   *  @param filter the query filter to apply the the delete operation
   *  @param options the options to apply to the delete operation
   *  @param resultHandler an async result with a single element the DeleteResult or with an com.mongodb.MongoException
   *  @return <code>this</code>
   *  @mongodb.server.release 3.6
   *  @since 1.7
   */
  MongoCollection<TDocument> deleteOne(ClientSession clientSession, JsonObject filter,
      DeleteOptions options, Handler<AsyncResult<DeleteResult>> resultHandler);

  /**
   *  Removes all documents from the collection that match the given query filter.  If no documents match, the collection is not modified.
   *  @param filter the query filter to apply the the delete operation
   *  @return a future with a single element the DeleteResult or with an com.mongodb.MongoException
   */
  Future<DeleteResult> deleteMany(JsonObject filter);

  /**
   *  Removes all documents from the collection that match the given query filter.  If no documents match, the collection is not modified.
   *  @param filter the query filter to apply the the delete operation
   *  @param resultHandler an async result with a single element the DeleteResult or with an com.mongodb.MongoException
   *  @return <code>this</code>
   */
  MongoCollection<TDocument> deleteMany(JsonObject filter,
      Handler<AsyncResult<DeleteResult>> resultHandler);

  /**
   *  Removes all documents from the collection that match the given query filter.  If no documents match, the collection is not modified.
   *  @param filter the query filter to apply the the delete operation
   *  @param options the options to apply to the delete operation
   *  @return a future with a single element the DeleteResult or with an com.mongodb.MongoException
   *  @since 1.5
   */
  Future<DeleteResult> deleteMany(JsonObject filter, DeleteOptions options);

  /**
   *  Removes all documents from the collection that match the given query filter.  If no documents match, the collection is not modified.
   *  @param filter the query filter to apply the the delete operation
   *  @param options the options to apply to the delete operation
   *  @param resultHandler an async result with a single element the DeleteResult or with an com.mongodb.MongoException
   *  @return <code>this</code>
   *  @since 1.5
   */
  MongoCollection<TDocument> deleteMany(JsonObject filter, DeleteOptions options,
      Handler<AsyncResult<DeleteResult>> resultHandler);

  /**
   *  Removes all documents from the collection that match the given query filter.  If no documents match, the collection is not modified.
   *  @param clientSession the client session with which to associate this operation
   *  @param filter the query filter to apply the the delete operation
   *  @return a future with a single element the DeleteResult or with an com.mongodb.MongoException
   *  @mongodb.server.release 3.6
   *  @since 1.7
   */
  Future<DeleteResult> deleteMany(ClientSession clientSession, JsonObject filter);

  /**
   *  Removes all documents from the collection that match the given query filter.  If no documents match, the collection is not modified.
   *  @param clientSession the client session with which to associate this operation
   *  @param filter the query filter to apply the the delete operation
   *  @param resultHandler an async result with a single element the DeleteResult or with an com.mongodb.MongoException
   *  @return <code>this</code>
   *  @mongodb.server.release 3.6
   *  @since 1.7
   */
  MongoCollection<TDocument> deleteMany(ClientSession clientSession, JsonObject filter,
      Handler<AsyncResult<DeleteResult>> resultHandler);

  /**
   *  Removes all documents from the collection that match the given query filter.  If no documents match, the collection is not modified.
   *  @param clientSession the client session with which to associate this operation
   *  @param filter the query filter to apply the the delete operation
   *  @param options the options to apply to the delete operation
   *  @return a future with a single element the DeleteResult or with an com.mongodb.MongoException
   *  @mongodb.server.release 3.6
   *  @since 1.7
   */
  Future<DeleteResult> deleteMany(ClientSession clientSession, JsonObject filter,
      DeleteOptions options);

  /**
   *  Removes all documents from the collection that match the given query filter.  If no documents match, the collection is not modified.
   *  @param clientSession the client session with which to associate this operation
   *  @param filter the query filter to apply the the delete operation
   *  @param options the options to apply to the delete operation
   *  @param resultHandler an async result with a single element the DeleteResult or with an com.mongodb.MongoException
   *  @return <code>this</code>
   *  @mongodb.server.release 3.6
   *  @since 1.7
   */
  MongoCollection<TDocument> deleteMany(ClientSession clientSession, JsonObject filter,
      DeleteOptions options, Handler<AsyncResult<DeleteResult>> resultHandler);

  /**
   *  Replace a document in the collection according to the specified arguments.
   *  @param filter      the query filter to apply the the replace operation
   *  @param replacement the replacement document
   *  @return a future with a single element the UpdateResult
   *  @mongodb.driver.manual tutorial/modify-documents/#replace-the-document Replace
   */
  Future<UpdateResult> replaceOne(JsonObject filter, TDocument replacement);

  /**
   *  Replace a document in the collection according to the specified arguments.
   *  @param filter      the query filter to apply the the replace operation
   *  @param replacement the replacement document
   *  @param resultHandler an async result with a single element the UpdateResult
   *  @return <code>this</code>
   *  @mongodb.driver.manual tutorial/modify-documents/#replace-the-document Replace
   */
  MongoCollection<TDocument> replaceOne(JsonObject filter, TDocument replacement,
      Handler<AsyncResult<UpdateResult>> resultHandler);

  /**
   *  Replace a document in the collection according to the specified arguments.
   *  @param filter      the query filter to apply the the replace operation
   *  @param replacement the replacement document
   *  @param options     the options to apply to the replace operation
   *  @return a future with a single element the UpdateResult
   *  @mongodb.driver.manual tutorial/modify-documents/#replace-the-document Replace
   *  @since 1.8
   */
  Future<UpdateResult> replaceOne(JsonObject filter, TDocument replacement, ReplaceOptions options);

  /**
   *  Replace a document in the collection according to the specified arguments.
   *  @param filter      the query filter to apply the the replace operation
   *  @param replacement the replacement document
   *  @param options     the options to apply to the replace operation
   *  @param resultHandler an async result with a single element the UpdateResult
   *  @return <code>this</code>
   *  @mongodb.driver.manual tutorial/modify-documents/#replace-the-document Replace
   *  @since 1.8
   */
  MongoCollection<TDocument> replaceOne(JsonObject filter, TDocument replacement,
      ReplaceOptions options, Handler<AsyncResult<UpdateResult>> resultHandler);

  /**
   *  Replace a document in the collection according to the specified arguments.
   *  @param clientSession the client session with which to associate this operation
   *  @param filter      the query filter to apply the the replace operation
   *  @param replacement the replacement document
   *  @return a future with a single element the UpdateResult
   *  @mongodb.driver.manual tutorial/modify-documents/#replace-the-document Replace
   *  @mongodb.server.release 3.6
   *  @since 1.7
   */
  Future<UpdateResult> replaceOne(ClientSession clientSession, JsonObject filter,
      TDocument replacement);

  /**
   *  Replace a document in the collection according to the specified arguments.
   *  @param clientSession the client session with which to associate this operation
   *  @param filter      the query filter to apply the the replace operation
   *  @param replacement the replacement document
   *  @param resultHandler an async result with a single element the UpdateResult
   *  @return <code>this</code>
   *  @mongodb.driver.manual tutorial/modify-documents/#replace-the-document Replace
   *  @mongodb.server.release 3.6
   *  @since 1.7
   */
  MongoCollection<TDocument> replaceOne(ClientSession clientSession, JsonObject filter,
      TDocument replacement, Handler<AsyncResult<UpdateResult>> resultHandler);

  /**
   *  Replace a document in the collection according to the specified arguments.
   *  @param clientSession the client session with which to associate this operation
   *  @param filter      the query filter to apply the the replace operation
   *  @param replacement the replacement document
   *  @param options     the options to apply to the replace operation
   *  @return a future with a single element the UpdateResult
   *  @mongodb.driver.manual tutorial/modify-documents/#replace-the-document Replace
   *  @mongodb.server.release 3.6
   *  @since 1.8
   */
  Future<UpdateResult> replaceOne(ClientSession clientSession, JsonObject filter,
      TDocument replacement, ReplaceOptions options);

  /**
   *  Replace a document in the collection according to the specified arguments.
   *  @param clientSession the client session with which to associate this operation
   *  @param filter      the query filter to apply the the replace operation
   *  @param replacement the replacement document
   *  @param options     the options to apply to the replace operation
   *  @param resultHandler an async result with a single element the UpdateResult
   *  @return <code>this</code>
   *  @mongodb.driver.manual tutorial/modify-documents/#replace-the-document Replace
   *  @mongodb.server.release 3.6
   *  @since 1.8
   */
  MongoCollection<TDocument> replaceOne(ClientSession clientSession, JsonObject filter,
      TDocument replacement, ReplaceOptions options,
      Handler<AsyncResult<UpdateResult>> resultHandler);

  /**
   *  Update a single document in the collection according to the specified arguments.
   *  @param filter a document describing the query filter, which may not be null.
   *  @param update a document describing the update, which may not be null. The update to apply must include only update operators.
   *  @return a future with a single element the UpdateResult
   *  @mongodb.driver.manual tutorial/modify-documents/ Updates
   *  @mongodb.driver.manual reference/operator/update/ Update Operators
   */
  Future<UpdateResult> updateOne(JsonObject filter, JsonObject update);

  /**
   *  Update a single document in the collection according to the specified arguments.
   *  @param filter a document describing the query filter, which may not be null.
   *  @param update a document describing the update, which may not be null. The update to apply must include only update operators.
   *  @param resultHandler an async result with a single element the UpdateResult
   *  @return <code>this</code>
   *  @mongodb.driver.manual tutorial/modify-documents/ Updates
   *  @mongodb.driver.manual reference/operator/update/ Update Operators
   */
  MongoCollection<TDocument> updateOne(JsonObject filter, JsonObject update,
      Handler<AsyncResult<UpdateResult>> resultHandler);

  /**
   *  Update a single document in the collection according to the specified arguments.
   *  @param filter  a document describing the query filter, which may not be null.
   *  @param update  a document describing the update, which may not be null. The update to apply must include only update operators.
   *  @param options the options to apply to the update operation
   *  @return a future with a single element the UpdateResult
   *  @mongodb.driver.manual tutorial/modify-documents/ Updates
   *  @mongodb.driver.manual reference/operator/update/ Update Operators
   */
  Future<UpdateResult> updateOne(JsonObject filter, JsonObject update, UpdateOptions options);

  /**
   *  Update a single document in the collection according to the specified arguments.
   *  @param filter  a document describing the query filter, which may not be null.
   *  @param update  a document describing the update, which may not be null. The update to apply must include only update operators.
   *  @param options the options to apply to the update operation
   *  @param resultHandler an async result with a single element the UpdateResult
   *  @return <code>this</code>
   *  @mongodb.driver.manual tutorial/modify-documents/ Updates
   *  @mongodb.driver.manual reference/operator/update/ Update Operators
   */
  MongoCollection<TDocument> updateOne(JsonObject filter, JsonObject update, UpdateOptions options,
      Handler<AsyncResult<UpdateResult>> resultHandler);

  /**
   *  Update a single document in the collection according to the specified arguments.
   *  @param clientSession the client session with which to associate this operation
   *  @param filter a document describing the query filter, which may not be null.
   *  @param update a document describing the update, which may not be null. The update to apply must include only update operators.
   *  @return a future with a single element the UpdateResult
   *  @mongodb.driver.manual tutorial/modify-documents/ Updates
   *  @mongodb.driver.manual reference/operator/update/ Update Operators
   *  @mongodb.server.release 3.6
   *  @since 1.7
   */
  Future<UpdateResult> updateOne(ClientSession clientSession, JsonObject filter, JsonObject update);

  /**
   *  Update a single document in the collection according to the specified arguments.
   *  @param clientSession the client session with which to associate this operation
   *  @param filter a document describing the query filter, which may not be null.
   *  @param update a document describing the update, which may not be null. The update to apply must include only update operators.
   *  @param resultHandler an async result with a single element the UpdateResult
   *  @return <code>this</code>
   *  @mongodb.driver.manual tutorial/modify-documents/ Updates
   *  @mongodb.driver.manual reference/operator/update/ Update Operators
   *  @mongodb.server.release 3.6
   *  @since 1.7
   */
  MongoCollection<TDocument> updateOne(ClientSession clientSession, JsonObject filter,
      JsonObject update, Handler<AsyncResult<UpdateResult>> resultHandler);

  /**
   *  Update a single document in the collection according to the specified arguments.
   *  @param clientSession the client session with which to associate this operation
   *  @param filter  a document describing the query filter, which may not be null.
   *  @param update  a document describing the update, which may not be null. The update to apply must include only update operators.
   *  @param options the options to apply to the update operation
   *  @return a future with a single element the UpdateResult
   *  @mongodb.driver.manual tutorial/modify-documents/ Updates
   *  @mongodb.driver.manual reference/operator/update/ Update Operators
   *  @mongodb.server.release 3.6
   *  @since 1.7
   */
  Future<UpdateResult> updateOne(ClientSession clientSession, JsonObject filter, JsonObject update,
      UpdateOptions options);

  /**
   *  Update a single document in the collection according to the specified arguments.
   *  @param clientSession the client session with which to associate this operation
   *  @param filter  a document describing the query filter, which may not be null.
   *  @param update  a document describing the update, which may not be null. The update to apply must include only update operators.
   *  @param options the options to apply to the update operation
   *  @param resultHandler an async result with a single element the UpdateResult
   *  @return <code>this</code>
   *  @mongodb.driver.manual tutorial/modify-documents/ Updates
   *  @mongodb.driver.manual reference/operator/update/ Update Operators
   *  @mongodb.server.release 3.6
   *  @since 1.7
   */
  MongoCollection<TDocument> updateOne(ClientSession clientSession, JsonObject filter,
      JsonObject update, UpdateOptions options, Handler<AsyncResult<UpdateResult>> resultHandler);

  /**
   *  Update a single document in the collection according to the specified arguments.
   *  <p>Note: Supports retryable writes on MongoDB server versions 3.6 or higher when the retryWrites setting is enabled.</p>
   *  @param filter a document describing the query filter, which may not be null.
   *  @param update a pipeline describing the update, which may not be null.
   *  @return a future with a single element the UpdateResult
   *  @since 1.12
   *  @mongodb.server.release 4.2
   *  @mongodb.driver.manual tutorial/modify-documents/ Updates
   *  @mongodb.driver.manual reference/operator/update/ Update Operators
   */
  Future<UpdateResult> updateOne(JsonObject filter, List<JsonObject> update);

  /**
   *  Update a single document in the collection according to the specified arguments.
   *  <p>Note: Supports retryable writes on MongoDB server versions 3.6 or higher when the retryWrites setting is enabled.</p>
   *  @param filter a document describing the query filter, which may not be null.
   *  @param update a pipeline describing the update, which may not be null.
   *  @param resultHandler an async result with a single element the UpdateResult
   *  @return <code>this</code>
   *  @since 1.12
   *  @mongodb.server.release 4.2
   *  @mongodb.driver.manual tutorial/modify-documents/ Updates
   *  @mongodb.driver.manual reference/operator/update/ Update Operators
   */
  MongoCollection<TDocument> updateOne(JsonObject filter, List<JsonObject> update,
      Handler<AsyncResult<UpdateResult>> resultHandler);

  /**
   *  Update a single document in the collection according to the specified arguments.
   *  <p>Note: Supports retryable writes on MongoDB server versions 3.6 or higher when the retryWrites setting is enabled.</p>
   *  @param filter        a document describing the query filter, which may not be null.
   *  @param update        a pipeline describing the update, which may not be null.
   *  @param options the options to apply to the update operation
   *  @return a future with a single element the UpdateResult
   *  @since 1.12
   *  @mongodb.server.release 4.2
   *  @mongodb.driver.manual tutorial/modify-documents/ Updates
   *  @mongodb.driver.manual reference/operator/update/ Update Operators
   */
  Future<UpdateResult> updateOne(JsonObject filter, List<JsonObject> update, UpdateOptions options);

  /**
   *  Update a single document in the collection according to the specified arguments.
   *  <p>Note: Supports retryable writes on MongoDB server versions 3.6 or higher when the retryWrites setting is enabled.</p>
   *  @param filter        a document describing the query filter, which may not be null.
   *  @param update        a pipeline describing the update, which may not be null.
   *  @param options the options to apply to the update operation
   *  @param resultHandler an async result with a single element the UpdateResult
   *  @return <code>this</code>
   *  @since 1.12
   *  @mongodb.server.release 4.2
   *  @mongodb.driver.manual tutorial/modify-documents/ Updates
   *  @mongodb.driver.manual reference/operator/update/ Update Operators
   */
  MongoCollection<TDocument> updateOne(JsonObject filter, List<JsonObject> update,
      UpdateOptions options, Handler<AsyncResult<UpdateResult>> resultHandler);

  /**
   *  Update a single document in the collection according to the specified arguments.
   *  <p>Note: Supports retryable writes on MongoDB server versions 3.6 or higher when the retryWrites setting is enabled.</p>
   *  @param clientSession the client session with which to associate this operation
   *  @param filter a document describing the query filter, which may not be null.
   *  @param update a pipeline describing the update, which may not be null.
   *  @return a future with a single element the UpdateResult
   *  @since 1.12
   *  @mongodb.server.release 4.2
   *  @mongodb.driver.manual tutorial/modify-documents/ Updates
   *  @mongodb.driver.manual reference/operator/update/ Update Operators
   */
  Future<UpdateResult> updateOne(ClientSession clientSession, JsonObject filter,
      List<JsonObject> update);

  /**
   *  Update a single document in the collection according to the specified arguments.
   *  <p>Note: Supports retryable writes on MongoDB server versions 3.6 or higher when the retryWrites setting is enabled.</p>
   *  @param clientSession the client session with which to associate this operation
   *  @param filter a document describing the query filter, which may not be null.
   *  @param update a pipeline describing the update, which may not be null.
   *  @param resultHandler an async result with a single element the UpdateResult
   *  @return <code>this</code>
   *  @since 1.12
   *  @mongodb.server.release 4.2
   *  @mongodb.driver.manual tutorial/modify-documents/ Updates
   *  @mongodb.driver.manual reference/operator/update/ Update Operators
   */
  MongoCollection<TDocument> updateOne(ClientSession clientSession, JsonObject filter,
      List<JsonObject> update, Handler<AsyncResult<UpdateResult>> resultHandler);

  /**
   *  Update a single document in the collection according to the specified arguments.
   *  <p>Note: Supports retryable writes on MongoDB server versions 3.6 or higher when the retryWrites setting is enabled.</p>
   *  @param clientSession the client session with which to associate this operation
   *  @param filter        a document describing the query filter, which may not be null.
   *  @param update        a pipeline describing the update, which may not be null.
   *  @param options the options to apply to the update operation
   *  @return a future with a single element the UpdateResult
   *  @since 1.12
   *  @mongodb.server.release 4.2
   *  @mongodb.driver.manual tutorial/modify-documents/ Updates
   *  @mongodb.driver.manual reference/operator/update/ Update Operators
   */
  Future<UpdateResult> updateOne(ClientSession clientSession, JsonObject filter,
      List<JsonObject> update, UpdateOptions options);

  /**
   *  Update a single document in the collection according to the specified arguments.
   *  <p>Note: Supports retryable writes on MongoDB server versions 3.6 or higher when the retryWrites setting is enabled.</p>
   *  @param clientSession the client session with which to associate this operation
   *  @param filter        a document describing the query filter, which may not be null.
   *  @param update        a pipeline describing the update, which may not be null.
   *  @param options the options to apply to the update operation
   *  @param resultHandler an async result with a single element the UpdateResult
   *  @return <code>this</code>
   *  @since 1.12
   *  @mongodb.server.release 4.2
   *  @mongodb.driver.manual tutorial/modify-documents/ Updates
   *  @mongodb.driver.manual reference/operator/update/ Update Operators
   */
  MongoCollection<TDocument> updateOne(ClientSession clientSession, JsonObject filter,
      List<JsonObject> update, UpdateOptions options,
      Handler<AsyncResult<UpdateResult>> resultHandler);

  /**
   *  Update all documents in the collection according to the specified arguments.
   *  @param filter a document describing the query filter, which may not be null.
   *  @param update a document describing the update, which may not be null. The update to apply must include only update operators.
   *  @return a future with a single element the UpdateResult
   *  @mongodb.driver.manual tutorial/modify-documents/ Updates
   *  @mongodb.driver.manual reference/operator/update/ Update Operators
   */
  Future<UpdateResult> updateMany(JsonObject filter, JsonObject update);

  /**
   *  Update all documents in the collection according to the specified arguments.
   *  @param filter a document describing the query filter, which may not be null.
   *  @param update a document describing the update, which may not be null. The update to apply must include only update operators.
   *  @param resultHandler an async result with a single element the UpdateResult
   *  @return <code>this</code>
   *  @mongodb.driver.manual tutorial/modify-documents/ Updates
   *  @mongodb.driver.manual reference/operator/update/ Update Operators
   */
  MongoCollection<TDocument> updateMany(JsonObject filter, JsonObject update,
      Handler<AsyncResult<UpdateResult>> resultHandler);

  /**
   *  Update all documents in the collection according to the specified arguments.
   *  @param filter  a document describing the query filter, which may not be null.
   *  @param update  a document describing the update, which may not be null. The update to apply must include only update operators.
   *  @param options the options to apply to the update operation
   *  @return a future with a single element the UpdateResult
   *  @mongodb.driver.manual tutorial/modify-documents/ Updates
   *  @mongodb.driver.manual reference/operator/update/ Update Operators
   */
  Future<UpdateResult> updateMany(JsonObject filter, JsonObject update, UpdateOptions options);

  /**
   *  Update all documents in the collection according to the specified arguments.
   *  @param filter  a document describing the query filter, which may not be null.
   *  @param update  a document describing the update, which may not be null. The update to apply must include only update operators.
   *  @param options the options to apply to the update operation
   *  @param resultHandler an async result with a single element the UpdateResult
   *  @return <code>this</code>
   *  @mongodb.driver.manual tutorial/modify-documents/ Updates
   *  @mongodb.driver.manual reference/operator/update/ Update Operators
   */
  MongoCollection<TDocument> updateMany(JsonObject filter, JsonObject update, UpdateOptions options,
      Handler<AsyncResult<UpdateResult>> resultHandler);

  /**
   *  Update all documents in the collection according to the specified arguments.
   *  @param clientSession the client session with which to associate this operation
   *  @param filter a document describing the query filter, which may not be null.
   *  @param update a document describing the update, which may not be null. The update to apply must include only update operators.
   *  @return a future with a single element the UpdateResult
   *  @mongodb.driver.manual tutorial/modify-documents/ Updates
   *  @mongodb.driver.manual reference/operator/update/ Update Operators
   *  @mongodb.server.release 3.6
   *  @since 1.7
   */
  Future<UpdateResult> updateMany(ClientSession clientSession, JsonObject filter,
      JsonObject update);

  /**
   *  Update all documents in the collection according to the specified arguments.
   *  @param clientSession the client session with which to associate this operation
   *  @param filter a document describing the query filter, which may not be null.
   *  @param update a document describing the update, which may not be null. The update to apply must include only update operators.
   *  @param resultHandler an async result with a single element the UpdateResult
   *  @return <code>this</code>
   *  @mongodb.driver.manual tutorial/modify-documents/ Updates
   *  @mongodb.driver.manual reference/operator/update/ Update Operators
   *  @mongodb.server.release 3.6
   *  @since 1.7
   */
  MongoCollection<TDocument> updateMany(ClientSession clientSession, JsonObject filter,
      JsonObject update, Handler<AsyncResult<UpdateResult>> resultHandler);

  /**
   *  Update all documents in the collection according to the specified arguments.
   *  @param clientSession the client session with which to associate this operation
   *  @param filter  a document describing the query filter, which may not be null.
   *  @param update  a document describing the update, which may not be null. The update to apply must include only update operators.
   *  @param options the options to apply to the update operation
   *  @return a future with a single element the UpdateResult
   *  @mongodb.driver.manual tutorial/modify-documents/ Updates
   *  @mongodb.driver.manual reference/operator/update/ Update Operators
   *  @mongodb.server.release 3.6
   *  @since 1.7
   */
  Future<UpdateResult> updateMany(ClientSession clientSession, JsonObject filter, JsonObject update,
      UpdateOptions options);

  /**
   *  Update all documents in the collection according to the specified arguments.
   *  @param clientSession the client session with which to associate this operation
   *  @param filter  a document describing the query filter, which may not be null.
   *  @param update  a document describing the update, which may not be null. The update to apply must include only update operators.
   *  @param options the options to apply to the update operation
   *  @param resultHandler an async result with a single element the UpdateResult
   *  @return <code>this</code>
   *  @mongodb.driver.manual tutorial/modify-documents/ Updates
   *  @mongodb.driver.manual reference/operator/update/ Update Operators
   *  @mongodb.server.release 3.6
   *  @since 1.7
   */
  MongoCollection<TDocument> updateMany(ClientSession clientSession, JsonObject filter,
      JsonObject update, UpdateOptions options, Handler<AsyncResult<UpdateResult>> resultHandler);

  /**
   *  Update all documents in the collection according to the specified arguments.
   *  @param filter a document describing the query filter, which may not be null.
   *  @param update a pipeline describing the update, which may not be null.
   *  @return a future with a single element the UpdateResult
   *  @since 1.12
   *  @mongodb.server.release 4.2
   *  @mongodb.driver.manual tutorial/modify-documents/ Updates
   *  @mongodb.driver.manual reference/operator/update/ Update Operators
   */
  Future<UpdateResult> updateMany(JsonObject filter, List<JsonObject> update);

  /**
   *  Update all documents in the collection according to the specified arguments.
   *  @param filter a document describing the query filter, which may not be null.
   *  @param update a pipeline describing the update, which may not be null.
   *  @param resultHandler an async result with a single element the UpdateResult
   *  @return <code>this</code>
   *  @since 1.12
   *  @mongodb.server.release 4.2
   *  @mongodb.driver.manual tutorial/modify-documents/ Updates
   *  @mongodb.driver.manual reference/operator/update/ Update Operators
   */
  MongoCollection<TDocument> updateMany(JsonObject filter, List<JsonObject> update,
      Handler<AsyncResult<UpdateResult>> resultHandler);

  /**
   *  Update all documents in the collection according to the specified arguments.
   *  @param filter        a document describing the query filter, which may not be null.
   *  @param update        a pipeline describing the update, which may not be null.
   *  @param options the options to apply to the update operation
   *  @return a future with a single element the UpdateResult
   *  @since 1.12
   *  @mongodb.server.release 4.2
   *  @mongodb.driver.manual tutorial/modify-documents/ Updates
   *  @mongodb.driver.manual reference/operator/update/ Update Operators
   */
  Future<UpdateResult> updateMany(JsonObject filter, List<JsonObject> update,
      UpdateOptions options);

  /**
   *  Update all documents in the collection according to the specified arguments.
   *  @param filter        a document describing the query filter, which may not be null.
   *  @param update        a pipeline describing the update, which may not be null.
   *  @param options the options to apply to the update operation
   *  @param resultHandler an async result with a single element the UpdateResult
   *  @return <code>this</code>
   *  @since 1.12
   *  @mongodb.server.release 4.2
   *  @mongodb.driver.manual tutorial/modify-documents/ Updates
   *  @mongodb.driver.manual reference/operator/update/ Update Operators
   */
  MongoCollection<TDocument> updateMany(JsonObject filter, List<JsonObject> update,
      UpdateOptions options, Handler<AsyncResult<UpdateResult>> resultHandler);

  /**
   *  Update all documents in the collection according to the specified arguments.
   *  @param clientSession the client session with which to associate this operation
   *  @param filter a document describing the query filter, which may not be null.
   *  @param update a pipeline describing the update, which may not be null.
   *  @return a future with a single element the UpdateResult
   *  @since 1.12
   *  @mongodb.server.release 4.2
   *  @mongodb.driver.manual tutorial/modify-documents/ Updates
   *  @mongodb.driver.manual reference/operator/update/ Update Operators
   */
  Future<UpdateResult> updateMany(ClientSession clientSession, JsonObject filter,
      List<JsonObject> update);

  /**
   *  Update all documents in the collection according to the specified arguments.
   *  @param clientSession the client session with which to associate this operation
   *  @param filter a document describing the query filter, which may not be null.
   *  @param update a pipeline describing the update, which may not be null.
   *  @param resultHandler an async result with a single element the UpdateResult
   *  @return <code>this</code>
   *  @since 1.12
   *  @mongodb.server.release 4.2
   *  @mongodb.driver.manual tutorial/modify-documents/ Updates
   *  @mongodb.driver.manual reference/operator/update/ Update Operators
   */
  MongoCollection<TDocument> updateMany(ClientSession clientSession, JsonObject filter,
      List<JsonObject> update, Handler<AsyncResult<UpdateResult>> resultHandler);

  /**
   *  Update all documents in the collection according to the specified arguments.
   *  @param clientSession the client session with which to associate this operation
   *  @param filter        a document describing the query filter, which may not be null.
   *  @param update        a pipeline describing the update, which may not be null.
   *  @param options the options to apply to the update operation
   *  @return a future with a single element the UpdateResult
   *  @since 1.12
   *  @mongodb.server.release 4.2
   *  @mongodb.driver.manual tutorial/modify-documents/ Updates
   *  @mongodb.driver.manual reference/operator/update/ Update Operators
   */
  Future<UpdateResult> updateMany(ClientSession clientSession, JsonObject filter,
      List<JsonObject> update, UpdateOptions options);

  /**
   *  Update all documents in the collection according to the specified arguments.
   *  @param clientSession the client session with which to associate this operation
   *  @param filter        a document describing the query filter, which may not be null.
   *  @param update        a pipeline describing the update, which may not be null.
   *  @param options the options to apply to the update operation
   *  @param resultHandler an async result with a single element the UpdateResult
   *  @return <code>this</code>
   *  @since 1.12
   *  @mongodb.server.release 4.2
   *  @mongodb.driver.manual tutorial/modify-documents/ Updates
   *  @mongodb.driver.manual reference/operator/update/ Update Operators
   */
  MongoCollection<TDocument> updateMany(ClientSession clientSession, JsonObject filter,
      List<JsonObject> update, UpdateOptions options,
      Handler<AsyncResult<UpdateResult>> resultHandler);

  /**
   *  Atomically find a document and remove it.
   *  @param filter the query filter to find the document with
   *  @return a future with a single element the document that was removed.  If no documents matched the query filter, then null will be
   *  returned
   */
  Future<TDocument> findOneAndDelete(JsonObject filter);

  /**
   *  Atomically find a document and remove it.
   *  @param filter the query filter to find the document with
   *  @param resultHandler an async result with a single element the document that was removed.  If no documents matched the query filter, then null will be
   *  @return <code>this</code>
   *  returned
   */
  MongoCollection<TDocument> findOneAndDelete(JsonObject filter,
      Handler<AsyncResult<TDocument>> resultHandler);

  /**
   *  Atomically find a document and remove it.
   *  @param filter  the query filter to find the document with
   *  @param options the options to apply to the operation
   *  @return a future with a single element the document that was removed.  If no documents matched the query filter, then null will be
   *  returned
   */
  Future<TDocument> findOneAndDelete(JsonObject filter, FindOneAndDeleteOptions options);

  /**
   *  Atomically find a document and remove it.
   *  @param filter  the query filter to find the document with
   *  @param options the options to apply to the operation
   *  @param resultHandler an async result with a single element the document that was removed.  If no documents matched the query filter, then null will be
   *  @return <code>this</code>
   *  returned
   */
  MongoCollection<TDocument> findOneAndDelete(JsonObject filter, FindOneAndDeleteOptions options,
      Handler<AsyncResult<TDocument>> resultHandler);

  /**
   *  Atomically find a document and remove it.
   *  @param clientSession the client session with which to associate this operation
   *  @param filter the query filter to find the document with
   *  @return a future with a single element the document that was removed.  If no documents matched the query filter, then null will be
   *  returned
   *  @mongodb.server.release 3.6
   *  @since 1.7
   */
  Future<TDocument> findOneAndDelete(ClientSession clientSession, JsonObject filter);

  /**
   *  Atomically find a document and remove it.
   *  @param clientSession the client session with which to associate this operation
   *  @param filter the query filter to find the document with
   *  @param resultHandler an async result with a single element the document that was removed.  If no documents matched the query filter, then null will be
   *  @return <code>this</code>
   *  returned
   *  @mongodb.server.release 3.6
   *  @since 1.7
   */
  MongoCollection<TDocument> findOneAndDelete(ClientSession clientSession, JsonObject filter,
      Handler<AsyncResult<TDocument>> resultHandler);

  /**
   *  Atomically find a document and remove it.
   *  @param clientSession the client session with which to associate this operation
   *  @param filter  the query filter to find the document with
   *  @param options the options to apply to the operation
   *  @return a future with a single element the document that was removed.  If no documents matched the query filter, then null will be
   *  returned
   *  @mongodb.server.release 3.6
   *  @since 1.7
   */
  Future<TDocument> findOneAndDelete(ClientSession clientSession, JsonObject filter,
      FindOneAndDeleteOptions options);

  /**
   *  Atomically find a document and remove it.
   *  @param clientSession the client session with which to associate this operation
   *  @param filter  the query filter to find the document with
   *  @param options the options to apply to the operation
   *  @param resultHandler an async result with a single element the document that was removed.  If no documents matched the query filter, then null will be
   *  @return <code>this</code>
   *  returned
   *  @mongodb.server.release 3.6
   *  @since 1.7
   */
  MongoCollection<TDocument> findOneAndDelete(ClientSession clientSession, JsonObject filter,
      FindOneAndDeleteOptions options, Handler<AsyncResult<TDocument>> resultHandler);

  /**
   *  Atomically find a document and replace it.
   *  @param filter      the query filter to apply the the replace operation
   *  @param replacement the replacement document
   *  @return a future with a single element the document that was replaced.  Depending on the value of the {@code returnOriginal}
   *  property, this will either be the document as it was before the update or as it is after the update.  If no documents matched the
   *  query filter, then null will be returned
   */
  Future<TDocument> findOneAndReplace(JsonObject filter, TDocument replacement);

  /**
   *  Atomically find a document and replace it.
   *  @param filter      the query filter to apply the the replace operation
   *  @param replacement the replacement document
   *  @param resultHandler an async result with a single element the document that was replaced.  Depending on the value of the {@code returnOriginal}
   *  @return <code>this</code>
   *  property, this will either be the document as it was before the update or as it is after the update.  If no documents matched the
   *  query filter, then null will be returned
   */
  MongoCollection<TDocument> findOneAndReplace(JsonObject filter, TDocument replacement,
      Handler<AsyncResult<TDocument>> resultHandler);

  /**
   *  Atomically find a document and replace it.
   *  @param filter      the query filter to apply the the replace operation
   *  @param replacement the replacement document
   *  @param options     the options to apply to the operation
   *  @return a future with a single element the document that was replaced.  Depending on the value of the {@code returnOriginal}
   *  property, this will either be the document as it was before the update or as it is after the update.  If no documents matched the
   *  query filter, then null will be returned
   */
  Future<TDocument> findOneAndReplace(JsonObject filter, TDocument replacement,
      FindOneAndReplaceOptions options);

  /**
   *  Atomically find a document and replace it.
   *  @param filter      the query filter to apply the the replace operation
   *  @param replacement the replacement document
   *  @param options     the options to apply to the operation
   *  @param resultHandler an async result with a single element the document that was replaced.  Depending on the value of the {@code returnOriginal}
   *  @return <code>this</code>
   *  property, this will either be the document as it was before the update or as it is after the update.  If no documents matched the
   *  query filter, then null will be returned
   */
  MongoCollection<TDocument> findOneAndReplace(JsonObject filter, TDocument replacement,
      FindOneAndReplaceOptions options, Handler<AsyncResult<TDocument>> resultHandler);

  /**
   *  Atomically find a document and replace it.
   *  @param clientSession the client session with which to associate this operation
   *  @param filter      the query filter to apply the the replace operation
   *  @param replacement the replacement document
   *  @return a future with a single element the document that was replaced.  Depending on the value of the {@code returnOriginal}
   *  property, this will either be the document as it was before the update or as it is after the update.  If no documents matched the
   *  query filter, then null will be returned
   *  @mongodb.server.release 3.6
   *  @since 1.7
   */
  Future<TDocument> findOneAndReplace(ClientSession clientSession, JsonObject filter,
      TDocument replacement);

  /**
   *  Atomically find a document and replace it.
   *  @param clientSession the client session with which to associate this operation
   *  @param filter      the query filter to apply the the replace operation
   *  @param replacement the replacement document
   *  @param resultHandler an async result with a single element the document that was replaced.  Depending on the value of the {@code returnOriginal}
   *  @return <code>this</code>
   *  property, this will either be the document as it was before the update or as it is after the update.  If no documents matched the
   *  query filter, then null will be returned
   *  @mongodb.server.release 3.6
   *  @since 1.7
   */
  MongoCollection<TDocument> findOneAndReplace(ClientSession clientSession, JsonObject filter,
      TDocument replacement, Handler<AsyncResult<TDocument>> resultHandler);

  /**
   *  Atomically find a document and replace it.
   *  @param clientSession the client session with which to associate this operation
   *  @param filter      the query filter to apply the the replace operation
   *  @param replacement the replacement document
   *  @param options     the options to apply to the operation
   *  @return a future with a single element the document that was replaced.  Depending on the value of the {@code returnOriginal}
   *  property, this will either be the document as it was before the update or as it is after the update.  If no documents matched the
   *  query filter, then null will be returned
   *  @mongodb.server.release 3.6
   *  @since 1.7
   */
  Future<TDocument> findOneAndReplace(ClientSession clientSession, JsonObject filter,
      TDocument replacement, FindOneAndReplaceOptions options);

  /**
   *  Atomically find a document and replace it.
   *  @param clientSession the client session with which to associate this operation
   *  @param filter      the query filter to apply the the replace operation
   *  @param replacement the replacement document
   *  @param options     the options to apply to the operation
   *  @param resultHandler an async result with a single element the document that was replaced.  Depending on the value of the {@code returnOriginal}
   *  @return <code>this</code>
   *  property, this will either be the document as it was before the update or as it is after the update.  If no documents matched the
   *  query filter, then null will be returned
   *  @mongodb.server.release 3.6
   *  @since 1.7
   */
  MongoCollection<TDocument> findOneAndReplace(ClientSession clientSession, JsonObject filter,
      TDocument replacement, FindOneAndReplaceOptions options,
      Handler<AsyncResult<TDocument>> resultHandler);

  /**
   *  Atomically find a document and update it.
   *  @param filter a document describing the query filter, which may not be null.
   *  @param update a document describing the update, which may not be null. The update to apply must include only update operators.
   *  @return a future with a single element the document that was updated before the update was applied.  If no documents matched the
   *  query filter, then null will be returned
   */
  Future<TDocument> findOneAndUpdate(JsonObject filter, JsonObject update);

  /**
   *  Atomically find a document and update it.
   *  @param filter a document describing the query filter, which may not be null.
   *  @param update a document describing the update, which may not be null. The update to apply must include only update operators.
   *  @param resultHandler an async result with a single element the document that was updated before the update was applied.  If no documents matched the
   *  @return <code>this</code>
   *  query filter, then null will be returned
   */
  MongoCollection<TDocument> findOneAndUpdate(JsonObject filter, JsonObject update,
      Handler<AsyncResult<TDocument>> resultHandler);

  /**
   *  Atomically find a document and update it.
   *  @param filter  a document describing the query filter, which may not be null.
   *  @param update  a document describing the update, which may not be null. The update to apply must include only update operators.
   *  @param options the options to apply to the operation
   *  @return a future with a single element the document that was updated.  Depending on the value of the {@code returnOriginal}
   *  property, this will either be the document as it was before the update or as it is after the update.  If no documents matched the
   *  query filter, then null will be returned
   */
  Future<TDocument> findOneAndUpdate(JsonObject filter, JsonObject update,
      FindOneAndUpdateOptions options);

  /**
   *  Atomically find a document and update it.
   *  @param filter  a document describing the query filter, which may not be null.
   *  @param update  a document describing the update, which may not be null. The update to apply must include only update operators.
   *  @param options the options to apply to the operation
   *  @param resultHandler an async result with a single element the document that was updated.  Depending on the value of the {@code returnOriginal}
   *  @return <code>this</code>
   *  property, this will either be the document as it was before the update or as it is after the update.  If no documents matched the
   *  query filter, then null will be returned
   */
  MongoCollection<TDocument> findOneAndUpdate(JsonObject filter, JsonObject update,
      FindOneAndUpdateOptions options, Handler<AsyncResult<TDocument>> resultHandler);

  /**
   *  Atomically find a document and update it.
   *  @param clientSession the client session with which to associate this operation
   *  @param filter a document describing the query filter, which may not be null.
   *  @param update a document describing the update, which may not be null. The update to apply must include only update operators.
   *  @return a future with a single element the document that was updated before the update was applied.  If no documents matched the
   *  query filter, then null will be returned
   *  @mongodb.server.release 3.6
   *  @since 1.7
   */
  Future<TDocument> findOneAndUpdate(ClientSession clientSession, JsonObject filter,
      JsonObject update);

  /**
   *  Atomically find a document and update it.
   *  @param clientSession the client session with which to associate this operation
   *  @param filter a document describing the query filter, which may not be null.
   *  @param update a document describing the update, which may not be null. The update to apply must include only update operators.
   *  @param resultHandler an async result with a single element the document that was updated before the update was applied.  If no documents matched the
   *  @return <code>this</code>
   *  query filter, then null will be returned
   *  @mongodb.server.release 3.6
   *  @since 1.7
   */
  MongoCollection<TDocument> findOneAndUpdate(ClientSession clientSession, JsonObject filter,
      JsonObject update, Handler<AsyncResult<TDocument>> resultHandler);

  /**
   *  Atomically find a document and update it.
   *  @param clientSession the client session with which to associate this operation
   *  @param filter  a document describing the query filter, which may not be null.
   *  @param update  a document describing the update, which may not be null. The update to apply must include only update operators.
   *  @param options the options to apply to the operation
   *  @return a future with a single element the document that was updated.  Depending on the value of the {@code returnOriginal}
   *  property, this will either be the document as it was before the update or as it is after the update.  If no documents matched the
   *  query filter, then null will be returned
   *  @mongodb.server.release 3.6
   *  @since 1.7
   */
  Future<TDocument> findOneAndUpdate(ClientSession clientSession, JsonObject filter,
      JsonObject update, FindOneAndUpdateOptions options);

  /**
   *  Atomically find a document and update it.
   *  @param clientSession the client session with which to associate this operation
   *  @param filter  a document describing the query filter, which may not be null.
   *  @param update  a document describing the update, which may not be null. The update to apply must include only update operators.
   *  @param options the options to apply to the operation
   *  @param resultHandler an async result with a single element the document that was updated.  Depending on the value of the {@code returnOriginal}
   *  @return <code>this</code>
   *  property, this will either be the document as it was before the update or as it is after the update.  If no documents matched the
   *  query filter, then null will be returned
   *  @mongodb.server.release 3.6
   *  @since 1.7
   */
  MongoCollection<TDocument> findOneAndUpdate(ClientSession clientSession, JsonObject filter,
      JsonObject update, FindOneAndUpdateOptions options,
      Handler<AsyncResult<TDocument>> resultHandler);

  /**
   *  Atomically find a document and update it.
   *  <p>Note: Supports retryable writes on MongoDB server versions 3.6 or higher when the retryWrites setting is enabled.</p>
   *  @param filter a document describing the query filter, which may not be null.
   *  @param update a pipeline describing the update, which may not be null.
   *  @return a future with a single element the document that was updated.  Depending on the value of the {@code returnOriginal}
   *  property, this will either be the document as it was before the update or as it is after the update.  If no documents matched the
   *  query filter, then null will be returned
   *  @since 1.12
   *  @mongodb.server.release 4.2
   */
  Future<TDocument> findOneAndUpdate(JsonObject filter, List<JsonObject> update);

  /**
   *  Atomically find a document and update it.
   *  <p>Note: Supports retryable writes on MongoDB server versions 3.6 or higher when the retryWrites setting is enabled.</p>
   *  @param filter a document describing the query filter, which may not be null.
   *  @param update a pipeline describing the update, which may not be null.
   *  @param resultHandler an async result with a single element the document that was updated.  Depending on the value of the {@code returnOriginal}
   *  @return <code>this</code>
   *  property, this will either be the document as it was before the update or as it is after the update.  If no documents matched the
   *  query filter, then null will be returned
   *  @since 1.12
   *  @mongodb.server.release 4.2
   */
  MongoCollection<TDocument> findOneAndUpdate(JsonObject filter, List<JsonObject> update,
      Handler<AsyncResult<TDocument>> resultHandler);

  /**
   *  Atomically find a document and update it.
   *  <p>Note: Supports retryable writes on MongoDB server versions 3.6 or higher when the retryWrites setting is enabled.</p>
   *  @param filter  a document describing the query filter, which may not be null.
   *  @param update  a pipeline describing the update, which may not be null.
   *  @param options the options to apply to the operation
   *  @return a future with a single element the document that was updated.  Depending on the value of the {@code returnOriginal}
   *  property, this will either be the document as it was before the update or as it is after the update.  If no documents matched the
   *  query filter, then null will be returned
   *  @since 1.12
   *  @mongodb.server.release 4.2
   */
  Future<TDocument> findOneAndUpdate(JsonObject filter, List<JsonObject> update,
      FindOneAndUpdateOptions options);

  /**
   *  Atomically find a document and update it.
   *  <p>Note: Supports retryable writes on MongoDB server versions 3.6 or higher when the retryWrites setting is enabled.</p>
   *  @param filter  a document describing the query filter, which may not be null.
   *  @param update  a pipeline describing the update, which may not be null.
   *  @param options the options to apply to the operation
   *  @param resultHandler an async result with a single element the document that was updated.  Depending on the value of the {@code returnOriginal}
   *  @return <code>this</code>
   *  property, this will either be the document as it was before the update or as it is after the update.  If no documents matched the
   *  query filter, then null will be returned
   *  @since 1.12
   *  @mongodb.server.release 4.2
   */
  MongoCollection<TDocument> findOneAndUpdate(JsonObject filter, List<JsonObject> update,
      FindOneAndUpdateOptions options, Handler<AsyncResult<TDocument>> resultHandler);

  /**
   *  Atomically find a document and update it.
   *  <p>Note: Supports retryable writes on MongoDB server versions 3.6 or higher when the retryWrites setting is enabled.</p>
   *  @param clientSession the client session with which to associate this operation
   *  @param filter a document describing the query filter, which may not be null.
   *  @param update a pipeline describing the update, which may not be null.
   *  @return a future with a single element the document that was updated.  Depending on the value of the {@code returnOriginal}
   *  property, this will either be the document as it was before the update or as it is after the update.  If no documents matched the
   *  query filter, then null will be returned
   *  @since 1.12
   *  @mongodb.server.release 4.2
   */
  Future<TDocument> findOneAndUpdate(ClientSession clientSession, JsonObject filter,
      List<JsonObject> update);

  /**
   *  Atomically find a document and update it.
   *  <p>Note: Supports retryable writes on MongoDB server versions 3.6 or higher when the retryWrites setting is enabled.</p>
   *  @param clientSession the client session with which to associate this operation
   *  @param filter a document describing the query filter, which may not be null.
   *  @param update a pipeline describing the update, which may not be null.
   *  @param resultHandler an async result with a single element the document that was updated.  Depending on the value of the {@code returnOriginal}
   *  @return <code>this</code>
   *  property, this will either be the document as it was before the update or as it is after the update.  If no documents matched the
   *  query filter, then null will be returned
   *  @since 1.12
   *  @mongodb.server.release 4.2
   */
  MongoCollection<TDocument> findOneAndUpdate(ClientSession clientSession, JsonObject filter,
      List<JsonObject> update, Handler<AsyncResult<TDocument>> resultHandler);

  /**
   *  Atomically find a document and update it.
   *  <p>Note: Supports retryable writes on MongoDB server versions 3.6 or higher when the retryWrites setting is enabled.</p>
   *  @param clientSession the client session with which to associate this operation
   *  @param filter  a document describing the query filter, which may not be null.
   *  @param update  a pipeline describing the update, which may not be null.
   *  @param options the options to apply to the operation
   *  @return a future with a single element the document that was updated.  Depending on the value of the {@code returnOriginal}
   *  property, this will either be the document as it was before the update or as it is after the update.  If no documents matched the
   *  query filter, then null will be returned
   *  @since 1.12
   *  @mongodb.server.release 4.2
   */
  Future<TDocument> findOneAndUpdate(ClientSession clientSession, JsonObject filter,
      List<JsonObject> update, FindOneAndUpdateOptions options);

  /**
   *  Atomically find a document and update it.
   *  <p>Note: Supports retryable writes on MongoDB server versions 3.6 or higher when the retryWrites setting is enabled.</p>
   *  @param clientSession the client session with which to associate this operation
   *  @param filter  a document describing the query filter, which may not be null.
   *  @param update  a pipeline describing the update, which may not be null.
   *  @param options the options to apply to the operation
   *  @param resultHandler an async result with a single element the document that was updated.  Depending on the value of the {@code returnOriginal}
   *  @return <code>this</code>
   *  property, this will either be the document as it was before the update or as it is after the update.  If no documents matched the
   *  query filter, then null will be returned
   *  @since 1.12
   *  @mongodb.server.release 4.2
   */
  MongoCollection<TDocument> findOneAndUpdate(ClientSession clientSession, JsonObject filter,
      List<JsonObject> update, FindOneAndUpdateOptions options,
      Handler<AsyncResult<TDocument>> resultHandler);

  /**
   *  Drops this collection from the Database.
   *  @return an empty future that indicates when the operation has completed
   *  @mongodb.driver.manual reference/command/drop/ Drop Collection
   */
  Future<Void> drop();

  /**
   *  Drops this collection from the Database.
   *  @param resultHandler an empty async result that indicates when the operation has completed
   *  @return <code>this</code>
   *  @mongodb.driver.manual reference/command/drop/ Drop Collection
   */
  MongoCollection<TDocument> drop(Handler<AsyncResult<Void>> resultHandler);

  /**
   *  Drops this collection from the Database.
   *  @param clientSession the client session with which to associate this operation
   *  @return an empty future that indicates when the operation has completed
   *  @mongodb.driver.manual reference/command/drop/ Drop Collection
   *  @mongodb.server.release 3.6
   *  @since 1.7
   */
  Future<Void> drop(ClientSession clientSession);

  /**
   *  Drops this collection from the Database.
   *  @param clientSession the client session with which to associate this operation
   *  @param resultHandler an empty async result that indicates when the operation has completed
   *  @return <code>this</code>
   *  @mongodb.driver.manual reference/command/drop/ Drop Collection
   *  @mongodb.server.release 3.6
   *  @since 1.7
   */
  MongoCollection<TDocument> drop(ClientSession clientSession,
      Handler<AsyncResult<Void>> resultHandler);

  /**
   *  Creates an index.
   *  @param key an object describing the index key(s), which may not be null.
   *  @return an empty future that indicates when the operation has completed
   *  @mongodb.driver.manual reference/method/db.collection.ensureIndex Ensure Index
   */
  Future<String> createIndex(JsonObject key);

  /**
   *  Creates an index.
   *  @param key an object describing the index key(s), which may not be null.
   *  @param resultHandler an empty async result that indicates when the operation has completed
   *  @return <code>this</code>
   *  @mongodb.driver.manual reference/method/db.collection.ensureIndex Ensure Index
   */
  MongoCollection<TDocument> createIndex(JsonObject key,
      Handler<AsyncResult<String>> resultHandler);

  /**
   *  Creates an index.
   *  @param key     an object describing the index key(s), which may not be null.
   *  @param options the options for the index
   *  @return an empty future that indicates when the operation has completed
   *  @mongodb.driver.manual reference/method/db.collection.ensureIndex Ensure Index
   */
  Future<String> createIndex(JsonObject key, IndexOptions options);

  /**
   *  Creates an index.
   *  @param key     an object describing the index key(s), which may not be null.
   *  @param options the options for the index
   *  @param resultHandler an empty async result that indicates when the operation has completed
   *  @return <code>this</code>
   *  @mongodb.driver.manual reference/method/db.collection.ensureIndex Ensure Index
   */
  MongoCollection<TDocument> createIndex(JsonObject key, IndexOptions options,
      Handler<AsyncResult<String>> resultHandler);

  /**
   *  Creates an index.
   *  @param clientSession the client session with which to associate this operation
   *  @param key an object describing the index key(s), which may not be null.
   *  @return an empty future that indicates when the operation has completed
   *  @mongodb.driver.manual reference/method/db.collection.ensureIndex Ensure Index
   *  @mongodb.server.release 3.6
   *  @since 1.7
   */
  Future<String> createIndex(ClientSession clientSession, JsonObject key);

  /**
   *  Creates an index.
   *  @param clientSession the client session with which to associate this operation
   *  @param key an object describing the index key(s), which may not be null.
   *  @param resultHandler an empty async result that indicates when the operation has completed
   *  @return <code>this</code>
   *  @mongodb.driver.manual reference/method/db.collection.ensureIndex Ensure Index
   *  @mongodb.server.release 3.6
   *  @since 1.7
   */
  MongoCollection<TDocument> createIndex(ClientSession clientSession, JsonObject key,
      Handler<AsyncResult<String>> resultHandler);

  /**
   *  Creates an index.
   *  @param clientSession the client session with which to associate this operation
   *  @param key     an object describing the index key(s), which may not be null.
   *  @param options the options for the index
   *  @return an empty future that indicates when the operation has completed
   *  @mongodb.driver.manual reference/method/db.collection.ensureIndex Ensure Index
   *  @mongodb.server.release 3.6
   *  @since 1.7
   */
  Future<String> createIndex(ClientSession clientSession, JsonObject key, IndexOptions options);

  /**
   *  Creates an index.
   *  @param clientSession the client session with which to associate this operation
   *  @param key     an object describing the index key(s), which may not be null.
   *  @param options the options for the index
   *  @param resultHandler an empty async result that indicates when the operation has completed
   *  @return <code>this</code>
   *  @mongodb.driver.manual reference/method/db.collection.ensureIndex Ensure Index
   *  @mongodb.server.release 3.6
   *  @since 1.7
   */
  MongoCollection<TDocument> createIndex(ClientSession clientSession, JsonObject key,
      IndexOptions options, Handler<AsyncResult<String>> resultHandler);

  /**
   *  Create multiple indexes.
   *  @param indexes the list of indexes
   *  @return an empty future that indicates when the operation has completed
   *  @mongodb.driver.manual reference/command/createIndexes Create indexes
   */
  Future<String> createIndexes(List<IndexModel> indexes);

  /**
   *  Create multiple indexes.
   *  @param indexes the list of indexes
   *  @param resultHandler an empty async result that indicates when the operation has completed
   *  @return <code>this</code>
   *  @mongodb.driver.manual reference/command/createIndexes Create indexes
   */
  MongoCollection<TDocument> createIndexes(List<IndexModel> indexes,
      Handler<AsyncResult<String>> resultHandler);

  /**
   *  Create multiple indexes.
   *  @param indexes the list of indexes
   *  @param createIndexOptions options to use when creating indexes
   *  @return an empty future that indicates when the operation has completed
   *  @mongodb.driver.manual reference/command/createIndexes Create indexes
   *  @since 1.7
   */
  Future<String> createIndexes(List<IndexModel> indexes, CreateIndexOptions createIndexOptions);

  /**
   *  Create multiple indexes.
   *  @param indexes the list of indexes
   *  @param createIndexOptions options to use when creating indexes
   *  @param resultHandler an empty async result that indicates when the operation has completed
   *  @return <code>this</code>
   *  @mongodb.driver.manual reference/command/createIndexes Create indexes
   *  @since 1.7
   */
  MongoCollection<TDocument> createIndexes(List<IndexModel> indexes,
      CreateIndexOptions createIndexOptions, Handler<AsyncResult<String>> resultHandler);

  /**
   *  Create multiple indexes.
   *  @param clientSession the client session with which to associate this operation
   *  @param indexes the list of indexes
   *  @return an empty future that indicates when the operation has completed
   *  @mongodb.driver.manual reference/command/createIndexes Create indexes
   *  @mongodb.server.release 3.6
   *  @since 1.7
   */
  Future<String> createIndexes(ClientSession clientSession, List<IndexModel> indexes);

  /**
   *  Create multiple indexes.
   *  @param clientSession the client session with which to associate this operation
   *  @param indexes the list of indexes
   *  @param resultHandler an empty async result that indicates when the operation has completed
   *  @return <code>this</code>
   *  @mongodb.driver.manual reference/command/createIndexes Create indexes
   *  @mongodb.server.release 3.6
   *  @since 1.7
   */
  MongoCollection<TDocument> createIndexes(ClientSession clientSession, List<IndexModel> indexes,
      Handler<AsyncResult<String>> resultHandler);

  /**
   *  Create multiple indexes.
   *  @param clientSession the client session with which to associate this operation
   *  @param indexes the list of indexes
   *  @param createIndexOptions options to use when creating indexes
   *  @return an empty future that indicates when the operation has completed
   *  @mongodb.driver.manual reference/command/createIndexes Create indexes
   *  @mongodb.server.release 3.6
   *  @since 1.7
   */
  Future<String> createIndexes(ClientSession clientSession, List<IndexModel> indexes,
      CreateIndexOptions createIndexOptions);

  /**
   *  Create multiple indexes.
   *  @param clientSession the client session with which to associate this operation
   *  @param indexes the list of indexes
   *  @param createIndexOptions options to use when creating indexes
   *  @param resultHandler an empty async result that indicates when the operation has completed
   *  @return <code>this</code>
   *  @mongodb.driver.manual reference/command/createIndexes Create indexes
   *  @mongodb.server.release 3.6
   *  @since 1.7
   */
  MongoCollection<TDocument> createIndexes(ClientSession clientSession, List<IndexModel> indexes,
      CreateIndexOptions createIndexOptions, Handler<AsyncResult<String>> resultHandler);

  /**
   *  Get all the indexes in this collection.
   *  @return the fluent list indexes interface
   *  @mongodb.driver.manual reference/command/listIndexes/ listIndexes
   */
  MongoResult<JsonObject> listIndexes();

  /**
   *  Get all the indexes in this collection.
   *  @param options options
   *  @return the fluent list indexes interface
   *  @mongodb.driver.manual reference/command/listIndexes/ listIndexes
   */
  MongoResult<JsonObject> listIndexes(ListIndexesOptions options);

  /**
   *  Get all the indexes in this collection.
   *  @param clientSession the client session with which to associate this operation
   *  @return the fluent list indexes interface
   *  @mongodb.driver.manual reference/command/listIndexes/ listIndexes
   *  @mongodb.server.release 3.6
   *  @since 1.7
   */
  MongoResult<JsonObject> listIndexes(ClientSession clientSession);

  /**
   *  Get all the indexes in this collection.
   *  @param clientSession the client session with which to associate this operation
   *  @param options options
   *  @return the fluent list indexes interface
   *  @mongodb.driver.manual reference/command/listIndexes/ listIndexes
   *  @mongodb.server.release 3.6
   *  @since 1.7
   */
  MongoResult<JsonObject> listIndexes(ClientSession clientSession, ListIndexesOptions options);

  /**
   *  Drops the given index.
   *  @param indexName the name of the index to remove
   *  @return an empty future that indicates when the operation has completed
   *  @mongodb.driver.manual reference/command/dropIndexes/ Drop Indexes
   */
  Future<Void> dropIndex(String indexName);

  /**
   *  Drops the given index.
   *  @param indexName the name of the index to remove
   *  @param resultHandler an empty async result that indicates when the operation has completed
   *  @return <code>this</code>
   *  @mongodb.driver.manual reference/command/dropIndexes/ Drop Indexes
   */
  MongoCollection<TDocument> dropIndex(String indexName, Handler<AsyncResult<Void>> resultHandler);

  /**
   *  Drops the index given the keys used to create it.
   *  @param keys the keys of the index to remove
   *  @return an empty future that indicates when the operation has completed
   *  @mongodb.driver.manual reference/command/dropIndexes/ Drop indexes
   */
  Future<Void> dropIndex(JsonObject keys);

  /**
   *  Drops the index given the keys used to create it.
   *  @param keys the keys of the index to remove
   *  @param resultHandler an empty async result that indicates when the operation has completed
   *  @return <code>this</code>
   *  @mongodb.driver.manual reference/command/dropIndexes/ Drop indexes
   */
  MongoCollection<TDocument> dropIndex(JsonObject keys, Handler<AsyncResult<Void>> resultHandler);

  /**
   *  Drops the given index.
   *  @param indexName the name of the index to remove
   *  @param dropIndexOptions options to use when dropping indexes
   *  @return an empty future that indicates when the operation has completed
   *  @mongodb.driver.manual reference/command/dropIndexes/ Drop Indexes
   *  @since 1.7
   */
  Future<Void> dropIndex(String indexName, DropIndexOptions dropIndexOptions);

  /**
   *  Drops the given index.
   *  @param indexName the name of the index to remove
   *  @param dropIndexOptions options to use when dropping indexes
   *  @param resultHandler an empty async result that indicates when the operation has completed
   *  @return <code>this</code>
   *  @mongodb.driver.manual reference/command/dropIndexes/ Drop Indexes
   *  @since 1.7
   */
  MongoCollection<TDocument> dropIndex(String indexName, DropIndexOptions dropIndexOptions,
      Handler<AsyncResult<Void>> resultHandler);

  /**
   *  Drops the index given the keys used to create it.
   *  @param keys the keys of the index to remove
   *  @param dropIndexOptions options to use when dropping indexes
   *  @return an empty future that indicates when the operation has completed
   *  @mongodb.driver.manual reference/command/dropIndexes/ Drop indexes
   *  @since 1.7
   */
  Future<Void> dropIndex(JsonObject keys, DropIndexOptions dropIndexOptions);

  /**
   *  Drops the index given the keys used to create it.
   *  @param keys the keys of the index to remove
   *  @param dropIndexOptions options to use when dropping indexes
   *  @param resultHandler an empty async result that indicates when the operation has completed
   *  @return <code>this</code>
   *  @mongodb.driver.manual reference/command/dropIndexes/ Drop indexes
   *  @since 1.7
   */
  MongoCollection<TDocument> dropIndex(JsonObject keys, DropIndexOptions dropIndexOptions,
      Handler<AsyncResult<Void>> resultHandler);

  /**
   *  Drops the given index.
   *  @param clientSession the client session with which to associate this operation
   *  @param indexName the name of the index to remove
   *  @return an empty future that indicates when the operation has completed
   *  @mongodb.driver.manual reference/command/dropIndexes/ Drop Indexes
   *  @mongodb.server.release 3.6
   *  @since 1.7
   */
  Future<Void> dropIndex(ClientSession clientSession, String indexName);

  /**
   *  Drops the given index.
   *  @param clientSession the client session with which to associate this operation
   *  @param indexName the name of the index to remove
   *  @param resultHandler an empty async result that indicates when the operation has completed
   *  @return <code>this</code>
   *  @mongodb.driver.manual reference/command/dropIndexes/ Drop Indexes
   *  @mongodb.server.release 3.6
   *  @since 1.7
   */
  MongoCollection<TDocument> dropIndex(ClientSession clientSession, String indexName,
      Handler<AsyncResult<Void>> resultHandler);

  /**
   *  Drops the index given the keys used to create it.
   *  @param clientSession the client session with which to associate this operation
   *  @param keys the keys of the index to remove
   *  @return an empty future that indicates when the operation has completed
   *  @mongodb.driver.manual reference/command/dropIndexes/ Drop indexes
   *  @mongodb.server.release 3.6
   *  @since 1.7
   */
  Future<Void> dropIndex(ClientSession clientSession, JsonObject keys);

  /**
   *  Drops the index given the keys used to create it.
   *  @param clientSession the client session with which to associate this operation
   *  @param keys the keys of the index to remove
   *  @param resultHandler an empty async result that indicates when the operation has completed
   *  @return <code>this</code>
   *  @mongodb.driver.manual reference/command/dropIndexes/ Drop indexes
   *  @mongodb.server.release 3.6
   *  @since 1.7
   */
  MongoCollection<TDocument> dropIndex(ClientSession clientSession, JsonObject keys,
      Handler<AsyncResult<Void>> resultHandler);

  /**
   *  Drops the given index.
   *  @param clientSession the client session with which to associate this operation
   *  @param indexName the name of the index to remove
   *  @param dropIndexOptions options to use when dropping indexes
   *  @return an empty future that indicates when the operation has completed
   *  @mongodb.driver.manual reference/command/dropIndexes/ Drop Indexes
   *  @mongodb.server.release 3.6
   *  @since 1.7
   */
  Future<Void> dropIndex(ClientSession clientSession, String indexName,
      DropIndexOptions dropIndexOptions);

  /**
   *  Drops the given index.
   *  @param clientSession the client session with which to associate this operation
   *  @param indexName the name of the index to remove
   *  @param dropIndexOptions options to use when dropping indexes
   *  @param resultHandler an empty async result that indicates when the operation has completed
   *  @return <code>this</code>
   *  @mongodb.driver.manual reference/command/dropIndexes/ Drop Indexes
   *  @mongodb.server.release 3.6
   *  @since 1.7
   */
  MongoCollection<TDocument> dropIndex(ClientSession clientSession, String indexName,
      DropIndexOptions dropIndexOptions, Handler<AsyncResult<Void>> resultHandler);

  /**
   *  Drops the index given the keys used to create it.
   *  @param clientSession the client session with which to associate this operation
   *  @param keys the keys of the index to remove
   *  @param dropIndexOptions options to use when dropping indexes
   *  @return an empty future that indicates when the operation has completed
   *  @mongodb.driver.manual reference/command/dropIndexes/ Drop indexes
   *  @mongodb.server.release 3.6
   *  @since 1.7
   */
  Future<Void> dropIndex(ClientSession clientSession, JsonObject keys,
      DropIndexOptions dropIndexOptions);

  /**
   *  Drops the index given the keys used to create it.
   *  @param clientSession the client session with which to associate this operation
   *  @param keys the keys of the index to remove
   *  @param dropIndexOptions options to use when dropping indexes
   *  @param resultHandler an empty async result that indicates when the operation has completed
   *  @return <code>this</code>
   *  @mongodb.driver.manual reference/command/dropIndexes/ Drop indexes
   *  @mongodb.server.release 3.6
   *  @since 1.7
   */
  MongoCollection<TDocument> dropIndex(ClientSession clientSession, JsonObject keys,
      DropIndexOptions dropIndexOptions, Handler<AsyncResult<Void>> resultHandler);

  /**
   *  Drop all the indexes on this collection, except for the default on _id.
   *  @return an empty future that indicates when the operation has completed
   *  @mongodb.driver.manual reference/command/dropIndexes/ Drop Indexes
   */
  Future<Void> dropIndexes();

  /**
   *  Drop all the indexes on this collection, except for the default on _id.
   *  @param resultHandler an empty async result that indicates when the operation has completed
   *  @return <code>this</code>
   *  @mongodb.driver.manual reference/command/dropIndexes/ Drop Indexes
   */
  MongoCollection<TDocument> dropIndexes(Handler<AsyncResult<Void>> resultHandler);

  /**
   *  Drop all the indexes on this collection, except for the default on _id.
   *  @param dropIndexOptions options to use when dropping indexes
   *  @return an empty future that indicates when the operation has completed
   *  @mongodb.driver.manual reference/command/dropIndexes/ Drop Indexes
   *  @since 1.7
   */
  Future<Void> dropIndexes(DropIndexOptions dropIndexOptions);

  /**
   *  Drop all the indexes on this collection, except for the default on _id.
   *  @param dropIndexOptions options to use when dropping indexes
   *  @param resultHandler an empty async result that indicates when the operation has completed
   *  @return <code>this</code>
   *  @mongodb.driver.manual reference/command/dropIndexes/ Drop Indexes
   *  @since 1.7
   */
  MongoCollection<TDocument> dropIndexes(DropIndexOptions dropIndexOptions,
      Handler<AsyncResult<Void>> resultHandler);

  /**
   *  Drop all the indexes on this collection, except for the default on _id.
   *  @param clientSession the client session with which to associate this operation
   *  @return an empty future that indicates when the operation has completed
   *  @mongodb.driver.manual reference/command/dropIndexes/ Drop Indexes
   *  @mongodb.server.release 3.6
   *  @since 1.7
   */
  Future<Void> dropIndexes(ClientSession clientSession);

  /**
   *  Drop all the indexes on this collection, except for the default on _id.
   *  @param clientSession the client session with which to associate this operation
   *  @param resultHandler an empty async result that indicates when the operation has completed
   *  @return <code>this</code>
   *  @mongodb.driver.manual reference/command/dropIndexes/ Drop Indexes
   *  @mongodb.server.release 3.6
   *  @since 1.7
   */
  MongoCollection<TDocument> dropIndexes(ClientSession clientSession,
      Handler<AsyncResult<Void>> resultHandler);

  /**
   *  Drop all the indexes on this collection, except for the default on _id.
   *  @param clientSession the client session with which to associate this operation
   *  @param dropIndexOptions options to use when dropping indexes
   *  @return an empty future that indicates when the operation has completed
   *  @mongodb.driver.manual reference/command/dropIndexes/ Drop Indexes
   *  @mongodb.server.release 3.6
   *  @since 1.7
   */
  Future<Void> dropIndexes(ClientSession clientSession, DropIndexOptions dropIndexOptions);

  /**
   *  Drop all the indexes on this collection, except for the default on _id.
   *  @param clientSession the client session with which to associate this operation
   *  @param dropIndexOptions options to use when dropping indexes
   *  @param resultHandler an empty async result that indicates when the operation has completed
   *  @return <code>this</code>
   *  @mongodb.driver.manual reference/command/dropIndexes/ Drop Indexes
   *  @mongodb.server.release 3.6
   *  @since 1.7
   */
  MongoCollection<TDocument> dropIndexes(ClientSession clientSession,
      DropIndexOptions dropIndexOptions, Handler<AsyncResult<Void>> resultHandler);

  /**
   *  Rename the collection with oldCollectionName to the newCollectionName.
   *  @param newCollectionNamespace the namespace the collection will be renamed to
   *  @return an empty future that indicates when the operation has completed
   *  @mongodb.driver.manual reference/commands/renameCollection Rename collection
   */
  Future<Void> renameCollection(MongoNamespace newCollectionNamespace);

  /**
   *  Rename the collection with oldCollectionName to the newCollectionName.
   *  @param newCollectionNamespace the namespace the collection will be renamed to
   *  @param resultHandler an empty async result that indicates when the operation has completed
   *  @return <code>this</code>
   *  @mongodb.driver.manual reference/commands/renameCollection Rename collection
   */
  MongoCollection<TDocument> renameCollection(MongoNamespace newCollectionNamespace,
      Handler<AsyncResult<Void>> resultHandler);

  /**
   *  Rename the collection with oldCollectionName to the newCollectionName.
   *  @param newCollectionNamespace the name the collection will be renamed to
   *  @param options                the options for renaming a collection
   *  @return an empty future that indicates when the operation has completed
   *  @mongodb.driver.manual reference/commands/renameCollection Rename collection
   */
  Future<Void> renameCollection(MongoNamespace newCollectionNamespace,
      RenameCollectionOptions options);

  /**
   *  Rename the collection with oldCollectionName to the newCollectionName.
   *  @param newCollectionNamespace the name the collection will be renamed to
   *  @param options                the options for renaming a collection
   *  @param resultHandler an empty async result that indicates when the operation has completed
   *  @return <code>this</code>
   *  @mongodb.driver.manual reference/commands/renameCollection Rename collection
   */
  MongoCollection<TDocument> renameCollection(MongoNamespace newCollectionNamespace,
      RenameCollectionOptions options, Handler<AsyncResult<Void>> resultHandler);

  /**
   *  Rename the collection with oldCollectionName to the newCollectionName.
   *  @param clientSession the client session with which to associate this operation
   *  @param newCollectionNamespace the namespace the collection will be renamed to
   *  @return an empty future that indicates when the operation has completed
   *  @mongodb.driver.manual reference/commands/renameCollection Rename collection
   *  @mongodb.server.release 3.6
   *  @since 1.7
   */
  Future<Void> renameCollection(ClientSession clientSession, MongoNamespace newCollectionNamespace);

  /**
   *  Rename the collection with oldCollectionName to the newCollectionName.
   *  @param clientSession the client session with which to associate this operation
   *  @param newCollectionNamespace the namespace the collection will be renamed to
   *  @param resultHandler an empty async result that indicates when the operation has completed
   *  @return <code>this</code>
   *  @mongodb.driver.manual reference/commands/renameCollection Rename collection
   *  @mongodb.server.release 3.6
   *  @since 1.7
   */
  MongoCollection<TDocument> renameCollection(ClientSession clientSession,
      MongoNamespace newCollectionNamespace, Handler<AsyncResult<Void>> resultHandler);

  /**
   *  Rename the collection with oldCollectionName to the newCollectionName.
   *  @param clientSession the client session with which to associate this operation
   *  @param newCollectionNamespace the name the collection will be renamed to
   *  @param options                the options for renaming a collection
   *  @return an empty future that indicates when the operation has completed
   *  @mongodb.driver.manual reference/commands/renameCollection Rename collection
   *  @mongodb.server.release 3.6
   *  @since 1.7
   */
  Future<Void> renameCollection(ClientSession clientSession, MongoNamespace newCollectionNamespace,
      RenameCollectionOptions options);

  /**
   *  Rename the collection with oldCollectionName to the newCollectionName.
   *  @param clientSession the client session with which to associate this operation
   *  @param newCollectionNamespace the name the collection will be renamed to
   *  @param options                the options for renaming a collection
   *  @param resultHandler an empty async result that indicates when the operation has completed
   *  @return <code>this</code>
   *  @mongodb.driver.manual reference/commands/renameCollection Rename collection
   *  @mongodb.server.release 3.6
   *  @since 1.7
   */
  MongoCollection<TDocument> renameCollection(ClientSession clientSession,
      MongoNamespace newCollectionNamespace, RenameCollectionOptions options,
      Handler<AsyncResult<Void>> resultHandler);

  /**
   * @return mongo object
   * @hidden
   */
  com.mongodb.reactivestreams.client.MongoCollection<TDocument> toDriverClass();
}
