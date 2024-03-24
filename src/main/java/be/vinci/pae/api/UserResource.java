package be.vinci.pae.api;

import be.vinci.pae.api.filters.Authorize;
import be.vinci.pae.domain.dto.UserDTO;
import be.vinci.pae.domain.ucc.UserUCC;
import be.vinci.pae.utils.Config;
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
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import java.util.List;
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
    if (!json.hasNonNull("email") || !json.hasNonNull("password")) {
      throw new WebApplicationException("Email and password required", Response.Status.BAD_REQUEST);
    }
    if (json.get("email").asText().isBlank() || json.get("password").asText().isBlank()) {
      System.out.println("here");
      throw new WebApplicationException("Email and password cannot be blank",
          Response.Status.BAD_REQUEST);
    }

    String email = json.get("email").asText();
    String password = json.get("password").asText();

    UserDTO userDTO;
    userDTO = userUCC.login(email, password);

    return createToken(userDTO);
  }

  /**
   * Get all users.
   *
   * @return a list containing all the users.
   */
  @GET
  @Path("all")
  @Produces(MediaType.APPLICATION_JSON)
  public List<UserDTO> getAll() {
    return userUCC.getAllUsers();
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
    UserDTO userDTO = (UserDTO) request.getProperty("user");
    return createToken(userDTO);
  }

  private ObjectNode createToken(UserDTO userDTO) {
    String token;
    try {
      token = JWT.create().withIssuer("auth0")
          .withClaim("user", userDTO.getId()).sign(this.jwtAlgorithm);
      ObjectNode publicUser = jsonMapper.createObjectNode()
          .put("token", token)
          .putPOJO("user", userDTO);
      return publicUser;
    } catch (Exception e) {
      System.out.println("Error while creating token");
      throw new WebApplicationException("error while creating token", Status.UNAUTHORIZED);
    }
  }
}

