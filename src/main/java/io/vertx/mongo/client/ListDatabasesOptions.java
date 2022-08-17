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

import com.mongodb.reactivestreams.client.ListDatabasesPublisher;
import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;
import io.vertx.mongo.impl.ConversionUtilsImpl;
import java.lang.Boolean;
import java.lang.Integer;
import java.lang.Long;
import java.util.concurrent.TimeUnit;

/**
 *  Options for ListDatabases.
 *
 *  @since 1.0
 */
@DataObject(
    generateConverter = true
)
public class ListDatabasesOptions {
  /**
   *  the max time
   */
  private Long maxTime;

  /**
   * the filter, which may be null.
   */
  private JsonObject filter;

  /**
   * the nameOnly flag, which may be null
   */
  private Boolean nameOnly;

  /**
   * the authorizedDatabasesOnly flag, which may be null
   */
  private Boolean authorizedDatabasesOnly;

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
  public ListDatabasesOptions maxTime(Long maxTime) {
    return this;
  }

  public Long getMaxTime() {
    return maxTime;
  }

  /**
   *  Sets the query filter to apply to the returned database names.
   *
   *  @param filter the filter, which may be null.
   *  @return this
   *  @mongodb.server.release 3.4.2
   *  @since 1.7
   */
  public ListDatabasesOptions filter(JsonObject filter) {
    return this;
  }

  public JsonObject getFilter() {
    return filter;
  }

  /**
   *  Sets the nameOnly flag that indicates whether the command should return just the database names or return the database names and
   *  size information.
   *
   *  @param nameOnly the nameOnly flag, which may be null
   *  @return this
   *  @mongodb.server.release 3.4.3
   *  @since 1.7
   */
  public ListDatabasesOptions nameOnly(Boolean nameOnly) {
    return this;
  }

  public Boolean isNameOnly() {
    return nameOnly;
  }

  /**
   *  Sets the authorizedDatabasesOnly flag that indicates whether the command should return just the databases which the user
   *  is authorized to see.
   *
   *  @param authorizedDatabasesOnly the authorizedDatabasesOnly flag, which may be null
   *  @return this
   *  @since 4.1
   *  @mongodb.server.release 4.0
   */
  public ListDatabasesOptions authorizedDatabasesOnly(Boolean authorizedDatabasesOnly) {
    return this;
  }

  public Boolean isAuthorizedDatabasesOnly() {
    return authorizedDatabasesOnly;
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
  public ListDatabasesOptions batchSize(Integer batchSize) {
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
  public <TDocument> void initializePublisher(ListDatabasesPublisher<TDocument> publisher) {
    if (this.maxTime != null) {
      publisher.maxTime(this.maxTime, TimeUnit.MILLISECONDS);
    }
    if (this.filter != null) {
      publisher.filter(ConversionUtilsImpl.INSTANCE.toBson(this.filter));
    }
    if (this.nameOnly != null) {
      publisher.nameOnly(this.nameOnly);
    }
    if (this.authorizedDatabasesOnly != null) {
      publisher.authorizedDatabasesOnly(this.authorizedDatabasesOnly);
    }
    if (this.batchSize != null) {
      publisher.batchSize(this.batchSize);
    }
  }
}
