package be.vinci.pae.services.utils;

import be.vinci.pae.utils.Config;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DalServiceImpl implements DalService {

  private Connection connection = null;

  private PreparedStatement getOneUserByEmail;

  public DalServiceImpl() {
    try {
      this.connection = DriverManager.getConnection(Config.getProperty("postgresUrl"),
          Config.getProperty("postgresUser"),
          Config.getProperty("postgresPassword"));
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public PreparedStatement getPreparedStatement(String query) {
    try {
      return connection.prepareStatement(query);
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }
}
