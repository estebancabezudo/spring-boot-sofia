package net.cabezudo.sofia.emails.persistence;

import net.cabezudo.sofia.emails.mappers.EMailRowMapper;
import net.cabezudo.sofia.persistence.DatabaseManager;
import net.cabezudo.sofia.persistence.NullKeyException;
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

  private @Autowired DatabaseManager databaseManager;

  public EMailEntity get(String address) {
    return get(databaseManager.getJDBCTemplate(), address);
  }

  public EMailEntity get(JdbcTemplate jdbcTemplate, String address) {
    log.debug("Search email with address " + address);

    String query = "SELECT id, address FROM emails AS e WHERE address = ?";
    return jdbcTemplate.query(query, new EMailRowMapper(), address).stream().findFirst().orElse(null);
  }

  public EMailEntity get(Integer id) {
    return get(databaseManager.getJDBCTemplate(), id);
  }

  public EMailEntity get(JdbcTemplate jdbcTemplate, Integer id) {
    log.debug("Search email with id " + id);

    if (id == null) {
      return null;
    }

    String query = "SELECT id, address FROM emails AS e WHERE id = ?";
    return jdbcTemplate.query(query, new EMailRowMapper(), id).stream().findFirst().orElse(null);
  }

  public EMailEntity create(String address) {
    return create(databaseManager.getJDBCTemplate(), address);
  }

  public EMailEntity create(JdbcTemplate jdbcTemplate, String address) {
    String sqlQuery = "INSERT INTO emails (address) VALUES (?)";
    KeyHolder keyHolder = new GeneratedKeyHolder();
    jdbcTemplate.update(connection -> {
      PreparedStatement ps = connection.prepareStatement(sqlQuery, new String[]{"address"});
      ps.setString(1, address);
      return ps;
    }, keyHolder);
    Number key = keyHolder.getKey();
    if (key == null) {
      throw new NullKeyException();
    }
    int id = key.intValue();
    return new EMailEntity(id, address);
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
      databaseManager.getJDBCTemplate().update(preparedStatementCreator);
    } catch (DataAccessException e) {
      if (e.getCause() instanceof SQLIntegrityConstraintViolationException) {
        log.debug("I can't delete the email because is used with another user in another account.");
      } else {
        throw e;
      }
    }
  }

  public EMailEntity findOrCreate(JdbcTemplate jdbcTemplate, String eMail) {
    EMailEntity eMailEntity = this.get(jdbcTemplate, eMail);
    if (eMailEntity == null) {
      return this.create(jdbcTemplate, eMail);
    }
    return eMailEntity;
  }
}
