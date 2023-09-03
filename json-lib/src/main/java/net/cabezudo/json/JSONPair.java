/**
 * MIT License
 * <p>
 * Copyright (c) 2017 Esteban Cabezudo
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
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
package net.cabezudo.json;

import net.cabezudo.json.values.JSONArray;
import net.cabezudo.json.values.JSONNull;
import net.cabezudo.json.values.JSONObject;
import net.cabezudo.json.values.JSONString;
import net.cabezudo.json.values.JSONValue;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

/**
 * A JSON object is build with only one element. A JSON pair. A JSON pair has a key and a value. The key is the property name for an element in a JSON object and the value can be
 * any type of value. A value only can be an object inherited of JSONValue. This class represent the element that contain a JSON object.
 *
 * @author <a href="http://cabezudo.net">Esteban Cabezudo</a>
 * @version 0.9, 10/01/2014
 */
public class JSONPair extends JSONElement implements Comparable<JSONPair> {

  private final String key;
  private final JSONValue<?> value;

  /**
   * @param key      the name for the key pair part
   * @param object   the object that represents the value. The constructor converts, if possible, the object to a {@link net.cabezudo.json.values.JSONValue} using
   *                 {@link net.cabezudo.json.JSON#toJSONTree(java.lang.Object) toJSONTree} method.
   * @param position the position of the pair in the JSON source code. Using for locate the new created pair in the source file. It can be null but is encouraged to use the
   *                 constructor without the parameter instead,
   */
  public JSONPair(String key, Object object, Position position) {
    super(position);
    if (key == null) {
      throw new IllegalArgumentException("The parameter key is null.");
    }
    this.key = key;
    this.value = JSON.toJSONTree(object);
  }

  /**
   * @param key    the name for the key pair part
   * @param object the object to use like value. The constructor convert, if possible, the object to a {@link net.cabezudo.json.values.JSONValue} using
   *               {@link JSON#toJSONTree(java.lang.Object)} method.
   */
  public JSONPair(String key, Object object) {
    this(key, object, null);
  }

  /**
   * Compare two {@link net.cabezudo.json.JSONPair} objects. Two {@link net.cabezudo.json.JSONPair} are the same if the key is the same and the value is the same. For the
   * comparison the key is compared first. If the key in both objects is the same the value is used to return the result of the comparison. Else, the result is the comparison
   * between keys.
   *
   * @param jsonPair the {@link net.cabezudo.json.JSONPair} to be compared.
   * @return the value {@code 0} if the argument string is equal to this string; a value less than {@code 0} if this {@link net.cabezudo.json.JSONPair} is less than the string
   * argument; and a value greater than {@code 0} if this {@link net.cabezudo.json.JSONPair} is greater than the {@link net.cabezudo.json.JSONPair} argument.
   */
  @Override
  public int compareTo(JSONPair jsonPair) {
    int c = this.getKey().compareTo(jsonPair.getKey());
    return c == 0 ? this.getValue().compareTo(jsonPair.getValue()) : c;
  }

  /**
   * Compares this {code JSONPair] to the object passed in the parameter. The result is {@code
   * true} if and only if the argument is not {@code null} and is a {@link net.cabezudo.json.JSONPair} object has equals key and value.
   *
   * @param o The object to compare this {@link net.cabezudo.json.JSONPair} against
   * @return {@code true} if the given object represents a {@link net.cabezudo.json.JSONPair} has an equal key and an equal value, {@code false} otherwise.
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
    final JSONPair other = (JSONPair) o;

    if (!Objects.equals(this.getKey(), other.getKey())) {
      return false;
    }
    return Objects.equals(this.getValue(), other.getValue());
  }

  /**
   * @return the key part of the {@link net.cabezudo.json.JSONPair}
   */
  public String getKey() {
    return key;
  }

  /**
   * Create a new {@link net.cabezudo.json.JSONPair} and replace the object for the value of the reference field. The key is the same and the value, if is a object, and have a
   * reference field, for the value of the reference field of the object.
   *
   * @return the referenced {@link net.cabezudo.json.JSONPair} for {@code this} object.
   */
  @Override
  public JSONPair toReferencedElement() {
    JSONElement jsonReferencedElement = value.toReferencedElement();
    return new JSONPair(key, jsonReferencedElement, getPosition());
  }

  /**
   * Return a {@link net.cabezudo.json.values.JSONValue} object, value part of this {@link net.cabezudo.json.JSONPair}
   *
   * @return the value part of this {@link net.cabezudo.json.JSONPair}.
   */
  public JSONValue getValue() {
    return value;
  }

  /**
   * Returns a hash code for this {@link net.cabezudo.json.JSONPair}. The hash code is computed using the hash code of the key and the hash code of the value.
   *
   * @return a hash code value for {@code this} object.
   */
  @Override
  public int hashCode() {
    int hash = 3;
    hash = 97 * hash + Objects.hashCode(this.getKey());
    hash = 97 * hash + Objects.hashCode(this.getValue());
    return hash;
  }

  /**
   * Convert the value of this {@link net.cabezudo.json.JSONPair} into a {@code BigDecimal}. If the value can't be converted the method throws a runtime exception
   * {@link net.cabezudo.json.exceptions.JSONConversionException}. The rules for conversion depends of the type of value.
   *
   * @return a {@code BigDecimal} with the conversion of value.
   */
  public BigDecimal toBigDecimal() {
    return value.toBigDecimal();
  }

  /**
   * Convert the value of this {@link net.cabezudo.json.JSONPair} into a {@code Boolean}. If the value can't be converted the method throws a runtime exception
   * {@link net.cabezudo.json.exceptions.JSONConversionException}. The rules for conversion depends of the type of value.
   *
   * @return a {@code Boolean} with the conversion of value.
   */
  public Boolean toBoolean() {
    return value.toBoolean();
  }

  /**
   * Convert the value of this {@link net.cabezudo.json.JSONPair} into a {@code Byte}. If the value can't be converted the method throws a runtime exception
   * {@link net.cabezudo.json.exceptions.JSONConversionException}. The rules for conversion depends of the type of value.
   *
   * @return a {@code Byte} with the conversion of value.
   */
  public Byte toByte() {
    return value.toByte();
  }

  /**
   * Convert the value of this {@link net.cabezudo.json.JSONPair} into a {@code Character}. If the value can't be converted the method throws a runtime exception
   * {@link net.cabezudo.json.exceptions.JSONConversionException}. The rules for conversion depend on the type of value.
   *
   * @return a {@code Character} with the conversion of value.
   */
  public Character toCharacter() {
    return value.toCharacter();
  }

  /**
   * Convert the value of this {@link net.cabezudo.json.JSONPair} into a {@code Double}. If the value can't be converted the method throws a runtime exception
   * {@link net.cabezudo.json.exceptions.JSONConversionException}. The rules for conversion depend on the type of value.
   *
   * @return a {@code Double} with the conversion of value.
   */
  public Double toDouble() {
    return value.toDouble();
  }

  /**
   * Convert the value of this {@link net.cabezudo.json.JSONPair} to a {@code Float}. If the value can't be converted the method throws a runtime exception
   * {@link net.cabezudo.json.exceptions.JSONConversionException}. The rules for conversion depend on the type of value.
   *
   * @return a {@code Float} with the conversion of value.
   */
  public Float toFloat() {
    return value.toFloat();
  }

  /**
   * Convert the value of this {@link net.cabezudo.json.JSONPair} to a {@code Integer}. If the value can't be converted the method throws a runtime exception
   * {@link net.cabezudo.json.exceptions.JSONConversionException}. The rules for conversion depend on the type of value.
   *
   * @return a {@code Integer} with the conversion of value.
   */
  public Integer toInteger() {
    return value.toInteger();
  }

  /**
   * Create a JSON string representation of this {@link net.cabezudo.json.JSONPair} including the JSON string representation of the value. The JSON string is the key in double
   * quotation marks, a colon and the JSON string value using the {@link JSONValue#toJSON() }
   * method. If the value is {@code null} the method return {@link net.cabezudo.json.values.JSONNull}.
   *
   * @return a {@code String} representation of this {@link net.cabezudo.json.JSONPair}.
   */
  @Override
  public String toJSON() {
    JSONValue<?> jsonValue;
    jsonValue = Objects.requireNonNullElseGet(value, JSONNull::new);
    return "\"" + key + "\": " + jsonValue.toJSON();
  }

  /**
   * Convert the value of this {@link net.cabezudo.json.JSONPair} into a {@link net.cabezudo.json.values.JSONArray}. If the value can't be converted the method throws a runtime
   * exception {@link net.cabezudo.json.exceptions.JSONConversionException}. The rules for conversion depend on the type of value.
   *
   * @return a {@link net.cabezudo.json.values.JSONArray} with the conversion of value.
   */
  public JSONArray toJSONArray() {
    return value.toJSONArray();
  }

  /**
   * Convert the value of this {@link net.cabezudo.json.JSONPair} into a {@link net.cabezudo.json.values.JSONString}. If the value can't be converted the method throws a runtime
   * exception {@link net.cabezudo.json.exceptions.JSONConversionException}. The rules for conversion depend on the type of value.
   *
   * @return a {@link net.cabezudo.json.values.JSONString} with the conversion of value.
   */
  public JSONString toJSONString() {
    return value.toJSONString();
  }

  /**
   * Return this {@link net.cabezudo.json.JSONPair}.
   *
   * @return a {@link net.cabezudo.json.JSONPair} with this object.
   */
  @Override
  public JSONValue<?> toJSONTree() {
    return value.toJSONTree();
  }

  /**
   * Convert the value of this {@link net.cabezudo.json.JSONPair} into a {@link java.util.List}. If the value can't be converted the method throws a runtime exception
   * {@link net.cabezudo.json.exceptions.JSONConversionException}. The rules for conversion depend on the type of value.
   *
   * @return a {@link java.util.List } with the conversion of value.
   */
  public List<JSONValue<?>> toList() {
    return value.toList();
  }

  /**
   * Convert the value of this {@link net.cabezudo.json.JSONPair} into a {@link java.lang.Long}. If the value can't be converted the method throws a runtime exception
   * {@link net.cabezudo.json.exceptions.JSONConversionException}. The rules for conversion depend on the type of value.
   *
   * @return a {@link java.lang.Long} with the conversion of value.
   */
  public Long toLong() {
    return value.toLong();
  }

  /**
   * Convert the value of this {@link net.cabezudo.json.JSONPair} into a {@link net.cabezudo.json.values.JSONObject}. If the value can't be converted the method throws a runtime
   * exception {@link net.cabezudo.json.exceptions.JSONConversionException}. The rules for conversion depend on the type of value.
   *
   * @return a {@link JSONObject} with the conversion of value.
   */
  public JSONObject toObject() {
    return value.toJSONObject();
  }

  /**
   * Convert the value of this {@link net.cabezudo.json.JSONPair} into a {@link java.lang.Short }. If the value can't be converted the method throws a runtime exception
   * {@link net.cabezudo.json.exceptions.JSONConversionException}. The rules for conversion depend on the type of value.
   *
   * @return a {@link java.lang.Short} with the conversion of value.
   */
  public Short toShort() {
    return value.toShort();
  }

  /**
   * Return a string representation of this {@link net.cabezudo.json.JSONPair}. The string is formed using a open parenthesis, the key, a comma, the string representation of the
   * value and a close parenthesis.
   *
   * @return a {@code String} representation of this {@link net.cabezudo.json.JSONPair}
   */
  @Override
  public String toString() {
    return "(" + key + ", " + value + ")";
  }

  /**
   * Convert the value of this {@link net.cabezudo.json.JSONPair} to an native array of strings and return it. If the value can't be converted the method throw a runtime exception
   * {@link net.cabezudo.json.exceptions.JSONConversionException}. The rules for conversion depend on the type of value.
   *
   * @return a {@code String[]} with the conversion of value.
   */
  public String[] toStringArray() {
    return value.toStringArray();
  }

  @Override
  public void toFormattedString(StringBuilder sb, int indent, boolean includeFirst) {
    sb.append("\"").append(key).append("\": ");
    value.toFormattedString(sb, indent, false);
  }
}
