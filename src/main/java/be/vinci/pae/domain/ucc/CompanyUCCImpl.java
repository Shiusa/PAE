package be.vinci.pae.domain.ucc;

import be.vinci.pae.domain.dto.CompanyDTO;
import be.vinci.pae.services.dal.DalServicesConnection;
import be.vinci.pae.services.dao.CompanyDAO;
import be.vinci.pae.utils.Logs;
import be.vinci.pae.utils.exceptions.FatalException;
import jakarta.inject.Inject;
import org.apache.logging.log4j.Level;

/**
 * Company UCC.
 */
public class CompanyUCCImpl implements CompanyUCC {

  @Inject
  private CompanyDAO companyDAO;
  @Inject
  private DalServicesConnection dalServices;

  @Override
  public CompanyDTO findOneById(int id) {
    Logs.log(Level.INFO, "CompanyUCC (findOneById) : entrance");
    CompanyDTO company;
    try {
      dalServices.startTransaction();
      company = companyDAO.getOneCompanyById(id);
    } catch (FatalException e) {
      dalServices.rollbackTransaction();
      throw e;
    }
    Logs.log(Level.DEBUG, "CompanyUCC (findOneById) : success!");
    dalServices.commitTransaction();
    return company;
  }
}
