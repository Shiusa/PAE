package be.vinci.pae.services.dao;


import be.vinci.pae.domain.ContactFactory;
import be.vinci.pae.domain.dto.ContactDTO;
import be.vinci.pae.services.dal.DalServices;
import be.vinci.pae.utils.Logs;
import be.vinci.pae.utils.exceptions.FatalException;
import jakarta.inject.Inject;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.Level;

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
    Logs.log(Level.INFO, "ContactDAO (findContactByCompanyStudentSchoolYear) : entrance");
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
      Logs.log(Level.FATAL, "ContactDAO (findContactByCompanyStudentSchoolYear) : internal error");
      throw new FatalException(e);
    }

    ContactDTO contact = buildContactDTO(ps);

    try {
      ps.close();
    } catch (SQLException e) {
      Logs.log(Level.FATAL, "ContactDAO (findContactByCompanyStudentSchoolYear) : internal error");
      throw new FatalException(e);
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
    PreparedStatement ps = dalServices.getPreparedStatement(requestSql);
    try {
      ps.setInt(1, company);
      ps.setInt(2, student);
      ps.setString(3, "started");
      ps.setString(4, schoolYear);
    } catch (SQLException e) {
      Logs.log(Level.FATAL, "ContactDAO (startContact) : internal error");
      throw new FatalException(e);
    }

    ContactDTO contact = buildContactDTO(ps);
    try {
      ps.close();
    } catch (SQLException e) {
      Logs.log(Level.FATAL, "ContactDAO (startContact) : internal error");
      throw new FatalException(e);
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
    PreparedStatement ps = dalServices.getPreparedStatement(requestSql);
    try {
      ps.setInt(1, contactId);
    } catch (SQLException e) {
      throw new FatalException(e);
    }
    ContactDTO contact = buildContactDTO(ps);
    try {
      ps.close();
    } catch (SQLException e) {
      throw new FatalException(e);
    }
    return contact;
  }

  @Override
  public ContactDTO getOneContactById(int id) {
    String requestSql = """
        SELECT contact_id, company, student, meeting, contact_state, reason_for_refusal, school_year
        FROM prostage.contacts
        WHERE contact_id = ?
        """;
    PreparedStatement ps = dalServices.getPreparedStatement(requestSql);
    try {
      ps.setInt(1, id);
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
    return buildContactDTO(ps);
  }

  public ContactDTO unsupervise(int contactId) {
    String requestSql = """
        UPDATE proStage.contacts
        SET contact_state = 'unsupervised'
        WHERE contact_id = ?
        RETURNING *;
        """;

    PreparedStatement ps = dalServices.getPreparedStatement(requestSql);
    try {
      ps.setInt(1, contactId);
    } catch (SQLException e) {
      throw new FatalException(e);
    }
    return buildContactDTO(ps);
  }

  @Override
  public List<ContactDTO> getAllContactsByStudent(int student) {
    List<ContactDTO> contactDTOList = new ArrayList<>();

    String requestSql = """
        SELECT contact_id, company, student, meeting, contact_state, reason_for_refusal, school_year
        FROM proStage.contacts 
        WHERE student = ?
        """;

    try (PreparedStatement ps = dalServices.getPreparedStatement(requestSql)) {
      ps.setInt(1, student);
      try (ResultSet rs = ps.executeQuery()) {
        while (rs.next()) {
          ContactDTO contactDTO = contactFactory.getContactDTO();
          contactDTO.setId(rs.getInt(1));
          contactDTO.setCompany(rs.getInt("company"));
          contactDTO.setStudent(rs.getInt("student"));
          contactDTO.setMeeting(rs.getString("meeting"));
          contactDTO.setState(rs.getString("contact_state"));
          contactDTO.setReasonRefusal(rs.getString("reason_for_refusal"));
          contactDTO.setSchoolYear(rs.getString("school_year"));
          contactDTOList.add(contactDTO);
        }
      }
    } catch (SQLException e) {
      throw new FatalException(e);
    }
    return contactDTOList;
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
      return null;
    } catch (SQLException e) {
      Logs.log(Level.FATAL, "CompanyDAO (buildCompanyDTO) : internal error!");
      throw new FatalException(e);
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
