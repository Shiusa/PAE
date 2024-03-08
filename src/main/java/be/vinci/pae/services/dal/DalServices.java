package be.vinci.pae.services.dal;

import java.sql.PreparedStatement;

/**
 * DalService Interface.
 */
public interface DalServices {

  PreparedStatement getPreparedStatement(String sql);
}
