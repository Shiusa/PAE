package be.vinci.pae.domain;

import be.vinci.pae.domain.dto.UserDTO;

/**
 * User interface inheriting the UserDTO interface and containing business methods.
 */
public interface User extends UserDTO {

  /**
   * Compare a raw password with a User's encrypted password.
   *
   * @param motDePasse encrypted password.
   * @return true if the passwords are matching; else false.
   */
  boolean checkMotDePasse(String motDePasse);

  /**
   * Hashes user's password.
   */
  void hasherMotDePasse();

}