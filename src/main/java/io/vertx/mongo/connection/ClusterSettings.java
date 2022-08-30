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

import com.mongodb.connection.ClusterConnectionMode;
import com.mongodb.connection.ClusterType;
import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;
import java.lang.Long;
import java.lang.String;
import java.util.concurrent.TimeUnit;

/**
 *  Settings for the cluster.
 *
 *  @since 3.0
 */
@DataObject(
    generateConverter = true
)
public class ClusterSettings {
  /**
   * the SRV host name
   */
  private String srvHost;

  /**
   * the cluster connection mode
   */
  private ClusterConnectionMode mode;

  /**
   * the required replica set name.
   */
  private String requiredReplicaSetName;

  /**
   * the required cluster type
   */
  private ClusterType requiredClusterType;

  /**
   * the acceptable latency difference, in milliseconds, which must be &gt;= 0
   */
  private Long localThreshold;

  /**
   * the timeout
   */
  private Long serverSelectionTimeout;

  public ClusterSettings() {
  }

  public ClusterSettings(JsonObject json) {
    ClusterSettingsConverter.fromJson(json, this);
  }

  /**
   * @return MongoDB driver object
   * @hidden
   */
  public com.mongodb.connection.ClusterSettings toDriverClass() {
    com.mongodb.connection.ClusterSettings.Builder builder = com.mongodb.connection.ClusterSettings.builder();
    initializeDriverBuilderClass(builder);
    return builder.build();
  }

  /**
   *  Sets the host name to use in order to look up an SRV DNS record to find the MongoDB hosts.
   *
   *  <p>
   *  Note that when setting srvHost via {@code ClusterSettings.Builder}, the driver will NOT process any associated TXT records
   *  associated with the host.  In order to enable the processing of TXT records while still using {@code MongoClientSettings},
   *  specify the SRV host via connection string and apply the connection string to the settings, e.g.
   *  {@code MongoClientSettings.builder().applyConnectionString(new ConnectionString("mongodb+srv://host1.acme.com")) }.
   *  </p>
   *
   *  @param srvHost the SRV host name
   *  @return this
   */
  public ClusterSettings setSrvHost(String srvHost) {
    this.srvHost = srvHost;
    return this;
  }

  /**
   *  Gets the host name from which to lookup SRV record for the seed list
   *  @return the SRV host, or null if none specified
   *  @since 3.10
   */
  public String getSrvHost() {
    return srvHost;
  }

  /**
   *  Sets the mode for this cluster.
   *
   *  @param mode the cluster connection mode
   *  @return this;
   */
  public ClusterSettings setMode(ClusterConnectionMode mode) {
    this.mode = mode;
    return this;
  }

  /**
   *  Gets the mode.
   *
   *  @return the mode
   */
  public ClusterConnectionMode getMode() {
    return mode;
  }

  /**
   *  Sets the required replica set name for the cluster.
   *
   *  @param requiredReplicaSetName the required replica set name.
   *  @return this
   */
  public ClusterSettings setRequiredReplicaSetName(String requiredReplicaSetName) {
    this.requiredReplicaSetName = requiredReplicaSetName;
    return this;
  }

  /**
   *  Gets the required replica set name.
   *
   *  @return the required replica set name
   */
  public String getRequiredReplicaSetName() {
    return requiredReplicaSetName;
  }

  /**
   *  Sets the required cluster type for the cluster.
   *
   *  @param requiredClusterType the required cluster type
   *  @return this
   */
  public ClusterSettings setRequiredClusterType(ClusterType requiredClusterType) {
    this.requiredClusterType = requiredClusterType;
    return this;
  }

  /**
   *  Gets the required cluster type
   *
   *  @return the required cluster type
   */
  public ClusterType getRequiredClusterType() {
    return requiredClusterType;
  }

  /**
   *  Sets the local threshold.
   *
   *  @param localThreshold the acceptable latency difference, in milliseconds, which must be &gt;= 0 (in milliseconds)
   *  @throws IllegalArgumentException if {@code localThreshold < 0}
   *  @return this
   *  @since 3.7
   */
  public ClusterSettings setLocalThreshold(Long localThreshold) {
    this.localThreshold = localThreshold;
    return this;
  }

  /**
   *  Gets the local threshold.  When choosing among multiple MongoDB servers to send a request, the MongoClient will only
   *  send that request to a server whose ping time is less than or equal to the server with the fastest ping time plus the local
   *  threshold.
   *
   *  <p>For example, let's say that the client is choosing a server to send a query when the read preference is {@code
   *  ReadPreference.secondary()}, and that there are three secondaries, server1, server2, and server3, whose ping times are 10, 15, and 16
   *  milliseconds, respectively.  With a local threshold of 5 milliseconds, the client will send the query to either
   *  server1 or server2 (randomly selecting between the two).
   *  </p>
   *
   *  <p>Default is 15 milliseconds.</p>
   *
   *  @return the local threshold in the given timeunit.
   *  @since 3.7
   *  @mongodb.driver.manual reference/program/mongos/#cmdoption--localThreshold Local Threshold
   */
  public Long getLocalThreshold() {
    return localThreshold;
  }

  /**
   *  Sets the timeout to apply when selecting a server.  If the timeout expires before a server is found to handle a request, a
   *  {@link com.mongodb.MongoTimeoutException} will be thrown.  The default value is 30 seconds.
   *
   *  <p> A value of 0 means that it will timeout immediately if no server is available.  A negative value means to wait
   *  indefinitely.</p>
   *
   *  @param serverSelectionTimeout the timeout (in milliseconds)
   *  @return this
   */
  public ClusterSettings setServerSelectionTimeout(Long serverSelectionTimeout) {
    this.serverSelectionTimeout = serverSelectionTimeout;
    return this;
  }

  /**
   *  Gets the timeout to apply when selecting a server.  If the timeout expires before a server is found to
   *  handle a request, a {@link com.mongodb.MongoTimeoutException} will be thrown.  The default value is 30 seconds.
   *
   *  <p> A value of 0 means that it will timeout immediately if no server is available.  A negative value means to wait
   *  indefinitely.</p>
   *
   *  @return the timeout (in milliseconds)
   */
  public Long getServerSelectionTimeout() {
    return serverSelectionTimeout;
  }

  /**
   * @param builder MongoDB driver builder
   * @hidden
   */
  public void initializeDriverBuilderClass(com.mongodb.connection.ClusterSettings.Builder builder) {
    if (this.srvHost != null) {
      builder.srvHost(this.srvHost);
    }
    if (this.mode != null) {
      builder.mode(this.mode);
    }
    if (this.requiredReplicaSetName != null) {
      builder.requiredReplicaSetName(this.requiredReplicaSetName);
    }
    if (this.requiredClusterType != null) {
      builder.requiredClusterType(this.requiredClusterType);
    }
    if (this.localThreshold != null) {
      builder.localThreshold(this.localThreshold, TimeUnit.MILLISECONDS);
    }
    if (this.serverSelectionTimeout != null) {
      builder.serverSelectionTimeout(this.serverSelectionTimeout, TimeUnit.MILLISECONDS);
    }
  }
}
