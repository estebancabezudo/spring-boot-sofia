package net.cabezudo.sofia.places;

import net.cabezudo.sofia.core.service.BusinessList;

public class PlaceList extends BusinessList<Place> {
  public PlaceList(int total, int start, int end) {
    super(total, start, end);
  }
}
