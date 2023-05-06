package net.cabezudo.sofia.web.client.persistence;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@Repository
public class WebClientRepository {
  private static final Logger log = LoggerFactory.getLogger(WebClientRepository.class);

  private @Autowired JdbcTemplate jdbcTemplate;

  @Transactional
  public WebClientEntity findById(Long id) {
    log.debug("Search web client using the session id " + id);

    String sqlQuery = "SELECT * FROM web_clients WHERE id = ?";
    return jdbcTemplate.query(sqlQuery, new ResultSetExtractor<WebClientEntity>() {
      @Override
      public WebClientEntity extractData(ResultSet rs) throws SQLException, DataAccessException {
        return rs.next() ?
            new WebClientEntity(rs.getInt(WebClientEntity.ID_COLUMN_NAME), rs.getString(WebClientEntity.LANGUAGE_COLUMN_NAME)) : null;
      }
    }, id);
  }

  public WebClientEntity create(WebClientEntity data) {
    KeyHolder keyHolder = new GeneratedKeyHolder();
    PreparedStatementCreator preparedStatementCreator = connection -> {
      String query = "INSERT INTO web_clients (language) values(?)";
      PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
      ps.setString(1, data.getLanguage());
      return ps;
    };
    jdbcTemplate.update(preparedStatementCreator, keyHolder);

    return new WebClientEntity(keyHolder.getKey().intValue(), data.getLanguage());
  }

  public WebClientEntity update(WebClientEntity data) {
    PreparedStatementCreator preparedStatementCreator = connection -> {
      String query = "UPDATE web_clients set language=? WHERE id=?";
      PreparedStatement ps = connection.prepareStatement(query);
      ps.setString(1, data.getLanguage());
      ps.setLong(2, data.getId());
      return ps;
    };
    jdbcTemplate.update(preparedStatementCreator);

    return new WebClientEntity(data.getId(), data.getLanguage());
  }

}