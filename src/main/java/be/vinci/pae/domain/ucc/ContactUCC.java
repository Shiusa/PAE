package be.vinci.pae.domain.ucc;

import be.vinci.pae.domain.dto.ContactDTO;

/**
 * ContactUCC interface.
 */
public interface ContactUCC {

  /**
   * Start the contact and get it.
   *
   * @return the started contact.
   */
  ContactDTO start(int company, int student);

  /**
   * Unsupervised the contact.
   *
   * @param companyId the id of the contact.
   * @param student   the id of the student.
   * @return the unsupervised state of a contact.
   */
  ContactDTO unsupervise(int companyId, int student);

}
