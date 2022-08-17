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
package io.vertx.mongo.client.vault.impl;

import static io.vertx.mongo.impl.Utils.setHandler;
import static java.util.Objects.requireNonNull;

import com.mongodb.reactivestreams.client.vault.ClientEncryption;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.mongo.client.model.vault.DataKeyOptions;
import io.vertx.mongo.client.model.vault.EncryptOptions;
import io.vertx.mongo.impl.ConversionUtilsImpl;
import io.vertx.mongo.impl.SingleResultSubscriber;
import java.io.Closeable;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import org.bson.BsonBinary;
import org.bson.BsonValue;
import org.reactivestreams.Publisher;

public class ClientEncryptionImpl extends ClientEncryptionBase implements Closeable {
  protected ClientEncryption wrapped;

  protected Vertx vertx;

  @Override
  public Future<byte[]> createDataKey(String kmsProvider) {
    requireNonNull(kmsProvider, "kmsProvider cannot be null");
    Publisher<BsonBinary> __publisher = wrapped.createDataKey(kmsProvider);
    Promise<byte[]> promise = Promise.promise();
    __publisher.subscribe(new SingleResultSubscriber<>(promise));
    return promise.future().map(res -> ConversionUtilsImpl.INSTANCE.toByteArray(res));
  }

  @Override
  public io.vertx.mongo.client.vault.ClientEncryption createDataKey(String kmsProvider,
      Handler<AsyncResult<byte[]>> resultHandler) {
    Future<byte[]> future = this.createDataKey(kmsProvider);
    setHandler(future, resultHandler);
    return this;
  }

  @Override
  public Future<byte[]> createDataKey(String kmsProvider, DataKeyOptions dataKeyOptions) {
    requireNonNull(kmsProvider, "kmsProvider cannot be null");
    requireNonNull(dataKeyOptions, "dataKeyOptions cannot be null");
    com.mongodb.client.model.vault.DataKeyOptions __dataKeyOptions = dataKeyOptions.toDriverClass();
    Publisher<BsonBinary> __publisher = wrapped.createDataKey(kmsProvider, __dataKeyOptions);
    Promise<byte[]> promise = Promise.promise();
    __publisher.subscribe(new SingleResultSubscriber<>(promise));
    return promise.future().map(res -> ConversionUtilsImpl.INSTANCE.toByteArray(res));
  }

  @Override
  public io.vertx.mongo.client.vault.ClientEncryption createDataKey(String kmsProvider,
      DataKeyOptions dataKeyOptions, Handler<AsyncResult<byte[]>> resultHandler) {
    Future<byte[]> future = this.createDataKey(kmsProvider, dataKeyOptions);
    setHandler(future, resultHandler);
    return this;
  }

  @Override
  public Future<byte[]> encrypt(Object value, EncryptOptions options) {
    requireNonNull(value, "value cannot be null");
    requireNonNull(options, "options cannot be null");
    BsonValue __value = ConversionUtilsImpl.INSTANCE.toBsonValue(value);
    com.mongodb.client.model.vault.EncryptOptions __options = options.toDriverClass();
    Publisher<BsonBinary> __publisher = wrapped.encrypt(__value, __options);
    Promise<byte[]> promise = Promise.promise();
    __publisher.subscribe(new SingleResultSubscriber<>(promise));
    return promise.future().map(res -> ConversionUtilsImpl.INSTANCE.toByteArray(res));
  }

  @Override
  public io.vertx.mongo.client.vault.ClientEncryption encrypt(Object value, EncryptOptions options,
      Handler<AsyncResult<byte[]>> resultHandler) {
    Future<byte[]> future = this.encrypt(value, options);
    setHandler(future, resultHandler);
    return this;
  }

  @Override
  public Future<Object> decrypt(byte[] value) {
    requireNonNull(value, "value cannot be null");
    BsonBinary __value = ConversionUtilsImpl.INSTANCE.toBsonBinary(value);
    Publisher<BsonValue> __publisher = wrapped.decrypt(__value);
    Promise<Object> promise = Promise.promise();
    __publisher.subscribe(new SingleResultSubscriber<>(promise));
    return promise.future().map(res -> ConversionUtilsImpl.INSTANCE.toObject(res));
  }

  @Override
  public io.vertx.mongo.client.vault.ClientEncryption decrypt(byte[] value,
      Handler<AsyncResult<Object>> resultHandler) {
    Future<Object> future = this.decrypt(value);
    setHandler(future, resultHandler);
    return this;
  }

  @Override
  public void close() {
  }

  public ClientEncryption toDriverClass() {
    return wrapped;
  }
}
