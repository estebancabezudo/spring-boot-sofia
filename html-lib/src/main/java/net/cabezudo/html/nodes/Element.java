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

import net.cabezudo.html.InvalidParameterException;

/**
 * @author <a href="http://cabezudo.net">Esteban Cabezudo</a>
 * @version 0.01.00, 2022.02.26
 */
public class Element extends Node {

  private final Attributes attributes;
  private boolean hasNodeElements = false;

  public Element(String tagName, boolean selfClosed, FilePosition position) {
    this(tagName, null, selfClosed, position);
  }

  public Element(String tagName, Attributes attributes, boolean selfClosed, FilePosition position) {
    super(tagName, selfClosed, position);
    if (attributes == null) {
      this.attributes = new Attributes();
    } else {
      this.attributes = attributes;
    }
  }

  public Element(String tagName, Element element) {
    this(tagName, new Attributes(element.getAttributes()), element.isSelfClosed(), element.getPosition());
  }

  @Override
  public boolean isElement() {
    return true;
  }

  public Attributes getAttributes() {
    return attributes;
  }

  @Override
  public boolean hasNodeElements() {
    return hasNodeElements;
  }

  @Override
  public String getInnerText() {
    StringBuilder sb = new StringBuilder();
    Nodes nodes = getChilds();
    for (Node node : nodes) {
      sb.append(node.toHTML());
    }
    return sb.toString();
  }

  public void add(Attribute attribute) {
    attributes.add(attribute);
  }

  @Override
  public String getType() {
    return getTagName().toUpperCase();
  }

  @Override
  public void appendChild(Node node) {
    if (node == null) {
      throw new InvalidParameterException("Can't append a null child");
    }
    if (node.isElement()) {
      hasNodeElements = true;
    }
    getChilds().add(node);
  }

  @Override
  public void insertChild(int index, Node node) {
    if (node == null) {
      throw new InvalidParameterException("Can't insert a null child");
    }
    if (node.isElement()) {
      hasNodeElements = true;
    }
    //node.setParent(this);
    getChilds().insert(index, node);
  }

  public void removeAttribute(String attributeName) {
    attributes.remove(attributeName);
  }

  public Attribute getAttribute(String id) {
    return attributes.get(id);
  }

  public void setAttribute(String key, String value) {
    attributes.add(new Attribute(key, value));
  }

  @Override
  public String toHTML() {
    StringBuilder sb = new StringBuilder();
    sb.append("<").append(getTagName()).append(attributes.isEmpty() ? "" : " " + attributes);
    if (isSelfClosed()) {
      sb.append("/>");
    } else {
      sb.append(">");
      Nodes nodes = getChilds();
      for (Node node : nodes) {
        sb.append(node.toHTML());
      }
      sb.append("</").append(getTagName()).append(">");
    }
    return sb.toString();
  }

}
