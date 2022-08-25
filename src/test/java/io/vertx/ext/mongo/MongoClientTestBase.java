package io.vertx.ext.mongo;

import com.mongodb.client.model.ReturnDocument;
import io.vertx.core.json.JsonObject;
import io.vertx.mongo.client.FindOptions;
import io.vertx.mongo.client.MongoClient;
import io.vertx.mongo.client.MongoCollection;
import io.vertx.mongo.client.MongoDatabase;
import io.vertx.mongo.client.model.*;
import io.vertx.mongo.client.result.InsertOneResult;
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
        coll.replaceOne(new JsonObject().put("_id", doc.getJsonObject("_id")), doc, onSuccess(res3 -> {
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

  private static JsonObject resultToId(InsertOneResult res2) {
    return new JsonObject().put("$oid", res2.getInsertedId());
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

  private static JsonObject resultToIdFilter(InsertOneResult res) {
    return new JsonObject().put("_id", resultToId(res));
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
    doc.put("otherId", new JsonObject().put("$oid", objectId.toHexString()));

    MongoCollection<JsonObject> coll = mongoDatabase.getCollection(collection);
    coll.insertOne(doc, onSuccess(res -> {
      assertNotNull(res.getInsertedId());
      coll.find(resultToIdFilter(res)).first().onSuccess(result -> {
        assertNotNull(result);
        assertNotNull(result.getJsonObject("otherId").getString("$oid"));
        assertEquals(objectId.toHexString(), result.getJsonObject("otherId").getString("$oid"));
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
//          mongoDatabase.findOne(collection, new JsonObject(), null, onSuccess(res2 -> {
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
//      mongoDatabase.insert(collection, doc1, onSuccess(id1 -> {
//        assertNotNull(id1);
//        mongoDatabase.insert(collection, doc2, onSuccess(id2 -> {
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
            assertEquals(id, ((JsonObject) id_value).getString("$oid"));
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
            assertEquals(id, ((JsonObject) id_value).getString("$oid"));
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
//
//  @Test
//  public void testReplaceMongoClientUpdateResultUnacknowledge() throws Exception {
//    String collection = randomCollection();
//    JsonObject doc = createDoc();
//    mongoDatabase.insert(collection, doc, onSuccess(id -> {
//      JsonObject replacement = createDoc();
//      replacement.put("replacement", true);
//      mongoDatabase.replaceDocumentsWithOptions(collection, new JsonObject().put("_id", id), replacement,
//        new UpdateOptions().setWriteOption(UNACKNOWLEDGED), onSuccess(v -> {
//          assertNull(v);
//          testComplete();
//        }));
//    }));
//
//    await();
//  }
//
//  @Test
//  public void testReplaceMongoClientUpdateResultAcknowledge() throws Exception {
//    String collection = randomCollection();
//    JsonObject doc = createDoc();
//    mongoDatabase.insert(collection, doc, onSuccess(id -> {
//      JsonObject replacement = createDoc();
//      replacement.put("replacement", true);
//      mongoDatabase.replaceDocumentsWithOptions(collection, new JsonObject().put("_id", id), replacement,
//        new UpdateOptions().setWriteOption(ACKNOWLEDGED), onSuccess(v -> {
//          assertNull(v.getDocUpsertedId());
//          assertEquals(1, v.getDocMatched());
//          assertEquals(1, v.getDocModified());
//          testComplete();
//        }));
//    }));
//    await();
//  }
//
//  @Test
//  public void testReplaceUpsert() {
//    String collection = randomCollection();
//    JsonObject doc = createDoc();
//    mongoDatabase.insert(collection, doc, onSuccess(id -> {
//      assertNotNull(id);
//      JsonObject replacement = createDoc();
//      replacement.put("replacement", true);
//      mongoDatabase.replaceDocumentsWithOptions(collection, new JsonObject().put("_id", "foo"), replacement, new UpdateOptions(true), onSuccess(v -> {
//        mongoDatabase.find(collection, new JsonObject(), onSuccess(list -> {
//          assertNotNull(list);
//          assertEquals(2, list.size());
//          JsonObject result = null;
//          for (JsonObject o : list) {
//            if (o.containsKey("replacement")) {
//              result = o;
//            }
//          }
//          assertNotNull(result);
//          testComplete();
//        }));
//      }));
//    }));
//
//    await();
//  }
//
//  @Test
//  public void testReplaceUpsertWithMongoClientUpdateResult() {
//    String collection = randomCollection();
//    JsonObject doc = createDoc();
//    String upsertTestId = "foo";
//    mongoDatabase.insert(collection, doc, onSuccess(id -> {
//      assertNotNull(id);
//      JsonObject replacement = createDoc();
//      replacement.put("replacement", true);
//      mongoDatabase.replaceDocumentsWithOptions(collection, new JsonObject().put("_id", upsertTestId), replacement, new UpdateOptions(true), onSuccess(v -> {
//        mongoDatabase.find(collection, new JsonObject(), onSuccess(list -> {
//          assertEquals(upsertTestId, v.getDocUpsertedId().getString(MongoClientUpdateResult.ID_FIELD));
//          assertEquals(0, v.getDocMatched());
//          assertEquals(0, v.getDocModified());
//
//          assertNotNull(list);
//          assertEquals(2, list.size());
//          JsonObject result = null;
//          for (JsonObject o : list) {
//            if (o.containsKey("replacement")) {
//              result = o;
//            }
//          }
//          assertNotNull(result);
//          testComplete();
//        }));
//      }));
//    }));
//
//    await();
//  }
//
//  @Test
//  public void testReplaceUpsert2() {
//    String collection = randomCollection();
//    JsonObject doc = createDoc();
//    mongoDatabase.insert(collection, doc, onSuccess(id -> {
//      assertNotNull(id);
//      JsonObject replacement = createDoc();
//      replacement.put("replacement", true);
//      mongoDatabase.replaceDocumentsWithOptions(collection, new JsonObject().put("_id", id), replacement, new UpdateOptions(true), onSuccess(v -> {
//        mongoDatabase.find(collection, new JsonObject(), onSuccess(list -> {
//          assertNotNull(list);
//          assertEquals(1, list.size());
//          Object id_value = list.get(0).getValue("_id");
//          if (id_value instanceof JsonObject) {
//            assertEquals(id, ((JsonObject) id_value).getString("$oid"));
//          } else {
//            assertEquals(id, id_value);
//          }
//          testComplete();
//        }));
//      }));
//    }));
//
//    await();
//  }
//
//  @Test
//  public void testReplaceUpsertWithNoNewIdWithMongoClientUpdateResult() {
//    String collection = randomCollection();
//    JsonObject doc = createDoc();
//    mongoDatabase.insert(collection, doc, onSuccess(id -> {
//      assertNotNull(id);
//      JsonObject replacement = createDoc();
//      replacement.put("replacement", true);
//      mongoDatabase.replaceDocumentsWithOptions(collection, new JsonObject().put("_id", id), replacement, new UpdateOptions(true), onSuccess(v -> {
//        mongoDatabase.find(collection, new JsonObject(), onSuccess(list -> {
//          assertNull(v.getDocUpsertedId());
//          assertEquals(1, v.getDocMatched());
//          assertEquals(1, v.getDocModified());
//
//          assertNotNull(list);
//          assertEquals(1, list.size());
//          Object id_value = list.get(0).getValue("_id");
//          if (id_value instanceof JsonObject) {
//            assertEquals(id, ((JsonObject) id_value).getString("$oid"));
//          } else {
//            assertEquals(id, id_value);
//          }
//          testComplete();
//        }));
//      }));
//    }));
//
//    await();
//  }
//
//  //TODO: Technical debt. Later may have to re-factor so that the tests that extend this does not cause different execution path because of the conditional tests.
//  @Test
//  public void testUpdate() throws Exception {
//    String collection = randomCollection();
//    mongoDatabase.insert(collection, createDoc(), onSuccess(id -> {
//      if (useObjectId) {
//        JsonObject docIdToBeUpdatedObjectId = new JsonObject().put(JsonObjectCodec.OID_FIELD, id);
//        updateDataBasedOnId(collection, docIdToBeUpdatedObjectId);
//      } else {
//        String docIdToBeUpdatedString = id;
//        updateDataBasedOnId(collection, docIdToBeUpdatedString);
//      }
//
//    }));
//    await();
//  }
//
//  private <T> void updateDataBasedOnId(String collection, T docIdToBeUpdated) {
//    mongoDatabase.updateCollection(collection, new JsonObject().put("_id", docIdToBeUpdated), new JsonObject().put("$set", new JsonObject().put("foo", "fooed")), onSuccess(res -> {
//      mongoDatabase.findOne(collection, new JsonObject().put("_id", docIdToBeUpdated), null, onSuccess(doc -> {
//        assertEquals("fooed", doc.getString("foo"));
//        testComplete();
//      }));
//    }));
//  }
//
//
//  @Test
//  public void testUpdateWithMongoClientUpdateResult() throws Exception {
//    String collection = randomCollection();
//    mongoDatabase.insert(collection, createDoc(), onSuccess(id -> {
//      if (useObjectId) {
//        JsonObject docIdToBeUpdatedObjectId = new JsonObject().put(JsonObjectCodec.OID_FIELD, id);
//        updateDataBasedOnIdWithMongoClientUpdateResult(collection, docIdToBeUpdatedObjectId);
//      } else {
//        String docIdToBeUpdatedString = id;
//        updateDataBasedOnIdWithMongoClientUpdateResult(collection, docIdToBeUpdatedString);
//      }
//    }));
//    await();
//  }
//
//  private <T> void updateDataBasedOnIdWithMongoClientUpdateResult(String collection, T id) {
//    mongoDatabase.updateCollection(collection, new JsonObject().put("_id", id), new JsonObject().put("$set", new JsonObject().put("foo", "fooed")), onSuccess(res -> {
//      assertEquals(1, res.getDocModified());
//      assertEquals(1, res.getDocMatched());
//      assertNull(res.getDocUpsertedId());
//      mongoDatabase.findOne(collection, new JsonObject().put("_id", id), null, onSuccess(doc -> {
//        assertEquals("fooed", doc.getString("foo"));
//        testComplete();
//      }));
//    }));
//  }
//
//  @Test
//  public void testUpdateOne() throws Exception {
//    int num = 1;
//    doTestUpdate(num, new JsonObject().put("num", 123), new JsonObject().put("$set", new JsonObject().put("foo", "fooed")), new UpdateOptions(), results -> {
//      assertEquals(num, results.size());
//      for (JsonObject doc : results) {
//        assertEquals(12, doc.size());
//        assertEquals("fooed", doc.getString("foo"));
//        assertNotNull(doc.getValue("_id"));
//      }
//    });
//  }
//
//  @Test
//  public void testUpdateOneWithMongoClientUpdateResult() throws Exception {
//    int num = 1;
//    doTestUpdateWithMongoClientUpdateResult(num, new JsonObject().put("num", 123), new JsonObject().put("$set", new JsonObject().put("foo", "fooed")), new UpdateOptions(), results -> {
//      assertEquals(num, results.size());
//      for (JsonObject doc : results) {
//        assertEquals(12, doc.size());
//        assertEquals("fooed", doc.getString("foo"));
//        assertNotNull(doc.getValue("_id"));
//      }
//    });
//  }
//
//  @Test
//  public void testUpdateAll() throws Exception {
//    int num = 10;
//    doTestUpdate(num, new JsonObject().put("num", 123), new JsonObject().put("$set", new JsonObject().put("foo", "fooed")), new UpdateOptions(false, true), results -> {
//      assertEquals(num, results.size());
//      for (JsonObject doc : results) {
//        assertEquals(12, doc.size());
//        assertEquals("fooed", doc.getString("foo"));
//        assertNotNull(doc.getValue("_id"));
//      }
//    });
//  }
//
//  @Test
//  public void testUpdateAllWithMongoClientUpdateResult() throws Exception {
//    int num = 10;
//    doTestUpdateWithMongoClientUpdateResult(num, new JsonObject().put("num", 123), new JsonObject().put("$set", new JsonObject().put("foo", "fooed")), new UpdateOptions(false, true), results -> {
//      assertEquals(num, results.size());
//      for (JsonObject doc : results) {
//        assertEquals(12, doc.size());
//        assertEquals("fooed", doc.getString("foo"));
//        assertNotNull(doc.getValue("_id"));
//      }
//    });
//  }
//
//  @Test
//  public void testUpdateMongoClientUpdateResultUpsertIdResponse() throws Exception {
//    String collection = randomCollection();
//    String upsertedId = TestUtils.randomAlphaString(20);
//    mongoDatabase.insert(collection, createDoc(), onSuccess(id -> {
//      mongoDatabase.updateCollectionWithOptions(collection, new JsonObject().put("_id", upsertedId), new JsonObject().put("$set", new JsonObject().put("foo", "fooed")),
//        new UpdateOptions().setUpsert(true), onSuccess(res -> {
//          assertEquals(0, res.getDocModified());
//          assertEquals(0, res.getDocMatched());
//          assertEquals(upsertedId, res.getDocUpsertedId().getString(MongoClientUpdateResult.ID_FIELD));
//          testComplete();
//        }));
//    }));
//    await();
//  }
//
//  @Test
//  public void testMongoClientUpdateResultAcknowledge() throws Exception {
//    String collection = randomCollection();
//    mongoDatabase.insert(collection, createDoc(), onSuccess(id -> {
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
//
//  private <T> void updateWithOptionWithMongoClientUpdateResultBasedOnIdAcknowledged(String collection, T id) {
//    mongoDatabase.updateCollectionWithOptions(collection, new JsonObject().put("_id", id), new JsonObject().put("$set", new JsonObject().put("foo", "fooed")),
//      new UpdateOptions().setWriteOption(ACKNOWLEDGED), onSuccess(res -> {
//        assertEquals(1, res.getDocModified());
//        assertEquals(1, res.getDocMatched());
//        assertNull(res.getDocUpsertedId());
//        testComplete();
//      }));
//  }
//
//  @Test
//  public void testMongoClientUpdateResultUnacknowledge() throws Exception {
//    String collection = randomCollection();
//    String upsertedId = TestUtils.randomAlphaString(20);
//    mongoDatabase.insert(collection, createDoc(), onSuccess(id -> {
//      mongoDatabase.updateCollectionWithOptions(collection, new JsonObject().put("_id", upsertedId), new JsonObject().put("$set", new JsonObject().put("foo", "fooed")),
//        new UpdateOptions().setWriteOption(UNACKNOWLEDGED), onSuccess(res -> {
//          assertNull(res);
//          testComplete();
//        }));
//    }));
//    await();
//  }
//
//  private void doTestUpdate(int numDocs, JsonObject query, JsonObject update, UpdateOptions options,
//                            Consumer<List<JsonObject>> resultConsumer) throws Exception {
//    String collection = randomCollection();
//    mongoDatabase.createCollection(collection, onSuccess(res -> {
//      insertDocs(mongoDatabase, collection, numDocs, onSuccess(res2 -> {
//        mongoDatabase.updateCollectionWithOptions(collection, query, update, options, onSuccess(res3 -> {
//          mongoDatabase.find(collection, new JsonObject(), onSuccess(res4 -> {
//            resultConsumer.accept(res4);
//            testComplete();
//          }));
//        }));
//      }));
//    }));
//    await();
//  }
//
//  private void doTestUpdateWithMongoClientUpdateResult(int numDocs, JsonObject query, JsonObject update, UpdateOptions options,
//                                                       Consumer<List<JsonObject>> resultConsumer) throws Exception {
//    String collection = randomCollection();
//    mongoDatabase.createCollection(collection, onSuccess(res -> {
//      insertDocs(mongoDatabase, collection, numDocs, onSuccess(res2 -> {
//        mongoDatabase.updateCollectionWithOptions(collection, query, update, options, onSuccess(res3 -> {
//          mongoDatabase.find(collection, new JsonObject(), onSuccess(res4 -> {
//            resultConsumer.accept(res4);
//            testComplete();
//          }));
//        }));
//      }));
//    }));
//    await();
//  }
//
//  @Test
//  public void testRemoveOne() throws Exception {
//    String collection = randomCollection();
//    insertDocs(mongoDatabase, collection, 6, onSuccess(res2 -> {
//      mongoDatabase.removeDocument(collection, new JsonObject().put("num", 123), onSuccess(res3 -> {
//        mongoDatabase.count(collection, new JsonObject(), onSuccess(count -> {
//          assertEquals(5, (long) count);
//          testComplete();
//        }));
//      }));
//    }));
//    await();
//  }
//
//  @Test
//  public void testRemoveOneWithMongoClientDeleteResult() throws Exception {
//    String collection = randomCollection();
//    insertDocs(mongoDatabase, collection, 6, onSuccess(res2 -> {
//      mongoDatabase.removeDocument(collection, new JsonObject().put("num", 123), onSuccess(res3 -> {
//        mongoDatabase.count(collection, new JsonObject(), onSuccess(count -> {
//          assertEquals(1, res3.getRemovedCount());
//          assertEquals(5, (long) count);
//          testComplete();
//        }));
//      }));
//    }));
//    await();
//  }
//
//  @Test
//  public void testRemoveOneWithOptions() throws Exception {
//    String collection = randomCollection();
//    insertDocs(mongoDatabase, collection, 6, onSuccess(res2 -> {
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
//
//  @Test
//  public void testRemoveDocumentWithOptionsCanHaveNullWriteOption() throws Exception {
//    String collection = randomCollection();
//    insertDocs(mongoDatabase, collection, 6, onSuccess(res2 -> {
//      mongoDatabase.removeDocumentWithOptions(collection, new JsonObject().put("num", 123), null, onSuccess(res3 -> {
//        assertEquals(1, res3.getRemovedCount());
//
//        testComplete();
//      }));
//    }));
//    await();
//  }
//
//  @Test
//  public void testRemoveOneWithOptionsWithMongoClientDeleteResultAcknowledged() throws Exception {
//    String collection = randomCollection();
//    insertDocs(mongoDatabase, collection, 6, onSuccess(res2 -> {
//      mongoDatabase.removeDocumentWithOptions(collection, new JsonObject().put("num", 123), ACKNOWLEDGED, onSuccess(res3 -> {
//        assertEquals(1, res3.getRemovedCount());
//
//        testComplete();
//      }));
//    }));
//    await();
//  }
//
//  @Test
//  public void testRemoveOneWithOptionsWithMongoClientDeleteResultUnacknowledged() throws Exception {
//    String collection = randomCollection();
//    insertDocs(mongoDatabase, collection, 6, onSuccess(res2 -> {
//      mongoDatabase.removeDocumentWithOptions(collection, new JsonObject().put("num", 123), UNACKNOWLEDGED, onSuccess(res3 -> {
//        assertNull(res3);
//
//        testComplete();
//      }));
//    }));
//    await();
//  }
//
//  @Test
//  public void testRemoveMultiple() throws Exception {
//    String collection = randomCollection();
//    insertDocs(mongoDatabase, collection, 10, onSuccess(v -> {
//      mongoDatabase.removeDocuments(collection, new JsonObject(), onSuccess(v2 -> {
//        mongoDatabase.find(collection, new JsonObject(), onSuccess(res2 -> {
//          assertTrue(res2.isEmpty());
//          testComplete();
//        }));
//      }));
//    }));
//    await();
//  }
//
//  @Test
//  public void testRemoveMultipleWithMongoClientDeleteResult() throws Exception {
//    String collection = randomCollection();
//    insertDocs(mongoDatabase, collection, 10, onSuccess(v -> {
//      mongoDatabase.removeDocuments(collection, new JsonObject(), onSuccess(v2 -> {
//        mongoDatabase.find(collection, new JsonObject(), onSuccess(res2 -> {
//          assertEquals(10, v2.getRemovedCount());
//
//          assertTrue(res2.isEmpty());
//          testComplete();
//        }));
//      }));
//    }));
//    await();
//  }
//
//  @Test
//  public void testRemoveWithOptions() throws Exception {
//    String collection = randomCollection();
//    insertDocs(mongoDatabase, collection, 10, onSuccess(v -> {
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
//
//  @Test
//  public void testRemoveDocumentsWithOptionsCanHaveNullWriteOption() throws Exception {
//    String collection = randomCollection();
//    insertDocs(mongoDatabase, collection, 10, onSuccess(v -> {
//      mongoDatabase.removeDocumentsWithOptions(collection, new JsonObject(), null, onSuccess(v2 -> {
//        assertEquals(10, v2.getRemovedCount());
//
//        testComplete();
//      }));
//    }));
//    await();
//  }
//
//  @Test
//  public void testRemoveWithOptionsWithMongoClientDeleteResultAcknowledge() throws Exception {
//    String collection = randomCollection();
//    insertDocs(mongoDatabase, collection, 10, onSuccess(v -> {
//      mongoDatabase.removeDocumentsWithOptions(collection, new JsonObject(), ACKNOWLEDGED, onSuccess(v2 -> {
//        assertEquals(10, v2.getRemovedCount());
//
//        testComplete();
//      }));
//    }));
//    await();
//  }
//
//  @Test
//  public void testRemoveWithOptionsWithMongoClientDeleteResultUnacknowledge() throws Exception {
//    String collection = randomCollection();
//    insertDocs(mongoDatabase, collection, 10, onSuccess(v -> {
//      mongoDatabase.removeDocumentsWithOptions(collection, new JsonObject(), UNACKNOWLEDGED, onSuccess(v2 -> {
//        assertNull(v2);
//
//        testComplete();
//      }));
//    }));
//    await();
//  }
//
//  @Test
//  public void testNonStringID() {
//    String collection = randomCollection();
//    JsonObject document = new JsonObject().put("title", "The Hobbit");
//    // here it happened
//    document.put("_id", 123456);
//    document.put("foo", "bar");
//
//    mongoDatabase.insert(collection, document, onSuccess(id -> {
//      mongoDatabase.findOne(collection, new JsonObject(), null, onSuccess(retrieved -> {
//        assertEquals(document, retrieved);
//        testComplete();
//      }));
//    }));
//    await();
//  }
//
//
//  @Test
//  public void testContexts() {
//    vertx.runOnContext(v -> {
//      Context currentContext = Vertx.currentContext();
//      assertNotNull(currentContext);
//
//      String collection = randomCollection();
//      JsonObject document = new JsonObject().put("title", "The Hobbit");
//      document.put("_id", 123456);
//      document.put("foo", "bar");
//
//      mongoDatabase.insert(collection, document, onSuccess(id -> {
//        Context resultContext = Vertx.currentContext();
//        assertSame(currentContext, resultContext);
//        testComplete();
//      }));
//
//    });
//    await();
//  }
//
//  @Test
//  public void testBulkOperation_insertDocument() {
//    String collection = randomCollection();
//    JsonObject doc = createDoc();
//    mongoDatabase.bulkWrite(collection, Arrays.asList(BulkOperation.createInsert(doc)), onSuccess(bulkResult -> {
//      assertEquals(1, bulkResult.getInsertedCount());
//      assertEquals(0, bulkResult.getModifiedCount());
//      assertEquals(0, bulkResult.getDeletedCount());
//      assertEquals(0, bulkResult.getMatchedCount());
//      mongoDatabase.find(collection, new JsonObject(), onSuccess(docs -> {
//
//        assertEquals(1, docs.size());
//        JsonObject foundDoc = docs.get(0);
//        doc.put("_id", foundDoc.getString("_id"));
//        assertEquals(foundDoc, doc);
//        testComplete();
//      }));
//    }));
//    await();
//  }
//
//  @Test
//  public void testBulkOperation_insertDocuments() {
//    String collection = randomCollection();
//    JsonObject doc1 = new JsonObject().put("foo", "bar");
//    JsonObject doc2 = new JsonObject().put("foo", "foobar");
//    mongoDatabase.bulkWrite(collection,
//      Arrays.asList(BulkOperation.createInsert(doc1), BulkOperation.createInsert(doc2)),
//      onSuccess(bulkResult -> {
//        assertEquals(2, bulkResult.getInsertedCount());
//        assertEquals(0, bulkResult.getModifiedCount());
//        assertEquals(0, bulkResult.getDeletedCount());
//        assertEquals(0, bulkResult.getMatchedCount());
//        testComplete();
//      }));
//    await();
//  }
//
//  @Test
//  public void testBulkOperation_updateDocument() {
//    String collection = randomCollection();
//    JsonObject doc = new JsonObject().put("foo", "bar");
//    mongoDatabase.insert(collection, doc, onSuccess(id -> {
//      JsonObject update = new JsonObject().put("$set", new JsonObject().put("foo", "foobar"));
//      JsonObject filter = new JsonObject();
//      if (useObjectId)
//        filter.put("_id", new JsonObject().put(JsonObjectCodec.OID_FIELD, id));
//      else
//        filter.put("_id", id);
//      mongoDatabase.bulkWrite(collection, Arrays.asList(BulkOperation.createUpdate(filter, update)),
//        onSuccess(bulkResult -> {
//          assertEquals(1, bulkResult.getModifiedCount());
//          assertEquals(0, bulkResult.getDeletedCount());
//          assertEquals(0, bulkResult.getInsertedCount());
//          assertEquals(1, bulkResult.getMatchedCount());
//          mongoDatabase.find(collection, new JsonObject(), onSuccess(docs -> {
//            assertEquals(1, docs.size());
//            JsonObject foundDoc = docs.get(0);
//            assertEquals("foobar", foundDoc.getString("foo"));
//            testComplete();
//          }));
//        }));
//    }));
//    await();
//  }
//
//  @Test
//  public void testBulkOperation_updateMultipleDocuments() {
//    String collection = randomCollection();
//    insertDocs(mongoDatabase, collection, 5, onSuccess(v -> {
//      JsonObject update = new JsonObject().put("$set", new JsonObject().put("foo", "foobar-bulk"));
//      JsonObject filter = new JsonObject();
//      BulkOperation bulkUpdate = BulkOperation.createUpdate(filter, update).setMulti(true);
//      mongoDatabase.bulkWrite(collection, Arrays.asList(bulkUpdate), onSuccess(bulkResult -> {
//        assertEquals(5, bulkResult.getModifiedCount());
//        assertEquals(5, bulkResult.getMatchedCount());
//        assertEquals(0, bulkResult.getDeletedCount());
//        assertEquals(0, bulkResult.getInsertedCount());
//        mongoDatabase.find(collection, new JsonObject(), onSuccess(docs -> {
//          assertEquals(5, docs.size());
//          for (JsonObject foundDoc : docs) {
//            assertEquals("foobar-bulk", foundDoc.getString("foo"));
//          }
//          testComplete();
//        }));
//      }));
//    }));
//    await();
//  }
//
//  @Test
//  public void testBulkOperation_updateMultipleDocuments_multiFalse() {
//    String collection = randomCollection();
//    insertDocs(mongoDatabase, collection, 5, onSuccess(v -> {
//      JsonObject update = new JsonObject().put("$set", new JsonObject().put("foo", "foobar-bulk"));
//      JsonObject filter = new JsonObject();
//      BulkOperation bulkUpdate = BulkOperation.createUpdate(filter, update).setMulti(false);
//      mongoDatabase.bulkWrite(collection, Arrays.asList(bulkUpdate), onSuccess(bulkResult -> {
//        assertEquals(1, bulkResult.getModifiedCount());
//        assertEquals(0, bulkResult.getDeletedCount());
//        assertEquals(0, bulkResult.getInsertedCount());
//        assertEquals(1, bulkResult.getMatchedCount());
//        mongoDatabase.find(collection, new JsonObject().put("foo", "foobar-bulk"), onSuccess(docs -> {
//          assertEquals(1, docs.size());
//          testComplete();
//        }));
//      }));
//    }));
//    await();
//  }
//
//  @Test
//  public void testBulkOperation_upsertDocument() {
//    String collection = randomCollection();
//    JsonObject doc = new JsonObject().put("$set", new JsonObject().put("foo", "bar"));
//    BulkOperation bulkUpdate = BulkOperation.createUpdate(new JsonObject().put("foo", "bur"), doc)
//      .setUpsert(true);
//    mongoDatabase.bulkWrite(collection, Arrays.asList(bulkUpdate), onSuccess(bulkResult -> {
//      // even though one document was created, the MongoDB client returns 0 for all counts
//      assertEquals(0, bulkResult.getInsertedCount());
//      assertEquals(0, bulkResult.getDeletedCount());
//      assertEquals(0, bulkResult.getModifiedCount());
//      assertEquals(0, bulkResult.getMatchedCount());
//      List<JsonObject> upserts = bulkResult.getUpserts();
//      assertNotNull(upserts);
//      assertEquals(1, upserts.size());
//      JsonObject upsert = upserts.get(0);
//      assertEquals(0, upsert.getInteger(MongoClientBulkWriteResult.INDEX).intValue());
//      testComplete();
//    }));
//    await();
//  }
//
//  @Test
//  public void testBulkOperation_upsertDocument_upsertDisabled() {
//    String collection = randomCollection();
//    JsonObject doc = new JsonObject().put("$set", new JsonObject().put("foo", "bar"));
//    BulkOperation bulkUpdate = BulkOperation.createUpdate(new JsonObject().put("foo", "bur"), doc)
//      .setUpsert(false);
//    mongoDatabase.bulkWrite(collection, Arrays.asList(bulkUpdate), onSuccess(bulkResult -> {
//      assertEquals(0, bulkResult.getInsertedCount());
//      assertEquals(0, bulkResult.getModifiedCount());
//      assertEquals(0, bulkResult.getDeletedCount());
//      assertEquals(0, bulkResult.getMatchedCount());
//      assertEquals(0, bulkResult.getUpserts().size());
//      testComplete();
//    }));
//    await();
//  }
//
//  @Test
//  public void testBulkOperation_replace() {
//    String collection = randomCollection();
//    insertDocs(mongoDatabase, collection, 1, onSuccess(v -> {
//      JsonObject filter = new JsonObject().put("num", 123);
//      JsonObject replace = new JsonObject().put("foo", "replaced");
//      BulkOperation bulkReplace = BulkOperation.createReplace(filter, replace);
//      mongoDatabase.bulkWrite(collection, Arrays.asList(bulkReplace), onSuccess(bulkResult -> {
//        assertEquals(0, bulkResult.getInsertedCount());
//        assertEquals(1, bulkResult.getModifiedCount());
//        assertEquals(0, bulkResult.getDeletedCount());
//        assertEquals(1, bulkResult.getMatchedCount());
//        assertEquals(0, bulkResult.getUpserts().size());
//        mongoDatabase.find(collection, new JsonObject(), onSuccess(docs -> {
//          assertEquals(1, docs.size());
//          JsonObject foundDoc = docs.get(0);
//          assertEquals("replaced", foundDoc.getString("foo"));
//          assertNull(foundDoc.getInteger("num"));
//          testComplete();
//        }));
//      }));
//    }));
//    await();
//  }
//
//  @Test
//  public void testBulkOperation_replaceById() {
//    String collection = randomCollection();
//    mongoDatabase.createCollection(collection, onSuccess(res -> {
//      JsonObject doc = new JsonObject();
//      doc.put("foo", "bar");
//      doc.put("num", 123);
//      mongoDatabase.insert(collection, doc, onSuccess(id -> {
//        JsonObject filter = new JsonObject().put("_id", id);
//        JsonObject replace = new JsonObject().put("foo", "replaced");
//        BulkOperation bulkReplace = BulkOperation.createReplace(filter, replace);
//        mongoDatabase.bulkWrite(collection, Arrays.asList(bulkReplace), onSuccess(bulkResult -> {
//          assertEquals(0, bulkResult.getInsertedCount());
//          assertEquals(0, bulkResult.getDeletedCount());
//          assertEquals(1, bulkResult.getMatchedCount());
//          assertEquals(0, bulkResult.getUpserts().size());
//          mongoDatabase.find(collection, new JsonObject(), onSuccess(docs -> {
//            assertEquals(1, docs.size());
//            JsonObject foundDoc = docs.get(0);
//            assertEquals("replaced", foundDoc.getString("foo"));
//            assertNull(foundDoc.getInteger("num"));
//            testComplete();
//          }));
//        }));
//      }));
//    }));
//    await();
//  }
//
//  @Test
//  public void testBulkOperation_replaceOne_upsert() {
//    String collection = randomCollection();
//    JsonObject filter = new JsonObject().put("foo", "bar");
//    JsonObject replace = new JsonObject().put("foo", "upsert");
//    BulkOperation bulkReplace = BulkOperation.createReplace(filter, replace, true);
//    mongoDatabase.bulkWrite(collection, Arrays.asList(bulkReplace), onSuccess(bulkResult -> {
//      assertEquals(0, bulkResult.getInsertedCount());
//      assertEquals(0, bulkResult.getDeletedCount());
//      assertEquals(0, bulkResult.getModifiedCount());
//      assertEquals(0, bulkResult.getMatchedCount());
//      assertEquals(1, bulkResult.getUpserts().size());
//      assertEquals(0, (int) bulkResult.getUpserts().get(0).getInteger(MongoClientBulkWriteResult.INDEX));
//      mongoDatabase.find(collection, new JsonObject(), onSuccess(docs -> {
//        assertEquals(1, docs.size());
//        JsonObject foundDoc = docs.get(0);
//        assertEquals("upsert", foundDoc.getString("foo"));
//        testComplete();
//      }));
//    }));
//    await();
//  }
//
//  @Test
//  public void testBulkOperation_delete_multiDisabled() {
//    String collection = randomCollection();
//    insertDocs(mongoDatabase, collection, 5, onSuccess(v -> {
//      JsonObject filter = new JsonObject().put("num", 123);
//      BulkOperation bulkDelete = BulkOperation.createDelete(filter).setMulti(false);
//      mongoDatabase.bulkWrite(collection, Arrays.asList(bulkDelete), onSuccess(bulkResult -> {
//        assertEquals(0, bulkResult.getInsertedCount());
//        assertEquals(1, bulkResult.getDeletedCount());
//        assertEquals(0, bulkResult.getModifiedCount());
//        assertEquals(0, bulkResult.getMatchedCount());
//        assertEquals(0, bulkResult.getUpserts().size());
//        testComplete();
//      }));
//    }));
//    await();
//  }
//
//  @Test
//  public void testBulkOperation_delete_multiEnabled() {
//    String collection = randomCollection();
//    insertDocs(mongoDatabase, collection, 5, onSuccess(v -> {
//      JsonObject filter = new JsonObject().put("num", 123);
//      BulkOperation bulkDelete = BulkOperation.createDelete(filter).setMulti(true);
//      mongoDatabase.bulkWrite(collection, Arrays.asList(bulkDelete), onSuccess(bulkResult -> {
//        assertEquals(0, bulkResult.getInsertedCount());
//        assertEquals(5, bulkResult.getDeletedCount());
//        assertEquals(0, bulkResult.getModifiedCount());
//        assertEquals(0, bulkResult.getMatchedCount());
//        assertEquals(0, bulkResult.getUpserts().size());
//        testComplete();
//      }));
//    }));
//    await();
//  }
//
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
//    insertDocs(mongoDatabase, collection, 5, onSuccess(v -> {
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
