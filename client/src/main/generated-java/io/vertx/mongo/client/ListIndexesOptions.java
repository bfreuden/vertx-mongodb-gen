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

import com.mongodb.reactivestreams.client.ListIndexesPublisher;
import io.vertx.mongo.impl.MongoClientContext;
import java.lang.Integer;
import java.lang.Long;
import java.lang.Object;
import java.util.concurrent.TimeUnit;

/**
 *  Options for ListIndexes.
 *
 *  @since 1.0
 */
public class ListIndexesOptions {
  /**
   *  the max time
   */
  private Long maxTime;

  /**
   * the batch size
   */
  private Integer batchSize;

  /**
   * the comment
   */
  private Object comment;

  /**
   *  Sets the maximum execution time on the server for this operation.
   *
   *  @param maxTime  the max time (in milliseconds)
   *  @return this
   *  @mongodb.driver.manual reference/operator/meta/maxTimeMS/ Max Time
   */
  public ListIndexesOptions setMaxTime(Long maxTime) {
    this.maxTime = maxTime;
    return this;
  }

  public Long getMaxTime() {
    return maxTime;
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
  public ListIndexesOptions setBatchSize(Integer batchSize) {
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
  public ListIndexesOptions setComment(Object comment) {
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
      ListIndexesPublisher<TDocument> publisher) {
    if (this.maxTime != null) {
      publisher.maxTime(this.maxTime, TimeUnit.MILLISECONDS);
    }
    if (this.batchSize != null) {
      publisher.batchSize(this.batchSize);
    }
    if (this.comment != null) {
      publisher.comment(clientContext.getMapper().toBsonValue(this.comment));
    }
  }
}
