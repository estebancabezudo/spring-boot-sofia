package net.cabezudo.json.values;

import net.cabezudo.json.exceptions.ElementNotExistException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author <a href="http://cabezudo.net">Esteban Cabezudo</a>
 * @version 0.9, 08/18/2016
 */
public class JSONBooleanTest {

  @Test
  public void testEquals() {
    JSONBoolean a = JSONBoolean.get(true);
    JSONBoolean b = JSONBoolean.get(false);
    JSONBoolean c = JSONBoolean.TRUE;
    JSONBoolean d = JSONBoolean.FALSE;
    assertEquals(a, a);
    assertNotEquals(a, b);
    assertEquals(a, c);
    assertNotEquals(a, d);

    assertEquals(b, b);
    assertNotEquals(b, a);
    assertEquals(b, d);
    assertNotEquals(b, c);

    assertEquals(c, a);
    assertNotEquals(c, b);
    assertEquals(c, c);
    assertNotEquals(c, d);

    assertEquals(d, b);
    assertNotEquals(d, a);
    assertEquals(d, d);
    assertNotEquals(d, c);
  }

  @Test
  public void testGetReferencedElement() {
    JSONBoolean e = JSONBoolean.TRUE;
    JSONBoolean r = e.toReferencedElement();

    assertEquals(e, r);
  }

  @Test
  public void testCompareTo() {
    JSONBoolean a = JSONBoolean.TRUE;
    JSONBoolean b = JSONBoolean.TRUE;

    assertEquals(0, a.compareTo(b));
  }

  @Test
  public void testIsArray() {
    JSONBoolean jsonBoolean = JSONBoolean.TRUE;
    assertEquals(false, jsonBoolean.isArray());
  }

  @Test
  public void testIsBoolean() {
    JSONBoolean jsonBoolean = JSONBoolean.TRUE;
    assertEquals(true, jsonBoolean.isBoolean());
  }

  @Test
  public void testToTrueBigDecimal() {
    JSONBoolean jsonBoolean = JSONBoolean.TRUE;
    BigDecimal a = jsonBoolean.toBigDecimal();
    BigDecimal b = new BigDecimal(1);

    assertEquals(a, b);
  }

  @Test
  public void testToFalseBigDecimal() {
    JSONBoolean jsonBoolean = JSONBoolean.FALSE;
    BigDecimal a = jsonBoolean.toBigDecimal();
    BigDecimal b = new BigDecimal(0);

    assertEquals(a, b);
  }

  @Test
  public void testToTrueBigInteger() {
    JSONBoolean jsonBoolean = JSONBoolean.TRUE;
    BigInteger a = jsonBoolean.toBigInteger();
    BigInteger b = new BigInteger("1");

    assertEquals(a, b);
  }

  @Test
  public void testToFalseBigInteger() {
    JSONBoolean jsonBoolean = JSONBoolean.FALSE;
    BigInteger a = jsonBoolean.toBigInteger();
    BigInteger b = new BigInteger("0");

    assertEquals(a, b);
  }

  @Test
  public void testIsNotReferenceable() {
    JSONBoolean jsonBoolean = JSONBoolean.TRUE;
    assertEquals(true, jsonBoolean.isNotReferenceable());
  }

  @Test
  public void testIsNull() {
    JSONBoolean jsonBoolean = JSONBoolean.TRUE;
    assertEquals(false, jsonBoolean.isNull());
  }

  @Test
  public void testIsNumber() {
    JSONBoolean jsonBoolean = JSONBoolean.TRUE;
    assertEquals(false, jsonBoolean.isNumber());
  }

  @Test
  public void testIsObject() {
    JSONBoolean jsonBoolean = JSONBoolean.TRUE;
    assertEquals(false, jsonBoolean.isObject());
  }

  @Test
  public void testIsReferenceable() {
    JSONBoolean jsonBoolean = JSONBoolean.TRUE;
    assertEquals(false, jsonBoolean.isReferenceable());
  }

  @Test
  public void testIsString() {
    JSONBoolean jsonBoolean = JSONBoolean.TRUE;
    assertEquals(false, jsonBoolean.isString());
  }

  @Test
  public void testIsValue() {
    JSONBoolean jsonBoolean = JSONBoolean.TRUE;
    assertEquals(true, jsonBoolean.isValue());
  }

  @Test
  public void testToBoolean() {
    JSONBoolean a = JSONBoolean.get(true);
    JSONBoolean b = JSONBoolean.get(false);
    JSONBoolean c = JSONBoolean.TRUE;
    JSONBoolean d = JSONBoolean.FALSE;
    assertEquals(true, a.toBoolean());
    assertEquals(false, b.toBoolean());
    assertEquals(true, c.toBoolean());
    assertEquals(false, d.toBoolean());
  }

  @Test
  public void testToTrueByte() {
    JSONBoolean jsonBoolean = JSONBoolean.TRUE;
    Byte a = jsonBoolean.toByte();
    Byte b = 1;

    assertEquals(a, b);
  }

  @Test
  public void testToFalseByte() {
    JSONBoolean jsonBoolean = JSONBoolean.FALSE;
    Byte a = jsonBoolean.toByte();
    Byte b = 0;

    assertEquals(a, b);
  }

  @Test
  public void testToTrueCharacter() {
    JSONBoolean jsonBoolean = JSONBoolean.TRUE;
    Character a = jsonBoolean.toCharacter();
    Character b = '1';

    assertEquals(a, b);
  }

  @Test
  public void testToFalseCharacter() {
    JSONBoolean jsonBoolean = JSONBoolean.FALSE;
    Character a = jsonBoolean.toCharacter();
    Character b = '0';

    assertEquals(a, b);
  }

  @Test
  public void testToTrueDouble() {
    JSONBoolean jsonBoolean = JSONBoolean.TRUE;
    Double a = jsonBoolean.toDouble();
    Double b = 1d;

    assertEquals(a, b);
  }

  @Test
  public void testToFalseDouble() {
    JSONBoolean jsonBoolean = JSONBoolean.FALSE;
    Double a = jsonBoolean.toDouble();
    Double b = 0d;

    assertEquals(a, b);
  }

  @Test
  public void testToTrueFloat() {
    JSONBoolean jsonBoolean = JSONBoolean.TRUE;
    Float a = jsonBoolean.toFloat();
    Float b = 1f;

    assertEquals(a, b);
  }

  @Test
  public void testToFalseFloat() {
    JSONBoolean jsonBoolean = JSONBoolean.FALSE;
    Float a = jsonBoolean.toFloat();
    Float b = 0f;

    assertEquals(a, b);
  }

  @Test
  public void testToTrueInteger() {
    JSONBoolean jsonBoolean = JSONBoolean.TRUE;
    Integer a = jsonBoolean.toInteger();
    Integer b = 1;

    assertEquals(a, b);
  }

  @Test
  public void testToFalseInteger() {
    JSONBoolean jsonBoolean = JSONBoolean.FALSE;
    Integer a = jsonBoolean.toInteger();
    Integer b = 0;

    assertEquals(a, b);
  }

  @Test
  public void testToJSON() {
    JSONBoolean a = JSONBoolean.TRUE;
    JSONBoolean b = JSONBoolean.FALSE;

    String aStirng = a.toJSON();
    String bStirng = b.toJSON();

    assertEquals("true", aStirng);
    assertEquals("false", bStirng);
  }

  @Test
  public void testToJSONArray() throws ElementNotExistException {
    JSONBoolean b = JSONBoolean.TRUE;

    JSONArray array = b.toJSONArray();

    assertEquals(1, array.size());
    assertEquals(JSONBoolean.TRUE, array.getValue(0));
  }

  @Test
  public void testToJSONString() {
    JSONBoolean a = JSONBoolean.TRUE;
    JSONBoolean b = JSONBoolean.FALSE;

    JSONString jsonTrue = a.toJSONString();
    JSONString jsonFalse = b.toJSONString();

    assertEquals(new JSONString("true"), jsonTrue);
    assertEquals(new JSONString("false"), jsonFalse);
  }

  @Test
  public void testToList() {
    JSONBoolean b = JSONBoolean.TRUE;

    List<JSONValue> list = b.toList();

    assertEquals(1, list.size());
    assertEquals(JSONBoolean.TRUE, list.get(0));
  }

  @Test
  public void testToTrueLong() {
    JSONBoolean jsonBoolean = JSONBoolean.TRUE;
    Long a = jsonBoolean.toLong();
    Long b = 1L;

    assertEquals(a, b);
  }

  @Test
  public void testToFalseLong() {
    JSONBoolean jsonBoolean = JSONBoolean.FALSE;
    Long a = jsonBoolean.toLong();
    Long b = 0L;

    assertEquals(a, b);
  }

  @Test
  public void testToTrueShort() {
    JSONBoolean jsonBoolean = JSONBoolean.TRUE;
    Short a = jsonBoolean.toShort();
    Short b = 1;

    assertEquals(a, b);
  }

  @Test
  public void testToFalseShort() {
    JSONBoolean jsonBoolean = JSONBoolean.FALSE;
    Short a = jsonBoolean.toShort();
    Short b = 0;

    assertEquals(a, b);
  }

  @Test
  public void testToStringArray() {
    JSONBoolean jsonBoolean = JSONBoolean.TRUE;
    String[] a = jsonBoolean.toStringArray();

    assertEquals(1, a.length);
    assertEquals("true", a[0]);
  }

}
