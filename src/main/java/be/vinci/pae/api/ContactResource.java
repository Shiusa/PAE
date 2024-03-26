package be.vinci.pae.api;

import be.vinci.pae.api.filters.Authorize;
import be.vinci.pae.domain.dto.ContactDTO;
import be.vinci.pae.domain.dto.UserDTO;
import be.vinci.pae.domain.ucc.ContactUCC;
import be.vinci.pae.utils.Config;
import be.vinci.pae.utils.Logs;
import be.vinci.pae.utils.exceptions.InvalidRequestException;
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
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.apache.logging.log4j.Level;
import org.glassfish.jersey.server.ContainerRequest;

/**
 * ContactResource class.
 */
@Singleton
@Path("/contacts")
public class ContactResource {

  private final Algorithm jwtAlgorithm = Algorithm.HMAC256(Config.getProperty("JWTSecret"));
  private final ObjectMapper jsonMapper = new ObjectMapper();
  @Inject
  private ContactUCC contactUCC;

  /**
   * Starting contact route.
   *
   * @param request the token.
   * @param json    jsonNode containing user id and company id to start the contact.
   * @return the started contact.
   */
  @POST
  @Path("start")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Authorize
  public ObjectNode start(@Context ContainerRequest request, JsonNode json) {
    UserDTO userDTO = (UserDTO) request.getProperty("user");
    Logs.log(Level.INFO, "ContactResource (start) : entrance");
    if (!json.hasNonNull("company")) {
      Logs.log(Level.WARN, "ContactResource (start) : Company is null");
      throw new WebApplicationException("company", Response.Status.BAD_REQUEST);
    }
    if (json.get("company").asText().isBlank()) {
      Logs.log(Level.WARN, "ContactResource (start) : Company is blank");
      throw new WebApplicationException("company required", Response.Status.BAD_REQUEST);
    }

    int company = json.get("company").asInt();
    int student = userDTO.getId();

    ContactDTO contactDTO = contactUCC.start(company, student);

    ObjectNode contact = jsonMapper.createObjectNode().putPOJO("contact", contactDTO);
    Logs.log(Level.DEBUG, "ContactResource (start) : success!");
    return contact;
  }

  /**
   * Unsupervised contact route.
   *
   * @param request the token.
   * @param json    jsonNode containing contact id to unsupervised the contact.
   * @return the unsupervised state of a contact.
   */
  @POST
  @Path("unsupervise")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Authorize
  public ObjectNode unsupervise(@Context ContainerRequest request, JsonNode json) {
    UserDTO userDTO = (UserDTO) request.getProperty("user");
    if (!json.hasNonNull("contactId")) {
      throw new InvalidRequestException();
    }
    if (json.get("contactId").asText().isBlank()) {
      throw new InvalidRequestException();
    }
    int contactId = json.get("contactId").asInt();
    int student = userDTO.getId();

    ContactDTO contactDTO = contactUCC.unsupervise(contactId, student);
    return jsonMapper.createObjectNode().putPOJO("contact", contactDTO);
  }
}
