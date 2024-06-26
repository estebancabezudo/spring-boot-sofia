package net.cabezudo.sofia.web.client.rest;

import com.fasterxml.jackson.annotation.JsonIgnore;
import net.cabezudo.sofia.accounts.mappers.BusinessToRestAccountMapper;
import net.cabezudo.sofia.accounts.rest.RestAccount;
import net.cabezudo.sofia.accounts.service.Account;
import net.cabezudo.sofia.core.WebMessageManager;
import net.cabezudo.sofia.language.Language;
import net.cabezudo.sofia.users.mappers.BusinessToRestUserMapper;
import net.cabezudo.sofia.users.rest.RestSofiaUser;
import net.cabezudo.sofia.web.client.WebClientData;
import net.cabezudo.sofia.web.user.WebUserData;


public class FullRestWebClientData {
  private final String language;
  private final RestAccount account;
  private final RestSofiaUser user;
  @JsonIgnore
  private final BusinessToRestAccountMapper businessToRestAccountMapper = new BusinessToRestAccountMapper();
  @JsonIgnore
  private final BusinessToRestUserMapper businessToRestUserMapper = new BusinessToRestUserMapper();
  private final String message;

  public FullRestWebClientData(Account account, WebMessageManager webMessageManager, WebClientData c, WebUserData u) {
    message = webMessageManager.getMessage();
    webMessageManager.clearMessage();
    this.account = businessToRestAccountMapper.map(account);

    if (u != null && u.getUser() != null) {
      Language languageFromWebUserData = u.getLanguage();
      if (languageFromWebUserData == null) {
        language = c.getLanguage().getCode();
      } else {
        language = languageFromWebUserData.getCode();
      }
      user = businessToRestUserMapper.map(u.getUser());
    } else {
      language = c.getLanguage() == null ? null : c.getLanguage().getCode();
      user = null;
    }
  }

  public String getLanguage() {
    return language;
  }

  public RestAccount getAccount() {
    return account;
  }

  public RestSofiaUser getUser() {
    return user;
  }

  public String getMessage() {
    return message;
  }

  @Override
  public String toString() {
    return "FullRestWebClientData{" +
        "language=" + language +
        ", account=" + account +
        ", user=" + user +
        ", message=" + message +
        ", businessToRestAccountMapper=" + businessToRestAccountMapper +
        ", businessToRestUserMapper=" + businessToRestUserMapper +
        '}';
  }
}
