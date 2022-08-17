package io.vertx.mongo.impl;

import com.mongodb.ClientSessionOptions;
import com.mongodb.MongoNamespace;
import com.mongodb.ReadConcern;
import com.mongodb.ReadPreference;
import com.mongodb.TransactionOptions;
import com.mongodb.WriteConcern;
import com.mongodb.client.gridfs.model.GridFSDownloadOptions;
import com.mongodb.client.model.BulkWriteOptions;
import com.mongodb.client.model.CountOptions;
import com.mongodb.client.model.CreateCollectionOptions;
import com.mongodb.client.model.CreateIndexOptions;
import com.mongodb.client.model.CreateViewOptions;
import com.mongodb.client.model.DeleteOptions;
import com.mongodb.client.model.DropIndexOptions;
import com.mongodb.client.model.EstimatedDocumentCountOptions;
import com.mongodb.client.model.FindOneAndDeleteOptions;
import com.mongodb.client.model.FindOneAndReplaceOptions;
import com.mongodb.client.model.FindOneAndUpdateOptions;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.InsertManyOptions;
import com.mongodb.client.model.InsertOneOptions;
import com.mongodb.client.model.RenameCollectionOptions;
import com.mongodb.client.model.ReplaceOptions;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.model.vault.DataKeyOptions;
import com.mongodb.client.model.vault.EncryptOptions;
import com.mongodb.reactivestreams.client.ClientSession;
import io.vertx.core.json.JsonObject;
import java.lang.Long;
import org.bson.BsonBinary;
import org.bson.BsonDocument;
import org.bson.BsonTimestamp;
import org.bson.Document;
import org.bson.conversions.Bson;

/**
 * @hidden
 */
public abstract interface ConversionUtils {
  Bson toBson(JsonObject from);

  BsonBinary toBsonBinary(byte[] from);

  BsonDocument toBsonDocument(JsonObject from);

  BsonTimestamp toBsonTimestamp(Long from);

  BulkWriteOptions toBulkWriteOptions(io.vertx.mongo.client.model.BulkWriteOptions from);

  ClientSession toClientSession(io.vertx.mongo.client.ClientSession from);

  ClientSessionOptions toClientSessionOptions(io.vertx.mongo.ClientSessionOptions from);

  CountOptions toCountOptions(io.vertx.mongo.client.model.CountOptions from);

  CreateCollectionOptions toCreateCollectionOptions(
      io.vertx.mongo.client.model.CreateCollectionOptions from);

  CreateIndexOptions toCreateIndexOptions(io.vertx.mongo.client.model.CreateIndexOptions from);

  CreateViewOptions toCreateViewOptions(io.vertx.mongo.client.model.CreateViewOptions from);

  DataKeyOptions toDataKeyOptions(io.vertx.mongo.client.model.vault.DataKeyOptions from);

  DeleteOptions toDeleteOptions(io.vertx.mongo.client.model.DeleteOptions from);

  Document toDocument(JsonObject from);

  DropIndexOptions toDropIndexOptions(io.vertx.mongo.client.model.DropIndexOptions from);

  EncryptOptions toEncryptOptions(io.vertx.mongo.client.model.vault.EncryptOptions from);

  EstimatedDocumentCountOptions toEstimatedDocumentCountOptions(
      io.vertx.mongo.client.model.EstimatedDocumentCountOptions from);

  FindOneAndDeleteOptions toFindOneAndDeleteOptions(
      io.vertx.mongo.client.model.FindOneAndDeleteOptions from);

  FindOneAndReplaceOptions toFindOneAndReplaceOptions(
      io.vertx.mongo.client.model.FindOneAndReplaceOptions from);

  FindOneAndUpdateOptions toFindOneAndUpdateOptions(
      io.vertx.mongo.client.model.FindOneAndUpdateOptions from);

  GridFSDownloadOptions toGridFSDownloadOptions(
      io.vertx.mongo.client.gridfs.model.GridFSDownloadOptions from);

  IndexOptions toIndexOptions(io.vertx.mongo.client.model.IndexOptions from);

  InsertManyOptions toInsertManyOptions(io.vertx.mongo.client.model.InsertManyOptions from);

  InsertOneOptions toInsertOneOptions(io.vertx.mongo.client.model.InsertOneOptions from);

  MongoNamespace toMongoNamespace(io.vertx.mongo.MongoNamespace from);

  ReadConcern toReadConcern(io.vertx.mongo.ReadConcern from);

  ReadPreference toReadPreference(io.vertx.mongo.ReadPreference from);

  RenameCollectionOptions toRenameCollectionOptions(
      io.vertx.mongo.client.model.RenameCollectionOptions from);

  ReplaceOptions toReplaceOptions(io.vertx.mongo.client.model.ReplaceOptions from);

  TransactionOptions toTransactionOptions(io.vertx.mongo.TransactionOptions from);

  UpdateOptions toUpdateOptions(io.vertx.mongo.client.model.UpdateOptions from);

  WriteConcern toWriteConcern(io.vertx.mongo.WriteConcern from);
}
