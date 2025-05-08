package io.ajl.nutriplus.exception;

import io.ajl.nutriplus.util.Ean13;

public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException(Ean13 ean) {
        super("Product not found: " + ean.getCode());
    }
}
