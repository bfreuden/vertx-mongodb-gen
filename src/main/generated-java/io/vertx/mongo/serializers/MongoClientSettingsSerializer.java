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
package io.vertx.mongo.serializers;

import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCompressor;
import com.mongodb.MongoCredential;
import com.mongodb.ReadConcern;
import com.mongodb.ReadPreference;
import com.mongodb.WriteConcern;
import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;
import io.vertx.mongo.AutoEncryptionSettings;
import io.vertx.mongo.ContextProvider;
import io.vertx.mongo.ServerApi;
import io.vertx.mongo.client.impl.OptionSerializer;
import io.vertx.mongo.connection.ClusterSettings;
import io.vertx.mongo.connection.ConnectionPoolSettings;
import io.vertx.mongo.connection.ServerSettings;
import io.vertx.mongo.connection.SocketSettings;
import io.vertx.mongo.connection.SslSettings;
import io.vertx.mongo.impl.MongoClientContext;
import io.vertx.mongo.impl.MongoCompressorSerializer;
import io.vertx.mongo.impl.MongoCredentialSerializer;
import io.vertx.mongo.impl.ReadConcernSerializer;
import io.vertx.mongo.impl.ReadPreferenceSerializer;
import io.vertx.mongo.impl.WriteConcernSerializer;
import java.lang.Boolean;
import java.lang.String;
import java.util.List;

@DataObject(
    generateConverter = true
)
public class MongoClientSettingsSerializer {
  private ClusterSettings clusterSettings;

  private SocketSettings socketSettings;

  private ConnectionPoolSettings connectionPoolSettings;

  private ServerSettings serverSettings;

  private SslSettings sslSettings;

  private ReadPreference readPreference;

  private WriteConcern writeConcern;

  private Boolean retryWrites;

  private Boolean retryReads;

  private ReadConcern readConcern;

  private MongoCredential credential;

  private String applicationName;

  private List<MongoCompressor> compressorList;

  private ServerApi serverApi;

  private AutoEncryptionSettings autoEncryptionSettings;

  private ContextProvider contextProvider;

  public MongoClientSettingsSerializer() {
  }

  public MongoClientSettingsSerializer(JsonObject json) {
    MongoClientSettingsSerializerConverter.fromJson(json, this);
  }

  public JsonObject toJson() {
    JsonObject result = new JsonObject();
    MongoClientSettingsSerializerConverter.toJson(this, result);
    return result;
  }

  public MongoClientSettings toDriverClass(MongoClientContext clientContext) {
    MongoClientSettings.Builder builder = MongoClientSettings.builder();
    initializeDriverBuilderClass(clientContext, builder);
    return builder.build();
  }

  public MongoClientSettingsSerializer setClusterSettings(ClusterSettings block) {
    this.clusterSettings = block;
    return this;
  }

  public ClusterSettings getClusterSettings() {
    return clusterSettings;
  }

  public MongoClientSettingsSerializer setSocketSettings(SocketSettings block) {
    this.socketSettings = block;
    return this;
  }

  public SocketSettings getSocketSettings() {
    return socketSettings;
  }

  public MongoClientSettingsSerializer setConnectionPoolSettings(ConnectionPoolSettings block) {
    this.connectionPoolSettings = block;
    return this;
  }

  public ConnectionPoolSettings getConnectionPoolSettings() {
    return connectionPoolSettings;
  }

  public MongoClientSettingsSerializer setServerSettings(ServerSettings block) {
    this.serverSettings = block;
    return this;
  }

  public ServerSettings getServerSettings() {
    return serverSettings;
  }

  public MongoClientSettingsSerializer setSslSettings(SslSettings block) {
    this.sslSettings = block;
    return this;
  }

  public SslSettings getSslSettings() {
    return sslSettings;
  }

  public MongoClientSettingsSerializer __setReadPreference(ReadPreference readPreference) {
    this.readPreference = readPreference;
    return this;
  }

  public MongoClientSettingsSerializer setReadPreference(ReadPreferenceSerializer readPreference) {
    this.readPreference = readPreference == null ? null : readPreference.getValue();
    return this;
  }

  public ReadPreference __getReadPreference() {
    return readPreference;
  }

  public ReadPreferenceSerializer getReadPreference() {
    return this.readPreference == null ? null : new ReadPreferenceSerializer(this.readPreference);
  }

  public MongoClientSettingsSerializer __setWriteConcern(WriteConcern writeConcern) {
    this.writeConcern = writeConcern;
    return this;
  }

  public MongoClientSettingsSerializer setWriteConcern(WriteConcernSerializer writeConcern) {
    this.writeConcern = writeConcern == null ? null : writeConcern.getValue();
    return this;
  }

  public WriteConcern __getWriteConcern() {
    return writeConcern;
  }

  public WriteConcernSerializer getWriteConcern() {
    return this.writeConcern == null ? null : new WriteConcernSerializer(this.writeConcern);
  }

  public MongoClientSettingsSerializer setRetryWrites(Boolean retryWrites) {
    this.retryWrites = retryWrites;
    return this;
  }

  public Boolean isRetryWrites() {
    return retryWrites;
  }

  public MongoClientSettingsSerializer setRetryReads(Boolean retryReads) {
    this.retryReads = retryReads;
    return this;
  }

  public Boolean isRetryReads() {
    return retryReads;
  }

  public MongoClientSettingsSerializer __setReadConcern(ReadConcern readConcern) {
    this.readConcern = readConcern;
    return this;
  }

  public MongoClientSettingsSerializer setReadConcern(ReadConcernSerializer readConcern) {
    this.readConcern = readConcern == null ? null : readConcern.getValue();
    return this;
  }

  public ReadConcern __getReadConcern() {
    return readConcern;
  }

  public ReadConcernSerializer getReadConcern() {
    return this.readConcern == null ? null : new ReadConcernSerializer(this.readConcern);
  }

  public MongoClientSettingsSerializer __setCredential(MongoCredential credential) {
    this.credential = credential;
    return this;
  }

  public MongoClientSettingsSerializer setCredential(MongoCredentialSerializer credential) {
    this.credential = credential == null ? null : credential.getValue();
    return this;
  }

  public MongoCredential __getCredential() {
    return credential;
  }

  public MongoCredentialSerializer getCredential() {
    return this.credential == null ? null : new MongoCredentialSerializer(this.credential);
  }

  public MongoClientSettingsSerializer setApplicationName(String applicationName) {
    this.applicationName = applicationName;
    return this;
  }

  public String getApplicationName() {
    return applicationName;
  }

  public MongoClientSettingsSerializer __setCompressorList(List<MongoCompressor> compressorList) {
    this.compressorList = compressorList;
    return this;
  }

  public MongoClientSettingsSerializer setCompressorList(
      List<MongoCompressorSerializer> compressorList) {
    this.compressorList = OptionSerializer.fromSerializerList(compressorList);
    return this;
  }

  public List<MongoCompressor> __getCompressorList() {
    return compressorList;
  }

  public List<MongoCompressorSerializer> getCompressorList() {
    return OptionSerializer.toSerializerList(this.compressorList, MongoCompressorSerializer.class, MongoCompressor.class);
  }

  public MongoClientSettingsSerializer setServerApi(ServerApi serverApi) {
    this.serverApi = serverApi;
    return this;
  }

  public ServerApi getServerApi() {
    return serverApi;
  }

  public MongoClientSettingsSerializer setAutoEncryptionSettings(
      AutoEncryptionSettings autoEncryptionSettings) {
    this.autoEncryptionSettings = autoEncryptionSettings;
    return this;
  }

  public AutoEncryptionSettings getAutoEncryptionSettings() {
    return autoEncryptionSettings;
  }

  public MongoClientSettingsSerializer setContextProvider(ContextProvider contextProvider) {
    this.contextProvider = contextProvider;
    return this;
  }

  public ContextProvider getContextProvider() {
    return contextProvider;
  }

  /**
   * @param builder MongoDB driver builder
   * @hidden
   */
  public void initializeDriverBuilderClass(MongoClientContext clientContext,
      MongoClientSettings.Builder builder) {
    if (this.clusterSettings != null) {
      builder.applyToClusterSettings(_builder -> clusterSettings.initializeDriverBuilderClass(clientContext, _builder));
    }
    if (this.socketSettings != null) {
      builder.applyToSocketSettings(_builder -> socketSettings.initializeDriverBuilderClass(clientContext, _builder));
    }
    if (this.connectionPoolSettings != null) {
      builder.applyToConnectionPoolSettings(_builder -> connectionPoolSettings.initializeDriverBuilderClass(clientContext, _builder));
    }
    if (this.serverSettings != null) {
      builder.applyToServerSettings(_builder -> serverSettings.initializeDriverBuilderClass(clientContext, _builder));
    }
    if (this.sslSettings != null) {
      builder.applyToSslSettings(_builder -> sslSettings.initializeDriverBuilderClass(clientContext, _builder));
    }
    if (this.readPreference != null) {
      builder.readPreference(this.readPreference);
    }
    if (this.writeConcern != null) {
      builder.writeConcern(this.writeConcern);
    }
    if (this.retryWrites != null) {
      builder.retryWrites(this.retryWrites);
    }
    if (this.retryReads != null) {
      builder.retryReads(this.retryReads);
    }
    if (this.readConcern != null) {
      builder.readConcern(this.readConcern);
    }
    if (this.credential != null) {
      builder.credential(this.credential);
    }
    if (this.applicationName != null) {
      builder.applicationName(this.applicationName);
    }
    if (this.compressorList != null) {
      builder.compressorList(this.compressorList);
    }
    if (this.serverApi != null) {
      builder.serverApi(this.serverApi.toDriverClass(clientContext));
    }
    if (this.autoEncryptionSettings != null) {
      builder.autoEncryptionSettings(this.autoEncryptionSettings.toDriverClass(clientContext));
    }
    if (this.contextProvider != null) {
      builder.contextProvider(this.contextProvider.toDriverClass(clientContext));
    }
  }
}
