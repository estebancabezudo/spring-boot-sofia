package net.cabezudo.sofia.people;

import net.cabezudo.sofia.accounts.Account;
import net.cabezudo.sofia.core.persistence.EntityList;
import net.cabezudo.sofia.people.mappers.EntityToBusinessPersonListMapper;
import net.cabezudo.sofia.people.mappers.EntityToBusinessPersonMapper;
import net.cabezudo.sofia.users.SofiaUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;

@Service
@Transactional
public class PeopleManager {

  @Resource
  private PeopleRepository peopleRepository;

  private @Autowired EntityToBusinessPersonMapper entityToBusinessPersonMapper;

  public PeopleList findAll(Account account) throws UsernameNotFoundException {
    final EntityList<PersonEntity> entityList = peopleRepository.findAll(account.getId());
    EntityToBusinessPersonListMapper mapper = new EntityToBusinessPersonListMapper();
    PeopleList list = mapper.map(entityList);
    return list;
  }

  public Person get(Account account, int id) {
    final PersonEntity personEntity = peopleRepository.get(account.getId(), id);
    EntityToBusinessPersonMapper mapper = new EntityToBusinessPersonMapper();
    return mapper.map(personEntity);
  }

  public Person getByUser(SofiaUser user) {
    PersonEntity personEntity = peopleRepository.findByUser(user.getId());
    if (personEntity == null) {
      return null;
    }
    return entityToBusinessPersonMapper.map(personEntity);
  }
}
