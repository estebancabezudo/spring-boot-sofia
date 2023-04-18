package net.cabezudo.sofia.places.mappers;

import net.cabezudo.sofia.places.AdministrativeDivision;
import net.cabezudo.sofia.places.AdministrativeDivisionList;
import net.cabezudo.sofia.places.rest.AdministrativeDivisionsRestList;
import net.cabezudo.sofia.places.rest.RestAdministrativeDivision;

public class RestToBusinessAdministrativeDivisionListMapper {
  public AdministrativeDivisionList map(AdministrativeDivisionsRestList administrativeDivisionsRestList) {
    AdministrativeDivisionList administrativeDivisionList = new AdministrativeDivisionList();
    for (RestAdministrativeDivision restAdministrativeDivision : administrativeDivisionsRestList) {
      RestToBusinessAdministrativeDivisionMapper restToBusinessAdministrativeDivisionMapper = new RestToBusinessAdministrativeDivisionMapper();
      AdministrativeDivision administrativeDivision = restToBusinessAdministrativeDivisionMapper.map(restAdministrativeDivision);
      administrativeDivisionList.add(administrativeDivision);
    }
    return administrativeDivisionList;
  }
}
