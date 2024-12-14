package dev.rm.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import dev.rm.model.Category;
import dev.rm.model.Product;
import dev.rm.repository.ProductRepository;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    private Product product;
    private Category catSmartphones;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        catSmartphones = Category.builder().name("Smartphones").build();
        product = Product.builder()
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
    }

    @Test
    public void testGetProducts() {
        Category catLaptops = Category.builder().name("Laptops").build();
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
        when(productRepository.findAll()).thenReturn(Arrays.asList(product, product2));

        List<Product> products = productService.getProducts();

        assertNotNull(products);
        assertEquals(2, products.size());
        assertEquals("iPhone 15 Pro", products.get(0).getName());
    }

    @Test
    public void testGetProductById_ProductExists() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        Product result = productService.getProductById(1L);

        assertNotNull(result);
        assertEquals(product.getName(), result.getName());
    }

    @Test
    public void testGetProductById_ProductNotFound() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        Product result = productService.getProductById(1L);

        assertNull(result);
    }

    @Test
    public void testCreateProduct() {
        when(productRepository.save(product)).thenReturn(product);

        Product createdProduct = productService.createProduct(product);

        assertNotNull(createdProduct);
        assertEquals("iPhone 15 Pro", createdProduct.getName());
    }

    @Test
    public void testUpdateProduct_ProductExists() {
        Product updatedProduct = Product.builder()
                .name("iPhone 15 Pro Max")
                .build();

        when(productRepository.existsById(1L)).thenReturn(true);
        when(productRepository.save(updatedProduct)).thenReturn(updatedProduct);

        Product result = productService.updateProduct(1L, updatedProduct);

        assertNotNull(result);
        assertEquals("iPhone 15 Pro Max", result.getName());
    }

    @Test
    public void testUpdateProduct_ProductNotFound() {
        Product updatedProduct = Product.builder()
                .name("iPhone 15 Pro Max")
                .build();

        when(productRepository.existsById(1L)).thenReturn(false);

        Product result = productService.updateProduct(1L, updatedProduct);

        assertNull(result);
    }

    @Test
    public void testDeleteProduct() {
        productService.deleteProduct(1L);

        verify(productRepository, times(1)).deleteById(1L);
    }
}
