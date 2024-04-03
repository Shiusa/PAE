package be.vinci.pae.api;

import be.vinci.pae.api.filters.Authorize;
import be.vinci.pae.domain.dto.ContactDTO;
import be.vinci.pae.domain.dto.InternshipDTO;
import be.vinci.pae.domain.dto.SupervisorDTO;
import be.vinci.pae.domain.dto.UserDTO;
import be.vinci.pae.domain.ucc.CompanyUCC;
import be.vinci.pae.domain.ucc.ContactUCC;
import be.vinci.pae.domain.ucc.InternshipUCC;
import be.vinci.pae.domain.ucc.SupervisorUCC;
import be.vinci.pae.utils.Logs;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.apache.logging.log4j.Level;
import org.glassfish.jersey.server.ContainerRequest;

/**
 * CompanyResource class.
 */
@Singleton
@Path("/internships")
public class InternshipResource {

  private final ObjectMapper jsonMapper = new ObjectMapper();

  @Inject
  private InternshipUCC internshipUCC;
  @Inject
  private ContactUCC contactUCC;
  @Inject
  private CompanyUCC companyUCC;
  @Inject
  private SupervisorUCC supervisorUCC;

  /**
   * returns an internship by a student id.
   *
   * @param request the token from the front.
   * @param idUser  of the student
   * @return internshipDTO
   */
  @GET
  @Path("/student/{idUser}")
  @Produces(MediaType.APPLICATION_JSON)
  @Authorize
  public ObjectNode getOneInternshipByIdUser(@Context ContainerRequest request,
      @PathParam("idUser") int idUser) {
    Logs.log(Level.INFO, "InternshipResource (getOneInternshipByIdUser) : entrance");
    UserDTO user = (UserDTO) request.getProperty("user");
    if (user.getId() != idUser) {
      Logs.log(Level.ERROR, "InternshipResource (getOneInternshipByIdUser) : unauthorized");
      throw new WebApplicationException("unauthorized", Response.Status.UNAUTHORIZED);
    }
    InternshipDTO internship = internshipUCC.getOneByStudent(user.getId());
    return buildJsonMapperInternship(internship);
  }

  /**
   * returns an internship by its id.
   *
   * @param request the token from the front.
   * @param id      of the internship.
   * @return internshipDTO with all details.
   */
  @GET
  @Path("/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  @Authorize
  public ObjectNode getOneInternship(@Context ContainerRequest request, @PathParam("id") int id) {
    Logs.log(Level.INFO, "InternshipResource (getOneInternship) : entrance");
    UserDTO user = (UserDTO) request.getProperty("user");
    InternshipDTO internship = internshipUCC.getOneById(id, user.getId());
    return buildJsonMapperInternship(internship);
  }

  /**
   * Build the ObjectNode with user, contact, company, supervisor, and internship.
   *
   * @param internship the internship.
   * @return the objectnode built.
   */
  private ObjectNode buildJsonMapperInternship(InternshipDTO internship) {
    ContactDTO contact = contactUCC.getOneById(internship.getContact());
    // CompanyDTO company = companyUCC.findOneById(contact.getCompany());
    SupervisorDTO supervisor = supervisorUCC.getOneById(internship.getSupervisor());

    return jsonMapper.createObjectNode().putPOJO("internship", internship)
        .putPOJO("contact", contact)
        .putPOJO("company", contact.getCompany())
        .putPOJO("user", contact.getStudent())
        .putPOJO("supervisor", supervisor);
  }

}