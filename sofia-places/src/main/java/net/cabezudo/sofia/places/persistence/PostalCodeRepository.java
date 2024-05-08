package net.cabezudo.sofia.places.persistence;

import net.cabezudo.sofia.core.SofiaRuntimeException;
import net.cabezudo.sofia.persistence.DatabaseManager;
import net.cabezudo.sofia.persistence.NullKeyException;
import net.cabezudo.sofia.places.PostalCode;
import net.cabezudo.sofia.places.mappers.PostalCodeRowMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.List;

@Repository
public class PostalCodeRepository {
  private static final Logger log = LoggerFactory.getLogger(PostalCodeRepository.class);
  private @Autowired DatabaseManager databaseManager;

  public PostalCodeEntity get(JdbcTemplate jdbcTemplate, Integer id) {
    log.debug("Search postal code by id: " + id);

    List<PostalCodeEntity> list = jdbcTemplate.query(
        "SELECT id, code, verified FROM postal_codes AS a WHERE id = ?",
        new PostalCodeRowMapper(), id);

    if (list.size() == 0) {
      return null;
    }
    if (list.size() == 1) {
      return list.get(0);
    }
    throw new SofiaRuntimeException("More than one register in query");
  }

  private PostalCodeEntity getByCode(String code) {
    JdbcTemplate jdbcTemplate = databaseManager.getJDBCTemplate();
    return getByCode(jdbcTemplate, code);
  }

  public PostalCodeEntity getByCode(JdbcTemplate jdbcTemplate, String code) {
    log.debug("Search postal code by code " + code);
    String query = "SELECT id, code, verified FROM postal_codes WHERE code = ?";
    return jdbcTemplate.query(query, new PostalCodeRowMapper(), code).stream().findFirst().orElse(null);
  }

  public PostalCodeEntity create(String code, boolean verified) {
    JdbcTemplate jdbcTemplate = databaseManager.getJDBCTemplate();
    return create(jdbcTemplate, code, verified);
  }

  public PostalCodeEntity create(JdbcTemplate jdbcTemplate, String code, boolean verified) {
    String sqlQuery = "INSERT INTO postal_codes (code, verified) VALUES (?, ?)";
    KeyHolder keyHolder = new GeneratedKeyHolder();
    jdbcTemplate.update(connection -> {
      PreparedStatement ps = connection.prepareStatement(sqlQuery, new String[]{"id"});
      ps.setString(1, code);
      ps.setBoolean(2, verified);
      return ps;
    }, keyHolder);
    Number key = keyHolder.getKey();
    if (key == null) {
      throw new NullKeyException();
    }
    int id = key.intValue();
    return new PostalCodeEntity(id, code, verified);
  }

  public PostalCodeEntity getByCodeOrCreate(JdbcTemplate jdbcTemplate, PostalCode postalCode) {
    if (postalCode == null || postalCode.getCode() == null) {
      return null;
    }
    PostalCodeEntity postalCodeEntity = getByCode(postalCode.getCode());
    if (postalCodeEntity == null) {
      return create(jdbcTemplate, postalCode.getCode(), postalCode.isVerified());
    }
    return postalCodeEntity;
  }
}
