package net.cabezudo.sofia.users.mappers;

import net.cabezudo.sofia.accounts.Account;
import net.cabezudo.sofia.accounts.mappers.EntityToBusinessAccountMapper;
import net.cabezudo.sofia.accounts.persistence.AccountEntity;
import net.cabezudo.sofia.emails.persistence.EMailEntity;
import net.cabezudo.sofia.users.Groups;
import net.cabezudo.sofia.users.SofiaUser;
import net.cabezudo.sofia.users.persistence.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class EntityToBusinessUserMapper {

  private @Autowired EntityToBusinessAccountMapper entityToBusinessAccountMapper;

  public SofiaUser map(UserEntity userEntity) {
    EntityToBusinessGroupsMapper mapper = new EntityToBusinessGroupsMapper();
    int id = userEntity.getId();
    EMailEntity eMailEntity = userEntity.getEMailEntity();
    String password = userEntity.getPassword();
    Groups groups = mapper.map(userEntity.getGroups());
    boolean isEnabled = userEntity.isEnabled();
    Locale locale = new Locale(userEntity.getLocale());
    AccountEntity accountEntity = userEntity.getAccount();
    Account account = entityToBusinessAccountMapper.map(accountEntity);
    return new SofiaUser(id, account, eMailEntity.getEmail(), password, groups, locale, isEnabled);
  }
}
