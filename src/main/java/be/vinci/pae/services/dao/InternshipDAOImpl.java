package be.vinci.pae.services.dao;

import be.vinci.pae.domain.CompanyFactory;
import be.vinci.pae.domain.ContactFactory;
import be.vinci.pae.domain.InternshipFactory;
import be.vinci.pae.domain.SupervisorFactory;
import be.vinci.pae.domain.UserFactory;
import be.vinci.pae.domain.dto.CompanyDTO;
import be.vinci.pae.domain.dto.ContactDTO;
import be.vinci.pae.domain.dto.InternshipDTO;
import be.vinci.pae.domain.dto.SupervisorDTO;
import be.vinci.pae.domain.dto.UserDTO;
import be.vinci.pae.services.dal.DalBackendServices;
import be.vinci.pae.utils.exceptions.FatalException;
import jakarta.inject.Inject;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Implementation of InternshipDAO.
 */
public class InternshipDAOImpl implements InternshipDAO {

  @Inject
  private DalBackendServices dalServices;
  @Inject
  private InternshipFactory internshipFactory;
  @Inject
  private ContactFactory contactFactory;
  @Inject
  private SupervisorFactory supervisorFactory;
  @Inject
  private CompanyFactory companyFactory;
  @Inject
  private UserFactory userFactory;

  @Override
  public InternshipDTO getOneInternshipByIdUser(int student) {
    String requestSql = """
        SELECT i.internship_id, i.contact, i.supervisor, i.signature_date, i.project, i.school_year,
                
        ct.contact_id, ct.company AS ct_company, ct.student, ct.meeting, ct.contact_state,
        ct.reason_for_refusal, ct.school_year AS ct_school_year, ct.version AS ct_version,
                
        cm.company_id, cm.name, cm.designation, cm.address, cm.phone_number AS cm_phone_number,
        cm.email AS cm_email, cm.is_blacklisted, cm.blacklist_motivation, cm.version AS cm_version,
                
        us.user_id, us.email AS us_email, us.lastname AS us_lastname, us.firstname AS us_firstname,
        us.phone_number AS us_phone_number, us.password, us.registration_date,
        us.school_year AS us_school_year, us.role,
                
        su.supervisor_id, su.company AS su_company, su.lastname AS su_lastname,
        su.firstname AS su_firstname, su.phone_number AS su_phone_number, su.email AS su_email
                
        FROM prostage.internships i, prostage.contacts ct, prostage.companies cm,
        prostage.supervisors su, prostage.users us
        WHERE i.contact = ct.contact_id AND i.supervisor = su.supervisor_id
        AND ct.company = cm.company_id AND ct.student = us.user_id
        AND ct.student = ?
        """;

    try (PreparedStatement ps = dalServices.getPreparedStatement(requestSql)) {
      ps.setInt(1, student);
      return buildInternshipDTO(ps);
    } catch (SQLException e) {
      throw new FatalException(e);
    }
  }

  @Override
  public InternshipDTO getOneInternshipById(int id) {
    String requestSql = """
        SELECT internship_id, contact, supervisor, signature_date, project, school_year
        FROM prostage.internships
        WHERE internship_id = ?
        """;
    PreparedStatement ps = dalServices.getPreparedStatement(requestSql);
    try {
      ps.setInt(1, id);
    } catch (SQLException e) {
      throw new FatalException(e);
    }
    return buildInternshipDTO(ps);
  }

  /**
   * Build the InternshipDTO based on the prepared statement.
   *
   * @param ps the prepared statement.
   * @return the internshipDTO built.
   */
  private InternshipDTO buildInternshipDTO(PreparedStatement ps) {
    try (ResultSet rs = ps.executeQuery()) {
      if (rs.next()) {
        CompanyDTO companyDTO = setCompanyDTO(rs);
        UserDTO student = setUserDTO(rs);
        ContactDTO contactDTO = setContactDTO(rs, companyDTO, student);
        SupervisorDTO supervisorDTO = setSupervisorDTO(rs, companyDTO);
        return setInternshipDTO(rs, contactDTO, supervisorDTO);
      }
      return null;
    } catch (SQLException e) {
      throw new FatalException(e);
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

  private UserDTO setUserDTO(ResultSet rs) {
    UserDTO studentDTO = userFactory.getUserDTO();
    try {
      studentDTO.setId(rs.getInt("user_id"));
      studentDTO.setEmail(rs.getString("us_email"));
      studentDTO.setLastname(rs.getString("us_lastname"));
      studentDTO.setFirstname(rs.getString("us_firstname"));
      studentDTO.setPhoneNumber(rs.getString("us_phone_number"));
      studentDTO.setPassword(rs.getString("password"));
      studentDTO.setRegistrationDate(rs.getDate("registration_date"));
      studentDTO.setSchoolYear(rs.getString("us_school_year"));
      studentDTO.setRole(rs.getString("role"));
      return studentDTO;
    } catch (SQLException e) {
      throw new FatalException(e);
    }
  }

  private ContactDTO setContactDTO(ResultSet rs, CompanyDTO companyDTO, UserDTO studentDTO) {
    ContactDTO contactDTO = contactFactory.getContactDTO();
    try {
      contactDTO.setId(rs.getInt("contact_id"));
      contactDTO.setCompany(companyDTO);
      contactDTO.setStudent(studentDTO);
      contactDTO.setMeeting(rs.getString("meeting"));
      contactDTO.setState(rs.getString("contact_state"));
      contactDTO.setReasonRefusal(rs.getString("reason_for_refusal"));
      contactDTO.setSchoolYear(rs.getString("ct_school_year"));
      contactDTO.setVersion(rs.getInt("ct_version"));
      return contactDTO;
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

  private InternshipDTO setInternshipDTO(ResultSet rs, ContactDTO contactDTO,
      SupervisorDTO supervisorDTO) {
    InternshipDTO internship = internshipFactory.getInternshipDTO();
    try {
      internship.setId(rs.getInt("internship_id"));
      internship.setContact(contactDTO);
      internship.setSupervisor(supervisorDTO);
      internship.setSignatureDate(rs.getDate("signature_date"));
      internship.setProject(rs.getString("project"));
      internship.setSchoolYear(rs.getString("school_year"));
      return internship;
    } catch (SQLException e) {
      throw new FatalException(e);
    }
  }


}
