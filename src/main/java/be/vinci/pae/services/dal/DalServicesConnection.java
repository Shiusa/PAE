package be.vinci.pae.services.dal;

/**
 * Handles DB connections.
 */
public interface DalServicesConnection {

  void startTransaction();

  void commitTransaction();

  void rollbackTransaction();

}
