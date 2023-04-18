/**
 * MIT License
 *
 * Copyright (c) 2016 - 2017 Esteban Cabezudo
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

import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="http://cabezudo.net">Esteban Cabezudo</a>
 * @version 0.9, 10/01/2014
 */
public class Formatter {

  private static String ident(int n) {
    StringBuilder sb = new StringBuilder(n * 3);
    for (int i = 0; i < n; i++) {
      sb.append("   ");
    }
    return sb.toString();
  }

  /**
   * Add carriages returns and indent the given {@code String} and return the {@code String} with formatted JSON.
   *
   * @param jsonString the JSON {@code String} with the JSON string in raw format.
   * @return the JSON string formatted.
   */
  public static String indent(String jsonString) {
    StringBuilder sb = new StringBuilder();
    List<String> lines = new ArrayList<>();

    char[] cs = jsonString.toCharArray();
    for (int i = 0; i < cs.length; i++) {
      char c = cs[i];
      do {
        if (c == '{' || c == '[' || c == ',' || c == '\n') {
          sb.append(c);
          String line = sb.toString().trim();
          lines.add(line);
          sb = new StringBuilder();
          break;
        }
        if (c == '}' || c == ']') {
          String line = sb.toString().trim();
          if (line.length() > 0) {
            lines.add(line);
            sb = new StringBuilder();
          }
          line = Character.toString(c);
          if (i + 1 < cs.length) {
            c = cs[i + 1];
            if (c == ',') {
              line += c;
              i += 1;
            }
          }
          lines.add(line);
          break;
        }
        sb.append(c);
      } while (false);
    }

    int ident = 0;
    sb = new StringBuilder();
    for (String line : lines) {

      char endChar;
      if (line.length() > 1) {
        endChar = line.charAt(line.length() - 1);
      } else {
        endChar = line.charAt(0);
      }
      do {

        if (endChar == '{' || endChar == '[') {
          sb.append(ident(ident));
          sb.append(line);
          sb.append('\n');
          ident++;
          break;
        }

        if (endChar == '}' || endChar == ']' || line.endsWith("},") || line.endsWith("],")) {
          ident--;
          sb.append(ident(ident));
          sb.append(line);
          sb.append('\n');

          break;
        }

        sb.append(ident(ident));
        sb.append(line);
        sb.append('\n');
      } while (false);
    }
    return sb.toString();
  }

  private Formatter() {
  }
}
