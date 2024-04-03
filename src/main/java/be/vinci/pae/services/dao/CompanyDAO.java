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
   * Get one company by it's name and designation.
   *
   * @param name        name of the company.
   * @param designation designation of the company.
   * @return the CompanyDTO if found, null otherwise.
   */
  CompanyDTO getOneCompanyByNameDesignation(String name, String designation);

  /**
   * Get all users.
   *
   * @return a list of all users.
   */
  List<CompanyDTO> getAllCompanies();

  /**
   * Get all users.
   *
   * @param userId the user id.
   * @return a list of all users.
   */
  List<CompanyDTO> getAllCompaniesByUserId(int userId);

  /**
   * Add one company.
   *
   * @param company company to add.
   * @return CompanyDTO of added company, null otherwise.
   */
  CompanyDTO addOneCompany(CompanyDTO company);

}
