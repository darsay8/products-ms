package dev.rm.validation;

import dev.rm.model.Product;

public interface ValidationHandler {
    void validate(Product product) throws RuntimeException;
}
