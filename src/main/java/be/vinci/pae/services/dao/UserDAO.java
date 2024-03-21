package be.vinci.pae.services.dao;

import be.vinci.pae.domain.dto.UserDTO;
import java.util.List;

/**
 * UserDAO Interface.
 */
public interface UserDAO {

  UserDTO getOneUserByEmail(String email);

<<<<<<< HEAD
  public UserDTO getOneUserById(int id);

=======
  List<UserDTO> getAllUsers();
>>>>>>> 5e06ee49c01fe22c5a684f1a75f9543cad569462
}
