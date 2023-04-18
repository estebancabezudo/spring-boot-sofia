package net.cabezudo.sofia.accounts.persistence;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AccountUserRelationRowMapper implements RowMapper<AccountUserRelation> {
  @Override
  public AccountUserRelation mapRow(ResultSet rs, int rowNum) throws SQLException {
    return new AccountUserRelation(rs.getInt("account_id"), rs.getInt("user_id"));
  }
}
