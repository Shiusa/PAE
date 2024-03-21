package be.vinci.pae.services.dao;

import be.vinci.pae.domain.UserFactory;
import be.vinci.pae.domain.dto.UserDTO;
import be.vinci.pae.services.dal.DalServices;
import be.vinci.pae.utils.exceptions.FatalException;
import jakarta.inject.Inject;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of UserDAO.
 */
public class UserDAOImpl implements UserDAO {

  /**
   * Implementation of UserDAO class.
   */
  @Inject
  private DalServices dalServices;
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
    PreparedStatement ps = dalServices.getPreparedStatement(requestSql);
    try {
      ps.setString(1, email);
    } catch (SQLException e) {
      throw new FatalException(e);
    }
    return buildUserDTO(ps);
  }

  /**
   * Get one user by id then set the userDTO if user exist.
   *
   * @param id user' id.
   * @return userDTO with setter corresponding to the id, null otherwise.
   */
  @Override
  public UserDTO getOneUserById(int id) {
    String requestSql = """
        SELECT user_id, email, lastname, firstname, phone_number, password,
        registration_date, school_year, role
        FROM prostage.users
        WHERE user_id = ?
        """;
    PreparedStatement ps = dalServices.getPreparedStatement(requestSql);
    try {
      ps.setInt(1, id);
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
    return buildUserDTO(ps);
  }

  private UserDTO buildUserDTO(PreparedStatement ps) {
    UserDTO user = userFactory.getUserDTO();

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
        rs.close();
        return user;
      }
      return null;
    } catch (SQLException e) {
      throw new FatalException(e);
    } finally {
      try {
        ps.close();
      } catch (SQLException e) {
        throw new FatalException(e);
      }
    }
  }

  /**
   * Get all users from the database who don't have the role admin.
   *
   * @return A list containing all users.
   */
  @Override
  public List<UserDTO> getAllUsers() {
    List<UserDTO> userDTOList = new ArrayList<>();

    String requestSql = """
        SELECT user_id,email, lastname, firstname,phone_number,password,
        registration_date, school_year, role
        FROM proStage.users """;

    try (PreparedStatement ps = dalServices.getPreparedStatement(requestSql)) {
      try (ResultSet rs = ps.executeQuery()) {
        while (rs.next()) {
          UserDTO userDTO = userFactory.getUserDTO();
          userDTO.setId(rs.getInt(1));
          userDTO.setEmail(rs.getString("email"));
          userDTO.setLastname(rs.getString("lastname"));
          userDTO.setFirstname(rs.getString("firstname"));
          userDTO.setPhoneNumber(rs.getString("phone_number"));
          userDTO.setPassword(rs.getString("password"));
          userDTO.setRegistrationDate(rs.getDate("registration_date"));
          userDTO.setSchoolYear(rs.getString("school_year"));
          userDTO.setRole(rs.getString("role"));
          userDTOList.add(userDTO);
        }
      }
    } catch (SQLException e) {
      throw new FatalException(e);
    }
    return userDTOList;
  }
}
