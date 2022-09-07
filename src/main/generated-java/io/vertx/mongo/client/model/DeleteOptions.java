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
import java.lang.Object;
import java.lang.String;

/**
 *  The options to apply when deleting documents.
 *
 *  @since 3.4
 *  @mongodb.driver.manual tutorial/remove-documents/ Remove documents
 *  @mongodb.driver.manual reference/command/delete/ Delete Command
 */
@DataObject(
    generateConverter = true
)
public class DeleteOptions {
  /**
   * the collation options to use
   */
  private Collation collation;

  /**
   * a document describing the index which should be used for this operation.
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

  public DeleteOptions() {
  }

  public DeleteOptions(JsonObject json) {
    DeleteOptionsConverter.fromJson(json, this);
  }

  public JsonObject toJson() {
    JsonObject result = new JsonObject();
    DeleteOptionsConverter.toJson(this, result);
    return result;
  }

  /**
   *  Sets the collation options
   *
   *  <p>A null value represents the server default.</p>
   *  @param collation the collation options to use
   *  @return this
   *  @mongodb.server.release 3.4
   */
  public DeleteOptions setCollation(Collation collation) {
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
   *  Sets the hint to apply.
   *
   *  @param hint a document describing the index which should be used for this operation.
   *  @return this
   *  @since 4.1
   *  @mongodb.server.release 4.4
   */
  public DeleteOptions setHint(JsonObject hint) {
    this.hint = hint;
    return this;
  }

  /**
   *  Gets the hint to apply.
   *
   *  @return the hint, which should describe an existing index
   *  @since 4.1
   *  @mongodb.server.release 4.4
   */
  public JsonObject getHint() {
    return hint;
  }

  /**
   *  Sets the hint to apply.
   *
   *  <p>Note: If {@link DeleteOptions#setHint(JsonObject)} is set that will be used instead of any hint string.</p>
   *
   *  @param hint the name of the index which should be used for the operation
   *  @return this
   *  @since 4.1
   *  @mongodb.server.release 4.4
   */
  public DeleteOptions setHintString(String hint) {
    this.hintString = hint;
    return this;
  }

  /**
   *  Gets the hint string to apply.
   *
   *  @return the hint string, which should be the name of an existing index
   *  @since 4.1
   *  @mongodb.server.release 4.4
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
  public DeleteOptions setComment(Object comment) {
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
  public DeleteOptions setLet(JsonObject variables) {
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
  public com.mongodb.client.model.DeleteOptions toDriverClass(MongoClientContext clientContext) {
    com.mongodb.client.model.DeleteOptions result = new com.mongodb.client.model.DeleteOptions();
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
