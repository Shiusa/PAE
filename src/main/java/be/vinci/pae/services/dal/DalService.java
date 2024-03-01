package be.vinci.pae.services.dal;

import java.sql.PreparedStatement;

/**
 * DalService Interface.
 */
public interface DalService {

  PreparedStatement getPreparedStatement(String sql);
}
