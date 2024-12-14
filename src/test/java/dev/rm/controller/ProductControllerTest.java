package dev.rm.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.test.web.servlet.MockMvc;

import dev.rm.model.Category;
import dev.rm.model.Product;
import dev.rm.service.ProductService;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @InjectMocks
    private ProductController productController;

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
    public void testGetAllProducts() throws Exception {
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
        when(productService.getProducts()).thenReturn(Arrays.asList(product, product2));

        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("iPhone 15 Pro"))
                .andExpect(jsonPath("$[1].name").value("MacBook Pro M3 Pro"));
    }

    @Test
    public void testGetAllProducts_NoContent() throws Exception {
        when(productService.getProducts()).thenReturn(Arrays.asList());

        mockMvc.perform(get("/api/products"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testGetProductById_ProductFound() throws Exception {
        when(productService.getProductById(1L)).thenReturn(product);

        mockMvc.perform(get("/api/products/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("iPhone 15 Pro"))
                .andExpect(jsonPath("$.sku").value("SKU123"));
    }

    @Test
    public void testGetProductById_ProductNotFound() throws Exception {
        when(productService.getProductById(1L)).thenReturn(null);

        mockMvc.perform(get("/api/products/{id}", 1L))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testCreateProduct() throws Exception {
        when(productService.createProduct(any(Product.class))).thenReturn(product);

        mockMvc.perform(post("/api/products")
                .contentType("application/json")
                .content(
                        "{\n" +
                                "    \"name\": \"iPhone 15 Pro\",\n" +
                                "    \"description\": \"Latest Apple smartphone with advanced features.\",\n" +
                                "    \"imageUrl\": \"https://cdn11.bigcommerce.com/s-xt5en0q8kf/images/stencil/1280x1280/products/12264/30177/iphone15p__00220.1700249228.jpg?c=2\",\n"
                                +
                                "    \"price\": 999.99,\n" +
                                "    \"category\": {\n" +
                                "        \"id\": 1,\n" +
                                "        \"name\": \"Smartphones\"\n" +
                                "    },\n" +
                                "    \"stock\": 100,\n" +
                                "    \"sku\": \"SKU123\",\n" +
                                "    \"brand\": \"Apple\",\n" +
                                "    \"createdAt\": \"2024-12-13T16:40:32.25337\"\n" +
                                "}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("iPhone 15 Pro"))
                .andExpect(jsonPath("$.sku").value("SKU123"))
                .andExpect(jsonPath("$.price").value(999.99)) // Check
                .andExpect(jsonPath("$.category.name").value("Smartphones"));
    }

    @Test
    public void testCreateProduct_BadRequest() throws Exception {
        when(productService.createProduct(any(Product.class))).thenThrow(new RuntimeException("Invalid product data"));

        mockMvc.perform(post("/api/products")
                .contentType("application/json")
                .content("{\"name\":\"\", \"sku\":\"\", \"price\":0.0}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testUpdateProduct_ProductFound() throws Exception {
        Product updatedProduct = Product.builder()
                .name("iPhone 15 Pro Max")
                .build();
        when(productService.updateProduct(eq(1L), any(Product.class))).thenReturn(updatedProduct);

        mockMvc.perform(put("/api/products/{id}", 1L)
                .contentType("application/json")
                .content(
                        "{\"name\":\"iPhone 15 Pro Max\", \"description\":\"Updated description\", \"sku\":\"sku123\", \"price\":120.0, \"stock\":40}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Product updated successfully."))
                .andExpect(jsonPath("$.product.name").value("iPhone 15 Pro Max"));
    }

    @Test
    public void testUpdateProduct_ProductNotFound() throws Exception {
        when(productService.updateProduct(eq(1L), any(Product.class))).thenReturn(null);

        mockMvc.perform(put("/api/products/{id}", 1L)
                .contentType("application/json")
                .content("{\"name\":\"Updated Product\", \"sku\":\"sku123\"}"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testDeleteProduct() throws Exception {
        doNothing().when(productService).deleteProduct(1L);

        mockMvc.perform(delete("/api/products/{id}", 1L))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testDeleteProduct_NotFound() throws Exception {
        doThrow(new RuntimeException("Product not found")).when(productService).deleteProduct(1L);

        mockMvc.perform(delete("/api/products/{id}", 1L))
                .andExpect(status().isNotFound());
    }
}
