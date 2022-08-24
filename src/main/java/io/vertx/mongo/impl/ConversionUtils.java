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
package io.vertx.mongo.impl;

import com.mongodb.client.model.DeleteOptions;
import com.mongodb.client.model.IndexModel;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.ReplaceOptions;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.model.WriteModel;
import io.vertx.core.json.JsonObject;
import io.vertx.mongo.bulk.BulkWriteInsert;
import io.vertx.mongo.bulk.BulkWriteUpsert;
import java.lang.Integer;
import java.lang.Long;
import java.lang.Object;
import java.util.List;
import java.util.Map;

import org.bson.*;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

/**
 * @hidden
 */
public abstract interface ConversionUtils {
  Bson toBson(JsonObject from);

  BsonBinary toBsonBinary(byte[] from);

  BsonDocument toBsonDocument(JsonObject from);

  List<Bson> toBsonList(List<JsonObject> from);

  BsonTimestamp toBsonTimestamp(Long from);

  BsonValue toBsonValue(Object from);

  List<BulkWriteInsert> toBulkWriteInsertList(List<com.mongodb.bulk.BulkWriteInsert> from);

  List<BulkWriteUpsert> toBulkWriteUpsertList(List<com.mongodb.bulk.BulkWriteUpsert> from);

  byte[] toByteArray(BsonBinary from);

  DeleteOptions toDeleteOptions(io.vertx.mongo.client.model.DeleteOptions from);

  Document toDocument(JsonObject from);

  List<IndexModel> toIndexModelList(List<io.vertx.mongo.client.model.IndexModel> from);

  IndexOptions toIndexOptions(io.vertx.mongo.client.model.IndexOptions from);

  Map<Integer, Object> toIntegerObjectMap(Map<Integer, BsonValue> from);

  JsonObject toJsonObject(Document from);

  JsonObject toJsonObject(BsonDocument from);

  JsonObject toJsonObject(ObjectId from);

  Object toObject(BsonValue from);

  ObjectId toObjectId(JsonObject from);

  ReplaceOptions toReplaceOptions(io.vertx.mongo.client.model.ReplaceOptions from);

  UpdateOptions toUpdateOptions(io.vertx.mongo.client.model.UpdateOptions from);

  <T> List<WriteModel<T>> toWriteModelList(List<io.vertx.mongo.client.model.WriteModel<T>> from);

  Long toLong(BsonTimestamp clusterTime);
  Long toLong(BsonInt64 clusterTime);
}
