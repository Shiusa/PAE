package be.vinci.pae;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import be.vinci.pae.domain.ContactFactory;
import be.vinci.pae.domain.InternshipFactory;
import be.vinci.pae.domain.dto.ContactDTO;
import be.vinci.pae.domain.dto.InternshipDTO;
import be.vinci.pae.domain.ucc.InternshipUCC;
import be.vinci.pae.services.dal.DalServices;
import be.vinci.pae.services.dao.ContactDAO;
import be.vinci.pae.services.dao.InternshipDAO;
import be.vinci.pae.utils.exceptions.FatalException;
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
 * InternshipUCC test class.
 */
public class InternshipUCCImplTest {

  private static ServiceLocator serviceLocator;
  private static InternshipDAO internshipDAOMock;
  private static ContactDAO contactDAOMock;
  private static DalServices dalServicesMock;
  private InternshipUCC internshipUCC;
  private InternshipDTO internshipDTO;
  private ContactDTO contactDTO;

  @BeforeAll
  static void init() {
    serviceLocator = ServiceLocatorUtilities.bind(new BinderTest());
    internshipDAOMock = serviceLocator.getService(InternshipDAO.class);
    contactDAOMock = serviceLocator.getService(ContactDAO.class);
    dalServicesMock = serviceLocator.getService(DalServices.class);
  }

  @BeforeEach
  void setup() {
    internshipUCC = serviceLocator.getService(InternshipUCC.class);
    InternshipFactory internshipFactory = serviceLocator.getService(InternshipFactory.class);
    ContactFactory contactFactory = serviceLocator.getService(ContactFactory.class);
    internshipDTO = internshipFactory.getInternshipDTO();
    contactDTO = contactFactory.getContactDTO();
    Mockito.doNothing().when(dalServicesMock).startTransaction();
    Mockito.doNothing().when(dalServicesMock).commitTransaction();
    Mockito.doNothing().when(dalServicesMock).rollbackTransaction();
  }

  @AfterEach
  void reset() {
    Mockito.reset(internshipDAOMock);
    Mockito.reset(contactDAOMock);
  }

  @Test
  @DisplayName("Test getOneByStudent student has no internship")
  public void testGetOneByStudentStudentNoInternship() {
    Mockito.when(internshipDAOMock.getOneInternshipByIdUser(1)).thenReturn(null);
    assertNull(internshipUCC.getOneByStudent(1));
  }

  @Test
  @DisplayName("Test getOneByStudent correct")
  public void testGetOneByStudentCorrect() {
    Mockito.when(internshipDAOMock.getOneInternshipByIdUser(1)).thenReturn(internshipDTO);
    assertNotNull(internshipUCC.getOneByStudent(1));
  }

  @Test
  @DisplayName("Test getOneById wrong id")
  public void testGetOneByIdWrongId() {
    Mockito.when(internshipDAOMock.getOneInternshipById(1)).thenReturn(null);
    assertThrows(ResourceNotFoundException.class,
        () -> internshipUCC.getOneById(1, 1));
  }

  @Test
  @DisplayName("Test getOneById wrong user")
  public void testGetOneByIdWrongUser() {
    contactDTO.setStudent(1);
    internshipDTO.setContact(1);
    Mockito.when(contactDAOMock.findContactById(1)).thenReturn(contactDTO);
    Mockito.when(internshipDAOMock.getOneInternshipById(1)).thenReturn(internshipDTO);
    assertThrows(NotAllowedException.class, () -> internshipUCC.getOneById(1, 3));
  }

  @Test
  @DisplayName("Test getOneById correct")
  public void testGetOneByIdCorrect() {
    contactDTO.setStudent(1);
    internshipDTO.setContact(1);
    Mockito.when(contactDAOMock.findContactById(1)).thenReturn(contactDTO);
    Mockito.when(internshipDAOMock.getOneInternshipById(1)).thenReturn(internshipDTO);
    assertNotNull(internshipUCC.getOneById(1, 1));
  }

  @Test
  @DisplayName("Test crash transaction")
  public void testCrashTransaction() {
    Mockito.doThrow(new FatalException(new RuntimeException()))
        .when(dalServicesMock).startTransaction();
    assertAll(
        () -> assertThrows(FatalException.class, () -> {
          internshipUCC.getOneByStudent(1);
        }),
        () -> assertThrows(FatalException.class, () -> {
          internshipUCC.getOneById(1, 1);
        })
    );
  }
}

