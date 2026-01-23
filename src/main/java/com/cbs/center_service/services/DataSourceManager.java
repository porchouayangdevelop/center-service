package com.cbs.center_service.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Service
@Slf4j
public class DataSourceManager {

  @Autowired
  @Qualifier("coreDataSource")
  private DataSource coreDataSource;

  @Autowired
  @Qualifier("odsDataSource")
  private DataSource odsDataSource;

  @Autowired
  @Qualifier("dcpDataSource")
  private DataSource dcpDataSource;

  @Autowired
  @Qualifier("prodCoreReadDataSource")
  private DataSource prodCoreReadDataSource;

  @Autowired
  @Qualifier("prodCoreExecuteDataSource")
  private DataSource prodCoreExecuteDataSource;

  @Autowired
  @Qualifier("prodOdsReadDataSource")
  private DataSource prodOdsReadDataSource;

//  @Autowired
//  @Qualifier("prodOdsExecuteDataSource")
//  private DataSource prodOdsExecuteDataSource;

  @Autowired
  @Qualifier("prodDcpDataSource")
  private DataSource prodDcpDataSource;

  public Connection getCoreConnection() throws SQLException {
    log.info("Getting connection from CORE database");
    return coreDataSource.getConnection();
  }

  public Connection getOdsConnection() throws SQLException {
    log.info("Getting connection from ODS database");
    return odsDataSource.getConnection();
  }

  public Connection getDcpConnection() throws SQLException {
    log.info("Getting connection from DCP database");
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

}
