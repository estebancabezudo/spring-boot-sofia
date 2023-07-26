/**
 * MIT License
 * <p>
 * Copyright (c) 2017 Esteban Cabezudo
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package net.cabezudo.json.values;

import net.cabezudo.json.JSON;
import net.cabezudo.json.JSONElement;
import net.cabezudo.json.JSONPair;
import net.cabezudo.json.Position;
import net.cabezudo.json.exceptions.DuplicateKeyException;
import net.cabezudo.json.exceptions.InvalidReferencedValue;
import net.cabezudo.json.exceptions.JSONParseException;
import net.cabezudo.json.exceptions.PropertyIndexNotExistException;
import net.cabezudo.json.exceptions.PropertyNotExistException;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * A {@link net.cabezudo.json.values.JSONObject} is an object extended from {@link net.cabezudo.json.values.JSONValue} object in order to represent a JSON object that can be used
 * to create JSON structures.
 *
 * <p>
 * A {@link net.cabezudo.json.values.JSONObject} is a list of {@link net.cabezudo.json.JSONPair} objects that represent the JSON object structure.
 *
 * @author <a href="http://cabezudo.net">Esteban Cabezudo</a>
 * @version 0.9, 10/01/2014
 */
public class JSONObject extends JSONValue<JSONObject> implements Iterable<JSONPair> {

  private final Set<String> keys = new TreeSet<>();

  private final List<JSONPair> list = new ArrayList<>();
  private final Map<String, JSONPair> map = new HashMap<>();

  /**
   * Create a new {@link net.cabezudo.json.values.JSONObject} object using a JSON string.
   *
   * <p>
   * This constructor parse a string passed by parameter in order to create the {@link net.cabezudo.json.values.JSONObject} that represents the JSON object structure.
   *
   * @param data The JSON string to parse.
   * @throws JSONParseException if the string passed by parameter can't be parsed.
   */
  public JSONObject(String origin, String data) throws JSONParseException, DuplicateKeyException {
    super(null);
    JSONValue jsonData = JSON.parse(origin, data);
    if (jsonData instanceof JSONObject) {
      JSONObject jsonObject = (JSONObject) jsonData;
      copy(jsonObject);
    } else {
      throw new JSONParseException("I can't parse the parameter to a JSONObject.", new Position(origin));
    }
  }

  /**
   * Construct an empty {@link net.cabezudo.json.values.JSONObject} object.
   * <p>
   * An empty {@link net.cabezudo.json.values.JSONObject} object can be used to create a more complex object by adding {@link net.cabezudo.json.JSONPair} objects.
   */
  public JSONObject() {
    super(null);
    // Nothing to do here. Just for convenience.
  }

  /**
   * Construct an empty {@link net.cabezudo.json.values.JSONObject} object.
   * <p>
   * An empty {@link net.cabezudo.json.values.JSONObject} object can be used to create a more complex object by adding {@link net.cabezudo.json.JSONPair} objects.
   *
   * @param position The position for the {@link net.cabezudo.json.values.JSONObject} object in the JSON source.
   */
  public JSONObject(Position position) {
    super(position);
  }

  /**
   * Construct a {@link net.cabezudo.json.values.JSONObject} object using an array of {@link net.cabezudo.json.JSONPair} objects passed by parameter in order to create the JSON
   * object properties.
   *
   * @param jsonPairs the array of {@link net.cabezudo.json.JSONPair} objects to create the JSON object properties.
   */
  public JSONObject(JSONPair... jsonPairs) throws DuplicateKeyException {
    super(null);
    for (JSONPair jsonPair : jsonPairs) {
      privateAdd(jsonPair);
    }
  }

  /**
   * Construct a {@link net.cabezudo.json.values.JSONObject} object using the properties from another {@link net.cabezudo.json.values.JSONObject} object.
   *
   * <p>
   * Create a copy of the {@link net.cabezudo.json.values.JSONObject} object passed by parameter.
   *
   * @param jsonObject the {@link net.cabezudo.json.values.JSONObject} object which from where properties are taken.
   */
  public JSONObject(JSONObject jsonObject) throws DuplicateKeyException {
    super(jsonObject.getPosition());
    copy(jsonObject);
  }

  /**
   * Construct a {@link net.cabezudo.json.values.JSONObject} object from an {@code Object} using the {@link net.cabezudo.json.JSON#toJSONTree(java.lang.Object)} method.
   * <p>
   * The object must have the properties annotated with {@link net.cabezudo.json.annotations.JSONProperty} in order to be used as a JSON object property. If the object is
   * {@code Iterable} or the object is a primitive array the method throws a {@link net.cabezudo.json.exceptions.JSONConversionException}.
   *
   * @param object a POJO {@code Object}.
   */
  public JSONObject(Object object) throws DuplicateKeyException {
    this(JSON.toJSONTree(object).toJSONObject());
  }

  private void copy(JSONObject jsonObject) throws DuplicateKeyException {
    for (JSONPair jsonPair : jsonObject.list) {
      this.add(jsonPair);
    }
  }

  public List<String> getKeyList() {
    return new ArrayList<String>(keys);
  }

  private JSONPair privateAdd(JSONPair jsonPair) throws DuplicateKeyException {
    String key = jsonPair.getKey();
    JSONPair keyJSONPair = map.get(key);
    if (keyJSONPair != null) {
      throw new DuplicateKeyException("The object " + this.toJSON() + " already has the key " + key + ".", key);
    }
    list.add(jsonPair);
    keys.add(key);
    return map.put(key, jsonPair);
  }

  /**
   * Add a {@link net.cabezudo.json.JSONPair} to the list of properties of {@code this} object.
   *
   * @param jsonPair a {@link net.cabezudo.json.JSONPair}.
   * @return the same {@link net.cabezudo.json.JSONPair} passed.
   */
  public JSONPair add(JSONPair jsonPair) throws DuplicateKeyException {
    return privateAdd(jsonPair);
  }

  /**
   * Add properties from a {@link net.cabezudo.json.values.JSONObject} to the actual object. If the property doesn't exists in the actual object add it. If the property exists in
   * the actual object and the value is not an object, leave unchanged. If the property exists in the actual object and the value is an object merge the object.
   *
   * @param jsonObject the {@link net.cabezudo.json.values.JSONObject} from which to add the properties..
   */
  public void merge(JSONObject jsonObject) {
    jsonObject.list.forEach((jsonPair) -> {
      String key = jsonPair.getKey();
      JSONValue value = this.getNullValue(key);
      if (value == null) {
        try {
          privateAdd(jsonPair);
        } catch (DuplicateKeyException e) {
          throw new RuntimeException(e);
        }
      } else {
        if (value.isObject()) {
          JSONObject object = value.toJSONObject();
          object.merge(jsonPair.getValue().toJSONObject());
        }
      }
    });
  }

  /**
   * Add properties from a {@link net.cabezudo.json.values.JSONObject} to the actual object.If the property doesn't exists in the actual object add it.If the property exists in the
   * actual object and the value is not an object, leave unchanged. If the property exists in the actual object and the value is an object merge the object.
   *
   * @param jsonObject       the {@link net.cabezudo.json.values.JSONObject} from which to add the properties..
   * @param acceptDuplicates throw an exception if there is a key with the same path
   * @throws net.cabezudo.json.exceptions.DuplicateKeyException
   */
  public void merge(JSONObject jsonObject, boolean acceptDuplicates) throws DuplicateKeyException {
    for (JSONPair jsonPair : jsonObject.list) {
      String key = jsonPair.getKey();
      JSONValue value = this.getNullValue(key);
      if (value == null) {
        privateAdd(jsonPair);
      } else {
        if (value.isObject()) {
          JSONObject object = value.toJSONObject();
          object.merge(jsonPair.getValue().toJSONObject());
        } else {
          if (!acceptDuplicates) {
            throw new DuplicateKeyException("The key " + key + " already defined.", key, jsonPair.getPosition(), value.getPosition());
          }
        }
      }
    }
  }

  /**
   * Replace the properties from a {@link net.cabezudo.json.values.JSONObject} in the actual object. If the property doesn't exists in the actual object. Add it. If the property
   * exists in the actual object and the value is not an object, replace the value. If the property exists in the actual object and the value is an object replace the object.
   *
   * @param jsonObject the {@link net.cabezudo.json.values.JSONObject} from which to add the properties..
   */
  public void replace(JSONObject jsonObject) {
    jsonObject.list.forEach((jsonPair) -> {
      String key = jsonPair.getKey();
      JSONValue value = this.getNullValue(key);
      if (value == null) {
        try {
          privateAdd(jsonPair);
        } catch (DuplicateKeyException e) {
          throw new RuntimeException(e);
        }
      } else {
        if (value.isObject()) {
          JSONObject object = value.toJSONObject();
          object.replace(jsonPair.getValue().toJSONObject());
        } else {
          this.remove(key);
          try {
            privateAdd(jsonPair);
          } catch (DuplicateKeyException e) {
            throw new RuntimeException(e);
          }
        }
      }
    });
  }

  /**
   * Replace the properties from a {@link net.cabezudo.json.values.JSONObject} in the actual object.If the property doesn't exists in the actual object.Add it. If the property
   * exists in the actual object, and the value is not an object, and <tt>acceptDuplicates</tt> is <tt>true</tt> replace the value. If the property exists in the actual object, and
   * the value is an object, and <tt>acceptDuplicates</tt> is <tt>true</tt> replace the object.
   *
   * @param jsonObject       the {@link net.cabezudo.json.values.JSONObject} from which to add the properties..
   * @param acceptDuplicates if <tt>true</tt> an existing key value is replaced, if <tt>false</tt> throw an exception when the key already exists
   * @throws net.cabezudo.json.exceptions.DuplicateKeyException if the acceptDuplicates are <tt>false</tt> and the key already exists
   */
  public void replace(JSONObject jsonObject, boolean acceptDuplicates) throws DuplicateKeyException {
    for (JSONPair jsonPair : jsonObject.list) {
      String key = jsonPair.getKey();
      JSONValue value = this.getNullValue(key);
      if (value == null) {
        privateAdd(jsonPair);
      } else {
        if (acceptDuplicates) {
          if (value.isObject()) {
            JSONObject object = value.toJSONObject();
            object.replace(jsonPair.getValue().toJSONObject());
          } else {
            this.remove(key);
            privateAdd(jsonPair);
          }
        } else {
          throw new DuplicateKeyException("The key " + key + " already defined.", key, jsonPair.getPosition(), value.getPosition());
        }
      }
    }
  }

  /**
   * /**
   * Returns <tt>true</tt> if this {@link net.cabezudo.json.values.JSONObject} contains the specified element.
   *
   * @param propertyName whose presence in this {@link net.cabezudo.json.values.JSONObject} is to be tested
   * @return <tt>true</tt> if this {@link net.cabezudo.json.values.JSONObject} contains the specified element
   */
  public boolean contains(String propertyName) {
    return this.keys.contains(propertyName);
  }

  /**
   * Compare two {@link net.cabezudo.json.values.JSONObject} objects.
   * <p>
   * The rules for comparison are the next:
   * <p>
   * The method first compares the number of properties and returns a value less than {@code 0} if {@code this} {@link net.cabezudo.json.values.JSONObject} has less properties than
   * the argument; and a value greater than {@code 0} if {@code this} {@link net.cabezudo.json.values.JSONObject} has more properties than the argument.
   * <p>
   * If the the number of properties comparison is {@code 0} then compare the keys in natural order. Compare one at a time the keys and the result is a negative integer if the
   * property name of {@code this} {@link net.cabezudo.json.values.JSONObject} object lexicographically precedes the property name of the argument
   * {@link net.cabezudo.json.values.JSONObject} object. The result is a positive integer if the property of {@code this} {@link net.cabezudo.json.values.JSONObject} object
   * lexicographically follows the argument property name in the same position. The result is zero if all the property names are equal and in the same order.
   * <p>
   * If the properties names are the same, compare the values. The method uses the property order to compare the values. Compare one at a time the values and the result is a
   * negative integer if the property value of {@code this} {@link net.cabezudo.json.values.JSONObject} object is less than the property value of the argument
   * {@link net.cabezudo.json.values.JSONObject} object for the same property. The result is a positive integer if the property value of {@code this}
   * {@link net.cabezudo.json.values.JSONObject} object is greater than the property value of the argument {@link net.cabezudo.json.values.JSONObject} object for the same property.
   * The result is zero if the property values are equal for all the properties.
   *
   * @param jsonObject the {@link net.cabezudo.json.values.JSONObject} to be compared.
   * @return the value {@code 0} if {@code this} {@link net.cabezudo.json.values.JSONObject} is equal to the argument {@link net.cabezudo.json.values.JSONObject}; a value less than
   * {@code 0} if {@code this} {@link net.cabezudo.json.values.JSONObject} is less (using the rules) than the argument {@link net.cabezudo.json.values.JSONObject} object; and a
   * value greater than {@code 0} if {@code this} {@link net.cabezudo.json.values.JSONObject} object is greater (using the rules) than the argument
   * {@link net.cabezudo.json.values.JSONObject}.
   */
  @Override
  public int compareTo(JSONObject jsonObject) {
    Integer a = this.size();
    Integer b = jsonObject.size();
    int c = a.compareTo(b);

    if (c != 0) {
      return c;
    }

    List<String> keyListOfThis = this.getKeyList();
    List<String> keyListOfObject = jsonObject.getKeyList();

    int size = keyListOfThis.size();

    for (int i = 0; i < size; i++) {
      String sa = keyListOfThis.get(i);
      String sb = keyListOfObject.get(i);
      c = sa.compareTo(sb);
      if (c != 0) {
        return c;
      }
    }

    for (int i = 0; i < size; i++) {
      JSONValue<JSONObject> va;
      JSONValue<JSONObject> vb;
      String key = keyListOfThis.get(i);
      try {
        va = this.getValue(key).toJSONObject();
        vb = jsonObject.getValue(key).toJSONObject();
      } catch (PropertyNotExistException e) {
        throw new RuntimeException(e);
      }
      c = va.compareTo(vb.toJSONObject());
      if (c != 0) {
        return c;
      }
    }
    return 0;
  }

  /**
   * Remove a property from {@code this} {@link net.cabezudo.json.values.JSONObject} object using the property name.
   *
   * @param propertyName the name of the property to remove.
   * @return the {@link net.cabezudo.json.JSONPair} object removed from {@code this} {@link net.cabezudo.json.values.JSONObject} object.
   */
  public JSONPair remove(String propertyName) {
    JSONPair element = map.get(propertyName);
    list.remove(element);
    keys.remove(propertyName);
    return map.remove(propertyName);
  }

  /**
   * Remove a property from {@code this} {@code net.cabezudo.json.values.JSONObject} object using the position of the property.
   *
   * @param index a{@code int} with the position of the property in {@code this} {@code net.cabezudo.json.values.JSONObject} object.
   * @return the {@code net.cabezudo.json.JSONPair} object removed from {@code this} {@code net.cabezudo.json.values.JSONObject} object.
   */
  public JSONPair remove(int index) {
    JSONPair element = list.get(index);
    list.remove(element);
    String propertyName = element.getKey();
    keys.remove(propertyName);
    return map.remove(propertyName);
  }

  /**
   * Dig into {@code this} {@code net.cabezudo.json.values.JSONObject} object to find a property to convert to {@code Boolean}. The properties are separated by dots and the
   * position of elements in an array are specified using the index in brackets. Example: person.childs.[3].name
   *
   * @param fullPropertyName The path of the property to search.
   * @return a {@code Boolean} with the property value.
   * @throws PropertyNotExistException if the property doesn't exist.
   */
  public Boolean digBoolean(String fullPropertyName) throws PropertyNotExistException {
    JSONValue jsonValue = digValue(fullPropertyName);
    return jsonValue.toBoolean();
  }

  /**
   * Dig into {@code this} {@code net.cabezudo.json.values.JSONObject} object to find a property to convert to {@code Byte}. The properties are separated by dots and the position
   * of elements in an array are specified using the index in brackets. Example: person.childs.[3].name
   *
   * @param fullPropertyName The path of the property to search.
   * @return a {@code Byte} with the property value.
   * @throws PropertyNotExistException if the property doesn't exist.
   */
  public Byte digByte(String fullPropertyName) throws PropertyNotExistException {
    JSONValue jsonValue = digValue(fullPropertyName);
    return jsonValue.toByte();
  }

  /**
   * Dig into {@code this} {@code net.cabezudo.json.values.JSONObject} object to find a property to convert to {@code Character}. The properties are separated by dots and the
   * position of elements in an array are specified using the index in brackets. Example: person.childs.[3].name
   *
   * @param fullPropertyName The path of the property to search.
   * @return a {@code Character} with the property value.
   * @throws PropertyNotExistException if the property doesn't exist.
   */
  public Character digCharacter(String fullPropertyName) throws PropertyNotExistException {
    JSONValue jsonValue = digValue(fullPropertyName);
    return jsonValue.toCharacter();
  }

  /**
   * Dig into {@code this} {@code net.cabezudo.json.values.JSONObject} object to find a property to convert to {@code Double}. The properties are separated by dots and the position
   * of elements in an array are specified using the index in brackets. Example: person.childs.[3].name
   *
   * @param fullPropertyName The path of the property to search.
   * @return a {@code Double} with the property value.
   * @throws PropertyNotExistException if the property doesn't exist.
   */
  public Double digDouble(String fullPropertyName) throws PropertyNotExistException {
    JSONValue jsonValue = digValue(fullPropertyName);
    return jsonValue.toDouble();
  }

  /**
   * Dig into {@code this} {@link net.cabezudo.json.values.JSONObject} object to find a property to convert to {@code Float}. The properties are separated by dots and the position
   * of elements in an array are specified using the index in brackets. Example: person.childs.[3].name
   *
   * @param fullPropertyName The path of the property to search.
   * @return a {@code Float} with the property value.
   * @throws PropertyNotExistException if the property doesn't exist.
   */
  public Float digFloat(String fullPropertyName) throws PropertyNotExistException {
    JSONValue jsonValue = digValue(fullPropertyName);
    return jsonValue.toFloat();
  }

  /**
   * Dig into {@code this} {@link net.cabezudo.json.values.JSONObject} object to find a property to convert to {@code Integer}. The properties are separated by dots and the
   * position of elements in an array are specified using the index in brackets. Example: person.childs.[3].name
   *
   * @param fullPropertyName The path of the property to search.
   * @return a {@code Integer} with the property value.
   * @throws PropertyNotExistException if the property doesn't exist.
   */
  public Integer digInteger(String fullPropertyName) throws PropertyNotExistException {
    JSONValue jsonValue = digValue(fullPropertyName);
    return jsonValue.toInteger();
  }

  /**
   * Dig into {@code this} {@link net.cabezudo.json.values.JSONObject} object to find a property to convert to {@code Long}. The properties are separated by dots and the position
   * of elements in an array are specified using the index in brackets. Example: person.childs.[3].name
   *
   * @param fullPropertyName The path of the property to search.
   * @return a {@code Long} with the property value.
   * @throws PropertyNotExistException if the property doesn't exist.
   */
  public Long digLong(String fullPropertyName) throws PropertyNotExistException {
    JSONValue jsonValue = digValue(fullPropertyName);
    return jsonValue.toLong();
  }

  /**
   * Dig into {@code this} {@link net.cabezudo.json.values.JSONObject} object to find a property to convert to {@code Boolean}. The properties are separated by dots and the
   * position of elements in an array are specified using the index in brackets. Example: person.childs.[3].name
   *
   * @param fullPropertyName The path of the property to search.
   * @return a {@code Boolean} with the property value or {@code null} if the property doesn't exist.
   */
  public Boolean digNullBoolean(String fullPropertyName) {
    JSONValue jsonValue = digNullValue(fullPropertyName);
    if (jsonValue == null) {
      return null;
    }
    return jsonValue.toBoolean();
  }

  /**
   * Dig into {@code this} {@link net.cabezudo.json.values.JSONObject} object to find a property to convert to {@code Byte}. The properties are separated by dots and the position
   * of elements in an array are specified using the index in brackets. Example: person.childs.[3].name
   *
   * @param fullPropertyName The path of the property to search.
   * @return a {@code Byte} with the property value or {@code null} if the property doesn't exist.
   */
  public Byte digNullByte(String fullPropertyName) {
    JSONValue jsonValue = digNullValue(fullPropertyName);
    if (jsonValue == null) {
      return null;
    }
    return jsonValue.toByte();
  }

  /**
   * Dig into {@code this} {@link net.cabezudo.json.values.JSONObject} object to find a property to convert to {@code Character}. The properties are separated by dots and the
   * position of elements in an array are specified using the index in brackets. Example: person.childs.[3].name
   *
   * @param fullPropertyName The path of the property to search.
   * @return a {@code Character} with the property value or {@code null} if the property doesn't exist.
   */
  public Character digNullCharacter(String fullPropertyName) {
    JSONValue jsonValue = digNullValue(fullPropertyName);
    if (jsonValue == null) {
      return null;
    }
    return jsonValue.toCharacter();
  }

  /**
   * Dig into {@code this} {@link net.cabezudo.json.values.JSONObject} object to find a property to convert to {@code Double}. The properties are separated by dots and the position
   * of elements in an array are specified using the index in brackets. Example: person.childs.[3].name
   *
   * @param fullPropertyName The path of the property to search.
   * @return a {@code Double} with the property value or {@code null} if the property doesn't exist.
   */
  public Double digNullDouble(String fullPropertyName) {
    JSONValue jsonValue = digNullValue(fullPropertyName);
    if (jsonValue == null) {
      return null;
    }
    return jsonValue.toDouble();
  }

  /**
   * Dig into {@code this} {@link net.cabezudo.json.values.JSONObject} object to find a property to convert to {@code Float}. The properties are separated by dots and the position
   * of elements in an array are specified using the index in brackets. Example: person.childs.[3].name
   *
   * @param fullPropertyName The path of the property to search.
   * @return a {@code Float} with the property value or {@code null} if the property doesn't exist.
   */
  public Float digNullFloat(String fullPropertyName) {
    JSONValue jsonValue = digNullValue(fullPropertyName);
    if (jsonValue == null) {
      return null;
    }
    return jsonValue.toFloat();
  }

  /**
   * Dig into {@code this} {@link net.cabezudo.json.values.JSONObject} object to find a property to convert to {@code Integer}. The properties are separated by dots and the
   * position of elements in an array are specified using the index in brackets. Example: person.childs.[3].name
   *
   * @param fullPropertyName The path of the property to search.
   * @return a {@code Integer} with the property value or {@code null} if the property doesn't exist.
   */
  public Integer digNullInteger(String fullPropertyName) {
    JSONValue jsonValue = digNullValue(fullPropertyName);
    if (jsonValue == null) {
      return null;
    }
    return jsonValue.toInteger();
  }

  /**
   * Dig into {@code this} {@link net.cabezudo.json.values.JSONObject} object to find a property to convert to {@code Long}. The properties are separated by dots and the position
   * of elements in an array are specified using the index in brackets. Example: person.childs.[3].name
   *
   * @param fullPropertyName The path of the property to search.
   * @return a {@code Long} with the property value or {@code null} if the property doesn't exist.
   */
  public Long digNullLong(String fullPropertyName) {
    JSONValue jsonValue = digNullValue(fullPropertyName);
    if (jsonValue == null) {
      return null;
    }
    return jsonValue.toLong();
  }

  /**
   * Dig into {@code this} {@link net.cabezudo.json.values.JSONObject} object to find a property to convert to {@link net.cabezudo.json.values.JSONObject}. The properties are
   * separated by dots and the position of elements in an array are specified using the index in brackets. Example: person.childs.[3].name
   *
   * @param fullPropertyName The path of the property to search.
   * @return a {@link net.cabezudo.json.values.JSONObject} with the property value or {@code null} if the property doesn't exist.
   */
  public JSONObject digNullObject(String fullPropertyName) {
    JSONValue jsonValue = digNullValue(fullPropertyName);
    if (jsonValue == null) {
      return null;
    }
    return jsonValue.toJSONObject();
  }

  /**
   * Dig into {@code this} {@link net.cabezudo.json.values.JSONObject} object to find a property to convert to {@code Short}. The properties are separated by dots and the position
   * of elements in an array are specified using the index in brackets. Example: person.childs.[3].name
   *
   * @param fullPropertyName The path of the property to search.
   * @return a {@code Short} with the property value or {@code null} if the property doesn't exist.
   */
  public Short digNullShort(String fullPropertyName) {
    JSONValue jsonValue = digNullValue(fullPropertyName);
    if (jsonValue == null) {
      return null;
    }
    return jsonValue.toShort();
  }

  /**
   * Dig into {@code this} {@link net.cabezudo.json.values.JSONObject} object to find a property to convert to {@code String}. The properties are separated by dots and the position
   * of elements in an array are specified using the index in brackets. Example: person.childs.[3].name
   *
   * @param fullPropertyName The path of the property to search.
   * @return a {@code String} with the property value or {@code null} if the property doesn't exist.
   */
  public String digNullString(String fullPropertyName) {
    JSONValue jsonValue = digNullValue(fullPropertyName);
    if (jsonValue == null) {
      return null;
    }
    return jsonValue.toString();
  }

  /**
   * Dig into {@code this} {@link net.cabezudo.json.values.JSONObject} object to find a property to convert to {@link net.cabezudo.json.values.JSONValue}. The properties are
   * separated by dots and the position of elements in an array are specified using the index in brackets. Example: person.childs.[3].name
   *
   * @param fullPropertyName The path of the property to search.
   * @return a {@link net.cabezudo.json.values.JSONValue} with the property value or {@code null} if the property doesn't exist.
   */
  public JSONValue digNullValue(String fullPropertyName) {
    return digNullValue(fullPropertyName, 1);
  }

  JSONValue digNullValue(String fullPropertyName, int oldPosition) {
    if (fullPropertyName == null || fullPropertyName.isEmpty()) {
      throw new IllegalArgumentException("Invalid parameter '" + fullPropertyName + "'.");
    }

    int point = fullPropertyName.indexOf('.');
    if (point == -1) {
      JSONValue jsonValue = getNullValue(fullPropertyName);
      return jsonValue;
    } else {
      String propertyName = fullPropertyName.substring(0, point);
      int newStartPosition = point + 1;
      if (newStartPosition >= fullPropertyName.length()) {
        throw new IllegalArgumentException("Invalid parameter '" + fullPropertyName + "'.");
      }
      JSONValue nextLevelValue = getNullValue(propertyName);
      if (nextLevelValue == null || !(nextLevelValue.isObject() || nextLevelValue.isArray())) {
        return null;
      }
      String nextPropertyName = fullPropertyName.substring(newStartPosition);

      if (nextLevelValue.isObject()) {
        JSONObject nextLevelObject = nextLevelValue.toJSONObject();
        return nextLevelObject.digNullValue(nextPropertyName, newStartPosition + oldPosition);
      }
      if (nextLevelValue.isArray()) {
        JSONArray nextLevelArray = nextLevelValue.toJSONArray();
        return nextLevelArray.digNullValue(nextPropertyName, newStartPosition + oldPosition);
      }
      throw new RuntimeException("The next level value is not an object nor an array.");
    }
  }

  /**
   * Dig into {@code this} {@link net.cabezudo.json.values.JSONObject} object to find a property to convert to {@link net.cabezudo.json.values.JSONObject}. The properties are
   * separated by dots and the position of elements in an array are specified using the index in brackets. Example: person.childs.[3].name
   *
   * @param fullPropertyName The path of the property to search.
   * @return a {@link net.cabezudo.json.values.JSONObject} with the property value.
   * @throws PropertyNotExistException if the property doesn't exist.
   */
  public JSONObject digObject(String fullPropertyName) throws PropertyNotExistException {
    JSONValue jsonValue = digValue(fullPropertyName);
    return jsonValue.toJSONObject();
  }

  /**
   * Dig into {@code this} {@link net.cabezudo.json.values.JSONObject} object to find a property to convert to {@code Short}. The properties are separated by dots and the position
   * of elements in an array are specified using the index in brackets. Example: person.childs.[3].name
   *
   * @param fullPropertyName The path of the property to search.
   * @return a {@code Short} with the property value.
   * @throws PropertyNotExistException if the property doesn't exist.
   */
  public Short digShort(String fullPropertyName) throws PropertyNotExistException {
    JSONValue jsonValue = digValue(fullPropertyName);
    return jsonValue.toShort();
  }

  /**
   * Dig into {@code this} {@link net.cabezudo.json.values.JSONObject} object to find a property to convert to {@code String}. The properties are separated by dots and the position
   * of elements in an array are specified using the index in brackets. Example: person.childs.[3].name
   *
   * @param fullPropertyName The path of the property to search.
   * @return a {@code String} with the property value.
   * @throws PropertyNotExistException if the property doesn't exist.
   */
  public String digString(String fullPropertyName) throws PropertyNotExistException {
    JSONValue jsonValue = digValue(fullPropertyName);
    return jsonValue.toString();
  }

  /**
   * Dig into {@code this} {@link net.cabezudo.json.values.JSONObject} object to find a property and return a {@link net.cabezudo.json.values.JSONValue} object. The properties are
   * separated by dots and the position of elements in an array are specified using the index in brackets. Example: person.childs.[3].name
   *
   * @param fullPropertyName The path of the property to search.
   * @return a {@link net.cabezudo.json.values.JSONValue} with the property value.
   * @throws PropertyNotExistException if the property doesn't exist.
   */
  public JSONValue digValue(String fullPropertyName) throws PropertyNotExistException {
    JSONValue value = digNullValue(fullPropertyName);
    if (value == null) {
      throw new PropertyNotExistException(fullPropertyName, "The property " + fullPropertyName + " doesn't exist.", getPosition());
    }
    return value;
  }

  /**
   * Return the value of the property with the name passed converted to {@link net.cabezudo.json.values.JSONValue[]}.
   *
   * @param propertyName the name of the property to return.
   * @return a {@link net.cabezudo.json.values.JSONValue[]}.
   * @throws PropertyNotExistException if the property doesn't exist.
   */
  public JSONValue[] getArray(String propertyName) throws PropertyNotExistException {
    JSONValue jsonValue = getValue(propertyName);

    return jsonValue.toArray();
  }

  /**
   * Return the value of the property with the name passed converted to {@code BigDecimal}.
   *
   * @param propertyName the name of the property to return.
   * @return a {@code BigDecimal}.
   * @throws PropertyNotExistException if the property doesn't exist.
   */
  public BigDecimal getBigDecimal(String propertyName) throws PropertyNotExistException {
    JSONValue jsonValue = getValue(propertyName);
    return jsonValue.toBigDecimal();
  }

  /**
   * Return the value of the property with the index passed converted to {@code BigDecimal}. If the property doesn't exist throw a
   * {@link net.cabezudo.json.exceptions.PropertyNotExistException}.
   *
   * @param index the index of the property to return.
   * @return a {@code BigDecimal}.
   * @throws PropertyIndexNotExistException if the index is out of range.
   */
  public BigDecimal getBigDecimal(int index) throws PropertyIndexNotExistException {
    JSONValue jsonValue = getValue(index);
    return jsonValue.toBigDecimal();
  }

  /**
   * Return the value of the property with the name passed converted to {@code BigInteger}.
   *
   * @param propertyName the name of the property to return.
   * @return a {@code BigInteger}.
   * @throws PropertyNotExistException if the property doesn't exist.
   */
  public BigInteger getBigInteger(String propertyName) throws PropertyNotExistException {
    JSONValue jsonValue = getValue(propertyName);
    return jsonValue.toBigInteger();
  }

  /**
   * Return the value of the property with the index passed converted to {@code BigInteger}. If the property doesn't exist throw a
   * {@link net.cabezudo.json.exceptions.PropertyNotExistException}.
   *
   * @param index the index of the property to return.
   * @return a {@code BigInteger}.
   * @throws PropertyIndexNotExistException if the index is out of range.
   */
  public BigInteger getBigInteger(int index) throws PropertyIndexNotExistException {
    JSONValue jsonValue = getValue(index);
    return jsonValue.toBigInteger();
  }

  /**
   * Return the value of the property with the name passed converted to {@code Boolean}.
   *
   * @param propertyName the name of the property to return.
   * @return a {@code Boolean}.
   * @throws PropertyNotExistException if the property doesn't exist.
   */
  public Boolean getBoolean(String propertyName) throws PropertyNotExistException {
    JSONValue jsonValue = getValue(propertyName);
    return jsonValue.toBoolean();
  }

  /**
   * Return the value of the property with the index passed converted to {@code Boolean}. If the property doesn't exist throw a
   * {@link net.cabezudo.json.exceptions.PropertyNotExistException}.
   *
   * @param index the index of the property to return.
   * @return a {@code Boolean}.
   * @throws PropertyIndexNotExistException if the index is out of range.
   */
  public Boolean getBoolean(int index) throws PropertyIndexNotExistException {
    JSONValue jsonValue = getValue(index);
    return jsonValue.toBoolean();
  }

  /**
   * Return the value of the property with the name passed converted to {@code Byte}.
   *
   * @param propertyName the name of the property to return.
   * @return a {@code Byte}.
   * @throws PropertyNotExistException if the property doesn't exist.
   */
  public Byte getByte(String propertyName) throws PropertyNotExistException {
    JSONValue jsonValue = getValue(propertyName);
    return jsonValue.toByte();
  }

  /**
   * Return the value of the property with the index passed converted to {@code Byte}. If the property doesn't exist throw a
   * {@link net.cabezudo.json.exceptions.PropertyNotExistException}.
   *
   * @param index the index of the property to return.
   * @return a {@code Byte}.
   * @throws PropertyIndexNotExistException if the index is out of range.
   */
  public Byte getByte(int index) throws PropertyIndexNotExistException {
    JSONValue jsonValue = getValue(index);
    return jsonValue.toByte();
  }

  /**
   * Return the value of the property with the name passed converted to {@code byte[]}.
   *
   * @param propertyName the name of the property to return.
   * @return a {@code byte[]}.
   * @throws PropertyNotExistException if the property doesn't exist.
   */
  public byte[] getByteArray(String propertyName) throws PropertyNotExistException {
    JSONValue jsonValue = getValue(propertyName);
    return jsonValue.toByteArray();
  }

  /**
   * Return the value of the property with the name passed converted to {@code int[]}.
   *
   * @param propertyName the name of the property to return.
   * @return a {@code int[]}.
   * @throws PropertyNotExistException if the property doesn't exist.
   */
  public int[] getIntArray(String propertyName) throws PropertyNotExistException {
    JSONValue jsonValue = getValue(propertyName);
    return jsonValue.toIntArray();
  }

  /**
   * Return the value of the property with the index passed converted to {@code int[]}. If the property doesn't exist throw a
   * {@link net.cabezudo.json.exceptions.PropertyNotExistException}.
   *
   * @param index the index of the property to return.
   * @return a {@code int[]}.
   * @throws PropertyIndexNotExistException if the index is out of range.
   */
  public int[] getIntArray(int index) throws PropertyIndexNotExistException {
    JSONValue jsonValue = getValue(index);
    return jsonValue.toIntArray();
  }

  /**
   * Return the value of the property with the index passed converted to {@code byte[]}. If the property doesn't exist throw a
   * {@link net.cabezudo.json.exceptions.PropertyNotExistException}.
   *
   * @param index the index of the property to return.
   * @return a {@code byte[]}.
   * @throws PropertyIndexNotExistException if the index is out of range.
   */
  public byte[] getByteArray(int index) throws PropertyIndexNotExistException {
    JSONValue jsonValue = getValue(index);
    return jsonValue.toByteArray();
  }

  /**
   * Return the value of the property with the name passed converted to {@code Calendar}.
   *
   * @param propertyName the name of the property to return.
   * @return a {@code Calendar}.
   * @throws PropertyNotExistException if the property doesn't exist.
   */
  public Calendar getCalendar(String propertyName) throws PropertyNotExistException {
    JSONValue jsonValue = getValue(propertyName);
    return jsonValue.toCalendar();
  }

  /**
   * Return the value of the property with the name passed converted to {@code Calendar} using the pattern parameter.
   *
   * @param propertyName the name of the property to return.
   * @param pattern      the pattern to use for the conversion.
   * @return a {@code Calendar}.
   * @throws PropertyNotExistException if the property doesn't exist.
   */
  public Calendar getCalendar(String propertyName, String pattern) throws PropertyNotExistException {
    JSONValue jsonValue = getValue(propertyName);
    return jsonValue.toCalendar(pattern);
  }

  /**
   * Return the value of the property with the index passed converted to {@code Calendar}. If the property doesn't exist throw a
   * {@link net.cabezudo.json.exceptions.PropertyNotExistException}.
   *
   * @param index the index of the property to return.
   * @return a {@code Calendar}.
   * @throws PropertyIndexNotExistException if the index is out of range.
   */
  public Calendar getCalendar(int index) throws PropertyIndexNotExistException {
    JSONValue jsonValue = getValue(index);
    return jsonValue.toCalendar();
  }

  /**
   * Return the value of the property with the index passed converted to {@code Calendar} using the pattern parameter. If the property doesn't exist throw a
   * {@link net.cabezudo.json.exceptions.PropertyNotExistException}.
   *
   * @param index   the index of the property to return.
   * @param pattern the pattern to use for the conversion.
   * @return a {@code Calendar}.
   * @throws PropertyIndexNotExistException if the index is out of range.
   */
  public Calendar getCalendar(int index, String pattern) throws PropertyIndexNotExistException {
    JSONValue jsonValue = getValue(index);
    return jsonValue.toCalendar(pattern);
  }

  /**
   * Return the value of the property with the name passed converted to {@code Character}.
   *
   * @param propertyName the name of the property to return.
   * @return a {@code Character}.
   * @throws PropertyNotExistException if the property doesn't exist.
   */
  public Character getCharacter(String propertyName) throws PropertyNotExistException {
    JSONValue jsonValue = getValue(propertyName);
    return jsonValue.toCharacter();
  }

  /**
   * Return the value of the property with the index passed converted to {@code Character}. If the property doesn't exist throw a
   * {@link net.cabezudo.json.exceptions.PropertyNotExistException}.
   *
   * @param index the index of the property to return.
   * @return a {@code Character}.
   * @throws PropertyIndexNotExistException if the index is out of range.
   */
  public Character getCharacter(int index) throws PropertyIndexNotExistException {
    JSONValue jsonValue = getValue(index);
    return jsonValue.toCharacter();
  }

  /**
   * Return a unmodifiable list of all childs of type {@link net.cabezudo.json.JSONPair} of the object in the natural order.
   *
   * @return a {@code List<JSONPair>} object with the child's.
   */
  public List<JSONPair> getChilds() {
    return Collections.unmodifiableList(list);
  }

  /**
   * Return the value of the property with the name passed converted to {@code Double}.
   *
   * @param propertyName the name of the property to return.
   * @return a {@code Double}.
   * @throws PropertyNotExistException if the property doesn't exist.
   */
  public Double getDouble(String propertyName) throws PropertyNotExistException {
    JSONValue jsonValue = getValue(propertyName);
    return jsonValue.toDouble();
  }

  /**
   * Return the value of the property with the index passed converted to {@code Double}. If the property doesn't exist throw a
   * {@link net.cabezudo.json.exceptions.PropertyNotExistException}.
   *
   * @param index the index of the property to return.
   * @return a {@code Double}.
   * @throws PropertyIndexNotExistException if the index is out of range.
   */
  public Double getDouble(int index) throws PropertyIndexNotExistException {
    JSONValue jsonValue = getValue(index);
    return jsonValue.toDouble();
  }

  /**
   * Return the element associated with the property name. For an object the element is a {@link net.cabezudo.json.JSONPair} formed by the property name and the value.
   *
   * @param propertyName the name of the property to get.
   * @return an object {@link net.cabezudo.json.JSONPair JSONArray} with the pair data/value in the object with the property name specified.
   * @throws PropertyNotExistException if the property specified don't exist in the object.
   */
  public JSONPair getElement(String propertyName) throws PropertyNotExistException {
    JSONPair jsonPair = getNullElement(propertyName);
    if (jsonPair == null) {
      throw new PropertyNotExistException(propertyName, "The property " + propertyName + " doesn't exist.", getPosition());
    }
    return jsonPair;
  }

  /**
   * Return the element in the position specified. For an object the element is a {@link net.cabezudo.json.JSONPair} formed by the property name and the value.
   *
   * @param index the index of the property.
   * @return an object {@link net.cabezudo.json.JSONPair} with the pair data/value in the object with the index specified.
   * @throws PropertyIndexNotExistException if the index is out of range.
   */
  public JSONPair getElement(int index) throws PropertyIndexNotExistException {
    JSONPair jsonPair = getNullElement(index);
    if (jsonPair == null) {
      throw new PropertyIndexNotExistException(index, "The property " + index + " doesn't exist.", getPosition());
    }
    return jsonPair;
  }

  /**
   * Return the value of the property with the name passed converted to {@code Float}.
   *
   * @param propertyName the name of the property to return.
   * @return a {@code Float}.
   * @throws PropertyNotExistException if the property doesn't exist.
   */
  public Float getFloat(String propertyName) throws PropertyNotExistException {
    JSONValue jsonValue = getValue(propertyName);
    return jsonValue.toFloat();
  }

  /**
   * Return the value of the property with the index passed converted to {@code Float}. If the property doesn't exist throw a
   * {@link net.cabezudo.json.exceptions.PropertyNotExistException}.
   *
   * @param index the index of the property to return.
   * @return a {@code Float}.
   * @throws PropertyIndexNotExistException if the index is out of range.
   */
  public Float getFloat(int index) throws PropertyIndexNotExistException {
    JSONValue jsonValue = getValue(index);
    return jsonValue.toFloat();
  }

  /**
   * Return the value of the property with the name passed converted to {@code Integer}.
   *
   * @param propertyName the name of the property to return.
   * @return a {@code Integer}.
   * @throws PropertyNotExistException if the property doesn't exist.
   */
  public Integer getInteger(String propertyName) throws PropertyNotExistException {
    JSONValue jsonValue = getValue(propertyName);
    return jsonValue.toInteger();
  }

  /**
   * Return the value of the property with the index passed converted to {@code Integer}. If the property doesn't exist throw a
   * {@link net.cabezudo.json.exceptions.PropertyNotExistException}.
   *
   * @param index the index of the property to return.
   * @return a {@code Integer}.
   * @throws PropertyIndexNotExistException if the index is out of range.
   */
  public Integer getInteger(int index) throws PropertyIndexNotExistException {
    JSONValue jsonValue = getValue(index);
    return jsonValue.toInteger();
  }

  /**
   * Return the value of the property with the name passed converted to {@link net.cabezudo.json.values.JSONArray}.
   *
   * @param propertyName the name of the property to return.
   * @return a {@link net.cabezudo.json.values.JSONArray}.
   * @throws PropertyNotExistException if the property doesn't exist.
   */
  public JSONArray getJSONArray(String propertyName) throws PropertyNotExistException {
    JSONValue jsonValue = getValue(propertyName);
    if (jsonValue == null) {
      return null;
    }
    return jsonValue.toJSONArray();
  }

  /**
   * Return the value of the property with the index passed converted to {@link net.cabezudo.json.values.JSONArray}. If the property doesn't exist throw a
   * {@link net.cabezudo.json.exceptions.PropertyNotExistException}.
   *
   * @param index the index of the property to return.
   * @return a {@link net.cabezudo.json.values.JSONArray}.
   * @throws PropertyIndexNotExistException if the index is out of range.
   */
  public JSONArray getJSONArray(int index) throws PropertyIndexNotExistException {
    JSONValue jsonValue = getValue(index);
    if (jsonValue == null) {
      return null;
    }
    return jsonValue.toJSONArray();
  }

  /**
   * Return the value of the property with the name passed converted to {@code Long}.
   *
   * @param propertyName the name of the property to return.
   * @return a {@code Long}.
   * @throws PropertyNotExistException if the property doesn't exist.
   */
  public Long getLong(String propertyName) throws PropertyNotExistException {
    JSONValue jsonValue = getValue(propertyName);
    return jsonValue.toLong();
  }

  /**
   * Return the value of the property with the index passed converted to {@code Long}. If the property doesn't exist throw a
   * {@link net.cabezudo.json.exceptions.PropertyNotExistException}.
   *
   * @param index the index of the property to return.
   * @return a {@code Long}.
   * @throws PropertyIndexNotExistException if the index is out of range.
   */
  public Long getLong(int index) throws PropertyIndexNotExistException {
    JSONValue jsonValue = getValue(index);
    return jsonValue.toLong();
  }

  /**
   * Return the value of the property with the name passed converted to {@code BigDecimal}. Return {@code null} if the property doesn't exist.
   *
   * @param propertyName the name of the property to return.
   * @return a {@code BigDecimal}.
   */
  public BigDecimal getNullBigDecimal(String propertyName) {
    JSONValue jsonValue = getNullValue(propertyName);
    if (jsonValue == null) {
      return null;
    }
    return jsonValue.toBigDecimal();
  }

  /**
   * Return the value of the property with the index passed converted to {@code BigDecimal}. If the property doesn't exist return {@code null}.
   *
   * @param index the index of the property to return.
   * @return a {@code BigDecimal}.
   */
  public BigDecimal getNullBigDecimal(int index) {
    JSONValue jsonValue = getNullValue(index);
    if (jsonValue == null) {
      return null;
    }
    return jsonValue.toBigDecimal();
  }

  /**
   * Return the value of the property with the name passed converted to {@code BigInteger}. If the property doesn't exist return {@code null}.
   *
   * @param propertyName the name of the property to return.
   * @return a {@code BigInteger}.
   */
  public BigInteger getNullBigInteger(String propertyName) {
    JSONValue jsonValue = getNullValue(propertyName);
    if (jsonValue == null) {
      return null;
    }
    return jsonValue.toBigInteger();
  }

  /**
   * Return the value of the property with the index passed converted to {@code BigInteger}. If the property doesn't exist return {@code null}.
   *
   * @param index the index of the property to return.
   * @return a {@code BigInteger}.
   */
  public BigInteger getNullBigInteger(int index) {
    JSONValue jsonValue = getNullValue(index);
    if (jsonValue == null) {
      return null;
    }
    return jsonValue.toBigInteger();
  }

  /**
   * Return the value of the property with the name passed converted to {@code Boolean}. If the property doesn't exist return {@code null}.
   *
   * @param propertyName the name of the property to return.
   * @return a {@code Boolean}.
   */
  public Boolean getNullBoolean(String propertyName) {
    JSONValue jsonValue = getNullValue(propertyName);
    if (jsonValue == null) {
      return null;
    }
    return jsonValue.toBoolean();
  }

  /**
   * Return the value of the property with the index passed converted to {@code Boolean}. If the property doesn't exist return {@code null}.
   *
   * @param index the index of the property to return.
   * @return a {@code Boolean}.
   */
  public Boolean getNullBoolean(int index) {
    JSONValue jsonValue = getNullValue(index);
    if (jsonValue == null) {
      return null;
    }
    return jsonValue.toBoolean();
  }

  /**
   * Return the value of the property with the name passed converted to {@code Byte}. If the property doesn't exist return {@code null}.
   *
   * @param propertyName the name of the property to return.
   * @return a {@code Byte}.
   */
  public Byte getNullByte(String propertyName) {
    JSONValue jsonValue = getNullValue(propertyName);
    if (jsonValue == null) {
      return null;
    }
    return jsonValue.toByte();
  }

  /**
   * Return the value of the property with the index passed converted to {@code Byte}. If the property doesn't exist return {@code null}.
   *
   * @param index the index of the property to return.
   * @return a {@code Byte}.
   */
  public Byte getNullByte(int index) {
    JSONValue jsonValue = getNullValue(index);
    if (jsonValue == null) {
      return null;
    }
    return jsonValue.toByte();
  }

  /**
   * Return the value of the property with the name passed converted to {@code byte[]}. If the property doesn't exist return {@code null}.
   *
   * @param propertyName the name of the property to return.
   * @return a {@code byte[]}.
   */
  public byte[] getNullByteArray(String propertyName) {
    JSONValue jsonValue = getNullValue(propertyName);
    if (jsonValue == null) {
      return null;
    }
    return jsonValue.toByteArray();
  }

  /**
   * Return the value of the property with the name passed converted to {@code Calendar}. If the property doesn't exist return {@code null}.
   *
   * @param propertyName the name of the property to return.
   * @return a {@code Calendar}.
   */
  public Calendar getNullCalendar(String propertyName) {
    JSONValue jsonValue = getNullValue(propertyName);
    if (jsonValue == null) {
      return null;
    }
    return jsonValue.toCalendar();
  }

  /**
   * Return the value of the property with the name passed converted to {@code Calendar} using the pattern parameter. If the property doesn't exist return {@code null}.
   *
   * @param propertyName the name of the property to return.
   * @param pattern      the pattern to use for the conversion.
   * @return a {@code Calendar}.
   */
  public Calendar getNullCalendar(String propertyName, String pattern) {
    JSONValue jsonValue = getNullValue(propertyName);
    if (jsonValue == null) {
      return null;
    }
    return jsonValue.toCalendar(pattern);
  }

  /**
   * Return the value of the property with the index passed converted to {@code Calendar}. If the property doesn't exist return {@code null}.
   *
   * @param index the index of the property to return.
   * @return a {@code Calendar}.
   */
  public Calendar getNullCalendar(int index) {
    JSONValue jsonValue = getNullValue(index);
    if (jsonValue == null) {
      return null;
    }
    return jsonValue.toCalendar();
  }

  /**
   * Return the value of the property with the index passed converted to {@code Calendar} using the pattern parameter. If the property doesn't exist return {@code null}.
   *
   * @param index   the index of the property to return.
   * @param pattern the patter to use for the conversion
   * @return a {@code Calendar}.
   */
  public Calendar getNullCalendar(int index, String pattern) {
    JSONValue jsonValue = getNullValue(index);
    if (jsonValue == null) {
      return null;
    }
    return jsonValue.toCalendar(pattern);
  }

  /**
   * Return the value of the property with the name passed converted to {@code Character}. If the property doesn't exist return {@code null}.
   *
   * @param propertyName the name of the property to return.
   * @return a {@code Character}.
   */
  public Character getNullCharacter(String propertyName) {
    JSONValue jsonValue = getNullValue(propertyName);
    if (jsonValue == null) {
      return null;
    }
    return jsonValue.toCharacter();
  }

  /**
   * Return the value of the property with the index passed converted to {@code Character}. If the property doesn't exist return {@code null}.
   *
   * @param index the index of the property to return.
   * @return a {@code Character}.
   */
  public Character getNullCharacter(int index) {
    JSONValue jsonValue = getNullValue(index);
    if (jsonValue == null) {
      return null;
    }
    return jsonValue.toCharacter();
  }

  /**
   * Return the value of the property with the name passed converted to {@code Double}. If the property doesn't exist return {@code null}.
   *
   * @param propertyName the name of the property to return.
   * @return a {@code Double}.
   */
  public Double getNullDouble(String propertyName) {
    JSONValue jsonValue = getNullValue(propertyName);
    if (jsonValue == null) {
      return null;
    }
    return jsonValue.toDouble();
  }

  /**
   * Return the value of the property with the index passed converted to {@code Double}. If the property doesn't exist return {@code null}.
   *
   * @param index the index of the property to return.
   * @return a {@code Double}.
   */
  public Double getNullDouble(int index) {
    JSONValue jsonValue = getNullValue(index);
    if (jsonValue == null) {
      return null;
    }
    return jsonValue.toDouble();
  }

  /**
   * Return the value of the property with the name passed converted to {@link net.cabezudo.json.JSONPair}. If the property doesn't exist return {@code null}.
   *
   * @param propertyName the name of the property to return.
   * @return a {@link net.cabezudo.json.JSONPair}.
   */
  public JSONPair getNullElement(String propertyName) {
    JSONPair jsonPair = map.get(propertyName);
    return jsonPair;
  }

  /**
   * Return the value of the property with the index passed converted to {@link net.cabezudo.json.JSONPair}. If the property doesn't exist return {@code null}.
   *
   * @param index the index of the property to return.
   * @return a {@link net.cabezudo.json.JSONPair}.
   */
  public JSONPair getNullElement(int index) {
    if (index < 0 || index > list.size()) {
      return null;
    }
    return list.get(index);
  }

  /**
   * Return the value of the property with the name passed converted to {@code Float}. If the property doesn't exist return {@code null}.
   *
   * @param propertyName the name of the property to return.
   * @return a {@code Float}.
   */
  public Float getNullFloat(String propertyName) {
    JSONValue jsonValue = getNullValue(propertyName);
    if (jsonValue == null) {
      return null;
    }
    return jsonValue.toFloat();
  }

  /**
   * Return the value of the property with the index passed converted to {@code Float}. If the property doesn't exist return {@code null}.
   *
   * @param index the index of the property to return.
   * @return a {@code Float}.
   */
  public Float getNullFloat(int index) {
    JSONValue jsonValue = getNullValue(index);
    if (jsonValue == null) {
      return null;
    }
    return jsonValue.toFloat();
  }

  /**
   * Return the value of the property with the name passed converted to {@code Integer}. If the property doesn't exist return {@code null}.
   *
   * @param propertyName the name of the property to return.
   * @return a {@code Integer}.
   */
  public Integer getNullInteger(String propertyName) {
    JSONValue jsonValue = getNullValue(propertyName);
    if (jsonValue == null) {
      return null;
    }
    return jsonValue.toInteger();
  }

  /**
   * Return the value of the property with the index passed converted to {@code Integer}. If the property doesn't exist return {@code null}.
   *
   * @param index the index of the property to return.
   * @return a {@code Integer}.
   */
  public Integer getNullInteger(int index) {
    JSONValue jsonValue = getNullValue(index);
    if (jsonValue == null) {
      return null;
    }
    return jsonValue.toInteger();
  }

  /**
   * Return the value of the property with the name passed converted to {@link net.cabezudo.json.values.JSONArray}. If the property doesn't exist return {@code null}.
   *
   * @param propertyName the name of the property to return.
   * @return a {@link net.cabezudo.json.values.JSONArray}.
   */
  public JSONArray getNullJSONArray(String propertyName) {
    JSONValue jsonValue = getNullValue(propertyName);
    if (jsonValue == null) {
      return null;
    }
    return jsonValue.toJSONArray();
  }

  /**
   * Return the value of the property with the index passed converted to {@link net.cabezudo.json.values.JSONArray}. If the property doesn't exist return {@code null}.
   *
   * @param index the index of the property to return.
   * @return a {@link net.cabezudo.json.values.JSONArray}.
   */
  public JSONArray getNullJSONArray(int index) {
    JSONValue jsonValue = getNullValue(index);
    if (jsonValue == null) {
      return null;
    }
    return jsonValue.toJSONArray();
  }

  /**
   * Return the value of the property with the name passed converted to {@code Long}. If the property doesn't exist return {@code null}.
   *
   * @param propertyName the name of the property to return.
   * @return a {@code Long}.
   */
  public Long getNullLong(String propertyName) {
    JSONValue jsonValue = getNullValue(propertyName);
    if (jsonValue == null) {
      return null;
    }
    return jsonValue.toLong();
  }

  /**
   * Return the value of the property with the index passed converted to {@code Long}. If the property doesn't exist return {@code null}.
   *
   * @param index the index of the property to return.
   * @return a {@code Long}.
   */
  public Long getNullLong(int index) {
    JSONValue jsonValue = getNullValue(index);
    if (jsonValue == null) {
      return null;
    }
    return jsonValue.toLong();
  }

  /**
   * Return the value of the property with the name passed converted to {@link net.cabezudo.json.values.JSONObject}. If the property doesn't exist return {@code null}.
   *
   * @param propertyName the name of the property to return.
   * @return a {@link net.cabezudo.json.values.JSONObject}.
   */
  public JSONObject getNullObject(String propertyName) {
    JSONValue jsonValue = getNullValue(propertyName);
    if (jsonValue == null) {
      return null;
    }
    return jsonValue.toJSONObject();
  }

  /**
   * Return the value of the property with the index passed converted to {@link net.cabezudo.json.values.JSONObject}. If the property doesn't exist return {@code null}.
   *
   * @param index the index of the property to return.
   * @return a {@link net.cabezudo.json.values.JSONObject}.
   */
  public JSONObject getNullObject(int index) {
    JSONValue jsonValue = getNullValue(index);
    if (jsonValue == null) {
      return null;
    }
    return jsonValue.toJSONObject();
  }

  /**
   * Return the value of the property with the name passed converted to {@code Short}. If the property doesn't exist return {@code null}.
   *
   * @param propertyName the name of the property to return.
   * @return a {@code Short}.
   */
  public Short getNullShort(String propertyName) {
    JSONValue jsonValue = getNullValue(propertyName);
    if (jsonValue == null) {
      return null;
    }
    return jsonValue.toShort();
  }

  /**
   * Return the value of the property with the index passed converted to {@code Short}. If the property doesn't exist return {@code null}.
   *
   * @param index the index of the property to return.
   * @return a {@code Short}.
   */
  public Short getNullShort(int index) {
    JSONValue jsonValue = getNullValue(index);
    if (jsonValue == null) {
      return null;
    }
    return jsonValue.toShort();
  }

  /**
   * Return the value of the property with the name passed converted to {@code String}. If the property doesn't exist return {@code null}.
   *
   * @param propertyName the name of the property to return.
   * @return a {@code String}.
   */
  public String getNullString(String propertyName) {
    JSONValue jsonValue = getNullValue(propertyName);
    if (jsonValue == null) {
      return null;
    }
    return jsonValue.toString();
  }

  /**
   * Return the value of the property with the index passed converted to {@code String}. If the property doesn't exist return {@code null}.
   *
   * @param index the index of the property to return.
   * @return a {@code String}.
   */
  public String getNullString(int index) {
    JSONValue jsonValue = getNullValue(index);
    if (jsonValue == null) {
      return null;
    }
    return jsonValue.toString();
  }

  /**
   * Return the value of the property with the name passed converted to {@link net.cabezudo.json.values.JSONValue}. If the property doesn't exist return {@code null}.
   *
   * @param propertyName the name of the property to return.
   * @return a {@link net.cabezudo.json.values.JSONValue}.
   */
  public JSONValue getNullValue(String propertyName) {
    if (propertyName == null || propertyName.isEmpty()) {
      throw new IllegalArgumentException("Invalid parameter '" + propertyName + "'.");
    }
    JSONPair jsonPair = map.get(propertyName);
    if (jsonPair == null) {
      return null;
    }
    return jsonPair.getValue();
  }

  /**
   * Return the value of the property with the index passed converted to {@link net.cabezudo.json.values.JSONValue}. If the property doesn't exist return {@code null}.
   *
   * @param index the index of the property to return.
   * @return a {@link net.cabezudo.json.values.JSONValue}.
   */
  public JSONValue getNullValue(int index) {
    if (index < 0 || index > list.size()) {
      return null;
    }
    JSONPair jsonPair = list.get(index);
    return jsonPair.getValue();
  }

  /**
   * Return the value of the property with the name passed converted to {@link net.cabezudo.json.values.JSONObject}.
   *
   * @param propertyName the name of the property to return.
   * @return a {@link net.cabezudo.json.values.JSONObject}.
   * @throws PropertyNotExistException if the property doesn't exist.
   */
  public JSONObject getObject(String propertyName) throws PropertyNotExistException {
    JSONValue jsonValue = getValue(propertyName);
    return jsonValue.toJSONObject();
  }

  /**
   * Return the value of the property with the index passed converted to {@link net.cabezudo.json.values.JSONObject}. If the property doesn't exist throw a
   * {@link net.cabezudo.json.exceptions.PropertyNotExistException}.
   *
   * @param index the index of the property to return.
   * @return a {@link net.cabezudo.json.values.JSONObject}.
   * @throws PropertyIndexNotExistException if the index is out of range.
   */
  public JSONObject getObject(int index) throws PropertyIndexNotExistException {
    JSONValue jsonValue = getValue(index);
    return jsonValue.toJSONObject();
  }

  /**
   * Convert {@code this} object in a referenced {@link net.cabezudo.json.JSONElement}. Because the referenced element is the reference, this method return the value of the
   * reference field.
   *
   * @return a {@link net.cabezudo.json.values.JSONValue} with the referenced element for {@code this} object.
   */
  @Override
  public JSONValue toReferencedElement() {
    String referenceFieldNameToSearch = getReferenceFieldName();
    for (JSONPair jsonPair : list) {
      String keyName = jsonPair.getKey();
      if (keyName.equals(referenceFieldNameToSearch)) {
        JSONValue jsonReferenceValue = jsonPair.getValue();
        if (jsonReferenceValue.isObject() || jsonReferenceValue.isArray() || jsonReferenceValue.isBoolean()) {
          throw new InvalidReferencedValue("The reference property value can't be a " + jsonReferenceValue.getClass().getSimpleName() + ". Property name: " + keyName + ".");
        }
        return jsonReferenceValue;
      }
    }
    JSONObject jsonObject = new JSONObject();
    if (list.size() > 0) {
      for (JSONPair jsonPair : list) {
        try {
          jsonObject.add(jsonPair.toReferencedElement());
        } catch (DuplicateKeyException e) {
          throw new RuntimeException(e);
        }
      }
    }
    return jsonObject;
  }

  /**
   * Create a JSON structure where the the root object don't contain another object structure, instead of it contain the references to the property value objects. The reference is
   * a field value of the object. The value of the property that has the object is replaced with the value of the object property marked like reference field. The reference field
   * must not be an object or array.
   *
   * @return a new {@link net.cabezudo.json.values.JSONObject} structure with all the object referenced.
   */
  public JSONObject toReferencedObject() throws DuplicateKeyException {
    JSONObject jsonReferencedObject = new JSONObject();

    for (JSONPair jsonPair : list) {
      JSONValue jsonValue = jsonPair.getValue();
      JSONElement referencedElement = jsonValue.toReferencedElement();

      JSONPair newJSONPair = new JSONPair(jsonPair.getKey(), referencedElement, getPosition());
      jsonReferencedObject.add(newJSONPair);
    }

    return jsonReferencedObject;
  }

  /**
   * Return the value of the property with the name passed converted to {@code Short}.
   *
   * @param propertyName the name of the property to return.
   * @return a {@code Short}.
   * @throws PropertyNotExistException if the property doesn't exist.
   */
  public Short getShort(String propertyName) throws PropertyNotExistException {
    JSONValue jsonValue = getValue(propertyName);
    return jsonValue.toShort();
  }

  /**
   * Return the value of the property with the index passed converted to {@code Short}. If the property doesn't exist throw a
   * {@link net.cabezudo.json.exceptions.PropertyNotExistException}.
   *
   * @param index the index of the property to return.
   * @return a {@code Short}.
   * @throws PropertyIndexNotExistException if the index is out of range.
   */
  public Short getShort(int index) throws PropertyIndexNotExistException {
    JSONValue jsonValue = getValue(index);
    return jsonValue.toShort();
  }

  /**
   * Return the value of the property with the name passed converted to {@code String}.
   *
   * @param propertyName the name of the property to return.
   * @return a {@code String}.
   * @throws PropertyNotExistException if the property doesn't exist.
   */
  public String getString(String propertyName) throws PropertyNotExistException {
    JSONValue jsonValue = getValue(propertyName);
    return jsonValue.toString();
  }

  /**
   * Return the value of the property with the index passed converted to {@code String}. If the property doesn't exist throw a
   * {@link net.cabezudo.json.exceptions.PropertyNotExistException}.
   *
   * @param index the index of the property to return.
   * @return a {@code String}.
   * @throws PropertyIndexNotExistException if the index is out of range.
   */
  public String getString(int index) throws PropertyIndexNotExistException {
    JSONValue jsonValue = getValue(index);
    return jsonValue.toString();
  }

  /**
   * Return the value of the property with the name passed converted to {@link net.cabezudo.json.values.JSONValue}.
   *
   * @param propertyName the name of the property to return.
   * @return a {@link net.cabezudo.json.values.JSONValue}.
   * @throws PropertyNotExistException if the property doesn't exist.
   */
  public JSONValue getValue(String propertyName) throws PropertyNotExistException {
    JSONValue jsonValue = getNullValue(propertyName);
    if (jsonValue == null) {
      throw new PropertyNotExistException(propertyName, "The property " + propertyName + " doesn't exist.", getPosition());
    }
    return jsonValue;
  }

  /**
   * Return the value of the property with the index passed converted to {@link net.cabezudo.json.values.JSONValue}. If the property doesn't exist throw a
   * {@link net.cabezudo.json.exceptions.PropertyNotExistException}.
   *
   * @param index the index of the property to return.
   * @return a {@link net.cabezudo.json.values.JSONValue}.
   * @throws PropertyIndexNotExistException if the index is out of range.
   */
  public JSONValue getValue(int index) throws PropertyIndexNotExistException {
    JSONValue jsonValue = getNullValue(index);
    if (jsonValue == null) {
      throw new PropertyIndexNotExistException(index, "The position " + index + " don't have a value.", getPosition());
    }
    return jsonValue;
  }

  /**
   * Tells whether or not {@code this} object has childs.
   *
   * @return {@code true} if, and only if, {@code this} object has childs, {@code false} otherwise.
   */
  public boolean hasChilds() {
    return !list.isEmpty();
  }

  /**
   * Returns whether the element is empty or not.
   *
   * @return {@code true} if the object hasn't properties, {@code false} otherwise.
   */
  @Override
  public boolean isEmpty() {
    return list.isEmpty();
  }

  /**
   * Tells whether or not {@code this} object is a {@link net.cabezudo.json.values.JSONObject}.
   *
   * @return {@code true}.
   */
  @Override
  public boolean isObject() {
    return true;
  }

  /**
   * Tells whether or not {@code this} object is referenceable.
   *
   * @return {@code true}.
   */
  @Override
  public boolean isReferenceable() {
    return true;
  }

  /**
   * Returns an iterator over the properties in {@code this} object in proper sequence.
   *
   * @return an iterator over the properties in {@code this} object in proper sequence.
   */
  @Override
  public Iterator<JSONPair> iterator() {
    return list.iterator();
  }

  /**
   * Returns the number of properties in {@code this} {@link net.cabezudo.json.values.JSONObject}. If {@code this} {@link net.cabezudo.json.values.JSONObject} contains more than
   * <tt>Integer.MAX_VALUE</tt> properties, returns
   * <tt>Integer.MAX_VALUE</tt>.
   *
   * @return the number of properties in {@code this} {@link net.cabezudo.json.values.JSONObject}.
   */
  public int size() {
    return list.size();
  }

  /**
   * Convert the properties values of {@code this} object in an array of elements of type {@link net.cabezudo.json.values.JSONValue} using the values of element and leaving out the
   * properties names.
   *
   * @return an array of type {@link net.cabezudo.json.values.JSONValue} with the values of {@code this} object properties.
   */
  @Override
  public JSONValue[] toArray() {
    JSONValue[] array = new JSONValue[list.size()];

    int i = 0;
    for (JSONPair jsonPair : list) {
      JSONValue value = jsonPair.getValue();
      array[i] = value;
      i++;
    }
    return array;
  }

  /**
   * Create a JSON string representation of {@code this} {@link net.cabezudo.json.values.JSONObject} including the JSON string representation of the properties.
   *
   * @return a {@code String} representation of {@code this} {@link net.cabezudo.json.values.JSONObject}.
   */
  @Override
  public String toJSON() {
    StringBuilder sb = new StringBuilder("{ ");
    if (map.size() > 0) {
      for (JSONPair jsonPair : list) {
        sb.append(jsonPair.toJSON());
        sb.append(", ");
      }
      sb = new StringBuilder(sb.substring(0, sb.length() - 2));
    }
    sb.append(" }");
    return sb.toString();
  }

  /**
   * Convert the properties values of {@code this} object in a {@link net.cabezudo.json.values.JSONArray} object using the values and leaving out the properties names.
   *
   * @return an array of type {@link net.cabezudo.json.values.JSONArray} with the values of {@code this} object properties.
   */
  @Override
  public JSONArray toJSONArray() {
    JSONArray jsonArray = new JSONArray();
    for (JSONPair jsonPair : list) {
      jsonArray.add(jsonPair.getValue());
    }
    return jsonArray;
  }

  /**
   * Convert the properties values of {@code this} object in a primitive array of {@link net.cabezudo.json.JSONPair} objects using the values and properties names.
   *
   * @return a primitive array of {@link net.cabezudo.json.JSONPair} with the values of {@code this} object names and properties.
   */
  public JSONPair[] toJSONPairArray() {
    int size = list.size();
    JSONPair[] jsonPairArray = new JSONPair[size];

    for (int i = 0; i < size; i++) {
      JSONPair jsonPair = list.get(i);
      jsonPairArray[i] = jsonPair;
    }
    return jsonPairArray;
  }

  /**
   * Return the JSON structure behind {@code this} object that is the reference to {@code this} object.
   *
   * @return {@code this} object.
   */
  @Override
  public JSONValue toJSONTree() {
    return this;
  }

  /**
   * Return {@code this} object.
   *
   * @return {@code this} object.
   */
  @Override
  public JSONObject toJSONObject() {
    return this;
  }

  /**
   * Return a {@code String} with a representation of {@code this} object.
   *
   * @return a {@code String} with a representation of {@code this} object.
   */
  @Override
  public String toString() {
    return toJSON();
  }

  @Override
  public void toFormattedString(StringBuilder sb, int indent, boolean includeFirst) {
    if (includeFirst) {
      sb.append(JSON.getIndent(indent));
    }
    sb.append("{\n");
    list.forEach(entry -> {
      sb.append(JSON.getIndent(indent + 1));
      entry.toFormattedString(sb, indent + 1, false);
      sb.append(",\n");
    });
    sb.setLength(Math.max(sb.length() - 2, 0));
    sb.append("\n");
    sb.append(JSON.getIndent(indent)).append("}");
  }
}
