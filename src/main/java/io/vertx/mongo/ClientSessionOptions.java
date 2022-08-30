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
package io.vertx.mongo;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;
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

  public ClientSessionOptions() {
  }

  public ClientSessionOptions(JsonObject json) {
    ClientSessionOptionsConverter.fromJson(json, this);
  }

  /**
   * @return MongoDB driver object
   * @hidden
   */
  public com.mongodb.ClientSessionOptions toDriverClass() {
    com.mongodb.ClientSessionOptions.Builder builder = com.mongodb.ClientSessionOptions.builder();
    initializeDriverBuilderClass(builder);
    return builder.build();
  }

  /**
   *  Sets whether operations using the session should causally consistent with each other.
   *
   *  @param causallyConsistent whether operations using the session should be causally consistent
   *  @return this
   *  @mongodb.driver.dochub core/causal-consistency Causal Consistency
   */
  public ClientSessionOptions setCausallyConsistent(Boolean causallyConsistent) {
    this.causallyConsistent = causallyConsistent;
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
  public ClientSessionOptions setDefaultTransactionOptions(
      TransactionOptions defaultTransactionOptions) {
    this.defaultTransactionOptions = defaultTransactionOptions;
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

  /**
   * @param builder MongoDB driver builder
   * @hidden
   */
  public void initializeDriverBuilderClass(com.mongodb.ClientSessionOptions.Builder builder) {
    if (this.causallyConsistent != null) {
      builder.causallyConsistent(this.causallyConsistent);
    }
    if (this.defaultTransactionOptions != null) {
      builder.defaultTransactionOptions(this.defaultTransactionOptions.toDriverClass());
    }
  }
}
