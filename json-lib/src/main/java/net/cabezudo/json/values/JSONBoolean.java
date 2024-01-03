/**
 * MIT License
 * <p>
 * Copyright (c) 2017 Esteban Cabezudo
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, without limitation of the rights
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
import java.util.ArrayList;
import java.util.List;

/**
 * A {@link JSONBoolean} is an object extended from {@link JSONValue} object in order to represent a boolean that can be used to create JSON structures.
 *
 * @author <a href="http://cabezudo.net">Esteban Cabezudo</a>
 * @version 0.9, 10/01/2014
 */
public class JSONBoolean extends JSONValue<JSONBoolean> {

  /**
   * A {@link net.cabezudo.json.values.JSONBoolean} {@code false} object.
   */
  public static final JSONBoolean FALSE = new JSONBoolean(false, null);
  /**
   * A {@link net.cabezudo.json.values.JSONBoolean} {@code true} object.
   */
  public static final JSONBoolean TRUE = new JSONBoolean(true, null);

  private final Boolean value;

  private JSONBoolean(boolean value, Position position) {
    super(position);
    this.value = value;
  }

  /**
   * Return a {@link net.cabezudo.json.values.JSONBoolean} that corresponds with the {@code boolean} value passed.
   *
   * @param value a {@code boolean} value.
   * @return Return a {@link net.cabezudo.json.values.JSONBoolean}.
   */
  public static JSONBoolean get(boolean value) {
    if (value) {
      return TRUE;
    } else {
      return FALSE;
    }
  }

  /**
   * Compares this {@link net.cabezudo.json.values.JSONBoolean} with another.
   *
   * @param jsonBoolean the {@link net.cabezudo.json.values.JSONBoolean} to be compared.
   * @return zero if this object represents the same boolean value as the argument; a positive value if this object represents {@code true} and the argument represents
   * {@code false}; and a negative value if this object represents {@code false} and the argument represents {@code true}
   */
  @Override
  public int compareTo(JSONBoolean jsonBoolean) {
    return value.compareTo(jsonBoolean.toBoolean());
  }

  /**
   * Return the referenced element for {@code this} object. For a {@link net.cabezudo.json.values.JSONBoolean} object, {@code this} object and the referenced version is the same.
   *
   * @return {@code this} object.
   */
  @Override
  public JSONBoolean toReferencedElement() {
    return this;
  }

  /**
   * Returns whether the element is a {@link net.cabezudo.json.values.JSONBoolean} or not.
   *
   * @return {@code true}.
   */
  @Override
  public boolean isBoolean() {
    return true;
  }

  /**
   * Convert {@code this} object to a {@code BigDecimal} object.
   *
   * @return a {@code BigDecimal} object with a value of {@code 1} it {@code this} represents {@code true}; {@code 0} otherwise.
   */
  @Override
  public BigDecimal toBigDecimal() {
    int numericValue = value ? 1 : 0;
    return BigDecimal.valueOf(numericValue);
  }

  /**
   * Convert {@code this} object to a {@code BigInteger} object.
   *
   * @return a {@code BigInteger} object with a value of {@code 1} it {@code this} represents {@code true}; {@code 0} otherwise.
   */
  @Override
  public BigInteger toBigInteger() {
    int numericValue = value ? 1 : 0;
    return BigInteger.valueOf(numericValue);
  }

  /**
   * Convert {@code this} object to a {@code Boolean} object.
   *
   * @return a {@code Boolean.TRUE} object it {@code this} represents {@code true}; {@code 0} otherwise.
   */
  @Override
  public Boolean toBoolean() {
    return value;
  }

  /**
   * Convert {@code this} object to a {@code Byte} object.
   *
   * @return a {@code Byte} object with a value of {@code 1} it {@code this} represents {@code true}; {@code 0} otherwise.
   */
  @Override
  public Byte toByte() {
    return (byte) (value ? 1 : 0);
  }

  /**
   * Convert {@code this} object to a {@code Character} object.
   *
   * @return a {@code Character} object with a value of {@code '1'} it {@code this} represent {@code true}; {@code '0'} otherwise.
   */
  @Override
  public Character toCharacter() {
    return (value ? '1' : '0');
  }

  /**
   * Convert {@code this} object to a {@code Double} object.
   *
   * @return a {@code Double} object with a value of {@code 1} it {@code this} represents {@code true}; {@code 0} otherwise.
   */
  @Override
  public Double toDouble() {
    return (double) (value ? 1 : 0);
  }

  /**
   * Convert {@code this} object to a {@code Float} object.
   *
   * @return a {@code Float} object with a value of {@code 1} it {@code this} represents {@code true}; {@code 0} otherwise.
   */
  @Override
  public Float toFloat() {
    return (float) (value ? 1 : 0);
  }

  /**
   * Convert {@code this} object to a {@code Integer} object.
   *
   * @return a primitive {@code int} with a value of {@code 1} it {@code this} represents {@code true}; {@code 0} otherwise.
   */
  @Override
  public int toInt() {
    return (value ? 1 : 0);
  }

  /**
   * Convert {@code this} object to a {@code Integer} object.
   *
   * @return a {@code Integer} object with a value of {@code 1} it {@code this} represents {@code true}; {@code 0} otherwise.
   */
  @Override
  public Integer toInteger() {
    return (value ? 1 : 0);
  }

  /**
   * Convert {@code this} object to a {@code String} object.
   *
   * @return a {@code String} object with a value of {@code true} it {@code this} represents {@code true}; {@code false} otherwise.
   */
  @Override
  public String toJSON() {
    return value ? "true" : "false";
  }

  /**
   * Convert {@code this} object to a {@link JSONArray} object.
   *
   * @return a {@link JSONArray} object with only {@code this} element.
   */
  @Override
  public JSONArray toJSONArray() {
    JSONArray jsonArray = new JSONArray();
    jsonArray.add(this);
    return jsonArray;
  }

  /**
   * Convert {@code this} object to a {@link net.cabezudo.json.values.JSONString} object using {@code toString()} method.
   *
   * @return a {@link net.cabezudo.json.values.JSONString} object.
   */
  @Override
  public JSONString toJSONString() {
    return new JSONString(value.toString(), getPosition());
  }

  /**
   * Convert {@code this} object to a {@code List} of {@link JSONValue} objects.
   *
   * @return a {@code List} of {@link JSONValue} with {@code this} element.
   */
  @Override
  public List<JSONValue<?>> toList() {
    List<JSONValue<?>> list = new ArrayList<>();
    list.add(this);
    return list;
  }

  /**
   * Convert {@code this} object to a {@code Byte} object.
   *
   * @return a {@code Byte} object with a value of {@code 1} it {@code this} represents {@code true}; {@code 0} otherwise.
   */
  @Override
  public Long toLong() {
    return (long) (value ? 1 : 0);
  }

  /**
   * Convert {@code this} object to a {@code Byte} object.
   *
   * @return a {@code Byte} object with a value of {@code 1} it {@code this} represent {@code true}; {@code 0} otherwise.
   */
  @Override
  public Short toShort() {
    return (short) (value ? 1 : 0);
  }

  /**
   * Convert {@code this} object to an array of {@code String} objects.
   *
   * @return an array of {@code String} object with one element converted using {@code toString()} method.
   */
  @Override
  public String[] toStringArray() {
    String[] s = new String[1];
    s[0] = value.toString();
    return s;
  }

  @Override
  public void toFormattedString(StringBuilder sb, int indent, boolean includeFirst) {
    sb.append(value);
  }
}
