package be.vinci.pae.services.utils;

import java.sql.PreparedStatement;

/**
 * DalService Interface.
 */
public interface DalService {

  PreparedStatement getPreparedStatement(String sql);
}
