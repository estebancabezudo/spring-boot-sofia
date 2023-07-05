package net.cabezudo.sofia.users.persistence;

import net.cabezudo.sofia.persistence.DatabaseManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.util.List;

@Component
public class GroupsRepository {
  private static final Logger log = LoggerFactory.getLogger(GroupsRepository.class);
  private @Autowired DatabaseManager databaseManager;

  public GroupEntity create(int accountUserId, String groupName) {
    String sqlQuery = "INSERT INTO authorities (account_user_id, authority) VALUES (?, ?)";
    PreparedStatementCreator preparedStatementCreator = connection -> {
      PreparedStatement ps = connection.prepareStatement(sqlQuery);
      ps.setInt(1, accountUserId);
      ps.setString(2, groupName);
      return ps;
    };
    databaseManager.getJDBCTemplate().update(preparedStatementCreator);

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
    databaseManager.getJDBCTemplate().update(preparedStatementCreator);
  }

  public List<GroupEntity> get(int accountId, int userId) {
    log.debug("Search group for user " + userId + " for the account " + accountId);

    String query =
        "SELECT a.account_user_id AS id, authority AS name " +
            "FROM accounts_users AS au " +
            "RIGHT JOIN authorities AS a ON au.id = a.account_user_id " +
            "WHERE au.account_id = ? AND au.user_id = ?";
    return databaseManager.getJDBCTemplate().query(query, new AuthorityRowMapper(), accountId, userId);
  }
}
