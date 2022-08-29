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
package io.vertx.mongo.client.gridfs;

import com.mongodb.reactivestreams.client.gridfs.GridFSFindPublisher;
import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;
import io.vertx.mongo.client.model.Collation;
import io.vertx.mongo.impl.ConversionUtilsImpl;
import java.lang.Boolean;
import java.lang.Integer;
import java.lang.Long;
import java.util.concurrent.TimeUnit;

/**
 *  Options for the GridFS Files Collection.
 *
 *  @since 1.3
 */
@DataObject(
    generateConverter = true
)
public class GridFSFindOptions {
  /**
   * the filter, which may be null.
   */
  private JsonObject filter;

  /**
   * the limit, which may be null
   */
  private Integer limit;

  /**
   * the number of documents to skip
   */
  private Integer skip;

  /**
   * the sort criteria, which may be null.
   */
  private JsonObject sort;

  /**
   * true if cursor timeout is disabled
   */
  private Boolean noCursorTimeout;

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
   *  Sets the query filter to apply to the query.
   *  <p>
   *  Below is an example of filtering against the filename and some nested metadata that can also be stored along with the file data:
   *  <pre>
   *   {@code
   *       Filters.and(Filters.eq("filename", "mongodb.png"), Filters.eq("metadata.contentType", "image/png"));
   *   }
   *   </pre>
   *
   *  @param filter the filter, which may be null.
   *  @return this
   *  @mongodb.driver.manual reference/method/db.collection.find/ Filter
   *  @see com.mongodb.client.model.Filters
   */
  public GridFSFindOptions setFilter(JsonObject filter) {
    this.filter = filter;
    return this;
  }

  public JsonObject getFilter() {
    return filter;
  }

  /**
   *  Sets the limit to apply.
   *
   *  @param limit the limit, which may be null
   *  @return this
   *  @mongodb.driver.manual reference/method/cursor.limit/#cursor.limit Limit
   */
  public GridFSFindOptions setLimit(Integer limit) {
    this.limit = limit;
    return this;
  }

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
  public GridFSFindOptions setSkip(Integer skip) {
    this.skip = skip;
    return this;
  }

  public Integer getSkip() {
    return skip;
  }

  /**
   *  Sets the sort criteria to apply to the query.
   *
   *  @param sort the sort criteria, which may be null.
   *  @return this
   *  @mongodb.driver.manual reference/method/cursor.sort/ Sort
   */
  public GridFSFindOptions setSort(JsonObject sort) {
    this.sort = sort;
    return this;
  }

  public JsonObject getSort() {
    return sort;
  }

  /**
   *  The server normally times out idle cursors after an inactivity period (10 minutes)
   *  to prevent excess memory use. Set this option to prevent that.
   *
   *  @param noCursorTimeout true if cursor timeout is disabled
   *  @return this
   */
  public GridFSFindOptions setNoCursorTimeout(Boolean noCursorTimeout) {
    this.noCursorTimeout = noCursorTimeout;
    return this;
  }

  public Boolean isNoCursorTimeout() {
    return noCursorTimeout;
  }

  /**
   *  Sets the maximum execution time on the server for this operation.
   *
   *  @param maxTime  the max time (in milliseconds)
   *  @return this
   *  @mongodb.driver.manual reference/method/cursor.maxTimeMS/#cursor.maxTimeMS Max Time
   */
  public GridFSFindOptions setMaxTime(Long maxTime) {
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
  public GridFSFindOptions setCollation(Collation collation) {
    this.collation = collation;
    return this;
  }

  public Collation getCollation() {
    return collation;
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
  public GridFSFindOptions setBatchSize(Integer batchSize) {
    this.batchSize = batchSize;
    return this;
  }

  public Integer getBatchSize() {
    return batchSize;
  }

  /**
   * @param publisher MongoDB driver publisher
   * @hidden
   */
  public void initializePublisher(GridFSFindPublisher publisher) {
    if (this.filter != null) {
      publisher.filter(ConversionUtilsImpl.INSTANCE.toBson(this.filter));
    }
    if (this.limit != null) {
      publisher.limit(this.limit);
    }
    if (this.skip != null) {
      publisher.skip(this.skip);
    }
    if (this.sort != null) {
      publisher.sort(ConversionUtilsImpl.INSTANCE.toBson(this.sort));
    }
    if (this.noCursorTimeout != null) {
      publisher.noCursorTimeout(this.noCursorTimeout);
    }
    if (this.maxTime != null) {
      publisher.maxTime(this.maxTime, TimeUnit.MILLISECONDS);
    }
    if (this.collation != null) {
      publisher.collation(this.collation.toDriverClass());
    }
    if (this.batchSize != null) {
      publisher.batchSize(this.batchSize);
    }
  }
}
