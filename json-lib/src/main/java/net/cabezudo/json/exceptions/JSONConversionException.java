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

/**
 * Thrown when the conversion between one object to another is not possible.
 *
 * @author <a href="http://cabezudo.net">Esteban Cabezudo</a>
 * @version 0.9, 01/21/2016
 */
public class JSONConversionException extends RuntimeException {

  private static final long serialVersionUID = 4633405861210769003L;

  /**
   * Constructs a {@link net.cabezudo.json.exceptions.JSONConversionException} with a specified
   * detail message, end a cause.
   *
   * @param message the detail message.
   * @param cause The cause (which is saved for later retrieval by the
   * {@link java.lang.Throwable#getCause()} method. (A null value is permitted, and indicates that
   * the cause is nonexistent or unknown.)
   */
  public JSONConversionException(String message, Throwable cause) {
    super(message, cause);
  }

  /**
   * Constructs a {@link net.cabezudo.json.exceptions.JSONConversionException} with a specified
   * detail message.
   *
   * @param message the detail message.
   */
  public JSONConversionException(String message) {
    super(message);
  }
}
