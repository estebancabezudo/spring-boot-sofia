package net.cabezudo.sofia.places.persistence;

import net.cabezudo.sofia.core.SofiaRuntimeException;
import net.cabezudo.sofia.persistence.DatabaseManager;
import net.cabezudo.sofia.places.mappers.AdministrativeDivisionRowMapper;
import net.cabezudo.sofia.sites.Site;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class AdministrativeDivisionRepository {
  private static final Logger log = LoggerFactory.getLogger(AdministrativeDivisionRepository.class);
  private @Autowired DatabaseManager databaseManager;

  @Transactional
  public AdministrativeDivisionEntityList findAll(Site site, int placeIdToSearch) {
    AdministrativeDivisionEntityList list = new AdministrativeDivisionEntityList();

    log.debug("Search administrative division for place " + placeIdToSearch);
    databaseManager.getJDBCTemplate(site).queryForList(
        "SELECT a.id AS id, place_id, t.id AS type_id, t.id AS type_name_id, t.name AS type_name, n.name AS name, enabled " +
            "FROM administrative_divisions AS a " +
            "LEFT JOIN administrative_division_types as t ON a.type_id=t.id " +
            "LEFT JOIN administrative_division_names as n ON a.name_id=n.id " +
            "WHERE place_id=?",
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

  public AdministrativeDivisionEntity create(
      Site site, PlaceEntity place, AdministrativeDivisionTypeEntity type, AdministrativeDivisionNameEntity name) {

    String sqlQuery = "INSERT INTO administrative_divisions (place_id, type_id, name_id) VALUES (?, ?, ?)";
    KeyHolder keyHolder = new GeneratedKeyHolder();
    databaseManager.getJDBCTemplate(site).update(connection -> {
      PreparedStatement ps = connection.prepareStatement(sqlQuery, new String[]{"id"});
      ps.setInt(1, place.getId());
      ps.setInt(2, type.getId());
      ps.setInt(3, name.getId());
      return ps;
    }, keyHolder);
    int id = keyHolder.getKey().intValue();
    return new AdministrativeDivisionEntity(id, place, type, name);
  }

  public AdministrativeDivisionEntity findByPlaceNameAndType(Site site, PlaceEntity place, String name, AdministrativeDivisionTypeEntity type) {
    log.debug("Search administrative division with name " + name + " and type " + type + " for place " + place);

    List<AdministrativeDivisionEntity> list = databaseManager.getJDBCTemplate(site).query(
        "SELECT a.id AS id, place_id, t.id AS type_id, t.name AS type_name, n.name AS name, enabled " +
            "FROM administrative_divisions AS a " +
            "LEFT JOIN administrative_division_types as t ON a.type_id=t.id " +
            "LEFT JOIN administrative_division_names as n ON a.name_id=n.id " +
            "WHERE place_id=? AND n.name = ? AND t.id = ?",
        new AdministrativeDivisionRowMapper(),
        place.getId(), name, type.getId());

    if (list.size() == 0) {
      return null;
    }
    if (list.size() == 1) {
      return list.get(0);
    }
    throw new SofiaRuntimeException("More than one register in query");
  }

  public AdministrativeDivisionEntityList findByPlaces(Site site, List<Integer> placeIds) {
    log.debug("Search administrative division for several places");

    if (placeIds.size() == 0) {
      ArrayList<AdministrativeDivisionEntity> list = new ArrayList();
      return new AdministrativeDivisionEntityList(list.size(), 0, list.size(), list);
    }

    String inParams = String.join(",", placeIds.stream().map(id -> "?").collect(Collectors.toList()));
    List<AdministrativeDivisionEntity> list = databaseManager.getJDBCTemplate(site).query(String.format(
        "SELECT a.id AS id, place_id, t.id AS type_id, t.id AS type_name_id, t.name AS type_name, n.name AS name, enabled " +
            "FROM administrative_divisions AS a " +
            "LEFT JOIN administrative_division_types as t ON a.type_id=t.id " +
            "LEFT JOIN administrative_division_names as n ON a.name_id=n.id " +
            "WHERE place_id IN (%s)", inParams), new AdministrativeDivisionRowMapper(), placeIds.toArray());
    return new AdministrativeDivisionEntityList(list.size(), 0, list.size(), list);
  }

  public void delete(Site site, int placeId) {
    String deleteQuery = "DELETE FROM administrative_divisions WHERE place_id=?";
    databaseManager.getJDBCTemplate(site).update(deleteQuery, placeId);
  }
}
