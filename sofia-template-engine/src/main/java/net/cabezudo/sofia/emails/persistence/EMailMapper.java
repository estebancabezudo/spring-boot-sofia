package net.cabezudo.sofia.emails.persistence;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class EMailMapper implements RowMapper<EMailEntity> {
  @Override
  public EMailEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
    int id = rs.getInt("id");
    String email = rs.getString("email");
    EMailEntity eMailEntity = new EMailEntity(id, email);
    return eMailEntity;
  }
}
