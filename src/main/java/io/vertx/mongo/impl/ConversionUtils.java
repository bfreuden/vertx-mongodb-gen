package io.vertx.mongo.impl;

import io.vertx.core.json.JsonObject;
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

  Document toDocument(JsonObject from);

  ObjectId toObjectId(JsonObject from);
}
