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
package io.vertx.mongo.connection;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;
import io.vertx.mongo.impl.MongoClientContext;
import java.lang.Long;
import java.util.concurrent.TimeUnit;

/**
 *  Settings relating to monitoring of each server.
 *
 *  @since 3.0
 */
@DataObject(
    generateConverter = true
)
public class ServerSettings {
  /**
   * the heartbeat frequency
   */
  private Long heartbeatFrequency;

  /**
   * the minimum heartbeat frequency
   */
  private Long minHeartbeatFrequency;

  public ServerSettings() {
  }

  public ServerSettings(JsonObject json) {
    ServerSettingsConverter.fromJson(json, this);
  }

  public JsonObject toJson() {
    JsonObject result = new JsonObject();
    ServerSettingsConverter.toJson(this, result);
    return result;
  }

  /**
   * @return MongoDB driver object
   * @hidden
   */
  public com.mongodb.connection.ServerSettings toDriverClass(MongoClientContext clientContext) {
    com.mongodb.connection.ServerSettings.Builder builder = com.mongodb.connection.ServerSettings.builder();
    initializeDriverBuilderClass(clientContext, builder);
    return builder.build();
  }

  /**
   *  Sets the frequency that the cluster monitor attempts to reach each server. The default value is 10 seconds.
   *
   *  @param heartbeatFrequency the heartbeat frequency (in milliseconds)
   *  @return this
   */
  public ServerSettings setHeartbeatFrequency(Long heartbeatFrequency) {
    this.heartbeatFrequency = heartbeatFrequency;
    return this;
  }

  /**
   *  Gets the frequency that the cluster monitor attempts to reach each server.  The default value is 10 seconds.
   *
   *  @return the heartbeat frequency
   */
  public Long getHeartbeatFrequency() {
    return heartbeatFrequency;
  }

  /**
   *  Sets the minimum heartbeat frequency.  In the event that the driver has to frequently re-check a server's availability, it will
   *  wait at least this long since the previous check to avoid wasted effort.  The default value is 500 milliseconds.
   *
   *  @param minHeartbeatFrequency the minimum heartbeat frequency (in milliseconds)
   *  @return this
   */
  public ServerSettings setMinHeartbeatFrequency(Long minHeartbeatFrequency) {
    this.minHeartbeatFrequency = minHeartbeatFrequency;
    return this;
  }

  /**
   *  Gets the minimum heartbeat frequency.  In the event that the driver has to frequently re-check a server's availability, it will wait
   *  at least this long since the previous check to avoid wasted effort.  The default value is 500 milliseconds.
   *
   *  @return the heartbeat reconnect retry frequency
   */
  public Long getMinHeartbeatFrequency() {
    return minHeartbeatFrequency;
  }

  /**
   * @param builder MongoDB driver builder
   * @hidden
   */
  public void initializeDriverBuilderClass(MongoClientContext clientContext,
      com.mongodb.connection.ServerSettings.Builder builder) {
    if (this.heartbeatFrequency != null) {
      builder.heartbeatFrequency(this.heartbeatFrequency, TimeUnit.MILLISECONDS);
    }
    if (this.minHeartbeatFrequency != null) {
      builder.minHeartbeatFrequency(this.minHeartbeatFrequency, TimeUnit.MILLISECONDS);
    }
  }
}
