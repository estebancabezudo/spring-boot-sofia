package net.cabezudo.json.objects;

import net.cabezudo.json.annotations.JSONProperty;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author <a href="http://cabezudo.net">Esteban Cabezudo</a>
 * @version 0.9, 01/22/2017
 */
public class Person {

  @JSONProperty
  private final String name;

  @JSONProperty
  private final String lastName;

  @JSONProperty
  private final int age;

  @JSONProperty(field = "name")
  private final List<Person> childs = new ArrayList<>();

  public Person(String name, String lastName, int age, Person... persons) {
    this.name = name;
    this.lastName = lastName;
    this.age = age;
    Collections.addAll(childs, persons);
  }

  public String getName() {
    return name;
  }

  public String getLastName() {
    return lastName;
  }

  public List<Person> getChilds() {
    return childs;
  }

  public int getAge() {
    return age;
  }
}
