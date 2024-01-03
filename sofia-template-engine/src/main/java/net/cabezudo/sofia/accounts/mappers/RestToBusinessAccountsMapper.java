package net.cabezudo.sofia.accounts.mappers;

import net.cabezudo.sofia.accounts.service.Account;
import net.cabezudo.sofia.accounts.service.Accounts;
import net.cabezudo.sofia.accounts.rest.RestAccount;
import net.cabezudo.sofia.accounts.rest.RestAccounts;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RestToBusinessAccountsMapper {

  private @Autowired BusinessToRestAccountMapper businessToRestAccountMapper;

  public RestAccounts map(Accounts accounts) {
    RestAccounts restAccounts = new RestAccounts();
    for (Account account : accounts) {
      RestAccount restAccount = businessToRestAccountMapper.map(account);
      restAccounts.add(restAccount);
    }
    return restAccounts;
  }
}
