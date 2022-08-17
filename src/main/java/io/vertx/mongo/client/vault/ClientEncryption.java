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
package io.vertx.mongo.client.vault;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.mongo.client.model.vault.DataKeyOptions;
import io.vertx.mongo.client.model.vault.EncryptOptions;
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
   *  @param resultHandler an async result containing the identifier for the created data key
   *  @return <code>this</code>
   */
  ClientEncryption createDataKey(String kmsProvider, Handler<AsyncResult<byte[]>> resultHandler);

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
   *  @param resultHandler an async result containing the identifier for the created data key
   *  @return <code>this</code>
   */
  ClientEncryption createDataKey(String kmsProvider, DataKeyOptions dataKeyOptions,
      Handler<AsyncResult<byte[]>> resultHandler);

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
   *  @param resultHandler an async result containing the encrypted value, a BSON binary of subtype 6
   *  @return <code>this</code>
   */
  ClientEncryption encrypt(Object value, EncryptOptions options,
      Handler<AsyncResult<byte[]>> resultHandler);

  /**
   *  Decrypt the given value.
   *  @param value the value to decrypt, which must be of subtype 6
   *  @return a future containing the decrypted value
   */
  Future<Object> decrypt(byte[] value);

  /**
   *  Decrypt the given value.
   *  @param value the value to decrypt, which must be of subtype 6
   *  @param resultHandler an async result containing the decrypted value
   *  @return <code>this</code>
   */
  ClientEncryption decrypt(byte[] value, Handler<AsyncResult<Object>> resultHandler);

  void close();

  /**
   * @return mongo object
   * @hidden
   */
  com.mongodb.reactivestreams.client.vault.ClientEncryption toDriverClass();
}
