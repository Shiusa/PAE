package be.vinci.pae.api.filters;

import jakarta.ws.rs.NameBinding;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Student annotation.
 */
@NameBinding
@Retention(RetentionPolicy.RUNTIME)
public @interface Student {

}
