package com.rubywebworks.jerseyrestdataapi.product;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class ProductRepository {

  private static ProductRepository instance;
  private static Connection dbConnection;

  private ProductRepository() {
    // Constructor hidden because this is a singleton
    // use ProductRepository.getInstance() instead of 'new ProductRepository'
  }

  public static ProductRepository getInstance() {
    if (instance == null) {
      // Create the instance
      instance = new ProductRepository();
      String url = "jdbc:postgresql://localhost:5432/depot_development";
      try {
        Class.forName("org.postgresql.Driver");
        dbConnection = DriverManager.getConnection(url);
      } catch (Exception e) {
        System.out.println(e.getMessage());
      }
    }
    return instance;
  }

  public Iterable<Product> findAll() {
    ArrayList<Product> products = new ArrayList<Product>();

    String sql = "select * from products";
    try {
      Statement st = dbConnection.createStatement();
      ResultSet rs = st.executeQuery(sql);
      while(rs.next()) {
        Product p = new Product();
        p.setId(rs.getLong(1));
        p.setTitle(rs.getString(2));
        p.setDescription(rs.getString(3));
        p.setImage_url(rs.getString(4));
        p.setPrice(rs.getBigDecimal(5));
        p.setCreated_at(rs.getTimestamp(6));
        p.setUpdated_at(rs.getTimestamp(7));
        products.add(p);
      }
    } catch (Exception e) {
        System.out.println(e.getMessage());
    }
    return products;
  }

  public void save(Product product) {
    System.out.println(product);

    String sql = "insert into products "
        + " (id, title, description, image_url, price, created_at, updated_at)"
        + " values (?,?,?,?,?,?,?)";
    try {
      PreparedStatement st = dbConnection.prepareStatement(sql);

      st.setLong(      1, product.getId());
      st.setString(    2, product.getTitle());
      st.setString(    3, product.getDescription());
      st.setString(    4, product.getImage_url());
      st.setBigDecimal(5, product.getPrice());
      st.setTimestamp( 6, product.getCreated_at());
      st.setTimestamp( 7, product.getUpdated_at());

      st.executeUpdate();
    } catch (Exception e) {
        System.out.println(e.getMessage());
    }
  }

  public Product findById(Long id) {
    Product p = new Product();
    String sql = "select * from products where id="+id;
    try {
      Statement st = dbConnection.createStatement();
      ResultSet rs = st.executeQuery(sql);
      if(rs.next()) {
      p.setId(rs.getLong(1));
      p.setTitle(rs.getString(2));
      p.setDescription(rs.getString(3));
      p.setImage_url(rs.getString(4));
      p.setPrice(rs.getBigDecimal(5));
      p.setCreated_at(rs.getTimestamp(6));
      p.setUpdated_at(rs.getTimestamp(7));
      System.out.println(p);
      }
    } catch (Exception e) {
        System.out.println(e.getMessage());
    }
    return p;
  }

  public void deleteById(Long id) {
    String sql = "delete from products where id=?";
    try {
      PreparedStatement st = dbConnection.prepareStatement(sql);
      st.setLong(1, id);
      st.executeUpdate();
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }

  public void update(Long id, Product product) {
    //TODO: implement update - UPSERT on DB is locking ??
  }
}