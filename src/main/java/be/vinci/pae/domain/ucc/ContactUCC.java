package be.vinci.pae.domain.ucc;

import be.vinci.pae.domain.dto.ContactDTO;

/**
 * ContactUCC interface.
 */
public interface ContactUCC {

  /**
   * Start the contact and get it.
   *
   * @param company the id of the company.
   * @param student the id of the student.
   * @return the started contact.
   */
  ContactDTO start(int company, int student);

  /**
   * turned down the contact.
   *
   * @param contactId        the id of the contact.
   * @param reasonForRefusal the reason for the refusal
   * @return the started contact.
   */
  ContactDTO turnedDown(int contactId, String reasonForRefusal);

}
