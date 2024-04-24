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
import be.vinci.pae.utils.Logs;
import be.vinci.pae.utils.exceptions.FatalException;
import jakarta.inject.Inject;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import org.apache.logging.log4j.Level;


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
        i.version,
                
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
        SELECT i.internship_id, i.contact, i.supervisor, i.signature_date, i.project, i.school_year,
        i.version,
                
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
        AND i.internship_id = ?
        """;

    try (PreparedStatement ps = dalServices.getPreparedStatement(requestSql)) {
      ps.setInt(1, id);
      return buildInternshipDTO(ps);
    } catch (SQLException e) {
      throw new FatalException(e);
    }
  }

  @Override
  public InternshipDTO getOneByContact(int id) {
    Logs.log(Level.INFO, "InternshipDAO (getOneByContact) : entrance");
    String requestSql = """
        SELECT i.internship_id, i.contact, i.supervisor, i.signature_date, i.project, i.school_year, i.version,
                
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
        AND i.contact = ?
        """;

    try (PreparedStatement ps = dalServices.getPreparedStatement(requestSql)) {
      ps.setInt(1, id);
      return buildInternshipDTO(ps);
    } catch (SQLException e) {
      Logs.log(Level.ERROR, "InternshipDAO (getOneByContact) : error");
      throw new FatalException(e);
    }
  }

  @Override
  public InternshipDTO createInternship(InternshipDTO internshipDTO) {
    Logs.log(Level.INFO, "DAO : entrance createInternship");
    String requestSql = """
        INSERT INTO prostage.internships(contact, supervisor, signature_date, project, school_year,
        version) VALUES (?, ?, ?, ?, ?, 1)
                
        """;

    try (PreparedStatement ps = dalServices.getPreparedStatement(requestSql)) {
      ps.setInt(1, internshipDTO.getContact().getId());
      ps.setInt(2, internshipDTO.getSupervisor().getId());
      ps.setDate(3, internshipDTO.getSignatureDate());
      ps.setString(4, internshipDTO.getProject());
      ps.setString(5, internshipDTO.getSchoolYear());
      ps.execute();
      return getOneByContact(internshipDTO.getContact().getId());
    } catch (SQLException e) {
      throw new FatalException(e);
    }
  }

  @Override
  public Map<String, Integer[]> getInternshipCountByYear() {
    String requestSql = """
        SELECT cn.school_year, count(i.internship_id) AS internship_count,
        count(DISTINCT us.user_id) AS total_students
        FROM prostage.contacts cn
        left outer join prostage.internships i on cn.contact_id = i.contact
        left outer join prostage.users us on cn.student = us.user_id
        WHERE us.role = 'Etudiant'
        GROUP BY cn.school_year;
        """;

    try (PreparedStatement ps = dalServices.getPreparedStatement(requestSql)) {
      try (ResultSet rs = ps.executeQuery()) {
        Map<String, Integer[]> internshipCountMap = new HashMap<>();
        while (rs.next()) {
          String year = rs.getString("school_year");
          int internshipCount = rs.getInt("internship_count");
          int totalStudent = rs.getInt("total_students");
          internshipCountMap.put(year, new Integer[]{internshipCount, totalStudent});
        }
        return internshipCountMap;
      }
    } catch (SQLException e) {
      throw new FatalException(e);
    }
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
        CompanyDTO companyDTO = DTOSetServices.setCompanyDTO(companyFactory.getCompanyDTO(), rs);
        UserDTO student = DTOSetServices.setUserDTO(userFactory.getUserDTO(), rs);
        ContactDTO contactDTO = DTOSetServices.setContactDTO(contactFactory.getContactDTO(), rs,
            companyDTO, student);
        SupervisorDTO supervisorDTO = DTOSetServices.setSupervisorDTO(
            supervisorFactory.getSupervisorDTO(), rs, companyDTO);
        return DTOSetServices.setInternshipDTO(internshipFactory.getInternshipDTO(), rs, contactDTO,
            supervisorDTO);
      }
      return null;
    } catch (SQLException e) {
      throw new FatalException(e);
    }
  }

  public InternshipDTO editProject(String project, int version, int internshipId) {
    String requestSql = """
        UPDATE proStage.internships
        SET project = ?
        WHERE internship_id = ? AND version = ?
        RETURNING *;
        """;
    try (PreparedStatement ps = dalServices.getPreparedStatement(requestSql)) {
      ps.setString(1, project);
      ps.setInt(2, internshipId);
      ps.setInt(3, version);
      ps.executeQuery();
      return getOneInternshipById(internshipId);
    } catch (SQLException e) {
      throw new FatalException(e);
    }
  }
}
