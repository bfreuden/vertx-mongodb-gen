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
import io.vertx.mongo.impl.MongoClientContext;
import java.lang.Boolean;
import java.lang.String;

/**
 *  Options for cluster index on a collection.
 *
 *  @see CreateCollectionOptions
 *  @since 4.7
 *  @mongodb.server.release 5.3
 */
@DataObject(
    generateConverter = true
)
public class ClusteredIndexOptions {
  private JsonObject key;

  private Boolean unique;

  /**
   * the index name
   */
  private String name;

  public ClusteredIndexOptions() {
  }

  public ClusteredIndexOptions(JsonObject json) {
    ClusteredIndexOptionsConverter.fromJson(json, this);
  }

  public JsonObject toJson() {
    JsonObject result = new JsonObject();
    ClusteredIndexOptionsConverter.toJson(this, result);
    return result;
  }

  /**
   *  Gets the index key.
   *
   *  @return the index key
   */
  public JsonObject getKey() {
    return key;
  }

  /**
   *  Gets whether the index entries must be unique
   *  @return whether the index entries must be unique
   */
  public Boolean isUnique() {
    return unique;
  }

  /**
   *  Sets the index name
   *  @param name the index name
   *  @return this
   */
  public ClusteredIndexOptions setName(String name) {
    this.name = name;
    return this;
  }

  /**
   *  Gets the index name
   *
   *  @return the index name
   */
  public String getName() {
    return name;
  }

  /**
   * @return MongoDB driver object
   * @hidden
   */
  public com.mongodb.client.model.ClusteredIndexOptions toDriverClass(
      MongoClientContext clientContext) {
    if (this.key == null) {
      throw new IllegalArgumentException("key is mandatory");
    }
    if (this.unique == null) {
      throw new IllegalArgumentException("unique is mandatory");
    }
    com.mongodb.client.model.ClusteredIndexOptions result = new com.mongodb.client.model.ClusteredIndexOptions(this.key, this.unique);
    if (this.name != null) {
      result.name(this.name);
    }
    return result;
  }
}
