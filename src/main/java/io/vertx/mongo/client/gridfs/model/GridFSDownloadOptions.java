package io.vertx.mongo.client.gridfs.model;

import io.vertx.codegen.annotations.DataObject;
import java.lang.Integer;

/**
 *  The GridFS download by name options
 *
 *  <p>Controls the selection of the revision to download</p>
 *
 *  @since 3.3
 */
@DataObject(
    generateConverter = true
)
public class GridFSDownloadOptions {
  /**
   * the file revision to download
   */
  private Integer revision;

  /**
   *  Set the revision of the file to retrieve.
   *
   *  <p>Revision numbers are defined as follows:</p>
   *  <ul>
   *   <li><strong>0</strong> = the original stored file</li>
   *   <li><strong>1</strong> = the first revision</li>
   *   <li><strong>2</strong> = the second revision</li>
   *   <li>etc..</li>
   *   <li><strong>-2</strong> = the second most recent revision</li>
   *   <li><strong>-1</strong> = the most recent revision</li>
   *  </ul>
   *
   *
   *  @param revision the file revision to download
   *  @return this
   */
  public GridFSDownloadOptions revision(Integer revision) {
    return this;
  }

  /**
   *  Gets the revision to download identifier
   *
   *  @return the revision to download identifier
   */
  public Integer getRevision() {
    return revision;
  }
}
