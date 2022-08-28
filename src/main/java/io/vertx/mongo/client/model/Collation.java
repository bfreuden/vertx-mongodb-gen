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
  private CollationCaseFirst CaseFirst;

  /**
   * the strength
   */
  private CollationStrength Strength;

  /**
   * if true will order numbers based on numerical order and not collation order
   */
  private Boolean numericOrdering;

  /**
   * the alternate
   */
  private CollationAlternate Alternate;

  /**
   * the maxVariable
   */
  private CollationMaxVariable MaxVariable;

  /**
   * the normalization value
   */
  private Boolean normalization;

  /**
   * the backwards value
   */
  private Boolean backwards;

  /**
   *  Sets the locale
   *
   *  @param locale the locale
   *  @see <a href="http://userguide.icu-project.org/locale">ICU User Guide - Locale</a>
   *  @return this
   */
  public Collation locale(String locale) {
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
  public Collation caseLevel(Boolean caseLevel) {
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
  public Collation CaseFirst(CollationCaseFirst caseFirst) {
    this.CaseFirst = CaseFirst;
    return this;
  }

  /**
   *  Returns the collation case first value
   *
   *  @return the collation case first value
   */
  public CollationCaseFirst getCaseFirst() {
    return CaseFirst;
  }

  /**
   *  Sets the collation strength
   *
   *  @param strength the strength
   *  @return this
   */
  public Collation Strength(CollationStrength strength) {
    this.Strength = Strength;
    return this;
  }

  /**
   *  Returns the collation strength
   *
   *  @return the collation strength
   */
  public CollationStrength getStrength() {
    return Strength;
  }

  /**
   *  Sets the numeric ordering
   *
   *  @param numericOrdering if true will order numbers based on numerical order and not collation order
   *  @return this
   */
  public Collation numericOrdering(Boolean numericOrdering) {
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
  public Collation Alternate(CollationAlternate alternate) {
    this.Alternate = Alternate;
    return this;
  }

  /**
   *  Returns the collation alternate
   *
   *  @return the alternate
   */
  public CollationAlternate getAlternate() {
    return Alternate;
  }

  /**
   *  Sets the maxVariable
   *
   *  @param maxVariable the maxVariable
   *  @return this
   */
  public Collation MaxVariable(CollationMaxVariable maxVariable) {
    this.MaxVariable = MaxVariable;
    return this;
  }

  /**
   *  Returns the maxVariable
   *
   *  <p>Controls which characters are affected by collection alternate {@link CollationAlternate#SHIFTED}.</p>
   *  @return the maxVariable
   */
  public CollationMaxVariable getMaxVariable() {
    return MaxVariable;
  }

  /**
   *  Sets the normalization value
   *
   *  <p>If true, normalizes text into Unicode NFD.</p>
   *  @param normalization the normalization value
   *  @return this
   */
  public Collation normalization(Boolean normalization) {
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
  public Collation backwards(Boolean backwards) {
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
   * @return MongoDB driver object
   * @hidden
   */
  public com.mongodb.client.model.Collation toDriverClass() {
    com.mongodb.client.model.Collation.Builder builder = com.mongodb.client.model.Collation.builder();
    if (this.locale != null) {
      builder.locale(this.locale);
    }
    if (this.caseLevel != null) {
      builder.caseLevel(this.caseLevel);
    }
    if (this.CaseFirst != null) {
      builder.collationCaseFirst(this.CaseFirst);
    }
    if (this.Strength != null) {
      builder.collationStrength(this.Strength);
    }
    if (this.numericOrdering != null) {
      builder.numericOrdering(this.numericOrdering);
    }
    if (this.Alternate != null) {
      builder.collationAlternate(this.Alternate);
    }
    if (this.MaxVariable != null) {
      builder.collationMaxVariable(this.MaxVariable);
    }
    if (this.normalization != null) {
      builder.normalization(this.normalization);
    }
    if (this.backwards != null) {
      builder.backwards(this.backwards);
    }
    return builder.build();
  }
}
