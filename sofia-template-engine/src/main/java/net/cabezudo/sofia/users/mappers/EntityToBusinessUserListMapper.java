package net.cabezudo.sofia.users.mappers;

import net.cabezudo.sofia.accounts.service.Account;
import net.cabezudo.sofia.accounts.persistence.AccountEntity;
import net.cabezudo.sofia.core.persistence.EntityList;
import net.cabezudo.sofia.users.persistence.UserEntity;
import net.cabezudo.sofia.users.service.UserList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EntityToBusinessUserListMapper {

  private @Autowired EntityToBusinessUserMapper entityToBusinessUserMapper;

  public UserList map(AccountEntity accountEntity, EntityList<UserEntity> entityList) {
    int total = entityList.getTotal();
    int start = entityList.getStart();
    int size = entityList.size();
    UserList list = new UserList(total, start, size);

    for (UserEntity userEntity : entityList) {
      list.add(entityToBusinessUserMapper.map(userEntity));
    }
    return list;
  }

  public UserList map(Account account, EntityList<UserEntity> entityList) {
    int total = entityList.getTotal();
    int start = entityList.getStart();
    int size = entityList.size();
    UserList list = new UserList(total, start, size);

    for (UserEntity userEntity : entityList) {
      list.add(entityToBusinessUserMapper.map(userEntity));
    }
    return list;
  }
}
