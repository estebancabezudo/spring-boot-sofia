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
package net.cabezudo.json;

import net.cabezudo.json.exceptions.EmptyQueueException;
import net.cabezudo.json.exceptions.UnexpectedElementException;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

import static net.cabezudo.json.TokenType.SPACE;

/**
 * @author <a href="http://cabezudo.net">Esteban Cabezudo</a>
 * @version 0.9, 10/02/2014
 */
class Tokens implements Iterable<Token> {

  private final Queue<Token> queue = new LinkedList<>();
  private Position position;

  boolean add(Token token) throws UnexpectedElementException {
    if (token == null || token.empty()) {
      return false;
    }
    token.clasify();

    TokenType tokenType = token.getType();
    if (SPACE.equals(tokenType)) {
      return false;
    }

    if (queue.isEmpty()) {
      position = token.getPosition();
    }

    return queue.offer(token);
  }

  Token element() throws EmptyQueueException {
    Token token = queue.element();
    if (token == null) {
      throw new EmptyQueueException();
    }
    return token;
  }

  Position getPosition() {
    return position;
  }

  boolean hasNext() {
    return queue.size() > 0;
  }

  Token consume() throws EmptyQueueException {
    Token token = queue.poll();
    if (token == null) {
      throw new EmptyQueueException();
    }
    return token;
  }

  @Override
  public Iterator<Token> iterator() {
    return queue.iterator();
  }
}
