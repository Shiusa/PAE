package be.vinci.pae.domain.ucc;

import be.vinci.pae.domain.dto.ContactDTO;
import be.vinci.pae.domain.dto.UserDTO;
import be.vinci.pae.services.dao.ContactDAO;
import be.vinci.pae.services.dao.UserDAO;
import jakarta.inject.Inject;

/**
 * Implementation of ContactUCC.
 */
public class ContactUCCImpl implements ContactUCC {

  @Inject
  private ContactDAO contactDAO;

  @Inject
  private UserDAO userDAO;

  @Override
  public ContactDTO start(int company, int student) {
    UserDTO studentDTO = userDAO.getOneUserById(student);
    String schoolYear = studentDTO.getSchoolYear();
    ContactDTO contactFound = contactDAO
        .findContactByCompanyStudentSchoolYear(company, student, schoolYear);
    if (contactFound.getCompany() == company) {
      return null;
    }
    return contactDAO.startContact(company, student, schoolYear);
  }
}
