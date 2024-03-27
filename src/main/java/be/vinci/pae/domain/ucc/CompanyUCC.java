package be.vinci.pae.domain.ucc;

import be.vinci.pae.domain.dto.CompanyDTO;
import be.vinci.pae.domain.dto.UserDTO;
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

}
