package be.vinci.pae.domain.ucc;

import be.vinci.pae.domain.dto.CompanyDTO;
import java.util.List;

/**
 * CompanyUCC interface.
 */
public interface CompanyUCC {

  /**
   * Get a company by its id.
   *
   * @param id the company id.
   * @return the company if found.
   */
  CompanyDTO findOneById(int id);

  /**
   * Get all companies.
   *
   * @return a list containing all companies.
   */
  List<CompanyDTO> getAllCompanies();

  /**
   * Get all companies available for one user.
   *
   * @param userId the user id.
   * @return a list containing all companies.
   */
  List<CompanyDTO> getAllCompaniesByUser(int userId);

  /**
   * To blacklist a company.
   *
   * @param companyId           the company id
   * @param blacklistMotivation the motivation of the blacklist.
   * @return a list containing all companies.
   */
  CompanyDTO blacklist(int companyId, String blacklistMotivation);

}
