package be.vinci.pae;

import be.vinci.pae.domain.ContactFactory;
import be.vinci.pae.domain.UserFactory;
import be.vinci.pae.domain.dto.ContactDTO;
import be.vinci.pae.domain.dto.UserDTO;
import be.vinci.pae.domain.ucc.ContactUCC;
import be.vinci.pae.services.dal.DalServicesConnection;
import be.vinci.pae.services.dao.ContactDAO;
import be.vinci.pae.services.dao.UserDAO;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.utilities.ServiceLocatorUtilities;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;

/**
 * ContactUCC test class.
 */
public class ContactUCCImplTest {

  private static ServiceLocator serviceLocator;
  private static ContactDAO contactDAOMock;
  private static UserDAO userDAOMock;
  private static DalServicesConnection dalServicesMock;
  private ContactUCC contactUCC;
  private ContactFactory contactFactory;
  private UserFactory userFactory;
  private ContactDTO contactDTO;
  private UserDTO userDTO;

  @BeforeAll
  static void init() {
    serviceLocator = ServiceLocatorUtilities.bind(new BinderTest());
    contactDAOMock = serviceLocator.getService(ContactDAO.class);
    userDAOMock = serviceLocator.getService(UserDAO.class);
    dalServicesMock = serviceLocator.getService(DalServicesConnection.class);
  }

  @BeforeEach
  void setup() {
    contactUCC = serviceLocator.getService(ContactUCC.class);
    contactFactory = serviceLocator.getService(ContactFactory.class);
    userFactory = serviceLocator.getService(UserFactory.class);
    contactDTO = contactFactory.getContactDTO();
    userDTO = userFactory.getUserDTO();
  }

  @AfterEach
  void reset() {
    Mockito.reset(contactDAOMock);
    Mockito.reset(userDAOMock);
  }


}
