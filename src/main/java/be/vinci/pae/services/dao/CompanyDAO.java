package be.vinci.pae.services.dao;

import be.vinci.pae.domain.dto.CompanyDTO;
import java.util.List;

/**
 * CompanyDAO interface.
 */
public interface CompanyDAO {

  /**
   * Get one company by its id.
   *
   * @param id id of the company.
   * @return the company if found.
   */
  CompanyDTO getOneCompanyById(int id);

  /**
   * Get all companies.
   *
   * @return a list of all companies.
   */
  List<CompanyDTO> getAllCompanies();

  /**
   * Get all companies that the user has not a contact with.
   *
   * @param userId the user id.
   * @return a list of all companies that the user has not a contact with.
   */
  List<CompanyDTO> getAllCompaniesByUserId(int userId);

  /**
   * To blacklist a company.
   *
   * @param companyId           the company id.
   * @param version             the last version of the company.
   * @param blacklistMotivation the motivation of the blackist
   * @return the company that has been blacklisted.
   */
  CompanyDTO blacklist(int companyId, String blacklistMotivation, int version);
}
