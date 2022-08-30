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

  public DeleteOptions() {
  }

  public DeleteOptions(JsonObject json) {
    DeleteOptionsConverter.fromJson(json, this);
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
   *  <p>Note: If {@link DeleteOptions#hint(JsonObject)} is set that will be used instead of any hint string.</p>
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
   * @return MongoDB driver object
   * @hidden
   */
  public com.mongodb.client.model.DeleteOptions toDriverClass() {
    com.mongodb.client.model.DeleteOptions result = new com.mongodb.client.model.DeleteOptions();
    if (this.collation != null) {
      result.collation(this.collation.toDriverClass());
    }
    if (this.hint != null) {
      result.hint(ConversionUtilsImpl.INSTANCE.toBson(this.hint));
    }
    if (this.hintString != null) {
      result.hintString(this.hintString);
    }
    return result;
  }
}
