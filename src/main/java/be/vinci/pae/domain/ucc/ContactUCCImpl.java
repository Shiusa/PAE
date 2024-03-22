package be.vinci.pae.domain.ucc;

import be.vinci.pae.domain.Contact;
import be.vinci.pae.domain.ContactFactory;
import be.vinci.pae.domain.dto.ContactDTO;
import be.vinci.pae.domain.dto.UserDTO;
import be.vinci.pae.services.dal.DalServicesConnection;
import be.vinci.pae.services.dao.ContactDAO;
import be.vinci.pae.services.dao.UserDAO;
import be.vinci.pae.utils.exceptions.BadRequestException;
import be.vinci.pae.utils.exceptions.DuplicateException;
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
  private ContactFactory contactFactory;


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

  @Override
  public ContactDTO turnDown(int contactId, String reasonForRefusal) {
    Contact contact = (Contact) contactDAO.findContactById(contactId);
    if (!contact.isAdmitted()) {
      throw new BadRequestException();
    }
    return contactDAO.turnDown(contactId, reasonForRefusal);
  }

}
