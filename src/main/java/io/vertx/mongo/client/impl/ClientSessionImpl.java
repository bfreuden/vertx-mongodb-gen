package io.vertx.mongo.client.impl;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.mongo.TransactionOptions;
import io.vertx.mongo.client.ClientSession;
import io.vertx.mongo.impl.Utils;
import java.lang.Override;
import java.lang.Void;

public class ClientSessionImpl extends ClientSessionBase {
  @Override
  public boolean hasActiveTransaction() {
    return false;
  }

  @Override
  public boolean notifyMessageSent() {
    return false;
  }

  @Override
  public TransactionOptions getTransactionOptions() {
    return null;
  }

  @Override
  public void startTransaction() {
  }

  @Override
  public void startTransaction(TransactionOptions transactionOptions) {
  }

  @Override
  public Future<Void> commitTransaction() {
    return null;
  }

  @Override
  public ClientSession commitTransaction(Handler<AsyncResult<Void>> resultHandler) {
    Future<Void> future = this.commitTransaction();
    Utils.setHandler(future, resultHandler);
    return this;
  }

  @Override
  public Future<Void> abortTransaction() {
    return null;
  }

  @Override
  public ClientSession abortTransaction(Handler<AsyncResult<Void>> resultHandler) {
    Future<Void> future = this.abortTransaction();
    Utils.setHandler(future, resultHandler);
    return this;
  }
}
