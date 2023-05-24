package net.cabezudo.sofia.accounts.mappers;

import net.cabezudo.sofia.accounts.Account;
import net.cabezudo.sofia.accounts.persistence.AccountEntity;
import org.springframework.stereotype.Component;

@Component
public class EntityToBusinessAccountMapper {
  public Account map(AccountEntity entity) {
    return new Account(entity.id(), entity.siteId());
  }
}
