package com.cbs.center_service.services;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Service
@Slf4j
public class DataSourceManager {

  private final Map<String ,DataSource> dataSourceMap = new ConcurrentHashMap<>();


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

//  @Autowired
//  @Qualifier("prodOdsExecuteDataSource")
//  private DataSource prodOdsExecuteDataSource;

  @Autowired(required = false)
  @Qualifier("prodDcpDataSource")
  private DataSource prodDcpDataSource;

  public Connection getCoreConnection() throws SQLException {
    log.info("Getting connection from UAT CORE database");
    return coreDataSource.getConnection();
  }

  public Connection getOdsConnection() throws SQLException {
    log.info("Getting connection from UAT ODS database");
    return odsDataSource.getConnection();
  }

  public Connection getDcpConnection() throws SQLException {
    log.info("Getting connection UAT from database");
    return dcpDataSource.getConnection();
  }

  public Connection getProdCoreReadConnection() throws SQLException {
    log.info("Getting connection from PROD CORE READ database");
    return prodCoreReadDataSource.getConnection();
  }

  public Connection getProdCoreExecuteConnection() throws SQLException {
    log.info("Getting connection from PROD CORE EXECUTE database");
    return prodCoreExecuteDataSource.getConnection();
  }

  public Connection getProdOdsReadConnection() throws SQLException {
    log.info("Getting connection from PROD ODS READ database");
    return prodOdsReadDataSource.getConnection();
  }

//  public Connection getProdOdsExecuteConnection() throws SQLException {
//    log.info("Getting connection from PROD ODS EXECUTE database");
//    return prodOdsExecuteDataSource.getConnection();
//  }

  public Connection getProdDcpConnection() throws SQLException {
    log.info("Getting connection from PROD DCP database");
    return prodDcpDataSource.getConnection();
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
  public void init(){
    dataSourceMap.put("coreDataSource", coreDataSource);
    dataSourceMap.put("odsDataSource", odsDataSource);
    dataSourceMap.put("dcpDataSource", dcpDataSource);
    dataSourceMap.put("prodCoreReadDataSource", prodCoreReadDataSource);
    dataSourceMap.put("prodCoreExecuteDataSource", prodCoreExecuteDataSource);
    dataSourceMap.put("prodOdsReadDataSource", prodOdsReadDataSource);
    dataSourceMap.put("prodDcpDataSource", prodDcpDataSource);

  }

  public DataSource getDataSource(String key) {
    return dataSourceMap.get(key);
  }

  public void addDataSource(String key, DataSource dataSource) {
    dataSourceMap.put(key, dataSource);
  }

}
