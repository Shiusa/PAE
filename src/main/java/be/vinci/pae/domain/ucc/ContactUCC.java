package be.vinci.pae.domain.ucc;

import be.vinci.pae.domain.dto.ContactDTO;
import java.util.List;

/**
 * ContactUCC interface.
 */
public interface ContactUCC {

  /**
   * Start the contact and get it.
   *
   * @param company the company.
   * @param student the student.
   * @return the started contact.
   */
  ContactDTO start(int company, int student);


  /**
   * Get all contacts by a student id.
   *
   * @return a list containing all the contacts.
   */
  List<ContactDTO> getAllContactsByStudent(int student);


  /**
   * Get a contact by his id.
   *
   * @return the contact found.
   */
  ContactDTO getOneById(int id);

  /**
   * Unsupervised the contact.
   *
   * @param contactId the id of the contact.
   * @param student   the id of the student.
   * @return the unsupervised state of a contact.
   */
  ContactDTO unsupervise(int contactId, int student);
}
