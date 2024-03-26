package be.vinci.pae.api;

import be.vinci.pae.api.filters.Authorize;
import be.vinci.pae.domain.dto.UserDTO;
import be.vinci.pae.domain.ucc.UserUCC;
import be.vinci.pae.utils.Config;
import be.vinci.pae.utils.Logs;
import be.vinci.pae.utils.exceptions.FatalException;
import be.vinci.pae.utils.exceptions.InvalidRequestException;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import java.util.List;
import org.apache.logging.log4j.Level;
import org.glassfish.jersey.server.ContainerRequest;

/**
 * UserResource class.
 */
@Singleton
@Path("/users")
public class UserResource {

  private final Algorithm jwtAlgorithm = Algorithm.HMAC256(Config.getProperty("JWTSecret"));
  private final ObjectMapper jsonMapper = new ObjectMapper();
  @Inject
  private UserUCC userUCC;

  /**
   * Login route.
   *
   * @param json object containing login and password.
   * @return a User and their token as a JsonNode.
   */
  @POST
  @Path("login")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public ObjectNode login(JsonNode json) {
    Logs.log(Level.INFO, "UserResource (login) : entrance");
    if (!json.hasNonNull("email") || !json.hasNonNull("password")) {
      Logs.log(Level.WARN, "UserResource (login) : email or password is null");
      throw new InvalidRequestException();
    }
    if (json.get("email").asText().isBlank() || json.get("password").asText().isBlank()) {
      throw new InvalidRequestException();
    }

    String email = json.get("email").asText();
    String password = json.get("password").asText();

    UserDTO userDTO = userUCC.login(email, password);
    return buildToken(userDTO);
  }

  /**
   * Get all users.
   *
   * @return a list containing all the users.
   */
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public List<UserDTO> getAll() {
    Logs.log(Level.INFO, "UserResource (getAll) : entrance");
    List<UserDTO> userDTOList;
    try {
      userDTOList = userUCC.getAllUsers();
    } catch (FatalException e) {
      throw e;
    }
    Logs.log(Level.DEBUG, "UserResource(getAll) : success!");
    return userDTOList;
  }

  /**
   * Login route with remember me.
   *
   * @param request the token.
   * @return a new token.
   */
  @GET
  @Path("login")
  @Produces(MediaType.APPLICATION_JSON)
  @Authorize
  public ObjectNode rememberMe(@Context ContainerRequest request) {
    Logs.log(Level.INFO, "UserResource (rememberMe) : entrance");
    UserDTO userDTO = (UserDTO) request.getProperty("user");
    return buildToken(userDTO);
  }

  /**
   * Build a token based on a UserDTO.
   *
   * @param userDTO the userDTO.
   * @return the token built.
   */
  private ObjectNode buildToken(UserDTO userDTO) {
    String token;
    try {
      token = JWT.create().withIssuer("auth0")
          .withClaim("user", userDTO.getId()).sign(this.jwtAlgorithm);
      ObjectNode publicUser = jsonMapper.createObjectNode()
          .put("token", token)
          .putPOJO("user", userDTO);
      Logs.log(Level.WARN, "UserResource (login) : success!");
      return publicUser;
    } catch (Exception e) {
      Logs.log(Level.FATAL, "UserResource (login) : internal error");
      throw new FatalException(e);
    }
  }
}

