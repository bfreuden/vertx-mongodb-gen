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
package io.vertx.mongo.client.impl;

import static io.vertx.mongo.impl.Utils.setHandler;
import static java.util.Objects.requireNonNull;

import com.mongodb.reactivestreams.client.ClientSession;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.mongo.TransactionOptions;
import io.vertx.mongo.impl.SingleResultSubscriber;
import java.lang.Override;
import java.lang.Void;
import org.reactivestreams.Publisher;

public class ClientSessionImpl extends ClientSessionBase {
  protected ClientSession wrapped;

  protected Vertx vertx;

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
    requireNonNull(transactionOptions, "transactionOptions cannot be null");
    com.mongodb.TransactionOptions __transactionOptions = transactionOptions.toDriverClass();
  }

  @Override
  public Future<Void> commitTransaction() {
    Publisher<Void> __publisher = wrapped.commitTransaction();
    Promise<Void> promise = Promise.promise();
    __publisher.subscribe(new SingleResultSubscriber<>(promise));
    return promise.future();
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
    Publisher<Void> __publisher = wrapped.abortTransaction();
    Promise<Void> promise = Promise.promise();
    __publisher.subscribe(new SingleResultSubscriber<>(promise));
    return promise.future();
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
