package net.cabezudo.sofia.accounts.mappers;

import net.cabezudo.sofia.accounts.service.Account;
import net.cabezudo.sofia.accounts.rest.RestAccount;
import org.springframework.stereotype.Component;

@Component
public class BusinessToRestAccountMapper {
  public RestAccount map(Account account) {
    if (account == null) {
      return null;
    }
    return new RestAccount(account.getId(), account.getName());
  }
}
