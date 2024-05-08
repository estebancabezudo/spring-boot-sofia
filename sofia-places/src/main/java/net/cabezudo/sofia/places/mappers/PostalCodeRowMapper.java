package net.cabezudo.sofia.places.mappers;

import net.cabezudo.sofia.places.persistence.PostalCodeEntity;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PostalCodeRowMapper implements RowMapper<PostalCodeEntity> {
  @Override
  public PostalCodeEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
    int id = rs.getInt("id");
    String code = rs.getString("code");
    boolean verified = rs.getBoolean("verified");

    return new PostalCodeEntity(id, code, verified);
  }
}
