package be.vinci.pae.services;

import be.vinci.pae.domain.dto.UserDTO;
import be.vinci.pae.services.dao.UserDAO;
import be.vinci.pae.services.utils.DalServiceImpl;
import jakarta.inject.Inject;

/**
 * Implementation of UserDAO.
 */
public class UserDAOImpl implements UserDAO {

  /**
   * Implementation of UserDAO class.
   */
  @Inject
  private DalServiceImpl dalService;

  @Override
  public UserDTO getOneUserByEmail(String email) {
    /*UserDTO userDTO;
    String requestSql = """
        SELECT id_utilisateur, nom, prenom, telephone, mot_de_passe,
        date_inscription, annee_academique, role
        FROM utilisateurs
        WHERE email = ?
        """;
    PreparedStatement ps = dalService.getPreparedStatement(requestSql);
    try {
      ps.setString(1, email);
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }

    try (ResultSet rs = ps.getResultSet()) {
      userDTO.setEmail(rs.getString("email"));
    }

    return userDTO;*/
    return null;
  }
}
