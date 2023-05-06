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
package net.cabezudo.html;

import net.cabezudo.html.nodes.FilePosition;
import net.cabezudo.html.nodes.Node;
import net.cabezudo.html.nodes.NodeFactory;
import net.cabezudo.html.nodes.TextNode;
import net.cabezudo.html.tokens.CommentToken;
import net.cabezudo.html.tokens.DoctypeToken;
import net.cabezudo.html.tokens.EndTagToken;
import net.cabezudo.html.tokens.StartTagToken;
import net.cabezudo.html.tokens.Token;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author <a href="http://cabezudo.net">Esteban Cabezudo</a>
 * @version 0.01.00, 2022.03.02
 */
class StackOfOpenElements {

  private static final Logger log = LoggerFactory.getLogger(StackOfOpenElements.class);

  private Node currentNode;
  private Node lastNode;

  public void add(Node node) {
    appendChild(currentNode, node);
    currentNode = node;
  }

  void add(char c, FilePosition position) {
    if (lastNode instanceof TextNode) {
      ((TextNode) lastNode).add(c);
    } else {
      Node node = NodeFactory.get(c, position);
      appendChild(currentNode, node);
      lastNode = node;
    }
  }

  void add(Token token) {
    if (token instanceof CommentToken || token instanceof DoctypeToken) {
      Node node = NodeFactory.get(token, currentNode);
      lastNode = null;
      appendChild(currentNode, node);
      return;
    }
    if (token instanceof StartTagToken) {
      Node node = NodeFactory.get(token, currentNode);
      lastNode = null;
      appendChild(currentNode, node);
      if (!node.isSelfClosed()) {
        currentNode = node;
      }
      return;
    }
    if (token instanceof EndTagToken) {
      lastNode = currentNode;
      currentNode = currentNode.getParent();
      return;
    }
    throw new HTMLRuntimeException("Invalid token parameter: " + token);
  }

  private void appendChild(Node currentNode, Node node) {
    if (currentNode == null) {
      this.currentNode = node;
    } else {
      this.currentNode.appendChild(node);
    }
  }
}
