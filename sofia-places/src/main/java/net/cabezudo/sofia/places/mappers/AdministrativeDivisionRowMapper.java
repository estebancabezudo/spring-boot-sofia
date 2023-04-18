package net.cabezudo.sofia.places.mappers;

import net.cabezudo.sofia.places.persistence.AdministrativeDivisionEntity;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AdministrativeDivisionRowMapper implements RowMapper<AdministrativeDivisionEntity> {
  @Override
  public AdministrativeDivisionEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
    Integer id = rs.getInt("id");
    Integer placeId = rs.getInt("place_id");
    int typeId = rs.getInt("type_id");
    String typeName = rs.getString("type_name");
    String name = rs.getString("name");

    return new AdministrativeDivisionEntity(id, placeId, typeId, typeName, name);
  }
}