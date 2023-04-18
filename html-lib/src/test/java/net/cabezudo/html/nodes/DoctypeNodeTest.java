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

import net.cabezudo.html.HTMLParser;
import net.cabezudo.html.InvalidOperationException;
import net.cabezudo.html.ParseException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author <a href="http://cabezudo.net">Esteban Cabezudo</a>
 * @version 0.01.00, 2022.09.23
 */
public class DoctypeNodeTest {

  @Test
  public void testManualCreation() throws IOException, ParseException {
    DoctypeNode doctype = new DoctypeNode(null);
    Assertions.assertEquals("<!DOCTYPE>", doctype.toHTML());

    doctype = new DoctypeNode("html");
    Assertions.assertEquals("<!DOCTYPE html>", doctype.toHTML());

    doctype = new DoctypeNode("html", new FilePosition(Paths.get("file"), 1, 3));
    Assertions.assertEquals("<!DOCTYPE html>", doctype.toHTML());

    doctype = new DoctypeNode("html", "PUBLIC", new FilePosition(Paths.get("file"), 1, 3));
    Assertions.assertEquals("<!DOCTYPE html PUBLIC>", doctype.toHTML());
    Assertions.assertEquals("<!DOCTYPE html PUBLIC>", doctype.toString());

    doctype = new DoctypeNode("html", "PUBLIC", "\"-//W3C//DTD XHTML 1.0 Transitional//EN\"", new FilePosition(Paths.get("file"), 1, 3));
    Assertions.assertEquals("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\">", doctype.toHTML());
    Assertions.assertEquals("", doctype.getInnerText());
    Assertions.assertFalse(doctype.isEmpty());
    InvalidOperationException invalidOperationException = Assertions.assertThrows(InvalidOperationException.class, () -> {
      DoctypeNode doctypeForException = new DoctypeNode("html");
      doctypeForException.appendChild(null);
    });
    Assertions.assertNotNull(invalidOperationException);
    Assertions.assertFalse(doctype.hasNodeElements());
  }

  @Test
  public void testDoctypeWithoutRootDocument() throws IOException, ParseException {
    String code = "<!DOCTYPE>";
    HTMLParser htmlParser = new HTMLParser();
    ParseException parseException = Assertions.assertThrows(ParseException.class, () -> {
      htmlParser.parse(code);
    });
    Assertions.assertNotNull(parseException);
  }

  @Test
  public void testDoctypeWithRootDocument() throws IOException, ParseException {
    String code = "<!DOCTYPE html>";
    HTMLParser htmlParser = new HTMLParser();
    DoctypeNode node = (DoctypeNode) htmlParser.parse(code).getChild(0);
    Assertions.assertTrue(node instanceof DoctypeNode);
  }

  @Test
  public void testDoctypeWithDTDTypePublic() throws IOException, ParseException {
    String code = "<!DOCTYPE html PUBLIC  \"-//W3C//DTD XHTML 1.0 Transitional//EN\">";
    HTMLParser htmlParser = new HTMLParser();
    DoctypeNode node;
    node = (DoctypeNode) htmlParser.parse(code).getChild(0);
    Assertions.assertTrue(node instanceof DoctypeNode);
    Assertions.assertEquals("html", node.getRootElement());
    Assertions.assertEquals("PUBLIC", node.getDTDType());
    Assertions.assertEquals("-//W3C//DTD XHTML 1.0 Transitional//EN", node.getIdentifiers());
  }

  @Test
  public void testDoctypeWithDTDTypeSystem() throws IOException, ParseException {
    String code = "<!DOCTYPE html SYSTEM  \"-//W3C//DTD XHTML 1.0 Transitional//EN\">";
    HTMLParser htmlParser = new HTMLParser();
    try {
      DoctypeNode node = (DoctypeNode) htmlParser.parse(code).getChild(0);
      Assertions.assertTrue(node instanceof DoctypeNode);
      Assertions.assertEquals("html", node.getRootElement());
      Assertions.assertEquals("SYSTEM", node.getDTDType());
      Assertions.assertEquals("-//W3C//DTD XHTML 1.0 Transitional//EN", node.getIdentifiers());
    } catch (ParseException ex) {
      Logger.getLogger(DoctypeNodeTest.class.getName()).log(Level.SEVERE, null, ex);
    }
  }
}
