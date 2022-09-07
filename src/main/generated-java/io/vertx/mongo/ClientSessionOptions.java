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

import com.mongodb.TransactionOptions;
import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;
import io.vertx.mongo.impl.MongoClientContext;
import java.lang.Boolean;

/**
 *  The options to apply to a {@code ClientSession}.
 *
 *  @mongodb.server.release 3.6
 *  @since 3.6
 *  @see io.vertx.mongo.client.ClientSession
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
   * true for snapshot reads, false otherwise
   */
  private Boolean snapshot;

  /**
   * the default transaction options to use for all transactions on this session,
   */
  private TransactionOptions defaultTransactionOptions;

  public ClientSessionOptions() {
  }

  public ClientSessionOptions(JsonObject json) {
    ClientSessionOptionsConverter.fromJson(json, this);
  }

  public JsonObject toJson() {
    JsonObject result = new JsonObject();
    ClientSessionOptionsConverter.toJson(this, result);
    return result;
  }

  /**
   * @return MongoDB driver object
   * @hidden
   */
  public com.mongodb.ClientSessionOptions toDriverClass(MongoClientContext clientContext) {
    com.mongodb.ClientSessionOptions.Builder builder = com.mongodb.ClientSessionOptions.builder();
    initializeDriverBuilderClass(clientContext, builder);
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
   *  Sets whether read operations using the session should share the same snapshot.
   *
   *  <p>
   *  The default value is unset, in which case the driver will use the global default value, which is currently false.
   *  </p>
   *
   *  @param snapshot true for snapshot reads, false otherwise
   *  @return this
   *  @since 4.3
   *  @mongodb.server.release 5.0
   *  @mongodb.driver.manual  reference/read-concern-snapshot/#read-concern-and-atclustertime Snapshot reads
   */
  public ClientSessionOptions setSnapshot(Boolean snapshot) {
    this.snapshot = snapshot;
    return this;
  }

  /**
   *  Whether read operations using this session should all share the same snapshot.
   *
   *  @return whether read operations using this session should all share the same snapshot. A null value indicates to use the global
   *  default, which is false.
   *  @since 4.3
   *  @mongodb.server.release 5.0
   *  @mongodb.driver.manual  reference/read-concern-snapshot/#read-concern-and-atclustertime Snapshot reads
   */
  public Boolean isSnapshot() {
    return snapshot;
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
  public void initializeDriverBuilderClass(MongoClientContext clientContext,
      com.mongodb.ClientSessionOptions.Builder builder) {
    if (this.causallyConsistent != null) {
      builder.causallyConsistent(this.causallyConsistent);
    }
    if (this.snapshot != null) {
      builder.snapshot(this.snapshot);
    }
    if (this.defaultTransactionOptions != null) {
      builder.defaultTransactionOptions(this.defaultTransactionOptions);
    }
  }
}
