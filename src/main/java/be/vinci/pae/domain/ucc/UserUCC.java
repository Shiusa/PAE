package be.vinci.pae.domain.ucc;

import be.vinci.pae.domain.dto.UserDTO;
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
  UserDTO login(String email, String password);

  /**
   * Get all users.
   *
   * @param user the user.
   * @return a list containing all the users.
   */
  List<UserDTO> getAllUsers(UserDTO user);

  /**
   * Get a user by his id.
   *
   * @param id the user id.
   * @return the user if found.
   */
  UserDTO getOneById(int id);

  /**
   * Register a user.
   *
   * @param user user to register.
   * @return a UserDTO of registered user.
   */
  UserDTO register(UserDTO user);

  /**
   * Edit one user.
   *
   * @param user the user to edit.
   * @return the edited user.
   */
  UserDTO editOneUser(UserDTO user);

  UserDTO editPassword(int id, String oldPassword, String newPassword);
}
