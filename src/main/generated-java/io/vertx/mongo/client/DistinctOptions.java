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

import com.mongodb.reactivestreams.client.DistinctPublisher;
import io.vertx.core.json.JsonObject;
import io.vertx.mongo.client.model.Collation;
import io.vertx.mongo.impl.MongoClientContext;
import java.lang.Integer;
import java.lang.Long;
import java.lang.Object;
import java.util.concurrent.TimeUnit;

/**
 *  Options for distinct.
 *
 *  @since 1.0
 */
public class DistinctOptions {
  /**
   * the filter, which may be null.
   */
  private JsonObject filter;

  /**
   *  the max time
   */
  private Long maxTime;

  /**
   * the collation options to use
   */
  private Collation collation;

  /**
   * the batch size
   */
  private Integer batchSize;

  /**
   * the comment
   */
  private Object comment;

  /**
   *  Sets the query filter to apply to the query.
   *
   *  @param filter the filter, which may be null.
   *  @return this
   *  @mongodb.driver.manual reference/method/db.collection.find/ Filter
   */
  public DistinctOptions setFilter(JsonObject filter) {
    this.filter = filter;
    return this;
  }

  public JsonObject getFilter() {
    return filter;
  }

  /**
   *  Sets the maximum execution time on the server for this operation.
   *
   *  @param maxTime  the max time (in milliseconds)
   *  @return this
   */
  public DistinctOptions setMaxTime(Long maxTime) {
    this.maxTime = maxTime;
    return this;
  }

  public Long getMaxTime() {
    return maxTime;
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
  public DistinctOptions setCollation(Collation collation) {
    this.collation = collation;
    return this;
  }

  public Collation getCollation() {
    return collation;
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
  public DistinctOptions setBatchSize(Integer batchSize) {
    this.batchSize = batchSize;
    return this;
  }

  public Integer getBatchSize() {
    return batchSize;
  }

  /**
   *  Sets the comment for this operation. A null value means no comment is set.
   *
   *  @param comment the comment
   *  @return this
   *  @since 4.6
   *  @mongodb.server.release 4.4
   */
  public DistinctOptions setComment(Object comment) {
    this.comment = comment;
    return this;
  }

  public Object getComment() {
    return comment;
  }

  /**
   * @param publisher MongoDB driver publisher
   * @param <TDocument> document class
   * @hidden
   */
  public <TDocument> void initializePublisher(MongoClientContext clientContext,
      DistinctPublisher<TDocument> publisher) {
    if (this.filter != null) {
      publisher.filter(clientContext.getMapper().toBson(this.filter));
    }
    if (this.maxTime != null) {
      publisher.maxTime(this.maxTime, TimeUnit.MILLISECONDS);
    }
    if (this.collation != null) {
      publisher.collation(this.collation.toDriverClass(clientContext));
    }
    if (this.batchSize != null) {
      publisher.batchSize(this.batchSize);
    }
    if (this.comment != null) {
      publisher.comment(clientContext.getMapper().toBsonValue(this.comment));
    }
  }
}
