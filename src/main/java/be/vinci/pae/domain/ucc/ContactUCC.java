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
   * take a meeting with the contact on site or remote. it checks if meeting is on site or remote,
   * if else return false
   *
   * @param contactId the id of the contact
   * @param meeting   the type of the meeting either on site or remote
   * @return the started contact.
   */
  ContactDTO admitted(int contactId, String meeting);
}
