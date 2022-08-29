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
package io.vertx.mongo.connection;

import io.vertx.codegen.annotations.DataObject;
import java.lang.Integer;
import java.util.concurrent.TimeUnit;

/**
 *  An immutable class representing socket settings used for connections to a MongoDB server.
 *
 *  @since 3.0
 */
@DataObject(
    generateConverter = true
)
public class SocketSettings {
  /**
   * the socketSettings
   */
  private SocketSettings applySettings;

  /**
   * the connect timeout
   */
  private Integer connectTimeout;

  /**
   * the read timeout
   */
  private Integer readTimeout;

  /**
   * the receive buffer size
   */
  private Integer receiveBufferSize;

  /**
   * the send buffer size
   */
  private Integer sendBufferSize;

  /**
   * @return MongoDB driver object
   * @hidden
   */
  public com.mongodb.connection.SocketSettings toDriverClass() {
    com.mongodb.connection.SocketSettings.Builder builder = com.mongodb.connection.SocketSettings.builder();
    initializeDriverBuilderClass(builder);
    return builder.build();
  }

  /**
   *  Applies the socketSettings to the builder
   *
   *  <p>Note: Overwrites all existing settings</p>
   *
   *  @param socketSettings the socketSettings
   *  @return this
   *  @since 3.7
   */
  public SocketSettings applySettings(SocketSettings socketSettings) {
    this.applySettings = socketSettings;
    return this;
  }

  public SocketSettings getApplySettings() {
    return applySettings;
  }

  /**
   *  Sets the socket connect timeout.
   *
   *  @param connectTimeout the connect timeout (in milliseconds)
   *  @return this
   */
  public SocketSettings connectTimeout(Integer connectTimeout) {
    this.connectTimeout = connectTimeout;
    return this;
  }

  /**
   *  Gets the timeout for socket connect.  Defaults to 10 seconds.
   *
   *  @return the connect timeout in the requested time unit.
   */
  public Integer getConnectTimeout() {
    return connectTimeout;
  }

  /**
   *  Sets the socket read timeout.
   *
   *  @param readTimeout the read timeout (in milliseconds)
   *  @return this
   */
  public SocketSettings readTimeout(Integer readTimeout) {
    this.readTimeout = readTimeout;
    return this;
  }

  /**
   *  Gets the timeout for socket reads.  Defaults to 0, which indicates no timeout
   *
   *  @return the read timeout in the requested time unit, or 0 if there is no timeout
   */
  public Integer getReadTimeout() {
    return readTimeout;
  }

  /**
   *  Sets the receive buffer size.
   *
   *  @param receiveBufferSize the receive buffer size
   *  @return this
   */
  public SocketSettings receiveBufferSize(Integer receiveBufferSize) {
    this.receiveBufferSize = receiveBufferSize;
    return this;
  }

  /**
   *  Gets the receive buffer size. Defaults to the operating system default.
   *  @return the receive buffer size
   */
  public Integer getReceiveBufferSize() {
    return receiveBufferSize;
  }

  /**
   *  Sets the send buffer size.
   *
   *  @param sendBufferSize the send buffer size
   *  @return this
   */
  public SocketSettings sendBufferSize(Integer sendBufferSize) {
    this.sendBufferSize = sendBufferSize;
    return this;
  }

  /**
   *  Gets the send buffer size.  Defaults to the operating system default.
   *
   *  @return the send buffer size
   */
  public Integer getSendBufferSize() {
    return sendBufferSize;
  }

  /**
   * @param builder MongoDB driver builder
   * @hidden
   */
  public void initializeDriverBuilderClass(com.mongodb.connection.SocketSettings.Builder builder) {
    if (this.applySettings != null) {
      builder.applySettings(this.applySettings.toDriverClass());
    }
    if (this.connectTimeout != null) {
      builder.connectTimeout(this.connectTimeout, TimeUnit.MILLISECONDS);
    }
    if (this.readTimeout != null) {
      builder.readTimeout(this.readTimeout, TimeUnit.MILLISECONDS);
    }
    if (this.receiveBufferSize != null) {
      builder.receiveBufferSize(this.receiveBufferSize);
    }
    if (this.sendBufferSize != null) {
      builder.sendBufferSize(this.sendBufferSize);
    }
  }
}
