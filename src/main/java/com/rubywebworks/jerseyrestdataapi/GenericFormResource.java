package com.rubywebworks.jerseyrestdataapi;


import javax.ws.rs.CookieParam;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.MatrixParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Generic Form Resource
 */
@Path("/forms")
public class GenericFormResource {

  @GET
  @Produces(MediaType.TEXT_PLAIN)
  @Path("processUser/{userId}")
  // /api/forms/processUser/11;name=Hani;age=57?height=66&weight=180
  public Response processUserInfo(
                           @PathParam("userId")  String id,
                           @MatrixParam("name")  String aName,
      @DefaultValue("25")  @MatrixParam("age")   String age,
      @DefaultValue("55")  @QueryParam("height") String height,
      @DefaultValue("170") @QueryParam("weight") String weight) {

    return Response
               .status(200)
               .entity("Id: " + id + ", Name: " + aName + ", Age: " + age + ", Height: " + height + ", Weight: " + weight)
               .build();
  }

  @GET
  @Produces(MediaType.TEXT_PLAIN)
  @Path("userAgent")
  // /api/forms/userAgent
  public String getUserDevice(
          @HeaderParam("user-agent") String userAgent,
          @HeaderParam("Content-Type") MediaType contentType) {
    return "User Agent: " + userAgent + ",\nContent-Type: " + contentType ;
  }

  @GET
  @Produces(MediaType.TEXT_PLAIN)
  @Path("browserCookies")
  // /api/forms/browserCookies
  public String browserCookies(
          @CookieParam("user-agent") Cookie userAgentCookie) {
    if( userAgentCookie != null ) {
      return
      "\nName: "    + userAgentCookie.getName() +
      "\nValue: "   + userAgentCookie.getValue() +
      "\nDomain: "  + userAgentCookie.getDomain() +
      "\nPath: "    + userAgentCookie.getPath() +
      "\nVersion: " + userAgentCookie.getVersion();

    } else {
     return "userAgentCookie is null";
    }
  }

  @POST
  @Produces(MediaType.TEXT_PLAIN)
  @Path("addUser")
  // curl -X POST -d "name=John&id=100" http://localhost:8080/api/forms/addUser
  public String addUser(
      @FormParam("name") String name,
      @FormParam("id") String id) {
        return
          "Add User:" +
            "\nId: "   + id +
            "\nName: " + name;
  }
}
