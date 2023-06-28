package net.cabezudo.sofia.users.mappers;

import net.cabezudo.sofia.users.SofiaUser;
import net.cabezudo.sofia.users.rest.RestUserList;
import net.cabezudo.sofia.users.service.UserList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BusinessToRestUserListMapper {

  private @Autowired BusinessToRestUserMapper businessToRestUserMapper;

  public RestUserList map(UserList userList) {
    int total = userList.getTotal();
    int start = userList.getStart();
    int size = userList.getSize();
    RestUserList list = new RestUserList();

    for (SofiaUser user : userList) {
      list.add(businessToRestUserMapper.map(user));
    }
    return list;
  }
}
