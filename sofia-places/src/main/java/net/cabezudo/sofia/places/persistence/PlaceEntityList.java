package net.cabezudo.sofia.places.persistence;

import net.cabezudo.sofia.core.persistence.EntityList;

import java.util.List;

public class PlaceEntityList extends EntityList<PlaceEntity> {
  public PlaceEntityList(int total, int start, int size, List<PlaceEntity> list) {
    super(total, start, size, list);
  }
}
