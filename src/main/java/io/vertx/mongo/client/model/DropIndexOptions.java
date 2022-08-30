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
 *  The options to apply to the command when dropping indexes.
 *
 *  @mongodb.driver.manual reference/command/dropIndexes/ Drop indexes
 *  @since 3.6
 */
@DataObject(
    generateConverter = true
)
public class DropIndexOptions {
  /**
   *  the max time
   */
  private Long maxTime;

  public DropIndexOptions() {
  }

  public DropIndexOptions(JsonObject json) {
    DropIndexOptionsConverter.fromJson(json, this);
  }

  public JsonObject toJson() {
    JsonObject result = new JsonObject();
    DropIndexOptionsConverter.toJson(this, result);
    return result;
  }

  /**
   *  Sets the maximum execution time on the server for this operation.
   *
   *  @param maxTime  the max time (in milliseconds)
   *  @return this
   */
  public DropIndexOptions setMaxTime(Long maxTime) {
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
  public com.mongodb.client.model.DropIndexOptions toDriverClass() {
    com.mongodb.client.model.DropIndexOptions result = new com.mongodb.client.model.DropIndexOptions();
    if (this.maxTime != null) {
      result.maxTime(this.maxTime, TimeUnit.MILLISECONDS);
    }
    return result;
  }
}
