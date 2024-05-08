package net.cabezudo.sofia.language;

import net.cabezudo.sofia.language.persistence.LanguageEntity;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class LanguageRowMapper implements RowMapper<LanguageEntity> {
  public LanguageEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
    int id = rs.getInt("id");
    String code = rs.getString("code");
    return new LanguageEntity(id, code);
  }
}
