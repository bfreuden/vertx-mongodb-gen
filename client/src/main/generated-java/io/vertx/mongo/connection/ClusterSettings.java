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

import com.mongodb.ServerAddress;
import com.mongodb.connection.ClusterConnectionMode;
import com.mongodb.connection.ClusterType;
import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;
import io.vertx.mongo.connection.serializers.ClusterSettingsSerializer;
import io.vertx.mongo.impl.MongoClientContext;
import java.lang.Integer;
import java.lang.Long;
import java.lang.String;
import java.util.List;

@DataObject
public class ClusterSettings {
  private ClusterSettingsSerializer serializer = new ClusterSettingsSerializer();

  public ClusterSettings() {
  }

  public ClusterSettings(JsonObject json) {
    serializer = new ClusterSettingsSerializer(json);
  }

  public JsonObject toJson() {
    return serializer.toJson();
  }

  /**
   * @return MongoDB driver object
   * @hidden
   */
  public com.mongodb.connection.ClusterSettings toDriverClass(MongoClientContext clientContext) {
    return this.serializer.toDriverClass(clientContext);
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
   *  @see com.mongodb.MongoClientSettings.Builder#applyConnectionString(ConnectionString)
   *  @see com.mongodb.connection.ClusterSettings.Builder#applyConnectionString(ConnectionString)
   */
  public ClusterSettings setSrvHost(String srvHost) {
    this.serializer.setSrvHost(srvHost);
    return this;
  }

  /**
   *  Gets the host name from which to lookup SRV record for the seed list
   *  @return the SRV host, or null if none specified
   *  @since 3.10
   */
  public String getSrvHost() {
    return this.serializer.getSrvHost();
  }

  /**
   *  Sets the maximum number of hosts to connect to when using SRV protocol.
   *
   *  @param srvMaxHosts the maximum number of hosts to connect to when using SRV protocol
   *  @return this
   *  @since 4.4
   */
  public ClusterSettings setSrvMaxHosts(Integer srvMaxHosts) {
    this.serializer.setSrvMaxHosts(srvMaxHosts);
    return this;
  }

  /**
   *  Gets the maximum number of hosts to connect to when using SRV protocol.
   *
   *  @return the maximum number of hosts to connect to when using SRV protocol.  Defaults to null.
   *  @since 4.4
   */
  public Integer getSrvMaxHosts() {
    return this.serializer.getSrvMaxHosts();
  }

  /**
   *  Sets the SRV service name.
   *
   *  <p>
   *  The SRV resource record (<a href="https://www.rfc-editor.org/rfc/rfc2782">RFC 2782</a>)
   *  service name, which is limited to 15 characters
   *  (<a href="https://www.rfc-editor.org/rfc/rfc6335#section-5.1">RFC 6335 section 5.1</a>).
   *  If specified, it is combined with the single host name specified by
   *  {@link #getHosts()} as follows: {@code _srvServiceName._tcp.hostName}. The combined string is an SRV resource record
   *  name (<a href="https://www.rfc-editor.org/rfc/rfc1035#section-2.3.1">RFC 1035 section 2.3.1</a>), which is limited to 255
   *  characters (<a href="https://www.rfc-editor.org/rfc/rfc1035#section-2.3.4">RFC 1035 section 2.3.4</a>).
   *  </p>
   *
   *  @param srvServiceName the SRV service name
   *  @return this
   *  @since 4.5
   */
  public ClusterSettings setSrvServiceName(String srvServiceName) {
    this.serializer.setSrvServiceName(srvServiceName);
    return this;
  }

  /**
   *  Gets the SRV service name.
   *
   *  <p>
   *  The SRV resource record (<a href="https://www.rfc-editor.org/rfc/rfc2782">RFC 2782</a>)
   *  service name, which is limited to 15 characters
   *  (<a href="https://www.rfc-editor.org/rfc/rfc6335#section-5.1">RFC 6335 section 5.1</a>).
   *  If specified, it is combined with the single host name specified by
   *  {@link #getHosts()} as follows: {@code _srvServiceName._tcp.hostName}. The combined string is an SRV resource record
   *  name (<a href="https://www.rfc-editor.org/rfc/rfc1035#section-2.3.1">RFC 1035 section 2.3.1</a>), which is limited to 255
   *  characters (<a href="https://www.rfc-editor.org/rfc/rfc1035#section-2.3.4">RFC 1035 section 2.3.4</a>).
   *  </p>
   *
   *  @return the SRV service name, which defaults to {@code "mongodb"}
   *  @since 4.5
   */
  public String getSrvServiceName() {
    return this.serializer.getSrvServiceName();
  }

  /**
   *  Sets the hosts for the cluster. Any duplicate server addresses are removed from the list.
   *
   *  @param hosts the seed list of hosts
   *  @return this
   */
  public ClusterSettings setHosts(List<ServerAddress> hosts) {
    this.serializer.__setHosts(hosts);
    return this;
  }

  /**
   *  Gets the seed list of hosts for the cluster.
   *
   *  @return the seed list of hosts
   */
  public List<ServerAddress> getHosts() {
    return this.serializer.__getHosts();
  }

  /**
   *  Sets the mode for this cluster.
   *
   *  @param mode the cluster connection mode
   *  @return this;
   */
  public ClusterSettings setMode(ClusterConnectionMode mode) {
    this.serializer.setMode(mode);
    return this;
  }

  /**
   *  Gets the mode.
   *
   *  @return the mode
   */
  public ClusterConnectionMode getMode() {
    return this.serializer.getMode();
  }

  /**
   *  Sets the required replica set name for the cluster.
   *
   *  @param requiredReplicaSetName the required replica set name.
   *  @return this
   */
  public ClusterSettings setRequiredReplicaSetName(String requiredReplicaSetName) {
    this.serializer.setRequiredReplicaSetName(requiredReplicaSetName);
    return this;
  }

  /**
   *  Gets the required replica set name.
   *
   *  @return the required replica set name
   */
  public String getRequiredReplicaSetName() {
    return this.serializer.getRequiredReplicaSetName();
  }

  /**
   *  Sets the required cluster type for the cluster.
   *
   *  @param requiredClusterType the required cluster type
   *  @return this
   */
  public ClusterSettings setRequiredClusterType(ClusterType requiredClusterType) {
    this.serializer.setRequiredClusterType(requiredClusterType);
    return this;
  }

  /**
   *  Gets the required cluster type
   *
   *  @return the required cluster type
   */
  public ClusterType getRequiredClusterType() {
    return this.serializer.getRequiredClusterType();
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
    this.serializer.setLocalThreshold(localThreshold);
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
    return this.serializer.getLocalThreshold();
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
    this.serializer.setServerSelectionTimeout(serverSelectionTimeout);
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
    return this.serializer.getServerSelectionTimeout();
  }

  /**
   * @param builder MongoDB driver builder
   * @hidden
   */
  public void initializeDriverBuilderClass(MongoClientContext clientContext,
      com.mongodb.connection.ClusterSettings.Builder builder) {
    this.serializer.initializeDriverBuilderClass(clientContext, builder);
  }
}
