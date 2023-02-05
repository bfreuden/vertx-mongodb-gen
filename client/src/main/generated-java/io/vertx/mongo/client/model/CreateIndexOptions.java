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
import io.vertx.core.json.JsonObject;
import io.vertx.mongo.client.model.serializers.CreateIndexOptionsSerializer;
import io.vertx.mongo.impl.MongoClientContext;
import java.lang.Long;

@DataObject
public class CreateIndexOptions {
  private CreateIndexOptionsSerializer serializer = new CreateIndexOptionsSerializer();

  public CreateIndexOptions() {
  }

  public CreateIndexOptions(JsonObject json) {
    serializer = new CreateIndexOptionsSerializer(json);
  }

  public JsonObject toJson() {
    return serializer.toJson();
  }

  /**
   *  Sets the maximum execution time on the server for this operation.
   *
   *  @param maxTime  the max time (in milliseconds)
   *  @return this
   */
  public CreateIndexOptions setMaxTime(Long maxTime) {
    this.serializer.setMaxTime(maxTime);
    return this;
  }

  /**
   *  Gets the maximum execution time on the server for this operation.  The default is 0, which places no limit on the execution time.
   *
   *  @return the maximum execution time (in milliseconds)
   */
  public Long getMaxTime() {
    return this.serializer.getMaxTime();
  }

  /**
   *  Sets the create index commit quorum for this operation.
   *
   *  @param commitQuorum the create index commit quorum
   *  @return this
   *  @mongodb.server.release 4.4
   *  @since 4.1
   */
  public CreateIndexOptions setCommitQuorum(CreateIndexCommitQuorum commitQuorum) {
    this.serializer.__setCommitQuorum(commitQuorum);
    return this;
  }

  /**
   *  Gets the create index commit quorum for this operation.
   *
   *  @return the create index commit quorum
   *  @mongodb.server.release 4.4
   *  @since 4.1
   */
  public CreateIndexCommitQuorum getCommitQuorum() {
    return this.serializer.__getCommitQuorum();
  }

  /**
   * @return MongoDB driver object
   * @hidden
   */
  public com.mongodb.client.model.CreateIndexOptions toDriverClass(
      MongoClientContext clientContext) {
    return this.serializer.toDriverClass(clientContext);
  }
}
