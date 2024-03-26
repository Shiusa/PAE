package be.vinci.pae.services.dao;

import be.vinci.pae.domain.dto.CompanyDTO;

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

}
