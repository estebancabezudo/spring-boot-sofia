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
import java.util.ArrayList;
import java.util.List;

/**
 * A {@link JSONNull} is an object extended from {@link JSONValue} object in order to represent a null that can be used to create JSON structures.
 *
 * @author <a href="http://cabezudo.net">Esteban Cabezudo</a>
 * @version 0.9, 10/01/2014
 */
public class JSONNull extends JSONValue<JSONNull> {

  public static final String NULL = "null";

  /**
   * Construct a {@link net.cabezudo.json.values.JSONNull}. The method also takes a {@link Position} in order to save the position in origin and give it when a parse error is
   * thrown.
   *
   * @param position the position of the value in the source.
   */
  public JSONNull(Position position) {
    super(position);
  }

  /**
   * Construct a {@link net.cabezudo.json.values.JSONNull}.
   */
  public JSONNull() {
    this(null);
  }

  /**
   * Compares two {@link net.cabezudo.json.values.JSONNull} objects.
   *
   * @param o the {@code Object} to be compared.
   * @return {@code true} if {@code this} {@link net.cabezudo.json.values.JSONNull} is equal to the argument {@code false} otherwise.
   */
  @Override
  public boolean equals(Object o) {
    return o instanceof JSONNull;
  }

  /**
   * Return the hash code for this {@link net.cabezudo.json.values.JSONNull}.
   *
   * @return hash code for this {@link net.cabezudo.json.values.JSONNull}.
   */
  @Override
  public int hashCode() {
    return NULL.hashCode();
  }

  /**
   * Compares two {@link net.cabezudo.json.values.JSONNull} objects.
   *
   * @param jsonNull the {@link net.cabezudo.json.values.JSONNull} to be compared.
   *
   * @return the value {@code 0}.
   */
  @Override
  public int compareTo(JSONNull jsonNull) {
    return 0;
  }

  /**
   * Convert {@code this} object to a {@code BigDecimal}.
   *
   * @return a {@code null}.
   */
  @Override
  public BigDecimal toBigDecimal() {
    return null;
  }

  /**
   * Return the referenced element for {@code this} object. For a {@link net.cabezudo.json.values.JSONNull} object, {@code this} object and the referenced version is the same.
   *
   * @return {@code this} object.
   */
  @Override
  public JSONNull toReferencedElement() {
    return this;
  }

  /**
   * Returns whether the element is a {@link net.cabezudo.json.values.JSONNull} or not.
   *
   * @return {@code true} if the element is a {@link net.cabezudo.json.values.JSONNull}; {@code false} otherwise.
   */
  @Override
  public boolean isNull() {
    return true;
  }

  /**
   * Convert {@code this} object to a string with the representation of the JSON structure in a JSON string form.
   *
   * @return a {@code String} with the JSON string representation of {@code this} object.
   */
  @Override
  public String toJSON() {
    return NULL;
  }

  /**
   * Convert {@code this} object to a {@link JSONArray} object. The result {@link JSONArray} only has {@code this} element.
   *
   * @return a {@link JSONArray} object.
   */
  @Override
  public JSONArray toJSONArray() {
    JSONArray jsonArray = new JSONArray();
    jsonArray.add(this);
    return jsonArray;
  }

  /**
   * Convert {@code this} object to a {@link net.cabezudo.json.values.JSONString} object.
   *
   * @return a {@link net.cabezudo.json.values.JSONString}.
   */
  @Override
  public JSONString toJSONString() {
    return new JSONString(NULL, getPosition());
  }

  /**
   * Convert {@code this} object to a {@code List} of {@link JSONValue} objects.
   *
   * @return a {@code List} of {@link JSONValue} with only {@code this} element.
   */
  @Override
  public List<JSONValue> toList() {
    List<JSONValue> list = new ArrayList<>();
    list.add(this);
    return list;
  }

  /**
   * Convert {@code this} object to a {@code Integer}.
   *
   * @return a {@code null}.
   */
  @Override
  public Integer toInteger() {
    return null;
  }

  /**
   * Convert {@code this} object to a {@code Long}.
   *
   * @return a {@code null}.
   */
  @Override
  public Long toLong() {
    return null;
  }

  /**
   * Convert {@code this} object to a {@code String}.
   *
   * @return a {@code null}.
   */
  @Override
  public String toString() {
    return NULL;
  }

  /**
   * Convert {@code this} object to an array of {@code String} objects.
   *
   * @return an array of {@code String} with only one element.
   */
  @Override
  public String[] toStringArray() {
    String[] s = new String[1];
    s[0] = NULL;
    return s;
  }

  @Override
  public void toFormatedString(StringBuilder sb, int indent, boolean includeFirst) {
    sb.append(NULL);
  }
}
