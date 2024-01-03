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
 * @version 0.01.00, 2022.03.05
 */
public class DoctypeNode extends Node {

  private final String dtdType;
  private final String rootElement;
  private final String identifiers;

  public DoctypeNode(String rootElement) {
    this(rootElement, null, null, null);
  }

  public DoctypeNode(String rootElement, FilePosition position) {
    this(rootElement, null, null, position);
  }

  public DoctypeNode(String rootElement, String dtdType, FilePosition position) {
    this(rootElement, dtdType, null, position);
  }

  public DoctypeNode(String rootElement, String dtdType, String identifiers, FilePosition position) {
    super(null, false, position);
    this.rootElement = rootElement;
    this.dtdType = dtdType;
    this.identifiers = identifiers;
  }

  @Override
  public String getTagName() {
    return TagName.DOCTYPE;
  }

  public String getRootElement() {
    return rootElement;
  }

  public String getDTDType() {
    return dtdType;
  }

  public String getIdentifiers() {
    return identifiers;
  }

  @Override
  public String toHTML() {
    return "<!DOCTYPE" + (getRootElement() == null ? "" : " " + getRootElement()) + (dtdType == null || dtdType.length() == 0 ? "" : " " + dtdType) + ((identifiers == null || identifiers.length() == 0) ? "" : " " + identifiers) + ">";
  }

  @Override
  public String getInnerText() {
    return "";
  }

  @Override
  public String toString() {
    return toHTML();
  }

  @Override
  public boolean isElement() {
    return false;
  }

  @Override
  public String getType() {
    return "#doctype";
  }

  @Override
  public boolean isEmpty() {
    return false;
  }

  @Override
  public void appendChild(Node node) {
    throw new InvalidOperationException("You can't append a child to a text node: " + node);
  }

  @Override
  public void insertChild(int index, Node node) {
    throw new InvalidOperationException("You can't append a child to a text node: " + node);
  }

  @Override
  public boolean hasNodeElements() {
    return false;
  }
}
