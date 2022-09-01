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
   *  provider (AWS KMS or a local master key).
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
  }
}
