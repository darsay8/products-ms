package dev.rm.validation;

import dev.rm.model.Product;

public class ProductNameValidator implements ValidationHandler {
    @Override
    public void validate(Product product) throws RuntimeException {
        if (product.getName() == null || product.getName().isEmpty()) {
            throw new RuntimeException("Product name is required.");
        }
    }
}