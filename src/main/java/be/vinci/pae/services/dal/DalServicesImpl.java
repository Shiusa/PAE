package be.vinci.pae.services.dal;

import be.vinci.pae.utils.Config;
import be.vinci.pae.utils.exceptions.FatalException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Implementation of DalService.
 */
public class DalServicesImpl implements DalServices, DalServicesConnection {

  Connection connection = null;
  private ThreadLocal<Connection> connections;

  /**
   * Create connection to database.
   */
  public DalServicesImpl() {
    this.connections = new ThreadLocal<>();
    try {
      this.connection = DriverManager.getConnection(Config.getProperty("postgresUrl"),
          Config.getProperty("postgresUser"),
          Config.getProperty("postgresPassword"));
    } catch (SQLException e) {
      throw new FatalException(e);
    }
  }

  /**
   * Get a prepared statement.
   *
   * @param query an sql request.
   * @return a prepared statement.
   */
  public PreparedStatement getPreparedStatement(String query) {
    try {
      return connection.prepareStatement(query);
    } catch (SQLException e) {
      throw new FatalException(e);
    }
  }

  @Override
  public void startTransaction() {

  }

  @Override
  public void commitTransaction() {

  }

  @Override
  public void rollbackTransaction() {

  }
}
