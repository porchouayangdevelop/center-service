package com.cbs.center_service.handlers;


public class DatabaseException extends RuntimeException {
  public DatabaseException(String message) {
    super(message);
  }

  public DatabaseException(String message, Throwable cause) {
    super(message, cause);
  }

  public DatabaseException(Throwable cause) {
    super("Database operation failed", cause);
  }

  public static DatabaseException connectionFailed(String dataSourceName) {
    return new DatabaseException(String.format("Failed to connect to database: %s", dataSourceName));
  }

  public static DatabaseException queryFailed(String sql, Throwable cause) {
    return new DatabaseException(
        String.format("Failed to execute query: %s", sql), cause
    );
  }

  public static DatabaseException procedureFailed(String procedureName, Throwable cause) {
    return new DatabaseException(
        String.format("Failed to execute stored procedure: %", procedureName),
        cause
    );
  }
}
