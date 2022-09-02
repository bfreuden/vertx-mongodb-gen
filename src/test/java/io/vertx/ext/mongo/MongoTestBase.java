/*
 * Copyright 2014 Red Hat, Inc.
 *
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  and Apache License v2.0 which accompanies this distribution.
 *
 *  The Eclipse Public License is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *
 *  The Apache License v2.0 is available at
 *  http://www.opensource.org/licenses/apache2.0.php
 *
 *  You may elect to redistribute this code under either of these licenses.
 */

package io.vertx.ext.mongo;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.mongo.client.ClientConfig;
import io.vertx.mongo.client.MongoClient;
import io.vertx.mongo.client.MongoCollection;
import io.vertx.mongo.client.MongoDatabase;
import io.vertx.test.core.TestUtils;
import io.vertx.test.core.VertxTestBase;
import org.bson.types.ObjectId;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.utility.DockerImageName;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

/**
 * @author <a href="http://tfox.org">Tim Fox</a>
 */
public abstract class MongoTestBase extends VertxTestBase {

  protected boolean useObjectId;//since this class will be inherited by other tests, some tests will toggle useObjectId in their client config. This will keep trakc of it and run the affected test accordingly.

  protected static String getConnectionString() {
    return getProperty("connection_string");
  }

  protected static String getDatabaseName() {
    String dbName = getProperty("db_name");
    return dbName != null ? dbName : "DEFAULT_DB";
  }

  protected static String getProperty(String name) {
    String s = System.getProperty(name);
    if (s != null) {
      s = s.trim();
      if (s.length() > 0) {
        return s;
      }
    }

    return null;
  }

  private static MongoDBContainer mongoDBContainer;

  @BeforeClass
  public static void startMongo() {
    int port = 27018;
    mongoDBContainer = new MongoDBContainer(DockerImageName.parse("mongo:4.2.1"));
    mongoDBContainer.setPortBindings(Collections.singletonList(port + ":27017"));
    mongoDBContainer.start();
  }

  @AfterClass
  public static void stopMongo() {
    mongoDBContainer.stop();
  }

  protected static ClientConfig getConfig() {
    ClientConfig config = new ClientConfig();
    String connectionString = getConnectionString();
    if (connectionString != null) {
      config.setConnectionString(connectionString);
    } else {
      config.setConnectionString("mongodb://localhost:27018");
    }
    return config;
  }

  protected void dropCollections(MongoDatabase database, CountDownLatch latch) {
    // Drop all the collections in the db
    database.listCollectionNames().all().onSuccess(list -> {
      AtomicInteger collCount = new AtomicInteger();
      List<String> toDrop = getOurCollections(list);
      int count = toDrop.size();
      if (!toDrop.isEmpty()) {
        for (String collection : toDrop) {
          database.getCollection(collection).drop(onSuccess(v -> {
            if (collCount.incrementAndGet() == count) {
              latch.countDown();
            }
          }));
        }
      } else {
        latch.countDown();
      }
    });
  }

  protected List<String> getOurCollections(List<String> colls) {
    List<String> ours = new ArrayList<>();
    for (String coll : colls) {
      if (coll.startsWith("ext-mongo")) {
        ours.add(coll);
      }
    }
    return ours;
  }

  protected String randomCollection() {
    return "ext-mongo" + TestUtils.randomAlphaString(20);
  }

  protected void insertDocs(MongoClient mongoClient, String collection, int num, Handler<AsyncResult<Void>> resultHandler) {
    insertDocs(mongoClient, collection, num, this::createDoc, resultHandler);
  }
  protected void insertDocs(MongoClient mongoClient, String collection, int num, Function<Integer, JsonObject> docSupplier, Handler<AsyncResult<Void>> resultHandler) {
    if (num != 0) {
      AtomicInteger cnt = new AtomicInteger();
      MongoCollection<JsonObject> coll = mongoClient.getDatabase(getDatabaseName()).getCollection(collection);
      for (int i = 0; i < num; i++) {
        coll.insertOne(docSupplier.apply(i), ar -> {
          if (ar.succeeded()) {
            if (cnt.incrementAndGet() == num) {
              resultHandler.handle(Future.succeededFuture());
            }
          } else {
            resultHandler.handle(Future.failedFuture(ar.cause()));
          }
        });
      }
    } else {
      resultHandler.handle(Future.succeededFuture());
    }
  }

  protected JsonObject createDoc() {
    String hexString = new ObjectId().toHexString();
    return new JsonObject()
      .put("foo", "bar")
      .put("num", 123)
      .put("big", true)
      .putNull("nullentry")
      .put("bigDec", BigDecimal.ONE)
      .put("arr", new JsonArray()
        .add("x")
        .add(true)
        .add(12)
        .add(1.23)
        .addNull()
        .add(BigDecimal.ONE)
        .add(new JsonObject()
          .put("wib", "wob")))
      .put("date", new JsonObject()
        .put("$date", "2015-05-30T22:50:02Z"))
      .put("object_id", useObjectId ? new JsonObject()
        .put("$oid", hexString) : hexString) // FIXME? small variation with previous client
      .put("other", new JsonObject()
        .put("quux", "flib")
        .put("myarr", new JsonArray()
          .add("blah")
          .add(true)
          .add(312)));
  }

  protected JsonObject createDoc(int num) {
    return new JsonObject().put("foo", "bar" + (num != -1 ? num : "")).put("num", 123).put("big", true).putNull("nullentry").
      put("counter", num).
      put("arr", new JsonArray().add("x").add(true).add(12).add(1.23).addNull().add(new JsonObject().put("wib", "wob"))).
      put("date", new JsonObject().put("$date", "2015-05-30T22:50:02Z")).
      put("object_id", new JsonObject().put("$oid", new ObjectId().toHexString())).
      put("other", new JsonObject().put("quux", "flib").put("myarr",
        new JsonArray().add("blah").add(true).add(312))).
      put("longval", 123456789L).put("dblval", 1.23);
  }

  protected JsonObject createDocWithAmbiguitiesDependingOnLocale(int num) {
    return new JsonObject()
      .put("foo", num % 2 == 0 ? "bar" : "bär")
      .put("num", 123).put("big", true)
      .putNull("nullentry")
      .put("counter", num)
      .put("arr", new JsonArray().add("x").add(true).add(12).add(1.23).addNull().add(new JsonObject().put("wib", "wob")))
      .put("date", new JsonObject().put("$date", "2015-05-30T22:50:02Z"))
      .put("object_id", new JsonObject().put("$oid", new ObjectId().toHexString()))
      .put("other", new JsonObject().put("quux", "flib")
      .put("myarr", new JsonArray().add("blah").add(true).add(312)))
      .put("longval", 123456789L).put("dblval", 1.23);
  }

}
