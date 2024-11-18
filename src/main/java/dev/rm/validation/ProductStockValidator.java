package dev.rm.validation;

import dev.rm.model.Product;

public class ProductStockValidator implements ValidationHandler {
    @Override
    public void validate(Product product) throws RuntimeException {
        if (product.getStock() == null || product.getStock() <= 0) {
            throw new RuntimeException("Product stock must be greater than zero.");
        }
    }
}