package net.cabezudo.sofia.people;

import net.cabezudo.sofia.core.persistence.EntityList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.sql.Date;
import java.util.List;

@Repository
public class PeopleRepository {
  Logger log = LoggerFactory.getLogger(PeopleRepository.class);
  @Autowired
  private JdbcTemplate jdbcTemplate;

  @Transactional
  public EntityList<PersonEntity> findAll(int accountId) {
    log.debug("Search people for " + accountId);

    EntityList<PersonEntity> list = new EntityList<>();
    jdbcTemplate.queryForList(
        "SELECT p.id AS id, name, second_name, last_name, second_last_name, date_of_birth " +
            "FROM sites_people AS s " +
            "LEFT JOIN people AS p ON s.person_id = p.id " +
            "WHERE s.account_id = ?",
        new Object[]{accountId}).forEach(rs -> {
      int id = (Integer) rs.get("id");
      String name = (String) rs.get("name");
      String secondName = (String) rs.get("name");
      String lastName = (String) rs.get("name");
      String secondLastName = (String) rs.get("name");
      Date dateOfBirth = (Date) rs.get("date_of_birth");
      PersonEntity personEntity = new PersonEntity(id, name, secondName, lastName, secondLastName, dateOfBirth);
      list.add(personEntity);
    });
    list.setTotal(list.size()); // The total is the same as the size because there isn't pagination.
    return list;
  }

  public PersonEntity get(int accountId, int id) {
    log.debug("Search person with id " + id);

    return jdbcTemplate.queryForObject(
        "SELECT p.id AS id, name, second_name, last_name, second_last_name, date_of_birth " +
            "FROM sites_people AS s " +
            "LEFT JOIN people AS p ON s.person_id = p.id" +
            "WHERE account_id = ? AND p.id = ?",
        new PersonRowMapper(), accountId, id);
  }

  public List<PersonEntity> findAllUsing(String searchCriteria) {
    log.debug("Search person using " + searchCriteria);

    return jdbcTemplate.query(
        "SELECT p.id AS id, name, second_name, last_name, second_last_name, date_of_birth " +
            "FROM sites_people AS s " +
            "LEFT JOIN people AS p ON s.person_id = p.id " +
            "WHERE p.id = ?",
        ps -> ps.setString(1, searchCriteria), new PersonRowMapper());
  }
}

