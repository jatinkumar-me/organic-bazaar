package com.jatin.products.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jatin.products.model.Product;
import com.jatin.products.service.ProductService;

/**
 * ProductController
 */
@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping()
    public List<Product> getAllProducts(
        @RequestParam(value = "category", required = false) String category,
        @RequestParam(value = "search", required = false) String search,
        @RequestParam(value = "sort", required = false) String sortBy,
        @RequestParam(value = "order", required = false, defaultValue = "1") Integer order
    ) {
        return productService.getProducts(category, search, sortBy, order);
    }

    @GetMapping("/all")
    public List<Product> getAllProducts(
    ) {
        return productService.getAllProducts();
    }

    @GetMapping("/{id}")
    public Product getProductById(@PathVariable Long id) {
        return productService.getProductById(id);
    }

    @GetMapping("/ids")
    public List<Product> getProductByIds(@RequestParam List<Long> ids) {
        return productService.getProductByIds(ids);
    }

    @PostMapping()
    public Product createProduct(@RequestBody Product product) {
        return productService.saveProduct(product);
    }

    @PostMapping("/add-many")
    public List<Product> addMany(@RequestBody List<Product> products) {
        return productService.addMany(products);
    }

    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
    }
}
