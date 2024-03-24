package be.vinci.pae.services.dao;


import be.vinci.pae.domain.ContactFactory;
import be.vinci.pae.domain.dto.ContactDTO;
import be.vinci.pae.utils.Logs;
import be.vinci.pae.utils.exceptions.FatalException;
import be.vinci.pae.services.dal.DalBackendServices;
import jakarta.inject.Inject;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.logging.log4j.Level;

/**
 * Implementation of ContactDAO.
 */
public class ContactDAOImpl implements ContactDAO {

  @Inject
  private DalBackendServices dalBackendServices;
  @Inject
  private ContactFactory contactFactory;

  @Override
  public ContactDTO findContactByCompanyStudentSchoolYear(int company, int student,
      String schoolYear) {
    Logs.log(Level.INFO, "ContactDAO (findContactByCompanyStudentSchoolYear) : entrance");
    String requestSql = """
        SELECT contact_id, company, student, meeting, contact_state, reason_for_refusal,
          school_year
        FROM prostage.contacts
        WHERE contacts.company = ? AND contacts.student = ? AND contacts.school_year = ?
        """;
    PreparedStatement ps = dalBackendServices.getPreparedStatement(requestSql);

    try {
      ps.setInt(1, company);
      ps.setInt(2, student);
      ps.setString(3, schoolYear);
    } catch (SQLException e) {
      Logs.log(Level.FATAL, "ContactDAO (findContactByCompanyStudentSchoolYear) : internal error");
      throw new RuntimeException(e);
    }

    ContactDTO contact = buildContactDTO(ps);

    try {
      ps.close();
    } catch (SQLException e) {
      Logs.log(Level.FATAL, "ContactDAO (findContactByCompanyStudentSchoolYear) : internal error");
      throw new RuntimeException(e);
    }

    Logs.log(Level.DEBUG, "ContactDAO (findContactByCompanyStudentSchoolYear) : success!");
    return contact;
  }

  @Override
  public ContactDTO startContact(int company, int student, String schoolYear) {
    Logs.log(Level.INFO, "ContactDAO (startContact) : entrance");
    String requestSql = """
        INSERT INTO prostage.contacts (company, student, contact_state, school_year)
         VALUES (?, ?, ?, ?) RETURNING *;
        """;
    PreparedStatement ps = dalBackendServices.getPreparedStatement(requestSql);
    try {
      ps.setInt(1, company);
      ps.setInt(2, student);
      ps.setString(3, "started");
      ps.setString(4, schoolYear);
    } catch (SQLException e) {
      Logs.log(Level.FATAL, "ContactDAO (startContact) : internal error");
      throw new RuntimeException(e);
    }

    ContactDTO contact = buildContactDTO(ps);
    try {
      ps.close();
    } catch (SQLException e) {
      Logs.log(Level.FATAL, "ContactDAO (startContact) : internal error");
      throw new RuntimeException(e);
    }
    Logs.log(Level.DEBUG, "ContactDAO (startContact) : success!");
    return contact;
  }

  @Override
  public ContactDTO findContactById(int contactId) {
    String requestSql = """
        SELECT *
        FROM prostage.contacts
        WHERE contacts.contact_id = ?
        """;
    PreparedStatement ps = dalBackendServices.getPreparedStatement(requestSql);

    try {
      ps.setInt(1, contactId);
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
  public ContactDTO unsupervise(int contactId) {
    ContactDTO contactDTO = contactFactory.getContactDTO();
    String requestSql = """
        UPDATE proStage.contacts
        SET contact_state = 'unsupervised'
        WHERE contact_id = ?
        RETURNING *;
        """;

    PreparedStatement ps = dalBackendServices.getPreparedStatement(requestSql);
    try {
      ps.setInt(1, contactId);
    } catch (SQLException e) {
      throw new FatalException(e);
    }
    System.out.println(contactDTO.getId());
    return buildContactDTO(ps);
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
