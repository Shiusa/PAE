package be.vinci.pae.services.dao;

import be.vinci.pae.domain.User;
import be.vinci.pae.domain.dto.UserDTO;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * UserDAO Interface.
 */
public interface UserDAO {

  UserDTO getOneUserByEmail(String email);

  ObjectNode login(User user);

}
