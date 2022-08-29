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
package io.vertx.mongo;

import io.vertx.codegen.annotations.DataObject;
import java.lang.Boolean;

/**
 *  Options to apply to hedged reads in the server.
 *
 *  @since 4.1
 *  @mongodb.server.release 4.4
 */
@DataObject(
    generateConverter = true
)
public class ReadPreferenceHedgeOptions {
  /**
   * true if hedged reads are enabled
   */
  private Boolean enabled;

  /**
   * @return MongoDB driver object
   * @hidden
   */
  public com.mongodb.ReadPreferenceHedgeOptions toDriverClass() {
    com.mongodb.ReadPreferenceHedgeOptions.Builder builder = com.mongodb.ReadPreferenceHedgeOptions.builder();
    initializeDriverBuilderClass(builder);
    return builder.build();
  }

  /**
   *  Sets whether hedged reads are enabled in the server.
   *
   *  @param enabled true if hedged reads are enabled
   *  @return this
   */
  public ReadPreferenceHedgeOptions enabled(Boolean enabled) {
    this.enabled = enabled;
    return this;
  }

  /**
   *  Gets whether hedged reads are enabled in the server.
   *
   *  @return true if hedged reads are enabled in the server
   */
  public Boolean isEnabled() {
    return enabled;
  }

  /**
   * @param builder MongoDB driver builder
   * @hidden
   */
  public void initializeDriverBuilderClass(com.mongodb.ReadPreferenceHedgeOptions.Builder builder) {
    if (this.enabled != null) {
      builder.enabled(this.enabled);
    }
  }
}
