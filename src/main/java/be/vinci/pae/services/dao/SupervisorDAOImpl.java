package be.vinci.pae.services.dao;

import be.vinci.pae.domain.SupervisorFactory;
import be.vinci.pae.domain.dto.SupervisorDTO;
import be.vinci.pae.services.dal.DalBackendServices;
import be.vinci.pae.utils.Logs;
import be.vinci.pae.utils.exceptions.DuplicateException;
import be.vinci.pae.utils.exceptions.FatalException;
import jakarta.inject.Inject;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.logging.log4j.Level;

/**
 * Implementation of SupervisorDAO.
 */
public class SupervisorDAOImpl implements SupervisorDAO {

  @Inject
  private DalBackendServices dalServices;
  @Inject
  private SupervisorFactory supervisorFactory;

  @Override
  public SupervisorDTO getOneById(int id) {
    String request = """
        SELECT *
        FROM prostage.supervisors
        WHERE supervisors.supervisor_id = ?
        """;

    PreparedStatement ps = dalServices.getPreparedStatement(request);
    try {
      ps.setInt(1, id);
    } catch (SQLException e) {
      Logs.log(Level.FATAL, "SupervisorDAOImpl (getOneById) : internal error");
      throw new FatalException(e);
    }

    Logs.log(Level.DEBUG, "SupervisorDAOImpl (getOneById) : success!");
    return buildSupervisorDTO(ps);
  }

  private SupervisorDTO buildSupervisorDTO(PreparedStatement ps) {
    SupervisorDTO supervisorDTO = supervisorFactory.getSupervisorDTO();

    try (ResultSet rs = ps.executeQuery()) {
      if (rs.next()) {
        supervisorDTO.setId(rs.getInt("supervisor_id"));
        supervisorDTO.setCompany(rs.getInt("company"));
        supervisorDTO.setLastname(rs.getString("lastname"));
        supervisorDTO.setFirstname(rs.getString("firstname"));
        supervisorDTO.setPhoneNumber(rs.getString("phone_number"));
        supervisorDTO.setEmail(rs.getString("email"));
        return supervisorDTO;
      }
      return null;
    } catch (SQLException e) {
      Logs.log(Level.FATAL, "CompanyDAO (buildCompanyDTO) : internal error!");
      throw new DuplicateException();
    } finally {
      try {
        ps.close();
      } catch (SQLException e) {
        Logs.log(Level.FATAL, "CompanyDAO (buildCompanyDTO) : internal error!");
        throw new FatalException(e);
      }
    }
  }

}
