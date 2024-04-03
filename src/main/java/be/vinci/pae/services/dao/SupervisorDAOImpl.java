package be.vinci.pae.services.dao;

import be.vinci.pae.domain.CompanyFactory;
import be.vinci.pae.domain.SupervisorFactory;
import be.vinci.pae.domain.dto.CompanyDTO;
import be.vinci.pae.domain.dto.SupervisorDTO;
import be.vinci.pae.services.dal.DalBackendServices;
import be.vinci.pae.services.utils.DTOSetServices;
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
  @Inject
  private CompanyFactory companyFactory;

  @Override
  public SupervisorDTO getOneById(int id) {
    String request = """
        SELECT su.supervisor_id, su.company AS su_company, su.lastname AS su_lastname,
        su.firstname AS su_firstname, su.phone_number AS su_phone_number, su.email AS su_email,
        cm.company_id, cm.name, cm.designation, cm.address, cm.phone_number AS cm_phone_number,
        cm.email AS cm_email, cm.is_blacklisted, cm.blacklist_motivation, cm.version AS cm_version
        FROM prostage.supervisors su, prostage.companies cm
        WHERE su.supervisor_id = ?
        """;

    try (PreparedStatement ps = dalServices.getPreparedStatement(request)) {
      ps.setInt(1, id);
      Logs.log(Level.DEBUG, "SupervisorDAO (getOneById) : success!");
      return buildSupervisorDTO(ps);
    } catch (SQLException e) {
      Logs.log(Level.FATAL, "SupervisorDAO (getOneById) : internal error");
      throw new FatalException(e);
    }

  }

  private SupervisorDTO buildSupervisorDTO(PreparedStatement ps) {
    try (ResultSet rs = ps.executeQuery()) {
      if (rs.next()) {
        CompanyDTO companyDTO = DTOSetServices.setCompanyDTO(companyFactory.getCompanyDTO(), rs);
        return DTOSetServices.setSupervisorDTO(supervisorFactory.getSupervisorDTO(), rs,
            companyDTO);
      }
      return null;
    } catch (SQLException e) {
      Logs.log(Level.FATAL, "SupervisorDAO (buildSupervisorDTO) : internal error!");
      throw new DuplicateException();
    }
  }

  private CompanyDTO setCompanyDTO(ResultSet rs) {
    CompanyDTO companyDTO = companyFactory.getCompanyDTO();
    try {
      companyDTO.setId(rs.getInt("company_id"));
      companyDTO.setName(rs.getString("name"));
      companyDTO.setDesignation(rs.getString("designation"));
      companyDTO.setAddress(rs.getString("address"));
      companyDTO.setPhoneNumber(rs.getString("cm_phone_number"));
      companyDTO.setEmail(rs.getString("cm_email"));
      companyDTO.setIsBlacklisted(rs.getBoolean("is_blacklisted"));
      companyDTO.setBlacklistMotivation(rs.getString("blacklist_motivation"));
      companyDTO.setVersion(rs.getInt("cm_version"));
      return companyDTO;
    } catch (SQLException e) {
      throw new FatalException(e);
    }
  }

  private SupervisorDTO setSupervisorDTO(ResultSet rs, CompanyDTO companyDTO) {
    SupervisorDTO supervisorDTO = supervisorFactory.getSupervisorDTO();
    try {
      supervisorDTO.setId(rs.getInt("supervisor_id"));
      supervisorDTO.setCompany(companyDTO);
      supervisorDTO.setLastname(rs.getString("su_lastname"));
      supervisorDTO.setFirstname(rs.getString("su_firstname"));
      supervisorDTO.setPhoneNumber(rs.getString("su_phone_number"));
      supervisorDTO.setEmail(rs.getString("su_email"));
      return supervisorDTO;
    } catch (SQLException e) {
      throw new FatalException(e);
    }
  }

}
