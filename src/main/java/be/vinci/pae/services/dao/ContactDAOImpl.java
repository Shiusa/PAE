package be.vinci.pae.services.dao;


import be.vinci.pae.domain.ContactFactory;
import be.vinci.pae.domain.dto.ContactDTO;
import be.vinci.pae.services.dal.DalBackendServices;
import be.vinci.pae.utils.Logs;
import be.vinci.pae.utils.exceptions.DuplicateException;
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
  private DalBackendServices dalBackendServices;
  @Inject
  private ContactFactory contactFactory;

  @Override
  public ContactDTO findContactByCompanyStudentSchoolYear(int company, int studentId,
      String schoolYear) {
    Logs.log(Level.INFO, "ContactDAO (findContactByCompanyStudentSchoolYear) : entrance");
    String requestSql = """
        SELECT *
        FROM prostage.contacts
        WHERE contacts.company = ? AND contacts.student = ? AND contacts.school_year = ?
        """;
    PreparedStatement ps = dalBackendServices.getPreparedStatement(requestSql);

    try {
      ps.setInt(1, company);
      ps.setInt(2, studentId);
      ps.setString(3, schoolYear);
    } catch (SQLException e) {
      throw new FatalException(e);
    }

    ContactDTO contact = buildContactDTO(ps);

    try {
      ps.close();
    } catch (SQLException e) {
      throw new FatalException(e);
    }

    Logs.log(Level.DEBUG, "ContactDAO (findContactByCompanyStudentSchoolYear) : success!");
    return contact;
  }

  @Override
  public ContactDTO startContact(int company, int studentId, String schoolYear) {
    Logs.log(Level.INFO, "ContactDAO (startContact) : entrance");
    String requestSql = """
        INSERT INTO prostage.contacts (company, student, contact_state, school_year, version)
         VALUES (?, ?, ?, ?, ?) RETURNING *;
        """;
    PreparedStatement ps = dalBackendServices.getPreparedStatement(requestSql);
    try {
      ps.setInt(1, company);
      ps.setInt(2, studentId);
      ps.setString(3, "initié");
      ps.setString(4, schoolYear);
      ps.setInt(5, 1);
    } catch (SQLException e) {
      throw new FatalException(e);
    }

    ContactDTO contact = buildContactDTO(ps);
    try {
      ps.close();
    } catch (SQLException e) {
      throw new FatalException(e);
    }
    Logs.log(Level.DEBUG, "ContactDAO (startContact) : success!");
    return contact;
  }

  @Override
  public ContactDTO findContactById(int contactId) {
    Logs.log(Level.INFO, "ContactDAO (findContactById) : entrance");
    String requestSql = """
        SELECT *
        FROM prostage.contacts
        WHERE contacts.contact_id = ?
        """;
    PreparedStatement ps = dalBackendServices.getPreparedStatement(requestSql);
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

    Logs.log(Level.DEBUG, "ContactDAO (findContactById) : success!");
    return contact;
  }


  @Override
  public ContactDTO unsupervise(int contactId, int version) {
    String requestSql = """
        UPDATE proStage.contacts
        SET contact_state = ? , version = ?
        WHERE contact_id = ? AND version = ?
        RETURNING *;
        """;

    PreparedStatement ps = dalBackendServices.getPreparedStatement(requestSql);
    try {
      ps.setString(1, "non suivi");
      ps.setInt(2, version + 1);
      ps.setInt(3, contactId);
      ps.setInt(4, version);
    } catch (SQLException e) {
      throw new FatalException(e);
    }
    return buildContactDTO(ps);
  }

  @Override
  public List<ContactDTO> getAllContactsByStudent(int student) {
    List<ContactDTO> contactDTOList = new ArrayList<>();

    String requestSql = """
        SELECT ct.contact_id, ct.student, cp.name, cp.designation, ct.company, ct.meeting,
        ct.contact_state, ct.reason_for_refusal, ct.school_year
        FROM proStage.contacts ct
        LEFT JOIN proStage.companies cp ON cp.company_id = ct.company
        WHERE ct.student = ?
        """;

    try (PreparedStatement ps = dalBackendServices.getPreparedStatement(requestSql)) {
      ps.setInt(1, student);
      try (ResultSet rs = ps.executeQuery()) {
        while (rs.next()) {
          ContactDTO contactDTO = contactFactory.getContactDTO();
          contactDTO.setId(rs.getInt(1));
          contactDTO.setCompany(rs.getInt("company"));
          contactDTO.setStudent(rs.getInt("student"));
          contactDTO.setNameCompany(rs.getString("name"));
          contactDTO.setDesignationCompany(rs.getString("designation"));
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
        contact.setVersion(rs.getInt("version"));
        rs.close();
        return contact;
      }
      return null;
    } catch (SQLException e) {
      throw new DuplicateException();
    } finally {
      try {
        ps.close();
      } catch (SQLException e) {
        throw new FatalException(e);
      }
    }
  }

  @Override
  public ContactDTO admitContact(int contactId, String meeting, int version) {
    Logs.log(Level.INFO, "ContactDAO (admit) : entrance");
    String requestSql = """
        UPDATE proStage.contacts
        SET meeting = ?, contact_state = ?, version = ?
        WHERE contact_id = ? AND version = ?
        RETURNING *;
        """;
    PreparedStatement ps = dalBackendServices.getPreparedStatement(requestSql);
    try {
      ps.setString(1, meeting);
      ps.setString(2, "pris");
      ps.setInt(3, version + 1);
      ps.setInt(4, contactId);
      ps.setInt(5, version);
    } catch (SQLException e) {
      throw new FatalException(e);
    }
    ContactDTO contact = buildContactDTO(ps);
    try {
      ps.close();
    } catch (SQLException e) {
      throw new FatalException(e);
    }
    Logs.log(Level.DEBUG, "ContactDAO (admit) : success!");
    return contact;
  }

  @Override
  public ContactDTO turnDown(int contactId, String reasonForRefusal, int version) {
    Logs.log(Level.INFO, "ContactDAO (turnDown) : entrance");
    String requestSql = """
                UPDATE proStage.contacts
                SET reason_for_refusal = ?, contact_state = ?, version = ?
                WHERE contact_id = ? AND version = ?
                RETURNING *;
                
        """;
    PreparedStatement ps = dalBackendServices.getPreparedStatement(requestSql);
    try {
      ps.setString(1, reasonForRefusal);
      ps.setString(2, "refusé");
      ps.setInt(3, version + 1);
      ps.setInt(4, contactId);
      ps.setInt(5, version);
    } catch (SQLException e) {
      throw new FatalException(e);
    }

    ContactDTO contact = buildContactDTO(ps);

    try {
      ps.close();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
    Logs.log(Level.DEBUG, "ContactDAO (turnDown) : success!");
    return contact;
  }
}
