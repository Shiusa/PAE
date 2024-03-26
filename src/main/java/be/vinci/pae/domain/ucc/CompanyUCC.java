package be.vinci.pae.domain.ucc;

import be.vinci.pae.domain.dto.CompanyDTO;

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

}
