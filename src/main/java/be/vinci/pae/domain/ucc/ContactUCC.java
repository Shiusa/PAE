package be.vinci.pae.domain.ucc;

import be.vinci.pae.domain.dto.ContactDTO;
import be.vinci.pae.domain.dto.UserDTO;
import java.util.List;

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
   * Get all contacts.
   *
   * @return a list containing all the contacts.
   */
  List<ContactDTO> getAllContacts();


  /**
   * Get all contacts by a student id.
   *
   * @return a list containing all the contacts.
   */
  List<ContactDTO> getAllContactsByStudent(int student);



  /**
   * Get a contact by his id.
   *
   * @return the contact found.
   */
  ContactDTO getOneById(int id);

}
