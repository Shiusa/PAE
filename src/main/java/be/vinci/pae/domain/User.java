package be.vinci.pae.domain;

import be.vinci.pae.domain.dto.UserDTO;

public interface User extends UserDTO {

  boolean checkMotDePasse(String motDePasse);

}
