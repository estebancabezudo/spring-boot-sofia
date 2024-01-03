package net.cabezudo.sofia.accounts.mappers;

import net.cabezudo.sofia.accounts.persistence.AccountUserRelationEntity;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AccountUserRelationRowMapper implements RowMapper<AccountUserRelationEntity> {
  @Override
  public AccountUserRelationEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
    return new AccountUserRelationEntity(
        rs.getInt("id"),
        rs.getInt("account_id"),
        rs.getInt("site_id"),
        rs.getBoolean("owner")
    );
  }
}
