package be.vinci.pae.utils.exceptions;

public class UnauthorizedAccesException extends RuntimeException {

  /**
   * Unauthorized acces exception.
   */
  public UnauthorizedAccesException() {
    super();
  }

  /**
   * Unauthorized acces exception with a parameter.
   *
   * @param e message.
   */
  public UnauthorizedAccesException(String e) {
    super(e);
  }

}
