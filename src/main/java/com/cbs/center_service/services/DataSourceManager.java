package com.cbs.center_service.services;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
public class DataSourceManager {

  private final Map<String, DataSource> dataSourceMap = new ConcurrentHashMap<>();

  @Autowired(required = false)
  @Qualifier("coreDataSource")
  private DataSource coreDataSource;

  @Autowired(required = false)
  @Qualifier("odsDataSource")
  private DataSource odsDataSource;

  @Autowired(required = false)
  @Qualifier("dcpDataSource")
  private DataSource dcpDataSource;

  @Autowired(required = false)
  @Qualifier("prodCoreReadDataSource")
  private DataSource prodCoreReadDataSource;

  @Autowired(required = false)
  @Qualifier("prodCoreExecuteDataSource")
  private DataSource prodCoreExecuteDataSource;

  @Autowired(required = false)
  @Qualifier("prodOdsReadDataSource")
  private DataSource prodOdsReadDataSource;

  @Autowired(required = false)
  @Qualifier("prodOdsExecuteDataSource")
  private DataSource prodOdsExecuteDataSource;

  @Autowired(required = false)
  @Qualifier("prodDcpDataSource")
  private DataSource prodDcpDataSource;

  @Autowired(required = false)
  @Qualifier("storeDataSource") //UAT
  private DataSource storeDataSource;

  @Autowired(required = false)
  @Qualifier("prodStoreDataSource")
  private DataSource prodStoreDataSource;

  @Autowired
  private Environment environment;

  public Connection getCoreConnection() throws SQLException {
    log.info("Getting connection from UAT CORE database");
    if (coreDataSource == null) {
      throw new SQLException("Core DataSource is not configured");
    }
    return coreDataSource.getConnection();
  }

  public Connection getOdsConnection() throws SQLException {
    log.info("Getting connection from UAT ODS database");
    if (odsDataSource == null) {
      throw new SQLException("ODS DataSource is not configured");
    }
    return odsDataSource.getConnection();
  }

  public Connection getDcpConnection() throws SQLException {
    log.info("Getting connection UAT from database");
    if (dcpDataSource == null) {
      throw new SQLException("DCP DataSource is not configured");
    }
    return dcpDataSource.getConnection();
  }

  public Connection getProdCoreReadConnection() throws SQLException {
    log.info("Getting connection from PROD CORE READ database");
    if (prodCoreReadDataSource == null) {
      throw new SQLException("PROD CORE READ DataSource is not configured");
    }
    return prodCoreReadDataSource.getConnection();
  }

  public Connection getProdCoreExecuteConnection() throws SQLException {
    log.info("Getting connection from PROD CORE EXECUTE database");
    if (prodCoreExecuteDataSource == null) {
      throw new SQLException("PROD CORE EXECUTE DataSource is not configured");
    }
    return prodCoreExecuteDataSource.getConnection();
  }

  public Connection getProdOdsReadConnection() throws SQLException {
    log.info("Getting connection from PROD ODS READ database");
    if (prodOdsReadDataSource == null) {
      throw new SQLException("PROD ODS READ DataSource is not configured");
    }
    return prodOdsReadDataSource.getConnection();
  }

  public Connection getProdOdsExecuteConnection() throws SQLException {
    log.info("Getting connection from PROD ODS EXECUTE database");
    if (prodOdsExecuteDataSource == null) {
      throw new SQLException("PROD ODS EXECUTE DataSource is not configured");
    }
    return prodOdsExecuteDataSource.getConnection();
  }

  public Connection getProdDcpConnection() throws SQLException {
    log.info("Getting connection from PROD DCP database");
    if (prodDcpDataSource == null) {
      throw new SQLException("PROD DCP DataSource is not configured");
    }
    return prodDcpDataSource.getConnection();
  }

  //UAT store datasource
  public Connection getStoreConnection() throws SQLException {
    log.info("Getting connection from UAT STORE database");
    if (storeDataSource == null) {
      throw new SQLException(String.format("% DataSource is not configured", environment.getActiveProfiles()));
    }
    return storeDataSource.getConnection();
  }

  //PRD store datasource
  public Connection getProdStoreConnection() throws SQLException {
    log.info("Getting connection from {} STORE database", (Object) environment.getActiveProfiles());
    if (prodStoreDataSource == null) {
      throw new SQLException(String.format("% DataSource is not configured", environment.getActiveProfiles()));
    }
    return prodStoreDataSource.getConnection();
  }

  public void closeConnection(Connection connection) {
    if (connection != null) {
      try {
        connection.close();
        log.info("Connection closed");
      } catch (SQLException ex) {
        log.error("Error closing connection: {}", ex.getMessage());
      }
    }
  }

  public void closeResource(Connection connection, java.sql.PreparedStatement stmt, java.sql.CallableStatement callStmt, java.sql.ResultSet rs) {
    try {
      if (rs != null) rs.close();
      if (stmt != null) stmt.close();
      if (callStmt != null) callStmt.close();
      if (connection != null) connection.close();
    } catch (SQLException ex) {
      log.error("Error closing resource: {}", ex.getMessage());
    }
  }

  @PostConstruct
  public void init() {
    if (coreDataSource != null) {
      dataSourceMap.put("coreDataSource", coreDataSource);
      log.info("Registered {} coreDataSource",environment.getActiveProfiles());
    }

    if (odsDataSource != null) {
      dataSourceMap.put("odsDataSource", odsDataSource);
      log.info("Registered odsDataSource");
    }
    if (dcpDataSource != null) {
      dataSourceMap.put("dcpDataSource", dcpDataSource);
      log.info("Registered dcpDataSource");
    }
    if (prodCoreReadDataSource != null) {
      dataSourceMap.put("prodCoreReadDataSource", prodCoreReadDataSource);
      log.info("Registered prodCoreReadDataSource");
    }
    if (prodCoreExecuteDataSource != null) {
      dataSourceMap.put("prodCoreExecuteDataSource", prodCoreExecuteDataSource);
      log.info("Registered prodCoreExecuteDataSource");
    }
    if (prodOdsReadDataSource != null) {
      dataSourceMap.put("prodOdsReadDataSource", prodOdsReadDataSource);
      log.info("Registered prodOdsReadDataSource");
    }
    if (prodOdsExecuteDataSource != null) {
      dataSourceMap.put("prodOdsExecuteDataSource", prodOdsExecuteDataSource);
      log.info("Registered prodOdsExecuteDataSource");
    }

    if (prodDcpDataSource != null) {
      dataSourceMap.put("prodDcpDataSource", prodDcpDataSource);
      log.info("Registered prodDcpDataSource");
    }
    if (storeDataSource != null) {
      dataSourceMap.put("storeDataSource", storeDataSource);
      log.info("Registered storeDataSource");
    }
    if (prodStoreDataSource != null) {
      dataSourceMap.put("prodStoreDataSource", prodStoreDataSource);
      log.info("Registered prodStoreDataSource");
    }

    log.info("DataSourceManager initialized with {} data sources", dataSourceMap.size());
  }

  public DataSource getDataSource(String key) {
    DataSource dataSource = dataSourceMap.get(key);
    if (dataSource == null) {
      log.warn("DataSource not found for key: {}. Available keys: {}", key, dataSourceMap.keySet());
    }
    return dataSource;
  }

  public void addDataSource(String key, DataSource dataSource) {
    if (dataSource != null) {
      dataSourceMap.put(key, dataSource);
      log.info("Added DataSource with key: {}", key);
    } else {
      log.warn("Attempted to add null DataSource with key: {}", key);
    }
    dataSourceMap.put(key, dataSource);
  }

}
