package be.vinci.pae.services;

import be.vinci.pae.domain.UserImpl;
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
   *
   * @param email user's email
   * @return userDTO corresponding to the email, null otherwise.
   */
  @Override
  public UserDTO getOneUserByEmail(String email) {

    UserDTO userDTO = new UserImpl(); //factory ?
    String requestSql = """
        SELECT id_utilisateur, nom, prenom, telephone, mot_de_passe,
        date_inscription, annee_academique, role
        FROM prostage.utilisateurs
        WHERE email = ?
        """;
    PreparedStatement ps = dalService.getPreparedStatement(requestSql);
    try {
      ps.setString(1, email);
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }

    try (ResultSet rs = ps.executeQuery()) {
      userDTO.setId(rs.getInt("id_utilisateur"));
      userDTO.setEmail(email);
      userDTO.setNom(rs.getString("nom"));
      userDTO.setPrenom(rs.getString("prenom"));
      userDTO.setTelephone(rs.getString("telephone"));
      userDTO.setMotDePasse(rs.getString("mot_de_passe"));
      userDTO.setDateInscription(rs.getDate("date_inscription"));
      userDTO.setAnneeAcademique(rs.getString("annee_academique"));
      userDTO.setRole(rs.getString("role"));
      return userDTO;
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
