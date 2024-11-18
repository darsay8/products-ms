package dev.rm.validation;

import java.util.Arrays;
import java.util.List;

import dev.rm.model.Product;

public class ProductValidationChain {

    private final List<ValidationHandler> validators;

    public ProductValidationChain() {
        validators = Arrays.asList(
                new ProductNameValidator(),
                new ProductSkuValidator(),
                new ProductPriceValidator(),
                new ProductStockValidator());
    }

    public void validate(Product product) {
        for (ValidationHandler validator : validators) {
            validator.validate(product);
        }
    }
}
