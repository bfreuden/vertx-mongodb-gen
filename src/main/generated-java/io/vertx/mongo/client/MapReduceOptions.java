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

import com.mongodb.client.model.MapReduceAction;
import com.mongodb.reactivestreams.client.MapReducePublisher;
import io.vertx.core.json.JsonObject;
import io.vertx.mongo.client.model.Collation;
import io.vertx.mongo.impl.MongoClientContext;
import java.lang.Boolean;
import java.lang.Deprecated;
import java.lang.Integer;
import java.lang.Long;
import java.lang.String;
import java.util.concurrent.TimeUnit;

/**
 *  Options for map reduce.
 *
 *  @since 1.0
 */
public class MapReduceOptions {
  /**
   * the name of the collection that you want the map-reduce operation to write its output.
   */
  private String collectionName;

  /**
   * the JavaScript function that follows the reduce method and modifies the output.
   */
  private String finalizeFunction;

  /**
   * the global variables that are accessible in the map, reduce and finalize functions.
   */
  private JsonObject scope;

  /**
   * the sort criteria, which may be null.
   */
  private JsonObject sort;

  /**
   * the filter to apply to the query.
   */
  private JsonObject filter;

  /**
   * the limit, which may be null
   */
  private Integer limit;

  /**
   * the flag that specifies whether to convert intermediate data into BSON format between the execution of the map and
   */
  private Boolean jsMode;

  /**
   * whether to include the timing information in the result information.
   */
  private Boolean verbose;

  /**
   *  the max time
   */
  private Long maxTime;

  /**
   * an {@link com.mongodb.client.model.MapReduceAction} to perform on the collection
   */
  private MapReduceAction action;

  /**
   * the name of the database to output into.
   */
  private String databaseName;

  /**
   * if the output database is sharded
   */
  private Boolean sharded;

  /**
   * if the post-processing step will prevent MongoDB from locking the database.
   */
  private Boolean nonAtomic;

  /**
   * If true, allows the write to opt-out of document level validation.
   */
  private Boolean bypassDocumentValidation;

  /**
   * the collation options to use
   */
  private Collation collation;

  /**
   * the batch size
   */
  private Integer batchSize;

  /**
   *  Sets the collectionName for the output of the MapReduce
   *
   *  <p>The default action is replace the collection if it exists, to change this use {@link #action}.</p>
   *
   *  @param collectionName the name of the collection that you want the map-reduce operation to write its output.
   *  @return this
   */
  public MapReduceOptions setCollectionName(String collectionName) {
    this.collectionName = collectionName;
    return this;
  }

  public String getCollectionName() {
    return collectionName;
  }

  /**
   *  Sets the JavaScript function that follows the reduce method and modifies the output.
   *
   *  @param finalizeFunction the JavaScript function that follows the reduce method and modifies the output.
   *  @return this
   *  @mongodb.driver.manual reference/command/mapReduce/#mapreduce-finalize-cmd Requirements for the finalize Function
   */
  public MapReduceOptions setFinalizeFunction(String finalizeFunction) {
    this.finalizeFunction = finalizeFunction;
    return this;
  }

  public String getFinalizeFunction() {
    return finalizeFunction;
  }

  /**
   *  Sets the global variables that are accessible in the map, reduce and finalize functions.
   *
   *  @param scope the global variables that are accessible in the map, reduce and finalize functions.
   *  @return this
   *  @mongodb.driver.manual reference/command/mapReduce mapReduce
   */
  public MapReduceOptions setScope(JsonObject scope) {
    this.scope = scope;
    return this;
  }

  public JsonObject getScope() {
    return scope;
  }

  /**
   *  Sets the sort criteria to apply to the query.
   *
   *  @param sort the sort criteria, which may be null.
   *  @return this
   *  @mongodb.driver.manual reference/method/cursor.sort/ Sort
   */
  public MapReduceOptions setSort(JsonObject sort) {
    this.sort = sort;
    return this;
  }

  public JsonObject getSort() {
    return sort;
  }

  /**
   *  Sets the query filter to apply to the query.
   *
   *  @param filter the filter to apply to the query.
   *  @return this
   *  @mongodb.driver.manual reference/method/db.collection.find/ Filter
   */
  public MapReduceOptions setFilter(JsonObject filter) {
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
  public MapReduceOptions setLimit(Integer limit) {
    this.limit = limit;
    return this;
  }

  public Integer getLimit() {
    return limit;
  }

  /**
   *  Sets the flag that specifies whether to convert intermediate data into BSON format between the execution of the map and reduce
   *  functions. Defaults to false.
   *
   *  @param jsMode the flag that specifies whether to convert intermediate data into BSON format between the execution of the map and
   *                reduce functions
   *  @return jsMode
   *  @mongodb.driver.manual reference/command/mapReduce mapReduce
   */
  public MapReduceOptions setJsMode(Boolean jsMode) {
    this.jsMode = jsMode;
    return this;
  }

  public Boolean isJsMode() {
    return jsMode;
  }

  /**
   *  Sets whether to include the timing information in the result information.
   *
   *  @param verbose whether to include the timing information in the result information.
   *  @return this
   */
  public MapReduceOptions setVerbose(Boolean verbose) {
    this.verbose = verbose;
    return this;
  }

  public Boolean isVerbose() {
    return verbose;
  }

  /**
   *  Sets the maximum execution time on the server for this operation.
   *
   *  @param maxTime  the max time (in milliseconds)
   *  @return this
   *  @mongodb.driver.manual reference/method/cursor.maxTimeMS/#cursor.maxTimeMS Max Time
   */
  public MapReduceOptions setMaxTime(Long maxTime) {
    this.maxTime = maxTime;
    return this;
  }

  public Long getMaxTime() {
    return maxTime;
  }

  /**
   *  Specify the {@code MapReduceAction} to be used when writing to a collection.
   *
   *  @param action an {@link com.mongodb.client.model.MapReduceAction} to perform on the collection
   *  @return this
   */
  public MapReduceOptions setAction(MapReduceAction action) {
    this.action = action;
    return this;
  }

  public MapReduceAction getAction() {
    return action;
  }

  /**
   *  Sets the name of the database to output into.
   *
   *  @param databaseName the name of the database to output into.
   *  @return this
   *  @mongodb.driver.manual reference/command/mapReduce/#output-to-a-collection-with-an-action output with an action
   */
  public MapReduceOptions setDatabaseName(String databaseName) {
    this.databaseName = databaseName;
    return this;
  }

  public String getDatabaseName() {
    return databaseName;
  }

  /**
   *  Sets if the output database is sharded
   *
   *  @param sharded if the output database is sharded
   *  @return this
   *  @mongodb.driver.manual reference/command/mapReduce/#output-to-a-collection-with-an-action output with an action
   *  @deprecated this option will no longer be supported in MongoDB 4.4
   */
  @Deprecated
  public MapReduceOptions setSharded(Boolean sharded) {
    this.sharded = sharded;
    return this;
  }

  @Deprecated
  public Boolean isSharded() {
    return sharded;
  }

  /**
   *  Sets if the post-processing step will prevent MongoDB from locking the database.
   *
   *  Valid only with the {@code MapReduceAction.MERGE} or {@code MapReduceAction.REDUCE} actions.
   *
   *  @param nonAtomic if the post-processing step will prevent MongoDB from locking the database.
   *  @return this
   *  @mongodb.driver.manual reference/command/mapReduce/#output-to-a-collection-with-an-action output with an action
   *  @deprecated this option will no longer be supported in MongoDB 4.4 as it will no longer hold a global or database level write lock.
   */
  @Deprecated
  public MapReduceOptions setNonAtomic(Boolean nonAtomic) {
    this.nonAtomic = nonAtomic;
    return this;
  }

  @Deprecated
  public Boolean isNonAtomic() {
    return nonAtomic;
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
  public MapReduceOptions setBypassDocumentValidation(Boolean bypassDocumentValidation) {
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
  public MapReduceOptions setCollation(Collation collation) {
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
  public MapReduceOptions setBatchSize(Integer batchSize) {
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
      MapReducePublisher<TDocument> publisher) {
    if (this.collectionName != null) {
      publisher.collectionName(this.collectionName);
    }
    if (this.finalizeFunction != null) {
      publisher.finalizeFunction(this.finalizeFunction);
    }
    if (this.scope != null) {
      publisher.scope(clientContext.getMapper().toBson(this.scope));
    }
    if (this.sort != null) {
      publisher.sort(clientContext.getMapper().toBson(this.sort));
    }
    if (this.filter != null) {
      publisher.filter(clientContext.getMapper().toBson(this.filter));
    }
    if (this.limit != null) {
      publisher.limit(this.limit);
    }
    if (this.jsMode != null) {
      publisher.jsMode(this.jsMode);
    }
    if (this.verbose != null) {
      publisher.verbose(this.verbose);
    }
    if (this.maxTime != null) {
      publisher.maxTime(this.maxTime, TimeUnit.MILLISECONDS);
    }
    if (this.action != null) {
      publisher.action(this.action);
    }
    if (this.databaseName != null) {
      publisher.databaseName(this.databaseName);
    }
    if (this.sharded != null) {
      publisher.sharded(this.sharded);
    }
    if (this.nonAtomic != null) {
      publisher.nonAtomic(this.nonAtomic);
    }
    if (this.bypassDocumentValidation != null) {
      publisher.bypassDocumentValidation(this.bypassDocumentValidation);
    }
    if (this.collation != null) {
      publisher.collation(this.collation.toDriverClass(clientContext));
    }
    if (this.batchSize != null) {
      publisher.batchSize(this.batchSize);
    }
  }
}
