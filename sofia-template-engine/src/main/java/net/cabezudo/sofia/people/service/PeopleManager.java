package net.cabezudo.sofia.people.service;

import net.cabezudo.sofia.accounts.service.Account;
import net.cabezudo.sofia.core.persistence.EntityList;
import net.cabezudo.sofia.people.PeopleList;
import net.cabezudo.sofia.people.Person;
import net.cabezudo.sofia.people.mappers.EntityToBusinessPersonListMapper;
import net.cabezudo.sofia.people.mappers.EntityToBusinessPersonMapper;
import net.cabezudo.sofia.people.persistence.PeopleRepository;
import net.cabezudo.sofia.people.persistence.PersonEntity;
import net.cabezudo.sofia.users.service.SofiaUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;

@Service
@Transactional
public class PeopleManager {
  private @Autowired PeopleRepository peopleRepository;

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

  public Person getByEMail(String email) {
    PersonEntity personEntity = peopleRepository.findByEMail(email);
    if (personEntity == null) {
      return null;
    }
    return entityToBusinessPersonMapper.map(personEntity);
  }

  public Person create(String name, String secondName, String lastName, String secondLastName, Date dateOfBirth) {
    PersonEntity personEntity = peopleRepository.create(name, secondName, lastName, secondLastName, dateOfBirth);
    return entityToBusinessPersonMapper.map(personEntity);
  }

  public void relate(SofiaUser user, Person person) {
    peopleRepository.relatePersonToUser(user.getId(), person.getId());
  }
}
