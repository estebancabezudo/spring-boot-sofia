package net.cabezudo.sofia.language.persistence;

import net.cabezudo.sofia.core.persistence.EntityList;

import java.util.List;

public class LanguageEntityList extends EntityList<LanguageEntity> {
  public LanguageEntityList(int total, int start, int size, List<LanguageEntity> list) {
    super(total, start, size, list);
  }

  public LanguageEntityList() {
    super();
  }
}