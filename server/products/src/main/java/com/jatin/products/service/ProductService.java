package com.jatin.products.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jatin.products.model.Product;
import com.jatin.products.repository.ProductRepository;

/**
 * ProductService
 */
@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public List<Product> getProductsByCategory(String category) {
        return productRepository.findByCategory(category);
    }
    public Product getProductById(Long id) {
        return productRepository.findById(id).orElse(null);
    }

    public List<Product> getProductByIds(List<Long> ids) {
        return productRepository.findByIdIn(ids);
    }

    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    public List<Product> addMany(List<Product> products) {
        List<Product> savedProducts = productRepository.saveAll(products);
        return savedProducts;
    }
}
