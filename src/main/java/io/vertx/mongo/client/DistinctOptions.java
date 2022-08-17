package io.vertx.mongo.client;

import com.mongodb.reactivestreams.client.DistinctPublisher;
import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;
import io.vertx.mongo.client.model.Collation;
import io.vertx.mongo.impl.ConversionUtilsImpl;
import java.lang.Integer;
import java.lang.Long;
import java.util.concurrent.TimeUnit;

/**
 *  Options for distinct.
 *
 *  @since 1.0
 */
@DataObject(
    generateConverter = true
)
public class DistinctOptions {
  /**
   * the filter, which may be null.
   */
  private JsonObject filter;

  /**
   *  the max time
   */
  private Long maxTime;

  /**
   * the collation options to use
   */
  private Collation collation;

  /**
   * the batch size
   */
  private Integer batchSize;

  /**
   *  Sets the query filter to apply to the query.
   *
   *  @param filter the filter, which may be null.
   *  @return this
   *  @mongodb.driver.manual reference/method/db.collection.find/ Filter
   */
  public DistinctOptions filter(JsonObject filter) {
    return this;
  }

  public JsonObject getFilter() {
    return filter;
  }

  /**
   *  Sets the maximum execution time on the server for this operation.
   *
   *  @param maxTime  the max time (in milliseconds)
   *  @return this
   */
  public DistinctOptions maxTime(Long maxTime) {
    return this;
  }

  public Long getMaxTime() {
    return maxTime;
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
  public DistinctOptions collation(Collation collation) {
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
  public DistinctOptions batchSize(Integer batchSize) {
    return this;
  }

  public Integer getBatchSize() {
    return batchSize;
  }

  /**
   * @hidden
   */
  public <TDocument> void initializePublisher(DistinctPublisher<TDocument> publisher) {
    if (this.filter != null) {
      publisher.filter(ConversionUtilsImpl.INSTANCE.toBson(this.filter));
    }
    if (this.maxTime != null) {
      publisher.maxTime(this.maxTime, TimeUnit.MILLISECONDS);
    }
    if (this.collation != null) {
      publisher.collation(this.collation.toDriverClass());
    }
    if (this.batchSize != null) {
      publisher.batchSize(this.batchSize);
    }
  }
}
