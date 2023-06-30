package net.cabezudo.sofia.people.mappers;

import net.cabezudo.sofia.people.persistence.PersonEntity;
import org.springframework.jdbc.core.RowMapper;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PersonRowMapper implements RowMapper<PersonEntity> {
  @Override
  public PersonEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
    int id = rs.getInt("id");
    String name = rs.getString("name");
    String secondName = rs.getString("second_name");
    String lastName = rs.getString("last_name");
    String secondLastName = rs.getString("second_last_name");
    Date dateOfBird = rs.getDate("date_of_birth");
    return new PersonEntity(id, name, secondName, lastName, secondLastName, dateOfBird);
  }
}
