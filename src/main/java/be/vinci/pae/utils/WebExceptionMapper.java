package be.vinci.pae.utils;

import be.vinci.pae.utils.exceptions.DuplicateException;
import be.vinci.pae.utils.exceptions.FatalException;
import be.vinci.pae.utils.exceptions.InvalidRequestException;
import be.vinci.pae.utils.exceptions.ResourceNotFoundException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import jakarta.ws.rs.ext.ExceptionMapper;

/**
 * WebExceptionMapper class.
 */
public class WebExceptionMapper implements ExceptionMapper<Throwable> {

  @Override
  public Response toResponse(Throwable exception) {
    if (exception instanceof FatalException) {
      return Response.status(Status.INTERNAL_SERVER_ERROR)
          .entity(exception.getMessage())
          .build();
    }
    if (exception instanceof InvalidRequestException) {
      return Response.status(Status.BAD_REQUEST)
          .entity(exception.getMessage())
          .build();
    }
    if (exception instanceof ResourceNotFoundException) {
      return Response.status(Status.NOT_FOUND)
          .entity(exception.getMessage())
          .build();
    }
    if (exception instanceof DuplicateException) {
      return Response.status(Status.CONFLICT)
          .entity(exception.getMessage())
          .build();
    }
    return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
        .entity(exception.getMessage())
        .type(MediaType.TEXT_PLAIN)
        .build();
  }
}
