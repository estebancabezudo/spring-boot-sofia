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

import net.cabezudo.html.HTMLRuntimeException;
import net.cabezudo.html.tokens.BogusCommentToken;
import net.cabezudo.html.tokens.CommentToken;
import net.cabezudo.html.tokens.DoctypeToken;
import net.cabezudo.html.tokens.StartTagToken;
import net.cabezudo.html.tokens.Token;
import net.cabezudo.html.tokens.TokenAttribute;


/**
 * @author <a href="http://cabezudo.net">Esteban Cabezudo</a>
 * @version 0.01.00, 2022.03.05
 */
public class NodeFactory {

  private NodeFactory() {
    // Utility classes should not have public constructor
  }

  public static Node get(char c, FilePosition position) {
    return new TextNode(c, position);
  }

  public static Document getDocument() {
    return new Document(null, null);
  }

  public static Node get(Token currentToken, Node parent) {
    if (currentToken instanceof BogusCommentToken token) {
      Comment comment = new Comment(token.getData(), currentToken.getPosition());
      comment.setParent(parent);
      return comment;
    }
    if (currentToken instanceof CommentToken token) {
      Comment comment = new Comment("!--" + token.getData() + "--", currentToken.getPosition());
      comment.setParent(parent);
      return comment;
    }
    if (currentToken instanceof DoctypeToken token) {
      DoctypeNode doctype = new DoctypeNode(token.getRootElement(), token.getDTDType(), token.getIdentifiers(), currentToken.getPosition());
      doctype.setParent(parent);
      return doctype;
    }
    if (currentToken instanceof StartTagToken) {
      boolean selfClosed = "br".equals(currentToken.getRootElement()) || currentToken.isSelfClosed();

      Element node = new Element(currentToken.getRootElement(), selfClosed, currentToken.getPosition());
      node.setParent(parent);
      for (TokenAttribute tokenAttribute : currentToken.getAttributes()) {
        Attribute attribute = new Attribute(tokenAttribute.getName(), tokenAttribute.getValue(), tokenAttribute.getPosition());
        node.add(attribute);
      }
      return node;
    }
    throw new HTMLRuntimeException("Invalid token class: " + currentToken.getClass().getName());
  }
}
