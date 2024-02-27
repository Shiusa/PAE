package be.vinci.pae.services;

import be.vinci.pae.domain.User;
import be.vinci.pae.domain.dto.UserDTO;
import be.vinci.pae.services.dao.UserDAO;
import be.vinci.pae.services.utils.DalServiceImpl;
import be.vinci.pae.utils.Config;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.inject.Inject;

/**
 * Implementation of UserDAO.
 */
public class UserDAOImpl implements UserDAO {

  private final Algorithm jwtAlgorithm = Algorithm.HMAC256(Config.getProperty("JWTSecret"));
  private final ObjectMapper jsonMapper = new ObjectMapper();
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

  /**
   * Create a token with the informations of the user. The token is made of the user id and his
   * email.
   *
   * @param user
   * @return the token in ObjectNode
   */
  public ObjectNode login(User user) {
    String token;
    try {
      token = JWT.create().withIssuer("auth0")
          .withClaim("user", user.getId()).sign(this.jwtAlgorithm);
      ObjectNode publicUser = jsonMapper.createObjectNode()
          .put("token", token)
          .put("id", user.getId())
          .put("email", user.getEmail());
      return publicUser;
    } catch (Exception e) {
      System.out.println("Error during token creation");
      return null;
    }

  }
}
