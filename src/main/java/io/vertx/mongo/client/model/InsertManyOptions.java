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
import java.lang.Boolean;

/**
 *  The options to apply to an operation that inserts multiple documents into a collection.
 *
 *  @since 3.0
 *  @mongodb.driver.manual tutorial/insert-documents/ Insert Tutorial
 *  @mongodb.driver.manual reference/command/insert/ Insert Command
 */
@DataObject(
    generateConverter = true
)
public class InsertManyOptions {
  /**
   * true if documents should be inserted in order
   */
  private Boolean ordered;

  /**
   * If true, allows the write to opt-out of document level validation.
   */
  private Boolean bypassDocumentValidation;

  public InsertManyOptions() {
  }

  public InsertManyOptions(JsonObject json) {
    InsertManyOptionsConverter.fromJson(json, this);
  }

  public JsonObject toJson() {
    JsonObject result = new JsonObject();
    InsertManyOptionsConverter.toJson(this, result);
    return result;
  }

  /**
   *  Sets whether the server should insert the documents in the order provided.
   *
   *  @param ordered true if documents should be inserted in order
   *  @return this
   */
  public InsertManyOptions setOrdered(Boolean ordered) {
    this.ordered = ordered;
    return this;
  }

  /**
   *  Gets whether the documents should be inserted in the order provided, stopping on the first failed insertion. The default is true.
   *  If false, the server will attempt to insert all the documents regardless of an failures.
   *
   *  @return whether the the documents should be inserted in order
   */
  public Boolean isOrdered() {
    return ordered;
  }

  /**
   *  Sets the bypass document level validation flag.
   *
   *  @param bypassDocumentValidation If true, allows the write to opt-out of document level validation.
   *  @return this
   *  @since 3.2
   *  @mongodb.server.release 3.2
   */
  public InsertManyOptions setBypassDocumentValidation(Boolean bypassDocumentValidation) {
    this.bypassDocumentValidation = bypassDocumentValidation;
    return this;
  }

  /**
   *  Gets the the bypass document level validation flag
   *
   *  @return the bypass document level validation flag
   *  @since 3.2
   *  @mongodb.server.release 3.2
   */
  public Boolean isBypassDocumentValidation() {
    return bypassDocumentValidation;
  }

  /**
   * @return MongoDB driver object
   * @hidden
   */
  public com.mongodb.client.model.InsertManyOptions toDriverClass() {
    com.mongodb.client.model.InsertManyOptions result = new com.mongodb.client.model.InsertManyOptions();
    if (this.ordered != null) {
      result.ordered(this.ordered);
    }
    if (this.bypassDocumentValidation != null) {
      result.bypassDocumentValidation(this.bypassDocumentValidation);
    }
    return result;
  }
}
