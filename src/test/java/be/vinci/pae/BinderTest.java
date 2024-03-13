package be.vinci.pae;

import be.vinci.pae.domain.ucc.UserUCC;
import be.vinci.pae.domain.ucc.UserUCCImpl;
import be.vinci.pae.services.dao.UserDAO;
import be.vinci.pae.services.dao.UserDAOImpl;
<<<<<<< HEAD
import be.vinci.pae.services.dal.DalServices;
import be.vinci.pae.services.dal.DalServicesImpl;
=======
>>>>>>> master
import jakarta.inject.Singleton;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.mockito.Mockito;

/**
 * BinderTest class.
 */
public class BinderTest extends AbstractBinder {

  /**
   * Binds mocks and class to interfaces.
   */
  @Override
  protected void configure() {
    bind(UserUCCImpl.class).to(UserUCC.class).in(Singleton.class);
    bind(Mockito.mock(UserDAOImpl.class)).to(UserDAO.class);
<<<<<<< HEAD
    bind(Mockito.mock(DalServicesImpl.class)).to(DalServices.class);
=======
>>>>>>> master
  }
}
