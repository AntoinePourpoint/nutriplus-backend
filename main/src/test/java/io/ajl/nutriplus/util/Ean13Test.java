package io.ajl.nutriplus.util;

import io.ajl.nutriplus.exception.InvalidBarcodeException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class Ean13Test {

    @ValueSource(strings = {
            "123456789012",
            "1234567890123",
            "12345678901234"
    })
    @ParameterizedTest
    void of_shouldThrowInvalidBarcodeException_whenBarcodeIsInvalid(String barcode) {
        assertThrows(InvalidBarcodeException.class, () -> Ean13.of(barcode));
    }

    @Test
    void of_shouldBuildEan13_whenBarcodeIsValid() {
        Ean13 ean13 = Ean13.of("3017620421006");

        assertEquals("3017620421006", ean13.getCode());
    }
}