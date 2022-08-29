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
import java.lang.Boolean;

/**
 *  Settings for connecting to MongoDB via SSL.
 *
 *  @since 3.0
 */
@DataObject(
    generateConverter = true
)
public class SslSettings {
  /**
   * the sslSettings
   */
  private SslSettings applySettings;

  /**
   * should be true if SSL is to be enabled.
   */
  private Boolean enabled;

  /**
   * whether invalid host names are allowed.
   */
  private Boolean invalidHostNameAllowed;

  /**
   * @return MongoDB driver object
   * @hidden
   */
  public com.mongodb.connection.SslSettings toDriverClass() {
    com.mongodb.connection.SslSettings.Builder builder = com.mongodb.connection.SslSettings.builder();
    initializeDriverBuilderClass(builder);
    return builder.build();
  }

  /**
   *  Applies the sslSettings to the builder
   *
   *  <p>Note: Overwrites all existing settings</p>
   *
   *  @param sslSettings the sslSettings
   *  @return this
   *  @since 3.7
   */
  public SslSettings applySettings(SslSettings sslSettings) {
    this.applySettings = sslSettings;
    return this;
  }

  public SslSettings getApplySettings() {
    return applySettings;
  }

  /**
   *  Define whether SSL should be enabled.
   *
   *  @param enabled should be true if SSL is to be enabled.
   *  @return this
   */
  public SslSettings enabled(Boolean enabled) {
    this.enabled = enabled;
    return this;
  }

  /**
   *  Returns whether SSL is enabled.
   *
   *  @return true if SSL is enabled.
   */
  public Boolean isEnabled() {
    return enabled;
  }

  /**
   *  Define whether invalid host names should be allowed.  Defaults to false.  Take care before setting this to true, as it makes
   *  the application susceptible to man-in-the-middle attacks.
   *
   *  @param invalidHostNameAllowed whether invalid host names are allowed.
   *  @return this
   */
  public SslSettings invalidHostNameAllowed(Boolean invalidHostNameAllowed) {
    this.invalidHostNameAllowed = invalidHostNameAllowed;
    return this;
  }

  /**
   *  Returns whether invalid host names should be allowed.  Defaults to false.  Take care before setting this to true, as it makes
   *  the application susceptible to man-in-the-middle attacks.
   *
   *  @return true if invalid host names are allowed.
   */
  public Boolean isInvalidHostNameAllowed() {
    return invalidHostNameAllowed;
  }

  /**
   * @param builder MongoDB driver builder
   * @hidden
   */
  public void initializeDriverBuilderClass(com.mongodb.connection.SslSettings.Builder builder) {
    if (this.applySettings != null) {
      builder.applySettings(this.applySettings.toDriverClass());
    }
    if (this.enabled != null) {
      builder.enabled(this.enabled);
    }
    if (this.invalidHostNameAllowed != null) {
      builder.invalidHostNameAllowed(this.invalidHostNameAllowed);
    }
  }
}