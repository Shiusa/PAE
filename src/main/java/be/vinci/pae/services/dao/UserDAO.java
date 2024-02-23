package be.vinci.pae.services.dao;

import be.vinci.pae.domain.dto.UserDTO;

/**
 * UserDAO Interface
 */
public interface UserDAO {

  UserDTO getOneUserByEmail(String email);

}
