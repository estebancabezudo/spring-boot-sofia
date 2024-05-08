package net.cabezudo.sofia.users.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import net.cabezudo.sofia.accounts.mappers.BusinessToEntityAccountMapper;
import net.cabezudo.sofia.accounts.persistence.AccountEntity;
import net.cabezudo.sofia.accounts.persistence.AccountRepository;
import net.cabezudo.sofia.accounts.persistence.AccountUserRelationEntity;
import net.cabezudo.sofia.accounts.service.Account;
import net.cabezudo.sofia.config.mail.SendEMailException;
import net.cabezudo.sofia.emails.EMail;
import net.cabezudo.sofia.emails.EMailManager;
import net.cabezudo.sofia.emails.persistence.EMailEntity;
import net.cabezudo.sofia.emails.persistence.EMailRepository;
import net.cabezudo.sofia.hostnames.HostnameManager;
import net.cabezudo.sofia.people.Person;
import net.cabezudo.sofia.people.persistence.PeopleRepository;
import net.cabezudo.sofia.people.service.PeopleManager;
import net.cabezudo.sofia.sites.Site;
import net.cabezudo.sofia.userpreferences.UserAccountPreferencesRepository;
import net.cabezudo.sofia.userpreferences.persistence.UserPreferencesRepository;
import net.cabezudo.sofia.users.PasswordGenerator;
import net.cabezudo.sofia.users.mappers.BusinessToEntityGroupsMapper;
import net.cabezudo.sofia.users.mappers.EntityToBusinessUserListMapper;
import net.cabezudo.sofia.users.mappers.EntityToBusinessUserMapper;
import net.cabezudo.sofia.users.persistence.GroupEntity;
import net.cabezudo.sofia.users.persistence.GroupsEntity;
import net.cabezudo.sofia.users.persistence.GroupsRepository;
import net.cabezudo.sofia.users.persistence.SofiaUserEntity;
import net.cabezudo.sofia.users.persistence.SofiaUserEntityList;
import net.cabezudo.sofia.users.persistence.SofiaUserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;

@Service
@Transactional
public class SofiaUserManager {
  private static final Logger log = LoggerFactory.getLogger(SofiaUserManager.class);

  private @Autowired BusinessToEntityGroupsMapper businessToEntityGroupsMapper;
  private @Autowired EntityToBusinessUserListMapper entityToBusinessUserListMapper;
  private @Autowired EntityToBusinessUserMapper entityToBusinessUserMapper;
  private @Autowired EMailRepository eMailRepository;
  private @Autowired SofiaUserRepository sofiaUserRepository;
  private @Autowired GroupsRepository groupsRepository;
  private @Autowired HttpServletRequest request;
  private @Autowired AccountRepository accountRepository;
  private @Autowired EMailManager eMailManager;
  private @Autowired PeopleManager peopleManager;
  private @Autowired HostnameManager hostnameManager;
  private @Autowired BusinessToEntityAccountMapper businessToEntityAccountMapper;
  private @Autowired PeopleRepository peopleRepository;
  private @Autowired UserPreferencesRepository userPreferencesRepository;
  private @Autowired UserAccountPreferencesRepository userAccountPreferencesRepository;

  public SofiaUser loadUserByUsername(String email) throws UsernameNotFoundException {
    Account account = (Account) request.getSession().getAttribute("account");

    final SofiaUserEntity sofiaUserEntity = sofiaUserRepository.findByEmailAndAccount(account.getId(), email);
    if (sofiaUserEntity == null) {
      throw new UsernameNotFoundException(email);
    }
    return entityToBusinessUserMapper.map(sofiaUserEntity);
  }

  private Collection<GrantedAuthority> getAuthorities(SofiaUserEntity sofiaUserEntity) {
    GroupsEntity userGroups = sofiaUserEntity.getGroups();
    Collection<GrantedAuthority> authorities = new ArrayList<>(userGroups.size());
    for (GroupEntity groupEntity : userGroups) {
      authorities.add(new SimpleGrantedAuthority(groupEntity.getName().toUpperCase()));
    }
    return authorities;
  }

  public SofiaUserList findAll(Account account) {
    final SofiaUserEntityList entityList = sofiaUserRepository.findAll(account.getId(), account.getSite().getId());
    return entityToBusinessUserListMapper.map(account, entityList);
  }

  public SofiaUser findById(int accountId, int userId) {
    AccountUserRelationEntity accountUserRelationEntity = accountRepository.getByAccountAndUser(accountId, userId);
    if (accountUserRelationEntity == null) {
      return null;
    }
    final SofiaUserEntity sofiaUserEntity = sofiaUserRepository.findByAccount(accountId, userId);
    return entityToBusinessUserMapper.map(sofiaUserEntity);
  }

  public SofiaUser findById(int accountId, String username) {
    final SofiaUserEntity sofiaUserEntity = sofiaUserRepository.get(username);
    if (sofiaUserEntity == null) {
      return null;
    }
    AccountUserRelationEntity relation = accountRepository.getByAccountAndUser(accountId, sofiaUserEntity.getId());
    if (relation == null) {
      return null;
    }
    return entityToBusinessUserMapper.map(sofiaUserEntity);
  }

  @Transactional
  public SofiaUser create(Site site, String eMailAddress, Groups groups, Locale locale, boolean enabled) {
    EMailEntity userFromDatabase = eMailRepository.get(eMailAddress);
    EMailEntity usernameToUse;
    if (userFromDatabase == null) {
      usernameToUse = eMailRepository.create(eMailAddress);
    } else {
      usernameToUse = userFromDatabase;
    }

    log.debug("Using email with id " + usernameToUse);

    SofiaUserEntity sofiaUserEntity = sofiaUserRepository.create(usernameToUse, locale.toString(), enabled);

    AccountEntity accountEntity = accountRepository.create(site.getId(), usernameToUse.getEmail());

    AccountUserRelationEntity accountUserRelationEntity = accountRepository.createAccountUserRelation(accountEntity.getId(), sofiaUserEntity.getId(), true);

    GroupsEntity groupsEntity = businessToEntityGroupsMapper.map(accountUserRelationEntity.getId(), groups);
    for (GroupEntity groupEntity : groupsEntity) {
      groupsRepository.create(sofiaUserEntity.getId(), groupEntity.getName());
    }

    return entityToBusinessUserMapper.map(sofiaUserEntity);
  }

  @Transactional
  public SofiaUser create(Account account, String eMailAddress, Groups groups, Locale locale, boolean enabled) {

    EMailEntity emailFromDatabase = eMailRepository.get(eMailAddress);
    EMailEntity emailToUse;
    if (emailFromDatabase == null) {
      emailToUse = eMailRepository.create(eMailAddress);
    } else {
      emailToUse = emailFromDatabase;
    }
    log.debug("Using email with id " + emailToUse);

    SofiaUserEntity userToUse;
    SofiaUserEntity userFromDatabase = sofiaUserRepository.getByEMail(emailToUse.getId());
    if (userFromDatabase == null) {
      userToUse = sofiaUserRepository.create(emailToUse, locale.toString(), enabled);
      accountRepository.getAccountByUserId(userToUse.getId());
      Site site = account.getSite();
      AccountEntity newAccountEntity = accountRepository.create(site.getId(), eMailAddress);
      accountRepository.createAccountUserRelation(newAccountEntity.getId(), userToUse.getId(), true);
    } else {
      userToUse = userFromDatabase;
    }

    AccountUserRelationEntity accountUserRelationEntity = accountRepository.createAccountUserRelation(account.getId(), userToUse.getId(), false);
    AccountEntity accountEntity = businessToEntityAccountMapper.map(account);
    userToUse.setAccount(accountEntity);

    GroupsEntity groupsEntity = businessToEntityGroupsMapper.map(accountUserRelationEntity.getId(), groups);
    for (GroupEntity groupEntity : groupsEntity) {
      groupsRepository.create(accountUserRelationEntity.getId(), groupEntity.getName());
    }

    return entityToBusinessUserMapper.map(userToUse);
  }

  @Transactional
  public SofiaUser update(int id, SofiaUser user) {
    SofiaUserEntity userInDatabase = sofiaUserRepository.get(id);

    String eMail = user.getUsername();

    EMailEntity userFromDatabase = eMailRepository.get(eMail);
    EMailEntity eMailToUse;
    if (userFromDatabase == null) {
      eMailToUse = eMailRepository.create(eMail);
    } else {
      eMailToUse = userFromDatabase;
    }
    String locale = user.getLocale().toString();
    SofiaUserEntity newEntity = new SofiaUserEntity(id, userInDatabase.getAccount(), eMailToUse, null, locale, user.isEnabled());

    final SofiaUserEntity updatedEntity = sofiaUserRepository.update(newEntity);

    AccountUserRelationEntity accountUserRelation = accountRepository.getByAccountAndUser(user.getAccount().getId(), user.getId());

    groupsRepository.deleteGroupsFor(accountUserRelation.getId());
    GroupsEntity groupsEntity = businessToEntityGroupsMapper.map(accountUserRelation.getId(), user.getGroups());
    for (GroupEntity groupEntity : groupsEntity) {
      GroupEntity newGroupEntity = groupsRepository.create(accountUserRelation.getId(), groupEntity.getName());
      updatedEntity.add(newGroupEntity);
    }

    int eMailToDelete = userInDatabase.getEMailEntity().getId();
    if (eMailToDelete != eMailToUse.getId()) {
      eMailRepository.delete(eMailToDelete);
    }

    return entityToBusinessUserMapper.map(updatedEntity);
  }

  public void delete(int accountId, int userId) {
    SofiaUserEntity user = sofiaUserRepository.get(userId);
    userAccountPreferencesRepository.delete(userId);
    AccountUserRelationEntity accountUserRelationEntity = accountRepository.getByAccountAndUser(accountId, userId);
    groupsRepository.deleteGroupsFor(accountUserRelationEntity.getId());
    accountRepository.delete(accountId, userId);


    int eMailIdToDelete = user.getEMailEntity().getId();
    try {
      sofiaUserRepository.delete(userId);
    } catch (DataAccessException e) {
      if (e.getCause() instanceof SQLIntegrityConstraintViolationException) {
        log.debug("I can't delete the user because is used in another account.");
      } else {
        throw e;
      }
    }

    try {
      eMailRepository.delete(eMailIdToDelete);
    } catch (DataAccessException e) {
      if (e.getCause() instanceof SQLIntegrityConstraintViolationException) {
        log.debug("I can't delete the user because is used in another account.");
      } else {
        throw e;
      }
    }
    eMailRepository.delete(eMailIdToDelete);
  }

  public SofiaUser updatePassword(Site site, Account account, int userId) throws SendEMailException, FileNotFoundException {
    // TODO put this generator values in some configuration. System, site or user, or all.
    String rawPassword = PasswordGenerator.generate(18, true, true, false);
    PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
    Password encodedPassword = new Password(encoder.encode(rawPassword));
    sofiaUserRepository.update(userId, encodedPassword);
    SofiaUserEntity sofiaUserEntity = sofiaUserRepository.get(userId);
    SofiaUser user = entityToBusinessUserMapper.map(sofiaUserEntity);
    Person person = peopleManager.getByUser(user);
    Locale userLocale = user.getLocale();
    EMail to = new EMail(user.getUsername());
    Password password = new Password(rawPassword);
    EMail emailFrom = new EMail(site.getReplyAddress());
    eMailManager.sendPasswordUpdated(site.getName(), emailFrom, person == null ? null : person.getName(), to, userLocale, password, site);
    return user;
  }
}


