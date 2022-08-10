package io.vertx.mongo.client.vault;

import com.mongodb.client.model.vault.DataKeyOptions;
import com.mongodb.client.model.vault.EncryptOptions;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import java.io.Closeable;
import java.lang.Object;
import java.lang.String;

/**
 *  The Key vault.
 *  <p>
 *  Used to create data encryption keys, and to explicitly encrypt and decrypt values when auto-encryption is not an option.
 *  </p>
 *  <p>
 *  Note: support for client-side encryption should be considered as beta.  Backwards-breaking changes may be made before the final
 *  release.
 *  </p>
 *  @since 1.12
 */
public interface ClientEncryption extends Closeable {
  /**
   *  Create a data key with the given KMS provider.
   *  <p>
   *  Creates a new key document and inserts into the key vault collection.
   *  </p>
   *  @param kmsProvider the KMS provider
   *  @return a future containing the identifier for the created data key
   */
  Future<byte[]> createDataKey(String kmsProvider);

  /**
   *  Create a data key with the given KMS provider.
   *  <p>
   *  Creates a new key document and inserts into the key vault collection.
   *  </p>
   *  @param kmsProvider the KMS provider
   *  @param handler an async result containing the identifier for the created data key
   *  @return a reference to <code>this</code>
   */
  ClientEncryption createDataKey(String kmsProvider, Handler<AsyncResult<Future<byte[]>>> handler);

  /**
   *  Create a data key with the given KMS provider and options.
   *  <p>
   *  Creates a new key document and inserts into the key vault collection.
   *  </p>
   *  @param kmsProvider    the KMS provider
   *  @param dataKeyOptions the options for data key creation
   *  @return a future containing the identifier for the created data key
   */
  Future<byte[]> createDataKey(String kmsProvider, DataKeyOptions dataKeyOptions);

  /**
   *  Create a data key with the given KMS provider and options.
   *  <p>
   *  Creates a new key document and inserts into the key vault collection.
   *  </p>
   *  @param kmsProvider    the KMS provider
   *  @param dataKeyOptions the options for data key creation
   *  @param handler an async result containing the identifier for the created data key
   *  @return a reference to <code>this</code>
   */
  ClientEncryption createDataKey(String kmsProvider, DataKeyOptions dataKeyOptions,
      Handler<AsyncResult<Future<byte[]>>> handler);

  /**
   *  Encrypt the given value with the given options.
   *  <p>
   *   The driver may throw an exception for prohibited BSON value types
   *  </p>
   *  @param value   the value to encrypt
   *  @param options the options for data encryption
   *  @return a future containing the encrypted value, a BSON binary of subtype 6
   */
  Future<byte[]> encrypt(Object value, EncryptOptions options);

  /**
   *  Encrypt the given value with the given options.
   *  <p>
   *   The driver may throw an exception for prohibited BSON value types
   *  </p>
   *  @param value   the value to encrypt
   *  @param options the options for data encryption
   *  @param handler an async result containing the encrypted value, a BSON binary of subtype 6
   *  @return a reference to <code>this</code>
   */
  ClientEncryption encrypt(Object value, EncryptOptions options,
      Handler<AsyncResult<Future<byte[]>>> handler);

  /**
   *  Decrypt the given value.
   *  @param value the value to decrypt, which must be of subtype 6
   *  @return a future containing the decrypted value
   */
  Future<Object> decrypt(byte[] value);

  /**
   *  Decrypt the given value.
   *  @param value the value to decrypt, which must be of subtype 6
   *  @param handler an async result containing the decrypted value
   *  @return a reference to <code>this</code>
   */
  ClientEncryption decrypt(byte[] value, Handler<AsyncResult<Future<Object>>> handler);

  void close();
}
