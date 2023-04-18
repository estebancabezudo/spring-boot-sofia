package net.cabezudo.json;

import net.cabezudo.json.exceptions.DuplicateKeyException;
import net.cabezudo.json.exceptions.JSONParseException;
import net.cabezudo.json.exceptions.PropertyNotExistException;
import net.cabezudo.json.values.JSONObject;

public class SmokeTest {

  public static void main(String... args) throws JSONParseException, PropertyNotExistException, DuplicateKeyException {
    JSONPair a = new JSONPair("a", "a");
    JSONPair b = new JSONPair("b", "b");
    JSONPair c = new JSONPair("c", "c");
    JSONObject jsonObjectExpected = new JSONObject("main()", "{\"a\": \"a\", \"b\":\"b\", \"c\":\"c\"}");
    JSONObject jsonObjectCreated = new JSONObject(a, b, c);
    System.out.println(jsonObjectExpected);
    System.out.println(jsonObjectCreated);
  }
}
