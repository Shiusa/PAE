package be.vinci.pae.domain.ucc;

import be.vinci.pae.domain.dto.ContactDTO;

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
   * Unsupervised the contact.
   *
   * @param contactId the id of the contact.
   * @param student   the id of the student.
   * @return the unsupervised state of a contact.
   */
  ContactDTO unsupervise(int contactId, int student);

}
