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
package io.vertx.mongo.client.gridfs.model;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;
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

  public GridFSDownloadOptions() {
  }

  public GridFSDownloadOptions(JsonObject json) {
    GridFSDownloadOptionsConverter.fromJson(json, this);
  }

  public JsonObject toJson() {
    JsonObject result = new JsonObject();
    GridFSDownloadOptionsConverter.toJson(this, result);
    return result;
  }

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
  public GridFSDownloadOptions setRevision(Integer revision) {
    this.revision = revision;
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

  /**
   * @return MongoDB driver object
   * @hidden
   */
  public com.mongodb.client.gridfs.model.GridFSDownloadOptions toDriverClass() {
    com.mongodb.client.gridfs.model.GridFSDownloadOptions result = new com.mongodb.client.gridfs.model.GridFSDownloadOptions();
    if (this.revision != null) {
      result.revision(this.revision);
    }
    return result;
  }
}
