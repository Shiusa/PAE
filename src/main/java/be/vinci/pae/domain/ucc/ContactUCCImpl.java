package be.vinci.pae.domain.ucc;

import be.vinci.pae.domain.dto.ContactDTO;
import be.vinci.pae.domain.dto.UserDTO;
import be.vinci.pae.services.dal.DalServicesConnection;
import be.vinci.pae.services.dao.ContactDAO;
import be.vinci.pae.services.dao.UserDAO;
import be.vinci.pae.utils.exceptions.DuplicateException;
import jakarta.inject.Inject;
import java.util.List;

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
    dalServices.startTransaction();
    UserDTO studentDTO = userDAO.getOneUserById(student);
    String schoolYear = studentDTO.getSchoolYear();
    ContactDTO contactFound = contactDAO
        .findContactByCompanyStudentSchoolYear(company, student, schoolYear);
    if (contactFound.getCompany() == company) {
      dalServices.rollbackTransaction();
      throw new DuplicateException("This contact already exist for this year.");
    }
    ContactDTO contact = contactDAO.startContact(company, student, schoolYear);
    dalServices.commitTransaction();
    return contact;
  }

  /**
   * Get all users.
   *
   * @return a list containing all the users.
   */
  @Override
  public List<ContactDTO> getAllContacts() {
    return contactDAO.getAllContacts();
  }


  /**
   * Get all contacts by a student id.
   *
   * @param student the student id.
   * @return a list containing all the contacts.
   */
  @Override
  public List<ContactDTO> getAllContactsByStudent(int student) {
    return contactDAO.getAllContactsByStudent(student);
  }

  /**
   * Get a contact by his id.
   *
   * @param id the contact id.
   * @return the contact found.
   */
  public ContactDTO getOneById(int id) {
    dalServices.startTransaction();
    ContactDTO contact = contactDAO.getOneContactById(id);
    if (contact == null) {
      dalServices.rollbackTransaction();
      throw new IllegalArgumentException("id unknown");
    }
    dalServices.commitTransaction();
    return contact;
  }
}
