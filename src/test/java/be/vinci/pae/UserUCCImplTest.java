package be.vinci.pae;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import be.vinci.pae.domain.User;
import be.vinci.pae.domain.UserFactory;
import be.vinci.pae.domain.UserFactoryImpl;
import be.vinci.pae.domain.UserImpl;
import be.vinci.pae.domain.dto.UserDTO;
import be.vinci.pae.domain.ucc.UserUCC;
import be.vinci.pae.domain.ucc.UserUCCImpl;
import be.vinci.pae.services.dao.UserDAO;
import be.vinci.pae.services.utils.DalService;
import be.vinci.pae.utils.ApplicationBinder;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.utilities.ServiceLocatorUtilities;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

/**
 * Demo test class. Please read the document on Moodle to know
 * how test classes are handled by Jenkins.
 */
public class UserUCCImplTest {

  private static UserUCC userUCC;
  private static UserDAO userDAOMock;
  private static User userMock;
  private static DalService dalServiceMock;


  @BeforeAll
  static void initAll() {
    ServiceLocator locator = ServiceLocatorUtilities.bind(new BinderTest());
    userUCC = locator.getService(UserUCC.class);
    userMock = Mockito.mock(UserImpl.class);
    dalServiceMock = locator.getService(DalService.class);
  }

  @BeforeEach
  void setUp() {
  }



  @Test
  @DisplayName("Test 1 : test login with correct email and password")
  public void testLoginCorrectEmailAndPassword() {
    // Email = test@vinci.be
    // Password = test
    Mockito.when(userMock.checkMotDePasse("test")).thenReturn(true);

    UserDTO actualUser = userUCC.login("test@vinci.be", "test");

    Mockito.verify(userDAOMock).getOneUserByEmail("test@vinci.be");
    Mockito.verify(userMock).checkMotDePasse("test");

    assertEquals(userMock, actualUser);
  }
}


