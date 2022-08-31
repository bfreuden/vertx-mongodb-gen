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
package io.vertx.mongo.client.model.serializers;

import com.mongodb.CreateIndexCommitQuorum;
import com.mongodb.client.model.CreateIndexOptions;
import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;
import io.vertx.mongo.impl.CreateIndexCommitQuorumSerializer;
import io.vertx.mongo.impl.MongoClientContext;
import java.lang.Long;
import java.util.concurrent.TimeUnit;

@DataObject(
    generateConverter = true
)
public class CreateIndexOptionsSerializer {
  private Long maxTime;

  private CreateIndexCommitQuorum commitQuorum;

  public CreateIndexOptionsSerializer() {
  }

  public CreateIndexOptionsSerializer(JsonObject json) {
    CreateIndexOptionsSerializerConverter.fromJson(json, this);
  }

  public JsonObject toJson() {
    JsonObject result = new JsonObject();
    CreateIndexOptionsSerializerConverter.toJson(this, result);
    return result;
  }

  public CreateIndexOptionsSerializer setMaxTime(Long maxTime) {
    this.maxTime = maxTime;
    return this;
  }

  public Long getMaxTime() {
    return maxTime;
  }

  public CreateIndexOptionsSerializer __setCommitQuorum(CreateIndexCommitQuorum commitQuorum) {
    this.commitQuorum = commitQuorum;
    return this;
  }

  public CreateIndexOptionsSerializer setCommitQuorum(
      CreateIndexCommitQuorumSerializer commitQuorum) {
    this.commitQuorum = commitQuorum == null ? null : commitQuorum.getValue();
    return this;
  }

  public CreateIndexCommitQuorum __getCommitQuorum() {
    return commitQuorum;
  }

  public CreateIndexCommitQuorumSerializer getCommitQuorum() {
    return this.commitQuorum == null ? null : new CreateIndexCommitQuorumSerializer(this.commitQuorum);
  }

  /**
   * @return MongoDB driver object
   * @hidden
   */
  public CreateIndexOptions toDriverClass(MongoClientContext clientContext) {
    CreateIndexOptions result = new CreateIndexOptions();
    if (this.maxTime != null) {
      result.maxTime(this.maxTime, TimeUnit.MILLISECONDS);
    }
    if (this.commitQuorum != null) {
      result.commitQuorum(this.commitQuorum);
    }
    return result;
  }
}
