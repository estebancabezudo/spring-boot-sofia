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

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.file.Paths;

/**
 * @author <a href="http://cabezudo.net">Esteban Cabezudo</a>
 * @version 0.01.00, 2022.02.26
 */
public class NodeTest {

  @Test
  public void testGeneralStuf() {
    Node div = new Element("div", false, new FilePosition(Paths.get("fileName"), 7, 3));
    Node text = new TextNode("Hello ", new FilePosition(Paths.get("fileName"), 7, 3));
    Node childDiv = new Element("div", false, new FilePosition(Paths.get("fileName"), 12, 3));
    div.appendChild(text);
    div.appendChild(childDiv);
    Assertions.assertEquals("<div>Hello <div></div></div>", div.toString());
    Assertions.assertEquals("Hello <div></div>", div.getInnerText());
    Node childZero = div.getChild(0);
    Assertions.assertEquals("Hello ", childZero.getInnerText());
    Node childOne = div.getChild(1);
    Assertions.assertTrue(childOne.isDiv());
    Node otherText = new TextNode("Word!", new FilePosition(Paths.get("fileName"), 7, 3));
    div.replace(childOne, otherText);
    Assertions.assertEquals("Hello Word!", div.getInnerText());
  }

  @Test
  public void testAdd() {
    Element div = new Element("div", false, new FilePosition(Paths.get("fileName"), 7, 3));
    Assertions.assertTrue(div.isEmpty());
    Assertions.assertFalse(div.isTextNode());
    Assertions.assertFalse(div.isScript());
    Assertions.assertFalse(div.isStyle());
    Assertions.assertFalse(div.hasChilds());

    Node text = new TextNode("Hello ", new FilePosition(Paths.get("fileName"), 7, 3));
    Node otherText = new TextNode("Word!", new FilePosition(Paths.get("fileName"), 7, 3));
    Nodes nodes = new Nodes();
    nodes.add(text);
    nodes.add(otherText);
    div.add(nodes);
    Assertions.assertEquals("Hello Word!", div.getInnerText());
    text.remove();
    Assertions.assertEquals("Word!", div.getInnerText());

    Assertions.assertTrue(div.hasChilds());
    div.removeChilds();
    Assertions.assertFalse(div.hasChilds());

    div = new Element("div", false, new FilePosition(Paths.get("fileName"), 7, 3));
    Node hello = new TextNode("Hello ", new FilePosition(Paths.get("fileName"), 7, 3));
    div.add(hello);
    Assertions.assertEquals("Hello ", div.getInnerText());

  }

  @Test
  public void testNodeIsNodeEmpty() {
    Element div = new Element("div", false, new FilePosition(Paths.get("fileName"), 7, 3));
    Assertions.assertTrue(div.isEmpty());
  }

  @Test
  public void testGetNodeByTag_String() {
    Element div = new Element("div", false, new FilePosition(Paths.get("fileName"), 7, 3));
    Node text = new TextNode("Hello ", new FilePosition(Paths.get("fileName"), 7, 3));
    Node otherText = new TextNode("Word!", new FilePosition(Paths.get("fileName"), 7, 3));
    Element innerDiv = new Element("div", false, new FilePosition(Paths.get("fileName"), 7, 3));
    Element innerBody = new Element("body", false, new FilePosition(Paths.get("fileName"), 7, 3));
    Element otherInnerDiv = new Element("div", false, new FilePosition(Paths.get("fileName"), 7, 3));
    Element innerInnerBodyHead = new Element("head", false, new FilePosition(Paths.get("fileName"), 7, 3));
    innerBody.appendChild(innerInnerBodyHead);

    Nodes nodes = new Nodes();
    nodes.add(text);
    nodes.add(otherText);
    nodes.add(innerDiv);
    nodes.add(innerBody);
    nodes.insert(2, otherInnerDiv);
    Assertions.assertTrue(nodes.hasNodes());

    div.add(nodes);
    Node body = div.getNodeByTag("body");
    Assertions.assertNotNull(body);
    Assertions.assertTrue(body instanceof Element);
    Assertions.assertEquals("body", body.getTagName());

    Node head = div.getNodeByTag("head");
    Assertions.assertNotNull(head);
    Assertions.assertTrue(head instanceof Element);
    Assertions.assertEquals("head", head.getTagName());

    FilePosition position = head.getPosition();
    Assertions.assertEquals("fileName", position.getPath().toString());
    Assertions.assertEquals("fileName:7:3", position.toString());

    div.removeNodeByTag("head");
    head = div.getNodeByTag("head");
    Assertions.assertNull(head);

    div.removeNodeByTag("body");
    body = div.getNodeByTag("body");
    Assertions.assertNull(body);

  }

}
