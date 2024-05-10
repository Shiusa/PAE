package be.vinci.pae.services.dao;

import be.vinci.pae.domain.dto.ContactDTO;
import java.util.List;

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
   * Create the contact.
   *
   * @param student    the student.
   * @param company    the company.
   * @param schoolYear the school year.
   * @return the contact created.
   */
  ContactDTO startContact(int company, int student, String schoolYear);


  /**
   * Get all contacts by a student id.
   *
   * @param student student' id.
   * @return a list of all contacts.
   */
  List<ContactDTO> getAllContactsByStudent(int student);

  /**
   * Find a contact by its id.
   *
   * @param contactId the contact id.
   * @return the contact found.
   */
  ContactDTO findContactById(int contactId);

  /**
   * Get all the student's contact that are in started or admitted state.
   *
   * @param student the student to get contacts from.
   * @return a list containing all the contacts in the right state.
   */
  List<ContactDTO> getAllContactsByStudentStartedOrAdmitted(int student);

  /**
   * Put a contact on hold.
   *
   * @param contactDTO the contact to put on hold.
   * @return the updated contactDTO.
   */
  ContactDTO putContactOnHold(ContactDTO contactDTO);

  /**
   * Get all contacts by a company id.
   *
   * @param company company's id.
   * @return a list of all contacts.
   */
  List<ContactDTO> getAllContactsByCompany(int company);

  /**
   * Update a contact.
   *
   * @param contact               contactDTO.
   * @param contactCurrentVersion contact version.
   * @return the updated contact.
   */
  ContactDTO updateContact(ContactDTO contact, int contactCurrentVersion);
}
