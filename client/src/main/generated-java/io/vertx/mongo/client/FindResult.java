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
import io.vertx.core.json.JsonObject;
import io.vertx.mongo.MongoResult;
import java.lang.Class;

/**
 *  Publisher interface for find.
 *  @param <TResult> The type of the result.
 *  @since 1.0
 */
public interface FindResult<TResult> extends MongoResult<TResult>  {
  /**
   *  Explain the execution plan for this operation with the server's default verbosity level
   *  @return the execution plan
   *  @since 4.2
   *  @mongodb.driver.manual reference/command/explain/
   *  @mongodb.server.release 3.2
   */
  MongoResult<JsonObject> explain();

  /**
   *  Explain the execution plan for this operation with the given verbosity level
   *  @param verbosity the verbosity of the explanation
   *  @return the execution plan
   *  @since 4.2
   *  @mongodb.driver.manual reference/command/explain/
   *  @mongodb.server.release 3.2
   */
  MongoResult<JsonObject> explain(ExplainVerbosity verbosity);

  /**
   *  Explain the execution plan for this operation with the server's default verbosity level
   *  @param <E> the type of the document class
   *  @param explainResultClass the document class to decode into
   *  @return the execution plan
   *  @since 4.2
   *  @mongodb.driver.manual reference/command/explain/
   *  @mongodb.server.release 3.2
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
   *  @mongodb.server.release 3.2
   */
  <E> MongoResult<E> explain(Class<E> explainResultClass, ExplainVerbosity verbosity);
}
