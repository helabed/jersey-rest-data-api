package com.rubywebworks.jerseyrestdataapi;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.servlet.ServletContainer;

import java.util.logging.Level;
import java.util.logging.Logger;
/**
 * Root resource (exposed at "myresource" path)
 */
@Path("/myresource")
public class JerseyRestDataApiApplication {
  public static void main(String[] args) {

    Server server = new Server(8080);

    ServletContextHandler ctx =
      new ServletContextHandler(ServletContextHandler.NO_SESSIONS);

    ctx.setContextPath("/");
    server.setHandler(ctx);

    ServletHolder serHol = ctx.addServlet(ServletContainer.class, "/rest/*");
    serHol.setInitOrder(1);
    serHol.setInitParameter("jersey.config.server.provider.packages",
            "com.rubywebworks.jerseyrestdataapi");

    try {
        server.start();
        server.join();
    } catch (Exception ex) {
        Logger.getLogger(
          JerseyRestDataApiApplication.class.getName()).
            log(Level.SEVERE, null, ex);
        System.out.println(ex);
    } finally {
        server.destroy();
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
  public String getIt() {
    return "Got it!";
  }
}
