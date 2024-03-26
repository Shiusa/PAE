package be.vinci.pae.services.dao;

import be.vinci.pae.domain.CompanyFactory;
import be.vinci.pae.domain.dto.CompanyDTO;
import be.vinci.pae.services.dal.DalServices;
import be.vinci.pae.utils.Logs;
import be.vinci.pae.utils.exceptions.FatalException;
import jakarta.inject.Inject;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.logging.log4j.Level;

/**
 * Implementation of CompanyDAO.
 */
public class CompanyDAOImpl implements CompanyDAO {

  @Inject
  private DalServices dalServices;
  @Inject
  private CompanyFactory companyFactory;

  @Override
  public CompanyDTO getOneCompanyById(int id) {
    Logs.log(Level.INFO, "UserDAO (getOneUserByEmail) : entrance");
    String requestSql = """
        SELECT company_id, name, designation, address, phone_number, email, is_blacklisted,
        blacklist_motivation
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

  /**
   * Build the CompanyDTO on the prepared statement.
   *
   * @param ps the prepared statement.
   * @return the CompanyDTO built.
   */
  private CompanyDTO buildCompanyDTO(PreparedStatement ps) {
    CompanyDTO company = companyFactory.getCompanyDTO();
    try (ResultSet rs = ps.executeQuery()) {
      if (rs.next()) {
        company.setId(rs.getInt("company_id"));
        company.setName(rs.getString("name"));
        company.setDesignation(rs.getString("designation"));
        company.setAddress(rs.getString("address"));
        company.setPhoneNumber(rs.getString("phone_number"));
        company.setEmail(rs.getString("email"));
        company.setIsBlacklisted(rs.getBoolean("is_blacklisted"));
        company.setBlacklistMotivation(rs.getString("blacklist_motivation"));
        rs.close();
        return company;
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return null;
  }
}
