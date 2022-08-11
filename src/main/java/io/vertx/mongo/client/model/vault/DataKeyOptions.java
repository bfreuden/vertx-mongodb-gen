package io.vertx.mongo.client.model.vault;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;
import java.util.List;

/**
 *  The options for creating a data key.
 *
 *  @since 3.11
 */
@DataObject(
    generateConverter = true
)
public class DataKeyOptions {
  /**
   * a list of alternate key names
   */
  private List keyAltNames;

  /**
   * the master key document
   */
  private JsonObject masterKey;

  /**
   *  Set the alternate key names.
   *
   *  @param keyAltNames a list of alternate key names
   *  @return this
   *  @see #getKeyAltNames()
   */
  public DataKeyOptions keyAltNames(List keyAltNames) {
    return this;
  }

  /**
   *  Gets the alternate key names.
   *
   *  <p>
   *  An optional list of alternate names used to reference a key. If a key is created with alternate names, then encryption may refer
   *  to the key by the unique alternate name instead of by _id.
   *  </p>
   *
   *  @return the list of alternate key names
   */
  public List getKeyAltNames() {
    return keyAltNames;
  }

  /**
   *  Sets the master key document.
   *
   *  @param masterKey the master key document
   *  @return this
   *  @see #getMasterKey()
   */
  public DataKeyOptions masterKey(JsonObject masterKey) {
    return this;
  }

  /**
   *  Gets the master key document
   *
   *  <p>
   *  The masterKey identifies a KMS-specific key used to encrypt the new data key. If the kmsProvider is "aws" it is required and
   *  must have the following fields:
   *  </p>
   *  <ul>
   *    <li>region: a String containing the AWS region in which to locate the master key</li>
   *    <li>key: a String containing the Amazon Resource Name (ARN) to the AWS customer master key</li>
   *  </ul>
   *  <p>
   *  If the kmsProvider is "local" the masterKey is not applicable.
   *  </p>
   *  @return the master key document
   */
  public JsonObject getMasterKey() {
    return masterKey;
  }
}
