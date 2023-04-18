/**
 * MIT License
 *
 * Copyright (c) 2017 Esteban Cabezudo
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package net.cabezudo.json;

import net.cabezudo.json.exceptions.InvalidOperationException;

/**
 * The {code JSONElement} class is an abstract class to implement every element in a JSON structure.
 *
 * <p>
 * The class also provides additional default methods for implementing a specific element type and the default methods implementation.
 *
 * @author <a href="http://cabezudo.net">Esteban Cabezudo</a>
 * @version 0.9, 10/03/2014
 */
public abstract class JSONElement implements JSONable {

  private String referenceFieldName = "id";
  private final Position position;

  public Position getPosition() {
    return position;
  }

  public JSONElement(Position position) {
    this.position = position;
  }

  /**
   * Returns the name of the field whose value is used as a reference.
   *
   * @return a {@code String} with the name of the field whose value is used as a reference.
   */
  public String getReferenceFieldName() {
    return referenceFieldName;
  }

  /**
   * Defines the name of the field used to reference the object.
   *
   * @param referenceFieldName a {@code String} with the name of the field to be used to refer to the object.
   */
  public void setReferenceFieldName(String referenceFieldName) {
    this.referenceFieldName = referenceFieldName;
  }

  /**
   * This forces the implementation of a method method that returns the result of replace the objects that can be referenced by their references.
   *
   * @return The element whose referable objects have been replaced with their references.
   */
  public abstract JSONElement toReferencedElement();

  /**
   * Returns whether the element has child elements or not.
   *
   * @return {@code true} if the element has elements, {@code false} otherwise.
   */
  public Boolean hasElements() {
    return false;
  }

  /**
   * Returns whether the element is an array or not.
   *
   * @return {@code true} if the element is an array, {@code false} otherwise.
   */
  public boolean isArray() {
    return false;
  }

  /**
   * Returns whether the element is a boolean or not.
   *
   * @return {@code true} if the element is a boolean, {@code false} otherwise.
   */
  public boolean isBoolean() {
    return false;
  }

  /**
   * Returns whether the element is empty or not.
   *
   * @throws InvalidOperationException if the element can't be empty or not empty.
   * @return {@code true} if the element is empty, {@code false} otherwise.
   */
  public boolean isEmpty() {
    throw new InvalidOperationException("A " + this.getClass().getName() + " can't be empty or not empty");
  }

  /**
   * Returns whether the element is referenceable or not. A element is referenceable if you can convert it into a reference.
   *
   * @return {@code true} if the element is not referenceable, {@code false} otherwise.
   */
  public boolean isNotReferenceable() {
    return !isReferenceable();
  }

  /**
   * Returns whether the element is the null JSON object.
   *
   * @return {@code true} if the element is a null JSON object, {@code false} otherwise.
   */
  public boolean isNull() {
    return false;
  }

  /**
   * Returns whether the element is a JSON number object or not.
   *
   * @return {@code true} if the element is a JSON null object, {@code false} otherwise.
   */
  public boolean isNumber() {
    return false;
  }

  /**
   * Returns whether the element is a JSON object or not.
   *
   * @return {@code true} if the element is a JSON object, {@code false} otherwise.
   */
  public boolean isObject() {
    return false;
  }

  /**
   * Returns whether the element is referenceable or not.
   *
   * @return {@code true} if the element is referenceable, {@code false} otherwise.
   */
  public boolean isReferenceable() {
    return false;
  }

  /**
   * Returns whether the element is a JSON string or not.
   *
   * @return {@code true} if the element is a JSON string, {@code false} otherwise.
   */
  public boolean isString() {
    return false;
  }

  /**
   * Returns whether the object is a JSON value object.
   *
   * @return {@code true} if the object is a JSON value, {@code false} otherwise.
   */
  public boolean isValue() {
    return false;
  }

  /**
   * Returns a string representation in JSON format of the object.
   *
   * @return a {@code String} representation of the JSON object in JSON format.
   */
  @Override
  public String toString() {
    return toJSON();
  }

  /**
   * Return a indented string representation for this object.
   *
   * @return a {@code String} with a representation of the object.
   */
  public String toFormatedString() {
    StringBuilder sb = new StringBuilder();
    toFormatedString(sb, 0, false);
    return sb.toString();
  }
}
