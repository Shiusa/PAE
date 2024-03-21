package be.vinci.pae.domain.ucc;

import be.vinci.pae.domain.User;
import be.vinci.pae.domain.dto.UserDTO;
import be.vinci.pae.services.dal.DalServicesConnection;
import be.vinci.pae.services.dao.UserDAO;
import be.vinci.pae.utils.exceptions.BadRequestException;
import be.vinci.pae.utils.exceptions.NotFoundException;
import jakarta.inject.Inject;
import java.util.List;

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

<<<<<<< HEAD
    if (user == null) {
      throw new NotFoundException();
    }
    if (!user.checkPassword(password)) {
      throw new BadRequestException();
    }
    userDTOFound.setPassword(null);
=======
    if (user == null || !user.checkPassword(password)) {
      dalServices.rollbackTransaction();
      return null;
    }

    dalServices.commitTransaction();
>>>>>>> ca4d4212ed873d321e191ccf6ff8d41a4d8533b0
    return userDTOFound;
  }

  /**
<<<<<<< HEAD
   * Get all users.
   *
   * @return a list containing all the users.
   */
  @Override
  public List<UserDTO> getAllUsers() {
    return userDAO.getAllUsers();
=======
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
>>>>>>> ca4d4212ed873d321e191ccf6ff8d41a4d8533b0
  }
}
