package net.cabezudo.sofia.people.persistence;

import net.cabezudo.sofia.core.persistence.EntityList;
import net.cabezudo.sofia.people.mappers.PersonRowMapper;
import net.cabezudo.sofia.persistence.DatabaseManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Objects;

@Repository
public class PeopleRepository {
  private static final Logger log = LoggerFactory.getLogger(PeopleRepository.class);
  private @Autowired DatabaseManager databaseManager;

  @Transactional
  public EntityList<PersonEntity> findAll(int accountId) {
    log.debug("Search people for " + accountId);

    EntityList<PersonEntity> list = new EntityList<>();
    databaseManager.getJDBCTemplate().queryForList(
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

    String query =
        "SELECT p.id AS id, name, second_name, last_name, second_last_name, date_of_birth " +
            "FROM sites_people AS s " +
            "LEFT JOIN people AS p ON s.person_id = p.id" +
            "WHERE account_id = ? AND p.id = ?";
    return databaseManager.getJDBCTemplate().query(query, new PersonRowMapper(), accountId, id).stream().findFirst().orElse(null);
  }

  public List<PersonEntity> findAllUsing(String searchCriteria) {
    log.debug("Search person using " + searchCriteria);

    return databaseManager.getJDBCTemplate().query(
        "SELECT p.id AS id, name, second_name, last_name, second_last_name, date_of_birth " +
            "FROM sites_people AS s " +
            "LEFT JOIN people AS p ON s.person_id = p.id " +
            "WHERE p.id = ?",
        ps -> ps.setString(1, searchCriteria), new PersonRowMapper());
  }

  public PersonEntity findByUser(int id) {
    log.debug("Search person with user id " + id);

    String query =
        "SELECT p.id AS id, name, second_name, last_name, second_last_name, date_of_birth " +
            "FROM users_people AS up " +
            "LEFT JOIN people AS p ON up.person_id = p.id " +
            "WHERE user_id = ?";
    return databaseManager.getJDBCTemplate().query(query, new PersonRowMapper(), id).stream().findFirst().orElse(null);
  }

  public PersonEntity findByEMail(String email) {
    log.debug("Search person by email " + email);

    String query =
        "SELECT p.id AS id, name, second_name, last_name, second_last_name, date_of_birth " +
            "FROM users AS u " +
            "LEFT JOIN emails AS e ON u.email_id = e.id " +
            "LEFT JOIN users_people AS up ON u.id = up.user_id " +
            "RIGHT JOIN people AS p ON up.person_id = p.id " +
            "WHERE email = ?";
    return databaseManager.getJDBCTemplate().query(query, new PersonRowMapper(), email).stream().findFirst().orElse(null);
  }

  public PersonEntity create(String name, String secondName, String lastName, String secondLastName, Date dateOfBird) {
    KeyHolder keyHolder = new GeneratedKeyHolder();
    PreparedStatementCreator preparedStatementCreator = connection -> {
      String query = "INSERT INTO `people` (name, second_name, last_name, second_last_name, date_of_birth) VALUES (?, ?, ?, ?, ?)";
      PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
      ps.setString(1, name);
      ps.setString(2, secondName);
      ps.setString(3, lastName);
      ps.setString(4, secondLastName);
      ps.setDate(5, dateOfBird);

      return ps;
    };
    databaseManager.getJDBCTemplate().update(preparedStatementCreator, keyHolder);
    return new PersonEntity(Objects.requireNonNull(keyHolder.getKey()).intValue(), name, secondName, lastName, secondLastName, dateOfBird);
  }

  public void relatePersonToUser(int userId, int personId) {
    PreparedStatementCreator preparedStatementCreator = connection -> {
      String query = "INSERT INTO `users_people` (user_id, person_id) VALUES (?, ?)";
      PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
      ps.setInt(1, userId);
      ps.setInt(2, personId);

      return ps;
    };
    databaseManager.getJDBCTemplate().update(preparedStatementCreator);
  }

  public void deletePersonUserRelation(int userId) {
    PreparedStatementCreator preparedStatementCreator = connection -> {
      String query = "DELETE FROM `users_people` WHERE user_id = ?";
      PreparedStatement ps = connection.prepareStatement(query);
      ps.setInt(1, userId);
      return ps;
    };
    databaseManager.getJDBCTemplate().update(preparedStatementCreator);
  }
}

