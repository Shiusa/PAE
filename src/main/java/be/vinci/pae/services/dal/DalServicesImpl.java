package be.vinci.pae.services.dal;

import be.vinci.pae.utils.Config;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Implementation of DalService.
 */
public class DalServicesImpl implements DalServices, DalServicesConnection {

  private ThreadLocal<Connection> connections = new ThreadLocal<>();

  /**
   * Get a prepared statement.
   *
   * @param query an sql request.
   * @return a prepared statement.
   */
  public PreparedStatement getPreparedStatement(String query) {
    try {
      return connections.get().prepareStatement(query);
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void startTransaction() {
    Connection conn;
    try {
      conn = DriverManager.getConnection(
          Config.getProperty("postgresUrl"),
          Config.getProperty("postgresUser"),
          Config.getProperty("postgresPassword"));
      connections.set(conn);
      connections.get().setAutoCommit(false);
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void commitTransaction() {
    Connection connection = connections.get();
    try {
      if (connection != null) {
        connection.commit();
      }
    } catch (SQLException e) {
      e.printStackTrace();
    } finally {
      try {
        connection.close();
      } catch (SQLException e) {
        throw new RuntimeException(e);
      }
      connections.remove();
    }
  }

  @Override
  public void rollbackTransaction() {
    Connection connection = connections.get();
    try {
      if (connection != null) {
        connection.rollback();
      }
    } catch (SQLException e) {
      e.printStackTrace();
    } finally {
      try {
        connection.close();
      } catch (SQLException e) {
        throw new RuntimeException(e);
      }
      connections.remove();
    }
  }
}
