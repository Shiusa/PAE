package be.vinci.pae.domain.ucc;

import be.vinci.pae.domain.dto.UserDTO;
import be.vinci.pae.services.dao.UserDAO;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * User UCC
 */
public class UserUCC {

  private UserDAO userDAO;

  /**
   * Get a user associated with an email and check their password with the password entered.
   *
   * @param email      the user's email.
   * @param motDePasse the user's hashed password.
   * @return a UserDTO if existing user and correct password;.
   */
  public UserDTO login(String email, String motDePasse) {
    UserDTO user;
    user = userDAO.getOneUserByEmail(email);
    if (user == null) {
      throw new WebApplicationException(Response.status(Status.BAD_REQUEST)
          .entity("Ressource not found").type("text/plain").build());
    }
    return user;
  }

}
