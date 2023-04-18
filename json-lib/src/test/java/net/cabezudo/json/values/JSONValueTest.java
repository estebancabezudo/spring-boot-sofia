package net.cabezudo.json.values;

import net.cabezudo.json.exceptions.JSONConversionException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author <a href="http://cabezudo.net">Esteban Cabezudo</a>
 * @version 0.9, 05/09/2017
 */
public class JSONValueTest {

  @Test
  public void testToArray() {
    JSONConversionException jsonConversionException = Assertions.assertThrows(JSONConversionException.class, () -> {
      JSONValue jsonValue = JSONBoolean.TRUE;
      jsonValue.toArray();
    });
    Assertions.assertNotNull(jsonConversionException);
  }

  @Test
  public void testToBigDecimal() {
    JSONConversionException jsonConversionException = Assertions.assertThrows(JSONConversionException.class, () -> {
      JSONValue jsonValue = new JSONObject();
      jsonValue.toBigDecimal();
    });
    Assertions.assertNotNull(jsonConversionException);
  }

  @Test
  public void testToBigInteger() {
    JSONConversionException jsonConversionException = Assertions.assertThrows(JSONConversionException.class, () -> {
      JSONValue jsonValue = new JSONObject();
      jsonValue.toBigInteger();
    });
    Assertions.assertNotNull(jsonConversionException);
  }

  @Test
  public void testToBoolean() {
    JSONConversionException jsonConversionException = Assertions.assertThrows(JSONConversionException.class, () -> {
      JSONValue jsonValue = new JSONObject();
      jsonValue.toBoolean();
    });
    Assertions.assertNotNull(jsonConversionException);
  }

  @Test
  public void testToByte() {
    JSONConversionException jsonConversionException = Assertions.assertThrows(JSONConversionException.class, () -> {
      JSONValue jsonValue = new JSONObject();
      jsonValue.toByte();
    });
    Assertions.assertNotNull(jsonConversionException);
  }

  @Test
  public void testToByteArray() {
    JSONConversionException jsonConversionException = Assertions.assertThrows(JSONConversionException.class, () -> {
      JSONValue jsonValue = new JSONObject();
      jsonValue.toByteArray();
    });
    Assertions.assertNotNull(jsonConversionException);
  }

  @Test
  public void testToCalendar() {
    JSONConversionException jsonConversionException = Assertions.assertThrows(JSONConversionException.class, () -> {
      JSONValue jsonValue = new JSONObject();
      jsonValue.toCalendar();
    });
    Assertions.assertNotNull(jsonConversionException);
  }

  @Test
  public void testToCharacter() {
    JSONConversionException jsonConversionException = Assertions.assertThrows(JSONConversionException.class, () -> {
      JSONValue jsonValue = new JSONObject();
      jsonValue.toCharacter();
    });
    Assertions.assertNotNull(jsonConversionException);
  }

  @Test
  public void testToDouble() {
    JSONConversionException jsonConversionException = Assertions.assertThrows(JSONConversionException.class, () -> {
      JSONValue jsonValue = new JSONObject();
      jsonValue.toDouble();
    });
    Assertions.assertNotNull(jsonConversionException);
  }

  @Test
  public void testToFloat() {
    JSONConversionException jsonConversionException = Assertions.assertThrows(JSONConversionException.class, () -> {
      JSONValue jsonValue = new JSONObject();
      jsonValue.toFloat();
    });
    Assertions.assertNotNull(jsonConversionException);
  }

  @Test
  public void testToInteger() {
    JSONConversionException jsonConversionException = Assertions.assertThrows(JSONConversionException.class, () -> {
      JSONValue jsonValue = new JSONObject();
      jsonValue.toInteger();
    });
    Assertions.assertNotNull(jsonConversionException);
  }

  @Test
  public void testToJSONString() {
    JSONConversionException jsonConversionException = Assertions.assertThrows(JSONConversionException.class, () -> {
      JSONValue jsonValue = new JSONObject();
      jsonValue.toJSONString();
    });
    Assertions.assertNotNull(jsonConversionException);
  }

  @Test
  public void testToList() {
    JSONConversionException jsonConversionException = Assertions.assertThrows(JSONConversionException.class, () -> {
      JSONValue jsonValue = new JSONObject();
      jsonValue.toList();
    });
    Assertions.assertNotNull(jsonConversionException);
  }

  @Test
  public void testToLong() {
    JSONConversionException jsonConversionException = Assertions.assertThrows(JSONConversionException.class, () -> {
      JSONValue jsonValue = new JSONObject();
      jsonValue.toLong();
    });
    Assertions.assertNotNull(jsonConversionException);
  }

  @Test
  public void testToStringArray() {
    JSONConversionException jsonConversionException = Assertions.assertThrows(JSONConversionException.class, () -> {
      JSONValue jsonValue = new JSONObject();
      jsonValue.toStringArray();
    });
    Assertions.assertNotNull(jsonConversionException);
  }
}
