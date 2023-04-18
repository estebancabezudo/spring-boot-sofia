package net.cabezudo.sofia.places.persistence;

import javax.persistence.Column;

public class PlaceEntity {

  @Column(name = "account_id")
  private final int accountId;
  private final String name;
  private final String street;
  private final String number;
  @Column(name = "interior_number")
  private final String interiorNumber;
  private final String references;
  @Column(name = "postal_code")
  private final String postalCode;
  @Column(name = "country_id")
  private final int countryId;
  private int id;

  public PlaceEntity(int id, int accountId, String name, String street, String number, String interiorNumber, String references, String postalCode, int countryId) {
    this.id = id;
    this.accountId = accountId;
    this.name = name;
    this.street = street;
    this.number = number;
    this.interiorNumber = interiorNumber;
    this.references = references;
    this.postalCode = postalCode;
    this.countryId = countryId;
  }

  public PlaceEntity(PlaceEntity entity) {
    this.id = entity.getId();
    this.accountId = entity.getAccountId();
    this.name = entity.getName();
    this.street = entity.getStreet();
    this.number = entity.getNumber();
    this.interiorNumber = entity.getInteriorNumber();
    this.references = entity.getReferences();
    this.postalCode = entity.getPostalCode();
    this.countryId = entity.getCountryId();
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public int getAccountId() {
    return accountId;
  }

  public String getName() {
    return name;
  }

  public String getStreet() {
    return street;
  }

  public String getNumber() {
    return number;
  }

  public String getInteriorNumber() {
    return interiorNumber;
  }

  public String getReferences() {
    return references;
  }

  public String getPostalCode() {
    return postalCode;
  }

  public int getCountryId() {
    return countryId;
  }
}
