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
 * Thrown when an application tried to get a non existent property from an object.
 *
 * @author <a href="http://cabezudo.net">Esteban Cabezudo</a>
 * @version 0.9, 08/09/2015
 */
public class PropertyIndexNotExistException extends Exception {

  private static final long serialVersionUID = 6985635990014405620L;

  private final Position position;
  private final int index;

  /**
   * Constructs a {@code PropertyNotExistException} with the specified detail message and a {@link net.cabezudo.json.Position}.The position
   * is used to store a position of the property in a source in order to search the misspelled property.
   *
   * @param index the non existing index.
   * @param message the detail message.
   * @param position the data position to store.
   */
  public PropertyIndexNotExistException(int index, String message, Position position) {
    super(message);
    this.index = index;
    this.position = position;
  }

  /**
   * Retrieve a {@code String} with the name of the non existing property.
   *
   * @return the {@link net.cabezudo.json.Position} stored in the exception.
   */
  public int getIndex() {
    return index;
  }

  /**
   * Retrieve a {@link net.cabezudo.json.Position} object used to store additional data about the exception.
   *
   * @return the {@link net.cabezudo.json.Position} stored in the exception.
   */
  public Position getPosition() {
    return position;
  }
}
