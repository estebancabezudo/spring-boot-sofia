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

import net.cabezudo.json.exceptions.UnexpectedElementException;

import java.math.BigDecimal;

/**
 * @author <a href="http://cabezudo.net">Esteban Cabezudo</a>
 * @version 0.9, 10/01/2014
 */
class Token {

  private final Position position;

  private StringBuilder sb;
  private TokenType type = TokenType.NONE;

  Token(Position position) {
    this.position = position;
    sb = new StringBuilder();
  }

  @Override
  public String toString() {
    return sb
        + " (type: " + type
        + ", position: " + position
        + ", length: " + sb.length()
        + ")";
  }

  void append(Character c) {
    sb.append(c);
  }

  void clasify() throws UnexpectedElementException {
    String s = sb.toString();

    switch (s.length()) {
      case 0:
        throw new RuntimeException("Zero length token.");
      case 1:
        switch (s) {
          case "\n":
            type = TokenType.NEWLINE;
            break;
          case " ":
          case "\u00A0":
            type = TokenType.SPACE;
            break;
          case ":":
            type = TokenType.COLON;
            break;
          case ",":
            type = TokenType.COMMA;
            break;
          case "{":
            type = TokenType.LEFT_BRACE;
            break;
          case "}":
            type = TokenType.RIGHT_BRACE;
            break;
          case "[":
            type = TokenType.LEFT_BRACKET;
            break;
          case "]":
            type = TokenType.RIGHT_BRACKET;
            break;
        }
        break;
      case 2:
        if ("\"\"".equals(s)) {
          type = TokenType.STRING;
        }
        break;
    }
    if (type == TokenType.NONE) {
      switch (s.toLowerCase()) {
        case "true":
          type = TokenType.TRUE;
          break;
        case "false":
          type = TokenType.FALSE;
          break;
        case "null":
          type = TokenType.NULL;
          break;
        default:
          do {
            if (s.startsWith("\"") && s.endsWith("\"")) {
              type = TokenType.STRING;
              break;
            }
            try {
              BigDecimal number = new BigDecimal(s);
              type = TokenType.NUMBER;
              sb = new StringBuilder(number.toString());
              break;
            } catch (NumberFormatException e) {
              throw new UnexpectedElementException(s, position);
            }
          } while (false);
          break;
      }
    }
  }

  boolean empty() {
    return sb.length() == 0;
  }

  Position getPosition() {
    return position;
  }

  TokenType getType() {
    return type;
  }

  String getValue() {
    return sb.toString();
  }

  boolean isLeftBrace() {
    return type == TokenType.LEFT_BRACE;
  }

  boolean isLeftBracket() {
    return type == TokenType.LEFT_BRACKET;
  }

  boolean isNumber() {
    return type == TokenType.NUMBER;
  }

  boolean isRightBrace() {
    return type == TokenType.RIGHT_BRACE;
  }

  boolean isRightBracket() {
    return type == TokenType.RIGHT_BRACKET;
  }

  int length() {
    return sb.length();
  }
}
