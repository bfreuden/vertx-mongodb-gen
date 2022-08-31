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
import java.lang.Long;
import java.util.concurrent.TimeUnit;

/**
 *  The options an estimated count operation.
 *
 *  @since 3.8
 *  @mongodb.driver.manual reference/command/count/ Count
 */
@DataObject(
    generateConverter = true
)
public class EstimatedDocumentCountOptions {
  /**
   *  the max time
   */
  private Long maxTime;

  public EstimatedDocumentCountOptions() {
  }

  public EstimatedDocumentCountOptions(JsonObject json) {
    EstimatedDocumentCountOptionsConverter.fromJson(json, this);
  }

  public JsonObject toJson() {
    JsonObject result = new JsonObject();
    EstimatedDocumentCountOptionsConverter.toJson(this, result);
    return result;
  }

  /**
   *  Sets the maximum execution time on the server for this operation.
   *
   *  @param maxTime  the max time (in milliseconds)
   *  @return this
   */
  public EstimatedDocumentCountOptions setMaxTime(Long maxTime) {
    this.maxTime = maxTime;
    return this;
  }

  /**
   *  Gets the maximum execution time on the server for this operation.  The default is 0, which places no limit on the execution time.
   *
   *  @return the maximum execution time (in milliseconds)
   */
  public Long getMaxTime() {
    return maxTime;
  }

  /**
   * @return MongoDB driver object
   * @hidden
   */
  public com.mongodb.client.model.EstimatedDocumentCountOptions toDriverClass() {
    com.mongodb.client.model.EstimatedDocumentCountOptions result = new com.mongodb.client.model.EstimatedDocumentCountOptions();
    if (this.maxTime != null) {
      result.maxTime(this.maxTime, TimeUnit.MILLISECONDS);
    }
    return result;
  }
}
