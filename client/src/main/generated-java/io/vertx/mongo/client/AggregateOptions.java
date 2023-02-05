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
package io.vertx.mongo.client;

import com.mongodb.reactivestreams.client.AggregatePublisher;
import io.vertx.core.json.JsonObject;
import io.vertx.mongo.client.model.Collation;
import io.vertx.mongo.impl.MongoClientContext;
import java.lang.Boolean;
import java.lang.Integer;
import java.lang.Long;
import java.lang.Object;
import java.lang.String;
import java.util.concurrent.TimeUnit;

/**
 *  Options for aggregate.
 *
 *  @since 1.0
 */
public class AggregateOptions {
  /**
   * true if writing to temporary files is enabled
   */
  private Boolean allowDiskUse;

  /**
   *  the max time
   */
  private Long maxTime;

  /**
   *  the max await time
   */
  private Long maxAwaitTime;

  /**
   * If true, allows the write to opt-out of document level validation.
   */
  private Boolean bypassDocumentValidation;

  /**
   * the collation options to use
   */
  private Collation collation;

  /**
   * the comment
   */
  private Object comment;

  /**
   * the hint
   */
  private JsonObject hint;

  /**
   * the name of the index which should be used for the operation
   */
  private String hintString;

  /**
   * the variables
   */
  private JsonObject let;

  /**
   * the batch size
   */
  private Integer batchSize;

  /**
   *  Enables writing to temporary files. A null value indicates that it's unspecified.
   *
   *  @param allowDiskUse true if writing to temporary files is enabled
   *  @return this
   *  @mongodb.driver.manual reference/command/aggregate/ Aggregation
   */
  public AggregateOptions setAllowDiskUse(Boolean allowDiskUse) {
    this.allowDiskUse = allowDiskUse;
    return this;
  }

  public Boolean isAllowDiskUse() {
    return allowDiskUse;
  }

  /**
   *  Sets the maximum execution time on the server for this operation.
   *
   *  @param maxTime  the max time (in milliseconds)
   *  @return this
   *  @mongodb.driver.manual reference/method/cursor.maxTimeMS/#cursor.maxTimeMS Max Time
   */
  public AggregateOptions setMaxTime(Long maxTime) {
    this.maxTime = maxTime;
    return this;
  }

  public Long getMaxTime() {
    return maxTime;
  }

  /**
   *  The maximum amount of time for the server to wait on new documents to satisfy a {@code $changeStream} aggregation.
   *
   *  A zero value will be ignored.
   *
   *  @param maxAwaitTime  the max await time (in milliseconds)
   *  @return the maximum await execution time in the given time unit
   *  @mongodb.server.release 3.6
   *  @since 1.6
   */
  public AggregateOptions setMaxAwaitTime(Long maxAwaitTime) {
    this.maxAwaitTime = maxAwaitTime;
    return this;
  }

  public Long getMaxAwaitTime() {
    return maxAwaitTime;
  }

  /**
   *  Sets the bypass document level validation flag.
   *
   *  <p>Note: This only applies when an $out stage is specified</p>.
   *
   *  @param bypassDocumentValidation If true, allows the write to opt-out of document level validation.
   *  @return this
   *  @since 1.2
   *  @mongodb.driver.manual reference/command/aggregate/ Aggregation
   *  @mongodb.server.release 3.2
   */
  public AggregateOptions setBypassDocumentValidation(Boolean bypassDocumentValidation) {
    this.bypassDocumentValidation = bypassDocumentValidation;
    return this;
  }

  public Boolean isBypassDocumentValidation() {
    return bypassDocumentValidation;
  }

  /**
   *  Sets the collation options
   *
   *  <p>A null value represents the server default.</p>
   *  @param collation the collation options to use
   *  @return this
   *  @since 1.3
   *  @mongodb.server.release 3.4
   */
  public AggregateOptions setCollation(Collation collation) {
    this.collation = collation;
    return this;
  }

  public Collation getCollation() {
    return collation;
  }

  /**
   *  Sets the comment for this operation. A null value means no comment is set.
   *
   *  <p>The comment can be any valid BSON type for server versions 4.4 and above.
   *  Server versions between 3.6 and 4.2 only support string as comment,
   *  and providing a non-string type will result in a server-side error.
   *
   *  @param comment the comment
   *  @return this
   *  @since 4.6
   *  @mongodb.server.release 3.6
   */
  public AggregateOptions setComment(Object comment) {
    this.comment = comment;
    return this;
  }

  public Object getComment() {
    return comment;
  }

  /**
   *  Sets the hint for which index to use. A null value means no hint is set.
   *
   *  @param hint the hint
   *  @return this
   *  @mongodb.server.release 3.6
   *  @since 1.7
   */
  public AggregateOptions setHint(JsonObject hint) {
    this.hint = hint;
    return this;
  }

  public JsonObject getHint() {
    return hint;
  }

  /**
   *  Sets the hint for which index to use. A null value means no hint is set.
   *
   *  <p>Note: If {@link AggregatePublisher#setHint(JsonObject)} is set that will be used instead of any hint string.</p>
   *
   *  @param hint the name of the index which should be used for the operation
   *  @return this
   *  @since 4.4
   */
  public AggregateOptions setHintString(String hint) {
    this.hintString = hint;
    return this;
  }

  public String getHintString() {
    return hintString;
  }

  /**
   *  Add top-level variables to the aggregation.
   *  <p>
   *  For MongoDB 5.0+, the aggregate command accepts a {@code let} option. This option is a document consisting of zero or more
   *  fields representing variables that are accessible to the aggregation pipeline.  The key is the name of the variable and the value is
   *  a constant in the aggregate expression language. Each parameter name is then usable to access the value of the corresponding
   *  expression with the "$$" syntax within aggregate expression contexts which may require the use of $expr or a pipeline.
   *  </p>
   *
   *  @param variables the variables
   *  @return this
   *  @since 4.3
   *  @mongodb.server.release 5.0
   */
  public AggregateOptions setLet(JsonObject variables) {
    this.let = variables;
    return this;
  }

  public JsonObject getLet() {
    return let;
  }

  /**
   *  Sets the number of documents to return per batch.
   *
   *  <p>Overrides the {@link org.reactivestreams.Subscription#request(long)} value for setting the batch size, allowing for fine-grained
   *  control over the underlying cursor.</p>
   *
   *  @param batchSize the batch size
   *  @return this
   *  @since 1.8
   *  @mongodb.driver.manual reference/method/cursor.batchSize/#cursor.batchSize Batch Size
   */
  public AggregateOptions setBatchSize(Integer batchSize) {
    this.batchSize = batchSize;
    return this;
  }

  public Integer getBatchSize() {
    return batchSize;
  }

  /**
   * @param publisher MongoDB driver publisher
   * @param <TDocument> document class
   * @hidden
   */
  public <TDocument> void initializePublisher(MongoClientContext clientContext,
      AggregatePublisher<TDocument> publisher) {
    if (this.allowDiskUse != null) {
      publisher.allowDiskUse(this.allowDiskUse);
    }
    if (this.maxTime != null) {
      publisher.maxTime(this.maxTime, TimeUnit.MILLISECONDS);
    }
    if (this.maxAwaitTime != null) {
      publisher.maxAwaitTime(this.maxAwaitTime, TimeUnit.MILLISECONDS);
    }
    if (this.bypassDocumentValidation != null) {
      publisher.bypassDocumentValidation(this.bypassDocumentValidation);
    }
    if (this.collation != null) {
      publisher.collation(this.collation.toDriverClass(clientContext));
    }
    if (this.comment != null) {
      publisher.comment(clientContext.getMapper().toBsonValue(this.comment));
    }
    if (this.hint != null) {
      publisher.hint(clientContext.getMapper().toBson(this.hint));
    }
    if (this.hintString != null) {
      publisher.hintString(this.hintString);
    }
    if (this.let != null) {
      publisher.let(clientContext.getMapper().toBson(this.let));
    }
    if (this.batchSize != null) {
      publisher.batchSize(this.batchSize);
    }
  }
}
