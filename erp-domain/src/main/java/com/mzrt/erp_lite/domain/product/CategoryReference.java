package com.mzrt.erp_lite.domain.product;

/**
 * Reference to Catalog in MongoDB. Example: cat-electronics
 */
public record CategoryReference(String categoryId) {

    public CategoryReference {
        if (categoryId == null || categoryId.isBlank()) {
            throw new IllegalArgumentException("CategoryReference cannot be null or blank");
        }
    }

    public static CategoryReference of(String categoryId) {
        return new CategoryReference(categoryId);
    }
}
