package be.vinci.pae.services.dao;

import be.vinci.pae.domain.dto.ContactDTO;

/**
 * ContactDAO interface.
 */
public interface ContactDAO {

  /**
   * Create the contact.
   *
   * @param student the student.
   * @param company the company.
   * @return the contact created.
   */
  ContactDTO startContact(int student, int company);

  /**
   * meeting the contact.
   *
   * @param idContact the id of the contact.
   * @param meeting   the way how they met.
   * @return the contact updated.
   */
  ContactDTO meetingContact(int idContact, String meeting);
}
