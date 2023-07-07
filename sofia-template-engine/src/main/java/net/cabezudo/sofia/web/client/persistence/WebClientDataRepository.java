package net.cabezudo.sofia.web.client.persistence;

import net.cabezudo.sofia.core.Stopwatch;
import net.cabezudo.sofia.persistence.DatabaseManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Types;
import java.util.Calendar;

@Repository
public class WebClientDataRepository {
  private static final Logger log = LoggerFactory.getLogger(WebClientDataRepository.class);
  private @Autowired DatabaseManager databaseManager;

  private @Autowired WebClientDataEntityCacheById webClientDataEntityCacheById;

  public WebClientDataEntity get(Integer id) {
    log.debug("Get the web client data for id " + id);

    Stopwatch sc = new Stopwatch().start();
    WebClientDataEntity webClientDataEntityCached = webClientDataEntityCacheById.get(id);
    log.debug("Time spent in cache: " + sc.stop().getValue());

    WebClientDataEntity webClientDataEntity;
    if (webClientDataEntityCached == null) {
      Stopwatch s = new Stopwatch().start();
      String query =
          "SELECT w.id AS id, w.language AS language, a.id AS account_id, a.site_id AS site_id, a.name AS name, w.last_update AS last_update " +
              "FROM web_client_data AS w " +
              "LEFT JOIN accounts AS a ON w.account_id = a.id " +
              "WHERE w.id = ?";
      webClientDataEntity = databaseManager.getJDBCTemplate().query(query, new WebClientDataRowMapper(), id).stream().findFirst().orElse(null);
      webClientDataEntityCacheById.put(id, webClientDataEntity);
      log.debug("Time spent in database: " + s.stop().getValue());
    } else {
      webClientDataEntity = webClientDataEntityCached;
    }
    return webClientDataEntity;
  }

  public WebClientDataEntity create(WebClientDataEntity data) {
    String sqlQuery = "INSERT INTO web_client_data (`language`, `account_id`) VALUES (?, ?)";
    KeyHolder keyHolder = new GeneratedKeyHolder();
    databaseManager.getJDBCTemplate().update(connection -> {
      PreparedStatement ps = connection.prepareStatement(sqlQuery, new String[]{"email"});
      if (data.getAccount() == null) {
        ps.setNull(1, Types.INTEGER);
      } else {
        ps.setString(1, data.getLanguage());
      }
      if (data.getAccount() == null) {
        ps.setNull(2, Types.INTEGER);
      } else {
        ps.setInt(2, data.getAccount().getId());
      }
      return ps;
    }, keyHolder);
    Integer id = keyHolder.getKey() == null ? null : keyHolder.getKey().intValue();
    Date now = new Date(Calendar.getInstance().getTime().getTime());
    WebClientDataEntity webClientDataEntity = new WebClientDataEntity(id, data.getLanguage(), data.getAccount(), now);

    webClientDataEntityCacheById.put(id, webClientDataEntity);
    return webClientDataEntity;
  }

  public void update(int id, WebClientDataEntity data) {
    log.debug("Update web client data " + data);
    String sqlQuery = "UPDATE web_client_data SET language = ?, account_id = ? WHERE id = ?";
    PreparedStatementCreator preparedStatementCreator = connection -> {
      PreparedStatement ps = connection.prepareStatement(sqlQuery);
      ps.setString(1, data.getLanguage());
      if (data.getAccount() == null) {
        ps.setNull(2, Types.INTEGER);
      } else {
        ps.setInt(2, data.getAccount().getId());
      }
      ps.setInt(3, id);
      return ps;
    };
    databaseManager.getJDBCTemplate().update(preparedStatementCreator);

    webClientDataEntityCacheById.put(data.getId(), data);
  }
}
