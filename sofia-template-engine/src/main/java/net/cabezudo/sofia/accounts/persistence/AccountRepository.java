package net.cabezudo.sofia.accounts.persistence;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public class AccountRepository {
  private static final Logger log = LoggerFactory.getLogger(AccountRepository.class);
  private @Autowired JdbcTemplate jdbcTemplate;

  public AccountEntity create(int siteId) {
    KeyHolder keyHolder = new GeneratedKeyHolder();
    String sqlQuery = "INSERT INTO accounts (site_id) VALUES (?)";
    PreparedStatementCreator preparedStatementCreator = connection -> {
      PreparedStatement ps = connection.prepareStatement(sqlQuery);
      ps.setInt(1, siteId);
      return ps;
    };
    jdbcTemplate.update(preparedStatementCreator, keyHolder);

    int id = keyHolder.getKey().intValue();
    return new AccountEntity(id, siteId);
  }

  public List<Integer> find(int accountId, int userId) {
    log.debug("Check if exists a relation between account " + accountId + " and user " + userId);

    List<Integer> list = jdbcTemplate.query(
        "SELECT account_id FROM users WHERE account_id = ? AND user_id = ?",
        new RowMapper<Integer>() {
          public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
            return rs.getInt(1);
          }
        }, accountId, userId);

    return list;
  }

  public List<AccountEntity> findAll(int userId) {
    log.debug("Return all the account for the user " + userId);

    List<AccountEntity> list = jdbcTemplate.query(
        "SELECT account_id, user_id FROM accounts_users WHERE user_id = ?",
        new AccountRowMapper(), userId);
    return list;
  }

  public void delete(int accountId, int userId) {
    log.debug("Delete account user relation between account " + accountId + " and user " + userId);

    String sqlQuery = "DELETE FROM accounts_users WHERE account_id = ? AND user_id =?";
    PreparedStatementCreator preparedStatementCreator = connection -> {
      PreparedStatement ps = connection.prepareStatement(sqlQuery);
      ps.setInt(1, accountId);
      ps.setInt(2, userId);
      return ps;
    };
    jdbcTemplate.update(preparedStatementCreator);
  }
}
