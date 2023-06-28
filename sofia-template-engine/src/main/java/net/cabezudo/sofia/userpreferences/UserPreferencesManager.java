package net.cabezudo.sofia.userpreferences;

import net.cabezudo.sofia.accounts.Account;
import net.cabezudo.sofia.accounts.mappers.EntityToBusinessAccountMapper;
import net.cabezudo.sofia.accounts.persistence.AccountEntity;
import net.cabezudo.sofia.accounts.persistence.AccountRepository;
import net.cabezudo.sofia.accounts.persistence.AccountUserRelationEntity;
import net.cabezudo.sofia.userpreferences.persistence.AccountPreferencesRepository;
import net.cabezudo.sofia.userpreferences.persistence.UserPreferencesRepository;
import net.cabezudo.sofia.users.SofiaUser;
import net.cabezudo.sofia.web.client.Language;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class UserPreferencesManager {
  private static final String ACCOUNT = "account";
  private static final String LANGUAGE = "language";
  private @Autowired UserPreferencesRepository userPreferencesRepository;
  private @Autowired AccountPreferencesRepository accountPreferencesRepository;
  private @Autowired AccountRepository accountRepository;
  private @Autowired EntityToBusinessAccountMapper entityToBusinessAccountMapper;

  @Transactional
  public void setLanguage(Account account, SofiaUser user, Language language) {
    if (account == null) {
      return;
    }
    AccountUserRelationEntity accountUserRelationEntity = accountRepository.find(account.getId(), user.getId(), account.getSite().getId());

    UserPreferences userPreferences = accountPreferencesRepository.get(accountUserRelationEntity.id(), LANGUAGE);
    // TODO set user preferences
  }

  @Transactional
  public Account getAccount(SofiaUser user) {
    String result = userPreferencesRepository.get(user.getId(), ACCOUNT);
    if (result == null) {
      return null;
    }

    int accountId = Integer.parseInt(result);
    AccountEntity accountEntity = accountRepository.get(accountId);
    return entityToBusinessAccountMapper.map(accountEntity);
  }

  public void setAccount(SofiaUser user, Account account) {
    userPreferencesRepository.update(user.getId(), ACCOUNT, Integer.toString(account.getId()));
  }

  public void createAccount(SofiaUser user, Account account) {
    userPreferencesRepository.create(user.getId(), ACCOUNT, Integer.toString(account.getId()));
  }
}
