package be.vinci.pae.api;

import be.vinci.pae.api.filters.Authorize;
import be.vinci.pae.domain.dto.InternshipDTO;
import be.vinci.pae.domain.dto.UserDTO;
import be.vinci.pae.domain.ucc.InternshipUCC;
import be.vinci.pae.utils.Logs;
import com.fasterxml.jackson.databind.JsonNode;
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
import jakarta.ws.rs.core.Response.Status;
import org.apache.logging.log4j.Level;
import org.glassfish.jersey.server.ContainerRequest;

/**
 * InternshipResource class.
 */
@Singleton
@Path("/internships")
public class InternshipResource {

  @Inject
  private InternshipUCC internshipUCC;

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
  public InternshipDTO getOneInternshipByIdUser(@Context ContainerRequest request,
      @PathParam("idUser") int idUser) {
    Logs.log(Level.INFO, "InternshipResource (getOneInternshipByIdUser) : entrance");
    UserDTO user = (UserDTO) request.getProperty("user");
    if (user.getId() != idUser && (!user.getRole().equals("Professeur") || !user.getRole()
        .equals("Professeur"))) {
      Logs.log(Level.ERROR, "InternshipResource (getOneInternshipByIdUser) : unauthorized");
      throw new WebApplicationException("unauthorized", Status.FORBIDDEN);
    }
    return internshipUCC.getOneByStudent(user.getId());
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
  public InternshipDTO getOneInternship(@Context ContainerRequest request,
      @PathParam("id") int id) {
    Logs.log(Level.INFO, "InternshipResource (getOneInternship) : entrance");
    UserDTO user = (UserDTO) request.getProperty("user");
    return internshipUCC.getOneById(id, user.getId());
  }

  /**
   * Starting contact route.
   *
   * @param request the token.
   * @param json    jsonNode containing user id and company id to start the contact.
   * @return the started contact.
   */
  @POST
  @Path("editProject")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Authorize
  public InternshipDTO editProject(@Context ContainerRequest request, JsonNode json) {
    Logs.log(Level.INFO, "InternshipResource (editSubject) : entrance");
    if (!json.hasNonNull("project")) {
      Logs.log(Level.WARN, "InternshipResource (editSubject) : project is null");
      throw new WebApplicationException("project required", Response.Status.BAD_REQUEST);
    }
    if (json.get("project").asText().isBlank()) {
      Logs.log(Level.WARN, "InternshipResource (editSubject) : project is blank");
      throw new WebApplicationException("project required", Response.Status.BAD_REQUEST);
    }
    if (!json.hasNonNull("version")) {
      Logs.log(Level.WARN, "InternshipResource (editSubject) : version is null");
      throw new WebApplicationException("version required", Response.Status.BAD_REQUEST);
    }
    if (json.get("version").asText().isBlank()) {
      Logs.log(Level.WARN, "InternshipResource (editSubject) : version is blank");
      throw new WebApplicationException("version required", Response.Status.BAD_REQUEST);
    }
    if (!json.hasNonNull("internshipId")) {
      Logs.log(Level.WARN, "InternshipResource (editSubject) : internshipId is null");
      throw new WebApplicationException("internshipId required", Response.Status.BAD_REQUEST);
    }
    if (json.get("internshipId").asText().isBlank()) {
      Logs.log(Level.WARN, "InternshipResource (editSubject) : internshipId is blank");
      throw new WebApplicationException("internshipId required", Response.Status.BAD_REQUEST);
    }

    String project = json.get("project").asText();
    int version = json.get("version").asInt();
    int internshipId = json.get("internshipId").asInt();

    InternshipDTO internshipDTO = internshipUCC.editProject(project, version, internshipId);

    Logs.log(Level.DEBUG, "ContactResource (start) : success!");
    return internshipDTO;
  }
}