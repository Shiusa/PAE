package be.vinci.pae;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import be.vinci.pae.domain.SupervisorFactory;
import be.vinci.pae.domain.dto.SupervisorDTO;
import be.vinci.pae.domain.ucc.SupervisorUCC;
import be.vinci.pae.services.dal.DalServices;
import be.vinci.pae.services.dao.SupervisorDAO;
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
 * SupervisorUCC test class.
 */
public class SupervisorUCCImplTest {

  private static ServiceLocator serviceLocator;
  private static SupervisorDAO supervisorDAOMock;
  private static DalServices dalServicesMock;
  private SupervisorUCC supervisorUCC;
  private SupervisorDTO supervisorDTO;

  @BeforeAll
  static void init() {
    serviceLocator = ServiceLocatorUtilities.bind(new BinderTest());
    supervisorDAOMock = serviceLocator.getService(SupervisorDAO.class);
    dalServicesMock = serviceLocator.getService(DalServices.class);
  }

  @BeforeEach
  void setup() {
    supervisorUCC = serviceLocator.getService(SupervisorUCC.class);
    SupervisorFactory supervisorFactory = serviceLocator.getService(SupervisorFactory.class);
    supervisorDTO = supervisorFactory.getSupervisorDTO();
    Mockito.doNothing().when(dalServicesMock).startTransaction();
    Mockito.doNothing().when(dalServicesMock).commitTransaction();
    Mockito.doNothing().when(dalServicesMock).rollbackTransaction();
  }

  @AfterEach
  void reset() {
    Mockito.reset(supervisorDAOMock);
  }


  @Test
  @DisplayName("Test get one by id not found")
  public void testGetOneByIdNotFound() {
    Mockito.when(supervisorDAOMock.getOneById(1)).thenReturn(null);
    assertThrows(ResourceNotFoundException.class, () -> supervisorUCC.getOneById(1));
  }

  @Test
  @DisplayName("Test get one by id correct")
  public void testGetOneByIdCorrect() {
    Mockito.when(supervisorDAOMock.getOneById(1)).thenReturn(supervisorDTO);
    assertNotNull(supervisorUCC.getOneById(1));
  }

}
