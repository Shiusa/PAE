package be.vinci.pae.domain.ucc;

import be.vinci.pae.domain.User;
import be.vinci.pae.domain.dto.UserDTO;
import be.vinci.pae.services.dal.DalServicesConnection;
import be.vinci.pae.services.dao.UserDAO;
import jakarta.inject.Inject;

/**
 * User UCC.
 */
public class UserUCCImpl implements UserUCC {

  @Inject
  private UserDAO userDAO;
  @Inject
  private DalServicesConnection dalServices;

  /**
   * Get a user associated with an email and check their password with the password entered.
   *
   * @param email    the user's email.
   * @param password the user's hashed password.
   * @return a UserDTO if existing user and correct password;.
   */
  @Override
  public UserDTO login(String email, String password) {
    dalServices.startTransaction();

    UserDTO userDTOFound = userDAO.getOneUserByEmail(email);

    User user = (User) userDTOFound;

    if (user == null || !user.checkPassword(password)) {
      dalServices.rollbackTransaction();
      return null;
    }

    dalServices.commitTransaction();
    return userDTOFound;
  }

  /**
   * Get a user by his id.
   *
   * @param id the user id.
   * @return the user found.
   */
  public UserDTO getOneById(int id) {
    dalServices.startTransaction();
    UserDTO user = userDAO.getOneUserById(id);
    if (user == null) {
      dalServices.rollbackTransaction();
      throw new IllegalArgumentException("id unknown");
    }
    dalServices.commitTransaction();
    return user;
  }
}
