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

import com.mongodb.MongoCompressor;
import com.mongodb.MongoCredential;
import com.mongodb.ReadConcern;
import com.mongodb.ReadPreference;
import com.mongodb.WriteConcern;
import io.vertx.codegen.annotations.DataObject;
import io.vertx.codegen.annotations.GenIgnore;
import io.vertx.core.json.JsonObject;
import io.vertx.mongo.connection.ClusterSettings;
import io.vertx.mongo.connection.ConnectionPoolSettings;
import io.vertx.mongo.connection.ServerSettings;
import io.vertx.mongo.connection.SocketSettings;
import io.vertx.mongo.connection.SslSettings;
import io.vertx.mongo.impl.MongoCompressorListSerializer;
import io.vertx.mongo.impl.MongoCredentialSerializer;
import io.vertx.mongo.impl.ReadConcernSerializer;
import io.vertx.mongo.impl.ReadPreferenceSerializer;
import io.vertx.mongo.impl.WriteConcernSerializer;
import java.lang.Boolean;
import java.lang.String;
import java.util.List;

/**
 *  Various settings to control the behavior of a {@code MongoClient}.
 *
 *  @since 3.7
 */
@DataObject(
    generateConverter = true
)
public class MongoClientSettings {
  /**
   * the block to apply to the ClusterSettings.
   */
  private ClusterSettings clusterSettings;

  /**
   * the block to apply to the SocketSettings.
   */
  private SocketSettings socketSettings;

  /**
   * the block to apply to the ConnectionPoolSettings.
   */
  private ConnectionPoolSettings connectionPoolSettings;

  /**
   * the block to apply to the ServerSettings.
   */
  private ServerSettings serverSettings;

  /**
   * the block to apply to the SslSettings.
   */
  private SslSettings sslSettings;

  /**
   * read preference
   */
  private ReadPreferenceSerializer readPreference;

  /**
   * the write concern
   */
  private WriteConcernSerializer writeConcern;

  /**
   * sets if writes should be retried if they fail due to a network error.
   */
  private Boolean retryWrites;

  /**
   * sets if reads should be retried if they fail due to a network error.
   */
  private Boolean retryReads;

  /**
   * the read concern
   */
  private ReadConcernSerializer readConcern;

  /**
   * the credential
   */
  private MongoCredentialSerializer credential;

  /**
   * the logical name of the application using this MongoClient.  It may be null.
   */
  private String applicationName;

  /**
   * the list of compressors to request
   */
  private MongoCompressorListSerializer compressorList;

  /**
   * the auto-encryption settings
   */
  private AutoEncryptionSettings autoEncryptionSettings;

  public MongoClientSettings() {
  }

  public MongoClientSettings(JsonObject json) {
    MongoClientSettingsConverter.fromJson(json, this);
  }

  public JsonObject toJson() {
    JsonObject result = new JsonObject();
    MongoClientSettingsConverter.toJson(this, result);
    return result;
  }

  /**
   * @return MongoDB driver object
   * @hidden
   */
  public com.mongodb.MongoClientSettings toDriverClass() {
    com.mongodb.MongoClientSettings.Builder builder = com.mongodb.MongoClientSettings.builder();
    initializeDriverBuilderClass(builder);
    return builder.build();
  }

  /**
   *  Applies the {@link ClusterSettings.Builder} block and then sets the clusterSettings.
   *
   *  @param block the block to apply to the ClusterSettings.
   *  @return this
   *  @see MongoClientSettings#getClusterSettings()
   */
  public MongoClientSettings setClusterSettings(ClusterSettings block) {
    this.clusterSettings = block;
    return this;
  }

  /**
   *  Gets the cluster settings.
   *
   *  @return the cluster settings
   */
  public ClusterSettings getClusterSettings() {
    return clusterSettings;
  }

  /**
   *  Applies the {@link SocketSettings.Builder} block and then sets the socketSettings.
   *
   *  @param block the block to apply to the SocketSettings.
   *  @return this
   *  @see MongoClientSettings#getSocketSettings()
   */
  public MongoClientSettings setSocketSettings(SocketSettings block) {
    this.socketSettings = block;
    return this;
  }

  /**
   *  Gets the connection-specific settings wrapped in a settings object.   This settings object uses the values for connectTimeout,
   *  socketTimeout and socketKeepAlive.
   *
   *  @return a SocketSettings object populated with the connection settings from this {@code MongoClientSettings} instance.
   *  @see SocketSettings
   */
  public SocketSettings getSocketSettings() {
    return socketSettings;
  }

  /**
   *  Applies the {@link ConnectionPoolSettings.Builder} block and then sets the connectionPoolSettings.
   *
   *  @param block the block to apply to the ConnectionPoolSettings.
   *  @return this
   *  @see MongoClientSettings#getConnectionPoolSettings()
   */
  public MongoClientSettings setConnectionPoolSettings(ConnectionPoolSettings block) {
    this.connectionPoolSettings = block;
    return this;
  }

  /**
   *  Gets the settings for the connection provider in a settings object.  This settings object wraps the values for minConnectionPoolSize,
   *  maxConnectionPoolSize, maxWaitTime, maxConnectionIdleTime and maxConnectionLifeTime.
   *
   *  @return a ConnectionPoolSettings populated with the settings from this {@code MongoClientSettings} instance that relate to the
   *  connection provider.
   *  @see ConnectionPoolSettings
   */
  public ConnectionPoolSettings getConnectionPoolSettings() {
    return connectionPoolSettings;
  }

  /**
   *  Applies the {@link ServerSettings.Builder} block and then sets the serverSettings.
   *
   *  @param block the block to apply to the ServerSettings.
   *  @return this
   *  @see MongoClientSettings#getServerSettings()
   */
  public MongoClientSettings setServerSettings(ServerSettings block) {
    this.serverSettings = block;
    return this;
  }

  /**
   *  Gets the server-specific settings wrapped in a settings object.  This settings object uses the heartbeatFrequency and
   *  minHeartbeatFrequency values from this {@code MongoClientSettings} instance.
   *
   *  @return a ServerSettings
   *  @see ServerSettings
   */
  public ServerSettings getServerSettings() {
    return serverSettings;
  }

  /**
   *  Applies the {@link SslSettings.Builder} block and then sets the sslSettings.
   *
   *  @param block the block to apply to the SslSettings.
   *  @return this
   *  @see MongoClientSettings#getSslSettings()
   */
  public MongoClientSettings setSslSettings(SslSettings block) {
    this.sslSettings = block;
    return this;
  }

  /**
   *  Gets the SSL settings.
   *
   *  @return the SSL settings
   */
  public SslSettings getSslSettings() {
    return sslSettings;
  }

  /**
   *  Sets the read preference.
   *
   *  @param readPreference read preference
   *  @return this
   *  @see MongoClientSettings#getReadPreference()
   */
  @GenIgnore
  public MongoClientSettings setReadPreference(ReadPreference readPreference) {
    if (readPreference == null) {
      this.readPreference = null;
    } else if (this.readPreference == null) {
      this.readPreference = new ReadPreferenceSerializer(readPreference);
    } else {
      this.readPreference.setValue(readPreference);
    }
    return this;
  }

  /**
   *  Sets the read preference.
   *
   *  @param readPreference read preference
   *  @return this
   *  @see MongoClientSettings#getReadPreference()
   *
   * @hidden
   */
  public MongoClientSettings setReadPreference(ReadPreferenceSerializer readPreference) {
    this.readPreference = readPreference;
    return this;
  }

  /**
   *  The read preference to use for queries, map-reduce, aggregation, and count.
   *
   *  <p>Default is {@code ReadPreference.primary()}.</p>
   *
   *  @return the read preference
   *  @see ReadPreference#primary()
   */
  @GenIgnore
  public ReadPreference getMongoReadPreference() {
    if (this.readPreference == null) {
      return null;
    } else {
      return readPreference.getValue();
    }
  }

  /**
   *  The read preference to use for queries, map-reduce, aggregation, and count.
   *
   *  <p>Default is {@code ReadPreference.primary()}.</p>
   *
   *  @return the read preference
   *  @see ReadPreference#primary()
   *
   * @hidden
   */
  public ReadPreferenceSerializer getReadPreference() {
    return readPreference;
  }

  /**
   *  Sets the write concern.
   *
   *  @param writeConcern the write concern
   *  @return this
   *  @see MongoClientSettings#getWriteConcern()
   */
  @GenIgnore
  public MongoClientSettings setWriteConcern(WriteConcern writeConcern) {
    if (writeConcern == null) {
      this.writeConcern = null;
    } else if (this.writeConcern == null) {
      this.writeConcern = new WriteConcernSerializer(writeConcern);
    } else {
      this.writeConcern.setValue(writeConcern);
    }
    return this;
  }

  /**
   *  Sets the write concern.
   *
   *  @param writeConcern the write concern
   *  @return this
   *  @see MongoClientSettings#getWriteConcern()
   *
   * @hidden
   */
  public MongoClientSettings setWriteConcern(WriteConcernSerializer writeConcern) {
    this.writeConcern = writeConcern;
    return this;
  }

  /**
   *  The write concern to use.
   *
   *  <p>Default is {@code WriteConcern.ACKNOWLEDGED}.</p>
   *
   *  @return the write concern
   *  @see WriteConcern#ACKNOWLEDGED
   */
  @GenIgnore
  public WriteConcern getMongoWriteConcern() {
    if (this.writeConcern == null) {
      return null;
    } else {
      return writeConcern.getValue();
    }
  }

  /**
   *  The write concern to use.
   *
   *  <p>Default is {@code WriteConcern.ACKNOWLEDGED}.</p>
   *
   *  @return the write concern
   *  @see WriteConcern#ACKNOWLEDGED
   *
   * @hidden
   */
  public WriteConcernSerializer getWriteConcern() {
    return writeConcern;
  }

  /**
   *  Sets whether writes should be retried if they fail due to a network error.
   *
   *  <p>Starting with the 3.11.0 release, the default value is true</p>
   *
   *  @param retryWrites sets if writes should be retried if they fail due to a network error.
   *  @return this
   *  @see #getRetryWrites()
   *  @mongodb.server.release 3.6
   */
  public MongoClientSettings setRetryWrites(Boolean retryWrites) {
    this.retryWrites = retryWrites;
    return this;
  }

  /**
   *  Returns true if writes should be retried if they fail due to a network error or other retryable error.
   *
   *  <p>Starting with the 3.11.0 release, the default value is true</p>
   *
   *  @return the retryWrites value
   *  @mongodb.server.release 3.6
   */
  public Boolean isRetryWrites() {
    return retryWrites;
  }

  /**
   *  Sets whether reads should be retried if they fail due to a network error.
   *
   *  @param retryReads sets if reads should be retried if they fail due to a network error.
   *  @return this
   *  @see #getRetryReads()
   *  @since 3.11
   *  @mongodb.server.release 3.6
   */
  public MongoClientSettings setRetryReads(Boolean retryReads) {
    this.retryReads = retryReads;
    return this;
  }

  /**
   *  Returns true if reads should be retried if they fail due to a network error or other retryable error. The default value is true.
   *
   *  @return the retryReads value
   *  @since 3.11
   *  @mongodb.server.release 3.6
   */
  public Boolean isRetryReads() {
    return retryReads;
  }

  /**
   *  Sets the read concern.
   *
   *  @param readConcern the read concern
   *  @return this
   *  @mongodb.server.release 3.2
   *  @mongodb.driver.manual reference/readConcern/ Read Concern
   */
  @GenIgnore
  public MongoClientSettings setReadConcern(ReadConcern readConcern) {
    if (readConcern == null) {
      this.readConcern = null;
    } else if (this.readConcern == null) {
      this.readConcern = new ReadConcernSerializer(readConcern);
    } else {
      this.readConcern.setValue(readConcern);
    }
    return this;
  }

  /**
   *  Sets the read concern.
   *
   *  @param readConcern the read concern
   *  @return this
   *  @mongodb.server.release 3.2
   *  @mongodb.driver.manual reference/readConcern/ Read Concern
   *
   * @hidden
   */
  public MongoClientSettings setReadConcern(ReadConcernSerializer readConcern) {
    this.readConcern = readConcern;
    return this;
  }

  /**
   *  The read concern to use.
   *
   *  @return the read concern
   *  @mongodb.server.release 3.2
   *  @mongodb.driver.manual reference/readConcern/ Read Concern
   */
  @GenIgnore
  public ReadConcern getMongoReadConcern() {
    if (this.readConcern == null) {
      return null;
    } else {
      return readConcern.getValue();
    }
  }

  /**
   *  The read concern to use.
   *
   *  @return the read concern
   *  @mongodb.server.release 3.2
   *  @mongodb.driver.manual reference/readConcern/ Read Concern
   *
   * @hidden
   */
  public ReadConcernSerializer getReadConcern() {
    return readConcern;
  }

  /**
   *  Sets the credential.
   *
   *  @param credential the credential
   *  @return this
   */
  @GenIgnore
  public MongoClientSettings setCredential(MongoCredential credential) {
    if (credential == null) {
      this.credential = null;
    } else if (this.credential == null) {
      this.credential = new MongoCredentialSerializer(credential);
    } else {
      this.credential.setValue(credential);
    }
    return this;
  }

  /**
   *  Sets the credential.
   *
   *  @param credential the credential
   *  @return this
   *
   * @hidden
   */
  public MongoClientSettings setCredential(MongoCredentialSerializer credential) {
    this.credential = credential;
    return this;
  }

  /**
   *  Gets the credential.
   *
   *  @return the credential, which may be null
   */
  @GenIgnore
  public MongoCredential getMongoCredential() {
    if (this.credential == null) {
      return null;
    } else {
      return credential.getValue();
    }
  }

  /**
   *  Gets the credential.
   *
   *  @return the credential, which may be null
   *
   * @hidden
   */
  public MongoCredentialSerializer getCredential() {
    return credential;
  }

  /**
   *  Sets the logical name of the application using this MongoClient.  The application name may be used by the client to identify
   *  the application to the server, for use in server logs, slow query logs, and profile collection.
   *
   *  @param applicationName the logical name of the application using this MongoClient.  It may be null.
   *                         The UTF-8 encoding may not exceed 128 bytes.
   *  @return this
   *  @see #getApplicationName()
   *  @mongodb.server.release 3.4
   */
  public MongoClientSettings setApplicationName(String applicationName) {
    this.applicationName = applicationName;
    return this;
  }

  /**
   *  Gets the logical name of the application using this MongoClient.  The application name may be used by the client to identify
   *  the application to the server, for use in server logs, slow query logs, and profile collection.
   *
   *  <p>Default is null.</p>
   *
   *  @return the application name, which may be null
   *  @mongodb.server.release 3.4
   */
  public String getApplicationName() {
    return applicationName;
  }

  /**
   *  Sets the compressors to use for compressing messages to the server. The driver will use the first compressor in the list
   *  that the server is configured to support.
   *
   *  @param compressorList the list of compressors to request
   *  @return this
   *  @see #getCompressorList()
   *  @mongodb.server.release 3.4
   */
  @GenIgnore
  public MongoClientSettings setCompressorList(List<MongoCompressor> compressorList) {
    if (compressorList == null) {
      this.compressorList = null;
    } else if (this.compressorList == null) {
      this.compressorList = new MongoCompressorListSerializer(compressorList);
    } else {
      this.compressorList.setValue(compressorList);
    }
    return this;
  }

  /**
   *  Sets the compressors to use for compressing messages to the server. The driver will use the first compressor in the list
   *  that the server is configured to support.
   *
   *  @param compressorList the list of compressors to request
   *  @return this
   *  @see #getCompressorList()
   *  @mongodb.server.release 3.4
   *
   * @hidden
   */
  public MongoClientSettings setCompressorList(MongoCompressorListSerializer compressorList) {
    this.compressorList = compressorList;
    return this;
  }

  @GenIgnore
  public List<MongoCompressor> getMongoCompressorList() {
    if (this.compressorList == null) {
      return null;
    } else {
      return compressorList.getValue();
    }
  }

  public MongoCompressorListSerializer getCompressorList() {
    return compressorList;
  }

  /**
   *  Sets the auto-encryption settings
   *
   *  @param autoEncryptionSettings the auto-encryption settings
   *  @return this
   *  @since 3.11
   *  @see #getAutoEncryptionSettings()
   */
  public MongoClientSettings setAutoEncryptionSettings(
      AutoEncryptionSettings autoEncryptionSettings) {
    this.autoEncryptionSettings = autoEncryptionSettings;
    return this;
  }

  /**
   *  Gets the auto-encryption settings.
   *  <p>
   *  Client side encryption enables an application to specify what fields in a collection must be
   *  encrypted, and the driver automatically encrypts commands and decrypts results.
   *  </p>
   *  <p>
   *  Automatic encryption is an enterprise only feature that only applies to operations on a collection. Automatic encryption is not
   *  supported for operations on a database or view and will result in error. To bypass automatic encryption,
   *  set bypassAutoEncryption=true in ClientSideEncryptionOptions.
   *  </p>
   *  <p>
   *  Explicit encryption/decryption and automatic decryption is a community feature, enabled with the new
   *  {@code com.mongodb.client.vault .ClientEncryption} type. A MongoClient configured with bypassAutoEncryption=true will still
   *  automatically decrypt.
   *  </p>
   *  <p>
   *  Automatic encryption requires the authenticated user to have the listCollections privilege action.
   *  </p>
   *  <p>
   *  Note: support for client side encryption is in beta.  Backwards-breaking changes may be made before the final release.
   *  </p>
   *
   *  @return the auto-encryption settings, which may be null
   *  @since 3.11
   */
  public AutoEncryptionSettings getAutoEncryptionSettings() {
    return autoEncryptionSettings;
  }

  /**
   * @param builder MongoDB driver builder
   * @hidden
   */
  public void initializeDriverBuilderClass(com.mongodb.MongoClientSettings.Builder builder) {
    if (this.clusterSettings != null) {
      builder.applyToClusterSettings(_builder -> clusterSettings.initializeDriverBuilderClass(_builder));
    }
    if (this.socketSettings != null) {
      builder.applyToSocketSettings(_builder -> socketSettings.initializeDriverBuilderClass(_builder));
    }
    if (this.connectionPoolSettings != null) {
      builder.applyToConnectionPoolSettings(_builder -> connectionPoolSettings.initializeDriverBuilderClass(_builder));
    }
    if (this.serverSettings != null) {
      builder.applyToServerSettings(_builder -> serverSettings.initializeDriverBuilderClass(_builder));
    }
    if (this.sslSettings != null) {
      builder.applyToSslSettings(_builder -> sslSettings.initializeDriverBuilderClass(_builder));
    }
    if (this.readPreference.getValue() != null) {
      builder.readPreference(this.readPreference.getValue());
    }
    if (this.writeConcern.getValue() != null) {
      builder.writeConcern(this.writeConcern.getValue());
    }
    if (this.retryWrites != null) {
      builder.retryWrites(this.retryWrites);
    }
    if (this.retryReads != null) {
      builder.retryReads(this.retryReads);
    }
    if (this.readConcern.getValue() != null) {
      builder.readConcern(this.readConcern.getValue());
    }
    if (this.credential.getValue() != null) {
      builder.credential(this.credential.getValue());
    }
    if (this.applicationName != null) {
      builder.applicationName(this.applicationName);
    }
    if (this.compressorList.getValue() != null) {
      builder.compressorList(this.compressorList.getValue());
    }
    if (this.autoEncryptionSettings != null) {
      builder.autoEncryptionSettings(this.autoEncryptionSettings.toDriverClass());
    }
  }
}