package io.vertx.mongo.client.vault.impl;

import static io.vertx.mongo.impl.Utils.setHandler;
import static java.util.Objects.requireNonNull;

import com.mongodb.reactivestreams.client.vault.ClientEncryption;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.mongo.client.model.vault.DataKeyOptions;
import io.vertx.mongo.client.model.vault.EncryptOptions;
import io.vertx.mongo.impl.ConversionUtilsImpl;
import java.io.Closeable;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import org.bson.BsonBinary;
import org.bson.BsonValue;

public class ClientEncryptionImpl extends ClientEncryptionBase implements Closeable {
  protected ClientEncryption wrapped;

  @Override
  public Future<byte[]> createDataKey(String kmsProvider) {
    requireNonNull(kmsProvider, "kmsProvider cannot be null");
    wrapped.createDataKey(kmsProvider);
    return null;
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
    wrapped.createDataKey(kmsProvider, __dataKeyOptions);
    return null;
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
    wrapped.encrypt(__value, __options);
    return null;
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
    wrapped.decrypt(__value);
    return null;
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
    wrapped.close();
  }

  public ClientEncryption toDriverClass() {
    return wrapped;
  }
}
