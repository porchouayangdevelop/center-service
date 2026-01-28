package com.cbs.center_service.components;

import com.cbs.center_service.services.DataSourceManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class DatabaseComponent {

//  @Autowired
//  private DataSource dataSource;

  @Autowired
  private DataSourceManager dbManager;

  public List<Map<String, Object>> executeQuery(String dataSourceKey, String query, Object... params) {
    // Implementation for executing the query and returning results
    List<Map<String, Object>> results = new ArrayList<>();
    Connection conn = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;

    try {
      DataSource dataSource = dbManager.getDataSource(dataSourceKey);
      if (dataSource == null) {
        log.error("DataSource not found for key: {}", dataSourceKey);
        return results;
      }

      conn = dataSource.getConnection();
      stmt = conn.prepareStatement(query);

      // Set parameters
      for (int i = 0; i < params.length; i++) {
        stmt.setObject(i + 1, params[i]);
      }

      rs = stmt.executeQuery();
      ResultSetMetaData metaData = rs.getMetaData();
      int columnCount = metaData.getColumnCount();

      // Process ResultSet and populate results list
      while (rs.next()) {
        // Convert each row into a Map and add to results
        // This is a placeholder; actual implementation will depend on requirements
        Map<String, Object> row = new HashMap<>();
        for (int j = 1; j <= columnCount; j++) {
          row.put(metaData.getColumnName(j), rs.getObject(j));
        }
        results.add(row);
      }
      return results;
    } catch (SQLException e) {
      // Handle SQL exception
      log.error("SQL Exception: {}", e.getMessage());
    } finally {
      // Clean up resources
      try {
        if (rs != null) rs.close();
        if (stmt != null) stmt.close();
        if (conn != null) conn.close();
      } catch (SQLException e) {
        log.error("Error closing resources query: {}", e.getMessage());
      }
    }

    return results; // Placeholder return
  }


  public List<Map<String, Object>> executeStoredProcedure(String dataSourceKey, String procedureCall, Object... params) {
    // Implementation for executing the stored procedure and returning results
    List<Map<String, Object>> results = new ArrayList<>();
    Connection conn = null;
    CallableStatement callStmt = null;
    ResultSet rs = null;

    try {
      DataSource dataSource = dbManager.getDataSource(dataSourceKey);
      if (dataSource == null) {
        log.error("DataSource not found for key: {}, execute", dataSourceKey);
        return results;
      }

      conn = dataSource.getConnection();
      callStmt = conn.prepareCall(procedureCall);

      // Set parameters
      for (int i = 0; i < params.length; i++) {
        callStmt.setObject(i + 1, params[i]);
      }

      rs = callStmt.executeQuery();
      ResultSetMetaData metaData = rs.getMetaData();
      int columnCount = metaData.getColumnCount();

      // Process ResultSet and populate results list
      while (rs.next()) {
        // Convert each row into a Map and add to results
        // This is a placeholder; actual implementation will depend on requirements
        Map<String, Object> row = new HashMap<>();
        for (int j = 1; j <= columnCount; j++) {
          row.put(metaData.getColumnName(j), rs.getObject(j));
        }
        results.add(row);
      }
      return results;
    } catch (SQLException e) {
      // Handle SQL exception
      log.error("SQL Execute Procedure Exception: {}", e.getMessage());
    } finally {
      // Clean up resources
      try {
        if (rs != null) rs.close();
        if (callStmt != null) callStmt.close();
        if (conn != null) conn.close();
      } catch (SQLException e) {
        log.error("Error closing resources call procedure: {}", e.getMessage());
      }
    }

    return results; // Placeholder return
  }

  public Map<String, Object> executeSingleResultQuery(String key, String query, Object... params) {
    List<Map<String, Object>> results = executeQuery(key, query, params);
    if (!results.isEmpty()) {
      return results.get(0);
    }
    return null;
  }

  public Map<String, Object> executeSingleResultProcedure(String key, String procedureCall, Object... params) {
    List<Map<String, Object>> results = executeStoredProcedure(key, procedureCall, params);
    if (!results.isEmpty()) {
      return results.get(0);
    }
    return null;
  }

  private void closeResources(AutoCloseable... resources) {
    for (AutoCloseable resource : resources) {
      if (resource != null) {
        try {
          resource.close();
        } catch (Exception e) {
          log.error("Error closing resource: {}", e.getMessage());
        }
      }
    }
  }
}
