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
package net.cabezudo.json.exceptions;

import net.cabezudo.json.Position;

import java.util.Objects;

/**
 * Thrown when an error has been reached unexpectedly while parsing a JSON string.
 *
 * @author <a href="http://cabezudo.net">Esteban Cabezudo</a>
 * @version 0.9, 06/04/2014
 */
public class JSONParseException extends Exception {

  private static final long serialVersionUID = -1056432009939512679L;

  private final Position position;

  /**
   * Constructs a {@link net.cabezudo.json.exceptions.JSONParseException} with a specified detail message, a cause, and a {@link net.cabezudo.json.Position}. The position is used
   * to store a position of the property in a source in order to search the misspelled property.
   *
   * @param message the detail message.
   * @param cause The cause (which is saved for later retrieval by the {@link java.lang.Throwable#getCause()} method. (A null value is permitted, and indicates that the cause is
   * nonexistent or unknown.)
   * @param position the position to store.
   */
  public JSONParseException(String message, Throwable cause, Position position) {
    super(message, cause);
    this.position = position;
  }

  /**
   * Constructs a {@link net.cabezudo.json.exceptions.JSONParseException} with a specified detail message, and a {@link net.cabezudo.json.Position}. The position is used to store a
   * position of the property in a source in order to search the misspelled property.
   *
   * @param message the detail message.
   * @param position the position to store.
   */
  public JSONParseException(String message, Position position) {
    super(message);
    this.position = position;
  }

  /**
   * Retrieve a {@link net.cabezudo.json.Position} object used to store additional data about the exception.
   *
   * @return the {@link net.cabezudo.json.Position} stored in the exception.
   */
  public Position getPosition() {
    return position;
  }

  /**
   * Compares this exception to the specified object. The result is {@code true} if and only if the argument is not {@code null} and is a
   * {@link net.cabezudo.json.exceptions.JSONParseException} object that has the same message and the same position as {@code this} object.
   *
   * @param object The object to compare this {@link net.cabezudo.json.exceptions.JSONParseException} against
   * @return {@code true} if the given object represents a {@link net.cabezudo.json.exceptions.JSONParseException} equivalent to {@code this}
   * {@link net.cabezudo.json.exceptions.JSONParseException}, {@code false} otherwise
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
    final JSONParseException other = (JSONParseException) object;
    return Objects.equals(this.getMessage(), other.getMessage())
        && Objects.equals(this.getPosition(), other.getPosition());
  }

  /**
   * Returns a hash code for this {@link net.cabezudo.json.exceptions.JSONParseException}. The hash code for a {@link net.cabezudo.json.exceptions.JSONParseException} object is
   * computed using the message and the position hash.
   *
   * @return a hash code value for {@code this} object.
   */
  @Override
  public int hashCode() {
    int hash = 7;
    hash = 23 * hash + Objects.hashCode(this.getMessage());
    hash = 23 * hash + Objects.hashCode(this.position);
    return hash;
  }
}
