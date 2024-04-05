package be.vinci.pae.api.filters;

import be.vinci.pae.domain.dto.UserDTO;
import be.vinci.pae.domain.ucc.UserUCC;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * handle some methods to verify tokens.
 */
public class TokenUtils {

  public static void setProperty(ContainerRequestContext requestContext, Algorithm jwtAlgorithm,
      JWTVerifier jwtVerifier, UserUCC userUCC, String token) {
    DecodedJWT decodedToken = null;
    try {
      decodedToken = jwtVerifier.verify(token);
    } catch (Exception e) {
      throw new WebApplicationException(Response.status(Status.UNAUTHORIZED)
          .entity("Malformed token : " + e.getMessage()).type("text/plain").build());
    }
    UserDTO authenticatedUser = userUCC.getOneById(
        decodedToken.getClaim("user").asInt());
    if (authenticatedUser == null) {
      requestContext.abortWith(Response.status(Status.FORBIDDEN)
          .entity("You are forbidden to access this resource").build());
    }

    requestContext.setProperty("user", authenticatedUser);
  }

}
