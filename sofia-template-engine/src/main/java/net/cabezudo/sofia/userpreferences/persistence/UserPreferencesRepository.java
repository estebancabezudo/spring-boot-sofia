package net.cabezudo.sofia.userpreferences.persistence;

import net.cabezudo.sofia.accounts.persistence.AccountRepository;
import net.cabezudo.sofia.core.SofiaRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Repository
public class UserPreferencesRepository {
  private static final Logger log = LoggerFactory.getLogger(AccountRepository.class);
  private @Autowired JdbcTemplate jdbcTemplate;

  public String get(int userId, String name) {
    log.debug("Get preference " + name + " for user " + userId);

    List<String> list = jdbcTemplate.query(
        "SELECT value FROM user_preferences WHERE user_id = ? AND `name` = ?",
        (resultSet, rowNum) -> resultSet.getString("value")
        , userId, name);
    if (list.size() == 0) {
      return null;
    }
    if (list.size() > 1) {
      throw new SofiaRuntimeException("More than one value");
    }
    return list.get(0);
  }

  public void update(int userId, String name, String value) {
    PreparedStatementCreator preparedStatementCreator = connection -> {
      String query = "UPDATE user_preferences SET value = ? WHERE user_id = ? AND name = ?";
      PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
      ps.setString(1, value);
      ps.setInt(2, userId);
      ps.setString(3, name);
      return ps;
    };
    jdbcTemplate.update(preparedStatementCreator);
  }

  public void create(int userId, String name, String value) {
    PreparedStatementCreator preparedStatementCreator = connection -> {
      String query = "INSERT INTO user_preferences (user_id, name, value) VALUES (?, ?, ?)";
      PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
      ps.setInt(1, userId);
      ps.setString(2, name);
      ps.setString(3, value);
      return ps;
    };
    jdbcTemplate.update(preparedStatementCreator);
  }
}
