package be.vinci.pae.domain.ucc;

import be.vinci.pae.domain.Contact;
import be.vinci.pae.domain.ContactFactory;
import be.vinci.pae.domain.dto.ContactDTO;
import be.vinci.pae.services.dao.ContactDAO;
import jakarta.inject.Inject;

/**
 * Implementation of ContactUCC.
 */
public class ContactUCCImpl implements ContactUCC {

  @Inject
  private ContactDAO contactDAO;

  //@Inject
  //private UserDAO userDAO;
  @Inject
  private ContactFactory contactFactory;

  @Override
  public ContactDTO start(int student, int company, String schoolYear) {
    return null;
  }

  @Override
  public ContactDTO start(int student, int company) {
    /*UserDTO studentDTO = userDAO.getOneUserById(student);
    String schoolYear = studentDTO.getSchoolYear();
    if (contactDAO.findContactByCompanyStudentSchoolYear(student, company, schoolYear) != null) {
     */
    return null;
  }

  /*
      contactDAO.startContact(student, company, schoolYear);
      return contactDAO.findContactByCompanyStudentSchoolYear(student, company, schoolYear);
    }
   */
  @Override
  public ContactDTO admitted(int idContact, String meeting) {
    System.out.println("oueueoeuoeueoueoeu");
    Contact contact = (Contact) contactFactory.getContactDTO();
    if (!contact.checkMeeting(meeting)) {
      return null;
    }
    return contactDAO.meetingContact(idContact, meeting);
  }
}
