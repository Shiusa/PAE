package be.vinci.pae.api;

import be.vinci.pae.domain.dto.InternshipDTO;
import be.vinci.pae.domain.ucc.InternshipUCC;
import be.vinci.pae.utils.Config;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.glassfish.jersey.server.ContainerRequest;

/**
 * CompanyResource class.
 */
@Singleton
@Path("/internships")
public class InternshipResource {

  private final Algorithm jwtAlgorithm = Algorithm.HMAC256(Config.getProperty("JWTSecret"));
  private final ObjectMapper jsonMapper = new ObjectMapper();

  @Inject
  private InternshipUCC internshipUCC;

  /**
   * returns an internship by a student id.
   *
   * @param request the token from the front.
   * @param idUser      of the student
   * @return internshipDTO
   */
  @GET
  @Path("/student/{idUser}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getOneInternshipByIdUser(@Context ContainerRequest request, @PathParam("idUser") int idUser) {
    InternshipDTO internshipDTO = internshipUCC.getOneByStudent(idUser);
    String internship = "";

    try {
      internship = jsonMapper.writeValueAsString(internshipDTO);
      return Response.ok(internship).build();

    } catch (Exception e) {
      return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Erreur lors de la récupération des données de l'utilisateur").build();
    }
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
  public Response getOneInternship(@Context ContainerRequest request, @PathParam("id") int id) {
    InternshipDTO internshipDTO = internshipUCC.getOneById(id);
    String internship = "";

    try {
      internship = jsonMapper.writeValueAsString(internshipDTO);
      return Response.ok(internship).build();

    } catch (Exception e) {
      return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Erreur lors de la récupération des données de l'utilisateur").build();
    }
  }

}