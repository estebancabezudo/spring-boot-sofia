package net.cabezudo.json;

import net.cabezudo.json.exceptions.JSONConversionException;
import net.cabezudo.json.values.JSONString;
import net.cabezudo.json.values.JSONValue;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;


/**
 * @author <a href="http://cabezudo.net">Esteban Cabezudo</a>
 * @version 0.9, 10/01/2014
 */
public class JSONPairTest {

  @Test
  public void testCompareTo() {
    Log.debug("Compare two objects JSONPair.");
    JSONPair a = new JSONPair("b", "b");
    JSONPair b = new JSONPair("b", "b");
    JSONPair c = new JSONPair("b", "a");
    JSONPair d = new JSONPair("b", "c");
    JSONPair e = new JSONPair("a", "x");
    JSONPair f = new JSONPair("c", "x");
    int comparation = a.compareTo(b);
    assertEquals(0, comparation);
    comparation = a.compareTo(c);
    assertTrue(comparation > 0);
    comparation = a.compareTo(d);
    assertTrue(comparation < 0);
    comparation = a.compareTo(e);
    assertTrue(comparation > 0);
    comparation = a.compareTo(f);
    assertTrue(comparation < 0);
  }

  @Test
  public void testEquals() {
    Log.debug("Compare two objects JSONPair for equality.");
    JSONPair a = new JSONPair("a", "b");
    JSONPair b = new JSONPair("a", "b");
    JSONPair c = new JSONPair("a", "c");
    JSONPair d = new JSONPair("b", "b");
    boolean comparation = a.equals(b);
    assertTrue(comparation);
    comparation = a.equals(c);
    assertFalse(comparation);
    comparation = a.equals(d);
    assertFalse(comparation);
  }

  @Test
  public void testGetValue() {
    Log.debug("Get the element value.");
    JSONString jsonExpectedValue = new JSONString("A string");
    JSONPair jsonPair = new JSONPair("key", jsonExpectedValue);
    JSONValue jsonValue = jsonPair.getValue();
    assertEquals(jsonExpectedValue, jsonValue);
  }

  @Test
  public void testHasElements() {
    Log.debug("Check if an element has childs.");
    JSONPair jsonPair = new JSONPair("key", "A string");
    jsonPair.hasElements();
  }

  @Test
  public void testHash() {
    Log.debug("Compare two objects JSONPair hash.");
    JSONPair a = new JSONPair("a", "b");
    JSONPair b = new JSONPair("a", "b");
    JSONPair c = new JSONPair("a", "c");
    JSONPair d = new JSONPair("b", "b");
    int ha = a.hashCode();
    int hb = b.hashCode();
    int hc = c.hashCode();
    int hd = d.hashCode();
    assertEquals(ha, hb);
    assertNotEquals(ha, hc);
    assertNotEquals(ha, hd);
  }

  @Test
  public void testIsArray() {
    Log.debug("Check if the element is an array.");
    JSONPair jsonPair = new JSONPair("key", "A string");
    assertFalse(jsonPair.isArray());
  }

  @Test
  public void testIsBoolean() {
    Log.debug("Check if the element is a boolean.");
    JSONPair jsonPair = new JSONPair("key", "A string");
    assertFalse(jsonPair.isBoolean());
  }

  @Test
  public void testIsNull() {
    Log.debug("Check if the element is a null.");
    JSONPair jsonPair = new JSONPair("key", "A string");
    assertFalse(jsonPair.isNull());
  }

  @Test
  public void testIsNumber() {
    Log.debug("Check if the element is a number.");
    JSONPair jsonPair = new JSONPair("key", "A string");
    assertFalse(jsonPair.isNumber());
  }

  @Test
  public void testIsObject() {
    Log.debug("Check if the element is an object.");
    JSONPair jsonPair = new JSONPair("key", "A string");
    assertFalse(jsonPair.isObject());
  }

  @Test
  public void testIsReferenceable() {
    Log.debug("Check if the element is referenceable.");
    JSONPair jsonPair = new JSONPair("key", "A string");
    assertFalse(jsonPair.isReferenceable());
  }

  @Test
  public void testIsString() {
    Log.debug("Check if the element is a string.");
    JSONPair jsonPair = new JSONPair("key", "A string");
    assertFalse(jsonPair.isString());
  }

  @Test
  public void testIsValue() {
    Log.debug("Check if the element is a value.");
    JSONPair jsonPair = new JSONPair("key", "A string");
    assertFalse(jsonPair.isValue());
  }

  @Test
  public void testObjectUsingABigDecimal() {
    Log.debug("Create a JSONValue using a BigDecimal object.");
    BigDecimal bigDecimal = new BigDecimal("10");
    JSONPair jsonPair = new JSONPair("key", bigDecimal);
    String key = jsonPair.getKey();
    assertEquals("key", key);
    JSONValue jsonValue = jsonPair.getValue();
    assertTrue(jsonValue.isNumber());
    String newBigDecimalString = jsonValue.toString();
    BigDecimal newBigDecimal = new BigDecimal(newBigDecimalString);
    assertEquals(bigDecimal, newBigDecimal);
  }

  @Test
  public void testObjectUsingABigInteger() {
    Log.debug("Create a JSONValue using a BigInteger object.");
    BigInteger bigInteger = new BigInteger("10");
    JSONPair jsonPair = new JSONPair("key", bigInteger);
    String key = jsonPair.getKey();
    assertEquals("key", key);
    JSONValue jsonValue = jsonPair.getValue();
    assertTrue(jsonValue.isNumber());
    String newBigIntegerString = jsonValue.toString();
    BigInteger newBigInteger = new BigInteger(newBigIntegerString);
    assertEquals(bigInteger, newBigInteger);
  }

  @Test
  public void testObjectUsingABoolean() {
    Log.debug("Create a JSONValue using a Boolean object.");
    Boolean b = true;
    JSONPair jsonPair = new JSONPair("key", b);
    String key = jsonPair.getKey();
    assertEquals("key", key);
    JSONValue jsonValue = jsonPair.getValue();
    assertTrue(jsonValue.isBoolean());
  }

  @Test
  public void testObjectUsingAByte() {
    Log.debug("Create a JSONValue using a Byte object.");
    Byte b = 10;
    JSONPair jsonPair = new JSONPair("key", b);
    String key = jsonPair.getKey();
    assertEquals("key", key);
    JSONValue jsonValue = jsonPair.getValue();
    assertTrue(jsonValue.isNumber());
  }

  @Test
  public void testObjectUsingACalendar() {
    Log.debug("Create a JSONValue using a Calendar object.");
    Calendar calendar = Calendar.getInstance();
    JSONPair jsonPair = new JSONPair("key", calendar);
    String key = jsonPair.getKey();
    assertEquals("key", key);
    JSONValue jsonValue = jsonPair.getValue();
    assertTrue(jsonValue.isString());
    SimpleDateFormat sdf = new SimpleDateFormat(JSON.SIMPLE_DATE_FORMAT_PATTERN);
    String stringDate = jsonValue.toString();
    try {
      Date date = sdf.parse(stringDate);
      Calendar newCalendar = Calendar.getInstance();
      newCalendar.setTime(date);
      assertEquals(newCalendar, calendar);
    } catch (ParseException e) {
      fail(e.getMessage());
    }
  }

  @Test
  public void testObjectUsingACharacter() {
    Log.debug("Create a JSONValue using a Character object.");
    Character c = 'a';
    JSONPair jsonPair = new JSONPair("key", c);
    String key = jsonPair.getKey();
    assertEquals("key", key);
    JSONValue jsonValue = jsonPair.getValue();
    assertTrue(jsonValue.isString());
  }

  @Test
  public void testObjectUsingADate() {
    Log.debug("Create a JSONValue using a Date object.");
    Date date = new Date();
    JSONPair jsonPair = new JSONPair("key", date);
    String key = jsonPair.getKey();
    assertEquals("key", key);
    JSONValue jsonValue = jsonPair.getValue();
    assertTrue(jsonValue.isString());
    SimpleDateFormat sdf = new SimpleDateFormat(JSON.SIMPLE_DATE_FORMAT_PATTERN);
    String stringDate = jsonValue.toString();
    try {
      Date newDate = sdf.parse(stringDate);
      assertEquals(newDate, date);
    } catch (ParseException e) {
      fail(e.getMessage());
    }
  }

  @Test
  public void testObjectUsingADouble() {
    Log.debug("Create a JSONValue using a Double object.");
    Double d = Double.valueOf(10);
    JSONPair jsonPair = new JSONPair("key", d);
    String key = jsonPair.getKey();
    assertEquals("key", key);
    JSONValue jsonValue = jsonPair.getValue();
    assertTrue(jsonValue.isNumber());
  }

  @Test
  public void testObjectUsingAFloat() {
    Log.debug("Create a JSONValue using a Float object.");
    Float f = Float.valueOf(10);
    JSONPair jsonPair = new JSONPair("key", f);
    String key = jsonPair.getKey();
    assertEquals("key", key);
    JSONValue jsonValue = jsonPair.getValue();
    assertTrue(jsonValue.isNumber());
  }

  @Test
  public void testObjectUsingAInteger() {
    Log.debug("Create a JSONValue using a Integer object.");
    Integer i = 10;
    JSONPair jsonPair = new JSONPair("key", i);
    String key = jsonPair.getKey();
    assertEquals("key", key);
    JSONValue jsonValue = jsonPair.getValue();
    assertTrue(jsonValue.isNumber());
  }

  @Test
  public void testObjectUsingALong() {
    Log.debug("Create a JSONValue using a Long object.");
    Long l = 10L;
    JSONPair jsonPair = new JSONPair("key", l);
    String key = jsonPair.getKey();
    assertEquals("key", key);
    JSONValue jsonValue = jsonPair.getValue();
    assertTrue(jsonValue.isNumber());
  }

  @Test
  public void testObjectUsingANull() {
    Log.debug("Create a JSONValue using a null.");
    Object n = null;
    JSONPair jsonPair = new JSONPair("key", n);
    String key = jsonPair.getKey();
    assertEquals("key", key);
    JSONValue jsonValue = jsonPair.getValue();
    assertTrue(jsonValue.isNull());
  }

  @Test
  public void testObjectUsingAPrimitiveBoolean() {
    Log.debug("Create a JSONValue using a primitive boolean.");
    boolean b = true;
    JSONPair jsonPair = new JSONPair("key", b);
    String key = jsonPair.getKey();
    assertEquals("key", key);
    JSONValue jsonValue = jsonPair.getValue();
    assertTrue(jsonValue.isBoolean());
  }

  @Test
  public void testObjectUsingAPrimitiveByte() {
    Log.debug("Create a JSONValue using a primitive byte.");
    byte b = 10;
    JSONPair jsonPair = new JSONPair("key", b);
    String key = jsonPair.getKey();
    assertEquals("key", key);
    JSONValue jsonValue = jsonPair.getValue();
    assertTrue(jsonValue.isNumber());
  }

  @Test
  public void testObjectUsingAPrimitiveChar() {
    Log.debug("Create a JSONValue using a primitive character.");
    char c = 'a';
    JSONPair jsonPair = new JSONPair("key", c);
    String key = jsonPair.getKey();
    assertEquals("key", key);
    JSONValue jsonValue = jsonPair.getValue();
    assertTrue(jsonValue.isString());
  }

  @Test
  public void testObjectUsingAPrimitiveDouble() {
    Log.debug("Create a JSONValue using a primitive double.");
    double d = 10;
    JSONPair jsonPair = new JSONPair("key", d);
    String key = jsonPair.getKey();
    assertEquals("key", key);
    JSONValue jsonValue = jsonPair.getValue();
    assertTrue(jsonValue.isNumber());
  }

  @Test
  public void testObjectUsingAPrimitiveFloat() {
    Log.debug("Create a JSONValue using a primitive float.");
    float f = 10;
    JSONPair jsonPair = new JSONPair("key", f);
    String key = jsonPair.getKey();
    assertEquals("key", key);
    JSONValue jsonValue = jsonPair.getValue();
    assertTrue(jsonValue.isNumber());
  }

  @Test
  public void testObjectUsingAPrimitiveInt() {
    Log.debug("Create a JSONValue using a primitive integer.");
    int i = 10;
    JSONPair jsonPair = new JSONPair("key", i);
    String key = jsonPair.getKey();
    assertEquals("key", key);
    JSONValue jsonValue = jsonPair.getValue();
    assertTrue(jsonValue.isNumber());
  }

  @Test
  public void testObjectUsingAPrimitiveLong() {
    Log.debug("Create a JSONValue using a primitive long.");
    long l = 10;
    JSONPair jsonPair = new JSONPair("key", l);
    String key = jsonPair.getKey();
    assertEquals("key", key);
    JSONValue jsonValue = jsonPair.getValue();
    assertTrue(jsonValue.isNumber());
  }

  @Test
  public void testObjectUsingAPrimitiveShort() {
    Log.debug("Create a JSONValue using a primitive short.");
    short s = 10;
    JSONPair jsonPair = new JSONPair("key", s);
    String key = jsonPair.getKey();
    assertEquals("key", key);
    JSONValue jsonValue = jsonPair.getValue();
    assertTrue(jsonValue.isNumber());
  }

  @Test
  public void testObjectUsingAShort() {
    Log.debug("Create a JSONValue using a Short object.");
    Short s = 10;
    JSONPair jsonPair = new JSONPair("key", s);
    String key = jsonPair.getKey();
    assertEquals("key", key);
    JSONValue jsonValue = jsonPair.getValue();
    assertTrue(jsonValue.isNumber());
  }

  @Test
  public void testObjectUsingAString() {
    Log.debug("Create a JSONValue using a String object.");
    String s = "A string";
    JSONPair jsonPair = new JSONPair("key", s);
    String key = jsonPair.getKey();
    assertEquals("key", key);
    JSONValue jsonValue = jsonPair.getValue();
    assertTrue(jsonValue.isString());
  }

  @Test
  public void testSetReferenceFieldName() {
    Log.debug("Set the reference field name.");
    JSONPair jsonPair = new JSONPair("key", "A string");
    jsonPair.setReferenceFieldName("fieldName");
  }

  @Test
  public void testToBigDecimal() {
    Log.debug("Convert the element into an object BigDecimal.");

    JSONPair jsonPair;

    jsonPair = new JSONPair("key", 10);
    jsonPair.toBigDecimal();

    jsonPair = new JSONPair("key", "10");
    jsonPair.toBigDecimal();
  }

  @Test
  public void testToBoolean() {
    Log.debug("Convert the element into an object Boolean.");
    JSONPair jsonPair = new JSONPair("key", "A string");
    jsonPair.toBoolean();
  }

  @Test
  public void testToByte() {
    Log.debug("Convert the element into a Byte object.");

    JSONPair jsonPair;

    jsonPair = new JSONPair("key", 120);
    jsonPair.toByte();

    jsonPair = new JSONPair("key", "120");
    jsonPair.toByte();
  }

  @Test
  public void testToCharacter() {
    Log.debug("Convert the element into a Character object.");
    JSONPair jsonPair = new JSONPair("key", "A string");
    jsonPair.toCharacter();
  }

  @Test
  public void testToDouble() {
    Log.debug("Convert the element into a Double object.");

    JSONPair jsonPair;

    jsonPair = new JSONPair("key", 1364.45);
    jsonPair.toDouble();

    jsonPair = new JSONPair("key", "1364.45");
    jsonPair.toDouble();
  }

  @Test
  public void testToFloat() {
    Log.debug("Convert the element into a Float object.");

    JSONPair jsonPair;

    jsonPair = new JSONPair("key", 1234.5);
    jsonPair.toFloat();

    jsonPair = new JSONPair("key", "1234.5");
    jsonPair.toFloat();
  }

  @Test
  public void testToInteger() {
    Log.debug("Convert the element into a Integer object.");

    JSONPair jsonPair;

    jsonPair = new JSONPair("key", 1234567890);
    jsonPair.toInteger();

    jsonPair = new JSONPair("key", "1234567890");
    jsonPair.toInteger();
  }

  @Test
  public void testToJSON() {
    Log.debug("Returnn the JSON string element representation.");
    String expectedValue = "\"" + "key" + "\": \"" + "A string" + "\"";
    JSONPair jsonPair = new JSONPair("key", "A string");
    String value = jsonPair.toJSON();
    assertEquals(expectedValue, value);
  }

  @Test
  public void testToJSONArray() {
    Log.debug("Convert the element into a JSONArray object.");
    JSONPair jsonPair = new JSONPair("key", "A string");
    jsonPair.toJSONArray();
  }

  @Test
  public void testToJSONString() {
    Log.debug("Convert the element into a JSONString object.");
    JSONPair jsonPair = new JSONPair("key", "A string");
    jsonPair.toJSONString();
  }

  @Test
  public void testToJSONTree() {
    Log.debug("Convert the element to a JSON tree.");
    JSONPair jsonPair = new JSONPair("key", "A string");
    jsonPair.toJSONTree();
  }

  @Test
  public void testToList() {
    Log.debug("Convert the element into a List<JSONValues>.");
    JSONPair jsonPair = new JSONPair("key", "A string");
    jsonPair.toList();
  }

  @Test
  public void testToLong() {
    Log.debug("Convert the element into a Long object.");

    JSONPair jsonPair;

    jsonPair = new JSONPair("key", 1234567890123456789L);
    jsonPair.toLong();

    jsonPair = new JSONPair("key", "1234567890123456789");
    jsonPair.toLong();
  }

  @Test
  public void testToObject() {
    JSONConversionException jsonConversionException = Assertions.assertThrows(JSONConversionException.class, () -> {
      Log.debug("Convert the element into a JSONObject object.");
      JSONPair jsonPair = new JSONPair("key", "A string");
      jsonPair.toObject();
    });
    Assertions.assertNotNull(jsonConversionException);
  }

  @Test
  public void testToShort() {
    Log.debug("Convert the element into a Short object.");

    JSONPair jsonPair;

    jsonPair = new JSONPair("key", 234);
    jsonPair.toShort();

    jsonPair = new JSONPair("key", "234");
    jsonPair.toShort();
  }

  @Test
  public void testToString() {
    Log.debug("Return the element string representation.");
    String expectedValue = "(" + "key" + ", " + "A string" + ")";
    JSONPair jsonPair = new JSONPair("key", "A string");
    String value = jsonPair.toString();
    assertEquals(expectedValue, value);
  }
}
