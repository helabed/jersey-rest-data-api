package com.rubywebworks.jerseyrestdataapi.product;


import java.math.BigDecimal;
import java.sql.Timestamp;

public class Product {
  private Long id;
  private String title;
  private String description;
  private String image_url;
  private BigDecimal price;
  private Timestamp created_at;
  private Timestamp updated_at;
  // create_table "products", force: :cascade do |t|
  //   t.string "title"
  //   t.text "description"
  //   t.string "image_url"
  //   t.decimal "price", precision: 8, scale: 2
  //   t.datetime "created_at", precision: 6, null: false
  //   t.datetime "updated_at", precision: 6, null: false
  // end

  // this default constructor is needed to work with JPA
  public Product() {
    super();
  }

  public Product(String title, String description, BigDecimal price) {
    super();
    this.title = title;
    this.description = description;
    this.price = price;
  }

  public Product(
      Long id,
      String title,
      String description,
      String image_url,
      BigDecimal price,
      Timestamp created_at,
      Timestamp updated_at) {
    super();
    this.id = id;
    this.title = title;
    this.description = description;
    this.image_url = image_url;
    this.price = price;
    this.created_at = created_at;
    this.updated_at = updated_at;
  }

  public Long getId() {
    return id;
  }
  public void setId(Long id) {
    this.id = id;
  }
  public String getTitle() {
    return title;
  }
  public void setTitle(String title) {
    this.title = title;
  }
  public String getDescription() {
    return description;
  }
  public void setDescription(String description) {
    this.description = description;
  }
  public String getImage_url() {
    return image_url;
  }
  public void setImage_url(String image_url) {
    this.image_url = image_url;
  }
  public BigDecimal getPrice() {
    return price;
  }
  public void setPrice(BigDecimal price) {
    this.price = price;
  }
  public Timestamp getCreated_at() {
    return created_at;
  }
  public void setCreated_at(Timestamp created_at) {
    this.created_at = created_at;
  }
  public Timestamp getUpdated_at() {
    return updated_at;
  }
  public void setUpdated_at(Timestamp updated_at) {
    this.updated_at = updated_at;
  }
  public String toString() {
    return
      "\nId: "          + this.id +
      "\nTitle: "       + this.title +
      "\nDescription: " + this.description +
      "\nImage URL: "   + this.image_url +
      "\nPrice: "       + this.price +
      "\nCreated at: "  + this.created_at +
      "\nUpdated at: "  + this.updated_at + "\n\n";
    }
}
