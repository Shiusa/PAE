package be.vinci.pae.api;

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
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

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
    UserDTO userDTO;

    if (!json.hasNonNull("email") || !json.hasNonNull("password")) {
      throw new WebApplicationException("email or password required", Response.Status.BAD_REQUEST);
    }
    String email = json.get("email").asText();
    String password = json.get("password").asText();

    String token;
    userDTO = userUCC.login(email, password);

    try {
      token = JWT.create().withIssuer("auth0")
          .withClaim("user", userDTO.getId()).sign(this.jwtAlgorithm);
      ObjectNode publicUser = jsonMapper.createObjectNode()
          .put("token", token)
          .put("id", userDTO.getId())
          .put("email", userDTO.getEmail());
      return publicUser;
    } catch (Exception e) {
      System.out.println("Error while creating token");
      return null;
    }
  }

  /**
   * Let a user automatically reconnect
   *
   * @param json the previous token containing user's informations
   * @return the new user token with his informations
   */
  @POST
  @Path("logged")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public ObjectNode logged(JsonNode json) {
    ObjectNode publicUser = jsonMapper.createObjectNode();

    if (json == null || json.get("token") == null) {
      publicUser.put("error", "Token not valid");
      return publicUser;
    }

    String newToken;

    try {
      newToken = JWT.create().withIssuer("auth0")
          .withClaim("user", json.get("id").asInt()).sign(this.jwtAlgorithm);
      publicUser.put("token", newToken)
          .put("id", json.get("id").asInt())
          .put("email", json.get("email").asText());
      return publicUser;
    } catch (Exception e) {
      System.out.println("Error while creating token");
      return null;
    }

  }

}

