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

/**
 *  Options for change stream pre- and post- images.
 *
 *  @see CreateCollectionOptions
 *  @since 4.7
 *  @mongodb.driver.manual reference/command/create/ Create Collection
 */
@DataObject(
    generateConverter = true
)
public class ChangeStreamPreAndPostImagesOptions {
  private Boolean enabled;

  public ChangeStreamPreAndPostImagesOptions() {
  }

  public ChangeStreamPreAndPostImagesOptions(JsonObject json) {
    ChangeStreamPreAndPostImagesOptionsConverter.fromJson(json, this);
  }

  public JsonObject toJson() {
    JsonObject result = new JsonObject();
    ChangeStreamPreAndPostImagesOptionsConverter.toJson(this, result);
    return result;
  }

  /**
   *  Gets whether change stream pre- and post- images are enabled for the collection.
   *
   *  @return whether change stream pre- and post- images are enabled for the collection
   */
  public Boolean isEnabled() {
    return enabled;
  }

  /**
   * @return MongoDB driver object
   * @hidden
   */
  public com.mongodb.client.model.ChangeStreamPreAndPostImagesOptions toDriverClass(
      MongoClientContext clientContext) {
    if (this.enabled == null) {
      throw new IllegalArgumentException("enabled is mandatory");
    }
    com.mongodb.client.model.ChangeStreamPreAndPostImagesOptions result = new com.mongodb.client.model.ChangeStreamPreAndPostImagesOptions(this.enabled);
    return result;
  }
}
