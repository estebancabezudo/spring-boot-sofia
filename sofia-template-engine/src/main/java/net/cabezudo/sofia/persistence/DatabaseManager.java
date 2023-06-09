package net.cabezudo.sofia.persistence;

import net.cabezudo.sofia.core.SofiaRuntimeException;
import net.cabezudo.sofia.sites.Site;
import org.jspecify.nullness.Nullable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
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

  public JdbcTemplate getJDBCTemplate(Site site) {
    if (site == null) {
      throw new SofiaRuntimeException("site is null");
    }
    DriverManagerDataSource dataSource = new DriverManagerDataSource(site.getDatabase().getURL(), username, password);
    return new JdbcTemplate(dataSource);
  }

  public JdbcTemplate getJDBCTemplate() {
    DriverManagerDataSource dataSource = new DriverManagerDataSource(url, username, password);
    return new JdbcTemplate(dataSource);
  }
}
