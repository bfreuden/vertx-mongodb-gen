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

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.mongo.MongoResult;

import java.lang.Void;

/**
 *  Publisher for map reduce.
 *  @param <TResult> The type of the result.
 *  @since 1.0
 *  @deprecated Superseded by aggregate
 */
public interface MapReduceResult<TResult> extends MongoResult<TResult> {
  /**
   *  Aggregates documents to a collection according to the specified map-reduce function with the given options, which must specify a
   *  non-inline result.
   *  @return an empty future that indicates when the operation has completed
   *  @mongodb.driver.manual aggregation/ Aggregation
   */
  Future<Void> toCollection();

  /**
   *  Aggregates documents to a collection according to the specified map-reduce function with the given options, which must specify a
   *  non-inline result.
   *  @param resultHandler an empty async result that indicates when the operation has completed
   *  @mongodb.driver.manual aggregation/ Aggregation
   */
  void toCollection(Handler<AsyncResult<Void>> resultHandler);
}
