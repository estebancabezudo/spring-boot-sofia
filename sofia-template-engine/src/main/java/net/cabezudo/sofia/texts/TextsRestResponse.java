package net.cabezudo.sofia.texts;

import net.cabezudo.sofia.core.rest.SofiaRestResponse;

public class TextsRestResponse extends SofiaRestResponse {
  private final String data;

  public TextsRestResponse(String data) {
    super(SofiaRestResponse.OK, "Texts found");
    this.data = data;
  }

  @Override
  public String getData() {
    return data;
  }
}
