package net.cabezudo.sofia.emails.persistence;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.sql.SQLIntegrityConstraintViolationException;

@Component
public class EMailRepository {
  private static final Logger log = LoggerFactory.getLogger(EMailRepository.class);

  private @Autowired JdbcTemplate jdbcTemplate;

  public EMailEntity get(String address) {
    log.debug("Search email with address " + address);

    String query = "SELECT id, email FROM emails AS e WHERE email = ?";
    return jdbcTemplate.query(query, new EMailMapper(), address).stream().findFirst().orElse(null);
  }


  public EMailEntity get(int id) {
    log.debug("Search email with id " + id);

    String query = "SELECT id, email FROM emails AS e WHERE id = ?";
    return jdbcTemplate.query(query, new EMailMapper(), id).stream().findFirst().orElse(null);
  }

  public EMailEntity create(String email) {
    String sqlQuery = "INSERT INTO emails (email) VALUES (?)";
    KeyHolder keyHolder = new GeneratedKeyHolder();
    jdbcTemplate.update(connection -> {
      PreparedStatement ps = connection.prepareStatement(sqlQuery, new String[]{"email"});
      ps.setString(1, email);
      return ps;
    }, keyHolder);
    int id = keyHolder.getKey().intValue();
    return new EMailEntity(id, email);
  }

  public void delete(int id) {
    log.debug("Delete email with id " + id);
    String sqlQuery = "DELETE FROM emails WHERE id = ?";
    PreparedStatementCreator preparedStatementCreator = connection -> {
      PreparedStatement ps = connection.prepareStatement(sqlQuery);
      ps.setInt(1, id);
      return ps;
    };
    try {
      jdbcTemplate.update(preparedStatementCreator);
    } catch (DataAccessException e) {
      if (e.getCause() instanceof SQLIntegrityConstraintViolationException) {
        log.debug("I can't delete the email because is used with another user in another account.");
      } else {
        throw e;
      }
    }
  }
}
