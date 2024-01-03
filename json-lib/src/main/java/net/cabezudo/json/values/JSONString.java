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
package net.cabezudo.json.values;

import net.cabezudo.json.JSON;
import net.cabezudo.json.Position;
import net.cabezudo.json.exceptions.JSONConversionException;


import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * A {@link net.cabezudo.json.values.JSONString} is an object extended from {@link JSONValue} object in order to represent a JSON string that can be used to create JSON structures.
 *
 * @author <a href="http://cabezudo.net">Esteban Cabezudo</a>
 * @version 0.9, 10/01/2014
 */
public class JSONString extends JSONValue<JSONString> {

  private final String value;

  /**
   * Initializes a newly created {@link net.cabezudo.json.values.JSONString} object so that it represents the same string as the argument.
   *
   * @param value A {@code String}
   */
  public JSONString(String value) {
    this(value, null);
  }

  /**
   * Initializes a newly created {@link net.cabezudo.json.values.JSONString} object so that it represents the same string as the argument.
   *
   * @param value    A {@code String}
   * @param position The position of the {@code String} in origen
   */
  public JSONString(String value, Position position) {
    super(position);
    // TODO validate the scape character sequence fir de string value
    this.value = value;
  }

  /**
   * Initializes a newly created {@link net.cabezudo.json.values.JSONString} object using the {@code Character} passed as argument.
   *
   * @param character A {@code Character} object.
   */
  public JSONString(Character character) {
    super(null);
    this.value = character.toString();
  }

  /**
   * Initializes a newly created {@link net.cabezudo.json.values.JSONString} object using the {@code BigInteger} object passed as argument. The object conversion is made using the
   * {@code toString()} method of the {@code BigInteger} object.
   *
   * @param bigInteger A {@code BigInteger} object.
   */
  public JSONString(BigInteger bigInteger) {
    super(null);
    this.value = bigInteger.toString();
  }

  /**
   * Initializes a newly created {@link net.cabezudo.json.values.JSONString} object using the {@code BigDecimal} object passed as argument. The object conversion is made using the
   * {@code toString()} method of the {@code BigDecimal} object.
   *
   * @param bigDecimal A {@code BigDecimal} object.
   */
  public JSONString(BigDecimal bigDecimal) {
    super(null);
    this.value = bigDecimal.toString();
  }

  public JSONString(Calendar calendar) {
    super(null);
    SimpleDateFormat sdf = new SimpleDateFormat(JSON.SIMPLE_DATE_FORMAT_PATTERN);
    this.value = sdf.format(calendar.getTimeInMillis());
  }

  /**
   * Compares the string values represented by two {@link net.cabezudo.json.values.JSONString} objects.
   *
   * @param jsonString the {@link net.cabezudo.json.values.JSONString} to be compared.
   * @return the integer value {@code 0} if the argument {@link net.cabezudo.json.values.JSONString} is equal to this {@link net.cabezudo.json.values.JSONString}; a value less than
   * {@code 0} if this {@link net.cabezudo.json.values.JSONString} is lexicographically less than the {@link net.cabezudo.json.values.JSONString} argument; and a value greater than
   * {@code 0} if this {@link net.cabezudo.json.values.JSONString} is lexicographically greater than the {@link net.cabezudo.json.values.JSONString} argument.
   */
  @Override
  public int compareTo(JSONString jsonString) {
    return value.compareTo(jsonString.value);
  }

  /**
   * Compares this string to the specified object. The result is {@code
   * true} if and only if the argument is not {@code null} and is a {@code
   * String} object that represents the same sequence of characters as this object.
   *
   * @param object The object to compare this {@code String} against
   * @return {@code true} if the given object represents a {@code String} equivalent to this string, {@code false} otherwise
   */
  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    }
    if (object == null) {
      return false;
    }
    if (getClass() != object.getClass()) {
      return false;
    }
    JSONString other = (JSONString) object;
    return Objects.equals(this.value, other.value);
  }

  /**
   * /**
   * Returns {@code this} {@link net.cabezudo.json.values.JSONString} object, a non referenced {@link net.cabezudo.json.values.JSONString} object is the same object than a
   * referenced {@link net.cabezudo.json.values.JSONString} object because is not an JSON object to be referenced.
   *
   * @return The element whose referenceable objects have been replaced with their references.
   */
  @Override
  public JSONValue toReferencedElement() {
    return this;
  }

  /**
   * Returns a hash code for this string. The hash code for a {@link net.cabezudo.json.values.JSONString} is the same hash that a {@code String} object.
   *
   * @return a hash code value for this object.
   */
  @Override
  public int hashCode() {
    return this.value.hashCode();
  }

  /**
   * Returns if the JSON element is a {@link net.cabezudo.json.values.JSONString}. This object represent a JSON string, so allways return a true.
   *
   * @return {@code true} because the object is a {@link net.cabezudo.json.values.JSONString}.
   */
  @Override
  public boolean isString() {
    return true;
  }

  /**
   * Convert the {@link net.cabezudo.json.values.JSONString} into an array of {@link JSONValue} objects. The result array is an array of only one element, the {@code this} object.
   *
   * @return an array of {@link net.cabezudo.json.values.JSONValue} with a single element.
   */
  @Override
  public JSONValue[] toArray() {
    JSONValue[] array = new JSONValue[1];
    array[0] = new JSONString(value, getPosition());
    return array;
  }

  /**
   * Try to create, using the {@code BigDecimal} constructor, a {@code BigDecimal} object using the {@code BigDecimal} rules for {@code String} and throw a
   * {@code NumberFormatException} if the {@link net.cabezudo.json.values.JSONString} don't have a valid representation for a {code BigDecimal}.
   *
   * @return a {code BigDecimal} result to translate the {@code String} represented in {@link net.cabezudo.json.values.JSONString}.
   * @throws NumberFormatException if {@code value} is not a valid representation of a {@code BigDecimal}.
   */
  @Override
  public BigDecimal toBigDecimal() {
    return new BigDecimal(value);
  }

  /**
   * Try to create, using the {@code BigInteger} constructor, a {@code BigInteger} object using the {@code BigInteger} rules for {code String} and throw a
   * {@code NumberFormatException} if the {@link net.cabezudo.json.values.JSONString} don't have a valid representation for a {code BigInteger}.
   *
   * @return a {code BigInteger} result to translate the {@code String} represented in {@link net.cabezudo.json.values.JSONString}.
   * @throws NumberFormatException if {@code value} is not a valid representation of a {@code BigInteger}.
   */
  @Override
  public BigInteger toBigInteger() {
    return new BigInteger(value);
  }

  /**
   * Returns a {@code Boolean} with the boolean value represented by the specified string. The {@code Boolean} returned represents a true value if the string represented in
   * {@code this} is not {@code null} and is equal, ignoring case, to the string {@code true}.
   *
   * @return a {code Boolean} result to convert, using {@code Boolean#valueOf()}, the {@code String} represented in {@link net.cabezudo.json.values.JSONString}.
   */
  @Override
  public Boolean toBoolean() {
    return Boolean.valueOf(value);
  }

  /**
   * Constructs an return a newly allocated {@code Byte} object that represents the value indicated by the {@code String} value in the {@link net.cabezudo.json.values.JSONString}.
   * The string is converted to a {@code Byte} value in exactly the manner used by the {@code parseByte} method for radix 10.
   *
   * @return the value of this {@link net.cabezudo.json.values.JSONString} as a {@code Byte}.
   * @throws NumberFormatException if the {@code String} does not contain a parseable {@code Byte}.
   */
  @Override
  public Byte toByte() {
    return Byte.valueOf(value);
  }

  /**
   * Convert the {@link net.cabezudo.json.values.JSONString} into an array of {@code byte} primitives. The result array is an array of only one element, the converted value of
   * {@code this} object.
   *
   * @return an array of {@link JSONValue} with a only one element.
   */
  @Override
  public byte[] toByteArray() {
    byte[] b = new byte[1];
    b[0] = toByte();
    return b;
  }

  /**
   * Constructs an return a newly allocated {@code Calendar} object that represents the value indicated by the {@code String} represented in the
   * {@link net.cabezudo.json.values.JSONString}. The string is converted to a {@code Calendar} value using the {@code SimpleDateFormat} {@code parse} method and the pattern
   * specified for {@link JSON#SIMPLE_DATE_FORMAT_PATTERN}.
   *
   * @return the value of this {@link net.cabezudo.json.values.JSONString} as a {@code Calendar}.
   * @throws JSONConversionException if the {@code String} does not contain a parseable {@code Calendar}.
   */
  @Override
  public Calendar toCalendar() {
    return toCalendar(JSON.SIMPLE_DATE_FORMAT_PATTERN);
  }

  @Override
  public Calendar toCalendar(String pattern) {
    SimpleDateFormat sdf = new SimpleDateFormat(pattern);

    Date date;
    try {
      date = sdf.parse(value);
    } catch (ParseException e) {
      throw new JSONConversionException("I can't convert a " + value + " to a Calendar.", e);
    }
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);
    return calendar;
  }

  /**
   * Convert the {@link net.cabezudo.json.values.JSONString} into a {@code Character} object using the {@code this} value. The result {@code Character} is a {@code Character}
   * object created with the first character of the value {@code String}.
   *
   * @return a {@code Character}.
   * @throws IndexOutOfBoundsException if the value {@code String} length is less than one.
   */
  @Override
  public Character toCharacter() {
    return value.charAt(0);
  }

  /**
   * Constructs an return a newly allocated {@code Double} object that represents the value indicated by the {@code String} represented in the
   * {@link net.cabezudo.json.values.JSONString}. The string is converted to a {@code Double} using the {@code Double} constructor.
   *
   * @return the value of this {@link net.cabezudo.json.values.JSONString} as a {@code Double}.
   * @throws NumberFormatException if the {@code String} value of {@code this} {@link net.cabezudo.json.values.JSONString} does not contain a parseable double number.
   */
  @Override
  public Double toDouble() {
    return Double.valueOf(value);
  }

  /**
   * Constructs an return a newly allocated {@code Float} object that represents the value indicated by the {@code String} represented in the
   * {@link net.cabezudo.json.values.JSONString}. The string is converted to a {@code Float} using the {@code Float} constructor.
   *
   * @return the value of this {@link net.cabezudo.json.values.JSONString} as a {@code Float}.
   * @throws NumberFormatException if the {@code String} value of {@code this} {@link net.cabezudo.json.values.JSONString} does not contain a parseable float number.
   */
  @Override
  public Float toFloat() {
    return Float.valueOf(value);
  }

  /**
   * Constructs an return a primitive {@code int} value that represents the value indicated by the {@code String} represented in the {@link net.cabezudo.json.values.JSONString}.
   * The string is converted to a {@code int} using the {@code Integer} constructor.
   *
   * @return the value of this {@link net.cabezudo.json.values.JSONString} as a {@code int}.
   * @throws NumberFormatException if the {@code String} value of {@code this} {@link net.cabezudo.json.values.JSONString} does not contain a parseable integer number.
   */
  @Override
  public int toInt() {
    return Integer.valueOf(value);
  }

  /**
   * Constructs an return a newly allocated {@code Integer} object that represents the value indicated by the {@code String} represented in the
   * {@link net.cabezudo.json.values.JSONString}. The string is converted to a {@code Integer} using the {@code Integer} constructor.
   *
   * @return the value of this {@link net.cabezudo.json.values.JSONString} as a {@code Integer}.
   * @throws NumberFormatException if the {@code String} value of {@code this} {@link net.cabezudo.json.values.JSONString} does not contain a parseable integer number.
   */
  @Override
  public Integer toInteger() {
    return Integer.valueOf(value);
  }

  /**
   * Return a {@code String} with the value of {@code this} {@link net.cabezudo.json.values.JSONString} object converted to a JSON valid representation. A JSON string is always a
   * string in double quotes. This method is used to create JSON strings.
   *
   * @return a {@code String} with the JSON formated {@code String}.
   */
  @Override
  public String toJSON() {
    if (value == null) {
      return null;
    }
    return "\"" + value + "\"";
  }

  /**
   * Convert the {@link net.cabezudo.json.values.JSONString} into a {@link net.cabezudo.json.values.JSONArray} object. The result is a {@link net.cabezudo.json.values.JSONArray}
   * with only one {@link net.cabezudo.json.values.JSONString} created with the value of {@code this} {@link net.cabezudo.json.values.JSONString} object.
   *
   * @return a {@link net.cabezudo.json.values.JSONArray} with only one element.
   */
  @Override
  public JSONArray toJSONArray() {
    return new JSONArray(value);
  }

  /**
   * Constructs an return a newly allocated {@link net.cabezudo.json.values.JSONString} object that represents the value indicated by the {@code String} value of {@code this}
   * {@link net.cabezudo.json.values.JSONString}. The string is created using the {@link net.cabezudo.json.values.JSONString} constructor.
   *
   * @return a {@link net.cabezudo.json.values.JSONString}.
   */
  @Override
  public JSONString toJSONString() {
    return new JSONString(value, getPosition());
  }

  /**
   * Convert the {@link net.cabezudo.json.values.JSONString} into an {@code ArrayList} of {@link net.cabezudo.json.values.JSONValue} objects. The result list has only one element,
   * the {@code this} object.
   *
   * @return an {@code ArrayList} of {@link net.cabezudo.json.values.JSONValue} objects.
   */
  @Override
  public List<JSONValue<?>> toList() {
    List<JSONValue<?>> list = new ArrayList<>();
    list.add(this);
    return list;
  }

  /**
   * Constructs an return a newly allocated {@code Long} object that represents the value indicated by the {@code String} represented in the
   * {@link net.cabezudo.json.values.JSONString}. The string is converted to a {@code Long} using the {@code Long} constructor.
   *
   * @return the value of this {@link net.cabezudo.json.values.JSONString} as a {@code Long}.
   * @throws NumberFormatException if the {@code String} value of {@code this} {@link net.cabezudo.json.values.JSONString} does not contain a parseable long number.
   */
  @Override
  public Long toLong() {
    return Long.valueOf(value);
  }

  /**
   * Constructs an return a newly allocated {@code Short} object that represents the value indicated by the {@code String} represented in the
   * {@link net.cabezudo.json.values.JSONString}. The string is converted to a {@code Short} using the {@code Short} constructor.
   *
   * @return the value of this {@link net.cabezudo.json.values.JSONString} as a {@code Short}.
   * @throws NumberFormatException if the {@code String} value of {@code this} {@link net.cabezudo.json.values.JSONString} does not contain a parseable short number.
   */
  @Override
  public Short toShort() {
    return Short.valueOf(value);
  }

  /**
   * Returns a string representation of the {@code this} object. The string representation for the object is the {@code String} represented for the
   * {@link net.cabezudo.json.values.JSONString} object and is the same object that the internal {@code String} value of the object.
   *
   * @return a {@code String} representation of the value of {@code this} object.
   */
  @Override
  public String toString() {
    return replaceEscapeCharacters(value);
  }

  private String replaceEscapeCharacters(String value) {
    StringBuilder sb = new StringBuilder(value.length());
    char[] chars = value.toCharArray();
    int i = 0;
    while (i < chars.length) {
      char c = chars[i];
      if (c == '\\') {
        i++;
        switch (chars[i]) {
          case '\"':
            sb.append('"');
            break;
          case '\\':
            sb.append('\\');
            break;
          case '/':
            sb.append('/');
            break;
          case 'b':
            sb.append('\b');
            break;
          case 'f':
            sb.append('\f');
            break;
          case 'n':
            sb.append('\n');
            break;
          case 'r':
            sb.append('\r');
            break;
          case 't':
            sb.append('\t');
            break;
          case 'u':
            sb.append(getHex(chars, i));
            break;
          default:
            throw new InvalidStringException("Invalid char sequence");
        }
      } else {
        sb.append(c);
      }
      i++;
    }
    return sb.toString();
  }

  private char getHex(char[] chars, int i) {
    if (i + 4 > chars.length) {
      throw new InvalidStringException("Invalid char sequence");
    }
    if (!isHexDigit(chars[i + 1]) || !isHexDigit(chars[i + 2]) || !isHexDigit(chars[i + 3]) || !isHexDigit(chars[i + 4])) {
      throw new InvalidStringException("Invalid char sequence");
    }
    String hex = Character.toString(chars[i + 1]) + chars[i + 2] + chars[i + 3] + chars[i + 4];
    return (char) Integer.parseInt(hex, 16);
  }

  private boolean isHexDigit(char value) {
    char c = Character.toLowerCase(value);
    return Character.isDigit(c) || c == 'a' || c == 'b' || c == 'c' || c == 'd' || c == 'e' || c == 'f';
  }

  /**
   * Convert the {@link net.cabezudo.json.values.JSONString} into an array of {@code String} objects. The result array is an array with only one element, the {@code this} object.
   *
   * @return an array of {@code String} with a single element.
   */
  @Override
  public String[] toStringArray() {
    String[] array = new String[1];
    array[0] = value;
    return array;
  }

  @Override
  public void toFormattedString(StringBuilder sb, int indent, boolean includeFirst) {
    sb.append("\"").append(value).append("\"");
  }
}
