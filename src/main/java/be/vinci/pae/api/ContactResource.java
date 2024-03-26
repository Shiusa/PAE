package be.vinci.pae.api;

import be.vinci.pae.api.filters.Authorize;
import be.vinci.pae.domain.dto.ContactDTO;
import be.vinci.pae.domain.dto.UserDTO;
import be.vinci.pae.domain.ucc.ContactUCC;
import be.vinci.pae.utils.Config;
import be.vinci.pae.utils.exceptions.FatalException;
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
   * returns a contact by its id.
   *
   * @param request the token from the front.
   * @param id      of the internship
   * @return internshipDTO
   */
  @GET
  @Path("/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  @Authorize
  public Response getOneContact(@Context ContainerRequest request, @PathParam("id") int id) {
    ContactDTO contactDTO = contactUCC.getOneById(id);
    String contact;

    try {
      contact = jsonMapper.writeValueAsString(contactDTO);
      return Response.ok(contact).build();
    } catch (JsonProcessingException e) {
      throw new FatalException(e);
    }
  }

  /**
   * Get all contacts by a student id.
   *
   * @return a list containing all the contacts by a student id.
   */
  @GET
  @Path("/all/{idStudent}")
  @Produces(MediaType.APPLICATION_JSON)
  @Authorize
  public List<ContactDTO> getAllByStudent(@Context ContainerRequest request,
      @PathParam("idStudent") int student) {
    UserDTO user = (UserDTO) request.getProperty("user");
    if (user.getId() != student) {
      throw new WebApplicationException("unauthorized", Response.Status.UNAUTHORIZED);
    }
    List<ContactDTO> contactDTOList;
    contactDTOList = contactUCC.getAllContactsByStudent(student);
    return contactDTOList;
  }

}
