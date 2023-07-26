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

import net.cabezudo.json.JSONElement;
import net.cabezudo.json.Position;
import net.cabezudo.json.exceptions.JSONConversionException;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.List;

/**
 * The {@link net.cabezudo.json.values.JSONValue} class is an abstract class to implement a JSON value. A JSON value is a JSON structure component inherited from
 * {@link net.cabezudo.json.JSONElement} that can be used in a pair or in an array. There are seven types for values: string, number, object, array, true, false, and null.
 * <p>
 * The class also provides additional default methods for implementing a concrete object value and the default type conversions.
 *
 * @param <T> the type of elements in this list
 * @author <a href="http://cabezudo.net">Esteban Cabezudo</a>
 * @version 0.9, 10/01/2014
 */
public abstract class JSONValue<T> extends JSONElement implements Comparable<T> {

  public JSONValue(Position position) {
    super(position);
  }

  /**
   * Returns if the JSON element is a value. This object represent a JSON value, so always return a {@code true}.
   *
   * @return {@code true} because the object is a JSON value.
   */
  @Override
  public boolean isValue() {
    return true;
  }

  /**
   * This method implements the default behavior to convert a value to an array of {@link net.cabezudo.json.values.JSONValue}. The default behavior is to throw a
   * {@link net.cabezudo.json.exceptions.JSONConversionException} exception. Each class that implements a value may have a different behavior so it could override this method.
   *
   * @return an array of {@link net.cabezudo.json.values.JSONValue} object if the conversion is possible.
   */
  public JSONValue<?>[] toArray() {
    throw new JSONConversionException("I can't convert a " + this.getClass().getName() + " to a JSONValue array.");
  }

  /**
   * This method implements the default behavior to convert a value to a {@code BigDecimal} object. The default behavior is to throw a
   * {@link net.cabezudo.json.exceptions.JSONConversionException} Each class that implements a value may have a different behavior so it could override this method.
   *
   * @return a {@code BigDecimal} object if the conversion is possible.
   */
  public BigDecimal toBigDecimal() {
    throw new JSONConversionException("I can't convert a " + this.getClass().getName() + " to a BigDecimal");
  }

  /**
   * This method implements the default behavior to convert a value to a {@code BigInteger} object. The default behavior is to throw a
   * {@link net.cabezudo.json.exceptions.JSONConversionException} Each class that implements a value may have a different behavior so it could override this method.
   *
   * @return a {@code BigInteger} object if the conversion is possible.
   */
  public BigInteger toBigInteger() {
    throw new JSONConversionException("I can't convert a " + this.getClass().getName() + " to a BigInteger");
  }

  /**
   * This method implements the default behavior to convert a value to a {@code Boolean} object. The default behavior is to throw a
   * {@link net.cabezudo.json.exceptions.JSONConversionException} Each class that implements a value may have a different behavior so it could override this method.
   *
   * @return a {@code Boolean} object if the conversion is possible.
   */
  public Boolean toBoolean() {
    throw new JSONConversionException("I can't convert a " + this.getClass().getName() + " to a Boolean");
  }

  /**
   * This method implements the default behavior to convert a value to a {@code Byte} object. The default behavior is to throw a
   * {@link net.cabezudo.json.exceptions.JSONConversionException} Each class that implements a value may have a different behavior so it could override this method.
   *
   * @return a {@code Byte} object if the conversion is possible.
   */
  public Byte toByte() {
    throw new JSONConversionException("I can't convert a " + this.getClass().getName() + " to a Byte");
  }

  /**
   * This method implements the default behavior to convert a value to an array of {@code byte}
   * <p>
   * object. The default behavior is to throw a {@link net.cabezudo.json.exceptions.JSONConversionException} Each class that implements a value may have a different behavior so it
   * could override this method.
   *
   * @return an array of {@code byte} values if the conversion is possible.
   */
  public byte[] toByteArray() {
    throw new JSONConversionException("I can't convert a " + this.getClass().getName() + " to a byte array");
  }

  /**
   * This method implements the default behavior to convert a value to an array of {@code double}
   * <p>
   * object. The default behavior is to throw a {@link net.cabezudo.json.exceptions.JSONConversionException} Each class that implements a value may have a different behavior so it
   * could override this method.
   *
   * @return an array of {@code double} values if the conversion is possible.
   */
  public double[] toDoubleArray() {
    throw new JSONConversionException("I can't convert a " + this.getClass().getName() + " to a double array");
  }

  /**
   * This method implements the default behavior to convert a value to an array of {@code int}
   * <p>
   * object. The default behavior is to throw a {@link net.cabezudo.json.exceptions.JSONConversionException} Each class that implements a value may have a different behavior so it
   * could override this method.
   *
   * @return an array of {@code int} values if the conversion is possible.
   */
  public int[] toIntArray() {
    throw new JSONConversionException("I can't convert a " + this.getClass().getName() + " to a int array");
  }

  /**
   * This method implements the default behavior to convert a value to a {@code Calendar} object. The default behavior is to throw a
   * {@link net.cabezudo.json.exceptions.JSONConversionException} Each class that implements a value may have a different behavior so it could override this method.
   *
   * @return a {@code Calendar} object if the conversion is possible.
   */
  public Calendar toCalendar() {
    throw new JSONConversionException("I can't convert a " + this.getClass().getName() + " to a Calendar");
  }

  /**
   * This method implements the default behavior to convert a value to a {@code Calendar} object. The default behavior is to throw a
   * {@link net.cabezudo.json.exceptions.JSONConversionException} Each class that implements a value may have a different behavior so it could override this method.
   *
   * @param pattern the patter to use for the conversion
   * @return a {@code Calendar} object if the conversion is possible.
   */
  public Calendar toCalendar(String pattern) {
    throw new JSONConversionException("I can't convert a " + this.getClass().getName() + " to a Calendar");
  }

  /**
   * This method implements the default behavior to convert a value to a {@code Character} object. The default behavior is to throw a
   * {@link net.cabezudo.json.exceptions.JSONConversionException} Each class that implements a value may have a different behavior so it could override this method.
   *
   * @return a {@code Character} object if the conversion is possible.
   */
  public Character toCharacter() {
    throw new JSONConversionException("I can't convert a " + this.getClass().getName() + " to a Character");
  }

  /**
   * This method implements the default behavior to convert a value to a {@code Double} object. The default behavior is to throw a
   * {@link net.cabezudo.json.exceptions.JSONConversionException} Each class that implements a value may have a different behavior so it could override this method.
   *
   * @return a {@code Double} object if the conversion is possible.
   */
  public Double toDouble() {
    throw new JSONConversionException("I can't convert a " + this.getClass().getName() + " to a Double");
  }

  /**
   * This method implements the default behavior to convert a value to a {@code Float} object. The default behavior is to throw a
   * {@link net.cabezudo.json.exceptions.JSONConversionException} Each class that implements a value may have a different behavior so it could override this method.
   *
   * @return a {@code Float} object if the conversion is possible.
   */
  public Float toFloat() {
    throw new JSONConversionException("I can't convert a " + this.getClass().getName() + " to a Float");
  }

  /**
   * This method implements the default behavior to convert a value to a primitive {@code int} value. The default behavior is to throw a
   * {@link net.cabezudo.json.exceptions.JSONConversionException} Each class that implements a value may have a different behavior so it could override this method.
   *
   * @return a primitive {@code int} value if the conversion is possible.
   */
  public int toInt() {
    throw new JSONConversionException("I can't convert a " + this.getClass().getName() + " to a primitve int");
  }

  /**
   * This method implements the default behavior to convert a value to a {@code Integer} object. The default behavior is to throw a
   * {@link net.cabezudo.json.exceptions.JSONConversionException} Each class that implements a value may have a different behavior so it could override this method.
   *
   * @return a {@code Integer} object if the conversion is possible.
   */
  public Integer toInteger() {
    throw new JSONConversionException("I can't convert a " + this.getClass().getName() + " to an Integer");
  }

  /**
   * This method implements the default behavior to convert a value to a {@link net.cabezudo.json.values.JSONArray} object. The default behavior is to throw a
   * {@link net.cabezudo.json.exceptions.JSONConversionException} Each class that implements a value may have a different behavior so it could override this method.
   *
   * @return a {@link net.cabezudo.json.values.JSONArray} object if the conversion is possible.
   */
  public abstract JSONArray toJSONArray();

  /**
   * This method implements the default behavior to convert a value to a {@link net.cabezudo.json.values.JSONString} object. The default behavior is to throw a
   * {@link JSONConversionException} Each object that implements a value may have a different behavior so it could override this method.
   *
   * @return a {@link net.cabezudo.json.values.JSONString} object if the conversion is possible.
   */
  public JSONString toJSONString() {
    throw new JSONConversionException("I can't convert a " + this.getClass().getName() + " to a JSONString");
  }

  /**
   * This method return the JSON structure of this object. A JSON structure is created with JSON elements and have a root element with child elements and this child elements can
   * have another child element, creating a tree structure with several leafs. If you convert this object to a JSON structure, the root element is {@code this} object.
   *
   * @return a {@link net.cabezudo.json.values.JSONValue} object with the JSON structure with the representation of {@code this} object if the conversion is possible.
   */
  @Override
  public JSONValue<?> toJSONTree() {
    return this;
  }

  /**
   * This method implements the default behavior to convert a value to a {@code List} of {@link net.cabezudo.json.values.JSONValue} objects. The default behavior is to throw a
   * {@link JSONConversionException} Each object that implements a value may have a different behavior so it could override this method.
   *
   * @return a {@code List} of {@link net.cabezudo.json.values.JSONValue} objects if the conversion is possible.
   */
  public List<JSONValue<?>> toList() {
    throw new JSONConversionException("I can't convert a " + this.getClass().getName() + " to a List");
  }

  /**
   * This method implements the default behavior to convert a value to a {@code Long} object. The default behavior is to throw a
   * {@link net.cabezudo.json.exceptions.JSONConversionException} Each class that implements a value may have a different behavior so it could override this method.
   *
   * @return a {@code Long} object if the conversion is possible.
   */
  public Long toLong() {
    throw new JSONConversionException("I can't convert a " + this.getClass().getName() + " to a Long");
  }

  /**
   * This method implements the default behavior to convert a value to a {@link JSONObject} object. The default behavior is to throw a
   * {@link net.cabezudo.json.exceptions.JSONConversionException} Each class that implements a value may have a different behavior so it could override this method.
   *
   * @return a {@link net.cabezudo.json.values.JSONObject} object if the conversion is possible.
   */
  public JSONObject toJSONObject() {
    throw new JSONConversionException("I can't convert a " + this.getClass().getName() + " to a JSONObject");
  }

  /**
   * This method implements the default behavior to convert a value to a {@code Short} object. The default behavior is to throw a
   * {@link net.cabezudo.json.exceptions.JSONConversionException} Each class that implements a value may have a different behavior so it could override this method.
   *
   * @return a {@code Short} object if the conversion is possible.
   */
  public Short toShort() {
    throw new JSONConversionException("I can't convert a " + this.getClass().getName() + " to a Short");
  }

  /**
   * This method implements the default behavior to convert a value to an array of {@code String} object. The default behavior is to throw a
   * {@link net.cabezudo.json.exceptions.JSONConversionException} Each class that implements a value may have a different behavior so it could override this method.
   *
   * @return an array of {@code String} object if the conversion is possible.
   */
  public String[] toStringArray() {
    throw new JSONConversionException("I can't convert a " + this.getClass().getName() + " to a String array.");
  }
}
