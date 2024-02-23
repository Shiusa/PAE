package be.vinci.pae.services.dao;

import be.vinci.pae.domain.dto.UserDTO;

public interface UserDAO {

  UserDTO getOneUserByEmail(String email);

}
