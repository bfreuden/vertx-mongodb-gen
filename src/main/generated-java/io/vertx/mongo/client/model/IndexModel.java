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

import io.vertx.core.json.JsonObject;
import io.vertx.mongo.impl.MongoClientContext;
import org.bson.conversions.Bson;

public class IndexModel {
  private JsonObject keys;

  private IndexOptions options;

  int __ctorIndex;

  /**
   *  Construct an instance with the given keys.
   *
   *  @param keys the index keys
   */
  public IndexModel(JsonObject keys) {
    __ctorIndex = 0;
    this.keys = keys;
  }

  /**
   *  Construct an instance with the given keys and options.
   *
   *  @param keys the index keys
   *  @param options the index options
   */
  public IndexModel(JsonObject keys, IndexOptions options) {
    __ctorIndex = 1;
    this.keys = keys;
    this.options = options;
  }

  /**
   *  Gets the index keys.
   *
   *  @return the index keys
   */
  public JsonObject getKeys() {
    return keys;
  }

  /**
   *  Gets the index options.
   *
   *  @return the index options
   */
  public IndexOptions getOptions() {
    return options;
  }

  /**
   * @return MongoDB driver object
   * @hidden
   */
  public com.mongodb.client.model.IndexModel toDriverClass(MongoClientContext clientContext) {
    if (__ctorIndex == 0) {
      Bson __keys = clientContext.getMapper().toBson(this.keys);
      return new com.mongodb.client.model.IndexModel(__keys);
    } else if (__ctorIndex == 1) {
      Bson __keys = clientContext.getMapper().toBson(this.keys);
      com.mongodb.client.model.IndexOptions __options = this.options.toDriverClass(clientContext);
      return new com.mongodb.client.model.IndexModel(__keys, __options);
    } else {
      throw new IllegalArgumentException("unknown constructor");
    }
  }
}
