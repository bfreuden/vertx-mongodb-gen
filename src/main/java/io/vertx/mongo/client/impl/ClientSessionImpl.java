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
import io.vertx.mongo.TransactionOptions;
import io.vertx.mongo.impl.MongoClientContext;
import io.vertx.mongo.impl.SingleResultSubscriber;
import java.lang.Override;
import java.lang.Void;
import org.reactivestreams.Publisher;

public class ClientSessionImpl extends ClientSessionBase {
  protected final MongoClientContext clientContext;

  protected final ClientSession wrapped;

  public ClientSessionImpl(MongoClientContext clientContext, ClientSession wrapped) {
    this.clientContext = clientContext;
    this.wrapped = wrapped;
  }

  @Override
  public boolean hasActiveTransaction() {
    return wrapped.hasActiveTransaction();
  }

  @Override
  public boolean notifyMessageSent() {
    return wrapped.notifyMessageSent();
  }

  @Override
  public TransactionOptions getTransactionOptions() {
    com.mongodb.TransactionOptions __result = wrapped.getTransactionOptions();
    return  TransactionOptions.fromDriverClass(__result);
  }

  @Override
  public void startTransaction() {
    wrapped.startTransaction();
  }

  @Override
  public void startTransaction(TransactionOptions transactionOptions) {
    requireNonNull(transactionOptions, "transactionOptions is null");
    com.mongodb.TransactionOptions __transactionOptions = transactionOptions.toDriverClass();
    wrapped.startTransaction(__transactionOptions);
  }

  @Override
  public Future<Void> commitTransaction() {
    Publisher<Void> __publisher = wrapped.commitTransaction();
    Promise<Void> __promise = Promise.promise();
    __publisher.subscribe(new SingleResultSubscriber<>(clientContext, __promise));
    return __promise.future();
  }

  @Override
  public io.vertx.mongo.client.ClientSession commitTransaction(
      Handler<AsyncResult<Void>> resultHandler) {
    Future<Void> __future = this.commitTransaction();
    setHandler(__future, resultHandler);
    return this;
  }

  @Override
  public Future<Void> abortTransaction() {
    Publisher<Void> __publisher = wrapped.abortTransaction();
    Promise<Void> __promise = Promise.promise();
    __publisher.subscribe(new SingleResultSubscriber<>(clientContext, __promise));
    return __promise.future();
  }

  @Override
  public io.vertx.mongo.client.ClientSession abortTransaction(
      Handler<AsyncResult<Void>> resultHandler) {
    Future<Void> __future = this.abortTransaction();
    setHandler(__future, resultHandler);
    return this;
  }

  public ClientSession toDriverClass() {
    return wrapped;
  }
}
