package dev.rm.validation;

import dev.rm.model.Product;

public class ProductSkuValidator implements ValidationHandler {
    @Override
    public void validate(Product product) throws RuntimeException {
        if (product.getSku() == null || product.getSku().isEmpty()) {
            throw new RuntimeException("Product SKU is required.");
        }
    }
}