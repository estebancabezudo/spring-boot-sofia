package net.cabezudo.sofia.places.persistence;

import net.cabezudo.sofia.core.persistence.EntityList;
import net.cabezudo.sofia.places.mappers.PlaceRowMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.sql.PreparedStatement;
import java.util.List;

@Repository
public class PlacesRepository {
  private static final Logger log = LoggerFactory.getLogger(PlacesRepository.class);
  private @Autowired JdbcTemplate jdbcTemplate;

  @Transactional
  public EntityList<PlaceEntity> findAll(int accountId) {
    log.debug("Search people for " + accountId);

    List<PlaceEntity> list = jdbcTemplate.query(
        "SELECT id, account_id, name, street, number, interior_number, `references`, postal_code, country_id " +
            "FROM places AS p " +
            "WHERE account_id = ?",
        new PlaceRowMapper(), accountId);

    // TODO Cambiar cuando paginación
    return new PlaceEntityList(list.size(), 0, list.size(), list);
  }

  public PlaceEntity get(int accountId, int id) {
    log.debug("Search place with id " + id);

    return jdbcTemplate.queryForObject(
        "SELECT id, account_id, name, street, number, interior_number, `references`, postal_code, country_id " +
            "FROM places AS p " +
            "WHERE account_id = ? AND id = ?",
        new PlaceRowMapper(),
        accountId, id);
  }

  public PlaceEntity create(PlaceEntity placeEntityToSave) {
    String sqlQuery =
        "INSERT INTO places (account_id, name, street, number, interior_number, `references`, postal_code, country_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    KeyHolder keyHolder = new GeneratedKeyHolder();
    jdbcTemplate.update(connection -> {
      PreparedStatement ps = connection.prepareStatement(sqlQuery, new String[]{"id"});
      ps.setInt(1, placeEntityToSave.getAccountId());
      ps.setString(2, placeEntityToSave.getName());
      ps.setString(3, placeEntityToSave.getStreet());
      ps.setString(4, placeEntityToSave.getNumber());
      ps.setString(5, placeEntityToSave.getInteriorNumber());
      ps.setString(6, placeEntityToSave.getReferences());
      ps.setString(7, placeEntityToSave.getPostalCode());
      ps.setInt(8, placeEntityToSave.getCountryId());
      return ps;
    }, keyHolder);
    int id = keyHolder.getKey().intValue();
    placeEntityToSave.setId(id);
    return new PlaceEntity(placeEntityToSave);
  }

  public PlaceEntity update(PlaceEntity placeEntityToUpdate) {
    String sqlQuery =
        "UPDATE places " +
            "SET name = ?, street = ?, number = ?, interior_number = ?, `references` = ?, postal_code = ?, country_id = ? " +
            "WHERE account_id = ? AND id = ?";
    jdbcTemplate.update(connection -> {
      PreparedStatement ps = connection.prepareStatement(sqlQuery, new String[]{"id"});
      ps.setString(1, placeEntityToUpdate.getName());
      ps.setString(2, placeEntityToUpdate.getStreet());
      ps.setString(3, placeEntityToUpdate.getNumber());
      ps.setString(4, placeEntityToUpdate.getInteriorNumber());
      ps.setString(5, placeEntityToUpdate.getReferences());
      ps.setString(6, placeEntityToUpdate.getPostalCode());
      ps.setInt(7, placeEntityToUpdate.getCountryId());
      ps.setInt(8, placeEntityToUpdate.getAccountId());
      ps.setInt(9, placeEntityToUpdate.getId());
      return ps;
    });
    return new PlaceEntity(placeEntityToUpdate);
  }

  public void delete(int accountId, int placeId) {
    String sqlQuery =
        "DELETE FROM places WHERE account_id = ? and id = ?";
    jdbcTemplate.update(connection -> {
      PreparedStatement ps = connection.prepareStatement(sqlQuery, new String[]{"id"});
      ps.setInt(1, accountId);
      ps.setInt(2, placeId);
      return ps;
    });
  }
}
