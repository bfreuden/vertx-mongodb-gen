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
import io.vertx.mongo.client.model.vault.DataKeyOptions;
import io.vertx.mongo.client.model.vault.EncryptOptions;
import io.vertx.mongo.impl.MongoClientContext;
import io.vertx.mongo.impl.SingleResultSubscriber;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import org.bson.BsonBinary;
import org.bson.BsonValue;
import org.reactivestreams.Publisher;

public class ClientEncryptionImpl extends ClientEncryptionBase {
  protected final MongoClientContext clientContext;

  protected final ClientEncryption wrapped;

  public ClientEncryptionImpl(MongoClientContext clientContext, ClientEncryption wrapped) {
    this.clientContext = clientContext;
    this.wrapped = wrapped;
  }

  @Override
  public Future<byte[]> createDataKey(String kmsProvider) {
    requireNonNull(kmsProvider, "kmsProvider is null");
    Publisher<BsonBinary> __publisher = wrapped.createDataKey(kmsProvider);
    Promise<BsonBinary> __promise = clientContext.getVertx().promise();
    __publisher.subscribe(new SingleResultSubscriber<>(clientContext, __promise));
    return __promise.future().map(clientContext.getConversionUtils()::toByteArray);
  }

  @Override
  public void createDataKey(String kmsProvider, Handler<AsyncResult<byte[]>> resultHandler) {
    Future<byte[]> __future = this.createDataKey(kmsProvider);
    setHandler(__future, resultHandler);
  }

  @Override
  public Future<byte[]> createDataKey(String kmsProvider, DataKeyOptions dataKeyOptions) {
    requireNonNull(kmsProvider, "kmsProvider is null");
    requireNonNull(dataKeyOptions, "dataKeyOptions is null");
    com.mongodb.client.model.vault.DataKeyOptions __dataKeyOptions = dataKeyOptions.toDriverClass(clientContext);
    Publisher<BsonBinary> __publisher = wrapped.createDataKey(kmsProvider, __dataKeyOptions);
    Promise<BsonBinary> __promise = clientContext.getVertx().promise();
    __publisher.subscribe(new SingleResultSubscriber<>(clientContext, __promise));
    return __promise.future().map(clientContext.getConversionUtils()::toByteArray);
  }

  @Override
  public void createDataKey(String kmsProvider, DataKeyOptions dataKeyOptions,
      Handler<AsyncResult<byte[]>> resultHandler) {
    Future<byte[]> __future = this.createDataKey(kmsProvider, dataKeyOptions);
    setHandler(__future, resultHandler);
  }

  @Override
  public Future<byte[]> encrypt(Object value, EncryptOptions options) {
    requireNonNull(value, "value is null");
    requireNonNull(options, "options is null");
    BsonValue __value = clientContext.getConversionUtils().toBsonValue(value);
    com.mongodb.client.model.vault.EncryptOptions __options = options.toDriverClass(clientContext);
    Publisher<BsonBinary> __publisher = wrapped.encrypt(__value, __options);
    Promise<BsonBinary> __promise = clientContext.getVertx().promise();
    __publisher.subscribe(new SingleResultSubscriber<>(clientContext, __promise));
    return __promise.future().map(clientContext.getConversionUtils()::toByteArray);
  }

  @Override
  public void encrypt(Object value, EncryptOptions options,
      Handler<AsyncResult<byte[]>> resultHandler) {
    Future<byte[]> __future = this.encrypt(value, options);
    setHandler(__future, resultHandler);
  }

  @Override
  public Future<Object> decrypt(byte[] value) {
    requireNonNull(value, "value is null");
    BsonBinary __value = clientContext.getConversionUtils().toBsonBinary(value);
    Publisher<BsonValue> __publisher = wrapped.decrypt(__value);
    Promise<BsonValue> __promise = clientContext.getVertx().promise();
    __publisher.subscribe(new SingleResultSubscriber<>(clientContext, __promise));
    return __promise.future().map(clientContext.getConversionUtils()::toObject);
  }

  @Override
  public void decrypt(byte[] value, Handler<AsyncResult<Object>> resultHandler) {
    Future<Object> __future = this.decrypt(value);
    setHandler(__future, resultHandler);
  }

  @Override
  public void close() {
    wrapped.close();
  }

  public MongoClientContext getClientContext() {
    return clientContext;
  }

  public ClientEncryption toDriverClass(MongoClientContext clientContext) {
    return wrapped;
  }
}
