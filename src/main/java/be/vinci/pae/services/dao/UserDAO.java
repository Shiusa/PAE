package be.vinci.pae.services.dao;

import be.vinci.pae.domain.dto.UserDTO;

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
   * Add one user.
   *
   * @param user  user to add.
   * @return added user.
   */
  UserDTO addOneUser(UserDTO user);

}
