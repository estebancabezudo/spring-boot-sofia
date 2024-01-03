/**
 * MIT License
 * <p>
 * Copyright (c) 2017 Esteban Cabezudo
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package net.cabezudo.json.values;

import net.cabezudo.json.Position;


import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * A {@link net.cabezudo.json.values.JSONNumber} is an object extended from {@link net.cabezudo.json.values.JSONValue} object in order to represent a number that can be used to
 * create JSON structures.
 *
 * @author <a href="http://cabezudo.net">Esteban Cabezudo</a>
 * @version 0.9, 10/01/2014
 */
public class JSONNumber extends JSONValue<JSONNumber> {

  /**
   * The default scale for the BigDecimal conversions.
   */
  public static final int DEFAULT_SCALE = 6;

  private final BigDecimal value;

  /**
   * Construct a new {@code String} value.
   *
   * @param value a {@code String} with a number.
   */
  public JSONNumber(String value) {
    this(new BigDecimal(value));
  }

  /**
   * Construct a {@link net.cabezudo.json.values.JSONNumber} using a {@code Byte} value.
   *
   * @param value a {@code Byte} with the number.
   */
  public JSONNumber(Byte value) {
    this(new BigDecimal(value));
  }

  /**
   * Construct a {@link net.cabezudo.json.values.JSONNumber} using a {@code Short} value.
   *
   * @param value a {@code Short} with the number.
   */
  public JSONNumber(Short value) {
    this(new BigDecimal(value));
  }

  /**
   * Construct a {@link net.cabezudo.json.values.JSONNumber} using a {@code Integer} value.
   *
   * @param value a {@code Integer} with the number.
   */
  public JSONNumber(Integer value) {
    this(new BigDecimal(value));
  }

  /**
   * Construct a {@link net.cabezudo.json.values.JSONNumber} using a {@code Long} value.
   *
   * @param value a {@code Long} with the number.
   */
  public JSONNumber(Long value) {
    this(new BigDecimal(value));
  }

  /**
   * Construct a {@link net.cabezudo.json.values.JSONNumber} using a {@code Float} value.
   *
   * @param value a {@code Float} with the number.
   */
  public JSONNumber(Float value) {
    this(new BigDecimal(value));
  }

  /**
   * Construct a {@link net.cabezudo.json.values.JSONNumber} using a {@code Double} value.
   *
   * @param value a {@code Double} with the number.
   */
  public JSONNumber(Double value) {
    this(new BigDecimal(value));
  }

  /**
   * Construct a {@link net.cabezudo.json.values.JSONNumber} using a {@code BigDecimal} value.
   *
   * @param value a {@code BigDecimal} with the number.
   */
  public JSONNumber(BigDecimal value) {
    this(value, null);
  }

  /**
   * Construct a {@link net.cabezudo.json.values.JSONNumber} using a {@code BigDecimal} value. The method also takes a {@link Position} in order to save the position in origin and
   * give it when a parse error is thrown.
   *
   * @param value    a {@code BigDecimal} with the number.
   * @param position the position of the value in the source.
   */
  public JSONNumber(BigDecimal value, Position position) {
    super(position);
    if (value == null) {
      throw new IllegalArgumentException("You can't create an object using null.");
    }
    this.value = value.round(MathContext.UNLIMITED).setScale(DEFAULT_SCALE, RoundingMode.HALF_UP).stripTrailingZeros();
  }

  /**
   * Construct a {@link net.cabezudo.json.values.JSONNumber} using a {@code BigInteger} value.
   *
   * @param value a {@code BigInteger} with a number.
   */
  public JSONNumber(BigInteger value) {
    super(null);
    if (value == null) {
      throw new IllegalArgumentException("You can't create an object using null.");
    }
    this.value = new BigDecimal(value).setScale(DEFAULT_SCALE).stripTrailingZeros();
  }

  /**
   * Compares two {@link net.cabezudo.json.values.JSONNumber} objects.
   *
   * @param jsonNumber the {@link net.cabezudo.json.values.JSONNumber} to be compared.
   * @return the value {@code 0} if {@code this} {@link net.cabezudo.json.values.JSONNumber} is equal to the argument {@link net.cabezudo.json.values.JSONNumber}; a value less than
   * {@code 0} if {@code this} {@link net.cabezudo.json.values.JSONNumber} is less than the argument {@link net.cabezudo.json.values.JSONNumber}; and a value greater than {@code 0}
   * if {@code this} {@link net.cabezudo.json.values.JSONNumber} is greater than the argument {@link net.cabezudo.json.values.JSONNumber}.
   */
  @Override
  public int compareTo(JSONNumber jsonNumber) {
    return value.compareTo(jsonNumber.value);
  }

  /**
   * Compares two {@link net.cabezudo.json.values.JSONNumber} objects.
   *
   * @param o the {@code Object} to be compared.
   * @return {@code true} if {@code this} {@link net.cabezudo.json.values.JSONNumber} is equal to the argument {@code false} otherwise.
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null) {
      return false;
    }
    if (getClass() != o.getClass()) {
      return false;
    }
    final JSONNumber jsonNumber = (JSONNumber) o;
    return value.equals(jsonNumber.value);
  }

  /**
   * Return the referenced element for {@code this} object. For a {@link net.cabezudo.json.values.JSONNumber} object, {@code this} object and the referenced version is the same.
   *
   * @return {@code this} object.
   */
  @Override
  public JSONNumber toReferencedElement() {
    return this;
  }

  /**
   * Return the hash code for this {@link net.cabezudo.json.values.JSONNumber}.
   *
   * @return hash code for this {@link net.cabezudo.json.values.JSONNumber}.
   */
  @Override
  public int hashCode() {
    return value.hashCode();
  }

  /**
   * Return whether the element is a {@link net.cabezudo.json.values.JSONNumber} or not.
   *
   * @return {@code true}.
   */
  @Override
  public boolean isNumber() {
    return true;
  }

  /**
   * Convert {@code this} object to a {@code BigDecimal} object.
   *
   * @return a {@code BigDecimal} object if the conversion is possible.
   */
  @Override
  public BigDecimal toBigDecimal() {
    return value;
  }

  /**
   * Convert {@code this} object to a {@code BigInteger} object.
   *
   * @return a {@code BigDecBigIntegerimal} object if the conversion is possible.
   */
  @Override
  public BigInteger toBigInteger() {
    return value.toBigInteger();
  }

  /**
   * Convert {@code this} object to a {@code Byte} object.
   *
   * @return a {@code Byte} object if the conversion is possible.
   */
  @Override
  public Byte toByte() {
    return value.byteValueExact();
  }

  /**
   * Convert {@code this} object to an array of {@code byte} primitive.
   *
   * @return an array of {@code byte} primitive if the conversion is possible.
   */
  @Override
  public byte[] toByteArray() {
    byte[] byteArray = new byte[1];
    byteArray[0] = this.toByte();
    return byteArray;
  }

  /**
   * Convert {@code this} object to a {@code Calendar} object.
   *
   * @return a {@code Calendar} object if the conversion is possible.
   */
  @Override
  public Calendar toCalendar() {
    long time = this.toLong();
    Calendar calendar = Calendar.getInstance();
    calendar.setTimeInMillis(time);
    return calendar;
  }

  /**
   * Convert {@code this} object to a {@code Character} object.
   *
   * @return a {@code Character} object if the conversion is possible.
   */
  @Override
  public Character toCharacter() {
    Character character = (char) value.intValue();
    return character;
  }

  /**
   * Convert {@code this} object to a {@code Double} object.
   *
   * @return a {@code Double} object if the conversion is possible.
   */
  @Override
  public Double toDouble() {
    return value.doubleValue();
  }

  /**
   * Convert {@code this} object to a {@code Float} object.
   *
   * @return a {@code Float} object if the conversion is possible.
   */
  @Override
  public Float toFloat() {
    float f = value.floatValue();
    return f;
  }

  /**
   * Convert {@code this} object to a primitive {@code int} value.
   *
   * @return a primitive {@code int} value if the conversion is possible.
   */
  @Override
  public int toInt() {
    return value.intValueExact();
  }

  /**
   * Convert {@code this} object to a {@code Integer} object.
   *
   * @return a {@code Integer} object if the conversion is possible.
   */
  @Override
  public Integer toInteger() {
    return value.intValueExact();
  }

  /**
   * Convert {@code this} object to a string with the representation of the JSON structure in a JSON string form.
   *
   * @return a {@code String} with the JSON string representation of {@code this} object.
   */
  @Override
  public String toJSON() {
    if (value.compareTo(BigDecimal.ZERO) == 0) { // To fix Java 7 trailing bug
      return "0";
    }
    return value.stripTrailingZeros().toPlainString();
  }

  /**
   * Convert {@code this} object to a {@link net.cabezudo.json.values.JSONArray} object.
   *
   * @return a {@link net.cabezudo.json.values.JSONArray} object with only {@code this} element.
   */
  @Override
  public JSONArray toJSONArray() {
    JSONArray jsonArray = new JSONArray();
    jsonArray.add(value);
    return jsonArray;
  }

  /**
   * Convert {@code this} object to a {@link net.cabezudo.json.values.JSONString} object.
   *
   * @return a {@link net.cabezudo.json.values.JSONString} object if the conversion is possible.
   */
  @Override
  public JSONString toJSONString() {
    JSONString jsonString = new JSONString(value);
    return jsonString;
  }

  /**
   * Convert {@code this} object to a {@code List} of {@link net.cabezudo.json.values.JSONValue} objects.
   *
   * @return a {@code List} of {@link net.cabezudo.json.values.JSONValue} with {@code this} element.
   */
  @Override
  public List<JSONValue<?>> toList() {
    List<JSONValue<?>> list = new ArrayList<>();
    list.add(this);
    return list;
  }

  /**
   * Convert {@code this} object to a {@code Long} object.
   *
   * @return a {@code Long} object if the conversion is possible.
   */
  @Override
  public Long toLong() {
    return value.longValueExact();
  }

  /**
   * Convert {@code this} object to a {@code Short} object.
   *
   * @return a {@code Short} object if the conversion is possible.
   */
  @Override
  public Short toShort() {
    short s = value.shortValueExact();
    return s;
  }

  /**
   * Convert {@code this} object to a {@code String} using {@link net.cabezudo.json.values.JSONNumber#toJSON()}.
   *
   * @return a {@code Stirng}.
   */
  @Override
  public String toString() {
    return toJSON();
  }

  /**
   * Convert {@code this} object to an array of {@code String} objects.
   *
   * @return an array of {@code String} object with one element.
   */
  @Override
  public String[] toStringArray() {
    String[] s = new String[1];
    s[0] = value.toString();
    return s;
  }

  @Override
  public void toFormattedString(StringBuilder sb, int indent, boolean inclueFisrt) {
    sb.append(value.toPlainString());
  }

}
