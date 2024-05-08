package net.cabezudo.sofia.phones.mappers;

import net.cabezudo.sofia.phones.persistence.PhoneEntity;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PhoneRowMapper implements RowMapper<PhoneEntity> {
  public PhoneEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
    int id = rs.getInt("id");
    int countryCode = rs.getInt("country_code");
    long number = rs.getLong("number");
    return new PhoneEntity(id, countryCode, number);
  }
}
