package be.vinci.pae.services.dao;

import be.vinci.pae.domain.UserFactory;
import be.vinci.pae.domain.dto.UserDTO;
import be.vinci.pae.services.utils.DalBackendServices;
import jakarta.inject.Inject;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Implementation of UserDAO.
 */
public class UserDAOImpl implements UserDAO {

  /**
   * Implementation of UserDAO class.
   */
  @Inject
  private DalBackendServices dalBackendServices;
  @Inject
  private UserFactory userFactory;

  /**
   * Get one user by email then set the userDTO if user exist.
   *
   * @param email user' email.
   * @return userDTO with setter corresponding to the email, null otherwise.
   */
  @Override
  public UserDTO getOneUserByEmail(String email) {

    String requestSql = """
        SELECT user_id, email, lastname, firstname, phone_number, password,
        registration_date, school_year, role
        FROM prostage.users
        WHERE email = ?
        """;

    UserDTO user = userFactory.getUserDTO();

    try (PreparedStatement ps = dalBackendServices.getPreparedStatement(requestSql)) {
      ps.setString(1, email);
      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          user.setId(rs.getInt("user_id"));
          user.setEmail(rs.getString("email"));
          user.setLastname(rs.getString("lastname"));
          user.setFirstname(rs.getString("firstname"));
          user.setPhoneNumber(rs.getString("phone_number"));
          user.setPassword(rs.getString("password"));
          user.setRegistrationDate(rs.getDate("registration_date"));
          user.setSchoolYear(rs.getString("school_year"));
          user.setRole(rs.getString("role"));
          return user;
        }
        return null;
      } catch (SQLException throwables) {
        throwables.printStackTrace();
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }

    return null;

  }

  /**
   * Add one user.
   *
   * @param user user to add.
   * @return UserDTO of added user, null otherwise.
   */
  @Override
  public UserDTO addOneUser(UserDTO user) {

    String requestSql = """
        INSERT INTO prostage.users(email, lastname, firstname, phone_number, 
        password, registration_date, school_year, role) VALUES (?,?,?,?,?,?,?,?)
        RETURNING email AS inserted_email
        """;

    try (PreparedStatement ps = dalBackendServices.getPreparedStatement(requestSql)) {
      ps.setString(1, user.getEmail());
      ps.setString(2, user.getLastname());
      ps.setString(3, user.getFirstname());
      ps.setString(4, user.getPhoneNumber());
      ps.setString(5, user.getPassword());
      ps.setDate(6, user.getRegistrationDate());
      ps.setString(7, user.getEmail());
      ps.setString(8, user.getEmail());
      try(ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          return getOneUserByEmail(rs.getString("inserted_email"));
        }
        return null;
      }
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    }

    return null;

  }
}
