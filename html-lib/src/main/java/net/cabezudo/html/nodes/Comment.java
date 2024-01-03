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
public final class Comment extends Node {

  private final StringBuilder comment = new StringBuilder();

  public Comment(String comment, FilePosition position) {
    super(null, false, position);
    this.comment.append(comment);
  }

  @Override
  public boolean isCommentNode() {
    return true;
  }

  @Override
  public boolean isElement() {
    return false;
  }

  @Override
  public boolean hasNodeElements() {
    return false;
  }

  @Override
  public String toHTML() {
    return "<" + getComment() + ">";
  }

  @Override
  public String getInnerText() {
    return getComment();
  }

  @Override
  public String toString() {
    return this.toHTML();
  }

  public String getComment() {
    return comment.toString();
  }

  @Override
  public String getType() {
    return "#comment";
  }

  @Override
  public void appendChild(Node node) {
    throw new InvalidOperationException("You can't append a child to a comment node."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public void insertChild(int indes, Node node) {
    throw new InvalidOperationException("You can't append a child to a comment node."); //To change body of generated methods, choose Tools | Templates.
  }
}
