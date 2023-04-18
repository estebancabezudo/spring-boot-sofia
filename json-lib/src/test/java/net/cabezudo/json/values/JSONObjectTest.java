package net.cabezudo.json.values;

import net.cabezudo.json.JSON;
import net.cabezudo.json.JSONElement;
import net.cabezudo.json.JSONPair;
import net.cabezudo.json.Log;
import net.cabezudo.json.Position;
import net.cabezudo.json.exceptions.DuplicateKeyException;
import net.cabezudo.json.exceptions.ElementNotExistException;
import net.cabezudo.json.exceptions.JSONParseException;
import net.cabezudo.json.exceptions.PropertyIndexNotExistException;
import net.cabezudo.json.exceptions.PropertyNotExistException;
import net.cabezudo.json.objects.Book;
import net.cabezudo.json.objects.BookList;
import net.cabezudo.json.objects.Data;
import net.cabezudo.json.objects.DigTypes;
import net.cabezudo.json.objects.Storage;
import net.cabezudo.json.objects.Types;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.util.Calendar;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * @author <a href="http://cabezudo.net">Esteban Cabezudo</a>
 * @version 0.9, 08/18/2016
 */
public class JSONObjectTest {

  private final int BIG_DECIMAL = 0;
  private final int BIG_INTEGER = 1;
  private final int BOOK = 2;
  private final int BOOLEAN = 15;
  private final int BYTE = 16;
  private final int BYTE_ARRAY = 3;
  private final int CALENDAR = 4;
  private final int CHARACTER = 17;
  private final int DATE = 5;
  private final int DOUBLE = 18;
  private final int FLOAT = 19;
  private final int INTEGER = 20;
  private final int LONG = 21;
  private final int NULL_REFERENCE = 6;
  private final int OBJECT_BOOLEAN = 7;
  private final int OBJECT_BYTE = 8;
  private final int OBJECT_CHARACTER = 9;
  private final int OBJECT_DOUBLE = 10;
  private final int OBJECT_FLOAT = 11;
  private final int OBJECT_INTEGER = 12;
  private final int OBJECT_LONG = 13;
  private final int OBJECT_SHORT = 14;
  private final int SHORT = 22;
  private final int STRING = 23;

  @Test
  public void testConstructorParseException() throws DuplicateKeyException, JSONParseException {
    JSONParseException jsonParseException = Assertions.assertThrows(JSONParseException.class, () -> {
      JSONObject jsonObject = new JSONObject("testConstructorParseException", "[1, 3, 4, 5]");
    });
    Assertions.assertNotNull(jsonParseException);
  }

  @Test
  public void testPositionConstructor() {
    JSONObject jsonObject = new JSONObject(new Position("testPositionConstructor"));
  }

  @Test
  public void testPairsConstructor() throws DuplicateKeyException, JSONParseException {
    JSONPair a = new JSONPair("a", "a");
    JSONPair b = new JSONPair("b", "b");
    JSONPair c = new JSONPair("c", "c");
    JSONObject jsonObjectExpected = new JSONObject("testPairsConstructor", "{\"a\": \"a\", \"b\":\"b\", \"c\":\"c\"}");
    JSONObject jsonObjectCreated = new JSONObject(a, b, c);
    System.out.println(jsonObjectExpected);
    System.out.println(jsonObjectCreated);
    assertEquals(jsonObjectExpected.toString(), jsonObjectCreated.toString());
  }

  @Test
  public void testAdd() throws DuplicateKeyException {
    JSONObject jsonObject = new JSONObject();
    JSONPair jsonNamePair = new JSONPair("name", "Esteban");
    jsonObject.add(jsonNamePair);
    JSONPair jsonNumberOfLegsPair = new JSONPair("numberOfLegs", 2);
    jsonObject.add(jsonNumberOfLegsPair);

    String s = jsonObject.toJSON();
    assertEquals("{ \"name\": \"Esteban\", \"numberOfLegs\": 2 }", s);
  }

  @Test
  public void testAddTheSameKey() {
    DuplicateKeyException duplicateKeyException = Assertions.assertThrows(DuplicateKeyException.class, () -> {
      JSONObject jsonObject = new JSONObject();
      JSONPair jsonNamePair = new JSONPair("name", "Esteban");
      jsonObject.add(jsonNamePair);
      JSONPair jsonNumberOfLegsPair = new JSONPair("numberOfLegs", 2);
      jsonObject.add(jsonNumberOfLegsPair);
      jsonNamePair = new JSONPair("name", "Esteban");
      jsonObject.add(jsonNamePair);
    });
    Assertions.assertNotNull(duplicateKeyException);


  }

  @Test
  public void testGetKeyList() throws DuplicateKeyException, JSONParseException {
    JSONObject jsonObject = new JSONObject("testGetKeyList", "{\"a\": \"a\", \"b\":\"b\", \"c\":\"c\"}");
    List<String> keyList = jsonObject.getKeyList();
    assertEquals("a", keyList.get(0));
    assertEquals("b", keyList.get(1));
    assertEquals("c", keyList.get(2));
  }

  @Test
  public void testDeleteElementUsingAnIndex() throws DuplicateKeyException {
    JSONObject jsonObject = new JSONObject();
    JSONPair jsonNamePair = new JSONPair("name", "Esteban");
    jsonObject.add(jsonNamePair);
    JSONPair jsonNumberOfLegsPair = new JSONPair("numberOfLegs", 2);
    jsonObject.add(jsonNumberOfLegsPair);

    String s;

    s = jsonObject.toJSON();
    assertEquals("{ \"name\": \"Esteban\", \"numberOfLegs\": 2 }", s);

    jsonObject.remove(0);

    s = jsonObject.toJSON();
    assertEquals("{ \"numberOfLegs\": 2 }", s);
  }

  @Test
  public void testDeleteElementUsingThePropertyName() throws DuplicateKeyException {
    JSONObject jsonObject = new JSONObject();
    JSONPair jsonNamePair = new JSONPair("name", "Esteban");
    jsonObject.add(jsonNamePair);
    JSONPair jsonNumberOfLegsPair = new JSONPair("numberOfLegs", 2);
    jsonObject.add(jsonNumberOfLegsPair);

    String s;

    s = jsonObject.toJSON();
    assertEquals("{ \"name\": \"Esteban\", \"numberOfLegs\": 2 }", s);

    jsonObject.remove("name");

    s = jsonObject.toJSON();
    assertEquals("{ \"numberOfLegs\": 2 }", s);
  }

  @Test
  public void testDigBoolean() throws PropertyNotExistException, DuplicateKeyException {
    DigTypes digTypes = new DigTypes();
    JSONObject jsonObject = new JSONObject(digTypes);

    boolean b = jsonObject.digBoolean("types.pBoolean");
    assertFalse(b);
  }

  @Test
  public void testDigByte() throws PropertyNotExistException, DuplicateKeyException {
    DigTypes digTypes = new DigTypes();
    JSONObject jsonObject = new JSONObject(digTypes);

    byte b = jsonObject.digByte("types.pByte");
    assertEquals(1, b);
  }

  @Test
  public void testDigChar() throws PropertyNotExistException, DuplicateKeyException {
    DigTypes digTypes = new DigTypes();
    JSONObject jsonObject = new JSONObject(digTypes);

    char c = jsonObject.digCharacter("types.pCharacter");
    assertEquals('a', c);
  }

  @Test
  public void testDigCharacter() throws PropertyNotExistException, DuplicateKeyException {
    DigTypes digTypes = new DigTypes();
    JSONObject jsonObject = new JSONObject(digTypes);

    char c = jsonObject.digCharacter("types.oCharacter");
    assertEquals('b', c);
  }

  @Test
  public void testDigDouble() throws PropertyNotExistException, DuplicateKeyException {
    DigTypes digTypes = new DigTypes();
    JSONObject jsonObject = new JSONObject(digTypes);

    double d = jsonObject.digDouble("types.pDouble");
    assertEquals(11.5, d, 0);
  }

  @Test
  public void testDigFloat() throws PropertyNotExistException, DuplicateKeyException {
    DigTypes digTypes = new DigTypes();
    JSONObject jsonObject = new JSONObject(digTypes);

    Float f = jsonObject.digFloat("types.pFloat");
    assertEquals(9.5, f, 0);
  }

  @Test
  public void testDigInt() throws PropertyNotExistException, DuplicateKeyException {
    DigTypes digTypes = new DigTypes();
    JSONObject jsonObject = new JSONObject(digTypes);

    int i = jsonObject.digInteger("types.pInteger");
    assertEquals(5, i);
  }

  @Test
  public void testDigInteger() throws PropertyNotExistException, DuplicateKeyException {
    DigTypes digTypes = new DigTypes();
    JSONObject jsonObject = new JSONObject(digTypes);

    int i = jsonObject.digInteger("types.oInteger");
    assertEquals(6, i);
  }

  @Test
  public void testDigLong() throws PropertyNotExistException, DuplicateKeyException {
    DigTypes digTypes = new DigTypes();
    JSONObject jsonObject = new JSONObject(digTypes);

    long l = jsonObject.digLong("types.pLong");
    assertEquals(7, l);
  }

  @Test
  public void testDigNullByte() throws DuplicateKeyException {
    DigTypes digTypes = new DigTypes();
    JSONObject jsonObject = new JSONObject(digTypes);

    Object o = jsonObject.digNullByte("noExiste");
    assertNull(o);

    byte b = jsonObject.digNullByte("types.pByte");
    assertEquals(1, b);
  }

  @Test
  public void testDigNullChar() throws DuplicateKeyException {
    DigTypes digTypes = new DigTypes();
    JSONObject jsonObject = new JSONObject(digTypes);

    Object o = jsonObject.digNullCharacter("noExiste");
    assertNull(o);

    char c = jsonObject.digNullCharacter("types.pCharacter");
    assertEquals('a', c);
  }

  @Test
  public void testDigNullCharacter() throws DuplicateKeyException {
    DigTypes digTypes = new DigTypes();
    JSONObject jsonObject = new JSONObject(digTypes);

    char c = jsonObject.digNullCharacter("types.oCharacter");
    assertEquals('b', c);
  }

  @Test
  public void testDigNullDouble() throws DuplicateKeyException {
    DigTypes digTypes = new DigTypes();
    JSONObject jsonObject = new JSONObject(digTypes);

    Object o = jsonObject.digNullDouble("noExiste");
    assertNull(o);

    double d = jsonObject.digNullDouble("types.pDouble");
    assertEquals(11.5, d, 0);
  }

  @Test
  public void testDigNullFloat() throws DuplicateKeyException {
    DigTypes digTypes = new DigTypes();
    JSONObject jsonObject = new JSONObject(digTypes);

    Object o = jsonObject.digNullFloat("noExiste");
    assertNull(o);

    Float f = jsonObject.digNullFloat("types.pFloat");
    assertEquals(9.5, f, 0);
  }

  @Test
  public void testDigNullInt() throws DuplicateKeyException {
    DigTypes digTypes = new DigTypes();
    JSONObject jsonObject = new JSONObject(digTypes);

    Object o = jsonObject.digNullInteger("noExiste");
    assertNull(o);

    int i = jsonObject.digNullInteger("types.pInteger");
    assertEquals(5, i);
  }

  @Test
  public void testDigNullInteger() throws DuplicateKeyException {
    DigTypes digTypes = new DigTypes();
    JSONObject jsonObject = new JSONObject(digTypes);

    int i = jsonObject.digNullInteger("types.oInteger");
    assertEquals(6, i);
  }

  @Test
  public void testDigNullLong() throws DuplicateKeyException {
    DigTypes digTypes = new DigTypes();
    JSONObject jsonObject = new JSONObject(digTypes);

    Object o = jsonObject.digNullLong("noExiste");
    assertNull(o);

    long l = jsonObject.digNullLong("types.pLong");
    assertEquals(7, l);
  }

  @Test
  public void testDigNullObjectByte() throws DuplicateKeyException {
    DigTypes digTypes = new DigTypes();
    JSONObject jsonObject = new JSONObject(digTypes);

    Object o = jsonObject.digNullByte("noExiste");
    assertNull(o);

    byte b = jsonObject.digNullByte("types.oByte");
    assertEquals(2, b);
  }

  @Test
  public void testDigNullObjectDouble() throws DuplicateKeyException {
    DigTypes digTypes = new DigTypes();
    JSONObject jsonObject = new JSONObject(digTypes);

    double d = jsonObject.digNullDouble("types.oDouble");
    assertEquals(12.5, d, 0);
  }

  @Test
  public void testDigNullObjectFloat() throws DuplicateKeyException {
    DigTypes digTypes = new DigTypes();
    JSONObject jsonObject = new JSONObject(digTypes);

    Float f = jsonObject.digNullFloat("types.oFloat");
    assertEquals(10.5, f, 0);
  }

  @Test
  public void testDigNullObjectLong() throws DuplicateKeyException {
    DigTypes digTypes = new DigTypes();
    JSONObject jsonObject = new JSONObject(digTypes);

    long l = jsonObject.digNullLong("types.oLong");
    assertEquals(8, l);
  }

  @Test
  public void testDigNullObjectShort() throws DuplicateKeyException {
    DigTypes digTypes = new DigTypes();
    JSONObject jsonObject = new JSONObject(digTypes);

    short s = jsonObject.digNullShort("types.oShort");
    assertEquals(4, s);
  }

  @Test
  public void testDigNullShort() throws DuplicateKeyException {
    DigTypes digTypes = new DigTypes();
    JSONObject jsonObject = new JSONObject(digTypes);

    Object o = jsonObject.digNullShort("noExiste");
    assertNull(o);

    short s = jsonObject.digNullShort("types.pShort");
    assertEquals(3, s);
  }

  @Test
  public void testDigNullString() throws DuplicateKeyException {
    DigTypes digTypes = new DigTypes();
    JSONObject jsonObject = new JSONObject(digTypes);

    Object o = jsonObject.digNullString("noExiste");
    assertNull(o);

    String s = jsonObject.digNullString("types.string");
    assertEquals("Esteban", s);
  }

  @Test
  public void testDigObjectBoolean() throws PropertyNotExistException, DuplicateKeyException {
    DigTypes digTypes = new DigTypes();
    JSONObject jsonObject = new JSONObject(digTypes);

    boolean b = jsonObject.digBoolean("types.oBoolean");
    assertTrue(b);
  }

  @Test
  public void testDigObjectByte() throws PropertyNotExistException, DuplicateKeyException {
    DigTypes digTypes = new DigTypes();
    JSONObject jsonObject = new JSONObject(digTypes);

    byte b = jsonObject.digByte("types.oByte");
    assertEquals(2, b);
  }

  @Test
  public void testDigObjectDouble() throws PropertyNotExistException, DuplicateKeyException {
    DigTypes digTypes = new DigTypes();
    JSONObject jsonObject = new JSONObject(digTypes);

    double d = jsonObject.digDouble("types.oDouble");
    assertEquals(12.5, d, 0);
  }

  @Test
  public void testDigObjectFloat() throws PropertyNotExistException, DuplicateKeyException {
    DigTypes digTypes = new DigTypes();
    JSONObject jsonObject = new JSONObject(digTypes);

    Float f = jsonObject.digFloat("types.oFloat");
    assertEquals(10.5, f, 0);
  }

  @Test
  public void testDigObjectLong() throws PropertyNotExistException, DuplicateKeyException {
    DigTypes digTypes = new DigTypes();
    JSONObject jsonObject = new JSONObject(digTypes);

    long l = jsonObject.digLong("types.oLong");
    assertEquals(8, l);
  }

  @Test
  public void testDigObjectShort() throws PropertyNotExistException, DuplicateKeyException {
    DigTypes digTypes = new DigTypes();
    JSONObject jsonObject = new JSONObject(digTypes);

    short s = jsonObject.digShort("types.oShort");
    assertEquals(4, s);
  }

  @Test
  public void testDigShort() throws PropertyNotExistException, DuplicateKeyException {
    DigTypes digTypes = new DigTypes();
    JSONObject jsonObject = new JSONObject(digTypes);

    short s = jsonObject.digShort("types.pShort");
    assertEquals(3, s);
  }

  @Test
  public void testDigString() throws PropertyNotExistException, DuplicateKeyException {
    DigTypes digTypes = new DigTypes();
    JSONObject jsonObject = new JSONObject(digTypes);

    String s = jsonObject.digString("types.string");
    assertEquals("Esteban", s);
  }

  @Test
  public void testGetBigDecimalUsingThePropertyIndex() throws PropertyIndexNotExistException, DuplicateKeyException {
    Types types = new Types();
    JSONObject jsonObject = new JSONObject(types);

    BigDecimal b = jsonObject.getBigDecimal(BIG_DECIMAL);
    BigDecimal bigDecimal = new BigDecimal("15.4", MathContext.DECIMAL32).setScale(JSONNumber.DEFAULT_SCALE).stripTrailingZeros();
    assertEquals(bigDecimal, b);
  }

  @Test
  public void testGetBigDecimalUsingThePropertyName() throws PropertyNotExistException, DuplicateKeyException {
    Types types = new Types();
    JSONObject jsonObject = new JSONObject(types);

    BigDecimal b = jsonObject.getBigDecimal("bigDecimal");
    BigDecimal bigDecimal = new BigDecimal("15.4", MathContext.DECIMAL32).setScale(JSONNumber.DEFAULT_SCALE).stripTrailingZeros();
    assertEquals(bigDecimal, b);
  }

  @Test
  public void testGetBigIntegerUsingThePropertyIndex() throws PropertyIndexNotExistException, DuplicateKeyException {
    Types types = new Types();
    JSONObject jsonObject = new JSONObject(types);

    BigInteger b = jsonObject.getBigInteger(BIG_INTEGER);
    assertEquals(new BigInteger("14"), b);
  }

  @Test
  public void testGetBigIntegerUsingThePropertyName() throws PropertyNotExistException, DuplicateKeyException {
    Types types = new Types();
    JSONObject jsonObject = new JSONObject(types);

    BigInteger b = jsonObject.getBigInteger("bigInteger");
    assertEquals(new BigInteger("14"), b);
  }

  @Test
  public void testGetBooleanObjectUsingThePropertyName() throws PropertyNotExistException, DuplicateKeyException {
    Types types = new Types();
    JSONObject jsonObject = new JSONObject(types);

    boolean b = jsonObject.getBoolean("oBoolean");
    assertTrue(b);
  }

  @Test
  public void testGetBooleanUsingThePropertyIndex() throws PropertyIndexNotExistException, DuplicateKeyException {
    Types types = new Types();
    JSONObject jsonObject = new JSONObject(types);

    boolean b = jsonObject.getBoolean(BOOLEAN);
    assertFalse(b);
  }

  @Test
  public void testGetBooleanUsingThePropertyName() throws PropertyNotExistException, DuplicateKeyException {
    Types types = new Types();
    JSONObject jsonObject = new JSONObject(types);

    boolean b = jsonObject.getBoolean("pBoolean");
    assertFalse(b);
  }

  @Test
  public void testGetByteArrayUsingThePropertyIndex() throws PropertyIndexNotExistException, DuplicateKeyException {
    Types types = new Types();
    JSONObject jsonObject = new JSONObject(types);

    byte[] b = jsonObject.getByteArray(BYTE_ARRAY);
    assertEquals(4, b.length);
    assertEquals(1, b[0]);
    assertEquals(2, b[1]);
    assertEquals(3, b[2]);
    assertEquals(4, b[3]);
  }

  @Test
  public void testGetByteArrayUsingThePropertyName() throws PropertyNotExistException, DuplicateKeyException {
    Types types = new Types();
    JSONObject jsonObject = new JSONObject(types);

    byte[] b = jsonObject.getByteArray("byteArray");
    assertEquals(4, b.length);
    assertEquals(1, b[0]);
    assertEquals(2, b[1]);
    assertEquals(3, b[2]);
    assertEquals(4, b[3]);
  }

  @Test
  public void testGetByteUsingThePropertyIndex() throws PropertyIndexNotExistException, DuplicateKeyException {
    Types types = new Types();
    JSONObject jsonObject = new JSONObject(types);

    byte b = jsonObject.getByte(BYTE);
    assertEquals(1, b);
  }

  @Test
  public void testGetByteUsingThePropertyName() throws PropertyNotExistException, DuplicateKeyException {
    Types types = new Types();
    JSONObject jsonObject = new JSONObject(types);

    byte b = jsonObject.getByte("pByte");
    assertEquals(1, b);
  }

  @Test
  public void testGetCalendarUsingThePropertyIndex() throws PropertyIndexNotExistException, DuplicateKeyException {
    Types types = new Types();
    JSONObject jsonObject = new JSONObject(types);

    Calendar expectedCalendar = Calendar.getInstance();
    expectedCalendar.set(1974, 0, 30, 14, 20, 12);
    expectedCalendar.set(Calendar.MILLISECOND, 125);

    Calendar calendar = jsonObject.getCalendar(CALENDAR);
    assertEquals(expectedCalendar, calendar);
  }

  @Test
  public void testGetCalendarUsingThePropertyName() throws PropertyNotExistException, DuplicateKeyException {
    Types types = new Types();
    JSONObject jsonObject = new JSONObject(types);

    Calendar expectedCalendar = Calendar.getInstance();
    expectedCalendar.set(1974, 0, 30, 14, 20, 12);
    expectedCalendar.set(Calendar.MILLISECOND, 125);

    Calendar calendar = jsonObject.getCalendar("calendar");
    assertEquals(expectedCalendar, calendar);
  }

  @Test
  public void testGetCharUsingThePropertyIndex() throws PropertyIndexNotExistException, DuplicateKeyException {
    Types types = new Types();
    JSONObject jsonObject = new JSONObject(types);

    char c = jsonObject.getCharacter(CHARACTER);
    assertEquals('a', c);
  }

  @Test
  public void testGetCharUsingThePropertyName() throws PropertyNotExistException, DuplicateKeyException {
    Types types = new Types();
    JSONObject jsonObject = new JSONObject(types);

    char c = jsonObject.getCharacter("pCharacter");
    assertEquals('a', c);
  }

  @Test
  public void testGetCharacterUsingThePropertyIndex() throws PropertyIndexNotExistException, DuplicateKeyException {
    Types types = new Types();
    JSONObject jsonObject = new JSONObject(types);

    char c = jsonObject.getCharacter(OBJECT_CHARACTER);
    assertEquals('b', c);
  }

  @Test
  public void testGetCharacterUsingThePropertyName() throws PropertyNotExistException, DuplicateKeyException {
    Types types = new Types();
    JSONObject jsonObject = new JSONObject(types);

    char c = jsonObject.getCharacter("oCharacter");
    assertEquals('b', c);
  }

  @Test
  public void testGetChilds() throws PropertyNotExistException, ElementNotExistException, DuplicateKeyException {
    Types types = new Types();
    JSONObject jsonObject = new JSONObject(types);

    List<JSONPair> childs = jsonObject.getChilds();
    assertEquals(24, childs.size());

    JSONPair pair;

    pair = childs.get(BIG_DECIMAL);
    assertEquals("bigDecimal", pair.getKey());
    assertEquals(new JSONNumber(15.4), pair.getValue());

    pair = childs.get(BIG_INTEGER);
    assertEquals("bigInteger", pair.getKey());
    assertEquals(new JSONNumber(14), pair.getValue());

    pair = childs.get(BYTE_ARRAY);
    assertEquals("byteArray", pair.getKey());
    JSONArray array = (JSONArray) pair.getValue();
    assertEquals(4, array.size());
    assertEquals(new JSONNumber(1), array.getValue(0));
    assertEquals(new JSONNumber(2), array.getValue(1));
    assertEquals(new JSONNumber(3), array.getValue(2));
    assertEquals(new JSONNumber(4), array.getValue(3));

    pair = childs.get(CALENDAR);
    assertEquals("calendar", pair.getKey());
    assertEquals(new JSONString("1974-01-30T14:20:12.125Z"), pair.getValue());

    pair = childs.get(DATE);
    assertEquals("date", pair.getKey());
    assertEquals(new JSONString("1974-01-30T14:20:12.125Z"), pair.getValue());

    pair = childs.get(OBJECT_BOOLEAN);
    assertEquals("oBoolean", pair.getKey());
    assertEquals(JSONBoolean.TRUE, pair.getValue());

    pair = childs.get(OBJECT_BYTE);
    assertEquals("oByte", pair.getKey());
    assertEquals(new JSONNumber(2), pair.getValue());

    pair = childs.get(OBJECT_CHARACTER);
    assertEquals("oCharacter", pair.getKey());
    assertEquals(new JSONString("b"), pair.getValue());

    pair = childs.get(OBJECT_DOUBLE);
    assertEquals("oDouble", pair.getKey());
    assertEquals(new JSONNumber(12.5), pair.getValue());

    pair = childs.get(OBJECT_FLOAT);
    assertEquals("oFloat", pair.getKey());
    assertEquals(new JSONNumber(10.5), pair.getValue());

    pair = childs.get(OBJECT_INTEGER);
    assertEquals("oInteger", pair.getKey());
    assertEquals(new JSONNumber(6), pair.getValue());

    pair = childs.get(OBJECT_LONG);
    assertEquals("oLong", pair.getKey());
    assertEquals(new JSONNumber(8), pair.getValue());

    pair = childs.get(OBJECT_SHORT);
    assertEquals("oShort", pair.getKey());
    assertEquals(new JSONNumber(4), pair.getValue());

    pair = childs.get(NULL_REFERENCE);
    assertEquals("nullReference", pair.getKey());
    assertEquals(new JSONNull(), pair.getValue());

    pair = childs.get(BOOLEAN);
    assertEquals("pBoolean", pair.getKey());
    assertEquals(JSONBoolean.get(false), pair.getValue());

    pair = childs.get(BYTE);
    assertEquals("pByte", pair.getKey());
    assertEquals(new JSONNumber(1), pair.getValue());

    pair = childs.get(CHARACTER);
    assertEquals("pCharacter", pair.getKey());
    assertEquals(new JSONString('a'), pair.getValue());

    pair = childs.get(DOUBLE);
    assertEquals("pDouble", pair.getKey());
    assertEquals(new JSONNumber(11.5), pair.getValue());

    pair = childs.get(FLOAT);
    assertEquals("pFloat", pair.getKey());
    assertEquals(new JSONNumber(9.5), pair.getValue());

    pair = childs.get(INTEGER);
    assertEquals("pInteger", pair.getKey());
    assertEquals(new JSONNumber(5), pair.getValue());

    pair = childs.get(LONG);
    assertEquals("pLong", pair.getKey());
    assertEquals(new JSONNumber(7), pair.getValue());

    pair = childs.get(SHORT);
    assertEquals("pShort", pair.getKey());
    assertEquals(new JSONNumber(3), pair.getValue());

    pair = childs.get(STRING);
    assertEquals("string", pair.getKey());
    assertEquals(new JSONString("Esteban"), pair.getValue());

    pair = childs.get(BOOK);
    assertEquals("book", pair.getKey());
    JSONObject jsonBook = pair.getValue().toJSONObject();
    int id = jsonBook.getInteger("id");
    String name = jsonBook.getString("name");
    assertEquals(1, id);
    assertEquals("Evolution", name);
  }

  @Test
  public void testGetDoubleUsingThePropertyIndex() throws PropertyIndexNotExistException, DuplicateKeyException {
    Types types = new Types();
    JSONObject jsonObject = new JSONObject(types);

    double d = jsonObject.getDouble(DOUBLE);
    assertEquals(11.5, d, 0);
  }

  @Test
  public void testGetDoubleUsingThePropertyName() throws PropertyNotExistException, DuplicateKeyException {
    Types types = new Types();
    JSONObject jsonObject = new JSONObject(types);

    double d = jsonObject.getDouble("pDouble");
    assertEquals(11.5, d, 0);
  }

  @Test
  public void testGetElementUsingThePropertyIndex() throws PropertyIndexNotExistException, DuplicateKeyException {
    Types types = new Types();
    JSONObject jsonObject = new JSONObject(types);

    JSONPair pair = jsonObject.getElement(STRING);
    assertEquals(new JSONPair("string", "Esteban"), pair);
  }

  @Test
  public void testGetElementUsingThePropertyName() throws PropertyNotExistException, DuplicateKeyException {
    Types types = new Types();
    JSONObject jsonObject = new JSONObject(types);

    JSONPair pair = jsonObject.getElement("string");
    assertEquals(new JSONPair("string", "Esteban"), pair);
  }

  @Test
  public void testGetFloatUsingThePropertyIndex() throws PropertyIndexNotExistException, DuplicateKeyException {
    Types types = new Types();
    JSONObject jsonObject = new JSONObject(types);

    float f = jsonObject.getFloat(FLOAT);
    assertEquals(9.5, f, 0);
  }

  @Test
  public void testGetFloatUsingThePropertyName() throws PropertyNotExistException, DuplicateKeyException {
    Types types = new Types();
    JSONObject jsonObject = new JSONObject(types);

    float f = jsonObject.getFloat("pFloat");
    assertEquals(9.5, f, 0);
  }

  @Test
  public void testGetIntUsingThePropertyIndex() throws PropertyIndexNotExistException, DuplicateKeyException {
    Types types = new Types();
    JSONObject jsonObject = new JSONObject(types);

    int i = jsonObject.getInteger(INTEGER);
    assertEquals(5, i);
  }

  @Test
  public void testGetIntUsingThePropertyName() throws PropertyNotExistException, DuplicateKeyException {
    Types types = new Types();
    JSONObject jsonObject = new JSONObject(types);

    int i = jsonObject.getInteger("pInteger");
    assertEquals(5, i);
  }

  @Test
  public void testGetIntegerUsingThePropertyIndex() throws PropertyIndexNotExistException, DuplicateKeyException {
    Types types = new Types();
    JSONObject jsonObject = new JSONObject(types);

    int i = jsonObject.getInteger(OBJECT_INTEGER);
    assertEquals(6, i);
  }

  @Test
  public void testGetIntegerUsingThePropertyName() throws PropertyNotExistException, DuplicateKeyException {
    Types types = new Types();
    JSONObject jsonObject = new JSONObject(types);

    int i = jsonObject.getInteger("oInteger");
    assertEquals(6, i);
  }

  @Test
  public void testGetJSONArrayUsingThePropertyIndex() throws ElementNotExistException, PropertyIndexNotExistException, DuplicateKeyException {
    Types types = new Types();
    JSONObject jsonObject = new JSONObject(types);

    JSONArray array = jsonObject.getJSONArray(BYTE_ARRAY);
    assertEquals(4, array.size());
    assertEquals(new JSONNumber(1), array.getValue(0));
    assertEquals(new JSONNumber(2), array.getValue(1));
    assertEquals(new JSONNumber(3), array.getValue(2));
    assertEquals(new JSONNumber(4), array.getValue(3));
  }

  @Test
  public void testGetJSONArrayUsingThePropertyName() throws ElementNotExistException, PropertyNotExistException, DuplicateKeyException {
    Types types = new Types();
    JSONObject jsonObject = new JSONObject(types);

    JSONArray array = jsonObject.getJSONArray("byteArray");
    assertEquals(4, array.size());
    assertEquals(new JSONNumber(1), array.getValue(0));
    assertEquals(new JSONNumber(2), array.getValue(1));
    assertEquals(new JSONNumber(3), array.getValue(2));
    assertEquals(new JSONNumber(4), array.getValue(3));
  }

  @Test
  public void testGetLongUsingThePropertyIndex() throws PropertyIndexNotExistException, DuplicateKeyException {
    Types types = new Types();
    JSONObject jsonObject = new JSONObject(types);

    long l = jsonObject.getLong(LONG);
    assertEquals(7, l);
  }

  @Test
  public void testGetLongUsingThePropertyName() throws PropertyNotExistException, DuplicateKeyException {
    Types types = new Types();
    JSONObject jsonObject = new JSONObject(types);

    long l = jsonObject.getLong("pLong");
    assertEquals(7, l);
  }

  @Test
  public void testGetNullBigDecimalUsingThePropertyIndex() throws DuplicateKeyException {
    Types types = new Types();
    JSONObject jsonObject = new JSONObject(types);

    BigDecimal b = jsonObject.getNullBigDecimal(BIG_DECIMAL);
    BigDecimal bigDecimal = new BigDecimal("15.4", MathContext.DECIMAL32).setScale(JSONNumber.DEFAULT_SCALE).stripTrailingZeros();
    assertEquals(bigDecimal, b);
  }

  @Test
  public void testGetNullBigDecimalUsingThePropertyName() throws DuplicateKeyException {
    Types types = new Types();
    JSONObject jsonObject = new JSONObject(types);

    Object o = jsonObject.getNullBigDecimal("noExiste");
    assertNull(o);

    BigDecimal b = jsonObject.getNullBigDecimal("bigDecimal");
    BigDecimal bigDecimal = new BigDecimal("15.4", MathContext.DECIMAL32).setScale(JSONNumber.DEFAULT_SCALE).stripTrailingZeros();
    assertEquals(bigDecimal, b);
  }

  @Test
  public void testGetNullBigIntegerUsingThePropertyIndex() throws DuplicateKeyException {
    Types types = new Types();
    JSONObject jsonObject = new JSONObject(types);

    BigInteger b = jsonObject.getNullBigInteger(BIG_INTEGER);
    assertEquals(new BigInteger("14"), b);
  }

  @Test
  public void testGetNullBigIntegerUsingThePropertyName() throws DuplicateKeyException {
    Types types = new Types();
    JSONObject jsonObject = new JSONObject(types);

    BigInteger b = jsonObject.getNullBigInteger("bigInteger");
    assertEquals(new BigInteger("14"), b);
  }

  @Test
  public void testGetNullBooleanUsingThePropertyIndex() throws DuplicateKeyException {
    Types types = new Types();
    JSONObject jsonObject = new JSONObject(types);

    boolean b = jsonObject.getNullBoolean(BOOLEAN);
    assertFalse(b);
  }

  @Test
  public void testGetNullBooleanUsingThePropertyName() throws DuplicateKeyException {
    Types types = new Types();
    JSONObject jsonObject = new JSONObject(types);

    Object o = jsonObject.getNullBoolean("noExiste");
    assertNull(o);

    boolean b = jsonObject.getNullBoolean("pBoolean");
    assertFalse(b);
  }

  @Test
  public void testGetNullByteArray() throws DuplicateKeyException {
    Types types = new Types();
    JSONObject jsonObject = new JSONObject(types);

    Object o = jsonObject.getNullByteArray("noExiste");
    assertNull(o);

    byte[] array = jsonObject.getNullByteArray("byteArray");
    assertEquals(4, array.length);
    assertEquals(1, array[0]);
    assertEquals(2, array[1]);
    assertEquals(3, array[2]);
    assertEquals(4, array[3]);
  }

  @Test
  public void testGetNullByteUsingThePropertyIndex() throws DuplicateKeyException {
    Types types = new Types();
    JSONObject jsonObject = new JSONObject(types);

    byte b = jsonObject.getNullByte(BYTE);
    assertEquals(1, b);
  }

  @Test
  public void testGetNullByteUsingThePropertyName() throws DuplicateKeyException {
    Types types = new Types();
    JSONObject jsonObject = new JSONObject(types);

    Object o = jsonObject.getNullByte("noExiste");
    assertNull(o);

    byte b = jsonObject.getNullByte("pByte");
    assertEquals(1, b);
  }

  @Test
  public void testGetNullCalendarUsingThePropertyIndex() throws PropertyIndexNotExistException, DuplicateKeyException {
    Types types = new Types();
    JSONObject jsonObject = new JSONObject(types);

    Object o = jsonObject.getNullByte("noExiste");
    assertNull(o);

    Calendar expectedCalendar = Calendar.getInstance();
    expectedCalendar.set(1974, 0, 30, 14, 20, 12);
    expectedCalendar.set(Calendar.MILLISECOND, 125);

    Calendar calendar = jsonObject.getCalendar(CALENDAR);
    assertEquals(expectedCalendar, calendar);
  }

  @Test
  public void testGetNullCalendarUsingThePropertyName() throws DuplicateKeyException {
    Types types = new Types();
    JSONObject jsonObject = new JSONObject(types);

    Object o = jsonObject.getNullCalendar("noExiste");
    assertNull(o);

    Calendar expectedCalendar = Calendar.getInstance();
    expectedCalendar.set(1974, 0, 30, 14, 20, 12);
    expectedCalendar.set(Calendar.MILLISECOND, 125);

    Calendar calendar = jsonObject.getNullCalendar("calendar");
    assertEquals(expectedCalendar, calendar);
  }

  @Test
  public void testGetNullCharString() throws DuplicateKeyException {
    Types types = new Types();
    JSONObject jsonObject = new JSONObject(types);

    Object o = jsonObject.getNullCharacter("noExiste");
    assertNull(o);

    char c = jsonObject.getNullCharacter("pCharacter");
    assertEquals('a', c);
  }

  @Test
  public void testGetNullCharUsingThePropertyIndex() throws DuplicateKeyException {
    Types types = new Types();
    JSONObject jsonObject = new JSONObject(types);

    char c = jsonObject.getNullCharacter(CHARACTER);
    assertEquals('a', c);
  }

  @Test
  public void testGetNullCharacterUsingThePropertyIndex() throws DuplicateKeyException {
    Types types = new Types();
    JSONObject jsonObject = new JSONObject(types);

    String a = jsonObject.toJSONArray().toJSON();

    Object o = jsonObject.getNullCharacter("noExiste");
    assertNull(o);

    char c = jsonObject.getNullCharacter(OBJECT_CHARACTER);
    assertEquals('b', c);
  }

  @Test
  public void testGetNullCharacterUsingThePropertyName() throws DuplicateKeyException {
    Types types = new Types();
    JSONObject jsonObject = new JSONObject(types);

    Object o = jsonObject.getNullCharacter("noExiste");
    assertNull(o);

    char c = jsonObject.getNullCharacter("oCharacter");
    assertEquals('b', c);
  }

  @Test
  public void testGetNullDoubleUsingThePropertyIndex() throws DuplicateKeyException {
    Types types = new Types();
    JSONObject jsonObject = new JSONObject(types);

    double d = jsonObject.getNullDouble(DOUBLE);
    assertEquals(11.5, d, 0);
  }

  @Test
  public void testGetNullDoubleUsingThePropertyName() throws DuplicateKeyException {
    Types types = new Types();
    JSONObject jsonObject = new JSONObject(types);

    Object o = jsonObject.getNullDouble("noExiste");
    assertNull(o);

    double d = jsonObject.getNullDouble("pDouble");
    assertEquals(11.5, d, 0);
  }

  @Test
  public void testGetNullElementUsingThePropertyIndex() throws PropertyIndexNotExistException, DuplicateKeyException {
    Types types = new Types();
    JSONObject jsonObject = new JSONObject(types);

    JSONPair pair = jsonObject.getElement(STRING);
    assertEquals(new JSONPair("string", "Esteban"), pair);
  }

  @Test
  public void testGetNullElementUsingThePropertyName() throws DuplicateKeyException {
    Types types = new Types();
    JSONObject jsonObject = new JSONObject(types);

    Object o = jsonObject.getNullElement("noExiste");
    assertNull(o);

    JSONPair pair = jsonObject.getNullElement("string");
    assertEquals(new JSONPair("string", "Esteban"), pair);
  }

  @Test
  public void testGetNullFloatUsingThePropertyIndex() throws DuplicateKeyException {
    Types types = new Types();
    JSONObject jsonObject = new JSONObject(types);

    float f = jsonObject.getNullFloat(FLOAT);
    assertEquals(9.5, f, 0);
  }

  @Test
  public void testGetNullFloatUsingThePropertyName() throws DuplicateKeyException {
    Types types = new Types();
    JSONObject jsonObject = new JSONObject(types);

    Object o = jsonObject.getNullFloat("noExiste");
    assertNull(o);

    float f = jsonObject.getNullFloat("pFloat");
    assertEquals(9.5, f, 0);
  }

  @Test
  public void testGetNullIntUsingThePropertyIndex() throws DuplicateKeyException {
    Types types = new Types();
    JSONObject jsonObject = new JSONObject(types);

    int i = jsonObject.getNullInteger(INTEGER);
    assertEquals(5, i);
  }

  @Test
  public void testGetNullIntUsingThePropertyName() throws DuplicateKeyException {
    Types types = new Types();
    JSONObject jsonObject = new JSONObject(types);

    Object o = jsonObject.getNullInteger("noExiste");
    assertNull(o);

    int i = jsonObject.getNullInteger("pInteger");
    assertEquals(5, i);
  }

  @Test
  public void testGetNullIntegerUsingThePropertyIndex() throws DuplicateKeyException {
    Types types = new Types();
    JSONObject jsonObject = new JSONObject(types);

    int i = jsonObject.getNullInteger(OBJECT_INTEGER);
    assertEquals(6, i);
  }

  @Test
  public void testGetNullIntegerUsingThePropertyName() throws DuplicateKeyException {
    Types types = new Types();
    JSONObject jsonObject = new JSONObject(types);

    Object o = jsonObject.getNullInteger("noExiste");
    assertNull(o);

    int i = jsonObject.getNullInteger("oInteger");
    assertEquals(6, i);
  }

  @Test
  public void testGetNullJSONArrayUsingThePropertyIndex() throws ElementNotExistException, DuplicateKeyException {
    Types types = new Types();
    JSONObject jsonObject = new JSONObject(types);

    JSONArray array = jsonObject.getNullJSONArray(BYTE_ARRAY);
    assertEquals(4, array.size());
    assertEquals(new JSONNumber(1), array.getValue(0));
    assertEquals(new JSONNumber(2), array.getValue(1));
    assertEquals(new JSONNumber(3), array.getValue(2));
    assertEquals(new JSONNumber(4), array.getValue(3));
  }

  @Test
  public void testGetNullJSONArrayUsingThePropertyName() throws ElementNotExistException, DuplicateKeyException {
    Types types = new Types();
    JSONObject jsonObject = new JSONObject(types);

    Object o = jsonObject.getNullJSONArray("noExiste");
    assertNull(o);

    JSONArray array = jsonObject.getNullJSONArray("byteArray");
    assertEquals(4, array.size());
    assertEquals(new JSONNumber(1), array.getValue(0));
    assertEquals(new JSONNumber(2), array.getValue(1));
    assertEquals(new JSONNumber(3), array.getValue(2));
    assertEquals(new JSONNumber(4), array.getValue(3));
  }

  @Test
  public void testGetNullLongUsingThePropertyIndex() throws DuplicateKeyException {
    Types types = new Types();
    JSONObject jsonObject = new JSONObject(types);

    long l = jsonObject.getNullLong(LONG);
    assertEquals(7, l);
  }

  @Test
  public void testGetNullLongUsingThePropertyName() throws DuplicateKeyException {
    Types types = new Types();
    JSONObject jsonObject = new JSONObject(types);

    Object o = jsonObject.getNullLong("noExiste");
    assertNull(o);

    long l = jsonObject.getNullLong("pLong");
    assertEquals(7, l);
  }

  @Test
  public void testGetNullObjectBooleanUsingThePropertyIndex() throws DuplicateKeyException {
    Types types = new Types();
    JSONObject jsonObject = new JSONObject(types);

    boolean b = jsonObject.getNullBoolean(OBJECT_BOOLEAN);
    assertTrue(b);
  }

  @Test
  public void testGetNullObjectBooleanUsingThePropertyName() throws DuplicateKeyException {
    Types types = new Types();
    JSONObject jsonObject = new JSONObject(types);

    boolean b = jsonObject.getNullBoolean("oBoolean");
    assertTrue(b);
  }

  public void testGetNullObjectByteUsingThePropertyIndex() throws DuplicateKeyException {
    Types types = new Types();
    JSONObject jsonObject = new JSONObject(types);

    byte b = jsonObject.getNullByte(OBJECT_BYTE);
    assertEquals(2, b);
  }

  @Test
  public void testGetNullObjectByteUsingThePropertyName() throws DuplicateKeyException {
    Types types = new Types();
    JSONObject jsonObject = new JSONObject(types);

    byte b = jsonObject.getNullByte("oByte");
    assertEquals(2, b);
  }

  @Test
  public void testGetNullObjectDoubleUsingThePropertyIndex() throws DuplicateKeyException {
    Types types = new Types();
    JSONObject jsonObject = new JSONObject(types);

    double d = jsonObject.getNullDouble(OBJECT_DOUBLE);
    assertEquals(12.5, d, 0);
  }

  @Test
  public void testGetNullObjectDoubleUsingThePropertyName() throws DuplicateKeyException {
    Types types = new Types();
    JSONObject jsonObject = new JSONObject(types);

    Object o = jsonObject.getNullDouble("noExiste");
    assertNull(o);

    double d = jsonObject.getNullDouble("oDouble");
    assertEquals(12.5, d, 0);
  }

  @Test
  public void testGetNullObjectFloatUsingThePropertyIndex() throws DuplicateKeyException {
    Types types = new Types();
    JSONObject jsonObject = new JSONObject(types);

    float f = jsonObject.getNullFloat(OBJECT_FLOAT);
    assertEquals(10.5, f, 0);
  }

  @Test
  public void testGetNullObjectFloatUsingThePropertyName() throws DuplicateKeyException {
    Types types = new Types();
    JSONObject jsonObject = new JSONObject(types);

    Object o = jsonObject.getNullFloat("noExiste");
    assertNull(o);

    float f = jsonObject.getNullFloat("oFloat");
    assertEquals(10.5, f, 0);
  }

  @Test
  public void testGetNullObjectLongUsingThePropertyIndex() throws DuplicateKeyException {
    Types types = new Types();
    JSONObject jsonObject = new JSONObject(types);

    long l = jsonObject.getNullLong(OBJECT_LONG);
    assertEquals(8, l);
  }

  @Test
  public void testGetNullObjectLongUsingThePropertyName() throws DuplicateKeyException {
    Types types = new Types();
    JSONObject jsonObject = new JSONObject(types);

    Object o = jsonObject.getNullLong("noExiste");
    assertNull(o);

    long l = jsonObject.getNullLong("oLong");
    assertEquals(8, l);
  }

  @Test
  public void testGetNullObjectShortUsingThePropertyIndex() throws DuplicateKeyException {
    Types types = new Types();
    JSONObject jsonObject = new JSONObject(types);

    short s = jsonObject.getNullShort(OBJECT_SHORT);
    assertEquals(4, s);
  }

  @Test
  public void testGetNullObjectShortUsingThePropertyName() throws DuplicateKeyException {
    Types types = new Types();
    JSONObject jsonObject = new JSONObject(types);

    Object o = jsonObject.getNullShort("noExiste");
    assertNull(o);

    short s = jsonObject.getNullShort("oShort");
    assertEquals(4, s);
  }

  @Test
  public void testGetNullShortUsingThePropertyIndex() throws DuplicateKeyException {
    Types types = new Types();
    JSONObject jsonObject = new JSONObject(types);

    short s = jsonObject.getNullShort(SHORT);
    assertEquals(3, s);
  }

  @Test
  public void testGetNullShortUsingThePropertyName() throws DuplicateKeyException {
    Types types = new Types();
    JSONObject jsonObject = new JSONObject(types);

    Object o = jsonObject.getNullShort("noExiste");
    assertNull(o);

    short s = jsonObject.getNullShort("pShort");
    assertEquals(3, s);
  }

  @Test
  public void testGetNullStringUsingThePropertyIndex() throws DuplicateKeyException {
    Types types = new Types();
    JSONObject jsonObject = new JSONObject(types);

    Object o = jsonObject.getNullByte("noExiste");
    assertNull(o);

    String s = jsonObject.getNullString(STRING);
    assertEquals("Esteban", s);
  }

  @Test
  public void testGetNullStringUsingThePropertyName() throws DuplicateKeyException {
    Types types = new Types();
    JSONObject jsonObject = new JSONObject(types);

    Object o = jsonObject.getNullByte("noExiste");
    assertNull(o);

    String s = jsonObject.getNullString("string");
    assertEquals("Esteban", s);
  }

  @Test
  public void testGetObjectBooleanUsingThePropertyIndex() throws PropertyIndexNotExistException, DuplicateKeyException {
    Types types = new Types();
    JSONObject jsonObject = new JSONObject(types);

    boolean b = jsonObject.getBoolean(OBJECT_BOOLEAN);
    assertTrue(b);
  }

  @Test
  public void testGetObjectByteUsingThePropertyIndex() throws PropertyIndexNotExistException, DuplicateKeyException {
    Types types = new Types();
    JSONObject jsonObject = new JSONObject(types);

    byte b = jsonObject.getByte(OBJECT_BYTE);
    assertEquals(2, b);
  }

  @Test
  public void testGetObjectByteUsingThePropertyName() throws PropertyNotExistException, DuplicateKeyException {
    Types types = new Types();
    JSONObject jsonObject = new JSONObject(types);

    byte b = jsonObject.getByte("oByte");
    assertEquals(2, b);
  }

  @Test
  public void testGetObjectDoubleUsingThePropertyIndex() throws PropertyIndexNotExistException, DuplicateKeyException {
    Types types = new Types();
    JSONObject jsonObject = new JSONObject(types);

    double d = jsonObject.getDouble(OBJECT_DOUBLE);
    assertEquals(12.5, d, 0);
  }

  @Test
  public void testGetObjectDoubleUsingThePropertyName() throws PropertyNotExistException, DuplicateKeyException {
    Types types = new Types();
    JSONObject jsonObject = new JSONObject(types);

    double d = jsonObject.getDouble("oDouble");
    assertEquals(12.5, d, 0);
  }

  @Test
  public void testGetObjectFloatUsingThePropertyIndex() throws PropertyIndexNotExistException, DuplicateKeyException {
    Types types = new Types();
    JSONObject jsonObject = new JSONObject(types);

    float f = jsonObject.getFloat(OBJECT_FLOAT);
    assertEquals(10.5, f, 0);
  }

  @Test
  public void testGetObjectFloatUsingThePropertyName() throws PropertyNotExistException, DuplicateKeyException {
    Types types = new Types();
    JSONObject jsonObject = new JSONObject(types);

    float f = jsonObject.getFloat("oFloat");
    assertEquals(10.5, f, 0);
  }

  @Test
  public void testGetObjectLongUsingThePropertyIndex() throws PropertyIndexNotExistException, DuplicateKeyException {
    Types types = new Types();
    JSONObject jsonObject = new JSONObject(types);

    long l = jsonObject.getLong(OBJECT_LONG);
    assertEquals(8, l);
  }

  @Test
  public void testGetObjectLongUsingThePropertyName() throws PropertyNotExistException, DuplicateKeyException {
    Types types = new Types();
    JSONObject jsonObject = new JSONObject(types);

    long l = jsonObject.getLong("oLong");
    assertEquals(8, l);
  }

  @Test
  public void testGetObjectShortUsingThePropertyIndex() throws PropertyIndexNotExistException, DuplicateKeyException {
    Types types = new Types();
    JSONObject jsonObject = new JSONObject(types);

    short s = jsonObject.getShort(OBJECT_SHORT);
    assertEquals(4, s);
  }

  @Test
  public void testGetObjectShortUsingThePropertyName() throws PropertyNotExistException, DuplicateKeyException {
    Types types = new Types();
    JSONObject jsonObject = new JSONObject(types);

    short s = jsonObject.getShort("oShort");
    assertEquals(4, s);
  }

  @Test
  public void testGetObjectUsingThePropertyIndex() throws PropertyNotExistException, PropertyIndexNotExistException, DuplicateKeyException {
    Types types = new Types();
    JSONObject jsonObject = new JSONObject(types);

    JSONObject jsonBook = jsonObject.getObject(BOOK);
    int id = jsonBook.getInteger("id");
    String name = jsonBook.getString("name");
    assertEquals(1, id);
    assertEquals("Evolution", name);
  }

  @Test
  public void testGetObjectUsingThePropertyName() throws PropertyNotExistException, DuplicateKeyException {
    Types types = new Types();
    JSONObject jsonObject = new JSONObject(types);

    JSONObject jsonBook = jsonObject.getObject("book");
    int id = jsonBook.getInteger("id");
    String name = jsonBook.getString("name");
    assertEquals(1, id);
    assertEquals("Evolution", name);
  }

  @Test
  public void testGetReferencedElement() throws PropertyNotExistException, DuplicateKeyException {
    Types types = new Types();
    JSONObject jsonObject = new JSONObject(types);

    JSONValue jsonTypes = jsonObject.toReferencedElement();
    JSONObject referencedObject = ((JSONValue) jsonTypes.toReferencedElement()).toJSONObject();
    JSONPair referencedPair = referencedObject.getElement("book");
    assertEquals("\"book\": 1", referencedPair.toJSON());
  }

  @Test
  public void testGetShortUsingThePropertyIndex() throws PropertyIndexNotExistException, DuplicateKeyException {
    Types types = new Types();
    JSONObject jsonObject = new JSONObject(types);

    short s = jsonObject.getShort(SHORT);
    assertEquals(3, s);
  }

  @Test
  public void testGetShortUsingThePropertyName() throws PropertyNotExistException, DuplicateKeyException {
    Types types = new Types();
    JSONObject jsonObject = new JSONObject(types);

    short s = jsonObject.getShort("pShort");
    assertEquals(3, s);
  }

  @Test
  public void testGetStringUsingThePropertyIndex() throws PropertyIndexNotExistException, DuplicateKeyException {
    Types types = new Types();
    JSONObject jsonObject = new JSONObject(types);

    String s = jsonObject.getString(STRING);
    assertEquals("Esteban", s);
  }

  @Test
  public void testGetStringUsingThePropertyName() throws PropertyNotExistException, DuplicateKeyException {
    Types types = new Types();
    JSONObject jsonObject = new JSONObject(types);

    String s = jsonObject.getString("string");
    assertEquals("Esteban", s);
  }

  @Test
  public void testHasChilds() throws DuplicateKeyException {
    Types types = new Types();
    JSONObject jsonObject = new JSONObject(types);

    assertTrue(jsonObject.hasChilds());
  }

  @Test
  public void testIsArray() throws DuplicateKeyException {
    Types types = new Types();
    JSONObject jsonObject = new JSONObject(types);

    assertFalse(jsonObject.isArray());
  }

  @Test
  public void testIsBoolean() throws DuplicateKeyException {
    Types types = new Types();
    JSONObject jsonObject = new JSONObject(types);

    assertFalse(jsonObject.isBoolean());
  }

  @Test
  public void testIsNotReferenceable() throws DuplicateKeyException {
    Types types = new Types();
    JSONObject jsonObject = new JSONObject(types);

    assertFalse(jsonObject.isNotReferenceable());
  }

  @Test
  public void testIsNull() throws DuplicateKeyException {
    Types types = new Types();
    JSONObject jsonObject = new JSONObject(types);

    assertFalse(jsonObject.isNull());
  }

  @Test
  public void testIsNumber() throws DuplicateKeyException {
    Types types = new Types();
    JSONObject jsonObject = new JSONObject(types);

    assertFalse(jsonObject.isNumber());
  }

  @Test
  public void testIsEmptyTrue() throws DuplicateKeyException {
    JSONObject jsonObject = new JSONObject();

    assertTrue(jsonObject.isEmpty());
  }

  @Test
  public void testIsEmptyFalse() throws DuplicateKeyException {
    JSONObject jsonObject = new JSONObject();
    JSONPair jsonPair = new JSONPair("edad", 20);
    jsonObject.add(jsonPair);

    assertFalse(jsonObject.isEmpty());
  }

  @Test
  public void testIsObject() throws DuplicateKeyException {
    Types types = new Types();
    JSONObject jsonObject = new JSONObject(types);

    assertTrue(jsonObject.isObject());
  }

  @Test
  public void testIsReferenceable() throws DuplicateKeyException {
    Types types = new Types();
    JSONObject jsonObject = new JSONObject(types);

    assertTrue(jsonObject.isReferenceable());
  }

  @Test
  public void testIsString() throws DuplicateKeyException {
    Types types = new Types();
    JSONObject jsonObject = new JSONObject(types);

    assertFalse(jsonObject.isString());
  }

  @Test
  public void testIsValue() throws DuplicateKeyException {
    Types types = new Types();
    JSONObject jsonObject = new JSONObject(types);

    assertTrue(jsonObject.isValue());
  }

  @Test
  public void testIterator() throws PropertyNotExistException, ElementNotExistException, DuplicateKeyException {
    Types types = new Types();
    JSONObject jsonObject = new JSONObject(types);

    int i = 0;
    for (JSONPair jsonPair : jsonObject) {
      switch (i) {
        case BIG_DECIMAL:
          assertEquals("bigDecimal", jsonPair.getKey());
          assertEquals(new JSONNumber(15.4), jsonPair.getValue());
          break;
        case BIG_INTEGER:
          assertEquals("bigInteger", jsonPair.getKey());
          assertEquals(new JSONNumber(14), jsonPair.getValue());
          break;
        case BYTE_ARRAY:
          assertEquals("byteArray", jsonPair.getKey());
          JSONArray array = (JSONArray) jsonPair.getValue();
          assertEquals(4, array.size());
          assertEquals(new JSONNumber(1), array.getValue(0));
          assertEquals(new JSONNumber(2), array.getValue(1));
          assertEquals(new JSONNumber(3), array.getValue(2));
          assertEquals(new JSONNumber(4), array.getValue(3));
          break;
        case CALENDAR:
          assertEquals("calendar", jsonPair.getKey());
          assertEquals(new JSONString("1974-01-30T14:20:12.125Z"), jsonPair.getValue());
          break;
        case DATE:
          assertEquals("date", jsonPair.getKey());
          assertEquals(new JSONString("1974-01-30T14:20:12.125Z"), jsonPair.getValue());
          break;
        case OBJECT_BOOLEAN:
          assertEquals("oBoolean", jsonPair.getKey());
          assertEquals(JSONBoolean.TRUE, jsonPair.getValue());
          break;
        case OBJECT_BYTE:
          assertEquals("oByte", jsonPair.getKey());
          assertEquals(new JSONNumber(2), jsonPair.getValue());
          break;
        case OBJECT_CHARACTER:
          assertEquals("oCharacter", jsonPair.getKey());
          assertEquals(new JSONString("b"), jsonPair.getValue());
          break;
        case OBJECT_DOUBLE:
          assertEquals("oDouble", jsonPair.getKey());
          assertEquals(new JSONNumber(12.5), jsonPair.getValue());
          break;
        case OBJECT_FLOAT:
          assertEquals("oFloat", jsonPair.getKey());
          assertEquals(new JSONNumber(10.5), jsonPair.getValue());
          break;
        case OBJECT_INTEGER:
          assertEquals("oInteger", jsonPair.getKey());
          assertEquals(new JSONNumber(6), jsonPair.getValue());
          break;
        case OBJECT_LONG:
          assertEquals("oLong", jsonPair.getKey());
          assertEquals(new JSONNumber(8), jsonPair.getValue());
          break;
        case OBJECT_SHORT:
          assertEquals("oShort", jsonPair.getKey());
          assertEquals(new JSONNumber(4), jsonPair.getValue());
          break;
        case NULL_REFERENCE:
          assertEquals("nullReference", jsonPair.getKey());
          assertEquals(new JSONNull(), jsonPair.getValue());
          break;
        case BOOLEAN:
          assertEquals("pBoolean", jsonPair.getKey());
          assertEquals(JSONBoolean.get(false), jsonPair.getValue());
          break;
        case BYTE:
          assertEquals("pByte", jsonPair.getKey());
          assertEquals(new JSONNumber(1), jsonPair.getValue());
          break;
        case CHARACTER:
          assertEquals("pCharacter", jsonPair.getKey());
          assertEquals(new JSONString('a'), jsonPair.getValue());
          break;
        case DOUBLE:
          assertEquals("pDouble", jsonPair.getKey());
          assertEquals(new JSONNumber(11.5), jsonPair.getValue());
          break;
        case FLOAT:
          assertEquals("pFloat", jsonPair.getKey());
          assertEquals(new JSONNumber(9.5), jsonPair.getValue());
          break;
        case INTEGER:
          assertEquals("pInteger", jsonPair.getKey());
          assertEquals(new JSONNumber(5), jsonPair.getValue());
          break;
        case LONG:
          assertEquals("pLong", jsonPair.getKey());
          assertEquals(new JSONNumber(7), jsonPair.getValue());
          break;
        case SHORT:
          assertEquals("pShort", jsonPair.getKey());
          assertEquals(new JSONNumber(3), jsonPair.getValue());
          break;
        case STRING:
          assertEquals("string", jsonPair.getKey());
          assertEquals(new JSONString("Esteban"), jsonPair.getValue());
          break;
        case BOOK:
          assertEquals("book", jsonPair.getKey());
          JSONObject jsonBook = jsonPair.getValue().toJSONObject();
          int id = jsonBook.getInteger("id");
          String name = jsonBook.getString("name");
          assertEquals(1, id);
          assertEquals("Evolution", name);
          break;
        default:
          fail();
          break;
      }
      i++;
    }
    assertEquals(24, i);
  }

  @Test
  public void testToArray() throws PropertyNotExistException, ElementNotExistException, DuplicateKeyException {
    Types types = new Types();
    JSONObject jsonObject = new JSONObject(types);

    JSONValue[] array = jsonObject.toArray();

    assertEquals(24, array.length);

    JSONValue value;

    value = array[BIG_DECIMAL];
    assertEquals(new JSONNumber(15.4), value);

    value = array[BIG_INTEGER];
    assertEquals(new JSONNumber(14), value);

    value = array[BYTE_ARRAY];
    JSONArray a = (JSONArray) value;
    assertEquals(4, a.size());
    assertEquals(new JSONNumber(1), a.getValue(0));
    assertEquals(new JSONNumber(2), a.getValue(1));
    assertEquals(new JSONNumber(3), a.getValue(2));
    assertEquals(new JSONNumber(4), a.getValue(3));

    value = array[CALENDAR];
    assertEquals(new JSONString("1974-01-30T14:20:12.125Z"), value);

    value = array[DATE];
    assertEquals(new JSONString("1974-01-30T14:20:12.125Z"), value);

    value = array[OBJECT_BOOLEAN];
    assertEquals(JSONBoolean.TRUE, value);

    value = array[OBJECT_BYTE];
    assertEquals(new JSONNumber(2), value);

    value = array[OBJECT_CHARACTER];
    assertEquals(new JSONString("b"), value);

    value = array[OBJECT_DOUBLE];
    assertEquals(new JSONNumber(12.5), value);

    value = array[OBJECT_FLOAT];
    assertEquals(new JSONNumber(10.5), value);

    value = array[OBJECT_INTEGER];
    assertEquals(new JSONNumber(6), value);

    value = array[OBJECT_LONG];
    assertEquals(new JSONNumber(8), value);

    value = array[OBJECT_SHORT];
    assertEquals(new JSONNumber(4), value);

    value = array[NULL_REFERENCE];
    assertEquals(new JSONNull(), value);

    value = array[BOOLEAN];
    assertEquals(JSONBoolean.get(false), value
    );

    value = array[BYTE];
    assertEquals(new JSONNumber(1), value);

    value = array[CHARACTER];
    assertEquals(new JSONString('a'), value);

    value = array[DOUBLE];
    assertEquals(new JSONNumber(11.5), value);

    value = array[FLOAT];
    assertEquals(new JSONNumber(9.5), value);

    value = array[INTEGER];
    assertEquals(new JSONNumber(5), value);

    value = array[LONG];
    assertEquals(new JSONNumber(7), value);

    value = array[SHORT];
    assertEquals(new JSONNumber(3), value);

    value = array[STRING];
    assertEquals(new JSONString("Esteban"), value);

    JSONObject jsonBook = array[BOOK].toJSONObject();
    int id = jsonBook.getInteger("id");
    String name = jsonBook.getString("name");
    assertEquals(1, id);
    assertEquals("Evolution", name);
  }

  @Test
  public void testToJSON() throws DuplicateKeyException {
    Types types = new Types();
    JSONObject jsonObject = new JSONObject(types);

    String json = jsonObject.toJSON();

    Log.debug(json);

    assertEquals("{ \"bigDecimal\": 15.4, \"bigInteger\": 14, \"book\": { \"id\": 1, \"name\": \"Evolution\" }, \"byteArray\": [ 1, 2, 3, 4 ], \"calendar\": \"1974-01-30T14:20:12.125Z\", \"date\": \"1974-01-30T14:20:12.125Z\", \"nullReference\": null, \"oBoolean\": true, \"oByte\": 2, \"oCharacter\": \"b\", \"oDouble\": 12.5, \"oFloat\": 10.5, \"oInteger\": 6, \"oLong\": 8, \"oShort\": 4, \"pBoolean\": false, \"pByte\": 1, \"pCharacter\": \"a\", \"pDouble\": 11.5, \"pFloat\": 9.5, \"pInteger\": 5, \"pLong\": 7, \"pShort\": 3, \"string\": \"Esteban\" }", json);
  }

  @Test
  public void testToJSONArray() throws PropertyNotExistException, ElementNotExistException, DuplicateKeyException {
    Types types = new Types();
    JSONObject jsonObject = new JSONObject(types);

    JSONArray array = jsonObject.toJSONArray();

    assertEquals(24, array.size());

    JSONValue value;

    value = array.getValue(BIG_DECIMAL);
    assertEquals(new JSONNumber(15.4), value);

    value = array.getValue(BIG_INTEGER);
    assertEquals(new JSONNumber(14), value);

    value = array.getValue(BYTE_ARRAY);
    JSONArray a = (JSONArray) value;
    assertEquals(4, a.size());
    assertEquals(new JSONNumber(1), a.getValue(0));
    assertEquals(new JSONNumber(2), a.getValue(1));
    assertEquals(new JSONNumber(3), a.getValue(2));
    assertEquals(new JSONNumber(4), a.getValue(3));

    value = array.getValue(CALENDAR);
    assertEquals(new JSONString("1974-01-30T14:20:12.125Z"), value);

    value = array.getValue(DATE);
    assertEquals(new JSONString("1974-01-30T14:20:12.125Z"), value);

    value = array.getValue(OBJECT_BOOLEAN);
    assertEquals(JSONBoolean.TRUE, value);

    value = array.getValue(OBJECT_BYTE);
    assertEquals(new JSONNumber(2), value);

    value = array.getValue(OBJECT_CHARACTER);
    assertEquals(new JSONString("b"), value);

    value = array.getValue(OBJECT_DOUBLE);
    assertEquals(new JSONNumber(12.5), value);

    value = array.getValue(OBJECT_FLOAT);
    assertEquals(new JSONNumber(10.5), value);

    value = array.getValue(OBJECT_INTEGER);
    assertEquals(new JSONNumber(6), value);

    value = array.getValue(OBJECT_LONG);
    assertEquals(new JSONNumber(8), value);

    value = array.getValue(OBJECT_SHORT);
    assertEquals(new JSONNumber(4), value);

    value = array.getValue(NULL_REFERENCE);
    assertEquals(new JSONNull(), value);

    value = array.getValue(BOOLEAN);
    assertEquals(JSONBoolean.get(false), value);

    value = array.getValue(BYTE);
    assertEquals(new JSONNumber(1), value);

    value = array.getValue(CHARACTER);
    assertEquals(new JSONString('a'), value);

    value = array.getValue(DOUBLE);
    assertEquals(new JSONNumber(11.5), value);

    value = array.getValue(FLOAT);
    assertEquals(new JSONNumber(9.5), value);

    value = array.getValue(INTEGER);
    assertEquals(new JSONNumber(5), value);

    value = array.getValue(LONG);
    assertEquals(new JSONNumber(7), value);

    value = array.getValue(SHORT);
    assertEquals(new JSONNumber(3), value);

    value = array.getValue(STRING);
    assertEquals(new JSONString("Esteban"), value);

    JSONObject jsonBook = array.getValue(BOOK).toJSONObject();
    int id = jsonBook.getInteger("id");
    String name = jsonBook.getString("name");
    assertEquals(1, id);
    assertEquals("Evolution", name);
  }

  @Test
  public void testToJSONPairArray() throws PropertyNotExistException, ElementNotExistException, DuplicateKeyException {
    Types types = new Types();
    JSONObject jsonObject = new JSONObject(types);

    JSONPair[] array = jsonObject.toJSONPairArray();

    assertEquals(24, array.length);

    JSONValue value;

    value = array[BIG_DECIMAL].getValue();
    assertEquals(new JSONNumber(15.4), value);

    value = array[BIG_INTEGER].getValue();
    assertEquals(new JSONNumber(14), value);

    value = array[BYTE_ARRAY].getValue();
    JSONArray a = (JSONArray) value;
    assertEquals(4, a.size());
    assertEquals(new JSONNumber(1), a.getValue(0));
    assertEquals(new JSONNumber(2), a.getValue(1));
    assertEquals(new JSONNumber(3), a.getValue(2));
    assertEquals(new JSONNumber(4), a.getValue(3));

    value = array[CALENDAR].getValue();
    assertEquals(new JSONString("1974-01-30T14:20:12.125Z"), value);

    value = array[DATE].getValue();
    assertEquals(new JSONString("1974-01-30T14:20:12.125Z"), value);

    value = array[OBJECT_BOOLEAN].getValue();
    assertEquals(JSONBoolean.TRUE, value);

    value = array[OBJECT_BYTE].getValue();
    assertEquals(new JSONNumber(2), value);

    value = array[OBJECT_CHARACTER].getValue();
    assertEquals(new JSONString("b"), value);

    value = array[OBJECT_DOUBLE].getValue();
    assertEquals(new JSONNumber(12.5), value);

    value = array[OBJECT_FLOAT].getValue();
    assertEquals(new JSONNumber(10.5), value);

    value = array[OBJECT_INTEGER].getValue();
    assertEquals(new JSONNumber(6), value);

    value = array[OBJECT_LONG].getValue();
    assertEquals(new JSONNumber(8), value);

    value = array[OBJECT_SHORT].getValue();
    assertEquals(new JSONNumber(4), value);

    value = array[NULL_REFERENCE].getValue();
    assertEquals(new JSONNull(), value);

    value = array[BOOLEAN].getValue();
    assertEquals(JSONBoolean.get(false), value);

    value = array[BYTE].getValue();
    assertEquals(new JSONNumber(1), value);

    value = array[CHARACTER].getValue();
    assertEquals(new JSONString('a'), value);

    value = array[DOUBLE].getValue();
    assertEquals(new JSONNumber(11.5), value);

    value = array[FLOAT].getValue();
    assertEquals(new JSONNumber(9.5), value);

    value = array[INTEGER].getValue();
    assertEquals(new JSONNumber(5), value);

    value = array[LONG].getValue();
    assertEquals(new JSONNumber(7), value);

    value = array[SHORT].getValue();
    assertEquals(new JSONNumber(3), value);

    value = array[STRING].getValue();
    assertEquals(new JSONString("Esteban"), value);

    JSONObject jsonBook = array[BOOK].getValue().toJSONObject();
    int id = jsonBook.getInteger("id");
    String name = jsonBook.getString("name");
    assertEquals(1, id);
    assertEquals("Evolution", name);
  }

  @Test
  public void testToJSONReferencedTree() throws DuplicateKeyException {
    Log.debug("Create a refered JSON tree using Java objects.");
    Data data = new Data();
    JSONObject jsonObject = JSON.toJSONTree(data).toJSONObject();
    JSONElement jsonReferencedTree = jsonObject.toReferencedObject();

    String expectedString = "{ \"version\": 1, \"countryId\": 1, \"countryName\": { \"version\": 1, \"language\": 97, \"nameType\": 1, \"word\": 2 } }";

    System.out.println("Expected: " + expectedString);
    System.out.println("  Result: " + jsonReferencedTree.toJSON());
    assertEquals(expectedString, jsonReferencedTree.toJSON());

    BookList bookList = new BookList();
    Book book;

    book = new Book(1, "El doble.");
    bookList.add(book);

    book = new Book(8, "El principito.");
    Book mostImportantBook = book;
    bookList.add(book);

    book = new Book(13, "Crónica de una muerte anunciada.");
    bookList.add(book);

    Storage storage = new Storage(69, bookList, mostImportantBook);

    JSONObject jsonStorageObject = JSON.toJSONTree(storage).toJSONObject();
    System.out.println(jsonStorageObject.toJSON());
    assertEquals("{ \"id\": 69, \"list\": [ { \"id\": 1, \"name\": \"El doble.\" }, { \"id\": 8, \"name\": \"El principito.\" }, { \"id\": 13, \"name\": \"Crónica de una muerte anunciada.\" } ], \"mostImportantBook\": { \"id\": 8, \"name\": \"El principito.\" } }", jsonStorageObject.toJSON());

    JSONElement jsonStorageReferencedTree = jsonStorageObject.toReferencedObject();
    System.out.println(jsonStorageReferencedTree.toJSON());
    assertEquals("{ \"id\": 69, \"list\": [ 1, 8, 13 ], \"mostImportantBook\": 8 }", jsonStorageReferencedTree.toJSON());
  }

}
