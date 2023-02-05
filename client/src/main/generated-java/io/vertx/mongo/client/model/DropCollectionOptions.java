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
package io.vertx.mongo.client.model;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;
import io.vertx.mongo.impl.MongoClientContext;

/**
 *  Options for dropping a collection
 *
 *  @mongodb.driver.manual reference/command/drop/ Drop Collection
 *  @mongodb.driver.manual core/security-client-side-encryption/ Client side encryption
 *  @since 4.7
 */
@DataObject(
    generateConverter = true
)
public class DropCollectionOptions {
  /**
   * the encrypted fields document
   */
  private JsonObject encryptedFields;

  public DropCollectionOptions() {
  }

  public DropCollectionOptions(JsonObject json) {
    DropCollectionOptionsConverter.fromJson(json, this);
  }

  public JsonObject toJson() {
    JsonObject result = new JsonObject();
    DropCollectionOptionsConverter.toJson(this, result);
    return result;
  }

  /**
   *  Sets the encrypted fields document
   *
   *  <p>Explicitly set encrypted fields.</p>
   *  <p>Note: If not set the driver will lookup the namespace in {@link AutoEncryptionSettings#getEncryptedFieldsMap()}</p>
   *  @param encryptedFields the encrypted fields document
   *  @return this
   *  @since 4.7
   *  @mongodb.driver.manual core/security-client-side-encryption/ Client side encryption
   */
  public DropCollectionOptions setEncryptedFields(JsonObject encryptedFields) {
    this.encryptedFields = encryptedFields;
    return this;
  }

  /**
   *  Gets any explicitly set encrypted fields.
   *
   *  <p>Note: If not set the driver will lookup the namespace in {@link AutoEncryptionSettings#getEncryptedFieldsMap()}</p>
   *  @return the encrypted fields document
   *  @since 4.7
   */
  public JsonObject getEncryptedFields() {
    return encryptedFields;
  }

  /**
   * @return MongoDB driver object
   * @hidden
   */
  public com.mongodb.client.model.DropCollectionOptions toDriverClass(
      MongoClientContext clientContext) {
    com.mongodb.client.model.DropCollectionOptions result = new com.mongodb.client.model.DropCollectionOptions();
    if (this.encryptedFields != null) {
      result.encryptedFields(clientContext.getMapper().toBson(this.encryptedFields));
    }
    return result;
  }
}
