package net.cabezudo.sofia.places.persistence;

public class AdministrativeDivisionEntity {
  private final int placeId;
  private final int administrativeDivisionTypeEntityId;
  private final String administrativeDivisiónTypeEntityName;
  private final String name;
  private Integer id;

  public AdministrativeDivisionEntity(
      int id,
      PlaceEntity place,
      AdministrativeDivisionTypeEntity type,
      AdministrativeDivisionNameEntity name) {

    this.id = id;
    this.placeId = place.getId();
    this.administrativeDivisionTypeEntityId = type.getId();
    this.administrativeDivisiónTypeEntityName = type.getName();
    this.name = name.getValue();
  }

  public AdministrativeDivisionEntity(Integer id, Integer placeId, int typeId, String typeName, String name) {
    this.id = id;
    this.placeId = placeId;
    this.administrativeDivisionTypeEntityId = typeId;
    this.administrativeDivisiónTypeEntityName = typeName;
    this.name = name;

  }


  public int getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public int getPlaceId() {
    return placeId;
  }

  public int getAdministrativeDivisionTypeEntityId() {
    return administrativeDivisionTypeEntityId;
  }
  
  public String getAdministrativeDivisiónTypeEntityName() {
    return administrativeDivisiónTypeEntityName;
  }

  public String getName() {
    return name;
  }
}
