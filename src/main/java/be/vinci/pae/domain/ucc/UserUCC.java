package be.vinci.pae.domain.ucc;

import be.vinci.pae.domain.dto.UserDTO;
import be.vinci.pae.utils.exceptions.BadRequestException;
import java.util.List;

/**
 * UserUCC Interface.
 */
public interface UserUCC {

  /**
   * Get a user associated with an email and check their password with the password entered.
   *
   * @param email    the user's email.
   * @param password the user's hashed password.
   * @return a UserDTO if existing user and correct password;.
   */
  UserDTO login(String email, String password) throws BadRequestException;

  /**
   * Get all users.
   *
   * @return a list containing all the users.
   */
  List<UserDTO> getAllUsers();
}
