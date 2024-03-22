package be.vinci.pae.services.dao;

import be.vinci.pae.domain.dto.ContactDTO;

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
   * Find a contact by its company, student and school year.
   *
   * @param contactId the id of the contact.
   * @return a ContactDTO if the contact was found, null otherwise.
   */
  ContactDTO findContactById(int contactId);

  /**
   * Find a contact by its company, student and school year.
   *
   * @param contactId        the id of the contact.
   * @param reasonForRefusal the reason of the refusal.
   * @return a ContactDTO if the update of contact was successful, null otherwise.
   */
  ContactDTO turnedDown(int contactId, String reasonForRefusal);

  /**
   * Create the contact.
   *
   * @param student    the student.
   * @param company    the company.
   * @param schoolYear the school year.
   * @return the contact created.
   */
  ContactDTO startContact(int company, int student, String schoolYear);

}
