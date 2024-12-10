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
        Category catSmartphones = Category.builder().name("Smartphones").build();
        Category catLaptops = Category.builder().name("Laptops").build();
        Category catTablets = Category.builder().name("Tablets").build();
        Category catWearables = Category.builder().name("Wearables").build();
        Category catAudio = Category.builder().name("Audio").build();

        categoryRepository
                .saveAll(Arrays.asList(catSmartphones, catLaptops, catTablets, catWearables, catWearables, catAudio));

        Product product1 = Product.builder()
                .name("iPhone 15 Pro")
                .description("Latest Apple smartphone with advanced features.")
                .imageUrl(
                        "https://cdn11.bigcommerce.com/s-xt5en0q8kf/images/stencil/1280x1280/products/12264/30177/iphone15p__00220.1700249228.jpg?c=2")
                .price(BigDecimal.valueOf(999.99))
                .category(catSmartphones)
                .stock(100)
                .sku("SKU123")
                .brand("Apple")
                .build();

        Product product2 = Product.builder()
                .name("MacBook Pro M3 Pro")
                .description("High-performance laptop with M1 chip.")
                .imageUrl(
                        "https://www.bhphotovideo.com/images/images500x500/apple_mrx33ll_a_14_macbook_pro_with_1698709615_1793624.jpg")
                .price(BigDecimal.valueOf(1999.99))
                .category(catLaptops)
                .stock(50)
                .sku("SKU456")
                .brand("Apple")
                .build();

        Product product3 = Product.builder()
                .name("iPad Pro M4")
                .description("Powerful tablet with Apple M4 chip and Liquid Retina display.")
                .imageUrl(
                        "https://www.macprices.net/blog/wp-content/uploads/2024/05/Apple-iPad-Pro-Ultra-Retina-XDR-display-2-up-240507.jpg")
                .price(BigDecimal.valueOf(1099.99))
                .category(catTablets)
                .stock(80)
                .sku("SKU789")
                .brand("Apple")
                .build();

        Product product4 = Product.builder()
                .name("Apple Watch Series 10")
                .description("Smartwatch with always-on Retina display and health tracking.")
                .imageUrl(
                        "https://www.apple.com/newsroom/images/2024/09/introducing-apple-watch-series-10/article/Apple-Watch-Series-10-watch-face-Flux-240909_inline.jpg.large_2x.jpg")
                .price(BigDecimal.valueOf(399.99))
                .category(catWearables)
                .stock(150)
                .sku("SKU1011")
                .brand("Apple")
                .build();

        Product product5 = Product.builder()
                .name("AirPods Pro")
                .description("Noise-cancelling wireless earbuds with spatial audio.")
                .imageUrl("https://macstore.id/wp-content/uploads/2022/10/MQD83_AV2.jpeg")
                .price(BigDecimal.valueOf(249.99))
                .category(catAudio)
                .stock(200)
                .sku("SKU1122")
                .brand("Apple")
                .build();

        Product product6 = Product.builder()
                .name("HomePod")
                .description("Immersive, high‑fidelity audio.")
                .imageUrl("https://cdn.iphoneincanada.ca/wp-content/uploads/2023/01/Apple-HomePod-2-up-230118.jpg")
                .price(BigDecimal.valueOf(299.99))
                .category(catAudio)
                .stock(120)
                .sku("SKU1103")
                .brand("Apple")
                .build();

        productRepository.saveAll(Arrays.asList(product1, product2, product3, product4, product5, product6));

    }

}
