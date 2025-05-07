package io.ajl.nutriplus.util;

import io.ajl.nutriplus.exception.InvalidBarcodeException;

import java.util.regex.Pattern;

public class Ean13 {

    private static final Pattern NUMERIC_13 = Pattern.compile("\\d{13}");

    private final String code;

    private Ean13(String code) {
        if (!isValid(code)) {
            throw new InvalidBarcodeException(code + " is not a valid EAN-13");
        }
        this.code = code;
    }

    public static Ean13 of(String code) {
        return new Ean13(code);
    }

    public String getCode() { return code; }

    private static boolean isValid(String code) {
        if (!NUMERIC_13.matcher(code).matches()) {
            return false;
        }

        int sumOdd  = 0;
        int sumEven = 0;
        for (int i = 0; i < 12; i++) {
            int digit = code.charAt(i) - '0';
            if ((i % 2) == 0) {
                sumOdd += digit;
            } else {
                sumEven += digit * 3;
            }
        }

        int total = sumOdd + sumEven;
        int mod   = total % 10;
        int check = (mod == 0) ? 0 : (10 - mod);

        return check == (code.charAt(12) - '0');
    }
}
