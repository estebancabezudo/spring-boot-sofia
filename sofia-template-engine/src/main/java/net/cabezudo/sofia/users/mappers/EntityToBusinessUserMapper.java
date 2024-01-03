package net.cabezudo.sofia.users.mappers;

import net.cabezudo.sofia.accounts.service.Account;
import net.cabezudo.sofia.accounts.mappers.EntityToBusinessAccountMapper;
import net.cabezudo.sofia.accounts.persistence.AccountEntity;
import net.cabezudo.sofia.emails.persistence.EMailEntity;
import net.cabezudo.sofia.users.service.Groups;
import net.cabezudo.sofia.users.service.SofiaUser;
import net.cabezudo.sofia.users.persistence.SofiaUserEntity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class EntityToBusinessUserMapper {

  private @Autowired EntityToBusinessAccountMapper entityToBusinessAccountMapper;

  public SofiaUser map(SofiaUserEntity sofiaUserEntity) {
    EntityToBusinessGroupsMapper mapper = new EntityToBusinessGroupsMapper();
    int id = sofiaUserEntity.getId();
    EMailEntity eMailEntity = sofiaUserEntity.getEMailEntity();
    String password = sofiaUserEntity.getPassword();
    Groups groups = mapper.map(sofiaUserEntity.getGroups());
    boolean isEnabled = sofiaUserEntity.isEnabled();
    Locale locale = new Locale(sofiaUserEntity.getLocale());
    AccountEntity accountEntity = sofiaUserEntity.getAccount();
    Account account = entityToBusinessAccountMapper.map(accountEntity);
    return new SofiaUser(id, account, eMailEntity.getEmail(), password, groups, locale, isEnabled);
  }
}
