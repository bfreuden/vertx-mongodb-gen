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
import io.vertx.core.json.JsonObject;
import io.vertx.mongo.connection.ClusterSettings;
import io.vertx.mongo.connection.ConnectionPoolSettings;
import io.vertx.mongo.connection.ServerSettings;
import io.vertx.mongo.connection.SocketSettings;
import io.vertx.mongo.connection.SslSettings;
import io.vertx.mongo.serializers.MongoClientSettingsSerializer;
import java.lang.Boolean;
import java.lang.String;
import java.util.List;

@DataObject
public class MongoClientSettings {
  private MongoClientSettingsSerializer serializer = new MongoClientSettingsSerializer();

  public MongoClientSettings() {
  }

  public MongoClientSettings(JsonObject json) {
    serializer = new MongoClientSettingsSerializer(json);
  }

  public JsonObject toJson() {
    return serializer.toJson();
  }

  /**
   * @return MongoDB driver object
   * @hidden
   */
  public com.mongodb.MongoClientSettings toDriverClass() {
    return this.serializer.toDriverClass();
  }

  /**
   *  Applies the {@link ClusterSettings.Builder} block and then sets the clusterSettings.
   *
   *  @param block the block to apply to the ClusterSettings.
   *  @return this
   *  @see MongoClientSettings#getClusterSettings()
   */
  public MongoClientSettings setClusterSettings(ClusterSettings block) {
    this.serializer.setClusterSettings(block);
    return this;
  }

  /**
   *  Gets the cluster settings.
   *
   *  @return the cluster settings
   */
  public ClusterSettings getClusterSettings() {
    return this.serializer.getClusterSettings();
  }

  /**
   *  Applies the {@link SocketSettings.Builder} block and then sets the socketSettings.
   *
   *  @param block the block to apply to the SocketSettings.
   *  @return this
   *  @see MongoClientSettings#getSocketSettings()
   */
  public MongoClientSettings setSocketSettings(SocketSettings block) {
    this.serializer.setSocketSettings(block);
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
    return this.serializer.getSocketSettings();
  }

  /**
   *  Applies the {@link ConnectionPoolSettings.Builder} block and then sets the connectionPoolSettings.
   *
   *  @param block the block to apply to the ConnectionPoolSettings.
   *  @return this
   *  @see MongoClientSettings#getConnectionPoolSettings()
   */
  public MongoClientSettings setConnectionPoolSettings(ConnectionPoolSettings block) {
    this.serializer.setConnectionPoolSettings(block);
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
    return this.serializer.getConnectionPoolSettings();
  }

  /**
   *  Applies the {@link ServerSettings.Builder} block and then sets the serverSettings.
   *
   *  @param block the block to apply to the ServerSettings.
   *  @return this
   *  @see MongoClientSettings#getServerSettings()
   */
  public MongoClientSettings setServerSettings(ServerSettings block) {
    this.serializer.setServerSettings(block);
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
    return this.serializer.getServerSettings();
  }

  /**
   *  Applies the {@link SslSettings.Builder} block and then sets the sslSettings.
   *
   *  @param block the block to apply to the SslSettings.
   *  @return this
   *  @see MongoClientSettings#getSslSettings()
   */
  public MongoClientSettings setSslSettings(SslSettings block) {
    this.serializer.setSslSettings(block);
    return this;
  }

  /**
   *  Gets the SSL settings.
   *
   *  @return the SSL settings
   */
  public SslSettings getSslSettings() {
    return this.serializer.getSslSettings();
  }

  /**
   *  Sets the read preference.
   *
   *  @param readPreference read preference
   *  @return this
   *  @see MongoClientSettings#getReadPreference()
   */
  public MongoClientSettings setReadPreference(ReadPreference readPreference) {
    this.serializer.__setReadPreference(readPreference);
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
  public ReadPreference getReadPreference() {
    return this.serializer.__getReadPreference();
  }

  /**
   *  Sets the write concern.
   *
   *  @param writeConcern the write concern
   *  @return this
   *  @see MongoClientSettings#getWriteConcern()
   */
  public MongoClientSettings setWriteConcern(WriteConcern writeConcern) {
    this.serializer.__setWriteConcern(writeConcern);
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
  public WriteConcern getWriteConcern() {
    return this.serializer.__getWriteConcern();
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
    this.serializer.setRetryWrites(retryWrites);
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
    return this.serializer.isRetryWrites();
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
    this.serializer.setRetryReads(retryReads);
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
    return this.serializer.isRetryReads();
  }

  /**
   *  Sets the read concern.
   *
   *  @param readConcern the read concern
   *  @return this
   *  @mongodb.server.release 3.2
   *  @mongodb.driver.manual reference/readConcern/ Read Concern
   */
  public MongoClientSettings setReadConcern(ReadConcern readConcern) {
    this.serializer.__setReadConcern(readConcern);
    return this;
  }

  /**
   *  The read concern to use.
   *
   *  @return the read concern
   *  @mongodb.server.release 3.2
   *  @mongodb.driver.manual reference/readConcern/ Read Concern
   */
  public ReadConcern getReadConcern() {
    return this.serializer.__getReadConcern();
  }

  /**
   *  Sets the credential.
   *
   *  @param credential the credential
   *  @return this
   */
  public MongoClientSettings setCredential(MongoCredential credential) {
    this.serializer.__setCredential(credential);
    return this;
  }

  /**
   *  Gets the credential.
   *
   *  @return the credential, which may be null
   */
  public MongoCredential getCredential() {
    return this.serializer.__getCredential();
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
    this.serializer.setApplicationName(applicationName);
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
    return this.serializer.getApplicationName();
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
  public MongoClientSettings setCompressorList(List<MongoCompressor> compressorList) {
    this.serializer.__setCompressorList(compressorList);
    return this;
  }

  public List<MongoCompressor> getCompressorList() {
    return this.serializer.__getCompressorList();
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
    this.serializer.setAutoEncryptionSettings(autoEncryptionSettings);
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
    return this.serializer.getAutoEncryptionSettings();
  }

  /**
   * @param builder MongoDB driver builder
   * @hidden
   */
  public void initializeDriverBuilderClass(com.mongodb.MongoClientSettings.Builder builder) {
    this.serializer.initializeDriverBuilderClass(builder);
  }
}
