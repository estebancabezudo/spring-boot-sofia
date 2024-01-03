package net.cabezudo.sofia.texts;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;


import java.io.IOException;

public class TextsRestResponseSerializer extends JsonSerializer<TextsRestResponse> {
  @Override
  public void serialize(TextsRestResponse textsRestResponse, JsonGenerator jgen, SerializerProvider serializers) throws IOException {
    jgen.writeStartObject();
    jgen.writeNumberField("status", textsRestResponse.getStatus());
    ObjectMapper objectMapper = new ObjectMapper();
    jgen.writeObjectField("data", objectMapper.readTree(textsRestResponse.getData()));
    jgen.writeStringField("message", textsRestResponse.getMessage());
    jgen.writeEndObject();
  }
}
