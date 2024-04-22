package be.vinci.pae.api;

import be.vinci.pae.api.filters.Authorize;
import be.vinci.pae.domain.dto.InternshipDTO;
import be.vinci.pae.domain.dto.UserDTO;
import be.vinci.pae.domain.ucc.InternshipUCC;
import be.vinci.pae.utils.Logs;
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

  @POST
  @Path("create")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Authorize
  public InternshipDTO create(@Context ContainerRequest request, InternshipDTO internship) {
    Logs.log(Level.INFO, "InternshipResource (create) : entrance");
    if (internship.getContact() == null || internship.getSupervisor() == null
        || internship.getSignatureDate() == null) {
      Logs.log(Level.WARN, "InternshipResource (create) : missing input");
      throw new WebApplicationException("Inputs cannot be blank", Response.Status.BAD_REQUEST);
    }

    InternshipDTO internshipDTO = internshipUCC.createInternship(internship,
        ((UserDTO) request.getProperty("user")).getId());
    Logs.log(Level.DEBUG, "InternshipResource (create) : success!");
    return internshipDTO;
  }

}