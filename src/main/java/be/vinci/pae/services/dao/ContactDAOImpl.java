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
  public ContactDTO startContact(int student, int company) {
    return null;
  }

  public ContactDTO meetingContact(int idContact, String meeting) {
    String requestSql = """
        UPDATE proStage.contacts
        SET meeting = ?, contact_state = 'admitted'
        WHERE contact_id = ?
        RETURNING contact_id, company, student, meeting, contact_state, reason_for_refusal, school_year;
        """;
    PreparedStatement ps = dalServices.getPreparedStatement(requestSql);
    try {
      ps.setString(1, meeting);
      ps.setInt(2, idContact);
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
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
      return null;
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    } finally {
      try {
        ps.close();
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
    return null;
  }
}
