package be.vinci.pae.services.utils;

import be.vinci.pae.utils.Config;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import org.apache.commons.dbcp2.BasicDataSource;

/**
 * Implementation of DalService.
 */
public class DalServicesImpl implements DalServices, DalBackendServices {

  //private Connection connection = null;

  private static final ThreadLocal<Connection> connectionThreadLocal = ThreadLocal.withInitial(
      () -> {
        try {
          return getDataSource().getConnection();
        } catch (SQLException throwables) {
          throwables.printStackTrace();
        }
        return null;
      });

  /**
   * Create connection pool.
   *
   * @return BasicDataSource of connection.
   */
  private static BasicDataSource getDataSource() {
    BasicDataSource dataSource = new BasicDataSource();
    try {
      dataSource.setUrl(Config.getProperty("postgresUrl"));
      dataSource.setUsername(Config.getProperty("postgresUser"));
      dataSource.setPassword(Config.getProperty("postgresPassword"));
      dataSource.setMinIdle(2);
      dataSource.setMaxIdle(10);
      dataSource.setMaxOpenPreparedStatements(100);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return dataSource;
  }

  /**
   * Get a connection.
   *
   * @return Connection.
   */
  private Connection getConnection() {
    Connection connection = connectionThreadLocal.get();
    try {
      if (connection == null || connection.isClosed()) {
        connection = getDataSource().getConnection();
        connectionThreadLocal.set(connection);
        return connectionThreadLocal.get();
      }
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    }
    return connection;
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
      } catch (SQLException throwables) {
        throwables.printStackTrace();
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
      } catch (SQLException throwables) {
        throwables.printStackTrace();
      }
      connectionThreadLocal.remove();
    }
  }
}
