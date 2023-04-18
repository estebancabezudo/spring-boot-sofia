package net.cabezudo.json.values;

import net.cabezudo.json.JSON;
import net.cabezudo.json.Log;
import net.cabezudo.json.exceptions.DuplicateKeyException;
import net.cabezudo.json.exceptions.ElementNotExistException;
import net.cabezudo.json.exceptions.JSONParseException;
import net.cabezudo.json.exceptions.PropertyNotExistException;
import net.cabezudo.json.objects.Book;
import net.cabezudo.json.objects.BookList;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * @author <a href="http://cabezudo.net">Esteban Cabezudo</a>
 * @version 0.9, 10/01/2014
 */
public class JSONArrayTest {

  @Test
  public void testAddJSONValue() throws ElementNotExistException {
    JSONArray jsonArray = new JSONArray();
    int size = jsonArray.size();
    assertEquals(0, size);
    JSONNumber jsonNumber = new JSONNumber(10);
    jsonArray.add(jsonNumber);
    size = jsonArray.size();
    assertEquals(1, size);
    JSONValue jsonValue = jsonArray.getValue(0);
    assertEquals(new JSONNumber(10), jsonValue);
  }

  @Test
  public void testAddObject() {
    JSONArray jsonArray = new JSONArray();
    int size = jsonArray.size();
    assertEquals(0, size);
    jsonArray.add(new JSONNumber(10));
    size = jsonArray.size();
    assertEquals(1, size);
  }

  @Test
  public void testCompareTo() {
    JSONArray firstJSONArray = new JSONArray();
    JSONArray secondJSONArray = new JSONArray();

    JSONNumber firstJSONNumber = new JSONNumber(1);
    firstJSONArray.add(firstJSONNumber);
    JSONNumber secondJSONNumber = new JSONNumber(1);
    secondJSONArray.add(secondJSONNumber);

    JSONString firstJSONString = new JSONString("Esteban");
    firstJSONArray.add(firstJSONString);
    JSONString secondJSONString = new JSONString("Esteban");
    secondJSONArray.add(secondJSONString);

    assertEquals(firstJSONArray.compareTo(secondJSONArray), 0);
  }

  @Test
  public void testDeleteElement() throws ElementNotExistException {
    JSONArray jsonArray = new JSONArray();
    JSONNumber jsonNumber = new JSONNumber(1);
    jsonArray.add(jsonNumber);

    JSONString jsonString = new JSONString("Esteban");
    jsonArray.add(jsonString);

    jsonArray.remove(0);

    assertEquals(1, jsonArray.size());

    JSONValue element = jsonArray.getValue(0);
    assertEquals(new JSONString("Esteban"), element);
  }

  @Test
  public void testGetNullValue() {
    JSONArray jsonArray = new JSONArray();
    JSONNumber jsonNumber = new JSONNumber(1);
    jsonArray.add(jsonNumber);

    JSONString jsonString = new JSONString("Esteban");
    jsonArray.add(jsonString);

    List<JSONValue> list = jsonArray.toList();

    assertEquals(2, list.size());

    JSONValue element;

    element = jsonArray.getNullValue(0);
    assertEquals(new JSONNumber(1), element);

    element = jsonArray.getNullValue(1);
    assertEquals(new JSONString("Esteban"), element);
  }

  @Test
  public void testGetReferencedElement() throws JSONParseException, ElementNotExistException, DuplicateKeyException {
    JSONArray jsonArray = new JSONArray();
    JSONObject a = new JSONObject("testGetReferencedElement", "{ \"id\": 2, \"name\": \"Esteban\" }");
    a.setReferenceFieldName("id");
    jsonArray.add(a);

    JSONObject b = new JSONObject("testGetReferencedElement", "{ \"id\": 1, \"name\": \"Ismael\" }");
    b.setReferenceFieldName("id");
    jsonArray.add(b);

    JSONArray referencedElement = jsonArray.toReferencedElement();

    JSONValue c = referencedElement.getValue(0);
    long d = c.toInteger();
    assertEquals(2, d);

    JSONValue e = referencedElement.getValue(1);
    long f = e.toInteger();
    assertEquals(1, f);
  }

  @Test
  public void testGetValue() throws Exception {
    JSONArray jsonArray = new JSONArray();
    JSONNumber jsonNumber = new JSONNumber(1);
    jsonArray.add(jsonNumber);

    JSONString jsonString = new JSONString("Esteban");
    jsonArray.add(jsonString);

    List<JSONValue> list = jsonArray.toList();

    assertEquals(2, list.size());

    JSONValue element;

    element = jsonArray.getValue(0);
    assertEquals(new JSONNumber(1), element);

    element = jsonArray.getValue(1);
    assertEquals(new JSONString("Esteban"), element);
  }

  @Test
  public void testHasElements() {
    JSONArray jsonArray = new JSONArray();
    assertFalse(jsonArray.hasElements());

    JSONNumber jsonNumber = new JSONNumber(1);
    jsonArray.add(jsonNumber);

    JSONString jsonString = new JSONString("Esteban");
    jsonArray.add(jsonString);

    List<JSONValue> list = jsonArray.toList();

    assertEquals(2, list.size());

    assertTrue(jsonArray.hasElements());
  }

  @Test
  public void testIsValue() {
    JSONArray jsonArray = new JSONArray();
    assertTrue(jsonArray.isValue());
  }

  @Test
  public void testIterator() {
    JSONArray jsonArray = new JSONArray();
    JSONNumber jsonNumber = new JSONNumber(1);
    jsonArray.add(jsonNumber);

    JSONString jsonString = new JSONString("Esteban");
    jsonArray.add(jsonString);

    int i = 0;
    for (JSONValue jsonValue : jsonArray) {
      switch (i) {
        case 0:
          assertEquals(new JSONNumber(1), jsonValue);
          break;
        case 1:
          assertEquals(new JSONString("Esteban"), jsonValue);
          break;
        default:
          fail();
      }
      i++;
    }
  }

  @Test
  public void testSetValue() throws ElementNotExistException {
    JSONArray jsonArray = new JSONArray();
    JSONNumber jsonNumber = new JSONNumber(1);
    jsonArray.add(jsonNumber);

    JSONString jsonString = new JSONString("Esteban");
    jsonArray.add(jsonString);

    jsonString = new JSONString("Ismael");
    jsonArray.setValue(0, jsonString);

    JSONValue element;

    element = jsonArray.getValue(0);
    assertEquals(new JSONString("Ismael"), element);

    element = jsonArray.getValue(1);
    assertEquals(new JSONString("Esteban"), element);
  }

  @Test
  public void testToArray() {
    JSONArray jsonArray = new JSONArray();
    JSONNumber jsonNumber = new JSONNumber(1);
    jsonArray.add(jsonNumber);

    JSONString jsonString = new JSONString("Esteban");
    jsonArray.add(jsonString);

    JSONValue[] array = jsonArray.toArray();

    JSONValue a = array[0];
    JSONValue b = array[1];

    assertEquals(2, array.length);
    assertEquals(new JSONNumber(1), a);
    assertEquals(new JSONString("Esteban"), b);
  }

  @Test
  public void testToJSON() {
    JSONArray jsonArray = new JSONArray();
    JSONNumber jsonNumber = new JSONNumber(1);
    jsonArray.add(jsonNumber);

    JSONString jsonString = new JSONString("Esteban");
    jsonArray.add(jsonString);

    String json = jsonArray.toJSON();

    assertEquals("[ 1, \"Esteban\" ]", json);
  }

  @Test
  public void testToJSONArray() throws ElementNotExistException {
    JSONArray jsonArray = new JSONArray();
    JSONNumber jsonNumber = new JSONNumber(1);
    jsonArray.add(jsonNumber);

    JSONString jsonString = new JSONString("Esteban");
    jsonArray.add(jsonString);

    JSONArray otherJSONArray = jsonArray.toJSONArray();

    JSONValue n1 = jsonArray.getValue(0);
    JSONValue s1 = jsonArray.getValue(1);

    JSONValue n2 = otherJSONArray.getValue(0);
    JSONValue s2 = otherJSONArray.getValue(1);

    assertEquals(jsonArray.size(), otherJSONArray.size());
    assertEquals(n1, n2);
    assertEquals(s1, s2);
  }

  @Test
  public void testToJSONTree() throws ElementNotExistException {
    JSONArray jsonArray = new JSONArray();
    JSONNumber jsonNumber = new JSONNumber(1);
    jsonArray.add(jsonNumber);

    JSONString jsonString = new JSONString("Esteban");
    jsonArray.add(jsonString);

    JSONArray jsonTree = (JSONArray) jsonArray.toJSONTree();

    JSONValue n1 = jsonArray.getValue(0);
    JSONValue s1 = jsonArray.getValue(1);

    JSONValue n2 = jsonTree.getValue(0);
    JSONValue s2 = jsonTree.getValue(1);

    assertEquals(jsonArray.size(), jsonTree.size());
    assertEquals(n1, n2);
    assertEquals(s1, s2);
  }

  @Test
  public void testToList() {
    JSONArray jsonArray = new JSONArray();
    JSONNumber jsonNumber = new JSONNumber(1);
    jsonArray.add(jsonNumber);

    JSONString jsonString = new JSONString("Esteban");
    jsonArray.add(jsonString);

    List<JSONValue> list = jsonArray.toList();

    JSONValue a = list.get(0);
    JSONValue b = list.get(1);

    assertEquals(new JSONNumber(1), a);
    assertEquals(new JSONString("Esteban"), b);
  }

  @Test
  public void testToStringArray() {
    JSONArray jsonArray = new JSONArray();
    JSONNumber jsonNumber = new JSONNumber(1);
    jsonArray.add(jsonNumber);

    JSONString jsonString = new JSONString("Esteban");
    jsonArray.add(jsonString);

    String[] list = jsonArray.toStringArray();

    String a = list[0];
    String b = list[1];

    assertEquals("1", a);
    assertEquals("Esteban", b);
  }

  @Test
  public void testGetBigDecimal() throws JSONParseException, ElementNotExistException {
    JSONArray jsonArray = JSON.parse("", "[ 123456.789 ]").toJSONArray();
    BigDecimal value = jsonArray.getBigDecimal(0);
    BigDecimal expectedValue = new BigDecimal("123456.789");
    assertEquals(expectedValue, value);
  }

  @Test
  public void testGetBigInteger() throws JSONParseException, ElementNotExistException {
    JSONArray jsonArray = JSON.parse("", "[ 62345324523452345234523452365476489 ]").toJSONArray();
    BigInteger value = jsonArray.getBigInteger(0);
    BigInteger expectedValue = new BigInteger("62345324523452345234523452365476489");
    assertEquals(expectedValue, value);
  }

  @Test
  public void testGetBoolean() throws JSONParseException, ElementNotExistException {
    JSONArray jsonArray = JSON.parse("", "[ true ]").toJSONArray();
    Boolean value = jsonArray.getBoolean(0);
    Boolean expectedValue = Boolean.TRUE;
    assertEquals(expectedValue, value);
  }

  @Test
  public void testGetByte() throws JSONParseException, ElementNotExistException {
    JSONArray jsonArray = JSON.parse("", "[ 42 ]").toJSONArray();
    Byte value = jsonArray.getByte(0);
    Byte expectedValue = Byte.valueOf("42");
    assertEquals(expectedValue, value);
  }

  @Test
  public void testGetCalendar() throws JSONParseException, ElementNotExistException {
    JSONArray jsonArray = JSON.parse("", "[ \"2017-01-23T11:20:32.222Z-0000\" ]").toJSONArray();
    Calendar value = jsonArray.getCalendar(0);
    Calendar expectedValue = Calendar.getInstance();
    expectedValue.set(Calendar.MILLISECOND, 222);
    expectedValue.set(2017, 0, 23, 11, 20, 32);
    assertEquals(expectedValue, value);
  }

  @Test
  public void testGetCharacter() throws JSONParseException, ElementNotExistException {
    JSONArray jsonArray = JSON.parse("", "[ \"a\", \"be\" ]").toJSONArray();
    Character value = jsonArray.getCharacter(0);
    Character expectedValue = 'a';
    assertEquals(expectedValue, value);
    value = jsonArray.getCharacter(1);
    expectedValue = 'b';
    assertEquals(expectedValue, value);
  }

  @Test
  public void testGetDouble() throws JSONParseException, ElementNotExistException {
    JSONArray jsonArray = JSON.parse("", "[ 15.5 ]").toJSONArray();
    Double value = jsonArray.getDouble(0);
    Double expectedValue = 15.5;
    assertEquals(expectedValue, value);
  }

  @Test
  public void testGetFloat() throws JSONParseException, ElementNotExistException {
    JSONArray jsonArray = JSON.parse("", "[ 123456.789 ]").toJSONArray();
    Float value = jsonArray.getFloat(0);
    Float expectedValue = Float.valueOf("123456.789");
    assertEquals(expectedValue, value, 3);
  }

  @Test
  public void testGetLong() throws JSONParseException, ElementNotExistException {
    JSONArray jsonArray = JSON.parse("", "[ 1865345634563456345 ]").toJSONArray();
    Long value = jsonArray.getLong(0);
    Long expectedValue = 1865345634563456345L;
    assertEquals(expectedValue, value);
  }

  @Test
  public void testGetInteger() throws JSONParseException, ElementNotExistException {
    JSONArray jsonArray = JSON.parse("", "[ 123456 ]").toJSONArray();
    Integer value = jsonArray.getInteger(0);
    Integer expectedValue = 123456;
    assertEquals(expectedValue, value);
  }

  @Test
  public void testGetObject() throws JSONParseException, ElementNotExistException {
    JSONArray jsonArray = JSON.parse("", "[ { \"number\": 123456 } ]").toJSONArray();
    JSONObject jsonValue = jsonArray.getObject(0);
    String value = jsonValue.toJSON();
    String expectedValue = "{ \"number\": 123456 }";
    assertEquals(expectedValue, value);
  }

  @Test
  public void testGetShort() throws JSONParseException, ElementNotExistException {
    JSONArray jsonArray = JSON.parse("", "[ 12789 ]").toJSONArray();
    Short value = jsonArray.getShort(0);
    short expectedValue = 12789;
    assertEquals(expectedValue, (short) value);
  }

  @Test
  public void testGetString() throws JSONParseException, ElementNotExistException {
    JSONArray jsonArray = JSON.parse("", "[ \"House\" ]").toJSONArray();
    String value = jsonArray.getString(0);
    String expectedValue = "House";
    assertEquals(expectedValue, value);
  }

  @Test
  public void testGetNullBigDecimal() throws JSONParseException {
    JSONArray jsonArray = JSON.parse("", "[ 123456.789 ]").toJSONArray();
    BigDecimal value = jsonArray.getNullBigDecimal(0);
    BigDecimal expectedValue = new BigDecimal("123456.789");
    assertEquals(expectedValue, value);
  }

  @Test
  public void testGetNullBigInteger() throws JSONParseException {
    JSONArray jsonArray = JSON.parse("", "[ 62345324523452345234523452365476489 ]").toJSONArray();
    BigInteger value = jsonArray.getNullBigInteger(0);
    BigInteger expectedValue = new BigInteger("62345324523452345234523452365476489");
    assertEquals(expectedValue, value);
  }

  @Test
  public void testGetNullBoolean() throws JSONParseException {
    JSONArray jsonArray = JSON.parse("", "[ true ]").toJSONArray();
    Boolean value = jsonArray.getNullBoolean(0);
    Boolean expectedValue = Boolean.TRUE;
    assertEquals(expectedValue, value);
  }

  @Test
  public void testGetNullByte() throws JSONParseException {
    JSONArray jsonArray = JSON.parse("", "[ 42 ]").toJSONArray();
    Byte value = jsonArray.getNullByte(0);
    Byte expectedValue = Byte.valueOf("42");
    assertEquals(expectedValue, value);
  }

  @Test
  public void testGetNullCalendar() throws JSONParseException {
    JSONArray jsonArray = JSON.parse("", "[ \"2017-01-23T11:20:32.222Z-0000\" ]").toJSONArray();
    Calendar value = jsonArray.getNullCalendar(0);
    Calendar expectedValue = Calendar.getInstance();
    expectedValue.set(Calendar.MILLISECOND, 222);
    expectedValue.set(2017, 0, 23, 11, 20, 32);
    assertEquals(expectedValue, value);
  }

  @Test
  public void testGetNullCharacter() throws JSONParseException {
    JSONArray jsonArray = JSON.parse("", "[ \"a\", \"be\" ]").toJSONArray();
    Character value = jsonArray.getNullCharacter(0);
    Character expectedValue = 'a';
    assertEquals(expectedValue, value);
    value = jsonArray.getNullCharacter(1);
    expectedValue = 'b';
    assertEquals(expectedValue, value);
  }

  @Test
  public void testGetNullDouble() throws JSONParseException {
    JSONArray jsonArray = JSON.parse("", "[ 15.5 ]").toJSONArray();
    Double value = jsonArray.getNullDouble(0);
    Double expectedValue = 15.5;
    assertEquals(expectedValue, value);
  }

  @Test
  public void testGetNullFloat() throws JSONParseException {
    JSONArray jsonArray = JSON.parse("", "[ 123456.789 ]").toJSONArray();
    Float value = jsonArray.getNullFloat(0);
    Float expectedValue = Float.valueOf("123456.789");
    assertEquals(expectedValue, value, 3);
  }

  @Test
  public void testGetNullLong() throws JSONParseException {
    JSONArray jsonArray = JSON.parse("", "[ 1234523453245232345 ]").toJSONArray();
    Long value = jsonArray.getNullLong(0);
    Long expectedValue = 1234523453245232345L;
    assertEquals(expectedValue, value);
  }

  @Test
  public void testGetNullInteger() throws JSONParseException {
    JSONArray jsonArray = JSON.parse("", "[ 123456789 ]").toJSONArray();
    Integer value = jsonArray.getNullInteger(0);
    Integer expectedValue = 123456789;
    assertEquals(expectedValue, value);
  }

  @Test
  public void testGetNullObject() throws JSONParseException {
    JSONArray jsonArray = JSON.parse("", "[ { \"number\": 123456 } ]").toJSONArray();
    JSONObject jsonValue = jsonArray.getNullObject(0);
    String value = jsonValue.toJSON();
    String expectedValue = "{ \"number\": 123456 }";
    assertEquals(expectedValue, value);
  }

  @Test
  public void testGetNullShort() throws JSONParseException {
    JSONArray jsonArray = JSON.parse("", "[ 12789 ]").toJSONArray();
    Short value = jsonArray.getNullShort(0);
    short expectedValue = 12789;
    assertEquals(expectedValue, (short) value);
  }

  @Test
  public void testGetNullString() throws JSONParseException {
    JSONArray jsonArray = JSON.parse("", "[ \"House\" ]").toJSONArray();
    String value = jsonArray.getNullString(0);
    String expectedValue = "House";
    assertEquals(expectedValue, value);
  }

  @Test
  public void testDigBigDecimal() throws JSONParseException, PropertyNotExistException {
    JSONArray jsonArray = JSON.parse("", "[ 123456.789 ]").toJSONArray();
    BigDecimal value = jsonArray.digBigDecimal("[0]");
    BigDecimal expectedValue = new BigDecimal("123456.789");
    assertEquals(expectedValue, value);
  }

  @Test
  public void testDigBigInteger() throws JSONParseException, PropertyNotExistException {
    JSONArray jsonArray = JSON.parse("", "[ 62345324523452345234523452365476489 ]").toJSONArray();
    BigInteger value = jsonArray.digBigInteger("[0]");
    BigInteger expectedValue = new BigInteger("62345324523452345234523452365476489");
    assertEquals(expectedValue, value);
  }

  @Test
  public void testDigBoolean() throws JSONParseException, PropertyNotExistException {
    JSONArray jsonArray = JSON.parse("", "[ true ]").toJSONArray();
    Boolean value = jsonArray.digBoolean("[0]");
    Boolean expectedValue = Boolean.TRUE;
    assertEquals(expectedValue, value);
  }

  @Test
  public void testDigByte() throws JSONParseException, PropertyNotExistException {
    JSONArray jsonArray = JSON.parse("", "[ 42 ]").toJSONArray();
    Byte value = jsonArray.digByte("[0]");
    Byte expectedValue = Byte.valueOf("42");
    assertEquals(expectedValue, value);
  }

  @Test
  public void testDigCalendar() throws JSONParseException, PropertyNotExistException {
    JSONArray jsonArray = JSON.parse("", "[ \"2017-01-23T11:20:32.222Z-0000\" ]").toJSONArray();
    Calendar value = jsonArray.digCalendar("[0]");
    Calendar expectedValue = Calendar.getInstance();
    expectedValue.set(Calendar.MILLISECOND, 222);
    expectedValue.set(2017, 0, 23, 11, 20, 32);
    assertEquals(expectedValue, value);
  }

  @Test
  public void testDigCharacter() throws JSONParseException, PropertyNotExistException {
    JSONArray jsonArray = JSON.parse("", "[ \"a\", \"be\" ]").toJSONArray();
    Character value = jsonArray.digCharacter("[0]");
    Character expectedValue = 'a';
    assertEquals(expectedValue, value);
    value = jsonArray.digCharacter("[1]");
    expectedValue = 'b';
    assertEquals(expectedValue, value);
  }

  @Test
  public void testDigDouble() throws JSONParseException, PropertyNotExistException {
    JSONArray jsonArray = JSON.parse("", "[ 15.5 ]").toJSONArray();
    Double value = jsonArray.digDouble("[0]");
    Double expectedValue = 15.5;
    assertEquals(expectedValue, value);
  }

  @Test
  public void testDigFloat() throws JSONParseException, PropertyNotExistException {
    JSONArray jsonArray = JSON.parse("", "[ 123456.789 ]").toJSONArray();
    Float value = jsonArray.digFloat("[0]");
    Float expectedValue = Float.valueOf("123456.789");
    assertEquals(expectedValue, value, 3);
  }

  @Test
  public void testDigLong() throws JSONParseException, PropertyNotExistException {
    JSONArray jsonArray = JSON.parse("", "[ 1865345634563456345 ]").toJSONArray();
    Long value = jsonArray.digLong("[0]");
    Long expectedValue = 1865345634563456345L;
    assertEquals(expectedValue, value);
  }

  @Test
  public void testDigInteger() throws JSONParseException, PropertyNotExistException {
    JSONArray jsonArray = JSON.parse("", "[ 123456 ]").toJSONArray();
    Integer value = jsonArray.digInteger("[0]");
    Integer expectedValue = 123456;
    assertEquals(expectedValue, value);
  }

  @Test
  public void testDigObject() throws JSONParseException, PropertyNotExistException {
    JSONArray jsonArray = JSON.parse("", "[ { \"person\": { \"name\": \"John\" } } ]").toJSONArray();
    JSONObject jsonValue = jsonArray.digObject("[0].person");
    String value = jsonValue.toJSON();
    String expectedValue = "{ \"name\": \"John\" }";
    assertEquals(expectedValue, value);
  }

  @Test
  public void testDigShort() throws JSONParseException, PropertyNotExistException {
    JSONArray jsonArray = JSON.parse("", "[ 12789 ]").toJSONArray();
    Short value = jsonArray.digShort("[0]");
    short expectedValue = 12789;
    assertEquals(expectedValue, (short) value);
  }

  @Test
  public void testDigString() throws JSONParseException, PropertyNotExistException {
    JSONArray jsonArray = JSON.parse("", "[ \"House\" ]").toJSONArray();
    String value = jsonArray.digString("[0]");
    String expectedValue = "House";
    assertEquals(expectedValue, value);
  }

  @Test
  public void testDigNullBigDecimal() throws JSONParseException {
    JSONArray jsonArray = JSON.parse("", "[ 123456.789 ]").toJSONArray();
    BigDecimal value = jsonArray.digNullBigDecimal("[0]");
    BigDecimal expectedValue = new BigDecimal("123456.789");
    assertEquals(expectedValue, value);
  }

  @Test
  public void testDigNullBigInteger() throws JSONParseException {
    JSONArray jsonArray = JSON.parse("", "[ 62345324523452345234523452365476489 ]").toJSONArray();
    BigInteger value = jsonArray.digNullBigInteger("[0]");
    BigInteger expectedValue = new BigInteger("62345324523452345234523452365476489");
    assertEquals(expectedValue, value);
  }

  @Test
  public void testDigNullBoolean() throws JSONParseException {
    JSONArray jsonArray = JSON.parse("", "[ true ]").toJSONArray();
    Boolean value = jsonArray.digNullBoolean("[0]");
    Boolean expectedValue = Boolean.TRUE;
    assertEquals(expectedValue, value);
  }

  @Test
  public void testDigNullByte() throws JSONParseException {
    JSONArray jsonArray = JSON.parse("", "[ 42 ]").toJSONArray();
    Byte value = jsonArray.digNullByte("[0]");
    Byte expectedValue = Byte.valueOf("42");
    assertEquals(expectedValue, value);
  }

  @Test
  public void testDigNullCalendar() throws JSONParseException {
    JSONArray jsonArray = JSON.parse("", "[ \"2017-01-23T11:20:32.222Z-0000\" ]").toJSONArray();
    Calendar value = jsonArray.digNullCalendar("[0]");
    Calendar expectedValue = Calendar.getInstance();
    expectedValue.set(Calendar.MILLISECOND, 222);
    expectedValue.set(2017, 0, 23, 11, 20, 32);
    assertEquals(expectedValue, value);
  }

  @Test
  public void testDigNullCharacter() throws JSONParseException {
    JSONArray jsonArray = JSON.parse("", "[ \"a\", \"be\" ]").toJSONArray();
    Character value = jsonArray.digNullCharacter("[0]");
    Character expectedValue = 'a';
    assertEquals(expectedValue, value);
    value = jsonArray.digNullCharacter("[1]");
    expectedValue = 'b';
    assertEquals(expectedValue, value);
  }

  @Test
  public void testDigNullDouble() throws JSONParseException {
    JSONArray jsonArray = JSON.parse("", "[ 15.5 ]").toJSONArray();
    Double value = jsonArray.digNullDouble("[0]");
    Double expectedValue = 15.5;
    assertEquals(expectedValue, value);
  }

  @Test
  public void testDigNullFloat() throws JSONParseException {
    JSONArray jsonArray = JSON.parse("", "[ 123456.789 ]").toJSONArray();
    Float value = jsonArray.digNullFloat("[0]");
    Float expectedValue = Float.valueOf("123456.789");
    assertEquals(expectedValue, value, 3);
  }

  @Test
  public void testDigNullLong() throws JSONParseException {
    JSONArray jsonArray = JSON.parse("", "[ 1234523453245232345 ]").toJSONArray();
    Long value = jsonArray.digNullLong("[0]");
    Long expectedValue = 1234523453245232345L;
    assertEquals(expectedValue, value);
  }

  @Test
  public void testDigNullInteger() throws JSONParseException {
    JSONArray jsonArray = JSON.parse("", "[ 123456789 ]").toJSONArray();
    Integer value = jsonArray.digNullInteger("[0]");
    Integer expectedValue = 123456789;
    assertEquals(expectedValue, value);
  }

  @Test
  public void testDigNullObject() throws JSONParseException {
    JSONArray jsonArray = JSON.parse("", "[ { \"person\": { \"name\": \"John\" } } ]").toJSONArray();
    JSONObject jsonValue = jsonArray.digNullObject("[0].person");
    String value = jsonValue.toJSON();
    String expectedValue = "{ \"name\": \"John\" }";
    assertEquals(expectedValue, value);
  }

  @Test
  public void testDigNullShort() throws JSONParseException {
    JSONArray jsonArray = JSON.parse("", "[ 12789 ]").toJSONArray();
    Short value = jsonArray.digNullShort("[0]");
    short expectedValue = 12789;
    assertEquals(expectedValue, (short) value);
  }

  @Test
  public void testDigNullString() throws JSONParseException {
    JSONArray jsonArray = JSON.parse("", "[ \"House\" ]").toJSONArray();
    String value = jsonArray.digNullString("[0]");
    String expectedValue = "House";
    assertEquals(expectedValue, value);
  }

  @Test
  public void testToJSONReferencedArray() {
    Log.debug("Create a refered JSONArray using Java objects.");

    String expectedString = "{ \"version\": 1, \"countryId\": 1, \"countryName\": { \"version\": 1, \"language\": 97, \"nameType\": 1, \"word\": 2 } }";

    BookList bookList = new BookList();
    Book book;

    book = new Book(1, "El doble.");
    bookList.add(book);

    book = new Book(8, "El principito.");
    bookList.add(book);

    book = new Book(13, "Crónica de una muerte anunciada.");
    bookList.add(book);

    JSONArray jsonBookListArray = JSON.toJSONTree(bookList).toJSONArray();
    assertEquals("[ { \"id\": 1, \"name\": \"El doble.\" }, { \"id\": 8, \"name\": \"El principito.\" }, { \"id\": 13, \"name\": \"Crónica de una muerte anunciada.\" } ]", jsonBookListArray.toJSON());
  }
}
