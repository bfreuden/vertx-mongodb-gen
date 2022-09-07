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
import java.lang.String;

/**
 *  The options to apply when replacing documents.
 *
 *  @since 3.7
 *  @mongodb.driver.manual tutorial/modify-documents/ Updates
 *  @mongodb.driver.manual reference/operator/update/ Update Operators
 *  @mongodb.driver.manual reference/command/update/ Update Command
 */
@DataObject(
    generateConverter = true
)
public class ReplaceOptions {
  /**
   * true if a new document should be inserted if there are no matches to the query filter
   */
  private Boolean upsert;

  /**
   * If true, allows the write to opt-out of document level validation.
   */
  private Boolean bypassDocumentValidation;

  /**
   * the collation options to use
   */
  private Collation collation;

  /**
   * the hint
   */
  private JsonObject hint;

  /**
   * the name of the index which should be used for the operation
   */
  private String hintString;

  /**
   * the comment
   */
  private Object comment;

  /**
   * for the operation or null
   */
  private JsonObject let;

  public ReplaceOptions() {
  }

  public ReplaceOptions(JsonObject json) {
    ReplaceOptionsConverter.fromJson(json, this);
  }

  public JsonObject toJson() {
    JsonObject result = new JsonObject();
    ReplaceOptionsConverter.toJson(this, result);
    return result;
  }

  /**
   *  Set to true if a new document should be inserted if there are no matches to the query filter.
   *
   *  @param upsert true if a new document should be inserted if there are no matches to the query filter
   *  @return this
   */
  public ReplaceOptions setUpsert(Boolean upsert) {
    this.upsert = upsert;
    return this;
  }

  /**
   *  Returns true if a new document should be inserted if there are no matches to the query filter.  The default is false.
   *
   *  @return true if a new document should be inserted if there are no matches to the query filter
   */
  public Boolean isUpsert() {
    return upsert;
  }

  /**
   *  Sets the bypass document level validation flag.
   *
   *  <p>For bulk operations use: {@link BulkWriteOptions#bypassDocumentValidation(Boolean)}</p>
   *
   *  @param bypassDocumentValidation If true, allows the write to opt-out of document level validation.
   *  @return this
   *  @mongodb.server.release 3.2
   */
  public ReplaceOptions setBypassDocumentValidation(Boolean bypassDocumentValidation) {
    this.bypassDocumentValidation = bypassDocumentValidation;
    return this;
  }

  /**
   *  Gets the bypass document level validation flag
   *
   *  @return the bypass document level validation flag
   *  @mongodb.server.release 3.2
   */
  public Boolean isBypassDocumentValidation() {
    return bypassDocumentValidation;
  }

  /**
   *  Sets the collation options
   *
   *  <p>A null value represents the server default.</p>
   *  @param collation the collation options to use
   *  @return this
   *  @mongodb.server.release 3.4
   */
  public ReplaceOptions setCollation(Collation collation) {
    this.collation = collation;
    return this;
  }

  /**
   *  Returns the collation options
   *
   *  @return the collation options
   *  @mongodb.server.release 3.4
   */
  public Collation getCollation() {
    return collation;
  }

  /**
   *  Sets the hint for which index to use. A null value means no hint is set.
   *
   *  @param hint the hint
   *  @return this
   *  @since 4.1
   */
  public ReplaceOptions setHint(JsonObject hint) {
    this.hint = hint;
    return this;
  }

  /**
   *  Returns the hint for which index to use. The default is not to set a hint.
   *
   *  @return the hint
   *  @since 4.1
   */
  public JsonObject getHint() {
    return hint;
  }

  /**
   *  Sets the hint to apply.
   *
   *  @param hint the name of the index which should be used for the operation
   *  @return this
   *  @since 4.1
   */
  public ReplaceOptions setHintString(String hint) {
    this.hintString = hint;
    return this;
  }

  /**
   *  Gets the hint string to apply.
   *
   *  @return the hint string, which should be the name of an existing index
   *  @since 4.1
   */
  public String getHintString() {
    return hintString;
  }

  /**
   *  Sets the comment for this operation. A null value means no comment is set.
   *
   *  <p>For bulk operations use: {@link BulkWriteOptions#comment(BsonValue)}</p>
   *
   *  @param comment the comment
   *  @return this
   *  @since 4.6
   *  @mongodb.server.release 4.4
   */
  public ReplaceOptions setComment(Object comment) {
    this.comment = comment;
    return this;
  }

  /**
   *  @return the comment for this operation. A null value means no comment is set.
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
   *  <p>For bulk operations use: {@link BulkWriteOptions#let(Bson)}
   *
   *  @param variables for the operation or null
   *  @return this
   *  @mongodb.server.release 5.0
   *  @since 4.6
   */
  public ReplaceOptions setLet(JsonObject variables) {
    this.let = variables;
    return this;
  }

  /**
   *  Add top-level variables to the operation
   *
   *  <p>The value of let will be passed to all update and delete, but not insert, commands.
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
  public com.mongodb.client.model.ReplaceOptions toDriverClass(MongoClientContext clientContext) {
    com.mongodb.client.model.ReplaceOptions result = new com.mongodb.client.model.ReplaceOptions();
    if (this.upsert != null) {
      result.upsert(this.upsert);
    }
    if (this.bypassDocumentValidation != null) {
      result.bypassDocumentValidation(this.bypassDocumentValidation);
    }
    if (this.collation != null) {
      result.collation(this.collation.toDriverClass(clientContext));
    }
    if (this.hint != null) {
      result.hint(clientContext.getMapper().toBson(this.hint));
    }
    if (this.hintString != null) {
      result.hintString(this.hintString);
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
