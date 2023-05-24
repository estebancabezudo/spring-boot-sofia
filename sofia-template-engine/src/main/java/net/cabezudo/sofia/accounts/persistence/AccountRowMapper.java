package net.cabezudo.sofia.accounts.persistence;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AccountRowMapper implements RowMapper<AccountEntity> {
  @Override
  public AccountEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
    return new AccountEntity(rs.getInt("id"), rs.getInt("site_id"));
  }
}
