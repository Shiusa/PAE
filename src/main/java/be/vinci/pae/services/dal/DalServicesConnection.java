package be.vinci.pae.services.dal;

/**
 * Handles DB connections.
 */
public interface DalServicesConnection {

  /**
   * Start a transaction and open a connection.
   */
  void startTransaction();

  /**
   * Commit a transaction and close a connection.
   */
  void commitTransaction();

  /**
   * Rollback a transaction and close a connection.
   */
  void rollbackTransaction();

}
