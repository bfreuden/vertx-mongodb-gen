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
import java.lang.Integer;
import java.lang.Long;
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
   *  Sets the maximum execution time on the server for this operation.
   *
   *  @param maxTime  the max time (in milliseconds)
   *  @return this
   *  @mongodb.driver.manual reference/operator/meta/maxTimeMS/ Max Time
   */
  public ListIndexesOptions maxTime(Long maxTime) {
    this.maxTime = maxTime;
    return this;
  }

  public Long getMaxTime() {
    return maxTime;
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
  public ListIndexesOptions batchSize(Integer batchSize) {
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
  public <TDocument> void initializePublisher(ListIndexesPublisher<TDocument> publisher) {
    if (this.maxTime != null) {
      publisher.maxTime(this.maxTime, TimeUnit.MILLISECONDS);
    }
    if (this.batchSize != null) {
      publisher.batchSize(this.batchSize);
    }
  }
}
