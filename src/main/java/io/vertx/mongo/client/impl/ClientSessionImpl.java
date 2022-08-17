package io.vertx.mongo.client.impl;

import static io.vertx.mongo.impl.Utils.setHandler;
import static java.util.Objects.requireNonNull;

import com.mongodb.reactivestreams.client.ClientSession;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.mongo.TransactionOptions;
import java.lang.Override;
import java.lang.Void;

public class ClientSessionImpl extends ClientSessionBase {
  protected ClientSession wrapped;

  @Override
  public boolean hasActiveTransaction() {
    wrapped.hasActiveTransaction();
    return false;
  }

  @Override
  public boolean notifyMessageSent() {
    wrapped.notifyMessageSent();
    return false;
  }

  @Override
  public TransactionOptions getTransactionOptions() {
    wrapped.getTransactionOptions();
    return null;
  }

  @Override
  public void startTransaction() {
    wrapped.startTransaction();
  }

  @Override
  public void startTransaction(TransactionOptions transactionOptions) {
    requireNonNull(transactionOptions, "transactionOptions cannot be null");
    com.mongodb.TransactionOptions __transactionOptions = transactionOptions.toDriverClass();
    wrapped.startTransaction(__transactionOptions);
  }

  @Override
  public Future<Void> commitTransaction() {
    wrapped.commitTransaction();
    return null;
  }

  @Override
  public io.vertx.mongo.client.ClientSession commitTransaction(
      Handler<AsyncResult<Void>> resultHandler) {
    Future<Void> future = this.commitTransaction();
    setHandler(future, resultHandler);
    return this;
  }

  @Override
  public Future<Void> abortTransaction() {
    wrapped.abortTransaction();
    return null;
  }

  @Override
  public io.vertx.mongo.client.ClientSession abortTransaction(
      Handler<AsyncResult<Void>> resultHandler) {
    Future<Void> future = this.abortTransaction();
    setHandler(future, resultHandler);
    return this;
  }

  public ClientSession toDriverClass() {
    return wrapped;
  }
}
