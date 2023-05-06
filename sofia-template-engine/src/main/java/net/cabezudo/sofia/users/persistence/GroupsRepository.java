package net.cabezudo.sofia.users.persistence;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;

@Component
public class GroupsRepository {
  private static final Logger log = LoggerFactory.getLogger(GroupsRepository.class);
  private @Autowired JdbcTemplate jdbcTemplate;

  public GroupEntity create(int userId, String groupName) {
    String sqlQuery = "INSERT INTO authorities (user_id, authority) VALUES (?, ?)";
    jdbcTemplate.update(connection -> {
      PreparedStatement ps = connection.prepareStatement(sqlQuery);
      ps.setInt(1, userId);
      ps.setString(2, groupName);
      return ps;
    });
    return new GroupEntity(userId, groupName);
  }

  public void deleteGroupsFor(UserEntity userEntity) {
    String sqlQuery = "DELETE FROM authorities WHERE user_id ?";
    jdbcTemplate.update(connection -> {
      PreparedStatement ps = connection.prepareStatement(sqlQuery);
      ps.setInt(1, userEntity.getId());
      return ps;
    });
  }
}
