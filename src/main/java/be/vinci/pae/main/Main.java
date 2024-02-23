package be.vinci.pae.main;

import be.vinci.pae.utils.Config;
import java.io.IOException;
import java.net.URI;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

/**
 * Main class.
 */
public class Main {

  public static final String BASE_URI;

  static {
    Config.load("dev.properties");
    BASE_URI = Config.getProperty("BaseUri");
  }
  // Base URI the Grizzly HTTP server will listen on

  /**
   * Starts Grizzly HTTP server exposing JAX-RS resources defined in this application.
   *
   * @return Grizzly HTTP server.
   */
  public static HttpServer startServer() {
    // create a resource config that scans for JAX-RS resources and providers
    // in be.vinci package
    final ResourceConfig rc = new ResourceConfig().packages("be.vinci.pae.api");
    // create and start a new instance of grizzly http server
    // exposing the Jersey application at BASE_URI
    return GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), rc);
  }

  /**
   * Main method.
   *
   * @param args list of arguments
   * @throws IOException I/O exception
   */
  public static void main(String[] args) throws IOException {
    final HttpServer server = startServer();
    System.out.println(String.format(
        "Jersey app started with endpoints available at " + "%s%nHit Ctrl-C to stop it...",
        BASE_URI));
    System.in.read();
    server.stop();
  }
}

