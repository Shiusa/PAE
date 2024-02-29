package be.vinci.pae.api;

import be.vinci.pae.domain.dto.UserDTO;
import be.vinci.pae.domain.ucc.UserUCC;
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

    userDTO = userUCC.login(email, password);

    ObjectNode publicUser = jsonMapper.createObjectNode()
        .putPOJO("user", userDTO);
    return publicUser;
  }
}
