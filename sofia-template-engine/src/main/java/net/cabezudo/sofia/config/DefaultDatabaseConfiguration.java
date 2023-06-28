package net.cabezudo.sofia.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Configuration
public class DefaultDatabaseConfiguration {

  @Bean
  @ConditionalOnMissingBean
  public DataSource dataSource() {
    DriverManagerDataSource dataSource = new DriverManagerDataSource();
    dataSource.setDriverClassName("org.h2.Driver");
    dataSource.setUrl("jdbc:h2:mem:sofia");
    dataSource.setUsername("sa");
    dataSource.setPassword("");
    return dataSource;
  }
}

