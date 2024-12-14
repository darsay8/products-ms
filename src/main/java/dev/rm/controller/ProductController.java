package dev.rm.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import dev.rm.factory.ProductFactory;
import dev.rm.model.Product;
import dev.rm.service.ProductService;
import dev.rm.validation.ProductValidationChain;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api")
public class ProductController {

    private final ProductService productService;
    private final ProductValidationChain validationChain;

    public ProductController(ProductService productService) {
        this.productService = productService;
        this.validationChain = new ProductValidationChain();
    }

    @GetMapping("/products")
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> products = productService.getProducts();
        if (products.isEmpty()) {
            log.info("No products found.");
            return ResponseEntity.noContent().build();
        } else {
            log.info("Returning {} products.", products.size());
            return ResponseEntity.ok(products);
        }
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        try {
            Product product = productService.getProductById(id);
            if (product == null) {
                log.warn("Product with id {} not found.", id);
                return ResponseEntity.notFound().build();
            }
            log.info("Returning product with id {}", id);
            return ResponseEntity.ok(product);
        } catch (RuntimeException e) {
            log.error("Error fetching product with id {}: {}", id, e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/products")
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        try {

            validationChain.validate(product);

            Product newProduct = ProductFactory.createProduct(
                    product.getName(),
                    product.getDescription(),
                    product.getImageUrl(),
                    product.getSku(),
                    product.getPrice(),
                    product.getStock(),
                    product.getCategory(),
                    product.getBrand());

            Product createdProduct = productService.createProduct(newProduct);

            log.info("Created product with id {}", createdProduct.getId());
            return ResponseEntity.status(201).body(createdProduct);
        } catch (RuntimeException e) {
            log.error("Error creating product: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/products/{id}")
    public ResponseEntity<Map<String, Object>> updateProduct(@PathVariable Long id, @RequestBody Product product) {
        if (product.getName() == null || product.getSku() == null) {
            log.warn("Update failed: name or SKU is missing.");
            return ResponseEntity.badRequest().build();
        }

        try {
            Product updatedProduct = productService.updateProduct(id, product);
            if (updatedProduct == null) {
                log.warn("Product with id {} not found for update.", id);
                return ResponseEntity.notFound().build();
            }

            log.info("Updated product with id {}", id);

            // Create response body
            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("message", "Product updated successfully.");
            responseBody.put("product", updatedProduct);

            return ResponseEntity.ok(responseBody);
        } catch (RuntimeException e) {
            log.error("Error updating product with id {}: {}", id, e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/products/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        try {
            productService.deleteProduct(id);
            log.info("Deleted product with id {}", id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            log.error("Error deleting product with id {}: {}", id, e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/products/by-ids")
    public ResponseEntity<List<Product>> getProductsByIds(@RequestBody List<Long> productIds) {
        try {

            List<Product> products = productService.getProductsByIds(productIds);

            if (products.isEmpty()) {
                log.info("No products found for the given IDs.");
                return ResponseEntity.notFound().build(); // No products found
            }

            log.info("Returning {} products.", products.size());
            return ResponseEntity.ok(products); // Return the list of products
        } catch (Exception e) {
            log.error("Error fetching products: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // Server error
        }
    }
}
