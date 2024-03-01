package be.vinci.pae.services.dao;

import be.vinci.pae.domain.UserFactory;
import be.vinci.pae.domain.dto.UserDTO;
import be.vinci.pae.services.utils.DalService;
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
  private DalService dalService;
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
        inscription_date, school_year, role
        FROM prostage.users
        WHERE email = ?
        """;
    PreparedStatement ps = dalService.getPreparedStatement(requestSql);
    try {
      ps.setString(1, email);
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }

    UserDTO user = userFactory.getUserDTO();

    try (ResultSet rs = ps.executeQuery()) {
      if (rs.next()) {
        user.setId(rs.getInt("user_id"));
        user.setEmail(rs.getString("email"));
        user.setLastname(rs.getString("lastname"));
        user.setFirstname(rs.getString("firstname"));
        user.setPhoneNumber(rs.getString("phone_number"));
        user.setPassword(rs.getString("password"));
        user.setInscriptionDate(rs.getDate("inscription_date"));
        user.setSchoolYear(rs.getString("school_year"));
        user.setRole(rs.getString("role"));
        return user;
      }
      return null;
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    } finally {
      try {
        ps.close();
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }

    return null;

  }
}
