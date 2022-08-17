package io.vertx.mongo.client.impl;

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
import io.vertx.mongo.client.AggregateOptions;
import io.vertx.mongo.client.ChangeStreamOptions;
import io.vertx.mongo.client.ClientSession;
import io.vertx.mongo.client.FindOptions;
import io.vertx.mongo.client.ListIndexesOptions;
import io.vertx.mongo.client.MapReduceOptions;
import io.vertx.mongo.client.MongoCollection;
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
import io.vertx.mongo.impl.Utils;
import java.lang.Class;
import java.lang.Long;
import java.lang.Override;
import java.lang.String;
import java.lang.Void;
import java.util.List;

public class MongoCollectionImpl<TDocument> extends MongoCollectionBase<TDocument> {
  @Override
  public MongoNamespace getNamespace() {
    return null;
  }

  @Override
  public Class<TDocument> getDocumentClass() {
    return null;
  }

  @Override
  public ReadPreference getReadPreference() {
    return null;
  }

  @Override
  public WriteConcern getWriteConcern() {
    return null;
  }

  @Override
  public ReadConcern getReadConcern() {
    return null;
  }

  @Override
  public <NewTDocument> MongoCollection withDocumentClass(Class<NewTDocument> clazz) {
    return null;
  }

  @Override
  public MongoCollection<TDocument> withReadPreference(ReadPreference readPreference) {
    return null;
  }

  @Override
  public MongoCollection<TDocument> withWriteConcern(WriteConcern writeConcern) {
    return null;
  }

  @Override
  public MongoCollection<TDocument> withReadConcern(ReadConcern readConcern) {
    return null;
  }

  @Override
  public Future<Long> estimatedDocumentCount() {
    return null;
  }

  @Override
  public MongoCollection<TDocument> estimatedDocumentCount(
      Handler<AsyncResult<Long>> resultHandler) {
    Future<Long> future = this.estimatedDocumentCount();
    Utils.setHandler(future, resultHandler);
    return this;
  }

  @Override
  public Future<Long> estimatedDocumentCount(EstimatedDocumentCountOptions options) {
    return null;
  }

  @Override
  public MongoCollection<TDocument> estimatedDocumentCount(EstimatedDocumentCountOptions options,
      Handler<AsyncResult<Long>> resultHandler) {
    Future<Long> future = this.estimatedDocumentCount(options);
    Utils.setHandler(future, resultHandler);
    return this;
  }

  @Override
  public Future<Long> countDocuments() {
    return null;
  }

  @Override
  public MongoCollection<TDocument> countDocuments(Handler<AsyncResult<Long>> resultHandler) {
    Future<Long> future = this.countDocuments();
    Utils.setHandler(future, resultHandler);
    return this;
  }

  @Override
  public Future<Long> countDocuments(JsonObject filter) {
    return null;
  }

  @Override
  public MongoCollection<TDocument> countDocuments(JsonObject filter,
      Handler<AsyncResult<Long>> resultHandler) {
    Future<Long> future = this.countDocuments(filter);
    Utils.setHandler(future, resultHandler);
    return this;
  }

  @Override
  public Future<Long> countDocuments(JsonObject filter, CountOptions options) {
    return null;
  }

  @Override
  public MongoCollection<TDocument> countDocuments(JsonObject filter, CountOptions options,
      Handler<AsyncResult<Long>> resultHandler) {
    Future<Long> future = this.countDocuments(filter, options);
    Utils.setHandler(future, resultHandler);
    return this;
  }

  @Override
  public Future<Long> countDocuments(ClientSession clientSession) {
    return null;
  }

  @Override
  public MongoCollection<TDocument> countDocuments(ClientSession clientSession,
      Handler<AsyncResult<Long>> resultHandler) {
    Future<Long> future = this.countDocuments(clientSession);
    Utils.setHandler(future, resultHandler);
    return this;
  }

  @Override
  public Future<Long> countDocuments(ClientSession clientSession, JsonObject filter) {
    return null;
  }

  @Override
  public MongoCollection<TDocument> countDocuments(ClientSession clientSession, JsonObject filter,
      Handler<AsyncResult<Long>> resultHandler) {
    Future<Long> future = this.countDocuments(clientSession, filter);
    Utils.setHandler(future, resultHandler);
    return this;
  }

  @Override
  public Future<Long> countDocuments(ClientSession clientSession, JsonObject filter,
      CountOptions options) {
    return null;
  }

  @Override
  public MongoCollection<TDocument> countDocuments(ClientSession clientSession, JsonObject filter,
      CountOptions options, Handler<AsyncResult<Long>> resultHandler) {
    Future<Long> future = this.countDocuments(clientSession, filter, options);
    Utils.setHandler(future, resultHandler);
    return this;
  }

  @Override
  public MongoResult<TDocument> find() {
    return null;
  }

  @Override
  public MongoResult<TDocument> find(FindOptions options) {
    return null;
  }

  @Override
  public MongoResult<TDocument> find(JsonObject filter) {
    return null;
  }

  @Override
  public MongoResult<TDocument> find(JsonObject filter, FindOptions options) {
    return null;
  }

  @Override
  public MongoResult<TDocument> find(ClientSession clientSession) {
    return null;
  }

  @Override
  public MongoResult<TDocument> find(ClientSession clientSession, FindOptions options) {
    return null;
  }

  @Override
  public MongoResult<TDocument> find(ClientSession clientSession, JsonObject filter) {
    return null;
  }

  @Override
  public MongoResult<TDocument> find(ClientSession clientSession, JsonObject filter,
      FindOptions options) {
    return null;
  }

  @Override
  public MongoResult<TDocument> aggregate(List<JsonObject> pipeline) {
    return null;
  }

  @Override
  public MongoResult<TDocument> aggregate(List<JsonObject> pipeline, AggregateOptions options) {
    return null;
  }

  @Override
  public MongoResult<TDocument> aggregate(ClientSession clientSession, List<JsonObject> pipeline) {
    return null;
  }

  @Override
  public MongoResult<TDocument> aggregate(ClientSession clientSession, List<JsonObject> pipeline,
      AggregateOptions options) {
    return null;
  }

  @Override
  public ReadStream<JsonObject> watch() {
    return null;
  }

  @Override
  public ReadStream<JsonObject> watch(ChangeStreamOptions options) {
    return null;
  }

  @Override
  public ReadStream<JsonObject> watch(List<JsonObject> pipeline) {
    return null;
  }

  @Override
  public ReadStream<JsonObject> watch(List<JsonObject> pipeline, ChangeStreamOptions options) {
    return null;
  }

  @Override
  public ReadStream<JsonObject> watch(ClientSession clientSession) {
    return null;
  }

  @Override
  public ReadStream<JsonObject> watch(ClientSession clientSession, ChangeStreamOptions options) {
    return null;
  }

  @Override
  public ReadStream<JsonObject> watch(ClientSession clientSession, List<JsonObject> pipeline) {
    return null;
  }

  @Override
  public ReadStream<JsonObject> watch(ClientSession clientSession, List<JsonObject> pipeline,
      ChangeStreamOptions options) {
    return null;
  }

  @Override
  public MongoResult<TDocument> mapReduce(String mapFunction, String reduceFunction) {
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
    return null;
  }

  @Override
  public MongoResult<TDocument> mapReduce(ClientSession clientSession, String mapFunction,
      String reduceFunction, MapReduceOptions options) {
    return null;
  }

  @Override
  public Future<BulkWriteResult> bulkWrite(List<JsonObject> requests) {
    return null;
  }

  @Override
  public MongoCollection<TDocument> bulkWrite(List<JsonObject> requests,
      Handler<AsyncResult<BulkWriteResult>> resultHandler) {
    Future<BulkWriteResult> future = this.bulkWrite(requests);
    Utils.setHandler(future, resultHandler);
    return this;
  }

  @Override
  public Future<BulkWriteResult> bulkWrite(List<JsonObject> requests, BulkWriteOptions options) {
    return null;
  }

  @Override
  public MongoCollection<TDocument> bulkWrite(List<JsonObject> requests, BulkWriteOptions options,
      Handler<AsyncResult<BulkWriteResult>> resultHandler) {
    Future<BulkWriteResult> future = this.bulkWrite(requests, options);
    Utils.setHandler(future, resultHandler);
    return this;
  }

  @Override
  public Future<BulkWriteResult> bulkWrite(ClientSession clientSession, List<JsonObject> requests) {
    return null;
  }

  @Override
  public MongoCollection<TDocument> bulkWrite(ClientSession clientSession,
      List<JsonObject> requests, Handler<AsyncResult<BulkWriteResult>> resultHandler) {
    Future<BulkWriteResult> future = this.bulkWrite(clientSession, requests);
    Utils.setHandler(future, resultHandler);
    return this;
  }

  @Override
  public Future<BulkWriteResult> bulkWrite(ClientSession clientSession, List<JsonObject> requests,
      BulkWriteOptions options) {
    return null;
  }

  @Override
  public MongoCollection<TDocument> bulkWrite(ClientSession clientSession,
      List<JsonObject> requests, BulkWriteOptions options,
      Handler<AsyncResult<BulkWriteResult>> resultHandler) {
    Future<BulkWriteResult> future = this.bulkWrite(clientSession, requests, options);
    Utils.setHandler(future, resultHandler);
    return this;
  }

  @Override
  public Future<InsertOneResult> insertOne(TDocument document) {
    return null;
  }

  @Override
  public MongoCollection<TDocument> insertOne(TDocument document,
      Handler<AsyncResult<InsertOneResult>> resultHandler) {
    Future<InsertOneResult> future = this.insertOne(document);
    Utils.setHandler(future, resultHandler);
    return this;
  }

  @Override
  public Future<InsertOneResult> insertOne(TDocument document, InsertOneOptions options) {
    return null;
  }

  @Override
  public MongoCollection<TDocument> insertOne(TDocument document, InsertOneOptions options,
      Handler<AsyncResult<InsertOneResult>> resultHandler) {
    Future<InsertOneResult> future = this.insertOne(document, options);
    Utils.setHandler(future, resultHandler);
    return this;
  }

  @Override
  public Future<InsertOneResult> insertOne(ClientSession clientSession, TDocument document) {
    return null;
  }

  @Override
  public MongoCollection<TDocument> insertOne(ClientSession clientSession, TDocument document,
      Handler<AsyncResult<InsertOneResult>> resultHandler) {
    Future<InsertOneResult> future = this.insertOne(clientSession, document);
    Utils.setHandler(future, resultHandler);
    return this;
  }

  @Override
  public Future<InsertOneResult> insertOne(ClientSession clientSession, TDocument document,
      InsertOneOptions options) {
    return null;
  }

  @Override
  public MongoCollection<TDocument> insertOne(ClientSession clientSession, TDocument document,
      InsertOneOptions options, Handler<AsyncResult<InsertOneResult>> resultHandler) {
    Future<InsertOneResult> future = this.insertOne(clientSession, document, options);
    Utils.setHandler(future, resultHandler);
    return this;
  }

  @Override
  public Future<InsertManyResult> insertMany(List<? super TDocument> documents) {
    return null;
  }

  @Override
  public MongoCollection<TDocument> insertMany(List<? super TDocument> documents,
      Handler<AsyncResult<InsertManyResult>> resultHandler) {
    Future<InsertManyResult> future = this.insertMany(documents);
    Utils.setHandler(future, resultHandler);
    return this;
  }

  @Override
  public Future<InsertManyResult> insertMany(List<? super TDocument> documents,
      InsertManyOptions options) {
    return null;
  }

  @Override
  public MongoCollection<TDocument> insertMany(List<? super TDocument> documents,
      InsertManyOptions options, Handler<AsyncResult<InsertManyResult>> resultHandler) {
    Future<InsertManyResult> future = this.insertMany(documents, options);
    Utils.setHandler(future, resultHandler);
    return this;
  }

  @Override
  public Future<InsertManyResult> insertMany(ClientSession clientSession,
      List<? super TDocument> documents) {
    return null;
  }

  @Override
  public MongoCollection<TDocument> insertMany(ClientSession clientSession,
      List<? super TDocument> documents, Handler<AsyncResult<InsertManyResult>> resultHandler) {
    Future<InsertManyResult> future = this.insertMany(clientSession, documents);
    Utils.setHandler(future, resultHandler);
    return this;
  }

  @Override
  public Future<InsertManyResult> insertMany(ClientSession clientSession,
      List<? super TDocument> documents, InsertManyOptions options) {
    return null;
  }

  @Override
  public MongoCollection<TDocument> insertMany(ClientSession clientSession,
      List<? super TDocument> documents, InsertManyOptions options,
      Handler<AsyncResult<InsertManyResult>> resultHandler) {
    Future<InsertManyResult> future = this.insertMany(clientSession, documents, options);
    Utils.setHandler(future, resultHandler);
    return this;
  }

  @Override
  public Future<DeleteResult> deleteOne(JsonObject filter) {
    return null;
  }

  @Override
  public MongoCollection<TDocument> deleteOne(JsonObject filter,
      Handler<AsyncResult<DeleteResult>> resultHandler) {
    Future<DeleteResult> future = this.deleteOne(filter);
    Utils.setHandler(future, resultHandler);
    return this;
  }

  @Override
  public Future<DeleteResult> deleteOne(JsonObject filter, DeleteOptions options) {
    return null;
  }

  @Override
  public MongoCollection<TDocument> deleteOne(JsonObject filter, DeleteOptions options,
      Handler<AsyncResult<DeleteResult>> resultHandler) {
    Future<DeleteResult> future = this.deleteOne(filter, options);
    Utils.setHandler(future, resultHandler);
    return this;
  }

  @Override
  public Future<DeleteResult> deleteOne(ClientSession clientSession, JsonObject filter) {
    return null;
  }

  @Override
  public MongoCollection<TDocument> deleteOne(ClientSession clientSession, JsonObject filter,
      Handler<AsyncResult<DeleteResult>> resultHandler) {
    Future<DeleteResult> future = this.deleteOne(clientSession, filter);
    Utils.setHandler(future, resultHandler);
    return this;
  }

  @Override
  public Future<DeleteResult> deleteOne(ClientSession clientSession, JsonObject filter,
      DeleteOptions options) {
    return null;
  }

  @Override
  public MongoCollection<TDocument> deleteOne(ClientSession clientSession, JsonObject filter,
      DeleteOptions options, Handler<AsyncResult<DeleteResult>> resultHandler) {
    Future<DeleteResult> future = this.deleteOne(clientSession, filter, options);
    Utils.setHandler(future, resultHandler);
    return this;
  }

  @Override
  public Future<DeleteResult> deleteMany(JsonObject filter) {
    return null;
  }

  @Override
  public MongoCollection<TDocument> deleteMany(JsonObject filter,
      Handler<AsyncResult<DeleteResult>> resultHandler) {
    Future<DeleteResult> future = this.deleteMany(filter);
    Utils.setHandler(future, resultHandler);
    return this;
  }

  @Override
  public Future<DeleteResult> deleteMany(JsonObject filter, DeleteOptions options) {
    return null;
  }

  @Override
  public MongoCollection<TDocument> deleteMany(JsonObject filter, DeleteOptions options,
      Handler<AsyncResult<DeleteResult>> resultHandler) {
    Future<DeleteResult> future = this.deleteMany(filter, options);
    Utils.setHandler(future, resultHandler);
    return this;
  }

  @Override
  public Future<DeleteResult> deleteMany(ClientSession clientSession, JsonObject filter) {
    return null;
  }

  @Override
  public MongoCollection<TDocument> deleteMany(ClientSession clientSession, JsonObject filter,
      Handler<AsyncResult<DeleteResult>> resultHandler) {
    Future<DeleteResult> future = this.deleteMany(clientSession, filter);
    Utils.setHandler(future, resultHandler);
    return this;
  }

  @Override
  public Future<DeleteResult> deleteMany(ClientSession clientSession, JsonObject filter,
      DeleteOptions options) {
    return null;
  }

  @Override
  public MongoCollection<TDocument> deleteMany(ClientSession clientSession, JsonObject filter,
      DeleteOptions options, Handler<AsyncResult<DeleteResult>> resultHandler) {
    Future<DeleteResult> future = this.deleteMany(clientSession, filter, options);
    Utils.setHandler(future, resultHandler);
    return this;
  }

  @Override
  public Future<UpdateResult> replaceOne(JsonObject filter, TDocument replacement) {
    return null;
  }

  @Override
  public MongoCollection<TDocument> replaceOne(JsonObject filter, TDocument replacement,
      Handler<AsyncResult<UpdateResult>> resultHandler) {
    Future<UpdateResult> future = this.replaceOne(filter, replacement);
    Utils.setHandler(future, resultHandler);
    return this;
  }

  @Override
  public Future<UpdateResult> replaceOne(JsonObject filter, TDocument replacement,
      ReplaceOptions options) {
    return null;
  }

  @Override
  public MongoCollection<TDocument> replaceOne(JsonObject filter, TDocument replacement,
      ReplaceOptions options, Handler<AsyncResult<UpdateResult>> resultHandler) {
    Future<UpdateResult> future = this.replaceOne(filter, replacement, options);
    Utils.setHandler(future, resultHandler);
    return this;
  }

  @Override
  public Future<UpdateResult> replaceOne(ClientSession clientSession, JsonObject filter,
      TDocument replacement) {
    return null;
  }

  @Override
  public MongoCollection<TDocument> replaceOne(ClientSession clientSession, JsonObject filter,
      TDocument replacement, Handler<AsyncResult<UpdateResult>> resultHandler) {
    Future<UpdateResult> future = this.replaceOne(clientSession, filter, replacement);
    Utils.setHandler(future, resultHandler);
    return this;
  }

  @Override
  public Future<UpdateResult> replaceOne(ClientSession clientSession, JsonObject filter,
      TDocument replacement, ReplaceOptions options) {
    return null;
  }

  @Override
  public MongoCollection<TDocument> replaceOne(ClientSession clientSession, JsonObject filter,
      TDocument replacement, ReplaceOptions options,
      Handler<AsyncResult<UpdateResult>> resultHandler) {
    Future<UpdateResult> future = this.replaceOne(clientSession, filter, replacement, options);
    Utils.setHandler(future, resultHandler);
    return this;
  }

  @Override
  public Future<UpdateResult> updateOne(JsonObject filter, JsonObject update) {
    return null;
  }

  @Override
  public MongoCollection<TDocument> updateOne(JsonObject filter, JsonObject update,
      Handler<AsyncResult<UpdateResult>> resultHandler) {
    Future<UpdateResult> future = this.updateOne(filter, update);
    Utils.setHandler(future, resultHandler);
    return this;
  }

  @Override
  public Future<UpdateResult> updateOne(JsonObject filter, JsonObject update,
      UpdateOptions options) {
    return null;
  }

  @Override
  public MongoCollection<TDocument> updateOne(JsonObject filter, JsonObject update,
      UpdateOptions options, Handler<AsyncResult<UpdateResult>> resultHandler) {
    Future<UpdateResult> future = this.updateOne(filter, update, options);
    Utils.setHandler(future, resultHandler);
    return this;
  }

  @Override
  public Future<UpdateResult> updateOne(ClientSession clientSession, JsonObject filter,
      JsonObject update) {
    return null;
  }

  @Override
  public MongoCollection<TDocument> updateOne(ClientSession clientSession, JsonObject filter,
      JsonObject update, Handler<AsyncResult<UpdateResult>> resultHandler) {
    Future<UpdateResult> future = this.updateOne(clientSession, filter, update);
    Utils.setHandler(future, resultHandler);
    return this;
  }

  @Override
  public Future<UpdateResult> updateOne(ClientSession clientSession, JsonObject filter,
      JsonObject update, UpdateOptions options) {
    return null;
  }

  @Override
  public MongoCollection<TDocument> updateOne(ClientSession clientSession, JsonObject filter,
      JsonObject update, UpdateOptions options, Handler<AsyncResult<UpdateResult>> resultHandler) {
    Future<UpdateResult> future = this.updateOne(clientSession, filter, update, options);
    Utils.setHandler(future, resultHandler);
    return this;
  }

  @Override
  public Future<UpdateResult> updateOne(JsonObject filter, List<JsonObject> update) {
    return null;
  }

  @Override
  public MongoCollection<TDocument> updateOne(JsonObject filter, List<JsonObject> update,
      Handler<AsyncResult<UpdateResult>> resultHandler) {
    Future<UpdateResult> future = this.updateOne(filter, update);
    Utils.setHandler(future, resultHandler);
    return this;
  }

  @Override
  public Future<UpdateResult> updateOne(JsonObject filter, List<JsonObject> update,
      UpdateOptions options) {
    return null;
  }

  @Override
  public MongoCollection<TDocument> updateOne(JsonObject filter, List<JsonObject> update,
      UpdateOptions options, Handler<AsyncResult<UpdateResult>> resultHandler) {
    Future<UpdateResult> future = this.updateOne(filter, update, options);
    Utils.setHandler(future, resultHandler);
    return this;
  }

  @Override
  public Future<UpdateResult> updateOne(ClientSession clientSession, JsonObject filter,
      List<JsonObject> update) {
    return null;
  }

  @Override
  public MongoCollection<TDocument> updateOne(ClientSession clientSession, JsonObject filter,
      List<JsonObject> update, Handler<AsyncResult<UpdateResult>> resultHandler) {
    Future<UpdateResult> future = this.updateOne(clientSession, filter, update);
    Utils.setHandler(future, resultHandler);
    return this;
  }

  @Override
  public Future<UpdateResult> updateOne(ClientSession clientSession, JsonObject filter,
      List<JsonObject> update, UpdateOptions options) {
    return null;
  }

  @Override
  public MongoCollection<TDocument> updateOne(ClientSession clientSession, JsonObject filter,
      List<JsonObject> update, UpdateOptions options,
      Handler<AsyncResult<UpdateResult>> resultHandler) {
    Future<UpdateResult> future = this.updateOne(clientSession, filter, update, options);
    Utils.setHandler(future, resultHandler);
    return this;
  }

  @Override
  public Future<UpdateResult> updateMany(JsonObject filter, JsonObject update) {
    return null;
  }

  @Override
  public MongoCollection<TDocument> updateMany(JsonObject filter, JsonObject update,
      Handler<AsyncResult<UpdateResult>> resultHandler) {
    Future<UpdateResult> future = this.updateMany(filter, update);
    Utils.setHandler(future, resultHandler);
    return this;
  }

  @Override
  public Future<UpdateResult> updateMany(JsonObject filter, JsonObject update,
      UpdateOptions options) {
    return null;
  }

  @Override
  public MongoCollection<TDocument> updateMany(JsonObject filter, JsonObject update,
      UpdateOptions options, Handler<AsyncResult<UpdateResult>> resultHandler) {
    Future<UpdateResult> future = this.updateMany(filter, update, options);
    Utils.setHandler(future, resultHandler);
    return this;
  }

  @Override
  public Future<UpdateResult> updateMany(ClientSession clientSession, JsonObject filter,
      JsonObject update) {
    return null;
  }

  @Override
  public MongoCollection<TDocument> updateMany(ClientSession clientSession, JsonObject filter,
      JsonObject update, Handler<AsyncResult<UpdateResult>> resultHandler) {
    Future<UpdateResult> future = this.updateMany(clientSession, filter, update);
    Utils.setHandler(future, resultHandler);
    return this;
  }

  @Override
  public Future<UpdateResult> updateMany(ClientSession clientSession, JsonObject filter,
      JsonObject update, UpdateOptions options) {
    return null;
  }

  @Override
  public MongoCollection<TDocument> updateMany(ClientSession clientSession, JsonObject filter,
      JsonObject update, UpdateOptions options, Handler<AsyncResult<UpdateResult>> resultHandler) {
    Future<UpdateResult> future = this.updateMany(clientSession, filter, update, options);
    Utils.setHandler(future, resultHandler);
    return this;
  }

  @Override
  public Future<UpdateResult> updateMany(JsonObject filter, List<JsonObject> update) {
    return null;
  }

  @Override
  public MongoCollection<TDocument> updateMany(JsonObject filter, List<JsonObject> update,
      Handler<AsyncResult<UpdateResult>> resultHandler) {
    Future<UpdateResult> future = this.updateMany(filter, update);
    Utils.setHandler(future, resultHandler);
    return this;
  }

  @Override
  public Future<UpdateResult> updateMany(JsonObject filter, List<JsonObject> update,
      UpdateOptions options) {
    return null;
  }

  @Override
  public MongoCollection<TDocument> updateMany(JsonObject filter, List<JsonObject> update,
      UpdateOptions options, Handler<AsyncResult<UpdateResult>> resultHandler) {
    Future<UpdateResult> future = this.updateMany(filter, update, options);
    Utils.setHandler(future, resultHandler);
    return this;
  }

  @Override
  public Future<UpdateResult> updateMany(ClientSession clientSession, JsonObject filter,
      List<JsonObject> update) {
    return null;
  }

  @Override
  public MongoCollection<TDocument> updateMany(ClientSession clientSession, JsonObject filter,
      List<JsonObject> update, Handler<AsyncResult<UpdateResult>> resultHandler) {
    Future<UpdateResult> future = this.updateMany(clientSession, filter, update);
    Utils.setHandler(future, resultHandler);
    return this;
  }

  @Override
  public Future<UpdateResult> updateMany(ClientSession clientSession, JsonObject filter,
      List<JsonObject> update, UpdateOptions options) {
    return null;
  }

  @Override
  public MongoCollection<TDocument> updateMany(ClientSession clientSession, JsonObject filter,
      List<JsonObject> update, UpdateOptions options,
      Handler<AsyncResult<UpdateResult>> resultHandler) {
    Future<UpdateResult> future = this.updateMany(clientSession, filter, update, options);
    Utils.setHandler(future, resultHandler);
    return this;
  }

  @Override
  public Future<TDocument> findOneAndDelete(JsonObject filter) {
    return null;
  }

  @Override
  public MongoCollection<TDocument> findOneAndDelete(JsonObject filter,
      Handler<AsyncResult<TDocument>> resultHandler) {
    Future<TDocument> future = this.findOneAndDelete(filter);
    Utils.setHandler(future, resultHandler);
    return this;
  }

  @Override
  public Future<TDocument> findOneAndDelete(JsonObject filter, FindOneAndDeleteOptions options) {
    return null;
  }

  @Override
  public MongoCollection<TDocument> findOneAndDelete(JsonObject filter,
      FindOneAndDeleteOptions options, Handler<AsyncResult<TDocument>> resultHandler) {
    Future<TDocument> future = this.findOneAndDelete(filter, options);
    Utils.setHandler(future, resultHandler);
    return this;
  }

  @Override
  public Future<TDocument> findOneAndDelete(ClientSession clientSession, JsonObject filter) {
    return null;
  }

  @Override
  public MongoCollection<TDocument> findOneAndDelete(ClientSession clientSession, JsonObject filter,
      Handler<AsyncResult<TDocument>> resultHandler) {
    Future<TDocument> future = this.findOneAndDelete(clientSession, filter);
    Utils.setHandler(future, resultHandler);
    return this;
  }

  @Override
  public Future<TDocument> findOneAndDelete(ClientSession clientSession, JsonObject filter,
      FindOneAndDeleteOptions options) {
    return null;
  }

  @Override
  public MongoCollection<TDocument> findOneAndDelete(ClientSession clientSession, JsonObject filter,
      FindOneAndDeleteOptions options, Handler<AsyncResult<TDocument>> resultHandler) {
    Future<TDocument> future = this.findOneAndDelete(clientSession, filter, options);
    Utils.setHandler(future, resultHandler);
    return this;
  }

  @Override
  public Future<TDocument> findOneAndReplace(JsonObject filter, TDocument replacement) {
    return null;
  }

  @Override
  public MongoCollection<TDocument> findOneAndReplace(JsonObject filter, TDocument replacement,
      Handler<AsyncResult<TDocument>> resultHandler) {
    Future<TDocument> future = this.findOneAndReplace(filter, replacement);
    Utils.setHandler(future, resultHandler);
    return this;
  }

  @Override
  public Future<TDocument> findOneAndReplace(JsonObject filter, TDocument replacement,
      FindOneAndReplaceOptions options) {
    return null;
  }

  @Override
  public MongoCollection<TDocument> findOneAndReplace(JsonObject filter, TDocument replacement,
      FindOneAndReplaceOptions options, Handler<AsyncResult<TDocument>> resultHandler) {
    Future<TDocument> future = this.findOneAndReplace(filter, replacement, options);
    Utils.setHandler(future, resultHandler);
    return this;
  }

  @Override
  public Future<TDocument> findOneAndReplace(ClientSession clientSession, JsonObject filter,
      TDocument replacement) {
    return null;
  }

  @Override
  public MongoCollection<TDocument> findOneAndReplace(ClientSession clientSession,
      JsonObject filter, TDocument replacement, Handler<AsyncResult<TDocument>> resultHandler) {
    Future<TDocument> future = this.findOneAndReplace(clientSession, filter, replacement);
    Utils.setHandler(future, resultHandler);
    return this;
  }

  @Override
  public Future<TDocument> findOneAndReplace(ClientSession clientSession, JsonObject filter,
      TDocument replacement, FindOneAndReplaceOptions options) {
    return null;
  }

  @Override
  public MongoCollection<TDocument> findOneAndReplace(ClientSession clientSession,
      JsonObject filter, TDocument replacement, FindOneAndReplaceOptions options,
      Handler<AsyncResult<TDocument>> resultHandler) {
    Future<TDocument> future = this.findOneAndReplace(clientSession, filter, replacement, options);
    Utils.setHandler(future, resultHandler);
    return this;
  }

  @Override
  public Future<TDocument> findOneAndUpdate(JsonObject filter, JsonObject update) {
    return null;
  }

  @Override
  public MongoCollection<TDocument> findOneAndUpdate(JsonObject filter, JsonObject update,
      Handler<AsyncResult<TDocument>> resultHandler) {
    Future<TDocument> future = this.findOneAndUpdate(filter, update);
    Utils.setHandler(future, resultHandler);
    return this;
  }

  @Override
  public Future<TDocument> findOneAndUpdate(JsonObject filter, JsonObject update,
      FindOneAndUpdateOptions options) {
    return null;
  }

  @Override
  public MongoCollection<TDocument> findOneAndUpdate(JsonObject filter, JsonObject update,
      FindOneAndUpdateOptions options, Handler<AsyncResult<TDocument>> resultHandler) {
    Future<TDocument> future = this.findOneAndUpdate(filter, update, options);
    Utils.setHandler(future, resultHandler);
    return this;
  }

  @Override
  public Future<TDocument> findOneAndUpdate(ClientSession clientSession, JsonObject filter,
      JsonObject update) {
    return null;
  }

  @Override
  public MongoCollection<TDocument> findOneAndUpdate(ClientSession clientSession, JsonObject filter,
      JsonObject update, Handler<AsyncResult<TDocument>> resultHandler) {
    Future<TDocument> future = this.findOneAndUpdate(clientSession, filter, update);
    Utils.setHandler(future, resultHandler);
    return this;
  }

  @Override
  public Future<TDocument> findOneAndUpdate(ClientSession clientSession, JsonObject filter,
      JsonObject update, FindOneAndUpdateOptions options) {
    return null;
  }

  @Override
  public MongoCollection<TDocument> findOneAndUpdate(ClientSession clientSession, JsonObject filter,
      JsonObject update, FindOneAndUpdateOptions options,
      Handler<AsyncResult<TDocument>> resultHandler) {
    Future<TDocument> future = this.findOneAndUpdate(clientSession, filter, update, options);
    Utils.setHandler(future, resultHandler);
    return this;
  }

  @Override
  public Future<TDocument> findOneAndUpdate(JsonObject filter, List<JsonObject> update) {
    return null;
  }

  @Override
  public MongoCollection<TDocument> findOneAndUpdate(JsonObject filter, List<JsonObject> update,
      Handler<AsyncResult<TDocument>> resultHandler) {
    Future<TDocument> future = this.findOneAndUpdate(filter, update);
    Utils.setHandler(future, resultHandler);
    return this;
  }

  @Override
  public Future<TDocument> findOneAndUpdate(JsonObject filter, List<JsonObject> update,
      FindOneAndUpdateOptions options) {
    return null;
  }

  @Override
  public MongoCollection<TDocument> findOneAndUpdate(JsonObject filter, List<JsonObject> update,
      FindOneAndUpdateOptions options, Handler<AsyncResult<TDocument>> resultHandler) {
    Future<TDocument> future = this.findOneAndUpdate(filter, update, options);
    Utils.setHandler(future, resultHandler);
    return this;
  }

  @Override
  public Future<TDocument> findOneAndUpdate(ClientSession clientSession, JsonObject filter,
      List<JsonObject> update) {
    return null;
  }

  @Override
  public MongoCollection<TDocument> findOneAndUpdate(ClientSession clientSession, JsonObject filter,
      List<JsonObject> update, Handler<AsyncResult<TDocument>> resultHandler) {
    Future<TDocument> future = this.findOneAndUpdate(clientSession, filter, update);
    Utils.setHandler(future, resultHandler);
    return this;
  }

  @Override
  public Future<TDocument> findOneAndUpdate(ClientSession clientSession, JsonObject filter,
      List<JsonObject> update, FindOneAndUpdateOptions options) {
    return null;
  }

  @Override
  public MongoCollection<TDocument> findOneAndUpdate(ClientSession clientSession, JsonObject filter,
      List<JsonObject> update, FindOneAndUpdateOptions options,
      Handler<AsyncResult<TDocument>> resultHandler) {
    Future<TDocument> future = this.findOneAndUpdate(clientSession, filter, update, options);
    Utils.setHandler(future, resultHandler);
    return this;
  }

  @Override
  public Future<Void> drop() {
    return null;
  }

  @Override
  public MongoCollection<TDocument> drop(Handler<AsyncResult<Void>> resultHandler) {
    Future<Void> future = this.drop();
    Utils.setHandler(future, resultHandler);
    return this;
  }

  @Override
  public Future<Void> drop(ClientSession clientSession) {
    return null;
  }

  @Override
  public MongoCollection<TDocument> drop(ClientSession clientSession,
      Handler<AsyncResult<Void>> resultHandler) {
    Future<Void> future = this.drop(clientSession);
    Utils.setHandler(future, resultHandler);
    return this;
  }

  @Override
  public Future<String> createIndex(JsonObject key) {
    return null;
  }

  @Override
  public MongoCollection<TDocument> createIndex(JsonObject key,
      Handler<AsyncResult<String>> resultHandler) {
    Future<String> future = this.createIndex(key);
    Utils.setHandler(future, resultHandler);
    return this;
  }

  @Override
  public Future<String> createIndex(JsonObject key, IndexOptions options) {
    return null;
  }

  @Override
  public MongoCollection<TDocument> createIndex(JsonObject key, IndexOptions options,
      Handler<AsyncResult<String>> resultHandler) {
    Future<String> future = this.createIndex(key, options);
    Utils.setHandler(future, resultHandler);
    return this;
  }

  @Override
  public Future<String> createIndex(ClientSession clientSession, JsonObject key) {
    return null;
  }

  @Override
  public MongoCollection<TDocument> createIndex(ClientSession clientSession, JsonObject key,
      Handler<AsyncResult<String>> resultHandler) {
    Future<String> future = this.createIndex(clientSession, key);
    Utils.setHandler(future, resultHandler);
    return this;
  }

  @Override
  public Future<String> createIndex(ClientSession clientSession, JsonObject key,
      IndexOptions options) {
    return null;
  }

  @Override
  public MongoCollection<TDocument> createIndex(ClientSession clientSession, JsonObject key,
      IndexOptions options, Handler<AsyncResult<String>> resultHandler) {
    Future<String> future = this.createIndex(clientSession, key, options);
    Utils.setHandler(future, resultHandler);
    return this;
  }

  @Override
  public Future<String> createIndexes(List<IndexModel> indexes) {
    return null;
  }

  @Override
  public MongoCollection<TDocument> createIndexes(List<IndexModel> indexes,
      Handler<AsyncResult<String>> resultHandler) {
    Future<String> future = this.createIndexes(indexes);
    Utils.setHandler(future, resultHandler);
    return this;
  }

  @Override
  public Future<String> createIndexes(List<IndexModel> indexes,
      CreateIndexOptions createIndexOptions) {
    return null;
  }

  @Override
  public MongoCollection<TDocument> createIndexes(List<IndexModel> indexes,
      CreateIndexOptions createIndexOptions, Handler<AsyncResult<String>> resultHandler) {
    Future<String> future = this.createIndexes(indexes, createIndexOptions);
    Utils.setHandler(future, resultHandler);
    return this;
  }

  @Override
  public Future<String> createIndexes(ClientSession clientSession, List<IndexModel> indexes) {
    return null;
  }

  @Override
  public MongoCollection<TDocument> createIndexes(ClientSession clientSession,
      List<IndexModel> indexes, Handler<AsyncResult<String>> resultHandler) {
    Future<String> future = this.createIndexes(clientSession, indexes);
    Utils.setHandler(future, resultHandler);
    return this;
  }

  @Override
  public Future<String> createIndexes(ClientSession clientSession, List<IndexModel> indexes,
      CreateIndexOptions createIndexOptions) {
    return null;
  }

  @Override
  public MongoCollection<TDocument> createIndexes(ClientSession clientSession,
      List<IndexModel> indexes, CreateIndexOptions createIndexOptions,
      Handler<AsyncResult<String>> resultHandler) {
    Future<String> future = this.createIndexes(clientSession, indexes, createIndexOptions);
    Utils.setHandler(future, resultHandler);
    return this;
  }

  @Override
  public MongoResult<JsonObject> listIndexes() {
    return null;
  }

  @Override
  public MongoResult<JsonObject> listIndexes(ListIndexesOptions options) {
    return null;
  }

  @Override
  public MongoResult<JsonObject> listIndexes(ClientSession clientSession) {
    return null;
  }

  @Override
  public MongoResult<JsonObject> listIndexes(ClientSession clientSession,
      ListIndexesOptions options) {
    return null;
  }

  @Override
  public Future<Void> dropIndex(String indexName) {
    return null;
  }

  @Override
  public MongoCollection<TDocument> dropIndex(String indexName,
      Handler<AsyncResult<Void>> resultHandler) {
    Future<Void> future = this.dropIndex(indexName);
    Utils.setHandler(future, resultHandler);
    return this;
  }

  @Override
  public Future<Void> dropIndex(JsonObject keys) {
    return null;
  }

  @Override
  public MongoCollection<TDocument> dropIndex(JsonObject keys,
      Handler<AsyncResult<Void>> resultHandler) {
    Future<Void> future = this.dropIndex(keys);
    Utils.setHandler(future, resultHandler);
    return this;
  }

  @Override
  public Future<Void> dropIndex(String indexName, DropIndexOptions dropIndexOptions) {
    return null;
  }

  @Override
  public MongoCollection<TDocument> dropIndex(String indexName, DropIndexOptions dropIndexOptions,
      Handler<AsyncResult<Void>> resultHandler) {
    Future<Void> future = this.dropIndex(indexName, dropIndexOptions);
    Utils.setHandler(future, resultHandler);
    return this;
  }

  @Override
  public Future<Void> dropIndex(JsonObject keys, DropIndexOptions dropIndexOptions) {
    return null;
  }

  @Override
  public MongoCollection<TDocument> dropIndex(JsonObject keys, DropIndexOptions dropIndexOptions,
      Handler<AsyncResult<Void>> resultHandler) {
    Future<Void> future = this.dropIndex(keys, dropIndexOptions);
    Utils.setHandler(future, resultHandler);
    return this;
  }

  @Override
  public Future<Void> dropIndex(ClientSession clientSession, String indexName) {
    return null;
  }

  @Override
  public MongoCollection<TDocument> dropIndex(ClientSession clientSession, String indexName,
      Handler<AsyncResult<Void>> resultHandler) {
    Future<Void> future = this.dropIndex(clientSession, indexName);
    Utils.setHandler(future, resultHandler);
    return this;
  }

  @Override
  public Future<Void> dropIndex(ClientSession clientSession, JsonObject keys) {
    return null;
  }

  @Override
  public MongoCollection<TDocument> dropIndex(ClientSession clientSession, JsonObject keys,
      Handler<AsyncResult<Void>> resultHandler) {
    Future<Void> future = this.dropIndex(clientSession, keys);
    Utils.setHandler(future, resultHandler);
    return this;
  }

  @Override
  public Future<Void> dropIndex(ClientSession clientSession, String indexName,
      DropIndexOptions dropIndexOptions) {
    return null;
  }

  @Override
  public MongoCollection<TDocument> dropIndex(ClientSession clientSession, String indexName,
      DropIndexOptions dropIndexOptions, Handler<AsyncResult<Void>> resultHandler) {
    Future<Void> future = this.dropIndex(clientSession, indexName, dropIndexOptions);
    Utils.setHandler(future, resultHandler);
    return this;
  }

  @Override
  public Future<Void> dropIndex(ClientSession clientSession, JsonObject keys,
      DropIndexOptions dropIndexOptions) {
    return null;
  }

  @Override
  public MongoCollection<TDocument> dropIndex(ClientSession clientSession, JsonObject keys,
      DropIndexOptions dropIndexOptions, Handler<AsyncResult<Void>> resultHandler) {
    Future<Void> future = this.dropIndex(clientSession, keys, dropIndexOptions);
    Utils.setHandler(future, resultHandler);
    return this;
  }

  @Override
  public Future<Void> dropIndexes() {
    return null;
  }

  @Override
  public MongoCollection<TDocument> dropIndexes(Handler<AsyncResult<Void>> resultHandler) {
    Future<Void> future = this.dropIndexes();
    Utils.setHandler(future, resultHandler);
    return this;
  }

  @Override
  public Future<Void> dropIndexes(DropIndexOptions dropIndexOptions) {
    return null;
  }

  @Override
  public MongoCollection<TDocument> dropIndexes(DropIndexOptions dropIndexOptions,
      Handler<AsyncResult<Void>> resultHandler) {
    Future<Void> future = this.dropIndexes(dropIndexOptions);
    Utils.setHandler(future, resultHandler);
    return this;
  }

  @Override
  public Future<Void> dropIndexes(ClientSession clientSession) {
    return null;
  }

  @Override
  public MongoCollection<TDocument> dropIndexes(ClientSession clientSession,
      Handler<AsyncResult<Void>> resultHandler) {
    Future<Void> future = this.dropIndexes(clientSession);
    Utils.setHandler(future, resultHandler);
    return this;
  }

  @Override
  public Future<Void> dropIndexes(ClientSession clientSession, DropIndexOptions dropIndexOptions) {
    return null;
  }

  @Override
  public MongoCollection<TDocument> dropIndexes(ClientSession clientSession,
      DropIndexOptions dropIndexOptions, Handler<AsyncResult<Void>> resultHandler) {
    Future<Void> future = this.dropIndexes(clientSession, dropIndexOptions);
    Utils.setHandler(future, resultHandler);
    return this;
  }

  @Override
  public Future<Void> renameCollection(MongoNamespace newCollectionNamespace) {
    return null;
  }

  @Override
  public MongoCollection<TDocument> renameCollection(MongoNamespace newCollectionNamespace,
      Handler<AsyncResult<Void>> resultHandler) {
    Future<Void> future = this.renameCollection(newCollectionNamespace);
    Utils.setHandler(future, resultHandler);
    return this;
  }

  @Override
  public Future<Void> renameCollection(MongoNamespace newCollectionNamespace,
      RenameCollectionOptions options) {
    return null;
  }

  @Override
  public MongoCollection<TDocument> renameCollection(MongoNamespace newCollectionNamespace,
      RenameCollectionOptions options, Handler<AsyncResult<Void>> resultHandler) {
    Future<Void> future = this.renameCollection(newCollectionNamespace, options);
    Utils.setHandler(future, resultHandler);
    return this;
  }

  @Override
  public Future<Void> renameCollection(ClientSession clientSession,
      MongoNamespace newCollectionNamespace) {
    return null;
  }

  @Override
  public MongoCollection<TDocument> renameCollection(ClientSession clientSession,
      MongoNamespace newCollectionNamespace, Handler<AsyncResult<Void>> resultHandler) {
    Future<Void> future = this.renameCollection(clientSession, newCollectionNamespace);
    Utils.setHandler(future, resultHandler);
    return this;
  }

  @Override
  public Future<Void> renameCollection(ClientSession clientSession,
      MongoNamespace newCollectionNamespace, RenameCollectionOptions options) {
    return null;
  }

  @Override
  public MongoCollection<TDocument> renameCollection(ClientSession clientSession,
      MongoNamespace newCollectionNamespace, RenameCollectionOptions options,
      Handler<AsyncResult<Void>> resultHandler) {
    Future<Void> future = this.renameCollection(clientSession, newCollectionNamespace, options);
    Utils.setHandler(future, resultHandler);
    return this;
  }
}
