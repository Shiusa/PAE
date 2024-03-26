package be.vinci.pae;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import be.vinci.pae.domain.UserFactory;
import be.vinci.pae.domain.dto.UserDTO;
import be.vinci.pae.domain.ucc.UserUCC;
import be.vinci.pae.services.dal.DalServicesConnection;
import be.vinci.pae.services.dao.UserDAO;
import be.vinci.pae.utils.exceptions.InvalidRequestException;
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
 * UserUCC test class.
 */
public class UserUCCImplTest {

  private static final String email = "eleonore.martin@vinci.be";
  private final static String password = "123";
  private final static String hashPassword = "$2a$10$HG7./iXYemq7gF/v9Hc98eXJFGo3KajGwPLoaiU0r9TlaxlIFxsAu";
  private static ServiceLocator serviceLocator;
  private static UserDAO userDAOMock;
  private static DalServicesConnection dalServicesMock;
  private UserUCC userUCC;
  private UserFactory userFactory;
  private UserDTO userDTO;

  @BeforeAll
  static void init() {
    serviceLocator = ServiceLocatorUtilities.bind(new BinderTest());
    userDAOMock = serviceLocator.getService(UserDAO.class);
    dalServicesMock = serviceLocator.getService(DalServicesConnection.class);
  }

  @BeforeEach
  void setup() {
    userUCC = serviceLocator.getService(UserUCC.class);
    userFactory = serviceLocator.getService(UserFactory.class);
    userDTO = userFactory.getUserDTO();
  }

  @AfterEach
  void reset() {
    Mockito.reset(userDAOMock);
  }

  @Test
  @DisplayName("Test login with unknown email")
  public void testLoginUnknownEmail() {
    Mockito.when(userDAOMock.getOneUserByEmail(email)).thenReturn(null);
    assertThrows(ResourceNotFoundException.class, () -> userUCC.login(email, password));
  }

  @Test
  @DisplayName("Test login with good email wrong password")
  public void testLoginGoodEmailWrongPassword() {
    userDTO.setEmail(email);
    userDTO.setPassword(hashPassword);
    Mockito.when(userDAOMock.getOneUserByEmail(email)).thenReturn(userDTO);
    assertThrows(InvalidRequestException.class, () -> userUCC.login(email, "boom"));
  }

  @Test
  @DisplayName("Test login with good email and good password")
  public void testLoginGoodEmailGoodPassword() {
    userDTO.setId(42);
    userDTO.setEmail(email);
    userDTO.setPassword(hashPassword);
    Mockito.when(userDAOMock.getOneUserByEmail(email)).thenReturn(userDTO);
    assertEquals(42, userUCC.login(email, password).getId());
  }

}


