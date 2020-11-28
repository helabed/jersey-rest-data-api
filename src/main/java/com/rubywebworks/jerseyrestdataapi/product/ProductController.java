package com.rubywebworks.jerseyrestdataapi.product;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;


@Path("/products")
public class ProductController {
  // this instance is a singleton.
  private ProductService productService = ProductService.getInstance();

  // mapping /products web url to this method getAllProducts(), defaults to GET method.
  // Jersey auto magically converts the List<Product> into JSON and returns it to the
  // caller, in this case the web browser.
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  // /api/products
  public List<Product> getAllProducts() {
    return productService.getAllProducts();
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/{productId}")
  // /api/products/75
  public Product getProduct(@PathParam("productId") Long id) {
    return productService.getProduct(id);
  }

  // Example of a curl command to produce a POST - i.e to create a record
  // curl -X POST  -H "Content-type: application/json" -d\
  //  "{\"id\":104,\"title\":\"Ruby on Rails\",\"description\":\"Introduction to Rails\"\
  //  ,\"price\":12.6,\"image_url\":\"oranges.jpg\",\"created_at\":\"2020-11-16T23:23:24.601-06:00\"\
  //  ,\"updated_at\":\"2020-11-20T20:20:24.601-06:00\"}"  http://localhost:8080/api/products
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  public void addProduct(Product product) {
    productService.addProduct(product);
  }

  // Example of a curl command to produce a PUT - i.e to update a record
  // curl -X PUT -H "Accept: text/json" -H "Content-type: application/json" -d \
  //  "{\"id\":104,\"title\":\"Ruby on Rails\",\"description\":\"Introduction to Rails\"\
  //  ,\"price\":12.6,\"image_url\":\"oranges.jpg\",\"created_at\":\"2020-11-16T23:23:24.601-06:00\"\
  //  ,\"updated_at\":\"2020-11-20T20:20:24.601-06:00\"}"  http://localhost:8080/api/products/104
  @PUT
  @Path("/{productId}")
  @Consumes(MediaType.APPLICATION_JSON)
  public void updateProduct(Product product, @PathParam("productId") Long id) {
    productService.updateProduct(id, product);
  }

  // Example of a curl command to produce a DELETE
  // curl -X DELETE -H "Accept: text/json" -H "Content-type: application/json" http://localhost:8080/products/106
  @DELETE
  @Path("/{productId}")
  public void deleteproduct(@PathParam("productId") long id) {
    productService.deleteProduct(id);
  }
}