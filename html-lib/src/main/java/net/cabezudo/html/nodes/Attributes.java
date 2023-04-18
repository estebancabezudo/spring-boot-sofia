/**
 * MIT License
 *
 * Copyright (c) 2016 - 2022 Esteban Cabezudo
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
package net.cabezudo.html.nodes;

import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author <a href="http://cabezudo.net">Esteban Cabezudo</a>
 * @version 0.01.00, 2022.03.05
 */
public class Attributes implements Iterable<Attribute> {

  private final Map<String, Attribute> map;

  Attributes(Attributes attributes) {
    this();
    for (Attribute attribute : attributes) {
      map.put(attribute.getName(), attribute);
    }
  }

  Attributes() {
    map = new TreeMap<>();
  }

  void add(Attribute attribute) {
    if (attribute != null) {
      map.put(attribute.getName(), attribute);
    }
  }

  @Override
  public Iterator<Attribute> iterator() {
    return map.values().iterator();
  }

  public Attribute get(String name) {
    return map.get(name);
  }

  public void remove(String attributeName) {
    map.remove(attributeName);
  }

  boolean isEmpty() {
    return map.isEmpty();
  }

  public int size() {
    return map.size();
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    for (Attribute attribute : map.values()) {
      sb.append(attribute.toString());
      sb.append(" ");
    }
    return sb.toString().trim();
  }
}
