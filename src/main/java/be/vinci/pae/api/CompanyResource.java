package be.vinci.pae.api;

import be.vinci.pae.api.filters.Authorize;
import be.vinci.pae.api.filters.Teacher;
import be.vinci.pae.domain.dto.CompanyDTO;
import be.vinci.pae.domain.dto.UserDTO;
import be.vinci.pae.domain.ucc.CompanyUCC;
import be.vinci.pae.utils.Logs;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
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
 * CompanyResource class.
 */
@Singleton
@Path("/companies")
public class CompanyResource {

  private final ObjectMapper jsonMapper = new ObjectMapper();
  @Inject
  private CompanyUCC companyUCC;

  /**
   * Get one company by its id.
   *
   * @param id the company's id.
   * @return the company in object node.
   */
  @GET
  @Path("/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  @Authorize
  public ObjectNode getOneById(@PathParam("id") int id) {
    Logs.log(Level.INFO, "CompanyResource (getOneById) : entrance");
    CompanyDTO company = companyUCC.findOneById(id);
    Logs.log(Level.DEBUG, "CompanyResource (getOneById) : success!");
    return jsonMapper.createObjectNode().putPOJO("company", company);
  }

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
   * @param request the token.
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

  /**
   * POST to blacklist one company by its id.
   *
   * @param json    containing the id of the company.
   * @param request containing the token of the user
   * @return the company in object node.
   */
  @POST
  @Path("/blacklist")
  @Produces(MediaType.APPLICATION_JSON)
  @Teacher
  public ObjectNode blacklist(@Context ContainerRequest request, JsonNode json) {
    Logs.log(Level.INFO, "CompanyResource (blacklist) : entrance");
    if (!json.hasNonNull("company")) {
      Logs.log(Level.WARN, "ContactResource (start) : Company is null");
      throw new WebApplicationException("company required", Response.Status.BAD_REQUEST);
    }
    if (json.get("company").asText().isBlank()) {
      Logs.log(Level.WARN, "ContactResource (start) : Company is blank");
      throw new WebApplicationException("company required", Response.Status.BAD_REQUEST);
    }

    if (!json.hasNonNull("blacklistMotivation")) {
      Logs.log(Level.WARN, "ContactResource (start) : blacklistMotivation is null");
      throw new WebApplicationException("blacklist's motivation required",
          Response.Status.BAD_REQUEST);
    }
    if (json.get("blacklistMotivation").asText().isBlank()) {
      Logs.log(Level.WARN, "ContactResource (start) : blacklistMotivation is blank");
      throw new WebApplicationException("blacklist's motivation required",
          Response.Status.BAD_REQUEST);
    }

    int companyId = json.get("company").asInt();
    String blacklistMotivation = json.get("blacklistMotivation").asText();

    CompanyDTO company = companyUCC.blacklist(companyId, blacklistMotivation);

    Logs.log(Level.DEBUG, "CompanyResource (getOneById) : success!");
    return jsonMapper.createObjectNode().putPOJO("company", company);
  }

}
