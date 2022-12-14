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
package io.vertx.mongo.client.model;

import io.vertx.mongo.impl.MongoClientContext;
import java.util.function.Function;

public abstract class WriteModel<T> {
  /**
   * @return MongoDB driver object
   * @hidden
   */
  public abstract com.mongodb.client.model.WriteModel<T> toDriverClass(
      MongoClientContext clientContext, Function<T, T> inputMapper);
}
