package io.vertx.ext.mongo;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.mongo.client.ClientConfig;
import io.vertx.mongo.client.MongoClient;
import io.vertx.mongo.client.MongoCollection;
import io.vertx.mongo.client.MongoDatabase;
import io.vertx.mongo.client.model.UpdateOptions;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;

/**
 * @author bfreuden
 */
public class MongoClientAggregateUpdateTest extends MongoTestBase {

  protected MongoClient mongoClient;
  private MongoDatabase mongoDatabase;

  @Override
  public void setUp() throws Exception {
    super.setUp();
    ClientConfig config = getConfig();
    mongoClient = MongoClient.create(vertx, config);
    CountDownLatch latch = new CountDownLatch(1);
    mongoDatabase = mongoClient.getDatabase(getDatabaseName());
    dropCollections(mongoDatabase, latch);
    awaitLatch(latch);
  }

  @Override
  public void tearDown() throws Exception {
    mongoClient.close();
    super.tearDown();
  }

  @Test
  public void testAggregateUpdateCollection() {

    String collection = randomCollection();
    MongoCollection<JsonObject> coll = mongoDatabase.getCollection(collection);
    coll.insertOne(new JsonObject().put("price", 10).put("quantity", 1), onSuccess(id -> {
      coll.insertOne(new JsonObject().put("price", 20).put("quantity", 2), onSuccess(id2 -> {
        coll.insertOne(new JsonObject().put("price", 30).put("quantity", 10), onSuccess(id3 -> {
          coll.updateMany(
            // reduce price of low quantity items
            new JsonObject().put("quantity", new JsonObject().put("$lte", 2)),
            new JsonArray().add(new JsonObject().put("$set", new JsonObject().put("price", new JsonObject().put("$subtract", new JsonArray().add("$price").add(2))))),
            onSuccess(res -> {
              assertEquals(2, res.getModifiedCount());
              assertEquals(2, res.getMatchedCount());
              testComplete();
            }));
        }));
      }));
    }));
    await();
  }

  @Test
  public void testAggregateUpdateCollectionWithOptions() {
    String collection = randomCollection();
    MongoCollection<JsonObject> coll = mongoDatabase.getCollection(collection);
    coll.insertOne(new JsonObject().put("price", 10).put("quantity", 1), onSuccess(id -> {
      coll.insertOne(new JsonObject().put("price", 20).put("quantity", 2), onSuccess(id2 -> {
        coll.insertOne(new JsonObject().put("price", 30).put("quantity", 10), onSuccess(id3 -> {
          coll.updateMany(
            // reduce price of low quantity items
            new JsonObject().put("quantity", new JsonObject().put("$lte", 2)),
            new JsonArray().add(new JsonObject().put("$set", new JsonObject().put("price", new JsonObject().put("$subtract", new JsonArray().add("$price").add(2))))),
            new UpdateOptions(), onSuccess(res -> {
              assertEquals(2, res.getModifiedCount());
              assertEquals(2, res.getMatchedCount());
              testComplete();
            }));
        }));
      }));
    }));
    await();
  }

}
