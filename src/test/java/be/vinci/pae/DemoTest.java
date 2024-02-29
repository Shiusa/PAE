package be.vinci.pae;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import be.vinci.pae.domain.ucc.UserUCC;
import be.vinci.pae.utils.ApplicationBinder;
import be.vinci.pae.utils.Config;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.utilities.ServiceLocatorUtilities;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Demo test class. Please read the document on Moodle to know
 * how test classes are handled by Jenkins.
 */
public class DemoTest {
  private UserUCC userUCC;

  @BeforeEach
  void initAll() {
    Config.load("dev.properties");
    ServiceLocator locator = ServiceLocatorUtilities.bind(new
        ApplicationBinderTest());
    this.userUCC = locator.getService(UserUCC.class);
  }
  @Test
  public void demoTest() {
    assertNotNull(this.userUCC);
  }
}

