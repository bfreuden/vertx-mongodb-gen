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

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;
import io.vertx.mongo.impl.ConversionUtilsImpl;
import org.bson.conversions.Bson;

@DataObject(
    generateConverter = true
)
public class DeleteOneModel<T> extends WriteModel<T> {
  private JsonObject filter;

  private DeleteOptions options;

  int __ctorIndex;

  /**
   *  Construct a new instance.
   *
   *  @param filter a document describing the query filter, which may not be null.
   */
  public DeleteOneModel(JsonObject filter) {
    __ctorIndex = 0;
    this.filter = filter;
  }

  /**
   *  Construct a new instance.
   *
   *  @param filter a document describing the query filter, which may not be null.
   *  @param options the options to apply
   *  @since 3.4
   *  @mongodb.server.release 3.4
   */
  public DeleteOneModel(JsonObject filter, DeleteOptions options) {
    __ctorIndex = 1;
    this.filter = filter;
    this.options = options;
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
   *  Gets the options to apply.
   *
   *  @return the options
   *  @since 3.4
   */
  public DeleteOptions getOptions() {
    return options;
  }

  /**
   * @return MongoDB driver object
   * @hidden
   */
  public com.mongodb.client.model.DeleteOneModel<T> toDriverClass() {
    if (__ctorIndex == 0) {
      Bson __filter = ConversionUtilsImpl.INSTANCE.toBson(this.filter);
      return new com.mongodb.client.model.DeleteOneModel<T>(__filter);
    } else if (__ctorIndex == 1) {
      Bson __filter = ConversionUtilsImpl.INSTANCE.toBson(this.filter);
      com.mongodb.client.model.DeleteOptions __options = ConversionUtilsImpl.INSTANCE.toDeleteOptions(this.options);
      return new com.mongodb.client.model.DeleteOneModel<T>(__filter, __options);
    } else {
      throw new IllegalArgumentException("unknown constructor");
    }
  }
}
