package net.cabezudo.sofia.users.service;

import net.cabezudo.sofia.accounts.Account;
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

  @Autowired
  BusinessToEntityGroupsMapper businessToEntityGroupsMapper;
  @Autowired
  EntityToBusinessUserListMapper entityToBusinessUserListMapper;
  @Autowired
  EntityToBusinessUserMapper entityToBusinessUserMapper;
  @Resource
  EMailRepository eMailRepository;
  @Resource
  UserRepository userRepository;

  @Autowired
  GroupsRepository groupsRepository;
  @Autowired
  HttpServletRequest request;

  public SofiaUser loadUserByUsername(String email) throws UsernameNotFoundException {
    Site site = (Site) request.getSession().getAttribute("site");
    final UserEntity userEntity = userRepository.findByEmail(site.getId(), email);
    if (userEntity == null) {
      throw new UsernameNotFoundException(email);
    }
    SofiaUser sofiaUser = new SofiaUser(userEntity.getId(), site, userEntity.getUsername(), userEntity.getPassword(), getAuthorities(userEntity), userEntity.isEnabled());
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

    final UserEntity userEntity = userRepository.create(account.id(), siteId, userToUse.email(), password, enabled);

    groupsRepository.deleteGroupsFor(userEntity);
    GroupsEntity groupsEntity = businessToEntityGroupsMapper.map(userEntity, groups);
    for (GroupEntity groupEntity : groupsEntity) {
      groupsRepository.create(userEntity.getId(), groupEntity.getName());
    }

    return entityToBusinessUserMapper.map(userEntity);
  }
}


