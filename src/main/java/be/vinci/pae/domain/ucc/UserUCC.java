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
<<<<<<< HEAD
   * Get all users.
   *
   * @return a list containing all the users.
   */
  List<UserDTO> getAllUsers();
=======
   * Get a user by his id.
   *
   * @param id the user id.
   * @return the user if found.
   */
  UserDTO getOneById(int id);
>>>>>>> ca4d4212ed873d321e191ccf6ff8d41a4d8533b0
}
