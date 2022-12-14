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

import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import java.lang.Long;
import java.lang.Object;
import java.nio.ByteBuffer;
import java.util.List;
import org.bson.BsonBinary;
import org.bson.BsonDocument;
import org.bson.BsonInt64;
import org.bson.BsonTimestamp;
import org.bson.BsonValue;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

/**
 * @hidden
 */
public interface ConversionUtils {
  Bson toBson(JsonObject from);

  BsonBinary toBsonBinary(byte[] from);

  BsonDocument toBsonDocument(JsonObject from);

  BsonInt64 toBsonInt64(Long from);

  List<Bson> toBsonList(JsonArray from);

  BsonTimestamp toBsonTimestamp(Long from);

  BsonValue toBsonValue(Object from);

  Buffer toBuffer(ByteBuffer from);

  byte[] toByteArray(BsonBinary from);

  ByteBuffer toByteBuffer(Buffer from);

  Document toDocument(JsonObject from);

  JsonObject toJsonObject(Bson from);

  JsonObject toJsonObject(Document from);

  JsonObject toJsonObject(BsonDocument from);

  Long toLong(BsonTimestamp from);

  Long toLong(BsonInt64 from);

  Object toObject(BsonValue from);

  ObjectId toObjectId(io.vertx.mongo.ObjectId from);

  io.vertx.mongo.ObjectId toObjectId(ObjectId from);
}
