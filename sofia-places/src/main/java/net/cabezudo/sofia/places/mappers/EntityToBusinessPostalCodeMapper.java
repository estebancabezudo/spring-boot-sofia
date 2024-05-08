package net.cabezudo.sofia.places.mappers;

import net.cabezudo.sofia.places.PostalCode;
import net.cabezudo.sofia.places.persistence.PostalCodeEntity;
import org.springframework.stereotype.Component;

@Component
public class EntityToBusinessPostalCodeMapper {
  public PostalCode map(PostalCodeEntity postalCodeEntity) {
    return new PostalCode(postalCodeEntity.getCode(), postalCodeEntity.isVerified());
  }
}
