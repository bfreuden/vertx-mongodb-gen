package io.vertx.mongo.impl;

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

  Document toDocument(JsonObject from);
}
