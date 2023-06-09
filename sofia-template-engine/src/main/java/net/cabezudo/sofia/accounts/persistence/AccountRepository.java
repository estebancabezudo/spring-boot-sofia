package net.cabezudo.sofia.accounts.persistence;

import net.cabezudo.sofia.config.DatabaseConfiguration;
import net.cabezudo.sofia.core.SofiaRuntimeException;
import net.cabezudo.sofia.core.persistence.InvalidKey;
import net.cabezudo.sofia.persistence.DatabaseManager;
import net.cabezudo.sofia.sites.Site;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Component
public class AccountRepository {
  private static final Logger log = LoggerFactory.getLogger(AccountRepository.class);
  private @Autowired DatabaseManager databaseManager;

  public AccountEntity create(Site site) {
    KeyHolder keyHolder = new GeneratedKeyHolder();
    String sqlQuery = "INSERT INTO accounts (site_id) VALUES (?)";
    PreparedStatementCreator preparedStatementCreator = connection -> {
      PreparedStatement ps = connection.prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS);
      ps.setInt(1, site.getId());
      return ps;
    };
    databaseManager.getJDBCTemplate(site).update(preparedStatementCreator, keyHolder);

    Number key = keyHolder.getKey();
    if (key == null) {
      throw new InvalidKey("key from key holder is null");
    }
    return new AccountEntity(key.intValue(), site.getId());
  }

  public AccountUserRelationEntity find(Site site, int accountId, int userId) {
    log.debug("Check if exists a relation between account " + accountId + " and user " + userId);

    List<AccountUserRelationEntity> list = databaseManager.getJDBCTemplate(site).query(
        "SELECT au.id AS id, a.id AS account_id, a.site_id AS site_id, au.owner AS owner " +
            "FROM accounts AS a " +
            "LEFT JOIN accounts_users AS au ON a.id = au.account_id " +
            "LEFT JOIN users AS u ON au.user_id = u.id " +
            "WHERE a.id = ? AND u.id = ?", new AccountUserRelationRowMapper(), accountId, userId);
    if (list.size() == 0) {
      return null;
    }
    if (list.size() > 1) {
      throw new SofiaRuntimeException("More than one relation");
    }
    return list.get(0);
  }

  public List<AccountEntity> findAll(Site site, int userId) {
    log.debug("Return all the account for the user " + userId);

    return databaseManager.getJDBCTemplate(site).query(
        "SELECT a.id AS id, a.site_id AS site_id " +
            "FROM accounts AS a " +
            "LEFT JOIN accounts_users AS au ON a.id = au.account_id " +
            "LEFT JOIN users AS u ON au.user_id = u.id " +
            "WHERE u.id = ?",
        new AccountRowMapper(), userId);
  }

  public AccountUserRelationEntity create(Site site, int accountId, int userId, boolean owner) {
    KeyHolder keyHolder = new GeneratedKeyHolder();
    String sqlQuery = "INSERT INTO accounts_users (account_id, user_id, owner) VALUES (?, ?, ?)";
    PreparedStatementCreator preparedStatementCreator = connection -> {
      PreparedStatement ps = connection.prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS);
      ps.setInt(1, accountId);
      ps.setInt(2, userId);
      ps.setBoolean(3, owner);
      return ps;
    };
    databaseManager.getJDBCTemplate(site).update(preparedStatementCreator, keyHolder);
    Number key = keyHolder.getKey();
    if (key == null) {
      throw new InvalidKey("key from key holder is null");
    }
    return new AccountUserRelationEntity(key.intValue(), accountId, userId, owner);
  }

  public void delete(Site site, int accountId, int userId) {
    String sqlQuery = "DELETE FROM accounts_users WHERE account_id = ? AND user_id = ?";
    databaseManager.getJDBCTemplate(site).update(sqlQuery, accountId, userId);
  }

  public AccountEntity getAccountFor(Site site, String email) {
    log.debug("Get the account owned by the user " + email);
    String sqlQuery =
        "SELECT a.id AS id, a.site_id AS site_id " +
            "FROM accounts AS a " +
            "LEFT JOIN accounts_users AS au ON a.id = au.account_id " +
            "LEFT JOIN users AS u ON au.user_id = u.id " +
            "LEFT JOIN `" + DatabaseConfiguration.DEFAULT_SCHEMA + "`.emails AS e ON u.email_id = e.id " +
            "WHERE owner = true AND e.email = ?";

    List<AccountEntity> list = databaseManager.getJDBCTemplate(site).query(sqlQuery, new AccountRowMapper(), email);
    if (list.size() == 0) {
      return null;
    }
    if (list.size() == 1) {
      return list.get(0);
    }
    throw new RuntimeException("An user only can own one account");
  }

  public AccountEntity get(Site site, int id) {
    log.debug("Get the account with the id " + id);

    String sqlQuery =
        "SELECT a.id AS id, a.site_id AS site_id " +
            "FROM accounts AS a " +
            "LEFT JOIN accounts_users AS au ON a.id = au.account_id " +
            "LEFT JOIN users AS u ON au.user_id = u.id " +
            "LEFT JOIN `" + DatabaseConfiguration.DEFAULT_SCHEMA + "`.emails AS e ON u.email_id = e.id " +
            "WHERE owner = true AND a.id = ?";

    return databaseManager.getJDBCTemplate(site).queryForObject(sqlQuery, new AccountRowMapper(), id);
  }
}
