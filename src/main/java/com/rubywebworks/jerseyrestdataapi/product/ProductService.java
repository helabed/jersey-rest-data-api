package com.rubywebworks.jerseyrestdataapi.product;

import java.util.ArrayList;
import java.util.List;

public class ProductService {

  private ProductRepository productRepository = ProductRepository.getInstance();
  private static ProductService instance;

  private ProductService() {
    // Constructor hidden because this is a singleton
    // use ProductService.getInstance() instead of 'new ProductService'
  }

  public static ProductService getInstance() {
    if (instance == null) {
        instance = new ProductService();
    }
    return instance;
  }

  public List<Product> getAllProducts() {
    List<Product> products = new ArrayList<>();
    productRepository.findAll().forEach(products::add);
    return products;
  }

  public void addProduct(Product product) {
    productRepository.save(product);
  }

  public Product getProduct(Long id) {
    return productRepository.findById(id);
  }

  public void updateProduct(Long id, Product product) {
    productRepository.update(id, product);
  }

  public void deleteProduct(Long id) {
    productRepository.deleteById(id);
  }
}