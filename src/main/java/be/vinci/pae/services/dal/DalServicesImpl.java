package be.vinci.pae.services.dal;

import be.vinci.pae.utils.Config;
import be.vinci.pae.utils.exceptions.FatalException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import org.apache.commons.dbcp2.BasicDataSource;

/**
 * Implementation of DalService.
 */
public class DalServicesImpl implements DalServices, DalBackendServices {

  private static final ThreadLocal<Connection> connectionThreadLocal = ThreadLocal.withInitial(
      () -> {
        try {
          return getDataSource().getConnection();
        } catch (SQLException e) {
          throw new FatalException(e);
        }
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
      dataSource.setMaxTotal(5);
    } catch (Exception e) {
      throw new FatalException(e);
    }
    return dataSource;
  }

  /**
   * Close a connection and remove it from ThreadLocal.
   *
   * @param connection BasicDataSource connection.
   */
  private static void closeConnection(Connection connection) {
    try {
      connection.close();
      connectionThreadLocal.remove();
    } catch (SQLException e) {
      throw new FatalException(e);
    }
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
    } catch (SQLException e) {
      throw new FatalException(e);
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
      throw new FatalException(e);
    }
  }

  /**
   * Start a transation.
   */
  @Override
  public void startTransaction() {
    Connection connection = getConnection();
    try {
      connection.setAutoCommit(false);
    } catch (SQLException e) {
      throw new FatalException(e);
    }
  }

  /**
   * Commit changes.
   */
  @Override
  public void commitTransaction() {
    Connection connection = getConnection();
    try {
      connection.commit();
      connection.setAutoCommit(true);
    } catch (SQLException e) {
      throw new FatalException(e);
    } finally {
      closeConnection(connection);
    }
  }

  /**
   * Rollback changes.
   */
  @Override
  public void rollbackTransaction() {
    Connection connection = getConnection();
    try {
      connection.rollback();
    } catch (SQLException e) {
      throw new FatalException(e);
    } finally {
      closeConnection(connection);
    }
  }
}
