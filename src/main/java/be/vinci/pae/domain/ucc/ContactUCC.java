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

  ContactDTO turnedDown(int contact_id, String contact_state, String reasonForRefusal);

}
