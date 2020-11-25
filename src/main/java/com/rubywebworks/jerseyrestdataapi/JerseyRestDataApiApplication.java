package com.rubywebworks.jerseyrestdataapi;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.servlet.ServletContainer;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Root resource (exposed at "test" path)
 */
@Path("/test")
public class JerseyRestDataApiApplication {

  public static void main(String[] args) {

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
    return "Good Morning " + aName;
  }
}