package net.cabezudo.json.values;

import net.cabezudo.json.exceptions.ElementNotExistException;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author <a href="http://cabezudo.net">Esteban Cabezudo</a>
 * @version 0.9, 08/18/2016
 */
public class JSONNullTest {

  @Test
  public void testCompareTo() {
    assertEquals(new JSONNull(), new JSONNull());
  }

  @Test
  public void testGetReferencedElement() {
    JSONNull jsonNull = new JSONNull();
    assertEquals(jsonNull, jsonNull.toReferencedElement());
  }

  @Test
  public void testIsArray() {
    JSONNull jsonNull = new JSONNull();
    assertFalse(jsonNull.isArray());
  }

  @Test
  public void testIsBoolean() {
    JSONNull jsonNull = new JSONNull();
    assertFalse(jsonNull.isBoolean());
  }

  @Test
  public void testIsNotReferenceable() {
    JSONNull jsonNull = new JSONNull();
    assertTrue(jsonNull.isNotReferenceable());
  }

  @Test
  public void testIsNull() {
    JSONNull jsonNull = new JSONNull();
    assertTrue(jsonNull.isNull());
  }

  @Test
  public void testIsNumber() {
    JSONNull jsonNull = new JSONNull();
    assertFalse(jsonNull.isNumber());
  }

  @Test
  public void testIsObject() {
    JSONNull jsonNull = new JSONNull();
    assertFalse(jsonNull.isObject());
  }

  @Test
  public void testIsReferenceable() {
    JSONNull jsonNull = new JSONNull();
    assertFalse(jsonNull.isReferenceable());
  }

  @Test
  public void testIsString() {
    JSONNull jsonNull = new JSONNull();
    assertFalse(jsonNull.isString());
  }

  @Test
  public void testIsValue() {
    JSONNull jsonNull = new JSONNull();
    assertTrue(jsonNull.isValue());
  }

  @Test
  public void testToJSON() {
    assertEquals("null", new JSONNull().toJSON());
  }

  @Test
  public void testToJSONArray() throws ElementNotExistException {
    JSONNull jsonNull = new JSONNull();
    JSONArray array = jsonNull.toJSONArray();
    assertEquals(1, array.size());
    assertEquals(new JSONNull(), array.getValue(0));
  }

  @Test
  public void testToJSONString() {
    assertEquals(new JSONString("null"), new JSONNull().toJSONString());
  }

  @Test
  public void testToJSONTree() {
    assertEquals(new JSONNull(), new JSONNull().toJSONTree());
  }

  @Test
  public void testToList() {
    JSONNull jsonNull = new JSONNull();
    List<JSONValue<?>> array = jsonNull.toList();
    assertEquals(1, array.size());
    assertEquals(new JSONNull(), array.get(0));
  }

  @Test
  public void testToString() {
    JSONNull jsonNull = new JSONNull();
    String string = jsonNull.toString();
    assertEquals("null", string);
  }

  @Test
  public void testToStringArray() {
    JSONNull jsonNull = new JSONNull();
    String[] array = jsonNull.toStringArray();
    assertEquals(1, array.length);
    assertEquals("null", array[0]);
  }
}
