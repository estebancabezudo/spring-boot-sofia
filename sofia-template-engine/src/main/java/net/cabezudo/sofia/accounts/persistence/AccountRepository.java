package net.cabezudo.sofia.accounts.persistence;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AccountRepository {
  private static final Logger log = LoggerFactory.getLogger(AccountRepository.class);
  private @Autowired JdbcTemplate jdbcTemplate;


  public List<AccountUserRelation> find(int accountId, int userId) {
    log.debug("Check if exists a relation between account " + accountId + " and user " + userId);

    List<AccountUserRelation> list = jdbcTemplate.query(
        "SELECT account_id, user_id FROM accounts_users WHERE account_id = ? AND user_id = ?",
        new AccountUserRelationRowMapper(), accountId, userId);

    return list;
  }

  public List<AccountUserRelation> findAll(int userId) {
    log.debug("Return all the account for the user " + userId);

    List<AccountUserRelation> list = jdbcTemplate.query(
        "SELECT account_id, user_id FROM accounts_users WHERE user_id = ?",
        new AccountUserRelationRowMapper(), userId);
    return list;
  }
}
