package be.vinci.pae.domain.ucc;

import be.vinci.pae.domain.dto.ContactDTO;

public interface ContactUCC {

  /**
   * Start the contact and get it.
   *
   * @return the started contact.
   */
  ContactDTO start(int student, int company, String schoolYear);

  ContactDTO start(int student, int company);

  /**
   * take a meeting with the contact on site or remote. it checks if meeting is on site or remote,
   * if else return false
   *
   * @param idContact the id of the contact
   * @param meeting   the type of the meeting either on site or remote
   * @return the started contact.
   */
  ContactDTO admitted(int idContact, String meeting);
}
