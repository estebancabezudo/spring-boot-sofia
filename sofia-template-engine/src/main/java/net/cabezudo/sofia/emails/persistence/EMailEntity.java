package net.cabezudo.sofia.emails.persistence;

public record EMailEntity(int id, String email) {
  @Override
  public String toString() {
    return "EMailEntity{" +
        "id=" + id +
        ", email='" + email + '\'' +
        '}';
  }
}
