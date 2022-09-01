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

import com.mongodb.client.model.ReturnDocument;
import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.mongo.impl.MongoClientContext;
import java.lang.Boolean;
import java.lang.Long;
import java.lang.String;
import java.util.concurrent.TimeUnit;

/**
 *  The options to apply to an operation that atomically finds a document and updates it.
 *
 *  @since 3.0
 *  @mongodb.driver.manual reference/command/findAndModify/
 */
@DataObject(
    generateConverter = true
)
public class FindOneAndUpdateOptions {
  /**
   * the project document, which may be null.
   */
  private JsonObject projection;

  /**
   * the sort criteria, which may be null.
   */
  private JsonObject sort;

  /**
   * true if a new document should be inserted if there are no matches to the query filter
   */
  private Boolean upsert;

  /**
   * set whether to return the document before it was updated / inserted or after
   */
  private ReturnDocument returnDocument;

  /**
   *  the max time
   */
  private Long maxTime;

  /**
   * If true, allows the write to opt-out of document level validation.
   */
  private Boolean bypassDocumentValidation;

  /**
   * the collation options to use
   */
  private Collation collation;

  /**
   * the array filters, which may be null
   */
  private JsonArray arrayFilters;

  /**
   * the hint
   */
  private JsonObject hint;

  /**
   * the name of the index which should be used for the operation
   */
  private String hintString;

  public FindOneAndUpdateOptions() {
  }

  public FindOneAndUpdateOptions(JsonObject json) {
    FindOneAndUpdateOptionsConverter.fromJson(json, this);
  }

  public JsonObject toJson() {
    JsonObject result = new JsonObject();
    FindOneAndUpdateOptionsConverter.toJson(this, result);
    return result;
  }

  /**
   *  Sets a document describing the fields to return for all matching documents.
   *
   *  @param projection the project document, which may be null.
   *  @return this
   *  @mongodb.driver.manual tutorial/project-fields-from-query-results Projection
   */
  public FindOneAndUpdateOptions setProjection(JsonObject projection) {
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
  public FindOneAndUpdateOptions setSort(JsonObject sort) {
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
   *  Set to true if a new document should be inserted if there are no matches to the query filter.
   *
   *  @param upsert true if a new document should be inserted if there are no matches to the query filter
   *  @return this
   */
  public FindOneAndUpdateOptions setUpsert(Boolean upsert) {
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
   *  Set whether to return the document before it was updated / inserted or after
   *
   *  @param returnDocument set whether to return the document before it was updated / inserted or after
   *  @return this
   */
  public FindOneAndUpdateOptions setReturnDocument(ReturnDocument returnDocument) {
    this.returnDocument = returnDocument;
    return this;
  }

  /**
   *  Gets the {@link ReturnDocument} value indicating whether to return the document before it was updated / inserted or after
   *
   *  @return {@link ReturnDocument#BEFORE} if returning the document before it was updated or inserted otherwise
   *  returns {@link ReturnDocument#AFTER}
   */
  public ReturnDocument getReturnDocument() {
    return returnDocument;
  }

  /**
   *  Sets the maximum execution time on the server for this operation.
   *
   *  @param maxTime  the max time (in milliseconds)
   *  @return this
   */
  public FindOneAndUpdateOptions setMaxTime(Long maxTime) {
    this.maxTime = maxTime;
    return this;
  }

  /**
   *  Gets the maximum execution time for the find one and update operation.
   *
   *  @return the max time
   */
  public Long getMaxTime() {
    return maxTime;
  }

  /**
   *  Sets the bypass document level validation flag.
   *
   *  @param bypassDocumentValidation If true, allows the write to opt-out of document level validation.
   *  @return this
   *  @since 3.2
   *  @mongodb.server.release 3.2
   */
  public FindOneAndUpdateOptions setBypassDocumentValidation(Boolean bypassDocumentValidation) {
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
   *  Sets the collation options
   *
   *  <p>A null value represents the server default.</p>
   *  @param collation the collation options to use
   *  @return this
   *  @since 3.4
   *  @mongodb.server.release 3.4
   */
  public FindOneAndUpdateOptions setCollation(Collation collation) {
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
   *  Sets the array filters option
   *
   *  @param arrayFilters the array filters, which may be null
   *  @return this
   *  @since 3.6
   *  @mongodb.server.release 3.6
   */
  public FindOneAndUpdateOptions setArrayFilters(JsonArray arrayFilters) {
    this.arrayFilters = arrayFilters;
    return this;
  }

  /**
   *  Returns the array filters option
   *
   *  @return the array filters, which may be null
   *  @since 3.6
   *  @mongodb.server.release 3.6
   */
  public JsonArray getArrayFilters() {
    return arrayFilters;
  }

  /**
   *  Sets the hint for which index to use. A null value means no hint is set.
   *
   *  @param hint the hint
   *  @return this
   *  @since 4.1
   */
  public FindOneAndUpdateOptions setHint(JsonObject hint) {
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
  public FindOneAndUpdateOptions setHintString(String hint) {
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
   * @return MongoDB driver object
   * @hidden
   */
  public com.mongodb.client.model.FindOneAndUpdateOptions toDriverClass(
      MongoClientContext clientContext) {
    com.mongodb.client.model.FindOneAndUpdateOptions result = new com.mongodb.client.model.FindOneAndUpdateOptions();
    if (this.projection != null) {
      result.projection(clientContext.getMapper().toBson(this.projection));
    }
    if (this.sort != null) {
      result.sort(clientContext.getMapper().toBson(this.sort));
    }
    if (this.upsert != null) {
      result.upsert(this.upsert);
    }
    if (this.returnDocument != null) {
      result.returnDocument(this.returnDocument);
    }
    if (this.maxTime != null) {
      result.maxTime(this.maxTime, TimeUnit.MILLISECONDS);
    }
    if (this.bypassDocumentValidation != null) {
      result.bypassDocumentValidation(this.bypassDocumentValidation);
    }
    if (this.collation != null) {
      result.collation(this.collation.toDriverClass(clientContext));
    }
    if (this.arrayFilters != null) {
      result.arrayFilters(clientContext.getMapper().toBsonList(this.arrayFilters));
    }
    if (this.hint != null) {
      result.hint(clientContext.getMapper().toBson(this.hint));
    }
    if (this.hintString != null) {
      result.hintString(this.hintString);
    }
    return result;
  }
}
