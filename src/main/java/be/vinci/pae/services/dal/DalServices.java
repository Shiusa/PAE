package be.vinci.pae.services.dal;

import java.sql.PreparedStatement;

/**
 * DalService Interface.
 */
public interface DalServices {

  /**
   * Get a prepared statement.
   *
   * @param sql the sql command.
   * @return the prepared statement.
   */
  PreparedStatement getPreparedStatement(String sql);

}
