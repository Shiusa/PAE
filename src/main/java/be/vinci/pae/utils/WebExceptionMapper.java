package be.vinci.pae.utils;

import be.vinci.pae.utils.exceptions.DuplicateException;
import be.vinci.pae.utils.exceptions.FatalException;
import be.vinci.pae.utils.exceptions.InvalidRequestException;
import be.vinci.pae.utils.exceptions.NotAllowedException;
import be.vinci.pae.utils.exceptions.ResourceNotFoundException;
import be.vinci.pae.utils.exceptions.UnauthorizedAccesException;
import jakarta.ws.rs.WebApplicationException;
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
    if (exception instanceof UnauthorizedAccesException) {
      return Response.status(Status.UNAUTHORIZED)
          .entity(exception.getMessage())
          .build();
    }
    if (exception instanceof DuplicateException) {
      return Response.status(Status.CONFLICT)
          .entity(exception.getMessage())
          .build();
    }
    if (exception instanceof NotAllowedException) {
      return Response.status(Status.FORBIDDEN)
          .entity(exception.getMessage())
          .build();
    }
    if (exception instanceof WebApplicationException) {
      return ((WebApplicationException) exception).getResponse();
    }
    return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
        .entity(exception.getMessage())
        .type(MediaType.TEXT_PLAIN)
        .build();
  }
}
