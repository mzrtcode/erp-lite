package com.mzrt.erp_lite.domain.shared;

import java.util.regex.Pattern;

/**
 * Email address for notifications
 * Pattern: RFC-5322-like simplified email pattern
 */
public record Email(String value) {

    private static final Pattern PATTERN = Pattern.compile(
        "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
    );

    public Email {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Email cannot be null or blank");
        }
        if (!PATTERN.matcher(value).matches()) {
            throw new IllegalArgumentException(
                "Email must be a valid email address: " + value
            );
        }
    }

    public static Email of(String value) {
        return new Email(value);
    }
}
