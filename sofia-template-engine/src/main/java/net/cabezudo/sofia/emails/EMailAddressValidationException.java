package net.cabezudo.sofia.emails;

public class EMailAddressValidationException extends Exception {
  private final String[] parameters;

  public EMailAddressValidationException(String message, String... parameters) {
    super(message);
    this.parameters = parameters;
  }

  public String[] getParameters() {
    return parameters;
  }
}
