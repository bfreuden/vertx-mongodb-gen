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
import java.lang.Integer;
import java.lang.Long;
import java.lang.String;
import java.util.concurrent.TimeUnit;

/**
 *  The options for a count operation.
 *
 *  @since 3.0
 *  @mongodb.driver.manual reference/command/count/ Count
 */
@DataObject(
    generateConverter = true
)
public class CountOptions {
  /**
   * a document describing the index which should be used for this operation.
   */
  private JsonObject hint;

  /**
   * the name of the index which should be used for the operation
   */
  private String hintString;

  /**
   * the limit
   */
  private Integer limit;

  /**
   * the number of documents to skip
   */
  private Integer skip;

  /**
   *  the max time
   */
  private Long maxTime;

  /**
   * the collation options to use
   */
  private Collation collation;

  /**
   *  Sets the hint to apply.
   *
   *  @param hint a document describing the index which should be used for this operation.
   *  @return this
   */
  public CountOptions hint(JsonObject hint) {
    this.hint = hint;
    return this;
  }

  /**
   *  Gets the hint to apply.
   *
   *  @return the hint, which should describe an existing
   */
  public JsonObject getHint() {
    return hint;
  }

  /**
   *  Sets the hint to apply.
   *
   *  <p>Note: If {@link CountOptions#hint(JsonObject)} is set that will be used instead of any hint string.</p>
   *
   *  @param hint the name of the index which should be used for the operation
   *  @return this
   */
  public CountOptions hintString(String hint) {
    this.hintString = hint;
    return this;
  }

  /**
   *  Gets the hint string to apply.
   *
   *  @return the hint string, which should be the name of an existing index
   */
  public String getHintString() {
    return hintString;
  }

  /**
   *  Sets the limit to apply.
   *
   *  @param limit the limit
   *  @return this
   *  @mongodb.driver.manual reference/method/cursor.limit/#cursor.limit Limit
   */
  public CountOptions limit(Integer limit) {
    this.limit = limit;
    return this;
  }

  /**
   *  Gets the limit to apply.  The default is 0, which means there is no limit.
   *
   *  @return the limit
   *  @mongodb.driver.manual reference/method/cursor.limit/#cursor.limit Limit
   */
  public Integer getLimit() {
    return limit;
  }

  /**
   *  Sets the number of documents to skip.
   *
   *  @param skip the number of documents to skip
   *  @return this
   *  @mongodb.driver.manual reference/method/cursor.skip/#cursor.skip Skip
   */
  public CountOptions skip(Integer skip) {
    this.skip = skip;
    return this;
  }

  /**
   *  Gets the number of documents to skip.  The default is 0.
   *
   *  @return the number of documents to skip
   *  @mongodb.driver.manual reference/method/cursor.skip/#cursor.skip Skip
   */
  public Integer getSkip() {
    return skip;
  }

  /**
   *  Sets the maximum execution time on the server for this operation.
   *
   *  @param maxTime  the max time (in milliseconds)
   *  @return this
   */
  public CountOptions maxTime(Long maxTime) {
    this.maxTime = maxTime;
    return this;
  }

  /**
   *  Gets the maximum execution time on the server for this operation.  The default is 0, which places no limit on the execution time.
   *
   *  @return the maximum execution time (in milliseconds)
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
  public CountOptions collation(Collation collation) {
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
   * @return MongoDB driver object
   * @hidden
   */
  public com.mongodb.client.model.CountOptions toDriverClass() {
    com.mongodb.client.model.CountOptions result = new com.mongodb.client.model.CountOptions();
    if (this.hint != null) {
      result.hint(ConversionUtilsImpl.INSTANCE.toBson(this.hint));
    }
    if (this.hintString != null) {
      result.hintString(this.hintString);
    }
    if (this.limit != null) {
      result.limit(this.limit);
    }
    if (this.skip != null) {
      result.skip(this.skip);
    }
    if (this.maxTime != null) {
      result.maxTime(this.maxTime, TimeUnit.MILLISECONDS);
    }
    if (this.collation != null) {
      result.collation(this.collation.toDriverClass());
    }
    return result;
  }
}
