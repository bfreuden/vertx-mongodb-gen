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
package io.vertx.mongo.connection.serializers;

import com.mongodb.ServerAddress;
import com.mongodb.connection.ClusterConnectionMode;
import com.mongodb.connection.ClusterSettings;
import com.mongodb.connection.ClusterType;
import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;
import io.vertx.mongo.client.impl.OptionSerializer;
import io.vertx.mongo.impl.MongoClientContext;
import io.vertx.mongo.impl.ServerAddressSerializer;
import java.lang.Long;
import java.lang.String;
import java.util.List;
import java.util.concurrent.TimeUnit;

@DataObject(
    generateConverter = true
)
public class ClusterSettingsSerializer {
  private String srvHost;

  private List<ServerAddress> hosts;

  private ClusterConnectionMode mode;

  private String requiredReplicaSetName;

  private ClusterType requiredClusterType;

  private Long localThreshold;

  private Long serverSelectionTimeout;

  public ClusterSettingsSerializer() {
  }

  public ClusterSettingsSerializer(JsonObject json) {
    ClusterSettingsSerializerConverter.fromJson(json, this);
  }

  public JsonObject toJson() {
    JsonObject result = new JsonObject();
    ClusterSettingsSerializerConverter.toJson(this, result);
    return result;
  }

  public ClusterSettings toDriverClass(MongoClientContext clientContext) {
    ClusterSettings.Builder builder = ClusterSettings.builder();
    initializeDriverBuilderClass(clientContext, builder);
    return builder.build();
  }

  public ClusterSettingsSerializer setSrvHost(String srvHost) {
    this.srvHost = srvHost;
    return this;
  }

  public String getSrvHost() {
    return srvHost;
  }

  public ClusterSettingsSerializer __setHosts(List<ServerAddress> hosts) {
    this.hosts = hosts;
    return this;
  }

  public ClusterSettingsSerializer setHosts(List<ServerAddressSerializer> hosts) {
    this.hosts = OptionSerializer.fromSerializerList(hosts);
    return this;
  }

  public List<ServerAddress> __getHosts() {
    return hosts;
  }

  public List<ServerAddressSerializer> getHosts() {
    return OptionSerializer.toSerializerList(this.hosts, ServerAddressSerializer.class, ServerAddress.class);
  }

  public ClusterSettingsSerializer setMode(ClusterConnectionMode mode) {
    this.mode = mode;
    return this;
  }

  public ClusterConnectionMode getMode() {
    return mode;
  }

  public ClusterSettingsSerializer setRequiredReplicaSetName(String requiredReplicaSetName) {
    this.requiredReplicaSetName = requiredReplicaSetName;
    return this;
  }

  public String getRequiredReplicaSetName() {
    return requiredReplicaSetName;
  }

  public ClusterSettingsSerializer setRequiredClusterType(ClusterType requiredClusterType) {
    this.requiredClusterType = requiredClusterType;
    return this;
  }

  public ClusterType getRequiredClusterType() {
    return requiredClusterType;
  }

  public ClusterSettingsSerializer setLocalThreshold(Long localThreshold) {
    this.localThreshold = localThreshold;
    return this;
  }

  public Long getLocalThreshold() {
    return localThreshold;
  }

  public ClusterSettingsSerializer setServerSelectionTimeout(Long serverSelectionTimeout) {
    this.serverSelectionTimeout = serverSelectionTimeout;
    return this;
  }

  public Long getServerSelectionTimeout() {
    return serverSelectionTimeout;
  }

  /**
   * @param builder MongoDB driver builder
   * @hidden
   */
  public void initializeDriverBuilderClass(MongoClientContext clientContext,
      ClusterSettings.Builder builder) {
    if (this.srvHost != null) {
      builder.srvHost(this.srvHost);
    }
    if (this.hosts != null) {
      builder.hosts(this.hosts);
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
