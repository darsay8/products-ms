package dev.rm.factory;

import java.math.BigDecimal;

import dev.rm.model.Category;
import dev.rm.model.Product;

public class ProductFactory {

    public static Product createProduct(String name, String sku, BigDecimal price, Integer stock, Category category,
            String brand) {
        return Product.builder()
                .name(name)
                .sku(sku)
                .price(price)
                .stock(stock)
                .category(category)
                .brand(brand)
                .build();
    }
}