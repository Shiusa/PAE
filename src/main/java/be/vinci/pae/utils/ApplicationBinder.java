package be.vinci.pae.utils;

import be.vinci.pae.domain.ContactFactory;
import be.vinci.pae.domain.ContactFactoryImpl;
import be.vinci.pae.domain.UserFactory;
import be.vinci.pae.domain.UserFactoryImpl;
import be.vinci.pae.domain.ucc.ContactUCC;
import be.vinci.pae.domain.ucc.ContactUCCImpl;
import be.vinci.pae.domain.ucc.UserUCC;
import be.vinci.pae.domain.ucc.UserUCCImpl;
import be.vinci.pae.services.dal.DalServices;
import be.vinci.pae.services.dal.DalServicesImpl;
import be.vinci.pae.services.dao.ContactDAO;
import be.vinci.pae.services.dao.ContactDAOImpl;
import be.vinci.pae.services.dao.UserDAO;
import be.vinci.pae.services.dao.UserDAOImpl;
import jakarta.inject.Singleton;
import jakarta.ws.rs.ext.Provider;
import org.glassfish.hk2.utilities.binding.AbstractBinder;

/**
 * ApplicationBinder class.
 */
@Provider
public class ApplicationBinder extends AbstractBinder {

  /**
   * Binds implementations to their interface.
   */
  @Override
  protected void configure() {
    bind(UserDAOImpl.class).to(UserDAO.class).in(Singleton.class);
    bind(DalServicesImpl.class).to(DalServices.class).in(Singleton.class);
    bind(UserFactoryImpl.class).to(UserFactory.class).in(Singleton.class);
    bind(UserUCCImpl.class).to(UserUCC.class).in(Singleton.class);
    bind(ContactFactoryImpl.class).to(ContactFactory.class).in(Singleton.class);
    bind(ContactUCCImpl.class).to(ContactUCC.class).in(Singleton.class);
    bind(ContactDAOImpl.class).to(ContactDAO.class).in(Singleton.class);

  }
}
