package net.cabezudo.sofia.users.service;

import net.cabezudo.sofia.accounts.Account;
import net.cabezudo.sofia.accounts.persistence.AccountRepository;
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
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
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
    Site site = (Site) request.getSession().getAttribute("site");
    final UserEntity userEntity = userRepository.findByEmail(site.getId(), email);
    if (userEntity == null) {
      throw new UsernameNotFoundException(email);
    }
    SofiaUser sofiaUser = new SofiaUser(userEntity.getId(), site, userEntity.getEMailEntity().email(), userEntity.getPassword(), getAuthorities(userEntity), userEntity.isEnabled());
    return sofiaUser;
  }

  private Collection<GrantedAuthority> getAuthorities(UserEntity userEntity) {
    GroupsEntity userGroups = userEntity.getEntityGroups();
    Collection<GrantedAuthority> authorities = new ArrayList<>(userGroups.size());
    for (GroupEntity groupEntity : userGroups) {
      authorities.add(new SimpleGrantedAuthority(groupEntity.getName().toUpperCase()));
    }
    return authorities;
  }

  public UserList findAll(Site site, Account account) {
    final UserEntityList entityList = userRepository.findAll(site.getId(), account.id());
    return entityToBusinessUserListMapper.map(entityList);
  }

  public SofiaUser get(Account account, Integer id) {
    final UserEntity placeEntity = userRepository.get(id);
    return entityToBusinessUserMapper.map(placeEntity);
  }

  @Transactional
  public SofiaUser create(Account account, int siteId, String eMailAddress, String password, Groups groups, boolean enabled) {

    EMailEntity userFromDatabase = eMailRepository.get(eMailAddress);
    EMailEntity userToUse;
    if (userFromDatabase == null) {
      userToUse = eMailRepository.create(eMailAddress);
    } else {
      userToUse = userFromDatabase;
    }

    log.debug("Using email with id " + userToUse);

    UserEntity userEntity = userRepository.create(siteId, userToUse, password, enabled);

    accountRepository.addUser(account.id(), userEntity.getId());

    GroupsEntity groupsEntity = businessToEntityGroupsMapper.map(userEntity, groups);
    for (GroupEntity groupEntity : groupsEntity) {
      groupsRepository.create(userEntity.getId(), groupEntity.getName());
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
        id, user.getSite().getId(), eMailToUse, null, user.isEnabled()
    );

    final UserEntity updatedEntity = userRepository.update(newEntity);

    groupsRepository.deleteGroupsFor(updatedEntity);
    GroupsEntity groupsEntity = businessToEntityGroupsMapper.map(updatedEntity, user.getGroups());
    for (GroupEntity groupEntity : groupsEntity) {
      GroupEntity newGroupEntity = groupsRepository.create(updatedEntity.getId(), groupEntity.getName());
      updatedEntity.add(newGroupEntity);
    }

    int eMailToDelete = userInDatabase.getEMailEntity().id();
    if (eMailToDelete != eMailToUse.id()) {
      eMailRepository.delete(eMailToDelete);
    }

    return entityToBusinessUserMapper.map(updatedEntity);
  }

}


