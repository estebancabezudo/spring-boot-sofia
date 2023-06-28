package net.cabezudo.sofia.accounts.mappers;

import net.cabezudo.sofia.accounts.Account;
import net.cabezudo.sofia.accounts.rest.RestAccount;
import org.springframework.stereotype.Component;

@Component
public class BusinessToRestAccountMapper {
  public RestAccount map(Account account) {
    return new RestAccount(account.getId(), account.getName());
  }
}
