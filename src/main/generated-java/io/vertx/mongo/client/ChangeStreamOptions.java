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
package io.vertx.mongo.client;

import com.mongodb.client.model.changestream.FullDocument;
import com.mongodb.reactivestreams.client.ChangeStreamPublisher;
import io.vertx.core.json.JsonObject;
import io.vertx.mongo.client.model.Collation;
import io.vertx.mongo.impl.MongoClientContext;
import java.lang.Integer;
import java.lang.Long;
import java.util.concurrent.TimeUnit;

/**
 *  Options for change streams.
 *
 *  @mongodb.server.release 3.6
 *  @since 1.6
 */
public class ChangeStreamOptions {
  /**
   * the fullDocument
   */
  private FullDocument fullDocument;

  /**
   * the resume token
   */
  private JsonObject resumeAfter;

  /**
   * the start at operation time
   */
  private Long startAtOperationTime;

  /**
   * the startAfter resumeToken
   */
  private JsonObject startAfter;

  /**
   *  the max await time.  A zero value will be ignored, and indicates that the driver should respect the server's
   */
  private Long maxAwaitTime;

  /**
   * the collation options to use
   */
  private Collation collation;

  /**
   * the batch size
   */
  private Integer batchSize;

  /**
   *  Sets the fullDocument value.
   *
   *  @param fullDocument the fullDocument
   *  @return this
   */
  public ChangeStreamOptions setFullDocument(FullDocument fullDocument) {
    this.fullDocument = fullDocument;
    return this;
  }

  public FullDocument getFullDocument() {
    return fullDocument;
  }

  /**
   *  Sets the logical starting point for the new change stream.
   *
   *  @param resumeToken the resume token
   *  @return this
   */
  public ChangeStreamOptions setResumeAfter(JsonObject resumeToken) {
    this.resumeAfter = resumeToken;
    return this;
  }

  public JsonObject getResumeAfter() {
    return resumeAfter;
  }

  /**
   *  The change stream will only provide changes that occurred after the specified timestamp.
   *
   *  <p>Any command run against the server will return an operation time that can be used here.</p>
   *  <p>The default value is an operation time obtained from the server before the change stream was created.</p>
   *
   *  @param startAtOperationTime the start at operation time
   *  @since 1.9
   *  @return this
   *  @mongodb.server.release 4.0
   *  @mongodb.driver.manual reference/method/db.runCommand/
   */
  public ChangeStreamOptions setStartAtOperationTime(Long startAtOperationTime) {
    this.startAtOperationTime = startAtOperationTime;
    return this;
  }

  public Long getStartAtOperationTime() {
    return startAtOperationTime;
  }

  /**
   *  Similar to {@code resumeAfter}, this option takes a resume token and starts a
   *  new change stream returning the first notification after the token.
   *
   *  <p>This will allow users to watch collections that have been dropped and recreated
   *  or newly renamed collections without missing any notifications.</p>
   *
   *  <p>Note: The server will report an error if both {@code startAfter} and {@code resumeAfter} are specified.</p>
   *
   *  @param startAfter the startAfter resumeToken
   *  @return this
   *  @since 1.12
   *  @mongodb.server.release 4.2
   *  @mongodb.driver.manual changeStreams/#change-stream-start-after
   */
  public ChangeStreamOptions setStartAfter(JsonObject startAfter) {
    this.startAfter = startAfter;
    return this;
  }

  public JsonObject getStartAfter() {
    return startAfter;
  }

  /**
   *  Sets the maximum await execution time on the server for this operation.
   *
   *  @param maxAwaitTime  the max await time.  A zero value will be ignored, and indicates that the driver should respect the server's (in milliseconds)
   *                       default value
   *  @return this
   */
  public ChangeStreamOptions setMaxAwaitTime(Long maxAwaitTime) {
    this.maxAwaitTime = maxAwaitTime;
    return this;
  }

  public Long getMaxAwaitTime() {
    return maxAwaitTime;
  }

  /**
   *  Sets the collation options
   *
   *  <p>A null value represents the server default.</p>
   *  @param collation the collation options to use
   *  @return this
   */
  public ChangeStreamOptions setCollation(Collation collation) {
    this.collation = collation;
    return this;
  }

  public Collation getCollation() {
    return collation;
  }

  /**
   *  Sets the number of documents to return per batch.
   *
   *  <p>Overrides the {@link org.reactivestreams.Subscription#request(long)} value for setting the batch size, allowing for fine grained
   *  control over the underlying cursor.</p>
   *
   *  @param batchSize the batch size
   *  @return this
   *  @since 1.8
   *  @mongodb.driver.manual reference/method/cursor.batchSize/#cursor.batchSize Batch Size
   */
  public ChangeStreamOptions setBatchSize(Integer batchSize) {
    this.batchSize = batchSize;
    return this;
  }

  public Integer getBatchSize() {
    return batchSize;
  }

  /**
   * @param publisher MongoDB driver publisher
   * @param <TDocument> document class
   * @hidden
   */
  public <TDocument> void initializePublisher(MongoClientContext clientContext,
      ChangeStreamPublisher<TDocument> publisher) {
    if (this.fullDocument != null) {
      publisher.fullDocument(this.fullDocument);
    }
    if (this.resumeAfter != null) {
      publisher.resumeAfter(clientContext.getConversionUtils().toBsonDocument(this.resumeAfter));
    }
    if (this.startAtOperationTime != null) {
      publisher.startAtOperationTime(clientContext.getConversionUtils().toBsonTimestamp(this.startAtOperationTime));
    }
    if (this.startAfter != null) {
      publisher.startAfter(clientContext.getConversionUtils().toBsonDocument(this.startAfter));
    }
    if (this.maxAwaitTime != null) {
      publisher.maxAwaitTime(this.maxAwaitTime, TimeUnit.MILLISECONDS);
    }
    if (this.collation != null) {
      publisher.collation(this.collation.toDriverClass(clientContext));
    }
    if (this.batchSize != null) {
      publisher.batchSize(this.batchSize);
    }
  }
}
