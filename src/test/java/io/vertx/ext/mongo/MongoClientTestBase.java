package io.vertx.ext.mongo;

import com.mongodb.client.model.ReturnDocument;
import io.vertx.core.Context;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.mongo.bulk.BulkWriteUpsert;
import io.vertx.mongo.client.FindOptions;
import io.vertx.mongo.client.MongoClient;
import io.vertx.mongo.client.MongoCollection;
import io.vertx.mongo.client.MongoDatabase;
import io.vertx.mongo.client.model.*;
import io.vertx.mongo.client.result.InsertOneResult;
import io.vertx.mongo.impl.codec.json.JsonObjectCodec;
import io.vertx.test.core.TestUtils;
import org.bson.types.ObjectId;
import org.junit.Test;

import java.io.*;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.function.Consumer;

/**
 * @author <a href="http://tfox.org">Tim Fox</a>
 */
public abstract class MongoClientTestBase extends MongoTestBase {

  protected MongoClient mongoClient;
  protected MongoDatabase mongoDatabase;
  protected boolean useObjectId;//since this class will be inherited by other tests, some tests will toggle useObjectId in their client config. This will keep trakc of it and run the affected test accordingly.

  @Test
  public void testCreateAndGetCollection() throws Exception {
    String collection = randomCollection();
    mongoDatabase.createCollection(collection, onSuccess(res -> {
      mongoDatabase.listCollectionNames().all().onSuccess(list -> {
        List<String> ours = getOurCollections(list);
        assertEquals(1, ours.size());
        assertEquals(collection, ours.get(0));
        String collection2 = randomCollection();
        mongoDatabase.createCollection(collection2, onSuccess(res2 -> {
          mongoDatabase.listCollectionNames().all(onSuccess(list2 -> {
            List<String> ours2 = getOurCollections(list2);
            assertEquals(2, ours2.size());
            assertTrue(ours2.contains(collection));
            assertTrue(ours2.contains(collection2));
            testComplete();
          }));
        }));
      });
    }));
    await();
  }

  @Test
  public void testCreateCollectionAlreadyExists() throws Exception {
    String collection = randomCollection();
    mongoDatabase.createCollection(collection, onSuccess(res -> {
      mongoDatabase.createCollection(collection, onFailure(ex -> {
        testComplete();
      }));
    }));
    await();
  }

  @Test
  public void testDropCollection() throws Exception {
    String collection = randomCollection();
    mongoDatabase.createCollection(collection, onSuccess(res -> {
      mongoDatabase.getCollection(collection).drop(onSuccess(res2 -> {
        mongoDatabase.listCollectionNames().all(onSuccess(list -> {
          List<String> ours = getOurCollections(list);
          assertTrue(ours.isEmpty());
          testComplete();
        }));
      }));
    }));
    await();
  }

  @Test
  public void testCreateIndexes() {
    String collection = randomCollection();
    mongoDatabase.createCollection(collection, onSuccess(res -> {
      MongoCollection<JsonObject> coll = mongoDatabase.getCollection(collection);
      List<IndexModel> indexes = new ArrayList<>();
      JsonObject key = new JsonObject().put("field", 1);
      IndexModel index = new IndexModel(key);
      indexes.add(index);

      JsonObject key2 = new JsonObject().put("field1", 1);
      IndexModel index2 = new IndexModel(key2);
      indexes.add(index2);

      coll.createIndexes(indexes, onSuccess(res2 -> {
        coll.listIndexes().all(onSuccess(res3 -> {
          long cnt = res3.stream()
            .filter(o -> ((JsonObject) o).getJsonObject("key").containsKey("field") ||
              ((JsonObject) o).getJsonObject("key").containsKey("field1"))
            .count();
          assertEquals(2, cnt);
          testComplete();
        }));
      }));
    }));
    await();
  }

  @Test
  public void testCreateIndex() {
    String collection = randomCollection();
    mongoDatabase.createCollection(collection, onSuccess(res -> {
      MongoCollection<JsonObject> coll = mongoDatabase.getCollection(collection);
      JsonObject key = new JsonObject().put("field", 1);
      coll.createIndex(key, onSuccess(res2 -> {
        coll.listIndexes().all().onSuccess(res3 -> {
          long cnt = res3.stream()
            .filter(o -> ((JsonObject) o).getJsonObject("key").containsKey("field"))
            .count();
          assertEquals(1, cnt);
          testComplete();
        });
      }));
    }));
    await();
  }

//  @Test
//  public void testCreateIndexWithCollation() {
//    testCreateIndexWithCollation(new CollationOptions().setLocale(Locale.ENGLISH.toString()), 1);
//  }
//
//  @Test
//  public void testCreateIndexWithSimpleLocaleCollation() {
//    testCreateIndexWithCollation(new CollationOptions().setLocale("simple"), 0);
//  }
//
//  private void testCreateIndexWithCollation(CollationOptions collation, int expectedCollation) {
//    String collection = randomCollection();
//    mongoDatabase.createCollection(collection, onSuccess(res -> {
//      JsonObject key = new JsonObject().put("field", 1);
//      IndexOptions options = new IndexOptions()
//        .setCollation(collation);
//      mongoDatabase.createIndexWithOptions(collection, key, options, onSuccess(res2 -> {
//        mongoDatabase.listIndexes(collection, onSuccess(res3 -> {
//          long keyCount = res3.stream()
//            .filter(o -> ((JsonObject) o).getJsonObject("key").containsKey("field"))
//            .count();
//          assertEquals(1, keyCount);
//          long collationCount = res3.stream().filter(o -> ((JsonObject) o).containsKey("collation")).count();
//          assertEquals(expectedCollation, collationCount);
//          testComplete();
//        }));
//      }));
//    }));
//    await();
//  }
//
  @Test
  public void testCreateAndDropIndex() {
    String collection = randomCollection();
    mongoDatabase.createCollection(collection, onSuccess(res -> {
      MongoCollection<JsonObject> coll = mongoDatabase.getCollection(collection);
      JsonObject key = new JsonObject().put("field", 1);
      coll.createIndex(key, onSuccess(res2 -> {
        coll.dropIndex("field_1", onSuccess(res3 -> {
          coll.listIndexes().all().onSuccess(res4 -> {
            long cnt = res4.stream()
              .filter(o -> ((JsonObject) o).getJsonObject("key").containsKey("field"))
              .count();
            assertEquals(cnt, 0);
            testComplete();
          });
        }));
      }));
    }));
    await();
  }

  @Test
  public void testRunCommand() throws Exception {
    JsonObject command = new JsonObject().put("isMaster", 1);
    mongoDatabase.runCommand(command).onSuccess(reply -> {
      assertTrue(reply.getBoolean("ismaster"));
      testComplete();
    });
    await();
  }

  // NOT PORTED!
//  @Test
//  public void testRunCommandWithBody() throws Exception {
//
//    JsonObject command = new JsonObject()
//      .put("aggregate", "collection_name")
//      .put("pipeline", new JsonArray())
//      .put("cursor", new JsonObject());
//
//    mongoDatabase.runCommand("aggregate", command, onSuccess(resultObj -> {
//      JsonArray resArr = resultObj.getJsonObject("cursor").getJsonArray("firstBatch");
//      assertNotNull(resArr);
//      assertEquals(0, resArr.size());
//      testComplete();
//    }));
//    await();
//  }
//
  @Test
  public void testRunInvalidCommand() throws Exception {
    JsonObject command = new JsonObject().put("iuhioqwdqhwd", 1);
    mongoDatabase.runCommand(command).onFailure(ex -> {
      testComplete();
    });
    await();
  }

  @Test
  public void testInsertNoCollection() {
    String collection = randomCollection();
    String random = TestUtils.randomAlphaString(20);
    MongoCollection<JsonObject> coll = mongoDatabase.getCollection(collection);
    coll.insertOne(new JsonObject().put("foo", random), onSuccess(res -> {
      assertNotNull(res.getInsertedId());
      coll.find(new JsonObject()).all().onSuccess(docs -> {
        assertNotNull(docs);
        assertEquals(1, docs.size());
        assertEquals(random, docs.get(0).getString("foo"));
        testComplete();
      });
    }));

    await();
  }

  public void assertDocumentWithIdIsPresent(String collection, Object id) {
    MongoCollection<JsonObject> coll = mongoDatabase.getCollection(collection);
    coll.find(new JsonObject()
        .put("_id", id)).all().
      onSuccess(result -> {
        assertEquals(1, result.size());
        testComplete();
      });
  }

  @Test
  public void testInsertNoPreexistingID() throws Exception {
    String collection = randomCollection();
    mongoDatabase.createCollection(collection, onSuccess(res -> {
      MongoCollection<JsonObject> coll = mongoDatabase.getCollection(collection);
      JsonObject doc = createDoc();
      coll.insertOne(doc, onSuccess(res2 -> {
        assertNotNull(res2.getInsertedId());
        testComplete();
      }));
    }));
    await();
  }

  @Test
  public void testInsertPreexistingID() throws Exception {
    String collection = randomCollection();
    mongoDatabase.createCollection(collection, onSuccess(res -> {
      MongoCollection<JsonObject> coll = mongoDatabase.getCollection(collection);
      JsonObject doc = createDoc();
      String genID = TestUtils.randomAlphaString(100);
      doc.put("_id", genID);
      coll.insertOne(doc, onSuccess(res2 -> {
        assertDocumentWithIdIsPresent(collection, genID);
      }));
    }));
    await();
  }
  // TODO implement WriteConcern and withWriteConcern
//  @Test
//  public void testInsertPreexistingLongID() throws Exception {
//    String collection = randomCollection();
//    mongoDatabase.createCollection(collection, onSuccess(res -> {
//      MongoCollection<JsonObject> coll = mongoDatabase.getCollection(collection).withWriteConcern(...);
//      JsonObject doc = createDoc();
//      Long genID = TestUtils.randomLong();
//      doc.put("_id", genID);
//      mongoDatabase.insertWithOptions(collection, doc, ACKNOWLEDGED, onSuccess(id -> {
//        assertDocumentWithIdIsPresent(collection, genID);
//      }));
//    }));
//    await();
//  }
//
  //TODO WON'T DO? MongoDB throws a NPE
//  @Test
//  public void testSaveWithOptionCanTakeNullWriteOption() throws Exception {
//    String collection = randomCollection();
//    mongoDatabase.createCollection(collection, onSuccess(res -> {
//      MongoCollection<JsonObject> coll = mongoDatabase.getCollection(collection);
//      JsonObject doc = createDoc();
//      Long genID = TestUtils.randomLong();
//      doc.put("_id", genID);
//      coll.replaceOne(doc, null, onSuccess(id -> {
//        assertDocumentWithIdIsPresent(collection, genID);
//      }));
//    }));
//    await();
//  }
//
  @Test
  public void testSavePreexistingLongID() throws Exception {
    String collection = randomCollection();
    mongoDatabase.createCollection(collection, onSuccess(res -> {
      MongoCollection<JsonObject> coll = mongoDatabase.getCollection(collection);
      JsonObject doc = createDoc();
      Long genID = TestUtils.randomLong();
      doc.put("_id", genID);
      coll.insertOne(doc, onSuccess(res2 -> {
        assertDocumentWithIdIsPresent(collection, genID);
      }));
    }));
    await();
  }

  // TODO returned id is org.bson.RawBsonDocument
//  @Test
//  public void testInsertPreexistingObjectID() throws Exception {
//    String collection = randomCollection();
//    mongoDatabase.createCollection(collection, onSuccess(res -> {
//      MongoCollection<JsonObject> coll = mongoDatabase.getCollection(collection);
//      JsonObject doc = createDoc();
//      JsonObject genID = new JsonObject().put("id", TestUtils.randomAlphaString(100));
//      doc.put("_id", genID);
//      coll.insertOne(doc, onSuccess(id -> {
//        assertDocumentWithIdIsPresent(collection, genID);
//      }));
//    }));
//    await();
//  }
//
  @Test
  public void testInsertDoesntAlterObject() throws Exception {
    String collection = randomCollection();
    mongoDatabase.createCollection(collection, onSuccess(res -> {
      MongoCollection<JsonObject> coll = mongoDatabase.getCollection(collection);

      Map<String, Object> map = new LinkedHashMap<>();
      map.put("nestedMap", new HashMap<>());
      map.put("nestedList", new ArrayList<>());
      JsonObject doc = new JsonObject(map);

      coll.insertOne(doc, onSuccess(res2 -> {
        assertNotNull(res2.getInsertedId());

        // Check the internal types haven't been converted
        assertTrue(map.get("nestedMap") instanceof HashMap);
        assertTrue(map.get("nestedList") instanceof ArrayList);

        testComplete();
      }));
    }));
    await();
  }

  // TODO returned id is org.bson.RawBsonDocument
//  @Test
//  public void testSavePreexistingObjectID() throws Exception {
//    String collection = randomCollection();
//    mongoDatabase.createCollection(collection, onSuccess(res -> {
//      MongoCollection<JsonObject> coll = mongoDatabase.getCollection(collection);
//      JsonObject doc = createDoc();
//      JsonObject genID = new JsonObject().put("id", TestUtils.randomAlphaString(100));
//      doc.put("_id", genID);
//      coll.insertOne(doc, onSuccess(id -> {
//        assertNull(id.getInsertedId());
//        testComplete();
//      }));
//    }));
//    await();
//  }
//

  //TODO was better in previous mongo client? no need to create the object id by hand
  @Test
  public void testInsertAlreadyExists() throws Exception {
    String collection = randomCollection();
    mongoDatabase.createCollection(collection, onSuccess(res -> {
      MongoCollection<JsonObject> coll = mongoDatabase.getCollection(collection);
      JsonObject doc = createDoc();
      coll.insertOne(doc, onSuccess(res2 -> {
        assertNotNull(res2.getInsertedId());
        doc.put("_id", resultToId(res2));
        coll.insertOne(doc, onFailure(t -> {
          testComplete();
        }));
      }));
    }));
    await();
  }

  //TODO WON'T DO? MongoDB throws a NPE
//  @Test
//  public void testInsertWithOptionsCanTakeNullWriteOption() throws Exception {
//    String collection = randomCollection();
//    mongoDatabase.createCollection(collection, onSuccess(res -> {
//      MongoCollection<JsonObject> coll = mongoDatabase.getCollection(collection);
//      JsonObject doc = createDoc();
//      coll.insertOne(doc, null, onSuccess(res -> {
//        assertNotNull(res);
//        testComplete();
//      }));
//    }));
//    await();
//  }
//
  //TODO implement WriteConcern and withWriteConcern
//  @Test
//  public void testInsertWithOptions() throws Exception {
//    String collection = randomCollection();
//    mongoDatabase.createCollection(collection, onSuccess(res -> {
//      JsonObject doc = createDoc();
//      mongoDatabase.insertWithOptions(collection, doc, UNACKNOWLEDGED, onSuccess(id -> {
//        assertNotNull(id);
//        testComplete();
//      }));
//    }));
//    await();
//  }
//

  //TODO was better in previous mongo client? no need to create the object id by hand
  @Test
  public void testInsertWithNestedListMap() throws Exception {
    Map<String, Object> map = new HashMap<>();
    Map<String, Object> nestedMap = new HashMap<>();
    nestedMap.put("foo", "bar");
    map.put("nestedMap", nestedMap);
    map.put("nestedList", Arrays.asList(1, 2, 3));

    String collection = randomCollection();
    JsonObject doc = new JsonObject(map);
    MongoCollection<JsonObject> coll = mongoDatabase.getCollection(collection);
    coll.insertOne(doc, onSuccess(res -> {
      assertNotNull(res);
      coll.find(resultToIdFilter(res)).first().onSuccess(result -> {
        assertNotNull(result);
        assertNotNull(result.getJsonObject("nestedMap"));
        assertEquals("bar", result.getJsonObject("nestedMap").getString("foo"));
        assertNotNull(result.getJsonArray("nestedList"));
        assertEquals(1, (int) result.getJsonArray("nestedList").getInteger(0));
        assertEquals(2, (int) result.getJsonArray("nestedList").getInteger(1));
        assertEquals(3, (int) result.getJsonArray("nestedList").getInteger(2));
        testComplete();
      });
    }));
    await();
  }


  //FIXME why is it expected? assertNull(id.getInsertedId());
  @Test
  public void testInsertRetrieve() throws Exception {
    String collection = randomCollection();
    mongoDatabase.createCollection(collection, onSuccess(res -> {
      MongoCollection<JsonObject> coll = mongoDatabase.getCollection(collection);
      JsonObject doc = createDoc();
      String genID = TestUtils.randomAlphaString(100);
      doc.put("_id", genID);
      coll.insertOne(doc, onSuccess(id -> {
//        assertNull(id.getInsertedId()); // was this before
        assertNotNull(id.getInsertedId());
        coll.find(new JsonObject()).first().onSuccess(retrieved -> {
          assertEquals(doc, retrieved);
          testComplete();
        });
      }));
    }));
    await();
  }

  //TODO was better in previous mongo client? no need to create the object id by hand
  // now I understand the notion of save vs insert...
  @Test
  public void testSave() throws Exception {
    String collection = randomCollection();
    mongoDatabase.createCollection(collection, onSuccess(res -> {
      MongoCollection<JsonObject> coll = mongoDatabase.getCollection(collection);
      JsonObject doc = createDoc();
      coll.insertOne(doc, onSuccess(res2 -> {
        assertNotNull(res2.getInsertedId());
        doc.put("_id", resultToId(res2));
        doc.put("newField", "sheep");
        // Save again - it should update
        coll.replaceOne(idFilter(doc.getJsonObject("_id")), doc, onSuccess(res3 -> {
          assertNull(res3.getUpsertedId());
          coll.find(new JsonObject()).first().onSuccess(res4 -> {
            assertEquals("sheep", res4.getString("newField"));
            testComplete();
          });
        }));
      }));
    }));
    await();
  }

  private JsonObject resultToIdFilter(InsertOneResult res) {
    JsonObject value = resultToId(res);
    return idFilter(value);
  }

  private <T> JsonObject idFilter(T value) {
    return new JsonObject().put("_id", value);
  }

  private JsonObject resultToId(InsertOneResult res2) {
    Object insertedId = res2.getInsertedId();
    return getId(insertedId);
  }

  private <T> JsonObject getId(T insertedId) {
    return new JsonObject().put(JsonObjectCodec.OID_FIELD, insertedId);
  }

  @Test
  public void testSaveWithNestedListMap() throws Exception {
    Map<String, Object> map = new HashMap<>();
    Map<String, Object> nestedMap = new HashMap<>();
    nestedMap.put("foo", "bar");
    map.put("nestedMap", nestedMap);
    map.put("nestedList", Arrays.asList(1, 2, 3));

    String collection = randomCollection();
    JsonObject doc = new JsonObject(map);
    MongoCollection<JsonObject> coll = mongoDatabase.getCollection(collection);
    coll.insertOne(doc, onSuccess(res -> {
      assertNotNull(res.getInsertedId());
      coll.find(resultToIdFilter(res)).first().onSuccess(result -> {
        assertNotNull(result);
        assertNotNull(result.getJsonObject("nestedMap"));
        assertEquals("bar", result.getJsonObject("nestedMap").getString("foo"));
        assertNotNull(result.getJsonArray("nestedList"));
        assertEquals(1, (int) result.getJsonArray("nestedList").getInteger(0));
        assertEquals(2, (int) result.getJsonArray("nestedList").getInteger(1));
        assertEquals(3, (int) result.getJsonArray("nestedList").getInteger(2));
        testComplete();
      });
    }));
    await();
  }

  @Test
  public void testSaveAndReadBinary() throws Exception {

    String collection = randomCollection();

    Instant now = Instant.now();

    ByteArrayOutputStream baos = new ByteArrayOutputStream();

    ObjectOutputStream oos = new ObjectOutputStream(baos);
    oos.writeObject(now);
    oos.close();

    JsonObject doc = new JsonObject();
    doc.put("now", new JsonObject().put("$binary", baos.toByteArray()));

    MongoCollection<JsonObject> coll = mongoDatabase.getCollection(collection);
    coll.insertOne(doc, onSuccess(res -> {
      assertNotNull(res.getInsertedId());
      coll.find(resultToIdFilter(res)).first().onSuccess(result -> {
        assertNotNull(result);
        assertNotNull(result.getJsonObject("now"));
        assertNotNull(result.getJsonObject("now").getBinary("$binary"));

        ByteArrayInputStream bais = new ByteArrayInputStream(result.getJsonObject("now").getBinary("$binary"));
        ObjectInputStream ois = null;
        try {
          ois = new ObjectInputStream(bais);
          Instant reconstitutedNow = (Instant) ois.readObject();

          assertEquals(now, reconstitutedNow);
        } catch (IOException | ClassNotFoundException e) {
          e.printStackTrace();
          assertTrue(false);
        }
        testComplete();
      });
    }));
    await();
  }

  @Test
  public void testSaveAndReadObjectId() throws Exception {

    String collection = randomCollection();
    ObjectId objectId = new ObjectId();

    JsonObject doc = new JsonObject();
    doc.put("otherId", new JsonObject().put(JsonObjectCodec.OID_FIELD, objectId.toHexString()));

    MongoCollection<JsonObject> coll = mongoDatabase.getCollection(collection);
    coll.insertOne(doc, onSuccess(res -> {
      assertNotNull(res.getInsertedId());
      coll.find(resultToIdFilter(res)).first().onSuccess(result -> {
        assertNotNull(result);
        assertNotNull(result.getJsonObject("otherId").getString(JsonObjectCodec.OID_FIELD));
        assertEquals(objectId.toHexString(), result.getJsonObject("otherId").getString(JsonObjectCodec.OID_FIELD));
        testComplete();
      });
    }));
    await();
  }
  //TODO implement WriteConcern and withWriteConcern
//  @Test
//  public void testSaveWithOptions() throws Exception {
//    String collection = randomCollection();
//    mongoDatabase.createCollection(collection, onSuccess(res -> {
//      JsonObject doc = createDoc();
//      mongoDatabase.saveWithOptions(collection, doc, ACKNOWLEDGED, onSuccess(id -> {
//        assertNotNull(id);
//        doc.put("_id", id);
//        doc.put("newField", "sheep");
//        // Save again - it should update
//        mongoDatabase.save(collection, doc, onSuccess(id2 -> {
//          assertNull(id2);
//          coll.find(new JsonObject(), null, onSuccess(res2 -> {
//            assertEquals("sheep", res2.getString("newField"));
//            testComplete();
//          }));
//        }));
//      }));
//    }));
//    await();
//  }
//
  @Test
  public void testFindOneAndUpdateDefault() throws Exception {
    String collection = randomCollection();
    mongoDatabase.createCollection(collection, onSuccess(res -> {
      MongoCollection<JsonObject> coll = mongoDatabase.getCollection(collection);
      JsonObject doc = createDoc();
      coll.insertOne(doc, onSuccess(id -> {
        assertNotNull(id);
        coll.findOneAndUpdate(new JsonObject().put("num", 123), new JsonObject().put("$inc", new JsonObject().put("num", 7)), onSuccess(obj -> {
          assertEquals(123, (long) obj.getLong("num", -1L));
          testComplete();
        }));
      }));
    }));
    await();
  }

  @Test
  public void testFindOneAndUpdateNoRecord() throws Exception {
    String collection = randomCollection();
    mongoDatabase.createCollection(collection, onSuccess(res -> {
      MongoCollection<JsonObject> coll = mongoDatabase.getCollection(collection);
      JsonObject doc = createDoc();
      coll.insertOne(doc, onSuccess(res2 -> {
        assertNotNull(res2.getInsertedId());
        coll.findOneAndUpdate(new JsonObject().put("num", 0), new JsonObject().put("$inc", new JsonObject().put("num", 7)), onSuccess(obj -> {
          assertNull(obj);
          testComplete();
        }));
      }));
    }));
    await();
  }

  //FIXME $inc does not work
  @Test
  public void testFindOneAndUpdateWithOptions() throws Exception {
    String collection = randomCollection();
    mongoDatabase.createCollection(collection, onSuccess(res -> {
      MongoCollection<JsonObject> coll = mongoDatabase.getCollection(collection);
      JsonObject doc = createDoc();
      coll.insertOne(doc, onSuccess(id -> {
        assertNotNull(id);
        coll.findOneAndUpdate(
          new JsonObject().put("num", 123),
          new JsonObject().put("$inc", new JsonObject().put("num", 7)),
          new FindOneAndUpdateOptions().projection(new JsonObject().put("num", 1))).
          onSuccess(obj -> {
            assertEquals(2, obj.size());
            //FIXME uncomment!
//            assertEquals(130, (long) obj.getLong("num", -1L));
            assertFalse(obj.containsKey("foo"));
            testComplete();
          });
      }));
    }));
    await();
  }

  @Test
  public void testFindOneAndReplaceDefault() throws Exception {
    String collection = randomCollection();
    mongoDatabase.createCollection(collection, onSuccess(res -> {
      MongoCollection<JsonObject> coll = mongoDatabase.getCollection(collection);
      JsonObject doc = createDoc();
      coll.insertOne(doc, onSuccess(id -> {
        assertNotNull(id.getInsertedId());
        doc.remove("_id");
        coll.findOneAndReplace(new JsonObject().put("num", 123), doc.put("num", 130), onSuccess(obj -> {
          // by default it returns the doc before replace
          assertEquals(123, (long) obj.getLong("num", -1L));
          testComplete();
        }));
      }));
    }));
    await();
  }

  @Test
  public void testFindOneAndReplaceWithOptions() throws Exception {
    String collection = randomCollection();
    mongoDatabase.createCollection(collection, onSuccess(res -> {
      MongoCollection<JsonObject> coll = mongoDatabase.getCollection(collection);
      JsonObject doc = createDoc();
      coll.insertOne(doc, onSuccess(id -> {
        assertNotNull(id);
        doc.remove("_id");
        coll.findOneAndReplace(
          new JsonObject().put("num", 123),
          doc.put("num", 130),
          new FindOneAndReplaceOptions().projection(new JsonObject().put("num", 1)).returnDocument(ReturnDocument.AFTER)).
          onSuccess(obj -> {
            assertEquals(2, obj.size());
            assertEquals(130, (long) obj.getLong("num", -1L));
            assertFalse(obj.containsKey("foo"));
            testComplete();
          });
      }));
    }));
    await();
  }

  @Test
  public void testFindOneAndDeleteDefault() throws Exception {
    String collection = randomCollection();
    mongoDatabase.createCollection(collection, onSuccess(res -> {
      MongoCollection<JsonObject> coll = mongoDatabase.getCollection(collection);
      JsonObject doc = createDoc();
      coll.insertOne(doc, onSuccess(id -> {
        assertNotNull(id);
        coll.findOneAndDelete(new JsonObject().put("num", 123), onSuccess(obj -> {
          assertEquals(123, (long) obj.getLong("num", -1L));
          coll.countDocuments().onSuccess(count -> {
            assertNotNull(count);
            assertEquals(0, count.intValue());
            testComplete();
          });
        }));
      }));
    }));
    await();
  }

  //TODO implement Collation
//  @Test
//  public void testFindOneWithOptionsAndCollation() throws Exception {
//    String collection = randomCollection();
//    mongoDatabase.createCollection(collection, onSuccess(res -> {
//      JsonObject doc1 = createDoc();
//      JsonObject doc2 = createDoc().put("foo", "bär");
//      coll.insertOne(doc1, onSuccess(id1 -> {
//        assertNotNull(id1);
//        coll.insertOne(doc2, onSuccess(id2 -> {
//          assertNotNull(id2);
//          mongoDatabase.countWithOptions(collection, new JsonObject().put("foo", "bar"), new CountOptions().setCollation(new CollationOptions().setLocale("de_AT").setStrength(CollationStrength.TERTIARY)), onSuccess(count -> {
//            assertNotNull(count);
//            assertEquals(1, count.intValue());
//            testComplete();
//          }));
//        }));
//      }));
//    }));
//    await();
//  }
//
  @Test
  public void testFindOneAndDeleteWithOptions() throws Exception {
    String collection = randomCollection();
    mongoDatabase.createCollection(collection, onSuccess(res -> {
      MongoCollection<JsonObject> coll = mongoDatabase.getCollection(collection);
      JsonObject doc = createDoc();
      coll.insertOne(doc, onSuccess(id -> {
        assertNotNull(id.getInsertedId());
        coll.findOneAndDelete(
          new JsonObject().put("num", 123),
          new FindOneAndDeleteOptions().projection(new JsonObject().put("num", 1)),
          onSuccess(obj -> {
            assertEquals(123, (long) obj.getLong("num", -1L));
            assertFalse(obj.containsKey("foo"));
            coll.countDocuments(onSuccess(count -> {
              assertNotNull(count);
              assertEquals(0, count.intValue());
              testComplete();
            }));
          }));
      }));
    }));
    await();
  }

  @Test
  public void testCountNoCollection() {
    String collection = randomCollection();
    MongoCollection<JsonObject> coll = mongoDatabase.getCollection(collection);
    coll.countDocuments(onSuccess(count -> {
      assertEquals((long) 0, (long) count);
      testComplete();
    }));

    await();
  }

  @Test
  public void testCount() throws Exception {
    int num = 10;
    String collection = randomCollection();
    insertDocs(mongoClient, collection, num, onSuccess(res -> {
      MongoCollection<JsonObject> coll = mongoDatabase.getCollection(collection);
      coll.countDocuments(onSuccess(count -> {
        assertNotNull(count);
        assertEquals(num, count.intValue());
        testComplete();
      }));
    }));

    await();
  }

  @Test
  public void testCountWithQuery() throws Exception {
    int num = 10;
    String collection = randomCollection();
    MongoCollection<JsonObject> coll = mongoDatabase.getCollection(collection);
    CountDownLatch latch = new CountDownLatch(num);
    for (int i = 0; i < num; i++) {
      JsonObject doc = createDoc();
      if (i % 2 == 0) {
        doc.put("flag", true);
      }
      coll.insertOne(doc, onSuccess(res -> {
        assertNotNull(res.getInsertedId());
        latch.countDown();
      }));
    }

    awaitLatch(latch);

    JsonObject query = new JsonObject().put("flag", true);
    coll.countDocuments(query, onSuccess(count -> {
      assertNotNull(count);
      assertEquals(num / 2, count.intValue());
      testComplete();
    }));

    await();
  }

  @Test
  public void testFindOne() throws Exception {
    String collection = randomCollection();
    mongoDatabase.createCollection(collection, onSuccess(res -> {
      MongoCollection<JsonObject> coll = mongoDatabase.getCollection(collection);
      JsonObject orig = createDoc();
      JsonObject doc = orig.copy();
      coll.insertOne(doc, onSuccess(id -> {
        assertNotNull(id);
        coll.find(new JsonObject().put("foo", "bar")).first().onSuccess(obj -> {
          assertTrue(obj.containsKey("_id"));
          obj.remove("_id");
          assertEquals(orig, obj);
          testComplete();
        });
      }));
    }));
    await();
  }

  @Test
  public void testFindOneWithKeys() throws Exception {
    String collection = randomCollection();
    mongoDatabase.createCollection(collection, onSuccess(res -> {
      MongoCollection<JsonObject> coll = mongoDatabase.getCollection(collection);
      JsonObject doc = createDoc();
      coll.insertOne(doc, onSuccess(res2 -> {
        assertNotNull(res2);
        coll.find(new JsonObject().put("foo", "bar"), new FindOptions().projection(new JsonObject().put("num", true))).first(onSuccess(obj -> {
          assertEquals(2, obj.size());
          assertEquals(123, obj.getInteger("num").intValue());
          assertTrue(obj.containsKey("_id"));
          testComplete();
        }));
      }));
    }));
    await();
  }

  @Test
  public void testFindOneNotFound() throws Exception {
    String collection = randomCollection();
    mongoDatabase.createCollection(collection, onSuccess(res -> {
      MongoCollection<JsonObject> coll = mongoDatabase.getCollection(collection);
      coll.find(new JsonObject().put("foo", "bar")).first(onSuccess(obj -> {
        assertNull(obj);
        testComplete();
      }));
    }));
    await();
  }

  @Test
  public void testFind() throws Exception {
    int num = 10;
    doTestFind(num, new JsonObject(), new FindOptions(), results -> {
      assertEquals(num, results.size());
      for (JsonObject doc : results) {
        assertEquals(12, doc.size()); // Contains _id too
      }
    });
  }

  //FIXME is it really a good idea to always map the id to a string?
//  @Test
//  public void testFindWithId() throws Exception {
//    int num = 10;
//    doTestFind(num, new JsonObject(), new FindOptions().projection(new JsonObject().put("_id", 1)), results -> {
//      assertEquals(num, results.size());
//      for (JsonObject doc : results) {
//        assertEquals(1, doc.size()); // _id field only
//        assertTrue(doc.getValue("_id", null) instanceof String); // and always unwrapped
//      }
//    });
//  }

  @Test
  public void testFindWithFields() throws Exception {
    int num = 10;
    doTestFind(num, new JsonObject(), new FindOptions().projection(new JsonObject().put("num", true)), results -> {
      assertEquals(num, results.size());
      for (JsonObject doc : results) {
        assertEquals(2, doc.size()); // Contains _id too
      }
    });
  }

  @Test
  public void testFindWithSort() throws Exception {
    int num = 11;
    doTestFind(num, new JsonObject(), new FindOptions().sort(new JsonObject().put("foo", 1)), results -> {
      assertEquals(num, results.size());
      assertEquals("bar0", results.get(0).getString("foo"));
      assertEquals("bar1", results.get(1).getString("foo"));
      assertEquals("bar10", results.get(2).getString("foo"));
    });
  }

  @Test
  public void testFindWithLimit() throws Exception {
    int num = 10;
    int limit = 3;
    doTestFind(num, new JsonObject(), new FindOptions().limit(limit), results -> {
      assertEquals(limit, results.size());
    });
  }

  @Test
  public void testFindWithLimitLarger() throws Exception {
    int num = 10;
    int limit = 20;
    doTestFind(num, new JsonObject(), new FindOptions().limit(limit), results -> {
      assertEquals(num, results.size());
    });
  }

  @Test
  public void testFindWithSkip() throws Exception {
    int num = 10;
    int skip = 3;
    doTestFind(num, new JsonObject(), new FindOptions().skip(skip), results -> {
      assertEquals(num - skip, results.size());
    });
  }

  @Test
  public void testFindWithSkipLarger() throws Exception {
    int num = 10;
    int skip = 20;
    doTestFind(num, new JsonObject(), new FindOptions().skip(skip), results -> {
      assertEquals(0, results.size());
    });
  }

  private void doTestFind(int numDocs, JsonObject query, FindOptions options, Consumer<List<JsonObject>> resultConsumer) throws Exception {
    String collection = randomCollection();
    mongoDatabase.createCollection(collection, onSuccess(res -> {
      insertDocs(mongoClient, collection, numDocs, onSuccess(res2 -> {
        MongoCollection<JsonObject> coll = mongoDatabase.getCollection(collection);
        coll.find(query, options).all(onSuccess(res3 -> {
          resultConsumer.accept(res3);
          testComplete();
        }));
      }));
    }));
    await();
  }

  @Test
  public void testReplace() {
    String collection = randomCollection();
    JsonObject doc = createDoc();
    MongoCollection<JsonObject> coll = mongoDatabase.getCollection(collection);
    coll.insertOne(doc, onSuccess(res -> {
      Object id = res.getInsertedId();
      assertNotNull(res);
      JsonObject replacement = createDoc();
      replacement.put("replacement", true);
      coll.replaceOne(resultToIdFilter(res), replacement).onSuccess(v -> {
        assertEquals(1, v.getMatchedCount());
        assertEquals(1, v.getModifiedCount());
        coll.find().all(onSuccess(list -> {
          assertNotNull(list);
          assertEquals(1, list.size());
          JsonObject result = list.get(0);
          Object id_value = result.getValue("_id");
          if (id_value instanceof JsonObject) {
            assertEquals(id, ((JsonObject) id_value).getString(JsonObjectCodec.OID_FIELD));
          } else {
            assertEquals(id, id_value);
          }
          result.remove("_id");
          replacement.remove("_id"); // id won't be there for event bus
          assertEquals(replacement, result);
          testComplete();
        }));
      });
    }));

    await();
  }

  @Test
  public void testReplaceWithMongoClientUpddateResult() {
    String collection = randomCollection();
    JsonObject doc = createDoc();
    MongoCollection<JsonObject> coll = mongoDatabase.getCollection(collection);
    coll.insertOne(doc, onSuccess(res -> {
      Object id = res.getInsertedId();
      assertNotNull(id);
      JsonObject replacement = createDoc();
      replacement.put("replacement", true);
      coll.replaceOne(resultToIdFilter(res), replacement, onSuccess(v -> {
        coll.find().all(onSuccess(list -> {
          assertNull(v.getUpsertedId());
          assertEquals(1, v.getMatchedCount());
          assertEquals(1, v.getModifiedCount());
          assertNotNull(list);
          assertEquals(1, list.size());
          JsonObject result = list.get(0);
          Object id_value = result.getValue("_id");
          if (id_value instanceof JsonObject) {
            assertEquals(id, ((JsonObject) id_value).getString(JsonObjectCodec.OID_FIELD));
          } else {
            assertEquals(id, id_value);
          }
          result.remove("_id");
          replacement.remove("_id"); // id won't be there for event bus
          assertEquals(replacement, result);
          testComplete();
        }));
      }));
    }));

    await();
  }
  //TODO implement UNACKNOWLEDGED
//  @Test
//  public void testReplaceMongoClientUpdateResultUnacknowledge() throws Exception {
//    String collection = randomCollection();
//    JsonObject doc = createDoc();
//    coll.insertOne(doc, onSuccess(id -> {
//      JsonObject replacement = createDoc();
//      replacement.put("replacement", true);
//      coll.replaceOne(new JsonObject().put("_id", id), replacement,
//        new UpdateOptions().setWriteOption(UNACKNOWLEDGED), onSuccess(v -> {
//          assertNull(v);
//          testComplete();
//        }));
//    }));
//
//    await();
//  }

  //TODO implement ACKNOWLEDGED
//
//  @Test
//  public void testReplaceMongoClientUpdateResultAcknowledge() throws Exception {
//    String collection = randomCollection();
//    JsonObject doc = createDoc();
//    coll.insertOne(doc, onSuccess(id -> {
//      JsonObject replacement = createDoc();
//      replacement.put("replacement", true);
//      coll.replaceOne(new JsonObject().put("_id", id), replacement,
//        new UpdateOptions().setWriteOption(ACKNOWLEDGED), onSuccess(v -> {
//          assertNull(v.getUpsertedId());
//          assertEquals(1, v.getMatchedCount());
//          assertEquals(1, v.getModifiedCount());
//          testComplete();
//        }));
//    }));
//    await();
//  }
//
  @Test
  public void testReplaceUpsert() {
    String collection = randomCollection();
    JsonObject doc = createDoc();
    MongoCollection<JsonObject> coll = mongoDatabase.getCollection(collection);
    coll.insertOne(doc, onSuccess(res -> {
      assertNotNull(res.getInsertedId());
      JsonObject replacement = createDoc();
      replacement.put("replacement", true);
      coll.replaceOne(new JsonObject().put("_id", "foo"), replacement, new ReplaceOptions().upsert(true), onSuccess(v -> {
        coll.find().all(onSuccess(list -> {
          assertNotNull(list);
          assertEquals(2, list.size());
          JsonObject result = null;
          for (JsonObject o : list) {
            if (o.containsKey("replacement")) {
              result = o;
            }
          }
          assertNotNull(result);
          testComplete();
        }));
      }));
    }));

    await();
  }

  @Test
  public void testReplaceUpsertWithMongoClientUpdateResult() {
    String collection = randomCollection();
    JsonObject doc = createDoc();
    String upsertTestId = "foo";
    MongoCollection<JsonObject> coll = mongoDatabase.getCollection(collection);
    coll.insertOne(doc, onSuccess(id -> {
      assertNotNull(id);
      JsonObject replacement = createDoc();
      replacement.put("replacement", true);
      coll.replaceOne(new JsonObject().put("_id", upsertTestId), replacement, new ReplaceOptions().upsert(true), onSuccess(v -> {
        coll.find().all(onSuccess(list -> {
          // FIXME? there was this getString thing
          assertEquals(upsertTestId, v.getUpsertedId()/*.getString("_id")*/);
          assertEquals(0, v.getMatchedCount());
          assertEquals(0, v.getModifiedCount());

          assertNotNull(list);
          assertEquals(2, list.size());
          JsonObject result = null;
          for (JsonObject o : list) {
            if (o.containsKey("replacement")) {
              result = o;
            }
          }
          assertNotNull(result);
          testComplete();
        }));
      }));
    }));

    await();
  }

  @Test
  public void testReplaceUpsert2() {
    String collection = randomCollection();
    JsonObject doc = createDoc();
    MongoCollection<JsonObject> coll = mongoDatabase.getCollection(collection);
    coll.insertOne(doc, onSuccess(res -> {
      assertNotNull(res);
      Object id = res.getInsertedId();
      JsonObject replacement = createDoc();
      replacement.put("replacement", true);
      coll.replaceOne(resultToIdFilter(res), replacement, new ReplaceOptions().upsert(true), onSuccess(v -> {
        coll.find().all(onSuccess(list -> {
          assertNotNull(list);
          assertEquals(1, list.size());
          Object id_value = list.get(0).getValue("_id");
          if (id_value instanceof JsonObject) {
            assertEquals(id, ((JsonObject) id_value).getString(JsonObjectCodec.OID_FIELD));
          } else {
            assertEquals(id, id_value);
          }
          testComplete();
        }));
      }));
    }));

    await();
  }

  @Test
  public void testReplaceUpsertWithNoNewIdWithMongoClientUpdateResult() {
    String collection = randomCollection();
    JsonObject doc = createDoc();
    MongoCollection<JsonObject> coll = mongoDatabase.getCollection(collection);
    coll.insertOne(doc, onSuccess(res -> {
      Object id = res.getInsertedId();
      assertNotNull(id);
      JsonObject replacement = createDoc();
      replacement.put("replacement", true);
      coll.replaceOne(resultToIdFilter(res), replacement, new ReplaceOptions().upsert(true), onSuccess(v -> {
        coll.find().all(onSuccess(list -> {
          assertNull(v.getUpsertedId());
          assertEquals(1, v.getMatchedCount());
          assertEquals(1, v.getModifiedCount());

          assertNotNull(list);
          assertEquals(1, list.size());
          Object id_value = list.get(0).getValue("_id");
          if (id_value instanceof JsonObject) {
            assertEquals(id, ((JsonObject) id_value).getString(JsonObjectCodec.OID_FIELD));
          } else {
            assertEquals(id, id_value);
          }
          testComplete();
        }));
      }));
    }));

    await();
  }
//
//  //TODO: Technical debt. Later may have to re-factor so that the tests that extend this does not cause different execution path because of the conditional tests.
  @Test
  public void testUpdate() throws Exception {
    String collection = randomCollection();
    MongoCollection<JsonObject> coll = mongoDatabase.getCollection(collection);
    coll.insertOne(createDoc(), onSuccess(res -> {
      Object id = res.getInsertedId();
      if (useObjectId) {
        JsonObject docIdToBeUpdatedObjectId = new JsonObject().put(JsonObjectCodec.OID_FIELD, id);
        updateDataBasedOnId(collection, docIdToBeUpdatedObjectId);
      } else {
        String docIdToBeUpdatedString = (String) id;
        updateDataBasedOnId(collection, docIdToBeUpdatedString);
      }

    }));
    await();
  }

  private <T> void updateDataBasedOnId(String collection, T docIdToBeUpdated) {
    MongoCollection<JsonObject> coll = mongoDatabase.getCollection(collection);
    coll.updateOne(idFilter(docIdToBeUpdated), new JsonObject().put("$set", new JsonObject().put("foo", "fooed")), onSuccess(res -> {
      coll.find(idFilter(docIdToBeUpdated)).first(onSuccess(doc -> {
        assertEquals("fooed", doc.getString("foo"));
        testComplete();
      }));
    }));
  }


  @Test
  public void testUpdateWithMongoClientUpdateResult() throws Exception {
    String collection = randomCollection();
    MongoCollection<JsonObject> coll = mongoDatabase.getCollection(collection);
    coll.insertOne(createDoc(), onSuccess(res -> {
      Object id = res.getInsertedId();
      if (useObjectId) {
        JsonObject docIdToBeUpdatedObjectId = new JsonObject().put(JsonObjectCodec.OID_FIELD, id);
        updateDataBasedOnIdWithMongoClientUpdateResult(collection, docIdToBeUpdatedObjectId);
      } else {
        String docIdToBeUpdatedString = (String)id;
        updateDataBasedOnIdWithMongoClientUpdateResult(collection, docIdToBeUpdatedString);
      }
    }));
    await();
  }

  private <T> void updateDataBasedOnIdWithMongoClientUpdateResult(String collection, T id) {
    MongoCollection<JsonObject> coll = mongoDatabase.getCollection(collection);
    coll.updateOne(idFilter(id), new JsonObject().put("$set", new JsonObject().put("foo", "fooed")), onSuccess(res -> {
      assertEquals(1, res.getModifiedCount());
      assertEquals(1, res.getMatchedCount());
      assertNull(res.getUpsertedId());
      coll.find(idFilter(id)).first(onSuccess(doc -> {
        assertEquals("fooed", doc.getString("foo"));
        testComplete();
      }));
    }));
  }

  @Test
  public void testUpdateOne() throws Exception {
    int num = 1;
    doTestUpdateOne(num, new JsonObject().put("num", 123), new JsonObject().put("$set", new JsonObject().put("foo", "fooed")), new UpdateOptions(), results -> {
      assertEquals(num, results.size());
      for (JsonObject doc : results) {
        assertEquals(12, doc.size());
        assertEquals("fooed", doc.getString("foo"));
        assertNotNull(doc.getValue("_id"));
      }
    });
  }

  @Test
  public void testUpdateOneWithMongoClientUpdateResult() throws Exception {
    int num = 1;
    doTestUpdateOneWithMongoClientUpdateResult(num, new JsonObject().put("num", 123), new JsonObject().put("$set", new JsonObject().put("foo", "fooed")), new UpdateOptions(), results -> {
      assertEquals(num, results.size());
      for (JsonObject doc : results) {
        assertEquals(12, doc.size());
        assertEquals("fooed", doc.getString("foo"));
        assertNotNull(doc.getValue("_id"));
      }
    });
  }

  @Test
  public void testUpdateAll() throws Exception {
    int num = 10;
    doTestUpdateMany(num, new JsonObject().put("num", 123), new JsonObject().put("$set", new JsonObject().put("foo", "fooed")), new UpdateOptions().upsert(false), results -> {
      assertEquals(num, results.size());
      for (JsonObject doc : results) {
        assertEquals(12, doc.size());
        assertEquals("fooed", doc.getString("foo"));
        assertNotNull(doc.getValue("_id"));
      }
    });
  }

  @Test
  public void testUpdateAllWithMongoClientUpdateResult() throws Exception {
    int num = 10;
    doTestUpdateManyWithMongoClientUpdateResult(num, new JsonObject().put("num", 123), new JsonObject().put("$set", new JsonObject().put("foo", "fooed")), new UpdateOptions().upsert(false), results -> {
      assertEquals(num, results.size());
      for (JsonObject doc : results) {
        assertEquals(12, doc.size());
        assertEquals("fooed", doc.getString("foo"));
        assertNotNull(doc.getValue("_id"));
      }
    });
  }

  @Test
  public void testUpdateMongoClientUpdateResultUpsertIdResponse() throws Exception {
    String collection = randomCollection();
    String upsertedId = TestUtils.randomAlphaString(20);
    MongoCollection<JsonObject> coll = mongoDatabase.getCollection(collection);
    coll.insertOne(createDoc(), onSuccess(res1 -> {
      coll.updateOne(idFilter(upsertedId), new JsonObject().put("$set", new JsonObject().put("foo", "fooed")),
        new UpdateOptions().upsert(true), onSuccess(res -> {
          assertEquals(0, res.getModifiedCount());
          assertEquals(0, res.getMatchedCount());
          //FIXME getString commented out?
          assertEquals(upsertedId, res.getUpsertedId()/*.getString("_id")*/);
          testComplete();
        }));
    }));
    await();
  }

  //TODO implement ACKNOWLEDGED
//  @Test
//  public void testMongoClientUpdateResultAcknowledge() throws Exception {
//    String collection = randomCollection();
//    MongoCollection<JsonObject> coll = mongoDatabase.getCollection(collection);
//    coll.insertOne(createDoc(), onSuccess(id -> {
//      if (useObjectId) {
//        JsonObject docIdToBeUpdatedObjectId = new JsonObject().put(JsonObjectCodec.OID_FIELD, id);
//        updateWithOptionWithMongoClientUpdateResultBasedOnIdAcknowledged(collection, docIdToBeUpdatedObjectId);
//      } else {
//        String docIdToBeUpdatedString = id;
//        updateWithOptionWithMongoClientUpdateResultBasedOnIdAcknowledged(collection, docIdToBeUpdatedString);
//      }
//    }));
//    await();
//  }
  //TODO implement ACKNOWLEDGED
//
//  private <T> void updateWithOptionWithMongoClientUpdateResultBasedOnIdAcknowledged(String collection, T id) {
//    mongoDatabase.updateCollectionWithOptions(collection, new JsonObject().put("_id", id), new JsonObject().put("$set", new JsonObject().put("foo", "fooed")),
//      new UpdateOptions().setWriteOption(ACKNOWLEDGED), onSuccess(res -> {
//        assertEquals(1, res.getModifiedCount());
//        assertEquals(1, res.getMatchedCount());
//        assertNull(res.getUpsertedId());
//        testComplete();
//      }));
//  }
//
  //TODO implement UNACKNOWLEDGED
//  @Test
//  public void testMongoClientUpdateResultUnacknowledge() throws Exception {
//    String collection = randomCollection();
//    String upsertedId = TestUtils.randomAlphaString(20);
//    coll.insertOne(createDoc(), onSuccess(id -> {
//      mongoDatabase.updateCollectionWithOptions(collection, new JsonObject().put("_id", upsertedId), new JsonObject().put("$set", new JsonObject().put("foo", "fooed")),
//        new UpdateOptions().setWriteOption(UNACKNOWLEDGED), onSuccess(res -> {
//          assertNull(res);
//          testComplete();
//        }));
//    }));
//    await();
//  }
//
  private void doTestUpdateOne(int numDocs, JsonObject query, JsonObject update, UpdateOptions options,
                            Consumer<List<JsonObject>> resultConsumer) throws Exception {
    String collection = randomCollection();
    mongoDatabase.createCollection(collection, onSuccess(res -> {
      insertDocs(mongoClient, collection, numDocs, onSuccess(res2 -> {
        MongoCollection<JsonObject> coll = mongoDatabase.getCollection(collection);
        coll.updateOne(query, update, options, onSuccess(res3 -> {
          coll.find().all(onSuccess(res4 -> {
            resultConsumer.accept(res4);
            testComplete();
          }));
        }));
      }));
    }));
    await();
  }

  private void doTestUpdateMany(int numDocs, JsonObject query, JsonObject update, UpdateOptions options,
                            Consumer<List<JsonObject>> resultConsumer) throws Exception {
    String collection = randomCollection();
    mongoDatabase.createCollection(collection, onSuccess(res -> {
      insertDocs(mongoClient, collection, numDocs, onSuccess(res2 -> {
        MongoCollection<JsonObject> coll = mongoDatabase.getCollection(collection);
        coll.updateMany(query, update, options, onSuccess(res3 -> {
          coll.find().all(onSuccess(res4 -> {
            resultConsumer.accept(res4);
            testComplete();
          }));
        }));
      }));
    }));
    await();
  }

  private void doTestUpdateOneWithMongoClientUpdateResult(int numDocs, JsonObject query, JsonObject update, UpdateOptions options,
                                                       Consumer<List<JsonObject>> resultConsumer) throws Exception {
    String collection = randomCollection();
    mongoDatabase.createCollection(collection, onSuccess(res -> {
      insertDocs(mongoClient, collection, numDocs, onSuccess(res2 -> {
        MongoCollection<JsonObject> coll = mongoDatabase.getCollection(collection);
        coll.updateOne(query, update, options, onSuccess(res3 -> {
          coll.find().all(onSuccess(res4 -> {
            resultConsumer.accept(res4);
            testComplete();
          }));
        }));
      }));
    }));
    await();
  }

  private void doTestUpdateManyWithMongoClientUpdateResult(int numDocs, JsonObject query, JsonObject update, UpdateOptions options,
                                                       Consumer<List<JsonObject>> resultConsumer) throws Exception {
    String collection = randomCollection();
    mongoDatabase.createCollection(collection, onSuccess(res -> {
      insertDocs(mongoClient, collection, numDocs, onSuccess(res2 -> {
        MongoCollection<JsonObject> coll = mongoDatabase.getCollection(collection);
        coll.updateMany(query, update, options, onSuccess(res3 -> {
          coll.find().all(onSuccess(res4 -> {
            resultConsumer.accept(res4);
            testComplete();
          }));
        }));
      }));
    }));
    await();
  }

  @Test
  public void testRemoveOne() throws Exception {
    String collection = randomCollection();
    insertDocs(mongoClient, collection, 6, onSuccess(res2 -> {
      MongoCollection<JsonObject> coll = mongoDatabase.getCollection(collection);
      coll.deleteOne(new JsonObject().put("num", 123), onSuccess(res3 -> {
        coll.countDocuments(onSuccess(count -> {
          assertEquals(5, (long) count);
          testComplete();
        }));
      }));
    }));
    await();
  }

  @Test
  public void testRemoveOneWithMongoClientDeleteResult() throws Exception {
    String collection = randomCollection();
    insertDocs(mongoClient, collection, 6, onSuccess(res2 -> {
      MongoCollection<JsonObject> coll = mongoDatabase.getCollection(collection);
      coll.deleteOne(new JsonObject().put("num", 123), onSuccess(res3 -> {
        coll.countDocuments(onSuccess(count -> {
          assertEquals(1, res3.getDeletedCount());
          assertEquals(5, (long) count);
          testComplete();
        }));
      }));
    }));
    await();
  }

  //TODO implement UNACKNOWLEDGED
//  @Test
//  public void testRemoveOneWithOptions() throws Exception {
//    String collection = randomCollection();
//    insertDocs(mongoClient, collection, 6, onSuccess(res2 -> {
//      mongoDatabase.removeDocumentWithOptions(collection, new JsonObject().put("num", 123), UNACKNOWLEDGED, onSuccess(res3 -> {
//        mongoDatabase.count(collection, new JsonObject(), onSuccess(count -> {
//          assertNull(res3);
//
//          assertEquals(5, (long) count);
//          testComplete();
//        }));
//      }));
//    }));
//    await();
//  }

  //TODO WON'T DO ? mongo says "options can not be null"
//  @Test
//  public void testRemoveDocumentWithOptionsCanHaveNullWriteOption() throws Exception {
//    String collection = randomCollection();
//    insertDocs(mongoClient, collection, 6, onSuccess(res2 -> {
//      MongoCollection<JsonObject> coll = mongoDatabase.getCollection(collection);
//      coll.deleteOne(new JsonObject().put("num", 123), null, onSuccess(res3 -> {
//        assertEquals(1, res3.getDeletedCount());
//
//        testComplete();
//      }));
//    }));
//    await();
//  }

  //TODO implement ACKNOWLEDGED
//  @Test
//  public void testRemoveOneWithOptionsWithMongoClientDeleteResultAcknowledged() throws Exception {
//    String collection = randomCollection();
//    insertDocs(mongoClient, collection, 6, onSuccess(res2 -> {
//      mongoDatabase.removeDocumentWithOptions(collection, new JsonObject().put("num", 123), ACKNOWLEDGED, onSuccess(res3 -> {
//        assertEquals(1, res3.getRemovedCount());
//
//        testComplete();
//      }));
//    }));
//    await();
//  }

  //TODO implement UNACKNOWLEDGED
//
//  @Test
//  public void testRemoveOneWithOptionsWithMongoClientDeleteResultUnacknowledged() throws Exception {
//    String collection = randomCollection();
//    insertDocs(mongoClient, collection, 6, onSuccess(res2 -> {
//      mongoDatabase.removeDocumentWithOptions(collection, new JsonObject().put("num", 123), UNACKNOWLEDGED, onSuccess(res3 -> {
//        assertNull(res3);
//
//        testComplete();
//      }));
//    }));
//    await();
//  }
//
  @Test
  public void testRemoveMultiple() throws Exception {
    String collection = randomCollection();
    insertDocs(mongoClient, collection, 10, onSuccess(v -> {
      MongoCollection<JsonObject> coll = mongoDatabase.getCollection(collection);
      coll.deleteMany(new JsonObject(), onSuccess(v2 -> {
        coll.find().all(onSuccess(res2 -> {
          assertTrue(res2.isEmpty());
          testComplete();
        }));
      }));
    }));
    await();
  }

  @Test
  public void testRemoveMultipleWithMongoClientDeleteResult() throws Exception {
    String collection = randomCollection();
    insertDocs(mongoClient, collection, 10, onSuccess(v -> {
      MongoCollection<JsonObject> coll = mongoDatabase.getCollection(collection);
      coll.deleteMany(new JsonObject(), onSuccess(v2 -> {
        coll.find().all(onSuccess(res2 -> {
          assertEquals(10, v2.getDeletedCount());

          assertTrue(res2.isEmpty());
          testComplete();
        }));
      }));
    }));
    await();
  }
  //TODO implement ACKNOWLEDGED
//  @Test
//  public void testRemoveWithOptions() throws Exception {
//    String collection = randomCollection();
//    insertDocs(mongoClient, collection, 10, onSuccess(v -> {
//      mongoDatabase.removeDocumentsWithOptions(collection, new JsonObject(), ACKNOWLEDGED, onSuccess(v2 -> {
//        mongoDatabase.find(collection, new JsonObject(), onSuccess(res2 -> {
//          assertTrue(res2.isEmpty());
//
//          testComplete();
//        }));
//      }));
//    }));
//    await();
//  }

  //TODO WON'T DO? Mongo doesn't like null options
//  @Test
//  public void testRemoveDocumentsWithOptionsCanHaveNullWriteOption() throws Exception {
//    String collection = randomCollection();
//    insertDocs(mongoClient, collection, 10, onSuccess(v -> {
//      mongoDatabase.removeDocumentsWithOptions(collection, new JsonObject(), null, onSuccess(v2 -> {
//        assertEquals(10, v2.getRemovedCount());
//
//        testComplete();
//      }));
//    }));
//    await();
//  }

  //TODO implement ACKNOWLEDGED
//  @Test
//  public void testRemoveWithOptionsWithMongoClientDeleteResultAcknowledge() throws Exception {
//    String collection = randomCollection();
//    insertDocs(mongoClient, collection, 10, onSuccess(v -> {
//      mongoDatabase.removeDocumentsWithOptions(collection, new JsonObject(), ACKNOWLEDGED, onSuccess(v2 -> {
//        assertEquals(10, v2.getRemovedCount());
//
//        testComplete();
//      }));
//    }));
//    await();
//  }

  //TODO implement UNACKNOWLEDGED
//  @Test
//  public void testRemoveWithOptionsWithMongoClientDeleteResultUnacknowledge() throws Exception {
//    String collection = randomCollection();
//    insertDocs(mongoClient, collection, 10, onSuccess(v -> {
//      mongoDatabase.removeDocumentsWithOptions(collection, new JsonObject(), UNACKNOWLEDGED, onSuccess(v2 -> {
//        assertNull(v2);
//
//        testComplete();
//      }));
//    }));
//    await();
//  }
//
  @Test
  public void testNonStringID() {
    String collection = randomCollection();
    JsonObject document = new JsonObject().put("title", "The Hobbit");
    // here it happened
    document.put("_id", 123456);
    document.put("foo", "bar");
    MongoCollection<JsonObject> coll = mongoDatabase.getCollection(collection);
    coll.insertOne(document, onSuccess(id -> {
      coll.find().first(onSuccess(retrieved -> {
        assertEquals(document, retrieved);
        testComplete();
      }));
    }));
    await();
  }


  @Test
  public void testContexts() {
    vertx.runOnContext(v -> {
      Context currentContext = Vertx.currentContext();
      assertNotNull(currentContext);

      String collection = randomCollection();
      JsonObject document = new JsonObject().put("title", "The Hobbit");
      document.put("_id", 123456);
      document.put("foo", "bar");
      MongoCollection<JsonObject> coll = mongoDatabase.getCollection(collection);
      coll.insertOne(document, onSuccess(id -> {
        Context resultContext = Vertx.currentContext();
        assertSame(currentContext, resultContext);
        testComplete();
      }));

    });
    await();
  }

  @Test
  public void testBulkOperation_insertDocument() {
    String collection = randomCollection();
    JsonObject doc = createDoc();
    MongoCollection<JsonObject> coll = mongoDatabase.getCollection(collection);
    coll.bulkWrite(List.of(new InsertOneModel<>(doc)), onSuccess(bulkResult -> {
      assertEquals(1, bulkResult.getInsertedCount());
      assertEquals(0, bulkResult.getModifiedCount());
      assertEquals(0, bulkResult.getDeletedCount());
      assertEquals(0, bulkResult.getMatchedCount());
      coll.find().all(onSuccess(docs -> {
        assertEquals(1, docs.size());
        JsonObject foundDoc = docs.get(0);
        // FIXME id was a string, now an OID?
        doc.put("_id", foundDoc.getValue("_id"));
        assertEquals(foundDoc, doc);
        testComplete();
      }));
    }));
    await();
  }

  @Test
  public void testBulkOperation_insertDocuments() {
    String collection = randomCollection();
    JsonObject doc1 = new JsonObject().put("foo", "bar");
    JsonObject doc2 = new JsonObject().put("foo", "foobar");
    MongoCollection<JsonObject> coll = mongoDatabase.getCollection(collection);
    coll.bulkWrite(
      Arrays.asList(new InsertOneModel<>(doc1), new InsertOneModel<>(doc2)),
      onSuccess(bulkResult -> {
        assertEquals(2, bulkResult.getInsertedCount());
        assertEquals(0, bulkResult.getModifiedCount());
        assertEquals(0, bulkResult.getDeletedCount());
        assertEquals(0, bulkResult.getMatchedCount());
        testComplete();
      }));
    await();
  }

  @Test
  public void testBulkOperation_updateDocument() {
    String collection = randomCollection();
    JsonObject doc = new JsonObject().put("foo", "bar");
    MongoCollection<JsonObject> coll = mongoDatabase.getCollection(collection);
    coll.insertOne(doc, onSuccess(res -> {
      Object id = res.getInsertedId();
      JsonObject update = new JsonObject().put("$set", new JsonObject().put("foo", "foobar"));
      JsonObject filter = new JsonObject();
      if (useObjectId)
        filter.put("_id", new JsonObject().put(JsonObjectCodec.OID_FIELD, id));
      else
        filter.put("_id", id);
      coll.bulkWrite(List.of(new UpdateOneModel<>(filter, update)),
        onSuccess(bulkResult -> {
          assertEquals(1, bulkResult.getModifiedCount());
          assertEquals(0, bulkResult.getDeletedCount());
          assertEquals(0, bulkResult.getInsertedCount());
          assertEquals(1, bulkResult.getMatchedCount());
          coll.find().all(onSuccess(docs -> {
            assertEquals(1, docs.size());
            JsonObject foundDoc = docs.get(0);
            assertEquals("foobar", foundDoc.getString("foo"));
            testComplete();
          }));
        }));
    }));
    await();
  }

  @Test
  public void testBulkOperation_updateMultipleDocuments() {
    String collection = randomCollection();
    insertDocs(mongoClient, collection, 5, onSuccess(v -> {
      JsonObject update = new JsonObject().put("$set", new JsonObject().put("foo", "foobar-bulk"));
      JsonObject filter = new JsonObject();
      MongoCollection<JsonObject> coll = mongoDatabase.getCollection(collection);
      coll.bulkWrite(List.of(new UpdateManyModel<>(filter, update)), onSuccess(bulkResult -> {
        assertEquals(5, bulkResult.getModifiedCount());
        assertEquals(5, bulkResult.getMatchedCount());
        assertEquals(0, bulkResult.getDeletedCount());
        assertEquals(0, bulkResult.getInsertedCount());
        coll.find().all(onSuccess(docs -> {
          assertEquals(5, docs.size());
          for (JsonObject foundDoc : docs) {
            assertEquals("foobar-bulk", foundDoc.getString("foo"));
          }
          testComplete();
        }));
      }));
    }));
    await();
  }

  @Test
  public void testBulkOperation_updateMultipleDocuments_multiFalse() {
    String collection = randomCollection();
    insertDocs(mongoClient, collection, 5, onSuccess(v -> {
      JsonObject update = new JsonObject().put("$set", new JsonObject().put("foo", "foobar-bulk"));
      JsonObject filter = new JsonObject();
      MongoCollection<JsonObject> coll = mongoDatabase.getCollection(collection);
      coll.bulkWrite(List.of(new UpdateOneModel<>(filter, update)), onSuccess(bulkResult -> {
        assertEquals(1, bulkResult.getModifiedCount());
        assertEquals(0, bulkResult.getDeletedCount());
        assertEquals(0, bulkResult.getInsertedCount());
        assertEquals(1, bulkResult.getMatchedCount());
        coll.find(new JsonObject().put("foo", "foobar-bulk")).all(onSuccess(docs -> {
          assertEquals(1, docs.size());
          testComplete();
        }));
      }));
    }));
    await();
  }

  @Test
  public void testBulkOperation_upsertDocument() {
    String collection = randomCollection();
    JsonObject doc = new JsonObject().put("$set", new JsonObject().put("foo", "bar"));
    UpdateOneModel<JsonObject> bulkUpdate = new UpdateOneModel<>(new JsonObject().put("foo", "bur"), doc, new UpdateOptions().upsert(true));
    MongoCollection<JsonObject> coll = mongoDatabase.getCollection(collection);
    coll.bulkWrite(List.of(bulkUpdate), onSuccess(bulkResult -> {
      // even though one document was created, the MongoDB client returns 0 for all counts
      assertEquals(0, bulkResult.getInsertedCount());
      assertEquals(0, bulkResult.getDeletedCount());
      assertEquals(0, bulkResult.getModifiedCount());
      assertEquals(0, bulkResult.getMatchedCount());
      List<BulkWriteUpsert> upserts = bulkResult.getUpserts();
      assertNotNull(upserts);
      assertEquals(1, upserts.size());
      BulkWriteUpsert upsert = upserts.get(0);
      assertEquals(0, upsert.getIndex());
      testComplete();
    }));
    await();
  }

  @Test
  public void testBulkOperation_upsertDocument_upsertDisabled() {
    String collection = randomCollection();
    JsonObject doc = new JsonObject().put("$set", new JsonObject().put("foo", "bar"));
    UpdateOneModel<JsonObject> bulkUpdate = new UpdateOneModel<>(new JsonObject().put("foo", "bur"), doc, new UpdateOptions().upsert(false));
    MongoCollection<JsonObject> coll = mongoDatabase.getCollection(collection);
    coll.bulkWrite(List.of(bulkUpdate), onSuccess(bulkResult -> {
      assertEquals(0, bulkResult.getInsertedCount());
      assertEquals(0, bulkResult.getModifiedCount());
      assertEquals(0, bulkResult.getDeletedCount());
      assertEquals(0, bulkResult.getMatchedCount());
      assertEquals(0, bulkResult.getUpserts().size());
      testComplete();
    }));
    await();
  }

  @Test
  public void testBulkOperation_replace() {
    String collection = randomCollection();
    insertDocs(mongoClient, collection, 1, onSuccess(v -> {
      JsonObject filter = new JsonObject().put("num", 123);
      JsonObject replace = new JsonObject().put("foo", "replaced");
      ReplaceOneModel<JsonObject> bulkReplace = new ReplaceOneModel<>(filter, replace);
      MongoCollection<JsonObject> coll = mongoDatabase.getCollection(collection);
      coll.bulkWrite(List.of(bulkReplace), onSuccess(bulkResult -> {
        assertEquals(0, bulkResult.getInsertedCount());
        assertEquals(1, bulkResult.getModifiedCount());
        assertEquals(0, bulkResult.getDeletedCount());
        assertEquals(1, bulkResult.getMatchedCount());
        assertEquals(0, bulkResult.getUpserts().size());
        coll.find().all(onSuccess(docs -> {
          assertEquals(1, docs.size());
          JsonObject foundDoc = docs.get(0);
          assertEquals("replaced", foundDoc.getString("foo"));
          assertNull(foundDoc.getInteger("num"));
          testComplete();
        }));
      }));
    }));
    await();
  }

  @Test
  public void testBulkOperation_replaceById() {
    String collection = randomCollection();
    mongoDatabase.createCollection(collection, onSuccess(res1 -> {
      MongoCollection<JsonObject> coll = mongoDatabase.getCollection(collection);
      JsonObject doc = new JsonObject();
      doc.put("foo", "bar");
      doc.put("num", 123);
      coll.insertOne(doc, onSuccess(res -> {
        JsonObject filter = resultToIdFilter(res);
        JsonObject replace = new JsonObject().put("foo", "replaced");
        ReplaceOneModel<JsonObject> bulkReplace = new ReplaceOneModel<>(filter, replace);
        coll.bulkWrite(List.of(bulkReplace), onSuccess(bulkResult -> {
          assertEquals(0, bulkResult.getInsertedCount());
          assertEquals(0, bulkResult.getDeletedCount());
          assertEquals(1, bulkResult.getMatchedCount());
          assertEquals(0, bulkResult.getUpserts().size());
          coll.find().all(onSuccess(docs -> {
            assertEquals(1, docs.size());
            JsonObject foundDoc = docs.get(0);
            assertEquals("replaced", foundDoc.getString("foo"));
            assertNull(foundDoc.getInteger("num"));
            testComplete();
          }));
        }));
      }));
    }));
    await();
  }

  @Test
  public void testBulkOperation_replaceOne_upsert() {
    String collection = randomCollection();
    JsonObject filter = new JsonObject().put("foo", "bar");
    JsonObject replace = new JsonObject().put("foo", "upsert");
    ReplaceOneModel<JsonObject> bulkReplace = new ReplaceOneModel<>(filter, replace, new ReplaceOptions().upsert(true));
    MongoCollection<JsonObject> coll = mongoDatabase.getCollection(collection);
    coll.bulkWrite(List.of(bulkReplace), onSuccess(bulkResult -> {
      assertEquals(0, bulkResult.getInsertedCount());
      assertEquals(0, bulkResult.getDeletedCount());
      assertEquals(0, bulkResult.getModifiedCount());
      assertEquals(0, bulkResult.getMatchedCount());
      assertEquals(1, bulkResult.getUpserts().size());
      assertEquals(0, bulkResult.getUpserts().get(0).getIndex());
      coll.find().all(onSuccess(docs -> {
        assertEquals(1, docs.size());
        JsonObject foundDoc = docs.get(0);
        assertEquals("upsert", foundDoc.getString("foo"));
        testComplete();
      }));
    }));
    await();
  }

  @Test
  public void testBulkOperation_delete_multiDisabled() {
    String collection = randomCollection();
    insertDocs(mongoClient, collection, 5, onSuccess(v -> {
      JsonObject filter = new JsonObject().put("num", 123);
      DeleteOneModel<JsonObject> bulkDelete = new DeleteOneModel<>(filter);
      MongoCollection<JsonObject> coll = mongoDatabase.getCollection(collection);
      coll.bulkWrite(List.of(bulkDelete), onSuccess(bulkResult -> {
        assertEquals(0, bulkResult.getInsertedCount());
        assertEquals(1, bulkResult.getDeletedCount());
        assertEquals(0, bulkResult.getModifiedCount());
        assertEquals(0, bulkResult.getMatchedCount());
        assertEquals(0, bulkResult.getUpserts().size());
        testComplete();
      }));
    }));
    await();
  }

  @Test
  public void testBulkOperation_delete_multiEnabled() {
    String collection = randomCollection();
    insertDocs(mongoClient, collection, 5, onSuccess(v -> {
      JsonObject filter = new JsonObject().put("num", 123);
      DeleteManyModel<JsonObject> bulkDelete = new DeleteManyModel<>(filter);
      MongoCollection<JsonObject> coll = mongoDatabase.getCollection(collection);
      coll.bulkWrite(List.of(bulkDelete), onSuccess(bulkResult -> {
        assertEquals(0, bulkResult.getInsertedCount());
        assertEquals(5, bulkResult.getDeletedCount());
        assertEquals(0, bulkResult.getModifiedCount());
        assertEquals(0, bulkResult.getMatchedCount());
        assertEquals(0, bulkResult.getUpserts().size());
        testComplete();
      }));
    }));
    await();
  }

  //TODO implement UNACKNOWLEDGED
//  @Test
//  public void testBulkOperation_completeBulk() {
//    testCompleteBulk(null);
//  }
//
//  @Test
//  public void testBulkOperation_completeBulk_unacknowleged() {
//    testCompleteBulk(new BulkWriteOptions().setWriteOption(UNACKNOWLEDGED));
//  }
//
//  @Test
//  public void testBulkOperationwithOptions_completeBulk_orderedFalse() {
//    testCompleteBulk(new BulkWriteOptions(false));
//  }
//
//  @Test
//  public void testBulkOperationwithOptions_completeBulk_orderedTrue() {
//    testCompleteBulk(new BulkWriteOptions(true));
//  }
//
//  private void testCompleteBulk(BulkWriteOptions bulkWriteOptions) {
//    String collection = randomCollection();
//    insertDocs(mongoClient, collection, 5, onSuccess(v -> {
//      BulkOperation bulkInsert = BulkOperation.createInsert(new JsonObject().put("foo", "insert"));
//      BulkOperation bulkUpdate = BulkOperation.createUpdate(new JsonObject().put("foo", "bar1"),
//        new JsonObject().put("$set", new JsonObject().put("foo", "update")));
//      BulkOperation bulkReplace = BulkOperation.createReplace(new JsonObject().put("foo", "bar2"),
//        new JsonObject().put("foo", "replace"));
//      BulkOperation bulkDelete = BulkOperation.createDelete(new JsonObject().put("foo", "bar3"));
//      Handler<AsyncResult<MongoClientBulkWriteResult>> successHandler = onSuccess(bulkResult -> {
//        if (bulkWriteOptions != null && bulkWriteOptions.getWriteOption() == UNACKNOWLEDGED) {
//          assertNull(bulkResult);
//          testComplete();
//        } else {
//          assertEquals(1, bulkResult.getInsertedCount());
//          assertEquals(1, bulkResult.getDeletedCount());
//          assertEquals(2, bulkResult.getModifiedCount());
//          assertEquals(2, bulkResult.getMatchedCount());
//          assertEquals(0, bulkResult.getUpserts().size());
//          mongoDatabase.find(collection, new JsonObject(), onSuccess(docs -> {
//            List<String> values = docs.stream().map(doc -> doc.getString("foo")).collect(Collectors.toList());
//            assertTrue(values.contains("insert"));
//            assertFalse(values.contains("bar1"));
//            assertTrue(values.contains("update"));
//            assertFalse(values.contains("bar2"));
//            assertTrue(values.contains("replace"));
//            assertFalse(values.contains("bar3"));
//            testComplete();
//          }));
//        }
//      });
//      if (bulkWriteOptions == null)
//        mongoDatabase.bulkWrite(collection, Arrays.asList(bulkInsert, bulkUpdate, bulkReplace, bulkDelete),
//          successHandler);
//      else
//        mongoDatabase.bulkWriteWithOptions(collection, Arrays.asList(bulkInsert, bulkUpdate, bulkReplace, bulkDelete),
//          bulkWriteOptions, successHandler);
//    }));
//    await();
//  }
}
