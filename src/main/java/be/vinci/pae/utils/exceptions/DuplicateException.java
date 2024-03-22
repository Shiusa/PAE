package be.vinci.pae.utils.exceptions;

/**
 * Duplicate exception class.
 */
public class DuplicateException extends RuntimeException {

  /**
   * Constructor.
   */
  public DuplicateException() {
    super();
  }

  /**
   * Constructor.
   *
   * @param e message.
   */
  public DuplicateException(String e) {
    super(e);
  }

}
