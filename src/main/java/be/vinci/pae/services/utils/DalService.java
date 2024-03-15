package be.vinci.pae.services.utils;

import be.vinci.pae.utils.exceptions.FatalException;
import java.sql.PreparedStatement;

/**
 * DalService Interface.
 */
public interface DalService {

  PreparedStatement getPreparedStatement(String sql) throws FatalException;
}
