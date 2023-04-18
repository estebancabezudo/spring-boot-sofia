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

/**
 * Thrown when an application tried to get a non existent element from a {@link net.cabezudo.json.values.JSONArray} or {@link net.cabezudo.json.values.JSONObject}.
 *
 * @author <a href="http://cabezudo.net">Esteban Cabezudo</a>
 * @version 0.9, 01/23/2017
 */
public class DuplicateKeyException extends Exception {

  private final String key;
  private final Position actualKeyPosition;
  private final Position newKeyPosition;

  /**
   * Constructs a {@link net.cabezudo.json.exceptions.DuplicateKeyException} with the specified detail message and a {@link net.cabezudo.json.Position}. The position is used to
   * store a position of the property in a source in order to search the duplicated property.
   *
   * @param message           the detail message.
   * @param key               the duplicate key.
   * @param actualKeyPosition the position of the fist key.
   * @param newKeyPosition    the position of the new key.
   */
  public DuplicateKeyException(String message, String key, Position actualKeyPosition, Position newKeyPosition) {
    super(message);
    this.key = key;
    this.actualKeyPosition = actualKeyPosition;
    this.newKeyPosition = newKeyPosition;
  }

  /**
   * Constructs a {@link net.cabezudo.json.exceptions.DuplicateKeyException} with the specified detail message and a {@link net.cabezudo.json.Position}. The position is used to
   * store a position of the property in a source in order to search the duplicated property.
   *
   * @param message the detail message.
   * @param key     the duplicate key.
   */
  public DuplicateKeyException(String message, String key) {
    this(message, key, null, null);
  }

  public String getKey() {
    return key;
  }

  /**
   * Retrieve a {@link net.cabezudo.json.Position} object used to store additional data about the exception.
   *
   * @return the {@link net.cabezudo.json.Position} stored in the exception.
   */
  public Position getActualKeyPosition() {
    return actualKeyPosition;
  }

  public Position getNewKeyPosition() {
    return newKeyPosition;
  }
}
