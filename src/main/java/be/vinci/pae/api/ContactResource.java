package be.vinci.pae.api;

import be.vinci.pae.api.filters.Authorize;
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

  /**
   * Starting contact route.
   *
   * @param json jsonNode containing user id and company id to start the contact.
   * @return the started contact.
   */
  @POST
  @Path("start")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Authorize
  public ObjectNode start(JsonNode json) {
    if (!json.hasNonNull("company") || !json.hasNonNull("student")) {
      throw new WebApplicationException("company and student", Response.Status.BAD_REQUEST);
    }
    if (json.get("company").asText().isBlank()) {
      throw new WebApplicationException("company required", Response.Status.BAD_REQUEST);
    }
    if (json.get("student").asText().isBlank()) {
      throw new WebApplicationException("student required", Response.Status.BAD_REQUEST);
    }

    int company = json.get("company").asInt();
    int student = json.get("student").asInt();

    ContactDTO contactDTO = contactUCC.start(company, student);

    ObjectNode contact = jsonMapper.createObjectNode().putPOJO("contact", contactDTO);
    return contact;
  }

  /**
   * admitting a contact with the type of the meeting("on site" or "remote").
   *
   * @param json jsonNode containing contact id and the type of the meeting.
   * @return ObjectNode containing all information about the contact admitted.
   * @throws WebApplicationException when the contact_id and/or the meeting field is invalid.
   */
  @POST
  @Path("turnedDown")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Authorize
  public ObjectNode turnedDown(JsonNode json) {
    if (!json.hasNonNull("contact_id") || !json.hasNonNull("reason_for_refusal") || json.get(
            "contact_id")
        .asText().isBlank() || json.get("reason_for_refusal").asText().isBlank()) {
      throw new WebApplicationException("contact or reason for refusal required",
          Response.Status.BAD_REQUEST);
    }

    int idContact = json.get("contact_id").asInt();
    String reasonForRefusal = json.get("reason_for_refusal").asText();

    ContactDTO contactDTO;
    contactDTO = contactUCC.turnedDown(idContact, reasonForRefusal);

    return jsonMapper.createObjectNode().putPOJO("contact", contactDTO);
  }

}
