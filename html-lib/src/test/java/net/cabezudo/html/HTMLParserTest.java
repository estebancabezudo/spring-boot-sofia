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

import net.cabezudo.html.nodes.Attribute;
import net.cabezudo.html.nodes.Attributes;
import net.cabezudo.html.nodes.Document;
import net.cabezudo.html.nodes.Element;
import net.cabezudo.html.nodes.Node;
import net.cabezudo.html.nodes.Nodes;
import net.cabezudo.html.nodes.TextNode;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * @author <a href="http://cabezudo.net">Esteban Cabezudo</a>
 * @version 0.01.00, 2022.02.25
 */
public class HTMLParserTest {

  private String load(String resourceName) throws IOException, ParseException {
    ClassLoader classLoader = getClass().getClassLoader();
    InputStream is = classLoader.getResourceAsStream(resourceName);
    try (InputStreamReader streamReader = new InputStreamReader(is, StandardCharsets.UTF_8); BufferedReader reader = new BufferedReader(streamReader)) {
      StringBuilder sb = new StringBuilder();
      String line;
      while ((line = reader.readLine()) != null) {
        sb.append(line).append(System.lineSeparator());
      }
      return sb.toString();
    }
  }

  @Test
  public void testHappyParse() throws IOException, ParseException {
    String code = load("test.html");
    HTMLParser htmlParser = new HTMLParser();
    Document context = htmlParser.parse(code);
    Assertions.assertNotNull(context.toString());
    Assertions.assertNotNull(context.toHTML());
    Assertions.assertNotNull(context.getInnerText());
    Assertions.assertEquals("#document", context.getType());

    Assertions.assertFalse(context.isElement());
    InvalidParameterException invalidParameterExceptionInContext = Assertions.assertThrows(InvalidParameterException.class, () -> {
      context.appendChild(null);
    });
    Assertions.assertNotNull(invalidParameterExceptionInContext);
    Element element = new Element("div", false, null);
    context.appendChild(element);
    Assertions.assertTrue(context.hasNodeElements());

    InvalidParameterException invalidParameterException = Assertions.assertThrows(InvalidParameterException.class, () -> {
      context.appendChild(null);
    });
    Assertions.assertNotNull(invalidParameterException);
    processDocumentForHappyParse(context);
  }

  private void processDocumentForHappyParse(Node node) {
    Nodes nodes = node.getChilds();
    for (Node n : nodes) {
      if ("#cdata".equals(n.getType())) {
        Assertions.assertFalse(n.hasNodeElements());
        InvalidOperationException invalidOperationWithTextNode = Assertions.assertThrows(InvalidOperationException.class, () -> {
          n.appendChild(new TextNode("text", null));
        });
        Assertions.assertNotNull(invalidOperationWithTextNode);
      }
      if ("script".equals(n.getTagName())) {
        Assertions.assertEquals("\n      if (a < 6) {\n        console.out(`${test}`);\n      }\n    ", n.getInnerText());
      }
      if (n.isCommentNode()) {
        Assertions.assertFalse(n.hasNodeElements());
        Assertions.assertEquals("#comment", n.getType());
        Assertions.assertNotNull(n.toString());
        Assertions.assertNotNull(n.getInnerText());
        InvalidOperationException invalidOperationWithCommentNode = Assertions.assertThrows(InvalidOperationException.class, () -> {
          n.appendChild(new TextNode("comment", null));
        });
        Assertions.assertNotNull(invalidOperationWithCommentNode);
      }
      if (n.isElement()) {
        Element element = (Element) n;
        if ("link".equals(element.getTagName())) {
          Assertions.assertEquals("LINK", element.getType());
          Assertions.assertEquals("<link href=\"icon.ico\" rel=\"shortcut icon\"/>", element.toHTML());
          Assertions.assertTrue(element.isSelfClosed());
        } else {
          if ("meta".equals(element.getTagName())) {
            Assertions.assertTrue(element.isSelfClosed());
          } else {
            Assertions.assertFalse(element.isSelfClosed());
          }
        }
        Assertions.assertEquals(element.toString(), element.toHTML());
        if ("div".equals(element.getTagName())) {
          element.add(new Attribute("class", "element"));
          Attribute attributeFromElement = element.getAttribute("class");
          Assertions.assertNull(attributeFromElement.getPosition());
          Assertions.assertEquals("element", attributeFromElement.getValue());
          Element newElement = new Element("p", element);
          Attributes attributes = newElement.getAttributes();
          attributes.remove("class");
          Assertions.assertFalse(newElement.hasNodeElements());
          Assertions.assertEquals("<p></p>", newElement.toHTML());
        }
      }
      processDocumentForHappyParse(n);
    }
  }

  @Test
  public void testBogusComment() throws IOException, ParseException {
    String code = load("bogusComment.html");
    HTMLParser htmlParser = new HTMLParser();
    htmlParser.parse(code);
  }

  @Test
  public void testSurrogateCharacter() throws IOException, ParseException {
    String code = "<html><body>" + (char) 0xD800 + "</body></html>";
    HTMLParser htmlParser = new HTMLParser();

    ParseException parseException = Assertions.assertThrows(ParseException.class, () -> {
      htmlParser.parse(code);
    });
    ParseErrors errors = htmlParser.getParseErrors();
    Assertions.assertEquals("Surrogate in input stream parse error: " + (char) 0xD800, errors.get(0).toString());
    Assertions.assertNotNull(parseException);
    Assertions.assertNotNull(parseException.getPosition());
  }

  @Test
  public void testForNonCharacter() throws IOException, ParseException {
    ParseException parseException = Assertions.assertThrows(ParseException.class, () -> {
      String code = "<html><body>" + (char) 0xFDD0 + "</body></html>";
      HTMLParser htmlParser = new HTMLParser();
      htmlParser.parse(code);
    });
    Assertions.assertNotNull(parseException);

    parseException = Assertions.assertThrows(ParseException.class, () -> {
      String code = "<html><body>" + (char) 0x2FFFF + "</body></html>";
      HTMLParser htmlParser = new HTMLParser();
      htmlParser.parse(code);
    });
    Assertions.assertNotNull(parseException);
  }

  @Test
  public void testDeleteControlCharacter() throws IOException, ParseException {
    String code = "<html><body>" + (char) 0x007F + (char) 0x0020 + "</body></html>";
    HTMLParser htmlParser = new HTMLParser();
    ParseException parseException = Assertions.assertThrows(ParseException.class, () -> {
      htmlParser.parse(code);
    });
    Assertions.assertNotNull(parseException);
  }

  @Test
  public void testLessThanDeleteControlCharacter() throws IOException, ParseException {
    String code = "<html><body>" + (char) 0x007E + "</body></html>";
    HTMLParser htmlParser = new HTMLParser();
    htmlParser.parse(code);
  }

  @Test
  public void testGreatThanDeleteControlCharacter() throws IOException, ParseException {
    String code = "<html><body>" + (char) 0x00A0 + "</body></html>";
    HTMLParser htmlParser = new HTMLParser();
    htmlParser.parse(code);
  }

  @Test
  public void testWhiteSpacesCharacters() throws IOException, ParseException {
    String code = "<html><body>" + HTMLParser.CHARACTER_TABULATION + HTMLParser.FORM_FEED + "</body></html>";
    HTMLParser htmlParser = new HTMLParser();
    htmlParser.parse(code);
  }

  @Test
  public void testEOFBeforTagName() throws IOException, ParseException {
    String code = "<html><";
    HTMLParser htmlParser = new HTMLParser();
    ParseException parseException = Assertions.assertThrows(ParseException.class, () -> {
      htmlParser.parse(code);
    });
    Assertions.assertNotNull(parseException);
  }

  @Test
  public void testEOFBeforeEndTagName() throws IOException, ParseException {
    String code = "<html></";
    HTMLParser htmlParser = new HTMLParser();
    ParseException parseException = Assertions.assertThrows(ParseException.class, () -> {
      htmlParser.parse(code);
    });
    Assertions.assertNotNull(parseException);
  }

  @Test
  public void testASCIIAlphaInTagName() throws IOException, ParseException {
    String code = "<HT9[m|l>";
    HTMLParser htmlParser = new HTMLParser();
    htmlParser.parse(code);
  }

  @Test
  public void testEndTagClosedBeforTagName() throws IOException, ParseException {
    String code = "<html></>";
    HTMLParser htmlParser = new HTMLParser();
    htmlParser.parse(code);
  }

  @Test
  public void testNullInTagName() throws IOException, ParseException {
    String code = "<html" + HTMLParser.NULL + ">";
    HTMLParser htmlParser = new HTMLParser();
    ParseException parseException = Assertions.assertThrows(ParseException.class, () -> {
      htmlParser.parse(code);
    });
    Assertions.assertNotNull(parseException);
  }

  @Test
  public void testEOFInTagName() throws IOException, ParseException {
    String code = "<html";
    HTMLParser htmlParser = new HTMLParser();
    ParseException parseException = Assertions.assertThrows(ParseException.class, () -> {
      htmlParser.parse(code);
    });
    Assertions.assertNotNull(parseException);
  }

  @Test
  public void testNoASCIIInTagName() throws IOException, ParseException {
    String code = "<html-></html>";
    HTMLParser htmlParser = new HTMLParser();
    htmlParser.parse(code);
  }

  @Test
  public void testInvalidFirstCharacterOfEndTagName() throws IOException, ParseException {
    String code = "<html></*";
    HTMLParser htmlParser = new HTMLParser();
    ParseException parseException = Assertions.assertThrows(ParseException.class, () -> {
      htmlParser.parse(code);
    });
    Assertions.assertNotNull(parseException);
  }

  @Test
  public void testElementMethods() throws IOException, ParseException {
    Element element = new Element("div", false, null);
    InvalidParameterException invalidParameterException = Assertions.assertThrows(InvalidParameterException.class,
        () -> {
          element.appendChild(null);
        });
    Assertions.assertNotNull(invalidParameterException);
    element.setAttribute("id", "elementId");
    Attribute attribute = element.getAttribute("id");
    Assertions.assertEquals("elementId", attribute.getValue());

    Attributes attributes = element.getAttributes();
    Assertions.assertNotNull(attributes);
    Assertions.assertEquals(1, attributes.size());

    element.removeAttribute("id");
    Assertions.assertEquals(0, element.getAttributes().size());
  }

  @Test
  public void testUnexpectedEqualsSignBeforeAttributeNameParseError() throws IOException, ParseException {
    String code = "<html =></html>";
    HTMLParser htmlParser = new HTMLParser();
    ParseException parseException = Assertions.assertThrows(ParseException.class, () -> {
      htmlParser.parse(code);
    });
    Assertions.assertNotNull(parseException);
  }

  @Test
  public void testUnexpectedNullInAttributeName() throws IOException, ParseException {
    String code = "<html na" + HTMLParser.NULL + "me=\"\"></html>";
    HTMLParser htmlParser = new HTMLParser();
    ParseException parseException = Assertions.assertThrows(ParseException.class, () -> {
      htmlParser.parse(code);
    });
    Assertions.assertNotNull(parseException);
  }

  @Test
  public void testUnexpectedCharacterInAttributeName() throws IOException, ParseException {
    String code = "<html n\"ame=\"\"></html>";
    HTMLParser htmlParser = new HTMLParser();
    ParseException parseException = Assertions.assertThrows(ParseException.class, () -> {
      htmlParser.parse(code);
    });
    Assertions.assertNotNull(parseException);
  }

  @Test
  public void testSpaceAfterAttributeName() throws IOException, ParseException {
    String code = "<html><body name  =\"value\"></body</html>";
    HTMLParser htmlParser = new HTMLParser();
    ParseException parseException = Assertions.assertThrows(ParseException.class, () -> {
      htmlParser.parse(code);
    });
    Assertions.assertNotNull(parseException);
  }

  @Test
  public void testEOFAfterAttributeName() throws IOException, ParseException {
    String code = "<html><body name ";
    HTMLParser htmlParser = new HTMLParser();
    ParseException parseException = Assertions.assertThrows(ParseException.class, () -> {
      htmlParser.parse(code);
    });
    Assertions.assertNotNull(parseException);
  }

  @Test
  public void testCharacterAfterAttributeName() throws IOException, ParseException {
    String code = "<html><body name  a=\"value\"></body</html>";
    HTMLParser htmlParser = new HTMLParser();
    ParseException parseException = Assertions.assertThrows(ParseException.class, () -> {
      htmlParser.parse(code);
    });
    Assertions.assertNotNull(parseException);
  }

  @Test
  public void testSpaceBeforeAttributeValue() throws IOException, ParseException {
    String code = "<html><body name= \"value\"></body</html>";
    HTMLParser htmlParser = new HTMLParser();
    ParseException parseException = Assertions.assertThrows(ParseException.class, () -> {
      htmlParser.parse(code);
    });
    Assertions.assertNotNull(parseException);
  }

  @Test
  public void testApostropeBeforeAttributeValue() throws IOException, ParseException {
    String code = "<html><body name='\"value\"></body</html>";
    HTMLParser htmlParser = new HTMLParser();
    ParseException parseException = Assertions.assertThrows(ParseException.class, () -> {
      htmlParser.parse(code);
    });
    Assertions.assertNotNull(parseException);
  }

  @Test
  public void testGreaterThanBeforeAttributeValue() throws IOException, ParseException {
    String code = "<html><body name=>\"value\"></body</html>";
    HTMLParser htmlParser = new HTMLParser();
    ParseException parseException = Assertions.assertThrows(ParseException.class, () -> {
      htmlParser.parse(code);
    });
    Assertions.assertNotNull(parseException);
  }

  @Test
  public void testCharacterBeforeAttributeValue() throws IOException, ParseException {
    String code = "<html><body name= a\"value\"></body</html>";
    HTMLParser htmlParser = new HTMLParser();
    ParseException parseException = Assertions.assertThrows(ParseException.class, () -> {
      htmlParser.parse(code);
    });
    Assertions.assertNotNull(parseException);
  }

  @Test
  public void testUnexpectedNullCharacterInAttributeValueDoubleQuoted() throws IOException, ParseException {
    String code = "<html><body name= \"val" + HTMLParser.NULL + "ue\"></body</html>";
    HTMLParser htmlParser = new HTMLParser();
    ParseException parseException = Assertions.assertThrows(ParseException.class, () -> {
      htmlParser.parse(code);
    });
    Assertions.assertNotNull(parseException);
  }

  @Test
  public void testUnexpectedEOFInAttributeValueDoubleQuoted() throws IOException, ParseException {
    String code = "<html><body name= \"val";
    HTMLParser htmlParser = new HTMLParser();
    ParseException parseException = Assertions.assertThrows(ParseException.class, () -> {
      htmlParser.parse(code);
    });
    Assertions.assertNotNull(parseException);
  }

  @Test
  public void testUnexpectedApostropheInAttributeValueSingleQuoted() throws IOException, ParseException {
    String code = "<html><body name= 'va" + HTMLParser.APOSTROPHE + "lue'></body><html>";
    HTMLParser htmlParser = new HTMLParser();
    ParseException parseException = Assertions.assertThrows(ParseException.class, () -> {
      htmlParser.parse(code);
    });
    Assertions.assertNotNull(parseException);
  }

  @Test
  public void testUnexpectedNullInAttributeValueSingleQuoted() throws IOException, ParseException {
    String code = "<html><body name= 'va" + HTMLParser.NULL + "lue'></body><html>";
    HTMLParser htmlParser = new HTMLParser();
    ParseException parseException = Assertions.assertThrows(ParseException.class, () -> {
      htmlParser.parse(code);
    });
    Assertions.assertNotNull(parseException);
  }

  @Test
  public void testUnexpectedEOFInAttributeValueSingleQuoted() throws IOException, ParseException {
    String code = "<html><body name= 'val";
    HTMLParser htmlParser = new HTMLParser();
    ParseException parseException = Assertions.assertThrows(ParseException.class, () -> {
      htmlParser.parse(code);
    });
    Assertions.assertNotNull(parseException);
  }

  @Test
  public void testUnexpectedSpaceInAttributeValueUnquoted() throws IOException, ParseException {
    String code = "<html><body name=v alue></body</html>";
    HTMLParser htmlParser = new HTMLParser();
    ParseException parseException = Assertions.assertThrows(ParseException.class, () -> {
      htmlParser.parse(code);
    });
    Assertions.assertNotNull(parseException);
  }

  @Test
  public void testUnexpectedNullInAttributeValueUnquoted() throws IOException, ParseException {
    String code = "<html><body name=va" + HTMLParser.NULL + "lue></body</html>";
    HTMLParser htmlParser = new HTMLParser();
    ParseException parseException = Assertions.assertThrows(ParseException.class, () -> {
      htmlParser.parse(code);
    });
    Assertions.assertNotNull(parseException);
  }

  @Test
  public void testUnexpectedEOFInAttributeValueUnquoted() throws IOException, ParseException {
    String code = "<html><body name= val";
    HTMLParser htmlParser = new HTMLParser();
    ParseException parseException = Assertions.assertThrows(ParseException.class, () -> {
      htmlParser.parse(code);
    });
    Assertions.assertNotNull(parseException);
  }

  @Test
  public void testUnexpectedCharacterAfterAttributeValueQuoted() throws IOException, ParseException {
    String code = "<html><body name='value' ></body</html>";
    HTMLParser htmlParser = new HTMLParser();
    ParseException parseException = Assertions.assertThrows(ParseException.class, () -> {
      htmlParser.parse(code);
    });
    Assertions.assertNotNull(parseException);
  }

  @Test
  public void testUnexpectedSolidusAfterAttributeValueQuoted() throws IOException, ParseException {
    String code = "<html><body name='value'" + HTMLParser.SOLIDUS + "></body</html>";
    HTMLParser htmlParser = new HTMLParser();
    ParseException parseException = Assertions.assertThrows(ParseException.class, () -> {
      htmlParser.parse(code);
    });
    Assertions.assertNotNull(parseException);
  }

  @Test
  public void testUnexpectedEOFAfterAttributeValueQuoted() throws IOException, ParseException {
    String code = "<html><body name='value'";
    HTMLParser htmlParser = new HTMLParser();
    ParseException parseException = Assertions.assertThrows(ParseException.class, () -> {
      htmlParser.parse(code);
    });
    Assertions.assertNotNull(parseException);
  }

  @Test
  public void testUnexpectedEOFInSelfClosingStartTag() throws IOException, ParseException {
    String code = "<html><body><meta rel=\"nothing\"/";
    HTMLParser htmlParser = new HTMLParser();
    ParseException parseException = Assertions.assertThrows(ParseException.class, () -> {
      htmlParser.parse(code);
    });
    Assertions.assertNotNull(parseException);
  }

  @Test
  public void testUnexpectedNullInBogusComment() throws IOException, ParseException {
    String code = "<html><body><?-- bogus" + HTMLParser.NULL + "comment --></body</html>";
    HTMLParser htmlParser = new HTMLParser();
    ParseException parseException = Assertions.assertThrows(ParseException.class, () -> {
      htmlParser.parse(code);
    });
    Assertions.assertNotNull(parseException);
  }

  @Test
  public void testIncorrectOpenedComment() throws IOException, ParseException {
    String code = "<html><body><!comment --></body</html>";
    HTMLParser htmlParser = new HTMLParser();
    ParseException parseException = Assertions.assertThrows(ParseException.class, () -> {
      htmlParser.parse(code);
    });
    Assertions.assertNotNull(parseException);
  }

  @Test
  public void testOneHyphenMinusInCommentStart() throws IOException, ParseException {
    String code = "<html><body><!-a comment --></body</html>";
    HTMLParser htmlParser = new HTMLParser();
    ParseException parseException = Assertions.assertThrows(ParseException.class, () -> {
      htmlParser.parse(code);
    });
    Assertions.assertNotNull(parseException);
  }

  @Test
  public void testBothHyphenMinusInCommentStart() throws IOException, ParseException {
    String code = "<html><body><!-- comment --></body</html>";
    HTMLParser htmlParser = new HTMLParser();
    ParseException parseException = Assertions.assertThrows(ParseException.class, () -> {
      htmlParser.parse(code);
    });
    Assertions.assertNotNull(parseException);
  }

  @Test
  public void testIncorrectGreaterThanSignInCommentStart() throws IOException, ParseException {
    String code = "<html><body><!--></body</html>";
    HTMLParser htmlParser = new HTMLParser();
    ParseException parseException = Assertions.assertThrows(ParseException.class, () -> {
      htmlParser.parse(code);
    });
    Assertions.assertNotNull(parseException);
  }

  @Test
  public void testIncorrectHyphenMinusInCommentStartDash() throws IOException, ParseException {
    String code = "<html><body><!----></body</html>";
    HTMLParser htmlParser = new HTMLParser();
    ParseException parseException = Assertions.assertThrows(ParseException.class, () -> {
      htmlParser.parse(code);
    });
    Assertions.assertNotNull(parseException);
  }

  @Test
  public void testIncorrectGreaterThanSignInCommentStartDash() throws IOException, ParseException {
    String code = "<html><body><!---></body</html>";
    HTMLParser htmlParser = new HTMLParser();
    ParseException parseException = Assertions.assertThrows(ParseException.class, () -> {
      htmlParser.parse(code);
    });
    Assertions.assertNotNull(parseException);
  }

  @Test
  public void testIncorrectEOFInCommentStartDash() throws IOException, ParseException {
    String code = "<html><body><!---";
    HTMLParser htmlParser = new HTMLParser();
    ParseException parseException = Assertions.assertThrows(ParseException.class, () -> {
      htmlParser.parse(code);
    });
    Assertions.assertNotNull(parseException);
  }

  @Test
  public void testCharacterInCommentStartDash() throws IOException, ParseException {
    String code = "<html><body><!---z></body</html>";
    HTMLParser htmlParser = new HTMLParser();
    ParseException parseException = Assertions.assertThrows(ParseException.class, () -> {
      htmlParser.parse(code);
    });
    Assertions.assertNotNull(parseException);
  }

  @Test
  public void testInvalidNullCharacterInComment() throws IOException, ParseException {
    String code = "<html><body><!-- co" + HTMLParser.NULL + "mment --></body</html>";
    HTMLParser htmlParser = new HTMLParser();
    ParseException parseException = Assertions.assertThrows(ParseException.class, () -> {
      htmlParser.parse(code);
    });
    Assertions.assertNotNull(parseException);
  }

  @Test
  public void testExclamationMarkInCommentLessThanSign() throws IOException, ParseException {
    String code = "<html><body><!-- comm<!ent --></body</html>";
    HTMLParser htmlParser = new HTMLParser();
    ParseException parseException = Assertions.assertThrows(ParseException.class, () -> {
      htmlParser.parse(code);
    });
    Assertions.assertNotNull(parseException);
  }

  @Test
  public void testLessThanSignInCommentLessThanSign() throws IOException, ParseException {
    String code = "<html><body><!-- comm<<ent --></body</html>";
    HTMLParser htmlParser = new HTMLParser();
    ParseException parseException = Assertions.assertThrows(ParseException.class, () -> {
      htmlParser.parse(code);
    });
    Assertions.assertNotNull(parseException);
  }

  @Test
  public void testHyphenMinusInCommentLessThanSignBang() throws IOException, ParseException {
    String code = "<html><body><!-- comm<!-ent --></body</html>";
    HTMLParser htmlParser = new HTMLParser();
    ParseException parseException = Assertions.assertThrows(ParseException.class, () -> {
      htmlParser.parse(code);
    });
    Assertions.assertNotNull(parseException);
  }

  @Test
  public void testHyphenMinusInCommentLessThanSignBangDash() throws IOException, ParseException {
    String code = "<html><body><!-- comm<!--ent --></body</html>";
    HTMLParser htmlParser = new HTMLParser();
    ParseException parseException = Assertions.assertThrows(ParseException.class, () -> {
      htmlParser.parse(code);
    });
    Assertions.assertNotNull(parseException);
  }

  @Test
  public void testEOFInCommentLessThanSignBangDashDash() throws IOException, ParseException {
    String code = "<html><body><!-- comm<!--";
    HTMLParser htmlParser = new HTMLParser();
    ParseException parseException = Assertions.assertThrows(ParseException.class, () -> {
      htmlParser.parse(code);
    });
    Assertions.assertNotNull(parseException);
  }

  @Test
  public void testEOFInCommentEndDash() throws IOException, ParseException {
    String code = "<html><body><!-- comment -";
    HTMLParser htmlParser = new HTMLParser();
    ParseException parseException = Assertions.assertThrows(ParseException.class, () -> {
      htmlParser.parse(code);
    });
    Assertions.assertNotNull(parseException);
  }

  @Test
  public void testExclamationMarkInCommentEnd() throws IOException, ParseException {
    String code = "<html><body><!-- comment --!";
    HTMLParser htmlParser = new HTMLParser();
    ParseException parseException = Assertions.assertThrows(ParseException.class, () -> {
      htmlParser.parse(code);
    });
    Assertions.assertNotNull(parseException);
  }

  @Test
  public void testHyphenMinusInCommentEnd() throws IOException, ParseException {
    String code = "<html><body><!-- comment ---";
    HTMLParser htmlParser = new HTMLParser();
    ParseException parseException = Assertions.assertThrows(ParseException.class, () -> {
      htmlParser.parse(code);
    });
    Assertions.assertNotNull(parseException);
  }

  @Test
  public void testCharacterInCommentEnd() throws IOException, ParseException {
    String code = "<html><body><!-- comment --a";
    HTMLParser htmlParser = new HTMLParser();
    ParseException parseException = Assertions.assertThrows(ParseException.class, () -> {
      htmlParser.parse(code);
    });
    Assertions.assertNotNull(parseException);
  }

  @Test
  public void testHyphenMinusInCommentEndBang() throws IOException, ParseException {
    String code = "<html><body><!-- comment --!-";
    HTMLParser htmlParser = new HTMLParser();
    ParseException parseException = Assertions.assertThrows(ParseException.class, () -> {
      htmlParser.parse(code);
    });
    Assertions.assertNotNull(parseException);
  }

  @Test
  public void testGreaterThanSignInCommentEndBang() throws IOException, ParseException {
    String code = "<html><body><!-- comment --!>";
    HTMLParser htmlParser = new HTMLParser();
    ParseException parseException = Assertions.assertThrows(ParseException.class, () -> {
      htmlParser.parse(code);
    });
    Assertions.assertNotNull(parseException);
  }

  @Test
  public void testCharacterInCommentEndBang() throws IOException, ParseException {
    String code = "<html><body><!-- comment --!a";
    HTMLParser htmlParser = new HTMLParser();
    ParseException parseException = Assertions.assertThrows(ParseException.class, () -> {
      htmlParser.parse(code);
    });
    Assertions.assertNotNull(parseException);
  }

  @Test
  public void testEOFInDoctype() throws IOException, ParseException {
    String code = "<!DOCTYPE";
    HTMLParser htmlParser = new HTMLParser();
    ParseException parseException = Assertions.assertThrows(ParseException.class, () -> {
      htmlParser.parse(code);
    });
    Assertions.assertNotNull(parseException);
  }

  @Test
  public void testGreaterThanSignInDoctype() throws IOException, ParseException {
    String code = "<!DOCTYPE>";
    HTMLParser htmlParser = new HTMLParser();
    ParseException parseException = Assertions.assertThrows(ParseException.class, () -> {
      htmlParser.parse(code);
    });
    Assertions.assertNotNull(parseException);
  }

  @Test
  public void testSpaceBeforeDoctypeName() throws IOException, ParseException {
    String code = "<!DOCTYPE  >";
    HTMLParser htmlParser = new HTMLParser();
    ParseException parseException = Assertions.assertThrows(ParseException.class, () -> {
      htmlParser.parse(code);
    });
    Assertions.assertNotNull(parseException);
  }

  @Test
  public void testNullBeforeDoctypeName() throws IOException, ParseException {
    String code = "<!DOCTYPE " + HTMLParser.NULL + ">";
    HTMLParser htmlParser = new HTMLParser();
    ParseException parseException = Assertions.assertThrows(ParseException.class, () -> {
      htmlParser.parse(code);
    });
    Assertions.assertNotNull(parseException);
  }

  @Test
  public void testEOFBeforeDoctypeName() throws IOException, ParseException {
    String code = "<!DOCTYPE ";
    HTMLParser htmlParser = new HTMLParser();
    ParseException parseException = Assertions.assertThrows(ParseException.class, () -> {
      htmlParser.parse(code);
    });
    Assertions.assertNotNull(parseException);
  }

  @Test
  public void testSpaceInDoctypeName() throws IOException, ParseException {
    String code = "<!DOCTYPE n ";
    HTMLParser htmlParser = new HTMLParser();
    ParseException parseException = Assertions.assertThrows(ParseException.class, () -> {
      htmlParser.parse(code);
    });
    Assertions.assertNotNull(parseException);
  }

  @Test
  public void testNullInDoctypeName() throws IOException, ParseException {
    String code = "<!DOCTYPE n" + HTMLParser.NULL + "ame>";
    HTMLParser htmlParser = new HTMLParser();
    ParseException parseException = Assertions.assertThrows(ParseException.class, () -> {
      htmlParser.parse(code);
    });
    Assertions.assertNotNull(parseException);
  }

  @Test
  public void testEOFInDoctypeName() throws IOException, ParseException {
    String code = "<!DOCTYPE name";
    HTMLParser htmlParser = new HTMLParser();
    ParseException parseException = Assertions.assertThrows(ParseException.class, () -> {
      htmlParser.parse(code);
    });
    Assertions.assertNotNull(parseException);
  }

  @Test
  public void testSpaceAfterDoctypeName() throws IOException, ParseException {
    String code = "<!DOCTYPE name  ";
    HTMLParser htmlParser = new HTMLParser();
    ParseException parseException = Assertions.assertThrows(ParseException.class, () -> {
      htmlParser.parse(code);
    });
    Assertions.assertNotNull(parseException);
  }

  @Test
  public void testNullAfterDoctypeName() throws IOException, ParseException {
    String code = "<!DOCTYPE name >" + HTMLParser.NULL;
    HTMLParser htmlParser = new HTMLParser();
    htmlParser.parse(code);
  }

  @Test
  public void testPublicAfterDoctypeName() throws IOException, ParseException {
    String code = "<!DOCTYPE name PUBLIC>";
    HTMLParser htmlParser = new HTMLParser();
    ParseException parseException = Assertions.assertThrows(ParseException.class, () -> {
      htmlParser.parse(code);
    });
    Assertions.assertNotNull(parseException);
  }

  @Test
  public void testSystemAfterDoctypeName() throws IOException, ParseException {
    String code = "<!DOCTYPE name SYSTEM>";
    HTMLParser htmlParser = new HTMLParser();
    ParseException parseException = Assertions.assertThrows(ParseException.class, () -> {
      htmlParser.parse(code);
    });
    Assertions.assertNotNull(parseException);
  }

  @Test
  public void testInvalidCharacterSequenceAfterDoctypeNameAfterDoctypeName() throws IOException, ParseException {
    String code = "<!DOCTYPE name INVALID>";
    HTMLParser htmlParser = new HTMLParser();
    ParseException parseException = Assertions.assertThrows(ParseException.class, () -> {
      htmlParser.parse(code);
    });
    Assertions.assertNotNull(parseException);
  }

  @Test
  public void testSpaceAfterDoctypePublicKeyword() throws IOException, ParseException {
    String code = "<!DOCTYPE name PUBLIC >";
    HTMLParser htmlParser = new HTMLParser();
    ParseException parseException = Assertions.assertThrows(ParseException.class, () -> {
      htmlParser.parse(code);
    });
    Assertions.assertNotNull(parseException);
  }

  @Test
  public void testQuotationMarkAfterDoctypePublicKeyword() throws IOException, ParseException {
    String code = "<!DOCTYPE name PUBLIC" + HTMLParser.QUOTATION_MARK + ">";
    HTMLParser htmlParser = new HTMLParser();
    ParseException parseException = Assertions.assertThrows(ParseException.class, () -> {
      htmlParser.parse(code);
    });
    Assertions.assertNotNull(parseException);
  }

  @Test
  public void testApostropheAfterDoctypePublicKeyword() throws IOException, ParseException {
    String code = "<!DOCTYPE name PUBLIC" + HTMLParser.APOSTROPHE + ">";
    HTMLParser htmlParser = new HTMLParser();
    ParseException parseException = Assertions.assertThrows(ParseException.class, () -> {
      htmlParser.parse(code);
    });
    Assertions.assertNotNull(parseException);
  }

  @Test
  public void testEOFAfterDoctypePublicKeyword() throws IOException, ParseException {
    String code = "<!DOCTYPE name PUBLIC";
    HTMLParser htmlParser = new HTMLParser();
    ParseException parseException = Assertions.assertThrows(ParseException.class, () -> {
      htmlParser.parse(code);
    });
    Assertions.assertNotNull(parseException);
  }

  @Test
  public void testMissingQuoteBeforeDoctypePublicIdentifierAfterDoctypePublicKeyword() throws IOException, ParseException {
    String code = "<!DOCTYPE name PUBLIC-";
    HTMLParser htmlParser = new HTMLParser();
    ParseException parseException = Assertions.assertThrows(ParseException.class, () -> {
      htmlParser.parse(code);
    });
    Assertions.assertNotNull(parseException);
  }

  @Test
  public void testSpaceBeforeDoctypePublicIdentifier() throws IOException, ParseException {
    String code = "<!DOCTYPE name PUBLIC  >";
    HTMLParser htmlParser = new HTMLParser();
    ParseException parseException = Assertions.assertThrows(ParseException.class, () -> {
      htmlParser.parse(code);
    });
    Assertions.assertNotNull(parseException);
  }

  @Test
  public void testQuotationMarkBeforeDoctypePublicIdentifier() throws IOException, ParseException {
    String code = "<!DOCTYPE name PUBLIC \">";
    HTMLParser htmlParser = new HTMLParser();
    ParseException parseException = Assertions.assertThrows(ParseException.class, () -> {
      htmlParser.parse(code);
    });
    Assertions.assertNotNull(parseException);
  }

  @Test
  public void testApostropheBeforeDoctypePublicIdentifier() throws IOException, ParseException {
    String code = "<!DOCTYPE name PUBLIC " + HTMLParser.APOSTROPHE + ">";
    HTMLParser htmlParser = new HTMLParser();
    ParseException parseException = Assertions.assertThrows(ParseException.class, () -> {
      htmlParser.parse(code);
    });
    Assertions.assertNotNull(parseException);
  }

  @Test
  public void testEOFBeforeDoctypePublicIdentifier() throws IOException, ParseException {
    String code = "<!DOCTYPE name PUBLIC ";
    HTMLParser htmlParser = new HTMLParser();
    ParseException parseException = Assertions.assertThrows(ParseException.class, () -> {
      htmlParser.parse(code);
    });
    Assertions.assertNotNull(parseException);
  }

  @Test
  public void testMissingQuoteBeforeDoctypePublicIdentifier() throws IOException, ParseException {
    String code = "<!DOCTYPE name PUBLIC ->";
    HTMLParser htmlParser = new HTMLParser();
    ParseException parseException = Assertions.assertThrows(ParseException.class, () -> {
      htmlParser.parse(code);
    });
    Assertions.assertNotNull(parseException);
  }

  @Test
  public void testQuotationMarkBeforeDoctypePublicIdentifierDoubleQuoted() throws IOException, ParseException {
    String code = "<!DOCTYPE name PUBLIC \"\">";
    HTMLParser htmlParser = new HTMLParser();
    htmlParser.parse(code);
  }

  @Test
  public void testNullBeforeDoctypePublicIdentifierDoubleQuoted() throws IOException, ParseException {
    String code = "<!DOCTYPE name PUBLIC \"" + HTMLParser.NULL + ">";
    HTMLParser htmlParser = new HTMLParser();
    ParseException parseException = Assertions.assertThrows(ParseException.class, () -> {
      htmlParser.parse(code);
    });
    Assertions.assertNotNull(parseException);
  }

  @Test
  public void testEOFBeforeDoctypePublicIdentifierDoubleQuoted() throws IOException, ParseException {
    String code = "<!DOCTYPE name PUBLIC \"";
    HTMLParser htmlParser = new HTMLParser();
    ParseException parseException = Assertions.assertThrows(ParseException.class, () -> {
      htmlParser.parse(code);
    });
    Assertions.assertNotNull(parseException);
  }

  @Test
  public void testCharacterBeforeDoctypePublicIdentifierDoubleQuoted() throws IOException, ParseException {
    String code = "<!DOCTYPE name PUBLIC \"a";
    HTMLParser htmlParser = new HTMLParser();
    ParseException parseException = Assertions.assertThrows(ParseException.class, () -> {
      htmlParser.parse(code);
    });
    Assertions.assertNotNull(parseException);
  }

  @Test
  public void testApostropheMarkBeforeDoctypePublicIdentifierSingleQuoted() throws IOException, ParseException {
    String code = "<!DOCTYPE name PUBLIC ''>";
    HTMLParser htmlParser = new HTMLParser();
    ParseException parseException = Assertions.assertThrows(ParseException.class, () -> {
      htmlParser.parse(code);
    });
    Assertions.assertNotNull(parseException);
  }

  @Test
  public void testNullBeforeDoctypePublicIdentifierSingleQuoted() throws IOException, ParseException {
    String code = "<!DOCTYPE name PUBLIC '" + HTMLParser.NULL + ">";
    HTMLParser htmlParser = new HTMLParser();
    ParseException parseException = Assertions.assertThrows(ParseException.class, () -> {
      htmlParser.parse(code);
    });
    Assertions.assertNotNull(parseException);
  }

  @Test
  public void testEOFBeforeDoctypePublicIdentifierSingleQuoted() throws IOException, ParseException {
    String code = "<!DOCTYPE name PUBLIC '";
    HTMLParser htmlParser = new HTMLParser();
    ParseException parseException = Assertions.assertThrows(ParseException.class, () -> {
      htmlParser.parse(code);
    });
    Assertions.assertNotNull(parseException);
  }

  @Test
  public void testCharacterBeforeDoctypePublicIdentifierSingleQuoted() throws IOException, ParseException {
    String code = "<!DOCTYPE name PUBLIC 'a";
    HTMLParser htmlParser = new HTMLParser();
    ParseException parseException = Assertions.assertThrows(ParseException.class, () -> {
      htmlParser.parse(code);
    });
    Assertions.assertNotNull(parseException);
  }

  @Test
  public void testSpaceAfterDoctypePublicIdentifier() throws IOException, ParseException {
    String code = "<!DOCTYPE name PUBLIC 'name' ";
    HTMLParser htmlParser = new HTMLParser();
    ParseException parseException = Assertions.assertThrows(ParseException.class, () -> {
      htmlParser.parse(code);
    });
    Assertions.assertNotNull(parseException);
  }

  @Test
  public void testQuotationMarkAfterDoctypePublicIdentifier() throws IOException, ParseException {
    String code = "<!DOCTYPE name PUBLIC 'name'\">";
    HTMLParser htmlParser = new HTMLParser();
    ParseException parseException = Assertions.assertThrows(ParseException.class, () -> {
      htmlParser.parse(code);
    });
    Assertions.assertNotNull(parseException);
  }

  @Test
  public void testApostropheAfterDoctypePublicIdentifier() throws IOException, ParseException {
    String code = "<!DOCTYPE name PUBLIC 'name''";
    HTMLParser htmlParser = new HTMLParser();
    ParseException parseException = Assertions.assertThrows(ParseException.class, () -> {
      htmlParser.parse(code);
    });
    Assertions.assertNotNull(parseException);
  }

  @Test
  public void testEOFAfterDoctypePublicIdentifier() throws IOException, ParseException {
    String code = "<!DOCTYPE name PUBLIC 'name'";
    HTMLParser htmlParser = new HTMLParser();
    ParseException parseException = Assertions.assertThrows(ParseException.class, () -> {
      htmlParser.parse(code);
    });
    Assertions.assertNotNull(parseException);
  }

  @Test
  public void testCharacterAfterDoctypePublicIdentifier() throws IOException, ParseException {
    String code = "<!DOCTYPE name PUBLIC 'name'a";
    HTMLParser htmlParser = new HTMLParser();
    ParseException parseException = Assertions.assertThrows(ParseException.class, () -> {
      htmlParser.parse(code);
    });
    Assertions.assertNotNull(parseException);
  }

  @Test
  public void testSpaceBetweenDoctypePublicAndSystemIdentifiers() throws IOException, ParseException {
    String code = "<!DOCTYPE name PUBLIC 'name'  ";
    HTMLParser htmlParser = new HTMLParser();
    ParseException parseException = Assertions.assertThrows(ParseException.class, () -> {
      htmlParser.parse(code);
    });
    Assertions.assertNotNull(parseException);
  }

  @Test
  public void testGreaterThanSignBetweenDoctypePublicAndSystemIdentifiers() throws IOException, ParseException {
    String code = "<!DOCTYPE name PUBLIC 'name' >";
    HTMLParser htmlParser = new HTMLParser();
    ParseException parseException = Assertions.assertThrows(ParseException.class, () -> {
      htmlParser.parse(code);
    });
    Assertions.assertNotNull(parseException);
  }

  @Test
  public void testQuotationMarkBetweenDoctypePublicAndSystemIdentifiers() throws IOException, ParseException {
    String code = "<!DOCTYPE name PUBLIC 'name' \"";
    HTMLParser htmlParser = new HTMLParser();
    ParseException parseException = Assertions.assertThrows(ParseException.class, () -> {
      htmlParser.parse(code);
    });
    Assertions.assertNotNull(parseException);
  }

  @Test
  public void testApostropheBetweenDoctypePublicAndSystemIdentifiers() throws IOException, ParseException {
    String code = "<!DOCTYPE name PUBLIC 'name' '";
    HTMLParser htmlParser = new HTMLParser();
    ParseException parseException = Assertions.assertThrows(ParseException.class, () -> {
      htmlParser.parse(code);
    });
    Assertions.assertNotNull(parseException);
  }

  @Test
  public void testMissinQuoteBeforeDoctypeSystemIdentifier() throws IOException, ParseException {
    String code = "<!DOCTYPE name PUBLIC 'name' a";
    HTMLParser htmlParser = new HTMLParser();
    ParseException parseException = Assertions.assertThrows(ParseException.class, () -> {
      htmlParser.parse(code);
    });
    Assertions.assertNotNull(parseException);
  }

  @Test
  public void testSpaceAfterDoctypeSystemKeyword() throws IOException, ParseException {
    String code = "<!DOCTYPE name SYSTEM  ";
    HTMLParser htmlParser = new HTMLParser();
    ParseException parseException = Assertions.assertThrows(ParseException.class, () -> {
      htmlParser.parse(code);
    });
    Assertions.assertNotNull(parseException);
  }

  @Test
  public void testQuotationMarkAfterDoctypeSystemKeyword() throws IOException, ParseException {
    String code = "<!DOCTYPE name SYSTEM \"";
    HTMLParser htmlParser = new HTMLParser();
    ParseException parseException = Assertions.assertThrows(ParseException.class, () -> {
      htmlParser.parse(code);
    });
    Assertions.assertNotNull(parseException);
  }

  @Test
  public void testApostropheAfterDoctypeSystemKeyword() throws IOException, ParseException {
    String code = "<!DOCTYPE name SYSTEM '";
    HTMLParser htmlParser = new HTMLParser();
    ParseException parseException = Assertions.assertThrows(ParseException.class, () -> {
      htmlParser.parse(code);
    });
    Assertions.assertNotNull(parseException);
  }

  @Test
  public void testGreaterThanSignAfterDoctypeSystemKeyword() throws IOException, ParseException {
    String code = "<!DOCTYPE name SYSTEM >";
    HTMLParser htmlParser = new HTMLParser();
    ParseException parseException = Assertions.assertThrows(ParseException.class, () -> {
      htmlParser.parse(code);
    });
    Assertions.assertNotNull(parseException);
  }

  @Test
  public void testMissingQuoteAfterDoctypeSystemIdentifier() throws IOException, ParseException {
    String code = "<!DOCTYPE name SYSTEM a";
    HTMLParser htmlParser = new HTMLParser();
    ParseException parseException = Assertions.assertThrows(ParseException.class, () -> {
      htmlParser.parse(code);
    });
    Assertions.assertNotNull(parseException);
  }

  @Test
  public void testMissingQuoteBeforeDoctypeSystemIdentifierAfterDoctypePublicKeyword() throws IOException, ParseException {
    String code = "<!DOCTYPE name SYSTEM-";
    HTMLParser htmlParser = new HTMLParser();
    ParseException parseException = Assertions.assertThrows(ParseException.class, () -> {
      htmlParser.parse(code);
    });
    Assertions.assertNotNull(parseException);
  }

  @Test
  public void testSpaceBeforeDoctypeSystemIdentifier() throws IOException, ParseException {
    String code = "<!DOCTYPE name SYSTEM   ";
    HTMLParser htmlParser = new HTMLParser();
    ParseException parseException = Assertions.assertThrows(ParseException.class, () -> {
      htmlParser.parse(code);
    });
    Assertions.assertNotNull(parseException);
  }

  @Test
  public void testQuotationMarkBeforeDoctypeSystemIdentifier() throws IOException, ParseException {
    String code = "<!DOCTYPE name SYSTEM  \">";
    HTMLParser htmlParser = new HTMLParser();
    ParseException parseException = Assertions.assertThrows(ParseException.class, () -> {
      htmlParser.parse(code);
    });
    Assertions.assertNotNull(parseException);
  }

  @Test
  public void testApostropheBeforeDoctypeSystemIdentifier() throws IOException, ParseException {
    String code = "<!DOCTYPE name SYSTEM  " + HTMLParser.APOSTROPHE + ">";
    HTMLParser htmlParser = new HTMLParser();
    ParseException parseException = Assertions.assertThrows(ParseException.class, () -> {
      htmlParser.parse(code);
    });
    Assertions.assertNotNull(parseException);
  }

  @Test
  public void testGreaterThanSignBeforeDoctypeSystemIdentifier() throws IOException, ParseException {
    String code = "<!DOCTYPE name SYSTEM  >";
    HTMLParser htmlParser = new HTMLParser();
    ParseException parseException = Assertions.assertThrows(ParseException.class, () -> {
      htmlParser.parse(code);
    });
    Assertions.assertNotNull(parseException);
  }

  @Test
  public void testMissingQuoteBeforeDoctypeSystemIdentifier() throws IOException, ParseException {
    String code = "<!DOCTYPE name SYSTEM  -";
    HTMLParser htmlParser = new HTMLParser();
    ParseException parseException = Assertions.assertThrows(ParseException.class, () -> {
      htmlParser.parse(code);
    });
    Assertions.assertNotNull(parseException);
  }

  @Test
  public void testQuotationMarkBeforeDoctypeSystemIdentifierDoubleQuoted() throws IOException, ParseException {
    String code = "<!DOCTYPE name SYSTEM  \"\">";
    HTMLParser htmlParser = new HTMLParser();
    htmlParser.parse(code);
  }

  @Test
  public void testNullBeforeDoctypeSystemIdentifierDoubleQuoted() throws IOException, ParseException {
    String code = "<!DOCTYPE name SYSTEM \"" + HTMLParser.NULL + ">";
    HTMLParser htmlParser = new HTMLParser();
    ParseException parseException = Assertions.assertThrows(ParseException.class, () -> {
      htmlParser.parse(code);
    });
    Assertions.assertNotNull(parseException);
  }

  @Test
  public void testEOFBeforeDoctypeSystemIdentifierDoubleQuoted() throws IOException, ParseException {
    String code = "<!DOCTYPE name SYSTEM \"";
    HTMLParser htmlParser = new HTMLParser();
    ParseException parseException = Assertions.assertThrows(ParseException.class, () -> {
      htmlParser.parse(code);
    });
    Assertions.assertNotNull(parseException);
  }

  @Test
  public void testCharacterBeforeDoctypeSystemIdentifierDoubleQuoted() throws IOException, ParseException {
    String code = "<!DOCTYPE name SYSTEM \"a";
    HTMLParser htmlParser = new HTMLParser();
    ParseException parseException = Assertions.assertThrows(ParseException.class, () -> {
      htmlParser.parse(code);
    });
    Assertions.assertNotNull(parseException);
  }

  @Test
  public void testApostropheMarkBeforeDoctypeSystemIdentifierSingleQuoted() throws IOException, ParseException {
    String code = "<!DOCTYPE name SYSTEM ''>";
    HTMLParser htmlParser = new HTMLParser();
    ParseException parseException = Assertions.assertThrows(ParseException.class, () -> {
      htmlParser.parse(code);
    });
    Assertions.assertNotNull(parseException);
  }

  @Test
  public void testNullBeforeDoctypeSystemIdentifierSingleQuoted() throws IOException, ParseException {
    String code = "<!DOCTYPE name SYSTEM '" + HTMLParser.NULL + ">";
    HTMLParser htmlParser = new HTMLParser();
    ParseException parseException = Assertions.assertThrows(ParseException.class, () -> {
      htmlParser.parse(code);
    });
    Assertions.assertNotNull(parseException);
  }

  @Test
  public void testEOFBeforeDoctypeSystemIdentifierSingleQuoted() throws IOException, ParseException {
    String code = "<!DOCTYPE name SYSTEM '";
    HTMLParser htmlParser = new HTMLParser();
    ParseException parseException = Assertions.assertThrows(ParseException.class, () -> {
      htmlParser.parse(code);
    });
    Assertions.assertNotNull(parseException);
  }

  @Test
  public void testCharacterBeforeDoctypeSystemIdentifierSingleQuoted() throws IOException, ParseException {
    String code = "<!DOCTYPE name SYSTEM 'a";
    HTMLParser htmlParser = new HTMLParser();
    ParseException parseException = Assertions.assertThrows(ParseException.class, () -> {
      htmlParser.parse(code);
    });
    Assertions.assertNotNull(parseException);
  }

  @Test
  public void testSpaceAfterDoctypeSystemIdentifier() throws IOException, ParseException {
    String code = "<!DOCTYPE name SYSTEM 'name' ";
    HTMLParser htmlParser = new HTMLParser();
    ParseException parseException = Assertions.assertThrows(ParseException.class, () -> {
      htmlParser.parse(code);
    });
    Assertions.assertNotNull(parseException);
  }

  @Test
  public void testQuotationMarkAfterDoctypeSystemIdentifier() throws IOException, ParseException {
    String code = "<!DOCTYPE name SYSTEM 'name'\">";
    HTMLParser htmlParser = new HTMLParser();
    ParseException parseException = Assertions.assertThrows(ParseException.class, () -> {
      htmlParser.parse(code);
    });
    Assertions.assertNotNull(parseException);
  }

  @Test
  public void testApostropheAfterDoctypeSystemIdentifier() throws IOException, ParseException {
    String code = "<!DOCTYPE name SYSTEM 'name''";
    HTMLParser htmlParser = new HTMLParser();
    ParseException parseException = Assertions.assertThrows(ParseException.class, () -> {
      htmlParser.parse(code);
    });
    Assertions.assertNotNull(parseException);
  }

  @Test
  public void testEOFAfterDoctypeSystemIdentifier() throws IOException, ParseException {
    String code = "<!DOCTYPE name SYSTEM 'name'";
    HTMLParser htmlParser = new HTMLParser();
    ParseException parseException = Assertions.assertThrows(ParseException.class, () -> {
      htmlParser.parse(code);
    });
    Assertions.assertNotNull(parseException);
  }

  @Test
  public void testCharacterAfterDoctypeSystemIdentifier() throws IOException, ParseException {
    String code = "<!DOCTYPE name SYSTEM 'name'a";
    HTMLParser htmlParser = new HTMLParser();
    ParseException parseException = Assertions.assertThrows(ParseException.class, () -> {
      htmlParser.parse(code);
    });
    Assertions.assertNotNull(parseException);
  }

  @Test
  public void testUnexpectedNullCharacterInBogusDoctype() throws IOException, ParseException {
    String code = "<!DOCTYPE name SYSTEM a" + HTMLParser.NULL;
    HTMLParser htmlParser = new HTMLParser();
    ParseException parseException = Assertions.assertThrows(ParseException.class, () -> {
      htmlParser.parse(code);
    });
    Assertions.assertNotNull(parseException);
  }

}
