package com.cbs.center_service.configs;

import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;

@Slf4j
@Configuration
public class DatabaseConfig {

  @Autowired
  private Environment env;

  //Bean definitions for DataSources will go here
  @Bean(name = "coreDataSource")
  @Primary
  @ConfigurationProperties(prefix = "spring.datasource.coredatasource")
  @Profile("dev")
  public DataSource coreDataSource() {
    HikariDataSource dataSource = DataSourceBuilder.create()
        .type(HikariDataSource.class)
        .build();

    if (dataSource.getPoolName() == null || dataSource.getPoolName().isEmpty()) {
      dataSource.setPoolName(String.format("%s Core Connection Pool", env.getActiveProfiles()));
    }

    logPoolConfiguration("coreDataSource", dataSource);

    return dataSource;
  }

  @Bean(name = "odsDataSource")
  @ConfigurationProperties(prefix = "spring.datasource.odsdatasource")
  @Profile("dev")
  public DataSource odsDataSource() {
    return DataSourceBuilder.create()
        .type(HikariDataSource.class)
        .build();
  }

  @Bean(name = "dcpDataSource")
  @Profile("dev")
  @ConfigurationProperties(prefix = "spring.datasource.dcptradedatasourcedefault")
  public DataSource dcpDataSource() {
    return DataSourceBuilder.create()
        .type(HikariDataSource.class)
        .build();
  }

  @Bean(name = "storeDataSource")
  @Profile("dev")
  @ConfigurationProperties(prefix = "spring.datasource.storedatasourcedefault")
  public DataSource storeDataSource() {
    return DataSourceBuilder.create()
        .type(HikariDataSource.class)
        .build();
  }

  @Bean(name = "prodCoreReadDataSource")
  @Primary
  @ConfigurationProperties(prefix = "spring.datasource.coredatasource.primary")
  @Profile("prod")
  public DataSource prodCoreReadDataSource() {
    return DataSourceBuilder.create()
        .type(HikariDataSource.class)
        .build();
  }

  @Bean(name = "prodCoreExecuteDataSource")
  @ConfigurationProperties(prefix = "spring.datasource.coredatasource.secondary")
  @Profile("prod")
  public DataSource prodCoreExecuteDataSource() {
    return DataSourceBuilder.create()
        .type(HikariDataSource.class)
        .build();
  }

  @Bean(name = "prodOdsReadDataSource")
  @ConfigurationProperties(prefix = "spring.datasource.odsdatasource.primary")
  @Profile("prod")
  public DataSource prodOdsReadDataSource() {
    return DataSourceBuilder.create()
        .type(HikariDataSource.class)
        .build();
  }

  @Bean(name = "prodOdsExecuteDataSource")
  @Profile("prod")
  @ConfigurationProperties(prefix = "spring.datasource.odsdatasource.secondary")
  public DataSource prodOdsExecuteDataSource() {
    return DataSourceBuilder.create()
        .type(HikariDataSource.class)
        .build();
  }

  @Bean(name = "prodDcpDataSource")
  @Profile("prod")
  @ConfigurationProperties(prefix = "spring.datasource.dcptradedatasource")
  public DataSource prodDcpDataSource() {
    return DataSourceBuilder.create()
        .type(HikariDataSource.class)
        .build();
  }

  @Bean(name = "prodStoreDataSource")
  @Profile("prod")
  @ConfigurationProperties(prefix = "spring.datasource.storedatasource")
  public DataSource prodStoreDataSource() {
    return DataSourceBuilder.create()
        .type(HikariDataSource.class)
        .build();

  }

  private void logPoolConfiguration(String beanName, HikariDataSource dataSource) {
    log.info("===== HikariCP Configuration for {} =====", beanName);
    log.info("Pool Name: {}", dataSource.getPoolName());
    log.info("JDBC URL: {}", dataSource.getJdbcUrl());
    log.info("Maximum Pool Size: {}", dataSource.getMaximumPoolSize());
    log.info("Minimum Idle: {}", dataSource.getMinimumIdle());
    log.info("Connection Timeout: {}ms", dataSource.getConnectionTimeout());
    log.info("Idle Timeout: {}ms", dataSource.getIdleTimeout());
    log.info("Max Lifetime: {}ms", dataSource.getMaxLifetime());
    log.info("Leak Detection Threshold: {}ms", dataSource.getLeakDetectionThreshold());
    log.info("=".repeat(20));

  }

  private String maskUrl(String url) {
    if (url.equals("")) return "";
    return url.replaceAll("password[^&;]*", "password=***");
  }


}
