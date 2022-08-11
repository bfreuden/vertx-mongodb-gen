package io.vertx.mongo.client.model.vault;

import io.vertx.codegen.annotations.DataObject;
import java.lang.String;

/**
 *  The options for explicit encryption.
 *
 *  @since 3.11
 */
@DataObject(
    generateConverter = true
)
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

  public EncryptOptions algorithm(String algorithm) {
    return this;
  }

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
  public EncryptOptions keyId(byte[] keyId) {
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
  public EncryptOptions keyAltName(String keyAltName) {
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
}
