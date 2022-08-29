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
package io.vertx.mongo.client.model;

import com.mongodb.client.model.CollationAlternate;
import com.mongodb.client.model.CollationCaseFirst;
import com.mongodb.client.model.CollationMaxVariable;
import com.mongodb.client.model.CollationStrength;
import io.vertx.codegen.annotations.DataObject;
import java.lang.Boolean;
import java.lang.String;

/**
 *  The options regarding collation support in MongoDB 3.4+
 *
 *  @mongodb.driver.manual reference/method/db.createCollection/ Create Collection
 *  @mongodb.driver.manual reference/command/createIndexes Index options
 *  @since 3.4
 *  @mongodb.server.release 3.4
 */
@DataObject(
    generateConverter = true
)
public class Collation {
  /**
   * the locale
   */
  private String locale;

  /**
   * the case level value
   */
  private Boolean caseLevel;

  /**
   * the collation case first value
   */
  private CollationCaseFirst caseFirst;

  /**
   * the strength
   */
  private CollationStrength strength;

  /**
   * if true will order numbers based on numerical order and not collation order
   */
  private Boolean numericOrdering;

  /**
   * the alternate
   */
  private CollationAlternate alternate;

  /**
   * the maxVariable
   */
  private CollationMaxVariable maxVariable;

  /**
   * the normalization value
   */
  private Boolean normalization;

  /**
   * the backwards value
   */
  private Boolean backwards;

  /**
   * @return MongoDB driver object
   * @hidden
   */
  public com.mongodb.client.model.Collation toDriverClass() {
    com.mongodb.client.model.Collation.Builder builder = com.mongodb.client.model.Collation.builder();
    initializeDriverBuilderClass(builder);
    return builder.build();
  }

  /**
   *  Sets the locale
   *
   *  @param locale the locale
   *  @see <a href="http://userguide.icu-project.org/locale">ICU User Guide - Locale</a>
   *  @return this
   */
  public Collation setLocale(String locale) {
    this.locale = locale;
    return this;
  }

  /**
   *  Returns the locale
   *
   *  @see <a href="http://userguide.icu-project.org/locale">ICU User Guide - Locale</a>
   *  @return the locale
   */
  public String getLocale() {
    return locale;
  }

  /**
   *  Sets the case level value
   *
   *  <p>Turns on case sensitivity</p>
   *  @param caseLevel the case level value
   *  @return this
   */
  public Collation setCaseLevel(Boolean caseLevel) {
    this.caseLevel = caseLevel;
    return this;
  }

  /**
   *  Returns the case level value
   *
   *  @return the case level value
   */
  public Boolean isCaseLevel() {
    return caseLevel;
  }

  /**
   *  Sets the collation case first value
   *
   *  <p>Determines if Uppercase or lowercase values should come first</p>
   *  @param caseFirst the collation case first value
   *  @return this
   */
  public Collation setCaseFirst(CollationCaseFirst caseFirst) {
    this.caseFirst = caseFirst;
    return this;
  }

  /**
   *  Returns the collation case first value
   *
   *  @return the collation case first value
   */
  public CollationCaseFirst getCaseFirst() {
    return caseFirst;
  }

  /**
   *  Sets the collation strength
   *
   *  @param strength the strength
   *  @return this
   */
  public Collation setStrength(CollationStrength strength) {
    this.strength = strength;
    return this;
  }

  /**
   *  Returns the collation strength
   *
   *  @return the collation strength
   */
  public CollationStrength getStrength() {
    return strength;
  }

  /**
   *  Sets the numeric ordering
   *
   *  @param numericOrdering if true will order numbers based on numerical order and not collation order
   *  @return this
   */
  public Collation setNumericOrdering(Boolean numericOrdering) {
    this.numericOrdering = numericOrdering;
    return this;
  }

  /**
   *  Returns the numeric ordering, if true will order numbers based on numerical order and not collation order.
   *
   *  @return the numeric ordering
   */
  public Boolean isNumericOrdering() {
    return numericOrdering;
  }

  /**
   *  Sets the alternate
   *
   *  <p>Controls whether spaces and punctuation are considered base characters</p>
   *
   *  @param alternate the alternate
   *  @return this
   */
  public Collation setAlternate(CollationAlternate alternate) {
    this.alternate = alternate;
    return this;
  }

  /**
   *  Returns the collation alternate
   *
   *  @return the alternate
   */
  public CollationAlternate getAlternate() {
    return alternate;
  }

  /**
   *  Sets the maxVariable
   *
   *  @param maxVariable the maxVariable
   *  @return this
   */
  public Collation setMaxVariable(CollationMaxVariable maxVariable) {
    this.maxVariable = maxVariable;
    return this;
  }

  /**
   *  Returns the maxVariable
   *
   *  <p>Controls which characters are affected by collection alternate {@link CollationAlternate#SHIFTED}.</p>
   *  @return the maxVariable
   */
  public CollationMaxVariable getMaxVariable() {
    return maxVariable;
  }

  /**
   *  Sets the normalization value
   *
   *  <p>If true, normalizes text into Unicode NFD.</p>
   *  @param normalization the normalization value
   *  @return this
   */
  public Collation setNormalization(Boolean normalization) {
    this.normalization = normalization;
    return this;
  }

  /**
   *  Returns the normalization value
   *
   *  <p>If true, normalizes text into Unicode NFD.</p>
   *  @return the normalization
   */
  public Boolean isNormalization() {
    return normalization;
  }

  /**
   *  Sets the backwards value
   *
   *  <p>Causes secondary differences to be considered in reverse order, as it is done in the French language</p>
   *
   *  @param backwards the backwards value
   *  @return this
   */
  public Collation setBackwards(Boolean backwards) {
    this.backwards = backwards;
    return this;
  }

  /**
   *  Returns the backwards value
   *
   *  @return the backwards value
   */
  public Boolean isBackwards() {
    return backwards;
  }

  /**
   * @param builder MongoDB driver builder
   * @hidden
   */
  public void initializeDriverBuilderClass(com.mongodb.client.model.Collation.Builder builder) {
    if (this.locale != null) {
      builder.locale(this.locale);
    }
    if (this.caseLevel != null) {
      builder.caseLevel(this.caseLevel);
    }
    if (this.caseFirst != null) {
      builder.collationCaseFirst(this.caseFirst);
    }
    if (this.strength != null) {
      builder.collationStrength(this.strength);
    }
    if (this.numericOrdering != null) {
      builder.numericOrdering(this.numericOrdering);
    }
    if (this.alternate != null) {
      builder.collationAlternate(this.alternate);
    }
    if (this.maxVariable != null) {
      builder.collationMaxVariable(this.maxVariable);
    }
    if (this.normalization != null) {
      builder.normalization(this.normalization);
    }
    if (this.backwards != null) {
      builder.backwards(this.backwards);
    }
  }
}
