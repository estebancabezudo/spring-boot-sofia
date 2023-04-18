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
package net.cabezudo.html.tokens;

import net.cabezudo.html.nodes.FilePosition;

/**
 * @author <a href="http://cabezudo.net">Esteban Cabezudo</a>
 * @version 0.9, 10/01/2014
 */
public abstract class Token {

  protected static final char REPLACEMENT_CHARACTER = 0Xfffd;

  private final FilePosition position;
  protected String dtdType = "";
  private TokenAttribute attribute;
  private final StringBuilder data;
  private boolean selfClosingFlag = false;
  private final TokenAttributes attributes = new TokenAttributes();
  private final StringBuilder rootElement = new StringBuilder();
  private String identifiers = "";

  public void appendCharacterToData(char c) {
    data.append(c);
  }

  public void appendReplacementCharacterToData() {
    appendCharacterToData(REPLACEMENT_CHARACTER);
  }

  Token(FilePosition position) {
    this.position = position;
    this.data = new StringBuilder();
  }

  public FilePosition getPosition() {
    return position;
  }

  public TokenAttributes getAttributes() {
    return attributes;
  }

  public String getData() {
    return data.toString();
  }

  public String getRootElement() {
    return rootElement.toString();
  }

  public String getIdentifiers() {
    return identifiers;
  }

  public void appendCharacterToRootElement(char c) {
    rootElement.append(c);
  }

  public String getDTDType() {
    return dtdType;
  }

  public void startNewAttibute(FilePosition position) {
    attribute = new TokenAttribute(position);
    attributes.add(attribute);
  }

  public void appendCharacterToAttributeName(char c) {
    attribute.appendCharacterToName(c);
  }

  public void appendCharacterToAttributeValue(char c) {
    attribute.appendCharacterToValue(c);
  }

  public void appendReplacementCharacterToAttributeValue() {
    appendCharacterToAttributeValue(REPLACEMENT_CHARACTER);
  }

  public void setSelfClosingFlag() {
    selfClosingFlag = true;
  }

  public Boolean isSelfClosed() {
    return selfClosingFlag;
  }

  public void setPublicdDTDType() {
    dtdType = "PUBLIC";
  }

  public void setSystemDTDType() {
    dtdType = "SYSTEM";
  }

  public void addtCharacterToIdentifier(String s) {
    identifiers += s;
  }

  public void addtCharacterToIdentifier(char c) {
    identifiers += c;
  }

  public void appendCharacterToSystemIdentifier(char c) {
    identifiers += c;
  }
}
