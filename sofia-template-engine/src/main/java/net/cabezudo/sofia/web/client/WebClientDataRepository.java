package net.cabezudo.sofia.web.client;

import net.cabezudo.sofia.persistence.DatabaseManager;
import net.cabezudo.sofia.web.client.repository.WebClientDataEntity;
import net.cabezudo.sofia.web.client.repository.WebClientDataRowMapper;
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

  public WebClientDataEntity get(Integer id) {
    log.debug("Get the web client data for id " + id);

    String query =
        "SELECT w.id AS id, w.language AS language, a.id AS account_id, a.site_id AS site_id, a.name AS name, w.last_update AS last_update " +
            "FROM web_client_data AS w " +
            "LEFT JOIN accounts AS a ON w.account_id = a.id " +
            "WHERE w.id = ?";
    return databaseManager.getJDBCTemplate().query(query, new WebClientDataRowMapper(), id).stream().findFirst().orElse(null);

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
    return new WebClientDataEntity(id, data.getLanguage(), data.getAccount(), now);
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
  }
}
