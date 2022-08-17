package io.vertx.mongo.client.impl;

import static io.vertx.mongo.impl.Utils.setHandler;
import static java.util.Objects.requireNonNull;

import com.mongodb.bulk.BulkWriteResult;
import com.mongodb.client.model.IndexModel;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.InsertManyResult;
import com.mongodb.client.result.InsertOneResult;
import com.mongodb.client.result.UpdateResult;
import com.mongodb.reactivestreams.client.MongoCollection;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.core.streams.ReadStream;
import io.vertx.mongo.MongoNamespace;
import io.vertx.mongo.ReadConcern;
import io.vertx.mongo.ReadPreference;
import io.vertx.mongo.WriteConcern;
import io.vertx.mongo.client.AggregateOptions;
import io.vertx.mongo.client.ChangeStreamOptions;
import io.vertx.mongo.client.ClientSession;
import io.vertx.mongo.client.FindOptions;
import io.vertx.mongo.client.ListIndexesOptions;
import io.vertx.mongo.client.MapReduceOptions;
import io.vertx.mongo.client.MongoResult;
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
import io.vertx.mongo.impl.ConversionUtilsImpl;
import java.lang.Class;
import java.lang.Long;
import java.lang.Override;
import java.lang.String;
import java.lang.Void;
import java.util.List;
import org.bson.conversions.Bson;

public class MongoCollectionImpl<TDocument> extends MongoCollectionBase<TDocument> {
  protected MongoCollection<TDocument> wrapped;

  @Override
  public MongoNamespace getNamespace() {
    wrapped.getNamespace();
    return null;
  }

  @Override
  public Class<TDocument> getDocumentClass() {
    wrapped.getDocumentClass();
    return null;
  }

  @Override
  public ReadPreference getReadPreference() {
    wrapped.getReadPreference();
    return null;
  }

  @Override
  public WriteConcern getWriteConcern() {
    wrapped.getWriteConcern();
    return null;
  }

  @Override
  public ReadConcern getReadConcern() {
    wrapped.getReadConcern();
    return null;
  }

  @Override
  public <NewTDocument> io.vertx.mongo.client.MongoCollection withDocumentClass(
      Class<NewTDocument> clazz) {
    requireNonNull(clazz, "clazz cannot be null");
    wrapped.withDocumentClass(clazz);
    return null;
  }

  @Override
  public io.vertx.mongo.client.MongoCollection<TDocument> withReadPreference(
      ReadPreference readPreference) {
    requireNonNull(readPreference, "readPreference cannot be null");
    com.mongodb.ReadPreference __readPreference = readPreference.toDriverClass();
    wrapped.withReadPreference(__readPreference);
    return null;
  }

  @Override
  public io.vertx.mongo.client.MongoCollection<TDocument> withWriteConcern(
      WriteConcern writeConcern) {
    requireNonNull(writeConcern, "writeConcern cannot be null");
    com.mongodb.WriteConcern __writeConcern = writeConcern.toDriverClass();
    wrapped.withWriteConcern(__writeConcern);
    return null;
  }

  @Override
  public io.vertx.mongo.client.MongoCollection<TDocument> withReadConcern(ReadConcern readConcern) {
    requireNonNull(readConcern, "readConcern cannot be null");
    com.mongodb.ReadConcern __readConcern = readConcern.toDriverClass();
    wrapped.withReadConcern(__readConcern);
    return null;
  }

  @Override
  public Future<Long> estimatedDocumentCount() {
    wrapped.estimatedDocumentCount();
    return null;
  }

  @Override
  public io.vertx.mongo.client.MongoCollection<TDocument> estimatedDocumentCount(
      Handler<AsyncResult<Long>> resultHandler) {
    Future<Long> future = this.estimatedDocumentCount();
    setHandler(future, resultHandler);
    return this;
  }

  @Override
  public Future<Long> estimatedDocumentCount(EstimatedDocumentCountOptions options) {
    requireNonNull(options, "options cannot be null");
    com.mongodb.client.model.EstimatedDocumentCountOptions __options = options.toDriverClass();
    wrapped.estimatedDocumentCount(__options);
    return null;
  }

  @Override
  public io.vertx.mongo.client.MongoCollection<TDocument> estimatedDocumentCount(
      EstimatedDocumentCountOptions options, Handler<AsyncResult<Long>> resultHandler) {
    Future<Long> future = this.estimatedDocumentCount(options);
    setHandler(future, resultHandler);
    return this;
  }

  @Override
  public Future<Long> countDocuments() {
    wrapped.countDocuments();
    return null;
  }

  @Override
  public io.vertx.mongo.client.MongoCollection<TDocument> countDocuments(
      Handler<AsyncResult<Long>> resultHandler) {
    Future<Long> future = this.countDocuments();
    setHandler(future, resultHandler);
    return this;
  }

  @Override
  public Future<Long> countDocuments(JsonObject filter) {
    requireNonNull(filter, "filter cannot be null");
    Bson __filter = ConversionUtilsImpl.INSTANCE.toBson(filter);
    wrapped.countDocuments(__filter);
    return null;
  }

  @Override
  public io.vertx.mongo.client.MongoCollection<TDocument> countDocuments(JsonObject filter,
      Handler<AsyncResult<Long>> resultHandler) {
    Future<Long> future = this.countDocuments(filter);
    setHandler(future, resultHandler);
    return this;
  }

  @Override
  public Future<Long> countDocuments(JsonObject filter, CountOptions options) {
    requireNonNull(filter, "filter cannot be null");
    requireNonNull(options, "options cannot be null");
    Bson __filter = ConversionUtilsImpl.INSTANCE.toBson(filter);
    com.mongodb.client.model.CountOptions __options = options.toDriverClass();
    wrapped.countDocuments(__filter, __options);
    return null;
  }

  @Override
  public io.vertx.mongo.client.MongoCollection<TDocument> countDocuments(JsonObject filter,
      CountOptions options, Handler<AsyncResult<Long>> resultHandler) {
    Future<Long> future = this.countDocuments(filter, options);
    setHandler(future, resultHandler);
    return this;
  }

  @Override
  public Future<Long> countDocuments(ClientSession clientSession) {
    requireNonNull(clientSession, "clientSession cannot be null");
    com.mongodb.reactivestreams.client.ClientSession __clientSession = clientSession.toDriverClass();
    wrapped.countDocuments(__clientSession);
    return null;
  }

  @Override
  public io.vertx.mongo.client.MongoCollection<TDocument> countDocuments(
      ClientSession clientSession, Handler<AsyncResult<Long>> resultHandler) {
    Future<Long> future = this.countDocuments(clientSession);
    setHandler(future, resultHandler);
    return this;
  }

  @Override
  public Future<Long> countDocuments(ClientSession clientSession, JsonObject filter) {
    requireNonNull(clientSession, "clientSession cannot be null");
    requireNonNull(filter, "filter cannot be null");
    com.mongodb.reactivestreams.client.ClientSession __clientSession = clientSession.toDriverClass();
    Bson __filter = ConversionUtilsImpl.INSTANCE.toBson(filter);
    wrapped.countDocuments(__clientSession, __filter);
    return null;
  }

  @Override
  public io.vertx.mongo.client.MongoCollection<TDocument> countDocuments(
      ClientSession clientSession, JsonObject filter, Handler<AsyncResult<Long>> resultHandler) {
    Future<Long> future = this.countDocuments(clientSession, filter);
    setHandler(future, resultHandler);
    return this;
  }

  @Override
  public Future<Long> countDocuments(ClientSession clientSession, JsonObject filter,
      CountOptions options) {
    requireNonNull(clientSession, "clientSession cannot be null");
    requireNonNull(filter, "filter cannot be null");
    requireNonNull(options, "options cannot be null");
    com.mongodb.reactivestreams.client.ClientSession __clientSession = clientSession.toDriverClass();
    Bson __filter = ConversionUtilsImpl.INSTANCE.toBson(filter);
    com.mongodb.client.model.CountOptions __options = options.toDriverClass();
    wrapped.countDocuments(__clientSession, __filter, __options);
    return null;
  }

  @Override
  public io.vertx.mongo.client.MongoCollection<TDocument> countDocuments(
      ClientSession clientSession, JsonObject filter, CountOptions options,
      Handler<AsyncResult<Long>> resultHandler) {
    Future<Long> future = this.countDocuments(clientSession, filter, options);
    setHandler(future, resultHandler);
    return this;
  }

  @Override
  public MongoResult<TDocument> find() {
    wrapped.find();
    return null;
  }

  @Override
  public MongoResult<TDocument> find(FindOptions options) {
    return null;
  }

  @Override
  public MongoResult<TDocument> find(JsonObject filter) {
    requireNonNull(filter, "filter cannot be null");
    Bson __filter = ConversionUtilsImpl.INSTANCE.toBson(filter);
    wrapped.find(__filter);
    return null;
  }

  @Override
  public MongoResult<TDocument> find(JsonObject filter, FindOptions options) {
    return null;
  }

  @Override
  public MongoResult<TDocument> find(ClientSession clientSession) {
    requireNonNull(clientSession, "clientSession cannot be null");
    com.mongodb.reactivestreams.client.ClientSession __clientSession = clientSession.toDriverClass();
    wrapped.find(__clientSession);
    return null;
  }

  @Override
  public MongoResult<TDocument> find(ClientSession clientSession, FindOptions options) {
    return null;
  }

  @Override
  public MongoResult<TDocument> find(ClientSession clientSession, JsonObject filter) {
    requireNonNull(clientSession, "clientSession cannot be null");
    requireNonNull(filter, "filter cannot be null");
    com.mongodb.reactivestreams.client.ClientSession __clientSession = clientSession.toDriverClass();
    Bson __filter = ConversionUtilsImpl.INSTANCE.toBson(filter);
    wrapped.find(__clientSession, __filter);
    return null;
  }

  @Override
  public MongoResult<TDocument> find(ClientSession clientSession, JsonObject filter,
      FindOptions options) {
    return null;
  }

  @Override
  public MongoResult<TDocument> aggregate(List<JsonObject> pipeline) {
    requireNonNull(pipeline, "pipeline cannot be null");
    List<? extends Bson> __pipeline = ConversionUtilsImpl.INSTANCE.toBsonList(pipeline);
    wrapped.aggregate(__pipeline);
    return null;
  }

  @Override
  public MongoResult<TDocument> aggregate(List<JsonObject> pipeline, AggregateOptions options) {
    return null;
  }

  @Override
  public MongoResult<TDocument> aggregate(ClientSession clientSession, List<JsonObject> pipeline) {
    requireNonNull(clientSession, "clientSession cannot be null");
    requireNonNull(pipeline, "pipeline cannot be null");
    com.mongodb.reactivestreams.client.ClientSession __clientSession = clientSession.toDriverClass();
    List<? extends Bson> __pipeline = ConversionUtilsImpl.INSTANCE.toBsonList(pipeline);
    wrapped.aggregate(__clientSession, __pipeline);
    return null;
  }

  @Override
  public MongoResult<TDocument> aggregate(ClientSession clientSession, List<JsonObject> pipeline,
      AggregateOptions options) {
    return null;
  }

  @Override
  public ReadStream<JsonObject> watch() {
    wrapped.watch();
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
    wrapped.watch(__pipeline);
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
    wrapped.watch(__clientSession);
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
    wrapped.watch(__clientSession, __pipeline);
    return null;
  }

  @Override
  public ReadStream<JsonObject> watch(ClientSession clientSession, List<JsonObject> pipeline,
      ChangeStreamOptions options) {
    return null;
  }

  @Override
  public MongoResult<TDocument> mapReduce(String mapFunction, String reduceFunction) {
    requireNonNull(mapFunction, "mapFunction cannot be null");
    requireNonNull(reduceFunction, "reduceFunction cannot be null");
    wrapped.mapReduce(mapFunction, reduceFunction);
    return null;
  }

  @Override
  public MongoResult<TDocument> mapReduce(String mapFunction, String reduceFunction,
      MapReduceOptions options) {
    return null;
  }

  @Override
  public MongoResult<TDocument> mapReduce(ClientSession clientSession, String mapFunction,
      String reduceFunction) {
    requireNonNull(clientSession, "clientSession cannot be null");
    requireNonNull(mapFunction, "mapFunction cannot be null");
    requireNonNull(reduceFunction, "reduceFunction cannot be null");
    com.mongodb.reactivestreams.client.ClientSession __clientSession = clientSession.toDriverClass();
    wrapped.mapReduce(__clientSession, mapFunction, reduceFunction);
    return null;
  }

  @Override
  public MongoResult<TDocument> mapReduce(ClientSession clientSession, String mapFunction,
      String reduceFunction, MapReduceOptions options) {
    return null;
  }

  @Override
  public Future<BulkWriteResult> bulkWrite(List<JsonObject> requests) {
    requireNonNull(requests, "requests cannot be null");
    List<? extends Bson> __requests = ConversionUtilsImpl.INSTANCE.toBsonList(requests);
    return null;
  }

  @Override
  public io.vertx.mongo.client.MongoCollection<TDocument> bulkWrite(List<JsonObject> requests,
      Handler<AsyncResult<BulkWriteResult>> resultHandler) {
    Future<BulkWriteResult> future = this.bulkWrite(requests);
    setHandler(future, resultHandler);
    return this;
  }

  @Override
  public Future<BulkWriteResult> bulkWrite(List<JsonObject> requests, BulkWriteOptions options) {
    requireNonNull(requests, "requests cannot be null");
    requireNonNull(options, "options cannot be null");
    List<? extends Bson> __requests = ConversionUtilsImpl.INSTANCE.toBsonList(requests);
    com.mongodb.client.model.BulkWriteOptions __options = options.toDriverClass();
    return null;
  }

  @Override
  public io.vertx.mongo.client.MongoCollection<TDocument> bulkWrite(List<JsonObject> requests,
      BulkWriteOptions options, Handler<AsyncResult<BulkWriteResult>> resultHandler) {
    Future<BulkWriteResult> future = this.bulkWrite(requests, options);
    setHandler(future, resultHandler);
    return this;
  }

  @Override
  public Future<BulkWriteResult> bulkWrite(ClientSession clientSession, List<JsonObject> requests) {
    requireNonNull(clientSession, "clientSession cannot be null");
    requireNonNull(requests, "requests cannot be null");
    com.mongodb.reactivestreams.client.ClientSession __clientSession = clientSession.toDriverClass();
    List<? extends Bson> __requests = ConversionUtilsImpl.INSTANCE.toBsonList(requests);
    return null;
  }

  @Override
  public io.vertx.mongo.client.MongoCollection<TDocument> bulkWrite(ClientSession clientSession,
      List<JsonObject> requests, Handler<AsyncResult<BulkWriteResult>> resultHandler) {
    Future<BulkWriteResult> future = this.bulkWrite(clientSession, requests);
    setHandler(future, resultHandler);
    return this;
  }

  @Override
  public Future<BulkWriteResult> bulkWrite(ClientSession clientSession, List<JsonObject> requests,
      BulkWriteOptions options) {
    requireNonNull(clientSession, "clientSession cannot be null");
    requireNonNull(requests, "requests cannot be null");
    requireNonNull(options, "options cannot be null");
    com.mongodb.reactivestreams.client.ClientSession __clientSession = clientSession.toDriverClass();
    List<? extends Bson> __requests = ConversionUtilsImpl.INSTANCE.toBsonList(requests);
    com.mongodb.client.model.BulkWriteOptions __options = options.toDriverClass();
    return null;
  }

  @Override
  public io.vertx.mongo.client.MongoCollection<TDocument> bulkWrite(ClientSession clientSession,
      List<JsonObject> requests, BulkWriteOptions options,
      Handler<AsyncResult<BulkWriteResult>> resultHandler) {
    Future<BulkWriteResult> future = this.bulkWrite(clientSession, requests, options);
    setHandler(future, resultHandler);
    return this;
  }

  @Override
  public Future<InsertOneResult> insertOne(TDocument document) {
    requireNonNull(document, "document cannot be null");
    wrapped.insertOne(document);
    return null;
  }

  @Override
  public io.vertx.mongo.client.MongoCollection<TDocument> insertOne(TDocument document,
      Handler<AsyncResult<InsertOneResult>> resultHandler) {
    Future<InsertOneResult> future = this.insertOne(document);
    setHandler(future, resultHandler);
    return this;
  }

  @Override
  public Future<InsertOneResult> insertOne(TDocument document, InsertOneOptions options) {
    requireNonNull(document, "document cannot be null");
    requireNonNull(options, "options cannot be null");
    com.mongodb.client.model.InsertOneOptions __options = options.toDriverClass();
    wrapped.insertOne(document, __options);
    return null;
  }

  @Override
  public io.vertx.mongo.client.MongoCollection<TDocument> insertOne(TDocument document,
      InsertOneOptions options, Handler<AsyncResult<InsertOneResult>> resultHandler) {
    Future<InsertOneResult> future = this.insertOne(document, options);
    setHandler(future, resultHandler);
    return this;
  }

  @Override
  public Future<InsertOneResult> insertOne(ClientSession clientSession, TDocument document) {
    requireNonNull(clientSession, "clientSession cannot be null");
    requireNonNull(document, "document cannot be null");
    com.mongodb.reactivestreams.client.ClientSession __clientSession = clientSession.toDriverClass();
    wrapped.insertOne(__clientSession, document);
    return null;
  }

  @Override
  public io.vertx.mongo.client.MongoCollection<TDocument> insertOne(ClientSession clientSession,
      TDocument document, Handler<AsyncResult<InsertOneResult>> resultHandler) {
    Future<InsertOneResult> future = this.insertOne(clientSession, document);
    setHandler(future, resultHandler);
    return this;
  }

  @Override
  public Future<InsertOneResult> insertOne(ClientSession clientSession, TDocument document,
      InsertOneOptions options) {
    requireNonNull(clientSession, "clientSession cannot be null");
    requireNonNull(document, "document cannot be null");
    requireNonNull(options, "options cannot be null");
    com.mongodb.reactivestreams.client.ClientSession __clientSession = clientSession.toDriverClass();
    com.mongodb.client.model.InsertOneOptions __options = options.toDriverClass();
    wrapped.insertOne(__clientSession, document, __options);
    return null;
  }

  @Override
  public io.vertx.mongo.client.MongoCollection<TDocument> insertOne(ClientSession clientSession,
      TDocument document, InsertOneOptions options,
      Handler<AsyncResult<InsertOneResult>> resultHandler) {
    Future<InsertOneResult> future = this.insertOne(clientSession, document, options);
    setHandler(future, resultHandler);
    return this;
  }

  @Override
  public Future<InsertManyResult> insertMany(List<? extends TDocument> documents) {
    requireNonNull(documents, "documents cannot be null");
    wrapped.insertMany(documents);
    return null;
  }

  @Override
  public io.vertx.mongo.client.MongoCollection<TDocument> insertMany(
      List<? extends TDocument> documents, Handler<AsyncResult<InsertManyResult>> resultHandler) {
    Future<InsertManyResult> future = this.insertMany(documents);
    setHandler(future, resultHandler);
    return this;
  }

  @Override
  public Future<InsertManyResult> insertMany(List<? extends TDocument> documents,
      InsertManyOptions options) {
    requireNonNull(documents, "documents cannot be null");
    requireNonNull(options, "options cannot be null");
    com.mongodb.client.model.InsertManyOptions __options = options.toDriverClass();
    wrapped.insertMany(documents, __options);
    return null;
  }

  @Override
  public io.vertx.mongo.client.MongoCollection<TDocument> insertMany(
      List<? extends TDocument> documents, InsertManyOptions options,
      Handler<AsyncResult<InsertManyResult>> resultHandler) {
    Future<InsertManyResult> future = this.insertMany(documents, options);
    setHandler(future, resultHandler);
    return this;
  }

  @Override
  public Future<InsertManyResult> insertMany(ClientSession clientSession,
      List<? extends TDocument> documents) {
    requireNonNull(clientSession, "clientSession cannot be null");
    requireNonNull(documents, "documents cannot be null");
    com.mongodb.reactivestreams.client.ClientSession __clientSession = clientSession.toDriverClass();
    wrapped.insertMany(__clientSession, documents);
    return null;
  }

  @Override
  public io.vertx.mongo.client.MongoCollection<TDocument> insertMany(ClientSession clientSession,
      List<? extends TDocument> documents, Handler<AsyncResult<InsertManyResult>> resultHandler) {
    Future<InsertManyResult> future = this.insertMany(clientSession, documents);
    setHandler(future, resultHandler);
    return this;
  }

  @Override
  public Future<InsertManyResult> insertMany(ClientSession clientSession,
      List<? extends TDocument> documents, InsertManyOptions options) {
    requireNonNull(clientSession, "clientSession cannot be null");
    requireNonNull(documents, "documents cannot be null");
    requireNonNull(options, "options cannot be null");
    com.mongodb.reactivestreams.client.ClientSession __clientSession = clientSession.toDriverClass();
    com.mongodb.client.model.InsertManyOptions __options = options.toDriverClass();
    wrapped.insertMany(__clientSession, documents, __options);
    return null;
  }

  @Override
  public io.vertx.mongo.client.MongoCollection<TDocument> insertMany(ClientSession clientSession,
      List<? extends TDocument> documents, InsertManyOptions options,
      Handler<AsyncResult<InsertManyResult>> resultHandler) {
    Future<InsertManyResult> future = this.insertMany(clientSession, documents, options);
    setHandler(future, resultHandler);
    return this;
  }

  @Override
  public Future<DeleteResult> deleteOne(JsonObject filter) {
    requireNonNull(filter, "filter cannot be null");
    Bson __filter = ConversionUtilsImpl.INSTANCE.toBson(filter);
    wrapped.deleteOne(__filter);
    return null;
  }

  @Override
  public io.vertx.mongo.client.MongoCollection<TDocument> deleteOne(JsonObject filter,
      Handler<AsyncResult<DeleteResult>> resultHandler) {
    Future<DeleteResult> future = this.deleteOne(filter);
    setHandler(future, resultHandler);
    return this;
  }

  @Override
  public Future<DeleteResult> deleteOne(JsonObject filter, DeleteOptions options) {
    requireNonNull(filter, "filter cannot be null");
    requireNonNull(options, "options cannot be null");
    Bson __filter = ConversionUtilsImpl.INSTANCE.toBson(filter);
    com.mongodb.client.model.DeleteOptions __options = options.toDriverClass();
    wrapped.deleteOne(__filter, __options);
    return null;
  }

  @Override
  public io.vertx.mongo.client.MongoCollection<TDocument> deleteOne(JsonObject filter,
      DeleteOptions options, Handler<AsyncResult<DeleteResult>> resultHandler) {
    Future<DeleteResult> future = this.deleteOne(filter, options);
    setHandler(future, resultHandler);
    return this;
  }

  @Override
  public Future<DeleteResult> deleteOne(ClientSession clientSession, JsonObject filter) {
    requireNonNull(clientSession, "clientSession cannot be null");
    requireNonNull(filter, "filter cannot be null");
    com.mongodb.reactivestreams.client.ClientSession __clientSession = clientSession.toDriverClass();
    Bson __filter = ConversionUtilsImpl.INSTANCE.toBson(filter);
    wrapped.deleteOne(__clientSession, __filter);
    return null;
  }

  @Override
  public io.vertx.mongo.client.MongoCollection<TDocument> deleteOne(ClientSession clientSession,
      JsonObject filter, Handler<AsyncResult<DeleteResult>> resultHandler) {
    Future<DeleteResult> future = this.deleteOne(clientSession, filter);
    setHandler(future, resultHandler);
    return this;
  }

  @Override
  public Future<DeleteResult> deleteOne(ClientSession clientSession, JsonObject filter,
      DeleteOptions options) {
    requireNonNull(clientSession, "clientSession cannot be null");
    requireNonNull(filter, "filter cannot be null");
    requireNonNull(options, "options cannot be null");
    com.mongodb.reactivestreams.client.ClientSession __clientSession = clientSession.toDriverClass();
    Bson __filter = ConversionUtilsImpl.INSTANCE.toBson(filter);
    com.mongodb.client.model.DeleteOptions __options = options.toDriverClass();
    wrapped.deleteOne(__clientSession, __filter, __options);
    return null;
  }

  @Override
  public io.vertx.mongo.client.MongoCollection<TDocument> deleteOne(ClientSession clientSession,
      JsonObject filter, DeleteOptions options, Handler<AsyncResult<DeleteResult>> resultHandler) {
    Future<DeleteResult> future = this.deleteOne(clientSession, filter, options);
    setHandler(future, resultHandler);
    return this;
  }

  @Override
  public Future<DeleteResult> deleteMany(JsonObject filter) {
    requireNonNull(filter, "filter cannot be null");
    Bson __filter = ConversionUtilsImpl.INSTANCE.toBson(filter);
    wrapped.deleteMany(__filter);
    return null;
  }

  @Override
  public io.vertx.mongo.client.MongoCollection<TDocument> deleteMany(JsonObject filter,
      Handler<AsyncResult<DeleteResult>> resultHandler) {
    Future<DeleteResult> future = this.deleteMany(filter);
    setHandler(future, resultHandler);
    return this;
  }

  @Override
  public Future<DeleteResult> deleteMany(JsonObject filter, DeleteOptions options) {
    requireNonNull(filter, "filter cannot be null");
    requireNonNull(options, "options cannot be null");
    Bson __filter = ConversionUtilsImpl.INSTANCE.toBson(filter);
    com.mongodb.client.model.DeleteOptions __options = options.toDriverClass();
    wrapped.deleteMany(__filter, __options);
    return null;
  }

  @Override
  public io.vertx.mongo.client.MongoCollection<TDocument> deleteMany(JsonObject filter,
      DeleteOptions options, Handler<AsyncResult<DeleteResult>> resultHandler) {
    Future<DeleteResult> future = this.deleteMany(filter, options);
    setHandler(future, resultHandler);
    return this;
  }

  @Override
  public Future<DeleteResult> deleteMany(ClientSession clientSession, JsonObject filter) {
    requireNonNull(clientSession, "clientSession cannot be null");
    requireNonNull(filter, "filter cannot be null");
    com.mongodb.reactivestreams.client.ClientSession __clientSession = clientSession.toDriverClass();
    Bson __filter = ConversionUtilsImpl.INSTANCE.toBson(filter);
    wrapped.deleteMany(__clientSession, __filter);
    return null;
  }

  @Override
  public io.vertx.mongo.client.MongoCollection<TDocument> deleteMany(ClientSession clientSession,
      JsonObject filter, Handler<AsyncResult<DeleteResult>> resultHandler) {
    Future<DeleteResult> future = this.deleteMany(clientSession, filter);
    setHandler(future, resultHandler);
    return this;
  }

  @Override
  public Future<DeleteResult> deleteMany(ClientSession clientSession, JsonObject filter,
      DeleteOptions options) {
    requireNonNull(clientSession, "clientSession cannot be null");
    requireNonNull(filter, "filter cannot be null");
    requireNonNull(options, "options cannot be null");
    com.mongodb.reactivestreams.client.ClientSession __clientSession = clientSession.toDriverClass();
    Bson __filter = ConversionUtilsImpl.INSTANCE.toBson(filter);
    com.mongodb.client.model.DeleteOptions __options = options.toDriverClass();
    wrapped.deleteMany(__clientSession, __filter, __options);
    return null;
  }

  @Override
  public io.vertx.mongo.client.MongoCollection<TDocument> deleteMany(ClientSession clientSession,
      JsonObject filter, DeleteOptions options, Handler<AsyncResult<DeleteResult>> resultHandler) {
    Future<DeleteResult> future = this.deleteMany(clientSession, filter, options);
    setHandler(future, resultHandler);
    return this;
  }

  @Override
  public Future<UpdateResult> replaceOne(JsonObject filter, TDocument replacement) {
    requireNonNull(filter, "filter cannot be null");
    requireNonNull(replacement, "replacement cannot be null");
    Bson __filter = ConversionUtilsImpl.INSTANCE.toBson(filter);
    wrapped.replaceOne(__filter, replacement);
    return null;
  }

  @Override
  public io.vertx.mongo.client.MongoCollection<TDocument> replaceOne(JsonObject filter,
      TDocument replacement, Handler<AsyncResult<UpdateResult>> resultHandler) {
    Future<UpdateResult> future = this.replaceOne(filter, replacement);
    setHandler(future, resultHandler);
    return this;
  }

  @Override
  public Future<UpdateResult> replaceOne(JsonObject filter, TDocument replacement,
      ReplaceOptions options) {
    requireNonNull(filter, "filter cannot be null");
    requireNonNull(replacement, "replacement cannot be null");
    requireNonNull(options, "options cannot be null");
    Bson __filter = ConversionUtilsImpl.INSTANCE.toBson(filter);
    com.mongodb.client.model.ReplaceOptions __options = options.toDriverClass();
    wrapped.replaceOne(__filter, replacement, __options);
    return null;
  }

  @Override
  public io.vertx.mongo.client.MongoCollection<TDocument> replaceOne(JsonObject filter,
      TDocument replacement, ReplaceOptions options,
      Handler<AsyncResult<UpdateResult>> resultHandler) {
    Future<UpdateResult> future = this.replaceOne(filter, replacement, options);
    setHandler(future, resultHandler);
    return this;
  }

  @Override
  public Future<UpdateResult> replaceOne(ClientSession clientSession, JsonObject filter,
      TDocument replacement) {
    requireNonNull(clientSession, "clientSession cannot be null");
    requireNonNull(filter, "filter cannot be null");
    requireNonNull(replacement, "replacement cannot be null");
    com.mongodb.reactivestreams.client.ClientSession __clientSession = clientSession.toDriverClass();
    Bson __filter = ConversionUtilsImpl.INSTANCE.toBson(filter);
    wrapped.replaceOne(__clientSession, __filter, replacement);
    return null;
  }

  @Override
  public io.vertx.mongo.client.MongoCollection<TDocument> replaceOne(ClientSession clientSession,
      JsonObject filter, TDocument replacement, Handler<AsyncResult<UpdateResult>> resultHandler) {
    Future<UpdateResult> future = this.replaceOne(clientSession, filter, replacement);
    setHandler(future, resultHandler);
    return this;
  }

  @Override
  public Future<UpdateResult> replaceOne(ClientSession clientSession, JsonObject filter,
      TDocument replacement, ReplaceOptions options) {
    requireNonNull(clientSession, "clientSession cannot be null");
    requireNonNull(filter, "filter cannot be null");
    requireNonNull(replacement, "replacement cannot be null");
    requireNonNull(options, "options cannot be null");
    com.mongodb.reactivestreams.client.ClientSession __clientSession = clientSession.toDriverClass();
    Bson __filter = ConversionUtilsImpl.INSTANCE.toBson(filter);
    com.mongodb.client.model.ReplaceOptions __options = options.toDriverClass();
    wrapped.replaceOne(__clientSession, __filter, replacement, __options);
    return null;
  }

  @Override
  public io.vertx.mongo.client.MongoCollection<TDocument> replaceOne(ClientSession clientSession,
      JsonObject filter, TDocument replacement, ReplaceOptions options,
      Handler<AsyncResult<UpdateResult>> resultHandler) {
    Future<UpdateResult> future = this.replaceOne(clientSession, filter, replacement, options);
    setHandler(future, resultHandler);
    return this;
  }

  @Override
  public Future<UpdateResult> updateOne(JsonObject filter, JsonObject update) {
    requireNonNull(filter, "filter cannot be null");
    requireNonNull(update, "update cannot be null");
    Bson __filter = ConversionUtilsImpl.INSTANCE.toBson(filter);
    Bson __update = ConversionUtilsImpl.INSTANCE.toBson(update);
    wrapped.updateOne(__filter, __update);
    return null;
  }

  @Override
  public io.vertx.mongo.client.MongoCollection<TDocument> updateOne(JsonObject filter,
      JsonObject update, Handler<AsyncResult<UpdateResult>> resultHandler) {
    Future<UpdateResult> future = this.updateOne(filter, update);
    setHandler(future, resultHandler);
    return this;
  }

  @Override
  public Future<UpdateResult> updateOne(JsonObject filter, JsonObject update,
      UpdateOptions options) {
    requireNonNull(filter, "filter cannot be null");
    requireNonNull(update, "update cannot be null");
    requireNonNull(options, "options cannot be null");
    Bson __filter = ConversionUtilsImpl.INSTANCE.toBson(filter);
    Bson __update = ConversionUtilsImpl.INSTANCE.toBson(update);
    com.mongodb.client.model.UpdateOptions __options = options.toDriverClass();
    wrapped.updateOne(__filter, __update, __options);
    return null;
  }

  @Override
  public io.vertx.mongo.client.MongoCollection<TDocument> updateOne(JsonObject filter,
      JsonObject update, UpdateOptions options, Handler<AsyncResult<UpdateResult>> resultHandler) {
    Future<UpdateResult> future = this.updateOne(filter, update, options);
    setHandler(future, resultHandler);
    return this;
  }

  @Override
  public Future<UpdateResult> updateOne(ClientSession clientSession, JsonObject filter,
      JsonObject update) {
    requireNonNull(clientSession, "clientSession cannot be null");
    requireNonNull(filter, "filter cannot be null");
    requireNonNull(update, "update cannot be null");
    com.mongodb.reactivestreams.client.ClientSession __clientSession = clientSession.toDriverClass();
    Bson __filter = ConversionUtilsImpl.INSTANCE.toBson(filter);
    Bson __update = ConversionUtilsImpl.INSTANCE.toBson(update);
    wrapped.updateOne(__clientSession, __filter, __update);
    return null;
  }

  @Override
  public io.vertx.mongo.client.MongoCollection<TDocument> updateOne(ClientSession clientSession,
      JsonObject filter, JsonObject update, Handler<AsyncResult<UpdateResult>> resultHandler) {
    Future<UpdateResult> future = this.updateOne(clientSession, filter, update);
    setHandler(future, resultHandler);
    return this;
  }

  @Override
  public Future<UpdateResult> updateOne(ClientSession clientSession, JsonObject filter,
      JsonObject update, UpdateOptions options) {
    requireNonNull(clientSession, "clientSession cannot be null");
    requireNonNull(filter, "filter cannot be null");
    requireNonNull(update, "update cannot be null");
    requireNonNull(options, "options cannot be null");
    com.mongodb.reactivestreams.client.ClientSession __clientSession = clientSession.toDriverClass();
    Bson __filter = ConversionUtilsImpl.INSTANCE.toBson(filter);
    Bson __update = ConversionUtilsImpl.INSTANCE.toBson(update);
    com.mongodb.client.model.UpdateOptions __options = options.toDriverClass();
    wrapped.updateOne(__clientSession, __filter, __update, __options);
    return null;
  }

  @Override
  public io.vertx.mongo.client.MongoCollection<TDocument> updateOne(ClientSession clientSession,
      JsonObject filter, JsonObject update, UpdateOptions options,
      Handler<AsyncResult<UpdateResult>> resultHandler) {
    Future<UpdateResult> future = this.updateOne(clientSession, filter, update, options);
    setHandler(future, resultHandler);
    return this;
  }

  @Override
  public Future<UpdateResult> updateOne(JsonObject filter, List<JsonObject> update) {
    requireNonNull(filter, "filter cannot be null");
    requireNonNull(update, "update cannot be null");
    Bson __filter = ConversionUtilsImpl.INSTANCE.toBson(filter);
    List<? extends Bson> __update = ConversionUtilsImpl.INSTANCE.toBsonList(update);
    wrapped.updateOne(__filter, __update);
    return null;
  }

  @Override
  public io.vertx.mongo.client.MongoCollection<TDocument> updateOne(JsonObject filter,
      List<JsonObject> update, Handler<AsyncResult<UpdateResult>> resultHandler) {
    Future<UpdateResult> future = this.updateOne(filter, update);
    setHandler(future, resultHandler);
    return this;
  }

  @Override
  public Future<UpdateResult> updateOne(JsonObject filter, List<JsonObject> update,
      UpdateOptions options) {
    requireNonNull(filter, "filter cannot be null");
    requireNonNull(update, "update cannot be null");
    requireNonNull(options, "options cannot be null");
    Bson __filter = ConversionUtilsImpl.INSTANCE.toBson(filter);
    List<? extends Bson> __update = ConversionUtilsImpl.INSTANCE.toBsonList(update);
    com.mongodb.client.model.UpdateOptions __options = options.toDriverClass();
    wrapped.updateOne(__filter, __update, __options);
    return null;
  }

  @Override
  public io.vertx.mongo.client.MongoCollection<TDocument> updateOne(JsonObject filter,
      List<JsonObject> update, UpdateOptions options,
      Handler<AsyncResult<UpdateResult>> resultHandler) {
    Future<UpdateResult> future = this.updateOne(filter, update, options);
    setHandler(future, resultHandler);
    return this;
  }

  @Override
  public Future<UpdateResult> updateOne(ClientSession clientSession, JsonObject filter,
      List<JsonObject> update) {
    requireNonNull(clientSession, "clientSession cannot be null");
    requireNonNull(filter, "filter cannot be null");
    requireNonNull(update, "update cannot be null");
    com.mongodb.reactivestreams.client.ClientSession __clientSession = clientSession.toDriverClass();
    Bson __filter = ConversionUtilsImpl.INSTANCE.toBson(filter);
    List<? extends Bson> __update = ConversionUtilsImpl.INSTANCE.toBsonList(update);
    wrapped.updateOne(__clientSession, __filter, __update);
    return null;
  }

  @Override
  public io.vertx.mongo.client.MongoCollection<TDocument> updateOne(ClientSession clientSession,
      JsonObject filter, List<JsonObject> update,
      Handler<AsyncResult<UpdateResult>> resultHandler) {
    Future<UpdateResult> future = this.updateOne(clientSession, filter, update);
    setHandler(future, resultHandler);
    return this;
  }

  @Override
  public Future<UpdateResult> updateOne(ClientSession clientSession, JsonObject filter,
      List<JsonObject> update, UpdateOptions options) {
    requireNonNull(clientSession, "clientSession cannot be null");
    requireNonNull(filter, "filter cannot be null");
    requireNonNull(update, "update cannot be null");
    requireNonNull(options, "options cannot be null");
    com.mongodb.reactivestreams.client.ClientSession __clientSession = clientSession.toDriverClass();
    Bson __filter = ConversionUtilsImpl.INSTANCE.toBson(filter);
    List<? extends Bson> __update = ConversionUtilsImpl.INSTANCE.toBsonList(update);
    com.mongodb.client.model.UpdateOptions __options = options.toDriverClass();
    wrapped.updateOne(__clientSession, __filter, __update, __options);
    return null;
  }

  @Override
  public io.vertx.mongo.client.MongoCollection<TDocument> updateOne(ClientSession clientSession,
      JsonObject filter, List<JsonObject> update, UpdateOptions options,
      Handler<AsyncResult<UpdateResult>> resultHandler) {
    Future<UpdateResult> future = this.updateOne(clientSession, filter, update, options);
    setHandler(future, resultHandler);
    return this;
  }

  @Override
  public Future<UpdateResult> updateMany(JsonObject filter, JsonObject update) {
    requireNonNull(filter, "filter cannot be null");
    requireNonNull(update, "update cannot be null");
    Bson __filter = ConversionUtilsImpl.INSTANCE.toBson(filter);
    Bson __update = ConversionUtilsImpl.INSTANCE.toBson(update);
    wrapped.updateMany(__filter, __update);
    return null;
  }

  @Override
  public io.vertx.mongo.client.MongoCollection<TDocument> updateMany(JsonObject filter,
      JsonObject update, Handler<AsyncResult<UpdateResult>> resultHandler) {
    Future<UpdateResult> future = this.updateMany(filter, update);
    setHandler(future, resultHandler);
    return this;
  }

  @Override
  public Future<UpdateResult> updateMany(JsonObject filter, JsonObject update,
      UpdateOptions options) {
    requireNonNull(filter, "filter cannot be null");
    requireNonNull(update, "update cannot be null");
    requireNonNull(options, "options cannot be null");
    Bson __filter = ConversionUtilsImpl.INSTANCE.toBson(filter);
    Bson __update = ConversionUtilsImpl.INSTANCE.toBson(update);
    com.mongodb.client.model.UpdateOptions __options = options.toDriverClass();
    wrapped.updateMany(__filter, __update, __options);
    return null;
  }

  @Override
  public io.vertx.mongo.client.MongoCollection<TDocument> updateMany(JsonObject filter,
      JsonObject update, UpdateOptions options, Handler<AsyncResult<UpdateResult>> resultHandler) {
    Future<UpdateResult> future = this.updateMany(filter, update, options);
    setHandler(future, resultHandler);
    return this;
  }

  @Override
  public Future<UpdateResult> updateMany(ClientSession clientSession, JsonObject filter,
      JsonObject update) {
    requireNonNull(clientSession, "clientSession cannot be null");
    requireNonNull(filter, "filter cannot be null");
    requireNonNull(update, "update cannot be null");
    com.mongodb.reactivestreams.client.ClientSession __clientSession = clientSession.toDriverClass();
    Bson __filter = ConversionUtilsImpl.INSTANCE.toBson(filter);
    Bson __update = ConversionUtilsImpl.INSTANCE.toBson(update);
    wrapped.updateMany(__clientSession, __filter, __update);
    return null;
  }

  @Override
  public io.vertx.mongo.client.MongoCollection<TDocument> updateMany(ClientSession clientSession,
      JsonObject filter, JsonObject update, Handler<AsyncResult<UpdateResult>> resultHandler) {
    Future<UpdateResult> future = this.updateMany(clientSession, filter, update);
    setHandler(future, resultHandler);
    return this;
  }

  @Override
  public Future<UpdateResult> updateMany(ClientSession clientSession, JsonObject filter,
      JsonObject update, UpdateOptions options) {
    requireNonNull(clientSession, "clientSession cannot be null");
    requireNonNull(filter, "filter cannot be null");
    requireNonNull(update, "update cannot be null");
    requireNonNull(options, "options cannot be null");
    com.mongodb.reactivestreams.client.ClientSession __clientSession = clientSession.toDriverClass();
    Bson __filter = ConversionUtilsImpl.INSTANCE.toBson(filter);
    Bson __update = ConversionUtilsImpl.INSTANCE.toBson(update);
    com.mongodb.client.model.UpdateOptions __options = options.toDriverClass();
    wrapped.updateMany(__clientSession, __filter, __update, __options);
    return null;
  }

  @Override
  public io.vertx.mongo.client.MongoCollection<TDocument> updateMany(ClientSession clientSession,
      JsonObject filter, JsonObject update, UpdateOptions options,
      Handler<AsyncResult<UpdateResult>> resultHandler) {
    Future<UpdateResult> future = this.updateMany(clientSession, filter, update, options);
    setHandler(future, resultHandler);
    return this;
  }

  @Override
  public Future<UpdateResult> updateMany(JsonObject filter, List<JsonObject> update) {
    requireNonNull(filter, "filter cannot be null");
    requireNonNull(update, "update cannot be null");
    Bson __filter = ConversionUtilsImpl.INSTANCE.toBson(filter);
    List<? extends Bson> __update = ConversionUtilsImpl.INSTANCE.toBsonList(update);
    wrapped.updateMany(__filter, __update);
    return null;
  }

  @Override
  public io.vertx.mongo.client.MongoCollection<TDocument> updateMany(JsonObject filter,
      List<JsonObject> update, Handler<AsyncResult<UpdateResult>> resultHandler) {
    Future<UpdateResult> future = this.updateMany(filter, update);
    setHandler(future, resultHandler);
    return this;
  }

  @Override
  public Future<UpdateResult> updateMany(JsonObject filter, List<JsonObject> update,
      UpdateOptions options) {
    requireNonNull(filter, "filter cannot be null");
    requireNonNull(update, "update cannot be null");
    requireNonNull(options, "options cannot be null");
    Bson __filter = ConversionUtilsImpl.INSTANCE.toBson(filter);
    List<? extends Bson> __update = ConversionUtilsImpl.INSTANCE.toBsonList(update);
    com.mongodb.client.model.UpdateOptions __options = options.toDriverClass();
    wrapped.updateMany(__filter, __update, __options);
    return null;
  }

  @Override
  public io.vertx.mongo.client.MongoCollection<TDocument> updateMany(JsonObject filter,
      List<JsonObject> update, UpdateOptions options,
      Handler<AsyncResult<UpdateResult>> resultHandler) {
    Future<UpdateResult> future = this.updateMany(filter, update, options);
    setHandler(future, resultHandler);
    return this;
  }

  @Override
  public Future<UpdateResult> updateMany(ClientSession clientSession, JsonObject filter,
      List<JsonObject> update) {
    requireNonNull(clientSession, "clientSession cannot be null");
    requireNonNull(filter, "filter cannot be null");
    requireNonNull(update, "update cannot be null");
    com.mongodb.reactivestreams.client.ClientSession __clientSession = clientSession.toDriverClass();
    Bson __filter = ConversionUtilsImpl.INSTANCE.toBson(filter);
    List<? extends Bson> __update = ConversionUtilsImpl.INSTANCE.toBsonList(update);
    wrapped.updateMany(__clientSession, __filter, __update);
    return null;
  }

  @Override
  public io.vertx.mongo.client.MongoCollection<TDocument> updateMany(ClientSession clientSession,
      JsonObject filter, List<JsonObject> update,
      Handler<AsyncResult<UpdateResult>> resultHandler) {
    Future<UpdateResult> future = this.updateMany(clientSession, filter, update);
    setHandler(future, resultHandler);
    return this;
  }

  @Override
  public Future<UpdateResult> updateMany(ClientSession clientSession, JsonObject filter,
      List<JsonObject> update, UpdateOptions options) {
    requireNonNull(clientSession, "clientSession cannot be null");
    requireNonNull(filter, "filter cannot be null");
    requireNonNull(update, "update cannot be null");
    requireNonNull(options, "options cannot be null");
    com.mongodb.reactivestreams.client.ClientSession __clientSession = clientSession.toDriverClass();
    Bson __filter = ConversionUtilsImpl.INSTANCE.toBson(filter);
    List<? extends Bson> __update = ConversionUtilsImpl.INSTANCE.toBsonList(update);
    com.mongodb.client.model.UpdateOptions __options = options.toDriverClass();
    wrapped.updateMany(__clientSession, __filter, __update, __options);
    return null;
  }

  @Override
  public io.vertx.mongo.client.MongoCollection<TDocument> updateMany(ClientSession clientSession,
      JsonObject filter, List<JsonObject> update, UpdateOptions options,
      Handler<AsyncResult<UpdateResult>> resultHandler) {
    Future<UpdateResult> future = this.updateMany(clientSession, filter, update, options);
    setHandler(future, resultHandler);
    return this;
  }

  @Override
  public Future<TDocument> findOneAndDelete(JsonObject filter) {
    requireNonNull(filter, "filter cannot be null");
    Bson __filter = ConversionUtilsImpl.INSTANCE.toBson(filter);
    wrapped.findOneAndDelete(__filter);
    return null;
  }

  @Override
  public io.vertx.mongo.client.MongoCollection<TDocument> findOneAndDelete(JsonObject filter,
      Handler<AsyncResult<TDocument>> resultHandler) {
    Future<TDocument> future = this.findOneAndDelete(filter);
    setHandler(future, resultHandler);
    return this;
  }

  @Override
  public Future<TDocument> findOneAndDelete(JsonObject filter, FindOneAndDeleteOptions options) {
    requireNonNull(filter, "filter cannot be null");
    requireNonNull(options, "options cannot be null");
    Bson __filter = ConversionUtilsImpl.INSTANCE.toBson(filter);
    com.mongodb.client.model.FindOneAndDeleteOptions __options = options.toDriverClass();
    wrapped.findOneAndDelete(__filter, __options);
    return null;
  }

  @Override
  public io.vertx.mongo.client.MongoCollection<TDocument> findOneAndDelete(JsonObject filter,
      FindOneAndDeleteOptions options, Handler<AsyncResult<TDocument>> resultHandler) {
    Future<TDocument> future = this.findOneAndDelete(filter, options);
    setHandler(future, resultHandler);
    return this;
  }

  @Override
  public Future<TDocument> findOneAndDelete(ClientSession clientSession, JsonObject filter) {
    requireNonNull(clientSession, "clientSession cannot be null");
    requireNonNull(filter, "filter cannot be null");
    com.mongodb.reactivestreams.client.ClientSession __clientSession = clientSession.toDriverClass();
    Bson __filter = ConversionUtilsImpl.INSTANCE.toBson(filter);
    wrapped.findOneAndDelete(__clientSession, __filter);
    return null;
  }

  @Override
  public io.vertx.mongo.client.MongoCollection<TDocument> findOneAndDelete(
      ClientSession clientSession, JsonObject filter,
      Handler<AsyncResult<TDocument>> resultHandler) {
    Future<TDocument> future = this.findOneAndDelete(clientSession, filter);
    setHandler(future, resultHandler);
    return this;
  }

  @Override
  public Future<TDocument> findOneAndDelete(ClientSession clientSession, JsonObject filter,
      FindOneAndDeleteOptions options) {
    requireNonNull(clientSession, "clientSession cannot be null");
    requireNonNull(filter, "filter cannot be null");
    requireNonNull(options, "options cannot be null");
    com.mongodb.reactivestreams.client.ClientSession __clientSession = clientSession.toDriverClass();
    Bson __filter = ConversionUtilsImpl.INSTANCE.toBson(filter);
    com.mongodb.client.model.FindOneAndDeleteOptions __options = options.toDriverClass();
    wrapped.findOneAndDelete(__clientSession, __filter, __options);
    return null;
  }

  @Override
  public io.vertx.mongo.client.MongoCollection<TDocument> findOneAndDelete(
      ClientSession clientSession, JsonObject filter, FindOneAndDeleteOptions options,
      Handler<AsyncResult<TDocument>> resultHandler) {
    Future<TDocument> future = this.findOneAndDelete(clientSession, filter, options);
    setHandler(future, resultHandler);
    return this;
  }

  @Override
  public Future<TDocument> findOneAndReplace(JsonObject filter, TDocument replacement) {
    requireNonNull(filter, "filter cannot be null");
    requireNonNull(replacement, "replacement cannot be null");
    Bson __filter = ConversionUtilsImpl.INSTANCE.toBson(filter);
    wrapped.findOneAndReplace(__filter, replacement);
    return null;
  }

  @Override
  public io.vertx.mongo.client.MongoCollection<TDocument> findOneAndReplace(JsonObject filter,
      TDocument replacement, Handler<AsyncResult<TDocument>> resultHandler) {
    Future<TDocument> future = this.findOneAndReplace(filter, replacement);
    setHandler(future, resultHandler);
    return this;
  }

  @Override
  public Future<TDocument> findOneAndReplace(JsonObject filter, TDocument replacement,
      FindOneAndReplaceOptions options) {
    requireNonNull(filter, "filter cannot be null");
    requireNonNull(replacement, "replacement cannot be null");
    requireNonNull(options, "options cannot be null");
    Bson __filter = ConversionUtilsImpl.INSTANCE.toBson(filter);
    com.mongodb.client.model.FindOneAndReplaceOptions __options = options.toDriverClass();
    wrapped.findOneAndReplace(__filter, replacement, __options);
    return null;
  }

  @Override
  public io.vertx.mongo.client.MongoCollection<TDocument> findOneAndReplace(JsonObject filter,
      TDocument replacement, FindOneAndReplaceOptions options,
      Handler<AsyncResult<TDocument>> resultHandler) {
    Future<TDocument> future = this.findOneAndReplace(filter, replacement, options);
    setHandler(future, resultHandler);
    return this;
  }

  @Override
  public Future<TDocument> findOneAndReplace(ClientSession clientSession, JsonObject filter,
      TDocument replacement) {
    requireNonNull(clientSession, "clientSession cannot be null");
    requireNonNull(filter, "filter cannot be null");
    requireNonNull(replacement, "replacement cannot be null");
    com.mongodb.reactivestreams.client.ClientSession __clientSession = clientSession.toDriverClass();
    Bson __filter = ConversionUtilsImpl.INSTANCE.toBson(filter);
    wrapped.findOneAndReplace(__clientSession, __filter, replacement);
    return null;
  }

  @Override
  public io.vertx.mongo.client.MongoCollection<TDocument> findOneAndReplace(
      ClientSession clientSession, JsonObject filter, TDocument replacement,
      Handler<AsyncResult<TDocument>> resultHandler) {
    Future<TDocument> future = this.findOneAndReplace(clientSession, filter, replacement);
    setHandler(future, resultHandler);
    return this;
  }

  @Override
  public Future<TDocument> findOneAndReplace(ClientSession clientSession, JsonObject filter,
      TDocument replacement, FindOneAndReplaceOptions options) {
    requireNonNull(clientSession, "clientSession cannot be null");
    requireNonNull(filter, "filter cannot be null");
    requireNonNull(replacement, "replacement cannot be null");
    requireNonNull(options, "options cannot be null");
    com.mongodb.reactivestreams.client.ClientSession __clientSession = clientSession.toDriverClass();
    Bson __filter = ConversionUtilsImpl.INSTANCE.toBson(filter);
    com.mongodb.client.model.FindOneAndReplaceOptions __options = options.toDriverClass();
    wrapped.findOneAndReplace(__clientSession, __filter, replacement, __options);
    return null;
  }

  @Override
  public io.vertx.mongo.client.MongoCollection<TDocument> findOneAndReplace(
      ClientSession clientSession, JsonObject filter, TDocument replacement,
      FindOneAndReplaceOptions options, Handler<AsyncResult<TDocument>> resultHandler) {
    Future<TDocument> future = this.findOneAndReplace(clientSession, filter, replacement, options);
    setHandler(future, resultHandler);
    return this;
  }

  @Override
  public Future<TDocument> findOneAndUpdate(JsonObject filter, JsonObject update) {
    requireNonNull(filter, "filter cannot be null");
    requireNonNull(update, "update cannot be null");
    Bson __filter = ConversionUtilsImpl.INSTANCE.toBson(filter);
    Bson __update = ConversionUtilsImpl.INSTANCE.toBson(update);
    wrapped.findOneAndUpdate(__filter, __update);
    return null;
  }

  @Override
  public io.vertx.mongo.client.MongoCollection<TDocument> findOneAndUpdate(JsonObject filter,
      JsonObject update, Handler<AsyncResult<TDocument>> resultHandler) {
    Future<TDocument> future = this.findOneAndUpdate(filter, update);
    setHandler(future, resultHandler);
    return this;
  }

  @Override
  public Future<TDocument> findOneAndUpdate(JsonObject filter, JsonObject update,
      FindOneAndUpdateOptions options) {
    requireNonNull(filter, "filter cannot be null");
    requireNonNull(update, "update cannot be null");
    requireNonNull(options, "options cannot be null");
    Bson __filter = ConversionUtilsImpl.INSTANCE.toBson(filter);
    Bson __update = ConversionUtilsImpl.INSTANCE.toBson(update);
    com.mongodb.client.model.FindOneAndUpdateOptions __options = options.toDriverClass();
    wrapped.findOneAndUpdate(__filter, __update, __options);
    return null;
  }

  @Override
  public io.vertx.mongo.client.MongoCollection<TDocument> findOneAndUpdate(JsonObject filter,
      JsonObject update, FindOneAndUpdateOptions options,
      Handler<AsyncResult<TDocument>> resultHandler) {
    Future<TDocument> future = this.findOneAndUpdate(filter, update, options);
    setHandler(future, resultHandler);
    return this;
  }

  @Override
  public Future<TDocument> findOneAndUpdate(ClientSession clientSession, JsonObject filter,
      JsonObject update) {
    requireNonNull(clientSession, "clientSession cannot be null");
    requireNonNull(filter, "filter cannot be null");
    requireNonNull(update, "update cannot be null");
    com.mongodb.reactivestreams.client.ClientSession __clientSession = clientSession.toDriverClass();
    Bson __filter = ConversionUtilsImpl.INSTANCE.toBson(filter);
    Bson __update = ConversionUtilsImpl.INSTANCE.toBson(update);
    wrapped.findOneAndUpdate(__clientSession, __filter, __update);
    return null;
  }

  @Override
  public io.vertx.mongo.client.MongoCollection<TDocument> findOneAndUpdate(
      ClientSession clientSession, JsonObject filter, JsonObject update,
      Handler<AsyncResult<TDocument>> resultHandler) {
    Future<TDocument> future = this.findOneAndUpdate(clientSession, filter, update);
    setHandler(future, resultHandler);
    return this;
  }

  @Override
  public Future<TDocument> findOneAndUpdate(ClientSession clientSession, JsonObject filter,
      JsonObject update, FindOneAndUpdateOptions options) {
    requireNonNull(clientSession, "clientSession cannot be null");
    requireNonNull(filter, "filter cannot be null");
    requireNonNull(update, "update cannot be null");
    requireNonNull(options, "options cannot be null");
    com.mongodb.reactivestreams.client.ClientSession __clientSession = clientSession.toDriverClass();
    Bson __filter = ConversionUtilsImpl.INSTANCE.toBson(filter);
    Bson __update = ConversionUtilsImpl.INSTANCE.toBson(update);
    com.mongodb.client.model.FindOneAndUpdateOptions __options = options.toDriverClass();
    wrapped.findOneAndUpdate(__clientSession, __filter, __update, __options);
    return null;
  }

  @Override
  public io.vertx.mongo.client.MongoCollection<TDocument> findOneAndUpdate(
      ClientSession clientSession, JsonObject filter, JsonObject update,
      FindOneAndUpdateOptions options, Handler<AsyncResult<TDocument>> resultHandler) {
    Future<TDocument> future = this.findOneAndUpdate(clientSession, filter, update, options);
    setHandler(future, resultHandler);
    return this;
  }

  @Override
  public Future<TDocument> findOneAndUpdate(JsonObject filter, List<JsonObject> update) {
    requireNonNull(filter, "filter cannot be null");
    requireNonNull(update, "update cannot be null");
    Bson __filter = ConversionUtilsImpl.INSTANCE.toBson(filter);
    List<? extends Bson> __update = ConversionUtilsImpl.INSTANCE.toBsonList(update);
    wrapped.findOneAndUpdate(__filter, __update);
    return null;
  }

  @Override
  public io.vertx.mongo.client.MongoCollection<TDocument> findOneAndUpdate(JsonObject filter,
      List<JsonObject> update, Handler<AsyncResult<TDocument>> resultHandler) {
    Future<TDocument> future = this.findOneAndUpdate(filter, update);
    setHandler(future, resultHandler);
    return this;
  }

  @Override
  public Future<TDocument> findOneAndUpdate(JsonObject filter, List<JsonObject> update,
      FindOneAndUpdateOptions options) {
    requireNonNull(filter, "filter cannot be null");
    requireNonNull(update, "update cannot be null");
    requireNonNull(options, "options cannot be null");
    Bson __filter = ConversionUtilsImpl.INSTANCE.toBson(filter);
    List<? extends Bson> __update = ConversionUtilsImpl.INSTANCE.toBsonList(update);
    com.mongodb.client.model.FindOneAndUpdateOptions __options = options.toDriverClass();
    wrapped.findOneAndUpdate(__filter, __update, __options);
    return null;
  }

  @Override
  public io.vertx.mongo.client.MongoCollection<TDocument> findOneAndUpdate(JsonObject filter,
      List<JsonObject> update, FindOneAndUpdateOptions options,
      Handler<AsyncResult<TDocument>> resultHandler) {
    Future<TDocument> future = this.findOneAndUpdate(filter, update, options);
    setHandler(future, resultHandler);
    return this;
  }

  @Override
  public Future<TDocument> findOneAndUpdate(ClientSession clientSession, JsonObject filter,
      List<JsonObject> update) {
    requireNonNull(clientSession, "clientSession cannot be null");
    requireNonNull(filter, "filter cannot be null");
    requireNonNull(update, "update cannot be null");
    com.mongodb.reactivestreams.client.ClientSession __clientSession = clientSession.toDriverClass();
    Bson __filter = ConversionUtilsImpl.INSTANCE.toBson(filter);
    List<? extends Bson> __update = ConversionUtilsImpl.INSTANCE.toBsonList(update);
    wrapped.findOneAndUpdate(__clientSession, __filter, __update);
    return null;
  }

  @Override
  public io.vertx.mongo.client.MongoCollection<TDocument> findOneAndUpdate(
      ClientSession clientSession, JsonObject filter, List<JsonObject> update,
      Handler<AsyncResult<TDocument>> resultHandler) {
    Future<TDocument> future = this.findOneAndUpdate(clientSession, filter, update);
    setHandler(future, resultHandler);
    return this;
  }

  @Override
  public Future<TDocument> findOneAndUpdate(ClientSession clientSession, JsonObject filter,
      List<JsonObject> update, FindOneAndUpdateOptions options) {
    requireNonNull(clientSession, "clientSession cannot be null");
    requireNonNull(filter, "filter cannot be null");
    requireNonNull(update, "update cannot be null");
    requireNonNull(options, "options cannot be null");
    com.mongodb.reactivestreams.client.ClientSession __clientSession = clientSession.toDriverClass();
    Bson __filter = ConversionUtilsImpl.INSTANCE.toBson(filter);
    List<? extends Bson> __update = ConversionUtilsImpl.INSTANCE.toBsonList(update);
    com.mongodb.client.model.FindOneAndUpdateOptions __options = options.toDriverClass();
    wrapped.findOneAndUpdate(__clientSession, __filter, __update, __options);
    return null;
  }

  @Override
  public io.vertx.mongo.client.MongoCollection<TDocument> findOneAndUpdate(
      ClientSession clientSession, JsonObject filter, List<JsonObject> update,
      FindOneAndUpdateOptions options, Handler<AsyncResult<TDocument>> resultHandler) {
    Future<TDocument> future = this.findOneAndUpdate(clientSession, filter, update, options);
    setHandler(future, resultHandler);
    return this;
  }

  @Override
  public Future<Void> drop() {
    wrapped.drop();
    return null;
  }

  @Override
  public io.vertx.mongo.client.MongoCollection<TDocument> drop(
      Handler<AsyncResult<Void>> resultHandler) {
    Future<Void> future = this.drop();
    setHandler(future, resultHandler);
    return this;
  }

  @Override
  public Future<Void> drop(ClientSession clientSession) {
    requireNonNull(clientSession, "clientSession cannot be null");
    com.mongodb.reactivestreams.client.ClientSession __clientSession = clientSession.toDriverClass();
    wrapped.drop(__clientSession);
    return null;
  }

  @Override
  public io.vertx.mongo.client.MongoCollection<TDocument> drop(ClientSession clientSession,
      Handler<AsyncResult<Void>> resultHandler) {
    Future<Void> future = this.drop(clientSession);
    setHandler(future, resultHandler);
    return this;
  }

  @Override
  public Future<String> createIndex(JsonObject key) {
    requireNonNull(key, "key cannot be null");
    Bson __key = ConversionUtilsImpl.INSTANCE.toBson(key);
    wrapped.createIndex(__key);
    return null;
  }

  @Override
  public io.vertx.mongo.client.MongoCollection<TDocument> createIndex(JsonObject key,
      Handler<AsyncResult<String>> resultHandler) {
    Future<String> future = this.createIndex(key);
    setHandler(future, resultHandler);
    return this;
  }

  @Override
  public Future<String> createIndex(JsonObject key, IndexOptions options) {
    requireNonNull(key, "key cannot be null");
    requireNonNull(options, "options cannot be null");
    Bson __key = ConversionUtilsImpl.INSTANCE.toBson(key);
    com.mongodb.client.model.IndexOptions __options = options.toDriverClass();
    wrapped.createIndex(__key, __options);
    return null;
  }

  @Override
  public io.vertx.mongo.client.MongoCollection<TDocument> createIndex(JsonObject key,
      IndexOptions options, Handler<AsyncResult<String>> resultHandler) {
    Future<String> future = this.createIndex(key, options);
    setHandler(future, resultHandler);
    return this;
  }

  @Override
  public Future<String> createIndex(ClientSession clientSession, JsonObject key) {
    requireNonNull(clientSession, "clientSession cannot be null");
    requireNonNull(key, "key cannot be null");
    com.mongodb.reactivestreams.client.ClientSession __clientSession = clientSession.toDriverClass();
    Bson __key = ConversionUtilsImpl.INSTANCE.toBson(key);
    wrapped.createIndex(__clientSession, __key);
    return null;
  }

  @Override
  public io.vertx.mongo.client.MongoCollection<TDocument> createIndex(ClientSession clientSession,
      JsonObject key, Handler<AsyncResult<String>> resultHandler) {
    Future<String> future = this.createIndex(clientSession, key);
    setHandler(future, resultHandler);
    return this;
  }

  @Override
  public Future<String> createIndex(ClientSession clientSession, JsonObject key,
      IndexOptions options) {
    requireNonNull(clientSession, "clientSession cannot be null");
    requireNonNull(key, "key cannot be null");
    requireNonNull(options, "options cannot be null");
    com.mongodb.reactivestreams.client.ClientSession __clientSession = clientSession.toDriverClass();
    Bson __key = ConversionUtilsImpl.INSTANCE.toBson(key);
    com.mongodb.client.model.IndexOptions __options = options.toDriverClass();
    wrapped.createIndex(__clientSession, __key, __options);
    return null;
  }

  @Override
  public io.vertx.mongo.client.MongoCollection<TDocument> createIndex(ClientSession clientSession,
      JsonObject key, IndexOptions options, Handler<AsyncResult<String>> resultHandler) {
    Future<String> future = this.createIndex(clientSession, key, options);
    setHandler(future, resultHandler);
    return this;
  }

  @Override
  public Future<String> createIndexes(List<IndexModel> indexes) {
    requireNonNull(indexes, "indexes cannot be null");
    wrapped.createIndexes(indexes);
    return null;
  }

  @Override
  public io.vertx.mongo.client.MongoCollection<TDocument> createIndexes(List<IndexModel> indexes,
      Handler<AsyncResult<String>> resultHandler) {
    Future<String> future = this.createIndexes(indexes);
    setHandler(future, resultHandler);
    return this;
  }

  @Override
  public Future<String> createIndexes(List<IndexModel> indexes,
      CreateIndexOptions createIndexOptions) {
    requireNonNull(indexes, "indexes cannot be null");
    requireNonNull(createIndexOptions, "createIndexOptions cannot be null");
    com.mongodb.client.model.CreateIndexOptions __createIndexOptions = createIndexOptions.toDriverClass();
    wrapped.createIndexes(indexes, __createIndexOptions);
    return null;
  }

  @Override
  public io.vertx.mongo.client.MongoCollection<TDocument> createIndexes(List<IndexModel> indexes,
      CreateIndexOptions createIndexOptions, Handler<AsyncResult<String>> resultHandler) {
    Future<String> future = this.createIndexes(indexes, createIndexOptions);
    setHandler(future, resultHandler);
    return this;
  }

  @Override
  public Future<String> createIndexes(ClientSession clientSession, List<IndexModel> indexes) {
    requireNonNull(clientSession, "clientSession cannot be null");
    requireNonNull(indexes, "indexes cannot be null");
    com.mongodb.reactivestreams.client.ClientSession __clientSession = clientSession.toDriverClass();
    wrapped.createIndexes(__clientSession, indexes);
    return null;
  }

  @Override
  public io.vertx.mongo.client.MongoCollection<TDocument> createIndexes(ClientSession clientSession,
      List<IndexModel> indexes, Handler<AsyncResult<String>> resultHandler) {
    Future<String> future = this.createIndexes(clientSession, indexes);
    setHandler(future, resultHandler);
    return this;
  }

  @Override
  public Future<String> createIndexes(ClientSession clientSession, List<IndexModel> indexes,
      CreateIndexOptions createIndexOptions) {
    requireNonNull(clientSession, "clientSession cannot be null");
    requireNonNull(indexes, "indexes cannot be null");
    requireNonNull(createIndexOptions, "createIndexOptions cannot be null");
    com.mongodb.reactivestreams.client.ClientSession __clientSession = clientSession.toDriverClass();
    com.mongodb.client.model.CreateIndexOptions __createIndexOptions = createIndexOptions.toDriverClass();
    wrapped.createIndexes(__clientSession, indexes, __createIndexOptions);
    return null;
  }

  @Override
  public io.vertx.mongo.client.MongoCollection<TDocument> createIndexes(ClientSession clientSession,
      List<IndexModel> indexes, CreateIndexOptions createIndexOptions,
      Handler<AsyncResult<String>> resultHandler) {
    Future<String> future = this.createIndexes(clientSession, indexes, createIndexOptions);
    setHandler(future, resultHandler);
    return this;
  }

  @Override
  public MongoResult<JsonObject> listIndexes() {
    wrapped.listIndexes();
    return null;
  }

  @Override
  public MongoResult<JsonObject> listIndexes(ListIndexesOptions options) {
    return null;
  }

  @Override
  public MongoResult<JsonObject> listIndexes(ClientSession clientSession) {
    requireNonNull(clientSession, "clientSession cannot be null");
    com.mongodb.reactivestreams.client.ClientSession __clientSession = clientSession.toDriverClass();
    wrapped.listIndexes(__clientSession);
    return null;
  }

  @Override
  public MongoResult<JsonObject> listIndexes(ClientSession clientSession,
      ListIndexesOptions options) {
    return null;
  }

  @Override
  public Future<Void> dropIndex(String indexName) {
    requireNonNull(indexName, "indexName cannot be null");
    wrapped.dropIndex(indexName);
    return null;
  }

  @Override
  public io.vertx.mongo.client.MongoCollection<TDocument> dropIndex(String indexName,
      Handler<AsyncResult<Void>> resultHandler) {
    Future<Void> future = this.dropIndex(indexName);
    setHandler(future, resultHandler);
    return this;
  }

  @Override
  public Future<Void> dropIndex(JsonObject keys) {
    requireNonNull(keys, "keys cannot be null");
    Bson __keys = ConversionUtilsImpl.INSTANCE.toBson(keys);
    wrapped.dropIndex(__keys);
    return null;
  }

  @Override
  public io.vertx.mongo.client.MongoCollection<TDocument> dropIndex(JsonObject keys,
      Handler<AsyncResult<Void>> resultHandler) {
    Future<Void> future = this.dropIndex(keys);
    setHandler(future, resultHandler);
    return this;
  }

  @Override
  public Future<Void> dropIndex(String indexName, DropIndexOptions dropIndexOptions) {
    requireNonNull(indexName, "indexName cannot be null");
    requireNonNull(dropIndexOptions, "dropIndexOptions cannot be null");
    com.mongodb.client.model.DropIndexOptions __dropIndexOptions = dropIndexOptions.toDriverClass();
    wrapped.dropIndex(indexName, __dropIndexOptions);
    return null;
  }

  @Override
  public io.vertx.mongo.client.MongoCollection<TDocument> dropIndex(String indexName,
      DropIndexOptions dropIndexOptions, Handler<AsyncResult<Void>> resultHandler) {
    Future<Void> future = this.dropIndex(indexName, dropIndexOptions);
    setHandler(future, resultHandler);
    return this;
  }

  @Override
  public Future<Void> dropIndex(JsonObject keys, DropIndexOptions dropIndexOptions) {
    requireNonNull(keys, "keys cannot be null");
    requireNonNull(dropIndexOptions, "dropIndexOptions cannot be null");
    Bson __keys = ConversionUtilsImpl.INSTANCE.toBson(keys);
    com.mongodb.client.model.DropIndexOptions __dropIndexOptions = dropIndexOptions.toDriverClass();
    wrapped.dropIndex(__keys, __dropIndexOptions);
    return null;
  }

  @Override
  public io.vertx.mongo.client.MongoCollection<TDocument> dropIndex(JsonObject keys,
      DropIndexOptions dropIndexOptions, Handler<AsyncResult<Void>> resultHandler) {
    Future<Void> future = this.dropIndex(keys, dropIndexOptions);
    setHandler(future, resultHandler);
    return this;
  }

  @Override
  public Future<Void> dropIndex(ClientSession clientSession, String indexName) {
    requireNonNull(clientSession, "clientSession cannot be null");
    requireNonNull(indexName, "indexName cannot be null");
    com.mongodb.reactivestreams.client.ClientSession __clientSession = clientSession.toDriverClass();
    wrapped.dropIndex(__clientSession, indexName);
    return null;
  }

  @Override
  public io.vertx.mongo.client.MongoCollection<TDocument> dropIndex(ClientSession clientSession,
      String indexName, Handler<AsyncResult<Void>> resultHandler) {
    Future<Void> future = this.dropIndex(clientSession, indexName);
    setHandler(future, resultHandler);
    return this;
  }

  @Override
  public Future<Void> dropIndex(ClientSession clientSession, JsonObject keys) {
    requireNonNull(clientSession, "clientSession cannot be null");
    requireNonNull(keys, "keys cannot be null");
    com.mongodb.reactivestreams.client.ClientSession __clientSession = clientSession.toDriverClass();
    Bson __keys = ConversionUtilsImpl.INSTANCE.toBson(keys);
    wrapped.dropIndex(__clientSession, __keys);
    return null;
  }

  @Override
  public io.vertx.mongo.client.MongoCollection<TDocument> dropIndex(ClientSession clientSession,
      JsonObject keys, Handler<AsyncResult<Void>> resultHandler) {
    Future<Void> future = this.dropIndex(clientSession, keys);
    setHandler(future, resultHandler);
    return this;
  }

  @Override
  public Future<Void> dropIndex(ClientSession clientSession, String indexName,
      DropIndexOptions dropIndexOptions) {
    requireNonNull(clientSession, "clientSession cannot be null");
    requireNonNull(indexName, "indexName cannot be null");
    requireNonNull(dropIndexOptions, "dropIndexOptions cannot be null");
    com.mongodb.reactivestreams.client.ClientSession __clientSession = clientSession.toDriverClass();
    com.mongodb.client.model.DropIndexOptions __dropIndexOptions = dropIndexOptions.toDriverClass();
    wrapped.dropIndex(__clientSession, indexName, __dropIndexOptions);
    return null;
  }

  @Override
  public io.vertx.mongo.client.MongoCollection<TDocument> dropIndex(ClientSession clientSession,
      String indexName, DropIndexOptions dropIndexOptions,
      Handler<AsyncResult<Void>> resultHandler) {
    Future<Void> future = this.dropIndex(clientSession, indexName, dropIndexOptions);
    setHandler(future, resultHandler);
    return this;
  }

  @Override
  public Future<Void> dropIndex(ClientSession clientSession, JsonObject keys,
      DropIndexOptions dropIndexOptions) {
    requireNonNull(clientSession, "clientSession cannot be null");
    requireNonNull(keys, "keys cannot be null");
    requireNonNull(dropIndexOptions, "dropIndexOptions cannot be null");
    com.mongodb.reactivestreams.client.ClientSession __clientSession = clientSession.toDriverClass();
    Bson __keys = ConversionUtilsImpl.INSTANCE.toBson(keys);
    com.mongodb.client.model.DropIndexOptions __dropIndexOptions = dropIndexOptions.toDriverClass();
    wrapped.dropIndex(__clientSession, __keys, __dropIndexOptions);
    return null;
  }

  @Override
  public io.vertx.mongo.client.MongoCollection<TDocument> dropIndex(ClientSession clientSession,
      JsonObject keys, DropIndexOptions dropIndexOptions,
      Handler<AsyncResult<Void>> resultHandler) {
    Future<Void> future = this.dropIndex(clientSession, keys, dropIndexOptions);
    setHandler(future, resultHandler);
    return this;
  }

  @Override
  public Future<Void> dropIndexes() {
    wrapped.dropIndexes();
    return null;
  }

  @Override
  public io.vertx.mongo.client.MongoCollection<TDocument> dropIndexes(
      Handler<AsyncResult<Void>> resultHandler) {
    Future<Void> future = this.dropIndexes();
    setHandler(future, resultHandler);
    return this;
  }

  @Override
  public Future<Void> dropIndexes(DropIndexOptions dropIndexOptions) {
    requireNonNull(dropIndexOptions, "dropIndexOptions cannot be null");
    com.mongodb.client.model.DropIndexOptions __dropIndexOptions = dropIndexOptions.toDriverClass();
    wrapped.dropIndexes(__dropIndexOptions);
    return null;
  }

  @Override
  public io.vertx.mongo.client.MongoCollection<TDocument> dropIndexes(
      DropIndexOptions dropIndexOptions, Handler<AsyncResult<Void>> resultHandler) {
    Future<Void> future = this.dropIndexes(dropIndexOptions);
    setHandler(future, resultHandler);
    return this;
  }

  @Override
  public Future<Void> dropIndexes(ClientSession clientSession) {
    requireNonNull(clientSession, "clientSession cannot be null");
    com.mongodb.reactivestreams.client.ClientSession __clientSession = clientSession.toDriverClass();
    wrapped.dropIndexes(__clientSession);
    return null;
  }

  @Override
  public io.vertx.mongo.client.MongoCollection<TDocument> dropIndexes(ClientSession clientSession,
      Handler<AsyncResult<Void>> resultHandler) {
    Future<Void> future = this.dropIndexes(clientSession);
    setHandler(future, resultHandler);
    return this;
  }

  @Override
  public Future<Void> dropIndexes(ClientSession clientSession, DropIndexOptions dropIndexOptions) {
    requireNonNull(clientSession, "clientSession cannot be null");
    requireNonNull(dropIndexOptions, "dropIndexOptions cannot be null");
    com.mongodb.reactivestreams.client.ClientSession __clientSession = clientSession.toDriverClass();
    com.mongodb.client.model.DropIndexOptions __dropIndexOptions = dropIndexOptions.toDriverClass();
    wrapped.dropIndexes(__clientSession, __dropIndexOptions);
    return null;
  }

  @Override
  public io.vertx.mongo.client.MongoCollection<TDocument> dropIndexes(ClientSession clientSession,
      DropIndexOptions dropIndexOptions, Handler<AsyncResult<Void>> resultHandler) {
    Future<Void> future = this.dropIndexes(clientSession, dropIndexOptions);
    setHandler(future, resultHandler);
    return this;
  }

  @Override
  public Future<Void> renameCollection(MongoNamespace newCollectionNamespace) {
    requireNonNull(newCollectionNamespace, "newCollectionNamespace cannot be null");
    com.mongodb.MongoNamespace __newCollectionNamespace = newCollectionNamespace.toDriverClass();
    wrapped.renameCollection(__newCollectionNamespace);
    return null;
  }

  @Override
  public io.vertx.mongo.client.MongoCollection<TDocument> renameCollection(
      MongoNamespace newCollectionNamespace, Handler<AsyncResult<Void>> resultHandler) {
    Future<Void> future = this.renameCollection(newCollectionNamespace);
    setHandler(future, resultHandler);
    return this;
  }

  @Override
  public Future<Void> renameCollection(MongoNamespace newCollectionNamespace,
      RenameCollectionOptions options) {
    requireNonNull(newCollectionNamespace, "newCollectionNamespace cannot be null");
    requireNonNull(options, "options cannot be null");
    com.mongodb.MongoNamespace __newCollectionNamespace = newCollectionNamespace.toDriverClass();
    com.mongodb.client.model.RenameCollectionOptions __options = options.toDriverClass();
    wrapped.renameCollection(__newCollectionNamespace, __options);
    return null;
  }

  @Override
  public io.vertx.mongo.client.MongoCollection<TDocument> renameCollection(
      MongoNamespace newCollectionNamespace, RenameCollectionOptions options,
      Handler<AsyncResult<Void>> resultHandler) {
    Future<Void> future = this.renameCollection(newCollectionNamespace, options);
    setHandler(future, resultHandler);
    return this;
  }

  @Override
  public Future<Void> renameCollection(ClientSession clientSession,
      MongoNamespace newCollectionNamespace) {
    requireNonNull(clientSession, "clientSession cannot be null");
    requireNonNull(newCollectionNamespace, "newCollectionNamespace cannot be null");
    com.mongodb.reactivestreams.client.ClientSession __clientSession = clientSession.toDriverClass();
    com.mongodb.MongoNamespace __newCollectionNamespace = newCollectionNamespace.toDriverClass();
    wrapped.renameCollection(__clientSession, __newCollectionNamespace);
    return null;
  }

  @Override
  public io.vertx.mongo.client.MongoCollection<TDocument> renameCollection(
      ClientSession clientSession, MongoNamespace newCollectionNamespace,
      Handler<AsyncResult<Void>> resultHandler) {
    Future<Void> future = this.renameCollection(clientSession, newCollectionNamespace);
    setHandler(future, resultHandler);
    return this;
  }

  @Override
  public Future<Void> renameCollection(ClientSession clientSession,
      MongoNamespace newCollectionNamespace, RenameCollectionOptions options) {
    requireNonNull(clientSession, "clientSession cannot be null");
    requireNonNull(newCollectionNamespace, "newCollectionNamespace cannot be null");
    requireNonNull(options, "options cannot be null");
    com.mongodb.reactivestreams.client.ClientSession __clientSession = clientSession.toDriverClass();
    com.mongodb.MongoNamespace __newCollectionNamespace = newCollectionNamespace.toDriverClass();
    com.mongodb.client.model.RenameCollectionOptions __options = options.toDriverClass();
    wrapped.renameCollection(__clientSession, __newCollectionNamespace, __options);
    return null;
  }

  @Override
  public io.vertx.mongo.client.MongoCollection<TDocument> renameCollection(
      ClientSession clientSession, MongoNamespace newCollectionNamespace,
      RenameCollectionOptions options, Handler<AsyncResult<Void>> resultHandler) {
    Future<Void> future = this.renameCollection(clientSession, newCollectionNamespace, options);
    setHandler(future, resultHandler);
    return this;
  }

  public MongoCollection<TDocument> toDriverClass() {
    return wrapped;
  }
}
