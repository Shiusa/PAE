package be.vinci.pae.domain.ucc;

import be.vinci.pae.domain.dto.CompanyDTO;
import be.vinci.pae.services.dao.CompanyDAO;
import be.vinci.pae.utils.Logs;
import jakarta.inject.Inject;
import org.apache.logging.log4j.Level;

/**
 * Company UCC.
 */
public class CompanyUCCImpl implements CompanyUCC {

  @Inject
  private CompanyDAO companyDAO;

  @Override
  public CompanyDTO findOneById(int id) {
    Logs.log(Level.INFO, "CompanyUCC (findOneById) : entrance");
    Logs.log(Level.DEBUG, "CompanyUCC (findOneById) : success!");
    return companyDAO.getOneCompanyById(id);
  }
}
