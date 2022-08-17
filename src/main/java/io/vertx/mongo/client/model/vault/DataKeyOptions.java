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
package io.vertx.mongo.client.model.vault;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;
import io.vertx.mongo.impl.ConversionUtilsImpl;
import java.util.List;

/**
 *  The options for creating a data key.
 *
 *  @since 3.11
 */
@DataObject(
    generateConverter = true
)
public class DataKeyOptions {
  /**
   * a list of alternate key names
   */
  private List keyAltNames;

  /**
   * the master key document
   */
  private JsonObject masterKey;

  /**
   *  Set the alternate key names.
   *
   *  @param keyAltNames a list of alternate key names
   *  @return this
   *  @see #getKeyAltNames()
   */
  public DataKeyOptions keyAltNames(List keyAltNames) {
    return this;
  }

  /**
   *  Gets the alternate key names.
   *
   *  <p>
   *  An optional list of alternate names used to reference a key. If a key is created with alternate names, then encryption may refer
   *  to the key by the unique alternate name instead of by _id.
   *  </p>
   *
   *  @return the list of alternate key names
   */
  public List getKeyAltNames() {
    return keyAltNames;
  }

  /**
   *  Sets the master key document.
   *
   *  @param masterKey the master key document
   *  @return this
   *  @see #getMasterKey()
   */
  public DataKeyOptions masterKey(JsonObject masterKey) {
    return this;
  }

  /**
   *  Gets the master key document
   *
   *  <p>
   *  The masterKey identifies a KMS-specific key used to encrypt the new data key. If the kmsProvider is "aws" it is required and
   *  must have the following fields:
   *  </p>
   *  <ul>
   *    <li>region: a String containing the AWS region in which to locate the master key</li>
   *    <li>key: a String containing the Amazon Resource Name (ARN) to the AWS customer master key</li>
   *  </ul>
   *  <p>
   *  If the kmsProvider is "local" the masterKey is not applicable.
   *  </p>
   *  @return the master key document
   */
  public JsonObject getMasterKey() {
    return masterKey;
  }

  /**
   * @return MongoDB driver object
   * @hidden
   */
  public com.mongodb.client.model.vault.DataKeyOptions toDriverClass() {
    com.mongodb.client.model.vault.DataKeyOptions result = new com.mongodb.client.model.vault.DataKeyOptions();
    if (this.keyAltNames != null) {
      result.keyAltNames(this.keyAltNames);
    }
    if (this.masterKey != null) {
      result.masterKey(ConversionUtilsImpl.INSTANCE.toBsonDocument(this.masterKey));
    }
    return result;
  }
}
