package be.vinci.pae.api;

import be.vinci.pae.api.filters.Authorize;
import be.vinci.pae.domain.dto.UserDTO;
import be.vinci.pae.domain.ucc.UserUCC;
import be.vinci.pae.utils.Config;
import be.vinci.pae.utils.exceptions.BadRequestException;
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
import org.glassfish.jersey.internal.util.collection.Views;
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
      throw new BadRequestException();
    }
    if (json.get("email").asText().isBlank() || json.get("password").asText().isBlank()) {
      throw new BadRequestException();
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
    List<UserDTO> userDTOList;
    try {
      userDTOList = userUCC.getAllUsers();
    } catch (FatalException e) {
      throw e;
    }
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
    UserDTO userDTO = (UserDTO) request.getProperty("user");
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
      throw new WebApplicationException("error while creating token", Response.Status.UNAUTHORIZED);
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
  public Response getOneUser(@Context ContainerRequest request, @PathParam("id") int id) {
    UserDTO userDTO = userUCC.getOneById(id);
    String user = "";

    try {
      user = jsonMapper.writeValueAsString(userDTO);
      return Response.ok(user).build();

    } catch (Exception e) {
      return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Erreur lors de la récupération des données de l'utilisateur").build();
    }
  }
}

