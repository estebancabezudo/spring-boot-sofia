package net.cabezudo.sofia.texts;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import net.cabezudo.sofia.core.rest.SofiaRestResponse;

@JsonSerialize(using = TextsRestResponseSerializer.class)
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
