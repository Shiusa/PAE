package be.vinci.pae.utils;

import be.vinci.pae.utils.exceptions.BadRequestException;
import be.vinci.pae.utils.exceptions.FatalException;
import be.vinci.pae.utils.exceptions.NotFoundException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import jakarta.ws.rs.ext.ExceptionMapper;

/**
 * WebExceptionMapper class
 */
public class WebExceptionMapper implements ExceptionMapper<Throwable> {

  @Override
  public Response toResponse(Throwable exception) {
    if (exception instanceof FatalException) {
      return Response.status(Status.INTERNAL_SERVER_ERROR)
          .entity(exception.getMessage())
          .build();
    }
    if (exception instanceof BadRequestException) {
      return Response.status(Status.BAD_REQUEST)
          .entity(exception.getMessage())
          .build();
    }
    if (exception instanceof NotFoundException) {
      return Response.status(Status.NOT_FOUND)
          .entity(exception.getMessage())
          .build();
    }
    return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
        .entity(exception.getMessage())
        .type(MediaType.TEXT_PLAIN)
        .build();
  }
}
