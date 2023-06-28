/**
 * MIT License
 * <p>
 * Copyright (c) 2016 - 2022 Esteban Cabezudo
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
package net.cabezudo.html.nodes;

/**
 * @author <a href="http://cabezudo.net">Esteban Cabezudo</a>
 * @version 0.01.00, 2022.02.26
 */
public class Position {

  private final int line;
  private final int row;

  public Position(Position position) {
    this.line = position.getLine();
    this.row = position.getRow();
  }

  public Position(int line, int row) {
    this.line = line;
    this.row = row;
  }

  /**
   * Return a primitive {@code int} with the line number of the position.
   *
   * @return the line number for the position.
   */
  public int getLine() {
    return line;
  }

  /**
   * Return a primitive {@code int} with the row number of the position.
   *
   * @return the row number for the position.
   */
  public int getRow() {
    return row;
  }

  @Override
  public String toString() {
    return (getLine() == 0 ? "" : getLine()) + (getRow() == 0 ? "" : ":" + getRow());
  }

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
    final Position other = (Position) object;
    if (this.row != other.row) {
      return false;
    }
    return this.line == other.line;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 67 * hash + this.row;
    hash = 67 * hash + this.line;
    return hash;
  }
}
