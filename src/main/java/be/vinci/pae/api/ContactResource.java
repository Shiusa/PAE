package be.vinci.pae.api;

import be.vinci.pae.api.filters.Authorize;
import be.vinci.pae.domain.dto.ContactDTO;
import be.vinci.pae.domain.ucc.ContactUCC;
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

    return jsonMapper.createObjectNode().putPOJO("contact", contactDTO);
  }

  /**
   * admitting a contact with the type of the meeting("on site" or "remote").
   *
   * @param json jsonNode containing contact id and the type of the meeting.
   * @return ObjectNode containing all information about the contact admitted.
   * @throws WebApplicationException when the contact_id and/or the meeting field is invalid.
   */
  @POST
  @Path("admit")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Authorize
  public ObjectNode admit(JsonNode json) {
    if (!json.hasNonNull("contactId") || !json.hasNonNull("meeting") || json.get("contactId")
        .asText().isBlank() || json.get("meeting").asText().isBlank()) {
      throw new WebApplicationException("contact or meeting required", Response.Status.BAD_REQUEST);
    }

    int contactId = json.get("contactId").asInt();
    String meeting = json.get("meeting").asText();

    ContactDTO contactDTO = contactUCC.admit(contactId, meeting);

    return jsonMapper.createObjectNode().putPOJO("contact", contactDTO);
  }
}
