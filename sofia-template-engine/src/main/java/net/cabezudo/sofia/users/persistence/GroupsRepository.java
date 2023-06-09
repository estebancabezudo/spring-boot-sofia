package net.cabezudo.sofia.users.persistence;

import net.cabezudo.sofia.persistence.DatabaseManager;
import net.cabezudo.sofia.sites.Site;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;

@Component
public class GroupsRepository {
  private static final Logger log = LoggerFactory.getLogger(GroupsRepository.class);
  private @Autowired DatabaseManager databaseManager;

  public GroupEntity create(Site site, int accountUserId, String groupName) {
    String sqlQuery = "INSERT INTO authorities (account_user_id, authority) VALUES (?, ?)";
    PreparedStatementCreator preparedStatementCreator = connection -> {
      PreparedStatement ps = connection.prepareStatement(sqlQuery);
      ps.setInt(1, accountUserId);
      ps.setString(2, groupName);
      return ps;
    };
    databaseManager.getJDBCTemplate(site).update(preparedStatementCreator);

    return new GroupEntity(accountUserId, groupName);
  }

  public void deleteGroupsFor(Site site, int accountUserRelationId) {
    log.debug("Delete groups for account user relation " + accountUserRelationId);
    String sqlQuery = "DELETE FROM authorities WHERE account_user_id = ?";
    PreparedStatementCreator preparedStatementCreator = connection -> {
      PreparedStatement ps = connection.prepareStatement(sqlQuery);
      ps.setInt(1, accountUserRelationId);
      return ps;
    };
    databaseManager.getJDBCTemplate(site).update(preparedStatementCreator);
  }
}
