package net.cabezudo.sofia.emails.persistence;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;

@Component
public class EMailRepository {
  Logger log = LoggerFactory.getLogger(EMailRepository.class);

  @Autowired
  private JdbcTemplate jdbcTemplate;

  public EMailEntity create(String email) {
    String sqlQuery = "INSERT INTO emails (email) VALUES (?)";
    KeyHolder keyHolder = new GeneratedKeyHolder();
    jdbcTemplate.update(connection -> {
      PreparedStatement ps = connection.prepareStatement(sqlQuery, new String[]{"email"});
      ps.setString(1, email);
      return ps;
    }, keyHolder);
    int id = keyHolder.getKey().intValue();
    return new EMailEntity(id, email);
  }
}
