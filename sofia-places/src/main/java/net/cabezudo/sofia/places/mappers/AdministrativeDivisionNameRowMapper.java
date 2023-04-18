package net.cabezudo.sofia.places.mappers;

import net.cabezudo.sofia.places.persistence.AdministrativeDivisionNameEntity;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AdministrativeDivisionNameRowMapper implements RowMapper<AdministrativeDivisionNameEntity> {
  @Override
  public AdministrativeDivisionNameEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
    Integer id = rs.getInt("id");
    String name = rs.getString("name");

    return new AdministrativeDivisionNameEntity(id, name);
  }
}
