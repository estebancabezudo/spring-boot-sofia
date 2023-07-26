package net.cabezudo.json.values;

import net.cabezudo.json.exceptions.ElementNotExistException;
import net.cabezudo.json.exceptions.JSONConversionException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author <a href="http://cabezudo.net">Esteban Cabezudo</a>
 * @version 0.9, 08/18/2016
 */
public class JSONStringTest {

  @Test
  public void testBigIntegerConstructor() {
    String s = "1234567";
    BigInteger bi = new BigInteger(s);
    JSONString a = new JSONString(s);
    JSONString b = new JSONString(bi);

    assertEquals(a, b);
  }

  @Test
  public void testCalendarConstructor() {
    String s = "1974-01-30T14:20:12.125Z";

    JSONString a = new JSONString(s);

    Calendar calendar = Calendar.getInstance();
    calendar.set(1974, 0, 30, 14, 20, 12);
    calendar.set(Calendar.MILLISECOND, 125);
    JSONString b = new JSONString(calendar);

    assertEquals(a, b);
  }

  @Test
  public void testCompareTo() {
    JSONString a = new JSONString("Esteban");
    JSONString b = new JSONString("Ismael");
    JSONString c = new JSONString("Esteban");

    assertTrue(a.compareTo(b) < 0);
    assertTrue(b.compareTo(a) > 0);
    assertEquals(0, a.compareTo(c));
    assertEquals(0, c.compareTo(a));
  }

  @Test
  public void testEquals() {
    JSONString a = new JSONString("Esteban");
    JSONString b = new JSONString("Ismael");
    JSONString c = new JSONString("Esteban");

    assertNotEquals(a, b);
    assertEquals(a, c);
    assertNotEquals(b, a);
    assertEquals(c, a);
  }

  @Test
  public void testOtherEquals() {
    JSONString a = new JSONString("Esteban");
    String b = null;

    assertNotEquals(a, b);

    b = "invalid class";
    assertNotEquals(a, b);
  }

  @Test
  public void testGetReferencedElement() {
    JSONString a = new JSONString("Esteban");
    a.setReferenceFieldName("id");
    JSONValue b = a.toReferencedElement();

    assertEquals(new JSONString("Esteban"), b);

  }

  @Test
  public void testHashCode() {
    int a = new JSONString("Esteban").hashCode();
    int b = new JSONString("Ismael").hashCode();
    int c = new JSONString("Esteban").hashCode();

    assertTrue(a != b);
    assertEquals(a, c);
    assertTrue(b != c);
  }

  @Test
  public void testIsString() {
    assertTrue(new JSONString("Esteban").isString());
    assertFalse(new JSONString("Esteban").isArray());
    assertFalse(new JSONString("Esteban").isBoolean());
    assertFalse(new JSONString("Esteban").isNull());
    assertFalse(new JSONString("Esteban").isNumber());
    assertFalse(new JSONString("Esteban").isObject());
    assertTrue(new JSONString("Esteban").isValue());
  }

  @Test
  public void testIsValue() {
    JSONString jsonString = new JSONString("Esteban");
    assertTrue(jsonString.isValue());
  }

  @Test
  public void testToArray() {
    JSONString a = new JSONString("Esteban");

    JSONValue[] array = a.toArray();

    assertEquals(1, array.length);

    JSONValue value = array[0];
    assertEquals(new JSONString("Esteban"), value);
  }

  @Test
  public void testToBigDecimal() {
    JSONString a = new JSONString("1000000000000000000.12345");

    BigDecimal bigDecimal = a.toBigDecimal();
    assertEquals(new BigDecimal("1000000000000000000.12345"), bigDecimal);
  }

  @Test
  public void testToBigInteger() {
    JSONString a = new JSONString("1000000000000000000");

    BigInteger bigInteger = a.toBigInteger();
    assertEquals(new BigInteger("1000000000000000000"), bigInteger);
  }

  @Test
  public void testToBoolean() {
    JSONString a = new JSONString("true");
    JSONString b = new JSONString("false");
    JSONString c = new JSONString("ok");

    assertTrue(a.toBoolean());
    assertFalse(b.toBoolean());
    assertFalse(c.toBoolean());
  }

  @Test
  public void testToByte() {
    JSONString a = new JSONString("1");

    byte b = a.toByte();
    assertEquals((byte) 1, b);
  }

  @Test
  public void testToByteArray() {
    JSONString a = new JSONString("15");

    byte[] byteArray = a.toByteArray();

    assertEquals(1, byteArray.length);

    byte b = byteArray[0];
    assertEquals(15, b);
  }

  @Test
  public void testToCalendar() {
    JSONString a = new JSONString("1974-01-30T14:20:12.125Z");

    Calendar expectedCalendar = Calendar.getInstance();

    expectedCalendar.set(1974, 0, 30, 14, 20, 12);
    expectedCalendar.set(Calendar.MILLISECOND, 125);

    Calendar calendar = a.toCalendar();
    assertEquals(expectedCalendar, calendar);
  }

  @Test
  public void testToCalendarWithPattern() {
    JSONString a = new JSONString("1974-01-30");

    Calendar expectedCalendar = Calendar.getInstance();

    expectedCalendar.set(1974, 0, 30, 0, 0, 0);
    expectedCalendar.set(Calendar.MILLISECOND, 0);

    Calendar calendar = a.toCalendar("yyyy-MM-dd");
    assertEquals(expectedCalendar, calendar);
  }

  @Test
  public void testToCalendarParseException() {
    JSONConversionException jsonConversionException = Assertions.assertThrows(JSONConversionException.class, () -> {
      JSONString a = new JSONString("wrong pattern");
      a.toCalendar();
    });
    Assertions.assertNotNull(jsonConversionException);
  }

  @Test
  public void testToCharacter() {
    JSONString a = new JSONString("a");
    JSONString b = new JSONString("Esteban");

    assertEquals('a', (long) a.toCharacter());
    assertEquals('E', b.toCharacter(), 'E');
    assertNotEquals('s', (char) b.toCharacter());
  }

  @Test
  public void testToDouble() {
    JSONString a = new JSONString("12340523450.34534");

    double d = a.toDouble();
    assertEquals(12340523450.34534, d, 0);
  }

  @Test
  public void testToFloat() {
    JSONString a = new JSONString("12340523450.34534");

    float f = a.toFloat();
    assertEquals((float) 12340523450.34534, f, 0);
  }

  @Test
  public void testToInteger() {
    JSONString a = new JSONString("1234567890");

    int i = a.toInteger();
    assertEquals(1234567890, i, 0);
  }

  @Test
  public void testToJSON() {
    JSONString s = new JSONString("Esteban");

    String a = s.toJSON();

    assertEquals("\"Esteban\"", a);
  }

  @Test
  public void testToJSONArray() throws ElementNotExistException {
    JSONString s = new JSONString("Esteban");

    JSONArray array = s.toJSONArray();

    assertEquals(1, array.size());

    JSONValue element = array.getValue(0);
    assertEquals(new JSONString("Esteban"), element);
  }

  @Test
  public void testToJSONString() {
    JSONString s = new JSONString("Esteban");

    JSONString element = s.toJSONString();
    assertEquals(new JSONString("Esteban"), element);
  }

  @Test
  public void testToList() {
    JSONString s = new JSONString("Esteban");

    List<JSONValue<?>> list = s.toList();

    assertEquals(1, list.size());

    JSONValue element = list.get(0);
    assertEquals(new JSONString("Esteban"), element);
  }

  @Test
  public void testToLong() {
    JSONString a = new JSONString("1234567890123456789");

    long l = a.toLong();
    assertEquals(l, 1234567890123456789L, 0);
  }

  @Test
  public void testToShort() {
    JSONString a = new JSONString("10");

    short s = a.toShort();
    assertEquals(s, (short) 10, 0);
  }

  @Test
  public void testToString() {
    JSONString a = new JSONString("Esteban");

    String s = a.toString();
    assertEquals("Esteban", s);
  }

  @Test
  public void testToStringArray() {
    JSONString a = new JSONString("Esteban");

    String[] array = a.toStringArray();

    assertEquals(1, array.length);

    String s = array[0];
    assertEquals("Esteban", s);
  }

}
