package net.cabezudo.json.objects;

import net.cabezudo.json.JSON;
import net.cabezudo.json.JSONable;
import net.cabezudo.json.annotations.JSONProperty;
import net.cabezudo.json.values.JSONObject;

import java.util.Objects;

/**
 * @author <a href="http://cabezudo.net">Esteban Cabezudo</a>
 * @version 0.9, 08/08/2016
 */
public class Book implements JSONable {

  @JSONProperty
  private final Integer id;

  @JSONProperty
  private final String name;

  public Book(int id, String name) {
    this.id = id;
    this.name = name;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null) {
      return false;
    }
    if (getClass() != o.getClass()) {
      return false;
    }
    final Book book = (Book) o;
    if (!Objects.equals(this.id, book.id)) {
      return false;
    }
    return Objects.equals(this.name, book.name);
  }

  public int getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 47 * hash + this.id;
    hash = 47 * hash + Objects.hashCode(this.name);
    return hash;
  }

  @Override
  public String toJSON() {
    return toJSONTree().toString();
  }

  @Override
  public JSONObject toJSONTree() {
    return JSON.toJSONObject(this);
  }

  @Override
  public String toFormatedString() {
    StringBuilder sb = new StringBuilder();
    toFormatedString(sb, 0, false);
    return sb.toString();
  }

  @Override
  public void toFormatedString(StringBuilder sb, int indent, boolean includeFirst) {
    sb.append("{\n");
    sb.append("  \"id\": \"").append(id).append("\",\n");
    sb.append("  \"name\": \"").append(name).append("\",\n");
    sb.append("}\n");
  }
}
