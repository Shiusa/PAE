package be.vinci.pae.domain.ucc;

import be.vinci.pae.domain.Contact;
import be.vinci.pae.domain.dto.ContactDTO;
import be.vinci.pae.domain.dto.UserDTO;
import be.vinci.pae.services.dal.DalServicesConnection;
import be.vinci.pae.services.dao.ContactDAO;
import be.vinci.pae.services.dao.UserDAO;
import be.vinci.pae.utils.exceptions.DuplicateException;
import be.vinci.pae.utils.exceptions.ResourceNotFoundException;
import jakarta.inject.Inject;

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

  @Override
  public ContactDTO start(int company, int student) {
    UserDTO studentDTO;
    String schoolYear;
    ContactDTO contactFound;
    try {
      dalServices.startTransaction();
      studentDTO = userDAO.getOneUserById(student);
      schoolYear = studentDTO.getSchoolYear();
      contactFound = contactDAO.findContactByCompanyStudentSchoolYear(company, student, schoolYear);
    } catch (Exception e) {
      dalServices.rollbackTransaction();
      throw e;
    }
    if (contactFound.getCompany() == company) {
      throw new DuplicateException("This contact already exist for this year.");
    }
    ContactDTO contact = contactDAO.startContact(company, student, schoolYear);
    dalServices.commitTransaction();
    return contact;
  }

  /**
   * Unsupervised the contact.
   *
   * @param contactId the id of the contact.
   * @return the unsupervised state of a contact.
   */
  @Override
  public ContactDTO unsupervise(int contactId) {
    dalServices.startTransaction();
    Contact contact = (Contact) contactDAO.findContactById(contactId);
    if (!contact.isStarted() && !contact.isAdmitted()) {
      dalServices.rollbackTransaction();
      throw new ResourceNotFoundException();
    }
    ContactDTO contactDTO = contactDAO.unsupervise(contactId);
    dalServices.commitTransaction();
    return contactDTO;
  }
}
