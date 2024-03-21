package be.vinci.pae.services.utils;

import be.vinci.pae.utils.Config;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Implementation of DalService.
 */
public class DalServicesImpl implements DalServices, DalBackendServices {

  //private Connection connection = null;
  private static final ThreadLocal<Connection> connectionThreadLocal = ThreadLocal.withInitial(
      () -> {
        try {
          return DriverManager.getConnection(Config.getProperty("postgresUrl"),
              Config.getProperty("postgresUser"),
              Config.getProperty("postgresPassword"));
        } catch (SQLException e) {
          throw new RuntimeException(e);
        }
      });

  /**
   * Create connection to database.
   */
  /*public DalServicesImpl() {
    try {
      this.connection = DriverManager.getConnection(Config.getProperty("postgresUrl"),
          Config.getProperty("postgresUser"),
          Config.getProperty("postgresPassword"));
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }*/
  private static Connection getConnection() {
    return connectionThreadLocal.get();
  }

  /**
   * Get a prepared statement.
   *
   * @param query an sql request.
   * @return a prepared statement.
   */
  public PreparedStatement getPreparedStatement(String query) {
    try {
      Connection connection = getConnection();
      return connection.prepareStatement(query);
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Start a transation
   */
  @Override
  public void startTransaction() {
    Connection connection = getConnection();
    try {
      connection.setAutoCommit(false);
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Commit changes.
   */
  @Override
  public void commit() {
    Connection connection = getConnection();
    try {
      connection.commit();
      connection.setAutoCommit(true);
    } catch (SQLException e) {
      throw new RuntimeException(e);
    } finally {
      try {
        connection.close();
      } catch (SQLException e) {
        throw new RuntimeException(e);
      }
      connectionThreadLocal.remove();
    }
  }

  /**
   * Rollback changes.
   */
  @Override
  public void rollback() {
    Connection connection = getConnection();
    try {
      connection.rollback();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    } finally {
      try {
        connection.close();
      } catch (SQLException e) {
        throw new RuntimeException(e);
      }
      connectionThreadLocal.remove();
    }
  }
}
