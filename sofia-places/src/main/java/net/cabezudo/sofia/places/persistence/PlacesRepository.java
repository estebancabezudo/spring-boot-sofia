package net.cabezudo.sofia.places.persistence;

import jakarta.transaction.Transactional;
import net.cabezudo.sofia.core.persistence.EntityList;
import net.cabezudo.sofia.persistence.DatabaseManager;
import net.cabezudo.sofia.places.mappers.PlaceRowMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

@Repository
public class PlacesRepository {
  private static final Logger log = LoggerFactory.getLogger(PlacesRepository.class);
  private @Autowired DatabaseManager databaseManager;

  @Transactional
  public EntityList<PlaceEntity> findAll(int accountId) {
    log.debug("Search people for " + accountId);

    List<PlaceEntity> list = databaseManager.getJDBCTemplate().query(
        """
            SELECT
              id, account_id, name, street_id, number, interior_number,
              corner_street_id, first_street_id, second_street_id,
              `references`, postal_code, country_id
            FROM places AS p
            WHERE account_id = ?
            """,
        new PlaceRowMapper(), accountId);

    // TODO Cambiar cuando paginación
    return new PlaceEntityList(list.size(), 0, list.size(), list);
  }

  public PlaceEntity get(int id) {
    return get(databaseManager.getJDBCTemplate(), id);
  }

  public PlaceEntity get(JdbcTemplate jdbcTemplate, Integer id) {
    log.debug("Search place with id " + id);

    String query =
        """
            SELECT
              id, account_id, name, street_id, number, interior_number,
              corner_street_id, first_street_id, second_street_id,
              `references`, postal_code_id, country_id
            FROM places
            WHERE id = ?
            """;
    return jdbcTemplate.query(query, new PlaceRowMapper(), id).stream().findFirst().orElse(null);
  }

  public PlaceEntity create(JdbcTemplate jdbcTemplate, int accountId, String name, Integer streetId, String number, String interiorNumber,
                            Integer cornerStreetId, Integer firstStreetId, Integer secondStreetId, String references, Integer postalCodeId, int countryId) {
    String sqlQuery = """
        INSERT INTO places (
          account_id, name, street_id, number, interior_number, corner_street_id, first_street_id, second_street_id, `references`, postal_code_id, country_id
        )
        VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;
    KeyHolder keyHolder = new GeneratedKeyHolder();
    jdbcTemplate.update(connection -> {
      PreparedStatement ps = connection.prepareStatement(sqlQuery, new String[]{"id"});
      ps.setInt(1, accountId);
      ps.setString(2, name);
      setIntOrNullFor(ps, 3, streetId);
      ps.setString(4, number);
      ps.setString(5, interiorNumber);
      setIntOrNullFor(ps, 6, cornerStreetId);
      setIntOrNullFor(ps, 7, firstStreetId);
      setIntOrNullFor(ps, 8, secondStreetId);
      ps.setString(9, references);
      ps.setInt(10, postalCodeId);
      ps.setInt(11, countryId);
      return ps;
    }, keyHolder);
    int id = keyHolder.getKey().intValue();
    return new PlaceEntity(id, accountId, name, streetId, number, interiorNumber, cornerStreetId, firstStreetId, secondStreetId, references, postalCodeId, countryId);
  }

  private void setIntOrNullFor(PreparedStatement ps, int i, Integer integer) throws SQLException {
    if (integer != null) {
      ps.setInt(i, integer);
    } else {
      ps.setNull(i, Types.INTEGER);
    }
  }

  public PlaceEntity update(PlaceEntity placeEntityToUpdate) {
    String sqlQuery = """
            UPDATE place
            SET
              name = ?, street_id = ?, number = ?, interior_number = ?, corner_street_id = ?,
              first_street_id = ?, second_street_id = ?, `references` = ?, postal_code = ?, country_id = ?
            WHERE account_id = ? AND id = ?
        """;
    databaseManager.getJDBCTemplate().update(connection -> {
      PreparedStatement ps = connection.prepareStatement(sqlQuery, new String[]{"id"});
      ps.setString(1, placeEntityToUpdate.getName());
      setIntOrNullFor(ps, 2, placeEntityToUpdate.getStreetId());
      ps.setString(3, placeEntityToUpdate.getNumber());
      ps.setString(4, placeEntityToUpdate.getInteriorNumber());
      setIntOrNullFor(ps, 5, placeEntityToUpdate.getCornerStreetId());
      setIntOrNullFor(ps, 6, placeEntityToUpdate.getFirstStreetId());
      setIntOrNullFor(ps, 7, placeEntityToUpdate.getSecondStreetId());
      ps.setString(8, placeEntityToUpdate.getReferences());
      ps.setInt(9, placeEntityToUpdate.getPostalCodeId());
      ps.setInt(10, placeEntityToUpdate.getCountryId());
      ps.setInt(11, placeEntityToUpdate.getAccountId());
      ps.setInt(12, placeEntityToUpdate.getId());
      return ps;
    });
    return new PlaceEntity(
        placeEntityToUpdate.getId(), placeEntityToUpdate.getAccountId(), placeEntityToUpdate.getName(), placeEntityToUpdate.getStreetId(), placeEntityToUpdate.getNumber(),
        placeEntityToUpdate.getInteriorNumber(), placeEntityToUpdate.getCornerStreetId(), placeEntityToUpdate.getFirstStreetId(), placeEntityToUpdate.getSecondStreetId(),
        placeEntityToUpdate.getReferences(), placeEntityToUpdate.getPostalCodeId(), placeEntityToUpdate.getCountryId());
  }

  public void delete(int accountId, int placeId) {
    String sqlQuery =
        "DELETE FROM places WHERE account_id = ? and id = ?";
    databaseManager.getJDBCTemplate().update(connection -> {
      PreparedStatement ps = connection.prepareStatement(sqlQuery, new String[]{"id"});
      ps.setInt(1, accountId);
      ps.setInt(2, placeId);
      return ps;
    });
  }

  public PlaceEntity findOrCreate(JdbcTemplate jdbcTemplate, int accountId, PlaceEntity placeEntityToFindOrCreate) {
    String name = placeEntityToFindOrCreate.getName();
    Integer streetId = placeEntityToFindOrCreate.getStreetId();
    String number = placeEntityToFindOrCreate.getNumber();
    String interiorNumber = placeEntityToFindOrCreate.getInteriorNumber();
    Integer cornerStreetId = placeEntityToFindOrCreate.getCornerStreetId();
    Integer firstStreetId = placeEntityToFindOrCreate.getFirstStreetId();
    Integer secondStreetId = placeEntityToFindOrCreate.getSecondStreetId();
    String references = placeEntityToFindOrCreate.getReferences();
    Integer postalCodeId = placeEntityToFindOrCreate.getPostalCodeId();
    int countryId = placeEntityToFindOrCreate.getCountryId();

    PlaceEntity placeEntity = this.get(jdbcTemplate, accountId, name, streetId, number, interiorNumber);
    if (placeEntity == null) {
      return this.create(jdbcTemplate, accountId, name, streetId, number, interiorNumber, cornerStreetId, firstStreetId, secondStreetId, references, postalCodeId, countryId);
    }
    return placeEntity;
  }

  private PlaceEntity get(JdbcTemplate jdbcTemplate, int accountId, String name, Integer streetId, String number, String interiorNumber) {
    return null;
  }
}
