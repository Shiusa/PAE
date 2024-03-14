package be.vinci.pae.services.utils;

/**
 * DalService Interface.
 */
public interface DalServices {

  void startTransaction();

  void commit();

  void rollback();
}
