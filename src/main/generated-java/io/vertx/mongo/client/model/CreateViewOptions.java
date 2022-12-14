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

/**
 *  Options for creating a view
 *
 *  @since 3.4
 *  @mongodb.server.release 3.4
 *  @mongodb.driver.manual reference/command/create Create Command
 */
@DataObject(
    generateConverter = true
)
public class CreateViewOptions {
  /**
   * the collation options to use
   */
  private Collation collation;

  public CreateViewOptions() {
  }

  public CreateViewOptions(JsonObject json) {
    CreateViewOptionsConverter.fromJson(json, this);
  }

  public JsonObject toJson() {
    JsonObject result = new JsonObject();
    CreateViewOptionsConverter.toJson(this, result);
    return result;
  }

  /**
   *  Sets the collation options
   *
   *  <p>A null value represents the server default.</p>
   *  @param collation the collation options to use
   *  @return this
   */
  public CreateViewOptions setCollation(Collation collation) {
    this.collation = collation;
    return this;
  }

  /**
   *  Returns the collation options
   *
   *  @return the collation options
   */
  public Collation getCollation() {
    return collation;
  }

  /**
   * @return MongoDB driver object
   * @hidden
   */
  public com.mongodb.client.model.CreateViewOptions toDriverClass(
      MongoClientContext clientContext) {
    com.mongodb.client.model.CreateViewOptions result = new com.mongodb.client.model.CreateViewOptions();
    if (this.collation != null) {
      result.collation(this.collation.toDriverClass(clientContext));
    }
    return result;
  }
}
