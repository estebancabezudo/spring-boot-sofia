package net.cabezudo.sofia.places.mappers;

import net.cabezudo.sofia.places.persistence.PlaceEntity;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PlaceRowMapper implements RowMapper<PlaceEntity> {
  @Override
  public PlaceEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
    int id = rs.getInt("id");
    int siteId = rs.getInt("account_id");
    String name = rs.getString("name");
    String street = rs.getString("street");
    String number = rs.getString("number");
    String interiorNumber = rs.getString("interior_number");
    String references = rs.getString("references");
    String postalCode = rs.getString("postal_code");
    int countryId = rs.getInt("country_id");

    return new PlaceEntity(id, siteId, name, street, number, interiorNumber, references, postalCode, countryId);
  }
}
