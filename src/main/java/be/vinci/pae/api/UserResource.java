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
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.sql.Date;
import java.time.LocalDate;
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

    UserDTO userDTO;
    String token;
    userDTO = userUCC.login(email, password);

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
    String token;
    try {
      token = JWT.create().withIssuer("auth0")
          .withClaim("user", userDTO.getId()).sign(this.jwtAlgorithm);
      ObjectNode publicUser = jsonMapper.createObjectNode()
          .put("token", token)
          .putPOJO("user", userDTO);
      Logs.log(Level.DEBUG, "UserResource (rememberMe) : success!");
      return publicUser;
    } catch (Exception e) {
      Logs.log(Level.ERROR, "UserResource (rememberMe) : error creating token");
      throw new WebApplicationException("error while creating token", Response.Status.UNAUTHORIZED);
    }
  }

  /**
   * Register route.
   *
   * @param userToRegister UserDTO object containing email, lastname, firstname, phone number,
   *                       password, role.
   * @return a User.
   */
  @POST
  @Path("register")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public UserDTO register(UserDTO userToRegister) {

    if (userToRegister.getEmail().isBlank() || userToRegister.getLastname().isBlank()
        || userToRegister.getFirstname().isBlank() || userToRegister.getPhoneNumber().isBlank()
        || userToRegister.getPassword().isBlank() || userToRegister.getRole().isBlank()) {
      throw new WebApplicationException("Inputs cannot be blank", Response.Status.BAD_REQUEST);
    }

    LocalDate localDate = LocalDate.now();
    Date registrationDate = Date.valueOf(localDate);
    userToRegister.setRegistrationDate(registrationDate);
    int monthValue = localDate.getMonthValue();
    String schoolYear;
    if (monthValue >= 9) {
      schoolYear = localDate.getYear() + "-" + localDate.plusYears(1).getYear();
    } else {
      schoolYear = localDate.minusYears(1).getYear() + "-" + localDate.getYear();
    }
    userToRegister.setSchoolYear(schoolYear);

    UserDTO registeredUser;

    registeredUser = userUCC.register(userToRegister);
    return registeredUser;

  }
}

