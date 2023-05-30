package net.cabezudo.sofia.users.service;

import net.cabezudo.sofia.accounts.Account;
import net.cabezudo.sofia.accounts.persistence.AccountEntity;
import net.cabezudo.sofia.accounts.persistence.AccountRepository;
import net.cabezudo.sofia.accounts.persistence.AccountUserRelationEntity;
import net.cabezudo.sofia.emails.persistence.EMailEntity;
import net.cabezudo.sofia.emails.persistence.EMailRepository;
import net.cabezudo.sofia.sites.Site;
import net.cabezudo.sofia.users.Groups;
import net.cabezudo.sofia.users.SofiaUser;
import net.cabezudo.sofia.users.mappers.BusinessToEntityGroupsMapper;
import net.cabezudo.sofia.users.mappers.EntityToBusinessUserListMapper;
import net.cabezudo.sofia.users.mappers.EntityToBusinessUserMapper;
import net.cabezudo.sofia.users.persistence.GroupEntity;
import net.cabezudo.sofia.users.persistence.GroupsEntity;
import net.cabezudo.sofia.users.persistence.GroupsRepository;
import net.cabezudo.sofia.users.persistence.UserEntity;
import net.cabezudo.sofia.users.persistence.UserEntityList;
import net.cabezudo.sofia.users.persistence.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.Collection;

@Service
@Transactional
public class UserManager {
  private static final Logger log = LoggerFactory.getLogger(UserManager.class);

  private @Autowired BusinessToEntityGroupsMapper businessToEntityGroupsMapper;
  private @Autowired EntityToBusinessUserListMapper entityToBusinessUserListMapper;
  private @Autowired EntityToBusinessUserMapper entityToBusinessUserMapper;
  private @Resource EMailRepository eMailRepository;
  private @Resource UserRepository userRepository;
  private @Autowired GroupsRepository groupsRepository;
  private @Autowired HttpServletRequest request;
  private @Autowired AccountRepository accountRepository;

  public SofiaUser loadUserByUsername(String email) throws UsernameNotFoundException {
    Account account = (Account) request.getSession().getAttribute("account");

    final UserEntity userEntity = userRepository.findByEmail(account.id(), email);
    if (userEntity == null) {
      throw new UsernameNotFoundException(email);
    }
    return new SofiaUser(userEntity.getId(), account, userEntity.getEMailEntity().email(), userEntity.getPassword(), getAuthorities(userEntity), userEntity.isEnabled());
  }

  private Collection<GrantedAuthority> getAuthorities(UserEntity userEntity) {
    GroupsEntity userGroups = userEntity.getEntityGroups();
    Collection<GrantedAuthority> authorities = new ArrayList<>(userGroups.size());
    for (GroupEntity groupEntity : userGroups) {
      authorities.add(new SimpleGrantedAuthority(groupEntity.getName().toUpperCase()));
    }
    return authorities;
  }

  public UserList findAll(Account account) {
    final UserEntityList entityList = userRepository.findAll(account.id());
    return entityToBusinessUserListMapper.map(entityList);
  }

  public SofiaUser findByAccountId(Integer id) {
    final UserEntity userEntity = userRepository.get(id);
    return entityToBusinessUserMapper.map(userEntity);
  }

  public SofiaUser findByAccountId(int accountId, String username) {
    final UserEntity userEntity = userRepository.get(accountId, username);
    if (userEntity == null) {
      return null;
    }
    return entityToBusinessUserMapper.map(userEntity);
  }

  @Transactional
  public SofiaUser create(Site site, String eMailAddress, Groups groups, boolean enabled) {
    EMailEntity userFromDatabase = eMailRepository.get(eMailAddress);
    EMailEntity usernameToUse;
    if (userFromDatabase == null) {
      usernameToUse = eMailRepository.create(eMailAddress);
    } else {
      usernameToUse = userFromDatabase;
    }

    log.debug("Using email with id " + usernameToUse);

    UserEntity userEntity = userRepository.create(usernameToUse, enabled);

    AccountEntity account = accountRepository.create(site.getId());

    AccountUserRelationEntity accountUserRelationEntity = accountRepository.create(account.id(), userEntity.getId(), true);

    GroupsEntity groupsEntity = businessToEntityGroupsMapper.map(accountUserRelationEntity.id(), groups);
    for (GroupEntity groupEntity : groupsEntity) {
      groupsRepository.create(userEntity.getId(), groupEntity.getName());
    }

    return entityToBusinessUserMapper.map(userEntity);
  }

  @Transactional
  public SofiaUser create(Account account, String eMailAddress, Groups groups, boolean enabled) {

    EMailEntity userFromDatabase = eMailRepository.get(eMailAddress);
    EMailEntity usernameToUse;
    if (userFromDatabase == null) {
      usernameToUse = eMailRepository.create(eMailAddress);
    } else {
      usernameToUse = userFromDatabase;
    }

    log.debug("Using email with id " + usernameToUse);

    UserEntity userEntity = userRepository.create(usernameToUse, enabled);

    AccountUserRelationEntity accountUserRelationEntity = accountRepository.create(account.id(), userEntity.getId(), false);
    userEntity.setAccountUserId(accountUserRelationEntity.id());

    GroupsEntity groupsEntity = businessToEntityGroupsMapper.map(accountUserRelationEntity.id(), groups);
    for (GroupEntity groupEntity : groupsEntity) {
      groupsRepository.create(userEntity.getAccountUserId(), groupEntity.getName());
    }

    return entityToBusinessUserMapper.map(userEntity);
  }

  @Transactional
  public SofiaUser update(int id, SofiaUser user) {
    UserEntity userInDatabase = userRepository.get(id);

    String eMail = user.getUsername();

    EMailEntity userFromDatabase = eMailRepository.get(eMail);
    EMailEntity eMailToUse;
    if (userFromDatabase == null) {
      eMailToUse = eMailRepository.create(eMail);
    } else {
      eMailToUse = userFromDatabase;
    }

    UserEntity newEntity = new UserEntity(
        id, user.getAccount().id(), userInDatabase.getAccountUserId(), eMailToUse, null, user.isEnabled()
    );

    final UserEntity updatedEntity = userRepository.update(newEntity);

    groupsRepository.deleteGroupsFor(userInDatabase.getAccountUserId());
    GroupsEntity groupsEntity = businessToEntityGroupsMapper.map(userInDatabase.getAccountUserId(), user.getGroups());
    for (GroupEntity groupEntity : groupsEntity) {
      GroupEntity newGroupEntity = groupsRepository.create(updatedEntity.getAccountUserId(), groupEntity.getName());
      updatedEntity.add(newGroupEntity);
    }

    int eMailToDelete = userInDatabase.getEMailEntity().id();
    if (eMailToDelete != eMailToUse.id()) {
      eMailRepository.delete(eMailToDelete);
    }

    return entityToBusinessUserMapper.map(updatedEntity);
  }

  public void delete(int accountId, int userId) {
    UserEntity user = userRepository.get(userId);
    groupsRepository.deleteGroupsFor(user.getAccountUserId());

    accountRepository.delete(accountId, userId);

    int eMailIdToDelete = user.getEMailEntity().id();
    userRepository.delete(userId);

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
}


