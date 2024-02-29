package be.vinci.pae.utils;

import be.vinci.pae.domain.User;
import be.vinci.pae.domain.UserFactory;
import be.vinci.pae.domain.UserFactoryImpl;
import be.vinci.pae.domain.UserImpl;
import be.vinci.pae.domain.ucc.UserUCC;
import be.vinci.pae.domain.ucc.UserUCCImpl;
import be.vinci.pae.services.dao.UserDAOImpl;
import be.vinci.pae.services.dao.UserDAO;
import be.vinci.pae.services.utils.DalService;
import be.vinci.pae.services.utils.DalServiceImpl;
import jakarta.inject.Singleton;
import jakarta.ws.rs.ext.Provider;
import org.glassfish.hk2.utilities.binding.AbstractBinder;

/**
 * ApplicationBinder class.
 */
@Provider
public class ApplicationBinder extends AbstractBinder {

  /**
   * Binds implementations to their interface
   */
  @Override
  protected void configure() {
    bind(UserImpl.class).to(User.class).in(Singleton.class);
    bind(UserDAOImpl.class).to(UserDAO.class).in(Singleton.class);
    bind(DalServiceImpl.class).to(DalService.class).in(Singleton.class);
    bind(UserFactoryImpl.class).to(UserFactory.class).in(Singleton.class);
    bind(UserUCCImpl.class).to(UserUCC.class).in(Singleton.class);
  }
}
