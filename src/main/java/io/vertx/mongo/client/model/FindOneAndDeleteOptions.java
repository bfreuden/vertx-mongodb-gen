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
import java.lang.Long;
import java.lang.String;
import java.util.concurrent.TimeUnit;

/**
 *  The options to apply to an operation that atomically finds a document and deletes it.
 *
 *  @since 3.0
 *  @mongodb.driver.manual reference/command/findAndModify/
 */
@DataObject(
    generateConverter = true
)
public class FindOneAndDeleteOptions {
  /**
   * the project document, which may be null.
   */
  private JsonObject projection;

  /**
   * the sort criteria, which may be null.
   */
  private JsonObject sort;

  /**
   *  the max time
   */
  private Long maxTime;

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
   *  Sets a document describing the fields to return for all matching documents.
   *
   *  @param projection the project document, which may be null.
   *  @return this
   *  @mongodb.driver.manual tutorial/project-fields-from-query-results Projection
   */
  public FindOneAndDeleteOptions setProjection(JsonObject projection) {
    this.projection = projection;
    return this;
  }

  /**
   *  Gets a document describing the fields to return for all matching documents.
   *
   *  @return the project document, which may be null
   *  @mongodb.driver.manual tutorial/project-fields-from-query-results Projection
   */
  public JsonObject getProjection() {
    return projection;
  }

  /**
   *  Sets the sort criteria to apply to the query.
   *
   *  @param sort the sort criteria, which may be null.
   *  @return this
   *  @mongodb.driver.manual reference/method/cursor.sort/ Sort
   */
  public FindOneAndDeleteOptions setSort(JsonObject sort) {
    this.sort = sort;
    return this;
  }

  /**
   *  Gets the sort criteria to apply to the query. The default is null, which means that the documents will be returned in an undefined
   *  order.
   *
   *  @return a document describing the sort criteria
   *  @mongodb.driver.manual reference/method/cursor.sort/ Sort
   */
  public JsonObject getSort() {
    return sort;
  }

  /**
   *  Sets the maximum execution time on the server for this operation.
   *
   *  @param maxTime  the max time (in milliseconds)
   *  @return this
   */
  public FindOneAndDeleteOptions setMaxTime(Long maxTime) {
    this.maxTime = maxTime;
    return this;
  }

  /**
   *  Gets the maximum execution time for the find one and delete operation.
   *
   *  @return the max time
   */
  public Long getMaxTime() {
    return maxTime;
  }

  /**
   *  Sets the collation options
   *
   *  <p>A null value represents the server default.</p>
   *  @param collation the collation options to use
   *  @return this
   *  @since 3.4
   *  @mongodb.server.release 3.4
   */
  public FindOneAndDeleteOptions setCollation(Collation collation) {
    this.collation = collation;
    return this;
  }

  /**
   *  Returns the collation options
   *
   *  @return the collation options
   *  @since 3.4
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
  public FindOneAndDeleteOptions setHint(JsonObject hint) {
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
   *  <p>Note: If {@link FindOneAndDeleteOptions#hint(JsonObject)} is set that will be used instead of any hint string.</p>
   *
   *  @param hint the name of the index which should be used for the operation
   *  @return this
   *  @since 4.1
   *  @mongodb.server.release 4.4
   */
  public FindOneAndDeleteOptions setHintString(String hint) {
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
  public com.mongodb.client.model.FindOneAndDeleteOptions toDriverClass() {
    com.mongodb.client.model.FindOneAndDeleteOptions result = new com.mongodb.client.model.FindOneAndDeleteOptions();
    if (this.projection != null) {
      result.projection(ConversionUtilsImpl.INSTANCE.toBson(this.projection));
    }
    if (this.sort != null) {
      result.sort(ConversionUtilsImpl.INSTANCE.toBson(this.sort));
    }
    if (this.maxTime != null) {
      result.maxTime(this.maxTime, TimeUnit.MILLISECONDS);
    }
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
