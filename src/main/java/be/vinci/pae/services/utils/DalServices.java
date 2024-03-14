package be.vinci.pae.services.utils;

/**
 * DalService Interface.
 */
public interface DalServices {

  /**
   * Start a transaction.
   */
  void startTransaction();

  /**
   * Commit changes.
   */
  void commit();

  /**
   * Rollback changes.
   */
  void rollback();
}
