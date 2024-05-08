package net.cabezudo.sofia.sex.mappers;

import net.cabezudo.sofia.core.SofiaRuntimeException;
import net.cabezudo.sofia.sex.Sex;
import org.springframework.stereotype.Component;

@Component
public class RestToBusinessSexMapper {
  public Sex map(Character sex) {
    switch (sex) {
      case 'm':
      case 'M':
        return Sex.MALE;
      case 'f':
      case 'F':
        return Sex.FEMALE;
      default:
        throw new SofiaRuntimeException("Invalid sex: " + sex);
    }
  }
}
