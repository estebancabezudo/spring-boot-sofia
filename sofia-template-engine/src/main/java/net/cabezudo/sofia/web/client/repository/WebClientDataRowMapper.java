package net.cabezudo.sofia.web.client.repository;

import net.cabezudo.sofia.accounts.persistence.AccountEntity;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class WebClientDataRowMapper implements RowMapper<WebClientDataEntity> {
  @Override
  public WebClientDataEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
    String name = rs.getString("name");
    AccountEntity accountEntity;
    if (name == null) {
      accountEntity = null;
    } else {
      accountEntity = new AccountEntity(rs.getInt("account_id"), rs.getInt("site_id"), name);
    }
    return new WebClientDataEntity(rs.getInt("id"), rs.getString("language"), accountEntity, rs.getDate("last_update"));
  }
}
