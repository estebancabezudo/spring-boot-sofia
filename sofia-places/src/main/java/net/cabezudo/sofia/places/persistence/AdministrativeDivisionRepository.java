package net.cabezudo.sofia.places.persistence;

import jakarta.transaction.Transactional;
import net.cabezudo.sofia.core.SofiaRuntimeException;
import net.cabezudo.sofia.persistence.DatabaseManager;
import net.cabezudo.sofia.places.mappers.AdministrativeDivisionRowMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class AdministrativeDivisionRepository {
  private static final Logger log = LoggerFactory.getLogger(AdministrativeDivisionRepository.class);
  private @Autowired DatabaseManager databaseManager;

  @Transactional
  public AdministrativeDivisionEntityList findAll(int placeIdToSearch) {
    log.debug("Search administrative division for place " + placeIdToSearch);

    AdministrativeDivisionEntityList list = new AdministrativeDivisionEntityList();
    databaseManager.getJDBCTemplate().queryForList(
        "SELECT a.id AS id, place_id, t.id AS type_id, t.id AS type_name_id, t.name AS type_name, n.name AS name, enabled " +
            "FROM administrative_divisions AS a " +
            "LEFT JOIN administrative_division_types as t ON a.type_id=t.id " +
            "LEFT JOIN administrative_division_names as n ON a.name_id=n.id " +
            "WHERE place_id = ?",
        new Object[]{placeIdToSearch}).forEach(rs -> {
      Integer id = (Integer) rs.get("id");
      Integer placeId = (Integer) rs.get("place_id");
      int typeId = (Integer) rs.get("type_id");
      String typeName = (String) rs.get("type_name");
      String name = (String) rs.get("name");

      AdministrativeDivisionEntity administrativeDivisionEntity = new AdministrativeDivisionEntity(id, placeId, typeId, typeName, name);
      list.add(administrativeDivisionEntity);
    });
    list.setTotal(list.size()); // The total is the same as the size because there isn't pagination.
    return list;
  }

  public AdministrativeDivisionEntity create(PlaceEntity place, AdministrativeDivisionTypeEntity type, AdministrativeDivisionNameEntity name) {
    return create(databaseManager.getJDBCTemplate(), place, type, name);
  }

  public AdministrativeDivisionEntity create(JdbcTemplate jdbcTemplate, PlaceEntity place, AdministrativeDivisionTypeEntity type, AdministrativeDivisionNameEntity name) {

    String sqlQuery = "INSERT INTO administrative_divisions (place_id, type_id, name_id) VALUES (?, ?, ?)";
    KeyHolder keyHolder = new GeneratedKeyHolder();
    jdbcTemplate.update(connection -> {
      PreparedStatement ps = connection.prepareStatement(sqlQuery, new String[]{"id"});
      ps.setInt(1, place.getId());
      ps.setInt(2, type.getId());
      ps.setInt(3, name.getId());
      return ps;
    }, keyHolder);
    // FIX Method invocation 'intValue' may produce 'NullPointerException'
    int id = keyHolder.getKey().intValue();
    return new AdministrativeDivisionEntity(id, place, type, name);
  }

  public AdministrativeDivisionEntity findByPlaceNameAndType(PlaceEntity place, AdministrativeDivisionTypeEntity type, AdministrativeDivisionNameEntity name) {
    return findByPlaceNameAndType(databaseManager.getJDBCTemplate(), place, type, name);
  }

  public AdministrativeDivisionEntity findByPlaceNameAndType(JdbcTemplate jdbcTemplate, PlaceEntity place, AdministrativeDivisionTypeEntity type, AdministrativeDivisionNameEntity name) {
    log.debug("Search administrative division with name " + name.getValue() + " and type " + type.getName() + " for place " + place.getName());

    List<AdministrativeDivisionEntity> list = jdbcTemplate.query(
        "SELECT a.id AS id, place_id, t.id AS type_id, t.name AS type_name, n.name AS name, enabled " +
            "FROM administrative_divisions AS a " +
            "LEFT JOIN administrative_division_types as t ON a.type_id=t.id " +
            "LEFT JOIN administrative_division_names as n ON a.name_id=n.id " +
            "WHERE place_id = ? AND n.name = ? AND t.id = ?",
        new AdministrativeDivisionRowMapper(),
        place.getId(), name.getValue(), type.getId());

    if (list.size() == 0) {
      return null;
    }
    if (list.size() == 1) {
      return list.get(0);
    }
    throw new SofiaRuntimeException("More than one register in query");
  }

  public AdministrativeDivisionEntityList findByPlaces(List<Integer> placeIds) {
    log.debug("Search administrative division for several places");

    if (placeIds.size() == 0) {
      ArrayList<AdministrativeDivisionEntity> list = new ArrayList<>();
      return new AdministrativeDivisionEntityList(0, 0, 0, list);
    }

    String inParams = placeIds.stream().map(id -> "?").collect(Collectors.joining(","));
    List<AdministrativeDivisionEntity> list = databaseManager.getJDBCTemplate().query(String.format(
        "SELECT a.id AS id, place_id, t.id AS type_id, t.id AS type_name_id, t.name AS type_name, n.name AS name, enabled " +
            "FROM administrative_divisions AS a " +
            "LEFT JOIN administrative_division_types as t ON a.type_id=t.id " +
            "LEFT JOIN administrative_division_names as n ON a.name_id=n.id " +
            "WHERE place_id IN (%s)", inParams), new AdministrativeDivisionRowMapper(), placeIds.toArray());
    return new AdministrativeDivisionEntityList(list.size(), 0, list.size(), list);
  }

  public void delete(int placeId) {
    String deleteQuery = "DELETE FROM administrative_divisions WHERE place_id = ?";
    databaseManager.getJDBCTemplate().update(deleteQuery, placeId);
  }

  public AdministrativeDivisionEntity findByPlaceTypeAndNameOrCreate(JdbcTemplate jdbcTemplate, PlaceEntity place, AdministrativeDivisionTypeEntity type, AdministrativeDivisionNameEntity name) {
    AdministrativeDivisionEntity administrativeDivisionTypeEntity = this.findByPlaceNameAndType(jdbcTemplate, place, type, name);
    if (administrativeDivisionTypeEntity == null) {
      return this.create(jdbcTemplate, place, type, name);
    }
    return administrativeDivisionTypeEntity;

  }
}
