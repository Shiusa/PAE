package be.vinci.pae.services.dao;


import be.vinci.pae.domain.ContactFactory;
import be.vinci.pae.domain.dto.ContactDTO;
import be.vinci.pae.services.dal.DalServices;
import jakarta.inject.Inject;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Implementation of ContactDAO.
 */
public class ContactDAOImpl implements ContactDAO {

  @Inject
  private DalServices dalServices;
  @Inject
  private ContactFactory contactFactory;

  @Override
  public ContactDTO findContactByCompanyStudentSchoolYear(int company, int student,
      String schoolYear) {
    String requestSql = """
        SELECT contact_id, company, student, meeting, contact_state, reason_for_refusal,
          school_year
        FROM prostage.contacts
        WHERE contacts.company = ? AND contacts.student = ? AND contacts.school_year = ?
        """;
    PreparedStatement ps = dalServices.getPreparedStatement(requestSql);

    try {
      ps.setInt(1, company);
      ps.setInt(2, student);
      ps.setString(3, schoolYear);
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }

    ContactDTO contact = buildContactDTO(ps);

    try {
      ps.close();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }

    return contact;
  }

  @Override
  public ContactDTO startContact(int company, int student, String schoolYear) {
    String requestSql = """
        INSERT INTO prostage.contacts (company, student, contact_state, school_year)
         VALUES (?, ?, ?, ?) RETURNING *;
        """;
    PreparedStatement ps = dalServices.getPreparedStatement(requestSql);
    try {
      ps.setInt(1, company);
      ps.setInt(2, student);
      ps.setString(3, "started");
      ps.setString(4, schoolYear);
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }

    ContactDTO contact = buildContactDTO(ps);
    try {
      ps.close();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
    return contact;
  }

  private ContactDTO buildContactDTO(PreparedStatement ps) {
    ContactDTO contact = contactFactory.getContactDTO();
    try (ResultSet rs = ps.executeQuery()) {
      if (rs.next()) {
        contact.setId(rs.getInt("contact_id"));
        contact.setCompany(rs.getInt("company"));
        contact.setStudent(rs.getInt("student"));
        contact.setMeeting(rs.getString("meeting"));
        contact.setState(rs.getString("contact_state"));
        contact.setReasonRefusal(rs.getString("reason_for_refusal"));
        contact.setSchoolYear(rs.getString("school_year"));
        rs.close();
        return contact;
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return contact;
  }

}
