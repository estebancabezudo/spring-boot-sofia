package net.cabezudo.sofia.users.mappers;

import net.cabezudo.sofia.users.service.SofiaUser;
import net.cabezudo.sofia.users.rest.RestSofiaUserList;
import net.cabezudo.sofia.users.service.SofiaUserList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BusinessToRestUserListMapper {

  private @Autowired BusinessToRestUserMapper businessToRestUserMapper;

  public RestSofiaUserList map(SofiaUserList sofiaUserList) {
    int total = sofiaUserList.getTotal();
    int start = sofiaUserList.getStart();
    int size = sofiaUserList.getSize();
    RestSofiaUserList list = new RestSofiaUserList();

    for (SofiaUser user : sofiaUserList) {
      list.add(businessToRestUserMapper.map(user));
    }
    return list;
  }
}
