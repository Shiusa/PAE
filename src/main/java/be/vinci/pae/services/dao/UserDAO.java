package be.vinci.pae.services.dao;

import be.vinci.pae.domain.dto.UserDTO;
import java.util.List;

/**
 * UserDAO Interface.
 */
public interface UserDAO {

  /**
   * Get one user by email then set the userDTO if user exist.
   *
   * @param email user' email.
   * @return userDTO with setter corresponding to the email, null otherwise.
   */
  UserDTO getOneUserByEmail(String email);

  /**
   * Get one user by id then set the userDTO if user exist.
   *
   * @param id user' id.
   * @return userDTO with setter corresponding to the id, null otherwise.
   */
  UserDTO getOneUserById(int id);

  /**
   * Get all users.
   *
   * @return a list of all users.
   */
  List<UserDTO> getAllUsers();
}
