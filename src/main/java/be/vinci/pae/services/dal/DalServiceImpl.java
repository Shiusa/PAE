package be.vinci.pae.services.dal;

import be.vinci.pae.utils.Config;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Implementation of DalService.
 */
public class DalServiceImpl implements DalService {

  private Connection connection = null;

  /**
   * Create connection to database.
   */
  public DalServiceImpl() {
    try {
      this.connection = DriverManager.getConnection(Config.getProperty("postgresUrl"),
          Config.getProperty("postgresUser"),
          Config.getProperty("postgresPassword"));
    } catch (SQLException e) {
      throw new RuntimeException(e);
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
      throw new RuntimeException(e);
    }
  }
}
