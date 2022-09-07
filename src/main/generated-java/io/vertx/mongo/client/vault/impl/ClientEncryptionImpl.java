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

import com.mongodb.reactivestreams.client.FindPublisher;
import com.mongodb.reactivestreams.client.vault.ClientEncryption;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;
import io.vertx.mongo.MongoResult;
import io.vertx.mongo.client.FindOptions;
import io.vertx.mongo.client.model.vault.DataKeyOptions;
import io.vertx.mongo.client.model.vault.EncryptOptions;
import io.vertx.mongo.client.model.vault.RewrapManyDataKeyOptions;
import io.vertx.mongo.client.model.vault.RewrapManyDataKeyResult;
import io.vertx.mongo.client.result.DeleteResult;
import io.vertx.mongo.impl.MappingPublisher;
import io.vertx.mongo.impl.MongoClientContext;
import io.vertx.mongo.impl.MongoResultImpl;
import io.vertx.mongo.impl.SingleResultSubscriber;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import org.bson.BsonBinary;
import org.bson.BsonDocument;
import org.bson.BsonValue;
import org.bson.conversions.Bson;
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
    return __promise.future().map(clientContext.getMapper()::toByteArray);
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
    return __promise.future().map(clientContext.getMapper()::toByteArray);
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
    BsonValue __value = clientContext.getMapper().toBsonValue(value);
    com.mongodb.client.model.vault.EncryptOptions __options = options.toDriverClass(clientContext);
    Publisher<BsonBinary> __publisher = wrapped.encrypt(__value, __options);
    Promise<BsonBinary> __promise = clientContext.getVertx().promise();
    __publisher.subscribe(new SingleResultSubscriber<>(clientContext, __promise));
    return __promise.future().map(clientContext.getMapper()::toByteArray);
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
    BsonBinary __value = clientContext.getMapper().toBsonBinary(value);
    Publisher<BsonValue> __publisher = wrapped.decrypt(__value);
    Promise<BsonValue> __promise = clientContext.getVertx().promise();
    __publisher.subscribe(new SingleResultSubscriber<>(clientContext, __promise));
    return __promise.future().map(clientContext.getMapper()::toObject);
  }

  @Override
  public void decrypt(byte[] value, Handler<AsyncResult<Object>> resultHandler) {
    Future<Object> __future = this.decrypt(value);
    setHandler(__future, resultHandler);
  }

  @Override
  public Future<DeleteResult> deleteKey(byte[] id) {
    requireNonNull(id, "id is null");
    BsonBinary __id = clientContext.getMapper().toBsonBinary(id);
    Publisher<com.mongodb.client.result.DeleteResult> __publisher = wrapped.deleteKey(__id);
    Promise<com.mongodb.client.result.DeleteResult> __promise = clientContext.getVertx().promise();
    __publisher.subscribe(new SingleResultSubscriber<>(clientContext, __promise));
    return __promise.future().map(_item -> DeleteResult.fromDriverClass(clientContext, _item));
  }

  @Override
  public void deleteKey(byte[] id, Handler<AsyncResult<DeleteResult>> resultHandler) {
    Future<DeleteResult> __future = this.deleteKey(id);
    setHandler(__future, resultHandler);
  }

  @Override
  public Future<JsonObject> getKey(byte[] id) {
    requireNonNull(id, "id is null");
    BsonBinary __id = clientContext.getMapper().toBsonBinary(id);
    Publisher<BsonDocument> __publisher = wrapped.getKey(__id);
    Promise<BsonDocument> __promise = clientContext.getVertx().promise();
    __publisher.subscribe(new SingleResultSubscriber<>(clientContext, __promise));
    if (clientContext.getConfig().getOutputMapper() == null) {
      return __promise.future().map(clientContext.getMapper()::toJsonObject);
    } else {
      return __promise.future().map(clientContext.getMapper()::toJsonObject).map(clientContext.getConfig().getOutputMapper());
    }
  }

  @Override
  public void getKey(byte[] id, Handler<AsyncResult<JsonObject>> resultHandler) {
    Future<JsonObject> __future = this.getKey(id);
    setHandler(__future, resultHandler);
  }

  @Override
  public MongoResult<JsonObject> getKeys() {
    FindPublisher<JsonObject> __publisher = wrapped.getKeys(JsonObject.class);
    MappingPublisher<BsonDocument, JsonObject> __mappingPublisher = new MappingPublisher<>(__publisher, clientContext.getMapper()::toJsonObject);
    return new MongoResultImpl<>(clientContext, __mappingPublisher, clientContext.getConfig().getOutputMapper(), __mappingPublisher::first);
  }

  @Override
  public MongoResult<JsonObject> getKeys(FindOptions options) {
    FindPublisher<JsonObject> __publisher = wrapped.getKeys(JsonObject.class);
    MappingPublisher<BsonDocument, JsonObject> __mappingPublisher = new MappingPublisher<>(__publisher, clientContext.getMapper()::toJsonObject);
    options.initializePublisher(clientContext, __publisher);
    Integer __batchSize = options.getBatchSize();
    if (__batchSize != null) {
      return new MongoResultImpl<>(clientContext, __mappingPublisher, clientContext.getConfig().getOutputMapper(), __mappingPublisher::first, __batchSize);
    } else {
      return new MongoResultImpl<>(clientContext, __mappingPublisher, clientContext.getConfig().getOutputMapper(), __mappingPublisher::first);
    }
  }

  @Override
  public Future<JsonObject> addKeyAltName(byte[] id, String keyAltName) {
    requireNonNull(id, "id is null");
    requireNonNull(keyAltName, "keyAltName is null");
    BsonBinary __id = clientContext.getMapper().toBsonBinary(id);
    Publisher<BsonDocument> __publisher = wrapped.addKeyAltName(__id, keyAltName);
    Promise<BsonDocument> __promise = clientContext.getVertx().promise();
    __publisher.subscribe(new SingleResultSubscriber<>(clientContext, __promise));
    if (clientContext.getConfig().getOutputMapper() == null) {
      return __promise.future().map(clientContext.getMapper()::toJsonObject);
    } else {
      return __promise.future().map(clientContext.getMapper()::toJsonObject).map(clientContext.getConfig().getOutputMapper());
    }
  }

  @Override
  public void addKeyAltName(byte[] id, String keyAltName,
      Handler<AsyncResult<JsonObject>> resultHandler) {
    Future<JsonObject> __future = this.addKeyAltName(id, keyAltName);
    setHandler(__future, resultHandler);
  }

  @Override
  public Future<JsonObject> removeKeyAltName(byte[] id, String keyAltName) {
    requireNonNull(id, "id is null");
    requireNonNull(keyAltName, "keyAltName is null");
    BsonBinary __id = clientContext.getMapper().toBsonBinary(id);
    Publisher<BsonDocument> __publisher = wrapped.removeKeyAltName(__id, keyAltName);
    Promise<BsonDocument> __promise = clientContext.getVertx().promise();
    __publisher.subscribe(new SingleResultSubscriber<>(clientContext, __promise));
    if (clientContext.getConfig().getOutputMapper() == null) {
      return __promise.future().map(clientContext.getMapper()::toJsonObject);
    } else {
      return __promise.future().map(clientContext.getMapper()::toJsonObject).map(clientContext.getConfig().getOutputMapper());
    }
  }

  @Override
  public void removeKeyAltName(byte[] id, String keyAltName,
      Handler<AsyncResult<JsonObject>> resultHandler) {
    Future<JsonObject> __future = this.removeKeyAltName(id, keyAltName);
    setHandler(__future, resultHandler);
  }

  @Override
  public Future<JsonObject> getKeyByAltName(String keyAltName) {
    requireNonNull(keyAltName, "keyAltName is null");
    Publisher<BsonDocument> __publisher = wrapped.getKeyByAltName(keyAltName);
    Promise<BsonDocument> __promise = clientContext.getVertx().promise();
    __publisher.subscribe(new SingleResultSubscriber<>(clientContext, __promise));
    if (clientContext.getConfig().getOutputMapper() == null) {
      return __promise.future().map(clientContext.getMapper()::toJsonObject);
    } else {
      return __promise.future().map(clientContext.getMapper()::toJsonObject).map(clientContext.getConfig().getOutputMapper());
    }
  }

  @Override
  public void getKeyByAltName(String keyAltName, Handler<AsyncResult<JsonObject>> resultHandler) {
    Future<JsonObject> __future = this.getKeyByAltName(keyAltName);
    setHandler(__future, resultHandler);
  }

  @Override
  public Future<RewrapManyDataKeyResult> rewrapManyDataKey(JsonObject filter) {
    requireNonNull(filter, "filter is null");
    Bson __filter = clientContext.getMapper().toBson(filter);
    Publisher<com.mongodb.client.model.vault.RewrapManyDataKeyResult> __publisher = wrapped.rewrapManyDataKey(__filter);
    Promise<com.mongodb.client.model.vault.RewrapManyDataKeyResult> __promise = clientContext.getVertx().promise();
    __publisher.subscribe(new SingleResultSubscriber<>(clientContext, __promise));
    return __promise.future().map(_item -> RewrapManyDataKeyResult.fromDriverClass(clientContext, _item));
  }

  @Override
  public void rewrapManyDataKey(JsonObject filter,
      Handler<AsyncResult<RewrapManyDataKeyResult>> resultHandler) {
    Future<RewrapManyDataKeyResult> __future = this.rewrapManyDataKey(filter);
    setHandler(__future, resultHandler);
  }

  @Override
  public Future<RewrapManyDataKeyResult> rewrapManyDataKey(JsonObject filter,
      RewrapManyDataKeyOptions options) {
    requireNonNull(filter, "filter is null");
    requireNonNull(options, "options is null");
    Bson __filter = clientContext.getMapper().toBson(filter);
    com.mongodb.client.model.vault.RewrapManyDataKeyOptions __options = options.toDriverClass(clientContext);
    Publisher<com.mongodb.client.model.vault.RewrapManyDataKeyResult> __publisher = wrapped.rewrapManyDataKey(__filter, __options);
    Promise<com.mongodb.client.model.vault.RewrapManyDataKeyResult> __promise = clientContext.getVertx().promise();
    __publisher.subscribe(new SingleResultSubscriber<>(clientContext, __promise));
    return __promise.future().map(_item -> RewrapManyDataKeyResult.fromDriverClass(clientContext, _item));
  }

  @Override
  public void rewrapManyDataKey(JsonObject filter, RewrapManyDataKeyOptions options,
      Handler<AsyncResult<RewrapManyDataKeyResult>> resultHandler) {
    Future<RewrapManyDataKeyResult> __future = this.rewrapManyDataKey(filter, options);
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
