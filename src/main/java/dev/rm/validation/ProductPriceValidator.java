package dev.rm.validation;

import dev.rm.model.Product;
import java.math.BigDecimal;

public class ProductPriceValidator implements ValidationHandler {
    @Override
    public void validate(Product product) throws RuntimeException {
        if (product.getPrice() == null || product.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Product price must be greater than zero.");
        }
    }
}