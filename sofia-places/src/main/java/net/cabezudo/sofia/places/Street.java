package net.cabezudo.sofia.places;

public class Street {
  private final Integer id;
  private final String name;
  private final boolean verified;

  public Street(Integer id, String name, boolean verified) {
    this.id = id;
    this.name = name;
    this.verified = verified;
  }

  public Integer getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public boolean isVerified() {
    return verified;
  }
}
