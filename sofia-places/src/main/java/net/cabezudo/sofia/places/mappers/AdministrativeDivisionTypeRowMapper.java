package net.cabezudo.sofia.places.mappers;

import net.cabezudo.sofia.places.persistence.AdministrativeDivisionTypeEntity;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AdministrativeDivisionTypeRowMapper implements RowMapper<AdministrativeDivisionTypeEntity> {
  @Override
  public AdministrativeDivisionTypeEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
    int id = rs.getInt("id");
    String name = rs.getString("name");

    return new AdministrativeDivisionTypeEntity(id, name);
  }
}
