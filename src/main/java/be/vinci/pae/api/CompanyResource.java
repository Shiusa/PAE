package be.vinci.pae.api;

import be.vinci.pae.api.filters.Authorize;
import be.vinci.pae.domain.dto.CompanyDTO;
import be.vinci.pae.domain.ucc.CompanyUCC;
import be.vinci.pae.utils.Logs;
import be.vinci.pae.utils.exceptions.FatalException;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import java.util.List;
import org.apache.logging.log4j.Level;

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
    try {
      companyDTOList = companyUCC.getAllCompanies();
    } catch (FatalException e) {
      throw e;
    }
    Logs.log(Level.INFO, "CompanyResource (getAll) : success!");
    return companyDTOList;
  }

}
