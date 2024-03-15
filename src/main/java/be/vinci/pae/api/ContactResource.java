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
import jakarta.ws.rs.core.Response.Status;

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
   * @param jsonNode jsonNode containing user id and company id to start the contact.
   * @return the started contact.
   */
  @POST
  @Path("start")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Authorize
  public ObjectNode start(JsonNode jsonNode) {
    String company = jsonNode.asText("company");
    String student = jsonNode.asText("student");

    if (company.isEmpty() || company.isBlank()) {
      throw new WebApplicationException("company required", Response.Status.BAD_REQUEST);
    }
    if (student.isEmpty() || student.isBlank()) {
      throw new WebApplicationException("student required", Response.Status.BAD_REQUEST);
    }

    int companyId;
    int studentId;

    try {
      companyId = Integer.parseInt(company);
      studentId = Integer.parseInt(student);
    } catch (NumberFormatException e) {
      throw new WebApplicationException("invalid format for company or student",
          Response.Status.BAD_REQUEST);
    }

    ContactDTO contactDTO = contactUCC.start(companyId, studentId);
    if (contactDTO == null) {
      throw new WebApplicationException("error inserting data", Status.BAD_REQUEST);
    }

    ObjectNode contact = jsonMapper.createObjectNode().putPOJO("pojo", contactDTO);

    return contact;
  }

}
