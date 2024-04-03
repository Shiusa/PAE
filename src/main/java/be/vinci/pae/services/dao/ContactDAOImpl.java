package be.vinci.pae.services.dao;


import be.vinci.pae.domain.CompanyFactory;
import be.vinci.pae.domain.ContactFactory;
import be.vinci.pae.domain.UserFactory;
import be.vinci.pae.domain.dto.CompanyDTO;
import be.vinci.pae.domain.dto.ContactDTO;
import be.vinci.pae.domain.dto.UserDTO;
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
  @Inject
  private CompanyFactory companyFactory;
  @Inject
  private UserFactory userFactory;

  @Override
  public ContactDTO findContactByCompanyStudentSchoolYear(int company, int studentId,
      String schoolYear) {
    Logs.log(Level.INFO, "ContactDAO (findContactByCompanyStudentSchoolYear) : entrance");
    String requestSql = """
        SELECT ct.contact_id, ct.company AS ct_company, ct.student, ct.meeting, ct.contact_state,
        ct.reason_for_refusal, ct.school_year AS ct_school_year, ct.version AS ct_version,
        cm.company_id, cm.name, cm.designation, cm.address, cm.phone_number AS cm_phone_number,
        cm.email AS cm_email, cm.is_blacklisted, cm.blacklist_motivation, cm.version AS cm_version,
        us.user_id, us.email AS us_email, us.lastname AS us_lastname, us.firstname AS us_firstname,
        us.phone_number AS us_phone_number, us.password, us.registration_date,
        us.school_year AS us_school_year, us.role
        FROM prostage.contacts ct, prostage.companies cm, prostage.users us
        WHERE ct.company = ? AND ct.student = ? AND ct.school_year = ?
        AND ct.company = cm.company_id AND ct.student = us.user_id
        """;

    try (PreparedStatement ps = dalBackendServices.getPreparedStatement(requestSql)) {
      ps.setInt(1, company);
      ps.setInt(2, studentId);
      ps.setString(3, schoolYear);
      ContactDTO contact = buildContactDTO(ps);

      Logs.log(Level.DEBUG, "ContactDAO (findContactByCompanyStudentSchoolYear) : success!");
      return contact;
    } catch (SQLException e) {
      Logs.log(Level.FATAL, "ContactDAO (findContactByCompanyStudentSchoolYear) : internal error");
      throw new FatalException(e);
    }

  }

  @Override
  public ContactDTO startContact(int company, int studentId, String schoolYear) {
    Logs.log(Level.INFO, "ContactDAO (startContact) : entrance");
    String requestSql = """
        INSERT INTO prostage.contacts (company, student, contact_state, school_year, version)
         VALUES (?, ?, ?, ?, ?) RETURNING *;
        """;

    try (PreparedStatement ps = dalBackendServices.getPreparedStatement(requestSql)) {
      ps.setInt(1, company);
      ps.setInt(2, studentId);
      ps.setString(3, "initié");
      ps.setString(4, schoolYear);
      ps.setInt(5, 1);
      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          ContactDTO contact = findContactById(rs.getInt("contact_id"));

          Logs.log(Level.DEBUG, "ContactDAO (startContact) : success!");
          return contact;
        }
        return null;
      }
    } catch (SQLException e) {
      Logs.log(Level.FATAL, "ContactDAO (startContact) : internal error");
      throw new FatalException(e);
    }

  }

  @Override
  public ContactDTO findContactById(int contactId) {
    Logs.log(Level.INFO, "ContactDAO (findContactById) : entrance");
    String requestSql = """
        SELECT ct.contact_id, ct.company AS ct_company, ct.student, ct.meeting, ct.contact_state,
        ct.reason_for_refusal, ct.school_year AS ct_school_year, ct.version AS ct_version,
        cm.company_id, cm.name, cm.designation, cm.address, cm.phone_number AS cm_phone_number,
        cm.email AS cm_email, cm.is_blacklisted, cm.blacklist_motivation, cm.version AS cm_version,
        us.user_id, us.email AS us_email, us.lastname AS us_lastname, us.firstname AS us_firstname,
        us.phone_number AS us_phone_number, us.password, us.registration_date,
        us.school_year AS us_school_year, us.role
        FROM prostage.contacts ct, prostage.companies cm, prostage.users us
        WHERE ct.contact_id = ? AND ct.company = cm.company_id AND ct.student = us.user_id
        """;

    try (PreparedStatement ps = dalBackendServices.getPreparedStatement(requestSql)) {
      ps.setInt(1, contactId);
      ContactDTO contact = buildContactDTO(ps);

      Logs.log(Level.DEBUG, "ContactDAO (findContactById) : success!");
      return contact;
    } catch (SQLException e) {
      Logs.log(Level.FATAL, "ContactDAO (findContactById) : internal error");
      throw new FatalException(e);
    }
  }


  @Override
  public ContactDTO unsupervise(int contactId, int version) {
    Logs.log(Level.DEBUG, "ContactDAO (unsupervise) : entrance");
    String requestSql = """
        UPDATE proStage.contacts
        SET contact_state = ? , version = ?
        WHERE contact_id = ? AND version = ?
        RETURNING *;
        """;

    try (PreparedStatement ps = dalBackendServices.getPreparedStatement(requestSql)) {
      ps.setString(1, "non suivi");
      ps.setInt(2, version + 1);
      ps.setInt(3, contactId);
      ps.setInt(4, version);
      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          ContactDTO contact = findContactById(rs.getInt("contact_id"));

          Logs.log(Level.DEBUG, "ContactDAO (unsupervise) : success!");
          return contact;
        }
        return null;
      }
    } catch (SQLException e) {
      Logs.log(Level.FATAL, "ContactDAO (unsupervise) : internal error");
      throw new FatalException(e);
    }
  }

  @Override
  public List<ContactDTO> getAllContactsByStudent(int student) {
    List<ContactDTO> contactDTOList = new ArrayList<>();

    String requestSql = """
        SELECT ct.contact_id, ct.company AS ct_company, ct.student, ct.meeting, ct.contact_state,
        ct.reason_for_refusal, ct.school_year AS ct_school_year, ct.version AS ct_version,
        cm.company_id, cm.name, cm.designation, cm.address, cm.phone_number AS cm_phone_number,
        cm.email AS cm_email, cm.is_blacklisted, cm.blacklist_motivation, cm.version AS cm_version,
        us.user_id, us.email AS us_email, us.lastname AS us_lastname, us.firstname AS us_firstname,
        us.phone_number AS us_phone_number, us.password, us.registration_date,
        us.school_year AS us_school_year, us.role
        FROM prostage.contacts ct, prostage.companies cm, prostage.users us
        WHERE ct.student = ? AND cm.company_id = ct.company AND ct.student = us.user_id
        """;

    try (PreparedStatement ps = dalBackendServices.getPreparedStatement(requestSql)) {
      ps.setInt(1, student);
      try (ResultSet rs = ps.executeQuery()) {
        while (rs.next()) {
          CompanyDTO companyDTO = setCompanyDTO(rs);
          UserDTO studentDTO = setUserDTO(rs);
          ContactDTO contactDTO = setContactDTO(rs, companyDTO, studentDTO);
          contactDTOList.add(contactDTO);
        }
      }
      return contactDTOList;
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

  private ContactDTO buildContactDTO(PreparedStatement ps) {
    try (ResultSet rs = ps.executeQuery()) {
      if (rs.next()) {
        CompanyDTO companyDTO = setCompanyDTO(rs);
        UserDTO studentDTO = setUserDTO(rs);
        return setContactDTO(rs, companyDTO, studentDTO);
      }
      return null;
    } catch (SQLException e) {
      Logs.log(Level.FATAL, "ContactDAO (buildContactDTO) : internal error!");
      throw new DuplicateException();
    }
  }

  @Override
  public ContactDTO admitContact(int contactId, String meeting, int version) {
    Logs.log(Level.DEBUG, "ContactDAO (admit) : entrance");
    String requestSql = """
        UPDATE proStage.contacts
        SET meeting = ?, contact_state = ?, version = ?
        WHERE contact_id = ? AND version = ?
        RETURNING *;
        """;

    try (PreparedStatement ps = dalBackendServices.getPreparedStatement(requestSql)) {
      ps.setString(1, meeting);
      ps.setString(2, "pris");
      ps.setInt(3, version + 1);
      ps.setInt(4, contactId);
      ps.setInt(5, version);
      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          ContactDTO contact = findContactById(rs.getInt("contact_id"));

          Logs.log(Level.DEBUG, "ContactDAO (admit) : success!");
          return contact;
        }
        return null;
      }
    } catch (SQLException e) {
      Logs.log(Level.FATAL, "ContactDAO (admit) : internal error");
      e.printStackTrace();
      throw new FatalException(e);
    }
  }

  @Override
  public ContactDTO turnDown(int contactId, String reasonForRefusal, int version) {
    Logs.log(Level.DEBUG, "ContactDAO (turnDown) : entrance");
    String requestSql = """
        UPDATE proStage.contacts
        SET reason_for_refusal = ?, contact_state = ?, version = ?
        WHERE contact_id = ? AND version = ?
        RETURNING *;
        """;

    try (PreparedStatement ps = dalBackendServices.getPreparedStatement(requestSql)) {
      ps.setString(1, reasonForRefusal);
      ps.setString(2, "refusé");
      ps.setInt(3, version + 1);
      ps.setInt(4, contactId);
      ps.setInt(5, version);
      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          ContactDTO contact = findContactById(rs.getInt("contact_id"));

          Logs.log(Level.DEBUG, "ContactDAO (turnDown) : success!");
          return contact;
        }
        return null;
      }
    } catch (SQLException e) {
      Logs.log(Level.FATAL, "ContactDAO (turnDown) : internal error");
      throw new FatalException(e);
    }
  }
}
