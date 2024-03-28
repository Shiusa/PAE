package be.vinci.pae.api;

import be.vinci.pae.api.filters.Authorize;
import be.vinci.pae.domain.dto.CompanyDTO;
import be.vinci.pae.domain.dto.UserDTO;
import be.vinci.pae.domain.ucc.CompanyUCC;
import be.vinci.pae.utils.Logs;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import java.util.List;
import org.apache.logging.log4j.Level;
import org.glassfish.jersey.server.ContainerRequest;

/**
 * CompanyResource class.
 */
@Singleton
@Path("/companies")
public class CompanyResource {

  @Inject
  private CompanyUCC companyUCC;

  /**
   * Get all companies.
   *
   * @return a list containing all the companies.
   */
  @GET
  @Path("all")
  @Produces(MediaType.APPLICATION_JSON)
  @Authorize
  public List<CompanyDTO> getAll() {
    Logs.log(Level.INFO, "CompanyResource (getAll) : entrance");
    List<CompanyDTO> companyDTOList;
    companyDTOList = companyUCC.getAllCompanies();
    Logs.log(Level.INFO, "CompanyResource (getAll) : success!");
    return companyDTOList;
  }

  /**
   * Get all companies available for the logged users.
   *
   * @return a list containing all the companies.
   */
  @GET
  @Path("all/user")
  @Produces(MediaType.APPLICATION_JSON)
  @Authorize
  public List<CompanyDTO> getAllByUser(@Context ContainerRequest request) {
    Logs.log(Level.INFO, "CompanyResource (getAllByUser) : entrance");
    UserDTO loggedUser = (UserDTO) request.getProperty("user");

    List<CompanyDTO> companyDTOList;
    companyDTOList = companyUCC.getAllCompaniesByUser(loggedUser.getId());
    Logs.log(Level.INFO, "CompanyResource (getAllByUser) : success!");
    return companyDTOList;
  }

}
