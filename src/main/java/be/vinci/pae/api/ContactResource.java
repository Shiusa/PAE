package be.vinci.pae.api;

import be.vinci.pae.domain.dto.ContactDTO;
import be.vinci.pae.domain.ucc.ContactUCC;
import be.vinci.pae.utils.Config;
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
 * ContactResource class.
 */
@Singleton
@Path("/contacts")
public class ContactResource {

  private final Algorithm jwtAlgorithm = Algorithm.HMAC256(Config.getProperty("JWTSecret"));
  private final ObjectMapper jsonMapper = new ObjectMapper();
  @Inject
  private ContactUCC contactUCC;


  @POST
  @Path("start")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public ObjectNode start(JsonNode jsonNode) {
    return null;
  }

  @POST
  @Path("admitted")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public ObjectNode taken(JsonNode json) {
    if (!json.hasNonNull("contact_id") || !json.hasNonNull("meeting") || json.get("contact_id")
        .asText().isBlank() || json.get("meeting").asText().isBlank()) {
      throw new WebApplicationException("contact or meeting required", Response.Status.BAD_REQUEST);
    }

    int idContact = json.get("contact_id").asInt();
    String meeting = json.get("meeting").asText();

    ContactDTO contactDTO = contactUCC.admitted(idContact, meeting);

    if (contactDTO == null) {
      throw new WebApplicationException("Your meeting type is neither on site nor remote",
          Response.Status.BAD_REQUEST);
    }
    ObjectNode contact = jsonMapper.createObjectNode().putPOJO("contact", contactDTO);
    return contact;
  }
}
