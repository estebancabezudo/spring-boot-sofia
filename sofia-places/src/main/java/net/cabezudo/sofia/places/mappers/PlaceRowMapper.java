package net.cabezudo.sofia.places.mappers;

import net.cabezudo.sofia.places.persistence.PlaceEntity;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PlaceRowMapper implements RowMapper<PlaceEntity> {
  @Override
  public PlaceEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
    int id = rs.getInt("id");
    int accountId = rs.getInt("account_id");
    String name = rs.getString("name");
    Integer streetId = rs.getObject("street_id") != null ? rs.getInt("street_id") : null;
    String number = rs.getString("number");
    String interiorNumber = rs.getString("interior_number");
    Integer cornerStreetId = rs.getObject("corner_street_id") != null ? rs.getInt("corner_street_id") : null;
    Integer firstStreetId = rs.getObject("first_street_id") != null ? rs.getInt("first_street_id") : null;
    Integer secondStreetId = rs.getObject("second_street_id") != null ? rs.getInt("second_street_id") : null;
    String references = rs.getString("references");
    Integer postalCodeId = rs.getInt("postal_code_id");
    int countryId = rs.getInt("country_id");

    return new PlaceEntity(id, accountId, name, streetId, number, interiorNumber, cornerStreetId, firstStreetId, secondStreetId, references, postalCodeId, countryId);
  }
}
