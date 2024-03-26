package be.vinci.pae.domain.ucc;

import be.vinci.pae.domain.Company;
import be.vinci.pae.domain.Contact;
import be.vinci.pae.domain.dto.ContactDTO;
import be.vinci.pae.domain.dto.UserDTO;
import be.vinci.pae.services.dal.DalServicesConnection;
import be.vinci.pae.services.dao.CompanyDAO;
import be.vinci.pae.services.dao.ContactDAO;
import be.vinci.pae.services.dao.UserDAO;
import be.vinci.pae.utils.Logs;
import be.vinci.pae.utils.exceptions.DuplicateException;
import be.vinci.pae.utils.exceptions.FatalException;
import be.vinci.pae.utils.exceptions.InvalidRequestException;
import be.vinci.pae.utils.exceptions.NotAllowedException;
import be.vinci.pae.utils.exceptions.ResourceNotFoundException;
import jakarta.inject.Inject;
import org.apache.logging.log4j.Level;

/**
 * Implementation of ContactUCC.
 */
public class ContactUCCImpl implements ContactUCC {

  @Inject
  private ContactDAO contactDAO;
  @Inject
  private DalServicesConnection dalServices;
  @Inject
  private UserDAO userDAO;
  @Inject
  private CompanyDAO companyDAO;

  @Override
  public ContactDTO start(int company, int student) {
    Logs.log(Level.DEBUG, "ContactUCC (start) : entrance");
    ContactDTO contact;
    try {
      dalServices.startTransaction();
      UserDTO studentDTO = userDAO.getOneUserById(student);
      if (studentDTO == null) {
        dalServices.rollbackTransaction();
        Logs.log(Level.ERROR,
            "ContactUCC (start) : student not found");
        throw new ResourceNotFoundException();
      }
      Company company2 = (Company) companyDAO.getOneCompanyById(company);
      if (company2 == null) {
        dalServices.rollbackTransaction();
        Logs.log(Level.ERROR,
            "ContactUCC (start) : company not found");
        throw new ResourceNotFoundException();
      } else if (!company2.studentCanContact()) {
        dalServices.rollbackTransaction();
        Logs.log(Level.ERROR,
            "ContactUCC (start) : company is blacklisted");
        throw new InvalidRequestException();
      }

      String schoolYear = studentDTO.getSchoolYear();
      ContactDTO contactFound = contactDAO
          .findContactByCompanyStudentSchoolYear(company, student, schoolYear);
      if (contactFound != null) {
        dalServices.rollbackTransaction();
        Logs.log(Level.ERROR,
            "ContactUCC (start) : contact already exist with this student, company, year");
        throw new DuplicateException("This contact already exist for this year.");
      }

      contact = contactDAO.startContact(company, student, schoolYear);
    } catch (FatalException e) {
      dalServices.rollbackTransaction();
      throw e;
    }
    dalServices.commitTransaction();
    Logs.log(Level.DEBUG, "ContactUCC (start) : success!");
    return contact;
  }

  @Override
  public ContactDTO unsupervise(int contactId, int student) {
    Contact contact;
    ContactDTO contactDTO;
    try {
      dalServices.startTransaction();
      contact = (Contact) contactDAO.findContactById(contactId);
      if (contact == null) {
        dalServices.rollbackTransaction();
        Logs.log(Level.ERROR,
            "ContactUCC (unsupervise) : contact not found");
        throw new ResourceNotFoundException();
      }
      contactDTO = contactDAO.unsupervise(contactId);
    } catch (FatalException e) {
      dalServices.rollbackTransaction();
      throw e;
    }
    if (!contact.isStarted() && !contact.isAdmitted()) {
      dalServices.rollbackTransaction();
      throw new InvalidRequestException();
    } else if (contact.getStudent() != student) {
      dalServices.rollbackTransaction();
      throw new NotAllowedException();
    }
    dalServices.commitTransaction();
    Logs.log(Level.DEBUG, "ContactUCC (unsupervise) : success!");
    return contactDTO;
  }
}
