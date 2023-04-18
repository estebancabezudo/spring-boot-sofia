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

import net.cabezudo.json.exceptions.UnexpectedElementException;

/**
 * @author <a href="http://cabezudo.net">Esteban Cabezudo</a>
 * @version 0.9, 07/15/2016
 */
class Tokenizer {

  static Tokens tokenize(String origin, String string) throws UnexpectedElementException {
    char[] chars = string.toCharArray();
    Tokens tokens = new Tokens();
    Token token = new Token(new Position(origin));
    boolean isString = false;

    int line = token.getPosition().getLine();
    int row = token.getPosition().getRow();
    for (int i = 0; i < chars.length; i++, row++) {
      char c = chars[i];
      if (isString) {
        do {
          if (c == '\\') {
            token.append(c);
            i++;
            if (i < chars.length) {
              c = chars[i];
              token.append(c);
            }
            break;
          }
          if (c == '"') {
            token.append(c);
            isString = false;
            tokens.add(token);
            token = new Token(new Position(origin, line, row + 1));
          } else {
            token.append(c);
          }
        } while (false);
      } else {
        switch (c) {
          case '"':
            token.append(c);
            isString = true;
            break;
          case '\n':
            line++;
            row = 1;
            break;
          case ' ':
          case '\u00A0':
          case ':':
          case ',':
          case '{':
          case '}':
          case '[':
          case ']':
            tokens.add(token);
            token = new Token(new Position(origin, line, row));
            token.append(c);
            tokens.add(token);
            token = new Token(new Position(origin, line, row + 1));
            break;
          default:
            token.append(c);
            break;
        }
      }
    }
    tokens.add(token);
    return tokens;
  }

  private Tokenizer() {
  }
}
