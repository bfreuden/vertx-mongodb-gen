package io.vertx.mongo.client.model;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;
import io.vertx.mongo.impl.ConversionUtilsImpl;

/**
 *  The default options for a collection to apply on the creation of indexes.
 *
 *  @since 3.2
 *  @mongodb.driver.manual reference/method/db.createCollection/ Create Collection
 *  @mongodb.driver.manual reference/command/createIndexes Index options
 *  @mongodb.server.release 3.2
 */
@DataObject(
    generateConverter = true
)
public class IndexOptionDefaults {
  /**
   * the storage engine options
   */
  private JsonObject storageEngine;

  /**
   *  Sets the default storage engine options document for indexes.
   *
   *  @param storageEngine the storage engine options
   *  @return this
   */
  public IndexOptionDefaults storageEngine(JsonObject storageEngine) {
    return this;
  }

  /**
   *  Gets the default storage engine options document for indexes.
   *
   *  @return the storage engine options
   */
  public JsonObject getStorageEngine() {
    return storageEngine;
  }

  /**
   * @return MongoDB driver object
   * @hidden
   */
  public com.mongodb.client.model.IndexOptionDefaults toDriverClass() {
    com.mongodb.client.model.IndexOptionDefaults result = new com.mongodb.client.model.IndexOptionDefaults();
    if (this.storageEngine != null) {
      result.storageEngine(ConversionUtilsImpl.INSTANCE.toBson(this.storageEngine));
    }
    return result;
  }
}
