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
    if (contactDAO.findContactByCompanyStudentSchoolYear(student, company, schoolYear) != null) {
      return null;
    }

    contactDAO.startContact(student, company, schoolYear);
    return contactDAO.findContactByCompanyStudentSchoolYear(student, company, schoolYear);
  }
}
