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

public class InsertOneModel<T> extends WriteModel<T> {
  private T document;

  /**
   *  Construct a new instance.
   *
   *  @param document the document to insert, which may not be null.
   */
  public InsertOneModel(T document) {
    this.document = document;
  }

  /**
   *  Gets the document to insert.
   *
   *  @return the document to insert
   */
  public T getDocument() {
    return document;
  }

  /**
   * @return MongoDB driver object
   * @hidden
   */
  public com.mongodb.client.model.InsertOneModel<T> toDriverClass(MongoClientContext clientContext,
      Function<T, T> inputMapper) {
    return new com.mongodb.client.model.InsertOneModel<T>(inputMapper == null ? this.document : inputMapper.apply(this.document));
  }
}
