package io.vertx.mongo.client;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.mongo.TransactionOptions;
import java.lang.Void;

/**
 *  A client session that supports transactions.
 *  @since 1.9
 */
public interface ClientSession {
  /**
   *  Returns true if there is an active transaction on this session, and false otherwise
   *
   *  @return true if there is an active transaction on this session
   *  @mongodb.server.release 4.0
   */
  boolean hasActiveTransaction();

  /**
   *   Notify the client session that a message has been sent.
   *   <p>
   *       For internal use only
   *   </p>
   *
   *  @return true if this is the first message sent, false otherwise
   *  @since 4.0
   */
  boolean notifyMessageSent();

  /**
   *  Gets the transaction options.  Only call this method of the session has an active transaction
   *
   *  @return the transaction options
   */
  TransactionOptions getTransactionOptions();

  /**
   *  Start a transaction in the context of this session with default transaction options. A transaction can not be started if there is
   *  already an active transaction on this session.
   *
   *  @mongodb.server.release 4.0
   */
  void startTransaction();

  /**
   *  Start a transaction in the context of this session with the given transaction options. A transaction can not be started if there is
   *  already an active transaction on this session.
   *
   *  @param transactionOptions the options to apply to the transaction
   *
   *  @mongodb.server.release 4.0
   */
  void startTransaction(TransactionOptions transactionOptions);

  /**
   *  Commit a transaction in the context of this session.  A transaction can only be commmited if one has first been started.
   *  @return an empty future that indicates when the operation has completed
   *  @mongodb.server.release 4.0
   */
  Future<Void> commitTransaction();

  /**
   *  Commit a transaction in the context of this session.  A transaction can only be commmited if one has first been started.
   *  @param resultHandler an empty async result that indicates when the operation has completed
   *  @return <code>this</code>
   *  @mongodb.server.release 4.0
   */
  ClientSession commitTransaction(Handler<AsyncResult<Void>> resultHandler);

  /**
   *  Abort a transaction in the context of this session.  A transaction can only be aborted if one has first been started.
   *  @return an empty future that indicates when the operation has completed
   *  @mongodb.server.release 4.0
   */
  Future<Void> abortTransaction();

  /**
   *  Abort a transaction in the context of this session.  A transaction can only be aborted if one has first been started.
   *  @param resultHandler an empty async result that indicates when the operation has completed
   *  @return <code>this</code>
   *  @mongodb.server.release 4.0
   */
  ClientSession abortTransaction(Handler<AsyncResult<Void>> resultHandler);
}
