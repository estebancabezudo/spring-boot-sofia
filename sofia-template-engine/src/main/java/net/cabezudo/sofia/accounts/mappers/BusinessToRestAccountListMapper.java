package net.cabezudo.sofia.accounts.mappers;

import net.cabezudo.sofia.accounts.service.Account;
import net.cabezudo.sofia.accounts.service.Accounts;
import net.cabezudo.sofia.accounts.rest.RestAccountList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BusinessToRestAccountListMapper {
  private @Autowired BusinessToRestAccountMapper businessToRestAccountMapper;

  public RestAccountList map(Accounts list) {
    int total = list.getTotal();
    int start = list.getStart();
    int size = list.getSize();
    RestAccountList restList = new RestAccountList();

    for (Account account : list) {
      restList.add(businessToRestAccountMapper.map(account));
    }
    return restList;
  }
}
