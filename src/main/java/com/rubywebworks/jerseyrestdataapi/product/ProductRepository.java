package com.rubywebworks.jerseyrestdataapi.product;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

import com.rubywebworks.jerseyrestdataapi.JerseyRestDataApiApplication;

public class ProductRepository {
  /**
   *  This product repository is designed for JDBC access to this table below as
   *  defined in a PostgreSQL database.
   *
   Column    |              Type              | Collation | Nullable |               Default
-------------+--------------------------------+-----------+----------+--------------------------------------
 id          | bigint                         |           | not null | nextval('products_id_seq'::regclass)
 title       | character varying              |           |          |
 description | text                           |           |          |
 image_url   | character varying              |           |          |
 price       | numeric(8,2)                   |           |          |
 created_at  | timestamp(6) without time zone |           | not null |
 updated_at  | timestamp(6) without time zone |           | not null |
Indexes:
    "products_pkey" PRIMARY KEY, btree (id)
Referenced by:
    TABLE "line_items" CONSTRAINT "fk_rails_11e15d5c6b" FOREIGN KEY (product_id) REFERENCES products(id)
   *
   */

  private static ProductRepository instance;
  private static Connection dbConnection;

  private ProductRepository() {
    // Constructor hidden because this is a singleton
    // use ProductRepository.getInstance() instead of 'new ProductRepository'
  }

  public static ProductRepository getInstance() {
    if (instance == null) {
      // Create this class's only instance
      instance = new ProductRepository();
      String url = JerseyRestDataApiApplication.databaseUrl;
      String driver = JerseyRestDataApiApplication.databaseDriverClass;
      try {
        Class.forName(driver); //load the JDBC driver.
        dbConnection = DriverManager.getConnection(url);
      } catch (Exception e) {
        System.out.println("Exception: " + e.getMessage());
      }
    }
    return instance;
  }

  public Iterable<Product> findAll() {
    ArrayList<Product> products = new ArrayList<Product>();

    String sql = "select * from products order by id DESC";
    Statement st = null;
    ResultSet rs = null;
    try {
      st = dbConnection.createStatement();
      rs = st.executeQuery(sql);
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
      System.out.println("Exception: " + e.getMessage());
    } finally {
      sqlCleanup(st, rs);
    }
    return products;
  }

  public void save(Product product) {
    System.out.println("INSERT product...");
    System.out.println(product);

    PreparedStatement st = null;
    String sql = "insert into products "
        + " (title, description, image_url, price, created_at, updated_at)"
        + " values (?,?,?,?,?,?)";
    try {
      st = dbConnection.prepareStatement(sql);

      // no need to insert 'id' because it is auto-incremented on the DB by default.
      // the 'id' field defaults to:  "nextval('products_id_seq'::regclass)"
      st.setString(    1, product.getTitle());
      st.setString(    2, product.getDescription());
      st.setString(    3, product.getImage_url());
      st.setBigDecimal(4, product.getPrice());
      Timestamp ts = new Timestamp(new Date().getTime());

      st.setTimestamp( 5, ts);
      st.setTimestamp( 6, ts);

      st.executeUpdate();
    } catch (Exception e) {
      System.out.println("Exception: " + e.getMessage());
    } finally {
      sqlCleanup(st);
    }
  }

  public Product findById(Long id) {
    Product p = new Product();
    String sql = "select * from products where id=?";
    PreparedStatement st = null;
    ResultSet rs = null;
    try {
      st = dbConnection.prepareStatement(sql);
      st.setLong(1, id);
      rs = st.executeQuery();
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
      System.out.println("Exception: " + e.getMessage());
    } finally {
      sqlCleanup(st, rs);
    }
    return p;
  }

  public void deleteById(Long id) {
    String sql = "delete from products where id=?";
    PreparedStatement st = null;
    try {
      st = dbConnection.prepareStatement(sql);
      st.setLong(1, id);
      st.executeUpdate();
    } catch (Exception e) {
      System.out.println("Exception: " + e.getMessage());
    } finally {
      sqlCleanup(st);
    }
  }

  public void update(Long id, Product product) {
    System.out.println("UPDATE product...");
    System.out.println(product);

    String sql = "update products "
        + " set title=?, description=?, image_url=?,"
        + "     price=?, created_at=?, updated_at=?"
        + " where id=?";
    PreparedStatement st = null;
    try {
      st = dbConnection.prepareStatement(sql);
      Product oldProduct = findById(id);

      if( product.getTitle() != null) {
        st.setString(    1, product.getTitle());
      } else {
        st.setString(    1, oldProduct.getTitle());
      }

      if( product.getDescription() != null) {
        st.setString(    2, product.getDescription());
      } else {
        st.setString(    2, oldProduct.getDescription());
      }

      if( product.getImage_url() != null) {
        st.setString(    3, product.getImage_url());
      } else {
        st.setString(    3, oldProduct.getImage_url());
      }

      if( product.getPrice() != null) {
        st.setBigDecimal(4, product.getPrice());
      } else {
        st.setBigDecimal(4, oldProduct.getPrice());
      }

      if( product.getCreated_at() != null) {
        // ignore new created_at value, use old DB value.
        st.setTimestamp( 5, oldProduct.getCreated_at());
      } else {
        st.setTimestamp( 5, oldProduct.getCreated_at());
      }

      Timestamp ts = new Timestamp(new Date().getTime());
        // ignore new updated_at value, use a Timestamp of now.
      st.setTimestamp( 6, ts);

      // setting WHERE clause
      // use the id key from URL path and not what is provided in body of PUT (i.e not product.id)
      st.setLong(      7, id);

      st.executeUpdate();
    } catch (Exception e) {
      System.out.println("Exception: " + e.getMessage());
    } finally {
      sqlCleanup(st);
    }
  }

  private void sqlCleanup(Statement st, ResultSet rs) {
    if (rs != null) {
      try { rs.close(); }
      catch (SQLException e) { } // ignore
    }
    if (st != null) {
      try { st.close(); }
      catch (SQLException e) { } // ignore
    }
  }

  private void sqlCleanup(Statement st) {
    if (st != null) {
      try { st.close(); }
      catch (SQLException e) { } // ignore
    }
  }
}
