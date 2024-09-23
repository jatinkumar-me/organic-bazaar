package com.jatin.products.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Sort;

import com.jatin.products.model.Product;
import com.jatin.products.repository.ProductRepository;

class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllProducts() {
        List<Product> products = Arrays.asList(new Product(), new Product());
        when(productRepository.findAll()).thenReturn(products);

        List<Product> result = productService.getAllProducts();

        assertEquals(2, result.size());
        verify(productRepository, times(1)).findAll();
    }

    @Test
    void testGetProductsWithCategoryAndSearch() {
        String category = "Electronics";
        String search = "Phone";
        String sortBy = "price";
        Integer order = 1;
        List<Product> products = Arrays.asList(new Product(), new Product());
        when(productRepository.findByCategoryAndSearch(any(String.class), any(String.class), any(Sort.class)))
                .thenReturn(products);

        List<Product> result = productService.getProducts(category, search, sortBy, order);

        assertEquals(2, result.size());
        verify(productRepository, times(1)).findByCategoryAndSearch(eq(category), eq(search), any(Sort.class));
    }

    @Test
    void testGetProductsByCategory() {
        String category = "Books";
        List<Product> products = Arrays.asList(new Product(), new Product());
        when(productRepository.findByCategory(category)).thenReturn(products);

        List<Product> result = productService.getProductsByCategory(category);

        assertEquals(2, result.size());
        verify(productRepository, times(1)).findByCategory(category);
    }

    @Test
    void testGetProductById() {
        Long id = 1L;
        Product product = new Product();
        product.setId(id);
        when(productRepository.findById(id)).thenReturn(Optional.of(product));

        Product result = productService.getProductById(id);

        assertNotNull(result);
        assertEquals(id, result.getId());
        verify(productRepository, times(1)).findById(id);
    }

    @Test
    void testGetProductByIds() {
        List<Long> ids = Arrays.asList(1L, 2L);
        List<Product> products = Arrays.asList(new Product(), new Product());
        when(productRepository.findByIdIn(ids)).thenReturn(products);

        List<Product> result = productService.getProductByIds(ids);

        assertEquals(2, result.size());
        verify(productRepository, times(1)).findByIdIn(ids);
    }

    @Test
    void testSaveProduct() {
        Product product = new Product();
        when(productRepository.save(product)).thenReturn(product);

        Product result = productService.saveProduct(product);

        assertNotNull(result);
        verify(productRepository, times(1)).save(product);
    }

    @Test
    void testDeleteProduct() {
        Long id = 1L;

        productService.deleteProduct(id);

        verify(productRepository, times(1)).deleteById(id);
    }

    @Test
    void testAddManyProducts() {
        List<Product> products = Arrays.asList(new Product(), new Product());
        when(productRepository.saveAll(products)).thenReturn(products);

        List<Product> result = productService.addMany(products);

        assertEquals(2, result.size());
        verify(productRepository, times(1)).saveAll(products);
    }

    @Test
    void testGetUniqueCategories() {
        List<String> categories = Arrays.asList("Electronics", "Books");
        when(productRepository.findUniqueCategories()).thenReturn(categories);

        List<String> result = productService.getUniqueCategories();

        assertEquals(2, result.size());
        verify(productRepository, times(1)).findUniqueCategories();
    }
}
