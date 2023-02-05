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
import io.vertx.core.json.JsonObject;
import io.vertx.mongo.impl.CollectionsConversionUtils;
import io.vertx.mongo.impl.MongoClientContext;
import java.lang.Boolean;
import java.lang.String;
import java.util.Map;

/**
 *  The client-side automatic encryption settings. Client side encryption enables an application to specify what fields in a collection
 *  must be encrypted, and the driver automatically encrypts commands sent to MongoDB and decrypts responses.
 *  <p>
 *  Automatic encryption is an enterprise only feature that only applies to operations on a collection. Automatic encryption is not
 *  supported for operations on a database or view and will result in error. To bypass automatic encryption,
 *  set bypassAutoEncryption=true in {@code AutoEncryptionSettings}.
 *  </p>
 *  <p>
 *  Explicit encryption/decryption and automatic decryption is a community feature, enabled with the new
 *  {@code com.mongodb.client.vault.ClientEncryption} type.
 *  </p>
 *  <p>
 *  A MongoClient configured with bypassAutoEncryption=true will still automatically decrypt.
 *  </p>
 *  <p>
 *  If automatic encryption fails on an operation, use a MongoClient configured with bypassAutoEncryption=true and use
 *  ClientEncryption#encrypt to manually encrypt values.
 *  </p>
 *  <p>
 *  Enabling client side encryption reduces the maximum document and message size (using a maxBsonObjectSize of 2MiB and
 *  maxMessageSizeBytes of 6MB) and may have a negative performance impact.
 *  </p>
 *  <p>
 *  Automatic encryption requires the authenticated user to have the listCollections privilege action.
 *  </p>
 *  <p>
 *  Supplying an {@code encryptedFieldsMap} provides more security than relying on an encryptedFields obtained from the server.
 *  It protects against a malicious server advertising false encryptedFields.
 *  </p>
 *
 *  @since 3.11
 */
@DataObject(
    generateConverter = true
)
public class AutoEncryptionSettings {
  /**
   * the key vault namespace, which may not be null
   */
  private String keyVaultNamespace;

  /**
   * the map from namespace to local schema document
   */
  private Map<String, JsonObject> schemaMap;

  /**
   * whether auto-encryption should be bypassed
   */
  private Boolean bypassAutoEncryption;

  /**
   * the mapping of the collection namespace to the encryptedFields
   */
  private Map<String, JsonObject> encryptedFieldsMap;

  /**
   * whether query analysis should be bypassed
   */
  private Boolean bypassQueryAnalysis;

  public AutoEncryptionSettings() {
  }

  public AutoEncryptionSettings(JsonObject json) {
    AutoEncryptionSettingsConverter.fromJson(json, this);
  }

  public JsonObject toJson() {
    JsonObject result = new JsonObject();
    AutoEncryptionSettingsConverter.toJson(this, result);
    return result;
  }

  /**
   * @return MongoDB driver object
   * @hidden
   */
  public com.mongodb.AutoEncryptionSettings toDriverClass(MongoClientContext clientContext) {
    com.mongodb.AutoEncryptionSettings.Builder builder = com.mongodb.AutoEncryptionSettings.builder();
    initializeDriverBuilderClass(clientContext, builder);
    return builder.build();
  }

  /**
   *  Sets the key vault namespace
   *
   *  @param keyVaultNamespace the key vault namespace, which may not be null
   *  @return this
   *  @see #getKeyVaultNamespace()
   */
  public AutoEncryptionSettings setKeyVaultNamespace(String keyVaultNamespace) {
    this.keyVaultNamespace = keyVaultNamespace;
    return this;
  }

  /**
   *  Gets the key vault namespace.
   *
   *  <p>
   *  The key vault namespace refers to a collection that contains all data keys used for encryption and decryption (aka the key vault
   *  collection). Data keys are stored as documents in a special MongoDB collection. Data keys are protected with encryption by a KMS
   *  provider (AWS, Azure, GCP KMS or a local master key).
   *  </p>
   *
   *  @return the key vault namespace, which may not be null
   */
  public String getKeyVaultNamespace() {
    return keyVaultNamespace;
  }

  /**
   *  Sets the map from namespace to local schema document
   *
   *  @param schemaMap the map from namespace to local schema document
   *  @return this
   *  @see #getSchemaMap()
   */
  public AutoEncryptionSettings setSchemaMap(Map<String, JsonObject> schemaMap) {
    this.schemaMap = schemaMap;
    return this;
  }

  /**
   *  Gets the map of namespace to local JSON schema.
   *  <p>
   *  Automatic encryption is configured with an "encrypt" field in a collection's JSONSchema. By default, a collection's JSONSchema is
   *  periodically polled with the listCollections command. But a JSONSchema may be specified locally with the schemaMap option.
   *  </p>
   *  <p>
   *  The key into the map is the full namespace of the collection, which is {@code &lt;database name>.&lt;collection name>}.  For
   *  example, if the database name is {@code "test"} and the collection name is {@code "users"}, then the namesspace is
   *  {@code "test.users"}.
   *  </p>
   *  <p>
   *  Supplying a schemaMap provides more security than relying on JSON Schemas obtained from the server. It protects against a
   *  malicious server advertising a false JSON Schema, which could trick the client into sending unencrypted data that should be
   *  encrypted.
   *  </p>
   *  <p>
   *  Schemas supplied in the schemaMap only apply to configuring automatic encryption for client side encryption. Other validation
   *  rules in the JSON schema will not be enforced by the driver and will result in an error.
   *  </p>
   *
   *  @return map of namespace to local JSON schema
   */
  public Map<String, JsonObject> getSchemaMap() {
    return schemaMap;
  }

  /**
   *  Sets whether auto-encryption should be bypassed.
   *
   *  @param bypassAutoEncryption whether auto-encryption should be bypassed
   *  @return this
   *  @see #isBypassAutoEncryption()
   */
  public AutoEncryptionSettings setBypassAutoEncryption(Boolean bypassAutoEncryption) {
    this.bypassAutoEncryption = bypassAutoEncryption;
    return this;
  }

  /**
   *  Gets whether auto-encryption should be bypassed.  Even when this option is true, auto-decryption is still enabled.
   *  <p>
   *  This option is useful for cases where the driver throws an exception because it is unable to prove that the command does not
   *  contain any fields that should be automatically encrypted, but the application is able to determine that it does not.  For these
   *  cases, the application can construct a {@code MongoClient} with {@code AutoEncryptionSettings} with {@code bypassAutoEncryption}
   *  enabled.
   *  </p>
   *
   *  @return true if auto-encryption should be bypassed
   */
  public Boolean isBypassAutoEncryption() {
    return bypassAutoEncryption;
  }

  /**
   *  Maps a collection namespace to an encryptedFields.
   *
   *  <p><strong>Note:</strong> only applies to queryable encryption.
   *  Automatic encryption in queryable encryption is configured with the encryptedFields.</p>
   *  <p>If a collection is present in both the {@code encryptedFieldsMap} and {@link #schemaMap}, the driver will error.</p>
   *  <p>If a collection is present on the {@code encryptedFieldsMap}, the behavior of {@code collection.createCollection()} and
   *  {@code collection.drop()} is altered.</p>
   *
   *  <p>If a collection is not present on the {@code encryptedFieldsMap} a server-side collection {@code encryptedFieldsMap} may be
   *  used by the driver.
   *
   *  @param encryptedFieldsMap the mapping of the collection namespace to the encryptedFields
   *  @return this
   *  @since 4.7
   */
  public AutoEncryptionSettings setEncryptedFieldsMap(Map<String, JsonObject> encryptedFieldsMap) {
    this.encryptedFieldsMap = encryptedFieldsMap;
    return this;
  }

  /**
   *  Gets the mapping of a collection namespace to encryptedFields.
   *
   *  <p><strong>Note:</strong> only applies to Queryable Encryption.
   *  Automatic encryption in Queryable Encryption is configured with the encryptedFields.</p>
   *  <p>If a collection is present in both the {@code encryptedFieldsMap} and {@link #schemaMap}, the driver will error.</p>
   *  <p>If a collection is present on the {@code encryptedFieldsMap}, the behavior of {@code collection.createCollection()} and
   *  {@code collection.drop()} is altered.</p>
   *
   *  <p>If a collection is not present on the {@code encryptedFieldsMap} a server-side collection {@code encryptedFieldsMap} may be
   *  used by the driver.</p>
   *
   *  @return the mapping of the collection namespaces to encryptedFields
   *  @since 4.7
   */
  public Map<String, JsonObject> getEncryptedFieldsMap() {
    return encryptedFieldsMap;
  }

  /**
   *  Enable or disable automatic analysis of outgoing commands.
   *
   *  <p>Set bypassQueryAnalysis to true to use explicit encryption on indexed fields
   *  without the MongoDB Enterprise Advanced licensed crypt shared library.</p>
   *
   *  @param bypassQueryAnalysis whether query analysis should be bypassed
   *  @return this
   *  @since 4.7
   */
  public AutoEncryptionSettings setBypassQueryAnalysis(Boolean bypassQueryAnalysis) {
    this.bypassQueryAnalysis = bypassQueryAnalysis;
    return this;
  }

  /**
   *  Gets whether automatic analysis of outgoing commands is set.
   *
   *  <p>Set bypassQueryAnalysis to true to use explicit encryption on indexed fields
   *  without the MongoDB Enterprise Advanced licensed crypt shared library.</p>
   *
   *  @return true if query analysis should be bypassed
   *  @since 4.7
   */
  public Boolean isBypassQueryAnalysis() {
    return bypassQueryAnalysis;
  }

  /**
   * @param builder MongoDB driver builder
   * @hidden
   */
  public void initializeDriverBuilderClass(MongoClientContext clientContext,
      com.mongodb.AutoEncryptionSettings.Builder builder) {
    if (this.keyVaultNamespace != null) {
      builder.keyVaultNamespace(this.keyVaultNamespace);
    }
    if (this.schemaMap != null) {
      builder.schemaMap(CollectionsConversionUtils.mapValues(this.schemaMap, clientContext.getMapper()::toBsonDocument));
    }
    if (this.bypassAutoEncryption != null) {
      builder.bypassAutoEncryption(this.bypassAutoEncryption);
    }
    if (this.encryptedFieldsMap != null) {
      builder.encryptedFieldsMap(CollectionsConversionUtils.mapValues(this.encryptedFieldsMap, clientContext.getMapper()::toBsonDocument));
    }
    if (this.bypassQueryAnalysis != null) {
      builder.bypassQueryAnalysis(this.bypassQueryAnalysis);
    }
  }
}
