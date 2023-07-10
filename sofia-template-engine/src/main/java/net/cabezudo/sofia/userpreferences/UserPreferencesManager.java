package net.cabezudo.sofia.userpreferences;

import net.cabezudo.sofia.accounts.service.Account;
import net.cabezudo.sofia.accounts.mappers.EntityToBusinessAccountMapper;
import net.cabezudo.sofia.accounts.persistence.AccountEntity;
import net.cabezudo.sofia.accounts.persistence.AccountRepository;
import net.cabezudo.sofia.userpreferences.persistence.AccountPreferencesRepository;
import net.cabezudo.sofia.userpreferences.persistence.UserPreferencesRepository;
import net.cabezudo.sofia.users.service.SofiaUser;
import net.cabezudo.sofia.web.client.Language;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class UserPreferencesManager {
  public static final String ACCOUNT = "account";
  public static final String LANGUAGE = "language";
  private @Autowired UserPreferencesRepository userPreferencesRepository;
  private @Autowired AccountPreferencesRepository accountPreferencesRepository;
  private @Autowired AccountRepository accountRepository;
  private @Autowired EntityToBusinessAccountMapper entityToBusinessAccountMapper;

  @Transactional
  public Language getLanguageByUserId(int userId) {
    String languageCode = userPreferencesRepository.get(userId, LANGUAGE);
    if (languageCode == null) {
      return null;
    }
    return new Language(languageCode);
  }

  @Transactional
  public void setLanguage(SofiaUser user, Language language) {
    if (user == null) {
      return;
    }
    userPreferencesRepository.update(user.getId(), LANGUAGE, language.getCode());
  }

  @Transactional
  public Account getAccountByUserId(int userId) {
    String result = userPreferencesRepository.get(userId, ACCOUNT);
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
}
