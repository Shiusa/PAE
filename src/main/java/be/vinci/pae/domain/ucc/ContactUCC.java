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
   * @param company   the company.
   * @param studentId the student.
   * @return the started contact.
   */
  ContactDTO start(int company, int studentId);

  /**
   * take a meeting with the contact on site or remote. it checks if meeting is on site or remote,
   * if else return false.
   *
   * @param contactId the id of the contact.
   * @param meeting   the type of the meeting either on site or remote.
   * @param studentId the id of the student.
   * @return the started contact.
   */
  ContactDTO admit(int contactId, String meeting, int studentId);


  /**
   * Get all contacts by a student id.
   *
   * @param student the student.
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
   * @param studentId the id of the student.
   * @return the unsupervised state of a contact.
   */
  ContactDTO unsupervise(int contactId, int studentId);

  /**
   * turn down the contact.
   *
   * @param contactId        the id of the contact.
   * @param reasonForRefusal the reason for the refusal.
   * @param studentId        the id of the student.
   * @return the started contact.
   */
  ContactDTO turnDown(int contactId, String reasonForRefusal, int studentId);
}
