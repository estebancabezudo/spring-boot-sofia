package net.cabezudo.sofia.places.mappers;

import net.cabezudo.sofia.places.AdministrativeDivision;
import net.cabezudo.sofia.places.AdministrativeDivisionList;
import net.cabezudo.sofia.places.rest.AdministrativeDivisionsRestList;
import net.cabezudo.sofia.places.rest.RestAdministrativeDivision;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RestToBusinessAdministrativeDivisionListMapper {
  private @Autowired RestToBusinessAdministrativeDivisionMapper restToBusinessAdministrativeDivisionMapper;

  public AdministrativeDivisionList map(AdministrativeDivisionsRestList administrativeDivisionsRestList) {
    AdministrativeDivisionList administrativeDivisionList = new AdministrativeDivisionList();
    if (administrativeDivisionsRestList != null) {
      for (RestAdministrativeDivision restAdministrativeDivision : administrativeDivisionsRestList) {
        AdministrativeDivision administrativeDivision = restToBusinessAdministrativeDivisionMapper.map(restAdministrativeDivision);
        administrativeDivisionList.add(administrativeDivision);
      }
    }
    return administrativeDivisionList;
  }
}
