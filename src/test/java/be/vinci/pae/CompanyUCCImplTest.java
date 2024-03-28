package be.vinci.pae;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import be.vinci.pae.domain.CompanyFactory;
import be.vinci.pae.domain.UserFactory;
import be.vinci.pae.domain.dto.CompanyDTO;
import be.vinci.pae.domain.dto.UserDTO;
import be.vinci.pae.domain.ucc.CompanyUCC;
import be.vinci.pae.services.dal.DalServices;
import be.vinci.pae.services.dao.CompanyDAO;
import be.vinci.pae.services.dao.UserDAO;
import be.vinci.pae.utils.exceptions.FatalException;
import be.vinci.pae.utils.exceptions.ResourceNotFoundException;
import java.util.List;
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
  private static UserDAO userDAOMock;
  private static DalServices dalServicesMock;
  private CompanyUCC companyUCC;
  private CompanyDTO companyDTO;
  private UserDTO userDTO;

  @BeforeAll
  static void init() {
    serviceLocator = ServiceLocatorUtilities.bind(new BinderTest());
    userDAOMock = serviceLocator.getService(UserDAO.class);
    companyDAOMock = serviceLocator.getService(CompanyDAO.class);
    dalServicesMock = serviceLocator.getService(DalServices.class);
  }

  @BeforeEach
  void setup() {
    companyUCC = serviceLocator.getService(CompanyUCC.class);
    CompanyFactory companyFactory = serviceLocator.getService(CompanyFactory.class);
    UserFactory userFactory = serviceLocator.getService(UserFactory.class);
    companyDTO = companyFactory.getCompanyDTO();
    userDTO = userFactory.getUserDTO();
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

  @Test
  @DisplayName("Test get all companies should return not null")
  public void testGetAllCompanies() {
    Mockito.when(companyDAOMock.getAllCompanies()).thenReturn(List.of(companyDTO));
    List<CompanyDTO> companyDTOList = companyUCC.getAllCompanies();
    assertNotNull(companyDTOList);
  }

  @Test
  @DisplayName("Test get all companies for existing user should return not null")
  public void testGetAllCompaniesByExistingUser() {
    userDTO.setId(1);
    Mockito.when(userDAOMock.getOneUserById(1)).thenReturn(userDTO);
    Mockito.when(companyDAOMock.getAllCompaniesByUserId(1)).thenReturn(List.of(companyDTO));
    List<CompanyDTO> companyDTOList = companyUCC.getAllCompaniesByUser(1);
    assertNotNull(companyDTOList);
  }

  @Test
  @DisplayName("Test get all companies for non existing "
      + "user should throw ResourceNotFoundException")
  public void testGetAllCompaniesByNonExistingUser() {
    userDTO.setId(1);
    Mockito.when(userDAOMock.getOneUserById(1)).thenReturn(null);
    Mockito.when(companyDAOMock.getAllCompaniesByUserId(1)).thenReturn(List.of(companyDTO));
    assertThrows(ResourceNotFoundException.class, () -> companyUCC.getAllCompaniesByUser(1));
  }

  @Test
  @DisplayName("Test get all companies crash transaction")
  public void testGetAllCompaniesCrashTransaction() {
    Mockito.doThrow(new FatalException(new RuntimeException()))
        .when(dalServicesMock).startTransaction();
    assertAll(
        () -> assertThrows(FatalException.class, () -> companyUCC.getAllCompanies()),
        () -> assertThrows(FatalException.class, () -> companyUCC.getAllCompaniesByUser(1))
    );
  }

}
