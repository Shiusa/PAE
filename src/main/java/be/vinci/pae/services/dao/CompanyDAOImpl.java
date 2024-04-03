package be.vinci.pae.services.dao;

import be.vinci.pae.domain.CompanyFactory;
import be.vinci.pae.domain.dto.CompanyDTO;
import be.vinci.pae.services.dal.DalBackendServices;
import be.vinci.pae.utils.Logs;
import be.vinci.pae.utils.exceptions.FatalException;
import jakarta.inject.Inject;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.Level;

/**
 * Implementation of CompanyDAO.
 */
public class CompanyDAOImpl implements CompanyDAO {

  @Inject
  private DalBackendServices dalServices;
  @Inject
  private CompanyFactory companyFactory;

  @Override
  public CompanyDTO getOneCompanyById(int id) {
    Logs.log(Level.INFO, "UserDAO (getOneUserByEmail) : entrance");
    String requestSql = """
        SELECT *
        FROM prostage.companies
        WHERE company_id = ?
        """;
    PreparedStatement ps = dalServices.getPreparedStatement(requestSql);
    try {
      ps.setInt(1, id);
    } catch (SQLException e) {
      Logs.log(Level.FATAL, "CompanyDAO (getOneCompanyById) : internal error");
      throw new FatalException(e);
    }
    Logs.log(Level.DEBUG, "CompanyDAO (getOneCompanyById) : success!");
    return buildCompanyDTO(ps);
  }

  @Override
  public List<CompanyDTO> getAllCompanies() {
    Logs.log(Level.DEBUG, "CompanyDAO (getAllCompanies) : entrance");

    String requestSql = """
        SELECT *
        FROM prostage.companies
        """;
    PreparedStatement ps = dalServices.getPreparedStatement(requestSql);

    List<CompanyDTO> companyDTOList = buildCompanyList(ps);

    Logs.log(Level.DEBUG, "CompanyDAO (getAllCompanies) : success!");
    return companyDTOList;
  }

  @Override
  public List<CompanyDTO> getAllCompaniesByUserId(int userId) {
    Logs.log(Level.DEBUG, "CompanyDAO (getAllCompaniesByUserId) : entrance");

    String requestSql = """
        SELECT *
        FROM prostage.companies
        WHERE company_id NOT IN (
          SELECT DISTINCT company
          FROM prostage.contacts
          WHERE student = ?
        )
        """;

    try (PreparedStatement ps = dalServices.getPreparedStatement(requestSql)) {
      ps.setInt(1, userId);
      List<CompanyDTO> companyDTOList = buildCompanyList(ps);

      Logs.log(Level.DEBUG, "CompanyDAO (getAllCompaniesByUserId) : success!");
      return companyDTOList;
    } catch (SQLException e) {
      throw new FatalException(e);
    }
  }

  /**
   * Build a list of companies based on the prepared statement.
   *
   * @param ps the prepared statement.
   * @return the list of companies.
   */
  private List<CompanyDTO> buildCompanyList(PreparedStatement ps) {
    List<CompanyDTO> companyDTOList = new ArrayList<>();
    try (ResultSet rs = ps.executeQuery()) {
      while (rs.next()) {
        CompanyDTO companyDTO = buildCompanyDTO(rs);
        companyDTOList.add(companyDTO);
      }
      return companyDTOList;
    } catch (SQLException e) {
      Logs.log(Level.FATAL, "CompanyDAO (getAllCompaniesByUserId) : internal error!");
      throw new FatalException(e);
    }
  }

  /**
   * Build the CompanyDTO on the prepared statement.
   *
   * @param ps the prepared statement.
   * @return the CompanyDTO built.
   */
  private CompanyDTO buildCompanyDTO(PreparedStatement ps) {
    try (ResultSet rs = ps.executeQuery()) {
      if (rs.next()) {
        return buildCompanyDTO(rs);
      }
      return null;
    } catch (SQLException e) {
      throw new FatalException(e);
    }
  }

  /**
   * Build a companyDTO based on a result set.
   *
   * @param rs the result set.
   * @return the companyDTO built.
   */
  private CompanyDTO buildCompanyDTO(ResultSet rs) {
    try {
      CompanyDTO companyDTO = companyFactory.getCompanyDTO();
      companyDTO.setId(rs.getInt("company_id"));
      companyDTO.setName(rs.getString("name"));
      companyDTO.setDesignation(rs.getString("designation"));
      companyDTO.setAddress(rs.getString("address"));
      companyDTO.setPhoneNumber(rs.getString("phone_number"));
      companyDTO.setEmail(rs.getString("email"));
      companyDTO.setIsBlacklisted(rs.getBoolean("is_blacklisted"));
      companyDTO.setBlacklistMotivation(rs.getString("blacklist_motivation"));
      companyDTO.setVersion(rs.getInt("version"));
      return companyDTO;
    } catch (SQLException e) {
      throw new FatalException(e);
    }
  }

}
