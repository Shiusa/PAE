package be.vinci.pae.domain.ucc;

import be.vinci.pae.domain.Company;
import be.vinci.pae.domain.Contact;
import be.vinci.pae.domain.dto.ContactDTO;
import be.vinci.pae.domain.dto.UserDTO;
import be.vinci.pae.services.dal.DalServices;
import be.vinci.pae.services.dao.CompanyDAO;
import be.vinci.pae.services.dao.ContactDAO;
import be.vinci.pae.services.dao.UserDAO;
import be.vinci.pae.utils.Logs;
import be.vinci.pae.utils.exceptions.DuplicateException;
import be.vinci.pae.utils.exceptions.InvalidRequestException;
import be.vinci.pae.utils.exceptions.NotAllowedException;
import be.vinci.pae.utils.exceptions.ResourceNotFoundException;
import jakarta.inject.Inject;
import java.util.List;
import org.apache.logging.log4j.Level;

/**
 * Implementation of ContactUCC.
 */
public class ContactUCCImpl implements ContactUCC {

  @Inject
  private ContactDAO contactDAO;
  @Inject
  private DalServices dalServices;
  @Inject
  private UserDAO userDAO;
  @Inject
  private CompanyDAO companyDAO;

  @Override
  public ContactDTO start(int company, int studentId) {
    Logs.log(Level.DEBUG, "ContactUCC (start) : entrance");
    ContactDTO contact;
    try {
      dalServices.startTransaction();
      UserDTO studentDTO = userDAO.getOneUserById(studentId);
      if (studentDTO == null) {
        Logs.log(Level.ERROR,
            "ContactUCC (start) : student not found");
        throw new ResourceNotFoundException();
      }
      Company company2 = (Company) companyDAO.getOneCompanyById(company);
      if (company2 == null) {
        Logs.log(Level.ERROR,
            "ContactUCC (start) : company not found");
        throw new ResourceNotFoundException();
      } else if (!company2.studentCanContact()) {
        Logs.log(Level.ERROR,
            "ContactUCC (start) : company is blacklisted");
        throw new InvalidRequestException();
      }

      String schoolYear = studentDTO.getSchoolYear();
      ContactDTO contactFound = contactDAO
          .findContactByCompanyStudentSchoolYear(company, studentId, schoolYear);
      if (contactFound != null) {
        Logs.log(Level.ERROR,
            "ContactUCC (start) : contact already exist with this student, company, year");
        throw new DuplicateException("This contact already exist for this year.");
      }

      contact = contactDAO.startContact(company, studentId, schoolYear);
    } catch (Exception e) {
      dalServices.rollbackTransaction();
      throw e;
    }
    dalServices.commitTransaction();
    Logs.log(Level.DEBUG, "ContactUCC (start) : success!");
    return contact;
  }

  @Override
  public List<ContactDTO> getAllContactsByStudent(int student) {
    List<ContactDTO> listContactDTO;
    try {
      dalServices.startTransaction();
      listContactDTO = contactDAO.getAllContactsByStudent(student);
      if (listContactDTO == null) {
        throw new ResourceNotFoundException();
      }
      dalServices.commitTransaction();
      return listContactDTO;
    } catch (Exception e) {
      dalServices.rollbackTransaction();
      throw e;
    }
  }

  @Override
  public ContactDTO getOneById(int id) {
    ContactDTO contact;
    try {
      dalServices.startTransaction();
      contact = contactDAO.findContactById(id);
      if (contact == null) {
        throw new ResourceNotFoundException();
      }
      dalServices.commitTransaction();
      return contact;
    } catch (Exception e) {
      dalServices.rollbackTransaction();
      throw e;
    }
  }

  @Override
  public ContactDTO unsupervise(int contactId, int student, int version) {
    Contact contact;
    ContactDTO contactDTO;
    try {
      dalServices.startTransaction();
      contact = (Contact) contactDAO.findContactById(contactId);
      if (contact == null) {
        Logs.log(Level.ERROR,
            "ContactUCC (unsupervise) : contact not found");
        throw new ResourceNotFoundException();
      }

      if (!contact.isStarted() && !contact.isAdmitted()) {
        throw new NotAllowedException();
      } else if (contact.getStudent().getId() != student) {
        throw new NotAllowedException();
      }

      contactDTO = contactDAO.unsupervise(contactId, version);

      if (contactDTO.getVersion() != version + 1) {
        Logs.log(Level.ERROR, "ContactUCC (admit) : the contact's version isn't matching");
        throw new InvalidRequestException();
      }

      dalServices.commitTransaction();
      Logs.log(Level.DEBUG, "ContactUCC (unsupervise) : success!");
      return contactDTO;
    } catch (Exception e) {
      dalServices.rollbackTransaction();
      throw e;
    }
  }

  @Override
  public ContactDTO admit(int contactId, String meeting, int studentId, int version) {
    Logs.log(Level.DEBUG, "ContactUCC (admit) : entrance");
    Contact contact;
    ContactDTO contactDTO;
    try {
      dalServices.startTransaction();
      contact = (Contact) contactDAO.findContactById(contactId);
      if (contact == null) {
        Logs.log(Level.ERROR, "ContactUCC (admit) : contact not found");
        throw new ResourceNotFoundException();
      }
      if (contact.getStudent().getId() != studentId) {
        Logs.log(Level.ERROR,
            "ContactUCC (admit) : the student of the contact isn't the student from the token");
        throw new NotAllowedException();
      }
      if (!contact.checkMeeting(meeting)) {
        Logs.log(Level.ERROR, "ContactUCC (admit) : type meeting is invalid");
        throw new InvalidRequestException();
      }
      if (!contact.isStarted()) {
        Logs.log(Level.ERROR, "ContactUCC (admit) : contact's state isn't started");
        throw new InvalidRequestException();
      }
      contactDTO = contactDAO.admitContact(contactId, meeting, version);

      if (contactDTO.getVersion() != version + 1) {
        Logs.log(Level.ERROR, "ContactUCC (admit) : the contact's version isn't matching");
        throw new InvalidRequestException();
      }

      dalServices.commitTransaction();
      Logs.log(Level.DEBUG, "ContactUCC (admit) : success!");
      return contactDTO;
    } catch (Exception e) {
      dalServices.rollbackTransaction();
      throw e;
    }
  }

  @Override
  public ContactDTO turnDown(int contactId, String reasonForRefusal, int studentId) {
    Logs.log(Level.DEBUG, "ContactUCC (turnDown) : entrance");
    Contact contact;
    ContactDTO contactDTO;
    try {
      dalServices.startTransaction();
      contact = (Contact) contactDAO.findContactById(contactId);
      if (contact == null) {
        dalServices.rollbackTransaction();
        Logs.log(Level.ERROR, "ContactUCC (turnDown) : contact not found");
        throw new ResourceNotFoundException();
      }

      int version = contact.getVersion();

      if (contact.getStudent().getId() != studentId) {
        Logs.log(Level.ERROR,
            "ContactUCC (turnDown) : the student of the contact isn't the student from the token");
        throw new NotAllowedException();
      }
      if (!contact.isAdmitted()) {
        Logs.log(Level.ERROR, "ContactUCC (turnDown) : contact's state not admitted");
        throw new NotAllowedException();
      }

      contactDTO = contactDAO.turnDown(contactId, reasonForRefusal, version);

      dalServices.commitTransaction();
      Logs.log(Level.DEBUG, "ContactUCC (turnDown) : success!");
      return contactDTO;
    } catch (Exception e) {
      dalServices.rollbackTransaction();
      throw e;
    }
  }

  @Override
  public void putStudentContactsOnHold(int studentId) {
    Logs.log(Level.DEBUG, "ContactUCC (putStudentContactsOnHold) : entrance");
    try {
      dalServices.startTransaction();
      List<ContactDTO> contactDTOList =
          contactDAO.getAllContactsByStudentStartedOrAdmitted(studentId);
      for (ContactDTO c : contactDTOList) {
        contactDAO.putContactOnHold(c);
      }
      dalServices.commitTransaction();
    } catch (Exception e) {
      dalServices.rollbackTransaction();
      throw e;
    }
  }

  @Override
  public ContactDTO accept(int contactId, int studentId) {
    Logs.log(Level.DEBUG, "ContactUCC (accept) : entrance");
    Contact contact;
    ContactDTO contactDTO;
    try {
      dalServices.startTransaction();
      contact = (Contact) contactDAO.findContactById(contactId);

      if (contact == null) {
        Logs.log(Level.ERROR,
            "ContactUCC (accept) : contact not found");
        throw new ResourceNotFoundException();
      }
      int version = contact.getVersion();

      if (!contact.isAdmitted()) {
        Logs.log(Level.ERROR, "ContactUCC (accept) : contact's state not admitted");
        throw new NotAllowedException();
      } else if (contact.getStudent().getId() != studentId) {
        throw new NotAllowedException();
      }
      contactDTO = contactDAO.accept(contactId, version);
      dalServices.commitTransaction();
      return contactDTO;
    } catch (Exception e) {
      dalServices.rollbackTransaction();
      throw e;
    }
  }

  @Override
  public List<ContactDTO> getAllContactsByCompany(int company) {
    List<ContactDTO> listContactDTO;
    try {
      dalServices.startTransaction();
      listContactDTO = contactDAO.getAllContactsByCompany(company);
      if (listContactDTO == null) {
        throw new ResourceNotFoundException();
      }
      dalServices.commitTransaction();
      return listContactDTO;
    } catch (Exception e) {
      dalServices.rollbackTransaction();
      throw e;
    }
  }
}
