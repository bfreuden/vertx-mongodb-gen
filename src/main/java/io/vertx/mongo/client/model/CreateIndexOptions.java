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
package io.vertx.mongo.client.model;

import com.mongodb.CreateIndexCommitQuorum;
import io.vertx.codegen.annotations.DataObject;
import io.vertx.codegen.annotations.GenIgnore;
import io.vertx.core.json.JsonObject;
import io.vertx.mongo.impl.CreateIndexCommitQuorumSerializer;
import java.lang.Long;
import java.util.concurrent.TimeUnit;

/**
 *  The options to apply to the command when creating indexes.
 *
 *  @mongodb.driver.manual reference/command/createIndexes Index options
 *  @since 3.6
 */
@DataObject(
    generateConverter = true
)
public class CreateIndexOptions {
  /**
   *  the max time
   */
  private Long maxTime;

  /**
   * the create index commit quorum
   */
  private CreateIndexCommitQuorumSerializer commitQuorum;

  public CreateIndexOptions() {
  }

  public CreateIndexOptions(JsonObject json) {
    CreateIndexOptionsConverter.fromJson(json, this);
  }

  public JsonObject toJson() {
    JsonObject result = new JsonObject();
    CreateIndexOptionsConverter.toJson(this, result);
    return result;
  }

  /**
   *  Sets the maximum execution time on the server for this operation.
   *
   *  @param maxTime  the max time (in milliseconds)
   *  @return this
   */
  public CreateIndexOptions setMaxTime(Long maxTime) {
    this.maxTime = maxTime;
    return this;
  }

  /**
   *  Gets the maximum execution time on the server for this operation.  The default is 0, which places no limit on the execution time.
   *
   *  @return the maximum execution time (in milliseconds)
   */
  public Long getMaxTime() {
    return maxTime;
  }

  /**
   *  Sets the create index commit quorum for this operation.
   *
   *  @param commitQuorum the create index commit quorum
   *  @return this
   *  @mongodb.server.release 4.4
   *  @since 4.1
   */
  @GenIgnore
  public CreateIndexOptions setCommitQuorum(CreateIndexCommitQuorum commitQuorum) {
    if (commitQuorum == null) {
      this.commitQuorum = null;
    } else if (this.commitQuorum == null) {
      this.commitQuorum = new CreateIndexCommitQuorumSerializer(commitQuorum);
    } else {
      this.commitQuorum.setValue(commitQuorum);
    }
    return this;
  }

  /**
   *  Sets the create index commit quorum for this operation.
   *
   *  @param commitQuorum the create index commit quorum
   *  @return this
   *  @mongodb.server.release 4.4
   *  @since 4.1
   *
   * @hidden
   */
  public CreateIndexOptions setCommitQuorum(CreateIndexCommitQuorumSerializer commitQuorum) {
    this.commitQuorum = commitQuorum;
    return this;
  }

  /**
   *  Gets the create index commit quorum for this operation.
   *
   *  @return the create index commit quorum
   *  @mongodb.server.release 4.4
   *  @since 4.1
   */
  @GenIgnore
  public CreateIndexCommitQuorum getMongoCommitQuorum() {
    if (this.commitQuorum == null) {
      return null;
    } else {
      return commitQuorum.getValue();
    }
  }

  /**
   *  Gets the create index commit quorum for this operation.
   *
   *  @return the create index commit quorum
   *  @mongodb.server.release 4.4
   *  @since 4.1
   *
   * @hidden
   */
  public CreateIndexCommitQuorumSerializer getCommitQuorum() {
    return commitQuorum;
  }

  /**
   * @return MongoDB driver object
   * @hidden
   */
  public com.mongodb.client.model.CreateIndexOptions toDriverClass() {
    com.mongodb.client.model.CreateIndexOptions result = new com.mongodb.client.model.CreateIndexOptions();
    if (this.maxTime != null) {
      result.maxTime(this.maxTime, TimeUnit.MILLISECONDS);
    }
    if (this.commitQuorum != null && this.commitQuorum.getValue() != null) {
      result.commitQuorum(this.commitQuorum.getValue());
    }
    return result;
  }
}
