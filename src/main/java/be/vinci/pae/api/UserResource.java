package be.vinci.pae.api;

import be.vinci.pae.domain.UserFactory;
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
import java.sql.Date;
import java.time.LocalDate;

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
  @Inject
  private UserFactory userFactory;

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
      throw new WebApplicationException("email or password required", Response.Status.BAD_REQUEST);
    }
    if (json.get("email").asText().isBlank()) {
      throw new WebApplicationException("email required", Response.Status.BAD_REQUEST);
    }
    if (json.get("password").asText().isBlank()) {
      throw new WebApplicationException("password required", Response.Status.BAD_REQUEST);
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
      return publicUser;
    } catch (Exception e) {
      System.out.println("Error while creating token");
      throw new WebApplicationException("error while creating token", Response.Status.UNAUTHORIZED);
    }
  }

  /**
   * Register route.
   *
   * @param json object containing email, lastname, firstname, phone number, password, role.
   * @return a User.
   */
  @POST
  @Path("register")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public ObjectNode register(JsonNode json) {

    String email = json.get("email").asText();
    String lastname = json.get("lastname").asText();
    String firstname = json.get("firstname").asText();
    String phoneNumber = json.get("phoneNumber").asText();
    String password = json.get("password").asText();
    LocalDate localDate = LocalDate.now();
    Date registrationDate = Date.valueOf(localDate);
    int monthValue = localDate.getMonthValue();
    String schoolYear;
    if (monthValue >= 9) {
      schoolYear =
          String.valueOf(localDate.getYear()) + "-" + String.valueOf(localDate.plusYears(1).getYear());
    } else {
      schoolYear =
          String.valueOf(localDate.minusYears(1).getYear()) + "-" + String.valueOf(localDate.getYear());
    }
    String role = json.get("role").asText();

    UserDTO user = userFactory.getUserDTO();
    user.setEmail(email);
    user.setLastname(lastname);
    user.setFirstname(firstname);
    user.setPhoneNumber(phoneNumber);
    user.setPassword(password);
    user.setRegistrationDate(registrationDate);
    user.setSchoolYear(schoolYear);
    user.setRole(role);

    UserDTO registeredUser = userUCC.register(user);

    ObjectNode publicUser = jsonMapper.createObjectNode()
        .putPOJO("user", registeredUser);

    return publicUser;

  }
}

