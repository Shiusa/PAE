package be.vinci.pae.api;

import be.vinci.pae.api.filters.Authorize;
import be.vinci.pae.domain.dto.InternshipDTO;
import be.vinci.pae.domain.dto.UserDTO;
import be.vinci.pae.domain.ucc.InternshipUCC;
import be.vinci.pae.utils.Logs;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import jakarta.ws.rs.core.Response.Status;
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
  public Response getOneInternshipByIdUser(@Context ContainerRequest request,
      @PathParam("idUser") int idUser) {
    Logs.log(Level.INFO, "InternshipResource (getOneInternshipByIdUser) : entrance");
    UserDTO user = (UserDTO) request.getProperty("user");
    if (user.getId() != idUser) {
      Logs.log(Level.ERROR, "InternshipResource (getOneInternshipByIdUser) : unauthorized");
      throw new WebApplicationException("unauthorized", Response.Status.UNAUTHORIZED);
    }
    InternshipDTO internshipDTO = internshipUCC.getOneByStudent(idUser);
    String internship;
    try {
      internship = jsonMapper.writeValueAsString(internshipDTO);
    } catch (JsonProcessingException e) {
      Logs.log(Level.FATAL,
          "InternshipResource (getOneInternshipByIdUser) : internal error");
      throw new WebApplicationException("internal error", Status.INTERNAL_SERVER_ERROR);
    }
    Logs.log(Level.DEBUG, "InternshipResource (getOneInternshipByIdUser) : success!");
    return Response.ok(internship).build();
  }

  /**
   * returns an internship by its id.
   *
   * @param request the token from the front.
   * @param id      of the internship
   * @return internshipDTO
   */
  @GET
  @Path("/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  @Authorize
  public Response getOneInternship(@Context ContainerRequest request, @PathParam("id") int id) {
    Logs.log(Level.INFO, "InternshipResource (getOneInternship) : entrance");
    UserDTO user = (UserDTO) request.getProperty("user");
    InternshipDTO internshipDTO = internshipUCC.getOneById(id, user.getId());
    String internship;
    try {
      internship = jsonMapper.writeValueAsString(internshipDTO);
    } catch (JsonProcessingException e) {
      Logs.log(Level.FATAL,
          "InternshipResource (getOneInternship) : internal error");
      throw new WebApplicationException("internal error", Status.INTERNAL_SERVER_ERROR);
    }
    Logs.log(Level.DEBUG, "InternshipResource (getOneInternship) : success!");
    return Response.ok(internship).build();
  }

}