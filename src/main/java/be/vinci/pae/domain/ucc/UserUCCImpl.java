package be.vinci.pae.domain.ucc;

import be.vinci.pae.domain.User;
import be.vinci.pae.domain.dto.UserDTO;
import be.vinci.pae.services.dal.DalServices;
import be.vinci.pae.services.dao.UserDAO;
import be.vinci.pae.utils.exceptions.BadRequestException;
import be.vinci.pae.utils.exceptions.FatalException;
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
  private DalServices dalServices;

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

    if (user == null) {
      throw new NotFoundException();
    }
    if (!user.checkPassword(password)) {
      throw new BadRequestException();
    }
    if (user == null || !user.checkPassword(password)) {
      dalServices.rollbackTransaction();
      return null;
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
  public List<UserDTO> getAllUsers() {
    return userDAO.getAllUsers();
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

  /**
   * Register a user.
   *
   * @param user user to register.
   * @return a UserDTO of registered user, null otherwise.
   */
  @Override
  public UserDTO register(UserDTO user) {

    UserDTO registeredUser;

    try {
      dalServices.startTransaction();
      UserDTO existingUser = userDAO.getOneUserByEmail(user.getEmail());
      if (existingUser != null) {
        throw new BadRequestException();
      }

      User userHashPwd = (User) user;
      userHashPwd.hashPassword();

      registeredUser = userDAO.addOneUser((UserDTO) userHashPwd);
      if (registeredUser == null) {
        throw new BadRequestException();
      }

      dalServices.commitTransaction();
      return registeredUser;
    } catch (Exception e) {
      dalServices.rollbackTransaction();
      throw new FatalException(e);
    }
  }
}
