package dev.rm.init;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import dev.rm.model.Category;
import dev.rm.model.Product;
import dev.rm.repository.CategoryRepository;
import dev.rm.repository.ProductRepository;

import java.math.BigDecimal;
import java.util.Arrays;

@Component
public class DataInitializer implements CommandLineRunner {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    public DataInitializer(CategoryRepository categoryRepository, ProductRepository productRepository) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
    }

    @Override
    @Transactional
    public void run(String... args) {
        Category cat1 = Category.builder().name("Smartphones").build();
        Category cat2 = Category.builder().name("Laptops").build();
        Category cat3 = Category.builder().name("TVs").build();
        Category cat4 = Category.builder().name("Cameras").build();
        Category cat5 = Category.builder().name("Wearable").build();
        Category cat6 = Category.builder().name("Audio").build();

        categoryRepository.saveAll(Arrays.asList(cat1, cat2, cat3, cat4, cat5, cat6));

        Product product1 = Product.builder()
                .name("iPhone 13")
                .description("Latest Apple smartphone")
                .price(BigDecimal.valueOf(999.99))
                .category(cat1)
                .stock(100)
                .sku("SKU123")
                .brand("Apple")
                .build();

        Product product2 = Product.builder()
                .name("MacBook Pro")
                .description("High-performance laptop")
                .price(BigDecimal.valueOf(1999.99))
                .category(cat2)
                .stock(50)
                .sku("SKU456")
                .brand("Apple")
                .build();

        productRepository.saveAll(Arrays.asList(product1, product2));

    }

}
