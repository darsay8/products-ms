package dev.rm.init;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import dev.rm.model.Category;
import dev.rm.model.Product;
import dev.rm.repository.CategoryRepository;
import dev.rm.repository.ProductRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductRepository productRepository;

    @Override
    public void run(String... args) {
        initializeCategories();
        initializeProducts();
    }

    private void initializeCategories() {
        if (categoryRepository.count() == 0) {
            Category smartphones = new Category(null, "Smartphones");
            Category laptops = new Category(null, "Laptops");
            Category tvs = new Category(null, "TVs");
            Category cameras = new Category(null, "Cameras");
            Category wearables = new Category(null, "Wearable");
            Category audio = new Category(null, "Audio");

            categoryRepository.saveAll(Arrays.asList(smartphones, laptops, tvs, cameras, wearables, audio));
            System.out.println("Categories initialized.");
        }
    }

    private void initializeProducts() {
        if (productRepository.count() == 0) {
            Category smartphones = categoryRepository.findById(1L).orElse(null);
            Category laptops = categoryRepository.findById(2L).orElse(null);

            Product product1 = new Product(null, "iPhone 13", "Latest Apple smartphone",
                    BigDecimal.valueOf(999.99), smartphones, 100, "SKU123", LocalDate.now(), "Apple");
            Product product2 = new Product(null, "MacBook Pro", "High-performance laptop",
                    BigDecimal.valueOf(1999.99), laptops, 50, "SKU456", LocalDate.now(), "Apple");

            productRepository.saveAll(Arrays.asList(product1, product2));
            System.out.println("Products initialized.");
        }
    }
}
