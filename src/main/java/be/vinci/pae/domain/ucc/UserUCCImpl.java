package be.vinci.pae.domain.ucc;

import be.vinci.pae.api.filters.Authorize;
import be.vinci.pae.domain.User;
import be.vinci.pae.domain.dto.UserDTO;
import be.vinci.pae.services.dal.DalServicesConnection;
import be.vinci.pae.services.dao.UserDAO;
import be.vinci.pae.utils.exceptions.InvalidRequestException;
import be.vinci.pae.utils.exceptions.ResourceNotFoundException;
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
    User user;
    UserDTO userDTOFound;

    try {
      dalServices.startTransaction();
      userDTOFound = userDAO.getOneUserByEmail(email);
      user = (User) userDTOFound;
    } catch (Exception e) {
      dalServices.rollbackTransaction();
      throw e;
    }
    if (user == null) {
      throw new ResourceNotFoundException();
    }
    if (!user.checkPassword(password)) {
      throw new InvalidRequestException();
    }
    dalServices.commitTransaction();
    return userDTOFound;
  }

  /**
   * Get all users.
   *
   * @return a list containing all the users.
   */
  @Override
  @Authorize
  public List<UserDTO> getAllUsers() {
    List<UserDTO> userList;
    try {
      dalServices.startTransaction();
      userList = userDAO.getAllUsers();
    } catch (Exception e) {
      dalServices.rollbackTransaction();
      throw e;
    }
    dalServices.commitTransaction();
    return userList;
  }

  /**
   * Get a user by his id.
   *
   * @param id the user id.
   * @return the user found.
   */
  public UserDTO getOneById(int id) {
    UserDTO user;
    try {
      dalServices.startTransaction();
      user = userDAO.getOneUserById(id);
    } catch (Exception e) {
      dalServices.rollbackTransaction();
      throw e;
    }
    if (user == null) {
      throw new ResourceNotFoundException();
    }
    dalServices.commitTransaction();
    return user;
  }
}
