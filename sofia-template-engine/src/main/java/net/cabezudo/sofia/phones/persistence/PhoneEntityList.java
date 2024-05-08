package net.cabezudo.sofia.phones.persistence;

import net.cabezudo.sofia.core.persistence.EntityList;

import java.util.List;

public class PhoneEntityList extends EntityList<PhoneEntity> {
  public PhoneEntityList(int total, int start, int size, List<PhoneEntity> list) {
    super(total, start, size, list);
  }
}
