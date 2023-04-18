/**
 * MIT License
 * <p>
 * Copyright (c) 2017 Esteban Cabezudo
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
package net.cabezudo.json;

import net.cabezudo.json.exceptions.DuplicateKeyException;
import net.cabezudo.json.exceptions.EOSException;
import net.cabezudo.json.exceptions.EmptyQueueException;
import net.cabezudo.json.exceptions.JSONParseException;
import net.cabezudo.json.exceptions.UnexpectedElementException;
import net.cabezudo.json.values.JSONArray;
import net.cabezudo.json.values.JSONBoolean;
import net.cabezudo.json.values.JSONNull;
import net.cabezudo.json.values.JSONNumber;
import net.cabezudo.json.values.JSONObject;
import net.cabezudo.json.values.JSONString;
import net.cabezudo.json.values.JSONValue;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author <a href="http://cabezudo.net">Esteban Cabezudo</a>
 * @version 0.9, 01/22/2016
 */
public class JSONFactory {

  /**
   * Create a JSON structure representation of the parameter object. For the conversion the method use any of the next rules.
   * <ul>
   * <li>
   * If the object is a {@link java.lang.Byte}, {@link java.lang.Short},
   * {@link java.lang.Integer}, {@link java.lang.Long}, {@link java.lang.Float},
   * {@link java.lang.Double}, {@link java.math.BigInteger}, or {@link java.math.BigDecimal} object the method use a {@link net.cabezudo.json.values.JSONNumber}.
   * </li>
   * <li>
   * If a property is a {@code byte}, {@code short}, {@code int}, {@code long}, {@code float}, or {@code double} type, the method use a {@link net.cabezudo.json.values.JSONNumber}.
   * </li>
   * <li>
   * If the object or a property is a {@link java.lang.Boolean} object the method use a {@link net.cabezudo.json.values.JSONBoolean}.
   * </li>
   * <li>
   * If some property is a {@code boolean} type the method use a {@link net.cabezudo.json.values.JSONBoolean}.
   * </li>
   * <li>
   * If the object or a property is a {@link java.lang.Character}, {@link java.lang.String},
   * {@link java.util.Date}, {@link java.util.GregorianCalendar}, or {@link java.lang.Class} object the method convert it to a {@link net.cabezudo.json.values.JSONString}.
   * </li>
   * <li>
   * If a property is a {@code char} type the method convert it to a {@link net.cabezudo.json.values.JSONString}.
   * </li>
   * <li>
   * If a property is a {@code byte[]} type the method use a {@link net.cabezudo.json.values.JSONArray}.
   * </li>
   * <li>
   * If the conversion can't be done using this rules the system try to convert the object using the {@link net.cabezudo.json.annotations.JSONProperty} annotation.
   * </li>
   * </ul>
   *
   * @param object the POJO to create the JSON structure from.
   * @return a {@link net.cabezudo.json.values.JSONValue} with a JSON structure.
   */
  public static JSONValue get(Object object) {
    if (object == null) {
      return new JSONNull();
    }
    if (object instanceof JSONValue) {
      return (JSONValue) object;
    }
    Class<?> objectClass = object.getClass();
    String fieldTypeName = objectClass.getName();
    SimpleDateFormat sdf;
    String stringDate;
    JSONValue jsonValue;
    switch (fieldTypeName) {
      case "byte":
      case "java.lang.Byte":
        jsonValue = new JSONNumber((Byte) object);
        break;
      case "short":
      case "java.lang.Short":
        jsonValue = new JSONNumber((Short) object);
        break;
      case "int":
      case "java.lang.Integer":
        jsonValue = new JSONNumber((Integer) object);
        break;
      case "long":
      case "java.lang.Long":
        jsonValue = new JSONNumber((Long) object);
        break;
      case "float":
      case "java.lang.Float":
        jsonValue = new JSONNumber((Float) object);
        break;
      case "double":
      case "java.lang.Double":
        jsonValue = new JSONNumber((Double) object);
        break;
      case "boolean":
      case "java.lang.Boolean":
        jsonValue = JSONBoolean.get((Boolean) object);
        break;
      case "char":
      case "java.lang.Character":
        jsonValue = new JSONString((Character) object);
        break;
      case "java.lang.String":
        jsonValue = new JSONString((String) object);
        break;
      case "java.util.Date":
        Date dateValue = (Date) object;
        sdf = new SimpleDateFormat(JSON.SIMPLE_DATE_FORMAT_PATTERN);
        stringDate = sdf.format(dateValue);
        jsonValue = new JSONString(stringDate);
        break;
      case "java.util.GregorianCalendar":
        Calendar calendarValue = (Calendar) object;
        sdf = new SimpleDateFormat(JSON.SIMPLE_DATE_FORMAT_PATTERN);
        stringDate = sdf.format(calendarValue.getTime());
        jsonValue = new JSONString(stringDate);
        break;
      case "java.math.BigInteger":
        jsonValue = new JSONNumber((BigInteger) object);
        break;
      case "java.math.BigDecimal":
        jsonValue = new JSONNumber((BigDecimal) object);
        break;
      case "java.lang.Class":
        Class classValue = (Class) object;
        String className = classValue.getName();
        jsonValue = new JSONString(className);
        break;
      case "[B":
        JSONArray jsonByteArray = new JSONArray();
        byte[] byteArray = (byte[]) object;
        for (Byte b : byteArray) {
          jsonByteArray.add(new JSONNumber(b));
        }
        jsonValue = jsonByteArray;
        break;
      case "[I":
        JSONArray jsonIntArray = new JSONArray();
        int[] intArray = (int[]) object;
        for (int i : intArray) {
          jsonIntArray.add(new JSONNumber(i));
        }
        jsonValue = jsonIntArray;
        break;
      default:
        jsonValue = null;
        break;
    }
    return jsonValue;
  }

  private JSONString createJSONString(Token token) {
    String valueInQuotes = token.getValue();
    String value = valueInQuotes.substring(1, valueInQuotes.length() - 1);
    return new JSONString(value, token.getPosition());
  }

  private JSONValue get(Position position, Tokens tokens) throws JSONParseException {
    Token token;
    try {
      token = tokens.consume();
    } catch (EmptyQueueException e) {
      throw new EOSException(position);
    }
    return get(token, tokens);
  }

  private JSONValue get(Token token, Tokens tokens) throws JSONParseException {

    JSONValue jsonValue;

    TokenType type = token.getType();
    switch (type) {
      case STRING:
        jsonValue = createJSONString(token);
        break;
      case NUMBER:
        BigDecimal bigDecimal = new BigDecimal(token.getValue());
        jsonValue = new JSONNumber(bigDecimal);
        break;
      case LEFT_BRACE:
        try {
          jsonValue = getJSONObject(tokens, token.getPosition());
        } catch (DuplicateKeyException e) {
          throw new JSONParseException("Nothing to parse.", e, e.getNewKeyPosition());
        }
        break;
      case LEFT_BRACKET:
        jsonValue = getJSONArray(tokens, token.getPosition());
        break;
      case FALSE:
      case TRUE:
        boolean booleanValue = "true".equals(token.getValue());
        jsonValue = JSONBoolean.get(booleanValue);
        break;
      case NULL:
        jsonValue = new JSONNull();
        break;
      default:
        throw new UnexpectedElementException("value", token.getValue(), token.getPosition());
    }
    return jsonValue;
  }

  JSONArray getJSONArray(Tokens tokens, Position position) throws JSONParseException {
    JSONArray jsonArray = new JSONArray(position);
    Token token;
    if (!tokens.hasNext()) {
      throw new EOSException(position);
    }

    try {
      token = tokens.consume();
    } catch (EmptyQueueException e) {
      throw new RuntimeException(e);
    }
    do {
      if (token.getType() == TokenType.RIGHT_BRACKET) {
        break;
      }

      JSONValue jsonValue = get(token, tokens);

      jsonArray.add(jsonValue);

      try {
        token = tokens.consume();
      } catch (EmptyQueueException e) {
        throw new EOSException(position);
      }
      if (token.getType() != TokenType.COMMA && token.getType() != TokenType.RIGHT_BRACKET) {
        throw new UnexpectedElementException("comma or right bracket", token.getValue(), token.getPosition());
      }
      if (token.getType() == TokenType.COMMA) {
        try {
          token = tokens.consume();
        } catch (EmptyQueueException e) {
          throw new EOSException(position);
        }
      }
    } while (true);

    return jsonArray;
  }

  JSONObject getJSONObject(Tokens tokens, Position position) throws JSONParseException, DuplicateKeyException {
    JSONObject jsonObject = new JSONObject(position);
    Token token;

    if (!tokens.hasNext()) {
      throw new EOSException(position);
    }

    try {
      token = tokens.consume();
    } catch (EmptyQueueException e) {
      throw new EOSException(position);
    }

    do {
      if (token.getType() == TokenType.RIGHT_BRACE) {
        break;
      }

      if (token.getType() != TokenType.STRING) {
        throw new UnexpectedElementException("string", token.getValue(), token.getPosition());
      }
      JSONString jsonKeyString = createJSONString(token);

      try {
        token = tokens.consume();
        position = token.getPosition();
      } catch (EmptyQueueException e) {
        throw new EOSException(position);
      }
      if (token.getType() != TokenType.COLON) {
        throw new UnexpectedElementException("colon", token.getValue(), token.getPosition());
      }
      JSONValue jsonValue = get(position, tokens);
      JSONPair jsonPair = new JSONPair(jsonKeyString.toString(), jsonValue, position);
      jsonObject.add(jsonPair);

      try {
        token = tokens.consume();
        position = token.getPosition();
      } catch (EmptyQueueException e) {
        throw new EOSException(position);
      }

      if (token.getType() != TokenType.COMMA && token.getType() != TokenType.RIGHT_BRACE) {
        throw new UnexpectedElementException("comma or right brace", token.getValue(), token.getPosition());
      }
      if (token.getType() == TokenType.COMMA) {
        try {
          token = tokens.consume();
          position = token.getPosition();
        } catch (EmptyQueueException e) {
          throw new EOSException(position);
        }
      }
    } while (true);

    return jsonObject;
  }
}
