package net.cabezudo.sofia.web.client.rest;

import com.fasterxml.jackson.annotation.JsonIgnore;
import net.cabezudo.sofia.accounts.mappers.BusinessToRestAccountMapper;
import net.cabezudo.sofia.accounts.rest.RestAccount;
import net.cabezudo.sofia.users.mappers.BusinessToRestUserMapper;
import net.cabezudo.sofia.users.rest.RestUser;
import net.cabezudo.sofia.web.client.Language;
import net.cabezudo.sofia.web.client.WebClientData;
import net.cabezudo.sofia.web.user.WebUserData;

public class FullRestWebClientData {
  private final String language;
  private final RestAccount account;
  private final RestUser user;
  @JsonIgnore
  private final BusinessToRestAccountMapper businessToRestAccountMapper = new BusinessToRestAccountMapper();
  @JsonIgnore
  private final BusinessToRestUserMapper businessToRestUserMapper = new BusinessToRestUserMapper();

  public FullRestWebClientData(WebClientData c, WebUserData u) {
    if (u != null && u.getUser() != null) {
      Language languageFromWebUserData = u.getLanguage();
      if (languageFromWebUserData == null) {
        language = c.getLanguage().getCode();
      } else {
        language = languageFromWebUserData.getCode();
      }
      if (c.getAccount() == null) {
        account = businessToRestAccountMapper.map(u.getAccount());
      } else {
        account = businessToRestAccountMapper.map(c.getAccount());
      }
      user = businessToRestUserMapper.map(u.getUser());
    } else {
      language = c.getLanguage() == null ? null : c.getLanguage().getCode();
      account = businessToRestAccountMapper.map(c.getAccount());
      user = null;
    }
  }

  public String getLanguage() {
    return language;
  }

  public RestAccount getAccount() {
    return account;
  }

  public RestUser getUser() {
    return user;
  }

  @Override
  public String toString() {
    return "FullRestWebClientData{" +
        "language=" + language +
        ", account=" + account +
        ", user=" + user +
        ", businessToRestAccountMapper=" + businessToRestAccountMapper +
        ", businessToRestUserMapper=" + businessToRestUserMapper +
        '}';
  }
}
