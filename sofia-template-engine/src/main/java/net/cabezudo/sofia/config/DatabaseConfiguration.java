package net.cabezudo.sofia.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
public class DatabaseConfiguration {

  private static final Logger log = LoggerFactory.getLogger(DatabaseConfiguration.class);

  @Bean
  @ConfigurationProperties(prefix = "spring.datasource")
  public DataSource mysqlDataSource() {
    DriverManagerDataSource dataSource = new DriverManagerDataSource();
    return dataSource;
  }

  @Bean
  JdbcUserDetailsManager jdbcUserDetailsManager(DataSource dataSource) {
    JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager(dataSource);
    return jdbcUserDetailsManager;
  }

  @Bean
  public PlatformTransactionManager transactionManager() {
    DataSourceTransactionManager txManager = new DataSourceTransactionManager();
    txManager.setDataSource(mysqlDataSource());
    return txManager;
  }
}
