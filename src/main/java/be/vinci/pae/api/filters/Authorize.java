package be.vinci.pae.api.filters;

import jakarta.ws.rs.NameBinding;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Authorize anotation.
 */
@NameBinding
@Retention(RetentionPolicy.RUNTIME)
public @interface Authorize {

}
