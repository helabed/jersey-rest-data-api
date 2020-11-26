package com.rubywebworks.jerseyrestdataapi;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.MatrixParam;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;

public class UserBean {
  @PathParam("id")
  public String id;
  @MatrixParam("name")
  public String name;
  @MatrixParam("age")
  public String age;
  @QueryParam("address")
  @DefaultValue("No address provided")
  public String address;
  @HeaderParam("user-agent")
  public String userAgent;

  public UserBean() {
  }

  public String toString() {
    return
      "\nId: "         + id +
      "\nName: "       + name +
      "\nAge: "        + age +
      "\nAddress: "    + address +
      "\nUser Agent: " + userAgent + "\n\n";
    }
}
