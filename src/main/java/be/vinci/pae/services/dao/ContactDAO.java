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
   * Create the contact.
   *
   * @param student    the student.
   * @param company    the company.
   * @param schoolYear the school year.
   * @return the contact created.
   */
  ContactDTO startContact(int company, int student, String schoolYear);

  /**
   * admit the contact.
   *
   * @param contactId the id of the contact.
   * @param meeting   the way how they met.
   * @return the contact updated.
   */
  ContactDTO admitContact(int contactId, String meeting);

  ContactDTO unsupervise(int contactId);

}
