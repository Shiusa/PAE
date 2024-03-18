package be.vinci.pae.utils.exceptions;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

/**
 * Exception handler class.
 */
@Provider
public class MapperException implements ExceptionMapper<Throwable> {

  @Override
  public Response toResponse(Throwable exception) {
    return Response.status(getCode(exception)).build();
  }

  /**
   * Get the code of the web exception.
   *
   * @param e the exception.
   * @return the code.
   */
  private int getCode(Throwable e) {
    if (e instanceof DuplicateException) {
      return 409;
    }
    return 500;
  }
}
