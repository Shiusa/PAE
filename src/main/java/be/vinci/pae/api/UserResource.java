package be.vinci.pae.api;

import be.vinci.pae.api.filters.Authorize;
import be.vinci.pae.domain.dto.UserDTO;
import be.vinci.pae.domain.ucc.UserUCC;
import be.vinci.pae.utils.Config;
import be.vinci.pae.utils.Logs;
import be.vinci.pae.utils.exceptions.FatalException;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
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
      throw new WebApplicationException("Email and password required", Response.Status.BAD_REQUEST);
    }
    if (json.get("email").asText().isBlank() || json.get("password").asText().isBlank()) {
      throw new WebApplicationException("Email and password cannot be blank",
          Response.Status.BAD_REQUEST);
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
  @Path("all")
  @Produces(MediaType.APPLICATION_JSON)
  @Authorize
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
    Logs.log(Level.DEBUG, "UserResource (rememberMe) : success!");
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


  /**
   * returns a user by its id.
   *
   * @param request the token from the front.
   * @param id      of the user
   * @return userDTO
   */
  @GET
  @Path("/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  @Authorize
  public Response getOneUser(@Context ContainerRequest request, @PathParam("id") int id) {
    Logs.log(Level.INFO, "UserResource (getOneUser) : entrance");
    UserDTO userCheck = (UserDTO) request.getProperty("user");
    if (userCheck.getId() != id) {
      Logs.log(Level.ERROR, "UserResource (getOneUser) : unauthorized");
      throw new WebApplicationException("you can't see this user", Response.Status.UNAUTHORIZED);
    }
    UserDTO userDTO = userUCC.getOneById(id);
    String user;
    try {
      user = jsonMapper.writeValueAsString(userDTO);
    } catch (JsonProcessingException e) {
      Logs.log(Level.FATAL, "UserResource (getOneUser) : internal error");
      throw new FatalException(e);
    }
    Logs.log(Level.INFO, "UserResource (getOneUser) : success!");
    return Response.ok(user).build();
  }
}

