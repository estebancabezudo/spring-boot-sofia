package net.cabezudo.sofia.places;


import java.util.Objects;

public final class AdministrativeDivision {
  private final Integer id;
  private final AdministrativeDivisionType administrativeDivisionType;
  private final String name;

  public AdministrativeDivision(Integer id, AdministrativeDivisionType administrativeDivisionType, String name) {
    this.id = id;
    this.administrativeDivisionType = administrativeDivisionType;
    this.name = name;
  }

  public Integer getId() {
    return id;
  }

  public AdministrativeDivisionType getType() {
    return administrativeDivisionType;
  }

  public String getName() {
    return name;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) return true;
    if (obj == null || obj.getClass() != this.getClass()) return false;
    var that = (AdministrativeDivision) obj;
    return Objects.equals(this.id, that.id) &&
        Objects.equals(this.administrativeDivisionType, that.administrativeDivisionType) &&
        Objects.equals(this.name, that.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, administrativeDivisionType, name);
  }

  @Override
  public String toString() {
    return "AdministrativeDivision[" +
        "id=" + id + ", " +
        "administrativeDivisionType=" + administrativeDivisionType + ", " +
        "name=" + name + ']';
  }
}
