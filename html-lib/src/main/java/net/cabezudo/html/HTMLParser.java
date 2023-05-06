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

import net.cabezudo.html.nodes.Document;
import net.cabezudo.html.nodes.FilePosition;
import net.cabezudo.html.nodes.NodeFactory;
import net.cabezudo.html.tokens.BogusCommentToken;
import net.cabezudo.html.tokens.CommentToken;
import net.cabezudo.html.tokens.DoctypeToken;
import net.cabezudo.html.tokens.EndTagToken;
import net.cabezudo.html.tokens.StartTagToken;
import net.cabezudo.html.tokens.Token;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;

/**
 * @author <a href="http://cabezudo.net">Esteban Cabezudo</a>
 * @version 0.01.00, 2022.02.26
 */
public class HTMLParser {

  public static final char AMPERSAND = 0x0026;
  public static final char APPLICATION_PROGRAM_COMMAND = 0x009F;
  public static final char APOSTROPHE = 0x0027;
  public static final char CHARACTER_TABULATION = 0x0009;
  public static final char CARRIAGE_RETURN = 0x000d;
  public static final char DELETE = 0x007F;
  public static final char EQUALS_SIGN = 0x003d;
  public static final char EOF = 0x0004;
  public static final char EXCLAMATION_MARK = 0x0021;
  public static final char FORM_FEED = 0x000c;
  public static final char GRAVE_ACCENT = 0x0060;
  public static final char GREATER_THAN_SIGN = 0x003e;
  public static final char HYPHEN_MINUS = 0x002d;
  public static final char INFORMATION_SEPARATOR_ONE = 0x001F;
  public static final char LATIN_CAPITAL_LETTER_X = 0x0058;
  public static final char LATIN_SMALL_LETTER_X = 0x0078;
  public static final char LESS_THAN_SIGN = 0x003c;
  public static final char LINE_FEED = 0x000a;
  public static final char LOWERCASE_A = 0x0061;
  public static final char LOWERCASE_F = 0x0066;
  public static final char NINE = 0x0039;
  public static final char NULL = 0x0000;
  public static final char NUMBER_SIGN = 0x0023;
  public static final char QUESTION_MARK = 0x003F;
  public static final char QUOTATION_MARK = 0x0022;
  public static final char REPLACEMENT_CHARACTER = 0xfffd;
  public static final char RIGHT_SQUARE_BRACKET = 0x005d;
  public static final char SEMICOLON = 0x003b;
  public static final char SOLIDUS = 0x002f;
  public static final char SPACE = 0x0020;
  public static final char UPPERCASE_A = 0x0041;
  public static final char UPPERCASE_F = 0x0046;
  public static final char ZERO = 0x0030;

  public static final char EURO_SIGN = 0x20AC;
  public static final char SINGLE_LOW_9_QUOTATION_MARK = 0x201A;
  public static final char LATIN_SMALL_LETTER_F_WITH_HOOK = 0x0192;
  public static final char DOUBLE_LOW_9_QUOTATION_MARK = 0x201E;
  public static final char HORIZONTAL_ELLIPSIS = 0x2026;
  public static final char DAGGER = 0x2020;
  public static final char DOUBLE_DAGGER = 0x2021;
  public static final char MODIFIER_LETTER_CIRCUMFLEX_ACCENT = 0x02C6;
  public static final char PER_MILLE_SIGN = 0x2030;
  public static final char LATIN_CAPITAL_LETTER_S_WITH_CARON = 0x0160;
  public static final char SINGLE_LEFT_POINTING_ANGLE_QUOTATION_MARK = 0x2039;
  public static final char LATIN_CAPITAL_LIGATURE_OE = 0x0152;
  public static final char LATIN_CAPITAL_LETTER_Z_WITH_CARON = 0x017D;
  public static final char LEFT_SINGLE_QUOTATION_MARK = 0x2018;
  public static final char RIGHT_SINGLE_QUOTATION_MARK = 0x2019;
  public static final char LEFT_DOUBLE_QUOTATION_MARK = 0x201C;
  public static final char RIGHT_DOUBLE_QUOTATION_MARK = 0x201D;
  public static final char BULLET = 0x2022;
  public static final char EN_DASH = 0x2013;
  public static final char EM_DASH = 0x2014;
  public static final char SMALL_TILDE = 0x02DC;
  public static final char TRADE_MARK_SIGN = 0x2122;
  public static final char LATIN_SMALL_LETTER_S_WITH_CARON = 0x0161;
  public static final char SINGLE_RIGHT_POINTING_ANGLE_QUOTATION_MARK = 0x203A;
  public static final char LATIN_SMALL_LIGATURE_OE = 0x0153;
  public static final char LATIN_SMALL_LETTER_Z_WITH_CARON = 0x017E;
  public static final char LATIN_CAPITAL_LETTER_Y_WITH_DIAERESIS = 0x0178;
  static final String EMPTY_STRING = "";
  private static final Logger log = LoggerFactory.getLogger(HTMLParser.class);
  private final StackOfOpenElements stackOfOpenElements = new StackOfOpenElements();
  private final ParseErrors parseErrors = new ParseErrors();
  private boolean endParse = false;
  private boolean reconsume = false;
  private Token currentToken = null;
  private State state = State.DATA;
  private int i = 0;
  private Path origin;
  private int line = 1;
  private int row = 1;
  private char[] chars;
  private char charConsumed;
  private char lastCharacter;
  private FilePosition position;

  private void afterDoctypeNameStatus() {
    switch (charConsumed) {
      case CHARACTER_TABULATION:
      case LINE_FEED:
      case FORM_FEED:
      case SPACE:
        break;
      case GREATER_THAN_SIGN:
        state = State.DATA;
        emitCurrentToken();
        break;
      case EOF:
        addParseError("EOF in doctype parse error");
        emitCurrentToken();
        emitEOFToken();
        break;
      default:
        String sixCharacters = stringElement(charConsumed, 5);
        if ("PUBLIC".equalsIgnoreCase(sixCharacters)) {
          consume(5);
          currentToken.setPublicdDTDType();
          state = State.AFTER_DOCTYPE_PUBLIC_KEYWORD;
          break;
        }
        if ("SYSTEM".equalsIgnoreCase(sixCharacters)) {
          consume(6);
          currentToken.setSystemDTDType();
          state = State.AFTER_DOCTYPE_SYSTEM_KEYWORD;
          break;
        }
        addParseError("Invalid character sequence after doctype name parse error");
        state = State.BOGUS_DOCTYPE;
        reconsume = true;
        break;
    }
  }

  private void afterDoctypePublicIdentifierStatus() {
    switch (charConsumed) {
      case CHARACTER_TABULATION:
      case LINE_FEED:
      case FORM_FEED:
      case SPACE:
        state = State.BETWEEN_DOCTYPE_PUBLIC_AND_SYSTEM_IDENTIFIERS;
        break;
      case GREATER_THAN_SIGN:
        state = State.DATA;
        emitCurrentToken();
        break;
      case QUOTATION_MARK:
        addParseError("Missing whitespace between doctype public and system identifiers parse error");
        currentToken.addtCharacterToIdentifier(EMPTY_STRING);
        state = State.DOCTYPE_SYSTEM_IDENTIFIER_DOUBLE_QUOTED;
        break;
      case APOSTROPHE:
        addParseError("Missing whitespace between doctype public and system identifiers parse error");
        currentToken.addtCharacterToIdentifier(EMPTY_STRING);
        state = State.DOCTYPE_SYSTEM_IDENTIFIER_SINGLE_QUOTED;
        break;
      case EOF:
        addParseError("EOF in doctype parse error");
        emitCurrentToken();
        emitEOFToken();
        break;
      default:
        addParseError("Missing quote before doctype system identifier parse error");
        state = State.BOGUS_DOCTYPE;
        reconsume = true;
        break;
    }
  }

  private void afterDoctypePublicKeywordStatus() {
    switch (charConsumed) {
      case CHARACTER_TABULATION:
      case LINE_FEED:
      case FORM_FEED:
      case SPACE:
        state = State.BEFORE_DOCTYPE_PUBLIC_IDENTIFIER;
        break;
      case QUOTATION_MARK:
        addParseError("Missing whitespace after doctype public keyword parse error");
        currentToken.addtCharacterToIdentifier(EMPTY_STRING);
        state = State.DOCTYPE_PUBLIC_IDENTIFIER_DOUBLE_QUOTED;
        break;
      case APOSTROPHE:
        addParseError("Missing whitespace after doctype public keyword parse error");
        currentToken.addtCharacterToIdentifier(EMPTY_STRING);
        state = State.DOCTYPE_PUBLIC_IDENTIFIER_SINGLE_QUOTED;
        break;
      case GREATER_THAN_SIGN:
        addParseError("Missing doctype public identifier parse error");
        state = State.DATA;
        emitCurrentToken();
        break;
      case EOF:
        addParseError("EOF in doctype parse error");
        emitCurrentToken();
        emitEOFToken();
        break;
      default:
        addParseError("Missing quote before doctype public identifier parse error");
        state = State.BOGUS_DOCTYPE;
        reconsume = true;
        break;
    }
  }

  private void afterDoctypeSystemIdentifierStatus() {
    switch (charConsumed) {
      case CHARACTER_TABULATION:
      case LINE_FEED:
      case FORM_FEED:
      case SPACE:
        break;
      case GREATER_THAN_SIGN:
        state = State.DATA;
        emitCurrentToken();
        break;
      case EOF:
        addParseError("EOF in doctype parse error");
        emitCurrentToken();
        emitEOFToken();
        break;
      default:
        addParseError("Unexpected character after doctype system identifier parse error");
        state = State.BOGUS_DOCTYPE;
        reconsume = true;
        break;
    }
  }

  private void afterDoctypeSystemKeywordStatus() {
    switch (charConsumed) {
      case CHARACTER_TABULATION:
      case LINE_FEED:
      case FORM_FEED:
      case SPACE:
        state = State.BEFORE_DOCTYPE_SYSTEM_IDENTIFIER;
        break;
      case QUOTATION_MARK:
        addParseError("Missing whitespace after doctype system keyword parse error");
        state = State.DOCTYPE_SYSTEM_IDENTIFIER_DOUBLE_QUOTED;
        break;
      case APOSTROPHE:
        addParseError("Missing whitespace after doctype system keyword parse error");
        state = State.DOCTYPE_SYSTEM_IDENTIFIER_SINGLE_QUOTED;
        break;
      case GREATER_THAN_SIGN:
        addParseError("Missing doctype system identifier parse error");
        state = State.DATA;
        emitCurrentToken();
        break;
      case EOF:
        addParseError("EOF in doctype parse error");
        emitCurrentToken();
        emitEOFToken();
        break;
      default:
        addParseError("Missing quote before doctype system identifier parse error");
        state = State.BOGUS_DOCTYPE;
        reconsume = true;
        break;
    }
  }

  private void beforeDoctypeNameStatus() {
    switch (charConsumed) {
      case CHARACTER_TABULATION:
      case LINE_FEED:
      case FORM_FEED:
      case SPACE:
        break;
      case NULL:
        addParseError("Unexpected null character parse error.");
        currentToken = new DoctypeToken(new FilePosition(origin, line, row));
        currentToken.appendCharacterToRootElement(REPLACEMENT_CHARACTER);
        state = State.DOCTYPE_NAME;
        break;
      case GREATER_THAN_SIGN:
        addParseError("Missing doctype name parse error.");
        currentToken = new DoctypeToken(new FilePosition(origin, line, row));
        state = State.DATA;
        emitCurrentToken();
        break;
      case EOF:
        addParseError("EOF in doctype parse error.");
        currentToken = new DoctypeToken(new FilePosition(origin, line, row));
        state = State.DATA;
        emitCurrentToken();
        emitEOFToken();
        break;
      default: // Here is included the ASCII upper alpha
        currentToken = new DoctypeToken(new FilePosition(origin, line, row));
        currentToken.appendCharacterToRootElement(Character.toLowerCase(charConsumed));
        state = State.DOCTYPE_NAME;
        break;
    }
  }

  private void beforeDoctypePublicIdentifierStatus() {
    switch (charConsumed) {
      case CHARACTER_TABULATION:
      case LINE_FEED:
      case FORM_FEED:
      case SPACE:
        break;
      case QUOTATION_MARK:
        currentToken.addtCharacterToIdentifier(EMPTY_STRING);
        state = State.DOCTYPE_PUBLIC_IDENTIFIER_DOUBLE_QUOTED;
        break;
      case APOSTROPHE:
        addParseError("Missing whitespace after doctype public keyword parse error");
        currentToken.addtCharacterToIdentifier(EMPTY_STRING);
        state = State.DOCTYPE_PUBLIC_IDENTIFIER_SINGLE_QUOTED;
        break;
      case GREATER_THAN_SIGN:
        addParseError("Missing doctype public identifier parse error");
        state = State.DATA;
        emitCurrentToken();
        break;
      case EOF:
        addParseError("EOF in doctype parse error");
        emitCurrentToken();
        emitEOFToken();
        break;
      default:
        addParseError("Missing quote before doctype public identifier parse error");
        state = State.BOGUS_DOCTYPE;
        reconsume = true;
        break;
    }
  }

  private void beforeDoctypeSystemIdentifierStatus() {
    switch (charConsumed) {
      case CHARACTER_TABULATION:
      case LINE_FEED:
      case FORM_FEED:
      case SPACE:
        break;
      case QUOTATION_MARK:
        state = State.DOCTYPE_SYSTEM_IDENTIFIER_DOUBLE_QUOTED;
        break;
      case APOSTROPHE:
        state = State.DOCTYPE_SYSTEM_IDENTIFIER_SINGLE_QUOTED;
        break;
      case GREATER_THAN_SIGN:
        addParseError("Missing doctype system identifier parse error");
        state = State.DATA;
        emitCurrentToken();
        break;
      case EOF:
        addParseError("EOF in doctype parse error");
        emitCurrentToken();
        emitEOFToken();
        break;
      default:
        addParseError("Missing quote before doctype system identifier parse error");
        state = State.BOGUS_DOCTYPE;
        reconsume = true;
        break;
    }
  }

  private void betweenDoctypePublicAndSystemIdentifiersStatus() {
    switch (charConsumed) {
      case CHARACTER_TABULATION:
      case LINE_FEED:
      case FORM_FEED:
      case SPACE:
        break;
      case GREATER_THAN_SIGN:
        state = State.DATA;
        emitCurrentToken();
        break;
      case QUOTATION_MARK:
        addParseError("Missing whitespace between doctype public and system identifiers parse error");
        currentToken.addtCharacterToIdentifier(EMPTY_STRING);
        state = State.DOCTYPE_SYSTEM_IDENTIFIER_DOUBLE_QUOTED;
        break;
      case APOSTROPHE:
        addParseError("Missing whitespace between doctype public and system identifiers parse error");
        currentToken.addtCharacterToIdentifier(EMPTY_STRING);
        state = State.DOCTYPE_SYSTEM_IDENTIFIER_SINGLE_QUOTED;
        break;
      case EOF:
        addParseError("EOF in doctype parse error");
        emitCurrentToken();
        emitEOFToken();
        break;
      default:
        addParseError("Missing quote before doctype system identifier parse error");
        state = State.BOGUS_DOCTYPE;
        reconsume = true;
        break;
    }
  }

  private void bogusCommentStatus() {
    switch (charConsumed) {
      case GREATER_THAN_SIGN:
        state = State.DATA;
        emitCurrentToken();
        break;
      case EOF:
        emitCurrentToken();
        emitEOFToken();
        break;
      case NULL:
        addParseError("Unexpected null character parse error");
        currentToken.appendReplacementCharacterToData();
        break;
      default:
        currentToken.appendCharacterToData(charConsumed);
        break;
    }
  }

  private void bogusDoctypeStatus() {
    switch (charConsumed) {
      case GREATER_THAN_SIGN:
        state = State.DATA;
        emitCurrentToken();
        break;
      case NULL:
        addParseError("Unexpected null character parse error");
        break;
      case EOF:
        addParseError("EOF in doctype parse error");
        emitCurrentToken();
        emitEOFToken();
        break;
      default:
        break;
    }
  }

  private void commentEndBangStatus() {
    switch (charConsumed) {
      case HYPHEN_MINUS:
        currentToken.appendCharacterToData(HYPHEN_MINUS);
        currentToken.appendCharacterToData(EXCLAMATION_MARK);
        state = State.COMMENT_END_DASH;
        break;
      case GREATER_THAN_SIGN:
        addParseError("Incorrect closed comment parse error");
        state = State.DATA;
        emitCurrentToken();
        break;
      case EOF:
        addParseError("EOF in comment parse error");
        emitCurrentToken();
        emitEOFToken();
        break;
      default:
        currentToken.appendCharacterToData(HYPHEN_MINUS);
        currentToken.appendCharacterToData(EXCLAMATION_MARK);
        state = State.COMMENT;
        reconsume = true;
        break;
    }
  }

  private void commentEndDashStatus() {
    switch (charConsumed) {
      case HYPHEN_MINUS:
        state = State.COMMENT_END;
        break;
      case EOF:
        addParseError("EOF in comment parse error");
        emitCurrentToken();
        emitEOFToken();
        break;
      default:
        currentToken.appendCharacterToData(HYPHEN_MINUS);
        state = State.COMMENT;
        reconsume = true;
        break;
    }
  }

  private void commentEndStatus() {
    switch (charConsumed) {
      case GREATER_THAN_SIGN:
        state = State.DATA;
        emitCurrentToken();
        break;
      case EXCLAMATION_MARK:
        state = State.COMMENT_END_BANG;
        break;
      case HYPHEN_MINUS:
        currentToken.appendCharacterToData(HYPHEN_MINUS);
        break;
      case EOF:
        addParseError("EOF in comment parse error");
        emitCurrentToken();
        emitEOFToken();
        break;
      default:
        currentToken.appendCharacterToData(HYPHEN_MINUS);
        state = State.COMMENT;
        reconsume = true;
        break;
    }
  }

  private void commentLessThanSignBangDashDashStatus() {
    switch (charConsumed) {
      case GREATER_THAN_SIGN:
      case EOF:
        state = State.COMMENT_END;
        reconsume = true;
        break;
      default:
        currentToken.appendCharacterToData(HYPHEN_MINUS);
        state = State.COMMENT;
        reconsume = true;
        break;
    }
  }

  private void commentLessThanSignBangDashStatus() {
    if (charConsumed == HYPHEN_MINUS) {
      state = State.COMMENT_LESS_THAN_SIGN_BANG_DASH_DASH;
    } else {
      state = State.COMMENT_END_DASH;
      reconsume = true;
    }
  }

  private void commentLessThanSignBangStatus() {
    if (charConsumed == HYPHEN_MINUS) {
      state = State.COMMENT_LESS_THAN_SIGN_BANG_DASH;
    } else {
      state = State.COMMENT_END_DASH;
      reconsume = true;
    }
  }

  private void commentLessThanSignStatus() {
    switch (charConsumed) {
      case EXCLAMATION_MARK:
        currentToken.appendCharacterToData(charConsumed);
        state = State.COMMENT_LESS_THAN_SIGN_BANG;
        break;
      case LESS_THAN_SIGN:
        currentToken.appendCharacterToData(charConsumed);
        break;
      default:
        state = State.COMMENT;
        reconsume = true;
        break;
    }
  }

  private void commentStartStatus() {
    switch (charConsumed) {
      case HYPHEN_MINUS:
        state = State.COMMENT_START_DASH;
        break;
      case GREATER_THAN_SIGN:
        addParseError("Abrupt closing of empty comment parse error");
        state = State.DATA;
        emitCurrentToken();
        break;
      default:
        state = State.COMMENT;
        reconsume = true;
        break;
    }
  }

  private void commentStartDashStatus() {
    switch (charConsumed) {
      case HYPHEN_MINUS:
        state = State.COMMENT_END;
        break;
      case GREATER_THAN_SIGN:
        addParseError("Abrupt closing of empty comment parse error");
        state = State.DATA;
        emitCurrentToken();
        break;
      case EOF:
        addParseError("EOF in comment parse error");
        emitCurrentToken();
        emitEOFToken();
        break;
      default:
        currentToken.appendCharacterToData(HYPHEN_MINUS);
        state = State.COMMENT;
        reconsume = true;
        break;
    }
  }

  private void commentStatus() {
    switch (charConsumed) {
      case LESS_THAN_SIGN:
        currentToken.appendCharacterToData(charConsumed);
        state = State.COMMENT_LESS_THAN_SIGN;
        break;
      case HYPHEN_MINUS:
        state = State.COMMENT_END_DASH;
        break;
      case NULL:
        addParseError("Unexpected null character parse error");
        currentToken.appendReplacementCharacterToData();
        break;
      case EOF:
        addParseError("EOF in comment parse error");
        emitCurrentToken();
        emitEOFToken();
        break;
      default:
        currentToken.appendCharacterToData(charConsumed);
        break;
    }
  }

  private void dataState() {
    switch (charConsumed) {
      case LESS_THAN_SIGN:
        state = State.TAG_OPEN;
        break;
      case EOF:
        emitEOFToken();
        break;
      default:
        emitCharacterToken(charConsumed);
        break;
    }
  }

  private void doctypeNameStatus() {
    switch (charConsumed) {
      case CHARACTER_TABULATION:
      case LINE_FEED:
      case FORM_FEED:
      case SPACE:
        state = State.AFTER_DOCTYPE_NAME;
        break;
      case GREATER_THAN_SIGN:
        state = State.DATA;
        emitCurrentToken();
        break;
      case NULL:
        addParseError("Unexpected null character parse error");
        currentToken.appendCharacterToRootElement(REPLACEMENT_CHARACTER);
        break;
      case EOF:
        addParseError("EOF in doctype parse error");
        emitCurrentToken();
        emitEOFToken();
        break;
      default:
        currentToken.appendCharacterToRootElement(Character.toLowerCase(charConsumed));
        break;
    }
  }

  private void doctypePublicIdentifierDoubleQuotedStatus() {
    switch (charConsumed) {
      case QUOTATION_MARK:
        state = State.AFTER_DOCTYPE_PUBLIC_IDENTIFIER;
        break;
      case NULL:
        addParseError("Unexpected null character parse error");
        currentToken.addtCharacterToIdentifier(REPLACEMENT_CHARACTER);
        break;
      case GREATER_THAN_SIGN:
        addParseError("Abrupt doctype public identifier parse error");
        state = State.DATA;
        emitCurrentToken();
        break;
      case EOF:
        addParseError("EOF in doctype parse error");
        emitCurrentToken();
        emitEOFToken();
        break;
      default:
        currentToken.addtCharacterToIdentifier(charConsumed);
        break;
    }
  }

  private void doctypePublicIdentifierSingleQuotedStatus() {
    switch (charConsumed) {
      case APOSTROPHE:
        state = State.AFTER_DOCTYPE_PUBLIC_IDENTIFIER;
        break;
      case NULL:
        addParseError("Unexpected null character parse error");
        currentToken.addtCharacterToIdentifier(REPLACEMENT_CHARACTER);
        break;
      case GREATER_THAN_SIGN:
        addParseError("Abrupt doctype public identifier parse error");
        state = State.DATA;
        emitCurrentToken();
        break;
      case EOF:
        addParseError("EOF in doctype parse error");
        emitCurrentToken();
        emitEOFToken();
        break;
      default:
        currentToken.addtCharacterToIdentifier(charConsumed);
        break;
    }
  }

  private void doctypeStatus() {
    switch (charConsumed) {
      case CHARACTER_TABULATION:
      case LINE_FEED:
      case FORM_FEED:
      case SPACE:
        state = State.BEFORE_DOCTYPE_NAME;
        break;
      case EOF:
        addParseError("EOF in doctype parse error");
        currentToken = new DoctypeToken(new FilePosition(origin, line, row));
        emitCurrentToken();
        emitEOFToken();
        break;
      case GREATER_THAN_SIGN:
      default:
        addParseError("Missing shitespace before doctype name parse error");
        state = State.BEFORE_DOCTYPE_NAME;
        reconsume = true;
        break;
    }
  }

  private void doctypeSystemIdentifierDoubleQuotedStatus() {
    switch (charConsumed) {
      case QUOTATION_MARK:
        state = State.AFTER_DOCTYPE_SYSTEM_IDENTIFIER;
        break;
      case NULL:
        addParseError("Unexpected null character parse error");
        currentToken.addtCharacterToIdentifier(REPLACEMENT_CHARACTER);
        break;
      case GREATER_THAN_SIGN:
        addParseError("Abrupt doctype system identifier parse error");
        state = State.DATA;
        emitCurrentToken();
        break;
      case EOF:
        addParseError("EOF in doctype parse error");
        emitCurrentToken();
        emitEOFToken();
        break;
      default:
        currentToken.appendCharacterToSystemIdentifier(charConsumed);
        break;
    }
  }

  private void doctypeSystemIdentifierSingleQuotedStatus() {
    switch (charConsumed) {
      case APOSTROPHE:
        state = State.AFTER_DOCTYPE_SYSTEM_IDENTIFIER;
        break;
      case NULL:
        addParseError("Unexpected null character parse error");
        currentToken.addtCharacterToIdentifier(REPLACEMENT_CHARACTER);
        break;
      case GREATER_THAN_SIGN:
        addParseError("Abrupt doctype system identifier parse error");
        state = State.DATA;
        emitCurrentToken();
        break;
      case EOF:
        addParseError("EOF in doctype parse error");
        emitCurrentToken();
        emitEOFToken();
        break;
      default:
        currentToken.appendCharacterToSystemIdentifier(charConsumed);
        break;
    }
  }

  private void markupDeclarationOpenStatus() {
    char firstElement = element(0);
    if (HYPHEN_MINUS == charConsumed && HYPHEN_MINUS == firstElement) {
      consume();
      currentToken = new CommentToken(new FilePosition(origin, line, row));
      state = State.COMMENT_START;
      return;
    }
    if (stringElement(charConsumed, 6).equalsIgnoreCase("doctype")) {
      consume(6);
      state = State.DOCTYPE;
      return;
    }
    addParseError("Incorrect opened comment parse error");
    currentToken = new CommentToken(new FilePosition(origin, line, row));
    state = State.BOGUS_COMMENT;
  }

  private void tagNameStatus() {
    if (isASCIIAlpha(charConsumed)) {
      currentToken.appendCharacterToRootElement(charConsumed);
      return;
    }
    switch (charConsumed) {
      case LINE_FEED:
      case CHARACTER_TABULATION:
      case FORM_FEED:
      case SPACE:
        state = State.BEFORE_ATTRIBUTE_NAME;
        break;
      case SOLIDUS:
        state = State.SELF_CLOSING_START_TAG;
        break;
      case GREATER_THAN_SIGN:
        state = State.DATA;
        emitCurrentToken();
        break;
      case NULL:
        addParseError("Unexpected null character parse error");
        currentToken.appendCharacterToRootElement(REPLACEMENT_CHARACTER);
        break;
      case EOF:
        addParseError("EOF in tag parse error");
        emitEOFToken();
        break;
      default:
        currentToken.appendCharacterToRootElement(charConsumed);
        break;
    }
  }

  private void tagOpen() {
    if (isASCIIAlpha(charConsumed)) {
      currentToken = new StartTagToken(position);
      reconsume = true;
      state = State.TAG_NAME;
      return;
    }
    switch (charConsumed) {
      case EXCLAMATION_MARK:
        state = State.MARKUP_DECLARATION_OPEN;
        break;
      case SOLIDUS:
        state = State.END_TAG_OPEN;
        break;
      case QUESTION_MARK:
        state = State.BOGUS_COMMENT;
        currentToken = new BogusCommentToken(position);
        reconsume = true;
        break;
      case EOF:
        addParseError("EOF before tag name parse error");
        emitCharacterToken(LESS_THAN_SIGN);
        emitEOFToken();
        break;
      default:
        emitLessThanSignCharacterToken();
        state = State.DATA;
        reconsume = true;
        break;
    }
  }

  private void endTagOpen() {
    if (isASCIIAlpha(charConsumed)) {
      currentToken = new EndTagToken(position);
      reconsume = true;
      state = State.TAG_NAME;
      return;
    }
    switch (charConsumed) {
      case GREATER_THAN_SIGN:
        state = State.DATA;
        break;
      case EOF:
        addParseError("EOF before tag name parse error");
        emitCharacterToken(LESS_THAN_SIGN);
        emitCharacterToken(SOLIDUS);
        emitEOFToken();
        break;
      default:
        addParseError("Invalid first character of tag name parse error");
        currentToken = new CommentToken(position);
        state = State.BOGUS_COMMENT;
        reconsume = true;
        break;
    }
  }

  private void afterAttributeNameStatus() {
    switch (charConsumed) {
      case CHARACTER_TABULATION:
      case LINE_FEED:
      case FORM_FEED:
      case SPACE:
        break;
      case SOLIDUS:
        state = State.SELF_CLOSING_START_TAG;
        break;
      case EQUALS_SIGN:
        state = State.BEFORE_ATTRIBUTE_VALUE;
        break;
      case GREATER_THAN_SIGN:
        state = State.DATA;
        emitCurrentToken();
        break;
      case EOF:
        addParseError("EOF in tag parse error");
        emitEOFToken();
        break;
      default:
        currentToken.startNewAttibute(position);
        state = State.ATTRIBUTE_NAME;
        reconsume = true;
        break;
    }
  }

  private void afterAttributeValueQuotedStatus() {
    switch (charConsumed) {
      case CHARACTER_TABULATION:
      case LINE_FEED:
      case FORM_FEED:
      case SPACE:
        state = State.BEFORE_ATTRIBUTE_NAME;
        break;
      case SOLIDUS:
        state = State.SELF_CLOSING_START_TAG;
        break;
      case GREATER_THAN_SIGN:
        state = State.DATA;
        emitCurrentToken();
        break;
      case EOF:
        addParseError("EOF in tag parse error.");
        emitEOFToken();
        break;
      default:
        addParseError("Missing whitespace between attributes parse error");
        state = State.BEFORE_ATTRIBUTE_NAME;
        reconsume = true;
        break;
    }
  }

  private void attributeNameStatus() {
    switch (charConsumed) {
      case CHARACTER_TABULATION:
      case LINE_FEED:
      case FORM_FEED:
      case SPACE:
      case SOLIDUS:
      case GREATER_THAN_SIGN:
      case EOF:
        state = State.AFTER_ATTRIBUTE_NAME;
        reconsume = true;
        break;
      case EQUALS_SIGN:
        state = State.BEFORE_ATTRIBUTE_VALUE;
        break;
      case NULL:
        addParseError("Unexpected null character parse error");
        currentToken.appendCharacterToAttributeName(REPLACEMENT_CHARACTER);
        break;
      case QUOTATION_MARK:
      case APOSTROPHE:
      case LESS_THAN_SIGN:
        addParseError("Unexpected character in attribute name parse error");
        currentToken.appendCharacterToAttributeName(REPLACEMENT_CHARACTER);
        break;
      default:
        currentToken.appendCharacterToAttributeName(charConsumed);
        break;
    }
  }

  private void attributeValueDobleQuotedStatus() {
    switch (charConsumed) {
      case QUOTATION_MARK:
        state = State.AFTER_ATTRIBUTE_VALUE_QUOTED;
        break;
      case NULL:
        addParseError("Unexpected null character parse error");
        currentToken.appendReplacementCharacterToAttributeValue();
        break;
      case EOF:
        addParseError("EOF in tag parse error.");
        emitEOFToken();
        break;
      default:
        currentToken.appendCharacterToAttributeValue(charConsumed);
        break;
    }
  }

  private void attributeValueSingleQuotedStatus() {
    switch (charConsumed) {
      case APOSTROPHE:
        state = State.AFTER_ATTRIBUTE_VALUE_QUOTED;
        break;
      case NULL:
        addParseError("Unexpected null character parse error");
        currentToken.appendReplacementCharacterToAttributeValue();
        break;
      case EOF:
        addParseError("EOF in tag parse error.");
        emitEOFToken();
        break;
      default:
        currentToken.appendCharacterToAttributeValue(charConsumed);
        break;
    }
  }

  private void attributeValueUnquotedStatus() {
    switch (charConsumed) {
      case CHARACTER_TABULATION:
      case LINE_FEED:
      case FORM_FEED:
      case SPACE:
        state = State.BEFORE_ATTRIBUTE_NAME;
        break;
      case GREATER_THAN_SIGN:
        state = State.DATA;
        emitCurrentToken();
        break;
      case NULL:
        addParseError("Unexpected null character parse error");
        currentToken.appendReplacementCharacterToAttributeValue();
        break;
      case QUOTATION_MARK:
      case APOSTROPHE:
      case LESS_THAN_SIGN:
      case EQUALS_SIGN:
      case GRAVE_ACCENT:
        addParseError("Unexpected character in unquoted attribute value parse error");
        currentToken.appendCharacterToAttributeValue(charConsumed);
        break;
      case EOF:
        addParseError("EOF in tag parse error");
        emitEOFToken();
        break;
      default:
        currentToken.appendCharacterToAttributeValue(charConsumed);
        break;
    }
  }

  private void beforeAttributeNameStatus() {
    switch (charConsumed) {
      case CHARACTER_TABULATION:
      case LINE_FEED:
      case FORM_FEED:
      case SPACE:
        break;
      case SOLIDUS:
      case GREATER_THAN_SIGN:
      case EOF:
        state = State.AFTER_ATTRIBUTE_NAME;
        reconsume = true;
        break;
      case EQUALS_SIGN:
        addParseError("Unexpected equals sign before attribute name parse error");
        currentToken.startNewAttibute(position);
        state = State.ATTRIBUTE_NAME;
        break;
      default:
        currentToken.startNewAttibute(position);
        state = State.ATTRIBUTE_NAME;
        reconsume = true;
        break;
    }
  }

  private void beforeAttributeValueStatus() {
    switch (charConsumed) {
      case CHARACTER_TABULATION:
      case LINE_FEED:
      case FORM_FEED:
      case SPACE:
        break;
      case QUOTATION_MARK:
        state = State.ATTRIBUTE_VALUE_DOUBLE_QUOTED;
        break;
      case APOSTROPHE:
        state = State.ATTRIBUTE_VALUE_SINGLE_QUOTED;
        break;
      case GREATER_THAN_SIGN:
        addParseError("Missing attribute value parse error");
        state = State.DATA;
        emitCurrentToken();
        break;
      default:
        state = State.ATTRIBUTE_VALUE_UNQUOTED;
        reconsume = true;
        break;
    }
  }

  public Document parse(String code) throws ParseException {
    return parse(null, code);
  }

  public Document parse(Path origin, String code) throws ParseException { // https://html.spec.whatwg.org/multipage/parsing.html
    Document document = NodeFactory.getDocument();
    stackOfOpenElements.add(document);

    code = code.replaceAll(Character.toString(CARRIAGE_RETURN) + LINE_FEED, Character.toString(LINE_FEED));
    code = code.replace(Character.toString(CARRIAGE_RETURN), Character.toString(LINE_FEED));
    chars = code.toCharArray();
    this.origin = origin;
    line = 1;
    row = 1;

    for (char c : chars) {
      if (isSurrogate(c)) {
        String message = "Surrogate in input stream parse error: " + c;
        addParseError(message);
        throw new ParseException(message, new FilePosition(origin, line, row));
      }
      if (isNoncharacter(c)) {
        String message = "Noncharacter in input stream parse error: " + c;
        addParseError(message);
        throw new ParseException(message, new FilePosition(origin, line, row));
      }
      if (isControl(c) && !isASCIIWhitespace(c) && c != NULL) {
        String message = "Control character in input stream parse error: " + (int) c + " (" + c + ")";
        addParseError(message);
        throw new ParseException(message, new FilePosition(origin, line, row));
      }
      row++;
      if (c == LINE_FEED) {
        row = 0;
        line++;
      }
    }
    code += EOF;
    chars = code.toCharArray();
    line = 1;
    row = 1;

    while (true) {
      if (endParse) {
        break;
      }

      charConsumed = consume();

      switch (state) {
        case TAG_OPEN:
          tagOpen();
          break;
        case END_TAG_OPEN:
          endTagOpen();
          break;
        case TAG_NAME:
          tagNameStatus();
          break;
        case BEFORE_ATTRIBUTE_NAME:
          beforeAttributeNameStatus();
          break;
        case ATTRIBUTE_NAME:
          attributeNameStatus();
          break;
        case AFTER_ATTRIBUTE_NAME:
          afterAttributeNameStatus();
          break;
        case BEFORE_ATTRIBUTE_VALUE:
          beforeAttributeValueStatus();
          break;
        case ATTRIBUTE_VALUE_DOUBLE_QUOTED:
          attributeValueDobleQuotedStatus();
          break;
        case ATTRIBUTE_VALUE_SINGLE_QUOTED:
          attributeValueSingleQuotedStatus();
          break;
        case ATTRIBUTE_VALUE_UNQUOTED:
          attributeValueUnquotedStatus();
          break;
        case AFTER_ATTRIBUTE_VALUE_QUOTED:
          afterAttributeValueQuotedStatus();
          break;
        case SELF_CLOSING_START_TAG:
          selfClosingStartTagStatus();
          break;
        case BOGUS_COMMENT:
          bogusCommentStatus();
          break;
        case MARKUP_DECLARATION_OPEN:
          markupDeclarationOpenStatus();
          break;
        case COMMENT_START:
          commentStartStatus();
          break;
        case COMMENT_START_DASH:
          commentStartDashStatus();
          break;
        case COMMENT:
          commentStatus();
          break;
        case COMMENT_LESS_THAN_SIGN:
          commentLessThanSignStatus();
          break;
        case COMMENT_LESS_THAN_SIGN_BANG:
          commentLessThanSignBangStatus();
          break;
        case COMMENT_LESS_THAN_SIGN_BANG_DASH:
          commentLessThanSignBangDashStatus();
          break;
        case COMMENT_LESS_THAN_SIGN_BANG_DASH_DASH:
          commentLessThanSignBangDashDashStatus();
          break;
        case COMMENT_END_DASH:
          commentEndDashStatus();
          break;
        case COMMENT_END:
          commentEndStatus();
          break;
        case COMMENT_END_BANG:
          commentEndBangStatus();
          break;
        case DOCTYPE:
          doctypeStatus();
          break;
        case BEFORE_DOCTYPE_NAME:
          beforeDoctypeNameStatus();
          break;
        case DOCTYPE_NAME:
          doctypeNameStatus();
          break;
        case AFTER_DOCTYPE_NAME:
          afterDoctypeNameStatus();
          break;
        case AFTER_DOCTYPE_PUBLIC_KEYWORD:
          afterDoctypePublicKeywordStatus();
          break;
        case BEFORE_DOCTYPE_PUBLIC_IDENTIFIER:
          beforeDoctypePublicIdentifierStatus();
          break;
        case DOCTYPE_PUBLIC_IDENTIFIER_DOUBLE_QUOTED:
          doctypePublicIdentifierDoubleQuotedStatus();
          break;
        case DOCTYPE_PUBLIC_IDENTIFIER_SINGLE_QUOTED:
          doctypePublicIdentifierSingleQuotedStatus();
          break;
        case AFTER_DOCTYPE_PUBLIC_IDENTIFIER:
          afterDoctypePublicIdentifierStatus();
          break;
        case BETWEEN_DOCTYPE_PUBLIC_AND_SYSTEM_IDENTIFIERS:
          betweenDoctypePublicAndSystemIdentifiersStatus();
          break;
        case AFTER_DOCTYPE_SYSTEM_KEYWORD:
          afterDoctypeSystemKeywordStatus();
          break;
        case BEFORE_DOCTYPE_SYSTEM_IDENTIFIER:
          beforeDoctypeSystemIdentifierStatus();
          break;
        case DOCTYPE_SYSTEM_IDENTIFIER_DOUBLE_QUOTED:
          doctypeSystemIdentifierDoubleQuotedStatus();
          break;
        case DOCTYPE_SYSTEM_IDENTIFIER_SINGLE_QUOTED:
          doctypeSystemIdentifierSingleQuotedStatus();
          break;
        case AFTER_DOCTYPE_SYSTEM_IDENTIFIER:
          afterDoctypeSystemIdentifierStatus();
          break;
        case BOGUS_DOCTYPE:
          bogusDoctypeStatus();
          break;
        case DATA:
        default:
          dataState();
      }
    }
    if (!parseErrors.isEmpty()) {
      throw new ParseException("Invalid HTML");
    }
    return document;
  }

  private void selfClosingStartTagStatus() {
    switch (charConsumed) {
      case GREATER_THAN_SIGN:
        currentToken.setSelfClosingFlag();
        state = State.DATA;
        emitCurrentToken();
        break;
      case EOF:
        addParseError("EOF in tag parse error");
        emitEOFToken();
        break;
      default:
        addParseError("Unexpected solidus in tag parse error");
        state = State.BEFORE_ATTRIBUTE_NAME;
        reconsume = true;
        break;
    }
  }

  private String stringElement(char c, int size) {
    StringBuilder sb = new StringBuilder();
    sb.append(c);
    for (int j = 0; j < size; j++) {
      sb.append(chars[i + j]);
    }
    return sb.toString();
  }

  private void consume(int size) {
    for (int j = 0; j < size; j++) {
      consume();
    }
  }

  private char element(int offset) {
    return chars[i + offset];
  }

  private char consume() {
    if (reconsume) {
      reconsume = false;
      return lastCharacter;
    } else {
      char c = chars[i];
      lastCharacter = c;
      position = new FilePosition(origin, line, row);
      i++;
      if (c == LINE_FEED) {
        line++;
        row = 1;
      } else {
        row++;
      }
      return c;
    }
  }

  private boolean isASCIIAlpha(char c) {
    return ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z'));
  }

  private boolean isSurrogate(char c) {
    return (c >= 0xd800 && c <= 0xdfff);
  }

  private boolean isNoncharacter(int c) {
    if (c >= 0xfdd0 && c <= 0xfdef) {
      return true;
    }
    switch (c) {
      case 0xfffe:
      case 0xffff:
      case 0x1fffe:
      case 0x1ffff:
      case 0x2fffe:
      case 0x2ffff:
      case 0x3fffe:
      case 0x3ffff:
      case 0x4fffe:
      case 0x4ffff:
      case 0x5fffe:
      case 0x5ffff:
      case 0x6fffe:
      case 0x6ffff:
      case 0x7fffe:
      case 0x7ffff:
      case 0x8fffe:
      case 0x8ffff:
      case 0x9fffe:
      case 0x9ffff:
      case 0xAfffe:
      case 0xAffff:
      case 0xBfffe:
      case 0xBffff:
      case 0xCfffe:
      case 0xCffff:
      case 0xDfffe:
      case 0xDffff:
      case 0xEfffe:
      case 0xEffff:
      case 0xffffE:
      case 0xffffF:
      case 0x10fffe:
      case 0x10ffff:
        return true;
      default:
        return false;
    }
  }

  private boolean isControl(char c) {
    return isC0Control(c) || (c >= DELETE && c <= APPLICATION_PROGRAM_COMMAND);
  }

  private boolean isASCIIWhitespace(char c) {
    return (c == CHARACTER_TABULATION || c == LINE_FEED || c == FORM_FEED);
  }

  private boolean isC0Control(char c) {
    return c <= INFORMATION_SEPARATOR_ONE;
  }

  private void emitLessThanSignCharacterToken() {
    emitCharacterToken(LESS_THAN_SIGN);
  }

  private void emitCharacterToken(char c) {
    stackOfOpenElements.add(c, position);
  }

  private void emitEOFToken() {
    endParse = true;
  }

  private void emitCurrentToken() {
    stackOfOpenElements.add(currentToken);
  }

  private void addParseError(String message) {
    parseErrors.add(new ParseError(message));
  }

  public ParseErrors getParseErrors() {
    return parseErrors;
  }

}
