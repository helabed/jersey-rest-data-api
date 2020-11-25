package com.rubywebworks.jerseyrestdataapi;


import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.MatrixParam;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
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
}
