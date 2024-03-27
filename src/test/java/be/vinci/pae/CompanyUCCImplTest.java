package be.vinci.pae;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import be.vinci.pae.domain.CompanyFactory;
import be.vinci.pae.domain.dto.CompanyDTO;
import be.vinci.pae.domain.ucc.CompanyUCC;
import be.vinci.pae.services.dal.DalServices;
import be.vinci.pae.services.dao.CompanyDAO;
import be.vinci.pae.utils.exceptions.FatalException;
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
  private static DalServices dalServicesMock;
  private CompanyUCC companyUCC;
  private CompanyDTO companyDTO;

  @BeforeAll
  static void init() {
    serviceLocator = ServiceLocatorUtilities.bind(new BinderTest());
    companyDAOMock = serviceLocator.getService(CompanyDAO.class);
    dalServicesMock = serviceLocator.getService(DalServices.class);
  }

  @BeforeEach
  void setup() {
    companyUCC = serviceLocator.getService(CompanyUCC.class);
    CompanyFactory companyFactory = serviceLocator.getService(CompanyFactory.class);
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

  @Test
  @DisplayName("Test find one by id crash transaction")
  public void testFindOneByIdCrashTransaction() {
    Mockito.doThrow(new FatalException(new RuntimeException())).when(dalServicesMock)
        .startTransaction();
    assertThrows(FatalException.class, () -> companyUCC.findOneById(1));
  }

}
