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
import java.lang.Integer;
import java.lang.Long;
import java.util.concurrent.TimeUnit;

/**
 *  All settings that relate to the pool of connections to a MongoDB server.
 *
 *  @since 3.0
 */
@DataObject(
    generateConverter = true
)
public class ConnectionPoolSettings {
  /**
   * the maximum number of connections in the pool.
   */
  private Integer maxSize;

  /**
   * the minimum number of connections to have in the pool at all times.
   */
  private Integer minSize;

  /**
   * the maximum amount of time to wait
   */
  private Long maxWaitTime;

  /**
   * the maximum length of time a connection can live
   */
  private Long maxConnectionLifeTime;

  /**
   * the maximum time a connection can be unused
   */
  private Long maxConnectionIdleTime;

  /**
   * the time period to wait
   */
  private Long maintenanceInitialDelay;

  /**
   * the time period between runs of the maintenance job
   */
  private Long maintenanceFrequency;

  public ConnectionPoolSettings() {
  }

  public ConnectionPoolSettings(JsonObject json) {
    ConnectionPoolSettingsConverter.fromJson(json, this);
  }

  public JsonObject toJson() {
    JsonObject result = new JsonObject();
    ConnectionPoolSettingsConverter.toJson(this, result);
    return result;
  }

  /**
   * @return MongoDB driver object
   * @hidden
   */
  public com.mongodb.connection.ConnectionPoolSettings toDriverClass() {
    com.mongodb.connection.ConnectionPoolSettings.Builder builder = com.mongodb.connection.ConnectionPoolSettings.builder();
    initializeDriverBuilderClass(builder);
    return builder.build();
  }

  /**
   *  <p>The maximum number of connections allowed. Those connections will be kept in the pool when idle. Once the pool is exhausted,
   *  any operation requiring a connection will block waiting for an available connection.</p>
   *
   *  <p>Default is 100.</p>
   *
   *  @param maxSize the maximum number of connections in the pool.
   *  @return this
   */
  public ConnectionPoolSettings setMaxSize(Integer maxSize) {
    this.maxSize = maxSize;
    return this;
  }

  /**
   *  <p>The maximum number of connections allowed. Those connections will be kept in the pool when idle. Once the pool is exhausted, any
   *  operation requiring a connection will block waiting for an available connection.</p>
   *
   *  <p>Default is 100.</p>
   *
   *  @return the maximum number of connections in the pool.
   */
  public Integer getMaxSize() {
    return maxSize;
  }

  /**
   *  <p>The minimum number of connections. Those connections will be kept in the pool when idle, and the pool will ensure that it
   *  contains at least this minimum number.</p>
   *
   *  <p>Default is 0.</p>
   *
   *  @param minSize the minimum number of connections to have in the pool at all times.
   *  @return this
   */
  public ConnectionPoolSettings setMinSize(Integer minSize) {
    this.minSize = minSize;
    return this;
  }

  /**
   *  <p>The minimum number of connections. Those connections will be kept in the pool when idle, and the pool will ensure that it contains
   *  at least this minimum number.</p>
   *
   *  <p>Default is 0.</p>
   *
   *  @return the minimum number of connections to have in the pool at all times.
   */
  public Integer getMinSize() {
    return minSize;
  }

  /**
   *  <p>The maximum time that a thread may wait for a connection to become available.</p>
   *
   *  <p>Default is 2 minutes. A value of 0 means that it will not wait.  A negative value means it will wait indefinitely.</p>
   *
   *  @param maxWaitTime the maximum amount of time to wait (in milliseconds)
   *  @return this
   */
  public ConnectionPoolSettings setMaxWaitTime(Long maxWaitTime) {
    this.maxWaitTime = maxWaitTime;
    return this;
  }

  /**
   *  <p>The maximum time that a thread may wait for a connection to become available.</p>
   *
   *  <p>Default is 2 minutes. A value of 0 means that it will not wait.  A negative value means it will wait indefinitely.</p>
   *
   *  @return the maximum amount of time to wait in the given TimeUnits
   */
  public Long getMaxWaitTime() {
    return maxWaitTime;
  }

  /**
   *  The maximum time a pooled connection can live for.  A zero value indicates no limit to the life time.  A pooled connection that
   *  has exceeded its life time will be closed and replaced when necessary by a new connection.
   *
   *  @param maxConnectionLifeTime the maximum length of time a connection can live (in milliseconds)
   *  @return this
   */
  public ConnectionPoolSettings setMaxConnectionLifeTime(Long maxConnectionLifeTime) {
    this.maxConnectionLifeTime = maxConnectionLifeTime;
    return this;
  }

  /**
   *  The maximum time a pooled connection can live for.  A zero value indicates no limit to the life time.  A pooled connection that has
   *  exceeded its life time will be closed and replaced when necessary by a new connection.
   *
   *  @return the maximum length of time a connection can live in the given TimeUnits
   */
  public Long getMaxConnectionLifeTime() {
    return maxConnectionLifeTime;
  }

  /**
   *  The maximum idle time of a pooled connection.  A zero value indicates no limit to the idle time.  A pooled connection that has
   *  exceeded its idle time will be closed and replaced when necessary by a new connection.
   *
   *  @param maxConnectionIdleTime the maximum time a connection can be unused (in milliseconds)
   *  @return this
   */
  public ConnectionPoolSettings setMaxConnectionIdleTime(Long maxConnectionIdleTime) {
    this.maxConnectionIdleTime = maxConnectionIdleTime;
    return this;
  }

  /**
   *  Returns the maximum idle time of a pooled connection.  A zero value indicates no limit to the idle time.  A pooled connection that
   *  has exceeded its idle time will be closed and replaced when necessary by a new connection.
   *
   *  @return the maximum time a connection can be unused, in the given TimeUnits
   */
  public Long getMaxConnectionIdleTime() {
    return maxConnectionIdleTime;
  }

  /**
   *  The period of time to wait before running the first maintenance job on the connection pool.
   *
   *  @param maintenanceInitialDelay the time period to wait (in milliseconds)
   *  @return this
   */
  public ConnectionPoolSettings setMaintenanceInitialDelay(Long maintenanceInitialDelay) {
    this.maintenanceInitialDelay = maintenanceInitialDelay;
    return this;
  }

  /**
   *  Returns the period of time to wait before running the first maintenance job on the connection pool.
   *
   *  @return the time period to wait in the given units
   */
  public Long getMaintenanceInitialDelay() {
    return maintenanceInitialDelay;
  }

  /**
   *  The time period between runs of the maintenance job.
   *
   *  @param maintenanceFrequency the time period between runs of the maintenance job (in milliseconds)
   *  @return this
   */
  public ConnectionPoolSettings setMaintenanceFrequency(Long maintenanceFrequency) {
    this.maintenanceFrequency = maintenanceFrequency;
    return this;
  }

  /**
   *  Returns the time period between runs of the maintenance job.
   *
   *  @return the time period between runs of the maintainance job in the given units
   */
  public Long getMaintenanceFrequency() {
    return maintenanceFrequency;
  }

  /**
   * @param builder MongoDB driver builder
   * @hidden
   */
  public void initializeDriverBuilderClass(
      com.mongodb.connection.ConnectionPoolSettings.Builder builder) {
    if (this.maxSize != null) {
      builder.maxSize(this.maxSize);
    }
    if (this.minSize != null) {
      builder.minSize(this.minSize);
    }
    if (this.maxWaitTime != null) {
      builder.maxWaitTime(this.maxWaitTime, TimeUnit.MILLISECONDS);
    }
    if (this.maxConnectionLifeTime != null) {
      builder.maxConnectionLifeTime(this.maxConnectionLifeTime, TimeUnit.MILLISECONDS);
    }
    if (this.maxConnectionIdleTime != null) {
      builder.maxConnectionIdleTime(this.maxConnectionIdleTime, TimeUnit.MILLISECONDS);
    }
    if (this.maintenanceInitialDelay != null) {
      builder.maintenanceInitialDelay(this.maintenanceInitialDelay, TimeUnit.MILLISECONDS);
    }
    if (this.maintenanceFrequency != null) {
      builder.maintenanceFrequency(this.maintenanceFrequency, TimeUnit.MILLISECONDS);
    }
  }
}
