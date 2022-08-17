package io.vertx.mongo.client;

import com.mongodb.CursorType;
import com.mongodb.reactivestreams.client.FindPublisher;
import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;
import io.vertx.mongo.client.model.Collation;
import io.vertx.mongo.impl.ConversionUtilsImpl;
import java.lang.Boolean;
import java.lang.Deprecated;
import java.lang.Integer;
import java.lang.Long;
import java.lang.String;
import java.util.concurrent.TimeUnit;

/**
 *  Options for find.
 *
 *  @since 1.0
 */
@DataObject(
    generateConverter = true
)
public class FindOptions {
  /**
   * the filter, which may be null.
   */
  private JsonObject filter;

  /**
   * the limit
   */
  private Integer limit;

  /**
   * the number of documents to skip
   */
  private Integer skip;

  /**
   *  the max time
   */
  private Long maxTime;

  /**
   *  the max await time
   */
  private Long maxAwaitTime;

  /**
   * the project document, which may be null.
   */
  private JsonObject projection;

  /**
   * the sort criteria, which may be null.
   */
  private JsonObject sort;

  /**
   * true if cursor timeout is disabled
   */
  private Boolean noCursorTimeout;

  /**
   * if oplog replay is enabled
   */
  private Boolean oplogReplay;

  /**
   * if partial results for sharded clusters is enabled
   */
  private Boolean partial;

  /**
   * the cursor type
   */
  private CursorType cursorType;

  /**
   * the collation options to use
   */
  private Collation collation;

  /**
   * the comment
   */
  private String comment;

  /**
   * the hint
   */
  private JsonObject hint;

  /**
   * the name of the index which should be used for the operation
   */
  private String hintString;

  /**
   * the max
   */
  private JsonObject max;

  /**
   * the min
   */
  private JsonObject min;

  /**
   * the returnKey
   */
  private Boolean returnKey;

  /**
   * the showRecordId
   */
  private Boolean showRecordId;

  /**
   * the batch size
   */
  private Integer batchSize;

  /**
   * the allowDiskUse
   */
  private Boolean allowDiskUse;

  /**
   *  Sets the query filter to apply to the query.
   *
   *  @param filter the filter, which may be null.
   *  @return this
   *  @mongodb.driver.manual reference/method/db.collection.find/ Filter
   */
  public FindOptions filter(JsonObject filter) {
    return this;
  }

  public JsonObject getFilter() {
    return filter;
  }

  /**
   *  Sets the limit to apply.
   *
   *  @param limit the limit
   *  @return this
   *  @mongodb.driver.manual reference/method/cursor.limit/#cursor.limit Limit
   */
  public FindOptions limit(Integer limit) {
    return this;
  }

  public Integer getLimit() {
    return limit;
  }

  /**
   *  Sets the number of documents to skip.
   *
   *  @param skip the number of documents to skip
   *  @return this
   *  @mongodb.driver.manual reference/method/cursor.skip/#cursor.skip Skip
   */
  public FindOptions skip(Integer skip) {
    return this;
  }

  public Integer getSkip() {
    return skip;
  }

  /**
   *  Sets the maximum execution time on the server for this operation.
   *
   *  @param maxTime  the max time (in milliseconds)
   *  @return this
   *  @mongodb.driver.manual reference/method/cursor.maxTimeMS/#cursor.maxTimeMS Max Time
   */
  public FindOptions maxTime(Long maxTime) {
    return this;
  }

  public Long getMaxTime() {
    return maxTime;
  }

  /**
   *  The maximum amount of time for the server to wait on new documents to satisfy a tailable cursor
   *  query. This only applies to a TAILABLE_AWAIT cursor. When the cursor is not a TAILABLE_AWAIT cursor,
   *  this option is ignored.
   *
   *  On servers &gt;= 3.2, this option will be specified on the getMore command as "maxTimeMS". The default
   *  is no value: no "maxTimeMS" is sent to the server with the getMore command.
   *
   *  On servers &lt; 3.2, this option is ignored, and indicates that the driver should respect the server's default value
   *
   *  A zero value will be ignored.
   *
   *  @param maxAwaitTime  the max await time (in milliseconds)
   *  @return the maximum await execution time in the given time unit
   *  @mongodb.driver.manual reference/method/cursor.maxTimeMS/#cursor.maxTimeMS Max Time
   *  @since 1.2
   */
  public FindOptions maxAwaitTime(Long maxAwaitTime) {
    return this;
  }

  public Long getMaxAwaitTime() {
    return maxAwaitTime;
  }

  /**
   *  Sets a document describing the fields to return for all matching documents.
   *
   *  @param projection the project document, which may be null.
   *  @return this
   *  @mongodb.driver.manual reference/method/db.collection.find/ Projection
   */
  public FindOptions projection(JsonObject projection) {
    return this;
  }

  public JsonObject getProjection() {
    return projection;
  }

  /**
   *  Sets the sort criteria to apply to the query.
   *
   *  @param sort the sort criteria, which may be null.
   *  @return this
   *  @mongodb.driver.manual reference/method/cursor.sort/ Sort
   */
  public FindOptions sort(JsonObject sort) {
    return this;
  }

  public JsonObject getSort() {
    return sort;
  }

  /**
   *  The server normally times out idle cursors after an inactivity period (10 minutes)
   *  to prevent excess memory use. Set this option to prevent that.
   *
   *  @param noCursorTimeout true if cursor timeout is disabled
   *  @return this
   */
  public FindOptions noCursorTimeout(Boolean noCursorTimeout) {
    return this;
  }

  public Boolean isNoCursorTimeout() {
    return noCursorTimeout;
  }

  /**
   *  Users should not set this under normal circumstances.
   *
   *  @param oplogReplay if oplog replay is enabled
   *  @return this
   *  @deprecated oplogReplay has been deprecated in MongoDB 4.4.
   */
  @Deprecated
  public FindOptions oplogReplay(Boolean oplogReplay) {
    return this;
  }

  @Deprecated
  public Boolean isOplogReplay() {
    return oplogReplay;
  }

  /**
   *  Get partial results from a sharded cluster if one or more shards are unreachable (instead of throwing an error).
   *
   *  @param partial if partial results for sharded clusters is enabled
   *  @return this
   */
  public FindOptions partial(Boolean partial) {
    return this;
  }

  public Boolean isPartial() {
    return partial;
  }

  /**
   *  Sets the cursor type.
   *
   *  @param cursorType the cursor type
   *  @return this
   */
  public FindOptions cursorType(CursorType cursorType) {
    return this;
  }

  public CursorType getCursorType() {
    return cursorType;
  }

  /**
   *  Sets the collation options
   *
   *  <p>A null value represents the server default.</p>
   *  @param collation the collation options to use
   *  @return this
   *  @since 1.3
   *  @mongodb.server.release 3.4
   */
  public FindOptions collation(Collation collation) {
    return this;
  }

  public Collation getCollation() {
    return collation;
  }

  /**
   *  Sets the comment to the query. A null value means no comment is set.
   *
   *  @param comment the comment
   *  @return this
   *  @since 1.6
   */
  public FindOptions comment(String comment) {
    return this;
  }

  public String getComment() {
    return comment;
  }

  /**
   *  Sets the hint for which index to use. A null value means no hint is set.
   *
   *  @param hint the hint
   *  @return this
   *  @since 1.6
   */
  public FindOptions hint(JsonObject hint) {
    return this;
  }

  public JsonObject getHint() {
    return hint;
  }

  /**
   *  Sets the hint for which index to use. A null value means no hint is set.
   *
   *  @param hint the name of the index which should be used for the operation
   *  @return this
   *  @since 1.13
   */
  public FindOptions hintString(String hint) {
    return this;
  }

  public String getHintString() {
    return hintString;
  }

  /**
   *  Sets the exclusive upper bound for a specific index. A null value means no max is set.
   *
   *  @param max the max
   *  @return this
   *  @since 1.6
   */
  public FindOptions max(JsonObject max) {
    return this;
  }

  public JsonObject getMax() {
    return max;
  }

  /**
   *  Sets the minimum inclusive lower bound for a specific index. A null value means no max is set.
   *
   *  @param min the min
   *  @return this
   *  @since 1.6
   */
  public FindOptions min(JsonObject min) {
    return this;
  }

  public JsonObject getMin() {
    return min;
  }

  /**
   *  Sets the returnKey. If true the find operation will return only the index keys in the resulting documents.
   *
   *  @param returnKey the returnKey
   *  @return this
   *  @since 1.6
   */
  public FindOptions returnKey(Boolean returnKey) {
    return this;
  }

  public Boolean isReturnKey() {
    return returnKey;
  }

  /**
   *  Sets the showRecordId. Set to true to add a field {@code &#x24;recordId} to the returned documents.
   *
   *  @param showRecordId the showRecordId
   *  @return this
   *  @since 1.6
   */
  public FindOptions showRecordId(Boolean showRecordId) {
    return this;
  }

  public Boolean isShowRecordId() {
    return showRecordId;
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
  public FindOptions batchSize(Integer batchSize) {
    return this;
  }

  public Integer getBatchSize() {
    return batchSize;
  }

  /**
   *  Enables writing to temporary files on the server. When set to true, the server
   *  can write temporary data to disk while executing the find operation.
   *
   *  This option is sent only if the caller explicitly sets it to true.
   *
   *  @param allowDiskUse the allowDiskUse
   *  @return this
   *  @since 4.1
   *  @mongodb.server.release 4.4
   */
  public FindOptions allowDiskUse(Boolean allowDiskUse) {
    return this;
  }

  public Boolean isAllowDiskUse() {
    return allowDiskUse;
  }

  /**
   * @hidden
   */
  public <TDocument> void initializePublisher(FindPublisher<TDocument> publisher) {
    if (this.filter != null) {
      publisher.filter(ConversionUtilsImpl.INSTANCE.toBson(this.filter));
    }
    if (this.limit != null) {
      publisher.limit(this.limit);
    }
    if (this.skip != null) {
      publisher.skip(this.skip);
    }
    if (this.maxTime != null) {
      publisher.maxTime(this.maxTime, TimeUnit.MILLISECONDS);
    }
    if (this.maxAwaitTime != null) {
      publisher.maxAwaitTime(this.maxAwaitTime, TimeUnit.MILLISECONDS);
    }
    if (this.projection != null) {
      publisher.projection(ConversionUtilsImpl.INSTANCE.toBson(this.projection));
    }
    if (this.sort != null) {
      publisher.sort(ConversionUtilsImpl.INSTANCE.toBson(this.sort));
    }
    if (this.noCursorTimeout != null) {
      publisher.noCursorTimeout(this.noCursorTimeout);
    }
    if (this.oplogReplay != null) {
      publisher.oplogReplay(this.oplogReplay);
    }
    if (this.partial != null) {
      publisher.partial(this.partial);
    }
    if (this.cursorType != null) {
      publisher.cursorType(this.cursorType);
    }
    if (this.collation != null) {
      publisher.collation(this.collation.toDriverClass());
    }
    if (this.comment != null) {
      publisher.comment(this.comment);
    }
    if (this.hint != null) {
      publisher.hint(ConversionUtilsImpl.INSTANCE.toBson(this.hint));
    }
    if (this.hintString != null) {
      publisher.hintString(this.hintString);
    }
    if (this.max != null) {
      publisher.max(ConversionUtilsImpl.INSTANCE.toBson(this.max));
    }
    if (this.min != null) {
      publisher.min(ConversionUtilsImpl.INSTANCE.toBson(this.min));
    }
    if (this.returnKey != null) {
      publisher.returnKey(this.returnKey);
    }
    if (this.showRecordId != null) {
      publisher.showRecordId(this.showRecordId);
    }
    if (this.batchSize != null) {
      publisher.batchSize(this.batchSize);
    }
    if (this.allowDiskUse != null) {
      publisher.allowDiskUse(this.allowDiskUse);
    }
  }
}
