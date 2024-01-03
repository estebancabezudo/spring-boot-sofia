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

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author <a href="http://cabezudo.net">Esteban Cabezudo</a>
 * @version 0.01.00, 2022.09.23
 */
public class PositionTest {

  @Test
  public void testSimpleMethods() {
    Position position = new Position(1, 3);
    Position other = new Position(position);
    Position andOther = new Position(2, 3);
    Position andOtherMore = new Position(1, 4);
    Position line = new Position(0, 4);
    Position row = new Position(1, 0);

    Assertions.assertEquals(position, position);
    Assertions.assertNotEquals(null, position);
    Assertions.assertNotEquals(position, andOther);
    Assertions.assertNotEquals(position, andOtherMore);
    Assertions.assertEquals(position, other);
    Assertions.assertEquals(position.hashCode(), other.hashCode());
    Assertions.assertEquals("1:3", position.toString());
    Assertions.assertEquals(":4", line.toString());
    Assertions.assertEquals("1", row.toString());
  }
}
