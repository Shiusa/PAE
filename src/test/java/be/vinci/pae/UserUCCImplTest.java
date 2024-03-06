package be.vinci.pae;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import be.vinci.pae.domain.User;
import be.vinci.pae.domain.dto.UserDTO;
import be.vinci.pae.domain.ucc.UserUCC;
import be.vinci.pae.services.dao.UserDAO;
import jakarta.ws.rs.WebApplicationException;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.utilities.ServiceLocatorUtilities;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

/**
 * Demo test class. Please read the document on Moodle to know how test classes are handled by
 * Jenkins.
 */
public class UserUCCImplTest {

  private static final String defaultEmail = "test@vinci.be";
  private static final String defaultPassword = "test";
  private static UserUCC userUCC;
  private static UserDAO userDAOMock;
  private static User userMock;

  @BeforeAll
  static void initAll() {
    ServiceLocator locator = ServiceLocatorUtilities.bind(new BinderTest());
    userUCC = locator.getService(UserUCC.class);
    userMock = Mockito.mock(User.class);
    userDAOMock = locator.getService(UserDAO.class);
  }

  @BeforeEach
  void setUp() {
    Mockito.reset(userMock);
    Mockito.reset(userDAOMock);
    Mockito.when(userDAOMock.getOneUserByEmail(defaultEmail)).thenReturn(userMock);
  }

  @Test
  @DisplayName("Test 1 : test login with correct email and password")
  public void testLoginCorrectEmailAndPassword() {
    Mockito.when(userMock.checkMotDePasse(defaultPassword)).thenReturn(true);

    UserDTO actualUser = userUCC.login(defaultEmail, defaultPassword);

    Mockito.verify(userDAOMock).getOneUserByEmail(defaultEmail);
    Mockito.verify(userMock).checkMotDePasse(defaultPassword);

    assertEquals(userMock, actualUser);
  }

  @Test
  @DisplayName("Test 2 : test login with good email and wrong password")
  public void testLoginCorrectEmailAndWrongPassword() {
    Mockito.when(userMock.checkMotDePasse(defaultPassword)).thenReturn(false);

    assertThrows(WebApplicationException.class,
        () -> userUCC.login(defaultEmail, defaultPassword),
        "Expected: WebApplicationException (Wrong password)");
  }

  @Test
  @DisplayName("Test 3 : test login with wrong email")
  public void testLoginWrongEmail() {
    Mockito.when(userDAOMock.getOneUserByEmail(defaultEmail))
        .thenThrow(WebApplicationException.class);

    assertThrows(WebApplicationException.class,
        () -> userUCC.login(defaultEmail, defaultPassword),
        "Expected: WebApplicationException (Email not found)");
  }
}


