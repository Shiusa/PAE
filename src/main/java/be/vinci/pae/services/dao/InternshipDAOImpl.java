package be.vinci.pae.services.dao;

import be.vinci.pae.domain.InternshipFactory;
import be.vinci.pae.domain.dto.InternshipDTO;
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

  @Override
  public InternshipDTO getOneInternshipByIdUser(int student) {
    String requestSql = """
        SELECT i.internship_id, i.contact, i.supervisor, i.signature_date, i.project, i.school_year, cm.name, cm.designation, cm.address,
               su.firstname, su.lastname, su.email
        FROM proStage.internships i, proStage.contacts ct, proStage.companies cm, proStage.supervisors su
        WHERE ct.student = ? AND ct.contact_id = i.contact AND i.supervisor = su.supervisor_id AND su.company = cm.company_id
        """;
    PreparedStatement ps = dalServices.getPreparedStatement(requestSql);
    try {
      ps.setInt(1, student);
    } catch (SQLException e) {
      throw new FatalException(e);
    }
    return buildInternshipDTO(ps);
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
    InternshipDTO internship = internshipFactory.getInternshipDTO();

    try (ResultSet rs = ps.executeQuery()) {
      if (rs.next()) {
        internship.setId(rs.getInt("internship_id"));
        internship.setContact(rs.getInt("contact"));
        internship.setSupervisor(rs.getInt("supervisor"));
        internship.setSignatureDate(rs.getDate("signature_date"));
        internship.setProject(rs.getString("project"));
        internship.setSchoolYear(rs.getString("school_year"));
        internship.setNameInternship(rs.getString("name"));
        internship.setDesignationInternship(rs.getString("designation"));
        internship.setAddressInternship(rs.getString("address"));
        internship.setFirstnameSupervisor(rs.getString("firstname"));
        internship.setLastnameSupervisor(rs.getString("lastname"));
        internship.setEmailSupervisor(rs.getString("email"));
        rs.close();
        return internship;
      }
      return null;
    } catch (SQLException e) {
      throw new FatalException(e);
    } finally {
      try {
        ps.close();
      } catch (SQLException e) {
        throw new FatalException(e);
      }
    }
  }

}
