package net.cabezudo.json.docs;

import net.cabezudo.json.Formatter;
import net.cabezudo.json.JSON;
import net.cabezudo.json.JSONElement;
import net.cabezudo.json.JSONPair;
import net.cabezudo.json.exceptions.DuplicateKeyException;
import net.cabezudo.json.exceptions.JSONParseException;
import net.cabezudo.json.exceptions.PropertyNotExistException;
import net.cabezudo.json.objects.Person;
import net.cabezudo.json.values.JSONArray;
import net.cabezudo.json.values.JSONObject;
import net.cabezudo.json.values.JSONString;
import net.cabezudo.json.values.JSONValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Calendar;
import java.util.List;

/**
 * @author <a href="http://cabezudo.net">Esteban Cabezudo</a>
 * @version 0.9, 01/20/2017
 */
public class Examples {

  private static final Logger log = LoggerFactory.getLogger(Examples.class);

  private final String jsonStringData = "{ \"name\": \"Jorge Méndez\", \"age\": 34, \"maritalStatus\": { \"id\": 8, \"name\": \"married\", \"happy\": true }, \"childs\": [ { \"id\": 6, \"name\": \"Huey\", \"age\": 5 }, { \"id\": 15, \"name\": \"Dewey\", \"age\": 6 }, { \"id\": 9, \"name\": \"Louie\", \"age\": 7 } ] }";

  public static void main(String[] args) throws JSONParseException, DuplicateKeyException {
    Examples p = new Examples();
    p.testStructureCreationFromJSONElements();
    p.testParseAJSONString();
    p.testGetPropertiesFromAnObject();
    p.testGetABooleanInOneLine();
    p.testDigABooleanInOneLine();
    p.testGetAPropertyUsingAnIndex();
    p.testTraverseAnArray();
    p.testGetTheNumberOfChildren();
    p.testAddPropertiesToAnObject();
    p.testRemovePropertiesFromAnObject();
    p.testGetChildsFromAnObject();
    p.testOnePairFromAnObjectUsingThePropertyName();
    p.testGetAReferencedElement();
    p.testGetACustomReferencedObject();
    p.testCreateAJSONStructureFromPOJO();
    p.testCreateAJSONStructureFromPOJOWithReferences();
    p.testGetAnElementFromArrayUsingTheIndex();
    p.testDigStringFromAnArray();
    p.testGetCalendar();
    p.testCreateDateProperty();
  }

  private void testStructureCreationFromJSONElements() throws DuplicateKeyException {
    System.out.println("\n" + (char) 27 + "[32m*** Structure creation from JSON elements.");

    JSONObject jsonObject = new JSONObject();
    jsonObject.add(new JSONPair("name", "Esteban"));
    jsonObject.add(new JSONPair("lastName", "Cabezudo"));
    jsonObject.add(new JSONPair("height", 1.84));

    String jsonStringObject = jsonObject.toJSON();
    System.out.println(jsonStringObject);
    System.out.println(Formatter.indent(jsonStringObject));
  }

  private void testParseAJSONString() {
    System.out.println("\n" + (char) 27 + "[32m*** Parse a JSON string.");

    JSONValue jsonValue;
    try {
      jsonValue = JSON.parse("", jsonStringData);
    } catch (JSONParseException e) {
      System.out.println(e.getMessage());
      System.out.println(jsonStringData);
      for (int i = 1; i < e.getPosition().getRow(); i++) {
        System.out.print(" ");
      }
      System.out.println("^");
      return;
    }

    System.out.println(Formatter.indent(jsonValue.toJSON()));

    System.out.println("Is an object: " + jsonValue.isObject());

    JSONObject jsonObject = jsonValue.toJSONObject();
  }

  private void testGetPropertiesFromAnObject() throws JSONParseException {
    System.out.println("\n" + (char) 27 + "[32m*** Get properties from an object.");
    JSONObject jsonObject = JSON.parse("", jsonStringData).toJSONObject();

    try {
      JSONValue jsonNameValue = jsonObject.getValue("name");
      System.out.println("name: " + jsonNameValue);

    } catch (PropertyNotExistException e) {
      log.error(e.getMessage());
    }

    JSONValue jsonNameValue = jsonObject.getNullValue("nonExistingProperty");
    System.out.println("nonExistingProperty: " + jsonNameValue);

    JSONValue jsonMaritalStatusNameValue = jsonObject.digNullValue("maritalStatus.name");
    System.out.println("maritalStatus.name: " + jsonMaritalStatusNameValue);

    JSONValue jsonMaritalStatusValue = jsonObject.getNullValue("maritalStatus");
    JSONObject jsonMaritalStatusObject = jsonMaritalStatusValue.toJSONObject();
    JSONValue jsonHappyValue = jsonMaritalStatusObject.getNullValue("happy");
    boolean happy = jsonHappyValue.toBoolean();
    System.out.println("Is happy: " + happy);
  }

  private void testGetABooleanInOneLine() throws JSONParseException {
    System.out.println("\n" + (char) 27 + "[32m*** Get a boolean in one line.");
    JSONObject jsonObject = JSON.parse("", jsonStringData).toJSONObject();

    JSONObject jsonMaritalStatusObject = jsonObject.getNullObject("maritalStatus");
    Boolean happy = jsonMaritalStatusObject.getNullBoolean("happy");
    System.out.println("Is happy: " + happy);
  }

  private void testDigABooleanInOneLine() throws JSONParseException {
    System.out.println("\n" + (char) 27 + "[32m*** Dig a boolean in one line.");
    JSONObject jsonObject = JSON.parse("", jsonStringData).toJSONObject();

    Boolean happy = jsonObject.digNullBoolean("maritalStatus.happy");
    System.out.println("Is happy: " + happy);
  }

  private void testGetAPropertyUsingAnIndex() throws JSONParseException {
    System.out.println("\n" + (char) 27 + "[32m*** Get a property using an index.");
    JSONObject jsonObject = JSON.parse("", jsonStringData).toJSONObject();

    int age = jsonObject.getNullInteger(1);
    System.out.println("age: " + age);
  }

  private void testTraverseAnArray() throws JSONParseException {
    System.out.println("\n" + (char) 27 + "[32m*** Traverse an array.");
    JSONObject jsonObject = JSON.parse("", jsonStringData).toJSONObject();

    JSONArray jsonArray = jsonObject.getNullJSONArray("childs");
    for (JSONValue jsonItemValue : jsonArray) {
      JSONObject child = jsonItemValue.toJSONObject();
      String name = child.getNullString("name");
      int age = child.getNullInteger("age");
      System.out.println(name + " as " + age + " years old.");
    }
  }

  private void testGetTheNumberOfChildren() throws JSONParseException {
    System.out.println("\n" + (char) 27 + "[32m*** Get the number of children.");
    JSONObject jsonObject = JSON.parse("", jsonStringData).toJSONObject();

    int objectSize = jsonObject.size();
    System.out.println("Object size: " + objectSize);
    JSONArray jsonArray = jsonObject.getNullJSONArray("childs");
    int arraySize = jsonArray.size();
    System.out.println("Array size: " + arraySize);
  }

  private void testAddPropertiesToAnObject() throws JSONParseException, DuplicateKeyException {
    System.out.println("\n" + (char) 27 + "[32m*** Add properties to an object.");
    JSONObject jsonObject = JSON.parse("", jsonStringData).toJSONObject();

    JSONPair jsonHairColorPair = new JSONPair("hairColor", "black");
    jsonObject.add(jsonHairColorPair);
    System.out.println(Formatter.indent(jsonObject.toJSON()));
  }

  private void testRemovePropertiesFromAnObject() throws JSONParseException {
    System.out.println("\n" + (char) 27 + "[32m*** Remove properties from an object.");
    JSONObject jsonObject = JSON.parse("", jsonStringData).toJSONObject();

    jsonObject.remove("hairColor");
    System.out.println(Formatter.indent(jsonObject.toJSON()));
  }

  private void testGetChildsFromAnObject() throws JSONParseException {
    System.out.println("\n" + (char) 27 + "[32m*** Get childs from an object.");
    JSONObject jsonObject = JSON.parse("", jsonStringData).toJSONObject();

    List<JSONPair> childs = jsonObject.getChilds();
    for (JSONPair child : childs) {
      System.out.println(child);
    }
  }

  private void testOnePairFromAnObjectUsingThePropertyName() throws JSONParseException {
    System.out.println("\n" + (char) 27 + "[32m*** One pair from an object using the property name.");
    JSONObject jsonObject = JSON.parse("", jsonStringData).toJSONObject();

    JSONPair jsonPair = jsonObject.getNullElement("age");
    System.out.println(jsonPair);
  }

  private void testGetAReferencedElement() throws JSONParseException {
    System.out.println("\n" + (char) 27 + "[32m*** Get a referenced element.");
    JSONObject jsonObject = JSON.parse("", jsonStringData).toJSONObject();

    JSONValue jsonReferencedElement = jsonObject.toReferencedElement();
    System.out.println(jsonReferencedElement);
  }

  private void testGetACustomReferencedObject() throws JSONParseException, DuplicateKeyException {
    System.out.println("\n" + (char) 27 + "[32m*** Get a custom referenced object.");
    JSONObject jsonObject = JSON.parse("", jsonStringData).toJSONObject();

    JSONObject maritalStatus = jsonObject.getNullObject("maritalStatus");
    maritalStatus.setReferenceFieldName("name");
    jsonObject.setReferenceFieldName("name");

    JSONObject jsonReferencedObject = jsonObject.toReferencedObject();
    System.out.println(jsonReferencedObject);
    JSONValue jsonReferencedElement = jsonObject.toReferencedElement();
    System.out.println(jsonReferencedElement);
  }

  private void testCreateAJSONStructureFromPOJO() {
    System.out.println("\n" + (char) 27 + "[32m*** Create a JSON structure from POJO.");

    Person son = new Person("Julio", "Perez", 12);
    Person person = new Person("Juan", "Perez", 34, son);
    JSONObject jsonPersonObject = JSON.toJSONObject(person);
    System.out.println(jsonPersonObject);
  }

  private void testCreateAJSONStructureFromPOJOWithReferences() throws DuplicateKeyException {
    System.out.println("\n" + (char) 27 + "[32m*** Create a JSON structure from POJO with references.");

    Person son = new Person("Julio", "Perez", 12);
    Person person = new Person("Juan", "Perez", 34, son);
    JSONObject jsonPerson = JSON.toJSONObject(person);
    jsonPerson.setReferenceFieldName("name");
    JSONElement jsonReferencedElement = jsonPerson.toReferencedObject();
    System.out.println(jsonReferencedElement);
  }

  private void testGetAnElementFromArrayUsingTheIndex() throws JSONParseException {
    System.out.println("\n" + (char) 27 + "[32m*** Get an element from an array using the index.");
    JSONObject jsonObject = JSON.parse("", jsonStringData).toJSONObject();
    JSONArray jsonArray = jsonObject.getNullJSONArray("childs");
    JSONObject jsonFirstChild = jsonArray.getNullObject(0);
    System.out.println("First child: " + jsonFirstChild);
  }

  private void testDigStringFromAnArray() throws JSONParseException {
    System.out.println("\n" + (char) 27 + "[32m*** Dig string from an array.");
    JSONObject jsonObject = JSON.parse("", jsonStringData).toJSONObject();

    JSONObject jsonChild = jsonObject.digNullObject("childs.[0]");
    System.out.println("Child: " + jsonChild);

    String name = jsonObject.digNullString("childs.[0].name");
    System.out.println("First child name: " + name);
  }

  private void testGetCalendar() throws JSONParseException {
    System.out.println("\n" + (char) 27 + "[32m*** Get calendar.");

    System.out.println(JSON.SIMPLE_DATE_FORMAT_PATTERN);

    JSONObject jsonObject = JSON.parse("", "{ \"date\": \"2017-01-23T11:20:32.222Z-0000\" }").toJSONObject();

    Calendar calendar = jsonObject.getNullCalendar("date");
    System.out.println("Calendar: " + calendar);

  }

  private void testCreateDateProperty() throws DuplicateKeyException {
    System.out.println("\n" + (char) 27 + "[32m*** Create date property.");

    Calendar calendar = Calendar.getInstance();
    JSONObject jsonObject = new JSONObject();
    JSONString jsonDateString = new JSONString(calendar);
    JSONPair jsonDatePair = new JSONPair("date", jsonDateString);
    jsonObject.add(jsonDatePair);
    System.out.println("Date: " + jsonObject.digNullString("date"));
  }
}
