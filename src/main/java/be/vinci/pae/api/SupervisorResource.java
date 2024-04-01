package be.vinci.pae.api;

import be.vinci.pae.api.filters.Authorize;
import be.vinci.pae.domain.dto.SupervisorDTO;
import be.vinci.pae.domain.ucc.SupervisorUCC;
import be.vinci.pae.utils.Logs;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import org.apache.logging.log4j.Level;
import org.glassfish.jersey.server.ContainerRequest;

/**
 * SupervisorResource class.
 */
@Singleton
@Path("/supervisors")
public class SupervisorResource {

  @Inject
  private SupervisorUCC supervisorUCC;

  /**
   * returns a supervisor by a supervisor id.
   *
   * @param request the token from the front.
   * @param id      of the supervisor
   * @return supervisorDTO
   */
  @GET
  @Path("/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  @Authorize
  public SupervisorDTO getOneSupervisorById(@Context ContainerRequest request,
      @PathParam("id") int id) {
    Logs.log(Level.INFO, "SupervisorResource (getOneSupervisorById) : entrance");
    return supervisorUCC.getOneById(id);
  }

}
