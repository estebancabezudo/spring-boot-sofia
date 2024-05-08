package net.cabezudo.sofia.places.mappers;

import net.cabezudo.sofia.places.persistence.StreetEntity;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class StreetRowMapper implements RowMapper<StreetEntity> {
  @Override
  public StreetEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
    int id = rs.getInt("id");
    String name = rs.getString("name");
    boolean verified = rs.getBoolean("verified");

    return new StreetEntity(id, name, verified);
  }
}
