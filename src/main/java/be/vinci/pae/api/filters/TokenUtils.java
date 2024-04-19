package be.vinci.pae.api.filters;

import be.vinci.pae.domain.dto.UserDTO;
import be.vinci.pae.domain.ucc.UserUCC;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * handle some methods to verify tokens.
 */
public class TokenUtils {

  public static UserDTO verifyToken(String token, JWTVerifier jwtVerifier,
      UserUCC userUCC) {
    DecodedJWT decodedToken = null;
    try {
      decodedToken = jwtVerifier.verify(token);
    } catch (Exception e) {
      throw new WebApplicationException(Response.status(Status.UNAUTHORIZED)
          .entity("Malformed token : " + e.getMessage()).type("text/plain").build());
    }
    return userUCC.getOneById(
        decodedToken.getClaim("user").asInt());
  }

}

