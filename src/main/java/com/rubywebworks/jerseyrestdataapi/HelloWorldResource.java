package com.rubywebworks.jerseyrestdataapi;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

/**
 *    helloWorld Root Resource
 */
@Path("/helloWorld")
public class HelloWorldResource {

  @GET
  @Produces(MediaType.TEXT_PLAIN)
  // /api/helloWorld?name=Hani&age=57
  public String greet(@QueryParam("name") String aName,
                      @DefaultValue("25") @QueryParam("age") String age) {
    if( aName != null) {
      return "Hello " + aName + ", your age is: " + age;
    } else {
      return "Hello World!!!";
    }
  }
}
