package net.cabezudo.sofia.emails.persistence;


import java.util.Objects;

public final class EMailEntity {
  private final int id;
  private final String email;

  public EMailEntity(int id, String email) {
    this.id = id;
    this.email = email;
  }

  @Override
  public String toString() {
    return "EMailEntity{" +
        "id=" + id +
        ", email='" + email + '\'' +
        '}';
  }

  public int getId() {
    return id;
  }

  public String getEmail() {
    return email;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) return true;
    if (obj == null || obj.getClass() != this.getClass()) return false;
    var that = (EMailEntity) obj;
    return this.id == that.id &&
        Objects.equals(this.email, that.email);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, email);
  }

}
