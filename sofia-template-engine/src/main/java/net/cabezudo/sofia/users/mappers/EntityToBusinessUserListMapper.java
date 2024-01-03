package net.cabezudo.sofia.users.mappers;

import net.cabezudo.sofia.accounts.service.Account;
import net.cabezudo.sofia.accounts.persistence.AccountEntity;
import net.cabezudo.sofia.core.persistence.EntityList;
import net.cabezudo.sofia.users.persistence.SofiaUserEntity;
import net.cabezudo.sofia.users.service.SofiaUserList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EntityToBusinessUserListMapper {

  private @Autowired EntityToBusinessUserMapper entityToBusinessUserMapper;

  public SofiaUserList map(AccountEntity accountEntity, EntityList<SofiaUserEntity> entityList) {
    int total = entityList.getTotal();
    int start = entityList.getStart();
    int size = entityList.size();
    SofiaUserList list = new SofiaUserList(total, start, size);

    for (SofiaUserEntity sofiaUserEntity : entityList) {
      list.add(entityToBusinessUserMapper.map(sofiaUserEntity));
    }
    return list;
  }

  public SofiaUserList map(Account account, EntityList<SofiaUserEntity> entityList) {
    int total = entityList.getTotal();
    int start = entityList.getStart();
    int size = entityList.size();
    SofiaUserList list = new SofiaUserList(total, start, size);

    for (SofiaUserEntity sofiaUserEntity : entityList) {
      list.add(entityToBusinessUserMapper.map(sofiaUserEntity));
    }
    return list;
  }
}
