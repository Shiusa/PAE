package be.vinci.pae.domain.ucc;

import be.vinci.pae.api.filters.Authorize;
import be.vinci.pae.domain.User;
import be.vinci.pae.domain.dto.UserDTO;
import be.vinci.pae.services.dal.DalServicesConnection;
import be.vinci.pae.services.dao.UserDAO;
import be.vinci.pae.utils.Logs;
import be.vinci.pae.utils.exceptions.ResourceNotFoundException;
import be.vinci.pae.utils.exceptions.UnauthorizedAccesException;
import jakarta.inject.Inject;
import java.util.List;
import org.apache.logging.log4j.Level;

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
      Logs.log(Level.INFO, "UserUCC (login) : entrance");
      dalServices.startTransaction();
      userDTOFound = userDAO.getOneUserByEmail(email);
      user = (User) userDTOFound;
    } catch (Exception e) {
      dalServices.rollbackTransaction();
      throw e;
    }
    if (user == null) {
      Logs.log(Level.ERROR, "UserUCC (login) : user not found");
      throw new ResourceNotFoundException("User not found.");
    }
    if (!user.checkPassword(password)) {
      Logs.log(Level.ERROR, "UserUCC (login) : wrong password");
      throw new UnauthorizedAccesException("The password is incorrect");
    }
    dalServices.commitTransaction();
    Logs.log(Level.DEBUG, "UserUCC (login) : success!");
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
      Logs.log(Level.INFO, "UserUCC (getAllUsers) : entrance");
      dalServices.startTransaction();
      userList = userDAO.getAllUsers();
    } catch (Exception e) {
      dalServices.rollbackTransaction();
      throw e;
    }
    if (userList.isEmpty()) {
      throw new ResourceNotFoundException("Users not found.");
    }
    dalServices.commitTransaction();
    Logs.log(Level.DEBUG, "UserUCC (getAllUsers) : success!");
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
      Logs.log(Level.INFO, "UserUCC (getOneById) : entrance");
      dalServices.startTransaction();
      user = userDAO.getOneUserById(id);
    } catch (Exception e) {
      dalServices.rollbackTransaction();
      throw e;
    }
    if (user == null) {
      Logs.log(Level.ERROR, "UserUCC (getOneById) : user is not in db");
      throw new ResourceNotFoundException();
    }
    dalServices.commitTransaction();
    Logs.log(Level.DEBUG, "UserUCC (getOneById) : success!");
    return user;
  }
}
