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
public abstract class Node {

  public static final String COMMENT_TYPE = "\"#comment\"";
  public static final String DOCTYPE_TYPE = "#doctype";
  public static final String TEXT_TYPE = "#text";
  private final boolean isDiv;
  private final boolean isScript;
  private final boolean isStyle;
  private final String tagName;
  private final FilePosition position;
  private boolean selfClosed;
  private Node parent;
  private Nodes childs = new Nodes(this);
  private boolean selected = false;

  Node(String tagName, boolean selfClosed, FilePosition position) {
    this.tagName = tagName == null ? null : tagName.toLowerCase();
    this.selfClosed = selfClosed;
    this.position = position;
    isDiv = "div".equals(this.tagName);
    isScript = "script".equals(this.tagName);
    isStyle = "style".equals(this.tagName);
  }

  public Node getChild(int i) {
    return childs.get(i);
  }

  public abstract String getInnerText();

  public boolean isDiv() {
    return isDiv;
  }

  public boolean isSelfClosed() {
    return selfClosed;
  }

  public void setSelfClosed(boolean selfClosed) {
    this.selfClosed = selfClosed;
  }

  public void replace(Node replaced, Node replacement) {
    childs.replace(replaced, replacement);
  }

  @Override
  public String toString() {
    return toHTML();
  }

  public String toHTML() {
    StringBuilder sb = new StringBuilder();
    for (Node node : childs) {
      sb.append(node.toHTML());
    }
    return sb.toString();
  }

  public void add(Nodes nodes) {
    for (Node node : nodes) {
      childs.add(node);
    }
  }

  public void add(Node node) {
    childs.add(node);
  }

  public Nodes getChilds() {
    return childs;
  }

  public String getTagName() {
    return tagName;
  }

  public FilePosition getPosition() {
    return position;
  }

  public abstract String getType();

  public abstract void appendChild(Node node);

  public abstract void insertChild(int index, Node node);

  public abstract boolean hasNodeElements();

  public Node getParent() {
    return parent;
  }

  void setParent(Node parent) {
    this.parent = parent;
  }

  public abstract boolean isElement();

  public boolean isCommentNode() {
    return false;
  }

  public boolean isTextNode() {
    return false;
  }

  public void remove() {
    selected = true;
    parent.removeMarkedNodes();
  }

  boolean isSelected() {
    return selected;
  }

  private void removeMarkedNodes() {
    childs.removeMarkedNodes();
  }

  public boolean isEmpty() {
    return childs.isEmpty();
  }

  public boolean isScript() {
    return isScript;
  }

  public boolean isStyle() {
    return isStyle;
  }

  public boolean hasChilds() {
    return !childs.isEmpty();
  }

  public void removeChilds() {
    childs = new Nodes();
  }

  public Node getNodeByTag(String tagName) {
    return getNodeByTag(this, tagName);
  }

  public Node getNodeByTag(Node root, String tagName) {
    for (Node node : root.getChilds()) {
      if (tagName.equals(node.getTagName())) {
        return node;
      } else {
        Node result = getNodeByTag(node, tagName);
        if (result != null) {
          return result;
        }
      }
    }
    return null;
  }

  public void removeNodeByTag(String tagName) {
    removeNodeByTag(this, tagName);
  }

  public void removeNodeByTag(Node root, String tagName) {
    for (Node node : root.getChilds()) {
      if (tagName.equals(node.getTagName())) {
        node.remove();
        break;
      } else {
        removeNodeByTag(node, tagName);
      }
    }
  }

  void select() {
    this.selected = true;
  }

}
