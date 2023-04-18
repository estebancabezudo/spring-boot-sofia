package net.cabezudo.sofia.places.persistence;

import net.cabezudo.sofia.core.persistence.EntityList;

import java.util.List;

public class AdministrativeDivisionEntityList extends EntityList<AdministrativeDivisionEntity> {
  public AdministrativeDivisionEntityList(int total, int start, int size, List<AdministrativeDivisionEntity> data) {
    super(total, start, size, data);
  }

  public AdministrativeDivisionEntityList() {
    super();
  }
}
