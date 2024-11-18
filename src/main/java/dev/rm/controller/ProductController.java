package dev.rm.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import dev.rm.model.Product;
import dev.rm.service.ProductService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ProductController {

    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    private ProductService productService;

    @GetMapping("/products")
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> products = productService.getProducts();
        if (products.isEmpty()) {
            logger.info("No products found.");
            return ResponseEntity.noContent().build();
        } else {
            logger.info("Returning {} products.", products.size());
            return ResponseEntity.ok(products);
        }
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        try {
            Product product = productService.getProductById(id);
            if (product == null) {
                logger.warn("Product with id {} not found.", id);
                return ResponseEntity.notFound().build();
            }
            logger.info("Returning product with id {}", id);
            return ResponseEntity.ok(product);
        } catch (RuntimeException e) {
            logger.error("Error fetching product with id {}: {}", id, e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/products")
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        if (product.getName() == null || product.getSku() == null) {
            logger.warn("Creation failed: name or SKU is missing.");
            return ResponseEntity.badRequest().build();
        }

        Product createdProduct = productService.createProduct(product);
        logger.info("Created product with id {}", createdProduct.getId());
        return ResponseEntity.status(201).body(createdProduct);
    }

    @PutMapping("/products/{id}")
    public ResponseEntity<Map<String, Object>> updateProduct(@PathVariable Long id, @RequestBody Product product) {
        if (product.getName() == null || product.getSku() == null) {
            logger.warn("Update failed: name or SKU is missing.");
            return ResponseEntity.badRequest().build();
        }

        try {
            Product updatedProduct = productService.updateProduct(id, product);
            if (updatedProduct == null) {
                logger.warn("Product with id {} not found for update.", id);
                return ResponseEntity.notFound().build();
            }

            logger.info("Updated product with id {}", id);

            // Create response body
            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("message", "Product updated successfully.");
            responseBody.put("product", updatedProduct);

            return ResponseEntity.ok(responseBody);
        } catch (RuntimeException e) {
            logger.error("Error updating product with id {}: {}", id, e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/products/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        try {
            productService.deleteProduct(id);
            logger.info("Deleted product with id {}", id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            logger.error("Error deleting product with id {}: {}", id, e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
}
