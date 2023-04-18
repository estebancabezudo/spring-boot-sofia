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

/**
 * Thrown when is not possible access to an Java object property.
 * <p>
 * The causes can be:
 * <ul>
 * <li>
 * if the POJO getter is enforcing Java language access control and the underlying method is
 * inaccessible.
 * </li>
 * <li>
 * if the getter has parameters.
 * </li>
 * <li>
 * if the getter throws an exception.
 * </li>
 * </ul>
 *
 * @author <a href="http://cabezudo.net">Esteban Cabezudo</a>
 * @version 0.9, 01/22/2016
 */
public class ObjectException extends RuntimeException {

  private static final long serialVersionUID = -3601249847252864843L;

  /**
   * Constructs a {@code ObjectException} with the specified detail message and a cause.
   *
   * @param message the detail message.
   * @param cause The cause (which is saved for later retrieval by the
   * {@link java.lang.Throwable#getCause()} method. (A null value is permitted, and indicates that
   * the cause is nonexistent or unknown.)
   */
  public ObjectException(String message, Throwable cause) {
    super(message, cause);
  }
}
