package be.vinci.pae;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import be.vinci.pae.domain.CompanyFactory;
import be.vinci.pae.domain.ContactFactory;
import be.vinci.pae.domain.UserFactory;
import be.vinci.pae.domain.dto.CompanyDTO;
import be.vinci.pae.domain.dto.ContactDTO;
import be.vinci.pae.domain.dto.UserDTO;
import be.vinci.pae.domain.ucc.ContactUCC;
import be.vinci.pae.services.dal.DalServices;
import be.vinci.pae.services.dao.CompanyDAO;
import be.vinci.pae.services.dao.ContactDAO;
import be.vinci.pae.services.dao.UserDAO;
import be.vinci.pae.utils.exceptions.DuplicateException;
import be.vinci.pae.utils.exceptions.FatalException;
import be.vinci.pae.utils.exceptions.InvalidRequestException;
import be.vinci.pae.utils.exceptions.NotAllowedException;
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
 * ContactUCC test class.
 */
public class ContactUCCImplTest {

  private static ServiceLocator serviceLocator;
  private static ContactDAO contactDAOMock;
  private static UserDAO userDAOMock;
  private static CompanyDAO companyDAOMock;
  private static DalServices dalServicesMock;
  private ContactUCC contactUCC;
  private ContactDTO contactDTO;
  private UserDTO userDTO;
  private CompanyDTO companyDTO;

  @BeforeAll
  static void init() {
    serviceLocator = ServiceLocatorUtilities.bind(new BinderTest());
    contactDAOMock = serviceLocator.getService(ContactDAO.class);
    userDAOMock = serviceLocator.getService(UserDAO.class);
    companyDAOMock = serviceLocator.getService(CompanyDAO.class);
    dalServicesMock = serviceLocator.getService(DalServices.class);
  }

  @BeforeEach
  void setup() {
    contactUCC = serviceLocator.getService(ContactUCC.class);
    ContactFactory contactFactory = serviceLocator.getService(ContactFactory.class);
    UserFactory userFactory = serviceLocator.getService(UserFactory.class);
    CompanyFactory companyFactory = serviceLocator.getService(CompanyFactory.class);
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
  @DisplayName("Test start crash transaction")
  public void testStartCrashTransaction() {
    Mockito.doThrow(new FatalException(new RuntimeException()))
        .when(dalServicesMock).startTransaction();
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
    userDTO.setId(5);
    contactDTO.setStudent(userDTO);
    contactDTO.setState("initié");
    Mockito.when(contactDAOMock.findContactById(1)).thenReturn(contactDTO);
    assertThrows(NotAllowedException.class, () -> contactUCC.unsupervise(1, 1));
  }

  @Test
  @DisplayName("Test unsupervise contact with state different than started or admitted")
  public void testUnsuperviseContactWrongState() {
    userDTO.setId(1);
    contactDTO.setStudent(userDTO);
    contactDTO.setState("refusé");
    Mockito.when(contactDAOMock.findContactById(1)).thenReturn(contactDTO);
    assertThrows(NotAllowedException.class, () -> contactUCC.unsupervise(1, 1));
  }

  @Test
  @DisplayName("Test unsupervise contact correctly started")
  public void testUnsuperviseContactCorrectlyStarted() {
    userDTO.setId(1);
    contactDTO.setStudent(userDTO);
    contactDTO.setState("initié");
    contactDTO.setVersion(1);
    Mockito.when(contactDAOMock.findContactById(1)).thenReturn(contactDTO);
    Mockito.when(contactDAOMock.unsupervise(1, 1)).thenReturn(contactDTO);
    assertNotNull(contactUCC.unsupervise(1, 1));
  }

  @Test
  @DisplayName("Test unsupervise contact correctly admitted")
  public void testUnsuperviseContactCorrectlyAdmitted() {
    userDTO.setId(1);
    contactDTO.setStudent(userDTO);
    contactDTO.setState("pris");
    contactDTO.setVersion(2);
    Mockito.when(contactDAOMock.findContactById(1)).thenReturn(contactDTO);
    Mockito.when(contactDAOMock.unsupervise(1, 2)).thenReturn(contactDTO);
    assertNotNull(contactUCC.unsupervise(1, 1));
  }

  @Test
  @DisplayName("Test unsupervise contact crash transaction")
  public void testCrashTransaction() {
    Mockito.doThrow(new FatalException(new RuntimeException()))
        .when(dalServicesMock).startTransaction();
    assertAll(
        () -> assertThrows(FatalException.class, () -> {
          contactUCC.start(1, 1);
        }),
        () -> assertThrows(FatalException.class, () -> {
          contactUCC.unsupervise(1, 1);
        }),
        () -> assertThrows(FatalException.class, () -> {
          contactUCC.admit(1, "sur place", 1);
        }),
        () -> assertThrows(FatalException.class, () -> {
          contactUCC.turnDown(1, "Student has not answered fast enough", 1);
        }),
        () -> assertThrows(FatalException.class, () -> {
          contactUCC.getOneById(1);
        }),
        () -> assertThrows(FatalException.class, () -> {
          contactUCC.getAllContactsByStudent(1);
        }),
        () -> assertThrows(FatalException.class, () -> {
          contactUCC.getAllContactsByCompany(1);
        })
    );
  }

  @Test
  @DisplayName("Test admit contact is null")
  public void testAdmitContactNotFound() {
    Mockito.when(contactDAOMock.findContactById(1)).thenReturn(null);
    assertThrows(ResourceNotFoundException.class, () -> contactUCC.admit(1, "sur place", 1));
  }

  @Test
  @DisplayName("Test admit contact with wrong student")
  public void testAdmitContactWrongStudent() {
    userDTO.setId(2);
    contactDTO.setStudent(userDTO);
    contactDTO.setState("initié");
    Mockito.when(contactDAOMock.findContactById(1)).thenReturn(contactDTO);
    assertThrows(NotAllowedException.class, () -> contactUCC.admit(1, "à distance", 1));
  }

  @Test
  @DisplayName("Test admit contact with state different than started")
  public void testAdmitContactWrongState() {
    userDTO.setId(1);
    contactDTO.setStudent(userDTO);
    contactDTO.setState("pris");
    Mockito.when(contactDAOMock.findContactById(1)).thenReturn(contactDTO);
    assertThrows(InvalidRequestException.class, () -> contactUCC.admit(1, "sur place", 1));
  }

  @Test
  @DisplayName("Test admit contact with type of meeting different than on site or remote")
  public void testAdmitContactWrongMeeting() {
    userDTO.setId(1);
    contactDTO.setStudent(userDTO);
    contactDTO.setState("initié");
    Mockito.when(contactDAOMock.findContactById(1)).thenReturn(contactDTO);
    assertThrows(InvalidRequestException.class, () -> contactUCC.admit(1, "test", 1));
  }

  @Test
  @DisplayName("Test admit contact with type of meeting is null")
  public void testAdmitContactMeetingIsNull() {
    userDTO.setId(1);
    contactDTO.setStudent(userDTO);
    contactDTO.setState("initié");
    Mockito.when(contactDAOMock.findContactById(1)).thenReturn(contactDTO);
    assertThrows(InvalidRequestException.class, () -> contactUCC.admit(1, "", 1));
  }

  @Test
  @DisplayName("Test admit contact correctly started")
  public void testAdmitContactCorrectlyStarted() {
    userDTO.setId(1);
    contactDTO.setStudent(userDTO);
    contactDTO.setState("initié");
    contactDTO.setVersion(1);
    Mockito.when(contactDAOMock.findContactById(1)).thenReturn(contactDTO);
    Mockito.when(contactDAOMock.admitContact(1, "Dans l entreprise", 1)).thenReturn(contactDTO);
    assertNotNull(contactUCC.admit(1, "Dans l entreprise", 1));
  }

  @Test
  @DisplayName("Test turn down contact is null")
  public void testTurnDownContactNotFound() {
    Mockito.when(contactDAOMock.findContactById(1)).thenReturn(null);
    assertThrows(ResourceNotFoundException.class,
        () -> contactUCC.turnDown(1, "Student has not answered fast enough", 1));
  }

  @Test
  @DisplayName("Test turn down contact with wrong student")
  public void testTurnDownContactWrongStudent() {
    userDTO.setId(5);
    contactDTO.setStudent(userDTO);
    contactDTO.setState("pris");
    Mockito.when(contactDAOMock.findContactById(1)).thenReturn(contactDTO);
    assertThrows(NotAllowedException.class,
        () -> contactUCC.turnDown(1, "Student has not answered fast enough", 1));
  }

  @Test
  @DisplayName("Test turn down contact with state different than admitted")
  public void testTurnDownContactWrongState() {
    userDTO.setId(1);
    contactDTO.setStudent(userDTO);
    contactDTO.setState("initié");
    Mockito.when(contactDAOMock.findContactById(1)).thenReturn(contactDTO);
    assertThrows(NotAllowedException.class,
        () -> contactUCC.turnDown(1, "Student has not answered fast enough", 1));
  }


  @Test
  @DisplayName("Test turn down contact correctly admitted")
  public void testTurnDownContactCorrectlyAdmitted() {
    userDTO.setId(1);
    contactDTO.setStudent(userDTO);
    contactDTO.setState("pris");
    contactDTO.setVersion(2);
    Mockito.when(contactDAOMock.findContactById(1)).thenReturn(contactDTO);
    Mockito.when(contactDAOMock.turnDown(1, "Student has not answered fast enough", 2))
        .thenReturn(contactDTO);
    assertNotNull(contactUCC.turnDown(1, "Student has not answered fast enough", 1));
  }

  @Test
  @DisplayName("Test get all contact by student")
  public void testGetAllContactByStudent() {
    Mockito.when(contactDAOMock.getAllContactsByStudent(1)).thenReturn(List.of(contactDTO));
    assertNotNull(contactUCC.getAllContactsByStudent(1));
  }

  @Test
  @DisplayName("Test get one by id with contact not found")
  public void testGetOneByIdWithContactNotFound() {
    Mockito.when(contactDAOMock.findContactById(1)).thenReturn(null);
    assertThrows(ResourceNotFoundException.class, () -> contactUCC.getOneById(1));
  }

  @Test
  @DisplayName("Test get one by id correct")
  public void testGetOneByIdCorrect() {
    Mockito.when(contactDAOMock.findContactById(1)).thenReturn(contactDTO);
    assertNotNull(contactUCC.getOneById(1));
  }

  @Test
  @DisplayName("Test get all contact by company")
  public void testGetAllContactByCompany() {
    Mockito.when(contactDAOMock.getAllContactsByCompany(1)).thenReturn(List.of(contactDTO));
    assertNotNull(contactUCC.getAllContactsByCompany(1));
  }

  @Test
  @DisplayName("Test get all contact by company returning null")
  public void testGetAllContactByCompanyReturningNull() {
    Mockito.when(contactDAOMock.getAllContactsByCompany(1)).thenReturn(null);
    assertThrows(ResourceNotFoundException.class,
        () -> contactUCC.getAllContactsByCompany(1));
  }

  @Test
  @DisplayName("Test get all contact by student returning null")
  public void testGetAllContactByStudentReturningNull() {
    Mockito.when(contactDAOMock.getAllContactsByStudent(1)).thenReturn(null);
    assertThrows(ResourceNotFoundException.class,
        () -> contactUCC.getAllContactsByStudent(1));
  }

}
