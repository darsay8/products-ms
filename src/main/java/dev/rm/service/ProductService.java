package dev.rm.service;

import java.util.List;
import dev.rm.model.Product;

public interface ProductService {
    List<Product> getProducts();

    Product getProductById(Long id);

    Product createProduct(Product product);

    Product updateProduct(Long id, Product product);

    void deleteProduct(Long id);
}
