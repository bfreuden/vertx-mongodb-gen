package io.vertx.ext.mongo;

import io.vertx.core.json.JsonObject;
import io.vertx.mongo.client.ClientConfig;
import io.vertx.mongo.client.MongoClient;
import io.vertx.mongo.client.MongoCollection;
import io.vertx.mongo.client.MongoDatabase;
import io.vertx.mongo.impl.codec.json.JsonObjectCodec;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;

/**
 * @author bfreuden
 */
public class MongoClientNoObjectIdTest extends MongoTestBase {

  protected MongoClient mongoClient;
  private MongoDatabase mongoDatabase;
  protected MongoClient mappedMongoClient;
  private MongoDatabase mappedMongoDatabase;

  @Override
  public void setUp() throws Exception {
    super.setUp();
    mappedMongoClient = MongoClient.create(vertx, getConfig().useObjectIds(false));
    mappedMongoDatabase = mappedMongoClient.getDatabase(getDatabaseName());
    mongoClient = MongoClient.create(vertx, getConfig().useObjectIds(true));
    mongoDatabase = mongoClient.getDatabase(getDatabaseName());
    CountDownLatch latch = new CountDownLatch(2);
    dropCollections(mappedMongoDatabase, latch);
    dropCollections(mongoDatabase, latch);
    awaitLatch(latch);
  }

  @Override
  public void tearDown() throws Exception {
    mappedMongoClient.close();
    super.tearDown();
  }

  @Test
  public void testMappedObjectId() {
    String collection = randomCollection();
    MongoCollection<JsonObject> mappedColl = mappedMongoDatabase.getCollection(collection);
    MongoCollection<JsonObject> coll = mongoDatabase.getCollection(collection);
    JsonObject doc = new JsonObject().put("name", "john");
    mappedColl.insertOne(doc, onSuccess(res -> {
      Object insertedId = res.getInsertedId();
      // object ids are mapped to string
//      assertTrue(insertedId instanceof String);
      mappedColl.find(doc).first(onSuccess(resultDoc -> {
        Object _id = resultDoc.getValue("_id");
        // object ids are mapped to string
        assertTrue(_id instanceof String);
        coll.find(doc).first(onSuccess(resultDoc2 -> {
          assertNotNull(resultDoc2);
          Object _id2 = resultDoc2.getValue("_id");
          // although they are stored as ObjectIds in the DB
          assertTrue(_id2 instanceof JsonObject);
          assertTrue(_id2.toString().contains(JsonObjectCodec.OID_FIELD));
          // you can search with a string id:
          mappedColl.find(new JsonObject().put("_id", (String)_id)).first(onSuccess(resultDoc3 -> {
            assertNotNull(resultDoc3);
            Object _id3 = resultDoc3.getValue("_id");
            assertEquals(_id, _id3);
            testComplete();
          }));
        }));
      }));
    }));
    await();
  }


}
