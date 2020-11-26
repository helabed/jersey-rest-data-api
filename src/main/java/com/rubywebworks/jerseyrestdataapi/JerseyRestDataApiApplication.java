package com.rubywebworks.jerseyrestdataapi;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.servlet.ServletContainer;

/**
 * Root resource (exposed at "test" path)
 */
@Path("/test")
public class JerseyRestDataApiApplication {

  public static void main(String[] args) {
    setLoggingLevel();
    displaySortedSystemProperties();
    displaySortedEnvironmentVars();

    ServletContextHandler context =
      new ServletContextHandler(ServletContextHandler.NO_SESSIONS);

    context.setContextPath("/");

    Server jettyServer = new Server(8080);
    jettyServer.setHandler(context);

    ServletHolder jerseyServlet = context.addServlet(ServletContainer.class, "/api/*");
    jerseyServlet.setInitOrder(1);
    jerseyServlet.setInitParameter(
      "jersey.config.server.provider.packages",
      "com.rubywebworks.jerseyrestdataapi");

    try {
      jettyServer.start();
      jettyServer.join();
    } catch (Exception ex) {
      Logger.getLogger(
        JerseyRestDataApiApplication.class.getName()).log(Level.SEVERE, null, ex);
      System.out.println(ex);
    } finally {
        jettyServer.destroy();
    }
  }

  private static void displaySortedEnvironmentVars() {
    System.out.println("----------------Display Sorted System Environment Vars-------------------");
    Map<String, String> env = System.getenv();

    LinkedHashMap<String, String> collectEnv =
        env.entrySet().stream()
            .sorted(Map.Entry.comparingByKey())
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                (oldValue, newValue) -> oldValue, LinkedHashMap::new));

    collectEnv.forEach((k, v) -> System.out.println(k + ":  " + v));
    System.out.println("----------------Display Sorted System Environment Vars-------------------");
  }

  private static void displaySortedSystemProperties() {
    System.out.println("----------------Display Sorted System Props-------------------");
    Properties properties = System.getProperties();
    // Thanks Java 8
    LinkedHashMap<String, String> collect = properties.entrySet().stream()
        .collect(Collectors.toMap(k -> (String) k.getKey(), e -> (String) e.getValue()))
        .entrySet().stream().sorted(Map.Entry.comparingByKey())
        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
            (oldValue, newValue) -> oldValue, LinkedHashMap::new));

    collect.forEach((k, v) -> System.out.println(k + ":  " + v));
    System.out.println("----------------Display Sorted System Props-------------------");
  }

  private static void setLoggingLevel() {
    System.out.println("------------------Setting Log Level-------------------");
    System.setProperty("org.eclipse.jetty.util.log.class", "org.eclipse.jetty.util.log.StdErrLog");
    System.setProperty("org.eclipse.jetty.LEVEL", "INFO");
  }

  /**
   * Method handling HTTP GET requests. The returned object will be sent
   * to the client as "text/plain" media type.
   *
   * @return String that will be returned as a text/plain response.
   */
  @GET
  @Produces(MediaType.TEXT_PLAIN)
  @Path("{name}")
  public String getGreeting(@PathParam(value = "name") String aName) {
    return "Good Morning " + aName + ", The time now is: " + LocalDateTime.now();
  }
}
