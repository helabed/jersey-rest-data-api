package com.rubywebworks.jerseyrestdataapi;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Stack;
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
  /**
  * Application Property Object for reading the Configuration file from disk.
  */
  public static final String APPLICATION_PROPERTIES_FILE =
      "./src/main/resources/application.properties";
  public static Properties appProperties;
  public static final boolean debugging = true;
  public static String databaseUrl = null;
  public static String databaseDriverClass = null;
  public static String databaseUsername = null;
  public static String databasePassword = null;

  public static void main(String[] args) {
    setLoggingLevel();
    displaySortedSystemProperties();
    displaySortedEnvironmentVars();
    loadConfigFile();

    ServletContextHandler jettyContext =
      new ServletContextHandler(ServletContextHandler.NO_SESSIONS);

    jettyContext.setContextPath("/");

    Server jettyServer = new Server(8080);
    jettyServer.setHandler(jettyContext);

    ServletHolder jerseyServlet = jettyContext.addServlet(ServletContainer.class, "/api/*");
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

  private static void loadConfigFile() {
    try {
      FileInputStream theFileInputStream = null;

      theFileInputStream = new FileInputStream(APPLICATION_PROPERTIES_FILE);
      if (loadConfig(theFileInputStream) == true) {
        if (debugging) {

          // Also dump any additional properties
          System.out.println("------------------unused application.properties-------------------");
          Enumeration<Object> keys = appProperties.keys();
          while (keys.hasMoreElements()) {
            String key = (String) keys.nextElement();
            String value = appProperties.getProperty(key);
            trace("(App Property) " + key + " = " + value);
          }
          System.out.println("------------------unused application.properties-------------------");
        }
      } else {
        throw new Exception("loadConfig() returned false !!");
      }

    } catch (Exception e) {
    } finally {
    }
  }

  /**
   * Loads the given configuration file. All global properties (such as Server
   * Info) will be read and removed from the properties list. Any leftover
   * properties will be returned. Returns null if the properties could not be
   * loaded
   *
   * @exception Exception Our own exception class. something went wrong.
   *
   * @param in Configuration file InputStream
   *
   * @return true if the configuration file was loaded
   */
  private static final boolean loadConfig(java.io.InputStream in) throws Exception {
    boolean resultCode = false;

    // If the input stream is null, then the configuration file
    // was not found
    if (in == null) {
      throw new Exception("application.properties configuration file not found");
    } else {
      try {
        appProperties = new Properties();
        // Load the configuration file into the properties object
        appProperties.load(in);
        databaseUrl = consume(appProperties, "database.url");
        databaseDriverClass = consume(appProperties, "database.driver.class");
        databaseUsername = consume(appProperties, "database.username");
        databasePassword = consume(appProperties, "database.password");

        resultCode = true;
      } catch (IOException e) {
        trace("loadConfig() - IOException: " + e.getMessage());
        e.printStackTrace();
        resultCode = false;
      } catch (Exception e) {
        trace("loadConfig(): " + e.getMessage());
        e.printStackTrace();
        resultCode = false;
      } finally {
        // Always close the input stream
        if (in != null) {
          try {
            in.close();
          } catch (IOException ex) {
            trace("loadConfig() - IOException: " + ex.getMessage());
            ex.printStackTrace();
          }
        }
      }
    }
    return resultCode;
  }

  private static final void trace(String s) {
    System.out.println("JettyServer: " + s);
  }

  /**
   * Display the string passed(i.e the string to be traced), and start the
   * stopwatch by getting a timestamp in milliseconds.
   *
   * @param s The string to display.
   *
   * @return The time stamp in milliseconds to be used by the sister function of
   *         this method.
   * @see #traceEndTimeStamp(long,String)
   */
  public final long traceStartTimeStamp(String s) {
    trace(s + "---------- Start StopWatch " + "---------- ");

    long startMilliseconds = System.currentTimeMillis();

    return startMilliseconds;
  }

  /**
   * Display the string passed(i.e the string to be traced), and STOPS the
   * stopwatch and calculates the time spent in milliseconds.
   *
   * @param startMilliseconds The starting time stamp.
   * @param s                 The string to display.
   *
   * @see #traceStartTimeStamp(String)
   */
  public final void traceEndTimeStamp(long startMilliseconds, String s) {
    trace(

        s + "---------- Stop StopWatch " + (System.currentTimeMillis() - startMilliseconds) + " milliseconds "
            + "---------- "

    );
  }

  /**
   * Consumes the given property and returns the value.
   *
   * @param properties Properties table
   * @param key        Key of the property to retrieve and remove from the
   *                   properties table
   * @return Value of the property, or null if not found
   */
  public static String consume(Properties p, String key) {
    String s = null;

    if ((p != null) && (key != null)) {
      // Get the value of the key
      s = p.getProperty(key);

      // If found, remove it from the properties table
      if (s != null) {
        p.remove(key);
      }
    }
    return s;
  }

  /**
   * Consumes the given property and returns the integer value.
   *
   * @param properties Properties table
   * @param key        Key of the property to retrieve and remove from the
   *                   properties table
   * @return Value of the property, or -1 if not found
   */
  public static int consumeInt(Properties p, String key) {
    int n = -1;

    // Get the String value
    String value = consume(p, key);

    // Got a value; convert to an integer
    if (value != null) {
      try {
        n = Integer.parseInt(value);
      } catch (NumberFormatException ex) {
        ex.printStackTrace();
        trace("consumeInt() - NumberFormatException: " + ex.getMessage());
      }
    }
    return n;
  }

  /**
   * Extracts a human readable/user readable error message from the errorStack
   * passed to it.
   *
   * @param errorStack the errorStack that is populated usually by the Form
   *                   Validator or some other runtime exceptions. The error stack
   *                   is used for recovering error messages in the order they
   *                   occured.
   *
   * @return String containing human readable error message.
   */
  public static String getErrorMessage(Stack<Object> errorStack) {
    String temp = "";

    StringBuffer theBuffer = new StringBuffer("");
    for (int i = errorStack.size() - 1; i >= 0; i--) {

      temp = (String) errorStack.pop();

      temp = convertTheError(temp);
      theBuffer.append("<B><FONT FACE=\"VERDANA\" SIZE=\"3\" COLOR=\"#AA0000\">" + temp + "</FONT></B><BR><BR>");

    }

    return theBuffer.toString();
  }

  /**
   * Converts the Error Message from a NON-Presentable one into a human/user
   * readable one. As this grows, it should be placed in a HashTable and all
   * errors be made into static final String.
   *
   * @param theError the String containing criptic error message
   *
   * @return String containing the Human/User readable error message.
   */
  public static String convertTheError(String theError) {
    if (theError.equals("JZ006: Caught IOException: java.io.InterruptedIOException: Read timed out"))
      return "Your query has taken more time than is allowed and " + "thus it was abandoned. "
          + "Please go back and restrict your search. ";
    else
      return theError;
  }
}
