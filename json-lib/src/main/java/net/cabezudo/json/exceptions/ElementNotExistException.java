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
package net.cabezudo.json.exceptions;

import net.cabezudo.json.Position;

/**
 * Thrown when an application tried to get a non existent element from a
 * {@link net.cabezudo.json.values.JSONArray} or {@link net.cabezudo.json.values.JSONObject}.
 *
 * @author <a href="http://cabezudo.net">Esteban Cabezudo</a>
 * @version 0.9, 01/23/2017
 */
public class ElementNotExistException extends Exception {

  private static final long serialVersionUID = 6985635990014405620L;

  private final Position position;

  /**
   * Constructs a {@link net.cabezudo.json.exceptions.ElementNotExistException} with the specified
   * detail message and a {@link net.cabezudo.json.Position}. The position is used to store a
   * position of the property in a source in order to search the misspelled property.
   *
   * @param message the detail message.
   * @param position the data position to store.
   */
  public ElementNotExistException(String message, Position position) {
    super(message);
    this.position = position;
  }

  /**
   * Retrieve a {@link net.cabezudo.json.Position} object used to store additional data about the
   * exception.
   *
   * @return the {@link net.cabezudo.json.Position} stored in the exception.
   */
  public Position getPosition() {
    return position;
  }
}
