package be.vinci.pae.services.dao;

import be.vinci.pae.domain.dto.UserDTO;
import be.vinci.pae.utils.exceptions.FatalException;
import java.util.List;

/**
 * UserDAO Interface.
 */
public interface UserDAO {

  UserDTO getOneUserByEmail(String email) throws FatalException;

  List<UserDTO> getAllUsers() throws FatalException;
}
