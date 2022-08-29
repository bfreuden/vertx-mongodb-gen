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
import io.vertx.mongo.impl.ConversionUtilsImpl;
import java.lang.Boolean;
import java.lang.Integer;
import java.lang.Long;
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
  private String comment;

  /**
   * the hint
   */
  private JsonObject hint;

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
  public AggregateOptions allowDiskUse(Boolean allowDiskUse) {
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
  public AggregateOptions maxTime(Long maxTime) {
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
  public AggregateOptions maxAwaitTime(Long maxAwaitTime) {
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
  public AggregateOptions bypassDocumentValidation(Boolean bypassDocumentValidation) {
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
  public AggregateOptions collation(Collation collation) {
    this.collation = collation;
    return this;
  }

  public Collation getCollation() {
    return collation;
  }

  /**
   *  Sets the comment to the aggregation. A null value means no comment is set.
   *
   *  @param comment the comment
   *  @return this
   *  @mongodb.server.release 3.6
   *  @since 1.7
   */
  public AggregateOptions comment(String comment) {
    this.comment = comment;
    return this;
  }

  public String getComment() {
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
  public AggregateOptions hint(JsonObject hint) {
    this.hint = hint;
    return this;
  }

  public JsonObject getHint() {
    return hint;
  }

  /**
   *  Sets the number of documents to return per batch.
   *
   *  <p>Overrides the {@link org.reactivestreams.Subscription#request(long)} value for setting the batch size, allowing for fine grained
   *  control over the underlying cursor.</p>
   *
   *  @param batchSize the batch size
   *  @return this
   *  @since 1.8
   *  @mongodb.driver.manual reference/method/cursor.batchSize/#cursor.batchSize Batch Size
   */
  public AggregateOptions batchSize(Integer batchSize) {
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
  public <TDocument> void initializePublisher(AggregatePublisher<TDocument> publisher) {
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
      publisher.collation(this.collation.toDriverClass());
    }
    if (this.comment != null) {
      publisher.comment(this.comment);
    }
    if (this.hint != null) {
      publisher.hint(ConversionUtilsImpl.INSTANCE.toBson(this.hint));
    }
    if (this.batchSize != null) {
      publisher.batchSize(this.batchSize);
    }
  }
}
