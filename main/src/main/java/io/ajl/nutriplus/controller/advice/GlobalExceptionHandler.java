package io.ajl.nutriplus.controller.advice;

import io.ajl.nutriplus.exception.InvalidBarcodeException;
import io.ajl.nutriplus.exception.ProductNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<ApiError> handleInvalidBarcodeException(InvalidBarcodeException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiError("INVALID_BARCODE", e.getMessage()));
    }

    @ExceptionHandler
    public ResponseEntity<ApiError> handleProductNotFoundException(ProductNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiError("PRODUCT_NOT_FOUND", e.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleAllOthers(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiError("INTERNAL_ERROR", "An unexpected error occurred"));
    }
}
