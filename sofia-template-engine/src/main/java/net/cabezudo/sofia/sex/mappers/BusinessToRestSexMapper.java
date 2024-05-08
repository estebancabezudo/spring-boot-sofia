package net.cabezudo.sofia.sex.mappers;

import net.cabezudo.sofia.sex.Sex;
import org.springframework.stereotype.Component;

@Component
public class BusinessToRestSexMapper {
  public Character map(Sex sex) {
    return sex.toCharacter();
  }
}
