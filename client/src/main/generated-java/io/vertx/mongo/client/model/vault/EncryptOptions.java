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

import io.vertx.mongo.impl.MongoClientContext;
import java.lang.Long;
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
   * the contention factor, which must be {@code >= 0} or null.
   */
  private Long contentionFactor;

  /**
   * the query type
   */
  private String queryType;

  /**
   *  Gets the encryption algorithm, which must be either:
   *
   *  <ul>
   *      <li>AEAD_AES_256_CBC_HMAC_SHA_512-Deterministic</li>
   *      <li>AEAD_AES_256_CBC_HMAC_SHA_512-Random</li>
   *      <li>Indexed</li>
   *      <li>Unindexed</li>
   *  </ul>
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
   *  The contention factor.
   *
   *  <p>It is an error to set contentionFactor when algorithm is not "Indexed".
   *  @param contentionFactor the contention factor, which must be {@code >= 0} or null.
   *  @return this
   *  @since 4.7
   */
  public EncryptOptions setContentionFactor(Long contentionFactor) {
    this.contentionFactor = contentionFactor;
    return this;
  }

  /**
   *  Gets the contention factor.
   *
   *  @see #contentionFactor(Long)
   *  @return the contention factor
   *  @since 4.7
   */
  public Long getContentionFactor() {
    return contentionFactor;
  }

  /**
   *  The QueryType.
   *
   *  <p>Currently, we support only "equality" queryType.</p>
   *  <p>It is an error to set queryType when the algorithm is not "Indexed".</p>
   *
   *  @param queryType the query type
   *  @return this
   *  @since 4.7
   */
  public EncryptOptions setQueryType(String queryType) {
    this.queryType = queryType;
    return this;
  }

  /**
   *  Gets the QueryType.
   *
   *  <p>Currently, we support only "equality" queryType.</p>
   *  @see #queryType(String)
   *  @return the queryType or null
   *  @since 4.7
   */
  public String getQueryType() {
    return queryType;
  }

  /**
   * @return MongoDB driver object
   * @hidden
   */
  public com.mongodb.client.model.vault.EncryptOptions toDriverClass(
      MongoClientContext clientContext) {
    if (this.algorithm == null) {
      throw new IllegalArgumentException("algorithm is mandatory");
    }
    com.mongodb.client.model.vault.EncryptOptions result = new com.mongodb.client.model.vault.EncryptOptions(this.algorithm);
    if (this.keyId != null) {
      result.keyId(clientContext.getMapper().toBsonBinary(this.keyId));
    }
    if (this.keyAltName != null) {
      result.keyAltName(this.keyAltName);
    }
    if (this.contentionFactor != null) {
      result.contentionFactor(this.contentionFactor);
    }
    if (this.queryType != null) {
      result.queryType(this.queryType);
    }
    return result;
  }
}
