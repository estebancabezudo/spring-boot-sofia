package net.cabezudo.sofia.users.persistence;


import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AuthorityRowMapper implements RowMapper<GroupEntity> {
  @Override
  public GroupEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
    int id = rs.getInt("id");
    String name = rs.getString("name");
    return new GroupEntity(id, name);
  }
}
