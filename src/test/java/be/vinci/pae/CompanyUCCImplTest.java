package be.vinci.pae;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import be.vinci.pae.domain.CompanyFactory;
import be.vinci.pae.domain.dto.CompanyDTO;
import be.vinci.pae.domain.ucc.CompanyUCC;
import be.vinci.pae.services.dal.DalServicesConnection;
import be.vinci.pae.services.dao.CompanyDAO;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.utilities.ServiceLocatorUtilities;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

/**
 * CompanyUCC test class.
 */
public class CompanyUCCImplTest {

  private static ServiceLocator serviceLocator;
  private static CompanyDAO companyDAOMock;
  private static DalServicesConnection dalServicesMock;
  private CompanyUCC companyUCC;
  private CompanyFactory companyFactory;
  private CompanyDTO companyDTO;

  @BeforeAll
  static void init() {
    serviceLocator = ServiceLocatorUtilities.bind(new BinderTest());
    companyDAOMock = serviceLocator.getService(CompanyDAO.class);
    dalServicesMock = serviceLocator.getService(DalServicesConnection.class);
  }

  @BeforeEach
  void setup() {
    companyUCC = serviceLocator.getService(CompanyUCC.class);
    companyFactory = serviceLocator.getService(CompanyFactory.class);
    companyDTO = companyFactory.getCompanyDTO();
    Mockito.doNothing().when(dalServicesMock).startTransaction();
    Mockito.doNothing().when(dalServicesMock).commitTransaction();
    Mockito.doNothing().when(dalServicesMock).rollbackTransaction();
  }

  @AfterEach
  void reset() {
    Mockito.reset(companyDAOMock);
  }

  @Test
  @DisplayName("Test find one by id")
  public void testFindOneById() {
    Mockito.when(companyDAOMock.getOneCompanyById(1)).thenReturn(companyDTO);
    assertNotNull(companyUCC.findOneById(1));
  }

}
