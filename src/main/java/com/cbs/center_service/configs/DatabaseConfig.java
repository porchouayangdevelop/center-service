package com.cbs.center_service.configs;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

import javax.sql.DataSource;

@Configuration
public class DatabaseConfig {

  //UAT DataSource Properties

  // UAT/core DataSource
//  @Value("${spring.datasource.coredatasource.jdbc-url}")
  private String coreUrl;

  //  @Value("${spring.datasource.coredatasource.username}")
  private String coreUsername;

  //  @Value("${spring.datasource.coredatasource.password}")
  private String corePassword;

  // UAT/ods DataSource
//  @Value("${spring.datasource.odsdatasource.jdbc-url}")
  private String odsUrl;

  //  @Value("${spring.datasource.odsdatasource.username}")
  private String odsUsername;

  //  @Value("${spring.datasource.odsdatasource.password}")
  private String odsPassword;

  // UAT/dcp DataSource
//  @Value("${spring.datasource.dcptradedatasource.jdbc-url}")
  private String dcpUrl;

  //  @Value("${spring.datasource.dcptradedatasource.username}")
  private String dcpUsername;

  //  @Value("${spring.datasource.dcptradedatasource.password}")
  private String dcpPassword;

  //PROD DataSource Properties
  // PROD/core read only DataSource
//  @Value("${spring.datasource.coredatasource.primary.jdbc-url}")
  private String prodCoreReadUrl;

  //  @Value("${spring.datasource.coredatasource.primary.username}")
  private String prodCoreReadUsername;

  //  @Value("${spring.datasource.coredatasource.primary.password}")
  private String prodCoreReadPassword;

  // PROD/core execute DataSource
//  @Value("${spring.datasource.coredatasource.secondary.jdbc-url}")
  private String prodCoreExecuteUrl;

  //  @Value("${spring.datasource.coredatasource.secondary.username}")
  private String prodCoreExecuteUsername;

  //  @Value("${spring.datasource.coredatasource.secondary.password}")
  private String prodCoreExecutePassword;

  // PROD/ods read only DataSource
//  @Value("${spring.datasource.odsdatasource.primary.jdbc-url}")
  private String prodOdsReadUrl;

  //  @Value("${spring.datasource.odsdatasource.primary.username}")
  private String prodOdsReadUsername;

  //  @Value("${spring.datasource.odsdatasource.primary.password}")
  private String prodOdsReadPassword;

  // PROD/ods execute DataSource
//  @Value("${spring.datasource.odsdatasource.secondary.jdbc-url}")
//  private String prodOdsExecuteUrl;
//
//  @Value("${spring.datasource.odsdatasource.secondary.username}")
//  private String prodOdsExecuteUsername;
//
//  @Value("${spring.datasource.odsdatasource.secondary.password}")
//  private String prodOdsExecutePassword;

  // PROD/dcp DataSource
//  @Value("${spring.datasource.dcptradedatasource.jdbc-url}")
  private String prodDcpUrl;

  //  @Value("${spring.datasource.dcptradedatasource.username}")
  private String prodDcpUsername;

  //  @Value("${spring.datasource.dcptradedatasource.password}")
  private String prodDcpPassword;

  @Value("${spring.datasource.driver-class-name}")
  private String driver;


  //Bean definitions for DataSources will go here
  @Bean(name = "coreDataSource")
  @Primary
  @ConfigurationProperties(prefix = "spring.datasource.coredatasource")
  @Profile("dev")
  public DataSource coreDataSource() {
//    DriverManagerDataSource dataSource = new DriverManagerDataSource();
//
//    dataSource.setDriverClassName(driver);
//    dataSource.setUrl(coreUrl);
//    dataSource.setUsername(coreUsername);
//    dataSource.setPassword(corePassword);
//    return dataSource;
    return DataSourceBuilder.create()
        .type(HikariDataSource.class)
        .build();
  }

  @Bean(name = "odsDataSource")
  @ConfigurationProperties(prefix = "spring.datasource.odsdatasource")
  @Profile("dev")
  public DataSource odsDataSource() {
//    DriverManagerDataSource dataSource = new DriverManagerDataSource();
//    dataSource.setDriverClassName(driver);
//    dataSource.setUrl(odsUrl);
//    dataSource.setUsername(odsUsername);
//    dataSource.setPassword(odsPassword);
//    return dataSource;
    return DataSourceBuilder.create()
        .type(HikariDataSource.class)
        .build();
  }

  @Bean(name = "dcpDataSource")
  @Profile("dev")
  @ConfigurationProperties(prefix = "spring.datasource.dcptradedatasourcedefault")
  public DataSource dcpDataSource() {
//    DriverManagerDataSource dataSource = new DriverManagerDataSource();
//    dataSource.setDriverClassName(driver);
//    dataSource.setUrl(dcpUrl);
//    dataSource.setUsername(dcpUsername);
//    dataSource.setPassword(dcpPassword);
//    return dataSource;
    return DataSourceBuilder.create()
        .type(HikariDataSource.class)
        .build();
  }

  @Bean(name = "prodCoreReadDataSource")
  @ConfigurationProperties(prefix = "spring.datasource.coredatasource.primary")
  @Profile("prod")
  public DataSource prodCoreReadDataSource() {
//    DriverManagerDataSource dataSource = new DriverManagerDataSource();
//    dataSource.setDriverClassName(driver);
//    dataSource.setUrl(prodCoreReadUrl);
//    dataSource.setUsername(prodCoreReadUsername);
//    dataSource.setPassword(prodCoreReadPassword);
//    return dataSource;

    return DataSourceBuilder.create()
        .type(HikariDataSource.class)
        .build();
  }

  @Bean(name = "prodCoreExecuteDataSource")
  @ConfigurationProperties(prefix = "spring.datasource.coredatasource.secondary")
  @Profile("prod")
  public DataSource prodCoreExecuteDataSource() {
//    DriverManagerDataSource dataSource = new DriverManagerDataSource();
//    dataSource.setDriverClassName(driver);
//    dataSource.setUrl(prodCoreExecuteUrl);
//    dataSource.setUsername(prodCoreExecuteUsername);
//    dataSource.setPassword(prodCoreExecutePassword);
//    return dataSource;
    return DataSourceBuilder.create()
        .type(HikariDataSource.class)
        .build();
  }

  @Bean(name = "prodOdsReadDataSource")
  @ConfigurationProperties(prefix = "spring.datasource.odsdatasource.primary")
  @Profile("prod")
  public DataSource prodOdsReadDataSource() {
//    DriverManagerDataSource dataSource = new DriverManagerDataSource();
//    dataSource.setDriverClassName(driver);
//    dataSource.setUrl(prodOdsReadUrl);
//    dataSource.setUsername(prodOdsReadUsername);
//    dataSource.setPassword(prodOdsReadPassword);
//    return dataSource;
    return DataSourceBuilder.create()
        .type(HikariDataSource.class)
        .build();
  }

//  @Bean(name = "prodOdsExecuteDataSource")
//  @ConfigurationProperties(prefix = "spring.datasource.odsdatasource.secondary")
//  public DataSource prodOdsExecuteDataSource() {
//    DriverManagerDataSource dataSource = new DriverManagerDataSource();
//    dataSource.setDriverClassName(driver);
//    dataSource.setUrl(prodOdsExecuteUrl);
//    dataSource.setUsername(prodOdsExecuteUsername);
//    dataSource.setPassword(prodOdsExecutePassword);
//    return dataSource;
//  }

  @Bean(name = "prodDcpDataSource")
  @Profile("prod")
  @ConfigurationProperties(prefix = "spring.datasource.dcptradedatasource")
  public DataSource prodDcpDataSource() {
//    DriverManagerDataSource dataSource = new DriverManagerDataSource();
//    dataSource.setDriverClassName(driver);
//    dataSource.setUrl(prodDcpUrl);
//    dataSource.setUsername(prodDcpUsername);
//    dataSource.setPassword(prodDcpPassword);
//    return dataSource;
    return DataSourceBuilder.create()
        .type(HikariDataSource.class)
        .build();
  }

}
