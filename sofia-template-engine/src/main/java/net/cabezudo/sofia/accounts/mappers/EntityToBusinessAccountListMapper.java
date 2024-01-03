package net.cabezudo.sofia.accounts.mappers;

import net.cabezudo.sofia.accounts.service.Accounts;
import net.cabezudo.sofia.accounts.persistence.AccountEntity;
import net.cabezudo.sofia.core.persistence.EntityList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EntityToBusinessAccountListMapper {

  private @Autowired EntityToBusinessAccountMapper entityToBusinessAccountMapper;

  public Accounts map(EntityList<AccountEntity> list) {
    int total = list.getTotal();
    int start = list.getStart();
    int size = list.getSize();
    Accounts accounts = new Accounts(total, start, size);

    for (AccountEntity accountEntity : list) {
      accounts.add(entityToBusinessAccountMapper.map(accountEntity));
    }
    return accounts;
  }

}
