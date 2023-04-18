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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author <a href="http://cabezudo.net">Esteban Cabezudo</a>
 * @version 0.01.00, 2022.02.26
 */
public class Nodes implements Iterable<Node> {

  private final Node parent;
  private List<Node> list = new ArrayList<>();

  Nodes(Node parent) {
    this.parent = parent;
  }

  Nodes() {
    this(null);
  }

  public Node get(int i) {
    return list.get(i);
  }

  @Override
  public Iterator<Node> iterator() {
    return list.iterator();
  }

  void add(Node node) {
    list.add(node);
    node.setParent(parent);
  }

  public void insert(int position, Node node) {
    list.add(position, node);
    node.setParent(parent);
  }

  public void removeMarkedNodes() {
    List<Node> newList = new ArrayList<>();
    for (int i = 0; i < list.size(); i++) {
      Node node = list.get(i);
      if (!node.isSelected()) {
        newList.add(node);
      }
    }
    list = newList;
  }

  boolean isEmpty() {
    return list.isEmpty();
  }

  public void replace(Node replace, Node replacement) {
    List<Node> newList = new ArrayList<>();

    replace.select();
    list.forEach(node -> {
      if (node.isSelected()) {
        newList.add(replacement);
      } else {
        newList.add(node);
      }
    });
    list = newList;
  }

  public boolean hasNodes() {
    return list.size() > 0;
  }
}
