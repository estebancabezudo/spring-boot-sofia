package net.cabezudo.sofia.people;

import net.cabezudo.sofia.accounts.Account;
import net.cabezudo.sofia.core.persistence.EntityList;
import net.cabezudo.sofia.sites.Site;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;

@Service
@Transactional
public class PeopleManager {

  @Resource
  private PeopleRepository peopleRepository;

  public PeopleList findAll(Site site, Account account) throws UsernameNotFoundException {
    final EntityList<PersonEntity> entityList = peopleRepository.findAll(site, account.id());
    EntityToBusinessPersonListMapper mapper = new EntityToBusinessPersonListMapper();
    PeopleList list = mapper.map(entityList);
    return list;
  }

  public Person get(Site site, Account account, int id) {
    final PersonEntity personEntity = peopleRepository.get(site, account.id(), id);
    EntityToBusinessPersonMapper mapper = new EntityToBusinessPersonMapper();
    return mapper.map(personEntity);
  }
}
