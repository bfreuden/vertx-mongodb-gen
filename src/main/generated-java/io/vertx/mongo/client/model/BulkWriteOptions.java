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
import java.lang.Object;

/**
 *  The options to apply to a bulk write.
 *
 *  @since 3.0
 */
@DataObject(
    generateConverter = true
)
public class BulkWriteOptions {
  /**
   * true if the writes should be ordered
   */
  private Boolean ordered;

  /**
   * If true, allows the write to opt-out of document level validation.
   */
  private Boolean bypassDocumentValidation;

  /**
   * the comment
   */
  private Object comment;

  /**
   * for the operation or null
   */
  private JsonObject let;

  public BulkWriteOptions() {
  }

  public BulkWriteOptions(JsonObject json) {
    BulkWriteOptionsConverter.fromJson(json, this);
  }

  public JsonObject toJson() {
    JsonObject result = new JsonObject();
    BulkWriteOptionsConverter.toJson(this, result);
    return result;
  }

  /**
   *  If true, then when a write fails, return without performing the remaining
   *  writes. If false, then when a write fails, continue with the remaining writes, if any.
   *  Defaults to true.
   *
   *  @param ordered true if the writes should be ordered
   *  @return this
   */
  public BulkWriteOptions setOrdered(Boolean ordered) {
    this.ordered = ordered;
    return this;
  }

  /**
   *  If true, then when a write fails, return without performing the remaining
   *  writes. If false, then when a write fails, continue with the remaining writes, if any.
   *  Defaults to true.
   *
   *  @return true if the writes are ordered
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
  public BulkWriteOptions setBypassDocumentValidation(Boolean bypassDocumentValidation) {
    this.bypassDocumentValidation = bypassDocumentValidation;
    return this;
  }

  /**
   *  Gets the bypass document level validation flag
   *
   *  @return the bypass document level validation flag
   *  @since 3.2
   *  @mongodb.server.release 3.2
   */
  public Boolean isBypassDocumentValidation() {
    return bypassDocumentValidation;
  }

  /**
   *  Sets the comment for this operation. A null value means no comment is set.
   *
   *  @param comment the comment
   *  @return this
   *  @since 4.6
   *  @mongodb.server.release 4.4
   */
  public BulkWriteOptions setComment(Object comment) {
    this.comment = comment;
    return this;
  }

  /**
   *  Returns the comment to send with the query. The default is not to include a comment with the query.
   *
   *  @return the comment
   *  @since 4.6
   *  @mongodb.server.release 4.4
   */
  public Object getComment() {
    return comment;
  }

  /**
   *  Add top-level variables for the operation
   *
   *  <p>Allows for improved command readability by separating the variables from the query text.
   *  The value of let will be passed to all update and delete, but not insert, commands.
   *
   *  @param variables for the operation or null
   *  @return this
   *  @mongodb.server.release 5.0
   *  @since 4.6
   */
  public BulkWriteOptions setLet(JsonObject variables) {
    this.let = variables;
    return this;
  }

  /**
   *  Add top-level variables to the operation
   *
   *  @return the top level variables if set or null.
   *  @mongodb.server.release 5.0
   *  @since 4.6
   */
  public JsonObject getLet() {
    return let;
  }

  /**
   * @return MongoDB driver object
   * @hidden
   */
  public com.mongodb.client.model.BulkWriteOptions toDriverClass(MongoClientContext clientContext) {
    com.mongodb.client.model.BulkWriteOptions result = new com.mongodb.client.model.BulkWriteOptions();
    if (this.ordered != null) {
      result.ordered(this.ordered);
    }
    if (this.bypassDocumentValidation != null) {
      result.bypassDocumentValidation(this.bypassDocumentValidation);
    }
    if (this.comment != null) {
      result.comment(clientContext.getMapper().toBsonValue(this.comment));
    }
    if (this.let != null) {
      result.let(clientContext.getMapper().toBson(this.let));
    }
    return result;
  }
}
