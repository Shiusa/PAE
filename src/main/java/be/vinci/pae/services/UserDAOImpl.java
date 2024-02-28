package be.vinci.pae.services;

import be.vinci.pae.domain.dto.UserDTO;
import be.vinci.pae.services.dao.UserDAO;
import be.vinci.pae.services.utils.DalServiceImpl;
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
  private DalServiceImpl dalService;

  /**
   * Get one user by email then set the userDTO if user exist.
   *
   * @param user user's userDTO object.
   * @return userDTO with setter corresponding to the email, null otherwise.
   */
  @Override
  public UserDTO getOneUserByEmail(UserDTO user) {

    String requestSql = """
        SELECT id_utilisateur, nom, prenom, telephone, mot_de_passe,
        date_inscription, annee_academique, role
        FROM prostage.utilisateurs
        WHERE email = ?
        """;
    PreparedStatement ps = dalService.getPreparedStatement(requestSql);
    try {
      ps.setString(1, user.getEmail());
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }

    try (ResultSet rs = ps.executeQuery()) {
      user.setId(rs.getInt("id_utilisateur"));
      user.setNom(rs.getString("nom"));
      user.setPrenom(rs.getString("prenom"));
      user.setTelephone(rs.getString("telephone"));
      user.setMotDePasse(rs.getString("mot_de_passe"));
      user.setDateInscription(rs.getDate("date_inscription"));
      user.setAnneeAcademique(rs.getString("annee_academique"));
      user.setRole(rs.getString("role"));
      return user;
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
