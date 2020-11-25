package com.rubywebworks.jerseyrestdataapi;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 *    helloWorld Root Resource
 */
@Path("helloWorld")
public class HelloWorldResource {

  @GET
  @Produces(MediaType.TEXT_PLAIN)
  public String greet() {
    return "Hello World!!!";
  }
}
