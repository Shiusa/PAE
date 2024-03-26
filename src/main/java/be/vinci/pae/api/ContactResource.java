package be.vinci.pae.api;

import be.vinci.pae.api.filters.Authorize;
import be.vinci.pae.domain.dto.ContactDTO;
import be.vinci.pae.domain.dto.UserDTO;
import be.vinci.pae.domain.ucc.ContactUCC;
import be.vinci.pae.utils.Logs;
import be.vinci.pae.utils.exceptions.FatalException;
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
import org.apache.logging.log4j.Level;
import org.glassfish.jersey.server.ContainerRequest;

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
    UserDTO userDTO = (UserDTO) request.getProperty("user");
    int student = userDTO.getId();

    ContactDTO contactDTO = contactUCC.start(company, student);

    ObjectNode contact = jsonMapper.createObjectNode().putPOJO("contact", contactDTO);
    Logs.log(Level.DEBUG, "ContactResource (start) : success!");
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
    Logs.log(Level.INFO, "ContactResource (getOneContact) : entrance");
    ContactDTO contactDTO = contactUCC.getOneById(id);
    String contact;

    try {
      contact = jsonMapper.writeValueAsString(contactDTO);
      return Response.ok(contact).build();
    } catch (JsonProcessingException e) {
      Logs.log(Level.FATAL, "ContactResource (getOneContact) : internal error");
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
    Logs.log(Level.INFO, "ContactResource (getAllByStudent) : entrance");
    UserDTO user = (UserDTO) request.getProperty("user");
    if (user.getId() != student) {
      Logs.log(Level.ERROR, "ContactResource (getAllByStudent) : not allowed");
      throw new WebApplicationException("unauthorized", Response.Status.UNAUTHORIZED);
    }
    List<ContactDTO> contactDTOList;
    contactDTOList = contactUCC.getAllContactsByStudent(student);
    Logs.log(Level.DEBUG, "ContactResource (getAllByStudent) : success!");
    return contactDTOList;
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
    Logs.log(Level.INFO, "ContactResource (unsupervise) : entrance");
    UserDTO userDTO = (UserDTO) request.getProperty("user");
    if (!json.hasNonNull("contactId")) {
      Logs.log(Level.ERROR, "ContactResource (unsupervise) : contact null");
      throw new WebApplicationException("Contact id required", Response.Status.BAD_REQUEST);
    }
    if (json.get("contactId").asText().isBlank()) {
      Logs.log(Level.ERROR, "ContactResource (unsupervise) : contact blank");
      throw new WebApplicationException("Contact id cannot be blank", Response.Status.BAD_REQUEST);
    }
    int contactId = json.get("contactId").asInt();
    int student = userDTO.getId();

    ContactDTO contactDTO = contactUCC.unsupervise(contactId, student);
    Logs.log(Level.DEBUG, "ContactResource (unsupervise) : success!");
    return jsonMapper.createObjectNode().putPOJO("contact", contactDTO);
  }
}
