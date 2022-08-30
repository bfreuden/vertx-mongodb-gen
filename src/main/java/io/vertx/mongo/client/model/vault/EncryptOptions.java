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

import io.vertx.mongo.impl.ConversionUtilsImpl;
import java.lang.String;

/**
 *  The options for explicit encryption.
 *
 *  @since 3.11
 */
public class EncryptOptions {
  private String algorithm;

  /**
   * the key identifier
   */
  private byte[] keyId;

  /**
   * the alternate key name
   */
  private String keyAltName;

  /**
   *  Gets the encryption algorithm, which must be either "AEAD_AES_256_CBC_HMAC_SHA_512-Deterministic" or
   *  "AEAD_AES_256_CBC_HMAC_SHA_512-Random".
   *
   *  @return the encryption algorithm
   */
  public String getAlgorithm() {
    return algorithm;
  }

  /**
   *  Sets the key identifier
   *
   *  @param keyId the key identifier
   *  @return this
   *  @see #getKeyId()
   */
  public EncryptOptions setKeyId(byte[] keyId) {
    this.keyId = keyId;
    return this;
  }

  /**
   *  Gets the key identifier.
   *
   *  <p>
   *  Identifies the data key by its _id value. The value is a UUID (binary subtype 4).
   *  </p>
   *
   *  @return the key identifier
   */
  public byte[] getKeyId() {
    return keyId;
  }

  /**
   *  Sets the alternate key name
   *
   *  @param keyAltName the alternate key name
   *  @return this
   *  @see #getKeyAltName()
   */
  public EncryptOptions setKeyAltName(String keyAltName) {
    this.keyAltName = keyAltName;
    return this;
  }

  /**
   *  Gets the alternate name with which to look up the key.
   *
   *  <p>
   *  Identifies the alternate key name to look up the key by.
   *  </p>
   *
   *  @return the alternate name
   */
  public String getKeyAltName() {
    return keyAltName;
  }

  /**
   * @return MongoDB driver object
   * @hidden
   */
  public com.mongodb.client.model.vault.EncryptOptions toDriverClass() {
    if (this.algorithm == null) {
      throw new IllegalArgumentException("algorithm is mandatory");
    }
    com.mongodb.client.model.vault.EncryptOptions result = new com.mongodb.client.model.vault.EncryptOptions(this.algorithm);
    if (this.keyId != null) {
      result.keyId(ConversionUtilsImpl.INSTANCE.toBsonBinary(this.keyId));
    }
    if (this.keyAltName != null) {
      result.keyAltName(this.keyAltName);
    }
    return result;
  }
}
