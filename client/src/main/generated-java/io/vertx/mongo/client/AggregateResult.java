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

import com.mongodb.ExplainVerbosity;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.mongo.MongoResult;
import java.lang.Class;
import java.lang.Void;

/**
 *  Publisher for aggregate.
 *  @param <TResult> The type of the result.
 *  @since 1.0
 */
public interface AggregateResult<TResult> extends MongoResult<TResult> {
  /**
   *  Aggregates documents according to the specified aggregation pipeline, which must end with a $out stage.
   *  @return an empty future that indicates when the operation has completed
   *  @mongodb.driver.manual aggregation/ Aggregation
   */
  Future<Void> toCollection();

  /**
   *  Aggregates documents according to the specified aggregation pipeline, which must end with a $out stage.
   *  @param resultHandler an empty async result that indicates when the operation has completed
   *  @mongodb.driver.manual aggregation/ Aggregation
   */
  void toCollection(Handler<AsyncResult<Void>> resultHandler);

  /**
   *  Explain the execution plan for this operation with the server's default verbosity level
   *  @return the execution plan
   *  @since 4.2
   *  @mongodb.driver.manual reference/command/explain/
   *  @mongodb.server.release 3.6
   */
  MongoResult<JsonObject> explain();

  /**
   *  Explain the execution plan for this operation with the given verbosity level
   *  @param verbosity the verbosity of the explanation
   *  @return the execution plan
   *  @since 4.2
   *  @mongodb.driver.manual reference/command/explain/
   *  @mongodb.server.release 3.6
   */
  MongoResult<JsonObject> explain(ExplainVerbosity verbosity);

  /**
   *  Explain the execution plan for this operation with the server's default verbosity level
   *  @param <E> the type of the document class
   *  @param explainResultClass the document class to decode into
   *  @return the execution plan
   *  @since 4.2
   *  @mongodb.driver.manual reference/command/explain/
   *  @mongodb.server.release 3.6
   */
  <E> MongoResult<E> explain(Class<E> explainResultClass);

  /**
   *  Explain the execution plan for this operation with the given verbosity level
   *  @param <E> the type of the document class
   *  @param explainResultClass the document class to decode into
   *  @param verbosity            the verbosity of the explanation
   *  @return the execution plan
   *  @since 4.2
   *  @mongodb.driver.manual reference/command/explain/
   *  @mongodb.server.release 3.6
   */
  <E> MongoResult<E> explain(Class<E> explainResultClass, ExplainVerbosity verbosity);
}
