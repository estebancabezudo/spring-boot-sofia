package net.cabezudo.sofia.places.rest;

import net.cabezudo.sofia.core.rest.SofiaRestResponse;

public class PlacesRestResponse extends SofiaRestResponse<RestPlace> {
  public PlacesRestResponse(int status, String message, RestPlace place) {
    super(status, message, place);
  }
}
