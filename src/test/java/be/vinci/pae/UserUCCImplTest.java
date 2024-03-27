package be.vinci.pae;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import be.vinci.pae.domain.UserFactory;
import be.vinci.pae.domain.dto.UserDTO;
import be.vinci.pae.domain.ucc.UserUCC;
import be.vinci.pae.services.dal.DalServices;
import be.vinci.pae.services.dao.UserDAO;
import be.vinci.pae.utils.exceptions.FatalException;
import be.vinci.pae.utils.exceptions.ResourceNotFoundException;
import be.vinci.pae.utils.exceptions.UnauthorizedAccessException;
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
 * UserUCC test class.
 */
public class UserUCCImplTest {

  private static final String email = "eleonore.martin@vinci.be";
  private static final String password = "123";
  private static final String hashPassword
      = "$2a$10$HG7./iXYemq7gF/v9Hc98eXJFGo3KajGwPLoaiU0r9TlaxlIFxsAu";
  private static ServiceLocator serviceLocator;
  private static UserDAO userDAOMock;
  private static DalServices dalServicesMock;
  private UserUCC userUCC;
  private UserDTO userDTO;

  @BeforeAll
  static void init() {
    serviceLocator = ServiceLocatorUtilities.bind(new BinderTest());
    userDAOMock = serviceLocator.getService(UserDAO.class);
    dalServicesMock = serviceLocator.getService(DalServices.class);
  }

  @BeforeEach
  void setup() {
    userUCC = serviceLocator.getService(UserUCC.class);
    UserFactory userFactory = serviceLocator.getService(UserFactory.class);
    userDTO = userFactory.getUserDTO();
    Mockito.doNothing().when(dalServicesMock).startTransaction();
    Mockito.doNothing().when(dalServicesMock).commitTransaction();
    Mockito.doNothing().when(dalServicesMock).rollbackTransaction();
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
    assertThrows(UnauthorizedAccessException.class, () -> userUCC.login(email, "boom"));
  }

  @Test
  @DisplayName("Test login with good email and good password")
  public void testLoginGoodEmailGoodPassword() {
    userDTO.setId(42);
    userDTO.setEmail(email);
    userDTO.setPassword(hashPassword);
    Mockito.when(userDAOMock.getOneUserByEmail(email)).thenReturn(userDTO);
    assertNotNull(userUCC.login(email, password));
  }

  @Test
  @DisplayName("Test get all users")
  public void testGetAllUsers() {
    userDTO.setId(56);
    Mockito.when(userDAOMock.getAllUsers()).thenReturn(List.of(userDTO));
    assertEquals(56, userUCC.getAllUsers().get(0).getId());
  }

  @Test
  @DisplayName("Test get one by wrong id")
  public void testGetOneByUnknownId() {
    Mockito.when(userDAOMock.getOneUserById(49)).thenReturn(null);
    assertThrows(ResourceNotFoundException.class, () -> userUCC.getOneById(49));
  }

  @Test
  @DisplayName("Test get one by id")
  public void testGetOneByKnownId() {
    userDTO.setEmail(email);
    Mockito.when(userDAOMock.getOneUserById(1)).thenReturn(userDTO);
    assertNotNull(userUCC.getOneById(1));
  }

  @Test
  @DisplayName("Test unsupervise contact crash transaction")
  public void testCrashTransaction() {
    Mockito.doThrow(new FatalException(new RuntimeException()))
        .when(dalServicesMock).startTransaction();
    assertAll(
        () -> assertThrows(FatalException.class, () -> {
          userUCC.login(email, password);
        }),
        () -> assertThrows(FatalException.class, () -> {
          userUCC.getAllUsers();
        }),
        () -> assertThrows(FatalException.class, () -> {
          userUCC.getOneById(1);
        })
    );
  }

}


