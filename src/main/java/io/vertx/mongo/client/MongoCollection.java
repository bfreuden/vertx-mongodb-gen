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
   *  @param handler an async result with a single element indicating the estimated number of documents
   *  @return <code>this</code>
   *  @since 1.9
   */
  MongoCollection<TDocument> estimatedDocumentCount(Handler<AsyncResult<Long>> handler);

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
   *  @param handler an async result with a single element indicating the estimated number of documents
   *  @return <code>this</code>
   *  @since 1.9
   */
  MongoCollection<TDocument> estimatedDocumentCount(EstimatedDocumentCountOptions options,
      Handler<AsyncResult<Long>> handler);

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
   *   | &#x24;where      |  &#x24;expr                         |
   *   +-------------+--------------------------------+
   *   | &#x24;near       |  &#x24;geoWithin with &#x24;center       |
   *   +-------------+--------------------------------+
   *   | &#x24;nearSphere |  &#x24;geoWithin with &#x24;centerSphere |
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
   *   | &#x24;where      |  &#x24;expr                         |
   *   +-------------+--------------------------------+
   *   | &#x24;near       |  &#x24;geoWithin with &#x24;center       |
   *   +-------------+--------------------------------+
   *   | &#x24;nearSphere |  &#x24;geoWithin with &#x24;centerSphere |
   *   +-------------+--------------------------------+
   *  </pre>
   *  @param handler an async result with a single element indicating the number of documents
   *  @return <code>this</code>
   *  @since 1.9
   */
  MongoCollection<TDocument> countDocuments(Handler<AsyncResult<Long>> handler);

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
   *   | &#x24;where      |  &#x24;expr                         |
   *   +-------------+--------------------------------+
   *   | &#x24;near       |  &#x24;geoWithin with &#x24;center       |
   *   +-------------+--------------------------------+
   *   | &#x24;nearSphere |  &#x24;geoWithin with &#x24;centerSphere |
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
   *   | &#x24;where      |  &#x24;expr                         |
   *   +-------------+--------------------------------+
   *   | &#x24;near       |  &#x24;geoWithin with &#x24;center       |
   *   +-------------+--------------------------------+
   *   | &#x24;nearSphere |  &#x24;geoWithin with &#x24;centerSphere |
   *   +-------------+--------------------------------+
   *  </pre>
   *  @param filter the query filter
   *  @param handler an async result with a single element indicating the number of documents
   *  @return <code>this</code>
   *  @since 1.9
   */
  MongoCollection<TDocument> countDocuments(JsonObject filter, Handler<AsyncResult<Long>> handler);

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
   *   | &#x24;where      |  &#x24;expr                         |
   *   +-------------+--------------------------------+
   *   | &#x24;near       |  &#x24;geoWithin with &#x24;center       |
   *   +-------------+--------------------------------+
   *   | &#x24;nearSphere |  &#x24;geoWithin with &#x24;centerSphere |
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
   *   | &#x24;where      |  &#x24;expr                         |
   *   +-------------+--------------------------------+
   *   | &#x24;near       |  &#x24;geoWithin with &#x24;center       |
   *   +-------------+--------------------------------+
   *   | &#x24;nearSphere |  &#x24;geoWithin with &#x24;centerSphere |
   *   +-------------+--------------------------------+
   *  </pre>
   *  @param filter  the query filter
   *  @param options the options describing the count
   *  @param handler an async result with a single element indicating the number of documents
   *  @return <code>this</code>
   *  @since 1.9
   */
  MongoCollection<TDocument> countDocuments(JsonObject filter, CountOptions options,
      Handler<AsyncResult<Long>> handler);

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
   *   | &#x24;where      |  &#x24;expr                         |
   *   +-------------+--------------------------------+
   *   | &#x24;near       |  &#x24;geoWithin with &#x24;center       |
   *   +-------------+--------------------------------+
   *   | &#x24;nearSphere |  &#x24;geoWithin with &#x24;centerSphere |
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
   *   | &#x24;where      |  &#x24;expr                         |
   *   +-------------+--------------------------------+
   *   | &#x24;near       |  &#x24;geoWithin with &#x24;center       |
   *   +-------------+--------------------------------+
   *   | &#x24;nearSphere |  &#x24;geoWithin with &#x24;centerSphere |
   *   +-------------+--------------------------------+
   *  </pre>
   *  @param clientSession the client session with which to associate this operation
   *  @param handler an async result with a single element indicating the number of documents
   *  @return <code>this</code>
   *  @mongodb.server.release 3.6
   *  @since 1.9
   */
  MongoCollection<TDocument> countDocuments(ClientSession clientSession,
      Handler<AsyncResult<Long>> handler);

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
   *  @param handler an async result with a single element indicating the number of documents
   *  @return <code>this</code>
   *  @mongodb.server.release 3.6
   *  @since 1.9
   */
  MongoCollection<TDocument> countDocuments(ClientSession clientSession, JsonObject filter,
      Handler<AsyncResult<Long>> handler);

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
   *   | &#x24;where      |  &#x24;expr                         |
   *   +-------------+--------------------------------+
   *   | &#x24;near       |  &#x24;geoWithin with &#x24;center       |
   *   +-------------+--------------------------------+
   *   | &#x24;nearSphere |  &#x24;geoWithin with &#x24;centerSphere |
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
   *   | &#x24;where      |  &#x24;expr                         |
   *   +-------------+--------------------------------+
   *   | &#x24;near       |  &#x24;geoWithin with &#x24;center       |
   *   +-------------+--------------------------------+
   *   | &#x24;nearSphere |  &#x24;geoWithin with &#x24;centerSphere |
   *   +-------------+--------------------------------+
   *  </pre>
   *  @param clientSession the client session with which to associate this operation
   *  @param filter  the query filter
   *  @param options the options describing the count
   *  @param handler an async result with a single element indicating the number of documents
   *  @return <code>this</code>
   *  @mongodb.server.release 3.6
   *  @since 1.9
   */
  MongoCollection<TDocument> countDocuments(ClientSession clientSession, JsonObject filter,
      CountOptions options, Handler<AsyncResult<Long>> handler);

  /**
   *  Finds all documents in the collection.
   *  @return the fluent find interface
   *  @mongodb.driver.manual tutorial/query-documents/ Find
   */
  MongoResult<TDocument> find();

  /**
   *  Finds all documents in the collection.
   *  @param filter the query filter
   *  @return the fluent find interface
   *  @mongodb.driver.manual tutorial/query-documents/ Find
   */
  MongoResult<TDocument> find(JsonObject filter);

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
   *  @param filter the query filter
   *  @return the fluent find interface
   *  @mongodb.driver.manual tutorial/query-documents/ Find
   *  @mongodb.server.release 3.6
   *  @since 1.7
   */
  MongoResult<TDocument> find(ClientSession clientSession, JsonObject filter);

  /**
   *  Aggregates documents according to the specified aggregation pipeline.
   *  @param pipeline the aggregate pipeline
   *  @return a result containing the result of the aggregation operation
   *  @mongodb.driver.manual aggregation/ Aggregation
   */
  MongoResult<TDocument> aggregate(List<JsonObject> pipeline);

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
   *  Creates a change stream for this collection.
   *  @return the change stream iterable
   *  @mongodb.driver.manual reference/operator/aggregation/changeStream &#x24;changeStream
   *  @mongodb.server.release 3.6
   *  @since 1.7
   */
  ReadStream<JsonObject> watch();

  /**
   *  Creates a change stream for this collection.
   *  @param pipeline the aggregation pipeline to apply to the change stream
   *  @return the change stream iterable
   *  @mongodb.driver.manual reference/operator/aggregation/changeStream &#x24;changeStream
   *  @since 1.6
   */
  ReadStream<JsonObject> watch(List<JsonObject> pipeline);

  /**
   *  Creates a change stream for this collection.
   *  @param clientSession the client session with which to associate this operation
   *  @return the change stream iterable
   *  @mongodb.driver.manual reference/operator/aggregation/changeStream &#x24;changeStream
   *  @mongodb.server.release 3.6
   *  @since 1.7
   */
  ReadStream<JsonObject> watch(ClientSession clientSession);

  /**
   *  Creates a change stream for this collection.
   *  @param clientSession the client session with which to associate this operation
   *  @param pipeline the aggregation pipeline to apply to the change stream
   *  @return the change stream iterable
   *  @mongodb.driver.manual reference/operator/aggregation/changeStream &#x24;changeStream
   *  @mongodb.server.release 3.6
   *  @since 1.7
   */
  ReadStream<JsonObject> watch(ClientSession clientSession, List<JsonObject> pipeline);

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
   *  Executes a mix of inserts, updates, replaces, and deletes.
   *  @param requests the writes to execute
   *  @return a future with a single element the BulkWriteResult
   */
  Future<BulkWriteResult> bulkWrite(List<JsonObject> requests);

  /**
   *  Executes a mix of inserts, updates, replaces, and deletes.
   *  @param requests the writes to execute
   *  @param handler an async result with a single element the BulkWriteResult
   *  @return <code>this</code>
   */
  MongoCollection<TDocument> bulkWrite(List<JsonObject> requests,
      Handler<AsyncResult<BulkWriteResult>> handler);

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
   *  @param handler an async result with a single element the BulkWriteResult
   *  @return <code>this</code>
   */
  MongoCollection<TDocument> bulkWrite(List<JsonObject> requests, BulkWriteOptions options,
      Handler<AsyncResult<BulkWriteResult>> handler);

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
   *  @param handler an async result with a single element the BulkWriteResult
   *  @return <code>this</code>
   *  @mongodb.server.release 3.6
   *  @since 1.7
   */
  MongoCollection<TDocument> bulkWrite(ClientSession clientSession, List<JsonObject> requests,
      Handler<AsyncResult<BulkWriteResult>> handler);

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
   *  @param handler an async result with a single element the BulkWriteResult
   *  @return <code>this</code>
   *  @mongodb.server.release 3.6
   *  @since 1.7
   */
  MongoCollection<TDocument> bulkWrite(ClientSession clientSession, List<JsonObject> requests,
      BulkWriteOptions options, Handler<AsyncResult<BulkWriteResult>> handler);

  /**
   *  Inserts the provided document. If the document is missing an identifier, the driver should generate one.
   *  @param document the document to insert
   *  @return a future with a single element with the InsertOneResult or with either a
   *  com.mongodb.DuplicateKeyException or com.mongodb.MongoException
   */
  Future<InsertOneResult> insertOne(JsonObject document);

  /**
   *  Inserts the provided document. If the document is missing an identifier, the driver should generate one.
   *  @param document the document to insert
   *  @param handler an async result with a single element with the InsertOneResult or with either a
   *  @return <code>this</code>
   *  com.mongodb.DuplicateKeyException or com.mongodb.MongoException
   */
  MongoCollection<TDocument> insertOne(JsonObject document,
      Handler<AsyncResult<InsertOneResult>> handler);

  /**
   *  Inserts the provided document. If the document is missing an identifier, the driver should generate one.
   *  @param document the document to insert
   *  @param options  the options to apply to the operation
   *  @return a future with a single element with the InsertOneResult or with either a
   *  com.mongodb.DuplicateKeyException or com.mongodb.MongoException
   *  @since 1.2
   */
  Future<InsertOneResult> insertOne(JsonObject document, InsertOneOptions options);

  /**
   *  Inserts the provided document. If the document is missing an identifier, the driver should generate one.
   *  @param document the document to insert
   *  @param options  the options to apply to the operation
   *  @param handler an async result with a single element with the InsertOneResult or with either a
   *  @return <code>this</code>
   *  com.mongodb.DuplicateKeyException or com.mongodb.MongoException
   *  @since 1.2
   */
  MongoCollection<TDocument> insertOne(JsonObject document, InsertOneOptions options,
      Handler<AsyncResult<InsertOneResult>> handler);

  /**
   *  Inserts the provided document. If the document is missing an identifier, the driver should generate one.
   *  @param clientSession the client session with which to associate this operation
   *  @param document the document to insert
   *  @return a future with a single element with the InsertOneResult or with either a
   *  com.mongodb.DuplicateKeyException or com.mongodb.MongoException
   *  @mongodb.server.release 3.6
   *  @since 1.7
   */
  Future<InsertOneResult> insertOne(ClientSession clientSession, JsonObject document);

  /**
   *  Inserts the provided document. If the document is missing an identifier, the driver should generate one.
   *  @param clientSession the client session with which to associate this operation
   *  @param document the document to insert
   *  @param handler an async result with a single element with the InsertOneResult or with either a
   *  @return <code>this</code>
   *  com.mongodb.DuplicateKeyException or com.mongodb.MongoException
   *  @mongodb.server.release 3.6
   *  @since 1.7
   */
  MongoCollection<TDocument> insertOne(ClientSession clientSession, JsonObject document,
      Handler<AsyncResult<InsertOneResult>> handler);

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
  Future<InsertOneResult> insertOne(ClientSession clientSession, JsonObject document,
      InsertOneOptions options);

  /**
   *  Inserts the provided document. If the document is missing an identifier, the driver should generate one.
   *  @param clientSession the client session with which to associate this operation
   *  @param document the document to insert
   *  @param options  the options to apply to the operation
   *  @param handler an async result with a single element with the InsertOneResult or with either a
   *  @return <code>this</code>
   *  com.mongodb.DuplicateKeyException or com.mongodb.MongoException
   *  @mongodb.server.release 3.6
   *  @since 1.7
   */
  MongoCollection<TDocument> insertOne(ClientSession clientSession, JsonObject document,
      InsertOneOptions options, Handler<AsyncResult<InsertOneResult>> handler);

  /**
   *  Inserts a batch of documents.
   *  @param documents the documents to insert
   *  @return a future with a single element with the InsertManyResult or with either a
   *  com.mongodb.DuplicateKeyException or com.mongodb.MongoException
   */
  Future<InsertManyResult> insertMany(List<? super TDocument> documents);

  /**
   *  Inserts a batch of documents.
   *  @param documents the documents to insert
   *  @param handler an async result with a single element with the InsertManyResult or with either a
   *  @return <code>this</code>
   *  com.mongodb.DuplicateKeyException or com.mongodb.MongoException
   */
  MongoCollection<TDocument> insertMany(List<? super TDocument> documents,
      Handler<AsyncResult<InsertManyResult>> handler);

  /**
   *  Inserts a batch of documents.
   *  @param documents the documents to insert
   *  @param options   the options to apply to the operation
   *  @return a future with a single element with the InsertManyResult or with either a
   *  com.mongodb.DuplicateKeyException or com.mongodb.MongoException
   */
  Future<InsertManyResult> insertMany(List<? super TDocument> documents, InsertManyOptions options);

  /**
   *  Inserts a batch of documents.
   *  @param documents the documents to insert
   *  @param options   the options to apply to the operation
   *  @param handler an async result with a single element with the InsertManyResult or with either a
   *  @return <code>this</code>
   *  com.mongodb.DuplicateKeyException or com.mongodb.MongoException
   */
  MongoCollection<TDocument> insertMany(List<? super TDocument> documents,
      InsertManyOptions options, Handler<AsyncResult<InsertManyResult>> handler);

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
      List<? super TDocument> documents);

  /**
   *  Inserts a batch of documents.
   *  @param clientSession the client session with which to associate this operation
   *  @param documents the documents to insert
   *  @param handler an async result with a single element with the InsertManyResult or with either a
   *  @return <code>this</code>
   *  com.mongodb.DuplicateKeyException or com.mongodb.MongoException
   *  @mongodb.server.release 3.6
   *  @since 1.7
   */
  MongoCollection<TDocument> insertMany(ClientSession clientSession,
      List<? super TDocument> documents, Handler<AsyncResult<InsertManyResult>> handler);

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
      List<? super TDocument> documents, InsertManyOptions options);

  /**
   *  Inserts a batch of documents.
   *  @param clientSession the client session with which to associate this operation
   *  @param documents the documents to insert
   *  @param options   the options to apply to the operation
   *  @param handler an async result with a single element with the InsertManyResult or with either a
   *  @return <code>this</code>
   *  com.mongodb.DuplicateKeyException or com.mongodb.MongoException
   *  @mongodb.server.release 3.6
   *  @since 1.7
   */
  MongoCollection<TDocument> insertMany(ClientSession clientSession,
      List<? super TDocument> documents, InsertManyOptions options,
      Handler<AsyncResult<InsertManyResult>> handler);

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
   *  @param handler an async result with a single element the DeleteResult or with an com.mongodb.MongoException
   *  @return <code>this</code>
   */
  MongoCollection<TDocument> deleteOne(JsonObject filter,
      Handler<AsyncResult<DeleteResult>> handler);

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
   *  @param handler an async result with a single element the DeleteResult or with an com.mongodb.MongoException
   *  @return <code>this</code>
   *  @since 1.5
   */
  MongoCollection<TDocument> deleteOne(JsonObject filter, DeleteOptions options,
      Handler<AsyncResult<DeleteResult>> handler);

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
   *  @param handler an async result with a single element the DeleteResult or with an com.mongodb.MongoException
   *  @return <code>this</code>
   *  @mongodb.server.release 3.6
   *  @since 1.7
   */
  MongoCollection<TDocument> deleteOne(ClientSession clientSession, JsonObject filter,
      Handler<AsyncResult<DeleteResult>> handler);

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
   *  @param handler an async result with a single element the DeleteResult or with an com.mongodb.MongoException
   *  @return <code>this</code>
   *  @mongodb.server.release 3.6
   *  @since 1.7
   */
  MongoCollection<TDocument> deleteOne(ClientSession clientSession, JsonObject filter,
      DeleteOptions options, Handler<AsyncResult<DeleteResult>> handler);

  /**
   *  Removes all documents from the collection that match the given query filter.  If no documents match, the collection is not modified.
   *  @param filter the query filter to apply the the delete operation
   *  @return a future with a single element the DeleteResult or with an com.mongodb.MongoException
   */
  Future<DeleteResult> deleteMany(JsonObject filter);

  /**
   *  Removes all documents from the collection that match the given query filter.  If no documents match, the collection is not modified.
   *  @param filter the query filter to apply the the delete operation
   *  @param handler an async result with a single element the DeleteResult or with an com.mongodb.MongoException
   *  @return <code>this</code>
   */
  MongoCollection<TDocument> deleteMany(JsonObject filter,
      Handler<AsyncResult<DeleteResult>> handler);

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
   *  @param handler an async result with a single element the DeleteResult or with an com.mongodb.MongoException
   *  @return <code>this</code>
   *  @since 1.5
   */
  MongoCollection<TDocument> deleteMany(JsonObject filter, DeleteOptions options,
      Handler<AsyncResult<DeleteResult>> handler);

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
   *  @param handler an async result with a single element the DeleteResult or with an com.mongodb.MongoException
   *  @return <code>this</code>
   *  @mongodb.server.release 3.6
   *  @since 1.7
   */
  MongoCollection<TDocument> deleteMany(ClientSession clientSession, JsonObject filter,
      Handler<AsyncResult<DeleteResult>> handler);

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
   *  @param handler an async result with a single element the DeleteResult or with an com.mongodb.MongoException
   *  @return <code>this</code>
   *  @mongodb.server.release 3.6
   *  @since 1.7
   */
  MongoCollection<TDocument> deleteMany(ClientSession clientSession, JsonObject filter,
      DeleteOptions options, Handler<AsyncResult<DeleteResult>> handler);

  /**
   *  Replace a document in the collection according to the specified arguments.
   *  @param filter      the query filter to apply the the replace operation
   *  @param replacement the replacement document
   *  @return a future with a single element the UpdateResult
   *  @mongodb.driver.manual tutorial/modify-documents/#replace-the-document Replace
   */
  Future<UpdateResult> replaceOne(JsonObject filter, JsonObject replacement);

  /**
   *  Replace a document in the collection according to the specified arguments.
   *  @param filter      the query filter to apply the the replace operation
   *  @param replacement the replacement document
   *  @param handler an async result with a single element the UpdateResult
   *  @return <code>this</code>
   *  @mongodb.driver.manual tutorial/modify-documents/#replace-the-document Replace
   */
  MongoCollection<TDocument> replaceOne(JsonObject filter, JsonObject replacement,
      Handler<AsyncResult<UpdateResult>> handler);

  /**
   *  Replace a document in the collection according to the specified arguments.
   *  @param filter      the query filter to apply the the replace operation
   *  @param replacement the replacement document
   *  @param options     the options to apply to the replace operation
   *  @return a future with a single element the UpdateResult
   *  @mongodb.driver.manual tutorial/modify-documents/#replace-the-document Replace
   *  @since 1.8
   */
  Future<UpdateResult> replaceOne(JsonObject filter, JsonObject replacement,
      ReplaceOptions options);

  /**
   *  Replace a document in the collection according to the specified arguments.
   *  @param filter      the query filter to apply the the replace operation
   *  @param replacement the replacement document
   *  @param options     the options to apply to the replace operation
   *  @param handler an async result with a single element the UpdateResult
   *  @return <code>this</code>
   *  @mongodb.driver.manual tutorial/modify-documents/#replace-the-document Replace
   *  @since 1.8
   */
  MongoCollection<TDocument> replaceOne(JsonObject filter, JsonObject replacement,
      ReplaceOptions options, Handler<AsyncResult<UpdateResult>> handler);

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
      JsonObject replacement);

  /**
   *  Replace a document in the collection according to the specified arguments.
   *  @param clientSession the client session with which to associate this operation
   *  @param filter      the query filter to apply the the replace operation
   *  @param replacement the replacement document
   *  @param handler an async result with a single element the UpdateResult
   *  @return <code>this</code>
   *  @mongodb.driver.manual tutorial/modify-documents/#replace-the-document Replace
   *  @mongodb.server.release 3.6
   *  @since 1.7
   */
  MongoCollection<TDocument> replaceOne(ClientSession clientSession, JsonObject filter,
      JsonObject replacement, Handler<AsyncResult<UpdateResult>> handler);

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
      JsonObject replacement, ReplaceOptions options);

  /**
   *  Replace a document in the collection according to the specified arguments.
   *  @param clientSession the client session with which to associate this operation
   *  @param filter      the query filter to apply the the replace operation
   *  @param replacement the replacement document
   *  @param options     the options to apply to the replace operation
   *  @param handler an async result with a single element the UpdateResult
   *  @return <code>this</code>
   *  @mongodb.driver.manual tutorial/modify-documents/#replace-the-document Replace
   *  @mongodb.server.release 3.6
   *  @since 1.8
   */
  MongoCollection<TDocument> replaceOne(ClientSession clientSession, JsonObject filter,
      JsonObject replacement, ReplaceOptions options, Handler<AsyncResult<UpdateResult>> handler);

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
   *  @param handler an async result with a single element the UpdateResult
   *  @return <code>this</code>
   *  @mongodb.driver.manual tutorial/modify-documents/ Updates
   *  @mongodb.driver.manual reference/operator/update/ Update Operators
   */
  MongoCollection<TDocument> updateOne(JsonObject filter, JsonObject update,
      Handler<AsyncResult<UpdateResult>> handler);

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
   *  @param handler an async result with a single element the UpdateResult
   *  @return <code>this</code>
   *  @mongodb.driver.manual tutorial/modify-documents/ Updates
   *  @mongodb.driver.manual reference/operator/update/ Update Operators
   */
  MongoCollection<TDocument> updateOne(JsonObject filter, JsonObject update, UpdateOptions options,
      Handler<AsyncResult<UpdateResult>> handler);

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
   *  @param handler an async result with a single element the UpdateResult
   *  @return <code>this</code>
   *  @mongodb.driver.manual tutorial/modify-documents/ Updates
   *  @mongodb.driver.manual reference/operator/update/ Update Operators
   *  @mongodb.server.release 3.6
   *  @since 1.7
   */
  MongoCollection<TDocument> updateOne(ClientSession clientSession, JsonObject filter,
      JsonObject update, Handler<AsyncResult<UpdateResult>> handler);

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
   *  @param handler an async result with a single element the UpdateResult
   *  @return <code>this</code>
   *  @mongodb.driver.manual tutorial/modify-documents/ Updates
   *  @mongodb.driver.manual reference/operator/update/ Update Operators
   *  @mongodb.server.release 3.6
   *  @since 1.7
   */
  MongoCollection<TDocument> updateOne(ClientSession clientSession, JsonObject filter,
      JsonObject update, UpdateOptions options, Handler<AsyncResult<UpdateResult>> handler);

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
   *  @param handler an async result with a single element the UpdateResult
   *  @return <code>this</code>
   *  @since 1.12
   *  @mongodb.server.release 4.2
   *  @mongodb.driver.manual tutorial/modify-documents/ Updates
   *  @mongodb.driver.manual reference/operator/update/ Update Operators
   */
  MongoCollection<TDocument> updateOne(JsonObject filter, List<JsonObject> update,
      Handler<AsyncResult<UpdateResult>> handler);

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
   *  @param handler an async result with a single element the UpdateResult
   *  @return <code>this</code>
   *  @since 1.12
   *  @mongodb.server.release 4.2
   *  @mongodb.driver.manual tutorial/modify-documents/ Updates
   *  @mongodb.driver.manual reference/operator/update/ Update Operators
   */
  MongoCollection<TDocument> updateOne(JsonObject filter, List<JsonObject> update,
      UpdateOptions options, Handler<AsyncResult<UpdateResult>> handler);

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
   *  @param handler an async result with a single element the UpdateResult
   *  @return <code>this</code>
   *  @since 1.12
   *  @mongodb.server.release 4.2
   *  @mongodb.driver.manual tutorial/modify-documents/ Updates
   *  @mongodb.driver.manual reference/operator/update/ Update Operators
   */
  MongoCollection<TDocument> updateOne(ClientSession clientSession, JsonObject filter,
      List<JsonObject> update, Handler<AsyncResult<UpdateResult>> handler);

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
   *  @param handler an async result with a single element the UpdateResult
   *  @return <code>this</code>
   *  @since 1.12
   *  @mongodb.server.release 4.2
   *  @mongodb.driver.manual tutorial/modify-documents/ Updates
   *  @mongodb.driver.manual reference/operator/update/ Update Operators
   */
  MongoCollection<TDocument> updateOne(ClientSession clientSession, JsonObject filter,
      List<JsonObject> update, UpdateOptions options, Handler<AsyncResult<UpdateResult>> handler);

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
   *  @param handler an async result with a single element the UpdateResult
   *  @return <code>this</code>
   *  @mongodb.driver.manual tutorial/modify-documents/ Updates
   *  @mongodb.driver.manual reference/operator/update/ Update Operators
   */
  MongoCollection<TDocument> updateMany(JsonObject filter, JsonObject update,
      Handler<AsyncResult<UpdateResult>> handler);

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
   *  @param handler an async result with a single element the UpdateResult
   *  @return <code>this</code>
   *  @mongodb.driver.manual tutorial/modify-documents/ Updates
   *  @mongodb.driver.manual reference/operator/update/ Update Operators
   */
  MongoCollection<TDocument> updateMany(JsonObject filter, JsonObject update, UpdateOptions options,
      Handler<AsyncResult<UpdateResult>> handler);

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
   *  @param handler an async result with a single element the UpdateResult
   *  @return <code>this</code>
   *  @mongodb.driver.manual tutorial/modify-documents/ Updates
   *  @mongodb.driver.manual reference/operator/update/ Update Operators
   *  @mongodb.server.release 3.6
   *  @since 1.7
   */
  MongoCollection<TDocument> updateMany(ClientSession clientSession, JsonObject filter,
      JsonObject update, Handler<AsyncResult<UpdateResult>> handler);

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
   *  @param handler an async result with a single element the UpdateResult
   *  @return <code>this</code>
   *  @mongodb.driver.manual tutorial/modify-documents/ Updates
   *  @mongodb.driver.manual reference/operator/update/ Update Operators
   *  @mongodb.server.release 3.6
   *  @since 1.7
   */
  MongoCollection<TDocument> updateMany(ClientSession clientSession, JsonObject filter,
      JsonObject update, UpdateOptions options, Handler<AsyncResult<UpdateResult>> handler);

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
   *  @param handler an async result with a single element the UpdateResult
   *  @return <code>this</code>
   *  @since 1.12
   *  @mongodb.server.release 4.2
   *  @mongodb.driver.manual tutorial/modify-documents/ Updates
   *  @mongodb.driver.manual reference/operator/update/ Update Operators
   */
  MongoCollection<TDocument> updateMany(JsonObject filter, List<JsonObject> update,
      Handler<AsyncResult<UpdateResult>> handler);

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
   *  @param handler an async result with a single element the UpdateResult
   *  @return <code>this</code>
   *  @since 1.12
   *  @mongodb.server.release 4.2
   *  @mongodb.driver.manual tutorial/modify-documents/ Updates
   *  @mongodb.driver.manual reference/operator/update/ Update Operators
   */
  MongoCollection<TDocument> updateMany(JsonObject filter, List<JsonObject> update,
      UpdateOptions options, Handler<AsyncResult<UpdateResult>> handler);

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
   *  @param handler an async result with a single element the UpdateResult
   *  @return <code>this</code>
   *  @since 1.12
   *  @mongodb.server.release 4.2
   *  @mongodb.driver.manual tutorial/modify-documents/ Updates
   *  @mongodb.driver.manual reference/operator/update/ Update Operators
   */
  MongoCollection<TDocument> updateMany(ClientSession clientSession, JsonObject filter,
      List<JsonObject> update, Handler<AsyncResult<UpdateResult>> handler);

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
   *  @param handler an async result with a single element the UpdateResult
   *  @return <code>this</code>
   *  @since 1.12
   *  @mongodb.server.release 4.2
   *  @mongodb.driver.manual tutorial/modify-documents/ Updates
   *  @mongodb.driver.manual reference/operator/update/ Update Operators
   */
  MongoCollection<TDocument> updateMany(ClientSession clientSession, JsonObject filter,
      List<JsonObject> update, UpdateOptions options, Handler<AsyncResult<UpdateResult>> handler);

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
   *  @param handler an async result with a single element the document that was removed.  If no documents matched the query filter, then null will be
   *  @return <code>this</code>
   *  returned
   */
  MongoCollection<TDocument> findOneAndDelete(JsonObject filter,
      Handler<AsyncResult<TDocument>> handler);

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
   *  @param handler an async result with a single element the document that was removed.  If no documents matched the query filter, then null will be
   *  @return <code>this</code>
   *  returned
   */
  MongoCollection<TDocument> findOneAndDelete(JsonObject filter, FindOneAndDeleteOptions options,
      Handler<AsyncResult<TDocument>> handler);

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
   *  @param handler an async result with a single element the document that was removed.  If no documents matched the query filter, then null will be
   *  @return <code>this</code>
   *  returned
   *  @mongodb.server.release 3.6
   *  @since 1.7
   */
  MongoCollection<TDocument> findOneAndDelete(ClientSession clientSession, JsonObject filter,
      Handler<AsyncResult<TDocument>> handler);

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
   *  @param handler an async result with a single element the document that was removed.  If no documents matched the query filter, then null will be
   *  @return <code>this</code>
   *  returned
   *  @mongodb.server.release 3.6
   *  @since 1.7
   */
  MongoCollection<TDocument> findOneAndDelete(ClientSession clientSession, JsonObject filter,
      FindOneAndDeleteOptions options, Handler<AsyncResult<TDocument>> handler);

  /**
   *  Atomically find a document and replace it.
   *  @param filter      the query filter to apply the the replace operation
   *  @param replacement the replacement document
   *  @return a future with a single element the document that was replaced.  Depending on the value of the {@code returnOriginal}
   *  property, this will either be the document as it was before the update or as it is after the update.  If no documents matched the
   *  query filter, then null will be returned
   */
  Future<TDocument> findOneAndReplace(JsonObject filter, JsonObject replacement);

  /**
   *  Atomically find a document and replace it.
   *  @param filter      the query filter to apply the the replace operation
   *  @param replacement the replacement document
   *  @param handler an async result with a single element the document that was replaced.  Depending on the value of the {@code returnOriginal}
   *  @return <code>this</code>
   *  property, this will either be the document as it was before the update or as it is after the update.  If no documents matched the
   *  query filter, then null will be returned
   */
  MongoCollection<TDocument> findOneAndReplace(JsonObject filter, JsonObject replacement,
      Handler<AsyncResult<TDocument>> handler);

  /**
   *  Atomically find a document and replace it.
   *  @param filter      the query filter to apply the the replace operation
   *  @param replacement the replacement document
   *  @param options     the options to apply to the operation
   *  @return a future with a single element the document that was replaced.  Depending on the value of the {@code returnOriginal}
   *  property, this will either be the document as it was before the update or as it is after the update.  If no documents matched the
   *  query filter, then null will be returned
   */
  Future<TDocument> findOneAndReplace(JsonObject filter, JsonObject replacement,
      FindOneAndReplaceOptions options);

  /**
   *  Atomically find a document and replace it.
   *  @param filter      the query filter to apply the the replace operation
   *  @param replacement the replacement document
   *  @param options     the options to apply to the operation
   *  @param handler an async result with a single element the document that was replaced.  Depending on the value of the {@code returnOriginal}
   *  @return <code>this</code>
   *  property, this will either be the document as it was before the update or as it is after the update.  If no documents matched the
   *  query filter, then null will be returned
   */
  MongoCollection<TDocument> findOneAndReplace(JsonObject filter, JsonObject replacement,
      FindOneAndReplaceOptions options, Handler<AsyncResult<TDocument>> handler);

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
      JsonObject replacement);

  /**
   *  Atomically find a document and replace it.
   *  @param clientSession the client session with which to associate this operation
   *  @param filter      the query filter to apply the the replace operation
   *  @param replacement the replacement document
   *  @param handler an async result with a single element the document that was replaced.  Depending on the value of the {@code returnOriginal}
   *  @return <code>this</code>
   *  property, this will either be the document as it was before the update or as it is after the update.  If no documents matched the
   *  query filter, then null will be returned
   *  @mongodb.server.release 3.6
   *  @since 1.7
   */
  MongoCollection<TDocument> findOneAndReplace(ClientSession clientSession, JsonObject filter,
      JsonObject replacement, Handler<AsyncResult<TDocument>> handler);

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
      JsonObject replacement, FindOneAndReplaceOptions options);

  /**
   *  Atomically find a document and replace it.
   *  @param clientSession the client session with which to associate this operation
   *  @param filter      the query filter to apply the the replace operation
   *  @param replacement the replacement document
   *  @param options     the options to apply to the operation
   *  @param handler an async result with a single element the document that was replaced.  Depending on the value of the {@code returnOriginal}
   *  @return <code>this</code>
   *  property, this will either be the document as it was before the update or as it is after the update.  If no documents matched the
   *  query filter, then null will be returned
   *  @mongodb.server.release 3.6
   *  @since 1.7
   */
  MongoCollection<TDocument> findOneAndReplace(ClientSession clientSession, JsonObject filter,
      JsonObject replacement, FindOneAndReplaceOptions options,
      Handler<AsyncResult<TDocument>> handler);

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
   *  @param handler an async result with a single element the document that was updated before the update was applied.  If no documents matched the
   *  @return <code>this</code>
   *  query filter, then null will be returned
   */
  MongoCollection<TDocument> findOneAndUpdate(JsonObject filter, JsonObject update,
      Handler<AsyncResult<TDocument>> handler);

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
   *  @param handler an async result with a single element the document that was updated.  Depending on the value of the {@code returnOriginal}
   *  @return <code>this</code>
   *  property, this will either be the document as it was before the update or as it is after the update.  If no documents matched the
   *  query filter, then null will be returned
   */
  MongoCollection<TDocument> findOneAndUpdate(JsonObject filter, JsonObject update,
      FindOneAndUpdateOptions options, Handler<AsyncResult<TDocument>> handler);

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
   *  @param handler an async result with a single element the document that was updated before the update was applied.  If no documents matched the
   *  @return <code>this</code>
   *  query filter, then null will be returned
   *  @mongodb.server.release 3.6
   *  @since 1.7
   */
  MongoCollection<TDocument> findOneAndUpdate(ClientSession clientSession, JsonObject filter,
      JsonObject update, Handler<AsyncResult<TDocument>> handler);

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
   *  @param handler an async result with a single element the document that was updated.  Depending on the value of the {@code returnOriginal}
   *  @return <code>this</code>
   *  property, this will either be the document as it was before the update or as it is after the update.  If no documents matched the
   *  query filter, then null will be returned
   *  @mongodb.server.release 3.6
   *  @since 1.7
   */
  MongoCollection<TDocument> findOneAndUpdate(ClientSession clientSession, JsonObject filter,
      JsonObject update, FindOneAndUpdateOptions options, Handler<AsyncResult<TDocument>> handler);

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
   *  @param handler an async result with a single element the document that was updated.  Depending on the value of the {@code returnOriginal}
   *  @return <code>this</code>
   *  property, this will either be the document as it was before the update or as it is after the update.  If no documents matched the
   *  query filter, then null will be returned
   *  @since 1.12
   *  @mongodb.server.release 4.2
   */
  MongoCollection<TDocument> findOneAndUpdate(JsonObject filter, List<JsonObject> update,
      Handler<AsyncResult<TDocument>> handler);

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
   *  @param handler an async result with a single element the document that was updated.  Depending on the value of the {@code returnOriginal}
   *  @return <code>this</code>
   *  property, this will either be the document as it was before the update or as it is after the update.  If no documents matched the
   *  query filter, then null will be returned
   *  @since 1.12
   *  @mongodb.server.release 4.2
   */
  MongoCollection<TDocument> findOneAndUpdate(JsonObject filter, List<JsonObject> update,
      FindOneAndUpdateOptions options, Handler<AsyncResult<TDocument>> handler);

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
   *  @param handler an async result with a single element the document that was updated.  Depending on the value of the {@code returnOriginal}
   *  @return <code>this</code>
   *  property, this will either be the document as it was before the update or as it is after the update.  If no documents matched the
   *  query filter, then null will be returned
   *  @since 1.12
   *  @mongodb.server.release 4.2
   */
  MongoCollection<TDocument> findOneAndUpdate(ClientSession clientSession, JsonObject filter,
      List<JsonObject> update, Handler<AsyncResult<TDocument>> handler);

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
   *  @param handler an async result with a single element the document that was updated.  Depending on the value of the {@code returnOriginal}
   *  @return <code>this</code>
   *  property, this will either be the document as it was before the update or as it is after the update.  If no documents matched the
   *  query filter, then null will be returned
   *  @since 1.12
   *  @mongodb.server.release 4.2
   */
  MongoCollection<TDocument> findOneAndUpdate(ClientSession clientSession, JsonObject filter,
      List<JsonObject> update, FindOneAndUpdateOptions options,
      Handler<AsyncResult<TDocument>> handler);

  /**
   *  Drops this collection from the Database.
   *  @return an empty future that indicates when the operation has completed
   *  @mongodb.driver.manual reference/command/drop/ Drop Collection
   */
  Future<Void> drop();

  /**
   *  Drops this collection from the Database.
   *  @param handler an empty async result that indicates when the operation has completed
   *  @return <code>this</code>
   *  @mongodb.driver.manual reference/command/drop/ Drop Collection
   */
  MongoCollection<TDocument> drop(Handler<AsyncResult<Void>> handler);

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
   *  @param handler an empty async result that indicates when the operation has completed
   *  @return <code>this</code>
   *  @mongodb.driver.manual reference/command/drop/ Drop Collection
   *  @mongodb.server.release 3.6
   *  @since 1.7
   */
  MongoCollection<TDocument> drop(ClientSession clientSession, Handler<AsyncResult<Void>> handler);

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
   *  @param handler an empty async result that indicates when the operation has completed
   *  @return <code>this</code>
   *  @mongodb.driver.manual reference/method/db.collection.ensureIndex Ensure Index
   */
  MongoCollection<TDocument> createIndex(JsonObject key, Handler<AsyncResult<String>> handler);

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
   *  @param handler an empty async result that indicates when the operation has completed
   *  @return <code>this</code>
   *  @mongodb.driver.manual reference/method/db.collection.ensureIndex Ensure Index
   */
  MongoCollection<TDocument> createIndex(JsonObject key, IndexOptions options,
      Handler<AsyncResult<String>> handler);

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
   *  @param handler an empty async result that indicates when the operation has completed
   *  @return <code>this</code>
   *  @mongodb.driver.manual reference/method/db.collection.ensureIndex Ensure Index
   *  @mongodb.server.release 3.6
   *  @since 1.7
   */
  MongoCollection<TDocument> createIndex(ClientSession clientSession, JsonObject key,
      Handler<AsyncResult<String>> handler);

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
   *  @param handler an empty async result that indicates when the operation has completed
   *  @return <code>this</code>
   *  @mongodb.driver.manual reference/method/db.collection.ensureIndex Ensure Index
   *  @mongodb.server.release 3.6
   *  @since 1.7
   */
  MongoCollection<TDocument> createIndex(ClientSession clientSession, JsonObject key,
      IndexOptions options, Handler<AsyncResult<String>> handler);

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
   *  @param handler an empty async result that indicates when the operation has completed
   *  @return <code>this</code>
   *  @mongodb.driver.manual reference/command/createIndexes Create indexes
   */
  MongoCollection<TDocument> createIndexes(List<IndexModel> indexes,
      Handler<AsyncResult<String>> handler);

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
   *  @param handler an empty async result that indicates when the operation has completed
   *  @return <code>this</code>
   *  @mongodb.driver.manual reference/command/createIndexes Create indexes
   *  @since 1.7
   */
  MongoCollection<TDocument> createIndexes(List<IndexModel> indexes,
      CreateIndexOptions createIndexOptions, Handler<AsyncResult<String>> handler);

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
   *  @param handler an empty async result that indicates when the operation has completed
   *  @return <code>this</code>
   *  @mongodb.driver.manual reference/command/createIndexes Create indexes
   *  @mongodb.server.release 3.6
   *  @since 1.7
   */
  MongoCollection<TDocument> createIndexes(ClientSession clientSession, List<IndexModel> indexes,
      Handler<AsyncResult<String>> handler);

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
   *  @param handler an empty async result that indicates when the operation has completed
   *  @return <code>this</code>
   *  @mongodb.driver.manual reference/command/createIndexes Create indexes
   *  @mongodb.server.release 3.6
   *  @since 1.7
   */
  MongoCollection<TDocument> createIndexes(ClientSession clientSession, List<IndexModel> indexes,
      CreateIndexOptions createIndexOptions, Handler<AsyncResult<String>> handler);

  /**
   *  Get all the indexes in this collection.
   *  @return the fluent list indexes interface
   *  @mongodb.driver.manual reference/command/listIndexes/ listIndexes
   */
  MongoResult<JsonObject> listIndexes();

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
   *  Drops the given index.
   *  @param indexName the name of the index to remove
   *  @return an empty future that indicates when the operation has completed
   *  @mongodb.driver.manual reference/command/dropIndexes/ Drop Indexes
   */
  Future<Void> dropIndex(String indexName);

  /**
   *  Drops the given index.
   *  @param indexName the name of the index to remove
   *  @param handler an empty async result that indicates when the operation has completed
   *  @return <code>this</code>
   *  @mongodb.driver.manual reference/command/dropIndexes/ Drop Indexes
   */
  MongoCollection<TDocument> dropIndex(String indexName, Handler<AsyncResult<Void>> handler);

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
   *  @param handler an empty async result that indicates when the operation has completed
   *  @return <code>this</code>
   *  @mongodb.driver.manual reference/command/dropIndexes/ Drop indexes
   */
  MongoCollection<TDocument> dropIndex(JsonObject keys, Handler<AsyncResult<Void>> handler);

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
   *  @param handler an empty async result that indicates when the operation has completed
   *  @return <code>this</code>
   *  @mongodb.driver.manual reference/command/dropIndexes/ Drop Indexes
   *  @since 1.7
   */
  MongoCollection<TDocument> dropIndex(String indexName, DropIndexOptions dropIndexOptions,
      Handler<AsyncResult<Void>> handler);

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
   *  @param handler an empty async result that indicates when the operation has completed
   *  @return <code>this</code>
   *  @mongodb.driver.manual reference/command/dropIndexes/ Drop indexes
   *  @since 1.7
   */
  MongoCollection<TDocument> dropIndex(JsonObject keys, DropIndexOptions dropIndexOptions,
      Handler<AsyncResult<Void>> handler);

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
   *  @param handler an empty async result that indicates when the operation has completed
   *  @return <code>this</code>
   *  @mongodb.driver.manual reference/command/dropIndexes/ Drop Indexes
   *  @mongodb.server.release 3.6
   *  @since 1.7
   */
  MongoCollection<TDocument> dropIndex(ClientSession clientSession, String indexName,
      Handler<AsyncResult<Void>> handler);

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
   *  @param handler an empty async result that indicates when the operation has completed
   *  @return <code>this</code>
   *  @mongodb.driver.manual reference/command/dropIndexes/ Drop indexes
   *  @mongodb.server.release 3.6
   *  @since 1.7
   */
  MongoCollection<TDocument> dropIndex(ClientSession clientSession, JsonObject keys,
      Handler<AsyncResult<Void>> handler);

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
   *  @param handler an empty async result that indicates when the operation has completed
   *  @return <code>this</code>
   *  @mongodb.driver.manual reference/command/dropIndexes/ Drop Indexes
   *  @mongodb.server.release 3.6
   *  @since 1.7
   */
  MongoCollection<TDocument> dropIndex(ClientSession clientSession, String indexName,
      DropIndexOptions dropIndexOptions, Handler<AsyncResult<Void>> handler);

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
   *  @param handler an empty async result that indicates when the operation has completed
   *  @return <code>this</code>
   *  @mongodb.driver.manual reference/command/dropIndexes/ Drop indexes
   *  @mongodb.server.release 3.6
   *  @since 1.7
   */
  MongoCollection<TDocument> dropIndex(ClientSession clientSession, JsonObject keys,
      DropIndexOptions dropIndexOptions, Handler<AsyncResult<Void>> handler);

  /**
   *  Drop all the indexes on this collection, except for the default on _id.
   *  @return an empty future that indicates when the operation has completed
   *  @mongodb.driver.manual reference/command/dropIndexes/ Drop Indexes
   */
  Future<Void> dropIndexes();

  /**
   *  Drop all the indexes on this collection, except for the default on _id.
   *  @param handler an empty async result that indicates when the operation has completed
   *  @return <code>this</code>
   *  @mongodb.driver.manual reference/command/dropIndexes/ Drop Indexes
   */
  MongoCollection<TDocument> dropIndexes(Handler<AsyncResult<Void>> handler);

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
   *  @param handler an empty async result that indicates when the operation has completed
   *  @return <code>this</code>
   *  @mongodb.driver.manual reference/command/dropIndexes/ Drop Indexes
   *  @since 1.7
   */
  MongoCollection<TDocument> dropIndexes(DropIndexOptions dropIndexOptions,
      Handler<AsyncResult<Void>> handler);

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
   *  @param handler an empty async result that indicates when the operation has completed
   *  @return <code>this</code>
   *  @mongodb.driver.manual reference/command/dropIndexes/ Drop Indexes
   *  @mongodb.server.release 3.6
   *  @since 1.7
   */
  MongoCollection<TDocument> dropIndexes(ClientSession clientSession,
      Handler<AsyncResult<Void>> handler);

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
   *  @param handler an empty async result that indicates when the operation has completed
   *  @return <code>this</code>
   *  @mongodb.driver.manual reference/command/dropIndexes/ Drop Indexes
   *  @mongodb.server.release 3.6
   *  @since 1.7
   */
  MongoCollection<TDocument> dropIndexes(ClientSession clientSession,
      DropIndexOptions dropIndexOptions, Handler<AsyncResult<Void>> handler);

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
   *  @param handler an empty async result that indicates when the operation has completed
   *  @return <code>this</code>
   *  @mongodb.driver.manual reference/commands/renameCollection Rename collection
   */
  MongoCollection<TDocument> renameCollection(MongoNamespace newCollectionNamespace,
      Handler<AsyncResult<Void>> handler);

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
   *  @param handler an empty async result that indicates when the operation has completed
   *  @return <code>this</code>
   *  @mongodb.driver.manual reference/commands/renameCollection Rename collection
   */
  MongoCollection<TDocument> renameCollection(MongoNamespace newCollectionNamespace,
      RenameCollectionOptions options, Handler<AsyncResult<Void>> handler);

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
   *  @param handler an empty async result that indicates when the operation has completed
   *  @return <code>this</code>
   *  @mongodb.driver.manual reference/commands/renameCollection Rename collection
   *  @mongodb.server.release 3.6
   *  @since 1.7
   */
  MongoCollection<TDocument> renameCollection(ClientSession clientSession,
      MongoNamespace newCollectionNamespace, Handler<AsyncResult<Void>> handler);

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
   *  @param handler an empty async result that indicates when the operation has completed
   *  @return <code>this</code>
   *  @mongodb.driver.manual reference/commands/renameCollection Rename collection
   *  @mongodb.server.release 3.6
   *  @since 1.7
   */
  MongoCollection<TDocument> renameCollection(ClientSession clientSession,
      MongoNamespace newCollectionNamespace, RenameCollectionOptions options,
      Handler<AsyncResult<Void>> handler);
}
