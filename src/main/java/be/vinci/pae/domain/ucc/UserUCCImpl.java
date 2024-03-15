package be.vinci.pae.domain.ucc;

import be.vinci.pae.domain.User;
import be.vinci.pae.domain.dto.UserDTO;
import be.vinci.pae.services.dao.UserDAO;
import jakarta.inject.Inject;

/**
 * User UCC.
 */
public class UserUCCImpl implements UserUCC {

  @Inject
  private UserDAO userDAO;

  /**
   * Get a user associated with an email and check their password with the password entered.
   *
   * @param email      the user's email.
   * @param password the user's hashed password.
   * @return a UserDTO if existing user and correct password;.
   */
  @Override
  public UserDTO login(String email, String password) {
    UserDTO userDTOFound = userDAO.getOneUserByEmail(email);

    User user = (User) userDTOFound;

    if (user == null || !user.checkPassword(password)) {
      return null;
    }

    userDTOFound.setPassword(null);
    return userDTOFound;
  }

  /**
   * Register a user.
   *
   * @param user  user to register.
   * @return a UserDTO of registered user, null otherwise.
   */
  @Override
  public UserDTO register(UserDTO user) {

    UserDTO existingUser = userDAO.getOneUserByEmail(user.getEmail());
    if (existingUser!=null) {
      return null;
    }

    User userHashPwd = (User) user;
    userHashPwd.hashPassword();

    UserDTO registeredUser = userDAO.addOneUser((UserDTO) userHashPwd);
    if (registeredUser==null){
      return null;
    }
    return registeredUser;
  }
}
