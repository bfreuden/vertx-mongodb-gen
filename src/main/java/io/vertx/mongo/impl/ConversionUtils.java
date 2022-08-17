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

import io.vertx.core.json.JsonObject;
import io.vertx.mongo.client.ClientSession;
import java.lang.Long;
import java.lang.Object;
import java.util.List;
import org.bson.BsonBinary;
import org.bson.BsonDocument;
import org.bson.BsonTimestamp;
import org.bson.BsonValue;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

/**
 * @hidden
 */
public abstract interface ConversionUtils {
  Bson toBson(JsonObject from);

  BsonBinary toBsonBinary(byte[] from);

  BsonDocument toBsonDocument(JsonObject from);

  List<? extends Bson> toBsonList(List<JsonObject> from);

  BsonTimestamp toBsonTimestamp(Long from);

  BsonValue toBsonValue(Object from);

  byte[] toByteArray(BsonBinary from);

  ClientSession toClientSession(com.mongodb.reactivestreams.client.ClientSession from);

  Document toDocument(JsonObject from);

  JsonObject toJsonObject(Document from);

  JsonObject toJsonObject(Document from);

  Object toObject(BsonValue from);

  ObjectId toObjectId(JsonObject from);
}
