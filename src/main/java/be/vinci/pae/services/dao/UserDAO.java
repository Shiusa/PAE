package be.vinci.pae.services.dao;

import be.vinci.pae.domain.dto.UserDTO;
import java.util.List;

/**
 * UserDAO Interface.
 */
public interface UserDAO {

  UserDTO getOneUserByEmail(String email);

  List<UserDTO> getAllUsers();
}
