package net.cabezudo.sofia.users.persistence;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;

@Component
public class GroupsRepository {
  private static final Logger log = LoggerFactory.getLogger(GroupsRepository.class);
  private @Autowired JdbcTemplate jdbcTemplate;

  public GroupEntity create(int accountUserId, String groupName) {
    String sqlQuery = "INSERT INTO authorities (account_user_id, authority) VALUES (?, ?)";
    PreparedStatementCreator preparedStatementCreator = connection -> {
      PreparedStatement ps = connection.prepareStatement(sqlQuery);
      ps.setInt(1, accountUserId);
      ps.setString(2, groupName);
      return ps;
    };
    jdbcTemplate.update(preparedStatementCreator);

    return new GroupEntity(accountUserId, groupName);
  }

  public void deleteGroupsFor(int accountUserRelationId) {
    log.debug("Delete groups for account user relation " + accountUserRelationId);
    String sqlQuery = "DELETE FROM authorities WHERE account_user_id = ?";
    PreparedStatementCreator preparedStatementCreator = connection -> {
      PreparedStatement ps = connection.prepareStatement(sqlQuery);
      ps.setInt(1, accountUserRelationId);
      return ps;
    };
    jdbcTemplate.update(preparedStatementCreator);
  }
}
