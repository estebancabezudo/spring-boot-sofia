package net.cabezudo.sofia.places.mappers;

import net.cabezudo.sofia.places.Street;
import net.cabezudo.sofia.places.persistence.StreetEntity;
import org.springframework.stereotype.Component;

@Component
public class EntityToBusinessStreetMapper {
  public Street map(StreetEntity streetEntity) {
    if (streetEntity == null) {
      return null;
    }
    return new Street(streetEntity.getId(), streetEntity.getName(), streetEntity.isVerified());
  }
}
