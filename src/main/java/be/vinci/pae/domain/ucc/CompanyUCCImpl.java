package be.vinci.pae.domain.ucc;

import be.vinci.pae.domain.dto.CompanyDTO;
import be.vinci.pae.domain.dto.UserDTO;
import be.vinci.pae.services.dal.DalServices;
import be.vinci.pae.services.dao.CompanyDAO;
import be.vinci.pae.services.dao.UserDAO;
import be.vinci.pae.utils.Logs;
import be.vinci.pae.utils.exceptions.DuplicateException;
import be.vinci.pae.utils.exceptions.FatalException;
import be.vinci.pae.utils.exceptions.ResourceNotFoundException;
import jakarta.inject.Inject;
import java.util.List;
import org.apache.logging.log4j.Level;

/**
 * Company UCC.
 */
public class CompanyUCCImpl implements CompanyUCC {

  @Inject
  private CompanyDAO companyDAO;
  @Inject
  private UserDAO userDAO;
  @Inject
  private DalServices dalServices;

  @Override
  public CompanyDTO findOneById(int id) {
    Logs.log(Level.INFO, "CompanyUCC (findOneById) : entrance");
    CompanyDTO company;
    try {
      dalServices.startTransaction();
      company = companyDAO.getOneCompanyById(id);
    } catch (Exception e) {
      dalServices.rollbackTransaction();
      throw e;
    }
    Logs.log(Level.DEBUG, "CompanyUCC (findOneById) : success!");
    dalServices.commitTransaction();
    return company;
  }

  /**
   * Get all companies.
   *
   * @return a list containing all the companies.
   */
  @Override
  public List<CompanyDTO> getAllCompanies() {
    List<CompanyDTO> companyList;
    try {
      Logs.log(Level.DEBUG, "CompanyUCC (getAllCompanies) : entrance");
      dalServices.startTransaction();
      companyList = companyDAO.getAllCompanies();
    } catch (FatalException e) {
      dalServices.rollbackTransaction();
      throw e;
    }
    dalServices.commitTransaction();
    Logs.log(Level.DEBUG, "CompanyUCC (getAllCompanies) : success!");
    return companyList;
  }

  /**
   * Get all companies available for one user.
   *
   * @return a list containing all the companies.
   */
  @Override
  public List<CompanyDTO> getAllCompaniesByUser(int userId) {
    List<CompanyDTO> companyList;
    try {
      Logs.log(Level.DEBUG, "CompanyUCC (getAllCompaniesByUser) : entrance");
      dalServices.startTransaction();
      UserDTO user = userDAO.getOneUserById(userId);
      if (user == null) {
        Logs.log(Level.ERROR, "CompanyUCC (getAllCompaniesByUser) : user not found");
        throw new ResourceNotFoundException();
      }
      companyList = companyDAO.getAllCompaniesByUserId(userId);
      dalServices.commitTransaction();
      Logs.log(Level.DEBUG, "CompanyUCC (getAllCompaniesByUser) : success!");
      return companyList;
    } catch (Exception e) {
      dalServices.rollbackTransaction();
      throw e;
    }
  }

  @Override
  public CompanyDTO blacklist(int companyId, String blacklistMotivation) {
    CompanyDTO companyDTO;
    Logs.log(Level.DEBUG, "CompanyUCC (blacklist) : entrance");
    try {
      dalServices.startTransaction();
      companyDTO = companyDAO.getOneCompanyById(companyId);
      if (companyDTO.isBlacklisted()) {
        Logs.log(Level.ERROR, "CompanyUCC (blacklist) : the company is already blacklisted");
        dalServices.rollbackTransaction();
        throw new DuplicateException();
      }
      int version = companyDTO.getVersion();
      companyDTO = companyDAO.blacklist(companyId, blacklistMotivation, version);
    } catch (FatalException e) {
      dalServices.rollbackTransaction();
      throw e;
    }
    dalServices.commitTransaction();
    return companyDTO;
  }

}
