package net.cabezudo.sofia.places.rest;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class RestStreet {
  private final Integer id;
  private final String name;
  private final boolean verified;
}
