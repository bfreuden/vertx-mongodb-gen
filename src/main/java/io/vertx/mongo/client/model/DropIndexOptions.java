package io.vertx.mongo.client.model;

import io.vertx.codegen.annotations.DataObject;
import java.lang.Long;

/**
 *  The options to apply to the command when dropping indexes.
 *
 *  @mongodb.driver.manual reference/command/dropIndexes/ Drop indexes
 *  @since 3.6
 */
@DataObject(
    generateConverter = true
)
public class DropIndexOptions {
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
  public DropIndexOptions maxTime(Long maxTime) {
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
}
