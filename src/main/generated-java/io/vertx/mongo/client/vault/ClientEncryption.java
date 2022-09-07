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
import io.vertx.core.json.JsonObject;
import io.vertx.mongo.MongoResult;
import io.vertx.mongo.client.FindOptions;
import io.vertx.mongo.client.model.vault.DataKeyOptions;
import io.vertx.mongo.client.model.vault.EncryptOptions;
import io.vertx.mongo.client.model.vault.RewrapManyDataKeyOptions;
import io.vertx.mongo.client.model.vault.RewrapManyDataKeyResult;
import io.vertx.mongo.client.result.DeleteResult;
import io.vertx.mongo.impl.MongoClientContext;
import java.lang.Object;
import java.lang.String;

/**
 *  The Key vault.
 *  <p>
 *  Used to create data encryption keys, and to explicitly encrypt and decrypt values when auto-encryption is not an option.
 *  </p>
 *  @since 1.12
 */
public interface ClientEncryption {
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
   */
  void createDataKey(String kmsProvider, Handler<AsyncResult<byte[]>> resultHandler);

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
   */
  void createDataKey(String kmsProvider, DataKeyOptions dataKeyOptions,
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
   */
  void encrypt(Object value, EncryptOptions options, Handler<AsyncResult<byte[]>> resultHandler);

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
   */
  void decrypt(byte[] value, Handler<AsyncResult<Object>> resultHandler);

  /**
   *  Removes the key document with the given data key from the key vault collection.
   *  @param id the data key UUID (BSON binary subtype 0x04)
   *  @return a future containing the delete result
   *  @since 4.7
   */
  Future<DeleteResult> deleteKey(byte[] id);

  /**
   *  Removes the key document with the given data key from the key vault collection.
   *  @param id the data key UUID (BSON binary subtype 0x04)
   *  @param resultHandler an async result containing the delete result
   *  @since 4.7
   */
  void deleteKey(byte[] id, Handler<AsyncResult<DeleteResult>> resultHandler);

  /**
   *  Finds a single key document with the given UUID (BSON binary subtype 0x04).
   *  @param id the data key UUID (BSON binary subtype 0x04)
   *  @return a future containing the single key document or an empty future if there is no match
   *  @since 4.7
   */
  Future<JsonObject> getKey(byte[] id);

  /**
   *  Finds a single key document with the given UUID (BSON binary subtype 0x04).
   *  @param id the data key UUID (BSON binary subtype 0x04)
   *  @param resultHandler an async result containing the single key document or an empty async result if there is no match
   *  @since 4.7
   */
  void getKey(byte[] id, Handler<AsyncResult<JsonObject>> resultHandler);

  /**
   *  Finds all documents in the key vault collection.
   *  @return a find result for the documents in the key vault collection
   *  @since 4.7
   */
  MongoResult<JsonObject> getKeys();

  /**
   *  Finds all documents in the key vault collection.
   *  @param options options
   *  @return a find result for the documents in the key vault collection
   *  @since 4.7
   */
  MongoResult<JsonObject> getKeys(FindOptions options);

  /**
   *  Adds a keyAltName to the keyAltNames array of the key document in the key vault collection with the given UUID.
   *  @param id the data key UUID (BSON binary subtype 0x04)
   *  @param keyAltName the alternative key name to add to the keyAltNames array
   *  @return a future containing the previous version of the key document or an empty future if no match
   *  @since 4.7
   */
  Future<JsonObject> addKeyAltName(byte[] id, String keyAltName);

  /**
   *  Adds a keyAltName to the keyAltNames array of the key document in the key vault collection with the given UUID.
   *  @param id the data key UUID (BSON binary subtype 0x04)
   *  @param keyAltName the alternative key name to add to the keyAltNames array
   *  @param resultHandler an async result containing the previous version of the key document or an empty async result if no match
   *  @since 4.7
   */
  void addKeyAltName(byte[] id, String keyAltName, Handler<AsyncResult<JsonObject>> resultHandler);

  /**
   *  Removes a keyAltName from the keyAltNames array of the key document in the key vault collection with the given id.
   *  @param id the data key UUID (BSON binary subtype 0x04)
   *  @param keyAltName the alternative key name
   *  @return a future containing the previous version of the key document or an empty future if there is no match
   *  @since 4.7
   */
  Future<JsonObject> removeKeyAltName(byte[] id, String keyAltName);

  /**
   *  Removes a keyAltName from the keyAltNames array of the key document in the key vault collection with the given id.
   *  @param id the data key UUID (BSON binary subtype 0x04)
   *  @param keyAltName the alternative key name
   *  @param resultHandler an async result containing the previous version of the key document or an empty async result if there is no match
   *  @since 4.7
   */
  void removeKeyAltName(byte[] id, String keyAltName,
      Handler<AsyncResult<JsonObject>> resultHandler);

  /**
   *  Returns a key document in the key vault collection with the given keyAltName.
   *  @param keyAltName the alternative key name
   *  @return a future containing the matching key document or an empty future if there is no match
   *  @since 4.7
   */
  Future<JsonObject> getKeyByAltName(String keyAltName);

  /**
   *  Returns a key document in the key vault collection with the given keyAltName.
   *  @param keyAltName the alternative key name
   *  @param resultHandler an async result containing the matching key document or an empty async result if there is no match
   *  @since 4.7
   */
  void getKeyByAltName(String keyAltName, Handler<AsyncResult<JsonObject>> resultHandler);

  /**
   *  Decrypts multiple data keys and (re-)encrypts them with the current masterKey.
   *  @param filter the filter
   *  @return a future containing the result
   *  @since 4.7
   */
  Future<RewrapManyDataKeyResult> rewrapManyDataKey(JsonObject filter);

  /**
   *  Decrypts multiple data keys and (re-)encrypts them with the current masterKey.
   *  @param filter the filter
   *  @param resultHandler an async result containing the result
   *  @since 4.7
   */
  void rewrapManyDataKey(JsonObject filter,
      Handler<AsyncResult<RewrapManyDataKeyResult>> resultHandler);

  /**
   *  Decrypts multiple data keys and (re-)encrypts them with a new masterKey, or with their current masterKey if a new one is not given.
   *  @param filter the filter
   *  @param options the options
   *  @return a future containing the result
   *  @since 4.7
   */
  Future<RewrapManyDataKeyResult> rewrapManyDataKey(JsonObject filter,
      RewrapManyDataKeyOptions options);

  /**
   *  Decrypts multiple data keys and (re-)encrypts them with a new masterKey, or with their current masterKey if a new one is not given.
   *  @param filter the filter
   *  @param options the options
   *  @param resultHandler an async result containing the result
   *  @since 4.7
   */
  void rewrapManyDataKey(JsonObject filter, RewrapManyDataKeyOptions options,
      Handler<AsyncResult<RewrapManyDataKeyResult>> resultHandler);

  void close();

  /**
   * @return mongo object
   * @hidden
   */
  com.mongodb.reactivestreams.client.vault.ClientEncryption toDriverClass(
      MongoClientContext clientContext);
}
