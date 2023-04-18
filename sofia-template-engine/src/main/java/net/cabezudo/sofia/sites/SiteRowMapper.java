package net.cabezudo.sofia.sites;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SiteRowMapper implements RowMapper<SiteEntity> {
  @Override
  public SiteEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
    int id = rs.getInt("id");
    String name = rs.getString("name");
    return new SiteEntity(id, name);
  }
}
