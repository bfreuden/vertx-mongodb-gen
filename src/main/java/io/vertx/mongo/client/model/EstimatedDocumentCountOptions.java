package io.vertx.mongo.client.model;

import io.vertx.codegen.annotations.DataObject;
import java.lang.Long;
import java.util.concurrent.TimeUnit;

/**
 *  The options an estimated count operation.
 *
 *  @since 3.8
 *  @mongodb.driver.manual reference/command/count/ Count
 */
@DataObject(
    generateConverter = true
)
public class EstimatedDocumentCountOptions {
  /**
   *  the max time
   */
  private Long maxTime;

  /**
   *  Sets the maximum execution time on the server for this operation.
   *
   *  @param maxTime  the max time (in milliseconds)
   *  @return this
   */
  public EstimatedDocumentCountOptions maxTime(Long maxTime) {
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
   * @return MongoDB driver object
   * @hidden
   */
  public com.mongodb.client.model.EstimatedDocumentCountOptions toDriverClass() {
    com.mongodb.client.model.EstimatedDocumentCountOptions result = new com.mongodb.client.model.EstimatedDocumentCountOptions();
    if (this.maxTime != null) {
      result.maxTime(this.maxTime, TimeUnit.MILLISECONDS);
    }
    return result;
  }
}
