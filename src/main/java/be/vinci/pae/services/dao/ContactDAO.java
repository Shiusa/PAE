package be.vinci.pae.services.dao;

import be.vinci.pae.domain.dto.ContactDTO;
import java.util.List;

/**
 * ContactDAO interface.
 */
public interface ContactDAO {

  /**
   * Find a contact by its company, student and school year.
   *
   * @param company    the company.
   * @param student    the student.
   * @param schoolYear the school year.
   * @return a ContactDTO if the contact was found, null otherwise.
   */
  ContactDTO findContactByCompanyStudentSchoolYear(int company, int student, String schoolYear);

  /**
   * Create the contact.
   *
   * @param student    the student.
   * @param company    the company.
   * @param schoolYear the school year.
   * @return the contact created.
   */
  ContactDTO startContact(int company, int student, String schoolYear);

  /**
   * Get one contact by id then set the contactDTO if contact exist.
   *
   * @param id contact' id.
   * @return contactDTO with setter corresponding to the id, null otherwise.
   */
  ContactDTO getOneContactById(int id);

  /**
   * Get all contacts by a student id.
   *
   * @param student student' id.
   * @return a list of all contacts.
   */
  List<ContactDTO> getAllContactsByStudent(int student);

  /**
   * Find a contact by its id.
   *
   * @param contactId the contact id.
   * @return the contact found.
   */
  ContactDTO findContactById(int contactId);

  /**
   * admit the contact.
   *
   * @param contactId the id of the contact.
   * @param meeting   the way how they met.
   * @return the contact updated.
   */
  ContactDTO admitContact(int contactId, String meeting);

  /**
   * Unsupervise the contact.
   *
   * @param contactId the contact id.
   * @return the unsupervised contact.
   */
  ContactDTO unsupervise(int contactId);

  /**
   * Turn down a contact and give the reason for refusal.
   *
   * @param contactId        the id of the contact.
   * @param reasonForRefusal the reason of the refusal.
   * @return a ContactDTO if the update of contact was successful, null otherwise.
   */
  ContactDTO turnDown(int contactId, String reasonForRefusal);
}
