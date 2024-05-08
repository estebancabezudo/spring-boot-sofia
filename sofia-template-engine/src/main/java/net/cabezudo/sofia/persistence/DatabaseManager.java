package net.cabezudo.sofia.persistence;

import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Component;

@Component
public class DatabaseManager {
  @Value("${spring.datasource.url:#{null}}")
  @Nullable
  private String url;

  @Value("${spring.datasource.username:#{null}}")
  @Nullable
  private String username;

  @Value("${spring.datasource.password:#{null}}")
  @Nullable
  private String password;

  public JdbcTemplate getJDBCTemplate() {
    DriverManagerDataSource dataSource = new DriverManagerDataSource(url, username, password);
    return new JdbcTemplate(dataSource);
  }

  public NamedParameterJdbcTemplate getNamedParameterJdbcTemplate() {
    DriverManagerDataSource dataSource = new DriverManagerDataSource(url, username, password);
    return new NamedParameterJdbcTemplate(dataSource);
  }
}
