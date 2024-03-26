package be.vinci.pae;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import be.vinci.pae.domain.CompanyFactory;
import be.vinci.pae.domain.ContactFactory;
import be.vinci.pae.domain.UserFactory;
import be.vinci.pae.domain.dto.CompanyDTO;
import be.vinci.pae.domain.dto.ContactDTO;
import be.vinci.pae.domain.dto.UserDTO;
import be.vinci.pae.domain.ucc.ContactUCC;
import be.vinci.pae.services.dal.DalServicesConnection;
import be.vinci.pae.services.dao.CompanyDAO;
import be.vinci.pae.services.dao.ContactDAO;
import be.vinci.pae.services.dao.UserDAO;
import be.vinci.pae.utils.exceptions.DuplicateException;
import be.vinci.pae.utils.exceptions.InvalidRequestException;
import be.vinci.pae.utils.exceptions.NotAllowedException;
import be.vinci.pae.utils.exceptions.ResourceNotFoundException;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.utilities.ServiceLocatorUtilities;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

/**
 * ContactUCC test class.
 */
public class ContactUCCImplTest {

  private static ServiceLocator serviceLocator;
  private static ContactDAO contactDAOMock;
  private static UserDAO userDAOMock;
  private static CompanyDAO companyDAOMock;
  private static DalServicesConnection dalServicesMock;
  private ContactUCC contactUCC;
  private ContactFactory contactFactory;
  private UserFactory userFactory;
  private CompanyFactory companyFactory;
  private ContactDTO contactDTO;
  private UserDTO userDTO;
  private CompanyDTO companyDTO;

  @BeforeAll
  static void init() {
    serviceLocator = ServiceLocatorUtilities.bind(new BinderTest());
    contactDAOMock = serviceLocator.getService(ContactDAO.class);
    userDAOMock = serviceLocator.getService(UserDAO.class);
    companyDAOMock = serviceLocator.getService(CompanyDAO.class);
    dalServicesMock = serviceLocator.getService(DalServicesConnection.class);
  }

  @BeforeEach
  void setup() {
    contactUCC = serviceLocator.getService(ContactUCC.class);
    contactFactory = serviceLocator.getService(ContactFactory.class);
    userFactory = serviceLocator.getService(UserFactory.class);
    companyFactory = serviceLocator.getService(CompanyFactory.class);
    contactDTO = contactFactory.getContactDTO();
    userDTO = userFactory.getUserDTO();
    companyDTO = companyFactory.getCompanyDTO();
    Mockito.doNothing().when(dalServicesMock).startTransaction();
    Mockito.doNothing().when(dalServicesMock).commitTransaction();
    Mockito.doNothing().when(dalServicesMock).rollbackTransaction();
  }

  @AfterEach
  void reset() {
    Mockito.reset(contactDAOMock);
    Mockito.reset(userDAOMock);
    Mockito.reset(companyDAOMock);
  }

  @Test
  @DisplayName("Test start with wrong student id")
  public void testStartWrongStudentId() {
    Mockito.when(userDAOMock.getOneUserById(1)).thenReturn(null);
    Mockito.when(companyDAOMock.getOneCompanyById(1)).thenReturn(companyDTO);
    assertThrows(ResourceNotFoundException.class, () -> contactUCC.start(1, 1));
  }

  @Test
  @DisplayName("Test start with wrong company id")
  public void testStartWrongCompanyId() {
    Mockito.when(userDAOMock.getOneUserById(1)).thenReturn(userDTO);
    Mockito.when(companyDAOMock.getOneCompanyById(1)).thenReturn(null);
    assertThrows(ResourceNotFoundException.class, () -> contactUCC.start(1, 1));
  }

  @Test
  @DisplayName("Test start with blacklisted company")
  public void testStartBlacklistedCompany() {
    companyDTO.setIsBlacklisted(true);
    Mockito.when(userDAOMock.getOneUserById(1)).thenReturn(userDTO);
    Mockito.when(companyDAOMock.getOneCompanyById(1)).thenReturn(companyDTO);
    assertThrows(InvalidRequestException.class, () -> contactUCC.start(1, 1));
  }

  @Test
  @DisplayName("Test start duplicated")
  public void testStartDuplicated() {
    userDTO.setSchoolYear("2023-2024");
    Mockito.when(userDAOMock.getOneUserById(1)).thenReturn(userDTO);
    Mockito.when(companyDAOMock.getOneCompanyById(1)).thenReturn(companyDTO);
    Mockito.when(contactDAOMock.findContactByCompanyStudentSchoolYear(1, 1, "2023-2024"))
        .thenReturn(contactDTO);
    assertThrows(DuplicateException.class, () -> contactUCC.start(1, 1));
  }

  @Test
  @DisplayName("Test start good contact and company")
  public void testStartGoodContactCompany() {
    contactDTO.setId(50);
    userDTO.setSchoolYear("2023-2024");
    Mockito.when(userDAOMock.getOneUserById(1)).thenReturn(userDTO);
    Mockito.when(companyDAOMock.getOneCompanyById(1)).thenReturn(companyDTO);
    Mockito.when(contactDAOMock.findContactByCompanyStudentSchoolYear(1, 1, "2023-2024"))
        .thenReturn(null);
    Mockito.when(contactDAOMock.startContact(1, 1, "2023-2024")).thenReturn(contactDTO);
    assertNotNull(contactUCC.start(1, 1));
  }

  @Test
  @DisplayName("Test unsupervise contact is null")
  public void testUnsuperviseContactNotFound() {
    Mockito.when(contactDAOMock.findContactById(1)).thenReturn(null);
    assertThrows(ResourceNotFoundException.class, () -> contactUCC.unsupervise(1, 1));
  }

  @Test
  @DisplayName("Test unsupervise contact with wrong student")
  public void testUnsuperviseContactWrongStudent() {
    contactDTO.setStudent(5);
    contactDTO.setState("started");
    Mockito.when(contactDAOMock.findContactById(1)).thenReturn(contactDTO);
    assertThrows(NotAllowedException.class, () -> contactUCC.unsupervise(1, 1));
  }

  @Test
  @DisplayName("Test unsupervise contact correctly")
  public void testUnsuperviseContactCorrectly() {
    contactDTO.setStudent(1);
    contactDTO.setState("started");
    Mockito.when(contactDAOMock.findContactById(1)).thenReturn(contactDTO);
    Mockito.when(contactDAOMock.unsupervise(1)).thenReturn(contactDTO);
    assertNotNull(contactUCC.unsupervise(1, 1));
  }
}
