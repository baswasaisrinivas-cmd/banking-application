package com.banking.banking_application.util;

import org.springframework.util.StringUtils;

public final class ValidationUtil {
    private ValidationUtil() {
    }

    public static void requireNonEmpty(String value, String name) {
        if (!StringUtils.hasText(value)) {
            throw new IllegalArgumentException(name + " must not be empty");
        }
    }
}