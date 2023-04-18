package net.cabezudo.json;

import net.cabezudo.json.exceptions.EOSException;
import net.cabezudo.json.exceptions.ElementNotExistException;
import net.cabezudo.json.exceptions.JSONParseException;
import net.cabezudo.json.exceptions.PropertyNotExistException;
import net.cabezudo.json.exceptions.UnexpectedElementException;
import net.cabezudo.json.objects.Book;
import net.cabezudo.json.objects.Types;
import net.cabezudo.json.values.JSONArray;
import net.cabezudo.json.values.JSONNull;
import net.cabezudo.json.values.JSONObject;
import net.cabezudo.json.values.JSONValue;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * @author <a href="http://cabezudo.net">Esteban Cabezudo</a>
 * @version 0.9, 08/03/2016
 */
public class JSONTest {

  @TempDir
  public File folder;

  @Test
  public void testParse() throws ElementNotExistException {
    String jsonUnparsedString = "{ \"array\": [ 1, 2, \"3\", 4], \"boolean\": true, \"null\": null, \"number\": 324, \"anotherNumber\": 324.3, \"object\": { \"string\": \"George \\\"Baby Face\\\" Nelson\", \"number\": 234 } }";
    try {
      JSONObject jsonObject = JSON.parse("testParse", jsonUnparsedString).toJSONObject();

      JSONArray jsonArray = jsonObject.getJSONArray("array");
      JSONValue jsonValue = jsonArray.getValue(1);
      assertTrue(jsonValue.isNumber());

      jsonValue = jsonObject.getValue("boolean");
      assertTrue(jsonValue.isBoolean());

      jsonValue = jsonObject.getValue("null");
      assertTrue(jsonValue.isNull());

      jsonValue = jsonObject.getValue("anotherNumber");
      assertTrue(jsonValue.isNumber());

      jsonObject = jsonObject.getValue("object").toJSONObject();
      assertTrue(jsonObject.isObject());

      jsonValue = jsonObject.getValue("string");
      assertTrue(jsonValue.isString());
      String name = jsonValue.toString();
      assertEquals("George \"Baby Face\" Nelson", name);

      jsonValue = jsonObject.getValue("number");
      assertTrue(jsonValue.isNumber());

    } catch (PropertyNotExistException | JSONParseException e) {
      fail(e.getMessage());
    }
  }

  @Test
  public void testParsePath() throws IOException, ElementNotExistException {

    final File temporaryFile = new File(folder, "tempFile.txt");
    try {
      try (PrintWriter writer = new PrintWriter(temporaryFile, StandardCharsets.UTF_8)) {
        writer.println("{ \"array\": [ 1, 2, \"3\", 4], \"boolean\": true, \"null\": null, \"number\": 324, \"anotherNumber\": 324.3, \"object\": { \"string\": \"Esteban Cabezudo\", \"number\": 234 } }");
      }
    } catch (FileNotFoundException | UnsupportedEncodingException e) {
      fail(e.getMessage());
    }

    Path path;
    try {
      URI uri = temporaryFile.toURI();
      path = Paths.get(uri);

      JSONObject jsonObject = JSON.parse(path, "utf-8").toJSONObject();

      JSONArray jsonArray = jsonObject.getJSONArray("array");
      JSONValue jsonValue = jsonArray.getValue(1);
      assertTrue(jsonValue.isNumber());

      jsonValue = jsonObject.getValue("boolean");
      assertTrue(jsonValue.isBoolean());

      jsonValue = jsonObject.getValue("null");
      assertTrue(jsonValue.isNull());

      jsonValue = jsonObject.getValue("anotherNumber");
      assertTrue(jsonValue.isNumber());

      jsonObject = jsonObject.getValue("object").toJSONObject();
      assertTrue(jsonObject.isObject());

      jsonValue = jsonObject.getValue("string");
      assertTrue(jsonValue.isString());
      String name = jsonValue.toString();
      assertEquals("Esteban Cabezudo", name);

      jsonValue = jsonObject.getValue("number");
      assertTrue(jsonValue.isNumber());
    } catch (JSONParseException | PropertyNotExistException e) {
      fail(e.getMessage());
    }
  }

  @Test
  public void testParse01() throws JSONParseException {
    String jsonStringData;

    jsonStringData = "[ ]";
    JSON.parse("", jsonStringData);
  }

  @Test
  public void testParse02() throws JSONParseException {
    EOSException eosException = Assertions.assertThrows(EOSException.class, () -> {
      String jsonStringData;
      jsonStringData = "[ \"John\", \"Peter\" ";
      JSON.parse("", jsonStringData);
    });
    Assertions.assertNotNull(eosException);
  }

  @Test
  public void testParse03() throws JSONParseException {
    String jsonStringData = "[ \"John\", \"Peter\" a";
    checkUnexpectedElementException(jsonStringData, "a", 1, 19);
  }

  @Test
  public void testParse04() throws JSONParseException {
    String jsonStringData = "[ \"John\", \"Peter\" }";
    checkUnexpectedElementException(jsonStringData, "comma or right bracket", "}", 1, 19);
  }

  @Test
  public void testParse05() throws JSONParseException {
    String jsonStringData = "[ { \"person\": { \"name\": \"John\" } ]";
    checkUnexpectedElementException(jsonStringData, "comma or right brace", "]", 1, 34);
  }

  @Test
  public void testParse06() throws JSONParseException {
    String jsonStringData = "[ { \"person\": { \"name\": \"John\" }, ]";
    checkUnexpectedElementException(jsonStringData, "string", "]", 1, 35);
  }

  @Test
  public void testParse07() throws JSONParseException {
    String jsonStringData = "[ { \"person\": { \"name\": \"John\" }, { \"person\": { \"name\": \"John\" }, ]";
    checkUnexpectedElementException(jsonStringData, "string", "{", 1, 35);
  }

  @Test
  public void testParse08() throws JSONParseException {
    String jsonStringData = "[ { \"person\": { \"name\": \"John\" } }, { \"person\": { \"name\": \"Peter\" } } ]";
    JSON.parse("", jsonStringData);
  }

  @Test
  public void testParse09() throws JSONParseException {
    String jsonStringData = "[ { \"person\": { \"name\": \"John\" } }, { \"person\": { \"name\": \"Peter\" } }, ]";
    checkUnexpectedElementException(jsonStringData, "value", "]", 1, 72);
  }

  @Test
  public void testParse10() throws JSONParseException {
    String jsonStringData = "[ { \"person\": { \"name\": \"John\" } }, { \"person\": { \"name\": \"Peter\" }, \"position\": { \"name\": \"Technical leader\" } } ]";
    JSON.parse("", jsonStringData);
  }

  @Test
  public void testParse11() throws JSONParseException {
    String jsonStringData = "{ \"name\": \"John\", \"childs\": [ { \"name\": \"Peter\" }, { \"name\": \"Jhon\" } ] }";
    checkUnexpectedElementException(jsonStringData, "comma or right brace", "}", 1, 31);
  }

  @Test
  public void testParse12() throws JSONParseException {
    String jsonStringData
        = "{\n"
        + "  \"coreVersion\": \"1.00.00\",\n"
        + "  \"general\": {\n"
        + "    \"initialSection\": \"sites.list\"\n"
        + "  }, \n"
        + "  \"data\": {\n"
        + "  },\n"
        + "  \"info\": \"one\"\n"
        + "}\n";

    String expected = "{ \"coreVersion\": \"1.00.00\", \"general\": { \"initialSection\": \"sites.list\" }, \"data\": {  }, \"info\": \"one\" }";

    JSONObject jsonValue = JSON.parse("", jsonStringData).toJSONObject();
    assertEquals(expected, jsonValue.toJSON());
  }

  @Test
  public void testParse13() throws JSONParseException {
    String jsonStringData
        = "{\n"
        + "  \"coreVersion\": \"1.00.00\",\n"
        + "  \"general\": {\n"
        + "    \"initialSection\": \"sites.list\"\n"
        + "  }, \n"
        + "  \"data\": [\n"
        + "  ],\n"
        + "  \"info\": \"one\"\n"
        + "}\n";

    String expected = "{ \"coreVersion\": \"1.00.00\", \"general\": { \"initialSection\": \"sites.list\" }, \"data\": [  ], \"info\": \"one\" }";

    JSONObject jsonValue = JSON.parse("", jsonStringData).toJSONObject();
    assertEquals(expected, jsonValue.toJSON());
  }

  @Test
  public void testParse14() throws JSONParseException {
    String jsonStringData
        = "{\n"
        + "  \"opacity\": 0.03,\n"
        + "}\n";

    String expected = "{ \"opacity\": 0.03 }";

    JSONObject jsonValue = JSON.parse("", jsonStringData).toJSONObject();
    assertEquals(expected, jsonValue.toJSON());
  }

  @Test
  public void testParseWithEndsOfLines() throws PropertyNotExistException {
    String jsonStringData
        = "{\n"
        + "  \"coreVersion\": \"1.00.00\",\n"
        + "  \"general\": {\n"
        + "    \"defaultLocale\": \"es\",\n"
        + "    \"defaultCountry\": \"MX\",\n"
        + "    \"initialSection\": \"sites.list\"\n"
        + "  }\n"
        + "}\n";
    try {
      JSONObject jsonData = JSON.parse("", jsonStringData).toJSONObject();
      assertEquals("1.00.00", jsonData.getString("coreVersion"));
      assertEquals("es", jsonData.digString("general.defaultLocale"));
      assertEquals("MX", jsonData.digString("general.defaultCountry"));
      assertEquals("sites.list", jsonData.digString("general.initialSection"));
    } catch (JSONParseException e) {
      System.out.println(e.getMessage() + " " + e.getPosition());
    }
  }

  private void checkUnexpectedElementException(
      String jsonStringData, String value, int line, int row)
      throws JSONParseException {
    try {
      JSON.parse("", jsonStringData);
    } catch (UnexpectedElementException e) {
      UnexpectedElementException expectedException = new UnexpectedElementException(value, new Position("", line, row));
      assertEquals(expectedException, e);
    }
  }

  private void checkUnexpectedElementException(
      String jsonStringData, String expected, String have, int line, int row)
      throws JSONParseException {
    try {
      JSON.parse("", jsonStringData);
    } catch (UnexpectedElementException e) {
      UnexpectedElementException expectedException = new UnexpectedElementException(expected, have, new Position("", line, row));
      assertEquals(expectedException, e);
    }
  }

  public void testToJSONArray() throws ElementNotExistException {

    List<JSONable> list = new ArrayList<>();

    Book book;

    book = new Book(1, "Evolution");
    list.add(book);
    book = new Book(2, "The double");
    list.add(book);

    JSONArray jsonArray = JSON.toJSONArray(list);
    assertEquals(2, jsonArray.size());

    JSONValue jsonValue;
    jsonValue = jsonArray.getValue(0);
    assertEquals("{ \"id\": 1, \"name\": \"Evolution\" }", jsonValue.toJSON());
    jsonValue = jsonArray.getValue(1);
    assertEquals("{ \"id\": 2, \"name\": \"The double\" }", jsonValue.toJSON());
  }

  @Test
  public void testToJSONTree() {

    try {
      Types types = new Types();
      JSONObject jsonTypesValue = JSON.toJSONTree(types).toJSONObject();
      assertEquals(BigDecimal.class, jsonTypesValue.getBigDecimal("bigDecimal").getClass());
      assertEquals(BigInteger.class, jsonTypesValue.getBigInteger("bigInteger").getClass());
      assertEquals(byte[].class, jsonTypesValue.getByteArray("byteArray").getClass());
      assertEquals(GregorianCalendar.class, jsonTypesValue.getCalendar("calendar").getClass());
      assertEquals(Boolean.class, jsonTypesValue.getBoolean("oBoolean").getClass());
      assertEquals(Byte.class, jsonTypesValue.getByte("oByte").getClass());
      assertEquals(Character.class, jsonTypesValue.getCharacter("oCharacter").getClass());
      assertEquals(Double.class, jsonTypesValue.getDouble("oDouble").getClass());
      assertEquals(Float.class, jsonTypesValue.getFloat("oFloat").getClass());
      assertEquals(Integer.class, jsonTypesValue.getInteger("oInteger").getClass());
      assertEquals(Long.class, jsonTypesValue.getLong("oLong").getClass());
      assertEquals(Short.class, jsonTypesValue.getShort("oShort").getClass());
      assertEquals(Boolean.class, jsonTypesValue.getBoolean("pBoolean").getClass());
      assertEquals(Byte.class, jsonTypesValue.getByte("pByte").getClass());
      assertEquals(Character.class, jsonTypesValue.getCharacter("pCharacter").getClass());
      assertEquals(Double.class, jsonTypesValue.getDouble("pDouble").getClass());
      assertEquals(Float.class, jsonTypesValue.getFloat("pFloat").getClass());
      assertEquals(Integer.class, jsonTypesValue.getInteger("pInteger").getClass());
      assertEquals(Long.class, jsonTypesValue.getLong("pLong").getClass());
      assertEquals(Short.class, jsonTypesValue.getShort("pShort").getClass());
      assertEquals(String.class, jsonTypesValue.getString("string").getClass());
      assertEquals(types.getBigDecimal(), jsonTypesValue.getBigDecimal("bigDecimal"));
      assertEquals(types.getBigInteger(), jsonTypesValue.getBigInteger("bigInteger"));
      assertArrayEquals(types.getByteArray(), jsonTypesValue.getByteArray("byteArray"));
      assertEquals(types.getCalendar(), jsonTypesValue.getCalendar("calendar"));
      assertEquals(types.isOBoolean(), jsonTypesValue.getBoolean("oBoolean"));
      assertEquals(types.getOByte(), jsonTypesValue.getByte("oByte"));
      assertEquals(types.getOCharacter(), jsonTypesValue.getCharacter("oCharacter"));
      assertEquals(types.getODouble(), jsonTypesValue.getDouble("oDouble"));
      assertEquals(types.getOFloat(), jsonTypesValue.getFloat("oFloat"));
      assertEquals(types.getOInteger(), jsonTypesValue.getInteger("oInteger"));
      assertEquals(types.getOLong(), jsonTypesValue.getLong("oLong"));
      assertEquals(types.getOShort(), jsonTypesValue.getShort("oShort"));
      assertEquals(new JSONNull(), jsonTypesValue.getValue("nullReference"));
      assertEquals(types.isPBoolean(), jsonTypesValue.getBoolean("pBoolean"));
      assertEquals(types.getPByte(), (long) jsonTypesValue.getByte("pByte"));
      assertEquals(types.getPCharacter(), (long) jsonTypesValue.getCharacter("pCharacter"));
      assertEquals(types.getPDouble(), jsonTypesValue.getDouble("pDouble"), 0.1);
      assertEquals(types.getPFloat(), jsonTypesValue.getFloat("pFloat"), 0.1);
      assertEquals(types.getPInteger(), (long) jsonTypesValue.getInteger("pInteger"));
      assertEquals(types.getPLong(), (long) jsonTypesValue.getLong("pLong"));
      assertEquals(types.getPShort(), (long) jsonTypesValue.getShort("pShort"));
      assertEquals(types.getString(), jsonTypesValue.getString("string"));
    } catch (PropertyNotExistException e) {
      fail(e.getMessage());
    }
  }
}
