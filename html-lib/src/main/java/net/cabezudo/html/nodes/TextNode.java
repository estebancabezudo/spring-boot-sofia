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

import net.cabezudo.html.InvalidOperationException;


/**
 * @author <a href="http://cabezudo.net">Esteban Cabezudo</a>
 * @version 0.01.00, 2022.02.26
 */
public class TextNode extends Node {

  private final StringBuilder sb = new StringBuilder();

  public TextNode(String data, FilePosition position) {
    super(null, false, position);
    sb.append(data);
  }

  public TextNode(char c, FilePosition position) {
    this(Character.toString(c), position);
  }

  void add(String data) {
    sb.append(data);
  }

  public void add(char c) {
    add(Character.toString(c));
  }

  @Override
  public boolean isTextNode() {
    return true;
  }

  @Override
  public String toString() {
    return this.toHTML();
  }

  @Override
  public String toHTML() {
    return sb.toString();
  }

  @Override
  public String getInnerText() {
    return this.toHTML();
  }

  @Override
  public boolean isElement() {
    return false;
  }

  @Override
  public String getType() {
    return "#text";
  }

  @Override
  public boolean isEmpty() {
    return sb.length() == 0;
  }

  @Override
  public void appendChild(Node node) {
    throw new InvalidOperationException("You can't append a child to a text node."); //To change body of generated methods, choose Tools | Templates.
  }

  public void insertChild(int index, Node node) {
    throw new InvalidOperationException("You can't append a child to a text node."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public boolean hasNodeElements() {
    return false;
  }
}
