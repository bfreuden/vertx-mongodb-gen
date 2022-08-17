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
