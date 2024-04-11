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
        SELECT cm.company_id, cm.name, cm.designation, cm.address,
        cm.phone_number AS cm_phone_number,
        cm.email AS cm_email, cm.is_blacklisted, cm.blacklist_motivation, cm.version AS cm_version
        FROM prostage.companies cm
        WHERE company_id = ?
        """;

    try (PreparedStatement ps = dalServices.getPreparedStatement(requestSql)) {
      ps.setInt(1, id);
      Logs.log(Level.DEBUG, "CompanyDAO (getOneCompanyById) : success!");
      return buildCompanyDTO(ps);
    } catch (SQLException e) {
      Logs.log(Level.FATAL, "CompanyDAO (getOneCompanyById) : internal error");
      throw new FatalException(e);
    }
  }

  @Override
  public CompanyDTO getOneCompanyByNameDesignation(String name, String designation) {
    Logs.log(Level.DEBUG, "CompanyDAO (getOneCompanyByNameDesignation) : success!");
    String requestSql = """
        SELECT cm.company_id, cm.name, cm.designation, cm.address,
        cm.phone_number AS cm_phone_number,
        cm.email AS cm_email, cm.is_blacklisted, cm.blacklist_motivation, cm.version AS cm_version
        FROM prostage.companies cm
        WHERE cm.name = ? AND cm.designation = ?
        """;

    try (PreparedStatement ps = dalServices.getPreparedStatement(requestSql)) {
      ps.setString(1, name);
      ps.setString(2, designation);
      Logs.log(Level.DEBUG, "CompanyDAO (getOneCompanyByNameDesignation) : success!");
      return buildCompanyDTO(ps);
    } catch (SQLException e) {
      Logs.log(Level.FATAL, "CompanyDAO (getOneCompanyByNameDesignation) : internal error");
      throw new FatalException(e);
    }
  }

  @Override
  public List<CompanyDTO> getAllCompanies() {
    Logs.log(Level.DEBUG, "CompanyDAO (getAllCompanies) : entrance");

    String requestSql = """
        SELECT cm.company_id, cm.name, cm.designation, cm.address,
        cm.phone_number AS cm_phone_number,
        cm.email AS cm_email, cm.is_blacklisted, cm.blacklist_motivation, cm.version AS cm_version
        FROM prostage.companies cm
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
        SELECT cm.company_id, cm.name, cm.designation, cm.address,
        cm.phone_number AS cm_phone_number,
        cm.email AS cm_email, cm.is_blacklisted, cm.blacklist_motivation, cm.version AS cm_version
        FROM prostage.companies cm
        WHERE cm.company_id NOT IN (
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

  @Override
  public List<CompanyDTO> getAllCompaniesByName(String name) {
    String requestSql = """
        SELECT cm.company_id, cm.name, cm.designation, cm.address,
        cm.phone_number AS cm_phone_number,
        cm.email AS cm_email, cm.is_blacklisted, cm.blacklist_motivation, cm.version AS cm_version
        FROM prostage.companies cm
        WHERE cm.name = ? AND cm.designation IS NULL
        """;

    try (PreparedStatement ps = dalServices.getPreparedStatement(requestSql)) {
      ps.setString(1, name);
      List<CompanyDTO> companyDTOList = buildCompanyList(ps);

      Logs.log(Level.DEBUG, "CompanyDAO (getAllCompaniesByName) : success!");
      return companyDTOList;
    } catch (SQLException e) {
      throw new FatalException(e);
    }
  }

  /**
   * Add one company.
   *
   * @param company company to add.
   * @return CompanyDTO of added company, null otherwise.
   */
  @Override
  public CompanyDTO addOneCompany(CompanyDTO company) {

    Logs.log(Level.DEBUG, "UserDAO (addOneUser) : entrance");
    String requestSql = """
        INSERT INTO prostage.companies(name, designation, address, phone_number, email, is_blacklisted, blacklist_motivation, version) VALUES (?,?,?,?,?,?,?,?)
        RETURNING name AS inserted_name, designation AS inserted_designation, company_id
        """;

    try (PreparedStatement ps = dalServices.getPreparedStatement(requestSql)) {
      ps.setString(1, company.getName());
      ps.setString(2, company.getDesignation());
      ps.setString(3, company.getAddress());
      ps.setString(4, company.getPhoneNumber());
      ps.setString(5, company.getEmail());
      ps.setBoolean(6, company.isBlacklisted());
      ps.setString(7, company.getBlacklistMotivation());
      ps.setInt(8, company.getVersion());
      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          /*CompanyDTO insertedCompany;
          insertedCompany = getOneCompanyByNameDesignation(rs.getString("inserted_name"),
              rs.getString("inserted_designation"));
          if (insertedCompany == null) {
            insertedCompany = getOneCompanyById(rs.getInt("company_id"));
            if (insertedCompany != null) {
              return insertedCompany;
            }
            return null;
          }*/
          Logs.log(Level.DEBUG, "UserDAO (addOneUser) : success!");
          /*if (company.getDesignation()==null) {
            return getOneCompanyById(rs.getInt("company_id"));
          }
          return getOneCompanyByNameDesignation(rs.getString("inserted_name"),
              rs.getString("inserted_designation"));*/
          return getOneCompanyById(rs.getInt("company_id"));
        }
        return null;
      }
    } catch (SQLException e) {
      Logs.log(Level.FATAL, "UserDAO (addOneUser) : internal error");
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
        /*CompanyDTO companyDTO = buildCompanyDTO(rs);*/
        CompanyDTO companyDTO = DTOSetServices.setCompanyDTO(companyFactory.getCompanyDTO(), rs);
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
        /*return buildCompanyDTO(rs);*/
        return DTOSetServices.setCompanyDTO(companyFactory.getCompanyDTO(), rs);
      }
      return null;
    } catch (SQLException e) {
      throw new FatalException(e);
    }
  }

}
