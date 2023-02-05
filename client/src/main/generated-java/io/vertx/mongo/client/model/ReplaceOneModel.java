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

import io.vertx.core.json.JsonObject;
import io.vertx.mongo.impl.MongoClientContext;
import java.util.function.Function;
import org.bson.conversions.Bson;

public class ReplaceOneModel<T> extends WriteModel<T> {
  private JsonObject filter;

  private T replacement;

  private ReplaceOptions replaceOptions;

  int __ctorIndex;

  /**
   *  Construct a new instance.
   *
   *  @param filter    a document describing the query filter, which may not be null.
   *  @param replacement the replacement document
   */
  public ReplaceOneModel(JsonObject filter, T replacement) {
    __ctorIndex = 0;
    this.filter = filter;
    this.replacement = replacement;
  }

  /**
   *  Construct a new instance.
   *
   *  @param filter    a document describing the query filter, which may not be null.
   *  @param replacement the replacement document
   *  @param options     the options to apply
   *  @since 3.7
   */
  public ReplaceOneModel(JsonObject filter, T replacement, ReplaceOptions options) {
    __ctorIndex = 1;
    this.filter = filter;
    this.replacement = replacement;
    this.replaceOptions = options;
  }

  /**
   *  Gets the query filter.
   *
   *  @return the query filter
   */
  public JsonObject getFilter() {
    return filter;
  }

  /**
   *  Gets the document which will replace the document matching the query filter.
   *
   *  @return the replacement document
   */
  public T getReplacement() {
    return replacement;
  }

  /**
   *  Gets the ReplaceOptions to apply.
   *
   *  @return the replace options
   *  @since 3.7
   */
  public ReplaceOptions getReplaceOptions() {
    return replaceOptions;
  }

  /**
   * @return MongoDB driver object
   * @hidden
   */
  public com.mongodb.client.model.ReplaceOneModel<T> toDriverClass(MongoClientContext clientContext,
      Function<T, T> inputMapper) {
    if (__ctorIndex == 0) {
      Bson __filter = clientContext.getMapper().toBson(this.filter);
      return new com.mongodb.client.model.ReplaceOneModel<T>(__filter, inputMapper == null ? this.replacement : inputMapper.apply(this.replacement));
    } else if (__ctorIndex == 1) {
      Bson __filter = clientContext.getMapper().toBson(this.filter);
      com.mongodb.client.model.ReplaceOptions __replaceOptions = this.replaceOptions.toDriverClass(clientContext);
      return new com.mongodb.client.model.ReplaceOneModel<T>(__filter, inputMapper == null ? this.replacement : inputMapper.apply(this.replacement), __replaceOptions);
    } else {
      throw new IllegalArgumentException("unknown constructor");
    }
  }
}
