package io.vertx.mongo.client.vault.impl;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.mongo.client.model.vault.DataKeyOptions;
import io.vertx.mongo.client.model.vault.EncryptOptions;
import io.vertx.mongo.client.vault.ClientEncryption;
import io.vertx.mongo.impl.Utils;
import java.io.Closeable;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;

public class ClientEncryptionImpl extends ClientEncryptionBase implements Closeable {
  @Override
  public Future<byte[]> createDataKey(String kmsProvider) {
    return null;
  }

  @Override
  public ClientEncryption createDataKey(String kmsProvider,
      Handler<AsyncResult<byte[]>> resultHandler) {
    Future<byte[]> future = this.createDataKey(kmsProvider);
    Utils.setHandler(future, resultHandler);
    return this;
  }

  @Override
  public Future<byte[]> createDataKey(String kmsProvider, DataKeyOptions dataKeyOptions) {
    com.mongodb.client.model.vault.DataKeyOptions __dataKeyOptions = dataKeyOptions.toDriverClass();
    return null;
  }

  @Override
  public ClientEncryption createDataKey(String kmsProvider, DataKeyOptions dataKeyOptions,
      Handler<AsyncResult<byte[]>> resultHandler) {
    Future<byte[]> future = this.createDataKey(kmsProvider, dataKeyOptions);
    Utils.setHandler(future, resultHandler);
    return this;
  }

  @Override
  public Future<byte[]> encrypt(Object value, EncryptOptions options) {
    com.mongodb.client.model.vault.EncryptOptions __options = options.toDriverClass();
    return null;
  }

  @Override
  public ClientEncryption encrypt(Object value, EncryptOptions options,
      Handler<AsyncResult<byte[]>> resultHandler) {
    Future<byte[]> future = this.encrypt(value, options);
    Utils.setHandler(future, resultHandler);
    return this;
  }

  @Override
  public Future<Object> decrypt(byte[] value) {
    return null;
  }

  @Override
  public ClientEncryption decrypt(byte[] value, Handler<AsyncResult<Object>> resultHandler) {
    Future<Object> future = this.decrypt(value);
    Utils.setHandler(future, resultHandler);
    return this;
  }

  @Override
  public void close() {
  }
}
