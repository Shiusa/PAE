package be.vinci.pae;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import be.vinci.pae.domain.UserFactory;
import be.vinci.pae.domain.dto.UserDTO;
import be.vinci.pae.domain.ucc.UserUCC;
import be.vinci.pae.services.dal.DalServices;
import be.vinci.pae.services.dao.UserDAO;
import be.vinci.pae.utils.exceptions.DuplicateException;
import java.sql.Date;
import java.time.LocalDate;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.utilities.ServiceLocatorUtilities;
import org.junit.jupiter.api.AfterEach;
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

  private static final String email = "eleonore.martin@vinci.be";
  private static final String lastname = "Martin";
  private static final String firstname = "Eléonore";
  private static final String phoneNumber = "+32485123456";
  private static final String password = "123";
  private static final String hashPassword = "$2a$10$HG7./iX"
      + "Yemq7gF/v9Hc98eXJFGo3KajGwPLoaiU0r9TlaxlIFxsAu";
  private static final String role = "teacher";
  private static final Date registrationDate = Date.valueOf(LocalDate.now());
  private static final String schoolYear = "2023-2024";
  private static UserDAO userDAOMock;
  private static DalServices dalServicesMock;
  private static ServiceLocator serviceLocator;
  private UserUCC userUCC;
  private UserFactory userFactory;
  private UserDTO userDTO;

  @BeforeAll
  static void init() {
    serviceLocator = ServiceLocatorUtilities.bind(new BinderTest());
    userDAOMock = Mockito.mock(UserDAO.class);
    dalServicesMock = Mockito.mock(DalServices.class);
    userDAOMock = serviceLocator.getService(UserDAO.class);
    dalServicesMock = serviceLocator.getService(DalServices.class);
  }

  @BeforeEach
  void setup() {
    this.userUCC = serviceLocator.getService(UserUCC.class);
    this.userFactory = serviceLocator.getService(UserFactory.class);
    this.userDTO = userFactory.getUserDTO();
    Mockito.doNothing().when(dalServicesMock).startTransaction();
    Mockito.doNothing().when(dalServicesMock).commitTransaction();
    Mockito.doNothing().when(dalServicesMock).rollbackTransaction();
  }

  @AfterEach
  void reset() {
    Mockito.reset(userDAOMock);
    Mockito.reset(dalServicesMock);
  }

  @Test
  @DisplayName("Register new user should work")
  public void testRegisterNewUser() {

    UserDTO newUserDTO = userFactory.getUserDTO();

    newUserDTO.setEmail(email);
    newUserDTO.setLastname(lastname);
    newUserDTO.setFirstname(firstname);
    newUserDTO.setPhoneNumber(phoneNumber);
    newUserDTO.setPassword(password);
    newUserDTO.setRole(role);
    newUserDTO.setRegistrationDate(registrationDate);
    newUserDTO.setSchoolYear(schoolYear);
    newUserDTO.setPassword(hashPassword);

    Mockito.when(userDAOMock.getOneUserByEmail(newUserDTO.getEmail()))
        .thenReturn(null)
        .thenReturn(newUserDTO);
    Mockito.when(userDAOMock.addOneUser(newUserDTO)).thenReturn(newUserDTO);

    UserDTO returnedUser;
    returnedUser = userUCC.register(newUserDTO);

    assertNotNull(returnedUser);

  }

  @Test
  @DisplayName("Register existing user should not work")
  public void testRegisterExistingUser() {

    userDTO.setEmail(email);
    userDTO.setLastname(lastname);
    userDTO.setFirstname(firstname);
    userDTO.setPhoneNumber(phoneNumber);
    userDTO.setRole(role);
    userDTO.setRegistrationDate(registrationDate);
    userDTO.setSchoolYear(schoolYear);
    userDTO.setPassword(hashPassword);

    UserDTO newUserDTO = userFactory.getUserDTO();

    newUserDTO.setEmail(email);
    newUserDTO.setLastname(lastname);
    newUserDTO.setFirstname(firstname);
    newUserDTO.setPhoneNumber(phoneNumber);
    newUserDTO.setRole(role);
    newUserDTO.setRegistrationDate(registrationDate);
    newUserDTO.setSchoolYear(schoolYear);
    newUserDTO.setPassword(hashPassword);

    Mockito.when(userDAOMock.getOneUserByEmail(newUserDTO.getEmail()))
        .thenReturn(userDTO);

    assertThrows(DuplicateException.class, () -> userUCC.register(newUserDTO));

  }

}


