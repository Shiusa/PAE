package be.vinci.pae.services.dao;

import be.vinci.pae.domain.dto.CompanyDTO;
import java.util.List;
import java.util.Map;

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
   * @return a list of all companies including their internship count by year.
   */
  Map<Integer, Map<CompanyDTO, Map<String, Integer>>> getAllCompanies();

  /**
   * Get all companies by company by user id.
   *
   * @param userId the user id.
   * @return a list of all companies.
   */
  List<CompanyDTO> getAllCompaniesByUserId(int userId);

  /**
   * Get all companies by company name.
   *
   * @param name the company name.
   * @return a list of all companies.
   */
  List<CompanyDTO> getAllCompaniesByName(String name);

  /**
   * Add one company.
   *
   * @param company company to add.
   * @return CompanyDTO of added company, null otherwise.
   */
  CompanyDTO addOneCompany(CompanyDTO company);

}
