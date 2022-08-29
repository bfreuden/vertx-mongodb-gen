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

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.mongo.impl.ConversionUtilsImpl;
import java.util.List;
import org.bson.conversions.Bson;

public class UpdateOneModel<T> extends WriteModel<T> {
  private JsonObject filter;

  private JsonObject update;

  private JsonArray updatePipeline;

  private UpdateOptions options;

  int __ctorIndex;

  /**
   *  Construct a new instance.
   *
   *  @param filter a document describing the query filter, which may not be null.
   *  @param update   a document describing the update, which may not be null. The update to apply must include only update operators.
   */
  public UpdateOneModel(JsonObject filter, JsonObject update) {
    __ctorIndex = 0;
    this.filter = filter;
    this.update = update;
  }

  /**
   *  Construct a new instance.
   *
   *  @param filter a document describing the query filter, which may not be null.
   *  @param update   a document describing the update, which may not be null. The update to apply must include only update operators.
   *  @param options the options to apply
   */
  public UpdateOneModel(JsonObject filter, JsonObject update, UpdateOptions options) {
    __ctorIndex = 1;
    this.filter = filter;
    this.update = update;
    this.options = options;
  }

  /**
   *  Construct a new instance.
   *
   *  @param filter a document describing the query filter, which may not be null.
   *  @param update a pipeline describing the update, which may not be null.
   *  @since 3.11
   *  @mongodb.server.release 4.2
   */
  public UpdateOneModel(JsonObject filter, JsonArray update) {
    __ctorIndex = 2;
    this.filter = filter;
    this.updatePipeline = update;
  }

  /**
   *  Construct a new instance.
   *
   *  @param filter a document describing the query filter, which may not be null.
   *  @param update a pipeline describing the update, which may not be null.
   *  @param options the options to apply
   *  @since 3.11
   *  @mongodb.server.release 4.2
   */
  public UpdateOneModel(JsonObject filter, JsonArray update, UpdateOptions options) {
    __ctorIndex = 3;
    this.filter = filter;
    this.updatePipeline = update;
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
   *  Gets the document specifying the updates to apply to the matching document.  The update to apply must include only update operators.
   *
   *  @return the document specifying the updates to apply
   */
  public JsonObject getUpdate() {
    return update;
  }

  /**
   *  Gets the pipeline specifying the updates to apply to the matching document.  The update to apply must include only update operators.
   *
   *  @return the pipeline specifying the updates to apply
   *  @since 3.11
   *  @mongodb.server.release 4.2
   */
  public JsonArray getUpdatePipeline() {
    return updatePipeline;
  }

  /**
   *  Gets the options to apply.
   *
   *  @return the options
   */
  public UpdateOptions getOptions() {
    return options;
  }

  /**
   * @return MongoDB driver object
   * @hidden
   */
  public com.mongodb.client.model.UpdateOneModel<T> toDriverClass() {
    if (__ctorIndex == 0) {
      Bson __filter = ConversionUtilsImpl.INSTANCE.toBson(this.filter);
      Bson __update = ConversionUtilsImpl.INSTANCE.toBson(this.update);
      return new com.mongodb.client.model.UpdateOneModel<T>(__filter, __update);
    } else if (__ctorIndex == 1) {
      Bson __filter = ConversionUtilsImpl.INSTANCE.toBson(this.filter);
      Bson __update = ConversionUtilsImpl.INSTANCE.toBson(this.update);
      com.mongodb.client.model.UpdateOptions __options = this.options.toDriverClass();
      return new com.mongodb.client.model.UpdateOneModel<T>(__filter, __update, __options);
    } else if (__ctorIndex == 2) {
      Bson __filter = ConversionUtilsImpl.INSTANCE.toBson(this.filter);
      List<? extends Bson> __updatePipeline = ConversionUtilsImpl.INSTANCE.toBsonList(this.updatePipeline);
      return new com.mongodb.client.model.UpdateOneModel<T>(__filter, __updatePipeline);
    } else if (__ctorIndex == 3) {
      Bson __filter = ConversionUtilsImpl.INSTANCE.toBson(this.filter);
      List<? extends Bson> __updatePipeline = ConversionUtilsImpl.INSTANCE.toBsonList(this.updatePipeline);
      com.mongodb.client.model.UpdateOptions __options = this.options.toDriverClass();
      return new com.mongodb.client.model.UpdateOneModel<T>(__filter, __updatePipeline, __options);
    } else {
      throw new IllegalArgumentException("unknown constructor");
    }
  }
}
