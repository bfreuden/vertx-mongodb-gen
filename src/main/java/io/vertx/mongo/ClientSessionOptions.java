package io.vertx.mongo;

import com.mongodb.TransactionOptions;
import io.vertx.codegen.annotations.DataObject;
import io.vertx.mongo.client.ClientSession;

import java.lang.Boolean;

/**
 *  The options to apply to a {@code ClientSession}.
 *
 *  @mongodb.server.release 3.6
 *  @since 3.6
 *  @see ClientSession
 *  @mongodb.driver.dochub core/causal-consistency Causal Consistency
 */
@DataObject(
    generateConverter = true
)
public class ClientSessionOptions {
  /**
   * whether operations using the session should be causally consistent
   */
  private Boolean causallyConsistent;

  /**
   * the default transaction options to use for all transactions on this session,
   */
  private TransactionOptions defaultTransactionOptions;

  /**
   *  Sets whether operations using the session should causally consistent with each other.
   *
   *  @param causallyConsistent whether operations using the session should be causally consistent
   *  @return this
   *  @mongodb.driver.dochub core/causal-consistency Causal Consistency
   */
  public ClientSessionOptions causallyConsistent(Boolean causallyConsistent) {
    return this;
  }

  /**
   *  Whether operations using the session should causally consistent with each other.
   *
   *  @return whether operations using the session should be causally consistent.  A null value indicates to use the global default,
   *  which is currently true.
   *  @mongodb.driver.dochub core/causal-consistency Causal Consistency
   */
  public Boolean isCausallyConsistent() {
    return causallyConsistent;
  }

  /**
   *  Sets whether operations using the session should causally consistent with each other.
   *
   *  @param defaultTransactionOptions the default transaction options to use for all transactions on this session,
   *  @return this
   *  @since 3.8
   *  @mongodb.server.release 4.0
   */
  public ClientSessionOptions defaultTransactionOptions(
      TransactionOptions defaultTransactionOptions) {
    return this;
  }

  /**
   *  Gets the default transaction options for the session.
   *
   *  @return the default transaction options for the session
   *  @since 3.8
   *  @mongodb.server.release 4.0
   */
  public TransactionOptions getDefaultTransactionOptions() {
    return defaultTransactionOptions;
  }
}
