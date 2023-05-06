package net.cabezudo.sofia.places.mappers;

import net.cabezudo.sofia.places.AdministrativeDivision;
import net.cabezudo.sofia.places.AdministrativeDivisionList;
import net.cabezudo.sofia.places.rest.AdministrativeDivisionsRestList;
import net.cabezudo.sofia.places.rest.RestAdministrativeDivision;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BusinessToRestAdminitrativeDivisionListMapper {
  private @Autowired BusinessToRestAdminitrativeDivisionMapper businessToRestAdminitrativeDivisionMapper;

  public AdministrativeDivisionsRestList map(AdministrativeDivisionList administrativeDivisionList) {
    AdministrativeDivisionsRestList administrativeDivisionsRestList = new AdministrativeDivisionsRestList();
    if (administrativeDivisionList == null) {
      return null;
    }

    for (AdministrativeDivision administrativeDivision : administrativeDivisionList) {
      RestAdministrativeDivision restAdministrativeDivision = businessToRestAdminitrativeDivisionMapper.map(administrativeDivision);
      administrativeDivisionsRestList.add(restAdministrativeDivision);
    }
    return administrativeDivisionsRestList;
  }
}
